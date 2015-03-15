package cz.vhromada.catalog.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.MovieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * A class represents test for class {@link MovieServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieServiceImplTest extends ObjectGeneratorTest {

    /**
     * Cache key for list of movies
     */
    private static final String MOVIES_CACHE_KEY = "movies";

    /**
     * Cache key for movie
     */
    private static final String MOVIE_CACHE_KEY = "movie";

    /**
     * Instance of {@link MovieDAO}
     */
    @Mock
    private MovieDAO movieDAO;

    /**
     * Instance of {@link Cache}
     */
    @Mock
    private Cache movieCache;

    /**
     * Instance of {@link MovieService}
     */
    private MovieService movieService;

    /**
     * Initializes service for movies.
     */
    @Before
    public void setUp() {
        movieService = new MovieServiceImpl(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieServiceImpl#MovieServiceImpl(MovieDAO, Cache)} with null DAO for movies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMovieDAO() {
        new MovieServiceImpl(null, movieCache);
    }

    /**
     * Test method for {@link MovieServiceImpl#MovieServiceImpl(MovieDAO, Cache)} with null cache for movies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMovieCache() {
        new MovieServiceImpl(movieDAO, null);
    }

    /**
     * Test method for {@link MovieService#newData()} with cached movies.
     */
    @Test
    public void testNewDataWithCachedMovies() {
        final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

        movieService.newData();

        for (final Movie movie : movies) {
            verify(movieDAO).remove(movie);
        }
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#newData()} with not cached movies.
     */
    @Test
    public void testNewDataWithNotCachedMovies() {
        final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
        when(movieDAO.getMovies()).thenReturn(movies);
        when(movieCache.get(anyString())).thenReturn(null);

        movieService.newData();

        verify(movieDAO).getMovies();
        for (final Movie movie : movies) {
            verify(movieDAO).remove(movie);
        }
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#newData()} with exception in DAO tier.
     */
    @Test
    public void testNewDataWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).getMovies();
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.newData();
            fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#getMovies()} with cached movies.
     */
    @Test
    public void testGetMoviesWithCachedMovies() {
        final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

        DeepAsserts.assertEquals(movies, movieService.getMovies());

        verify(movieCache).get(MOVIES_CACHE_KEY);
        verifyNoMoreInteractions(movieCache);
        verifyZeroInteractions(movieDAO);
    }

    /**
     * Test method for {@link MovieService#getMovies()} with not cached movies.
     */
    @Test
    public void testGetMoviesWithNotCachedMovies() {
        final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
        when(movieDAO.getMovies()).thenReturn(movies);
        when(movieCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(movies, movieService.getMovies());

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).put(MOVIES_CACHE_KEY, movies);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#getMovies()} with exception in DAO tier.
     */
    @Test
    public void testGetMoviesWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).getMovies();
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.getMovies();
            fail("Can't get movies with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#getMovie(Integer)} with cached existing movie.
     */
    @Test
    public void testGetMovieWithCachedExistingMovie() {
        final Movie movie = generate(Movie.class);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movie));

        DeepAsserts.assertEquals(movie, movieService.getMovie(movie.getId()));

        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verifyNoMoreInteractions(movieCache);
        verifyZeroInteractions(movieDAO);
    }

    /**
     * Test method for {@link MovieService#getMovie(Integer)} with cached not existing movie.
     */
    @Test
    public void testGetMovieWithCachedNotExistingMovie() {
        final Movie movie = generate(Movie.class);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertNull(movieService.getMovie(movie.getId()));

        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verifyNoMoreInteractions(movieCache);
        verifyZeroInteractions(movieDAO);
    }

    /**
     * Test method for {@link MovieService#getMovie(Integer)} with not cached existing movie.
     */
    @Test
    public void testGetMovieWithNotCachedExistingMovie() {
        final Movie movie = generate(Movie.class);
        when(movieDAO.getMovie(anyInt())).thenReturn(movie);
        when(movieCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(movie, movieService.getMovie(movie.getId()));

        verify(movieDAO).getMovie(movie.getId());
        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verify(movieCache).put(MOVIE_CACHE_KEY + movie.getId(), movie);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#getMovie(Integer)} with not cached not existing movie.
     */
    @Test
    public void testGetMovieWithNotCachedNotExistingMovie() {
        final Movie movie = generate(Movie.class);
        when(movieDAO.getMovie(anyInt())).thenReturn(null);
        when(movieCache.get(anyString())).thenReturn(null);

        assertNull(movieService.getMovie(movie.getId()));

        verify(movieDAO).getMovie(movie.getId());
        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verify(movieCache).put(MOVIE_CACHE_KEY + movie.getId(), null);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#getMovie(Integer)} with null argument.
     */
    @Test
    public void testGetMovieWithNullArgument() {
        try {
            movieService.getMovie(null);
            fail("Can't get movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#getMovie(Integer)} with exception in DAO tier.
     */
    @Test
    public void testGetMovieWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).getMovie(anyInt());
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.getMovie(Integer.MAX_VALUE);
            fail("Can't get movie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovie(Integer.MAX_VALUE);
        verify(movieCache).get(MOVIE_CACHE_KEY + Integer.MAX_VALUE);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#add(Movie)} with cached movies.
     */
    @Test
    public void testAddWithCachedMovies() {
        final Movie movie = generate(Movie.class);
        final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
        final List<Movie> moviesList = new ArrayList<>(movies);
        moviesList.add(movie);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

        movieService.add(movie);

        verify(movieDAO).add(movie);
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verify(movieCache).put(MOVIES_CACHE_KEY, moviesList);
        verify(movieCache).put(MOVIE_CACHE_KEY + movie.getId(), movie);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#add(Movie)} with not cached movies.
     */
    @Test
    public void testAddWithNotCachedMovies() {
        final Movie movie = generate(Movie.class);
        when(movieCache.get(anyString())).thenReturn(null);

        movieService.add(movie);

        verify(movieDAO).add(movie);
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#add(Movie)} with null argument.
     */
    @Test
    public void testAddWithNullArgument() {
        try {
            movieService.add(null);
            fail("Can't add movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#add(Movie)} with exception in DAO tier.
     */
    @Test
    public void testAddWithDAOTierException() {
        final Movie movie = generate(Movie.class);
        doThrow(DataStorageException.class).when(movieDAO).add(any(Movie.class));

        try {
            movieService.add(movie);
            fail("Can't add movie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).add(movie);
        verifyNoMoreInteractions(movieDAO);
        verifyZeroInteractions(movieCache);
    }

    /**
     * Test method for {@link MovieService#update(Movie)}.
     */
    @Test
    public void testUpdate() {
        final Movie movie = generate(Movie.class);

        movieService.update(movie);

        verify(movieDAO).update(movie);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#update(Movie)} with null argument.
     */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            movieService.update(null);
            fail("Can't update movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#update(Movie)} with exception in DAO tier.
     */
    @Test
    public void testUpdateWithDAOTierException() {
        final Movie movie = generate(Movie.class);
        doThrow(DataStorageException.class).when(movieDAO).update(any(Movie.class));

        try {
            movieService.update(movie);
            fail("Can't update movie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).update(movie);
        verifyNoMoreInteractions(movieDAO);
        verifyZeroInteractions(movieCache);
    }

    /**
     * Test method for {@link MovieService#remove(Movie)} with cached movies.
     */
    @Test
    public void testRemoveWithCachedMovies() {
        final Movie movie = generate(Movie.class);
        final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
        final List<Movie> moviesList = new ArrayList<>(movies);
        moviesList.add(movie);
        when(movieCache.get(MOVIES_CACHE_KEY)).thenReturn(new SimpleValueWrapper(moviesList));
        when(movieCache.get(MOVIE_CACHE_KEY + movie.getId())).thenReturn(new SimpleValueWrapper(movie));

        movieService.remove(movie);

        verify(movieDAO).remove(movie);
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).put(MOVIES_CACHE_KEY, movies);
        verify(movieCache).evict(movie.getId());
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#remove(Movie)} with not cached movies.
     */
    @Test
    public void testRemoveWithNotCachedMovies() {
        final Movie movie = generate(Movie.class);
        when(movieCache.get(anyString())).thenReturn(null);

        movieService.remove(movie);

        verify(movieDAO).remove(movie);
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).evict(movie.getId());
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#remove(Movie)} with null argument.
     */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            movieService.remove(null);
            fail("Can't remove movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#remove(Movie)} with exception in DAO tier.
     */
    @Test
    public void testRemoveWithDAOTierException() {
        final Movie movie = generate(Movie.class);
        doThrow(DataStorageException.class).when(movieDAO).remove(any(Movie.class));

        try {
            movieService.remove(movie);
            fail("Can't remove movie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).remove(movie);
        verifyNoMoreInteractions(movieDAO);
        verifyZeroInteractions(movieCache);
    }

    /**
     * Test method for {@link MovieService#duplicate(Movie)} with cached movies.
     */
    @Test
    public void testDuplicateWithCachedMovies() {
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(CollectionUtils.newList(mock(Movie.class), mock(Movie.class))));

        movieService.duplicate(generate(Movie.class));

        verify(movieDAO).add(any(Movie.class));
        verify(movieDAO).update(any(Movie.class));
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).get(MOVIE_CACHE_KEY + null);
        verify(movieCache).put(eq(MOVIES_CACHE_KEY), anyListOf(Movie.class));
        verify(movieCache).put(eq(MOVIE_CACHE_KEY + null), any(Movie.class));
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#duplicate(Movie)} with not cached movies.
     */
    @Test
    public void testDuplicateWithNotCachedMovies() {
        when(movieCache.get(anyString())).thenReturn(null);

        movieService.duplicate(generate(Movie.class));

        verify(movieDAO).add(any(Movie.class));
        verify(movieDAO).update(any(Movie.class));
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).get(MOVIE_CACHE_KEY + null);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#duplicate(Movie)} with null argument.
     */
    @Test
    public void testDuplicateWithNullArgument() {
        try {
            movieService.duplicate(null);
            fail("Can't duplicate movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#duplicate(Movie)} with exception in DAO tier.
     */
    @Test
    public void testDuplicateWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).add(any(Movie.class));

        try {
            movieService.duplicate(generate(Movie.class));
            fail("Can't duplicate movie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).add(any(Movie.class));
        verifyNoMoreInteractions(movieDAO);
        verifyZeroInteractions(movieCache);
    }

    /**
     * Test method for {@link MovieService#moveUp(Movie)} with cached movies.
     */
    @Test
    public void testMoveUpWithCachedMovies() {
        final Movie movie1 = generate(Movie.class);
        final int position1 = movie1.getPosition();
        final Movie movie2 = generate(Movie.class);
        final int position2 = movie2.getPosition();
        final List<Movie> movies = CollectionUtils.newList(movie1, movie2);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

        movieService.moveUp(movie2);
        DeepAsserts.assertEquals(position2, movie1.getPosition());
        DeepAsserts.assertEquals(position1, movie2.getPosition());

        verify(movieDAO).update(movie1);
        verify(movieDAO).update(movie2);
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#moveUp(Movie)} with not cached movies.
     */
    @Test
    public void testMoveUpWithNotCachedMovies() {
        final Movie movie1 = generate(Movie.class);
        final int position1 = movie1.getPosition();
        final Movie movie2 = generate(Movie.class);
        final int position2 = movie2.getPosition();
        final List<Movie> movies = CollectionUtils.newList(movie1, movie2);
        when(movieDAO.getMovies()).thenReturn(movies);
        when(movieCache.get(anyString())).thenReturn(null);

        movieService.moveUp(movie2);
        DeepAsserts.assertEquals(position2, movie1.getPosition());
        DeepAsserts.assertEquals(position1, movie2.getPosition());

        verify(movieDAO).update(movie1);
        verify(movieDAO).update(movie2);
        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#moveUp(Movie)} with null argument.
     */
    @Test
    public void testMoveUpWithNullArgument() {
        try {
            movieService.moveUp(null);
            fail("Can't move up movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#moveUp(Movie)} with exception in DAO tier.
     */
    @Test
    public void testMoveUpWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).getMovies();
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.moveUp(generate(Movie.class));
            fail("Can't move up movie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#moveDown(Movie)} with cached movies.
     */
    @Test
    public void testMoveDownWithCachedMovies() {
        final Movie movie1 = generate(Movie.class);
        final int position1 = movie1.getPosition();
        final Movie movie2 = generate(Movie.class);
        final int position2 = movie2.getPosition();
        final List<Movie> movies = CollectionUtils.newList(movie1, movie2);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

        movieService.moveDown(movie1);
        DeepAsserts.assertEquals(position2, movie1.getPosition());
        DeepAsserts.assertEquals(position1, movie2.getPosition());

        verify(movieDAO).update(movie1);
        verify(movieDAO).update(movie2);
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#moveDown(Movie)} with not cached movies.
     */
    @Test
    public void testMoveDownWithNotCachedMovies() {
        final Movie movie1 = generate(Movie.class);
        final int position1 = movie1.getPosition();
        final Movie movie2 = generate(Movie.class);
        final int position2 = movie2.getPosition();
        final List<Movie> movies = CollectionUtils.newList(movie1, movie2);
        when(movieDAO.getMovies()).thenReturn(movies);
        when(movieCache.get(anyString())).thenReturn(null);

        movieService.moveDown(movie1);
        DeepAsserts.assertEquals(position2, movie1.getPosition());
        DeepAsserts.assertEquals(position1, movie2.getPosition());

        verify(movieDAO).update(movie1);
        verify(movieDAO).update(movie2);
        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#moveDown(Movie)} with null argument.
     */
    @Test
    public void testMoveDownWithNullArgument() {
        try {
            movieService.moveDown(null);
            fail("Can't move down movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#moveDown(Movie)} with exception in DAO tier.
     */
    @Test
    public void testMoveDownWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).getMovies();
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.moveDown(generate(Movie.class));
            fail("Can't move down movie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#exists(Movie)} with cached existing movie.
     */
    @Test
    public void testExistsWithCachedExistingMovie() {
        final Movie movie = generate(Movie.class);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movie));

        assertTrue(movieService.exists(movie));

        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verifyNoMoreInteractions(movieCache);
        verifyZeroInteractions(movieDAO);
    }

    /**
     * Test method for {@link MovieService#exists(Movie)} with cached not existing movie.
     */
    @Test
    public void testExistsWithCachedNotExistingMovie() {
        final Movie movie = generate(Movie.class);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertFalse(movieService.exists(movie));

        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verifyNoMoreInteractions(movieCache);
        verifyZeroInteractions(movieDAO);
    }

    /**
     * Test method for {@link MovieService#exists(Movie)} with not cached existing movie.
     */
    @Test
    public void testExistsWithNotCachedExistingMovie() {
        final Movie movie = generate(Movie.class);
        when(movieDAO.getMovie(anyInt())).thenReturn(movie);
        when(movieCache.get(anyString())).thenReturn(null);

        assertTrue(movieService.exists(movie));

        verify(movieDAO).getMovie(movie.getId());
        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verify(movieCache).put(MOVIE_CACHE_KEY + movie.getId(), movie);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#exists(Movie)} with not cached not existing movie.
     */
    @Test
    public void testExistsWithNotCachedNotExistingMovie() {
        final Movie movie = generate(Movie.class);
        when(movieDAO.getMovie(anyInt())).thenReturn(null);
        when(movieCache.get(anyString())).thenReturn(null);

        assertFalse(movieService.exists(movie));

        verify(movieDAO).getMovie(movie.getId());
        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verify(movieCache).put(MOVIE_CACHE_KEY + movie.getId(), null);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#exists(Movie)} with null argument.
     */
    @Test
    public void testExistsWithNullArgument() {
        try {
            movieService.exists(null);
            fail("Can't exists movie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#exists(Movie)} with exception in DAO tier.
     */
    @Test
    public void testExistsWithDAOTierException() {
        final Movie movie = generate(Movie.class);
        doThrow(DataStorageException.class).when(movieDAO).getMovie(anyInt());
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.exists(movie);
            fail("Can't exists movie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovie(movie.getId());
        verify(movieCache).get(MOVIE_CACHE_KEY + movie.getId());
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#updatePositions()} with cached movies.
     */
    @Test
    public void testUpdatePositionsWithCachedMovies() {
        final List<Movie> movies = CollectionUtils.newList(generate(Movie.class), generate(Movie.class));
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

        movieService.updatePositions();

        for (int i = 0; i < movies.size(); i++) {
            final Movie movie = movies.get(i);
            DeepAsserts.assertEquals(i, movie.getPosition());
            verify(movieDAO).update(movie);
        }
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#updatePositions()} with not cached movies.
     */
    @Test
    public void testUpdatePositionsWithNotCachedMovies() {
        final List<Movie> movies = CollectionUtils.newList(generate(Movie.class), generate(Movie.class));
        when(movieDAO.getMovies()).thenReturn(movies);
        when(movieCache.get(anyString())).thenReturn(null);

        movieService.updatePositions();

        verify(movieDAO).getMovies();
        for (int i = 0; i < movies.size(); i++) {
            final Movie movie = movies.get(i);
            DeepAsserts.assertEquals(i, movie.getPosition());
            verify(movieDAO).update(movie);
        }
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).clear();
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#updatePositions()} with exception in DAO tier.
     */
    @Test
    public void testUpdatePositionsWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).getMovies();
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.updatePositions();
            fail("Can't update positions with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#getTotalMediaCount()} with cached movies.
     */
    @Test
    public void testGetTotalMediaCountWithCachedMovies() {
        final Movie movie1 = mock(Movie.class);
        final Movie movie2 = mock(Movie.class);
        final Movie movie3 = mock(Movie.class);
        final List<Movie> movies = CollectionUtils.newList(movie1, movie2, movie3);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));
        when(movie1.getMedia()).thenReturn(CollectionUtils.newList(mock(Medium.class)));
        when(movie2.getMedia()).thenReturn(CollectionUtils.newList(mock(Medium.class), mock(Medium.class)));
        when(movie3.getMedia()).thenReturn(CollectionUtils.newList(mock(Medium.class), mock(Medium.class), mock(Medium.class)));

        DeepAsserts.assertEquals(6, movieService.getTotalMediaCount());

        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movie1).getMedia();
        verify(movie2).getMedia();
        verify(movie3).getMedia();
        verifyNoMoreInteractions(movieCache, movie1, movie2, movie3);
        verifyZeroInteractions(movieDAO);
    }

    /**
     * Test method for {@link MovieService#getTotalMediaCount()} with not cached movies.
     */
    @Test
    public void testGetTotalMediaCountWithNotCachedMovies() {
        final Movie movie1 = mock(Movie.class);
        final Movie movie2 = mock(Movie.class);
        final Movie movie3 = mock(Movie.class);
        final List<Movie> movies = CollectionUtils.newList(movie1, movie2, movie3);
        when(movieDAO.getMovies()).thenReturn(movies);
        when(movieCache.get(anyString())).thenReturn(null);
        when(movie1.getMedia()).thenReturn(CollectionUtils.newList(mock(Medium.class)));
        when(movie2.getMedia()).thenReturn(CollectionUtils.newList(mock(Medium.class), mock(Medium.class)));
        when(movie3.getMedia()).thenReturn(CollectionUtils.newList(mock(Medium.class), mock(Medium.class), mock(Medium.class)));

        DeepAsserts.assertEquals(6, movieService.getTotalMediaCount());

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).put(MOVIES_CACHE_KEY, movies);
        verify(movie1).getMedia();
        verify(movie2).getMedia();
        verify(movie3).getMedia();
        verifyNoMoreInteractions(movieDAO, movieCache, movie1, movie2, movie3);
    }

    /**
     * Test method for {@link MovieService#getTotalMediaCount()} with exception in DAO tier.
     */
    @Test
    public void testGetTotalMediaCountWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).getMovies();
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.getTotalMediaCount();
            fail("Can't get total media count with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    /**
     * Test method for {@link MovieService#getTotalLength()} with cached movies.
     */
    @Test
    public void testGetTotalLengthWithCachedMovies() {
        final Movie movie1 = mock(Movie.class);
        final Movie movie2 = mock(Movie.class);
        final Movie movie3 = mock(Movie.class);
        final Medium medium1 = mock(Medium.class);
        final Medium medium2 = mock(Medium.class);
        final Medium medium3 = mock(Medium.class);
        final Medium medium4 = mock(Medium.class);
        final Medium medium5 = mock(Medium.class);
        final Medium medium6 = mock(Medium.class);
        final List<Movie> movies = CollectionUtils.newList(movie1, movie2, movie3);
        final int[] lengths = getLengths();
        final int totalLength = getTotalLength(lengths);
        when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));
        when(movie1.getMedia()).thenReturn(CollectionUtils.newList(medium1));
        when(movie2.getMedia()).thenReturn(CollectionUtils.newList(medium2, medium3));
        when(movie3.getMedia()).thenReturn(CollectionUtils.newList(medium4, medium5, medium6));
        when(medium1.getLength()).thenReturn(lengths[0]);
        when(medium2.getLength()).thenReturn(lengths[1]);
        when(medium3.getLength()).thenReturn(lengths[2]);
        when(medium4.getLength()).thenReturn(lengths[3]);
        when(medium5.getLength()).thenReturn(lengths[4]);
        when(medium6.getLength()).thenReturn(lengths[5]);

        DeepAsserts.assertEquals(new Time(totalLength), movieService.getTotalLength());

        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movie1).getMedia();
        verify(movie2).getMedia();
        verify(movie3).getMedia();
        verifyNoMoreInteractions(movieCache, movie1, movie2, movie3);
        verifyZeroInteractions(movieDAO);
    }

    /**
     * Test method for {@link MovieService#getTotalLength()} with not cached movies.
     */
    @Test
    public void testGetTotalLengthWithNotCachedMovies() {
        final Movie movie1 = mock(Movie.class);
        final Movie movie2 = mock(Movie.class);
        final Movie movie3 = mock(Movie.class);
        final Medium medium1 = mock(Medium.class);
        final Medium medium2 = mock(Medium.class);
        final Medium medium3 = mock(Medium.class);
        final Medium medium4 = mock(Medium.class);
        final Medium medium5 = mock(Medium.class);
        final Medium medium6 = mock(Medium.class);
        final List<Movie> movies = CollectionUtils.newList(movie1, movie2, movie3);
        final int[] lengths = getLengths();
        final int totalLength = getTotalLength(lengths);
        when(movieDAO.getMovies()).thenReturn(movies);
        when(movieCache.get(anyString())).thenReturn(null);
        when(movie1.getMedia()).thenReturn(CollectionUtils.newList(medium1));
        when(movie2.getMedia()).thenReturn(CollectionUtils.newList(medium2, medium3));
        when(movie3.getMedia()).thenReturn(CollectionUtils.newList(medium4, medium5, medium6));
        when(medium1.getLength()).thenReturn(lengths[0]);
        when(medium2.getLength()).thenReturn(lengths[1]);
        when(medium3.getLength()).thenReturn(lengths[2]);
        when(medium4.getLength()).thenReturn(lengths[3]);
        when(medium5.getLength()).thenReturn(lengths[4]);
        when(medium6.getLength()).thenReturn(lengths[5]);

        DeepAsserts.assertEquals(new Time(totalLength), movieService.getTotalLength());

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verify(movieCache).put(MOVIES_CACHE_KEY, movies);
        verify(movie1).getMedia();
        verify(movie2).getMedia();
        verify(movie3).getMedia();
        verifyNoMoreInteractions(movieDAO, movieCache, movie1, movie2, movie3);
    }

    /**
     * Test method for {@link MovieService#getTotalLength()} with exception in DAO tier.
     */
    @Test
    public void testGetTotalLengthWithDAOTierException() {
        doThrow(DataStorageException.class).when(movieDAO).getMovies();
        when(movieCache.get(anyString())).thenReturn(null);

        try {
            movieService.getTotalLength();
            fail("Can't get total length with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(movieDAO).getMovies();
        verify(movieCache).get(MOVIES_CACHE_KEY);
        verifyNoMoreInteractions(movieDAO, movieCache);
    }

    private int[] getLengths() {
        final int[] lengths = new int[6];
        for (int i = 0; i < 6; i++) {
            final int length = generate(Integer.class);
            lengths[i] = length;
        }

        return lengths;
    }

    private static int getTotalLength(final int[] lengths) {
        int totalLength = 0;
        for (final int length : lengths) {
            totalLength += length;
        }

        return totalLength;
    }

}
