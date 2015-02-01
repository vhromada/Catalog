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
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.catalog.service.MusicService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.convert.ConversionService;

/**
 * A class represents test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicFacadeImplTest extends ObjectGeneratorTest {

    /** Instance of {@link MusicService} */
    @Mock
    private MusicService musicService;

    /** Instance of {@link ConversionService} */
    @Mock
    private ConversionService conversionService;

    /** Instance of {@link MusicTOValidator} */
    @Mock
    private MusicTOValidator musicTOValidator;

    /** Instance of {@link MusicFacade} */
    private MusicFacade musicFacade;

    /** Initializes facade for music. */
    @Before
    public void setUp() {
        musicFacade = new MusicFacadeImpl(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacadeImpl#MusicFacadeImpl(MusicService, ConversionService, MusicTOValidator)} with null service for music. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMusicService() {
        new MusicFacadeImpl(null, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacadeImpl#MusicFacadeImpl(MusicService, ConversionService, MusicTOValidator)} with null conversion service. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConversionService() {
        new MusicFacadeImpl(musicService, null, musicTOValidator);
    }

    /** Test method for {@link MusicFacadeImpl#MusicFacadeImpl(MusicService, ConversionService, MusicTOValidator)} with null validator for TO for music. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMusicTOValidator() {
        new MusicFacadeImpl(musicService, conversionService, null);
    }

    /** Test method for {@link MusicFacade#newData()}. */
    @Test
    public void testNewData() {
        musicFacade.newData();

        verify(musicService).newData();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#newData()} with exception in service tier. */
    @Test
    public void testNewDataWithFacadeTierException() {
        doThrow(ServiceOperationException.class).when(musicService).newData();

        try {
            musicFacade.newData();
            fail("Can't create new data with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).newData();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#getMusic()}. */
    @Test
    public void testGetMusic() {
        final List<Music> music = CollectionUtils.newList(generate(Music.class), generate(Music.class));
        final List<MusicTO> musicList = CollectionUtils.newList(generate(MusicTO.class), generate(MusicTO.class));
        when(musicService.getMusic()).thenReturn(music);
        for (int i = 0; i < music.size(); i++) {
            final Music aMusic = music.get(i);
            when(conversionService.convert(aMusic, MusicTO.class)).thenReturn(musicList.get(i));
        }

        DeepAsserts.assertEquals(musicList, musicFacade.getMusic());

        verify(musicService).getMusic();
        for (final Music aMusic : music) {
            verify(conversionService).convert(aMusic, MusicTO.class);
        }
        verifyNoMoreInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#getMusic()} with null music. */
    @Test
    public void testGetMusicWithNullMusic() {
        final List<Music> music = CollectionUtils.newList(generate(Music.class), generate(Music.class));
        final List<MusicTO> musicList = CollectionUtils.newList(null, null);
        when(musicService.getMusic()).thenReturn(music);
        when(conversionService.convert(any(Music.class), eq(MusicTO.class))).thenReturn(null);

        final List<MusicTO> result = musicFacade.getMusic();
        DeepAsserts.assertEquals(musicList, result);

        verify(musicService).getMusic();
        for (final Music aMusic : music) {
            verify(conversionService).convert(aMusic, MusicTO.class);
        }
        verifyNoMoreInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#getMusic()} with exception in service tier. */
    @Test
    public void testGetMusicWithFacadeTierException() {
        doThrow(ServiceOperationException.class).when(musicService).getMusic();

        try {
            musicFacade.getMusic();
            fail("Can't get music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic();
        verifyNoMoreInteractions(musicService);
        verifyZeroInteractions(conversionService);
    }

    /** Test method for {@link MusicFacade#getMusic(Integer)} with existing music. */
    @Test
    public void testGetMusicByIdWithExistingMusic() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(music);
        when(conversionService.convert(any(Music.class), eq(MusicTO.class))).thenReturn(musicTO);

        DeepAsserts.assertEquals(musicTO, musicFacade.getMusic(musicTO.getId()));

        verify(musicService).getMusic(musicTO.getId());
        verify(conversionService).convert(music, MusicTO.class);
        verifyNoMoreInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#getMusic(Integer)} with not existing music. */
    @Test
    public void testGetMusicByIdWithNotExistingMusic() {
        when(musicService.getMusic(anyInt())).thenReturn(null);
        when(conversionService.convert(any(Music.class), eq(MusicTO.class))).thenReturn(null);

        assertNull(musicFacade.getMusic(Integer.MAX_VALUE));

        verify(musicService).getMusic(Integer.MAX_VALUE);
        verify(conversionService).convert(null, MusicTO.class);
        verifyNoMoreInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#getMusic(Integer)} with null argument. */
    @Test
    public void testGetMusicByIdWithNullArgument() {
        try {
            musicFacade.getMusic(null);
            fail("Can't get music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#getMusic(Integer)} with exception in service tier. */
    @Test
    public void testGetMusicByIdWithFacadeTierException() {
        doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

        try {
            musicFacade.getMusic(Integer.MAX_VALUE);
            fail("Can't get music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic(anyInt());
        verifyNoMoreInteractions(musicService);
        verifyZeroInteractions(conversionService);
    }

    /** Test method for {@link MusicFacade#add(MusicTO)}. */
    @Test
    public void testAdd() {
        final Music music = generate(Music.class);
        music.setId(null);
        final MusicTO musicTO = generate(MusicTO.class);
        musicTO.setId(null);
        final int id = generate(Integer.class);
        final int position = generate(Integer.class);
        doAnswer(setMusicIdAndPosition(id, position)).when(musicService).add(any(Music.class));
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        musicFacade.add(musicTO);
        DeepAsserts.assertEquals(id, musicTO.getId());
        DeepAsserts.assertEquals(position, musicTO.getPosition());

        verify(musicService).add(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateNewMusicTO(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#add(MusicTO)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(musicTOValidator).validateNewMusicTO(any(MusicTO.class));

        try {
            musicFacade.add(null);
            fail("Can't add music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(musicTOValidator).validateNewMusicTO(null);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#add(MusicTO)} with argument with bad data. */
    @Test
    public void testAddWithBadArgument() {
        final MusicTO music = generate(MusicTO.class);
        music.setId(null);
        doThrow(ValidationException.class).when(musicTOValidator).validateNewMusicTO(any(MusicTO.class));

        try {
            musicFacade.add(music);
            fail("Can't add music with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicTOValidator).validateNewMusicTO(music);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#add(MusicTO)} with service tier not setting ID. */
    @Test
    public void testAddWithNotServiceTierSettingID() {
        final Music music = generate(Music.class);
        music.setId(null);
        final MusicTO musicTO = generate(MusicTO.class);
        musicTO.setId(null);
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        try {
            musicFacade.add(musicTO);
            fail("Can't add music with service tier not setting ID.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).add(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateNewMusicTO(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#add(MusicTO)} with exception in service tier. */
    @Test
    public void testAddWithServiceTierException() {
        final Music music = generate(Music.class);
        music.setId(null);
        final MusicTO musicTO = generate(MusicTO.class);
        musicTO.setId(null);
        doThrow(ServiceOperationException.class).when(musicService).add(any(Music.class));
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        try {
            musicFacade.add(musicTO);
            fail("Can't add music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).add(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateNewMusicTO(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#update(MusicTO)}. */
    @Test
    public void testUpdate() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.exists(any(Music.class))).thenReturn(true);
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        musicFacade.update(musicTO);

        verify(musicService).exists(music);
        verify(musicService).update(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateExistingMusicTO(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#update(MusicTO)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(musicTOValidator).validateExistingMusicTO(any(MusicTO.class));

        try {
            musicFacade.update(null);
            fail("Can't update music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(musicTOValidator).validateExistingMusicTO(null);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#update(MusicTO)} with argument with bad data. */
    @Test
    public void testUpdateWithBadArgument() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ValidationException.class).when(musicTOValidator).validateExistingMusicTO(any(MusicTO.class));

        try {
            musicFacade.update(music);
            fail("Can't update music with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicTOValidator).validateExistingMusicTO(music);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#update(MusicTO)} with not existing argument. */
    @Test
    public void testUpdateWithNotExistingArgument() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.exists(any(Music.class))).thenReturn(false);
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        try {
            musicFacade.update(musicTO);
            fail("Can't update music with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(musicService).exists(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateExistingMusicTO(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#update(MusicTO)} with exception in service tier. */
    @Test
    public void testUpdateWithServiceTierException() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        doThrow(ServiceOperationException.class).when(musicService).exists(any(Music.class));
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        try {
            musicFacade.update(musicTO);
            fail("Can't update music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).exists(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateExistingMusicTO(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#remove(MusicTO)}. */
    @Test
    public void testRemove() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(music);

        musicFacade.remove(musicTO);

        verify(musicService).getMusic(musicTO.getId());
        verify(musicService).remove(music);
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#remove(MusicTO)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.remove(null);
            fail("Can't remove music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(null);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#remove(MusicTO)} with argument with bad data. */
    @Test
    public void testRemoveWithBadArgument() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ValidationException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.remove(music);
            fail("Can't remove music with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#remove(MusicTO)} with not existing argument. */
    @Test
    public void testRemoveWithNotExistingArgument() {
        final MusicTO music = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(null);

        try {
            musicFacade.remove(music);
            fail("Can't remove music with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#remove(MusicTO)} with exception in service tier. */
    @Test
    public void testRemoveWithServiceTierException() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

        try {
            musicFacade.remove(music);
            fail("Can't remove music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#duplicate(MusicTO)}. */
    @Test
    public void testDuplicate() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(music);

        musicFacade.duplicate(musicTO);

        verify(musicService).getMusic(musicTO.getId());
        verify(musicService).duplicate(music);
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#duplicate(MusicTO)} with null argument. */
    @Test
    public void testDuplicateWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.duplicate(null);
            fail("Can't duplicate music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(null);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#duplicate(MusicTO)} with argument with bad data. */
    @Test
    public void testDuplicateWithBadArgument() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ValidationException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.duplicate(music);
            fail("Can't duplicate music with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#duplicate(MusicTO)} with not existing argument. */
    @Test
    public void testDuplicateWithNotExistingArgument() {
        final MusicTO music = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(null);

        try {
            musicFacade.duplicate(music);
            fail("Can't duplicate music with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#duplicate(MusicTO)} with exception in service tier. */
    @Test
    public void testDuplicateWithServiceTierException() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

        try {
            musicFacade.duplicate(music);
            fail("Can't duplicate music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#moveUp(MusicTO)}. */
    @Test
    public void testMoveUp() {
        final Music music = generate(Music.class);
        final List<Music> musicList = CollectionUtils.newList(mock(Music.class), music);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(music);
        when(musicService.getMusic()).thenReturn(musicList);

        musicFacade.moveUp(musicTO);

        verify(musicService).getMusic(musicTO.getId());
        verify(musicService).getMusic();
        verify(musicService).moveUp(music);
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#moveUp(MusicTO)} with null argument. */
    @Test
    public void testMoveUpWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.moveUp(null);
            fail("Can't move up music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(null);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#moveUp(MusicTO)} with argument with bad data. */
    @Test
    public void testMoveUpWithBadArgument() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ValidationException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.moveUp(music);
            fail("Can't move up music with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#moveUp(MusicTO)} with not existing argument. */
    @Test
    public void testMoveUpWithNotExistingArgument() {
        final MusicTO music = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(null);

        try {
            musicFacade.moveUp(music);
            fail("Can't move up music with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#moveUp(MusicTO)} with not moveable argument. */
    @Test
    public void testMoveUpWithNotMoveableArgument() {
        final Music music = generate(Music.class);
        final List<Music> musicList = CollectionUtils.newList(music, mock(Music.class));
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(music);
        when(musicService.getMusic()).thenReturn(musicList);

        try {
            musicFacade.moveUp(musicTO);
            fail("Can't move up music with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicService).getMusic(musicTO.getId());
        verify(musicService).getMusic();
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#moveUp(MusicTO)} with exception in service tier. */
    @Test
    public void testMoveUpWithServiceTierException() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

        try {
            musicFacade.moveUp(music);
            fail("Can't move up music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#moveDown(MusicTO)}. */
    @Test
    public void testMoveDown() {
        final Music music = generate(Music.class);
        final List<Music> musicList = CollectionUtils.newList(music, mock(Music.class));
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(music);
        when(musicService.getMusic()).thenReturn(musicList);

        musicFacade.moveDown(musicTO);

        verify(musicService).getMusic(musicTO.getId());
        verify(musicService).getMusic();
        verify(musicService).moveDown(music);
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#moveDown(MusicTO)} with null argument. */
    @Test
    public void testMoveDownWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.moveDown(null);
            fail("Can't move down music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(null);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#moveDown(MusicTO)} with argument with bad data. */
    @Test
    public void testMoveDownWithBadArgument() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ValidationException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.moveDown(music);
            fail("Can't move down music with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#moveDown(MusicTO)} with not existing argument. */
    @Test
    public void testMoveDownWithNotExistingArgument() {
        final MusicTO music = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(null);

        try {
            musicFacade.moveDown(music);
            fail("Can't move down music with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#moveDown(MusicTO)} with not moveable argument. */
    @Test
    public void testMoveDownWithNotMoveableArgument() {
        final Music music = generate(Music.class);
        final List<Music> musicList = CollectionUtils.newList(mock(Music.class), music);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.getMusic(anyInt())).thenReturn(music);
        when(musicService.getMusic()).thenReturn(musicList);

        try {
            musicFacade.moveDown(musicTO);
            fail("Can't move down music with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicService).getMusic(musicTO.getId());
        verify(musicService).getMusic();
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#moveDown(MusicTO)} with exception in service tier. */
    @Test
    public void testMoveDownWithServiceTierException() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

        try {
            musicFacade.moveDown(music);
            fail("Can't move down music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getMusic(music.getId());
        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#exists(MusicTO)} with existing music. */
    @Test
    public void testExistsWithExistingMusic() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.exists(any(Music.class))).thenReturn(true);
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        assertTrue(musicFacade.exists(musicTO));

        verify(musicService).exists(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#exists(MusicTO)} with not existing music. */
    @Test
    public void testExistsWithNotExistingMusic() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        when(musicService.exists(any(Music.class))).thenReturn(false);
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        assertFalse(musicFacade.exists(musicTO));

        verify(musicService).exists(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#exists(MusicTO)} with null argument. */
    @Test
    public void testExistsWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.exists(null);
            fail("Can't exists music with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(null);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#exists(MusicTO)} with argument with bad data. */
    @Test
    public void testExistsWithBadArgument() {
        final MusicTO music = generate(MusicTO.class);
        doThrow(ValidationException.class).when(musicTOValidator).validateMusicTOWithId(any(MusicTO.class));

        try {
            musicFacade.exists(music);
            fail("Can't exists music with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(musicTOValidator).validateMusicTOWithId(music);
        verifyNoMoreInteractions(musicTOValidator);
        verifyZeroInteractions(musicService, conversionService);
    }

    /** Test method for {@link MusicFacade#exists(MusicTO)} with exception in service tier. */
    @Test
    public void testExistsWithServiceTierException() {
        final Music music = generate(Music.class);
        final MusicTO musicTO = generate(MusicTO.class);
        doThrow(ServiceOperationException.class).when(musicService).exists(any(Music.class));
        when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

        try {
            musicFacade.exists(musicTO);
            fail("Can't exists music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).exists(music);
        verify(conversionService).convert(musicTO, Music.class);
        verify(musicTOValidator).validateMusicTOWithId(musicTO);
        verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
    }

    /** Test method for {@link MusicFacade#updatePositions()}. */
    @Test
    public void testUpdatePositions() {
        musicFacade.updatePositions();

        verify(musicService).updatePositions();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#updatePositions()} with exception in service tier. */
    @Test
    public void testUpdatePositionsWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(musicService).updatePositions();

        try {
            musicFacade.updatePositions();
            fail("Can't update positions with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).updatePositions();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#getTotalMediaCount()}. */
    @Test
    public void testGetTotalMediaCount() {
        final int count = generate(Integer.class);
        when(musicService.getTotalMediaCount()).thenReturn(count);

        DeepAsserts.assertEquals(count, musicFacade.getTotalMediaCount());

        verify(musicService).getTotalMediaCount();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#getTotalMediaCount()} with exception in service tier. */
    @Test
    public void testGetTotalMediaCountWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(musicService).getTotalMediaCount();

        try {
            musicFacade.getTotalMediaCount();
            fail("Can't get total media count with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getTotalMediaCount();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#getTotalLength()}. */
    @Test
    public void testGetTotalLength() {
        final Time length = generate(Time.class);
        when(musicService.getTotalLength()).thenReturn(length);

        DeepAsserts.assertEquals(length, musicFacade.getTotalLength());

        verify(musicService).getTotalLength();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#getTotalLength()} with exception in service tier. */
    @Test
    public void testGetTotalLengthWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(musicService).getTotalLength();

        try {
            musicFacade.getTotalLength();
            fail("Can't get total length of all songs with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getTotalLength();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#getSongsCount()}. */
    @Test
    public void testGetSongsCount() {
        final int count = generate(Integer.class);
        when(musicService.getSongsCount()).thenReturn(count);

        DeepAsserts.assertEquals(count, musicFacade.getSongsCount());

        verify(musicService).getSongsCount();
        verifyNoMoreInteractions(musicService);
    }

    /** Test method for {@link MusicFacade#getSongsCount()} with exception in service tier. */
    @Test
    public void testGetSongsCountWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(musicService).getSongsCount();

        try {
            musicFacade.getSongsCount();
            fail("Can't get count of songs from all music with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(musicService).getSongsCount();
        verifyNoMoreInteractions(musicService);
    }

    /**
     * Sets music's ID and position.
     *
     * @param id       ID
     * @param position position
     * @return mocked answer
     */
    private static Answer<Void> setMusicIdAndPosition(final Integer id, final int position) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                final Music music = (Music) invocation.getArguments()[0];
                music.setId(id);
                music.setPosition(position);
                return null;
            }

        };
    }

}
