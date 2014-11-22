package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.catalog.facade.validators.SongTOValidator;
import cz.vhromada.catalog.service.MusicService;
import cz.vhromada.catalog.service.SongService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.convert.ConversionService;

/**
 * A class represents test for class {@link SongFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SongFacadeImplTest extends ObjectGeneratorTest {

    /** Instance of {@link MusicService} */
    @Mock
    private MusicService musicService;

    /** Instance of {@link SongService} */
    @Mock
    private SongService songService;

    /** Instance of {@link ConversionService} */
    @Mock
    private ConversionService conversionService;

    /** Instance of {@link MusicTOValidator} */
    @Mock
    private MusicTOValidator musicTOValidator;

    /** Instance of {@link SongTOValidator} */
    @Mock
    private SongTOValidator songTOValidator;

    /** Instance of (@link SongFacade} */
    @InjectMocks
    private SongFacade songFacade = new SongFacadeImpl();

    /** Test method for {@link SongFacade#getSong(Integer)} with existing song. */
    @Test
    public void testGetSongWithExistingSong() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(song);
        when(conversionService.convert(any(Song.class), eq(SongTO.class))).thenReturn(songTO);

        DeepAsserts.assertEquals(songTO, songFacade.getSong(songTO.getId()));

        verify(songService).getSong(songTO.getId());
        verify(conversionService).convert(song, SongTO.class);
        verifyNoMoreInteractions(songService, conversionService);
    }

    /** Test method for {@link SongFacade#getSong(Integer)} with not existing song. */
    @Test
    public void testGetSongWithNotExistingSong() {
        when(songService.getSong(anyInt())).thenReturn(null);
        when(conversionService.convert(any(Song.class), eq(SongTO.class))).thenReturn(null);

        assertNull(songFacade.getSong(Integer.MAX_VALUE));

        verify(songService).getSong(Integer.MAX_VALUE);
        verify(conversionService).convert(null, SongTO.class);
        verifyNoMoreInteractions(songService, conversionService);
    }

    /** Test method for {@link SongFacade#getSong(Integer)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testGetSongWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.getSong(Integer.MAX_VALUE);
    }

    /** Test method for {@link SongFacade#getSong(Integer)} with not set conversion service. */
    @Test(expected = IllegalStateException.class)
    public void testGetSongWithNotSetConversionService() {
        ((SongFacadeImpl) songFacade).setConversionService(null);
        songFacade.getSong(Integer.MAX_VALUE);
    }

    /** Test method for {@link SongFacade#getSong(Integer)} with null argument. */
    @Test
    public void testGetSongWithNullArgument() {
        try {
            songFacade.getSong(null);
            fail("Can't get song with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(songService, conversionService);
    }

    /** Test method for {@link SongFacade#getSong(Integer)} with exception in service tier. */
    @Test
    public void testGetSongWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(songService).getSong(anyInt());

        try {
            songFacade.getSong(Integer.MAX_VALUE);
            fail("Can't get song with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(songService).getSong(Integer.MAX_VALUE);
        verifyNoMoreInteractions(songService);
        verifyZeroInteractions(conversionService);
    }

    /** Test method for {@link SongFacade#add(SongTO)}. */
    @Test
    public void testAdd() {
        final Song song = generate(Song.class);
        song.setId(null);
        final SongTO songTO = generate(SongTO.class);
        songTO.setId(null);
        final int id = generate(Integer.class);
        final int position = generate(Integer.class);
        when(musicService.getMusic(anyInt())).thenReturn(generate(Music.class));
        doAnswer(setSongIdAndPosition(id, position)).when(songService).add(any(Song.class));
        when(conversionService.convert(any(SongTO.class), eq(Song.class))).thenReturn(song);

        songFacade.add(songTO);

        DeepAsserts.assertEquals(id, song.getId());
        DeepAsserts.assertEquals(position, song.getPosition());

        verify(musicService).getMusic(songTO.getMusic().getId());
        verify(songService).add(song);
        verify(conversionService).convert(songTO, Song.class);
        verify(songTOValidator).validateNewSongTO(songTO);
        verifyNoMoreInteractions(musicService, songService, conversionService, songTOValidator);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with not set service for music. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetMusicService() {
        ((SongFacadeImpl) songFacade).setMusicService(null);
        songFacade.add(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#add(SongTO)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.add(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#add(SongTO)} with not set conversion service. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetConversionService() {
        ((SongFacadeImpl) songFacade).setConversionService(null);
        songFacade.add(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#add(SongTO)} with not set validator for TO for song. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetSongTOValidator() {
        ((SongFacadeImpl) songFacade).setSongTOValidator(null);
        songFacade.add(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#add(SongTO)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(songTOValidator).validateNewSongTO(any(SongTO.class));

        try {
            songFacade.add(null);
            fail("Can't add song with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(songTOValidator).validateNewSongTO(null);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(musicService, songService, conversionService);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with argument with bad data. */
    @Test
    public void testAddWithBadArgument() {
        final SongTO song = generate(SongTO.class);
        song.setId(null);
        doThrow(ValidationException.class).when(songTOValidator).validateNewSongTO(any(SongTO.class));

        try {
            songFacade.add(song);
            fail("Can't add song with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songTOValidator).validateNewSongTO(song);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(musicService, songService, conversionService);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with not existing argument. */
    @Test
    public void testAddWithNotExistingArgument() {
        final SongTO song = generate(SongTO.class);
        song.setId(null);
        when(musicService.getMusic(anyInt())).thenReturn(null);

        try {
            songFacade.add(song);
            fail("Can't add song with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(musicService).getMusic(song.getMusic().getId());
        verify(songTOValidator).validateNewSongTO(song);
        verifyNoMoreInteractions(musicService, songTOValidator);
        verifyZeroInteractions(songService, conversionService);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with service tier not setting ID. */
    @Test
    public void testAddWithNotServiceTierSettingID() {
        final Song song = generate(Song.class);
        song.setId(null);
        final SongTO songTO = generate(SongTO.class);
        songTO.setId(null);
        when(musicService.getMusic(anyInt())).thenReturn(generate(Music.class));
        when(conversionService.convert(any(SongTO.class), eq(Song.class))).thenReturn(song);

        try {
            songFacade.add(songTO);
            fail("Can't add song with service tier not setting ID.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic(songTO.getMusic().getId());
        verify(songService).add(song);
        verify(conversionService).convert(songTO, Song.class);
        verify(songTOValidator).validateNewSongTO(songTO);
        verifyNoMoreInteractions(musicService, songService, conversionService, songTOValidator);
    }

    /** Test method for {@link SongFacade#add(SongTO)} with exception in service tier. */
    @Test
    public void testAddWithServiceTierException() {
        final SongTO song = generate(SongTO.class);
        song.setId(null);
        doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

        try {
            songFacade.add(song);
            fail("Can't add song with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic(song.getMusic().getId());
        verify(songTOValidator).validateNewSongTO(song);
        verifyNoMoreInteractions(musicService, songTOValidator);
        verifyZeroInteractions(songService, conversionService);
    }

    /** Test method for {@link SongFacade#update(SongTO)}. */
    @Test
    public void testUpdate() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(generate(Music.class));
        when(songService.exists(any(Song.class))).thenReturn(true);
        when(conversionService.convert(any(SongTO.class), eq(Song.class))).thenReturn(song);

        songFacade.update(songTO);

        verify(musicService).getMusic(songTO.getMusic().getId());
        verify(songService).exists(song);
        verify(songService).update(song);
        verify(conversionService).convert(songTO, Song.class);
        verify(songTOValidator).validateExistingSongTO(songTO);
        verifyNoMoreInteractions(musicService, songService, conversionService, songTOValidator);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with not set service for music. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetMusicService() {
        ((SongFacadeImpl) songFacade).setMusicService(null);
        songFacade.update(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#update(SongTO)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.update(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#update(SongTO)} with not set conversion service. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetConversionService() {
        ((SongFacadeImpl) songFacade).setConversionService(null);
        songFacade.update(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#update(SongTO)} with not set validator for TO for song. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetSongTOValidator() {
        ((SongFacadeImpl) songFacade).setSongTOValidator(null);
        songFacade.update(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#update(SongTO)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(songTOValidator).validateExistingSongTO(any(SongTO.class));

        try {
            songFacade.update(null);
            fail("Can't update song with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(songTOValidator).validateExistingSongTO(null);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(musicService, songService, conversionService);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with argument with bad data. */
    @Test
    public void testUpdateWithBadArgument() {
        final SongTO song = generate(SongTO.class);
        doThrow(ValidationException.class).when(songTOValidator).validateExistingSongTO(any(SongTO.class));

        try {
            songFacade.update(song);
            fail("Can't update song with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songTOValidator).validateExistingSongTO(song);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(musicService, songService, conversionService);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with not existing argument. */
    @Test
    public void testUpdateWithNotExistingArgument() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        when(songService.exists(any(Song.class))).thenReturn(false);
        when(conversionService.convert(any(SongTO.class), eq(Song.class))).thenReturn(song);

        try {
            songFacade.update(songTO);
            fail("Can't update song with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(songService).exists(song);
        verify(conversionService).convert(songTO, Song.class);
        verify(songTOValidator).validateExistingSongTO(songTO);
        verifyNoMoreInteractions(songService, conversionService, songTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link SongFacade#update(SongTO)} with exception in service tier. */
    @Test
    public void testUpdateWithServiceTierException() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        doThrow(ServiceOperationException.class).when(songService).exists(any(Song.class));
        when(conversionService.convert(any(SongTO.class), eq(Song.class))).thenReturn(song);

        try {
            songFacade.update(songTO);
            fail("Can't update song with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(songService).exists(song);
        verify(conversionService).convert(songTO, Song.class);
        verify(songTOValidator).validateExistingSongTO(songTO);
        verifyNoMoreInteractions(songService, conversionService, songTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link SongFacade#remove(SongTO)}. */
    @Test
    public void testRemove() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(song);

        songFacade.remove(songTO);

        verify(songService).getSong(songTO.getId());
        verify(songService).remove(song);
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.remove(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with not set validator for TO for song. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetSongTOValidator() {
        ((SongFacadeImpl) songFacade).setSongTOValidator(null);
        songFacade.remove(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.remove(null);
            fail("Can't remove song with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(null);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService);
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with argument with bad data. */
    @Test
    public void testRemoveWithBadArgument() {
        final SongTO song = generate(SongTO.class);
        doThrow(ValidationException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.remove(song);
            fail("Can't remove song with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService);
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with not existing argument. */
    @Test
    public void testRemoveWithNotExistingArgument() {
        final SongTO song = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(null);

        try {
            songFacade.remove(song);
            fail("Can't remove song with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(songService).getSong(song.getId());
        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#remove(SongTO)} with exception in service tier. */
    @Test
    public void testRemoveWithServiceTierException() {
        final SongTO song = generate(SongTO.class);
        doThrow(ServiceOperationException.class).when(songService).getSong(anyInt());

        try {
            songFacade.remove(song);
            fail("Can't remove song with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(songService).getSong(song.getId());
        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)}. */
    @Test
    public void testDuplicate() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(song);

        songFacade.duplicate(songTO);

        verify(songService).getSong(songTO.getId());
        verify(songService).duplicate(song);
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.duplicate(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with not set validator for TO for song. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetSongTOValidator() {
        ((SongFacadeImpl) songFacade).setSongTOValidator(null);
        songFacade.duplicate(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with null argument. */
    @Test
    public void testDuplicateWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.duplicate(null);
            fail("Can't duplicate song with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(null);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService);
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with argument with bad data. */
    @Test
    public void testDuplicateWithBadArgument() {
        final SongTO song = generate(SongTO.class);
        doThrow(ValidationException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.duplicate(song);
            fail("Can't duplicate song with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService);
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with not existing argument. */
    @Test
    public void testDuplicateWithNotExistingArgument() {
        final SongTO song = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(null);

        try {
            songFacade.duplicate(song);
            fail("Can't duplicate song with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(songService).getSong(song.getId());
        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#duplicate(SongTO)} with exception in service tier. */
    @Test
    public void testDuplicateWithServiceTierException() {
        final SongTO song = generate(SongTO.class);
        doThrow(ServiceOperationException.class).when(songService).getSong(anyInt());

        try {
            songFacade.duplicate(song);
            fail("Can't duplicate song with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(songService).getSong(song.getId());
        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)}. */
    @Test
    public void testMoveUp() {
        final Song song = generate(Song.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), song);
        final SongTO songTO = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(song);
        when(songService.findSongsByMusic(any(Music.class))).thenReturn(songs);

        songFacade.moveUp(songTO);

        verify(songService).getSong(songTO.getId());
        verify(songService).findSongsByMusic(song.getMusic());
        verify(songService).moveUp(song);
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testMoveUpWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.moveUp(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with not set validator for TO for song. */
    @Test(expected = IllegalStateException.class)
    public void testMoveUpWithNotSetSongTOValidator() {
        ((SongFacadeImpl) songFacade).setSongTOValidator(null);
        songFacade.moveUp(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with null argument. */
    @Test
    public void testMoveUpWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.moveUp(null);
            fail("Can't move up song with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(null);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService);
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with argument with bad data. */
    @Test
    public void testMoveUpWithBadArgument() {
        final SongTO song = generate(SongTO.class);
        doThrow(ValidationException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.moveUp(song);
            fail("Can't move up song with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService);
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with not existing argument. */
    @Test
    public void testMoveUpWithNotExistingArgument() {
        final SongTO song = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(null);

        try {
            songFacade.moveUp(song);
            fail("Can't move up song with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(songService).getSong(song.getId());
        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with not moveable argument. */
    @Test
    public void testMoveUpWithNotMoveableArgument() {
        final Song song = generate(Song.class);
        final List<Song> songs = CollectionUtils.newList(song, mock(Song.class));
        final SongTO songTO = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(song);
        when(songService.findSongsByMusic(any(Music.class))).thenReturn(songs);

        try {
            songFacade.moveUp(songTO);
            fail("Can't move up song with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songService).getSong(songTO.getId());
        verify(songService).findSongsByMusic(song.getMusic());
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#moveUp(SongTO)} with exception in service tier. */
    @Test
    public void testMoveUpWithServiceTierException() {
        final SongTO song = generate(SongTO.class);
        doThrow(ServiceOperationException.class).when(songService).getSong(anyInt());

        try {
            songFacade.moveUp(song);
            fail("Can't move up song with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(songService).getSong(song.getId());
        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)}. */
    @Test
    public void testMoveDown() {
        final Song song = generate(Song.class);
        final List<Song> songs = CollectionUtils.newList(song, mock(Song.class));
        final SongTO songTO = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(song);
        when(songService.findSongsByMusic(any(Music.class))).thenReturn(songs);

        songFacade.moveDown(songTO);

        verify(songService).getSong(songTO.getId());
        verify(songService).findSongsByMusic(song.getMusic());
        verify(songService).moveDown(song);
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testMoveDownWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.moveDown(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with not set validator for TO for song. */
    @Test(expected = IllegalStateException.class)
    public void testMoveDownWithNotSetSongTOValidator() {
        ((SongFacadeImpl) songFacade).setSongTOValidator(null);
        songFacade.moveDown(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with null argument. */
    @Test
    public void testMoveDownWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.moveDown(null);
            fail("Can't move down song with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(null);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService);
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with argument with bad data. */
    @Test
    public void testMoveDownWithBadArgument() {
        final SongTO song = generate(SongTO.class);
        doThrow(ValidationException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.moveDown(song);
            fail("Can't move down song with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService);
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with not existing argument. */
    @Test
    public void testMoveDownWithNotExistingArgument() {
        final SongTO song = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(null);

        try {
            songFacade.moveDown(song);
            fail("Can't move down song with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(songService).getSong(song.getId());
        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with not moveable argument. */
    @Test
    public void testMoveDownWithNotMoveableArgument() {
        final Song song = generate(Song.class);
        final List<Song> songs = CollectionUtils.newList(mock(Song.class), song);
        final SongTO songTO = generate(SongTO.class);
        when(songService.getSong(anyInt())).thenReturn(song);
        when(songService.findSongsByMusic(any(Music.class))).thenReturn(songs);

        try {
            songFacade.moveDown(songTO);
            fail("Can't move down song with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songService).getSong(songTO.getId());
        verify(songService).findSongsByMusic(song.getMusic());
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#moveDown(SongTO)} with exception in service tier. */
    @Test
    public void testMoveDownWithServiceTierException() {
        final SongTO song = generate(SongTO.class);
        doThrow(ServiceOperationException.class).when(songService).getSong(anyInt());

        try {
            songFacade.moveDown(song);
            fail("Can't move down song with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(songService).getSong(song.getId());
        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songService, songTOValidator);
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with existing song. */
    @Test
    public void testExistsWithExistingSong() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        when(songService.exists(any(Song.class))).thenReturn(true);
        when(conversionService.convert(any(SongTO.class), eq(Song.class))).thenReturn(song);

        assertTrue(songFacade.exists(songTO));

        verify(songService).exists(song);
        verify(conversionService).convert(songTO, Song.class);
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, conversionService, songTOValidator);
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with not existing song. */
    @Test
    public void testExistsWithNotExistingSong() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        when(songService.exists(any(Song.class))).thenReturn(false);
        when(conversionService.convert(any(SongTO.class), eq(Song.class))).thenReturn(song);

        assertFalse(songFacade.exists(songTO));

        verify(songService).exists(song);
        verify(conversionService).convert(songTO, Song.class);
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, conversionService, songTOValidator);
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testExistsWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.exists(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with not set conversion service. */
    @Test(expected = IllegalStateException.class)
    public void testExistsWithNotSetConversionService() {
        ((SongFacadeImpl) songFacade).setConversionService(null);
        songFacade.exists(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with not set validator for TO for song. */
    @Test(expected = IllegalStateException.class)
    public void testExistsWithNotSetSongTOValidator() {
        ((SongFacadeImpl) songFacade).setSongTOValidator(null);
        songFacade.exists(mock(SongTO.class));
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with null argument. */
    @Test
    public void testExistsWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.exists(null);
            fail("Can't exists song with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(null);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService, conversionService);
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with argument with bad data. */
    @Test
    public void testExistsWithBadArgument() {
        final SongTO song = generate(SongTO.class);
        doThrow(ValidationException.class).when(songTOValidator).validateSongTOWithId(any(SongTO.class));

        try {
            songFacade.exists(song);
            fail("Can't exists song with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(songTOValidator).validateSongTOWithId(song);
        verifyNoMoreInteractions(songTOValidator);
        verifyZeroInteractions(songService, conversionService);
    }

    /** Test method for {@link SongFacade#exists(SongTO)} with exception in service tier. */
    @Test
    public void testExistsWithServiceTierException() {
        final Song song = generate(Song.class);
        final SongTO songTO = generate(SongTO.class);
        doThrow(ServiceOperationException.class).when(songService).exists(any(Song.class));
        when(conversionService.convert(any(SongTO.class), eq(Song.class))).thenReturn(song);

        try {
            songFacade.exists(songTO);
            fail("Can't exists song with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(songService).exists(song);
        verify(conversionService).convert(songTO, Song.class);
        verify(songTOValidator).validateSongTOWithId(songTO);
        verifyNoMoreInteractions(songService, conversionService, songTOValidator);
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)}. */
    @Test
    public void testFindSongsByMusic() {
        final Music music = generate(Music.class);
        final List<Song> songs = CollectionUtils.newList(generate(Song.class), generate(Song.class));
        final MusicTO musicTO = generate(MusicTO.class);
        final List<SongTO> songsList = CollectionUtils.newList(generate(SongTO.class), generate(SongTO.class));
        when(musicService.getMusic(anyInt())).thenReturn(music);
        when(songService.findSongsByMusic(any(Music.class))).thenReturn(songs);
        for (int i = 0; i < songs.size(); i++) {
            final Song song = songs.get(i);
            when(conversionService.convert(song, SongTO.class)).thenReturn(songsList.get(i));
        }

        DeepAsserts.assertEquals(songsList, songFacade.findSongsByMusic(musicTO));

        verify(musicService).getMusic(musicTO.getId());
        verify(songService).findSongsByMusic(music);
        for (final Song song : songs) {
            verify(conversionService).convert(song, SongTO.class);
        }
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, songService, conversionService, musicTOValidator);
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with not set service for music. */
    @Test(expected = IllegalStateException.class)
    public void testFindSongsByMusicWithNotSetMusicService() {
        ((SongFacadeImpl) songFacade).setMusicService(null);
        songFacade.findSongsByMusic(mock(MusicTO.class));
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with not set service for songs. */
    @Test(expected = IllegalStateException.class)
    public void testFindSongsByMusicWithNotSetSongService() {
        ((SongFacadeImpl) songFacade).setSongService(null);
        songFacade.findSongsByMusic(mock(MusicTO.class));
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with not set conversion service. */
    @Test(expected = IllegalStateException.class)
    public void testFindSongsByMusicWithNotSetConversionService() {
        ((SongFacadeImpl) songFacade).setConversionService(null);
        songFacade.findSongsByMusic(mock(MusicTO.class));
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with not set validator for TO for music. */
    @Test(expected = IllegalStateException.class)
    public void testFindSongsByMusicWithNotSetMusicTOValidator() {
        ((SongFacadeImpl) songFacade).setMusicTOValidator(null);
        songFacade.findSongsByMusic(mock(MusicTO.class));
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with null argument. */
    @Test
    public void testFindSongsByMusicWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            songFacade.findSongsByMusic(null);
            fail("Can't find songs by music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(null);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService, songService, conversionService);
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with argument with bad data. */
    @Test
    public void testFindSongsByMusicWithBadArgument() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ValidationException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            songFacade.findSongsByMusic(music);
            fail("Can't find songs by music with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService, songService, conversionService);
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with not existing argument. */
    @Test
    public void testFindSongsByMusicWithNotExistingArgument() {
        final MusicTO music = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(null);

        try {
            songFacade.findSongsByMusic(music);
            fail("Can't find songs by music with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
        verifyZeroInteractions(songService, conversionService);
    }

    /** Test method for {@link SongFacade#findSongsByMusic(MusicTO)} with exception in service tier. */
    @Test
    public void testFindSongsByMusicWithServiceTierException() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

        try {
            songFacade.findSongsByMusic(music);
            fail("Can't find songs by music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
        verifyZeroInteractions(songService, conversionService);
    }

    /**
     * Sets song's ID and position.
     *
     * @param id       ID
     * @param position position
     * @return mocked answer
     */
    private static Answer<Void> setSongIdAndPosition(final Integer id, final int position) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                final Song song = (Song) invocation.getArguments()[0];
                song.setId(id);
                song.setPosition(position);
                return null;
            }

        };
    }

}
