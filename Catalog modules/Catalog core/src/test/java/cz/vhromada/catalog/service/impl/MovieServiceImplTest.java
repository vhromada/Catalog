package cz.vhromada.catalog.service.impl;

import static cz.vhromada.catalog.commons.TestConstants.MOVE_POSITION;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_ID;
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
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.MovieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
public class MovieServiceImplTest {

	/** Instance of {@link MovieDAO} */
	@Mock
	private MovieDAO movieDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache movieCache;

	/** Instance of {@link MovieService} */
	@InjectMocks
	private MovieService movieService = new MovieServiceImpl();

	/** Test method for {@link MovieServiceImpl#getMovieDAO()} and {@link MovieServiceImpl#setMovieDAO(MovieDAO)}. */
	@Test
	public void testMovieDAO() {
		final MovieServiceImpl movieService = new MovieServiceImpl();
		movieService.setMovieDAO(movieDAO);
		DeepAsserts.assertEquals(movieDAO, movieService.getMovieDAO());
	}

	/** Test method for {@link MovieServiceImpl#getMovieCache()} and {@link MovieServiceImpl#setMovieCache(Cache)}. */
	@Test
	public void testMovieCache() {
		final MovieServiceImpl movieService = new MovieServiceImpl();
		movieService.setMovieCache(movieCache);
		DeepAsserts.assertEquals(movieCache, movieService.getMovieCache());
	}

