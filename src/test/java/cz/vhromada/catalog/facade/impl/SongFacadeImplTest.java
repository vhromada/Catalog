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

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.common.SongUtils;
import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.MusicTO;
import cz.vhromada.catalog.entity.SongTO;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.MusicValidator;
import cz.vhromada.catalog.validator.SongValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
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
public class SongFacadeImplTest {

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<Music> musicService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link MusicValidator}
     */
    @Mock
    private MusicValidator musicValidator;

    /**
     * Instance of {@link SongValidator}
     */
    @Mock
    private SongValidator songValidator;

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
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, MusicValidator, SongValidator)} with null service for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicService() {
        new SongFacadeImpl(null, converter, musicValidator, songValidator);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, MusicValidator, SongValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new SongFacadeImpl(musicService, null, musicValidator, songValidator);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, MusicValidator, SongValidator)} with null validator for TO for
     * music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicTOValidator() {
        new SongFacadeImpl(musicService, converter, null, songValidator);
    }

    /**
     * Test method for {@link SongFacadeImpl#SongFacadeImpl(CatalogService, Converter, MusicValidator, SongValidator)} with null validator for TO for song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSongTOValidator() {
        new SongFacadeImpl(musicService, converter, musicValidator, null);
    }

    /**
     * Test method for {@link SongFacade#getSong(Integer)} with existing song.
     */
    @Test
    public void testGetSong_ExistingSong() {
        final SongTO expectedSong = SongUtils.newSongTO(1);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));
        when(converter.convert(any(Song.class), eq(SongTO.class))).thenReturn(expectedSong);

        final SongTO song = songFacade.getSong(1);

        assertNotNull(song);
        assertEquals(expectedSong, song);

        verify(musicService).getAll();
        verify(converter).convert(SongUtils.newSong(1), SongTO.class);
        verifyNoMoreInteractions(musicService, converter);
        verifyZeroInteractions(musicValidator, songValidator);
    }

    /**
     * Test method for {@link SongFacade#getSong(Integer)} with not existing song.
     */
    @Test
    public void testGetSong_NotExistingSong() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));
        when(converter.convert(any(Song.class), eq(SongTO.class))).thenReturn(null);

        assertNull(songFacade.getSong(Integer.MAX_VALUE));

        verify(musicService).getAll();
        verify(converter).convert(null, SongTO.class);
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
     * Test method for {@link SongFacade#add(MusicTO, SongTO)}.
     */
    @Test
    public void testAdd() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        final SongTO song = SongUtils.newSongTO(null);
        final Song songEntity = SongUtils.newSong(null);
        final ArgumentCaptor<Music> musicArgumentCaptor = ArgumentCaptor.forClass(Music.class);

        when(musicService.get(anyInt())).thenReturn(MusicUtils.newMusic(1));
        when(converter.convert(any(SongTO.class), eq(Song.class))).thenReturn(songEntity);

        songFacade.add(music, song);

        verify(musicService).get(music.getId());
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(musicValidator).validateMusicTOWithId(music);
        verify(songValidator).validateNewSongTO(song);
        verify(converter).convert(song, Song.class);
        verifyNoMoreInteractions(musicService, converter, musicValidator, songValidator);

        MusicUtils.assertMusicDeepEquals(newMusicWithSongs(1, songEntity), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#add(MusicTO, SongTO)} with null TO for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullMusicTO() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        songFacade.add(null, SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#add(MusicTO, SongTO)} with null TO for song.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSongTO() {
        doThrow(IllegalArgumentException.class).when(songValidator).validateNewSongTO(any(SongTO.class));

        songFacade.add(MusicUtils.newMusicTO(1), null);
    }

    /**
     * Test method for {@link SongFacade#add(MusicTO, SongTO)} with TO for music with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMusicTO() {
        doThrow(ValidationException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        songFacade.add(MusicUtils.newMusicTO(1), SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#add(MusicTO, SongTO)} with TO for song with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadSongTO() {
        doThrow(ValidationException.class).when(songValidator).validateNewSongTO(any(SongTO.class));

        songFacade.add(MusicUtils.newMusicTO(1), SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#add(MusicTO, SongTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        songFacade.add(MusicUtils.newMusicTO(Integer.MAX_VALUE), SongUtils.newSongTO(null));
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)}.
     */
    @Test
    public void testUpdate() {
        final SongTO song = SongUtils.newSongTO(1);
        final Song songEntity = SongUtils.newSong(1);
        final ArgumentCaptor<Music> musicArgumentCaptor = ArgumentCaptor.forClass(Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));
        when(converter.convert(any(SongTO.class), eq(Song.class))).thenReturn(songEntity);

        songFacade.update(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(converter).convert(song, Song.class);
        verify(songValidator).validateExistingSongTO(song);
        verifyNoMoreInteractions(musicService, converter, songValidator);
        verifyZeroInteractions(musicValidator);

        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusicWithSongs(1), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validateExistingSongTO(any(SongTO.class));

        songFacade.update(null);
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(songValidator).validateExistingSongTO(any(SongTO.class));

        songFacade.update(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#update(SongTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.update(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#remove(SongTO)}.
     */
    @Test
    public void testRemove() {
        final SongTO song = SongUtils.newSongTO(1);
        final ArgumentCaptor<Music> musicArgumentCaptor = ArgumentCaptor.forClass(Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.remove(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(songValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(musicService, songValidator);
        verifyZeroInteractions(converter, musicValidator);

        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusic(1), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#remove(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validateSongTOWithId(any(SongTO.class));

        songFacade.remove(null);
    }

    /**
     * Test method for {@link SongFacade#remove(SongTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(songValidator).validateSongTOWithId(any(SongTO.class));

        songFacade.remove(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#remove(SongTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.remove(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#duplicate(SongTO)}.
     */
    @Test
    public void testDuplicate() {
        final SongTO song = SongUtils.newSongTO(1);
        final Song songEntity = SongUtils.newSong(null);
        song.setPosition(0);
        final Music expectedMusic = newMusicWithSongs(1, SongUtils.newSong(1), songEntity);
        final ArgumentCaptor<Music> musicArgumentCaptor = ArgumentCaptor.forClass(Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.duplicate(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(songValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(musicService, songValidator);
        verifyZeroInteractions(converter, musicValidator);

        MusicUtils.assertMusicDeepEquals(expectedMusic, musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#duplicate(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validateSongTOWithId(any(SongTO.class));

        songFacade.duplicate(null);
    }

    /**
     * Test method for {@link SongFacade#duplicate(SongTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(songValidator).validateSongTOWithId(any(SongTO.class));

        songFacade.duplicate(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#duplicate(SongTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.duplicate(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)}.
     */
    @Test
    public void testMoveUp() {
        final SongTO song = SongUtils.newSongTO(2);
        final Song expectedSong1 = SongUtils.newSong(1);
        expectedSong1.setPosition(1);
        final Song expectedSong2 = SongUtils.newSong(2);
        expectedSong2.setPosition(0);
        final ArgumentCaptor<Music> musicArgumentCaptor = ArgumentCaptor.forClass(Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(newMusicWithSongs(1, SongUtils.newSong(1), SongUtils.newSong(2))));

        songFacade.moveUp(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(songValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(musicService, songValidator);
        verifyZeroInteractions(converter, musicValidator);

        MusicUtils.assertMusicDeepEquals(newMusicWithSongs(1, expectedSong1, expectedSong2), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validateSongTOWithId(any(SongTO.class));

        songFacade.moveUp(null);
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(songValidator).validateSongTOWithId(any(SongTO.class));

        songFacade.moveUp(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.moveUp(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveUp(SongTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(newMusicWithSongs(1, SongUtils.newSong(1), SongUtils.newSong(2))));

        songFacade.moveUp(SongUtils.newSongTO(1));
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)}.
     */
    @Test
    public void testMoveDown() {
        final SongTO song = SongUtils.newSongTO(1);
        final Song expectedSong1 = SongUtils.newSong(1);
        expectedSong1.setPosition(1);
        final Song expectedSong2 = SongUtils.newSong(2);
        expectedSong2.setPosition(0);
        final ArgumentCaptor<Music> musicArgumentCaptor = ArgumentCaptor.forClass(Music.class);

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(newMusicWithSongs(1, SongUtils.newSong(1), SongUtils.newSong(2))));

        songFacade.moveDown(song);

        verify(musicService).getAll();
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(songValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(musicService, songValidator);
        verifyZeroInteractions(converter, musicValidator);

        MusicUtils.assertMusicDeepEquals(newMusicWithSongs(1, expectedSong1, expectedSong2), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(songValidator).validateSongTOWithId(any(SongTO.class));

        songFacade.moveDown(null);
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(songValidator).validateSongTOWithId(any(SongTO.class));

        songFacade.moveDown(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(MusicUtils.newMusicWithSongs(1)));

        songFacade.moveDown(SongUtils.newSongTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#moveDown(SongTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        when(musicService.getAll()).thenReturn(CollectionUtils.newList(newMusicWithSongs(1, SongUtils.newSong(1), SongUtils.newSong(2))));

        songFacade.moveDown(SongUtils.newSongTO(2));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(MusicTO)}.
     */
    @Test
    public void testFindSongsByMusic() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        final List<SongTO> expectedSongs = CollectionUtils.newList(SongUtils.newSongTO(1));

        when(musicService.get(anyInt())).thenReturn(MusicUtils.newMusicWithSongs(1));
        when(converter.convertCollection(anyListOf(Song.class), eq(SongTO.class))).thenReturn(expectedSongs);

        final List<SongTO> songs = songFacade.findSongsByMusic(music);

        assertNotNull(songs);
        assertEquals(expectedSongs, songs);

        verify(musicService).get(music.getId());
        verify(converter).convertCollection(CollectionUtils.newList(SongUtils.newSong(1)), SongTO.class);
        verify(musicValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, converter, musicValidator);
        verifyZeroInteractions(songValidator);
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        songFacade.findSongsByMusic(null);
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testFindSongsByMusic_BadArgument() {
        doThrow(ValidationException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        songFacade.findSongsByMusic(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testFindSongsByMusic_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        songFacade.findSongsByMusic(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Returns music with music.
     *
     * @param id    ID
     * @param songs songs
     * @return music with music
     */
    private static Music newMusicWithSongs(final Integer id, final Song... songs) {
        final Music music = MusicUtils.newMusic(id);
        music.setSongs(CollectionUtils.newList(songs));

        return music;
    }

}
