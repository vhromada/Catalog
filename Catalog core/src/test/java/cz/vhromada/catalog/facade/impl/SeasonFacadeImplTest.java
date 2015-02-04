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
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.SerieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
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

/**
 * A class represents test for class {@link SeasonFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SeasonFacadeImplTest extends ObjectGeneratorTest {

    /** Instance of {@link SerieService} */
    @Mock
    private SerieService serieService;

    /** Instance of {@link SeasonService} */
    @Mock
    private SeasonService seasonService;

    /** Instance of {@link Converter} */
    @Mock
    private Converter converter;

    /** Instance of {@link SerieTOValidator} */
    @Mock
    private SerieTOValidator serieTOValidator;

    /** Instance of {@link SeasonTOValidator} */
    @Mock
    private SeasonTOValidator seasonTOValidator;

    /** Instance of {@link SeasonFacade} */
    private SeasonFacade seasonFacade;

    /** Initializes facade for seasons. */
    @Before
    public void setUp() {
        seasonFacade = new SeasonFacadeImpl(serieService, seasonService, converter, serieTOValidator, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(SerieService, SeasonService, Converter, SerieTOValidator, SeasonTOValidator)} with null
     * service for series.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSerieService() {
        new SeasonFacadeImpl(null, seasonService, converter, serieTOValidator, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(SerieService, SeasonService, Converter, SerieTOValidator, SeasonTOValidator)} with null
     * service for seasons.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSeasonService() {
        new SeasonFacadeImpl(serieService, null, converter, serieTOValidator, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(SerieService, SeasonService, Converter, SerieTOValidator, SeasonTOValidator)} with null
     * converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConverter() {
        new SeasonFacadeImpl(serieService, seasonService, null, serieTOValidator, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(SerieService, SeasonService, Converter, SerieTOValidator, SeasonTOValidator)} with null
     * validator for TO for serie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSerieTOValidator() {
        new SeasonFacadeImpl(serieService, seasonService, converter, null, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(SerieService, SeasonService, Converter, SerieTOValidator, SeasonTOValidator)} with null
     * validator for TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSeasonTOValidator() {
        new SeasonFacadeImpl(serieService, seasonService, converter, serieTOValidator, null);
    }

    /** Test method for {@link SeasonFacade#getSeason(Integer)} with existing season. */
    @Test
    public void testGetSeasonWithExistingSeason() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(season);
        when(converter.convert(any(Season.class), eq(SeasonTO.class))).thenReturn(seasonTO);

        DeepAsserts.assertEquals(seasonTO, seasonFacade.getSeason(seasonTO.getId()));

        verify(seasonService).getSeason(seasonTO.getId());
        verify(converter).convert(season, SeasonTO.class);
        verifyNoMoreInteractions(seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#getSeason(Integer)} with not existing season. */
    @Test
    public void testGetSeasonWithNotExistingSeason() {
        when(seasonService.getSeason(anyInt())).thenReturn(null);
        when(converter.convert(any(Season.class), eq(SeasonTO.class))).thenReturn(null);

        assertNull(seasonFacade.getSeason(Integer.MAX_VALUE));

        verify(seasonService).getSeason(Integer.MAX_VALUE);
        verify(converter).convert(null, SeasonTO.class);
        verifyNoMoreInteractions(seasonService, converter);
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

        verifyZeroInteractions(seasonService, converter);
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
        verifyZeroInteractions(converter);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)}. */
    @Test
    public void testAdd() {
        final Season season = generate(Season.class);
        season.setId(null);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        seasonTO.setId(null);
        final int id = generate(Integer.class);
        final int position = generate(Integer.class);
        when(serieService.getSerie(anyInt())).thenReturn(generate(Serie.class));
        doAnswer(setSeasonIdAndPosition(id, position)).when(seasonService).add(any(Season.class));
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

        seasonFacade.add(seasonTO);

        DeepAsserts.assertEquals(id, season.getId());
        DeepAsserts.assertEquals(position, season.getPosition());

        verify(serieService).getSerie(seasonTO.getSerie().getId());
        verify(seasonService).add(season);
        verify(converter).convert(seasonTO, Season.class);
        verify(seasonTOValidator).validateNewSeasonTO(seasonTO);
        verifyNoMoreInteractions(serieService, seasonService, converter, seasonTOValidator);
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
        verifyZeroInteractions(serieService, seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with argument with bad data. */
    @Test
    public void testAddWithBadArgument() {
        final SeasonTO season = generate(SeasonTO.class);
        season.setId(null);
        doThrow(ValidationException.class).when(seasonTOValidator).validateNewSeasonTO(any(SeasonTO.class));

        try {
            seasonFacade.add(season);
            fail("Can't add season with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(seasonTOValidator).validateNewSeasonTO(season);
        verifyNoMoreInteractions(seasonTOValidator);
        verifyZeroInteractions(serieService, seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with not existing argument. */
    @Test
    public void testAddWithNotExistingArgument() {
        final SeasonTO season = generate(SeasonTO.class);
        season.setId(null);
        when(serieService.getSerie(anyInt())).thenReturn(null);

        try {
            seasonFacade.add(season);
            fail("Can't add season with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(serieService).getSerie(season.getSerie().getId());
        verify(seasonTOValidator).validateNewSeasonTO(season);
        verifyNoMoreInteractions(serieService, seasonTOValidator);
        verifyZeroInteractions(seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with service tier not setting ID. */
    @Test
    public void testAddWithNotServiceTierSettingID() {
        final Season season = generate(Season.class);
        season.setId(null);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        seasonTO.setId(null);
        when(serieService.getSerie(anyInt())).thenReturn(generate(Serie.class));
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

        try {
            seasonFacade.add(seasonTO);
            fail("Can't add season with service tier not setting ID.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(serieService).getSerie(seasonTO.getSerie().getId());
        verify(seasonService).add(season);
        verify(converter).convert(seasonTO, Season.class);
        verify(seasonTOValidator).validateNewSeasonTO(seasonTO);
        verifyNoMoreInteractions(serieService, seasonService, converter, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#add(SeasonTO)} with exception in service tier. */
    @Test
    public void testAddWithServiceTierException() {
        final SeasonTO season = generate(SeasonTO.class);
        season.setId(null);
        doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

        try {
            seasonFacade.add(season);
            fail("Can't add season with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(serieService).getSerie(season.getSerie().getId());
        verify(seasonTOValidator).validateNewSeasonTO(season);
        verifyNoMoreInteractions(serieService, seasonTOValidator);
        verifyZeroInteractions(seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)}. */
    @Test
    public void testUpdate() {
        final Serie serie = generate(Serie.class);
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(serieService.getSerie(anyInt())).thenReturn(serie);
        when(seasonService.exists(any(Season.class))).thenReturn(true);
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

        seasonFacade.update(seasonTO);

        verify(serieService).getSerie(seasonTO.getSerie().getId());
        verify(seasonService).exists(season);
        verify(seasonService).update(season);
        verify(converter).convert(seasonTO, Season.class);
        verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
        verifyNoMoreInteractions(serieService, seasonService, converter, seasonTOValidator);
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
        verifyZeroInteractions(serieService, seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with argument with bad data. */
    @Test
    public void testUpdateWithBadArgument() {
        final SeasonTO season = generate(SeasonTO.class);
        doThrow(ValidationException.class).when(seasonTOValidator).validateExistingSeasonTO(any(SeasonTO.class));

        try {
            seasonFacade.update(season);
            fail("Can't update season with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(seasonTOValidator).validateExistingSeasonTO(season);
        verifyNoMoreInteractions(seasonTOValidator);
        verifyZeroInteractions(serieService, seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with not existing argument. */
    @Test
    public void testUpdateWithNotExistingArgument() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.exists(any(Season.class))).thenReturn(false);
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

        try {
            seasonFacade.update(seasonTO);
            fail("Can't update season with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(seasonService).exists(season);
        verify(converter).convert(seasonTO, Season.class);
        verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
        verifyZeroInteractions(serieService);
    }

    /** Test method for {@link SeasonFacade#update(SeasonTO)} with exception in service tier. */
    @Test
    public void testUpdateWithServiceTierException() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        doThrow(ServiceOperationException.class).when(seasonService).exists(any(Season.class));
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

        try {
            seasonFacade.update(seasonTO);
            fail("Can't update season with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).exists(season);
        verify(converter).convert(seasonTO, Season.class);
        verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
        verifyZeroInteractions(serieService);
    }

    /** Test method for {@link SeasonFacade#remove(SeasonTO)}. */
    @Test
    public void testRemove() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(season);

        seasonFacade.remove(seasonTO);

        verify(seasonService).getSeason(seasonTO.getId());
        verify(seasonService).remove(season);
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
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
        final SeasonTO season = generate(SeasonTO.class);
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
        final SeasonTO season = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(null);

        try {
            seasonFacade.remove(season);
            fail("Can't remove season with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#remove(SeasonTO)} with exception in service tier. */
    @Test
    public void testRemoveWithServiceTierException() {
        final SeasonTO season = generate(SeasonTO.class);
        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

        try {
            seasonFacade.remove(season);
            fail("Can't remove season with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#duplicate(SeasonTO)}. */
    @Test
    public void testDuplicate() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(season);

        seasonFacade.duplicate(seasonTO);

        verify(seasonService).getSeason(seasonTO.getId());
        verify(seasonService).duplicate(season);
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
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
        final SeasonTO season = generate(SeasonTO.class);
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
        final SeasonTO season = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(null);

        try {
            seasonFacade.duplicate(season);
            fail("Can't duplicate season with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#duplicate(SeasonTO)} with exception in service tier. */
    @Test
    public void testDuplicateWithServiceTierException() {
        final SeasonTO season = generate(SeasonTO.class);
        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

        try {
            seasonFacade.duplicate(season);
            fail("Can't duplicate season with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#moveUp(SeasonTO)}. */
    @Test
    public void testMoveUp() {
        final Season season = generate(Season.class);
        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), season);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(season);
        when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);

        seasonFacade.moveUp(seasonTO);

        verify(seasonService).getSeason(seasonTO.getId());
        verify(seasonService).findSeasonsBySerie(season.getSerie());
        verify(seasonService).moveUp(season);
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
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
        final SeasonTO season = generate(SeasonTO.class);
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
        final SeasonTO season = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(null);

        try {
            seasonFacade.moveUp(season);
            fail("Can't move up season with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not moveable argument. */
    @Test
    public void testMoveUpWithNotMoveableArgument() {
        final Season season = generate(Season.class);
        final List<Season> seasons = CollectionUtils.newList(season, mock(Season.class));
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(season);
        when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);

        try {
            seasonFacade.moveUp(seasonTO);
            fail("Can't move up season with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(seasonService).getSeason(seasonTO.getId());
        verify(seasonService).findSeasonsBySerie(season.getSerie());
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#moveUp(SeasonTO)} with exception in service tier. */
    @Test
    public void testMoveUpWithServiceTierException() {
        final SeasonTO season = generate(SeasonTO.class);
        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

        try {
            seasonFacade.moveUp(season);
            fail("Can't move up season with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#moveDown(SeasonTO)}. */
    @Test
    public void testMoveDown() {
        final Season season = generate(Season.class);
        final List<Season> seasons = CollectionUtils.newList(season, mock(Season.class));
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(season);
        when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);

        seasonFacade.moveDown(seasonTO);

        verify(seasonService).getSeason(seasonTO.getId());
        verify(seasonService).findSeasonsBySerie(season.getSerie());
        verify(seasonService).moveDown(season);
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

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
        final SeasonTO season = generate(SeasonTO.class);
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
        final SeasonTO season = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(null);

        try {
            seasonFacade.moveDown(season);
            fail("Can't move down season with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not moveable argument. */
    @Test
    public void testMoveDownWithNotMoveableArgument() {
        final Season season = generate(Season.class);
        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), season);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(season);
        when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);

        try {
            seasonFacade.moveDown(seasonTO);
            fail("Can't move down season with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(seasonService).getSeason(seasonTO.getId());
        verify(seasonService).findSeasonsBySerie(season.getSerie());
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#moveDown(SeasonTO)} with exception in service tier. */
    @Test
    public void testMoveDownWithServiceTierException() {
        final SeasonTO season = generate(SeasonTO.class);
        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

        try {
            seasonFacade.moveDown(season);
            fail("Can't move down season with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#exists(SeasonTO)} with existing season. */
    @Test
    public void testExistsWithExistingSeason() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.exists(any(Season.class))).thenReturn(true);
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

        assertTrue(seasonFacade.exists(seasonTO));

        verify(seasonService).exists(season);
        verify(converter).convert(seasonTO, Season.class);
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#exists(SeasonTO)} with not existing season. */
    @Test
    public void testExistsWithNotExistingSeason() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        when(seasonService.exists(any(Season.class))).thenReturn(false);
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

        assertFalse(seasonFacade.exists(seasonTO));

        verify(seasonService).exists(season);
        verify(converter).convert(seasonTO, Season.class);
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
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
        verifyZeroInteractions(seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#exists(SeasonTO)} with argument with bad data. */
    @Test
    public void testExistsWithBadArgument() {
        final SeasonTO season = generate(SeasonTO.class);
        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        try {
            seasonFacade.exists(season);
            fail("Can't exists season with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonTOValidator);
        verifyZeroInteractions(seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#exists(SeasonTO)} with exception in service tier. */
    @Test
    public void testExistsWithServiceTierException() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = generate(SeasonTO.class);
        doThrow(ServiceOperationException.class).when(seasonService).exists(any(Season.class));
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);

        try {
            seasonFacade.exists(seasonTO);
            fail("Can't exists season with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).exists(season);
        verify(converter).convert(seasonTO, Season.class);
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
    }

    /** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)}. */
    @Test
    public void testFindSeasonsBySerie() {
        final Serie serie = generate(Serie.class);
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final SerieTO serieTO = generate(SerieTO.class);
        final List<SeasonTO> seasonsList = CollectionUtils.newList(generate(SeasonTO.class), generate(SeasonTO.class));
        when(serieService.getSerie(anyInt())).thenReturn(serie);
        when(seasonService.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(converter.convertCollection(seasons, SeasonTO.class)).thenReturn(seasonsList);

        DeepAsserts.assertEquals(seasonsList, seasonFacade.findSeasonsBySerie(serieTO));

        verify(serieService).getSerie(serieTO.getId());
        verify(seasonService).findSeasonsBySerie(serie);
        verify(converter).convertCollection(seasons, SeasonTO.class);
        verify(serieTOValidator).validateSerieTOWithId(serieTO);
        verifyNoMoreInteractions(serieService, seasonService, converter, serieTOValidator);
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
        verifyZeroInteractions(serieService, seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with argument with bad data. */
    @Test
    public void testFindSeasonsBySerieWithBadArgument() {
        final SerieTO serie = generate(SerieTO.class);
        doThrow(ValidationException.class).when(serieTOValidator).validateSerieTOWithId(any(SerieTO.class));

        try {
            seasonFacade.findSeasonsBySerie(serie);
            fail("Can't find seasons by serie with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(serieTOValidator).validateSerieTOWithId(serie);
        verifyNoMoreInteractions(serieTOValidator);
        verifyZeroInteractions(serieService, seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with not existing argument. */
    @Test
    public void testFindSeasonsBySerieWithNotExistingArgument() {
        final SerieTO serie = generate(SerieTO.class);
        when(serieService.getSerie(anyInt())).thenReturn(null);

        try {
            seasonFacade.findSeasonsBySerie(serie);
            fail("Can't find seasons by serie with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(serieService).getSerie(serie.getId());
        verify(serieTOValidator).validateSerieTOWithId(serie);
        verifyNoMoreInteractions(serieService, serieTOValidator);
        verifyZeroInteractions(seasonService, converter);
    }

    /** Test method for {@link SeasonFacade#findSeasonsBySerie(SerieTO)} with exception in service tier. */
    @Test
    public void testFindSeasonsBySerieWithServiceTierException() {
        final SerieTO serie = generate(SerieTO.class);
        doThrow(ServiceOperationException.class).when(serieService).getSerie(anyInt());

        try {
            seasonFacade.findSeasonsBySerie(serie);
            fail("Can't find seasons by serie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(serieService).getSerie(serie.getId());
        verify(serieTOValidator).validateSerieTOWithId(serie);
        verifyNoMoreInteractions(serieService, serieTOValidator);
        verifyZeroInteractions(seasonService, converter);
    }

    /**
     * Sets season's ID and position.
     *
     * @param id       ID
     * @param position position
     * @return mocked answer
     */
    private static Answer<Void> setSeasonIdAndPosition(final Integer id, final int position) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                final Season season = (Season) invocation.getArguments()[0];
                season.setId(id);
                season.setPosition(position);
                return null;
            }

        };
    }

}
