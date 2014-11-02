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
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.service.EpisodeService;
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
 * A class represents test for class {@link cz.vhromada.catalog.service.impl.EpisodeServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class EpisodeServiceImplSpringTest {

	/** Cache key for list of episodes */
	private static final String EPISODES_CACHE_KEY = "episodes";

	/** Cache key for episode */
	private static final String EPISODE_CACHE_KEY = "episode";

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('serieCache')}")
	private Cache serieCache;

	/** Instance of {@link EpisodeService} */
	@Autowired
	private EpisodeService episodeService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Clears cache and restarts sequence. */
	@Before
	public void setUp() {
		serieCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE episodes_sq RESTART WITH 28").executeUpdate();
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with existing episode. */
	@Test
	public void testGetEpisodeWithExistingEpisode() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SpringUtils.EPISODES_COUNT; i++) {
			keys.add(EPISODE_CACHE_KEY + i);
		}

		for (int i = 0; i < SpringUtils.EPISODES_COUNT; i++) {
			final int serieNumber = i / SpringUtils.EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.EPISODES_PER_SERIE_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber), episodeService.getEpisode(i + 1));
		}
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 0; i < SpringUtils.EPISODES_COUNT; i++) {
			final int serieNumber = i / SpringUtils.EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.EPISODES_PER_SERIE_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			SpringUtils.assertCacheValue(serieCache, keys.get(i), SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber));
		}
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with not existing episode. */
	@Test
	public void testGetEpisodeWithNotExistingEpisode() {
		final String key = EPISODE_CACHE_KEY + Integer.MAX_VALUE;

		assertNull(episodeService.getEpisode(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, key, null);
	}

	/** Test method for {@link EpisodeService#add(Episode)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final Episode episode = SpringEntitiesUtils.newEpisode(objectGenerator, entityManager);

		episodeService.add(episode);

		DeepAsserts.assertNotNull(episode.getId());
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, episode.getId());
		final Episode addedEpisode = SpringUtils.getEpisode(entityManager, SpringUtils.EPISODES_COUNT + 1);
		DeepAsserts.assertEquals(episode, addedEpisode);
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link EpisodeService#add(Episode)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final Episode episode = SpringEntitiesUtils.newEpisode(objectGenerator, entityManager);
		final String keyList = EPISODES_CACHE_KEY + episode.getSeason().getId();
		final String keyItem = EPISODE_CACHE_KEY + (SpringUtils.EPISODES_COUNT + 1);
		serieCache.put(keyList, new ArrayList<>());
		serieCache.put(keyItem, null);

		episodeService.add(episode);

		DeepAsserts.assertNotNull(episode.getId());
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, episode.getId());
		final Episode addedEpisode = SpringUtils.getEpisode(entityManager, SpringUtils.EPISODES_COUNT + 1);
		DeepAsserts.assertEquals(episode, addedEpisode);
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, keyList, CollectionUtils.newList(episode));
		SpringUtils.assertCacheValue(serieCache, keyItem, episode);
	}

	/** Test method for {@link EpisodeService#update(Episode)}. */
	@Test
	public void testUpdate() {
		final Episode episode = SpringEntitiesUtils.updateEpisode(1, objectGenerator, entityManager);

		episodeService.update(episode);

		final Episode updatedEpisode = SpringUtils.getEpisode(entityManager, 1);
		DeepAsserts.assertEquals(episode, updatedEpisode);
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link EpisodeService#remove(Episode)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final Episode episode = SpringEntitiesUtils.newEpisode(objectGenerator, entityManager);
		entityManager.persist(episode);
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));

		episodeService.remove(episode);

		assertNull(SpringUtils.getEpisode(entityManager, episode.getId()));
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link EpisodeService#remove(Episode)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final Episode episode = SpringEntitiesUtils.newEpisode(objectGenerator, entityManager);
		entityManager.persist(episode);
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
		final String key = EPISODES_CACHE_KEY + episode.getSeason().getId();
		final List<Episode> cacheEpisodes = new ArrayList<>();
		cacheEpisodes.add(episode);
		serieCache.put(key, cacheEpisodes);

		episodeService.remove(episode);

		assertNull(SpringUtils.getEpisode(entityManager, episode.getId()));
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, key, new ArrayList<>());
	}

	/** Test method for {@link EpisodeService#duplicate(Episode)} with empty cache. */
	@Test
	public void testDuplicateWithEmptyCache() {
		final Episode episode = SpringUtils.getEpisode(entityManager, 3);
		final Episode expectedEpisode = SpringEntitiesUtils.getEpisode(1, 1, 3);
		expectedEpisode.setId(SpringUtils.EPISODES_COUNT + 1);

		episodeService.duplicate(episode);

		DeepAsserts.assertEquals(expectedEpisode, SpringUtils.getEpisode(entityManager, SpringUtils.EPISODES_COUNT + 1));
		for (int i = 0; i < SpringUtils.EPISODES_COUNT; i++) {
			final int serieNumber = i / SpringUtils.EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.EPISODES_PER_SERIE_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber), SpringUtils.getEpisode(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link EpisodeService#duplicate(Episode)} with not empty cache. */
	@Test
	public void testDuplicateWithNotEmptyCache() {
		final Episode episode = SpringUtils.getEpisode(entityManager, 3);
		final Episode expectedEpisode = SpringEntitiesUtils.getEpisode(1, 1, 3);
		expectedEpisode.setId(SpringUtils.EPISODES_COUNT + 1);
		final String keyList = EPISODES_CACHE_KEY + episode.getSeason().getId();
		final String keyItem = EPISODE_CACHE_KEY + (SpringUtils.EPISODES_COUNT + 1);
		serieCache.put(keyList, new ArrayList<>());
		serieCache.put(keyItem, null);

		episodeService.duplicate(episode);

		DeepAsserts.assertEquals(expectedEpisode, SpringUtils.getEpisode(entityManager, SpringUtils.EPISODES_COUNT + 1));
		for (int i = 0; i < SpringUtils.EPISODES_COUNT; i++) {
			final int serieNumber = i / SpringUtils.EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.EPISODES_PER_SERIE_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber), SpringUtils.getEpisode(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT + 1, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, keyList, CollectionUtils.newList(expectedEpisode));
		SpringUtils.assertCacheValue(serieCache, keyItem, expectedEpisode);
	}

	/** Test method for {@link EpisodeService#moveUp(Episode)}. */
	@Test
	public void testMoveUp() {
		final Episode episode = SpringUtils.getEpisode(entityManager, 2);
		final Episode expectedEpisode1 = SpringEntitiesUtils.getEpisode(1, 1, 1);
		expectedEpisode1.setPosition(1);
		final Episode expectedEpisode2 = SpringEntitiesUtils.getEpisode(1, 1, 2);
		expectedEpisode2.setPosition(0);

		episodeService.moveUp(episode);

		DeepAsserts.assertEquals(expectedEpisode1, SpringUtils.getEpisode(entityManager, 1));
		DeepAsserts.assertEquals(expectedEpisode2, SpringUtils.getEpisode(entityManager, 2));
		for (int i = 2; i < SpringUtils.EPISODES_COUNT; i++) {
			final int serieNumber = i / SpringUtils.EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.EPISODES_PER_SERIE_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber), SpringUtils.getEpisode(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}

	/** Test method for {@link EpisodeService#moveDown(Episode)}. */
	@Test
	public void testMoveDown() {
		final Episode episode = SpringUtils.getEpisode(entityManager, 1);
		final Episode expectedEpisode1 = SpringEntitiesUtils.getEpisode(1, 1, 1);
		expectedEpisode1.setPosition(1);
		final Episode expectedEpisode2 = SpringEntitiesUtils.getEpisode(1, 1, 2);
		expectedEpisode2.setPosition(0);

		episodeService.moveDown(episode);

		DeepAsserts.assertEquals(expectedEpisode1, SpringUtils.getEpisode(entityManager, 1));
		DeepAsserts.assertEquals(expectedEpisode2, SpringUtils.getEpisode(entityManager, 2));
		for (int i = 2; i < SpringUtils.EPISODES_COUNT; i++) {
			final int serieNumber = i / SpringUtils.EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.EPISODES_PER_SERIE_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber), SpringUtils.getEpisode(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(serieCache).size());
	}


	/** Test method for {@link EpisodeService#exists(Episode)} with existing episode. */
	@Test
	public void testExistsWithExistingEpisode() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SpringUtils.EPISODES_COUNT; i++) {
			keys.add(EPISODE_CACHE_KEY + i);
		}

		for (int i = 0; i < SpringUtils.EPISODES_COUNT; i++) {
			final int serieNumber = i / SpringUtils.EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.EPISODES_PER_SERIE_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			assertTrue(episodeService.exists(SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber)));
		}
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 0; i < SpringUtils.EPISODES_COUNT; i++) {
			final int serieNumber = i / SpringUtils.EPISODES_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.EPISODES_PER_SERIE_COUNT / SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			final int episodeNumber = i % SpringUtils.EPISODES_PER_SEASON_COUNT + 1;
			SpringUtils.assertCacheValue(serieCache, keys.get(i), SpringEntitiesUtils.getEpisode(serieNumber, seasonNumber, episodeNumber));
		}
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with not existing episode. */
	@Test
	public void testExistsWithNotExistingEpisode() {
		final Episode episode = SpringEntitiesUtils.newEpisode(objectGenerator, entityManager);
		episode.setId(Integer.MAX_VALUE);
		final String key = EPISODE_CACHE_KEY + Integer.MAX_VALUE;

		assertFalse(episodeService.exists(episode));
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(serieCache));
		SpringUtils.assertCacheValue(serieCache, key, null);
	}

	/** Test method for {@link EpisodeService#findEpisodesBySeason(Season)}. */
	@Test
	public void testFindEpisodesBySeason() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SpringUtils.SEASONS_COUNT; i++) {
			keys.add(EPISODES_CACHE_KEY + i);
		}

		for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
			final Season season = SpringUtils.getSeason(entityManager, i + 1);
			final int seasonNumber = i % SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getEpisodes(season.getSerie().getId(), seasonNumber), episodeService.findEpisodesBySeason(season));
		}
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
			final int serieNumber = i / SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
			SpringUtils.assertCacheValue(serieCache, keys.get(i), SpringEntitiesUtils.getEpisodes(serieNumber, seasonNumber));
		}
	}

	/** Test method for {@link EpisodeService#getTotalLengthBySeason(Season)}. */
	@Test
	public void testGetTotalLengthBySeason() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SpringUtils.SEASONS_COUNT; i++) {
			keys.add(EPISODES_CACHE_KEY + i);
		}

		for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
			final Season season = SpringUtils.getSeason(entityManager, i + 1);
			final int seasonNumber = i % SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
			DeepAsserts.assertEquals(new Time(6 * SpringUtils.LENGTH_MULTIPLIERS[seasonNumber - 1]), episodeService.getTotalLengthBySeason(season));
		}
		DeepAsserts.assertEquals(SpringUtils.EPISODES_COUNT, SpringUtils.getEpisodesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(serieCache));
		for (int i = 0; i < SpringUtils.SEASONS_COUNT; i++) {
			final int serieNumber = i / SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
			final int seasonNumber = i % SpringUtils.SEASONS_PER_SERIE_COUNT + 1;
			SpringUtils.assertCacheValue(serieCache, keys.get(i), SpringEntitiesUtils.getEpisodes(serieNumber, seasonNumber));
		}
	}

}
