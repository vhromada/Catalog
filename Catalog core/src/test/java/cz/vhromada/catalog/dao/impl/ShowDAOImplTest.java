package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.ShowDAO;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.test.DeepAsserts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link ShowDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ShowDAOImplTest extends ObjectGeneratorTest {

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
    public void testConstructorWithNullEntityManager() {
        new ShowDAOImpl(null);
    }

    /**
     * Test method for {@link ShowDAO#getShows()}.
     */
    @Test
    public void testGetShows() {
        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
        when(entityManager.createNamedQuery(anyString(), eq(Show.class))).thenReturn(showsQuery);
        when(showsQuery.getResultList()).thenReturn(shows);

        DeepAsserts.assertEquals(shows, showDAO.getShows());

        verify(entityManager).createNamedQuery(Show.SELECT_SHOWS, Show.class);
        verify(showsQuery).getResultList();
        verifyNoMoreInteractions(entityManager, showsQuery);
    }

    /**
     * Test method for {@link ShowDAO#getShows()} with exception in persistence.
     */
    @Test
    public void testGetShowsWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Show.class));

        try {
            showDAO.getShows();
            fail("Can't get shows with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).createNamedQuery(Show.SELECT_SHOWS, Show.class);
        verifyNoMoreInteractions(entityManager);
        verifyZeroInteractions(showsQuery);
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)} with existing show.
     */
    @Test
    public void testGetShowWithExistingShow() {
        final int id = generate(Integer.class);
        final Show show = mock(Show.class);
        when(entityManager.find(eq(Show.class), anyInt())).thenReturn(show);

        DeepAsserts.assertEquals(show, showDAO.getShow(id));

        verify(entityManager).find(Show.class, id);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)} with not existing show.
     */
    @Test
    public void testGetShowWithNotExistingShow() {
        when(entityManager.find(eq(Show.class), anyInt())).thenReturn(null);

        assertNull(showDAO.getShow(Integer.MAX_VALUE));

        verify(entityManager).find(Show.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)} with null argument.
     */
    @Test
    public void testGetShowWithNullArgument() {
        try {
            showDAO.getShow(null);
            fail("Can't get show with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#getShow(Integer)} with exception in persistence.
     */
    @Test
    public void testGetShowWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Show.class), anyInt());

        try {
            showDAO.getShow(Integer.MAX_VALUE);
            fail("Can't get show with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).find(Show.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#add(Show)}.
     */
    @Test
    public void testAdd() {
        final Show show = generate(Show.class);
        final int id = generate(Integer.class);
        doAnswer(setId(id)).when(entityManager).persist(any(Show.class));

        showDAO.add(show);
        DeepAsserts.assertEquals(id, show.getId());
        DeepAsserts.assertEquals(id - 1, show.getPosition());

        verify(entityManager).persist(show);
        verify(entityManager).merge(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#add(Show)} with null argument.
     */
    @Test
    public void testAddWithNullArgument() {
        try {
            showDAO.add(null);
            fail("Can't add show with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#add(Show)} with exception in persistence.
     */
    @Test
    public void testAddWithPersistenceException() {
        final Show show = generate(Show.class);
        doThrow(PersistenceException.class).when(entityManager).persist(any(Show.class));

        try {
            showDAO.add(show);
            fail("Can't add show with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).persist(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#update(Show)}.
     */
    @Test
    public void testUpdate() {
        final Show show = generate(Show.class);

        showDAO.update(show);

        verify(entityManager).merge(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#update(Show)} with null argument.
     */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            showDAO.update(null);
            fail("Can't update show with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#update(Show)} with exception in persistence.
     */
    @Test
    public void testUpdateWithPersistenceException() {
        final Show show = generate(Show.class);
        doThrow(PersistenceException.class).when(entityManager).merge(any(Show.class));

        try {
            showDAO.update(show);
            fail("Can't update show with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).merge(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#remove(Show)} with managed show.
     */
    @Test
    public void testRemoveWithManagedShow() {
        final Show show = generate(Show.class);
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
    public void testRemoveWithNotManagedShow() {
        final Show show = generate(Show.class);
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
    @Test
    public void testRemoveWithNullArgument() {
        try {
            showDAO.remove(null);
            fail("Can't remove show with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /**
     * Test method for {@link ShowDAO#remove(Show)} with exception in persistence.
     */
    @Test
    public void testRemoveWithPersistenceException() {
        final Show show = generate(Show.class);
        doThrow(PersistenceException.class).when(entityManager).contains(any(Show.class));

        try {
            showDAO.remove(show);
            fail("Can't remove show with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).contains(show);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                ((Show) invocation.getArguments()[0]).setId(id);
                return null;
            }

        };
    }

}
