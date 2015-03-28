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
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.service.ShowService;
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
 * A class represents test for class {@link cz.vhromada.catalog.service.impl.ShowServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class ShowServiceImplSpringTest {

    /**
     * Cache key for list of shows
     */
    private static final String SHOWS_CACHE_KEY = "shows";

    /**
     * Cache key for book show
     */
    private static final String SHOW_CACHE_KEY = "show";

    /**
     * Cache key for list of seasons
     */
    private static final String SEASONS_CACHE_KEY = "seasons";

    /**
     * Cache key for list of episodes
     */
    private static final String EPISODES_CACHE_KEY = "episodes";

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
     * Instance of {@link ShowService}
     */
    @Autowired
    private ShowService showService;

    /**
     * Instance of {@link ObjectGenerator}
     */
    @Autowired
    private ObjectGenerator objectGenerator;

    /**
     * Clears cache and restarts sequences.
     */
    @Before
    public void setUp() {
        showCache.clear();
        entityManager.createNativeQuery("ALTER SEQUENCE tv_shows_sq RESTART WITH 4").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE seasons_sq RESTART WITH 10").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE episodes_sq RESTART WITH 28").executeUpdate();
    }

    /**
     * Test method for {@link ShowService#newData()}.
     */
    @Test
    public void testNewData() {
        showService.newData();

        DeepAsserts.assertEquals(0, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getSeasonsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getEpisodesCount(entityManager));
        assertTrue(SpringUtils.getCacheKeys(showCache).isEmpty());
    }

    /**
     * Test method for {@link ShowService#getShows()}.
     */
    @Test
    public void testGetShows() {
        final List<Show> shows = SpringEntitiesUtils.getShows();
        final String key = SHOWS_CACHE_KEY;

        DeepAsserts.assertEquals(shows, showService.getShows());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, key, shows);
    }

    /**
     * Test method for {@link ShowService#getShow(Integer)} with existing show.
     */
    @Test
    public void testGetShowWithExistingShow() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            keys.add(SHOW_CACHE_KEY + i);
        }

        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), showService.getShow(i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(showCache));
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            SpringUtils.assertCacheValue(showCache, keys.get(i - 1), SpringEntitiesUtils.getShow(i));
        }
    }

    /**
     * Test method for {@link ShowService#getShow(Integer)} with not existing show.
     */
    @Test
    public void testGetShowWithNotExistingShow() {
        final String key = SHOW_CACHE_KEY + Integer.MAX_VALUE;

        assertNull(showService.getShow(Integer.MAX_VALUE));
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, key, null);
    }

    /**
     * Test method for {@link ShowService#add(Show)} with empty cache.
     */
    @Test
    public void testAddWithEmptyCache() {
        final Show show = SpringEntitiesUtils.newShow(objectGenerator, entityManager);

        showService.add(show);

        DeepAsserts.assertNotNull(show.getId());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, show.getId());
        final Show addedShow = SpringUtils.getShow(entityManager, SpringUtils.SHOWS_COUNT + 1);
        DeepAsserts.assertEquals(show, addedShow);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link ShowService#add(Show)} with not empty cache.
     */
    @Test
    public void testAddWithNotEmptyCache() {
        final Show show = SpringEntitiesUtils.newShow(objectGenerator, entityManager);
        final String keyList = SHOWS_CACHE_KEY;
        final String keyItem = SHOW_CACHE_KEY + (SpringUtils.SHOWS_COUNT + 1);
        showCache.put(keyList, new ArrayList<>());
        showCache.put(keyItem, null);

        showService.add(show);

        DeepAsserts.assertNotNull(show.getId());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, show.getId());
        final Show addedShow = SpringUtils.getShow(entityManager, SpringUtils.SHOWS_COUNT + 1);
        DeepAsserts.assertEquals(show, addedShow);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, keyList, CollectionUtils.newList(show));
        SpringUtils.assertCacheValue(showCache, keyItem, show);
    }

    /**
     * Test method for {@link ShowService#update(Show)}.
     */
    @Test
    public void testUpdate() {
        final Show show = SpringEntitiesUtils.updateShow(1, objectGenerator, entityManager);

        showService.update(show);

        final Show updatedShow = SpringUtils.getShow(entityManager, 1);
        DeepAsserts.assertEquals(show, updatedShow);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link ShowService#remove(Show)} with empty cache.
     */
    @Test
    public void testRemoveWithEmptyCache() {
        final Show show = SpringEntitiesUtils.newShow(objectGenerator, entityManager);
        entityManager.persist(show);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));

        showService.remove(show);

        assertNull(SpringUtils.getShow(entityManager, show.getId()));
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link ShowService#remove(Show)} with not empty cache.
     */
    @Test
    public void testRemoveWithNotEmptyCache() {
        final Show show = objectGenerator.generate(Show.class);
        show.setId(null);
        show.setGenres(CollectionUtils.newList(SpringUtils.getGenre(entityManager, 4)));
        entityManager.persist(show);
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));
        final String key = SHOWS_CACHE_KEY;
        final List<Show> cacheShows = new ArrayList<>();
        cacheShows.add(show);
        showCache.put(key, cacheShows);

        showService.remove(show);

        assertNull(SpringUtils.getShow(entityManager, show.getId()));
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link ShowService#duplicate(Show)} with empty cache.
     */
    @Test
    public void testDuplicateWithEmptyCache() {
        final Show show = SpringUtils.getShow(entityManager, 3);
        final Show expectedShow = SpringEntitiesUtils.getShow(3);
        expectedShow.setId(SpringUtils.SHOWS_COUNT + 1);

        showService.duplicate(show);

        DeepAsserts.assertEquals(expectedShow, SpringUtils.getShow(entityManager, SpringUtils.SHOWS_COUNT + 1));
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), SpringUtils.getShow(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link ShowService#duplicate(Show)} with not empty cache.
     */
    @Test
    public void testDuplicateWithNotEmptyCache() {
        final Show show = SpringUtils.getShow(entityManager, 3);
        final Show expectedShow = SpringEntitiesUtils.getShow(3);
        expectedShow.setId(SpringUtils.SHOWS_COUNT + 1);
        final String keyList = SHOWS_CACHE_KEY;
        final String keyItem = SHOW_CACHE_KEY + (SpringUtils.SHOWS_COUNT + 1);
        showCache.put(keyList, new ArrayList<>());
        showCache.put(keyItem, null);

        showService.duplicate(show);

        DeepAsserts.assertEquals(expectedShow, SpringUtils.getShow(entityManager, SpringUtils.SHOWS_COUNT + 1));
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), SpringUtils.getShow(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT + 1, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link ShowService#moveUp(Show)}.
     */
    @Test
    public void testMoveUp() {
        final Show show = SpringUtils.getShow(entityManager, 2);
        final Show expectedShow1 = SpringEntitiesUtils.getShow(1);
        expectedShow1.setPosition(1);
        final Show expectedShow2 = SpringEntitiesUtils.getShow(2);
        expectedShow2.setPosition(0);

        showService.moveUp(show);

        DeepAsserts.assertEquals(expectedShow1, SpringUtils.getShow(entityManager, 1));
        DeepAsserts.assertEquals(expectedShow2, SpringUtils.getShow(entityManager, 2));
        for (int i = 3; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), SpringUtils.getShow(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link ShowService#moveDown(Show)}.
     */
    @Test
    public void testMoveDown() {
        final Show show = SpringUtils.getShow(entityManager, 1);
        final Show expectedShow1 = SpringEntitiesUtils.getShow(1);
        expectedShow1.setPosition(1);
        final Show expectedShow2 = SpringEntitiesUtils.getShow(2);
        expectedShow2.setPosition(0);

        showService.moveDown(show);

        DeepAsserts.assertEquals(expectedShow1, SpringUtils.getShow(entityManager, 1));
        DeepAsserts.assertEquals(expectedShow2, SpringUtils.getShow(entityManager, 2));
        for (int i = 3; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), SpringUtils.getShow(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }


    /**
     * Test method for {@link ShowService#exists(Show)} with existing show.
     */
    @Test
    public void testExistsWithExistingShow() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            keys.add(SHOW_CACHE_KEY + i);
        }

        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            assertTrue(showService.exists(SpringEntitiesUtils.getShow(i)));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(showCache));
        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            SpringUtils.assertCacheValue(showCache, keys.get(i - 1), SpringEntitiesUtils.getShow(i));
        }
    }

    /**
     * Test method for {@link ShowService#exists(Show)} with not existing show.
     */
    @Test
    public void testExistsWithNotExistingShow() {
        final Show show = SpringEntitiesUtils.newShow(objectGenerator, entityManager);
        show.setId(Integer.MAX_VALUE);
        final String key = SHOW_CACHE_KEY + Integer.MAX_VALUE;

        assertFalse(showService.exists(show));
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, key, null);
    }

    /**
     * Test method for {@link ShowService#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        final Show show = SpringUtils.getShow(entityManager, SpringUtils.SHOWS_COUNT);
        show.setPosition(objectGenerator.generate(Integer.class));
        entityManager.merge(show);

        showService.updatePositions();

        for (int i = 1; i <= SpringUtils.SHOWS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getShow(i), SpringUtils.getShow(entityManager, i));
        }
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(showCache).size());
    }

    /**
     * Test method for {@link ShowService#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final String keyList = SHOWS_CACHE_KEY;
        final List<String> seasonKeyItems = new ArrayList<>();
        final int seasonsCount = SpringUtils.SEASONS_COUNT / SpringUtils.SHOWS_COUNT;
        for (int i = 1; i <= seasonsCount; i++) {
            seasonKeyItems.add(SEASONS_CACHE_KEY + i);
        }
        final List<String> episodeKeyItems = new ArrayList<>();
        final int episodesCount = SpringUtils.EPISODES_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT;
        for (int i = 1; i <= episodesCount; i++) {
            episodeKeyItems.add(EPISODES_CACHE_KEY + i);
        }
        final List<String> keys = new ArrayList<>();
        keys.add(keyList);
        keys.addAll(seasonKeyItems);
        keys.addAll(episodeKeyItems);
        final Time length = new Time(1998);

        DeepAsserts.assertEquals(length, showService.getTotalLength());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, keyList, SpringEntitiesUtils.getShows());
        for (int i = 1; i <= seasonsCount; i++) {
            SpringUtils.assertCacheValue(showCache, seasonKeyItems.get(i - 1), SpringEntitiesUtils.getSeasons(i));
        }
        for (int i = 0; i < episodesCount; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            SpringUtils.assertCacheValue(showCache, episodeKeyItems.get(i), SpringEntitiesUtils.getEpisodes(showNumber, seasonNumber));
        }
    }

    /**
     * Test method for {@link ShowService#getSeasonsCount()}.
     */
    @Test
    public void testGetSeasonsCount() {
        final String keyList = SHOWS_CACHE_KEY;
        final List<String> keyItems = new ArrayList<>();
        final int count = SpringUtils.SEASONS_COUNT / SpringUtils.SHOWS_COUNT;
        for (int i = 1; i <= count; i++) {
            keyItems.add(SEASONS_CACHE_KEY + i);
        }
        final List<String> keys = new ArrayList<>();
        keys.add(keyList);
        keys.addAll(keyItems);

        DeepAsserts.assertEquals(SpringUtils.SEASONS_COUNT, showService.getSeasonsCount());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, keyList, SpringEntitiesUtils.getShows());
        for (int i = 1; i <= count; i++) {
            SpringUtils.assertCacheValue(showCache, keyItems.get(i - 1), SpringEntitiesUtils.getSeasons(i));
        }
    }

    /**
     * Test method for {@link ShowService#getEpisodesCount()}.
     */
    @Test
    public void testGetEpisodesCount() {
        final String keyList = SHOWS_CACHE_KEY;
        final List<String> seasonKeyItems = new ArrayList<>();
        final int seasonsCount = SpringUtils.SEASONS_COUNT / SpringUtils.SHOWS_COUNT;
        for (int i = 1; i <= seasonsCount; i++) {
            seasonKeyItems.add(SEASONS_CACHE_KEY + i);
        }
        final List<String> episodeKeyItems = new ArrayList<>();
        final int episodesCount = SpringUtils.EPISODES_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT;
        for (int i = 1; i <= episodesCount; i++) {
            episodeKeyItems.add(EPISODES_CACHE_KEY + i);
        }
        final List<String> keys = new ArrayList<>();
        keys.add(keyList);
        keys.addAll(seasonKeyItems);
        keys.addAll(episodeKeyItems);

        DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, showService.getEpisodesCount());
        DeepAsserts.assertEquals(SpringUtils.SHOWS_COUNT, SpringUtils.getShowsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(showCache));
        SpringUtils.assertCacheValue(showCache, keyList, SpringEntitiesUtils.getShows());
        for (int i = 1; i <= seasonsCount; i++) {
            SpringUtils.assertCacheValue(showCache, seasonKeyItems.get(i - 1), SpringEntitiesUtils.getSeasons(i));
        }
        for (int i = 0; i < episodesCount; i++) {
            final int showNumber = i / SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SpringUtils.SEASONS_PER_SHOW_COUNT + 1;
            SpringUtils.assertCacheValue(showCache, episodeKeyItems.get(i), SpringEntitiesUtils.getEpisodes(showNumber, seasonNumber));
        }
    }

}
