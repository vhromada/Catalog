package cz.vhromada.catalog.facade.impl;

import static cz.vhromada.catalog.commons.TestConstants.ADD_ID;
import static cz.vhromada.catalog.commons.TestConstants.ADD_POSITION;
import static cz.vhromada.catalog.commons.TestConstants.INNER_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.TOTAL_LENGTH;
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

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.catalog.service.EpisodeService;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.SerieService;
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
 * A class represents test for class {@link SeasonFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SeasonFacadeImplTest {

	/** Instance of {@link SerieService} */
	@Mock
	private SerieService serieService;

	/** Instance of {@link SeasonService} */
	@Mock
	private SeasonService seasonService;

	/** Instance of {@link EpisodeService} */
	@Mock
	private EpisodeService episodeService;

	/** Instance of {@link ConversionService} */
	@Mock
	private ConversionService conversionService;

	/** Instance of {@link SerieTOValidator} */
	@Mock
	private SerieTOValidator serieTOValidator;

	/** Instance of {@link SeasonTOValidator} */
	@Mock
	private SeasonTOValidator seasonTOValidator;

	/** Instance of {@link SeasonFacade} */
	@InjectMocks
	private SeasonFacade seasonFacade = new SeasonFacadeImpl();

	/** Test method for {@link SeasonFacadeImpl#getSerieService()} and {@link SeasonFacadeImpl#setSerieService(SerieService)}. */
	@Test
	public void testSerieService() {
		final SeasonFacadeImpl seasonFacade = new SeasonFacadeImpl();
		seasonFacade.setSerieService(serieService);
		DeepAsserts.assertEquals(serieService, seasonFacade.getSerieService());
	}

	/** Test method for {@link SeasonFacadeImpl#getSeasonService()} and {@link SeasonFacadeImpl#setSeasonService(SeasonService)}. */
	@Test
	public void testSeasonService() {
		final SeasonFacadeImpl seasonFacade = new SeasonFacadeImpl();
		seasonFacade.setSeasonService(seasonService);
		DeepAsserts.assertEquals(seasonService, seasonFacade.getSeasonService());
	}

	/** Test method for {@link SeasonFacadeImpl#getEpisodeService()} and {@link SeasonFacadeImpl#setEpisodeService(EpisodeService)}. */
	@Test
	public void testEpisodeService() {
		final SeasonFacadeImpl seasonFacade = new SeasonFacadeImpl();
		seasonFacade.setEpisodeService(episodeService);
		DeepAsserts.assertEquals(episodeService, seasonFacade.getEpisodeService());
	}

	/** Test method for {@link SeasonFacadeImpl#getConversionService()} and {@link SeasonFacadeImpl#setConversionService(ConversionService)}. */
	@Test
	public void testConversionService() {
		final SeasonFacadeImpl seasonFacade = new SeasonFacadeImpl();
		seasonFacade.setConversionService(conversionService);
		DeepAsserts.assertEquals(conversionService, seasonFacade.getConversionService());
	}

	/** Test method for {@link SeasonFacadeImpl#getSerieTOValidator()} and {@link SeasonFacadeImpl#setSerieTOValidator(SerieTOValidator)}. */
	@Test
	public void testSerieTOValidator() {
		final SeasonFacadeImpl seasonFacade = new SeasonFacadeImpl();
		seasonFacade.setSerieTOValidator(serieTOValidator);
		DeepAsserts.assertEquals(serieTOValidator, seasonFacade.getSerieTOValidator());
	}

	/** Test method for {@link SeasonFacadeImpl#getSeasonTOValidator()} and {@link SeasonFacadeImpl#setSeasonTOValidator(SeasonTOValidator)}. */
	@Test
	public void testSeasonTOValidator() {
		final SeasonFacadeImpl seasonFacade = new SeasonFacadeImpl();
		seasonFacade.setSeasonTOValidator(seasonTOValidator);
		DeepAsserts.assertEquals(seasonTOValidator, seasonFacade.getSeasonTOValidator());
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)} with existing season. */
	@Test
	public void testGetSeasonWithExistingSeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_INNER_ID));
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID, ToGenerator.createSerie(INNER_INNER_ID));
		final List<Episode> episodes = new ArrayList<>();
		for (int i = 0; i < INNER_COUNT; i++) {
			episodes.add(EntityGenerator.createEpisode(i));
		}
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(episodeService.getTotalLengthBySeason(any(Season.class))).thenReturn(TOTAL_LENGTH);
		when(conversionService.convert(any(Season.class), eq(SeasonTO.class))).thenReturn(seasonTO);

		DeepAsserts.assertEquals(seasonTO, seasonFacade.getSeason(PRIMARY_ID));

		verify(seasonService).getSeason(PRIMARY_ID);
		verify(episodeService).findEpisodesBySeason(season);
		verify(episodeService).getTotalLengthBySeason(season);
		verify(conversionService).convert(season, SeasonTO.class);
		verifyNoMoreInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)} with not existing season. */
	@Test
	public void testGetSeasonWithNotExistingSeason() {
		when(seasonService.getSeason(anyInt())).thenReturn(null);
		when(conversionService.convert(any(Season.class), eq(SeasonTO.class))).thenReturn(null);

		assertNull(seasonFacade.getSeason(Integer.MAX_VALUE));

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(conversionService).convert(null, SeasonTO.class);
		verifyNoMoreInteractions(seasonService, conversionService);
		verifyZeroInteractions(episodeService);
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeasonWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.getSeason(Integer.MAX_VALUE);
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeasonWithNotSetEpisodeService() {
		((SeasonFacadeImpl) seasonFacade).setEpisodeService(null);
		seasonFacade.getSeason(Integer.MAX_VALUE);
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeasonWithNotSetConversionService() {
		((SeasonFacadeImpl) seasonFacade).setConversionService(null);
		seasonFacade.getSeason(Integer.MAX_VALUE);
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)} with null argument. */
	@Test
	public void testGetSeasonWithNullArgument() {
		try {
			seasonFacade.getSeason(null);
			fail("Can't get season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SeasonFacade#getSeason(Integer)} with exception in service tier. */
	@Test
	public void testGetSeasonWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

		try {
			seasonFacade.getSeason(Integer.MAX_VALUE);
			fail("Can't get season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verifyNoMoreInteractions(seasonService);
		verifyZeroInteractions(episodeService, conversionService);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)}. */
	@Test
	public void testAdd() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season = EntityGenerator.createSeason(EntityGenerator.createSerie(INNER_ID));
		final SeasonTO seasonTO = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		doAnswer(setSeasonIdAndPosition(ADD_ID, ADD_POSITION)).when(seasonService).add(any(Season.class));
		when(conversionService.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

		seasonFacade.add(seasonTO);

		DeepAsserts.assertEquals(ADD_ID, season.getId());
		DeepAsserts.assertEquals(ADD_POSITION, season.getPosition());

		verify(serieService).getSerie(INNER_ID);
		verify(seasonService).add(season);
		verify(conversionService).convert(seasonTO, Season.class);
		verify(seasonTOValidator).validateNewSeasonTO(seasonTO);
		verifyNoMoreInteractions(serieService, seasonService, conversionService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetSerieService() {
		((SeasonFacadeImpl) seasonFacade).setSerieService(null);
		seasonFacade.add(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.add(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((SeasonFacadeImpl) seasonFacade).setConversionService(null);
		seasonFacade.add(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with not set validator for TO for season. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetSeasonTOValidator() {
		((SeasonFacadeImpl) seasonFacade).setSeasonTOValidator(null);
		seasonFacade.add(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateNewSeasonTO(any(SeasonTO.class));

		try {
			seasonFacade.add(null);
			fail("Can't add season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(seasonTOValidator).validateNewSeasonTO(null);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(serieService, seasonService, conversionService);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with argument with bad data. */
	@Test
	public void testAddWithBadArgument() {
		final SeasonTO season = ToGenerator.createSeason();
		doThrow(ValidationException.class).when(seasonTOValidator).validateNewSeasonTO(any(SeasonTO.class));

		try {
			seasonFacade.add(season);
			fail("Can't add season with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonTOValidator).validateNewSeasonTO(season);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(serieService, seasonService, conversionService);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with not existing argument. */
	@Test
	public void testAddWithNotExistingArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		final SeasonTO season = ToGenerator.createSeason(serie);
		when(serieService.getSerie(anyInt())).thenReturn(null);

		try {
			seasonFacade.add(season);
			fail("Can't add season with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateNewSeasonTO(season);
		verifyNoMoreInteractions(serieService, seasonTOValidator);
		verifyZeroInteractions(seasonService, conversionService);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with service tier not setting ID. */
	@Test
	public void testAddWithNotServiceTierSettingID() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season = EntityGenerator.createSeason(serie);
		final SeasonTO seasonTO = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		when(conversionService.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

		try {
			seasonFacade.add(seasonTO);
			fail("Can't add season with service tier not setting ID.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSerie(INNER_ID);
		verify(seasonService).add(season);
		verify(conversionService).convert(seasonTO, Season.class);
		verify(seasonTOValidator).validateNewSeasonTO(seasonTO);
		verifyNoMoreInteractions(serieService, seasonService, conversionService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#add(SeasonTO)} with exception in service tier. */
	@Test
	public void testAddWithServiceTierException() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(Integer.MAX_VALUE));
		doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

		try {
			seasonFacade.add(season);
			fail("Can't add season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateNewSeasonTO(season);
		verifyNoMoreInteractions(serieService, seasonTOValidator);
		verifyZeroInteractions(seasonService, conversionService);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)}. */
	@Test
	public void testUpdate() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, serie);
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID, ToGenerator.createSerie(INNER_ID));
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		when(seasonService.exists(any(Season.class))).thenReturn(true);
		when(conversionService.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

		seasonFacade.update(seasonTO);

		verify(serieService).getSerie(INNER_ID);
		verify(seasonService).exists(season);
		verify(seasonService).update(season);
		verify(conversionService).convert(seasonTO, Season.class);
		verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
		verifyNoMoreInteractions(serieService, seasonService, conversionService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetSerieService() {
		((SeasonFacadeImpl) seasonFacade).setSerieService(null);
		seasonFacade.update(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.update(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((SeasonFacadeImpl) seasonFacade).setConversionService(null);
		seasonFacade.update(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with not set validator for TO for season. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetSeasonTOValidator() {
		((SeasonFacadeImpl) seasonFacade).setSeasonTOValidator(null);
		seasonFacade.update(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateExistingSeasonTO(any(SeasonTO.class));

		try {
			seasonFacade.update(null);
			fail("Can't update season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(seasonTOValidator).validateExistingSeasonTO(null);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(serieService, seasonService, conversionService);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with argument with bad data. */
	@Test
	public void testUpdateWithBadArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(seasonTOValidator).validateExistingSeasonTO(any(SeasonTO.class));

		try {
			seasonFacade.update(season);
			fail("Can't update season with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonTOValidator).validateExistingSeasonTO(season);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(serieService, seasonService, conversionService);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with not existing argument. */
	@Test
	public void testUpdateWithNotExistingArgument() {
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE);
		final SeasonTO seasonTO = ToGenerator.createSeason(Integer.MAX_VALUE);
		when(seasonService.exists(any(Season.class))).thenReturn(false);
		when(conversionService.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

		try {
			seasonFacade.update(seasonTO);
			fail("Can't update season with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(seasonService).exists(season);
		verify(conversionService).convert(seasonTO, Season.class);
		verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
		verifyNoMoreInteractions(seasonService, conversionService, seasonTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SeasonFacade#update(SeasonTO)} with exception in service tier. */
	@Test
	public void testUpdateWithServiceTierException() {
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE);
		final SeasonTO seasonTO = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(seasonService).exists(any(Season.class));
		when(conversionService.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

		try {
			seasonFacade.update(seasonTO);
			fail("Can't update season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).exists(season);
		verify(conversionService).convert(seasonTO, Season.class);
		verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
		verifyNoMoreInteractions(seasonService, conversionService, seasonTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)}. */
	@Test
	public void testRemove() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID);
		when(seasonService.getSeason(anyInt())).thenReturn(season);

		seasonFacade.remove(seasonTO);

		verify(seasonService).getSeason(PRIMARY_ID);
		verify(seasonService).remove(season);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.remove(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with not set validator for TO for season. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetSeasonTOValidator() {
		((SeasonFacadeImpl) seasonFacade).setSeasonTOValidator(null);
		seasonFacade.remove(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.remove(null);
			fail("Can't remove season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(null);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with argument with bad data. */
	@Test
	public void testRemoveWithBadArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.remove(season);
			fail("Can't remove season with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with not existing argument. */
	@Test
	public void testRemoveWithNotExistingArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		when(seasonService.getSeason(anyInt())).thenReturn(null);

		try {
			seasonFacade.remove(season);
			fail("Can't remove season with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#remove(SeasonTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

		try {
			seasonFacade.remove(season);
			fail("Can't remove season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)}. */
	@Test
	public void testDuplicate() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID);
		when(seasonService.getSeason(anyInt())).thenReturn(season);

		seasonFacade.duplicate(seasonTO);

		verify(seasonService).getSeason(PRIMARY_ID);
		verify(seasonService).duplicate(season);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.duplicate(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with not set validator for TO for season. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetSeasonTOValidator() {
		((SeasonFacadeImpl) seasonFacade).setSeasonTOValidator(null);
		seasonFacade.duplicate(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.duplicate(null);
			fail("Can't duplicate season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(null);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with argument with bad data. */
	@Test
	public void testDuplicateWithBadArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.duplicate(season);
			fail("Can't duplicate season with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with not existing argument. */
	@Test
	public void testDuplicateWithNotExistingArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		when(seasonService.getSeason(anyInt())).thenReturn(null);

		try {
			seasonFacade.duplicate(season);
			fail("Can't duplicate season with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

		try {
			seasonFacade.duplicate(season);
			fail("Can't duplicate season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)}. */
	@Test
	public void testMoveUp() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, serie);
		final List<Season> seasons = CollectionUtils.newList(mock(Season.class), season);
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID);
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);

		seasonFacade.moveUp(seasonTO);

		verify(seasonService).getSeason(PRIMARY_ID);
		verify(seasonService).findSeasonsBySerie(serie);
		verify(seasonService).moveUp(season);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.moveUp(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not set validator for TO for season. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSeasonTOValidator() {
		((SeasonFacadeImpl) seasonFacade).setSeasonTOValidator(null);
		seasonFacade.moveUp(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.moveUp(null);
			fail("Can't move up season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(null);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with argument with bad data. */
	@Test
	public void testMoveUpWithBadArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.moveUp(season);
			fail("Can't move up season with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not existing argument. */
	@Test
	public void testMoveUpWithNotExistingArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		when(seasonService.getSeason(anyInt())).thenReturn(null);

		try {
			seasonFacade.moveUp(season);
			fail("Can't move up season with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not moveable argument. */
	@Test
	public void testMoveUpWithNotMoveableArgument() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE, serie);
		final List<Season> seasons = CollectionUtils.newList(season, mock(Season.class));
		final SeasonTO seasonTO = ToGenerator.createSeason(Integer.MAX_VALUE);
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);

		try {
			seasonFacade.moveUp(seasonTO);
			fail("Can't move up season with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonService).findSeasonsBySerie(serie);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with exception in service tier. */
	@Test
	public void testMoveUpWithServiceTierException() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

		try {
			seasonFacade.moveUp(season);
			fail("Can't move up season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)}. */
	@Test
	public void testMoveDown() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, serie);
		final List<Season> seasons = CollectionUtils.newList(season, mock(Season.class));
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID);
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);

		seasonFacade.moveDown(seasonTO);

		verify(seasonService).getSeason(PRIMARY_ID);
		verify(seasonService).findSeasonsBySerie(serie);
		verify(seasonService).moveDown(season);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.moveDown(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not set validator for TO for season. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSeasonTOValidator() {
		((SeasonFacadeImpl) seasonFacade).setSeasonTOValidator(null);
		seasonFacade.moveDown(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.moveDown(null);
			fail("Can't move down season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(null);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with argument with bad data. */
	@Test
	public void testMoveDownWithBadArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.moveDown(season);
			fail("Can't move down season with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService);
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not existing argument. */
	@Test
	public void testMoveDownWithNotExistingArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		when(seasonService.getSeason(anyInt())).thenReturn(null);

		try {
			seasonFacade.moveDown(season);
			fail("Can't move down season with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not moveable argument. */
	@Test
	public void testMoveDownWithNotMoveableArgument() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE, serie);
		final List<Season> seasons = CollectionUtils.newList(mock(Season.class), season);
		final SeasonTO seasonTO = ToGenerator.createSeason(Integer.MAX_VALUE);
		when(seasonService.getSeason(anyInt())).thenReturn(season);
		when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);

		try {
			seasonFacade.moveDown(seasonTO);
			fail("Can't move down season with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonService).findSeasonsBySerie(serie);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with exception in service tier. */
	@Test
	public void testMoveDownWithServiceTierException() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

		try {
			seasonFacade.moveDown(season);
			fail("Can't move down season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).getSeason(Integer.MAX_VALUE);
		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with existing season. */
	@Test
	public void testExistsWithExistingSeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID);
		when(seasonService.exists(any(Season.class))).thenReturn(true);
		when(conversionService.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

		assertTrue(seasonFacade.exists(seasonTO));

		verify(seasonService).exists(season);
		verify(conversionService).convert(seasonTO, Season.class);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, conversionService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with not existing season. */
	@Test
	public void testExistsWithNotExistingSeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID);
		when(seasonService.exists(any(Season.class))).thenReturn(false);
		when(conversionService.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

		assertFalse(seasonFacade.exists(seasonTO));

		verify(seasonService).exists(season);
		verify(conversionService).convert(seasonTO, Season.class);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, conversionService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.exists(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((SeasonFacadeImpl) seasonFacade).setConversionService(null);
		seasonFacade.exists(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with not set validator for TO for season. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetSeasonTOValidator() {
		((SeasonFacadeImpl) seasonFacade).setSeasonTOValidator(null);
		seasonFacade.exists(mock(SeasonTO.class));
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.exists(null);
			fail("Can't exists season with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(null);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService, conversionService);
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with argument with bad data. */
	@Test
	public void testExistsWithBadArgument() {
		final SeasonTO season = ToGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

		try {
			seasonFacade.exists(season);
			fail("Can't exists season with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(seasonTOValidator).validateSeasonTOWithId(season);
		verifyNoMoreInteractions(seasonTOValidator);
		verifyZeroInteractions(seasonService, conversionService);
	}

	/** Test method for {@link SeasonFacade#exists(SeasonTO)} with exception in service tier. */
	@Test
	public void testExistsWithServiceTierException() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final SeasonTO seasonTO = ToGenerator.createSeason(PRIMARY_ID);
		doThrow(ServiceOperationException.class).when(seasonService).exists(any(Season.class));
		when(conversionService.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

		try {
			seasonFacade.exists(seasonTO);
			fail("Can't exists season with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(seasonService).exists(season);
		verify(conversionService).convert(seasonTO, Season.class);
		verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
		verifyNoMoreInteractions(seasonService, conversionService, seasonTOValidator);
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)}. */
	@Test
	public void testFindSeasonsBySerie() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final List<Season> seasons = CollectionUtils.newList(EntityGenerator.createSeason(INNER_ID, serie),
				EntityGenerator.createSeason(SECONDARY_INNER_ID, serie));
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		final List<SeasonTO> seasonsList = CollectionUtils.newList(ToGenerator.createSeason(INNER_ID, serieTO),
				ToGenerator.createSeason(SECONDARY_INNER_ID, serieTO));
		final List<Episode> episodes = new ArrayList<>();
		for (int i = 0; i < INNER_COUNT; i++) {
			episodes.add(EntityGenerator.createEpisode(i));
		}
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(episodeService.getTotalLengthBySeason(any(Season.class))).thenReturn(TOTAL_LENGTH);
		for (int i = 0; i < seasons.size(); i++) {
			final Season season = seasons.get(i);
			when(conversionService.convert(season, SeasonTO.class)).thenReturn(seasonsList.get(i));
		}

		DeepAsserts.assertEquals(seasonsList, seasonFacade.findSeasonsBySerie(serieTO));

		verify(serieService).getSerie(PRIMARY_ID);
		verify(seasonService).findSeasonsBySerie(serie);
		for (Season season : seasons) {
			verify(episodeService).findEpisodesBySeason(season);
			verify(episodeService).getTotalLengthBySeason(season);
			verify(conversionService).convert(season, SeasonTO.class);
		}
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, seasonService, episodeService, conversionService, serieTOValidator);
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testFindSeasonsBySerieWithNotSetSerieService() {
		((SeasonFacadeImpl) seasonFacade).setSerieService(null);
		seasonFacade.findSeasonsBySerie(mock(SerieTO.class));
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testFindSeasonsBySerieWithNotSetSeasonService() {
		((SeasonFacadeImpl) seasonFacade).setSeasonService(null);
		seasonFacade.findSeasonsBySerie(mock(SerieTO.class));
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testFindSeasonsBySerieWithNotSetEpisodeService() {
		((SeasonFacadeImpl) seasonFacade).setEpisodeService(null);
		seasonFacade.findSeasonsBySerie(mock(SerieTO.class));
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testFindSeasonsBySerieWithNotSetConversionService() {
		((SeasonFacadeImpl) seasonFacade).setConversionService(null);
		seasonFacade.findSeasonsBySerie(mock(SerieTO.class));
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with not set validator for TO for serie. */
	@Test(expected = IllegalStateException.class)
	public void testFindSeasonsBySerieWithNotSetSerieTOValidator() {
		((SeasonFacadeImpl) seasonFacade).setSerieTOValidator(null);
		seasonFacade.findSeasonsBySerie(mock(SerieTO.class));
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with null argument. */
	@Test
	public void testFindSeasonsBySerieWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			seasonFacade.findSeasonsBySerie(null);
			fail("Can't find seasons by serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(null);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService, seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with argument with bad data. */
	@Test
	public void testFindSeasonsBySerieWithBadArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			seasonFacade.findSeasonsBySerie(serie);
			fail("Can't find seasons by serie with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService, seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with not existing argument. */
	@Test
	public void testFindSeasonsBySerieWithNotExistingArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		when(serieService.getSerie(anyInt())).thenReturn(null);

		try {
			seasonFacade.findSeasonsBySerie(serie);
			fail("Can't find seasons by serie with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with exception in service tier. */
	@Test
	public void testFindSeasonsBySerieWithServiceTierException() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

		try {
			seasonFacade.findSeasonsBySerie(serie);
			fail("Can't find seasons by serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/**
	 * Sets season's ID and position.
	 *
	 * @param id       ID
	 * @param position position
	 * @return mocked answer
	 */
	private Answer<Void> setSeasonIdAndPosition(final Integer id, final int position) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Season season = (Season) invocation.getArguments()[0];
				season.setId(id);
				season.setPosition(position);
				return null;
			}

		};
	}

}
