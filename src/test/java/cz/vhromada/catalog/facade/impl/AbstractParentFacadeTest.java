package cz.vhromada.catalog.facade.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;
import cz.vhromada.test.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

/**
 * An abstract class represents test for parent facade.
 *
 * @param <T> type of entity data
 * @param <U> type of domain data
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension.class)
abstract class AbstractParentFacadeTest<T extends Movable, U extends Movable> {

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
     * Instance of {@link CatalogParentFacade}
     */
    private CatalogParentFacade<T> parentCatalogFacade;

    /**
     * Initializes facade for catalog.
     */
    @BeforeEach
    void setUp() {
        parentCatalogFacade = getCatalogParentFacade();
    }

    /**
     * Test method for {@link CatalogParentFacade#newData()}.
     */
    @Test
    void newData() {
        final Result<Void> result = parentCatalogFacade.newData();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).newData();
        verifyNoMoreInteractions(catalogService);
        verifyZeroInteractions(converter, catalogValidator);
    }

    /**
     * Test method for {@link CatalogParentFacade#getAll()}.
     */
    @Test
    void getAll() {
        final List<U> domainList = CollectionUtils.newList(newDomain(1), newDomain(2));
        final List<T> entityList = CollectionUtils.newList(newEntity(1), newEntity(2));

        when(catalogService.getAll()).thenReturn(domainList);
        when(converter.convertCollection(anyList(), eq(getEntityClass()))).thenReturn(entityList);

        final Result<List<T>> result = parentCatalogFacade.getAll();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertEquals(entityList, result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).getAll();
        verify(converter).convertCollection(domainList, getEntityClass());
        verifyNoMoreInteractions(catalogService, converter);
        verifyZeroInteractions(catalogValidator);
    }


    /**
     * Test method for {@link CatalogParentFacade#get(Integer)} with existing data.
     */
    @Test
    void get_ExistingData() {
        final U domain = newDomain(1);
        final T entity = newEntity(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(converter.convert(any(getDomainClass()), eq(getEntityClass()))).thenReturn(entity);

        final Result<T> result = parentCatalogFacade.get(1);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertEquals(entity, result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).get(1);
        verify(converter).convert(domain, getEntityClass());
        verifyNoMoreInteractions(catalogService, converter);
        verifyZeroInteractions(catalogValidator);
    }

    /**
     * Test method for {@link CatalogParentFacade#get(Integer)} with not existing data.
     */
    @Test
    void get_NotExistingData() {
        when(catalogService.get(any(Integer.class))).thenReturn(null);

        final Result<T> result = parentCatalogFacade.get(Integer.MAX_VALUE);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, getEntityClass());
        verifyNoMoreInteractions(catalogService, converter);
        verifyZeroInteractions(catalogValidator);
    }

    /**
     * Test method for {@link CatalogParentFacade#get(Integer)} with null data.
     */
    @Test
    void get_NullData() {
        final Result<T> result = parentCatalogFacade.get(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")), result.getEvents())
        );

        verifyZeroInteractions(catalogService, converter, catalogValidator);
    }

    /**
     * Test method for {@link CatalogParentFacade#add(Movable)}.
     */
    @Test
    void add() {
        final T entity = newEntity(null);
        final U domain = newDomain(null);

        when(converter.convert(any(getEntityClass()), eq(getDomainClass()))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.add(entity);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).add(domain);
        verify(converter).convert(entity, getDomainClass());
        verify(catalogValidator).validate(entity, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(catalogService, converter, catalogValidator);
    }

    /**
     * Test method for {@link CatalogParentFacade#add(Movable)} with invalid data.
     */
    @Test
    void add_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.add(entity);

        assertEquals(INVALID_DATA_RESULT, result);

        verify(catalogValidator).validate(entity, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)}.
     */
    @Test
    void update() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        initUpdateMock(domain);

        final Result<Void> result = parentCatalogFacade.update(entity);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verifyUpdateMock(entity, domain);
        verifyNoMoreInteractions(catalogService, converter, catalogValidator);
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)} with invalid data.
     */
    @Test
    void update_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.update(entity);

        assertEquals(INVALID_DATA_RESULT, result);

        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)}.
     */
    @Test
    void remove() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.remove(entity);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).get(1);
        verify(catalogService).remove(domain);
        verify(catalogValidator).validate(entity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogService, catalogValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)} with invalid data.
     */
    @Test
    void remove_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.remove(entity);

        assertEquals(INVALID_DATA_RESULT, result);

        verify(catalogValidator).validate(entity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)}.
     */
    @Test
    void duplicate() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.duplicate(entity);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).get(1);
        verify(catalogService).duplicate(domain);
        verify(catalogValidator).validate(entity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogService, catalogValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)} with invalid data.
     */
    @Test
    void duplicate_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.duplicate(entity);

        assertEquals(INVALID_DATA_RESULT, result);

        verify(catalogValidator).validate(entity, ValidationType.EXISTS);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)}.
     */
    @Test
    void moveUp() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.moveUp(entity);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).get(1);
        verify(catalogService).moveUp(domain);
        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(catalogService, catalogValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)} with invalid data.
     */
    @Test
    void moveUp_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.moveUp(entity);

        assertEquals(INVALID_DATA_RESULT, result);

        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)}.
     */
    @Test
    void moveDown() {
        final T entity = newEntity(1);
        final U domain = newDomain(1);

        when(catalogService.get(any(Integer.class))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(new Result<>());

        final Result<Void> result = parentCatalogFacade.moveDown(entity);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).get(1);
        verify(catalogService).moveDown(domain);
        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(catalogService, catalogValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)} with invalid data.
     */
    @Test
    void moveDown_InvalidData() {
        final T entity = newEntity(Integer.MAX_VALUE);

        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(INVALID_DATA_RESULT);

        final Result<Void> result = parentCatalogFacade.moveDown(entity);

        assertEquals(INVALID_DATA_RESULT, result);

        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(catalogValidator);
        verifyZeroInteractions(catalogService, converter);
    }

    /**
     * Test method for {@link CatalogParentFacade#updatePositions()}.
     */
    @Test
    void updatePositions() {
        final Result<Void> result = parentCatalogFacade.updatePositions();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(catalogService).updatePositions();
        verifyNoMoreInteractions(catalogService);
        verifyZeroInteractions(converter, catalogValidator);
    }

    /**
     * Returns service for catalog.
     *
     * @return service for catalog
     */
    protected CatalogService<U> getCatalogService() {
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
     * Returns validator for catalog.
     *
     * @return validator for catalog
     */
    protected CatalogValidator<T> getCatalogValidator() {
        return catalogValidator;
    }

    /**
     * Initializes mock for update.
     *
     * @param domain domain
     */
    protected void initUpdateMock(final U domain) {
        when(converter.convert(any(getEntityClass()), eq(getDomainClass()))).thenReturn(domain);
        when(catalogValidator.validate(any(getEntityClass()), any())).thenReturn(new Result<>());
    }

    /**
     * Verifies mock for update.
     *
     * @param entity entity
     * @param domain domain
     */
    protected void verifyUpdateMock(final T entity, final U domain) {
        verify(catalogService).update(domain);
        verify(converter).convert(entity, getDomainClass());
        verify(catalogValidator).validate(entity, ValidationType.EXISTS, ValidationType.DEEP);
    }

    /**
     * Returns facade for catalog for parent data.
     *
     * @return facade for catalog for parent data
     */
    protected abstract CatalogParentFacade<T> getCatalogParentFacade();

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
