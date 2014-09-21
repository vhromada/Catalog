package cz.vhromada.catalog.service.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.MEDIA_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.MOVIES_COUNT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.service.MovieService;
import cz.vhromada.catalog.service.impl.MovieServiceImpl;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link MovieServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class MovieServiceImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('movieCache')}")
	private Cache movieCache;

	/** Instance of {@link MovieService} */
	@Autowired
	private MovieService movieService;

	/** Clears cache and restarts sequences. */
	@Before
	public void setUp() {
		movieCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE movies_sq RESTART WITH 4").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE media_sq RESTART WITH 5").executeUpdate();
	}

	/** Test method for {@link MovieService#newData()}. */
	@Test
	public void testNewData() {
		movieService.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getMediaCount(entityManager));
		assertTrue(SpringUtils.getCacheKeys(movieCache).isEmpty());
	}

	/** Test method for {@link MovieService#getMovies()}. */
	@Test
	public void testGetMovies() {
		final List<Movie> movies = SpringEntitiesUtils.getMovies();
		final String key = "movies";

		DeepAsserts.assertEquals(movies, movieService.getMovies());
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(movieCache));
		SpringUtils.assertCacheValue(movieCache, key, movies);
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with existing movie. */
	@Test
	public void testGetMovieWithExistingMovie() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= MOVIES_COUNT; i++) {
			keys.add("movie" + i);
		}

		for (int i = 1; i <= MOVIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), movieService.getMovie(i));
		}
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(movieCache));
		for (int i = 1; i <= MOVIES_COUNT; i++) {
			SpringUtils.assertCacheValue(movieCache, keys.get(i - 1), SpringEntitiesUtils.getMovie(i));
		}
	}

	/** Test method for {@link MovieService#getMovie(Integer)} with not existing movie. */
	@Test
	public void testGetMovieWithNotExistingMovie() {
		final String key = "movie" + Integer.MAX_VALUE;

		assertNull(movieService.getMovie(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(movieCache));
		SpringUtils.assertCacheValue(movieCache, key, null);
	}

	/** Test method for {@link MovieService#add(Movie)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final List<Genre> genres = new ArrayList<>();
		genres.add(SpringUtils.getGenre(entityManager, 4));
		final Movie movie = EntityGenerator.createMovie();
		for (Medium medium : movie.getMedia()) {
			medium.setId(null);
		}
		movie.setGenres(genres);
		final Movie expectedMovie = EntityGenerator.createMovie(MOVIES_COUNT + 1);
		for (Medium medium : expectedMovie.getMedia()) {
			medium.setId(MEDIA_COUNT + expectedMovie.getMedia().indexOf(medium) + 1);
		}
		expectedMovie.setGenres(genres);
		expectedMovie.setPosition(MOVIES_COUNT);

		movieService.add(movie);

		DeepAsserts.assertNotNull(movie.getId());
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, movie.getId());
		final Movie addedMovie = SpringUtils.getMovie(entityManager, MOVIES_COUNT + 1);
		DeepAsserts.assertEquals(movie, addedMovie);
		DeepAsserts.assertEquals(expectedMovie, addedMovie);
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT + 2, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(movieCache).size());
	}

	/** Test method for {@link MovieService#add(Movie)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final List<Genre> genres = new ArrayList<>();
		genres.add(SpringUtils.getGenre(entityManager, 4));
		final Movie movie = EntityGenerator.createMovie();
		for (Medium medium : movie.getMedia()) {
			medium.setId(null);
		}
		movie.setGenres(genres);
		final Movie expectedMovie = EntityGenerator.createMovie(MOVIES_COUNT + 1);
		for (Medium medium : expectedMovie.getMedia()) {
			medium.setId(MEDIA_COUNT + expectedMovie.getMedia().indexOf(medium) + 1);
		}
		expectedMovie.setGenres(genres);
		expectedMovie.setPosition(MOVIES_COUNT);
		final String keyList = "movies";
		final String keyItem = "movie" + (MOVIES_COUNT + 1);
		movieCache.put(keyList, new ArrayList<>());
		movieCache.put(keyItem, null);

		movieService.add(movie);

		DeepAsserts.assertNotNull(movie.getId());
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, movie.getId());
		final Movie addedMovie = SpringUtils.getMovie(entityManager, MOVIES_COUNT + 1);
		DeepAsserts.assertEquals(movie, addedMovie);
		DeepAsserts.assertEquals(expectedMovie, addedMovie);
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT + 2, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(movieCache));
		SpringUtils.assertCacheValue(movieCache, keyList, CollectionUtils.newList(movie));
		SpringUtils.assertCacheValue(movieCache, keyItem, movie);
	}

	/** Test method for {@link MovieService#update(Movie)}. */
	@Test
	public void testUpdate() {
		final Movie movie = SpringEntitiesUtils.updateMovie(SpringUtils.getMovie(entityManager, 1));

		movieService.update(movie);

		final Movie updatedMovie = SpringUtils.getMovie(entityManager, 1);
		DeepAsserts.assertEquals(movie, updatedMovie);
		DeepAsserts.assertEquals(SpringEntitiesUtils.updateMovie(SpringUtils.getMovie(entityManager, 1)), updatedMovie);
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(movieCache).size());
	}

	/** Test method for {@link MovieService#remove(Movie)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final List<Genre> genres = CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4));
		final Medium medium = EntityGenerator.createMedium();
		final List<Medium> media = CollectionUtils.newList(medium);
		final Movie movie = EntityGenerator.createMovie();
		movie.setMedia(media);
		movie.setGenres(genres);
		entityManager.persist(medium);
		entityManager.persist(movie);
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT + 1, SpringUtils.getMediaCount(entityManager));

		movieService.remove(movie);

		assertNull(SpringUtils.getMovie(entityManager, movie.getId()));
		assertNull(SpringUtils.getMedium(entityManager, medium.getId()));
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(movieCache).size());
	}

	/** Test method for {@link MovieService#remove(Movie)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final List<Genre> genres = CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4));
		final Medium medium = EntityGenerator.createMedium();
		final List<Medium> media = CollectionUtils.newList(medium);
		final Movie movie = EntityGenerator.createMovie();
		movie.setMedia(media);
		movie.setGenres(genres);
		entityManager.persist(medium);
		entityManager.persist(movie);
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT + 1, SpringUtils.getMediaCount(entityManager));
		final String key = "movies";
		final List<Movie> cacheMovies = new ArrayList<>();
		cacheMovies.add(movie);
		movieCache.put(key, cacheMovies);

		movieService.remove(movie);

		assertNull(SpringUtils.getMovie(entityManager, movie.getId()));
		assertNull(SpringUtils.getMedium(entityManager, medium.getId()));
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(movieCache));
		SpringUtils.assertCacheValue(movieCache, key, new ArrayList<>());
	}

	/** Test method for {@link MovieService#duplicate(Movie)} with empty cache. */
	@Test
	public void testDuplicateWithEmptyCache() {
		final Movie movie = SpringUtils.getMovie(entityManager, 3);
		final Movie expectedMovie = SpringEntitiesUtils.getMovie(3);
		expectedMovie.setId(MOVIES_COUNT + 1);
		for (Medium medium : expectedMovie.getMedia()) {
			medium.setId(MEDIA_COUNT + expectedMovie.getMedia().indexOf(medium) + 1);
		}

		movieService.duplicate(movie);

		DeepAsserts.assertEquals(expectedMovie, SpringUtils.getMovie(entityManager, MOVIES_COUNT + 1));
		for (int i = 1; i <= MOVIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), SpringUtils.getMovie(entityManager, i));
		}
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT + 2, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(movieCache).size());
	}

	/** Test method for {@link MovieService#duplicate(Movie)} with not empty cache. */
	@Test
	public void testDuplicateWithNotEmptyCache() {
		final Movie movie = SpringUtils.getMovie(entityManager, 3);
		final Movie expectedMovie = SpringEntitiesUtils.getMovie(3);
		expectedMovie.setId(MOVIES_COUNT + 1);
		for (Medium medium : expectedMovie.getMedia()) {
			medium.setId(MEDIA_COUNT + expectedMovie.getMedia().indexOf(medium) + 1);
		}
		final String keyList = "movies";
		final String keyItem = "movie" + (MOVIES_COUNT + 1);
		movieCache.put(keyList, new ArrayList<>());
		movieCache.put(keyItem, null);

		movieService.duplicate(movie);

		DeepAsserts.assertEquals(expectedMovie, SpringUtils.getMovie(entityManager, MOVIES_COUNT + 1));
		for (int i = 1; i <= MOVIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), SpringUtils.getMovie(entityManager, i));
		}
		DeepAsserts.assertEquals(MOVIES_COUNT + 1, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT + 2, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(movieCache));
		SpringUtils.assertCacheValue(movieCache, keyList, CollectionUtils.newList(expectedMovie));
		SpringUtils.assertCacheValue(movieCache, keyItem, expectedMovie);
	}

	/** Test method for {@link MovieService#moveUp(Movie)}. */
	@Test
	public void testMoveUp() {
		final Movie movie = SpringUtils.getMovie(entityManager, 2);
		final Movie expectedMovie1 = SpringEntitiesUtils.getMovie(1);
		expectedMovie1.setPosition(1);
		final Movie expectedMovie2 = SpringEntitiesUtils.getMovie(2);
		expectedMovie2.setPosition(0);

		movieService.moveUp(movie);

		DeepAsserts.assertEquals(expectedMovie1, SpringUtils.getMovie(entityManager, 1));
		DeepAsserts.assertEquals(expectedMovie2, SpringUtils.getMovie(entityManager, 2));
		for (int i = 3; i <= MOVIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), SpringUtils.getMovie(entityManager, i));
		}
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(movieCache).size());
	}

	/** Test method for {@link MovieService#moveDown(Movie)}. */
	@Test
	public void testMoveDown() {
		final Movie movie = SpringUtils.getMovie(entityManager, 1);
		final Movie expectedMovie1 = SpringEntitiesUtils.getMovie(1);
		expectedMovie1.setPosition(1);
		final Movie expectedMovie2 = SpringEntitiesUtils.getMovie(2);
		expectedMovie2.setPosition(0);

		movieService.moveDown(movie);

		DeepAsserts.assertEquals(expectedMovie1, SpringUtils.getMovie(entityManager, 1));
		DeepAsserts.assertEquals(expectedMovie2, SpringUtils.getMovie(entityManager, 2));
		for (int i = 3; i <= MOVIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), SpringUtils.getMovie(entityManager, i));
		}
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(movieCache).size());
	}

	/** Test method for {@link MovieService#exists(Movie)} with existing movie. */
	@Test
	public void testExistsWithExistingMovie() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= MOVIES_COUNT; i++) {
			keys.add("movie" + i);
		}

		for (int i = 1; i <= MOVIES_COUNT; i++) {
			assertTrue(movieService.exists(SpringEntitiesUtils.getMovie(i)));
		}
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(movieCache));
		for (int i = 1; i <= MOVIES_COUNT; i++) {
			SpringUtils.assertCacheValue(movieCache, keys.get(i - 1), SpringEntitiesUtils.getMovie(i));
		}
	}

	/** Test method for {@link MovieService#exists(Movie)} with not existing movie. */
	@Test
	public void testExistsWithNotExistingMovie() {
		final String key = "movie" + Integer.MAX_VALUE;

		assertFalse(movieService.exists(EntityGenerator.createMovie(Integer.MAX_VALUE)));
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(movieCache));
		SpringUtils.assertCacheValue(movieCache, key, null);
	}

	/** Test method for {@link MovieService#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		final Movie movie = SpringUtils.getMovie(entityManager, MOVIES_COUNT);
		movie.setPosition(5000);
		entityManager.merge(movie);

		movieService.updatePositions();

		for (int i = 1; i <= MOVIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMovie(i), SpringUtils.getMovie(entityManager, i));
		}
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(movieCache).size());
	}

	/** Test method for {@link MovieService#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		final String key = "movies";

		DeepAsserts.assertEquals(4, movieService.getTotalMediaCount());
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(movieCache));
		SpringUtils.assertCacheValue(movieCache, key, SpringEntitiesUtils.getMovies());
	}

	/** Test method for {@link MovieService#getTotalLength()}. */
	@Test
	public void testGetTotalLength() {
		final String key = "movies";

		DeepAsserts.assertEquals(new Time(1000), movieService.getTotalLength());
		DeepAsserts.assertEquals(MOVIES_COUNT, SpringUtils.getMoviesCount(entityManager));
		DeepAsserts.assertEquals(MEDIA_COUNT, SpringUtils.getMediaCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(movieCache));
		SpringUtils.assertCacheValue(movieCache, key, SpringEntitiesUtils.getMovies());
	}

}