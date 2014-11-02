package cz.vhromada.catalog.service.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.EPISODES_PER_SEASON_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.SEASONS_PER_SERIE_COUNT;
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
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.service.SerieService;
import cz.vhromada.catalog.service.impl.SerieServiceImpl;
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
 * A class represents test for class {@link SerieServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class SerieServiceImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('serieCache')}")
	private Cache serieCache;

	/** Instance of {@link SerieService} */
	@Autowired
	private SerieService serieService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Clears cache and restarts sequences. */
	@Before
	public void setUp() {
		serieCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE series_sq RESTART WITH 4").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE seasons_sq RESTART WITH 10").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE episodes_sq RESTART WITH 28").executeUpdate();
	}

	/** Test method for {@link SerieService#newData()}. */
	@Test
	public void testNewData() {
		serieService.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getEpisodesCount(entityManager));
		assertTrue(SpringUtils.getCacheKeys(serieCache).isEmpty());
	}

	/** Test method for {@link SerieService#getSeries()}. */
	@Test
	public void testGetSeries() {
		final List<Serie> series = SpringEntitiesUtils.getSeries();
		final String key = "series";

		DeepAsserts.assertEquals(series, serieService.getSeries());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, key, series);
	}

	/** Test method for {@link SerieService#getSerie(Integer)} with existing serie. */
	@Test
	public void testGetSerieWithExistingSerie() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SERIES_COUNT; i++) {
			keys.add("serie" + i);
		}

		for (int i = 1; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), serieService.getSerie(i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 1; i <= SERIES_COUNT; i++) {
			SpringUtils.assertCacheValue(serieCache, keys.get(i - 1), SpringEntitiesUtils.getSerie(i));
		}
	}

	/** Test method for {@link SerieService#getSerie(Integer)} with not existing serie. */
	@Test
	public void testGetSerieWithNotExistingSerie() {
		final String key = "serie" + Integer.MAX_VALUE;

		assertNull(serieService.getSerie(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, key, null);
	}

	/** Test method for {@link SerieService#add(Serie)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final Serie serie = SpringEntitiesUtils.newSerie(objectGenerator, entityManager);

		serieService.add(serie);

		DeepAsserts.assertNotNull(serie.getId());
		DeepAsserts.assertEquals(SERIES_COUNT + 1, serie.getId());
		final Serie addedSerie = SpringUtils.getSerie(entityManager, SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, addedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SerieService#add(Serie)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final Serie serie = SpringEntitiesUtils.newSerie(objectGenerator, entityManager);
		final String keyList = "series";
		final String keyItem = "serie" + (SERIES_COUNT + 1);
		serieCache.put(keyList, new ArrayList<>());
		serieCache.put(keyItem, null);

		serieService.add(serie);

		DeepAsserts.assertNotNull(serie.getId());
		DeepAsserts.assertEquals(SERIES_COUNT + 1, serie.getId());
		final Serie addedSerie = SpringUtils.getSerie(entityManager, SERIES_COUNT + 1);
		DeepAsserts.assertEquals(serie, addedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, keyList, CollectionUtils.newList(serie));
		SpringUtils.assertCacheValue(serieCache, keyItem, serie);
	}

	/** Test method for {@link SerieService#update(Serie)}. */
	@Test
	public void testUpdate() {
		final Serie serie = SpringEntitiesUtils.updateSerie(1, objectGenerator, entityManager);

		serieService.update(serie);

		final Serie updatedSerie = SpringUtils.getSerie(entityManager, 1);
		DeepAsserts.assertEquals(serie, updatedSerie);
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SerieService#remove(Serie)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final Serie serie = SpringEntitiesUtils.newSerie(objectGenerator, entityManager);
		entityManager.persist(serie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));

		serieService.remove(serie);

		assertNull(SpringUtils.getSerie(entityManager, serie.getId()));
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SerieService#remove(Serie)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final Serie serie = objectGenerator.generate(Serie.class);
		serie.setId(null);
		serie.setGenres(CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4)));
		entityManager.persist(serie);
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
		final String key = "series";
		final List<Serie> cacheSeries = new ArrayList<>();
		cacheSeries.add(serie);
		serieCache.put(key, cacheSeries);

		serieService.remove(serie);

		assertNull(SpringUtils.getSerie(entityManager, serie.getId()));
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SerieService#duplicate(Serie)} with empty cache. */
	@Test
	public void testDuplicateWithEmptyCache() {
		final Serie serie = SpringUtils.getSerie(entityManager, 3);
		final Serie expectedSerie = SpringEntitiesUtils.getSerie(3);
		expectedSerie.setId(SERIES_COUNT + 1);

		serieService.duplicate(serie);

		DeepAsserts.assertEquals(expectedSerie, SpringUtils.getSerie(entityManager, SERIES_COUNT + 1));
		for (int i = 1; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SerieService#duplicate(Serie)} with not empty cache. */
	@Test
	public void testDuplicateWithNotEmptyCache() {
		final Serie serie = SpringUtils.getSerie(entityManager, 3);
		final Serie expectedSerie = SpringEntitiesUtils.getSerie(3);
		expectedSerie.setId(SERIES_COUNT + 1);
		final String keyList = "series";
		final String keyItem = "serie" + (SERIES_COUNT + 1);
		serieCache.put(keyList, new ArrayList<>());
		serieCache.put(keyItem, null);

		serieService.duplicate(serie);

		DeepAsserts.assertEquals(expectedSerie, SpringUtils.getSerie(entityManager, SERIES_COUNT + 1));
		for (int i = 1; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT + 1, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SerieService#moveUp(Serie)}. */
	@Test
	public void testMoveUp() {
		final Serie serie = SpringUtils.getSerie(entityManager, 2);
		final Serie expectedSerie1 = SpringEntitiesUtils.getSerie(1);
		expectedSerie1.setPosition(1);
		final Serie expectedSerie2 = SpringEntitiesUtils.getSerie(2);
		expectedSerie2.setPosition(0);

		serieService.moveUp(serie);

		DeepAsserts.assertEquals(expectedSerie1, SpringUtils.getSerie(entityManager, 1));
		DeepAsserts.assertEquals(expectedSerie2, SpringUtils.getSerie(entityManager, 2));
		for (int i = 3; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SerieService#moveDown(Serie)}. */
	@Test
	public void testMoveDown() {
		final Serie serie = SpringUtils.getSerie(entityManager, 1);
		final Serie expectedSerie1 = SpringEntitiesUtils.getSerie(1);
		expectedSerie1.setPosition(1);
		final Serie expectedSerie2 = SpringEntitiesUtils.getSerie(2);
		expectedSerie2.setPosition(0);

		serieService.moveDown(serie);

		DeepAsserts.assertEquals(expectedSerie1, SpringUtils.getSerie(entityManager, 1));
		DeepAsserts.assertEquals(expectedSerie2, SpringUtils.getSerie(entityManager, 2));
		for (int i = 3; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}


	/** Test method for {@link SerieService#exists(Serie)} with existing serie. */
	@Test
	public void testExistsWithExistingSerie() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SERIES_COUNT; i++) {
			keys.add("serie" + i);
		}

		for (int i = 1; i <= SERIES_COUNT; i++) {
			assertTrue(serieService.exists(SpringEntitiesUtils.getSerie(i)));
		}
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 1; i <= SERIES_COUNT; i++) {
			SpringUtils.assertCacheValue(serieCache, keys.get(i - 1), SpringEntitiesUtils.getSerie(i));
		}
	}

	/** Test method for {@link SerieService#exists(Serie)} with not existing serie. */
	@Test
	public void testExistsWithNotExistingSerie() {
		final Serie serie = SpringEntitiesUtils.newSerie(objectGenerator, entityManager);
		serie.setId(Integer.MAX_VALUE);
		final String key = "serie" + Integer.MAX_VALUE;

		assertFalse(serieService.exists(serie));
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, key, null);
	}

	/** Test method for {@link SerieService#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		final Serie serie = SpringUtils.getSerie(entityManager, SERIES_COUNT);
		serie.setPosition(5000);
		entityManager.merge(serie);

		serieService.updatePositions();

		for (int i = 1; i <= SERIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSerie(i), SpringUtils.getSerie(entityManager, i));
		}
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SerieService#getTotalLength()}. */
	@Test
	public void testGetTotalLength() {
		final String keyList = "series";
		final List<String> seasonKeyItems = new ArrayList<>();
		final int seasonsCount = SEASONS_COUNT / SERIES_COUNT;
		for (int i = 1; i <= seasonsCount; i++) {
			seasonKeyItems.add("seasons" + i);
		}
		final List<String> episodeKeyItems = new ArrayList<>();
		final int episodesCount = EPISODES_COUNT / EPISODES_PER_SEASON_COUNT;
		for (int i = 1; i <= episodesCount; i++) {
			episodeKeyItems.add("episodes" + i);
		}
		final List<String> keys = new ArrayList<>();
		keys.add(keyList);
		keys.addAll(seasonKeyItems);
		keys.addAll(episodeKeyItems);

		DeepAsserts.assertEquals(new Time(1998), serieService.getTotalLength());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, keyList, SpringEntitiesUtils.getSeries());
		for (int i = 1; i <= seasonsCount; i++) {
			SpringUtils.assertCacheValue(serieCache, seasonKeyItems.get(i - 1), SpringEntitiesUtils.getSeasons(i));
		}
		for (int i = 0; i < episodesCount; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			SpringUtils.assertCacheValue(serieCache, episodeKeyItems.get(i), SpringEntitiesUtils.getEpisodes(serieNumber, seasonNumber));
		}
	}

	/** Test method for {@link SerieService#getSeasonsCount()}. */
	@Test
	public void testGetSeasonsCount() {
		final String keyList = "series";
		final List<String> keyItems = new ArrayList<>();
		final int count = SEASONS_COUNT / SERIES_COUNT;
		for (int i = 1; i <= count; i++) {
			keyItems.add("seasons" + i);
		}
		final List<String> keys = new ArrayList<>();
		keys.add(keyList);
		keys.addAll(keyItems);

		DeepAsserts.assertEquals(SEASONS_COUNT, serieService.getSeasonsCount());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, keyList, SpringEntitiesUtils.getSeries());
		for (int i = 1; i <= count; i++) {
			SpringUtils.assertCacheValue(serieCache, keyItems.get(i - 1), SpringEntitiesUtils.getSeasons(i));
		}
	}

	/** Test method for {@link SerieService#getEpisodesCount()}. */
	@Test
	public void testGetEpisodesCount() {
		final String keyList = "series";
		final List<String> seasonKeyItems = new ArrayList<>();
		final int seasonsCount = SEASONS_COUNT / SERIES_COUNT;
		for (int i = 1; i <= seasonsCount; i++) {
			seasonKeyItems.add("seasons" + i);
		}
		final List<String> episodeKeyItems = new ArrayList<>();
		final int episodesCount = EPISODES_COUNT / EPISODES_PER_SEASON_COUNT;
		for (int i = 1; i <= episodesCount; i++) {
			episodeKeyItems.add("episodes" + i);
		}
		final List<String> keys = new ArrayList<>();
		keys.add(keyList);
		keys.addAll(seasonKeyItems);
		keys.addAll(episodeKeyItems);

		DeepAsserts.assertEquals(EPISODES_COUNT, serieService.getEpisodesCount());
		DeepAsserts.assertEquals(SERIES_COUNT, SpringUtils.getSeriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, keyList, SpringEntitiesUtils.getSeries());
		for (int i = 1; i <= seasonsCount; i++) {
			SpringUtils.assertCacheValue(serieCache, seasonKeyItems.get(i - 1), SpringEntitiesUtils.getSeasons(i));
		}
		for (int i = 0; i < episodesCount; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			SpringUtils.assertCacheValue(serieCache, episodeKeyItems.get(i), SpringEntitiesUtils.getEpisodes(serieNumber, seasonNumber));
		}
	}

}
