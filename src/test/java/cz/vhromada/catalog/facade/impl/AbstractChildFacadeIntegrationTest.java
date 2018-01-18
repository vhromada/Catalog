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
import cz.vhromada.catalog.facade.CatalogChildFacade;
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
 * An abstract class represents integration test for child facade.
 *
 * @param <T> type of child entity data
 * @param <U> type of child domain data
 * @param <V> type of parent entity data
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
abstract class AbstractChildFacadeIntegrationTest<T extends Movable, U extends Movable, V extends Movable> {

    /**
     * Null ID message
     */
    private static final String NULL_ID_MESSAGE = "ID mustn't be null.";

    /**
     * Test method for {@link CatalogChildFacade#get(Integer)}.
     */
    @Test
    void get() {
        for (int i = 1; i <= getDefaultChildDataCount(); i++) {
            final Result<T> result = getCatalogChildFacade().get(i);
            final int index = i;

            assertNotNull(result);
            assertAll(
                () -> assertEquals(Status.OK, result.getStatus()),
                () -> assertDataDeepEquals(result.getData(), getDomainData(index)),
                () -> assertTrue(result.getEvents().isEmpty())
            );
        }

        final Result<T> result = getCatalogChildFacade().get(Integer.MAX_VALUE);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#get(Integer)} with null data.
     */
    @Test
    void get_NullData() {
        final Result<T> result = getCatalogChildFacade().get(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "ID_NULL", NULL_ID_MESSAGE)), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#add(Movable, Movable)}.
     */
    @Test
    @DirtiesContext
    void add() {
        final U expectedData = newDomainData(getDefaultChildDataCount() + 1);
        expectedData.setPosition(Integer.MAX_VALUE);

        final Result<Void> result = getCatalogChildFacade().add(newParentData(1), newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertDataDomainDeepEquals(expectedData, getRepositoryData(getDefaultChildDataCount() + 1));
        assertAddRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#add(Movable, Movable)} with null parent.
     */
    @Test
    void add_NullParent() {
        final Result<Void> result = getCatalogChildFacade().add(null, newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullParentDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#add(Movable, Movable)} with parent with null ID.
     */
    @Test
    void add_NullId() {
        final Result<Void> result = getCatalogChildFacade().add(newParentData(null), newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullParentDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#add(Movable, Movable)} with with not existing parent.
     */
    @Test
    void add_NotExistingParent() {
        final Result<Void> result = getCatalogChildFacade().add(newParentData(Integer.MAX_VALUE), newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingParentDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#add(Movable, Movable)} with null child.
     */
    @Test
    void add_NullChild() {
        final Result<Void> result = getCatalogChildFacade().add(newParentData(1), null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#add(Movable, Movable)} with child with null ID.
     */
    @Test
    void add_NotNullId() {
        final Result<Void> result = getCatalogChildFacade().add(newParentData(1), newChildData(1));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, getChildPrefix() + "_ID_NOT_NULL", "ID must be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#update(Movable)}.
     */
    @Test
    @DirtiesContext
    void update() {
        final T data = newChildData(1);

        final Result<Void> result = getCatalogChildFacade().update(data);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertDataDeepEquals(data, getRepositoryData(1));
        assertUpdateRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#update(Movable)} with null data.
     */
    @Test
    void update_NullData() {
        final Result<Void> result = getCatalogChildFacade().update(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#update(Movable)} with data with null ID.
     */
    @Test
    void update_NullId() {
        final Result<Void> result = getCatalogChildFacade().update(newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#update(Movable)} with data with bad ID.
     */
    @Test
    void update_BadId() {
        final Result<Void> result = getCatalogChildFacade().update(newChildData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#remove(Movable)}.
     */
    @Test
    @DirtiesContext
    void remove() {
        final Result<Void> result = getCatalogChildFacade().remove(newChildData(1));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertNull(getRepositoryData(1));
        assertRemoveRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#remove(Movable)} with null data.
     */
    @Test
    void remove_NullData() {
        final Result<Void> result = getCatalogChildFacade().remove(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#remove(Movable)} with data with null ID.
     */
    @Test
    void remove_NullId() {
        final Result<Void> result = getCatalogChildFacade().remove(newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#remove(Movable)} with data with bad ID.
     */
    @Test
    void remove_BadId() {
        final Result<Void> result = getCatalogChildFacade().remove(newChildData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#duplicate(Movable)}.
     */
    @Test
    @DirtiesContext
    void duplicate() {
        final Result<Void> result = getCatalogChildFacade().duplicate(newChildData(1));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertDataDomainDeepEquals(getExpectedDuplicatedData(), getRepositoryData(getDefaultChildDataCount() + 1));
        assertDuplicateRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#duplicate(Movable)} with null data.
     */
    @Test
    void duplicate_NullData() {
        final Result<Void> result = getCatalogChildFacade().duplicate(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#duplicate(Movable)} with data with null ID.
     */
    @Test
    void duplicate_NullId() {
        final Result<Void> result = getCatalogChildFacade().duplicate(newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#duplicate(Movable)} with data with bad ID.
     */
    @Test
    void duplicate_BadId() {
        final Result<Void> result = getCatalogChildFacade().duplicate(newChildData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveUp(Movable)}.
     */
    @Test
    @DirtiesContext
    void moveUp() {
        final Result<Void> result = getCatalogChildFacade().moveUp(newChildData(2));

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
        for (int i = 3; i <= getDefaultChildDataCount(); i++) {
            assertDataDomainDeepEquals(getDomainData(i), getRepositoryData(i));
        }
        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveUp(Movable)} with null data.
     */
    @Test
    void moveUp_NullData() {
        final Result<Void> result = getCatalogChildFacade().moveUp(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveUp(Movable)} with data with null ID.
     */
    @Test
    void moveUp_NullId() {
        final Result<Void> result = getCatalogChildFacade().moveUp(newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveUp(Movable)} with not movable data.
     */
    @Test
    void moveUp_NotMovableData() {
        final Result<Void> result = getCatalogChildFacade().moveUp(newChildData(1));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, getChildPrefix() + "_NOT_MOVABLE", getChildName() + " can't be moved up.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveUp(Movable)} with data with bad ID.
     */
    @Test
    void moveUp_BadId() {
        final Result<Void> result = getCatalogChildFacade().moveUp(newChildData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveDown(Movable)}.
     */
    @Test
    @DirtiesContext
    void moveDown() {
        final Result<Void> result = getCatalogChildFacade().moveDown(newChildData(1));

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
        for (int i = 3; i <= getDefaultChildDataCount(); i++) {
            assertDataDomainDeepEquals(getDomainData(i), getRepositoryData(i));
        }
        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveDown(Movable)} with null data.
     */
    @Test
    void moveDown_NullData() {
        final Result<Void> result = getCatalogChildFacade().moveDown(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveDown(Movable)} with data with null ID.
     */
    @Test
    void moveDown_NullId() {
        final Result<Void> result = getCatalogChildFacade().moveDown(newChildData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNullChildDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveDown(Movable)} with not movable data.
     */
    @Test
    void moveDown_NotMovableData() {
        final Result<Void> result = getCatalogChildFacade().moveDown(newChildData(getDefaultChildDataCount()));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, getChildPrefix() + "_NOT_MOVABLE",
                getChildName() + " can't be moved down.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#moveDown(Movable)} with data with bad ID.
     */
    @Test
    void moveDown_BadId() {
        final Result<Void> result = getCatalogChildFacade().moveDown(newChildData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(getNotExistingChildDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#find(Movable)}.
     */
    @Test
    void find() {
        for (int i = 1; i <= getDefaultParentDataCount(); i++) {
            final Result<List<T>> result = getCatalogChildFacade().find(newParentData(i));
            final int index = i;

            assertNotNull(result);
            assertAll(
                () -> assertEquals(Status.OK, result.getStatus()),
                () -> assertDataListDeepEquals(result.getData(), getDataList(index)),
                () -> assertTrue(result.getEvents().isEmpty())
            );
        }

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#find(Movable)} with null parent.
     */
    @Test
    void find_NullParent() {
        final Result<List<T>> result = getCatalogChildFacade().find(null);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Collections.singletonList(getNullParentDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#find(Movable)} with parent with null ID.
     */
    @Test
    void find_NullId() {
        final Result<List<T>> result = getCatalogChildFacade().find(newParentData(null));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Collections.singletonList(getNullParentDataIdEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogChildFacade#find(Movable)} with parent with bad ID.
     */
    @Test
    void find_BadId() {
        final Result<List<T>> result = getCatalogChildFacade().find(newParentData(Integer.MAX_VALUE));

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertNull(result.getData()),
            () -> assertEquals(Collections.singletonList(getNotExistingParentDataEvent()), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Returns facade for catalog for child data.
     *
     * @return facade for catalog for child data
     */
    protected abstract CatalogChildFacade<T, V> getCatalogChildFacade();

    /**
     * Returns default count of parent data.
     *
     * @return default count of parent data
     */
    protected abstract Integer getDefaultParentDataCount();

    /**
     * Returns default count of child data.
     *
     * @return default count of child data
     */
    protected abstract Integer getDefaultChildDataCount();

    /**
     * Returns repository parent count of data.
     *
     * @return repository parent count of data
     */
    protected abstract Integer getRepositoryParentDataCount();

    /**
     * Returns repository child count of data.
     *
     * @return repository child count of data
     */
    protected abstract Integer getRepositoryChildDataCount();

    /**
     * Returns list of data.
     *
     * @param parentId parent ID
     * @return list of data
     */
    protected abstract List<U> getDataList(Integer parentId);

    /**
     * Returns domain data.
     *
     * @param index index
     * @return domain data
     */
    protected abstract U getDomainData(Integer index);

    /**
     * Returns new parent data.
     *
     * @param id ID
     * @return new parent data
     */
    protected abstract V newParentData(Integer id);

    /**
     * Returns new child data.
     *
     * @param id ID
     * @return new child data
     */
    protected abstract T newChildData(Integer id);

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
     * Returns name of parent entity.
     *
     * @return name of parent entity
     */
    protected abstract String getParentName();

    /**
     * Returns name of child entity.
     *
     * @return name of child entity
     */
    protected abstract String getChildName();

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
     * Returns expected duplicated data.
     *
     * @return expected duplicated data
     */
    protected U getExpectedDuplicatedData() {
        final U data = getDomainData(1);
        data.setId(getDefaultChildDataCount() + 1);

        return data;
    }

    /**
     * Asserts default repository data.
     */
    protected void assertDefaultRepositoryData() {
        assertAll(
            () -> assertEquals(getDefaultChildDataCount(), getRepositoryChildDataCount()),
            this::assertReferences
        );
    }

    /**
     * Asserts repository data for {@link CatalogChildFacade#add(Movable, Movable)}.
     */
    private void assertAddRepositoryData() {
        assertAll(
            () -> assertEquals(Integer.valueOf(getDefaultChildDataCount() + 1), getRepositoryChildDataCount()),
            this::assertReferences
        );
    }

    /**
     * Asserts repository data for {@link CatalogChildFacade#update(Movable)}.
     */
    protected void assertUpdateRepositoryData() {
        assertAll(
            () -> assertEquals(getDefaultChildDataCount(), getRepositoryChildDataCount()),
            this::assertReferences
        );
    }

    /**
     * Asserts repository data for {@link CatalogChildFacade#remove(Movable)}.
     */
    protected void assertRemoveRepositoryData() {
        assertAll(
            () -> assertEquals(Integer.valueOf(getDefaultChildDataCount() - 1), getRepositoryChildDataCount()),
            this::assertReferences
        );
    }

    /**
     * Asserts repository data for {@link CatalogChildFacade#duplicate(Movable)}.
     */
    protected void assertDuplicateRepositoryData() {
        assertAll(
            () -> assertEquals(Integer.valueOf(getDefaultChildDataCount() + 1), getRepositoryChildDataCount()),
            this::assertReferences
        );
    }

    /**
     * Asserts references.
     */
    protected void assertReferences() {
        assertEquals(getDefaultParentDataCount(), getRepositoryParentDataCount());
    }

    /**
     * Returns event for null parent data.
     *
     * @return event for null parent data
     */
    private Event getNullParentDataEvent() {
        return new Event(Severity.ERROR, getParentPrefix() + "_NULL", getParentName() + " mustn't be null.");
    }

    /**
     * Returns event for parent data with null ID.
     *
     * @return event for parent data with null ID
     */
    private Event getNullParentDataIdEvent() {
        return new Event(Severity.ERROR, getParentPrefix() + "_ID_NULL", NULL_ID_MESSAGE);
    }

    /**
     * Returns event for not existing parent data.
     *
     * @return event for not existing parent data
     */
    private Event getNotExistingParentDataEvent() {
        return new Event(Severity.ERROR, getParentPrefix() + "_NOT_EXIST", getParentName() + " doesn't exist.");
    }

    /**
     * Returns event for null child data.
     *
     * @return event for null child data
     */
    private Event getNullChildDataEvent() {
        return new Event(Severity.ERROR, getChildPrefix() + "_NULL", getChildName() + " mustn't be null.");
    }

    /**
     * Returns event for child data with null ID.
     *
     * @return event for child data with null ID
     */
    private Event getNullChildDataIdEvent() {
        return new Event(Severity.ERROR, getChildPrefix() + "_ID_NULL", NULL_ID_MESSAGE);
    }

    /**
     * Returns event for not existing child data.
     *
     * @return event for not existing child data
     */
    private Event getNotExistingChildDataEvent() {
        return new Event(Severity.ERROR, getChildPrefix() + "_NOT_EXIST", getChildName() + " doesn't exist.");
    }

    /**
     * Returns parent prefix for validation keys.
     *
     * @return parent prefix for validation keys
     */
    private String getParentPrefix() {
        return getParentName().toUpperCase();
    }

    /**
     * Returns child prefix for validation keys.
     *
     * @return child prefix for validation keys
     */
    private String getChildPrefix() {
        return getChildName().toUpperCase();
    }

}
