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

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.MusicUtils;
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
 * A class represents test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class MusicFacadeImplTest {

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
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicService() {
        new MusicFacadeImpl(null, converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new MusicFacadeImpl(musicService, null, musicValidator);
    }

    /**
     * Test method for {@link MusicFacadeImpl#MusicFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for music.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMusicValidator() {
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
        final List<cz.vhromada.catalog.domain.Music> musicList = CollectionUtils.newList(MusicUtils.newMusicDomain(1), MusicUtils.newMusicDomain(2));
        final List<Music> expectedMusic = CollectionUtils.newList(MusicUtils.newMusic(1), MusicUtils.newMusic(2));

        when(musicService.getAll()).thenReturn(musicList);
        when(converter.convertCollection(musicList, Music.class)).thenReturn(expectedMusic);

        final List<Music> music = musicFacade.getMusic();

        assertNotNull(music);
        assertEquals(expectedMusic, music);

        verify(musicService).getAll();
        verify(converter).convertCollection(musicList, Music.class);
        verifyNoMoreInteractions(musicService, converter);
        verifyZeroInteractions(musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getMusic(Integer)} with existing music.
     */
    @Test
    public void testGetMusicById_ExistingMusic() {
        final cz.vhromada.catalog.domain.Music musicEntity = MusicUtils.newMusicDomain(1);
        final Music expectedMusic = MusicUtils.newMusic(1);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(converter.convert(any(cz.vhromada.catalog.domain.Music.class), eq(Music.class))).thenReturn(expectedMusic);

        final Music music = musicFacade.getMusic(expectedMusic.getId());

        assertNotNull(music);
        assertEquals(expectedMusic, music);

        verify(musicService).get(expectedMusic.getId());
        verify(converter).convert(musicEntity, Music.class);
        verifyNoMoreInteractions(musicService, converter);
        verifyZeroInteractions(musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#getMusic(Integer)} with not existing music.
     */
    @Test
    public void testGetMusicById_NotExistingMusic() {
        when(musicService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(cz.vhromada.catalog.domain.Music.class), eq(Music.class))).thenReturn(null);

        assertNull(musicFacade.getMusic(Integer.MAX_VALUE));

        verify(musicService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, Music.class);
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
     * Test method for {@link MusicFacade#add(Music)}.
     */
    @Test
    public void testAdd() {
        final cz.vhromada.catalog.domain.Music musicEntity = MusicUtils.newMusicDomain(null);
        final Music music = MusicUtils.newMusic(null);

        when(converter.convert(any(Music.class), eq(cz.vhromada.catalog.domain.Music.class))).thenReturn(musicEntity);

        musicFacade.add(music);

        verify(musicService).add(musicEntity);
        verify(converter).convert(music, cz.vhromada.catalog.domain.Music.class);
        verify(musicValidator).validate(music);
        verifyNoMoreInteractions(musicService, converter, musicValidator);
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.add(null);
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.add(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)}.
     */
    @Test
    public void testUpdate() {
        final Music music = MusicUtils.newMusic(1);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Music> musicArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Music.class);

        when(musicService.get(anyInt())).thenReturn(MusicUtils.newMusicDomain(1));

        musicFacade.update(music);

        verify(musicService).get(1);
        verify(musicService).update(musicArgumentCaptor.capture());
        verify(musicValidator).validate(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);

        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusicDomain(1), musicArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.update(null);
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.update(MusicUtils.newMusic(null));
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.update(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#remove(Music)}.
     */
    @Test
    public void testRemove() {
        final cz.vhromada.catalog.domain.Music musicEntity = MusicUtils.newMusicDomain(1);
        final Music music = MusicUtils.newMusic(1);

        when(musicService.get(anyInt())).thenReturn(musicEntity);

        musicFacade.remove(music);

        verify(musicService).get(1);
        verify(musicService).remove(musicEntity);
        verify(musicValidator).validate(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MusicFacade#remove(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.remove(null);
    }

    /**
     * Test method for {@link MusicFacade#remove(Music)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.remove(MusicUtils.newMusic(null));
    }

    /**
     * Test method for {@link MusicFacade#remove(Music)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.remove(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(Music)}.
     */
    @Test
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Music musicEntity = MusicUtils.newMusicDomain(1);
        final Music music = MusicUtils.newMusic(1);

        when(musicService.get(anyInt())).thenReturn(musicEntity);

        musicFacade.duplicate(music);

        verify(musicService).get(1);
        verify(musicService).duplicate(musicEntity);
        verify(musicValidator).validate(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MusicFacade#duplicate(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.duplicate(null);
    }

    /**
     * Test method for {@link MusicFacade#duplicate(Music)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.duplicate(MusicUtils.newMusic(null));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(Music)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.duplicate(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)}.
     */
    @Test
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Music musicEntity = MusicUtils.newMusicDomain(2);
        final List<cz.vhromada.catalog.domain.Music> musicList = CollectionUtils.newList(MusicUtils.newMusicDomain(1), musicEntity);
        final Music music = MusicUtils.newMusic(2);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(musicService.getAll()).thenReturn(musicList);

        musicFacade.moveUp(music);

        verify(musicService).get(2);
        verify(musicService).getAll();
        verify(musicService).moveUp(musicEntity);
        verify(musicValidator).validate(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.moveUp(null);
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.moveUp(MusicUtils.newMusic(null));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.moveUp(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(Music)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Music musicEntity = MusicUtils.newMusicDomain(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Music> musicList = CollectionUtils.newList(musicEntity, MusicUtils.newMusicDomain(1));
        final Music music = MusicUtils.newMusic(Integer.MAX_VALUE);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(musicService.getAll()).thenReturn(musicList);

        musicFacade.moveUp(music);
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)}.
     */
    @Test
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Music musicEntity = MusicUtils.newMusicDomain(1);
        final List<cz.vhromada.catalog.domain.Music> musicList = CollectionUtils.newList(musicEntity, MusicUtils.newMusicDomain(2));
        final Music music = MusicUtils.newMusic(1);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(musicService.getAll()).thenReturn(musicList);

        musicFacade.moveDown(music);

        verify(musicService).get(1);
        verify(musicService).getAll();
        verify(musicService).moveDown(musicEntity);
        verify(musicValidator).validate(music);
        verifyNoMoreInteractions(musicService, musicValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.moveDown(null);
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadArgument() {
        doThrow(IllegalArgumentException.class).when(musicValidator).validate(any(Music.class));

        musicFacade.moveDown(MusicUtils.newMusic(null));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotExistingArgument() {
        when(musicService.get(anyInt())).thenReturn(null);

        musicFacade.moveDown(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(Music)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Music musicEntity = MusicUtils.newMusicDomain(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Music> musicList = CollectionUtils.newList(MusicUtils.newMusicDomain(1), musicEntity);
        final Music music = MusicUtils.newMusic(Integer.MAX_VALUE);

        when(musicService.get(anyInt())).thenReturn(musicEntity);
        when(musicService.getAll()).thenReturn(musicList);

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
        final cz.vhromada.catalog.domain.Music music1 = MusicUtils.newMusicDomain(1);
        final cz.vhromada.catalog.domain.Music music2 = MusicUtils.newMusicDomain(2);
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
        final List<cz.vhromada.catalog.domain.Music> musicList = CollectionUtils.newList(MusicUtils.newMusicWithSongs(1), MusicUtils.newMusicWithSongs(2));
        int expectedTotalLength = 0;
        for (final cz.vhromada.catalog.domain.Music music : musicList) {
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
        final cz.vhromada.catalog.domain.Music music1 = MusicUtils.newMusicWithSongs(1);
        final cz.vhromada.catalog.domain.Music music2 = MusicUtils.newMusicWithSongs(2);
        final int expectedSongs = music1.getSongs().size() + music2.getSongs().size();

        when(musicService.getAll()).thenReturn(CollectionUtils.newList(music1, music2));

        assertEquals(expectedSongs, musicFacade.getSongsCount());

        verify(musicService).getAll();
        verifyNoMoreInteractions(musicService);
        verifyZeroInteractions(converter, musicValidator);
    }

}
