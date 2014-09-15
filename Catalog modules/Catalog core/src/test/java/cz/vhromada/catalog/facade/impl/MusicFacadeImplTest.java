package cz.vhromada.catalog.facade.impl;

import static cz.vhromada.catalog.common.TestConstants.ADD_ID;
import static cz.vhromada.catalog.common.TestConstants.ADD_POSITION;
import static cz.vhromada.catalog.common.TestConstants.COUNT;
import static cz.vhromada.catalog.common.TestConstants.INNER_COUNT;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_ID;
import static cz.vhromada.catalog.common.TestConstants.TOTAL_LENGTH;
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

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
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
 * A class represents test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicFacadeImplTest {

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

	/** Instance of {@link MusicFacade} */
	@InjectMocks
	private MusicFacade musicFacade = new MusicFacadeImpl();

	/** Test method for {@link MusicFacadeImpl#getMusicService()} and {@link MusicFacadeImpl#setMusicService(MusicService)}. */
	@Test
	public void testMusicService() {
		final MusicFacadeImpl musicFacade = new MusicFacadeImpl();
		musicFacade.setMusicService(musicService);
		DeepAsserts.assertEquals(musicService, musicFacade.getMusicService());
	}

	/** Test method for {@link MusicFacadeImpl#getSongService()} and {@link MusicFacadeImpl#setSongService(SongService)}. */
	@Test
	public void testSongService() {
		final MusicFacadeImpl musicFacade = new MusicFacadeImpl();
		musicFacade.setSongService(songService);
		DeepAsserts.assertEquals(songService, musicFacade.getSongService());
	}

	/** Test method for {@link MusicFacadeImpl#getConversionService()} and {@link MusicFacadeImpl#setConversionService(ConversionService)}. */
	@Test
	public void testConversionService() {
		final MusicFacadeImpl musicFacade = new MusicFacadeImpl();
		musicFacade.setConversionService(conversionService);
		DeepAsserts.assertEquals(conversionService, musicFacade.getConversionService());
	}

	/** Test method for {@link MusicFacadeImpl#getMusicTOValidator()} and {@link MusicFacadeImpl#setMusicTOValidator(MusicTOValidator)}. */
	@Test
	public void testMusicTOValidator() {
		final MusicFacadeImpl musicFacade = new MusicFacadeImpl();
		musicFacade.setMusicTOValidator(musicTOValidator);
		DeepAsserts.assertEquals(musicTOValidator, musicFacade.getMusicTOValidator());
	}

	/** Test method for {@link MusicFacade#newData()}. */
	@Test
	public void testNewData() {
		musicFacade.newData();

		verify(musicService).newData();
		verifyNoMoreInteractions(musicService);
	}

	/** Test method for {@link MusicFacade#newData()} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.newData();
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
		final List<Music> music = CollectionUtils.newList(EntityGenerator.createMusic(PRIMARY_ID), EntityGenerator.createMusic(SECONDARY_ID));
		final List<MusicTO> musicList = CollectionUtils.newList(ToGenerator.createMusic(PRIMARY_ID), ToGenerator.createMusic(SECONDARY_ID));
		final List<Song> songs = new ArrayList<>();
		for (int i = 0; i < INNER_COUNT; i++) {
			songs.add(EntityGenerator.createSong(i));
		}
		when(musicService.getMusic()).thenReturn(music);
		when(songService.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(songService.getTotalLengthByMusic(any(Music.class))).thenReturn(TOTAL_LENGTH);
		for (int i = 0; i < music.size(); i++) {
			final Music aMusic = music.get(i);
			when(conversionService.convert(aMusic, MusicTO.class)).thenReturn(musicList.get(i));
		}

		DeepAsserts.assertEquals(musicList, musicFacade.getMusic());

		verify(musicService).getMusic();
		for (Music aMusic : music) {
			verify(songService).findSongsByMusic(aMusic);
			verify(songService).getTotalLengthByMusic(aMusic);
			verify(conversionService).convert(aMusic, MusicTO.class);
		}
		verifyNoMoreInteractions(musicService, songService, conversionService);
	}

	/** Test method for {@link MusicFacade#getMusic()} with null music. */
	@Test
	public void testGetMusicWithNullMusic() {
		final List<Music> music = CollectionUtils.newList(EntityGenerator.createMusic(PRIMARY_ID), EntityGenerator.createMusic(SECONDARY_ID));
		final List<MusicTO> musicList = CollectionUtils.newList(null, null);
		when(musicService.getMusic()).thenReturn(music);
		when(conversionService.convert(any(Music.class), eq(MusicTO.class))).thenReturn(null);

		final List<MusicTO> result = musicFacade.getMusic();
		DeepAsserts.assertEquals(musicList, result);

		verify(musicService).getMusic();
		for (Music aMusic : music) {
			verify(conversionService).convert(aMusic, MusicTO.class);
		}
		verifyNoMoreInteractions(musicService, conversionService);
		verifyZeroInteractions(songService);
	}

	/** Test method for {@link MusicFacade#getMusic()} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.getMusic();
	}

	/** Test method for {@link MusicFacade#getMusic()} with not set service for songs. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicWithNotSetSongService() {
		((MusicFacadeImpl) musicFacade).setSongService(null);
		musicFacade.getMusic();
	}

	/** Test method for {@link MusicFacade#getMusic()} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicWithNotSetConversionService() {
		((MusicFacadeImpl) musicFacade).setConversionService(null);
		musicFacade.getMusic();
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
		verifyZeroInteractions(songService, conversionService);
	}

	/** Test method for {@link MusicFacade#getMusic(Integer)} with existing music. */
	@Test
	public void testGetMusicByIdWithExistingMusic() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
		final List<Song> songs = new ArrayList<>();
		for (int i = 0; i < INNER_COUNT; i++) {
			songs.add(EntityGenerator.createSong(i));
		}
		when(musicService.getMusic(anyInt())).thenReturn(music);
		when(songService.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(songService.getTotalLengthByMusic(any(Music.class))).thenReturn(TOTAL_LENGTH);
		when(conversionService.convert(any(Music.class), eq(MusicTO.class))).thenReturn(musicTO);

		DeepAsserts.assertEquals(musicTO, musicFacade.getMusic(PRIMARY_ID));

		verify(musicService).getMusic(PRIMARY_ID);
		verify(songService).findSongsByMusic(music);
		verify(songService).getTotalLengthByMusic(music);
		verify(conversionService).convert(music, MusicTO.class);
		verifyNoMoreInteractions(musicService, songService, conversionService);
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
		verifyZeroInteractions(songService);
	}

	/** Test method for {@link MusicFacade#getMusic(Integer)} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicByIdWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.getMusic(Integer.MAX_VALUE);
	}

	/** Test method for {@link MusicFacade#getMusic(Integer)} with not set service for songs. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicByIdWithNotSetSongService() {
		((MusicFacadeImpl) musicFacade).setSongService(null);
		musicFacade.getMusic(Integer.MAX_VALUE);
	}

	/** Test method for {@link MusicFacade#getMusic(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicByIdWithNotSetConversionService() {
		((MusicFacadeImpl) musicFacade).setConversionService(null);
		musicFacade.getMusic(Integer.MAX_VALUE);
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

		verifyZeroInteractions(musicService, songService, conversionService);
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
		verifyZeroInteractions(songService, conversionService);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)}. */
	@Test
	public void testAdd() {
		final Music music = EntityGenerator.createMusic();
		final MusicTO musicTO = ToGenerator.createMusic();
		doAnswer(setMusicIdAndPosition(ADD_ID, ADD_POSITION)).when(musicService).add(any(Music.class));
		when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

		musicFacade.add(musicTO);
		DeepAsserts.assertEquals(ADD_ID, musicTO.getId());
		DeepAsserts.assertEquals(ADD_POSITION, musicTO.getPosition());

		verify(musicService).add(music);
		verify(conversionService).convert(musicTO, Music.class);
		verify(musicTOValidator).validateNewMusicTO(musicTO);
		verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.add(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((MusicFacadeImpl) musicFacade).setConversionService(null);
		musicFacade.add(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#add(MusicTO)} with not set validator for TO for music. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetMusicTOValidator() {
		((MusicFacadeImpl) musicFacade).setMusicTOValidator(null);
		musicFacade.add(mock(MusicTO.class));
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
		final MusicTO music = ToGenerator.createMusic();
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
		final Music music = EntityGenerator.createMusic();
		final MusicTO musicTO = ToGenerator.createMusic();
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
		final Music music = EntityGenerator.createMusic();
		final MusicTO musicTO = ToGenerator.createMusic();
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
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
		when(musicService.exists(any(Music.class))).thenReturn(true);
		when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

		musicFacade.update(musicTO);

		verify(musicService).exists(music);
		verify(musicService).update(music);
		verify(conversionService).convert(musicTO, Music.class);
		verify(musicTOValidator).validateExistingMusicTO(musicTO);
		verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.update(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((MusicFacadeImpl) musicFacade).setConversionService(null);
		musicFacade.update(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#update(MusicTO)} with not set validator for TO for music. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetMusicTOValidator() {
		((MusicFacadeImpl) musicFacade).setMusicTOValidator(null);
		musicFacade.update(mock(MusicTO.class));
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
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
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		final MusicTO musicTO = ToGenerator.createMusic(Integer.MAX_VALUE);
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
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		final MusicTO musicTO = ToGenerator.createMusic(Integer.MAX_VALUE);
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
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
		when(musicService.getMusic(anyInt())).thenReturn(music);

		musicFacade.remove(musicTO);

		verify(musicService).getMusic(PRIMARY_ID);
		verify(musicService).remove(music);
		verify(musicTOValidator).validateMusicTOWithId(musicTO);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.remove(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)} with not set validator for TO for music. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetMusicTOValidator() {
		((MusicFacadeImpl) musicFacade).setMusicTOValidator(null);
		musicFacade.remove(mock(MusicTO.class));
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
		when(musicService.getMusic(anyInt())).thenReturn(null);

		try {
			musicFacade.remove(music);
			fail("Can't remove music with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicTOValidator).validateMusicTOWithId(music);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#remove(MusicTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

		try {
			musicFacade.remove(music);
			fail("Can't remove music with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicTOValidator).validateMusicTOWithId(music);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)}. */
	@Test
	public void testDuplicate() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
		when(musicService.getMusic(anyInt())).thenReturn(music);

		musicFacade.duplicate(musicTO);

		verify(musicService).getMusic(PRIMARY_ID);
		verify(musicService).duplicate(music);
		verify(musicTOValidator).validateMusicTOWithId(musicTO);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.duplicate(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)} with not set validator for TO for music. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetMusicTOValidator() {
		((MusicFacadeImpl) musicFacade).setMusicTOValidator(null);
		musicFacade.duplicate(mock(MusicTO.class));
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
		when(musicService.getMusic(anyInt())).thenReturn(null);

		try {
			musicFacade.duplicate(music);
			fail("Can't duplicate music with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicTOValidator).validateMusicTOWithId(music);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#duplicate(MusicTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

		try {
			musicFacade.duplicate(music);
			fail("Can't duplicate music with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicTOValidator).validateMusicTOWithId(music);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)}. */
	@Test
	public void testMoveUp() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final List<Music> musicList = CollectionUtils.newList(mock(Music.class), music);
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
		when(musicService.getMusic(anyInt())).thenReturn(music);
		when(musicService.getMusic()).thenReturn(musicList);

		musicFacade.moveUp(musicTO);

		verify(musicService).getMusic(PRIMARY_ID);
		verify(musicService).getMusic();
		verify(musicService).moveUp(music);
		verify(musicTOValidator).validateMusicTOWithId(musicTO);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.moveUp(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with not set validator for TO for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetMusicTOValidator() {
		((MusicFacadeImpl) musicFacade).setMusicTOValidator(null);
		musicFacade.moveUp(mock(MusicTO.class));
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
		when(musicService.getMusic(anyInt())).thenReturn(null);

		try {
			musicFacade.moveUp(music);
			fail("Can't move up music with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicTOValidator).validateMusicTOWithId(music);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with not moveable argument. */
	@Test
	public void testMoveUpWithNotMoveableArgument() {
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		final List<Music> musicList = CollectionUtils.newList(music, mock(Music.class));
		final MusicTO musicTO = ToGenerator.createMusic(Integer.MAX_VALUE);
		when(musicService.getMusic(anyInt())).thenReturn(music);
		when(musicService.getMusic()).thenReturn(musicList);

		try {
			musicFacade.moveUp(musicTO);
			fail("Can't move up music with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicService).getMusic();
		verify(musicTOValidator).validateMusicTOWithId(musicTO);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#moveUp(MusicTO)} with exception in service tier. */
	@Test
	public void testMoveUpWithServiceTierException() {
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

		try {
			musicFacade.moveUp(music);
			fail("Can't move up music with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicTOValidator).validateMusicTOWithId(music);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)}. */
	@Test
	public void testMoveDown() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final List<Music> musicList = CollectionUtils.newList(music, mock(Music.class));
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
		when(musicService.getMusic(anyInt())).thenReturn(music);
		when(musicService.getMusic()).thenReturn(musicList);

		musicFacade.moveDown(musicTO);

		verify(musicService).getMusic(PRIMARY_ID);
		verify(musicService).getMusic();
		verify(musicService).moveDown(music);
		verify(musicTOValidator).validateMusicTOWithId(musicTO);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.moveDown(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with not set validator for TO for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetMusicTOValidator() {
		((MusicFacadeImpl) musicFacade).setMusicTOValidator(null);
		musicFacade.moveDown(mock(MusicTO.class));
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
		when(musicService.getMusic(anyInt())).thenReturn(null);

		try {
			musicFacade.moveDown(music);
			fail("Can't move down music with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicTOValidator).validateMusicTOWithId(music);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with not moveable argument. */
	@Test
	public void testMoveDownWithNotMoveableArgument() {
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		final List<Music> musicList = CollectionUtils.newList(mock(Music.class), music);
		final MusicTO musicTO = ToGenerator.createMusic(Integer.MAX_VALUE);
		when(musicService.getMusic(anyInt())).thenReturn(music);
		when(musicService.getMusic()).thenReturn(musicList);

		try {
			musicFacade.moveDown(musicTO);
			fail("Can't move down music with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicService).getMusic();
		verify(musicTOValidator).validateMusicTOWithId(musicTO);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#moveDown(MusicTO)} with exception in service tier. */
	@Test
	public void testMoveDownWithServiceTierException() {
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(musicService).getMusic(anyInt());

		try {
			musicFacade.moveDown(music);
			fail("Can't move down music with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(musicService).getMusic(Integer.MAX_VALUE);
		verify(musicTOValidator).validateMusicTOWithId(music);
		verifyNoMoreInteractions(musicService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#exists(MusicTO)} with existing music. */
	@Test
	public void testExistsWithExistingMusic() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
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
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
		when(musicService.exists(any(Music.class))).thenReturn(false);
		when(conversionService.convert(any(MusicTO.class), eq(Music.class))).thenReturn(music);

		assertFalse(musicFacade.exists(musicTO));

		verify(musicService).exists(music);
		verify(conversionService).convert(musicTO, Music.class);
		verify(musicTOValidator).validateMusicTOWithId(musicTO);
		verifyNoMoreInteractions(musicService, conversionService, musicTOValidator);
	}

	/** Test method for {@link MusicFacade#exists(MusicTO)} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.exists(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#exists(MusicTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((MusicFacadeImpl) musicFacade).setConversionService(null);
		musicFacade.exists(mock(MusicTO.class));
	}

	/** Test method for {@link MusicFacade#exists(MusicTO)} with not set validator for TO for music. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetMusicTOValidator() {
		((MusicFacadeImpl) musicFacade).setMusicTOValidator(null);
		musicFacade.exists(mock(MusicTO.class));
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
		final MusicTO music = ToGenerator.createMusic(Integer.MAX_VALUE);
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
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final MusicTO musicTO = ToGenerator.createMusic(PRIMARY_ID);
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

	/** Test method for {@link MusicFacade#updatePositions()} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.updatePositions();
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
		when(musicService.getTotalMediaCount()).thenReturn(COUNT);

		DeepAsserts.assertEquals(COUNT, musicFacade.getTotalMediaCount());

		verify(musicService).getTotalMediaCount();
		verifyNoMoreInteractions(musicService);
	}

	/** Test method for {@link MusicFacade#getTotalMediaCount()} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.getTotalMediaCount();
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
		when(musicService.getTotalLength()).thenReturn(TOTAL_LENGTH);

		DeepAsserts.assertEquals(TOTAL_LENGTH, musicFacade.getTotalLength());

		verify(musicService).getTotalLength();
		verifyNoMoreInteractions(musicService);
	}

	/** Test method for {@link MusicFacade#getTotalLength()} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.getTotalLength();
	}

	/** Test method for {@link MusicFacade#getTotalLength()} with exception in service tier. */
	@Test
	public void testGetTotalLengthWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(musicService).getTotalLength();

		try {
			musicFacade.getTotalLength();
			fail("Can't get total media count with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(musicService).getTotalLength();
		verifyNoMoreInteractions(musicService);
	}

	/** Test method for {@link MusicFacade#getSongsCount()}. */
	@Test
	public void testGetSongsCount() {
		when(musicService.getSongsCount()).thenReturn(COUNT);

		DeepAsserts.assertEquals(COUNT, musicFacade.getSongsCount());

		verify(musicService).getSongsCount();
		verifyNoMoreInteractions(musicService);
	}

	/** Test method for {@link MusicFacade#getSongsCount()} with not set service for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetSongsCountWithNotSetMusicService() {
		((MusicFacadeImpl) musicFacade).setMusicService(null);
		musicFacade.getSongsCount();
	}

	/** Test method for {@link MusicFacade#getSongsCount()} with exception in service tier. */
	@Test
	public void testGetSongsCountWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(musicService).getSongsCount();

		try {
			musicFacade.getSongsCount();
			fail("Can't get total media count with not thrown FacadeOperationException for service tier exception.");
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
	private Answer<Void> setMusicIdAndPosition(final Integer id, final int position) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Music music = (Music) invocation.getArguments()[0];
				music.setId(id);
				music.setPosition(position);
				return null;
			}

		};
	}

}
