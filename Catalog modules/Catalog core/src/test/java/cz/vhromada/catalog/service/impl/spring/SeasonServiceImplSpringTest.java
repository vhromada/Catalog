package cz.vhromada.catalog.service.impl.spring;

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
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.impl.SeasonServiceImpl;
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
 * A class represents test for class {@link SeasonServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class SeasonServiceImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('serieCache')}")
	private Cache serieCache;

	/** Instance of {@link SeasonService} */
	@Autowired
	private SeasonService seasonService;

	/** Clears cache and restarts sequence. */
	@Before
	public void setUp() {
		serieCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE seasons_sq RESTART WITH 10").executeUpdate();
	}

	/** Test method for {@link SeasonService#getSeason(Integer)} with existing season. */
	@Test
	public void testGetSeasonWithExistingSeason() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SEASONS_COUNT; i++) {
			keys.add("season" + i);
		}

		for (int i = 0; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), seasonService.getSeason(i + 1));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 0; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			SpringUtils.assertCacheValue(serieCache, keys.get(i), SpringEntitiesUtils.getSeason(serieNumber, seasonNumber));
		}
	}

	/** Test method for {@link SeasonService#getSeason(Integer)} with not existing season. */
	@Test
	public void testGetSeasonWithNotExistingSeason() {
		final String key = "season" + Integer.MAX_VALUE;

		assertNull(seasonService.getSeason(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, key, null);
	}

	/** Test method for {@link SeasonService#add(Season)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final Serie serie = SpringUtils.getSerie(entityManager, 1);
		final Season season = EntityGenerator.createSeason(serie);
		final Season expectedSeason = EntityGenerator.createSeason(SEASONS_COUNT + 1, serie);
		expectedSeason.setPosition(SEASONS_COUNT);

		seasonService.add(season);

		DeepAsserts.assertNotNull(season.getId());
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, season.getId());
		final Season addedSeason = SpringUtils.getSeason(entityManager, SEASONS_COUNT + 1);
		DeepAsserts.assertEquals(season, addedSeason);
		DeepAsserts.assertEquals(expectedSeason, addedSeason);
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SeasonService#add(Season)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final Serie serie = SpringUtils.getSerie(entityManager, 1);
		final Season season = EntityGenerator.createSeason(serie);
		final Season expectedSeason = EntityGenerator.createSeason(SEASONS_COUNT + 1, serie);
		expectedSeason.setPosition(SEASONS_COUNT);
		final String keyList = "seasons" + serie.getId();
		final String keyItem = "season" + (SEASONS_COUNT + 1);
		serieCache.put(keyList, new ArrayList<>());
		serieCache.put(keyItem, null);

		seasonService.add(season);

		DeepAsserts.assertNotNull(season.getId());
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, season.getId());
		final Season addedSeason = SpringUtils.getSeason(entityManager, SEASONS_COUNT + 1);
		DeepAsserts.assertEquals(season, addedSeason);
		DeepAsserts.assertEquals(expectedSeason, addedSeason);
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, keyList, CollectionUtils.newList(season));
		SpringUtils.assertCacheValue(serieCache, keyItem, season);
	}

	/** Test method for {@link SeasonService#update(Season)}. */
	@Test
	public void testUpdate() {
		final Season season = SpringEntitiesUtils.updateSeason(SpringUtils.getSeason(entityManager, 1));

		seasonService.update(season);

		final Season updatedSeason = SpringUtils.getSeason(entityManager, 1);
		DeepAsserts.assertEquals(season, updatedSeason);
		DeepAsserts.assertEquals(SpringEntitiesUtils.updateSeason(SpringUtils.getSeason(entityManager, 1)), updatedSeason);
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SeasonService#remove(Season)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final Season season = EntityGenerator.createSeason(SpringUtils.getSerie(entityManager, 1));
		entityManager.persist(season);
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));

		seasonService.remove(season);

		assertNull(SpringUtils.getSeason(entityManager, season.getId()));
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SeasonService#remove(Season)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final Season season = EntityGenerator.createSeason(SpringUtils.getSerie(entityManager, 1));
		entityManager.persist(season);
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
		final String key = "seasons" + season.getSerie().getId();
		final List<Season> cacheSeasons = new ArrayList<>();
		cacheSeasons.add(season);
		serieCache.put(key, cacheSeasons);

		seasonService.remove(season);

		assertNull(SpringUtils.getSeason(entityManager, season.getId()));
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SeasonService#duplicate(Season)} with empty cache. */
	@Test
	public void testDuplicateWithEmptyCache() {
		final Season season = SpringUtils.getSeason(entityManager, 3);
		final Season expectedSeason = SpringEntitiesUtils.getSeason(1, 3);
		expectedSeason.setId(SEASONS_COUNT + 1);

		seasonService.duplicate(season);

		DeepAsserts.assertEquals(expectedSeason, SpringUtils.getSeason(entityManager, SEASONS_COUNT + 1));
		for (int i = 0; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SeasonService#duplicate(Season)} with not empty cache. */
	@Test
	public void testDuplicateWithNotEmptyCache() {
		final Season season = SpringUtils.getSeason(entityManager, 3);
		final Season expectedSeason = SpringEntitiesUtils.getSeason(1, 3);
		expectedSeason.setId(SEASONS_COUNT + 1);
		final String keyList = "seasons" + season.getSerie().getId();
		final String keyItem = "season" + (SEASONS_COUNT + 1);
		serieCache.put(keyList, new ArrayList<>());
		serieCache.put(keyItem, null);

		seasonService.duplicate(season);

		DeepAsserts.assertEquals(expectedSeason, SpringUtils.getSeason(entityManager, SEASONS_COUNT + 1));
		for (int i = 0; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SeasonService#moveUp(Season)}. */
	@Test
	public void testMoveUp() {
		final Season season = SpringUtils.getSeason(entityManager, 2);
		final Season expectedSeason1 = SpringEntitiesUtils.getSeason(1, 1);
		expectedSeason1.setPosition(1);
		final Season expectedSeason2 = SpringEntitiesUtils.getSeason(1, 2);
		expectedSeason2.setPosition(0);

		seasonService.moveUp(season);

		DeepAsserts.assertEquals(expectedSeason1, SpringUtils.getSeason(entityManager, 1));
		DeepAsserts.assertEquals(expectedSeason2, SpringUtils.getSeason(entityManager, 2));
		for (int i = 2; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link SeasonService#moveDown(Season)}. */
	@Test
	public void testMoveDown() {
		final Season season = SpringUtils.getSeason(entityManager, 1);
		final Season expectedSeason1 = SpringEntitiesUtils.getSeason(1, 1);
		expectedSeason1.setPosition(1);
		final Season expectedSeason2 = SpringEntitiesUtils.getSeason(1, 2);
		expectedSeason2.setPosition(0);

		seasonService.moveDown(season);

		DeepAsserts.assertEquals(expectedSeason1, SpringUtils.getSeason(entityManager, 1));
		DeepAsserts.assertEquals(expectedSeason2, SpringUtils.getSeason(entityManager, 2));
		for (int i = 2; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}


	/** Test method for {@link SeasonService#exists(Season)} with existing season. */
	@Test
	public void testExistsWithExistingSeason() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SEASONS_COUNT; i++) {
			keys.add("season" + i);
		}

		for (int i = 0; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			assertTrue(seasonService.exists(SpringEntitiesUtils.getSeason(serieNumber, seasonNumber)));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 0; i < SEASONS_COUNT; i++) {
			final int serieNumber = i / SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SEASONS_PER_SERIE_COUNT + 1;
			SpringUtils.assertCacheValue(serieCache, keys.get(i), SpringEntitiesUtils.getSeason(serieNumber, seasonNumber));
		}
	}

	/** Test method for {@link SeasonService#exists(Season)} with not existing season. */
	@Test
	public void testExistsWithNotExistingSeason() {
		final String key = "season" + Integer.MAX_VALUE;

		assertFalse(seasonService.exists(EntityGenerator.createSeason(Integer.MAX_VALUE)));
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, key, null);
	}

	/** Test method for {@link SeasonService#findSeasonsBySerie(Serie)}. */
	@Test
	public void testFindSeasonsBySerie() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SERIES_COUNT; i++) {
			keys.add("seasons" + i);
		}

		for (int i = 1; i <= SERIES_COUNT; i++) {
			final Serie serie = SpringUtils.getSerie(entityManager, i);
			DeepAsserts.assertEquals(SpringEntitiesUtils.getSeasons(i), seasonService.findSeasonsBySerie(serie));
		}
		DeepAsserts.assertEquals(SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 0; i < SERIES_COUNT; i++) {
			SpringUtils.assertCacheValue(serieCache, keys.get(i), SpringEntitiesUtils.getSeasons(i + 1));
		}
	}

}
