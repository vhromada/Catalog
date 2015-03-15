package cz.vhromada.catalog.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SongService;
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
 * A class represents test for class {@link SongServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SongServiceImplTest extends ObjectGeneratorTest {

    /**
     * Cache key for list of songs
     */
    private static final String SONGS_CACHE_KEY = "songs";

    /**
     * Cache key for song
     */
    private static final String SONG_CACHE_KEY = "song";

    /**
     * Instance of {@link SongDAO}
     */
    @Mock
    private SongDAO songDAO;

    /**
     * Instance of {@link Cache}
     */
    @Mock
    private Cache musicCache;

    /**
     * Instance of {@link SongService}
     */
    private SongService songService;

    /**
     * Initializes service for songs.
     */
    @Before
    public void setUp() {
        songService = new SongServiceImpl(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongServiceImpl#SongServiceImpl(SongDAO, Cache)} with null DAO for songs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSongDAO() {
        new SongServiceImpl(null, musicCache);
    }

    /**
     * Test method for {@link SongServiceImpl#SongServiceImpl(SongDAO, Cache))} with null cache for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMusicCache() {
        new SongServiceImpl(songDAO, null);
    }

    /**
     * Test method for {@link SongService#getSong(Integer)} with cached existing song.
     */
    @Test
    public void testGetSongWithCachedExistingSong() {
        final Song song = generate(Song.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(song));

        DeepAsserts.assertEquals(song, songService.getSong(song.getId()));

        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(songDAO);
    }

    /**
     * Test method for {@link SongService#getSong(Integer)} with cached not existing song.
     */
    @Test
    public void testGetSongWithCachedNotExistingSong() {
        final int id = generate(Integer.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertNull(songService.getSong(id));

        verify(musicCache).get(SONG_CACHE_KEY + id);
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(songDAO);
    }

    /**
     * Test method for {@link SongService#getSong(Integer)} with not cached existing song.
     */
    @Test
    public void testGetSongWithNotCachedExistingSong() {
        final Song song = generate(Song.class);
        when(songDAO.getSong(anyInt())).thenReturn(song);
        when(musicCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(song, songService.getSong(song.getId()));

        verify(songDAO).getSong(song.getId());
        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verify(musicCache).put(SONG_CACHE_KEY + song.getId(), song);
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#getSong(Integer)} with not cached not existing song.
     */
    @Test
    public void testGetSongWithNotCachedNotExistingSong() {
        final int id = generate(Integer.class);
        when(songDAO.getSong(anyInt())).thenReturn(null);
        when(musicCache.get(anyString())).thenReturn(null);

        assertNull(songService.getSong(id));

        verify(songDAO).getSong(id);
        verify(musicCache).get(SONG_CACHE_KEY + id);
        verify(musicCache).put(SONG_CACHE_KEY + id, null);
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#getSong(Integer)} with null argument.
     */
    @Test
    public void testGetSongWithNullArgument() {
        try {
            songService.getSong(null);
            fail("Can't get song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#getSong(Integer)} with exception in DAO tier.
     */
    @Test
    public void testGetSongWithDAOTierException() {
        doThrow(DataStorageException.class).when(songDAO).getSong(anyInt());
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            songService.getSong(Integer.MAX_VALUE);
            fail("Can't get song with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).getSong(Integer.MAX_VALUE);
        verify(musicCache).get(SONG_CACHE_KEY + Integer.MAX_VALUE);
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#add(Song)} with cached songs.
     */
    @Test
    public void testAddWithCachedSongs() {
        final Song song = generate(Song.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        final List<Song> songsList = new ArrayList<>(songs);
        songsList.add(song);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

        songService.add(song);

        verify(songDAO).add(song);
        verify(musicCache).get(SONGS_CACHE_KEY + song.getMusic().getId());
        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verify(musicCache).put(SONGS_CACHE_KEY + song.getMusic().getId(), songsList);
        verify(musicCache).put(SONG_CACHE_KEY + song.getId(), song);
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#add(Song)} with not cached songs.
     */
    @Test
    public void testAddWithNotCachedSongs() {
        final Song song = generate(Song.class);
        when(musicCache.get(anyString())).thenReturn(null);

        songService.add(song);

        verify(songDAO).add(song);
        verify(musicCache).get(SONGS_CACHE_KEY + song.getMusic().getId());
        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#add(Song)} with null argument.
     */
    @Test
    public void testAddWithNullArgument() {
        try {
            songService.add(null);
            fail("Can't add song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#add(Song)} with exception in DAO tier.
     */
    @Test
    public void testAddWithDAOTierException() {
        final Song song = generate(Song.class);
        doThrow(DataStorageException.class).when(songDAO).add(any(Song.class));

        try {
            songService.add(song);
            fail("Can't add song with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).add(song);
        verifyNoMoreInteractions(songDAO);
        verifyZeroInteractions(musicCache);
    }

    /**
     * Test method for {@link SongService#update(Song)}.
     */
    @Test
    public void testUpdate() {
        final Song song = generate(Song.class);

        songService.update(song);

        verify(songDAO).update(song);
        verify(musicCache).clear();
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#update(Song)} with null argument.
     */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            songService.update(null);
            fail("Can't update song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#update(Song)} with exception in DAO tier.
     */
    @Test
    public void testUpdateWithDAOTierException() {
        final Song song = generate(Song.class);
        doThrow(DataStorageException.class).when(songDAO).update(any(Song.class));

        try {
            songService.update(song);
            fail("Can't update song with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).update(song);
        verifyNoMoreInteractions(songDAO);
        verifyZeroInteractions(musicCache);
    }

    /**
     * Test method for {@link SongService#remove(Song)} with cached songs.
     */
    @Test
    public void testRemoveWithCachedSongs() {
        final Song song = generate(Song.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        final List<Song> songsList = new ArrayList<>(songs);
        songsList.add(song);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songsList));

        songService.remove(song);

        verify(songDAO).remove(song);
        verify(musicCache).get(SONGS_CACHE_KEY + song.getMusic().getId());
        verify(musicCache).put(SONGS_CACHE_KEY + song.getMusic().getId(), songs);
        verify(musicCache).evict(SONG_CACHE_KEY + song.getId());
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#remove(Song)} with not cached songs.
     */
    @Test
    public void testRemoveWithNotCachedSongs() {
        final Song song = generate(Song.class);
        when(musicCache.get(anyString())).thenReturn(null);

        songService.remove(song);

        verify(songDAO).remove(song);
        verify(musicCache).get(SONGS_CACHE_KEY + song.getMusic().getId());
        verify(musicCache).evict(SONG_CACHE_KEY + song.getId());
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#remove(Song)} with null argument.
     */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            songService.remove(null);
            fail("Can't remove song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#remove(Song)} with exception in DAO tier.
     */
    @Test
    public void testRemoveWithDAOTierException() {
        final Song song = generate(Song.class);
        doThrow(DataStorageException.class).when(songDAO).remove(any(Song.class));

        try {
            songService.remove(song);
            fail("Can't remove song with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).remove(song);
        verifyNoMoreInteractions(songDAO);
        verifyZeroInteractions(musicCache);
    }

    /**
     * Test method for {@link SongService#duplicate(Song)} with cached songs.
     */
    @Test
    public void testDuplicateWithCachedSongs() {
        final Song song = generate(Song.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(CollectionUtils.newList(mock(Song.class), mock(Song.class))));

        songService.duplicate(song);

        verify(songDAO).add(any(Song.class));
        verify(songDAO).update(any(Song.class));
        verify(musicCache).get(SONGS_CACHE_KEY + song.getMusic().getId());
        verify(musicCache).get(SONG_CACHE_KEY + null);
        verify(musicCache).put(eq(SONGS_CACHE_KEY + song.getMusic().getId()), anyListOf(Song.class));
        verify(musicCache).put(eq(SONG_CACHE_KEY + null), any(Song.class));
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#duplicate(Song)} with not cached songs.
     */
    @Test
    public void testDuplicateWithNotCachedSongs() {
        final Song song = generate(Song.class);
        when(musicCache.get(anyString())).thenReturn(null);

        songService.duplicate(song);

        verify(songDAO).add(any(Song.class));
        verify(songDAO).update(any(Song.class));
        verify(musicCache).get(SONGS_CACHE_KEY + song.getMusic().getId());
        verify(musicCache).get(SONG_CACHE_KEY + null);
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#duplicate(Song)} with null argument.
     */
    @Test
    public void testDuplicateWithNullArgument() {
        try {
            songService.duplicate(null);
            fail("Can't duplicate song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#duplicate(Song)} with exception in DAO tier.
     */
    @Test
    public void testDuplicateWithDAOTierException() {
        doThrow(DataStorageException.class).when(songDAO).add(any(Song.class));

        try {
            songService.duplicate(generate(Song.class));
            fail("Can't duplicate song with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).add(any(Song.class));
        verifyNoMoreInteractions(songDAO);
        verifyZeroInteractions(musicCache);
    }

    /**
     * Test method for {@link SongService#moveUp(Song)} with cached songs.
     */
    @Test
    public void testMoveUpWithCachedSongs() {
        final Music music = generate(Music.class);
        final Song song1 = generate(Song.class);
        song1.setMusic(music);
        final int position1 = song1.getPosition();
        final Song song2 = generate(Song.class);
        song2.setMusic(music);
        final int position2 = song2.getPosition();
        final List<Song> songs = CollectionUtils.newList(song1, song2);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

        songService.moveUp(song2);
        DeepAsserts.assertEquals(position2, song1.getPosition());
        DeepAsserts.assertEquals(position1, song2.getPosition());

        verify(songDAO).update(song1);
        verify(songDAO).update(song2);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).clear();
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#moveUp(Song)} with not cached songs.
     */
    @Test
    public void testMoveUpWithNotCachedSongs() {
        final Music music = generate(Music.class);
        final Song song1 = generate(Song.class);
        song1.setMusic(music);
        final int position1 = song1.getPosition();
        final Song song2 = generate(Song.class);
        song2.setMusic(music);
        final int position2 = song2.getPosition();
        final List<Song> songs = CollectionUtils.newList(song1, song2);
        when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
        when(musicCache.get(anyString())).thenReturn(null);

        songService.moveUp(song2);
        DeepAsserts.assertEquals(position2, song1.getPosition());
        DeepAsserts.assertEquals(position1, song2.getPosition());

        verify(songDAO).update(song1);
        verify(songDAO).update(song2);
        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).clear();
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#moveUp(Song)} with null argument.
     */
    @Test
    public void testMoveUpWithNullArgument() {
        try {
            songService.moveUp(null);
            fail("Can't move up song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#moveUp(Song)} with exception in DAO tier.
     */
    @Test
    public void testMoveUpWithDAOTierException() {
        final Music music = generate(Music.class);
        final Song song = generate(Song.class);
        song.setMusic(music);
        doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            songService.moveUp(song);
            fail("Can't move up song with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#moveDown(Song)} with cached songs.
     */
    @Test
    public void testMoveDownWithCachedSongs() {
        final Music music = generate(Music.class);
        final Song song1 = generate(Song.class);
        song1.setMusic(music);
        final int position1 = song1.getPosition();
        final Song song2 = generate(Song.class);
        song2.setMusic(music);
        final int position2 = song2.getPosition();
        final List<Song> songs = CollectionUtils.newList(song1, song2);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

        songService.moveDown(song1);
        DeepAsserts.assertEquals(position2, song1.getPosition());
        DeepAsserts.assertEquals(position1, song2.getPosition());

        verify(songDAO).update(song1);
        verify(songDAO).update(song2);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).clear();
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#moveDown(Song)} with not cached songs.
     */
    @Test
    public void testMoveDownWithNotCachedSongs() {
        final Music music = generate(Music.class);
        final Song song1 = generate(Song.class);
        song1.setMusic(music);
        final int position1 = song1.getPosition();
        final Song song2 = generate(Song.class);
        song2.setMusic(music);
        final int position2 = song2.getPosition();
        final List<Song> songs = CollectionUtils.newList(song1, song2);
        when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
        when(musicCache.get(anyString())).thenReturn(null);

        songService.moveDown(song1);
        DeepAsserts.assertEquals(position2, song1.getPosition());
        DeepAsserts.assertEquals(position1, song2.getPosition());

        verify(songDAO).update(song1);
        verify(songDAO).update(song2);
        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).clear();
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#moveDown(Song)} with null argument.
     */
    @Test
    public void testMoveDownWithNullArgument() {
        try {
            songService.moveDown(null);
            fail("Can't move down song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#moveDown(Song)} with exception in DAO tier.
     */
    @Test
    public void testMoveDownWithDAOTierException() {
        final Music music = generate(Music.class);
        final Song song = generate(Song.class);
        song.setMusic(music);
        doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            songService.moveDown(song);
            fail("Can't move down song with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + song.getMusic().getId());
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#exists(Song)} with cached existing song.
     */
    @Test
    public void testExistsWithCachedExistingSong() {
        final Song song = generate(Song.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(song));

        assertTrue(songService.exists(song));

        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(songDAO);
    }

    /**
     * Test method for {@link SongService#exists(Song)} with cached not existing song.
     */
    @Test
    public void testExistsWithCachedNotExistingSong() {
        final Song song = generate(Song.class);
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertFalse(songService.exists(song));

        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(songDAO);
    }

    /**
     * Test method for {@link SongService#exists(Song)} with not cached existing song.
     */
    @Test
    public void testExistsWithNotCachedExistingSong() {
        final Song song = generate(Song.class);
        when(songDAO.getSong(anyInt())).thenReturn(song);
        when(musicCache.get(anyString())).thenReturn(null);

        assertTrue(songService.exists(song));

        verify(songDAO).getSong(song.getId());
        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verify(musicCache).put(SONG_CACHE_KEY + song.getId(), song);
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#exists(Song)} with not cached not existing song.
     */
    @Test
    public void testExistsWithNotCachedNotExistingSong() {
        final Song song = generate(Song.class);
        when(songDAO.getSong(anyInt())).thenReturn(null);
        when(musicCache.get(anyString())).thenReturn(null);

        assertFalse(songService.exists(song));

        verify(songDAO).getSong(song.getId());
        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verify(musicCache).put(SONG_CACHE_KEY + song.getId(), null);
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#exists(Song)} with null argument.
     */
    @Test
    public void testExistsWithNullArgument() {
        try {
            songService.exists(null);
            fail("Can't exists song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#exists(Song)} with exception in DAO tier.
     */
    @Test
    public void testExistsWithDAOTierException() {
        final Song song = generate(Song.class);
        doThrow(DataStorageException.class).when(songDAO).getSong(anyInt());
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            songService.exists(song);
            fail("Can't exists song with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).getSong(song.getId());
        verify(musicCache).get(SONG_CACHE_KEY + song.getId());
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#findSongsByMusic(Music)} with cached songs.
     */
    @Test
    public void testFindSongsByMusicWithCachedSongs() {
        final Music music = generate(Music.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

        DeepAsserts.assertEquals(songs, songService.findSongsByMusic(music));

        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(musicCache);
        verifyZeroInteractions(songDAO);
    }

    /**
     * Test method for {@link SongService#findSongsByMusic(Music)} with not cached songs.
     */
    @Test
    public void testFindSongsByMusicWithNotCachedSongs() {
        final Music music = generate(Music.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
        when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
        when(musicCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(songs, songService.findSongsByMusic(music));

        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).put(SONGS_CACHE_KEY + music.getId(), songs);
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#findSongsByMusic(Music)} with null argument.
     */
    @Test
    public void testFindSongsByMusicWithNullArgument() {
        try {
            songService.findSongsByMusic(null);
            fail("Can't find songs by music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#findSongsByMusic(Music)} with exception in DAO tier.
     */
    @Test
    public void testFindSongsByMusicWithDAOTierException() {
        final Music music = generate(Music.class);
        doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            songService.findSongsByMusic(music);
            fail("Can't find songs by music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#getTotalLengthByMusic(Music)} with cached songs.
     */
    @Test
    public void testGetTotalLengthByMusicWithCachedSongs() {
        final Music music = generate(Music.class);
        final Song song1 = mock(Song.class);
        final Song song2 = mock(Song.class);
        final Song song3 = mock(Song.class);
        final List<Song> songs = CollectionUtils.newList(song1, song2, song3);
        final int[] lengths = new int[3];
        int totalLength = 0;
        for (int i = 0; i < 3; i++) {
            final int length = generate(Integer.class);
            lengths[i] = length;
            totalLength += length;
        }
        when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));
        when(song1.getLength()).thenReturn(lengths[0]);
        when(song2.getLength()).thenReturn(lengths[1]);
        when(song3.getLength()).thenReturn(lengths[2]);

        DeepAsserts.assertEquals(new Time(totalLength), songService.getTotalLengthByMusic(music));

        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(song1).getLength();
        verify(song2).getLength();
        verify(song3).getLength();
        verifyNoMoreInteractions(musicCache, song1, song2, song3);
        verifyZeroInteractions(songDAO);
    }

    /**
     * Test method for {@link SongService#getTotalLengthByMusic(Music)} with not cached songs.
     */
    @Test
    public void testGetTotalLengthByMusicWithNotCachedSongs() {
        final Music music = generate(Music.class);
        final Song song1 = mock(Song.class);
        final Song song2 = mock(Song.class);
        final Song song3 = mock(Song.class);
        final List<Song> songs = CollectionUtils.newList(song1, song2, song3);
        final int[] lengths = new int[3];
        int totalLength = 0;
        for (int i = 0; i < 3; i++) {
            final int length = generate(Integer.class);
            lengths[i] = length;
            totalLength += length;
        }
        when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
        when(musicCache.get(anyString())).thenReturn(null);
        when(song1.getLength()).thenReturn(lengths[0]);
        when(song2.getLength()).thenReturn(lengths[1]);
        when(song3.getLength()).thenReturn(lengths[2]);

        DeepAsserts.assertEquals(new Time(totalLength), songService.getTotalLengthByMusic(music));

        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verify(musicCache).put(SONGS_CACHE_KEY + music.getId(), songs);
        verify(song1).getLength();
        verify(song2).getLength();
        verify(song3).getLength();
        verifyNoMoreInteractions(songDAO, musicCache, song1, song2, song3);
    }

    /**
     * Test method for {@link SongService#getTotalLengthByMusic(Music)} with null argument.
     */
    @Test
    public void testGetTotalLengthByMusicWithNullArgument() {
        try {
            songService.getTotalLengthByMusic(null);
            fail("Can't get total length by music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songDAO, musicCache);
    }

    /**
     * Test method for {@link SongService#getTotalLengthByMusic(Music)} with exception in DAO tier.
     */
    @Test
    public void testGetTotalLengthByMusicWithDAOTierException() {
        final Music music = generate(Music.class);
        doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
        when(musicCache.get(anyString())).thenReturn(null);

        try {
            songService.getTotalLengthByMusic(music);
            fail("Can't get total length by music with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(songDAO).findSongsByMusic(music);
        verify(musicCache).get(SONGS_CACHE_KEY + music.getId());
        verifyNoMoreInteractions(songDAO, musicCache);
    }

}
