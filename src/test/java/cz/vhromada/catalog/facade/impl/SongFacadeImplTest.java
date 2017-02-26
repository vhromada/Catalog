package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link SongFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class SongFacadeImplTest {

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<cz.vhromada.catalog.domain.Music> musicService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link CatalogValidator}
     */
    @Mock
    private CatalogValidator<Music> musicValidator;

    /**
     * Instance of {@link CatalogValidator}
     */
    @Mock
    private CatalogValidator<Song> songValidator;

    /**
     * Instance of (@link SongFacade}
     */
    private SongFacade songFacade;

    /**
     * Initializes facade for songs.
     */
    @Before
    public void setUp() {
        songFacade = new SongFacadeImpl(musicService, converter, musicValidator, songValidator);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null service for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicService() {
        new SongFacadeImpl(null, converter, musicValidator, songValidator);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new SongFacadeImpl(musicService, null, musicValidator, songValidator);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null validator for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicEpisodeValidator() {
        new SongFacadeImpl(musicService, converter, null, songValidator);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null validator for song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSongEpisodeValidator() {
        new SongFacadeImpl(musicService, converter, musicValidator, null);
    }

    /**
     * Test method for {@link SongFacade#getSong(Integer)} with existing song.
     */
    @Test
    public void testGetSong_ExistingSong() {
        final Song expectedSong = SongUtils.newSong(1);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));
        when(converter.convert(any(cz.vhromada.catalog.domain.Song.class), eq(Song.class))).thenReturn(expectedSong);

        final Song song = songFacade.getSong(1);

        assertNotNull(song);
        assertEquals(expectedSong, song);

        verify(musicService).getAll();
        verify(converter).convert(SongUtils.newSongDomain(1), Song.class);
        verifyNoMoreInteractions(musicService, converter);
        verifyZeroInteractions(musicValidator, songValidator);
    }

    /**
     * Test method for {@link SongFacade#getSong(Integer)} with not existing song.
     */
    @Test
    public void testGetSong_NotExistingSong() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));
        when(converter.convert(any(cz.vhromada.catalog.domain.Song.class), eq(Song.class))).thenReturn(null);

        assertNull(songFacade.getSong(Integer.MAX_VALUE));

        verify(musicService).getAll();
        verify(converter).convert(null, Song.class);
        verifyNoMoreInteractions(musicService, converter);
    }

    /**
     * Test method for {@link SongFacade#getSong(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSong_NullArgument() {
        songFacade.getSong(null);
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)}.
     */
    @Test
    public void testAdd() {
        final Music music = MusicUtils.newMusic(1);
        final Song song = SongUtils.newSong(null);
        final cz.vhromada.catalog.domain.Song songEntity = SongUtils.newSongDomain(null);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Music> musicArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Music.class);

        when(musicService.get(anyInt())).thenReturn(MusicUtils.newMusicDomain(1));
        when(converter.convert(any(Song.class), eq(cz.vhromada.catalog.domain.Song.class))).thenReturn(songEntity);

        songFacade.add(music, song);

        verify(musicService).get(music.getId());
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(musicValidator).validate(music);
        verify(songValidator).validate(song);
        verify(converter).convert(song, cz.vhromada.catalog.domain.Song.class);
        verifyNoMoreInteractions(musicService, converter, musicValidator, songValidator);

        MusicUtils.assertMusicDeepEquals(newMusicWithSongs(1, songEntity), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with null music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullMusicEpisode() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        songFacade.add(null, SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with null song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSongEpisode() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.add(MusicUtils.newMusic(1), null);
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with music with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMusicEpisode() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        songFacade.add(MusicUtils.newMusic(1), SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with song with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadSongEpisode() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.add(MusicUtils.newMusic(1), SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#add(Music, Song)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        songFacade.add(MusicUtils.newMusic(Integer.MAX_VALUE), SongUtils.newSong(null));
    }

    /**
     * Test method for {@link SongFacade#update(Song)}.
     */
    @Test
    public void testUpdate() {
        final Song song = SongUtils.newSong(1);
        final cz.vhromada.catalog.domain.Song songEntity = SongUtils.newSongDomain(1);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Music> musicArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));
        when(converter.convert(any(Song.class), eq(cz.vhromada.catalog.domain.Song.class))).thenReturn(songEntity);

        songFacade.update(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(converter).convert(song, cz.vhromada.catalog.domain.Song.class);
        verify(songValidator).validate(song);
        verifyNoMoreInteractions(musicService, converter, songValidator);
        verifyZeroInteractions(musicValidator);

        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusicWithSongs(1), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.update(null);
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.update(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#update(Song)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.update(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)}.
     */
    @Test
    public void testRemove() {
        final Song song = SongUtils.newSong(1);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Music> musicArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.remove(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(songValidator).validate(song);
        verifyNoMoreInteractions(musicService, songValidator);
        verifyZeroInteractions(converter, musicValidator);

        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusicDomain(1), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.remove(null);
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.remove(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#remove(Song)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.remove(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)}.
     */
    @Test
    public void testDuplicate() {
        final Song song = SongUtils.newSong(1);
        final cz.vhromada.catalog.domain.Song songEntity = SongUtils.newSongDomain(null);
        song.setPosition(0);
        final cz.vhromada.catalog.domain.Music expectedMusic = newMusicWithSongs(1, SongUtils.newSongDomain(1), songEntity);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Music> musicArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.duplicate(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(songValidator).validate(song);
        verifyNoMoreInteractions(musicService, songValidator);
        verifyZeroInteractions(converter, musicValidator);

        MusicUtils.assertMusicDeepEquals(expectedMusic, musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.duplicate(null);
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.duplicate(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#duplicate(Song)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.duplicate(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)}.
     */
    @Test
    public void testMoveUp() {
        final Song song = SongUtils.newSong(2);
        final cz.vhromada.catalog.domain.Song expectedSong1 = SongUtils.newSongDomain(1);
        expectedSong1.setPosition(1);
        final cz.vhromada.catalog.domain.Song expectedSong2 = SongUtils.newSongDomain(2);
        expectedSong2.setPosition(0);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Music> musicArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(newMusicWithSongs(1, SongUtils.newSongDomain(1), SongUtils.newSongDomain(2))));

        songFacade.moveUp(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(songValidator).validate(song);
        verifyNoMoreInteractions(musicService, songValidator);
        verifyZeroInteractions(converter, musicValidator);

        MusicUtils.assertMusicDeepEquals(newMusicWithSongs(1, expectedSong1, expectedSong2), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.moveUp(null);
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.moveUp(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.moveUp(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveUp(Song)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(newMusicWithSongs(1, SongUtils.newSongDomain(1), SongUtils.newSongDomain(2))));

        songFacade.moveUp(SongUtils.newSong(1));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)}.
     */
    @Test
    public void testMoveDown() {
        final Song song = SongUtils.newSong(1);
        final cz.vhromada.catalog.domain.Song expectedSong1 = SongUtils.newSongDomain(1);
        expectedSong1.setPosition(1);
        final cz.vhromada.catalog.domain.Song expectedSong2 = SongUtils.newSongDomain(2);
        expectedSong2.setPosition(0);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Music> musicArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(newMusicWithSongs(1, SongUtils.newSongDomain(1), SongUtils.newSongDomain(2))));

        songFacade.moveDown(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(songValidator).validate(song);
        verifyNoMoreInteractions(musicService, songValidator);
        verifyZeroInteractions(converter, musicValidator);

        MusicUtils.assertMusicDeepEquals(newMusicWithSongs(1, expectedSong1, expectedSong2), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.moveDown(null);
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validate(any(Song.class));

        songFacade.moveDown(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.moveDown(SongUtils.newSong(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveDown(Song)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(newMusicWithSongs(1, SongUtils.newSongDomain(1), SongUtils.newSongDomain(2))));

        songFacade.moveDown(SongUtils.newSong(2));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(Music)}.
     */
    @Test
    public void testFindSongsByMusic() {
        final Music music = MusicUtils.newMusic(1);
        final List<Song> expectedSongs = CollectionUtils.newList(SongUtils.newSong(1));

        when(musicService.get(anyInt())).thenReturn(MusicUtils.newMusicWithSongs(1));
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Song.class), eq(Song.class))).thenReturn(expectedSongs);

        final List<Song> songs = songFacade.findSongsByMusic(music);

        assertNotNull(songs);
        assertEquals(expectedSongs, songs);

        verify(musicService).get(music.getId());
        verify(converter).convertCollection(CollectionUtils.newList(SongUtils.newSongDomain(1)), Song.class);
        verify(musicValidator).validate(music);
        verifyNoMoreInteractions(musicService, converter, musicValidator);
        verifyZeroInteractions(songValidator);
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        songFacade.findSongsByMusic(null);
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(Music)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_BadArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        songFacade.findSongsByMusic(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(Music)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        songFacade.findSongsByMusic(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Returns music with music.
     *
     * @param id    ID
     * @param songs songs
     * @return music with music
     */
    private static cz.vhromada.catalog.domain.Music newMusicWithSongs(final Integer id, final cz.vhromada.catalog.domain.Song... songs) {
        final cz.vhromada.catalog.domain.Music music = MusicUtils.newMusicDomain(id);
        music.setSongs(CollectionUtils.newList(songs));

        return music;
    }

}
