package cz.vhromada.catalog.service.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.GENRES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.MOVIES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SERIES_COUNT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.service.GenreService;
import cz.vhromada.catalog.service.impl.GenreServiceImpl;
import cz.vhromada.generator.ObjectGenerator;
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
 * A class represents test for class {@link GenreServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class GenreServiceImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('genreCache')}")
	private Cache genreCache;

	/** Instance of {@link GenreService} */
	@Autowired
	private GenreService genreService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Clears cache and restarts sequence. */
	@Before
	public void setUp() {
		genreCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE genres_sq RESTART WITH 5").executeUpdate();
	}

	/** Test method for {@link GenreService#newData()}. */
	@Test
	public void testNewData() {
		for (int i = 1; i <= MOVIES_COUNT; i++) {
			entityManager.remove(SpringUtils.getMovie(entityManager, i));
		}
		for (int i = 1; i <= EPISODES_COUNT; i++) {
			entityManager.remove(SpringUtils.getEpisode(entityManager, i));
		}
		for (int i = 1; i <= SEASONS_COUNT; i++) {
			entityManager.remove(SpringUtils.getSeason(entityManager, i));
		}
		for (int i = 1; i <= SERIES_COUNT; i++) {
			entityManager.remove(SpringUtils.getSerie(entityManager, i));
		}

		genreService.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getGenresCount(entityManager));
		assertTrue(SpringUtils.getCacheKeys(genreCache).isEmpty());
	}

	/** Test method for {@link GenreService#getGenres()}. */
	@Test
	public void testGetGenres() {
		final List<Genre> genres = SpringEntitiesUtils.getGenres();
		final String key = "genres";

		DeepAsserts.assertEquals(genres, genreService.getGenres());
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(genreCache));
		SpringUtils.assertCacheValue(genreCache, key, genres);
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with existing genre. */
	@Test
	public void testGetGenreWithExistingGenre() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= GENRES_COUNT; i++) {
			keys.add("genre" + i);
		}

		for (int i = 1; i <= GENRES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGenre(i), genreService.getGenre(i));
		}
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(genreCache));
		for (int i = 1; i <= GENRES_COUNT; i++) {
			SpringUtils.assertCacheValue(genreCache, keys.get(i - 1), SpringEntitiesUtils.getGenre(i));
		}
	}

	/** Test method for {@link GenreService#getGenre(Integer)} with not existing genre. */
	@Test
	public void testGetGenreWithNotExistingGenre() {
		final String key = "genre" + Integer.MAX_VALUE;

		assertNull(genreService.getGenre(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(genreCache));
		SpringUtils.assertCacheValue(genreCache, key, null);
	}

	/** Test method for {@link GenreService#add(Genre)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final Genre genre = objectGenerator.generate(Genre.class);
		genre.setId(null);

		genreService.add(genre);

		DeepAsserts.assertNotNull(genre.getId());
		DeepAsserts.assertEquals(GENRES_COUNT + 1, genre.getId());
		final Genre addedGenre = SpringUtils.getGenre(entityManager, GENRES_COUNT + 1);
		DeepAsserts.assertEquals(genre, addedGenre);
		DeepAsserts.assertEquals(GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(genreCache).size());
	}

	/** Test method for {@link GenreService#add(Genre)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final Genre genre = objectGenerator.generate(Genre.class);
		genre.setId(null);
		final String keyList = "genres";
		final String keyItem = "genre" + (GENRES_COUNT + 1);
		genreCache.put(keyList, new ArrayList<>());
		genreCache.put(keyItem, null);

		genreService.add(genre);

		DeepAsserts.assertNotNull(genre.getId());
		DeepAsserts.assertEquals(GENRES_COUNT + 1, genre.getId());
		final Genre addedGenre = SpringUtils.getGenre(entityManager, GENRES_COUNT + 1);
		DeepAsserts.assertEquals(genre, addedGenre);
		DeepAsserts.assertEquals(GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(genreCache));
		SpringUtils.assertCacheValue(genreCache, keyList, CollectionUtils.newList(genre));
		SpringUtils.assertCacheValue(genreCache, keyItem, genre);
	}

	/** Test method for {@link GenreService#add(List)}. */
	@Test
	public void testAddList() {
		final List<String> names = CollectionUtils.newList(objectGenerator.generate(String.class), objectGenerator.generate(String.class));

		genreService.add(names);

		final Genre addedGenre1 = SpringUtils.getGenre(entityManager, GENRES_COUNT + 1);
		final Genre addedGenre2 = SpringUtils.getGenre(entityManager, GENRES_COUNT + 2);
		DeepAsserts.assertEquals(createGenre(GENRES_COUNT + 1, names.get(0)), addedGenre1);
		DeepAsserts.assertEquals(createGenre(GENRES_COUNT + 2, names.get(1)), addedGenre2);
		DeepAsserts.assertEquals(GENRES_COUNT + 2, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(genreCache).size());
	}

	/** Test method for {@link GenreService#update(Genre)}. */
	@Test
	public void testUpdate() {
		final Genre genre = SpringEntitiesUtils.updateGenre(SpringUtils.getGenre(entityManager, 1), objectGenerator);

		genreService.update(genre);

		final Genre updatedGenre = SpringUtils.getGenre(entityManager, 1);
		DeepAsserts.assertEquals(genre, updatedGenre);
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(genreCache).size());
	}

	/** Test method for {@link GenreService#remove(Genre)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final Genre genre = objectGenerator.generate(Genre.class);
		genre.setId(null);
		entityManager.persist(genre);
		DeepAsserts.assertEquals(GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));

		genreService.remove(genre);

		assertNull(SpringUtils.getGenre(entityManager, genre.getId()));
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(genreCache).size());
	}

	/** Test method for {@link GenreService#remove(Genre)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final Genre genre = objectGenerator.generate(Genre.class);
		genre.setId(null);
		entityManager.persist(genre);
		DeepAsserts.assertEquals(GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));
		final String key = "genres";
		final List<Genre> cacheGenres = new ArrayList<>();
		cacheGenres.add(genre);
		genreCache.put(key, cacheGenres);

		genreService.remove(genre);

		assertNull(SpringUtils.getGenre(entityManager, genre.getId()));
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(genreCache));
		SpringUtils.assertCacheValue(genreCache, key, new ArrayList<>());
	}

	/** Test method for {@link GenreService#exists(Genre)} with existing genre. */
	@Test
	public void testExistsWithExistingGenre() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= GENRES_COUNT; i++) {
			keys.add("genre" + i);
		}

		for (int i = 1; i <= GENRES_COUNT; i++) {
			assertTrue(genreService.exists(SpringEntitiesUtils.getGenre(i)));
		}
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(genreCache));
		for (int i = 1; i <= GENRES_COUNT; i++) {
			SpringUtils.assertCacheValue(genreCache, keys.get(i - 1), SpringEntitiesUtils.getGenre(i));
		}
	}

	/** Test method for {@link GenreService#exists(Genre)} with not existing genre. */
	@Test
	public void testExistsWithNotExistingGenre() {
		final Genre genre = objectGenerator.generate(Genre.class);
		genre.setId(Integer.MAX_VALUE);
		final String key = "genre" + Integer.MAX_VALUE;

		assertFalse(genreService.exists(genre));
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(genreCache));
		SpringUtils.assertCacheValue(genreCache, key, null);
	}

	/**
	 * Returns new genre.
	 *
	 * @param id   ID
	 * @param name name
	 * @return new genre
	 */
	private Genre createGenre(final int id, final String name) {
		final Genre genre = objectGenerator.generate(Genre.class);
		genre.setId(id);
		genre.setName(name);
		return genre;
	}

}
