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
import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link MovieDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieDAOImplTest extends ObjectGeneratorTest {

    /** Instance of {@link EntityManager} */
    @Mock
    private EntityManager entityManager;

    /** Query for movies */
    @Mock
    private TypedQuery<Movie> moviesQuery;

    /** Instance of {@link MovieDAO} */
    @InjectMocks
    private MovieDAO movieDAO = new MovieDAOImpl();

    /** Test method for {@link MovieDAO#getMovies()}. */
    @Test
    public void testGetMovies() {
        final List<Movie> movies = CollectionUtils.newList(generate(Movie.class), generate(Movie.class));
        when(entityManager.createNamedQuery(anyString(), eq(Movie.class))).thenReturn(moviesQuery);
        when(moviesQuery.getResultList()).thenReturn(movies);

        DeepAsserts.assertEquals(movies, movieDAO.getMovies());

        verify(entityManager).createNamedQuery(Movie.SELECT_MOVIES, Movie.class);
        verify(moviesQuery).getResultList();
        verifyNoMoreInteractions(entityManager, moviesQuery);
    }

    /** Test method for {@link MovieDAOImpl#getMovies()} with not set entity manager. */
    @Test(expected = IllegalStateException.class)
    public void testGetMoviesWithNotSetEntityManager() {
        ((MovieDAOImpl) movieDAO).setEntityManager(null);
        movieDAO.getMovies();
    }

    /** Test method for {@link MovieDAOImpl#getMovies()} with exception in persistence. */
    @Test
    public void testGetMoviesWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Movie.class));

        try {
            movieDAO.getMovies();
            fail("Can't get movies with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).createNamedQuery(Movie.SELECT_MOVIES, Movie.class);
        verifyNoMoreInteractions(entityManager);
        verifyZeroInteractions(moviesQuery);
    }

    /** Test method for {@link MovieDAO#getMovie(Integer)} with existing movie. */
    @Test
    public void testGetMovieWithExistingMovie() {
        final int id = generate(Integer.class);
        final Movie movie = mock(Movie.class);
        when(entityManager.find(eq(Movie.class), anyInt())).thenReturn(movie);

        DeepAsserts.assertEquals(movie, movieDAO.getMovie(id));

        verify(entityManager).find(Movie.class, id);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAO#getMovie(Integer)} with not existing movie. */
    @Test
    public void testGetMovieWithNotExistingMovie() {
        when(entityManager.find(eq(Movie.class), anyInt())).thenReturn(null);

        assertNull(movieDAO.getMovie(Integer.MAX_VALUE));

        verify(entityManager).find(Movie.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAOImpl#getMovie(Integer)} with not set entity manager. */
    @Test(expected = IllegalStateException.class)
    public void testGetMovieWithNotSetEntityManager() {
        ((MovieDAOImpl) movieDAO).setEntityManager(null);
        movieDAO.getMovie(Integer.MAX_VALUE);
    }

    /** Test method for {@link MovieDAO#getMovie(Integer)} with null argument. */
    @Test
    public void testGetMovieWithNullArgument() {
        try {
            movieDAO.getMovie(null);
            fail("Can't get movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link MovieDAOImpl#getMovie(Integer)} with exception in persistence. */
    @Test
    public void testGetMovieWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Movie.class), anyInt());

        try {
            movieDAO.getMovie(Integer.MAX_VALUE);
            fail("Can't get movie with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).find(Movie.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAO#add(Movie)}. */
    @Test
    public void testAdd() {
        final Movie movie = generate(Movie.class);
        final int id = generate(Integer.class);
        doAnswer(setId(id)).when(entityManager).persist(any(Movie.class));

        movieDAO.add(movie);
        DeepAsserts.assertEquals(id, movie.getId());
        DeepAsserts.assertEquals(id - 1, movie.getPosition());

        verify(entityManager).persist(movie);
        verify(entityManager).merge(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAOImpl#add(Movie)} with not set entity manager. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetEntityManager() {
        ((MovieDAOImpl) movieDAO).setEntityManager(null);
        movieDAO.add(mock(Movie.class));
    }

    /** Test method for {@link MovieDAO#add(Movie)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        try {
            movieDAO.add(null);
            fail("Can't add movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link MovieDAOImpl#add(Movie)} with exception in persistence. */
    @Test
    public void testAddWithPersistenceException() {
        final Movie movie = generate(Movie.class);
        doThrow(PersistenceException.class).when(entityManager).persist(any(Movie.class));

        try {
            movieDAO.add(movie);
            fail("Can't add movie with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).persist(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAO#update(Movie)}. */
    @Test
    public void testUpdate() {
        final Movie movie = generate(Movie.class);

        movieDAO.update(movie);

        verify(entityManager).merge(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAOImpl#update(Movie)} with not set entity manager. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetEntityManager() {
        ((MovieDAOImpl) movieDAO).setEntityManager(null);
        movieDAO.update(mock(Movie.class));
    }

    /** Test method for {@link MovieDAO#update(Movie)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            movieDAO.update(null);
            fail("Can't update movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link MovieDAOImpl#update(Movie)} with exception in persistence. */
    @Test
    public void testUpdateWithPersistenceException() {
        final Movie movie = generate(Movie.class);
        doThrow(PersistenceException.class).when(entityManager).merge(any(Movie.class));

        try {
            movieDAO.update(movie);
            fail("Can't update movie with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).merge(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAO#remove(Movie)} with managed movie. */
    @Test
    public void testRemoveWithManagedMovie() {
        final Movie movie = generate(Movie.class);
        when(entityManager.contains(any(Movie.class))).thenReturn(true);

        movieDAO.remove(movie);

        verify(entityManager).contains(movie);
        verify(entityManager).remove(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAO#remove(Movie)} with not managed movie. */
    @Test
    public void testRemoveWithNotManagedMovie() {
        final Movie movie = generate(Movie.class);
        when(entityManager.contains(any(Movie.class))).thenReturn(false);
        when(entityManager.getReference(eq(Movie.class), anyInt())).thenReturn(movie);

        movieDAO.remove(movie);

        verify(entityManager).contains(movie);
        verify(entityManager).getReference(Movie.class, movie.getId());
        verify(entityManager).remove(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link MovieDAOImpl#remove(Movie)} with not set entity manager. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetEntityManager() {
        ((MovieDAOImpl) movieDAO).setEntityManager(null);
        movieDAO.remove(mock(Movie.class));
    }

    /** Test method for {@link MovieDAO#remove(Movie)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            movieDAO.remove(null);
            fail("Can't remove movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link MovieDAOImpl#remove(Movie)} with exception in persistence. */
    @Test
    public void testRemoveWithPersistenceException() {
        final Movie movie = generate(Movie.class);
        doThrow(PersistenceException.class).when(entityManager).contains(any(Movie.class));

        try {
            movieDAO.remove(movie);
            fail("Can't remove movie with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).contains(movie);
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
                ((Movie) invocation.getArguments()[0]).setId(id);
                return null;
            }

        };
    }

}
