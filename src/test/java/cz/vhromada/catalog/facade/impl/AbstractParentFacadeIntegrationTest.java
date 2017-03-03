package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * An abstract class represents integration test for parent facade.
 *
 * @param <T> type of entity data
 * @param <U> type of domain data
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public abstract class AbstractParentFacadeIntegrationTest<T extends Movable, U extends Movable> {

    /**
     * Test method for {@link CatalogParentFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void newData() {
        clearReferencedData();

        final Result<Void> result = getCatalogParentFacade().newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertNewRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final Result<List<T>> result = getCatalogParentFacade().getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));
        assertDataListDeepEquals(result.getData(), getDataList());

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= getDefaultDataCount(); i++) {
            final Result<T> result = getCatalogParentFacade().get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            assertDataDeepEquals(result.getData(), getDomainData(i));
        }

        final Result<T> result = getCatalogParentFacade().get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#get(Integer)} with null data.
     */
    @Test
    public void get_NullData() {
        final Result<T> result = getCatalogParentFacade().get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#add(Movable)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final Result<Void> result = getCatalogParentFacade().add(newData(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertDataDomainDeepEquals(getExpectedAddData(), getRepositoryData(getDefaultDataCount() + 1));
        assertAddRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#add(Movable)} with null data.
     */
    @Test
    public void add_NullData() {
        final Result<Void> result = getCatalogParentFacade().add(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#add(Movable)} with data with not null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = getCatalogParentFacade().add(newData(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_ID_NOT_NULL", "ID must be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final T data = getUpdateData(1);

        final Result<Void> result = getCatalogParentFacade().update(data);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertDataDeepEquals(data, getRepositoryData(1));
        assertUpdateRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)} with null data.
     */
    @Test
    public void update_NullData() {
        final Result<Void> result = getCatalogParentFacade().update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)} with data with null ID.
     */
    @Test
    public void update_NullId() {
        final Result<Void> result = getCatalogParentFacade().update(newData(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataIdEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#update(Movable)} with data with bad ID.
     */
    @Test
    public void update_BadId() {
        final Result<Void> result = getCatalogParentFacade().update(newData(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNotExistingDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        clearReferencedData();

        final Result<Void> result = getCatalogParentFacade().remove(newData(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(getRepositoryData(1), is(nullValue()));
        assertRemoveRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)} with null data.
     */
    @Test
    public void remove_NullData() {
        final Result<Void> result = getCatalogParentFacade().remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)} with data with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = getCatalogParentFacade().remove(newData(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataIdEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#remove(Movable)} with data with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = getCatalogParentFacade().remove(newData(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNotExistingDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final Result<Void> result = getCatalogParentFacade().duplicate(newData(getDefaultDataCount()));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertDataDomainDeepEquals(getExpectedDuplicatedData(), getRepositoryData(getDefaultDataCount() + 1));
        assertDuplicateRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)} with null data.
     */
    @Test
    public void duplicate_NullData() {
        final Result<Void> result = getCatalogParentFacade().duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)} with data with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = getCatalogParentFacade().duplicate(newData(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataIdEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#duplicate(Movable)} with data with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = getCatalogParentFacade().duplicate(newData(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNotExistingDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final Result<Void> result = getCatalogParentFacade().moveUp(newData(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

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
    public void moveUp_NullData() {
        final Result<Void> result = getCatalogParentFacade().moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)} with data with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = getCatalogParentFacade().moveUp(newData(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataIdEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)} with not movable data.
     */
    @Test
    public void moveUp_NotMovableData() {
        final Result<Void> result = getCatalogParentFacade().moveUp(newData(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_NOT_MOVABLE", getName() + " can't be moved up.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveUp(Movable)} with data with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = getCatalogParentFacade().moveUp(newData(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNotExistingDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final Result<Void> result = getCatalogParentFacade().moveDown(newData(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

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
    public void moveDown_NullData() {
        final Result<Void> result = getCatalogParentFacade().moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)} with data with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = getCatalogParentFacade().moveDown(newData(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNullDataIdEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)} with not movable data.
     */
    @Test
    public void moveDown_NotMovableData() {
        final Result<Void> result = getCatalogParentFacade().moveDown(newData(getDefaultDataCount()));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, getPrefix() + "_NOT_MOVABLE", getName() + " can't be moved down.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#moveDown(Movable)} with data with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = getCatalogParentFacade().moveDown(newData(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(getNotExistingDataEvent()));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link CatalogParentFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void updatePositions() {
        final Result<Void> result = getCatalogParentFacade().updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

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
        assertThat(getRepositoryDataCount(), is(getDefaultDataCount()));
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#newData()}.
     */
    protected void assertNewRepositoryData() {
        assertThat(getRepositoryDataCount(), is(0));
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#add(Movable)}.
     */
    protected void assertAddRepositoryData() {
        assertThat(getRepositoryDataCount(), is(getDefaultDataCount() + 1));
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#update(Movable)}.
     */
    protected void assertUpdateRepositoryData() {
        assertThat(getRepositoryDataCount(), is(getDefaultDataCount()));
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#remove(Movable)}.
     */
    protected void assertRemoveRepositoryData() {
        assertThat(getRepositoryDataCount(), is(getDefaultDataCount() - 1));
    }

    /**
     * Asserts repository data for {@link CatalogParentFacade#duplicate(Movable)}.
     */
    protected void assertDuplicateRepositoryData() {
        assertThat(getRepositoryDataCount(), is(getDefaultDataCount() + 1));
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
