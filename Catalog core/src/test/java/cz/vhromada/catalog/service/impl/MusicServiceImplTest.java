package cz.vhromada.catalog.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.MusicService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * A class represents test for class {@link MusicServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicServiceImplTest extends ObjectGeneratorTest {

    /** Cache key for list of music */
    private static final String MUSIC_LIST_CACHE_KEY = "music";

    /** Cache key for music */
    private static final String MUSIC_CACHE_KEY = "musicItem";

    /** Cache key for list of songs */
    private static final String SONGS_CACHE_KEY = "songs";

    /** Instance of {@link MusicDAO} */
    @Mock
    private MusicDAO musicDAO;

    /** Instance of {@link SongDAO} */
    @Mock
    private SongDAO songDAO;

    /** Instance of {@link Cache} */
    @Mock
    private Cache musicCache;

    /** Instance of {@link MusicService} */
    private MusicService musicService;

    /** Initializes service for music. */
    @Before
    public void setUp() {
        musicService = new MusicServiceImpl(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicServiceImpl#MusicServiceImpl(MusicDAO, SongDAO, Cache)} with DAO for music. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMusicDAO() {
        new MusicServiceImpl(null, songDAO, musicCache);
    }

    /** Test method for {@link MusicServiceImpl#MusicServiceImpl(MusicDAO, SongDAO, Cache)} with DAO for songs. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSongDAO() {
        new MusicServiceImpl(musicDAO, null, musicCache);
    }

    /** Test method for {@link MusicServiceImpl#MusicServiceImpl(MusicDAO, SongDAO, Cache))} with cache for music. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMusicCache() {
        new MusicServiceImpl(musicDAO, songDAO, null);
    }

    /** Test method for {@link MusicService#newData()} with cached data. */
    @Test
    public void testNewDataWithCachedData() {
        final List<Music> musicList = CollectionUtils.newList(generate(Music.class), generate(Music.class));
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(musicCache.get(MUSIC_LIST_CACHE_KEY)).thenReturn(new SimpleValueWrapper(musicList));
        for (final Music music : musicList) {
            when(musicCache.get(SONGS_CACHE_KEY + music.getId())).thenReturn(new SimpleValueWrapper(songs));
        }

        musicService.newData();

        for (final Music music : musicList) {
            verify(musicDAO).remove(music);
            verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        }
        for (final Song song : songs) {
            verify(songDAO, times(musicList.size())).remove(song);
        }
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#newData()} with not cached data. */
    @Test
    public void testNewDataWithNotCachedData() {
        final List<Music> musicList = CollectionUtils.newList(generate(Music.class), generate(Music.class));
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(musicDAO.getMusic()).thenReturn(musicList);
        when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
        when(musicCache.get(anyString())).thenReturn(null);

        musicService.newData();

        verify(musicDAO).getMusic();
        for (final Music music : musicList) {
            verify(musicDAO).remove(music);
            verify(songDAO).findSongsByMusic(music);
            verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        }
        for (final Song song : songs) {
            verify(songDAO, times(musicList.size())).remove(song);
        }
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#newData()} with exception in DAO tier. */
    @Test
    public void testNewDataWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic();
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.newData();
            fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicDAO, musicCache);
        verifyZeroInteractions(songDAO);
    }

    /** Test method for {@link MusicService#getMusic()} with cached music. */
    @Test
    public void testGetMusicWithCachedMusic() {
        final List<Music> music = CollectionUtils.newList(mock(Music.class), mock(Music.class));
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

        DeepAsserts.assertEquals(music, musicService.getMusic());

        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(musicDAO);
    }

    /** Test method for {@link MusicService#getMusic()} with not cached music. */
    @Test
    public void testGetMusicWithNotCachedMusic() {
        final List<Music> music = CollectionUtils.newList(mock(Music.class), mock(Music.class));
        when(musicDAO.getMusic()).thenReturn(music);
        when(musicCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(music, musicService.getMusic());

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).put(MUSIC_LIST_CACHE_KEY, music);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#getMusic()} with exception in DAO tier. */
    @Test
    public void testGetMusicWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic();
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.getMusic();
            fail("Can't get music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#getMusic(Integer)} with cached existing music. */
    @Test
    public void testGetMusicByIdWithCachedExistingMusic() {
        final Music music = generate(Music.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

        DeepAsserts.assertEquals(music, musicService.getMusic(music.getId()));

        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(musicDAO);
    }

    /** Test method for {@link MusicService#getMusic(Integer)} with cached not existing music. */
    @Test
    public void testGetMusicByIdWithCachedNotExistingMusic() {
        final Music music = generate(Music.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertNull(musicService.getMusic(music.getId()));

        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(musicDAO);
    }

    /** Test method for {@link MusicService#getMusic(Integer)} with not cached existing music. */
    @Test
    public void testGetMusicByIdWithNotCachedExistingMusic() {
        final Music music = generate(Music.class);
        when(musicDAO.getMusic(anyInt())).thenReturn(music);
        when(musicCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(music, musicService.getMusic(music.getId()));

        verify(musicDAO).getMusic(music.getId());
        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verify(musicCache).put(MUSIC_CACHE_KEY + music.getId(), music);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#getMusic(Integer)} with not cached not existing music. */
    @Test
    public void testGetMusicByIdWithNotCachedNotExistingMusic() {
        final Music music = generate(Music.class);
        when(musicDAO.getMusic(anyInt())).thenReturn(null);
        when(musicCache.get(anyString())).thenReturn(null);

        assertNull(musicService.getMusic(music.getId()));

        verify(musicDAO).getMusic(music.getId());
        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verify(musicCache).put(MUSIC_CACHE_KEY + music.getId(), null);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#getMusic(Integer)} with null argument. */
    @Test
    public void testGetMusicByIdWithNullArgument() {
        try {
            musicService.getMusic(null);
            fail("Can't get music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#getMusic(Integer)} with exception in DAO tier. */
    @Test
    public void testGetMusicByIdWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic(anyInt());
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.getMusic(Integer.MAX_VALUE);
            fail("Can't get music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic(Integer.MAX_VALUE);
        verify(musicCache).get(MUSIC_CACHE_KEY + Integer.MAX_VALUE);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#add(Music)} with cached music. */
    @Test
    public void testAddWithCachedMusic() {
        final Music music = generate(Music.class);
        final List<Music> musicList = CollectionUtils.newList(mock(Music.class), mock(Music.class));
        final List<Music> addedMusicList = new ArrayList<>(musicList);
        addedMusicList.add(music);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(musicList));

        musicService.add(music);

        verify(musicDAO).add(music);
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verify(musicCache).put(MUSIC_LIST_CACHE_KEY, addedMusicList);
        verify(musicCache).put(MUSIC_CACHE_KEY + music.getId(), music);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#add(Music)} with not cached music. */
    @Test
    public void testAddWithNotCachedMusic() {
        final Music music = generate(Music.class);
        when(musicCache.get(anyString())).thenReturn(null);

        musicService.add(music);

        verify(musicDAO).add(music);
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#add(Music)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        try {
            musicService.add(null);
            fail("Can't add music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#add(Music)} with exception in DAO tier. */
    @Test
    public void testAddWithDAOTierException() {
        final Music music = generate(Music.class);
        doThrow(DataStorageException.class).when(musicDAO).add(any(Music.class));

        try {
            musicService.add(music);
            fail("Can't add music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).add(music);
        verifyNoMoreInteractions(musicDAO);
        verifyZeroInteractions(musicCache);
    }

    /** Test method for {@link MusicService#update(Music)}. */
    @Test
    public void testUpdate() {
        final Music music = generate(Music.class);

        musicService.update(music);

        verify(musicDAO).update(music);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#update(Music)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            musicService.update(null);
            fail("Can't update music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#update(Music)} with exception in DAO tier. */
    @Test
    public void testUpdateWithDAOTierException() {
        final Music music = generate(Music.class);
        doThrow(DataStorageException.class).when(musicDAO).update(any(Music.class));

        try {
            musicService.update(music);
            fail("Can't update music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).update(music);
        verifyNoMoreInteractions(musicDAO);
        verifyZeroInteractions(musicCache);
    }

    /** Test method for {@link MusicService#remove(Music)} with cached songs. */
    @Test
    public void testRemoveWithCachedSongs() {
        final Music music = generate(Music.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

        musicService.remove(music);

        verify(musicDAO).remove(music);
        for (final Song song : songs) {
            verify(songDAO).remove(song);
        }
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#remove(Music)} with not cached songs. */
    @Test
    public void testRemoveWithNotCachedSongs() {
        final Music music = generate(Music.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
        when(musicCache.get(anyString())).thenReturn(null);

        musicService.remove(music);

        verify(musicDAO).remove(music);
        verify(songDAO).findSongsByMusic(music);
        for (final Song song : songs) {
            verify(songDAO).remove(song);
        }
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#remove(Music)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            musicService.remove(null);
            fail("Can't remove music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#remove(Music)} with exception in DAO tier. */
    @Test
    public void testRemoveWithDAOTierException() {
        final Music music = generate(Music.class);
        doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.remove(music);
            fail("Can't remove music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(songDAO, musicCache);
        verifyZeroInteractions(musicDAO);
    }

    /** Test method for {@link MusicService#duplicate(Music)} with cached songs. */
    @Test
    public void testDuplicateWithCachedSongs() {
        final Music music = generate(Music.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

        musicService.duplicate(music);

        verify(musicDAO).add(any(Music.class));
        verify(musicDAO).update(any(Music.class));
        verify(songDAO, times(songs.size())).add(any(Song.class));
        verify(songDAO, times(songs.size())).update(any(Song.class));
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#duplicate(Music)} with not cached songs. */
    @Test
    public void testDuplicateWithNotCachedSongs() {
        final Music music = generate(Music.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
        when(musicCache.get(anyString())).thenReturn(null);

        musicService.duplicate(music);

        verify(musicDAO).add(any(Music.class));
        verify(musicDAO).update(any(Music.class));
        verify(songDAO).findSongsByMusic(music);
        verify(songDAO, times(songs.size())).add(any(Song.class));
        verify(songDAO, times(songs.size())).update(any(Song.class));
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#duplicate(Music)} with null argument. */
    @Test
    public void testDuplicateWithNullArgument() {
        try {
            musicService.duplicate(null);
            fail("Can't duplicate music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#duplicate(Music)} with exception in DAO tier. */
    @Test
    public void testDuplicateWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).add(any(Music.class));

        try {
            musicService.duplicate(generate(Music.class));
            fail("Can't duplicate music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).add(any(Music.class));
        verifyNoMoreInteractions(musicDAO);
        verifyZeroInteractions(songDAO, musicCache);
    }

    /** Test method for {@link MusicService#moveUp(Music)} with cached music. */
    @Test
    public void testMoveUpWithCachedMusic() {
        final Music music1 = generate(Music.class);
        final int position1 = music1.getPosition();
        final Music music2 = generate(Music.class);
        final int position2 = music2.getPosition();
        final List<Music> music = CollectionUtils.newList(music1, music2);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

        musicService.moveUp(music2);
        DeepAsserts.assertEquals(position2, music1.getPosition());
        DeepAsserts.assertEquals(position1, music2.getPosition());

        verify(musicDAO).update(music1);
        verify(musicDAO).update(music2);
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#moveUp(Music)} with not cached music. */
    @Test
    public void testMoveUpWithNotCachedMusic() {
        final Music music1 = generate(Music.class);
        final int position1 = music1.getPosition();
        final Music music2 = generate(Music.class);
        final int position2 = music2.getPosition();
        final List<Music> music = CollectionUtils.newList(music1, music2);
        when(musicDAO.getMusic()).thenReturn(music);
        when(musicCache.get(anyString())).thenReturn(null);

        musicService.moveUp(music2);
        DeepAsserts.assertEquals(position2, music1.getPosition());
        DeepAsserts.assertEquals(position1, music2.getPosition());

        verify(musicDAO).update(music1);
        verify(musicDAO).update(music2);
        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#moveUp(Music)} with null argument. */
    @Test
    public void testMoveUpWithNullArgument() {
        try {
            musicService.moveUp(null);
            fail("Can't move up music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#moveUp(Music)} with exception in DAO tier. */
    @Test
    public void testMoveUpWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic();
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.moveUp(generate(Music.class));
            fail("Can't move up music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#moveDown(Music)} with cached music. */
    @Test
    public void testMoveDownWithCachedMusic() {
        final Music music1 = generate(Music.class);
        final int position1 = music1.getPosition();
        final Music music2 = generate(Music.class);
        final int position2 = music2.getPosition();
        final List<Music> music = CollectionUtils.newList(music1, music2);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

        musicService.moveDown(music1);
        DeepAsserts.assertEquals(position2, music1.getPosition());
        DeepAsserts.assertEquals(position1, music2.getPosition());

        verify(musicDAO).update(music1);
        verify(musicDAO).update(music2);
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#moveDown(Music)} with not cached music. */
    @Test
    public void testMoveDownWithNotCachedMusic() {
        final Music music1 = generate(Music.class);
        final int position1 = music1.getPosition();
        final Music music2 = generate(Music.class);
        final int position2 = music2.getPosition();
        final List<Music> music = CollectionUtils.newList(music1, music2);
        when(musicDAO.getMusic()).thenReturn(music);
        when(musicCache.get(anyString())).thenReturn(null);

        musicService.moveDown(music1);
        DeepAsserts.assertEquals(position2, music1.getPosition());
        DeepAsserts.assertEquals(position1, music2.getPosition());

        verify(musicDAO).update(music1);
        verify(musicDAO).update(music2);
        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#moveDown(Music)} with null argument. */
    @Test
    public void testMoveDownWithNullArgument() {
        try {
            musicService.moveDown(null);
            fail("Can't move down music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#moveDown(Music)} with exception in DAO tier. */
    @Test
    public void testMoveDownWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic();
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.moveDown(generate(Music.class));
            fail("Can't move down music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#exists(Music)} with cached existing music. */
    @Test
    public void testExistsWithCachedExistingMusic() {
        final Music music = generate(Music.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

        assertTrue(musicService.exists(music));

        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(musicDAO);
    }

    /** Test method for {@link MusicService#exists(Music)} with cached not existing music. */
    @Test
    public void testExistsWithCachedNotExistingMusic() {
        final Music music = generate(Music.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertFalse(musicService.exists(music));

        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(musicDAO);
    }

    /** Test method for {@link MusicService#exists(Music)} with not cached existing music. */
    @Test
    public void testExistsWithNotCachedExistingMusic() {
        final Music music = generate(Music.class);
        when(musicDAO.getMusic(anyInt())).thenReturn(music);
        when(musicCache.get(anyString())).thenReturn(null);

        assertTrue(musicService.exists(music));

        verify(musicDAO).getMusic(music.getId());
        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verify(musicCache).put(MUSIC_CACHE_KEY + music.getId(), music);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#exists(Music)} with not cached not existing music. */
    @Test
    public void testExistsWithNotCachedNotExistingMusic() {
        final Music music = generate(Music.class);
        when(musicDAO.getMusic(anyInt())).thenReturn(null);
        when(musicCache.get(anyString())).thenReturn(null);

        assertFalse(musicService.exists(music));

        verify(musicDAO).getMusic(music.getId());
        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verify(musicCache).put(MUSIC_CACHE_KEY + music.getId(), null);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#exists(Music)} with null argument. */
    @Test
    public void testExistsWithNullArgument() {
        try {
            musicService.exists(null);
            fail("Can't exists music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#exists(Music)} with exception in DAO tier. */
    @Test
    public void testExistsWithDAOTierException() {
        final Music music = generate(Music.class);
        doThrow(DataStorageException.class).when(musicDAO).getMusic(anyInt());
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.exists(music);
            fail("Can't exists music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic(music.getId());
        verify(musicCache).get(MUSIC_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#updatePositions()} with cached data. */
    @Test
    public void testUpdatePositionsWithCachedData() {
        final List<Music> musicList = CollectionUtils.newList(generate(Music.class), generate(Music.class));
        final List<Song> songs = CollectionUtils.newList(generate(Song.class), generate(Song.class));
        when(musicCache.get(MUSIC_LIST_CACHE_KEY)).thenReturn(new SimpleValueWrapper(musicList));
        for (final Music music : musicList) {
            when(musicCache.get(SONGS_CACHE_KEY + music.getId())).thenReturn(new SimpleValueWrapper(songs));
        }

        musicService.updatePositions();

        for (int i = 0; i < musicList.size(); i++) {
            final Music music = musicList.get(i);
            DeepAsserts.assertEquals(i, music.getPosition());
            verify(musicDAO).update(music);
            verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        }
        for (int i = 0; i < songs.size(); i++) {
            final Song song = songs.get(i);
            DeepAsserts.assertEquals(i, song.getPosition());
            verify(songDAO, times(musicList.size())).update(song);
        }
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#updatePositions()} with not cached data. */
    @Test
    public void testUpdatePositionsWithNotCachedData() {
        final List<Music> musicList = CollectionUtils.newList(generate(Music.class), generate(Music.class));
        final List<Song> songs = CollectionUtils.newList(generate(Song.class), generate(Song.class));
        when(musicDAO.getMusic()).thenReturn(musicList);
        when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
        when(musicCache.get(anyString())).thenReturn(null);

        musicService.updatePositions();

        verify(musicDAO).getMusic();
        for (int i = 0; i < musicList.size(); i++) {
            final Music music = musicList.get(i);
            DeepAsserts.assertEquals(i, music.getPosition());
            verify(musicDAO).update(music);
            verify(songDAO).findSongsByMusic(music);
            verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        }
        for (int i = 0; i < songs.size(); i++) {
            final Song song = songs.get(i);
            DeepAsserts.assertEquals(i, song.getPosition());
            verify(songDAO, times(musicList.size())).update(song);
        }
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).clear();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#updatePositions()} with exception in DAO tier. */
    @Test
    public void testUpdatePositionsWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic();
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.updatePositions();
            fail("Can't update positions with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicDAO, musicCache);
        verifyZeroInteractions(songDAO);
    }

    /** Test method for {@link MusicService#getTotalMediaCount()} with cached music. */
    @Test
    public void testGetTotalMediaCountWithCachedMusic() {
        final Music music1 = mock(Music.class);
        final Music music2 = mock(Music.class);
        final Music music3 = mock(Music.class);
        final List<Music> musicList = CollectionUtils.newList(music1, music2, music3);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(musicList));
        when(music1.getMediaCount()).thenReturn(1);
        when(music2.getMediaCount()).thenReturn(2);
        when(music3.getMediaCount()).thenReturn(3);

        DeepAsserts.assertEquals(6, musicService.getTotalMediaCount());

        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(music1).getMediaCount();
        verify(music2).getMediaCount();
        verify(music3).getMediaCount();
        verifyNoMoreInteractions(musicCache, music1, music2, music3);
        verifyZeroInteractions(musicDAO);
    }

    /** Test method for {@link MusicService#getTotalMediaCount()} with not cached music. */
    @Test
    public void testGetTotalMediaCountWithNotCachedMusic() {
        final Music music1 = mock(Music.class);
        final Music music2 = mock(Music.class);
        final Music music3 = mock(Music.class);
        final List<Music> musicList = CollectionUtils.newList(music1, music2, music3);
        when(musicDAO.getMusic()).thenReturn(musicList);
        when(musicCache.get(anyString())).thenReturn(null);
        when(music1.getMediaCount()).thenReturn(1);
        when(music2.getMediaCount()).thenReturn(2);
        when(music3.getMediaCount()).thenReturn(3);

        DeepAsserts.assertEquals(6, musicService.getTotalMediaCount());

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).put(MUSIC_LIST_CACHE_KEY, musicList);
        verify(music1).getMediaCount();
        verify(music2).getMediaCount();
        verify(music3).getMediaCount();
        verifyNoMoreInteractions(musicDAO, musicCache, music1, music2, music3);
    }

    /** Test method for {@link MusicService#getTotalMediaCount()} with exception in DAO tier. */
    @Test
    public void testGetTotalMediaCountWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic();
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.getTotalMediaCount();
            fail("Can't get total media count with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicDAO, musicCache);
    }

    /** Test method for {@link MusicService#getTotalLength()} with cached data. */
    @Test
    public void testGetTotalLengthWithCachedData() {
        final Music music1 = generate(Music.class);
        final Music music2 = generate(Music.class);
        final List<Music> music = CollectionUtils.newList(music1, music2);
        final Song song1 = mock(Song.class);
        final Song song2 = mock(Song.class);
        final Song song3 = mock(Song.class);
        final List<Song> songs1 = CollectionUtils.newList(song1);
        final List<Song> songs2 = CollectionUtils.newList(song2, song3);
        final int[] lengths = new int[3];
        int totalLength = 0;
        for (int i = 0; i < 3; i++) {
            final int length = generate(Integer.class);
            lengths[i] = length;
            totalLength += length;
        }
        when(musicCache.get(MUSIC_LIST_CACHE_KEY)).thenReturn(new SimpleValueWrapper(music));
        when(musicCache.get(SONGS_CACHE_KEY + music1.getId())).thenReturn(new SimpleValueWrapper(songs1));
        when(musicCache.get(SONGS_CACHE_KEY + music2.getId())).thenReturn(new SimpleValueWrapper(songs2));
        when(song1.getLength()).thenReturn(lengths[0]);
        when(song2.getLength()).thenReturn(lengths[1]);
        when(song3.getLength()).thenReturn(lengths[2]);

        DeepAsserts.assertEquals(new Time(totalLength), musicService.getTotalLength());

        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).get(SONGS_CACHE_KEY + music1.getId());
        verify(musicCache).get(SONGS_CACHE_KEY + music2.getId());
        verify(song1).getLength();
        verify(song2).getLength();
        verify(song3).getLength();
        verifyNoMoreInteractions(musicCache, song1, song2, song3);
        verifyZeroInteractions(musicDAO, songDAO);
    }

    /** Test method for {@link MusicService#getTotalLength()} with not cached data. */
    @Test
    public void testGetTotalLengthWithNotCachedMusic() {
        final Music music1 = generate(Music.class);
        final Music music2 = generate(Music.class);
        final List<Music> music = CollectionUtils.newList(music1, music2);
        final Song song1 = mock(Song.class);
        final Song song2 = mock(Song.class);
        final Song song3 = mock(Song.class);
        final List<Song> songs1 = CollectionUtils.newList(song1);
        final List<Song> songs2 = CollectionUtils.newList(song2, song3);
        final int[] lengths = new int[3];
        int totalLength = 0;
        for (int i = 0; i < 3; i++) {
            final int length = generate(Integer.class);
            lengths[i] = length;
            totalLength += length;
        }
        when(musicDAO.getMusic()).thenReturn(music);
        when(songDAO.findSongsByMusic(music1)).thenReturn(songs1);
        when(songDAO.findSongsByMusic(music2)).thenReturn(songs2);
        when(musicCache.get(anyString())).thenReturn(null);
        when(song1.getLength()).thenReturn(lengths[0]);
        when(song2.getLength()).thenReturn(lengths[1]);
        when(song3.getLength()).thenReturn(lengths[2]);

        DeepAsserts.assertEquals(new Time(totalLength), musicService.getTotalLength());

        verify(musicDAO).getMusic();
        verify(songDAO).findSongsByMusic(music1);
        verify(songDAO).findSongsByMusic(music2);
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).put(MUSIC_LIST_CACHE_KEY, music);
        verify(musicCache).get(SONGS_CACHE_KEY + music1.getId());
        verify(musicCache).put(SONGS_CACHE_KEY + music1.getId(), songs1);
        verify(musicCache).get(SONGS_CACHE_KEY + music2.getId());
        verify(musicCache).put(SONGS_CACHE_KEY + music2.getId(), songs2);
        verify(song1).getLength();
        verify(song2).getLength();
        verify(song3).getLength();
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache, song1, song2, song3);
    }

    /** Test method for {@link MusicService#getTotalLength()} with exception in DAO tier. */
    @Test
    public void testGetTotalLengthWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic();
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.getTotalLength();
            fail("Can't get total length with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicDAO, musicCache);
        verifyZeroInteractions(songDAO);
    }

    /** Test method for {@link MusicService#getSongsCount()} with cached data. */
    @Test
    public void testGetSongsCountWithCachedData() {
        final Music music1 = generate(Music.class);
        final Music music2 = generate(Music.class);
        final List<Music> music = CollectionUtils.newList(music1, music2);
        final List<Song> songs1 = CollectionUtils.newList(mock(Song.class));
        final List<Song> songs2 = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(musicCache.get(MUSIC_LIST_CACHE_KEY)).thenReturn(new SimpleValueWrapper(music));
        when(musicCache.get(SONGS_CACHE_KEY + music1.getId())).thenReturn(new SimpleValueWrapper(songs1));
        when(musicCache.get(SONGS_CACHE_KEY + music2.getId())).thenReturn(new SimpleValueWrapper(songs2));

        DeepAsserts.assertEquals(songs1.size() + songs2.size(), musicService.getSongsCount());

        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).get(SONGS_CACHE_KEY + music1.getId());
        verify(musicCache).get(SONGS_CACHE_KEY + music2.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(musicDAO, songDAO);
    }

    /** Test method for {@link MusicService#getSongsCount()} with not cached data. */
    @Test
    public void testGetSongsCountWithNotCachedMusic() {
        final Music music1 = generate(Music.class);
        final Music music2 = generate(Music.class);
        final List<Music> music = CollectionUtils.newList(music1, music2);
        final List<Song> songs1 = CollectionUtils.newList(mock(Song.class));
        final List<Song> songs2 = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(musicDAO.getMusic()).thenReturn(music);
        when(songDAO.findSongsByMusic(music1)).thenReturn(songs1);
        when(songDAO.findSongsByMusic(music2)).thenReturn(songs2);
        when(musicCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(songs1.size() + songs2.size(), musicService.getSongsCount());

        verify(musicDAO).getMusic();
        verify(songDAO).findSongsByMusic(music1);
        verify(songDAO).findSongsByMusic(music2);
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verify(musicCache).put(MUSIC_LIST_CACHE_KEY, music);
        verify(musicCache).get(SONGS_CACHE_KEY + music1.getId());
        verify(musicCache).put(SONGS_CACHE_KEY + music1.getId(), songs1);
        verify(musicCache).get(SONGS_CACHE_KEY + music2.getId());
        verify(musicCache).put(SONGS_CACHE_KEY + music2.getId(), songs2);
        verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
    }

    /** Test method for {@link MusicService#getSongsCount()} with exception in DAO tier. */
    @Test
    public void testGetSongsCountWithDAOTierException() {
        doThrow(DataStorageException.class).when(musicDAO).getMusic();
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            musicService.getSongsCount();
            fail("Can't get songs count with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(musicDAO).getMusic();
        verify(musicCache).get(MUSIC_LIST_CACHE_KEY);
        verifyNoMoreInteractions(musicDAO, musicCache);
        verifyZeroInteractions(songDAO);
    }

}