	/** Test method for {@link MovieService#newData()} with cached movies. */
	@Test
	public void testNewDataWithCachedMovies() {
		final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

		movieService.newData();

		for (Movie movie : movies) {
			verify(movieDAO).remove(movie);
		}
		verify(movieCache).get("movies");
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#newData()} with not cached movies. */
	@Test
	public void testNewDataWithNotCachedMovies() {
		final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
		when(movieDAO.getMovies()).thenReturn(movies);
		when(movieCache.get(anyString())).thenReturn(null);

		movieService.newData();

		verify(movieDAO).getMovies();
		for (Movie movie : movies) {
			verify(movieDAO).remove(movie);
		}
		verify(movieCache).get("movies");
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#newData()} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.newData();
	}

	/** Test method for {@link MovieService#newData()} with not set movie cache. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.newData();
	}

	/** Test method for {@link MovieService#newData()} with exception in DAO tier. */
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
		verify(movieCache).get("movies");
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#getMovies()} with cached movies. */
	@Test
	public void testGetMoviesWithCachedMovies() {
		final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

		DeepAsserts.assertEquals(movies, movieService.getMovies());

		verify(movieCache).get("movies");
		verifyNoMoreInteractions(movieCache);
		verifyZeroInteractions(movieDAO);
	}

	/** Test method for {@link MovieService#getMovies()} with not cached movies. */
	@Test
	public void testGetMoviesWithNotCachedMovies() {
		final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
		when(movieDAO.getMovies()).thenReturn(movies);
		when(movieCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(movies, movieService.getMovies());

		verify(movieDAO).getMovies();
		verify(movieCache).get("movies");
		verify(movieCache).put("movies", movies);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#getMovies()} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testGetMoviesWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.getMovies();
	}

	/** Test method for {@link MovieService#getMovies()} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testGetMoviesWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.getMovies();
	}

	/** Test method for {@link MovieService#getMovies()} with exception in DAO tier. */
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
		verify(movieCache).get("movies");
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with cached existing movie. */
	@Test
	public void testGetMovieWithCachedExistingMovie() {
		final Movie movie = EntityGenerator.createMovie(PRIMARY_ID);
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movie));

		DeepAsserts.assertEquals(movie, movieService.getMovie(PRIMARY_ID));

		verify(movieCache).get("movie" + PRIMARY_ID);
		verifyNoMoreInteractions(movieCache);
		verifyZeroInteractions(movieDAO);
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with cached not existing movie. */
	@Test
	public void testGetMovieWithCachedNotExistingMovie() {
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(movieService.getMovie(PRIMARY_ID));

		verify(movieCache).get("movie" + PRIMARY_ID);
		verifyNoMoreInteractions(movieCache);
		verifyZeroInteractions(movieDAO);
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with not cached existing movie. */
	@Test
	public void testGetMovieWithNotCachedExistingMovie() {
		final Movie movie = EntityGenerator.createMovie(PRIMARY_ID);
		when(movieDAO.getMovie(anyInt())).thenReturn(movie);
		when(movieCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(movie, movieService.getMovie(PRIMARY_ID));

		verify(movieDAO).getMovie(PRIMARY_ID);
		verify(movieCache).get("movie" + PRIMARY_ID);
		verify(movieCache).put("movie" + PRIMARY_ID, movie);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with not cached not existing movie. */
	@Test
	public void testGetMovieWithNotCachedNotExistingMovie() {
		when(movieDAO.getMovie(anyInt())).thenReturn(null);
		when(movieCache.get(anyString())).thenReturn(null);

		assertNull(movieService.getMovie(PRIMARY_ID));

		verify(movieDAO).getMovie(PRIMARY_ID);
		verify(movieCache).get("movie" + PRIMARY_ID);
		verify(movieCache).put("movie" + PRIMARY_ID, null);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testGetMovieWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.getMovie(Integer.MAX_VALUE);
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testGetMovieWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.getMovie(Integer.MAX_VALUE);
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with null argument. */
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

	/** Test method for {@link MovieService#getMovie(Integer)} with exception in DAO tier. */
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
		verify(movieCache).get("movie" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#add(Movie)} with cached movies. */
	@Test
	public void testAddWithCachedMovies() {
		final Movie movie = EntityGenerator.createMovie();
		final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
		final List<Movie> moviesList = new ArrayList<>(movies);
		moviesList.add(movie);
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

		movieService.add(movie);

		verify(movieDAO).add(movie);
		verify(movieCache).get("movies");
		verify(movieCache).get("movie" + null);
		verify(movieCache).put("movies", moviesList);
		verify(movieCache).put("movie" + null, movie);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#add(Movie)} with not cached movies. */
	@Test
	public void testAddWithNotCachedMovies() {
		final Movie movie = EntityGenerator.createMovie();
		when(movieCache.get(anyString())).thenReturn(null);

		movieService.add(movie);

		verify(movieDAO).add(movie);
		verify(movieCache).get("movies");
		verify(movieCache).get("movie" + null);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#add(Movie)} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.add(mock(Movie.class));
	}

	/** Test method for {@link MovieService#add(Movie)} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.add(mock(Movie.class));
	}

	/** Test method for {@link MovieService#add(Movie)} with null argument. */
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

	/** Test method for {@link MovieService#add(Movie)} with exception in DAO tier. */
	@Test
	public void testAddWithDAOTierException() {
		final Movie movie = EntityGenerator.createMovie();
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

	/** Test method for {@link MovieService#update(Movie)}. */
	@Test
	public void testUpdate() {
		final Movie movie = EntityGenerator.createMovie(PRIMARY_ID);

		movieService.update(movie);

		verify(movieDAO).update(movie);
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#update(Movie)} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.update(mock(Movie.class));
	}

	/** Test method for {@link MovieService#update(Movie)} with not set movie cache. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.update(mock(Movie.class));
	}

	/** Test method for {@link MovieService#update(Movie)} with null argument. */
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

	/** Test method for {@link MovieService#update(Movie)} with exception in DAO tier. */
	@Test
	public void testUpdateWithDAOTierException() {
		final Movie movie = EntityGenerator.createMovie(Integer.MAX_VALUE);
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

	/** Test method for {@link MovieService#remove(Movie)} with cached movies. */
	@Test
	public void testRemoveWithCachedMovies() {
		final Movie movie = EntityGenerator.createMovie(PRIMARY_ID);
		final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), mock(Movie.class));
		final List<Movie> moviesList = new ArrayList<>(movies);
		moviesList.add(movie);
		when(movieCache.get("movies")).thenReturn(new SimpleValueWrapper(moviesList));
		when(movieCache.get("movie" + PRIMARY_ID)).thenReturn(new SimpleValueWrapper(movie));

		movieService.remove(movie);

		verify(movieDAO).remove(movie);
		verify(movieCache).get("movies");
		verify(movieCache).put("movies", movies);
		verify(movieCache).evict(PRIMARY_ID);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#remove(Movie)} with not cached movies. */
	@Test
	public void testRemoveWithNotCachedMovies() {
		final Movie movie = EntityGenerator.createMovie(PRIMARY_ID);
		when(movieCache.get(anyString())).thenReturn(null);

		movieService.remove(movie);

		verify(movieDAO).remove(movie);
		verify(movieCache).get("movies");
		verify(movieCache).evict(PRIMARY_ID);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#remove(Movie)} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.remove(mock(Movie.class));
	}

	/** Test method for {@link MovieService#remove(Movie)} with not set movie cache. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.remove(mock(Movie.class));
	}

	/** Test method for {@link MovieService#remove(Movie)} with null argument. */
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

	/** Test method for {@link MovieService#remove(Movie)} with exception in DAO tier. */
	@Test
	public void testRemoveWithDAOTierException() {
		final Movie movie = EntityGenerator.createMovie(Integer.MAX_VALUE);
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

	/** Test method for {@link MovieService#duplicate(Movie)} with cached movies. */
	@Test
	public void testDuplicateWithCachedMovies() {
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(CollectionUtils.newList(mock(Movie.class), mock(Movie.class))));

		movieService.duplicate(EntityGenerator.createMovie(PRIMARY_ID));

		verify(movieDAO).add(any(Movie.class));
		verify(movieDAO).update(any(Movie.class));
		verify(movieCache).get("movies");
		verify(movieCache).get("movie" + null);
		verify(movieCache).put(eq("movies"), anyListOf(Movie.class));
		verify(movieCache).put(eq("movie" + null), any(Movie.class));
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#duplicate(Movie)} with not cached movies. */
	@Test
	public void testDuplicateWithNotCachedMovies() {
		when(movieCache.get(anyString())).thenReturn(null);

		movieService.duplicate(EntityGenerator.createMovie(PRIMARY_ID));

		verify(movieDAO).add(any(Movie.class));
		verify(movieDAO).update(any(Movie.class));
		verify(movieCache).get("movies");
		verify(movieCache).get("movie" + null);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#duplicate(Movie)} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.duplicate(mock(Movie.class));
	}

	/** Test method for {@link MovieService#duplicate(Movie)} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.duplicate(mock(Movie.class));
	}

	/** Test method for {@link MovieService#duplicate(Movie)} with null argument. */
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

	/** Test method for {@link MovieService#duplicate(Movie)} with exception in DAO tier. */
	@Test
	public void testDuplicateWithDAOTierException() {
		doThrow(DataStorageException.class).when(movieDAO).add(any(Movie.class));

		try {
			movieService.duplicate(EntityGenerator.createMovie(Integer.MAX_VALUE));
			fail("Can't duplicate movie with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(movieDAO).add(any(Movie.class));
		verifyNoMoreInteractions(movieDAO);
		verifyZeroInteractions(movieCache);
	}

	/** Test method for {@link MovieService#moveUp(Movie)} with cached movies. */
	@Test
	public void testMoveUpWithCachedMovies() {
		final Movie movie1 = EntityGenerator.createMovie(PRIMARY_ID);
		movie1.setPosition(MOVE_POSITION);
		final Movie movie2 = EntityGenerator.createMovie(SECONDARY_ID);
		final List<Movie> movies = CollectionUtils.newList(movie1, movie2);
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

		movieService.moveUp(movie2);
		DeepAsserts.assertEquals(POSITION, movie1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, movie2.getPosition());

		verify(movieDAO).update(movie1);
		verify(movieDAO).update(movie2);
		verify(movieCache).get("movies");
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#moveUp(Movie)} with not cached movies. */
	@Test
	public void testMoveUpWithNotCachedMovies() {
		final Movie movie1 = EntityGenerator.createMovie(PRIMARY_ID);
		movie1.setPosition(MOVE_POSITION);
		final Movie movie2 = EntityGenerator.createMovie(SECONDARY_ID);
		final List<Movie> movies = CollectionUtils.newList(movie1, movie2);
		when(movieDAO.getMovies()).thenReturn(movies);
		when(movieCache.get(anyString())).thenReturn(null);

		movieService.moveUp(movie2);
		DeepAsserts.assertEquals(POSITION, movie1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, movie2.getPosition());

		verify(movieDAO).update(movie1);
		verify(movieDAO).update(movie2);
		verify(movieDAO).getMovies();
		verify(movieCache).get("movies");
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#moveUp(Movie)} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.moveUp(mock(Movie.class));
	}

	/** Test method for {@link MovieService#moveUp(Movie)} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.moveUp(mock(Movie.class));
	}

	/** Test method for {@link MovieService#moveUp(Movie)} with null argument. */
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

	/** Test method for {@link MovieService#moveUp(Movie)} with exception in DAO tier. */
	@Test
	public void testMoveUpWithDAOTierException() {
		doThrow(DataStorageException.class).when(movieDAO).getMovies();
		when(movieCache.get(anyString())).thenReturn(null);

		try {
			movieService.moveUp(EntityGenerator.createMovie(Integer.MAX_VALUE));
			fail("Can't move up movie with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(movieDAO).getMovies();
		verify(movieCache).get("movies");
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#moveDown(Movie)} with cached movies. */
	@Test
	public void testMoveDownWithCachedMovies() {
		final Movie movie1 = EntityGenerator.createMovie(PRIMARY_ID);
		final Movie movie2 = EntityGenerator.createMovie(SECONDARY_ID);
		movie2.setPosition(MOVE_POSITION);
		final List<Movie> movies = CollectionUtils.newList(movie1, movie2);
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

		movieService.moveDown(movie1);
		DeepAsserts.assertEquals(MOVE_POSITION, movie1.getPosition());
		DeepAsserts.assertEquals(POSITION, movie2.getPosition());

		verify(movieDAO).update(movie1);
		verify(movieDAO).update(movie2);
		verify(movieCache).get("movies");
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#moveDown(Movie)} with not cached movies. */
	@Test
	public void testMoveDownWithNotCachedMovies() {
		final Movie movie1 = EntityGenerator.createMovie(PRIMARY_ID);
		final Movie movie2 = EntityGenerator.createMovie(SECONDARY_ID);
		movie2.setPosition(MOVE_POSITION);
		final List<Movie> movies = CollectionUtils.newList(movie1, movie2);
		when(movieDAO.getMovies()).thenReturn(movies);
		when(movieCache.get(anyString())).thenReturn(null);

		movieService.moveDown(movie1);
		DeepAsserts.assertEquals(MOVE_POSITION, movie1.getPosition());
		DeepAsserts.assertEquals(POSITION, movie2.getPosition());

		verify(movieDAO).update(movie1);
		verify(movieDAO).update(movie2);
		verify(movieDAO).getMovies();
		verify(movieCache).get("movies");
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#moveDown(Movie)} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.moveDown(mock(Movie.class));
	}

	/** Test method for {@link MovieService#moveDown(Movie)} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.moveDown(mock(Movie.class));
	}

	/** Test method for {@link MovieService#moveDown(Movie)} with null argument. */
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

	/** Test method for {@link MovieService#moveDown(Movie)} with exception in DAO tier. */
	@Test
	public void testMoveDownWithDAOTierException() {
		doThrow(DataStorageException.class).when(movieDAO).getMovies();
		when(movieCache.get(anyString())).thenReturn(null);

		try {
			movieService.moveDown(EntityGenerator.createMovie(Integer.MAX_VALUE));
			fail("Can't move down movie with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(movieDAO).getMovies();
		verify(movieCache).get("movies");
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#exists(Movie)} with cached existing movie. */
	@Test
	public void testExistsWithCachedExistingMovie() {
		final Movie movie = EntityGenerator.createMovie(PRIMARY_ID);
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movie));

		assertTrue(movieService.exists(movie));

		verify(movieCache).get("movie" + PRIMARY_ID);
		verifyNoMoreInteractions(movieCache);
		verifyZeroInteractions(movieDAO);
	}

	/** Test method for {@link MovieService#exists(Movie)} with cached not existing movie. */
	@Test
	public void testExistsWithCachedNotExistingMovie() {
		final Movie movie = EntityGenerator.createMovie(Integer.MAX_VALUE);
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(movieService.exists(movie));

		verify(movieCache).get("movie" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(movieCache);
		verifyZeroInteractions(movieDAO);
	}

	/** Test method for {@link MovieService#exists(Movie)} with not cached existing movie. */
	@Test
	public void testExistsWithNotCachedExistingMovie() {
		final Movie movie = EntityGenerator.createMovie(PRIMARY_ID);
		when(movieDAO.getMovie(anyInt())).thenReturn(movie);
		when(movieCache.get(anyString())).thenReturn(null);

		assertTrue(movieService.exists(movie));

		verify(movieDAO).getMovie(PRIMARY_ID);
		verify(movieCache).get("movie" + PRIMARY_ID);
		verify(movieCache).put("movie" + PRIMARY_ID, movie);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#exists(Movie)} with not cached not existing movie. */
	@Test
	public void testExistsWithNotCachedNotExistingMovie() {
		when(movieDAO.getMovie(anyInt())).thenReturn(null);
		when(movieCache.get(anyString())).thenReturn(null);

		assertFalse(movieService.exists(EntityGenerator.createMovie(Integer.MAX_VALUE)));

		verify(movieDAO).getMovie(Integer.MAX_VALUE);
		verify(movieCache).get("movie" + Integer.MAX_VALUE);
		verify(movieCache).put("movie" + Integer.MAX_VALUE, null);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#exists(Movie)} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.exists(mock(Movie.class));
	}

	/** Test method for {@link MovieService#exists(Movie)} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.exists(mock(Movie.class));
	}

	/** Test method for {@link MovieService#exists(Movie)} with null argument. */
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

	/** Test method for {@link MovieService#exists(Movie)} with exception in DAO tier. */
	@Test
	public void testExistsWithDAOTierException() {
		doThrow(DataStorageException.class).when(movieDAO).getMovie(anyInt());
		when(movieCache.get(anyString())).thenReturn(null);

		try {
			movieService.exists(EntityGenerator.createMovie(Integer.MAX_VALUE));
			fail("Can't exists movie with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(movieDAO).getMovie(Integer.MAX_VALUE);
		verify(movieCache).get("movie" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#updatePositions()} with cached movies. */
	@Test
	public void testUpdatePositionsWithCachedMovies() {
		final List<Movie> movies = CollectionUtils.newList(EntityGenerator.createMovie(PRIMARY_ID), EntityGenerator.createMovie(SECONDARY_ID));
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));

		movieService.updatePositions();

		for (int i = 0; i < movies.size(); i++) {
			final Movie movie = movies.get(i);
			DeepAsserts.assertEquals(i, movie.getPosition());
			verify(movieDAO).update(movie);
		}
		verify(movieCache).get("movies");
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#updatePositions()} with not cached movies. */
	@Test
	public void testUpdatePositionsWithNotCachedMovies() {
		final List<Movie> movies = CollectionUtils.newList(EntityGenerator.createMovie(PRIMARY_ID), EntityGenerator.createMovie(SECONDARY_ID));
		when(movieDAO.getMovies()).thenReturn(movies);
		when(movieCache.get(anyString())).thenReturn(null);

		movieService.updatePositions();

		verify(movieDAO).getMovies();
		for (int i = 0; i < movies.size(); i++) {
			final Movie movie = movies.get(i);
			DeepAsserts.assertEquals(i, movie.getPosition());
			verify(movieDAO).update(movie);
		}
		verify(movieCache).get("movies");
		verify(movieCache).clear();
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#updatePositions()} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.updatePositions();
	}

	/** Test method for {@link MovieService#updatePositions()} with not set movie cache. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.updatePositions();
	}

	/** Test method for {@link MovieService#updatePositions()} with exception in DAO tier. */
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
		verify(movieCache).get("movies");
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#getTotalMediaCount()} with cached movies. */
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

		verify(movieCache).get("movies");
		verify(movie1).getMedia();
		verify(movie2).getMedia();
		verify(movie3).getMedia();
		verifyNoMoreInteractions(movieCache, movie1, movie2, movie3);
		verifyZeroInteractions(movieDAO);
	}

	/** Test method for {@link MovieService#getTotalMediaCount()} with not cached movies. */
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
		verify(movieCache).get("movies");
		verify(movieCache).put("movies", movies);
		verify(movie1).getMedia();
		verify(movie2).getMedia();
		verify(movie3).getMedia();
		verifyNoMoreInteractions(movieDAO, movieCache, movie1, movie2, movie3);
	}

	/** Test method for {@link MovieService#getTotalMediaCount()} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.getTotalMediaCount();
	}

	/** Test method for {@link MovieService#getTotalMediaCount()} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.getTotalMediaCount();
	}

	/** Test method for {@link MovieService#getTotalMediaCount()} with exception in DAO tier. */
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
		verify(movieCache).get("movies");
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

	/** Test method for {@link MovieService#getTotalLength()} with cached movies. */
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
		when(movieCache.get(anyString())).thenReturn(new SimpleValueWrapper(movies));
		when(movie1.getMedia()).thenReturn(CollectionUtils.newList(medium1));
		when(movie2.getMedia()).thenReturn(CollectionUtils.newList(medium2, medium3));
		when(movie3.getMedia()).thenReturn(CollectionUtils.newList(medium4, medium5, medium6));
		when(medium1.getLength()).thenReturn(1);
		when(medium2.getLength()).thenReturn(2);
		when(medium3.getLength()).thenReturn(3);
		when(medium4.getLength()).thenReturn(4);
		when(medium5.getLength()).thenReturn(5);
		when(medium6.getLength()).thenReturn(6);

		DeepAsserts.assertEquals(new Time(21), movieService.getTotalLength());

		verify(movieCache).get("movies");
		verify(movie1).getMedia();
		verify(movie2).getMedia();
		verify(movie3).getMedia();
		verifyNoMoreInteractions(movieCache, movie1, movie2, movie3);
		verifyZeroInteractions(movieDAO);
	}

	/** Test method for {@link MovieService#getTotalLength()} with not cached movies. */
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
		when(movieDAO.getMovies()).thenReturn(movies);
		when(movieCache.get(anyString())).thenReturn(null);
		when(movie1.getMedia()).thenReturn(CollectionUtils.newList(medium1));
		when(movie2.getMedia()).thenReturn(CollectionUtils.newList(medium2, medium3));
		when(movie3.getMedia()).thenReturn(CollectionUtils.newList(medium4, medium5, medium6));
		when(medium1.getLength()).thenReturn(1);
		when(medium2.getLength()).thenReturn(2);
		when(medium3.getLength()).thenReturn(3);
		when(medium4.getLength()).thenReturn(4);
		when(medium5.getLength()).thenReturn(5);
		when(medium6.getLength()).thenReturn(6);

		DeepAsserts.assertEquals(new Time(21), movieService.getTotalLength());

		verify(movieDAO).getMovies();
		verify(movieCache).get("movies");
		verify(movieCache).put("movies", movies);
		verify(movie1).getMedia();
		verify(movie2).getMedia();
		verify(movie3).getMedia();
		verifyNoMoreInteractions(movieDAO, movieCache, movie1, movie2, movie3);
	}

	/** Test method for {@link MovieService#getTotalLength()} with not set DAO for movies. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthWithNotSetMovieDAO() {
		((MovieServiceImpl) movieService).setMovieDAO(null);
		movieService.getTotalLength();
	}

	/** Test method for {@link MovieService#getTotalLength()} with not set cache for movies. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthWithNotSetMovieCache() {
		((MovieServiceImpl) movieService).setMovieCache(null);
		movieService.getTotalLength();
	}

	/** Test method for {@link MovieService#getTotalLength()} with exception in DAO tier. */
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
		verify(movieCache).get("movies");
		verifyNoMoreInteractions(movieDAO, movieCache);
	}

}
