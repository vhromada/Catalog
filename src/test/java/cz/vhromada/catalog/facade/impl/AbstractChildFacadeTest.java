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
import cz.vhromada.catalog.facade.CatalogChildFacade;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * An abstract class represents test for child facade.
 *
 * @param <S> type of child entity data
 * @param <T> type of child domain data
 * @param <U> type of parent entity data
 * @param <V> type of parent domain data
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractChildFacadeTest<S extends Movable, T extends Movable, U extends Movable, V extends Movable> {

    /**
     * Result for invalid data
     */
    private static final Result<Void> INVALID_DATA_RESULT = Result.error("DATA_INVALID", "Data must be valid.");

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<V> catalogService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link CatalogValidator}
     */
    @Mock
    private CatalogValidator<U> parentValidator;

    /**
     * Instance of {@link CatalogValidator}
     */
    @Mock
    private CatalogValidator<S> childValidator;

    /**
     * Instance of {@link CatalogChildFacade}
     */
    private CatalogChildFacade<S, U> childCatalogFacade;

    /**
     * Initializes facade for catalog.
     */
    @Before
    public void setUp() {
        childCatalogFacade = getCatalogChildFacade();
    }

    /**
     * Test method for {@link CatalogChildFacade#get(Integer)} with existing data.
     */
    @Test
    public void get_ExistingData() {
        final S childEntity = newChildEntity(1);

        when(catalogService.getAll()).thenReturn(CollectionUtils.newList(newParentDomain(1)));
        when(converter.convert(any(getChildDomainClass()), eq(getChildEntityClass()))).thenReturn(childEntity);

        final Result<S> result = childCatalogFacade.get(1);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(childEntity));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).getAll();
        verify(converter).convert(newChildDomain(1), getChildEntityClass());
        verifyNoMoreInteractions(catalogService, converter);
        verifyZeroInteractions(parentValidator, childValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#get(Integer)} with not existing data.
     */
    @Test
    public void get_NotExistingData() {
        when(catalogService.getAll()).thenReturn(CollectionUtils.newList(newParentDomain(1)));
        when(converter.convert(any(getChildDomainClass()), eq(getChildEntityClass()))).thenReturn(null);

        final Result<S> result = childCatalogFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).getAll();
        verify(converter).convert(null, getChildEntityClass());
        verifyNoMoreInteractions(catalogService, converter);
        verifyZeroInteractions(parentValidator, childValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#get(Integer)} with null data.
     */
    @Test
    public void get_NullData() {
        final Result<S> result = childCatalogFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        verifyZeroInteractions(catalogService, converter, parentValidator, childValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#add(Movable, Movable)}.
     */
    @Test
    public void add() {
        final U parentEntity = newParentEntity(1);
        final S childEntity = newChildEntity(null);
        final T childDomain = newChildDomain(null);
        final ArgumentCaptor<V> argumentCaptor = ArgumentCaptor.forClass(getParentDomainClass());

        if (isFirstChild()) {
            when(catalogService.get(any(Integer.class))).thenReturn(newParentDomain(1));
        } else {
            when(catalogService.getAll()).thenReturn(CollectionUtils.newList(newParentDomain(1)));
        }
        when(converter.convert(any(getChildEntityClass()), eq(getChildDomainClass()))).thenReturn(childDomain);
        when(parentValidator.validate(any(getParentEntityClass()), anyVararg())).thenReturn(new Result<>());
        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = childCatalogFacade.add(parentEntity, childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        if (isFirstChild()) {
            verify(catalogService).get(parentEntity.getId());
        } else {
            verify(catalogService).getAll();
        }
        verify(catalogService).update(argumentCaptor.capture());
        verify(parentValidator).validate(parentEntity, ValidationType.EXISTS);
        verify(childValidator).validate(childEntity, ValidationType.NEW, ValidationType.DEEP);
        verify(converter).convert(childEntity, getChildDomainClass());
        verifyNoMoreInteractions(catalogService, converter, parentValidator, childValidator);

        assertParentDeepEquals(newParentDomainWithChildren(1, CollectionUtils.newList(newChildDomain(1), childDomain)), argumentCaptor.getValue());
    }

    /**
     * Test method for {@link CatalogChildFacade#add(Movable, Movable)} with invalid data.
     */
    @Test
    public void add_InvalidData() {
        final U parentEntity = newParentEntity(Integer.MAX_VALUE);
        final S childEntity = newChildEntity(null);
        final Result<Void> invalidParentResult = Result.error("PARENT_INVALID", "Parent must be valid.");
        final Result<Void> invalidChildResult = Result.error("CHILD_INVALID", "Child must be valid.");

        when(parentValidator.validate(any(getParentEntityClass()), anyVararg())).thenReturn(invalidParentResult);
        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(invalidChildResult);

        final Result<Void> result = childCatalogFacade.add(parentEntity, childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(2));
        assertThat(result.getEvents().get(0), is(invalidParentResult.getEvents().get(0)));
        assertThat(result.getEvents().get(1), is(invalidChildResult.getEvents().get(0)));

        verify(parentValidator).validate(parentEntity, ValidationType.EXISTS);
        verify(childValidator).validate(childEntity, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(parentValidator, childValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link CatalogChildFacade#update(Movable)}.
     */
    @Test
    public void update() {
        final S childEntity = newChildEntity(1);
        final T childDomain = newChildDomain(1);
        final V parentDomain = newParentDomain(1);
        final ArgumentCaptor<V> argumentCaptor = ArgumentCaptor.forClass(getParentDomainClass());

        when(catalogService.getAll()).thenReturn(CollectionUtils.newList(parentDomain));
        when(converter.convert(any(getChildEntityClass()), eq(getChildDomainClass()))).thenReturn(childDomain);
        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = childCatalogFacade.update(childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).getAll();
        verify(catalogService).update(argumentCaptor.capture());
        verify(converter).convert(childEntity, getChildDomainClass());
        verify(childValidator).validate(childEntity, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(catalogService, converter, childValidator);
        verifyZeroInteractions(parentValidator);

        assertParentDeepEquals(parentDomain, argumentCaptor.getValue());
    }

    /**
     * Test method for {@link CatalogChildFacade#update(Movable)} with invalid data.
     */
    @Test
    public void update_InvalidData() {
        final S childEntity = newChildEntity(Integer.MAX_VALUE);

        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = childCatalogFacade.update(childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(childValidator).validate(childEntity, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(childValidator);
        verifyZeroInteractions(catalogService, converter, parentValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#remove(Movable)}.
     */
    @Test
    public void remove() {
        final S childEntity = newChildEntity(1);
        final V parentDomain = newParentDomain(1);
        final ArgumentCaptor<V> argumentCaptor = ArgumentCaptor.forClass(getParentDomainClass());

        when(catalogService.getAll()).thenReturn(CollectionUtils.newList(parentDomain));
        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = childCatalogFacade.remove(childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(catalogService).getAll();
        verify(catalogService).update(argumentCaptor.capture());
        verify(childValidator).validate(childEntity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogService, childValidator);
        verifyZeroInteractions(converter, parentValidator);

        assertParentDeepEquals(parentDomain, argumentCaptor.getValue());
    }

    /**
     * Test method for {@link CatalogChildFacade#remove(Movable)} with invalid data.
     */
    @Test
    public void remove_InvalidData() {
        final S childEntity = newChildEntity(Integer.MAX_VALUE);

        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = childCatalogFacade.remove(childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(childValidator).validate(childEntity, ValidationType.EXISTS);
        verifyNoMoreInteractions(childValidator);
        verifyZeroInteractions(catalogService, converter, parentValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#duplicate(Movable)}.
     */
    @Test
    public void duplicate() {
        final S childEntity = newChildEntity(1);
        final T childDomain = newChildDomain(null);
        childDomain.setPosition(0);
        final ArgumentCaptor<V> argumentCaptor = ArgumentCaptor.forClass(getParentDomainClass());

        when(catalogService.getAll()).thenReturn(CollectionUtils.newList(newParentDomain(1)));
        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(new Result<>());

        childCatalogFacade.duplicate(childEntity);

        verify(catalogService).getAll();
        verify(catalogService).update(argumentCaptor.capture());
        verify(childValidator).validate(childEntity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogService, childValidator);
        verifyZeroInteractions(converter, parentValidator);

        assertParentDeepEquals(newParentDomainWithChildren(1, CollectionUtils.newList(newChildDomain(1), childDomain)), argumentCaptor.getValue());
    }

    /**
     * Test method for {@link CatalogChildFacade#duplicate(Movable)} with invalid data.
     */
    @Test
    public void duplicate_InvalidData() {
        final S childEntity = newChildEntity(Integer.MAX_VALUE);

        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = childCatalogFacade.duplicate(childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(childValidator).validate(childEntity, ValidationType.EXISTS);
        verifyNoMoreInteractions(childValidator);
        verifyZeroInteractions(catalogService, converter, parentValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#moveUp(Movable)}.
     */
    @Test
    public void moveUp() {
        final S childEntity = newChildEntity(2);
        final T childDomain1 = newChildDomain(1);
        childDomain1.setPosition(1);
        final T childDomain2 = newChildDomain(2);
        childDomain2.setPosition(0);
        final ArgumentCaptor<V> argumentCaptor = ArgumentCaptor.forClass(getParentDomainClass());

        when(catalogService.getAll()).thenReturn(CollectionUtils.newList(newParentDomainWithChildren(1, CollectionUtils.newList(newChildDomain(1),
                newChildDomain(2)))));
        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(new Result<>());

        childCatalogFacade.moveUp(childEntity);

        verify(catalogService).getAll();
        verify(catalogService).update(argumentCaptor.capture());
        verify(childValidator).validate(childEntity, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(catalogService, childValidator);
        verifyZeroInteractions(converter, parentValidator);

        assertParentDeepEquals(newParentDomainWithChildren(1, CollectionUtils.newList(childDomain1, childDomain2)), argumentCaptor.getValue());
    }

    /**
     * Test method for {@link CatalogChildFacade#moveUp(Movable)} with invalid data.
     */
    @Test
    public void moveUp_InvalidData() {
        final S childEntity = newChildEntity(Integer.MAX_VALUE);

        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = childCatalogFacade.moveUp(childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(childValidator).validate(childEntity, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(childValidator);
        verifyZeroInteractions(catalogService, converter, parentValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#moveDown(Movable)}.
     */
    @Test
    public void moveDown() {
        final S childEntity = newChildEntity(1);
        final T childDomain1 = newChildDomain(1);
        childDomain1.setPosition(1);
        final T childDomain2 = newChildDomain(2);
        childDomain2.setPosition(0);
        final ArgumentCaptor<V> argumentCaptor = ArgumentCaptor.forClass(getParentDomainClass());

        when(catalogService.getAll()).thenReturn(CollectionUtils.newList(newParentDomainWithChildren(1, CollectionUtils.newList(newChildDomain(1),
                newChildDomain(2)))));
        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(new Result<>());

        childCatalogFacade.moveDown(childEntity);

        verify(catalogService).getAll();
        verify(catalogService).update(argumentCaptor.capture());
        verify(childValidator).validate(childEntity, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(catalogService, childValidator);
        verifyZeroInteractions(converter, parentValidator);

        assertParentDeepEquals(newParentDomainWithChildren(1, CollectionUtils.newList(childDomain1, childDomain2)), argumentCaptor.getValue());
    }

    /**
     * Test method for {@link CatalogChildFacade#moveDown(Movable)} with invalid data.
     */
    @Test
    public void moveDown_InvalidData() {
        final S childEntity = newChildEntity(Integer.MAX_VALUE);

        when(childValidator.validate(any(getChildEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = childCatalogFacade.moveDown(childEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_DATA_RESULT));

        verify(childValidator).validate(childEntity, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(childValidator);
        verifyZeroInteractions(catalogService, converter, parentValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#find(Movable)}.
     */
    @Test
    public void find() {
        final U parentEntity = newParentEntity(1);
        final List<S> expectedData = CollectionUtils.newList(newChildEntity(1));

        if (isFirstChild()) {
            when(catalogService.get(any(Integer.class))).thenReturn(newParentDomain(1));
        } else {
            when(catalogService.getAll()).thenReturn(CollectionUtils.newList(newParentDomain(1)));
        }
        when(converter.convertCollection(anyListOf(getChildDomainClass()), eq(getChildEntityClass()))).thenReturn(expectedData);
        when(parentValidator.validate(any(getParentEntityClass()), anyVararg())).thenReturn(new Result<>());

        final Result<List<S>> result = childCatalogFacade.find(parentEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedData));
        assertThat(result.getEvents().isEmpty(), is(true));

        if (isFirstChild()) {
            verify(catalogService).get(parentEntity.getId());
        } else {
            verify(catalogService).getAll();
        }
        verify(converter).convertCollection(CollectionUtils.newList(newChildDomain(1)), getChildEntityClass());
        verify(parentValidator).validate(parentEntity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogService, converter, parentValidator);
        verifyZeroInteractions(childValidator);
    }

    /**
     * Test method for {@link CatalogChildFacade#find(Movable)} with invalid data.
     */
    @Test
    public void find_InvalidData() {
        final U parentEntity = newParentEntity(1);

        when(parentValidator.validate(any(getParentEntityClass()), anyVararg())).thenReturn(INVALID_DATA_RESULT);

        final Result<List<S>> result = childCatalogFacade.find(parentEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents(), is(INVALID_DATA_RESULT.getEvents()));

        verify(parentValidator).validate(parentEntity, ValidationType.EXISTS);
        verifyNoMoreInteractions(parentValidator);
        verifyZeroInteractions(catalogService, converter, childValidator);
    }

    /**
     * Returns service for catalog.
     *
     * @return service for catalog
     */
    protected CatalogService<V> getCatalogService() {
        return catalogService;
    }

    /**
     * Returns converter.
     *
     * @return converter
     */
    protected Converter getConverter() {
        return converter;
    }

    /**
     * Returns validator for catalog for parent data.
     *
     * @return validator for catalog for parent data
     */
    protected CatalogValidator<U> getParentCatalogValidator() {
        return parentValidator;
    }

    /**
     * Returns validator for catalog for child data.
     *
     * @return validator for catalog for child data
     */
    protected CatalogValidator<S> getChildCatalogValidator() {
        return childValidator;
    }

    /**
     * Returns true if child if 1st parent child.
     *
     * @return true if child if 1st parent child
     */
    protected boolean isFirstChild() {
        return true;
    }

    /**
     * Returns facade for catalog for child data.
     *
     * @return facade for catalog for child data
     */
    protected abstract CatalogChildFacade<S, U> getCatalogChildFacade();

    /**
     * Returns parent entity.
     *
     * @param id ID
     * @return parent entity
     */
    protected abstract U newParentEntity(Integer id);

    /**
     * Returns parent domain.
     *
     * @param id ID
     * @return parent domain
     */
    protected abstract V newParentDomain(Integer id);

    /**
     * Returns parent domain with children.
     *
     * @param id       ID
     * @param children children
     * @return parent domain with children
     */
    protected abstract V newParentDomainWithChildren(Integer id, List<T> children);

    /**
     * Returns child entity.
     *
     * @param id ID
     * @return child entity
     */
    protected abstract S newChildEntity(Integer id);

    /**
     * Returns child domain.
     *
     * @param id ID
     * @return child domain
     */
    protected abstract T newChildDomain(Integer id);

    /**
     * Returns parent entity class.
     *
     * @return parent entity class.
     */
    protected abstract Class<U> getParentEntityClass();

    /**
     * Returns parent domain class.
     *
     * @return parent domain class.
     */
    protected abstract Class<V> getParentDomainClass();

    /**
     * Returns child entity class.
     *
     * @return child entity class.
     */
    protected abstract Class<S> getChildEntityClass();

    /**
     * Returns child domain class.
     *
     * @return child domain class.
     */
    protected abstract Class<T> getChildDomainClass();

    /**
     * Assert parent deep equals.
     *
     * @param expected expected
     * @param actual   actual
     */
    protected abstract void assertParentDeepEquals(V expected, V actual);

}
