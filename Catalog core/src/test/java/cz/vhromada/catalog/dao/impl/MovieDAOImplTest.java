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
import cz.vhromada.catalog.commons.MovieUtils;
import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link MovieDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for movies
     */
    @Mock
    private TypedQuery<Movie> moviesQuery;

    /**
     * Instance of {@link MovieDAO}
     */
    private MovieDAO movieDAO;

    /**
     * Initializes DAO for movies.
     */
    @Before
    public void setUp() {
        movieDAO = new MovieDAOImpl(entityManager);
    }

    /**
     * Test method for {@link MovieDAOImpl#MovieDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new MovieDAOImpl(null);
    }

    /**
     * Test method for {@link MovieDAO#getMovies()}.
     */
    @Test
    public void testGetMovies() {
        when(entityManager.createNamedQuery(anyString(), eq(Movie.class))).thenReturn(moviesQuery);
        when(moviesQuery.getResultList()).thenReturn(CollectionUtils.newList(MovieUtils.newMovie(MovieUtils.ID), MovieUtils.newMovie(2)));

        final List<Movie> movies = movieDAO.getMovies();

        MovieUtils.assertMoviesDeepEquals(CollectionUtils.newList(MovieUtils.newMovie(MovieUtils.ID), MovieUtils.newMovie(2)), movies);

        verify(entityManager).createNamedQuery(Movie.SELECT_MOVIES, Movie.class);
        verify(moviesQuery).getResultList();
        verifyNoMoreInteractions(entityManager, moviesQuery);
    }

    /**
     * Test method for {@link MovieDAO#getMovies()} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetMovies_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Movie.class));

        movieDAO.getMovies();
    }

    /**
     * Test method for {@link MovieDAO#getMovie(Integer)} with existing movie.
     */
    @Test
    public void testGetMovie_ExistingMovie() {
        when(entityManager.find(eq(Movie.class), anyInt())).thenReturn(MovieUtils.newMovie(MovieUtils.ID));

        final Movie movie = movieDAO.getMovie(MovieUtils.ID);

        MovieUtils.assertMovieDeepEquals(MovieUtils.newMovie(MovieUtils.ID), movie);

        verify(entityManager).find(Movie.class, MovieUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MovieDAO#getMovie(Integer)} with not existing movie.
     */
    @Test
    public void testGetMovie_NotExistingMovie() {
        when(entityManager.find(eq(Movie.class), anyInt())).thenReturn(null);

        final Movie movie = movieDAO.getMovie(Integer.MAX_VALUE);

        assertNull(movie);

        verify(entityManager).find(Movie.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MovieDAO#getMovie(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMovie_NullArgument() {
        movieDAO.getMovie(null);
    }

    /**
     * Test method for {@link MovieDAO#getMovie(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetMovie_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Movie.class), anyInt());

        movieDAO.getMovie(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link MovieDAO#add(Movie)}.
     */
    @Test
    public void testAdd() {
        final Movie movie = MovieUtils.newMovie(MovieUtils.ID);
        doAnswer(setId(MovieUtils.ID)).when(entityManager).persist(any(Movie.class));

        movieDAO.add(movie);

        assertEquals(MovieUtils.ID, movie.getId());
        assertEquals(MovieUtils.ID - 1, movie.getPosition());

        verify(entityManager).persist(movie);
        verify(entityManager).merge(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MovieDAO#add(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        movieDAO.add(null);
    }

    /**
     * Test method for {@link MovieDAO#add(Movie)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Movie.class));

        movieDAO.add(MovieUtils.newMovie(MovieUtils.ID));
    }

    /**
     * Test method for {@link MovieDAO#update(Movie)}.
     */
    @Test
    public void testUpdate() {
        final Movie movie = MovieUtils.newMovie(MovieUtils.ID);

        movieDAO.update(movie);

        verify(entityManager).merge(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MovieDAO#update(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        movieDAO.update(null);
    }

    /**
     * Test method for {@link MovieDAO#update(Movie)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Movie.class));

        movieDAO.update(MovieUtils.newMovie(MovieUtils.ID));
    }

    /**
     * Test method for {@link MovieDAO#remove(Movie)} with managed movie.
     */
    @Test
    public void testRemove_ManagedMovie() {
        final Movie movie = MovieUtils.newMovie(MovieUtils.ID);
        when(entityManager.contains(any(Movie.class))).thenReturn(true);

        movieDAO.remove(movie);

        verify(entityManager).contains(movie);
        verify(entityManager).remove(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MovieDAO#remove(Movie)} with not managed movie.
     */
    @Test
    public void testRemove_NotManagedMovie() {
        final Movie movie = MovieUtils.newMovie(MovieUtils.ID);
        when(entityManager.contains(any(Movie.class))).thenReturn(false);
        when(entityManager.getReference(eq(Movie.class), anyInt())).thenReturn(movie);

        movieDAO.remove(movie);

        verify(entityManager).contains(movie);
        verify(entityManager).getReference(Movie.class, movie.getId());
        verify(entityManager).remove(movie);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MovieDAO#remove(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        movieDAO.remove(null);
    }

    /**
     * Test method for {@link MovieDAO#remove(Movie)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Movie.class));

        movieDAO.remove(MovieUtils.newMovie(MovieUtils.ID));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Movie) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
