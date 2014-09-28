package cz.vhromada.catalog.facade.impl;

import static cz.vhromada.catalog.commons.TestConstants.ADD_ID;
import static cz.vhromada.catalog.commons.TestConstants.ADD_POSITION;
import static cz.vhromada.catalog.commons.TestConstants.INNER_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.INNER_INNER_COUNT;
import static cz.vhromada.catalog.commons.TestConstants.LENGTH;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_ID;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.catalog.service.EpisodeService;
import cz.vhromada.catalog.service.GenreService;
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
 * A class represents test for class {@link SerieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SerieFacadeImplTest {

	/** Instance of {@link SerieService} */
	@Mock
	private SerieService serieService;

	/** Instance of {@link SeasonService} */
	@Mock
	private SeasonService seasonService;

	/** Instance of {@link EpisodeService} */
	@Mock
	private EpisodeService episodeService;

	/** Instance of {@link GenreService} */
	@Mock
	private GenreService genreService;

	/** Instance of {@link ConversionService} */
	@Mock
	private ConversionService conversionService;

	/** Instance of {@link SerieTOValidator} */
	@Mock
	private SerieTOValidator serieTOValidator;

	/** Instance of {@link SerieFacade} */
	@InjectMocks
	private SerieFacade serieFacade = new SerieFacadeImpl();

	/** Test method for {@link SerieFacadeImpl#getSerieService()} and {@link SerieFacadeImpl#setSerieService(SerieService)}. */
	@Test
	public void testSerieService() {
		final SerieFacadeImpl serieFacade = new SerieFacadeImpl();
		serieFacade.setSerieService(serieService);
		DeepAsserts.assertEquals(serieService, serieFacade.getSerieService());
	}

	/** Test method for {@link SerieFacadeImpl#getSeasonService()} and {@link SerieFacadeImpl#setSeasonService(SeasonService)}. */
	@Test
	public void testSeasonService() {
		final SerieFacadeImpl serieFacade = new SerieFacadeImpl();
		serieFacade.setSeasonService(seasonService);
		DeepAsserts.assertEquals(seasonService, serieFacade.getSeasonService());
	}

	/** Test method for {@link SerieFacadeImpl#getEpisodeService()} and {@link SerieFacadeImpl#setEpisodeService(EpisodeService)}. */
	@Test
	public void testEpisodeService() {
		final SerieFacadeImpl serieFacade = new SerieFacadeImpl();
		serieFacade.setEpisodeService(episodeService);
		DeepAsserts.assertEquals(episodeService, serieFacade.getEpisodeService());
	}

	/** Test method for {@link SerieFacadeImpl#getGenreService()} and {@link SerieFacadeImpl#setGenreService(GenreService)}}. */
	@Test
	public void testGenreService() {
		final SerieFacadeImpl serieFacade = new SerieFacadeImpl();
		serieFacade.setGenreService(genreService);
		DeepAsserts.assertEquals(genreService, serieFacade.getGenreService());
	}

	/** Test method for {@link SerieFacadeImpl#getConversionService()} and {@link SerieFacadeImpl#setConversionService(ConversionService)}. */
	@Test
	public void testConversionService() {
		final SerieFacadeImpl serieFacade = new SerieFacadeImpl();
		serieFacade.setConversionService(conversionService);
		DeepAsserts.assertEquals(conversionService, serieFacade.getConversionService());
	}

	/** Test method for {@link SerieFacadeImpl#getSerieTOValidator()} and {@link SerieFacadeImpl#setSerieTOValidator(SerieTOValidator)}. */
	@Test
	public void testSerieTOValidator() {
		final SerieFacadeImpl serieFacade = new SerieFacadeImpl();
		serieFacade.setSerieTOValidator(serieTOValidator);
		DeepAsserts.assertEquals(serieTOValidator, serieFacade.getSerieTOValidator());
	}

	/** Test method for {@link SerieFacade#newData()}. */
	@Test
	public void testNewData() {
		serieFacade.newData();

		verify(serieService).newData();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#newData()} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.newData();
	}

	/** Test method for {@link SerieFacade#newData()} with exception in service tier. */
	@Test
	public void testNewDataWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(serieService).newData();

		try {
			serieFacade.newData();
			fail("Can't create new data with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).newData();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#getSeries()}. */
	@Test
	public void testGetSeries() {
		final List<Serie> series = CollectionUtils.newList(EntityGenerator.createSerie(PRIMARY_ID), EntityGenerator.createSerie(SECONDARY_ID));
		final List<SerieTO> seriesList = CollectionUtils.newList(ToGenerator.createSerie(PRIMARY_ID), ToGenerator.createSerie(SECONDARY_ID));
		final List<Season> seasons = new ArrayList<>();
		final List<Episode> episodes = new ArrayList<>();
		for (int i = 0; i < INNER_COUNT; i++) {
			seasons.add(EntityGenerator.createSeason(i));
		}
		for (int i = 0; i < INNER_INNER_COUNT / INNER_COUNT; i++) {
			episodes.add(EntityGenerator.createEpisode(i));
		}
		when(serieService.getSeries()).thenReturn(series);
		when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(episodeService.getTotalLengthBySeason(any(Season.class))).thenReturn(new Time(LENGTH * INNER_INNER_COUNT / INNER_COUNT));
		for (int i = 0; i < series.size(); i++) {
			final Serie serie = series.get(i);
			when(conversionService.convert(serie, SerieTO.class)).thenReturn(seriesList.get(i));
		}

		DeepAsserts.assertEquals(seriesList, serieFacade.getSeries());

		verify(serieService).getSeries();
		for (Serie serie : series) {
			verify(seasonService).findSeasonsBySerie(serie);
			verify(conversionService).convert(serie, SerieTO.class);
		}
		for (Season season : seasons) {
			verify(episodeService, times(series.size())).findEpisodesBySeason(season);
			verify(episodeService, times(series.size())).getTotalLengthBySeason(season);
		}
		verifyNoMoreInteractions(serieService, seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SerieFacade#getSeries()} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeriesWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.getSeries();
	}

	/** Test method for {@link SerieFacade#getSeries()} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeriesWithNotSetSeasonService() {
		((SerieFacadeImpl) serieFacade).setSeasonService(null);
		serieFacade.getSeries();
	}

	/** Test method for {@link SerieFacade#getSeries()} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeriesWithNotSetEpisodeService() {
		((SerieFacadeImpl) serieFacade).setEpisodeService(null);
		serieFacade.getSeries();
	}

	/** Test method for {@link SerieFacade#getSeries()} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeriesWithNotSetConversionService() {
		((SerieFacadeImpl) serieFacade).setConversionService(null);
		serieFacade.getSeries();
	}

	/** Test method for {@link SerieFacade#getSeries()} with exception in service tier. */
	@Test
	public void testGetSeriesWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(serieService).getSeries();

		try {
			serieFacade.getSeries();
			fail("Can't get series with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSeries();
		verifyNoMoreInteractions(serieService);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with existing serie. */
	@Test
	public void testGetSerieWithExistingSerie() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		final List<Season> seasons = new ArrayList<>();
		final List<Episode> episodes = new ArrayList<>();
		for (int i = 0; i < INNER_COUNT; i++) {
			seasons.add(EntityGenerator.createSeason(i));
		}
		for (int i = 0; i < INNER_INNER_COUNT / INNER_COUNT; i++) {
			episodes.add(EntityGenerator.createEpisode(i));
		}
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
		when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(episodeService.getTotalLengthBySeason(any(Season.class))).thenReturn(new Time(LENGTH * INNER_INNER_COUNT / INNER_COUNT));
		when(conversionService.convert(any(Serie.class), eq(SerieTO.class))).thenReturn(serieTO);

		DeepAsserts.assertEquals(serieTO, serieFacade.getSerie(PRIMARY_ID));

		verify(serieService).getSerie(PRIMARY_ID);
		verify(conversionService).convert(serie, SerieTO.class);
		verify(seasonService).findSeasonsBySerie(serie);
		for (Season season : seasons) {
			verify(episodeService).findEpisodesBySeason(season);
			verify(episodeService).getTotalLengthBySeason(season);
		}
		verify(conversionService).convert(serie, SerieTO.class);
		verifyNoMoreInteractions(serieService, seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with not existing serie. */
	@Test
	public void testGetSerieWithNotExistingSerie() {
		when(serieService.getSerie(anyInt())).thenReturn(null);
		when(conversionService.convert(any(Serie.class), eq(SerieTO.class))).thenReturn(null);

		assertNull(serieFacade.getSerie(Integer.MAX_VALUE));

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(conversionService).convert(null, SerieTO.class);
		verifyNoMoreInteractions(serieService, conversionService);
		verifyZeroInteractions(seasonService, episodeService);
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testGetSerieWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.getSerie(Integer.MAX_VALUE);
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with not set service for seasons. */
	@Test(expected = IllegalStateException.class)
	public void testGetSerieWithNotSetSeasonService() {
		((SerieFacadeImpl) serieFacade).setSeasonService(null);
		serieFacade.getSerie(Integer.MAX_VALUE);
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with not set service for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testGetSerieWithNotSetEpisodeService() {
		((SerieFacadeImpl) serieFacade).setEpisodeService(null);
		serieFacade.getSerie(Integer.MAX_VALUE);
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetSerieWithNotSetConversionService() {
		((SerieFacadeImpl) serieFacade).setConversionService(null);
		serieFacade.getSerie(Integer.MAX_VALUE);
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with null argument. */
	@Test
	public void testGetSerieWithNullArgument() {
		try {
			serieFacade.getSerie(null);
			fail("Can't get serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(serieService, seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SerieFacade#getSerie(Integer)} with exception in service tier. */
	@Test
	public void testGetSerieWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

		try {
			serieFacade.getSerie(Integer.MAX_VALUE);
			fail("Can't get serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verifyNoMoreInteractions(serieService);
		verifyZeroInteractions(seasonService, episodeService, conversionService);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)}. */
	@Test
	public void testAdd() {
		final Serie serie = EntityGenerator.createSerie();
		final SerieTO serieTO = ToGenerator.createSerie();
		doAnswer(setSerieIdAndPosition(ADD_ID, ADD_POSITION)).when(serieService).add(any(Serie.class));
		when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
		when(conversionService.convert(any(SerieTO.class), eq(Serie.class))).thenReturn(serie);

		serieFacade.add(serieTO);
		DeepAsserts.assertEquals(ADD_ID, serie.getId());
		DeepAsserts.assertEquals(ADD_POSITION, serie.getPosition());

		verify(serieService).add(serie);
		for (GenreTO genre : serieTO.getGenres()) {
			verify(genreService).getGenre(genre.getId());
		}
		verify(conversionService).convert(serieTO, Serie.class);
		verify(serieTOValidator).validateNewSerieTO(serieTO);
		verifyNoMoreInteractions(serieService, genreService, conversionService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.add(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGenreService() {
		((SerieFacadeImpl) serieFacade).setGenreService(null);
		serieFacade.add(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((SerieFacadeImpl) serieFacade).setConversionService(null);
		serieFacade.add(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with not set validator for TO for serie. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetSerieTOValidator() {
		((SerieFacadeImpl) serieFacade).setSerieTOValidator(null);
		serieFacade.add(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(serieTOValidator).validateNewSerieTO(any(SerieTO.class));

		try {
			serieFacade.add(null);
			fail("Can't add serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(serieTOValidator).validateNewSerieTO(null);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService, genreService, conversionService);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with argument with bad data. */
	@Test
	public void testAddWithBadArgument() {
		final SerieTO serie = ToGenerator.createSerie();
		doThrow(ValidationException.class).when(serieTOValidator).validateNewSerieTO(any(SerieTO.class));

		try {
			serieFacade.add(serie);
			fail("Can't add serie with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieTOValidator).validateNewSerieTO(serie);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService, genreService, conversionService);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with service tier not setting ID. */
	@Test
	public void testAddWithNotServiceTierSettingID() {
		final Serie serie = EntityGenerator.createSerie();
		final SerieTO serieTO = ToGenerator.createSerie();
		when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
		when(conversionService.convert(any(SerieTO.class), eq(Serie.class))).thenReturn(serie);

		try {
			serieFacade.add(serieTO);
			fail("Can't add serie with service tier not setting ID.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).add(serie);
		for (GenreTO genre : serieTO.getGenres()) {
			verify(genreService).getGenre(genre.getId());
		}
		verify(conversionService).convert(serieTO, Serie.class);
		verify(serieTOValidator).validateNewSerieTO(serieTO);
		verifyNoMoreInteractions(serieService, genreService, conversionService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#add(SerieTO)} with exception in service tier. */
	@Test
	public void testAddWithServiceTierException() {
		final SerieTO serie = ToGenerator.createSerie();
		doThrow(ServiceOperationException.class).when(genreService).getGenre(anyInt());

		try {
			serieFacade.add(serie);
			fail("Can't add serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(genreService).getGenre(serie.getGenres().get(0).getId());
		verify(serieTOValidator).validateNewSerieTO(serie);
		verifyNoMoreInteractions(genreService, serieTOValidator);
		verifyZeroInteractions(serieService, conversionService);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)}. */
	@Test
	public void testUpdate() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		when(serieService.exists(any(Serie.class))).thenReturn(true);
		when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
		when(conversionService.convert(any(SerieTO.class), eq(Serie.class))).thenReturn(serie);

		serieFacade.update(serieTO);

		verify(serieService).exists(serie);
		verify(serieService).update(serie);
		for (GenreTO genre : serieTO.getGenres()) {
			verify(genreService).getGenre(genre.getId());
		}
		verify(conversionService).convert(serieTO, Serie.class);
		verify(serieTOValidator).validateExistingSerieTO(serieTO);
		verifyNoMoreInteractions(serieService, conversionService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.update(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with not set service for genres. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGenreService() {
		((SerieFacadeImpl) serieFacade).setGenreService(null);
		serieFacade.update(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((SerieFacadeImpl) serieFacade).setConversionService(null);
		serieFacade.update(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with not set validator for TO for serie. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetSerieTOValidator() {
		((SerieFacadeImpl) serieFacade).setSerieTOValidator(null);
		serieFacade.update(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(serieTOValidator).validateExistingSerieTO(any(SerieTO.class));

		try {
			serieFacade.update(null);
			fail("Can't update serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(serieTOValidator).validateExistingSerieTO(null);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService, genreService, conversionService);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with argument with bad data. */
	@Test
	public void testUpdateWithBadArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(serieTOValidator).validateExistingSerieTO(any(SerieTO.class));

		try {
			serieFacade.update(serie);
			fail("Can't update serie with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieTOValidator).validateExistingSerieTO(serie);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService, genreService, conversionService);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with not existing argument. */
	@Test
	public void testUpdateWithNotExistingArgument() {
		final Serie serie = EntityGenerator.createSerie(Integer.MAX_VALUE);
		final SerieTO serieTO = ToGenerator.createSerie(Integer.MAX_VALUE);
		when(serieService.exists(any(Serie.class))).thenReturn(false);
		when(conversionService.convert(any(SerieTO.class), eq(Serie.class))).thenReturn(serie);

		try {
			serieFacade.update(serieTO);
			fail("Can't update serie with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(serieService).exists(serie);
		verify(conversionService).convert(serieTO, Serie.class);
		verify(serieTOValidator).validateExistingSerieTO(serieTO);
		verifyNoMoreInteractions(serieService, genreService, conversionService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#update(SerieTO)} with exception in service tier. */
	@Test
	public void testUpdateWithServiceTierException() {
		final Serie serie = EntityGenerator.createSerie(Integer.MAX_VALUE);
		final SerieTO serieTO = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(serieService).exists(any(Serie.class));
		when(conversionService.convert(any(SerieTO.class), eq(Serie.class))).thenReturn(serie);

		try {
			serieFacade.update(serieTO);
			fail("Can't update serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).exists(serie);
		verify(conversionService).convert(serieTO, Serie.class);
		verify(serieTOValidator).validateExistingSerieTO(serieTO);
		verifyNoMoreInteractions(serieService, conversionService, serieTOValidator);
		verifyZeroInteractions(genreService);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)}. */
	@Test
	public void testRemove() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		when(serieService.getSerie(anyInt())).thenReturn(serie);

		serieFacade.remove(serieTO);

		verify(serieService).getSerie(PRIMARY_ID);
		verify(serieService).remove(serie);
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.remove(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with not set validator for TO for serie. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetSerieTOValidator() {
		((SerieFacadeImpl) serieFacade).setSerieTOValidator(null);
		serieFacade.remove(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.remove(null);
			fail("Can't remove serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(null);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with argument with bad data. */
	@Test
	public void testRemoveWithBadArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.remove(serie);
			fail("Can't remove serie with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with not existing argument. */
	@Test
	public void testRemoveWithNotExistingArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		when(serieService.getSerie(anyInt())).thenReturn(null);

		try {
			serieFacade.remove(serie);
			fail("Can't remove serie with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#remove(SerieTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

		try {
			serieFacade.remove(serie);
			fail("Can't remove serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)}. */
	@Test
	public void testDuplicate() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		when(serieService.getSerie(anyInt())).thenReturn(serie);

		serieFacade.duplicate(serieTO);

		verify(serieService).getSerie(PRIMARY_ID);
		verify(serieService).duplicate(serie);
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.duplicate(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with not set validator for TO for serie. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetSerieTOValidator() {
		((SerieFacadeImpl) serieFacade).setSerieTOValidator(null);
		serieFacade.duplicate(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.duplicate(null);
			fail("Can't duplicate serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(null);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with argument with bad data. */
	@Test
	public void testDuplicateWithBadArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.duplicate(serie);
			fail("Can't duplicate serie with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with not existing argument. */
	@Test
	public void testDuplicateWithNotExistingArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		when(serieService.getSerie(anyInt())).thenReturn(null);

		try {
			serieFacade.duplicate(serie);
			fail("Can't duplicate serie with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#duplicate(SerieTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

		try {
			serieFacade.duplicate(serie);
			fail("Can't duplicate serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)}. */
	@Test
	public void testMoveUp() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final List<Serie> series = CollectionUtils.newList(mock(Serie.class), serie);
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		when(serieService.getSeries()).thenReturn(series);

		serieFacade.moveUp(serieTO);

		verify(serieService).getSerie(PRIMARY_ID);
		verify(serieService).getSeries();
		verify(serieService).moveUp(serie);
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.moveUp(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with not set validator for TO for serie. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSerieTOValidator() {
		((SerieFacadeImpl) serieFacade).setSerieTOValidator(null);
		serieFacade.moveUp(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.moveUp(null);
			fail("Can't move up serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(null);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with argument with bad data. */
	@Test
	public void testMoveUpWithBadArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.moveUp(serie);
			fail("Can't move up serie with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with not existing argument. */
	@Test
	public void testMoveUpWithNotExistingArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		when(serieService.getSerie(anyInt())).thenReturn(null);

		try {
			serieFacade.moveUp(serie);
			fail("Can't move up serie with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with not moveable argument. */
	@Test
	public void testMoveUpWithNotMoveableArgument() {
		final Serie serie = EntityGenerator.createSerie(Integer.MAX_VALUE);
		final List<Serie> series = CollectionUtils.newList(serie, mock(Serie.class));
		final SerieTO serieTO = ToGenerator.createSerie(Integer.MAX_VALUE);
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		when(serieService.getSeries()).thenReturn(series);

		try {
			serieFacade.moveUp(serieTO);
			fail("Can't move up serie with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieService).getSeries();
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#moveUp(SerieTO)} with exception in service tier. */
	@Test
	public void testMoveUpWithServiceTierException() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

		try {
			serieFacade.moveUp(serie);
			fail("Can't move up serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)}. */
	@Test
	public void testMoveDown() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final List<Serie> series = CollectionUtils.newList(serie, mock(Serie.class));
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		when(serieService.getSeries()).thenReturn(series);

		serieFacade.moveDown(serieTO);

		verify(serieService).getSerie(PRIMARY_ID);
		verify(serieService).getSeries();
		verify(serieService).moveDown(serie);
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.moveDown(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with not set validator for TO for serie. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSerieTOValidator() {
		((SerieFacadeImpl) serieFacade).setSerieTOValidator(null);
		serieFacade.moveDown(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.moveDown(null);
			fail("Can't move down serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(null);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with argument with bad data. */
	@Test
	public void testMoveDownWithBadArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.moveDown(serie);
			fail("Can't move down serie with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with not existing argument. */
	@Test
	public void testMoveDownWithNotExistingArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		when(serieService.getSerie(anyInt())).thenReturn(null);

		try {
			serieFacade.moveDown(serie);
			fail("Can't move down serie with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with not moveable argument. */
	@Test
	public void testMoveDownWithNotMoveableArgument() {
		final Serie serie = EntityGenerator.createSerie(Integer.MAX_VALUE);
		final List<Serie> series = CollectionUtils.newList(mock(Serie.class), serie);
		final SerieTO serieTO = ToGenerator.createSerie(Integer.MAX_VALUE);
		when(serieService.getSerie(anyInt())).thenReturn(serie);
		when(serieService.getSeries()).thenReturn(series);

		try {
			serieFacade.moveDown(serieTO);
			fail("Can't move down serie with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieService).getSeries();
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#moveDown(SerieTO)} with exception in service tier. */
	@Test
	public void testMoveDownWithServiceTierException() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

		try {
			serieFacade.moveDown(serie);
			fail("Can't move down serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSerie(Integer.MAX_VALUE);
		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with existing serie. */
	@Test
	public void testExistsWithExistingSerie() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		when(serieService.exists(any(Serie.class))).thenReturn(true);
		when(conversionService.convert(any(SerieTO.class), eq(Serie.class))).thenReturn(serie);

		assertTrue(serieFacade.exists(serieTO));

		verify(serieService).exists(serie);
		verify(conversionService).convert(serieTO, Serie.class);
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, conversionService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with not existing serie. */
	@Test
	public void testExistsWithNotExistingSerie() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		when(serieService.exists(any(Serie.class))).thenReturn(false);
		when(conversionService.convert(any(SerieTO.class), eq(Serie.class))).thenReturn(serie);

		assertFalse(serieFacade.exists(serieTO));

		verify(serieService).exists(serie);
		verify(conversionService).convert(serieTO, Serie.class);
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, conversionService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.exists(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((SerieFacadeImpl) serieFacade).setConversionService(null);
		serieFacade.exists(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with not set validator for TO for serie. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetSerieTOValidator() {
		((SerieFacadeImpl) serieFacade).setSerieTOValidator(null);
		serieFacade.exists(mock(SerieTO.class));
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.exists(null);
			fail("Can't exists serie with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(null);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService, conversionService);
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with argument with bad data. */
	@Test
	public void testExistsWithBadArgument() {
		final SerieTO serie = ToGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

		try {
			serieFacade.exists(serie);
			fail("Can't exists serie with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(serieTOValidator).validateSerieTOWithId(serie);
		verifyNoMoreInteractions(serieTOValidator);
		verifyZeroInteractions(serieService, conversionService);
	}

	/** Test method for {@link SerieFacade#exists(SerieTO)} with exception in service tier. */
	@Test
	public void testExistsWithServiceTierException() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final SerieTO serieTO = ToGenerator.createSerie(PRIMARY_ID);
		doThrow(ServiceOperationException.class).when(serieService).exists(any(Serie.class));
		when(conversionService.convert(any(SerieTO.class), eq(Serie.class))).thenReturn(serie);

		try {
			serieFacade.exists(serieTO);
			fail("Can't exists serie with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).exists(serie);
		verify(conversionService).convert(serieTO, Serie.class);
		verify(serieTOValidator).validateSerieTOWithId(serieTO);
		verifyNoMoreInteractions(serieService, conversionService, serieTOValidator);
	}

	/** Test method for {@link SerieFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		serieFacade.updatePositions();

		verify(serieService).updatePositions();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#updatePositions()} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.updatePositions();
	}

	/** Test method for {@link SerieFacade#updatePositions()} with exception in service tier. */
	@Test
	public void testUpdatePositionsWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(serieService).updatePositions();

		try {
			serieFacade.updatePositions();
			fail("Can't update positions with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).updatePositions();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#getTotalLength()}. */
	@Test
	public void testGetTotalLength() {
		when(serieService.getTotalLength()).thenReturn(TOTAL_LENGTH);

		DeepAsserts.assertEquals(TOTAL_LENGTH, serieFacade.getTotalLength());

		verify(serieService).getTotalLength();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#getTotalLength()} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.getTotalLength();
	}

	/** Test method for {@link SerieFacade#getTotalLength()} with exception in service tier. */
	@Test
	public void testGetTotalLengthWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(serieService).getTotalLength();

		try {
			serieFacade.getTotalLength();
			fail("Can't get total length with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getTotalLength();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#getSeasonsCount()}. */
	@Test
	public void testGetSeasonsCount() {
		when(serieService.getSeasonsCount()).thenReturn(INNER_COUNT);

		DeepAsserts.assertEquals(INNER_COUNT, serieFacade.getSeasonsCount());

		verify(serieService).getSeasonsCount();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#getSeasonsCount()} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeasonsCountWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.getSeasonsCount();
	}

	/** Test method for {@link SerieFacade#getSeasonsCount()} with exception in service tier. */
	@Test
	public void testGetSeasonsCountWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(serieService).getSeasonsCount();

		try {
			serieFacade.getSeasonsCount();
			fail("Can't get count of seasons with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getSeasonsCount();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#getEpisodesCount()}. */
	@Test
	public void testGetEpisodesCount() {
		when(serieService.getEpisodesCount()).thenReturn(INNER_INNER_COUNT);

		DeepAsserts.assertEquals(INNER_INNER_COUNT, serieFacade.getEpisodesCount());

		verify(serieService).getEpisodesCount();
		verifyNoMoreInteractions(serieService);
	}

	/** Test method for {@link SerieFacade#getEpisodesCount()} with not set service for series. */
	@Test(expected = IllegalStateException.class)
	public void testGetEpisodesCountWithNotSetSerieService() {
		((SerieFacadeImpl) serieFacade).setSerieService(null);
		serieFacade.getEpisodesCount();
	}

	/** Test method for {@link SerieFacade#getEpisodesCount()} with exception in service tier. */
	@Test
	public void testGetEpisodesCountWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(serieService).getEpisodesCount();

		try {
			serieFacade.getEpisodesCount();
			fail("Can't get count of episodes with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(serieService).getEpisodesCount();
		verifyNoMoreInteractions(serieService);
	}

	/**
	 * Sets serie's ID and position.
	 *
	 * @param id       ID
	 * @param position position
	 * @return mocked answer
	 */
	private Answer<Void> setSerieIdAndPosition(final Integer id, final int position) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Serie serie = (Serie) invocation.getArguments()[0];
				serie.setId(id);
				serie.setPosition(position);
				return null;
			}

		};
	}

}
