//package cz.vhromada.catalog.facade.impl;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoMoreInteractions;
//import static org.mockito.Mockito.verifyZeroInteractions;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//
//import cz.vhromada.catalog.commons.CollectionUtils;
//import cz.vhromada.catalog.commons.ObjectGeneratorTest;
//import cz.vhromada.catalog.dao.entities.Season;
//import cz.vhromada.catalog.dao.entities.Show;
//import cz.vhromada.catalog.facade.SeasonFacade;
//import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
//import cz.vhromada.catalog.facade.to.SeasonTO;
//import cz.vhromada.catalog.facade.to.ShowTO;
//import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
//import cz.vhromada.catalog.facade.validators.ShowTOValidator;
//import cz.vhromada.catalog.service.SeasonService;
//import cz.vhromada.catalog.service.ShowService;
//import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
//import cz.vhromada.converters.Converter;
//import cz.vhromada.test.DeepAsserts;
//import cz.vhromada.validators.exceptions.RecordNotFoundException;
//import cz.vhromada.validators.exceptions.ValidationException;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.mockito.stubbing.Answer;
//
///**
// * A class represents test for class {@link SeasonFacadeImpl}.
// *
// * @author Vladimir Hromada
// */
//@RunWith(MockitoJUnitRunner.class)
//public class SeasonFacadeImplTest extends ObjectGeneratorTest {
//
//    /**
//     * Instance of {@link ShowService}
//     */
//    @Mock
//    private ShowService showService;
//
//    /**
//     * Instance of {@link SeasonService}
//     */
//    @Mock
//    private SeasonService seasonService;
//
//    /**
//     * Instance of {@link Converter}
//     */
//    @Mock
//    private Converter converter;
//
//    /**
//     * Instance of {@link ShowTOValidator}
//     */
//    @Mock
//    private ShowTOValidator showTOValidator;
//
//    /**
//     * Instance of {@link SeasonTOValidator}
//     */
//    @Mock
//    private SeasonTOValidator seasonTOValidator;
//
//    /**
//     * Instance of {@link SeasonFacade}
//     */
//    private SeasonFacade seasonFacade;
//
//    /**
//     * Initializes facade for seasons.
//     */
//    @Before
//    public void setUp() {
//        seasonFacade = new SeasonFacadeImpl(showService, seasonService, converter, showTOValidator, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(ShowService, SeasonService, Converter, ShowTOValidator, SeasonTOValidator)} with null
//     * service for shows.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullShowService() {
//        new SeasonFacadeImpl(null, seasonService, converter, showTOValidator, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(ShowService, SeasonService, Converter, ShowTOValidator, SeasonTOValidator)} with null
//     * service for seasons.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullSeasonService() {
//        new SeasonFacadeImpl(showService, null, converter, showTOValidator, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(ShowService, SeasonService, Converter, ShowTOValidator, SeasonTOValidator)} with null
//     * converter.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullConverter() {
//        new SeasonFacadeImpl(showService, seasonService, null, showTOValidator, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(ShowService, SeasonService, Converter, ShowTOValidator, SeasonTOValidator)} with null
//     * validator for TO for show.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullShowTOValidator() {
//        new SeasonFacadeImpl(showService, seasonService, converter, null, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(ShowService, SeasonService, Converter, ShowTOValidator, SeasonTOValidator)} with null
//     * validator for TO for season.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullSeasonTOValidator() {
//        new SeasonFacadeImpl(showService, seasonService, converter, showTOValidator, null);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#getSeason(Integer)} with existing season.
//     */
//    @Test
//    public void testGetSeasonWithExistingSeason() {
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(season);
//        when(converter.convert(any(Season.class), eq(SeasonTO.class))).thenReturn(seasonTO);
//
//        DeepAsserts.assertEquals(seasonTO, seasonFacade.getSeason(seasonTO.getId()));
//
//        verify(seasonService).getSeason(seasonTO.getId());
//        verify(converter).convert(season, SeasonTO.class);
//        verifyNoMoreInteractions(seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#getSeason(Integer)} with not existing season.
//     */
//    @Test
//    public void testGetSeasonWithNotExistingSeason() {
//        when(seasonService.getSeason(anyInt())).thenReturn(null);
//        when(converter.convert(any(Season.class), eq(SeasonTO.class))).thenReturn(null);
//
//        assertNull(seasonFacade.getSeason(Integer.MAX_VALUE));
//
//        verify(seasonService).getSeason(Integer.MAX_VALUE);
//        verify(converter).convert(null, SeasonTO.class);
//        verifyNoMoreInteractions(seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#getSeason(Integer)} with null argument.
//     */
//    @Test
//    public void testGetSeasonWithNullArgument() {
//        try {
//            seasonFacade.getSeason(null);
//            fail("Can't get season with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#getSeason(Integer)} with exception in service tier.
//     */
//    @Test
//    public void testGetSeasonWithServiceTierException() {
//        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());
//
//        try {
//            seasonFacade.getSeason(Integer.MAX_VALUE);
//            fail("Can't get season with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(Integer.MAX_VALUE);
//        verifyNoMoreInteractions(seasonService);
//        verifyZeroInteractions(converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#add(SeasonTO)}.
//     */
//    @Test
//    public void testAdd() {
//        final Season season = generate(Season.class);
//        season.setId(null);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        seasonTO.setId(null);
//        final int id = generate(Integer.class);
//        final int position = generate(Integer.class);
//        when(showService.getShow(anyInt())).thenReturn(generate(Show.class));
//        doAnswer(setSeasonIdAndPosition(id, position)).when(seasonService).add(any(Season.class));
//        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);
//
//        seasonFacade.add(seasonTO);
//
//        DeepAsserts.assertEquals(id, season.getId());
//        DeepAsserts.assertEquals(position, season.getPosition());
//
//        verify(showService).getShow(seasonTO.getShow().getId());
//        verify(seasonService).add(season);
//        verify(converter).convert(seasonTO, Season.class);
//        verify(seasonTOValidator).validateNewSeasonTO(seasonTO);
//        verifyNoMoreInteractions(showService, seasonService, converter, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#add(SeasonTO)} with null argument.
//     */
//    @Test
//    public void testAddWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateNewSeasonTO(any(SeasonTO.class));
//
//        try {
//            seasonFacade.add(null);
//            fail("Can't add season with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateNewSeasonTO(null);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(showService, seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#add(SeasonTO)} with argument with bad data.
//     */
//    @Test
//    public void testAddWithBadArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        season.setId(null);
//        doThrow(ValidationException.class).when(seasonTOValidator).validateNewSeasonTO(any(SeasonTO.class));
//
//        try {
//            seasonFacade.add(season);
//            fail("Can't add season with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateNewSeasonTO(season);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(showService, seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#add(SeasonTO)} with not existing argument.
//     */
//    @Test
//    public void testAddWithNotExistingArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        season.setId(null);
//        when(showService.getShow(anyInt())).thenReturn(null);
//
//        try {
//            seasonFacade.add(season);
//            fail("Can't add season with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(season.getShow().getId());
//        verify(seasonTOValidator).validateNewSeasonTO(season);
//        verifyNoMoreInteractions(showService, seasonTOValidator);
//        verifyZeroInteractions(seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#add(SeasonTO)} with service tier not setting ID.
//     */
//    @Test
//    public void testAddWithNotServiceTierSettingID() {
//        final Season season = generate(Season.class);
//        season.setId(null);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        seasonTO.setId(null);
//        when(showService.getShow(anyInt())).thenReturn(generate(Show.class));
//        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);
//
//        try {
//            seasonFacade.add(seasonTO);
//            fail("Can't add season with service tier not setting ID.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(seasonTO.getShow().getId());
//        verify(seasonService).add(season);
//        verify(converter).convert(seasonTO, Season.class);
//        verify(seasonTOValidator).validateNewSeasonTO(seasonTO);
//        verifyNoMoreInteractions(showService, seasonService, converter, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#add(SeasonTO)} with exception in service tier.
//     */
//    @Test
//    public void testAddWithServiceTierException() {
//        final SeasonTO season = generate(SeasonTO.class);
//        season.setId(null);
//        doThrow(ServiceOperationException.class).when(showService).getShow(anyInt());
//
//        try {
//            seasonFacade.add(season);
//            fail("Can't add season with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(season.getShow().getId());
//        verify(seasonTOValidator).validateNewSeasonTO(season);
//        verifyNoMoreInteractions(showService, seasonTOValidator);
//        verifyZeroInteractions(seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#update(SeasonTO)}.
//     */
//    @Test
//    public void testUpdate() {
//        final Show show = generate(Show.class);
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(showService.getShow(anyInt())).thenReturn(show);
//        when(seasonService.exists(any(Season.class))).thenReturn(true);
//        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);
//
//        seasonFacade.update(seasonTO);
//
//        verify(showService).getShow(seasonTO.getShow().getId());
//        verify(seasonService).exists(season);
//        verify(seasonService).update(season);
//        verify(converter).convert(seasonTO, Season.class);
//        verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
//        verifyNoMoreInteractions(showService, seasonService, converter, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#update(SeasonTO)} with null argument.
//     */
//    @Test
//    public void testUpdateWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateExistingSeasonTO(any(SeasonTO.class));
//
//        try {
//            seasonFacade.update(null);
//            fail("Can't update season with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateExistingSeasonTO(null);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(showService, seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#update(SeasonTO)} with argument with bad data.
//     */
//    @Test
//    public void testUpdateWithBadArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ValidationException.class).when(seasonTOValidator).validateExistingSeasonTO(any(SeasonTO.class));
//
//        try {
//            seasonFacade.update(season);
//            fail("Can't update season with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateExistingSeasonTO(season);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(showService, seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#update(SeasonTO)} with not existing argument.
//     */
//    @Test
//    public void testUpdateWithNotExistingArgument() {
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.exists(any(Season.class))).thenReturn(false);
//        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);
//
//        try {
//            seasonFacade.update(seasonTO);
//            fail("Can't update season with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(seasonService).exists(season);
//        verify(converter).convert(seasonTO, Season.class);
//        verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
//        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#update(SeasonTO)} with exception in service tier.
//     */
//    @Test
//    public void testUpdateWithServiceTierException() {
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        doThrow(ServiceOperationException.class).when(seasonService).exists(any(Season.class));
//        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);
//
//        try {
//            seasonFacade.update(seasonTO);
//            fail("Can't update season with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(seasonService).exists(season);
//        verify(converter).convert(seasonTO, Season.class);
//        verify(seasonTOValidator).validateExistingSeasonTO(seasonTO);
//        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#remove(SeasonTO)}.
//     */
//    @Test
//    public void testRemove() {
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(season);
//
//        seasonFacade.remove(seasonTO);
//
//        verify(seasonService).getSeason(seasonTO.getId());
//        verify(seasonService).remove(season);
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#remove(SeasonTO)} with null argument.
//     */
//    @Test
//    public void testRemoveWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.remove(null);
//            fail("Can't remove season with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(null);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#remove(SeasonTO)} with argument with bad data.
//     */
//    @Test
//    public void testRemoveWithBadArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.remove(season);
//            fail("Can't remove season with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#remove(SeasonTO)} with not existing argument.
//     */
//    @Test
//    public void testRemoveWithNotExistingArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(null);
//
//        try {
//            seasonFacade.remove(season);
//            fail("Can't remove season with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(season.getId());
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#remove(SeasonTO)} with exception in service tier.
//     */
//    @Test
//    public void testRemoveWithServiceTierException() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());
//
//        try {
//            seasonFacade.remove(season);
//            fail("Can't remove season with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(season.getId());
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#duplicate(SeasonTO)}.
//     */
//    @Test
//    public void testDuplicate() {
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(season);
//
//        seasonFacade.duplicate(seasonTO);
//
//        verify(seasonService).getSeason(seasonTO.getId());
//        verify(seasonService).duplicate(season);
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with null argument.
//     */
//    @Test
//    public void testDuplicateWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.duplicate(null);
//            fail("Can't duplicate season with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(null);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with argument with bad data.
//     */
//    @Test
//    public void testDuplicateWithBadArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.duplicate(season);
//            fail("Can't duplicate season with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with not existing argument.
//     */
//    @Test
//    public void testDuplicateWithNotExistingArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(null);
//
//        try {
//            seasonFacade.duplicate(season);
//            fail("Can't duplicate season with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(season.getId());
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with exception in service tier.
//     */
//    @Test
//    public void testDuplicateWithServiceTierException() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());
//
//        try {
//            seasonFacade.duplicate(season);
//            fail("Can't duplicate season with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(season.getId());
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveUp(SeasonTO)}.
//     */
//    @Test
//    public void testMoveUp() {
//        final Season season = generate(Season.class);
//        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), season);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(season);
//        when(seasonService.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//
//        seasonFacade.moveUp(seasonTO);
//
//        verify(seasonService).getSeason(seasonTO.getId());
//        verify(seasonService).findSeasonsByShow(season.getShow());
//        verify(seasonService).moveUp(season);
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with null argument.
//     */
//    @Test
//    public void testMoveUpWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.moveUp(null);
//            fail("Can't move up season with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(null);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with argument with bad data.
//     */
//    @Test
//    public void testMoveUpWithBadArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.moveUp(season);
//            fail("Can't move up season with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not existing argument.
//     */
//    @Test
//    public void testMoveUpWithNotExistingArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(null);
//
//        try {
//            seasonFacade.moveUp(season);
//            fail("Can't move up season with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(season.getId());
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not movable argument.
//     */
//    @Test
//    public void testMoveUpWithNotMovableArgument() {
//        final Season season = generate(Season.class);
//        final List<Season> seasons = CollectionUtils.newList(season, mock(Season.class));
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(season);
//        when(seasonService.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//
//        try {
//            seasonFacade.moveUp(seasonTO);
//            fail("Can't move up season with not thrown ValidationException for not movable argument.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(seasonTO.getId());
//        verify(seasonService).findSeasonsByShow(season.getShow());
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with exception in service tier.
//     */
//    @Test
//    public void testMoveUpWithServiceTierException() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());
//
//        try {
//            seasonFacade.moveUp(season);
//            fail("Can't move up season with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(season.getId());
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveDown(SeasonTO)}.
//     */
//    @Test
//    public void testMoveDown() {
//        final Season season = generate(Season.class);
//        final List<Season> seasons = CollectionUtils.newList(season, mock(Season.class));
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(season);
//        when(seasonService.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//
//        seasonFacade.moveDown(seasonTO);
//
//        verify(seasonService).getSeason(seasonTO.getId());
//        verify(seasonService).findSeasonsByShow(season.getShow());
//        verify(seasonService).moveDown(season);
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    @Test
//    public void testMoveDownWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.moveDown(null);
//            fail("Can't move down season with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(null);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with argument with bad data.
//     */
//    @Test
//    public void testMoveDownWithBadArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.moveDown(season);
//            fail("Can't move down season with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not existing argument.
//     */
//    @Test
//    public void testMoveDownWithNotExistingArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(null);
//
//        try {
//            seasonFacade.moveDown(season);
//            fail("Can't move down season with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(season.getId());
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not movable argument.
//     */
//    @Test
//    public void testMoveDownWithNotMovableArgument() {
//        final Season season = generate(Season.class);
//        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), season);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.getSeason(anyInt())).thenReturn(season);
//        when(seasonService.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//
//        try {
//            seasonFacade.moveDown(seasonTO);
//            fail("Can't move down season with not thrown ValidationException for not movable argument.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(seasonTO.getId());
//        verify(seasonService).findSeasonsByShow(season.getShow());
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with exception in service tier.
//     */
//    @Test
//    public void testMoveDownWithServiceTierException() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());
//
//        try {
//            seasonFacade.moveDown(season);
//            fail("Can't move down season with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(seasonService).getSeason(season.getId());
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonService, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#exists(SeasonTO)} with existing season.
//     */
//    @Test
//    public void testExistsWithExistingSeason() {
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.exists(any(Season.class))).thenReturn(true);
//        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);
//
//        assertTrue(seasonFacade.exists(seasonTO));
//
//        verify(seasonService).exists(season);
//        verify(converter).convert(seasonTO, Season.class);
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#exists(SeasonTO)} with not existing season.
//     */
//    @Test
//    public void testExistsWithNotExistingSeason() {
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        when(seasonService.exists(any(Season.class))).thenReturn(false);
//        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);
//
//        assertFalse(seasonFacade.exists(seasonTO));
//
//        verify(seasonService).exists(season);
//        verify(converter).convert(seasonTO, Season.class);
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#exists(SeasonTO)} with null argument.
//     */
//    @Test
//    public void testExistsWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.exists(null);
//            fail("Can't exists season with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(null);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#exists(SeasonTO)} with argument with bad data.
//     */
//    @Test
//    public void testExistsWithBadArgument() {
//        final SeasonTO season = generate(SeasonTO.class);
//        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));
//
//        try {
//            seasonFacade.exists(season);
//            fail("Can't exists season with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(seasonTOValidator).validateSeasonTOWithId(season);
//        verifyNoMoreInteractions(seasonTOValidator);
//        verifyZeroInteractions(seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#exists(SeasonTO)} with exception in service tier.
//     */
//    @Test
//    public void testExistsWithServiceTierException() {
//        final Season season = generate(Season.class);
//        final SeasonTO seasonTO = generate(SeasonTO.class);
//        doThrow(ServiceOperationException.class).when(seasonService).exists(any(Season.class));
//        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(season);
//
//        try {
//            seasonFacade.exists(seasonTO);
//            fail("Can't exists season with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(seasonService).exists(season);
//        verify(converter).convert(seasonTO, Season.class);
//        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
//        verifyNoMoreInteractions(seasonService, converter, seasonTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)}.
//     */
//    @Test
//    public void testFindSeasonsByShow() {
//        final Show show = generate(Show.class);
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final ShowTO showTO = generate(ShowTO.class);
//        final List<SeasonTO> seasonsList = CollectionUtils.newList(generate(SeasonTO.class), generate(SeasonTO.class));
//        when(showService.getShow(anyInt())).thenReturn(show);
//        when(seasonService.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//        when(converter.convertCollection(seasons, SeasonTO.class)).thenReturn(seasonsList);
//
//        DeepAsserts.assertEquals(seasonsList, seasonFacade.findSeasonsByShow(showTO));
//
//        verify(showService).getShow(showTO.getId());
//        verify(seasonService).findSeasonsByShow(show);
//        verify(converter).convertCollection(seasons, SeasonTO.class);
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, seasonService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with null argument.
//     */
//    @Test
//    public void testFindSeasonsByShowWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            seasonFacade.findSeasonsByShow(null);
//            fail("Can't find seasons by show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(null);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService, seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with argument with bad data.
//     */
//    @Test
//    public void testFindSeasonsByShowWithBadArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            seasonFacade.findSeasonsByShow(show);
//            fail("Can't find seasons by show with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService, seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with not existing argument.
//     */
//    @Test
//    public void testFindSeasonsByShowWithNotExistingArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(null);
//
//        try {
//            seasonFacade.findSeasonsByShow(show);
//            fail("Can't find seasons by show with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//        verifyZeroInteractions(seasonService, converter);
//    }
//
//    /**
//     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with exception in service tier.
//     */
//    @Test
//    public void testFindSeasonsByShowWithServiceTierException() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ServiceOperationException.class).when(showService).getShow(anyInt());
//
//        try {
//            seasonFacade.findSeasonsByShow(show);
//            fail("Can't find seasons by show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//        verifyZeroInteractions(seasonService, converter);
//    }
//
//    /**
//     * Sets season's ID and position.
//     *
//     * @param id       ID
//     * @param position position
//     * @return mocked answer
//     */
//    private static Answer<Void> setSeasonIdAndPosition(final Integer id, final int position) {
//        return new Answer<Void>() {
//
//            @Override
//            public Void answer(final InvocationOnMock invocation) {
//                final Season season = (Season) invocation.getArguments()[0];
//                season.setId(id);
//                season.setPosition(position);
//                return null;
//            }
//
//        };
//    }
//
//}
