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
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.service.MusicService;
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
 * A class represents test for class {@link cz.vhromada.catalog.service.impl.MusicServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class MusicServiceImplSpringTest {

	/** Cache key for list of music */
	private static final String MUSIC_LIST_CACHE_KEY = "music";

	/** Cache key for music */
	private static final String MUSIC_CACHE_KEY = "musicItem";

	/** Cache key for list of songs */
	private static final String SONGS_CACHE_KEY = "songs";

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('musicCache')}")
	private Cache musicCache;

	/** Instance of {@link MusicService} */
	@Autowired
	private MusicService musicService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Clears cache and restarts sequences. */
	@Before
	public void setUp() {
		musicCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE music_sq RESTART WITH 4").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE songs_sq RESTART WITH 10").executeUpdate();
	}

	/** Test method for {@link MusicService#newData()}. */
	@Test
	public void testNewData() {
		musicService.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getSongsCount(entityManager));
		assertTrue(SpringUtils.getCacheKeys(musicCache).isEmpty());
	}

	/** Test method for {@link MusicService#getMusic()}. */
	@Test
	public void testGetMusic() {
		final List<Music> music = SpringEntitiesUtils.getMusic();
		final String key = MUSIC_LIST_CACHE_KEY;

		DeepAsserts.assertEquals(music, musicService.getMusic());
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(musicCache));
		SpringUtils.assertCacheValue(musicCache, key, music);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with existing music. */
	@Test
	public void testGetMusicByIdWithExistingMusic() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			keys.add(MUSIC_CACHE_KEY + i);
		}

		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), musicService.getMusic(i));
		}
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(musicCache));
		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			SpringUtils.assertCacheValue(musicCache, keys.get(i - 1), SpringEntitiesUtils.getMusic(i));
		}
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with not existing music. */
	@Test
	public void testGetMusicByIdWithNotExistingMusic() {
		final String key = MUSIC_CACHE_KEY + Integer.MAX_VALUE;

		assertNull(musicService.getMusic(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(musicCache));
		SpringUtils.assertCacheValue(musicCache, key, null);
	}

	/** Test method for {@link MusicService#add(Music)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final Music music = SpringEntitiesUtils.newMusic(objectGenerator);

		musicService.add(music);

		DeepAsserts.assertNotNull(music.getId());
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, music.getId());
		final Music addedMusic = SpringUtils.getMusic(entityManager, SpringUtils.MUSIC_COUNT + 1);
		DeepAsserts.assertEquals(music, addedMusic);
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}

	/** Test method for {@link MusicService#add(Music)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final Music music = SpringEntitiesUtils.newMusic(objectGenerator);
		final String keyList = MUSIC_LIST_CACHE_KEY;
		final String keyItem = MUSIC_CACHE_KEY + (SpringUtils.MUSIC_COUNT + 1);
		musicCache.put(keyList, new ArrayList<>());
		musicCache.put(keyItem, null);

		musicService.add(music);

		DeepAsserts.assertNotNull(music.getId());
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, music.getId());
		final Music addedMusic = SpringUtils.getMusic(entityManager, SpringUtils.MUSIC_COUNT + 1);
		DeepAsserts.assertEquals(music, addedMusic);
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(musicCache));
		SpringUtils.assertCacheValue(musicCache, keyList, CollectionUtils.newList(music));
		SpringUtils.assertCacheValue(musicCache, keyItem, music);
	}

	/** Test method for {@link MusicService#update(Music)}. */
	@Test
	public void testUpdate() {
		final Music music = SpringEntitiesUtils.updateMusic(1, objectGenerator, entityManager);

		musicService.update(music);

		final Music updatedMusic = SpringUtils.getMusic(entityManager, 1);
		DeepAsserts.assertEquals(music, updatedMusic);
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}

	/** Test method for {@link MusicService#remove(Music)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final Music music = SpringEntitiesUtils.newMusic(objectGenerator);
		entityManager.persist(music);
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));

		musicService.remove(music);

		assertNull(SpringUtils.getMusic(entityManager, music.getId()));
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}

	/** Test method for {@link MusicService#remove(Music)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final Music music = SpringEntitiesUtils.newMusic(objectGenerator);
		entityManager.persist(music);
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
		final String key = MUSIC_LIST_CACHE_KEY;
		final List<Music> cacheMusic = new ArrayList<>();
		cacheMusic.add(music);
		musicCache.put(key, cacheMusic);

		musicService.remove(music);

		assertNull(SpringUtils.getMusic(entityManager, music.getId()));
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}

	/** Test method for {@link MusicService#duplicate(Music)} with empty cache. */
	@Test
	public void testDuplicateWithEmptyCache() {
		final Music music = SpringUtils.getMusic(entityManager, 3);
		final Music expectedMusic = SpringEntitiesUtils.getMusic(3);
		expectedMusic.setId(SpringUtils.MUSIC_COUNT + 1);

		musicService.duplicate(music);

		DeepAsserts.assertEquals(expectedMusic, SpringUtils.getMusic(entityManager, SpringUtils.MUSIC_COUNT + 1));
		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}

	/** Test method for {@link MusicService#duplicate(Music)} with not empty cache. */
	@Test
	public void testDuplicateWithNotEmptyCache() {
		final Music music = SpringUtils.getMusic(entityManager, 3);
		final Music expectedMusic = SpringEntitiesUtils.getMusic(3);
		expectedMusic.setId(SpringUtils.MUSIC_COUNT + 1);
		final String keyList = MUSIC_LIST_CACHE_KEY;
		final String keyItem = MUSIC_CACHE_KEY + (SpringUtils.MUSIC_COUNT + 1);
		musicCache.put(keyList, new ArrayList<>());
		musicCache.put(keyItem, null);

		musicService.duplicate(music);

		DeepAsserts.assertEquals(expectedMusic, SpringUtils.getMusic(entityManager, SpringUtils.MUSIC_COUNT + 1));
		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}

	/** Test method for {@link MusicService#moveUp(Music)}. */
	@Test
	public void testMoveUp() {
		final Music music = SpringUtils.getMusic(entityManager, 2);
		final Music expectedMusic1 = SpringEntitiesUtils.getMusic(1);
		expectedMusic1.setPosition(1);
		final Music expectedMusic2 = SpringEntitiesUtils.getMusic(2);
		expectedMusic2.setPosition(0);

		musicService.moveUp(music);

		DeepAsserts.assertEquals(expectedMusic1, SpringUtils.getMusic(entityManager, 1));
		DeepAsserts.assertEquals(expectedMusic2, SpringUtils.getMusic(entityManager, 2));
		for (int i = 3; i <= SpringUtils.MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}

	/** Test method for {@link MusicService#moveDown(Music)}. */
	@Test
	public void testMoveDown() {
		final Music music = SpringUtils.getMusic(entityManager, 1);
		final Music expectedMusic1 = SpringEntitiesUtils.getMusic(1);
		expectedMusic1.setPosition(1);
		final Music expectedMusic2 = SpringEntitiesUtils.getMusic(2);
		expectedMusic2.setPosition(0);

		musicService.moveDown(music);

		DeepAsserts.assertEquals(expectedMusic1, SpringUtils.getMusic(entityManager, 1));
		DeepAsserts.assertEquals(expectedMusic2, SpringUtils.getMusic(entityManager, 2));
		for (int i = 3; i <= SpringUtils.MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}


	/** Test method for {@link MusicService#exists(Music)} with existing music. */
	@Test
	public void testExistsWithExistingMusic() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			keys.add(MUSIC_CACHE_KEY + i);
		}

		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			assertTrue(musicService.exists(SpringEntitiesUtils.getMusic(i)));
		}
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(musicCache));
		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			SpringUtils.assertCacheValue(musicCache, keys.get(i - 1), SpringEntitiesUtils.getMusic(i));
		}
	}

	/** Test method for {@link MusicService#exists(Music)} with not existing music. */
	@Test
	public void testExistsWithNotExistingMusic() {
		final Music music = SpringEntitiesUtils.newMusic(objectGenerator);
		music.setId(Integer.MAX_VALUE);
		final String key = MUSIC_CACHE_KEY + Integer.MAX_VALUE;

		assertFalse(musicService.exists(music));
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(musicCache));
		SpringUtils.assertCacheValue(musicCache, key, null);
	}

	/** Test method for {@link MusicService#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		final Music music = SpringUtils.getMusic(entityManager, SpringUtils.MUSIC_COUNT);
		music.setPosition(5000);
		entityManager.merge(music);

		musicService.updatePositions();

		for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
	}

	/** Test method for {@link MusicService#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		final List<Music> music = SpringEntitiesUtils.getMusic();
		final String key = MUSIC_LIST_CACHE_KEY;
		final int mediaCount = 60;

		DeepAsserts.assertEquals(mediaCount, musicService.getTotalMediaCount());
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(musicCache));
		SpringUtils.assertCacheValue(musicCache, key, music);
	}

	/** Test method for {@link MusicService#getTotalLength()}. */
	@Test
	public void testGetTotalLength() {
		final String keyList = MUSIC_LIST_CACHE_KEY;
		final List<String> keyItems = new ArrayList<>();
		final int count = SpringUtils.SONGS_COUNT / SpringUtils.MUSIC_COUNT;
		for (int i = 1; i <= count; i++) {
			keyItems.add(SONGS_CACHE_KEY + i);
		}
		final List<String> keys = new ArrayList<>();
		keys.add(keyList);
		keys.addAll(keyItems);
		final Time length = new Time(666);

		DeepAsserts.assertEquals(length, musicService.getTotalLength());
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(musicCache));
		SpringUtils.assertCacheValue(musicCache, keyList, SpringEntitiesUtils.getMusic());
		for (int i = 1; i <= count; i++) {
			SpringUtils.assertCacheValue(musicCache, keyItems.get(i - 1), SpringEntitiesUtils.getSongs(i));
		}
	}

	/** Test method for {@link MusicService#getSongsCount()}. */
	@Test
	public void testGetSongsCount() {
		final String keyList = MUSIC_LIST_CACHE_KEY;
		final List<String> keyItems = new ArrayList<>();
		final int count = SpringUtils.SONGS_COUNT / SpringUtils.MUSIC_COUNT;
		for (int i = 1; i <= count; i++) {
			keyItems.add(SONGS_CACHE_KEY + i);
		}
		final List<String> keys = new ArrayList<>();
		keys.add(keyList);
		keys.addAll(keyItems);

		DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, musicService.getSongsCount());
		DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(musicCache));
		SpringUtils.assertCacheValue(musicCache, keyList, SpringEntitiesUtils.getMusic());
		for (int i = 1; i <= count; i++) {
			SpringUtils.assertCacheValue(musicCache, keyItems.get(i - 1), SpringEntitiesUtils.getSongs(i));
		}
	}

}
