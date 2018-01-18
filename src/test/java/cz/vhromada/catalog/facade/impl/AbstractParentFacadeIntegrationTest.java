package cz.vhromada.catalog.facade.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * An abstract class represents integration test for parent facade.
 *
 * @param <T> type of entity data
 * @param <U> type of domain data
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
abstract class AbstractParentFacadeIntegrationTest<T extends Movable, U extends Movable> {

    /**
     * Test method for {@link CatalogParentFacade#newData()}.
     */
    @Test
    @DirtiesContext
    void newData() {
        clearReferencedData();

        final Result<Void> result = getCatalogParentFacade().newData();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );
        
        assertNewRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#getAll()}.
     */
    @Test
    void getAll() {
        final Result<List<T>> result = getCatalogParentFacade().getAll();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertDataListDeepEquals(result.getData(), getDataList()),
            () -> assertTrue(result.getEvents().isEmpty())
        );
        
        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#get(Integer)}.
     */
    @Test
    void get() {
        for (int i = 1; i <= getDefaultDataCount(); i++) {
            final Result<T> result = getCatalogParentFacade().get(i);
            final int index = i;

            assertNotNull(result);
            assertAll(
                () -> assertEquals(Status.OK, result.getStatus()),
                () -> assertDataDeepEquals(result.getData(), getDomainData(index)),
                () -> assertTrue(result.getEvents().isEmpty())
            );
        }

        final Result<T> result = getCatalogParentFacade().get(Integer.MAX_VALUE);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#get(Integer)} with null data.
     */
    @Test
    void get_NullData() {
        final Result<T> result = getCatalogParentFacade().get(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#add(Movable)}.
     */
    @Test
    @DirtiesContext
    void add() {
        final Result<Void> result = getCatalogParentFacade().add(newData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertDataDomainDeepEquals(getExpectedAddData(), getRepositoryData(getDefaultDataCount() + 1));
        assertAddRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#add(Movable)} with null data.
     */
    @Test
    void add_NullData() {
        final Result<Void> result = getCatalogParentFacade().add(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#add(Movable)} with data with not null ID.
     */
    @Test
    void add_NotNullId() {
        final Result<Void> result = getCatalogParentFacade().add(newData(1));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, getPrefix() + "_ID_NOT_NULL", "ID must be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)}.
     */
    @Test
    @DirtiesContext
    void update() {
        final T data = getUpdateData(1);

        final Result<Void> result = getCatalogParentFacade().update(data);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertDataDeepEquals(data, getRepositoryData(1));
        assertUpdateRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)} with null data.
     */
    @Test
    void update_NullData() {
        final Result<Void> result = getCatalogParentFacade().update(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)} with data with null ID.
     */
    @Test
    void update_NullId() {
        final Result<Void> result = getCatalogParentFacade().update(newData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)} with data with bad ID.
     */
    @Test
    void update_BadId() {
        final Result<Void> result = getCatalogParentFacade().update(newData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)}.
     */
    @Test
    @DirtiesContext
    void remove() {
        clearReferencedData();

        final Result<Void> result = getCatalogParentFacade().remove(newData(1));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertNull(getRepositoryData(1));
        assertRemoveRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)} with null data.
     */
    @Test
    void remove_NullData() {
        final Result<Void> result = getCatalogParentFacade().remove(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)} with data with null ID.
     */
    @Test
    void remove_NullId() {
        final Result<Void> result = getCatalogParentFacade().remove(newData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)} with data with bad ID.
     */
    @Test
    void remove_BadId() {
        final Result<Void> result = getCatalogParentFacade().remove(newData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)}.
     */
    @Test
    @DirtiesContext
    void duplicate() {
        final Result<Void> result = getCatalogParentFacade().duplicate(newData(getDefaultDataCount()));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertDataDomainDeepEquals(getExpectedDuplicatedData(), getRepositoryData(getDefaultDataCount() + 1));
        assertDuplicateRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)} with null data.
     */
    @Test
    void duplicate_NullData() {
        final Result<Void> result = getCatalogParentFacade().duplicate(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)} with data with null ID.
     */
    @Test
    void duplicate_NullId() {
        final Result<Void> result = getCatalogParentFacade().duplicate(newData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)} with data with bad ID.
     */
    @Test
    void duplicate_BadId() {
        final Result<Void> result = getCatalogParentFacade().duplicate(newData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)}.
     */
    @Test
    @DirtiesContext
    void moveUp() {
        final Result<Void> result = getCatalogParentFacade().moveUp(newData(2));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        final U data1 = getDomainData(1);
        data1.setPosition(1);
        final U data2 = getDomainData(2);
        data2.setPosition(0);
        assertDataDomainDeepEquals(data1, getRepositoryData(1));
        assertDataDomainDeepEquals(data2, getRepositoryData(2));
        for (int i = 3; i <= getDefaultDataCount(); i++) {
            assertDataDomainDeepEquals(getDomainData(i), getRepositoryData(i));
        }
        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)} with null data.
     */
    @Test
    void moveUp_NullData() {
        final Result<Void> result = getCatalogParentFacade().moveUp(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)} with data with null ID.
     */
    @Test
    void moveUp_NullId() {
        final Result<Void> result = getCatalogParentFacade().moveUp(newData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)} with not movable data.
     */
    @Test
    void moveUp_NotMovableData() {
        final Result<Void> result = getCatalogParentFacade().moveUp(newData(1));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, getPrefix() + "_NOT_MOVABLE", getName() + " can't be moved up.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)} with data with bad ID.
     */
    @Test
    void moveUp_BadId() {
        final Result<Void> result = getCatalogParentFacade().moveUp(newData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)}.
     */
    @Test
    @DirtiesContext
    void moveDown() {
        final Result<Void> result = getCatalogParentFacade().moveDown(newData(1));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        final U data1 = getDomainData(1);
        data1.setPosition(1);
        final U data2 = getDomainData(2);
        data2.setPosition(0);
        assertDataDomainDeepEquals(data1, getRepositoryData(1));
        assertDataDomainDeepEquals(data2, getRepositoryData(2));
        for (int i = 3; i <= getDefaultDataCount(); i++) {
            assertDataDomainDeepEquals(getDomainData(i), getRepositoryData(i));
        }
        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)} with null data.
     */
    @Test
    void moveDown_NullData() {
        final Result<Void> result = getCatalogParentFacade().moveDown(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)} with data with null ID.
     */
    @Test
    void moveDown_NullId() {
        final Result<Void> result = getCatalogParentFacade().moveDown(newData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)} with not movable data.
     */
    @Test
    void moveDown_NotMovableData() {
        final Result<Void> result = getCatalogParentFacade().moveDown(newData(getDefaultDataCount()));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, getPrefix() + "_NOT_MOVABLE", getName() + " can't be moved down.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)} with data with bad ID.
     */
    @Test
    void moveDown_BadId() {
        final Result<Void> result = getCatalogParentFacade().moveDown(newData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    void updatePositions() {
        final Result<Void> result = getCatalogParentFacade().updatePositions();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        for (int i = 1; i <= getDefaultDataCount(); i++) {
            assertDataDomainDeepEquals(getDomainData(i), getRepositoryData(i));
        }
        assertDefaultRepositoryData();
    }

    /**
     * Returns facade for catalog for parent data.
     *
     * @return facade for catalog for parent data
     */
    protected abstract CatalogParentFacade<T> getCatalogParentFacade();

    /**
     * Returns default count of data.
     *
     * @return default count of data
     */
    protected abstract Integer getDefaultDataCount();

    /**
     * Returns repository count of data.
     *
     * @return repository count of data
     */
    protected abstract Integer getRepositoryDataCount();

    /**
     * Returns list of data.
     *
     * @return list of data
     */
    protected abstract List<U> getDataList();

    /**
     * Returns domain data.
     *
     * @param index index
     * @return domain data
     */
    protected abstract U getDomainData(Integer index);

    /**
     * Returns new data.
     *
     * @param id ID
     * @return new data
     */
    protected abstract T newData(Integer id);

    /**
     * Returns domain data.
     *
     * @param id ID
     * @return domain data
     */
    protected abstract U newDomainData(Integer id);

    /**
     * Returns repository data.
     *
     * @param id ID
     * @return repository data
     */
    protected abstract U getRepositoryData(Integer id);

    /**
     * Returns name of entity.
     *
     * @return name of entity
     */
    protected abstract String getName();

    /**
     * Clears referenced data.
     */
    protected abstract void clearReferencedData();

    /**
     * Asserts list of data deep equals.
     *
     * @param expected expected
     * @param actual   actual
     */
    protected abstract void assertDataListDeepEquals(List<T> expected, List<U> actual);

    /**
     * Asserts data deep equals.
     *
     * @param expected expected
     * @param actual   actual
     */
    protected abstract void assertDataDeepEquals(T expected, U actual);

    /**
     * Asserts domain data deep equals.
     *
     * @param expected expected
     * @param actual   actual
     */
    protected abstract void assertDataDomainDeepEquals(U expected, U actual);

    /**
     * Returns update data.
     *
     * @param id ID
     * @return update data
     */
    //TODO vhromada 17.01.2018:
    protected T getUpdateData(final Integer id) {
        return newData(id);
    }

    /**
     * Returns expected add data.
     *
     * @return expected add data
     */
    protected U getExpectedAddData() {
        return newDomainData(getDefaultDataCount() + 1);
    }

    /**
     * Returns expected duplicated data.
     *
     * @return expected duplicated data
     */
    protected U getExpectedDuplicatedData() {
        final U data = getDomainData(getDefaultDataCount());
        data.setId(getDefaultDataCount() + 1);

        return data;
    }

    /**
     * Asserts default repository data.
     */
    protected void assertDefaultRepositoryData() {
        assertEquals(getDefaultDataCount(), getRepositoryDataCount());
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#newData()}.
     */
    protected void assertNewRepositoryData() {
        assertEquals(Integer.valueOf(0), getRepositoryDataCount());
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#add(Movable)}.
     */
    protected void assertAddRepositoryData() {
        assertEquals(Integer.valueOf(getDefaultDataCount() + 1), getRepositoryDataCount());
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#update(Movable)}.
     */
    protected void assertUpdateRepositoryData() {
        assertEquals(getDefaultDataCount(), getRepositoryDataCount());
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#remove(Movable)}.
     */
    protected void assertRemoveRepositoryData() {
        assertEquals(Integer.valueOf(getDefaultDataCount() - 1), getRepositoryDataCount());
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#duplicate(Movable)}.
     */
    protected void assertDuplicateRepositoryData() {
        assertEquals(Integer.valueOf(getDefaultDataCount() + 1), getRepositoryDataCount());
    }

    /**
     * Returns event for null data.
     *
     * @return event for null data
     */
    private Event getNullDataEvent() {
        return new Event(Severity.ERROR, getPrefix() + "_NULL", getName() + " mustn't be null.");
    }

    /**
     * Returns event for data with null ID.
     *
     * @return event for data with null ID
     */
    private Event getNullDataIdEvent() {
        return new Event(Severity.ERROR, getPrefix() + "_ID_NULL", "ID mustn't be null.");
    }

    /**
     * Returns event for not existing data.
     *
     * @return event for not existing data
     */
    private Event getNotExistingDataEvent() {
        return new Event(Severity.ERROR, getPrefix() + "_NOT_EXIST", getName() + " doesn't exist.");
    }

    /**
     * Returns prefix for validation keys.
     *
     * @return prefix for validation keys
     */
    private String getPrefix() {
        return getName().toUpperCase();
    }

}
