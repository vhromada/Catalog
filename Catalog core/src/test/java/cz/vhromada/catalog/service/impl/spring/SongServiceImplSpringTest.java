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
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.service.SongService;
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
 * A class represents test for class {@link cz.vhromada.catalog.service.impl.SongServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class SongServiceImplSpringTest {

    /** Cache key for list of songs */
    private static final String SONGS_CACHE_KEY = "songs";

    /** Cache key for song */
    private static final String SONG_CACHE_KEY = "song";

    /** Instance of {@link EntityManager} */
    @Autowired
    private EntityManager entityManager;

    /** Instance of {@link Cache} */
    @Value("#{cacheManager.getCache('musicCache')}")
    private Cache musicCache;

    /** Instance of {@link SongService} */
    @Autowired
    private SongService songService;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Clears cache and restarts sequence. */
    @Before
    public void setUp() {
        musicCache.clear();
        entityManager.createNativeQuery("ALTER SEQUENCE songs_sq RESTART WITH 10").executeUpdate();
    }

    /** Test method for {@link SongService#getSong(Integer)} with existing song. */
    @Test
    public void testGetSongWithExistingSong() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.SONGS_COUNT; i++) {
            keys.add(SONG_CACHE_KEY + i);
        }

        for (int i = 0; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), songService.getSong(i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(musicCache));
        for (int i = 0; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            SpringUtils.assertCacheValue(musicCache, keys.get(i), SpringEntitiesUtils.getSong(musicNumber, songNumber));
        }
    }

    /** Test method for {@link SongService#getSong(Integer)} with not existing song. */
    @Test
    public void testGetSongWithNotExistingSong() {
        final String key = SONG_CACHE_KEY + Integer.MAX_VALUE;

        assertNull(songService.getSong(Integer.MAX_VALUE));
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(musicCache));
        SpringUtils.assertCacheValue(musicCache, key, null);
    }

    /** Test method for {@link SongService#add(Song)} with empty cache. */
    @Test
    public void testAddWithEmptyCache() {
        final Song song = SpringEntitiesUtils.newSong(objectGenerator, entityManager);

        songService.add(song);

        DeepAsserts.assertNotNull(song.getId());
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, song.getId());
        final Song addedSong = SpringUtils.getSong(entityManager, SpringUtils.SONGS_COUNT + 1);
        DeepAsserts.assertEquals(song, addedSong);
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
    }

    /** Test method for {@link SongService#add(Song)} with not empty cache. */
    @Test
    public void testAddWithNotEmptyCache() {
        final Song song = SpringEntitiesUtils.newSong(objectGenerator, entityManager);
        final String keyList = SONGS_CACHE_KEY + song.getMusic().getId();
        final String keyItem = SONG_CACHE_KEY + (SpringUtils.SONGS_COUNT + 1);
        musicCache.put(keyList, new ArrayList<>());
        musicCache.put(keyItem, null);

        songService.add(song);

        DeepAsserts.assertNotNull(song.getId());
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, song.getId());
        final Song addedSong = SpringUtils.getSong(entityManager, SpringUtils.SONGS_COUNT + 1);
        DeepAsserts.assertEquals(song, addedSong);
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(musicCache));
        SpringUtils.assertCacheValue(musicCache, keyList, CollectionUtils.newList(song));
        SpringUtils.assertCacheValue(musicCache, keyItem, song);
    }

    /** Test method for {@link SongService#update(Song)}. */
    @Test
    public void testUpdate() {
        final Song song = SpringEntitiesUtils.updateSong(1, objectGenerator, entityManager);

        songService.update(song);

        final Song updatedSong = SpringUtils.getSong(entityManager, 1);
        DeepAsserts.assertEquals(song, updatedSong);
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
    }

    /** Test method for {@link SongService#remove(Song)} with empty cache. */
    @Test
    public void testRemoveWithEmptyCache() {
        final Song song = SpringEntitiesUtils.newSong(objectGenerator, entityManager);
        entityManager.persist(song);
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));

        songService.remove(song);

        assertNull(SpringUtils.getSong(entityManager, song.getId()));
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
    }

    /** Test method for {@link SongService#remove(Song)} with not empty cache. */
    @Test
    public void testRemoveWithNotEmptyCache() {
        final Song song = SpringEntitiesUtils.newSong(objectGenerator, entityManager);
        entityManager.persist(song);
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
        final String key = SONGS_CACHE_KEY + song.getMusic().getId();
        final List<Song> cacheSongs = new ArrayList<>();
        cacheSongs.add(song);
        musicCache.put(key, cacheSongs);

        songService.remove(song);

        assertNull(SpringUtils.getSong(entityManager, song.getId()));
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(musicCache));
        SpringUtils.assertCacheValue(musicCache, key, new ArrayList<>());
    }

    /** Test method for {@link SongService#duplicate(Song)} with empty cache. */
    @Test
    public void testDuplicateWithEmptyCache() {
        final Song song = SpringUtils.getSong(entityManager, 3);
        final Song expectedSong = SpringEntitiesUtils.getSong(1, 3);
        expectedSong.setId(SpringUtils.SONGS_COUNT + 1);

        songService.duplicate(song);

        DeepAsserts.assertEquals(expectedSong, SpringUtils.getSong(entityManager, SpringUtils.SONGS_COUNT + 1));
        for (int i = 0; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), SpringUtils.getSong(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
    }

    /** Test method for {@link SongService#duplicate(Song)} with not empty cache. */
    @Test
    public void testDuplicateWithNotEmptyCache() {
        final Song song = SpringUtils.getSong(entityManager, 3);
        final Song expectedSong = SpringEntitiesUtils.getSong(1, 3);
        expectedSong.setId(SpringUtils.SONGS_COUNT + 1);
        final String keyList = SONGS_CACHE_KEY + song.getMusic().getId();
        final String keyItem = SONG_CACHE_KEY + (SpringUtils.SONGS_COUNT + 1);
        musicCache.put(keyList, new ArrayList<>());
        musicCache.put(keyItem, null);

        songService.duplicate(song);

        DeepAsserts.assertEquals(expectedSong, SpringUtils.getSong(entityManager, SpringUtils.SONGS_COUNT + 1));
        for (int i = 0; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), SpringUtils.getSong(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT + 1, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(musicCache));
        SpringUtils.assertCacheValue(musicCache, keyList, CollectionUtils.newList(expectedSong));
        SpringUtils.assertCacheValue(musicCache, keyItem, expectedSong);
    }

    /** Test method for {@link SongService#moveUp(Song)}. */
    @Test
    public void testMoveUp() {
        final Song song = SpringUtils.getSong(entityManager, 2);
        final Song expectedSong1 = SpringEntitiesUtils.getSong(1, 1);
        expectedSong1.setPosition(1);
        final Song expectedSong2 = SpringEntitiesUtils.getSong(1, 2);
        expectedSong2.setPosition(0);

        songService.moveUp(song);

        DeepAsserts.assertEquals(expectedSong1, SpringUtils.getSong(entityManager, 1));
        DeepAsserts.assertEquals(expectedSong2, SpringUtils.getSong(entityManager, 2));
        for (int i = 2; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), SpringUtils.getSong(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
    }

    /** Test method for {@link SongService#moveDown(Song)}. */
    @Test
    public void testMoveDown() {
        final Song song = SpringUtils.getSong(entityManager, 1);
        final Song expectedSong1 = SpringEntitiesUtils.getSong(1, 1);
        expectedSong1.setPosition(1);
        final Song expectedSong2 = SpringEntitiesUtils.getSong(1, 2);
        expectedSong2.setPosition(0);

        songService.moveDown(song);

        DeepAsserts.assertEquals(expectedSong1, SpringUtils.getSong(entityManager, 1));
        DeepAsserts.assertEquals(expectedSong2, SpringUtils.getSong(entityManager, 2));
        for (int i = 2; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSong(musicNumber, songNumber), SpringUtils.getSong(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(musicCache).size());
    }


    /** Test method for {@link SongService#exists(Song)} with existing song. */
    @Test
    public void testExistsWithExistingSong() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.SONGS_COUNT; i++) {
            keys.add(SONG_CACHE_KEY + i);
        }

        for (int i = 0; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            assertTrue(songService.exists(SpringEntitiesUtils.getSong(musicNumber, songNumber)));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(musicCache));
        for (int i = 0; i < SpringUtils.SONGS_COUNT; i++) {
            final int musicNumber = i / SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            final int songNumber = i % SpringUtils.SONGS_PER_MUSIC_COUNT + 1;
            SpringUtils.assertCacheValue(musicCache, keys.get(i), SpringEntitiesUtils.getSong(musicNumber, songNumber));
        }
    }

    /** Test method for {@link SongService#exists(Song)} with not existing song. */
    @Test
    public void testExistsWithNotExistingSong() {
        final Song song = SpringEntitiesUtils.newSong(objectGenerator, entityManager);
        song.setId(Integer.MAX_VALUE);
        final String key = SONG_CACHE_KEY + Integer.MAX_VALUE;

        assertFalse(songService.exists(song));
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(musicCache));
        SpringUtils.assertCacheValue(musicCache, key, null);
    }

    /** Test method for {@link SongService#findSongsByMusic(Music)}. */
    @Test
    public void testFindSongsByMusic() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
            keys.add(SONGS_CACHE_KEY + i);
        }

        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
            final Music music = SpringUtils.getMusic(entityManager, i);
            DeepAsserts.assertEquals(SpringEntitiesUtils.getSongs(i), songService.findSongsByMusic(music));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(musicCache));
        for (int i = 0; i < SpringUtils.MUSIC_COUNT; i++) {
            SpringUtils.assertCacheValue(musicCache, keys.get(i), SpringEntitiesUtils.getSongs(i + 1));
        }
    }

    /** Test method for {@link SongService#getTotalLengthByMusic(Music)}. */
    @Test
    public void testGetTotalLengthByMusic() {
        final List<String> keys = new ArrayList<>();
        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
            keys.add(SONGS_CACHE_KEY + i);
        }

        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
            final Music music = SpringUtils.getMusic(entityManager, i);
            DeepAsserts.assertEquals(new Time(6 * SpringUtils.LENGTH_MULTIPLIERS[i - 1]), songService.getTotalLengthByMusic(music));
        }
        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, SpringUtils.getSongsCount(entityManager));
        DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(musicCache));
        for (int i = 0; i < SpringUtils.MUSIC_COUNT; i++) {
            SpringUtils.assertCacheValue(musicCache, keys.get(i), SpringEntitiesUtils.getSongs(i + 1));
        }
    }

}
