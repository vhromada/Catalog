package cz.vhromada.catalog.service.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.service.SeasonService;
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
 * A class represents test for class {@link cz.vhromada.catalog.service.impl.SeasonServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class SeasonServiceImplSpringTest {

    /**
     * Cache key for list of seasons
     */
    private static final String SEASONS_CACHE_KEY = "seasons";

    /**
     * Cache key for season
     */
    private static final String SEASON_CACHE_KEY = "season";

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link Cache}
     */
    @Value("#{cacheManager.getCache('showCache')}")
    private Cache showCache;

    /**
     * Instance of {@link SeasonService}
     */
    @Autowired
    private SeasonService seasonService;

    /**
     * Instance of {@link ObjectGenerator}
     */
    @Autowired
    private ObjectGenerator objectGenerator;

    /**
     * Clears cache and restarts sequence.
     */
    @Before
    public void setUp() {
        showCache.clear();
        entityManager.createNativeQuery("ALTER SEQUENCE seasons_sq RESTART WITH 10").executeUpdate();
    }

    /**
     * Test method for {@link SeasonService#getSeason(Integer)} with existing season.
     */
    @Test
    public void testGetSeasonWithExistingSeason() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.SEASONS_COUNT; i++) {
            keys.add(SEASON_CACHE_KEY + i);
        }

        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(showNumber, seasonNumber), seasonService.getSeason(i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(showCache));
        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            SpringUtils.assertCacheValue(showCache, keys.get(i), SpringEntitiesUtils.getSeason(showNumber, seasonNumber));
        }
    }

    /**
     * Test method for {@link SeasonService#getSeason(Integer)} with not existing season.
     */
    @Test
    public void testGetSeasonWithNotExistingSeason() {
        final String key = SEASON_CACHE_KEY + Integer.MAX_VALUE;

        assertNull(seasonService.getSeason(Integer.MAX_VALUE));
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, key, null);
    }

    /**
     * Test method for {@link SeasonService#add(Season)} with empty cache.
     */
    @Test
    public void testAddWithEmptyCache() {
        final Season season = SpringEntitiesUtils.newSeason(objectGenerator, entityManager);

        seasonService.add(season);

        DeepAsserts.assertNotNull(season.getId());
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, season.getId());
        final Season addedSeason = SpringUtils.getSeason(entityManager, SpringUtils.SEASONS_COUNT + 1);
        DeepAsserts.assertEquals(season, addedSeason);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link SeasonService#add(Season)} with not empty cache.
     */
    @Test
    public void testAddWithNotEmptyCache() {
        final Season season = SpringEntitiesUtils.newSeason(objectGenerator, entityManager);
        final String keyList = SEASONS_CACHE_KEY + season.getShow().getId();
        final String keyItem = SEASON_CACHE_KEY + (SpringUtils.SEASONS_COUNT + 1);
        showCache.put(keyList, new ArrayList<>());
        showCache.put(keyItem, null);

        seasonService.add(season);

        DeepAsserts.assertNotNull(season.getId());
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, season.getId());
        final Season addedSeason = SpringUtils.getSeason(entityManager, SpringUtils.SEASONS_COUNT + 1);
        DeepAsserts.assertEquals(season, addedSeason);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, keyList, CollectionUtils.newList(season));
        SpringUtils.assertCacheValue(showCache, keyItem, season);
    }

    /**
     * Test method for {@link SeasonService#update(Season)}.
     */
    @Test
    public void testUpdate() {
        final Season season = SpringEntitiesUtils.updateSeason(1, objectGenerator, entityManager);

        seasonService.update(season);

        final Season updatedSeason = SpringUtils.getSeason(entityManager, 1);
        DeepAsserts.assertEquals(season, updatedSeason);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link SeasonService#remove(Season)} with empty cache.
     */
    @Test
    public void testRemoveWithEmptyCache() {
        final Season season = SpringEntitiesUtils.newSeason(objectGenerator, entityManager);
        entityManager.persist(season);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));

        seasonService.remove(season);

        assertNull(SpringUtils.getSeason(entityManager, season.getId()));
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link SeasonService#remove(Season)} with not empty cache.
     */
    @Test
    public void testRemoveWithNotEmptyCache() {
        final Season season = SpringEntitiesUtils.newSeason(objectGenerator, entityManager);
        entityManager.persist(season);
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
        final String key = SEASONS_CACHE_KEY + season.getShow().getId();
        final List<Season> cacheSeasons = new ArrayList<>();
        cacheSeasons.add(season);
        showCache.put(key, cacheSeasons);

        seasonService.remove(season);

        assertNull(SpringUtils.getSeason(entityManager, season.getId()));
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link SeasonService#duplicate(Season)} with empty cache.
     */
    @Test
    public void testDuplicateWithEmptyCache() {
        final Season season = SpringUtils.getSeason(entityManager, 3);
        final Season expectedSeason = SpringEntitiesUtils.getSeason(1, 3);
        expectedSeason.setId(SpringUtils.SEASONS_COUNT + 1);

        seasonService.duplicate(season);

        DeepAsserts.assertEquals(expectedSeason, SpringUtils.getSeason(entityManager, SpringUtils.SEASONS_COUNT + 1));
        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(showNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link SeasonService#duplicate(Season)} with not empty cache.
     */
    @Test
    public void testDuplicateWithNotEmptyCache() {
        final Season season = SpringUtils.getSeason(entityManager, 3);
        final Season expectedSeason = SpringEntitiesUtils.getSeason(1, 3);
        expectedSeason.setId(SpringUtils.SEASONS_COUNT + 1);
        final String keyList = SEASONS_CACHE_KEY + season.getShow().getId();
        final String keyItem = SEASON_CACHE_KEY + (SpringUtils.SEASONS_COUNT + 1);
        showCache.put(keyList, new ArrayList<>());
        showCache.put(keyItem, null);

        seasonService.duplicate(season);

        DeepAsserts.assertEquals(expectedSeason, SpringUtils.getSeason(entityManager, SpringUtils.SEASONS_COUNT + 1));
        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(showNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT + 1, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link SeasonService#moveUp(Season)}.
     */
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
        for (int i = 2; i < SpringUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(showNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link SeasonService#moveDown(Season)}.
     */
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
        for (int i = 2; i < SpringUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeason(showNumber, seasonNumber), SpringUtils.getSeason(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }


    /**
     * Test method for {@link SeasonService#exists(Season)} with existing season.
     */
    @Test
    public void testExistsWithExistingSeason() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.SEASONS_COUNT; i++) {
            keys.add(SEASON_CACHE_KEY + i);
        }

        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            assertTrue(seasonService.exists(SpringEntitiesUtils.getSeason(showNumber, seasonNumber)));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(showCache));
        for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            SpringUtils.assertCacheValue(showCache, keys.get(i), SpringEntitiesUtils.getSeason(showNumber, seasonNumber));
        }
    }

    /**
     * Test method for {@link SeasonService#exists(Season)} with not existing season.
     */
    @Test
    public void testExistsWithNotExistingSeason() {
        final Season season = SpringEntitiesUtils.newSeason(objectGenerator, entityManager);
        season.setId(Integer.MAX_VALUE);
        final String key = SEASON_CACHE_KEY + Integer.MAX_VALUE;

        assertFalse(seasonService.exists(season));
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, key, null);
    }

    /**
     * Test method for {@link SeasonService#findSeasonsByShow(Show)}.
     */
    @Test
    public void testFindSeasonsByShow() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            keys.add(SEASONS_CACHE_KEY + i);
        }

        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            final Show show = SpringUtils.getShow(entityManager, i);
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSeasons(i), seasonService.findSeasonsByShow(show));
        }
        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(showCache));
        for (int i = 0; i < SpringUtils.SHOWS_COUNT; i++) {
            SpringUtils.assertCacheValue(showCache, keys.get(i), SpringEntitiesUtils.getSeasons(i + 1));
        }
    }

}
