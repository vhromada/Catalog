package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ShowUtils;
import cz.vhromada.catalog.dao.ShowDAO;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link ShowDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ShowDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for shows
     */
    @Mock
    private TypedQuery<Show> showsQuery;

    /**
     * Instance of {@link ShowDAO}
     */
    private ShowDAO showDAO;

    /**
     * Initializes DAO for shows.
     */
    @Before
    public void setUp() {
        showDAO = new ShowDAOImpl(entityManager);
    }

    /**
     * Test method for {@link ShowDAOImpl#ShowDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new ShowDAOImpl(null);
    }

    /**
     * Test method for {@link ShowDAO#getShows()}.
     */
    @Test
    public void testGetShows() {
        when(entityManager.createNamedQuery(anyString(), eq(Show.class))).thenReturn(showsQuery);
        when(showsQuery.getResultList()).thenReturn(CollectionUtils.newList(ShowUtils.newShow(ShowUtils.ID), ShowUtils.newShow(2)));

        final List<Show> shows = showDAO.getShows();

        ShowUtils.assertShowsDeepEquals(CollectionUtils.newList(ShowUtils.newShow(ShowUtils.ID), ShowUtils.newShow(2)), shows);

        verify(entityManager).createNamedQuery(Show.SELECT_SHOWS, Show.class);
        verify(showsQuery).getResultList();
        verifyNoMoreInteractions(entityManager, showsQuery);
    }

    /**
     * Test method for {@link ShowDAO#getShows()} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetShows_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Show.class));

        showDAO.getShows();
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)} with existing show.
     */
    @Test
    public void testGetShow_ExistingShow() {
        when(entityManager.find(eq(Show.class), anyInt())).thenReturn(ShowUtils.newShow(ShowUtils.ID));

        final Show show = showDAO.getShow(ShowUtils.ID);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShow(ShowUtils.ID), show);

        verify(entityManager).find(Show.class, ShowUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)} with not existing show.
     */
    @Test
    public void testGetShow_NotExistingShow() {
        when(entityManager.find(eq(Show.class), anyInt())).thenReturn(null);

        final Show show = showDAO.getShow(Integer.MAX_VALUE);

        assertNull(show);

        verify(entityManager).find(Show.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetShow_NullArgument() {
        showDAO.getShow(null);
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetShow_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Show.class), anyInt());

        showDAO.getShow(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link ShowDAO#add(Show)}.
     */
    @Test
    public void testAdd() {
        final Show show = ShowUtils.newShow(ShowUtils.ID);
        doAnswer(setId(ShowUtils.ID)).when(entityManager).persist(any(Show.class));

        showDAO.add(show);

        assertEquals(ShowUtils.ID, show.getId());
        assertEquals(ShowUtils.ID - 1, show.getPosition());

        verify(entityManager).persist(show);
        verify(entityManager).merge(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#add(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        showDAO.add(null);
    }

    /**
     * Test method for {@link ShowDAO#add(Show)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Show.class));

        showDAO.add(ShowUtils.newShow(ShowUtils.ID));
    }

    /**
     * Test method for {@link ShowDAO#update(Show)}.
     */
    @Test
    public void testUpdate() {
        final Show show = ShowUtils.newShow(ShowUtils.ID);

        showDAO.update(show);

        verify(entityManager).merge(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#update(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        showDAO.update(null);
    }

    /**
     * Test method for {@link ShowDAO#update(Show)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Show.class));

        showDAO.update(ShowUtils.newShow(ShowUtils.ID));
    }

    /**
     * Test method for {@link ShowDAO#remove(Show)} with managed show.
     */
    @Test
    public void testRemove_ManagedShow() {
        final Show show = ShowUtils.newShow(ShowUtils.ID);
        when(entityManager.contains(any(Show.class))).thenReturn(true);

        showDAO.remove(show);

        verify(entityManager).contains(show);
        verify(entityManager).remove(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#remove(Show)} with not managed show.
     */
    @Test
    public void testRemove_NotManagedShow() {
        final Show show = ShowUtils.newShow(ShowUtils.ID);
        when(entityManager.contains(any(Show.class))).thenReturn(false);
        when(entityManager.getReference(eq(Show.class), anyInt())).thenReturn(show);

        showDAO.remove(show);

        verify(entityManager).contains(show);
        verify(entityManager).getReference(Show.class, show.getId());
        verify(entityManager).remove(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#remove(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        showDAO.remove(null);
    }

    /**
     * Test method for {@link ShowDAO#remove(Show)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Show.class));

        showDAO.remove(ShowUtils.newShow(ShowUtils.ID));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Show) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
