package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converters.Converter;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * An abstract class represents test for parent facade.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractParentFacadeTest<T extends Movable, U extends Movable> {

    /**
     * Result for invalid data
     */
    private static final Result<Void> INVALID_DATA_RESULT = Result.error("DATA_INVALID", "Data must be valid.");

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<U> catalogService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link CatalogValidator}
     */
    @Mock
    private CatalogValidator<T> catalogValidator;

    /**
     * Instance of {@link AbstractParentCatalogFacade}
     */
    private AbstractParentCatalogFacade<T, U> parentCatalogFacade;

    /**
     * Initializes facade for catalog.
     */
    @Before
    public void setUp() {
        parentCatalogFacade = getParentCatalogFacade();
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#newData()}.
     */
    @Test
    public void newData() {
        final Result<Void> result = parentCatalogFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).newData();
        verifyNoMoreInteractions(catalogService);
        verifyZeroInteractions(converter, catalogValidator);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final List<U> domainList = CollectionUtils.newList(newDomain(1), newDomain(2));
        final List<T> entityList = CollectionUtils.newList(newEntity(1), newEntity(2));

        when(catalogService.getAll()).thenReturn(domainList);
        when(converter.convertCollection(anyListOf(getDomainClass()), eq(getEntityClass()))).thenReturn(entityList);

        final Result<List<T>> result = parentCatalogFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(entityList));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).getAll();
        verify(converter).convertCollection(domainList, getEntityClass());
        verifyNoMoreInteractions(catalogService, converter);
        verifyZeroInteractions(catalogValidator);
    }


    /**
     * Test method for {@link AbstractParentCatalogFacade#get(Integer)} with existing data.
     */
    @Test
    public void get_ExistingData() {
        final U domain = newDomain(1);
        final T entity = newEntity(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(converter.convert(any(getDomainClass()), eq(getEntityClass()))).thenReturn(entity);

        final Result<T> result = parentCatalogFacade.get(1);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(entity));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).get(1);
        verify(converter).convert(domain, getEntityClass());
        verifyNoMoreInteractions(catalogService, converter);
        verifyZeroInteractions(catalogValidator);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#get(Integer)} with not existing data.
     */
    @Test
    public void get_NotExistingData() {
        when(catalogService.get(any(Integer.class))).thenReturn(null);
        when(converter.convert(any(getDomainClass()), eq(getEntityClass()))).thenReturn(null);

        final Result<T> result = parentCatalogFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, getEntityClass());
        verifyNoMoreInteractions(catalogService, converter);
        verifyZeroInteractions(catalogValidator);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#get(Integer)} with null data.
     */
    @Test
    public void get_NullData() {
        final Result<T> result = parentCatalogFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        verifyZeroInteractions(catalogService, converter, catalogValidator);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#add(Movable)}.
     */
    @Test
    public void add() {
        final T entity = newEntity(null);
        final U domain = newDomain(null);

        when(converter.convert(any(getEntityClass()), eq(getDomainClass()))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.add(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).add(domain);
        verify(converter).convert(entity, getDomainClass());
        verify(catalogValidator).validate(entity, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(catalogService, converter, catalogValidator);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#add(Movable)} with invalid data.
     */
    @Test
    public void add_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.add(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(catalogValidator).validate(entity, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#update(Movable)}.
     */
    @Test
    public void update() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(converter.convert(any(getEntityClass()), eq(getDomainClass()))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.update(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).update(domain);
        verify(converter).convert(entity, getDomainClass());
        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(catalogService, converter, catalogValidator);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#update(Movable)} with invalid data.
     */
    @Test
    public void update_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.update(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#remove(Movable)}.
     */
    @Test
    public void remove() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.remove(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).get(1);
        verify(catalogService).remove(domain);
        verify(catalogValidator).validate(entity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogService, catalogValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#remove(Movable)} with invalid data.
     */
    @Test
    public void remove_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.remove(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(catalogValidator).validate(entity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#duplicate(Movable)}.
     */
    @Test
    public void duplicate() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.duplicate(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).get(1);
        verify(catalogService).duplicate(domain);
        verify(catalogValidator).validate(entity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogService, catalogValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#duplicate(Movable)} with invalid data.
     */
    @Test
    public void duplicate_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.duplicate(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(catalogValidator).validate(entity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#moveUp(Movable)}.
     */
    @Test
    public void moveUp() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.moveUp(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).get(1);
        verify(catalogService).moveUp(domain);
        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(catalogService, catalogValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#moveUp(Movable)} with invalid data.
     */
    @Test
    public void moveUp_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.moveUp(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#moveDown(Movable)}.
     */
    @Test
    public void moveDown() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.moveDown(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).get(1);
        verify(catalogService).moveDown(domain);
        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(catalogService, catalogValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#moveDown(Movable)} with invalid data.
     */
    @Test
    public void moveDown_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.moveDown(entity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link AbstractParentCatalogFacade#updatePositions()}.
     */
    @Test
    public void updatePositions() {
        final Result<Void> result = parentCatalogFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).updatePositions();
        verifyNoMoreInteractions(catalogService);
        verifyZeroInteractions(converter, catalogValidator);
    }

    /**
     * Returns service for catalog.
     *
     * @return service for catalog
     */
    public CatalogService<U> getCatalogService() {
        return catalogService;
    }

    /**
     * Returns converter.
     *
     * @return converter
     */
    public Converter getConverter() {
        return converter;
    }

    /**
     * Returns validator for catalog.
     *
     * @return validator for catalog
     */
    public CatalogValidator<T> getCatalogValidator() {
        return catalogValidator;
    }

    /**
     * Returns facade for catalog.
     *
     * @return facade for catalog
     */
    protected abstract AbstractParentCatalogFacade<T, U> getParentCatalogFacade();

    /**
     * Returns entity.
     *
     * @param id ID
     * @return entity
     */
    protected abstract T newEntity(Integer id);

    /**
     * Returns domain.
     *
     * @param id ID
     * @return domain
     */
    protected abstract U newDomain(Integer id);

    /**
     * Returns entity class.
     *
     * @return entity class.
     */
    protected abstract Class<T> getEntityClass();

    /**
     * Returns domain class.
     *
     * @return domain class.
     */
    protected abstract Class<U> getDomainClass();

}
