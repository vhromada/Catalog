package cz.vhromada.catalog.facade.impl;

import static cz.vhromada.catalog.commons.TestConstants.ADD_ID;
import static cz.vhromada.catalog.commons.TestConstants.ADD_POSITION;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_ID;
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
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.validators.EpisodeTOValidator;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.service.EpisodeService;
import cz.vhromada.catalog.service.SeasonService;
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
 * A class represents test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class EpisodeFacadeImplTest {

	/** Instance of {@link SeasonService} */
	@Mock
	private SeasonService seasonService;

	/** Instance of {@link EpisodeService} */
	@Mock
	private EpisodeService episodeService;

	/** Instance of {@link ConversionService} */
	@Mock
	private ConversionService conversionService;

	/** Instance of {@link SeasonTOValidator} */
	@Mock
	private SeasonTOValidator seasonTOValidator;

	/** Instance of {@link EpisodeTOValidator} */
	@Mock
	private EpisodeTOValidator episodeTOValidator;

	/** Instance of (@link EpisodeFacade} */
	@InjectMocks
	private EpisodeFacade episodeFacade = new EpisodeFacadeImpl();

	/** Test method for {@link EpisodeFacadeImpl#getSeasonService()} and {@link EpisodeFacadeImpl#setSeasonService(SeasonService)}. */
	@Test
	public void testSeasonService() {
		final EpisodeFacadeImpl episodeFacade = new EpisodeFacadeImpl();
		episodeFacade.setSeasonService(seasonService);
		DeepAsserts.assertEquals(seasonService, episodeFacade.getSeasonService());
	}

	/** Test method for {@link EpisodeFacadeImpl#getEpisodeService()} and {@link EpisodeFacadeImpl#setEpisodeService(EpisodeService)}. */
	@Test
	public void testEpisodeService() {
		final EpisodeFacadeImpl episodeFacade = new EpisodeFacadeImpl();
		episodeFacade.setEpisodeService(episodeService);
		DeepAsserts.assertEquals(episodeService, episodeFacade.getEpisodeService());
	}

	/** Test method for {@link EpisodeFacadeImpl#getConversionService()} and {@link EpisodeFacadeImpl#setConversionService(ConversionService)}. */
	@Test
	public void testConversionService() {
		final EpisodeFacadeImpl episodeFacade = new EpisodeFacadeImpl();
		episodeFacade.setConversionService(conversionService);
		DeepAsserts.assertEquals(conversionService, episodeFacade.getConversionService());
	}

	/** Test method for {@link EpisodeFacadeImpl#getSeasonTOValidator()} and {@link EpisodeFacadeImpl#setSeasonTOValidator(SeasonTOValidator)}. */
	@Test
	public void testSeasonTOValidator() {
		final EpisodeFacadeImpl episodeFacade = new EpisodeFacadeImpl();
		episodeFacade.setSeasonTOValidator(seasonTOValidator);
		DeepAsserts.assertEquals(seasonTOValidator, episodeFacade.getSeasonTOValidator());
	}

	/** Test method for {@link EpisodeFacadeImpl#getEpisodeTOValidator()} and {@link EpisodeFacadeImpl#setEpisodeTOValidator(EpisodeTOValidator)}. */
	@Test
	public void testEpisodeTOValidator() {
		final EpisodeTOValidator episodeTOValidator = mock(EpisodeTOValidator.class);
		final EpisodeFacadeImpl episodeFacade = new EpisodeFacadeImpl();
		episodeFacade.setEpisodeTOValidator(episodeTOValidator);
		DeepAsserts.assertEquals(episodeTOValidator, episodeFacade.getEpisodeTOValidator());
	}

	/** Test method for {@link EpisodeFacade#getEpisode(Integer)} with existing episode. */
	@Test
	public void testGetEpisodeWithExistingEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID));
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID, ToGenerator.createSeason(INNER_ID));
		when(episodeService.getEpisode(anyInt())).thenReturn(episode);
		when(conversionService.convert(any(Episode.class), eq(EpisodeTO.class))).thenReturn(episodeTO);

		DeepAsserts.assertEquals(episodeTO, episodeFacade.getEpisode(PRIMARY_ID));

		verify(episodeService).getEpisode(PRIMARY_ID);
		verify(conversionService).convert(episode, EpisodeTO.class);
		verifyNoMoreInteractions(episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#getEpisode(Integer)} with not existing episode. */
	@Test
	public void testGetEpisodeWithNotExistingEpisode() {
		when(episodeService.getEpisode(anyInt())).thenReturn(null);
		when(conversionService.convert(any(Episode.class), eq(EpisodeTO.class))).thenReturn(null);

		assertNull(episodeFacade.getEpisode(Integer.MAX_VALUE));

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(conversionService).convert(null, EpisodeTO.class);
		verifyNoMoreInteractions(episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#getEpisode(Integer)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testGetEpisodeWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.getEpisode(Integer.MAX_VALUE);
	}

	/** Test method for {@link EpisodeFacade#getEpisode(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetEpisodeWithNotSetConversionService() {
		((EpisodeFacadeImpl) episodeFacade).setConversionService(null);
		episodeFacade.getEpisode(Integer.MAX_VALUE);
	}

	/** Test method for {@link EpisodeFacade#getEpisode(Integer)} with null argument. */
	@Test
	public void testGetEpisodeWithNullArgument() {
		try {
			episodeFacade.getEpisode(null);
			fail("Can't get episode with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#getEpisode(Integer)} with exception in service tier. */
	@Test
	public void testGetEpisodeWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

		try {
			episodeFacade.getEpisode(Integer.MAX_VALUE);
			fail("Can't get episode with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verifyNoMoreInteractions(episodeService);
		verifyZeroInteractions(conversionService);
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)}. */
	@Test
	public void testAdd() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode = EntityGenerator.createEpisode(season);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(ToGenerator.createSeason(INNER_ID));
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		doAnswer(setEpisodeIdAndPosition(ADD_ID, ADD_POSITION)).when(episodeService).add(any(Episode.class));
		when(conversionService.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

		episodeFacade.add(episodeTO);

		DeepAsserts.assertEquals(ADD_ID, episode.getId());
		DeepAsserts.assertEquals(ADD_POSITION, episode.getPosition());

		verify(seasonService).getSeason(INNER_ID);
		verify(episodeService).add(episode);
		verify(episodeTOValidator).validateNewEpisodeTO(episodeTO);
		verify(conversionService).convert(episodeTO, Episode.class);
		verifyNoMoreInteractions(seasonService, episodeService, conversionService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with not set service for season. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetSeasonService() {
		((EpisodeFacadeImpl) episodeFacade).setSeasonService(null);
		episodeFacade.add(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.add(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((EpisodeFacadeImpl) episodeFacade).setConversionService(null);
		episodeFacade.add(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with not set validator for TO for episode. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEpisodeTOValidator() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeTOValidator(null);
		episodeFacade.add(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateNewEpisodeTO(any(EpisodeTO.class));

		try {
			episodeFacade.add(null);
			fail("Can't add episode with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(episodeTOValidator).validateNewEpisodeTO(null);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with argument with bad data. */
	@Test
	public void testAddWithBadArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode();
		doThrow(ValidationException.class).when(episodeTOValidator).validateNewEpisodeTO(any(EpisodeTO.class));

		try {
			episodeFacade.add(episode);
			fail("Can't add episode with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeTOValidator).validateNewEpisodeTO(episode);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with not existing argument. */
	@Test
	public void testAddWithNotExistingArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		final EpisodeTO episode = ToGenerator.createEpisode(season);
		when(seasonService.getSeason(anyInt())).thenReturn(null);

		try {
			episodeFacade.add(episode);
			fail("Can't add episode with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateNewEpisodeTO(episode);
		verifyNoMoreInteractions(seasonService, episodeTOValidator);
		verifyZeroInteractions(episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with service tier not setting ID. */
	@Test
	public void testAddWithNotServiceTierSettingID() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode = EntityGenerator.createEpisode(season);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(ToGenerator.createSeason(INNER_ID));
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		when(conversionService.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

		try {
			episodeFacade.add(episodeTO);
			fail("Can't add episode with service tier not setting ID.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).getSeason(INNER_ID);
		verify(episodeService).add(episode);
		verify(conversionService).convert(episodeTO, Episode.class);
		verify(episodeTOValidator).validateNewEpisodeTO(episodeTO);
		verifyNoMoreInteractions(seasonService, episodeService, conversionService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#add(EpisodeTO)} with exception in service tier. */
	@Test
	public void testAddWithServiceTierException() {
		final EpisodeTO episode = ToGenerator.createEpisode(ToGenerator.createSeason(Integer.MAX_VALUE));
		doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

		try {
			episodeFacade.add(episode);
			fail("Can't add episode with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateNewEpisodeTO(episode);
		verifyNoMoreInteractions(seasonService, episodeTOValidator);
		verifyZeroInteractions(episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)}. */
	@Test
	public void testUpdate() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, season);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID, ToGenerator.createSeason(INNER_ID));
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		when(episodeService.exists(any(Episode.class))).thenReturn(true);
		when(conversionService.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

		episodeFacade.update(episodeTO);

		verify(seasonService).getSeason(INNER_ID);
		verify(episodeService).exists(episode);
		verify(episodeService).update(episode);
		verify(conversionService).convert(episodeTO, Episode.class);
		verify(episodeTOValidator).validateExistingEpisodeTO(episodeTO);
		verifyNoMoreInteractions(seasonService, episodeService, conversionService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)} with not set service for season. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetSeasonService() {
		((EpisodeFacadeImpl) episodeFacade).setSeasonService(null);
		episodeFacade.update(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.update(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((EpisodeFacadeImpl) episodeFacade).setConversionService(null);
		episodeFacade.update(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)} with not set validator for TO for episode. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEpisodeTOValidator() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeTOValidator(null);
		episodeFacade.update(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateExistingEpisodeTO(any(EpisodeTO.class));

		try {
			episodeFacade.update(null);
			fail("Can't update episode with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(episodeTOValidator).validateExistingEpisodeTO(null);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)} with argument with bad data. */
	@Test
	public void testUpdateWithBadArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(episodeTOValidator).validateExistingEpisodeTO(any(EpisodeTO.class));

		try {
			episodeFacade.update(episode);
			fail("Can't update episode with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeTOValidator).validateExistingEpisodeTO(episode);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)} with not existing argument. */
	@Test
	public void testUpdateWithNotExistingArgument() {
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(Integer.MAX_VALUE);
		when(episodeService.exists(any(Episode.class))).thenReturn(false);
		when(conversionService.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

		try {
			episodeFacade.update(episodeTO);
			fail("Can't update episode with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(episodeService).exists(episode);
		verify(conversionService).convert(episodeTO, Episode.class);
		verify(episodeTOValidator).validateExistingEpisodeTO(episodeTO);
		verifyNoMoreInteractions(episodeService, conversionService, episodeTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link EpisodeFacade#update(EpisodeTO)} with exception in service tier. */
	@Test
	public void testUpdateWithServiceTierException() {
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(episodeService).exists(any(Episode.class));
		when(conversionService.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

		try {
			episodeFacade.update(episodeTO);
			fail("Can't update episode with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(episodeService).exists(episode);
		verify(conversionService).convert(episodeTO, Episode.class);
		verify(episodeTOValidator).validateExistingEpisodeTO(episodeTO);
		verifyNoMoreInteractions(episodeService, conversionService, episodeTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link EpisodeFacade#remove(EpisodeTO)}. */
	@Test
	public void testRemove() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID);
		when(episodeService.getEpisode(anyInt())).thenReturn(episode);

		episodeFacade.remove(episodeTO);

		verify(episodeService).getEpisode(PRIMARY_ID);
		verify(episodeService).remove(episode);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#remove(EpisodeTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.remove(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#remove(EpisodeTO)} with not set validator for TO for episode. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEpisodeTOValidator() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeTOValidator(null);
		episodeFacade.remove(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#remove(EpisodeTO)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.remove(null);
			fail("Can't remove episode with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(null);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link EpisodeFacade#remove(EpisodeTO)} with argument with bad data. */
	@Test
	public void testRemoveWithBadArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.remove(episode);
			fail("Can't remove episode with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link EpisodeFacade#remove(EpisodeTO)} with not existing argument. */
	@Test
	public void testRemoveWithNotExistingArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		when(episodeService.getEpisode(anyInt())).thenReturn(null);

		try {
			episodeFacade.remove(episode);
			fail("Can't remove episode with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#remove(EpisodeTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

		try {
			episodeFacade.remove(episode);
			fail("Can't remove episode with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#duplicate(EpisodeTO)}. */
	@Test
	public void testDuplicate() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID);
		when(episodeService.getEpisode(anyInt())).thenReturn(episode);

		episodeFacade.duplicate(episodeTO);

		verify(episodeService).getEpisode(PRIMARY_ID);
		verify(episodeService).duplicate(episode);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.duplicate(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with not set validator for TO for episode. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetEpisodeTOValidator() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeTOValidator(null);
		episodeFacade.duplicate(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.duplicate(null);
			fail("Can't duplicate episode with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(null);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with argument with bad data. */
	@Test
	public void testDuplicateWithBadArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.duplicate(episode);
			fail("Can't duplicate episode with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with not existing argument. */
	@Test
	public void testDuplicateWithNotExistingArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		when(episodeService.getEpisode(anyInt())).thenReturn(null);

		try {
			episodeFacade.duplicate(episode);
			fail("Can't duplicate episode with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

		try {
			episodeFacade.duplicate(episode);
			fail("Can't duplicate episode with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#moveUp(EpisodeTO)}. */
	@Test
	public void testMoveUp() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, season);
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), episode);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID);
		when(episodeService.getEpisode(anyInt())).thenReturn(episode);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);

		episodeFacade.moveUp(episodeTO);

		verify(episodeService).getEpisode(PRIMARY_ID);
		verify(episodeService).findEpisodesBySeason(season);
		verify(episodeService).moveUp(episode);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.moveUp(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not set validator for TO for episode. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetEpisodeTOValidator() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeTOValidator(null);
		episodeFacade.moveUp(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.moveUp(null);
			fail("Can't move up episode with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(null);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with argument with bad data. */
	@Test
	public void testMoveUpWithBadArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.moveUp(episode);
			fail("Can't move up episode with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not existing argument. */
	@Test
	public void testMoveUpWithNotExistingArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		when(episodeService.getEpisode(anyInt())).thenReturn(null);

		try {
			episodeFacade.moveUp(episode);
			fail("Can't move up episode with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not moveable argument. */
	@Test
	public void testMoveUpWithNotMoveableArgument() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE, season);
		final List<Episode> episodes = CollectionUtils.newList(episode, mock(Episode.class));
		final EpisodeTO episodeTO = ToGenerator.createEpisode(Integer.MAX_VALUE);
		when(episodeService.getEpisode(anyInt())).thenReturn(episode);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);

		try {
			episodeFacade.moveUp(episodeTO);
			fail("Can't move up episode with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeService).findEpisodesBySeason(season);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with exception in service tier. */
	@Test
	public void testMoveUpWithServiceTierException() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

		try {
			episodeFacade.moveUp(episode);
			fail("Can't move up episode with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#moveDown(EpisodeTO)}. */
	@Test
	public void testMoveDown() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, season);
		final List<Episode> episodes = CollectionUtils.newList(episode, mock(Episode.class));
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID);
		when(episodeService.getEpisode(anyInt())).thenReturn(episode);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);

		episodeFacade.moveDown(episodeTO);

		verify(episodeService).getEpisode(PRIMARY_ID);
		verify(episodeService).findEpisodesBySeason(season);
		verify(episodeService).moveDown(episode);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.moveDown(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not set validator for TO for episode. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetEpisodeTOValidator() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeTOValidator(null);
		episodeFacade.moveDown(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.moveDown(null);
			fail("Can't move down episode with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(null);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with argument with bad data. */
	@Test
	public void testMoveDownWithBadArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.moveDown(episode);
			fail("Can't move down episode with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not existing argument. */
	@Test
	public void testMoveDownWithNotExistingArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		when(episodeService.getEpisode(anyInt())).thenReturn(null);

		try {
			episodeFacade.moveDown(episode);
			fail("Can't move down episode with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not moveable argument. */
	@Test
	public void testMoveDownWithNotMoveableArgument() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE, season);
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), episode);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(Integer.MAX_VALUE);
		when(episodeService.getEpisode(anyInt())).thenReturn(episode);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);

		try {
			episodeFacade.moveDown(episodeTO);
			fail("Can't move down episode with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeService).findEpisodesBySeason(season);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with exception in service tier. */
	@Test
	public void testMoveDownWithServiceTierException() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

		try {
			episodeFacade.moveDown(episode);
			fail("Can't move down episode with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(episodeService).getEpisode(Integer.MAX_VALUE);
		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#exists(EpisodeTO)} with existing episode. */
	@Test
	public void testExistsWithExistingEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID);
		when(episodeService.exists(any(Episode.class))).thenReturn(true);
		when(conversionService.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

		assertTrue(episodeFacade.exists(episodeTO));

		verify(episodeService).exists(episode);
		verify(conversionService).convert(episodeTO, Episode.class);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, conversionService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#exists(EpisodeTO)} with not existing episode. */
	@Test
	public void testExistsWithNotExistingEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID);
		when(episodeService.exists(any(Episode.class))).thenReturn(false);
		when(conversionService.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

		assertFalse(episodeFacade.exists(episodeTO));

		verify(episodeService).exists(episode);
		verify(conversionService).convert(episodeTO, Episode.class);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, conversionService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#exists(EpisodeTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.exists(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#exists(EpisodeTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((EpisodeFacadeImpl) episodeFacade).setConversionService(null);
		episodeFacade.exists(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#exists(EpisodeTO)} with not set validator for TO for episode. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetEpisodeTOValidator() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeTOValidator(null);
		episodeFacade.exists(mock(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeFacade#exists(EpisodeTO)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.exists(null);
			fail("Can't exists episode with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(null);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#exists(EpisodeTO)} with argument with bad data. */
	@Test
	public void testExistsWithBadArgument() {
		final EpisodeTO episode = ToGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

		try {
			episodeFacade.exists(episode);
			fail("Can't exists episode with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(episodeTOValidator).validateEpisodeTOWithId(episode);
		verifyNoMoreInteractions(episodeTOValidator);
		verifyZeroInteractions(episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#exists(EpisodeTO)} with exception in service tier. */
	@Test
	public void testExistsWithServiceTierException() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID);
		final EpisodeTO episodeTO = ToGenerator.createEpisode(PRIMARY_ID);
		doThrow(ServiceOperationException.class).when(episodeService).exists(any(Episode.class));
		when(conversionService.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

		try {
			episodeFacade.exists(episodeTO);
			fail("Can't exists episode with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(episodeService).exists(episode);
		verify(conversionService).convert(episodeTO, Episode.class);
		verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
		verifyNoMoreInteractions(episodeService, conversionService, episodeTOValidator);
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)}. */
	@Test
	public void testFindEpisodesBySeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final List<Episode> episodes = CollectionUtils.newList(EntityGenerator.createEpisode(INNER_ID, season),
				EntityGenerator.createEpisode(SECONDARY_INNER_ID, season));
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID);
		final List<EpisodeTO> episodesList = CollectionUtils.newList(ToGenerator.createEpisode(INNER_ID, seasonTO),
				ToGenerator.createEpisode(SECONDARY_INNER_ID, seasonTO));
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		for (int i = 0; i < episodes.size(); i++) {
			final Episode episode = episodes.get(i);
			when(conversionService.convert(episode, EpisodeTO.class)).thenReturn(episodesList.get(i));
		}

		DeepAsserts.assertEquals(episodesList, episodeFacade.findEpisodesBySeason(seasonTO));

		verify(seasonService).getSeason(PRIMARY_ID);
		verify(episodeService).findEpisodesBySeason(season);
		for (Episode episode : episodes) {
			verify(conversionService).convert(episode, EpisodeTO.class);
		}
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, episodeService, conversionService, seasonTOValidator);
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with not set service for season. */
	@Test(expected = IllegalStateException.class)
	public void testFindEpisodesBySeasonWithNotSetSeasonService() {
		((EpisodeFacadeImpl) episodeFacade).setSeasonService(null);
		episodeFacade.findEpisodesBySeason(mock(SeasonTO.class));
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testFindEpisodesBySeasonWithNotSetEpisodeService() {
		((EpisodeFacadeImpl) episodeFacade).setEpisodeService(null);
		episodeFacade.findEpisodesBySeason(mock(SeasonTO.class));
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testFindEpisodesBySeasonWithNotSetConversionService() {
		((EpisodeFacadeImpl) episodeFacade).setConversionService(null);
		episodeFacade.findEpisodesBySeason(mock(SeasonTO.class));
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with not set validator for TO for season. */
	@Test(expected = IllegalStateException.class)
	public void testFindEpisodesBySeasonWithNotSetSeasonTOValidator() {
		((EpisodeFacadeImpl) episodeFacade).setSeasonTOValidator(null);
		episodeFacade.findEpisodesBySeason(mock(SeasonTO.class));
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with null argument. */
	@Test
	public void testFindEpisodesBySeasonWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			episodeFacade.findEpisodesBySeason(null);
			fail("Can't find episodes by season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(null);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with argument with bad data. */
	@Test
	public void testFindEpisodesBySeasonWithBadArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			episodeFacade.findEpisodesBySeason(season);
			fail("Can't find episodes by season with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with not existing argument. */
	@Test
	public void testFindEpisodesBySeasonWithNotExistingArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		when(seasonService.getSeason(anyInt())).thenReturn(null);

		try {
			episodeFacade.findEpisodesBySeason(season);
			fail("Can't find episodes by season with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
		verifyZeroInteractions(episodeService, conversionService);
	}

	/** Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with exception in service tier. */
	@Test
	public void testFindEpisodesBySeasonWithServiceTierException() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

		try {
			episodeFacade.findEpisodesBySeason(season);
			fail("Can't find episodes by season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
		verifyZeroInteractions(episodeService, conversionService);
	}

	/**
	 * Sets episode's ID and position.
	 *
	 * @param id       ID
	 * @param position position
	 * @return mocked answer
	 */
	private Answer<Void> setEpisodeIdAndPosition(final Integer id, final int position) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Episode episode = (Episode) invocation.getArguments()[0];
				episode.setId(id);
				episode.setPosition(position);
				return null;
			}

		};
	}

}
