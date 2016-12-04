package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.MusicTO;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.MusicValidator;
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
 * A class represents test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicFacadeImplTest {

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
     * Instance of {@link MusicFacade}
     */
    private MusicFacade musicFacade;

    /**
     * Initializes facade for music.
     */
    @Before
    public void setUp() {
        musicFacade = new MusicFacadeImpl(musicService, converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, MusicValidator)} with null service for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicService() {
        new MusicFacadeImpl(null, converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, MusicValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new MusicFacadeImpl(musicService, null, musicValidator);
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, MusicValidator)} with null validator for TO for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicTOValidator() {
        new MusicFacadeImpl(musicService, converter, null);
    }

    /**
     * Test method for {@link MusicFacade#newData()}.
     */
    @Test
    public void testNewData() {
        musicFacade.newData();

        verify(musicService).newData();
        verifyNoMoreInteractions(musicService);
        verifyZeroInteractions(converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getMusic()}.
     */
    @Test
    public void testGetMusic() {
        final List<Music> musicList = CollectionUtils.newList(MusicUtils.newMusic(1), MusicUtils.newMusic(2));
        final List<MusicTO> expectedMusic = CollectionUtils.newList(MusicUtils.newMusicTO(1), MusicUtils.newMusicTO(2));

        when(musicService.getAll()).thenReturn(musicList);
        when(converter.convertCollection(musicList, MusicTO.class)).thenReturn(expectedMusic);

        final List<MusicTO> music = musicFacade.getMusic();

        assertNotNull(music);
        assertEquals(expectedMusic, music);

        verify(musicService).getAll();
        verify(converter).convertCollection(musicList, MusicTO.class);
        verifyNoMoreInteractions(musicService, converter);
        verifyZeroInteractions(musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getMusic(Integer)} with existing music.
     */
    @Test
    public void testGetMusicById_ExistingMusic() {
        final Music musicEntity = MusicUtils.newMusic(1);
        final MusicTO expectedMusic = MusicUtils.newMusicTO(1);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(converter.convert(any(Music.class), eq(MusicTO.class))).thenReturn(expectedMusic);

        final MusicTO music = musicFacade.getMusic(expectedMusic.getId());

        assertNotNull(music);
        assertEquals(expectedMusic, music);

        verify(musicService).get(expectedMusic.getId());
        verify(converter).convert(musicEntity, MusicTO.class);
        verifyNoMoreInteractions(musicService, converter);
        verifyZeroInteractions(musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getMusic(Integer)} with not existing music.
     */
    @Test
    public void testGetMusicById_NotExistingMusic() {
        when(musicService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(Music.class), eq(MusicTO.class))).thenReturn(null);

        assertNull(musicFacade.getMusic(Integer.MAX_VALUE));

        verify(musicService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, MusicTO.class);
        verifyNoMoreInteractions(musicService, converter);
        verifyZeroInteractions(musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getMusic(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMusicById_NullArgument() {
        musicFacade.getMusic(null);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)}.
     */
    @Test
    public void testAdd() {
        final Music musicEntity = MusicUtils.newMusic(null);
        final MusicTO music = MusicUtils.newMusicTO(null);

        when(converter.convert(any(MusicTO.class), eq(Music.class))).thenReturn(musicEntity);

        musicFacade.add(music);

        verify(musicService).add(musicEntity);
        verify(converter).convert(music, Music.class);
        verify(musicValidator).validateNewMusicTO(music);
        verifyNoMoreInteractions(musicService, converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validateNewMusicTO(any(MusicTO.class));

        musicFacade.add(null);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadArgument() {
        doThrow(ValidationException.class).when(musicValidator).validateNewMusicTO(any(MusicTO.class));

        musicFacade.add(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)}.
     */
    @Test
    public void testUpdate() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        final ArgumentCaptor<Music> musicArgumentCaptor = ArgumentCaptor.forClass(Music.class);

        when(musicService.get(anyInt())).thenReturn(MusicUtils.newMusic(1));

        musicFacade.update(music);

        verify(musicService).get(1);
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(musicValidator).validateExistingMusicTO(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);

        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusic(1), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validateExistingMusicTO(any(MusicTO.class));

        musicFacade.update(null);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(musicValidator).validateExistingMusicTO(any(MusicTO.class));

        musicFacade.update(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.update(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#remove(MusicTO)}.
     */
    @Test
    public void testRemove() {
        final Music musicEntity = MusicUtils.newMusic(1);
        final MusicTO music = MusicUtils.newMusicTO(1);

        when(musicService.get(anyInt())).thenReturn(musicEntity);

        musicFacade.remove(music);

        verify(musicService).get(1);
        verify(musicService).remove(musicEntity);
        verify(musicValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MusicFacade#remove(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        musicFacade.remove(null);
    }

    /**
     * Test method for {@link MusicFacade#remove(MusicTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        musicFacade.remove(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#remove(MusicTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.remove(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(MusicTO)}.
     */
    @Test
    public void testDuplicate() {
        final Music musicEntity = MusicUtils.newMusic(1);
        final MusicTO music = MusicUtils.newMusicTO(1);

        when(musicService.get(anyInt())).thenReturn(musicEntity);

        musicFacade.duplicate(music);

        verify(musicService).get(1);
        verify(musicService).duplicate(musicEntity);
        verify(musicValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MusicFacade#duplicate(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        musicFacade.duplicate(null);
    }

    /**
     * Test method for {@link MusicFacade#duplicate(MusicTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        musicFacade.duplicate(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(MusicTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.duplicate(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)}.
     */
    @Test
    public void testMoveUp() {
        final Music musicEntity = MusicUtils.newMusic(2);
        final List<Music> musics = CollectionUtils.newList(MusicUtils.newMusic(1), musicEntity);
        final MusicTO music = MusicUtils.newMusicTO(2);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(musicService.getAll()).thenReturn(musics);

        musicFacade.moveUp(music);

        verify(musicService).get(2);
        verify(musicService).getAll();
        verify(musicService).moveUp(musicEntity);
        verify(musicValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        musicFacade.moveUp(null);
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        musicFacade.moveUp(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.moveUp(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        final Music musicEntity = MusicUtils.newMusic(Integer.MAX_VALUE);
        final List<Music> musics = CollectionUtils.newList(musicEntity, MusicUtils.newMusic(1));
        final MusicTO music = MusicUtils.newMusicTO(Integer.MAX_VALUE);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(musicService.getAll()).thenReturn(musics);

        musicFacade.moveUp(music);
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)}.
     */
    @Test
    public void testMoveDown() {
        final Music musicEntity = MusicUtils.newMusic(1);
        final List<Music> musics = CollectionUtils.newList(musicEntity, MusicUtils.newMusic(2));
        final MusicTO music = MusicUtils.newMusicTO(1);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(musicService.getAll()).thenReturn(musics);

        musicFacade.moveDown(music);

        verify(musicService).get(1);
        verify(musicService).getAll();
        verify(musicService).moveDown(musicEntity);
        verify(musicValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        musicFacade.moveDown(null);
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(musicValidator).validateMusicTOWithId(any(MusicTO.class));

        musicFacade.moveDown(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.moveDown(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        final Music musicEntity = MusicUtils.newMusic(Integer.MAX_VALUE);
        final List<Music> musics = CollectionUtils.newList(MusicUtils.newMusic(1), musicEntity);
        final MusicTO music = MusicUtils.newMusicTO(Integer.MAX_VALUE);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(musicService.getAll()).thenReturn(musics);

        musicFacade.moveDown(music);
    }

    /**
     * Test method for {@link MusicFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        musicFacade.updatePositions();

        verify(musicService).updatePositions();
        verifyNoMoreInteractions(musicService);
        verifyZeroInteractions(converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        final Music music1 = MusicUtils.newMusic(1);
        final Music music2 = MusicUtils.newMusic(2);
        final int expectedCount = music1.getMediaCount() + music2.getMediaCount();

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(music1, music2));

        assertEquals(expectedCount, musicFacade.getTotalMediaCount());

        verify(musicService).getAll();
        verifyNoMoreInteractions(musicService);
        verifyZeroInteractions(converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final List<Music> musicList = CollectionUtils.newList(MusicUtils.newMusicWithSongs(1), MusicUtils.newMusicWithSongs(2));
        int expectedTotalLength = 0;
        for (final Music music : musicList) {
            for (final Song song : music.getSongs()) {
                expectedTotalLength += song.getLength();
            }
        }

        when(musicService.getAll()).thenReturn(musicList);

        assertEquals(new Time(expectedTotalLength), musicFacade.getTotalLength());

        verify(musicService).getAll();
        verifyNoMoreInteractions(musicService);
        verifyZeroInteractions(converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getSongsCount()}.
     */
    @Test
    public void testGetSongsCount() {
        final Music music1 = MusicUtils.newMusicWithSongs(1);
        final Music music2 = MusicUtils.newMusicWithSongs(2);
        final int expectedSongs = music1.getSongs().size() + music2.getSongs().size();

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(music1, music2));

        assertEquals(expectedSongs, musicFacade.getSongsCount());

        verify(musicService).getAll();
        verifyNoMoreInteractions(musicService);
        verifyZeroInteractions(converter, musicValidator);
    }

}
