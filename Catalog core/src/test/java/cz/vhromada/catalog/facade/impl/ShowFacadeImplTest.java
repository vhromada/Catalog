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
//import cz.vhromada.catalog.commons.Time;
//import cz.vhromada.catalog.entities.Genre;
//import cz.vhromada.catalog.entities.Show;
//import cz.vhromada.catalog.facade.ShowFacade;
//import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
//import cz.vhromada.catalog.facade.to.GenreTO;
//import cz.vhromada.catalog.facade.to.ShowTO;
//import cz.vhromada.catalog.facade.validators.ShowTOValidator;
//import cz.vhromada.catalog.service.GenreService;
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
// * A class represents test for class {@link ShowFacadeImpl}.
// *
// * @author Vladimir Hromada
// */
//@RunWith(MockitoJUnitRunner.class)
//public class ShowFacadeImplTest extends ObjectGeneratorTest {
//
//    /**
//     * Instance of {@link ShowService}
//     */
//    @Mock
//    private ShowService showService;
//
//    /**
//     * Instance of {@link GenreService}
//     */
//    @Mock
//    private GenreService genreService;
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
//     * Instance of {@link ShowFacade}
//     */
//    private ShowFacade showFacade;
//
//    /**
//     * Initializes facade for shows.
//     */
//    @Before
//    public void setUp() {
//        showFacade = new ShowFacadeImpl(showService, genreService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(ShowService, GenreService, Converter, ShowTOValidator)} with null service for shows.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullShowService() {
//        new ShowFacadeImpl(null, genreService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(ShowService, GenreService, Converter, ShowTOValidator)} with null service for genres.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullGenreService() {
//        new ShowFacadeImpl(showService, null, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(ShowService, GenreService, Converter, ShowTOValidator)} with null converter.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullConverter() {
//        new ShowFacadeImpl(showService, genreService, null, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(ShowService, GenreService, Converter, ShowTOValidator)} with null validator for TO for show.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullShowTOValidator() {
//        new ShowFacadeImpl(showService, genreService, converter, null);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#newData()}.
//     */
//    @Test
//    public void testNewData() {
//        showFacade.newData();
//
//        verify(showService).newData();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#newData()} with exception in service tier.
//     */
//    @Test
//    public void testNewDataWithServiceTierException() {
//        doThrow(ServiceOperationException.class).when(showService).newData();
//
//        try {
//            showFacade.newData();
//            fail("Can't create new data with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).newData();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getShows()}.
//     */
//    @Test
//    public void testGetShows() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<ShowTO> showsList = CollectionUtils.newList(generate(ShowTO.class), generate(ShowTO.class));
//        when(showService.getShows()).thenReturn(shows);
//        when(converter.convertCollection(shows, ShowTO.class)).thenReturn(showsList);
//
//        DeepAsserts.assertEquals(showsList, showFacade.getShows());
//
//        verify(showService).getShows();
//        verify(converter).convertCollection(shows, ShowTO.class);
//        verifyNoMoreInteractions(showService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getShows()} with exception in service tier.
//     */
//    @Test
//    public void testGetShowsWithServiceTierException() {
//        doThrow(ServiceOperationException.class).when(showService).getShows();
//
//        try {
//            showFacade.getShows();
//            fail("Can't get shows with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShows();
//        verifyNoMoreInteractions(showService);
//        verifyZeroInteractions(converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getShow(Integer)} with existing show.
//     */
//    @Test
//    public void testGetShowWithExistingShow() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(show);
//        when(converter.convert(any(Show.class), eq(ShowTO.class))).thenReturn(showTO);
//
//        DeepAsserts.assertEquals(showTO, showFacade.getShow(showTO.getId()));
//
//        verify(showService).getShow(showTO.getId());
//        verify(converter).convert(show, ShowTO.class);
//        verify(converter).convert(show, ShowTO.class);
//        verifyNoMoreInteractions(showService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getShow(Integer)} with not existing show.
//     */
//    @Test
//    public void testGetShowWithNotExistingShow() {
//        when(showService.getShow(anyInt())).thenReturn(null);
//        when(converter.convert(any(Show.class), eq(ShowTO.class))).thenReturn(null);
//
//        assertNull(showFacade.getShow(Integer.MAX_VALUE));
//
//        verify(showService).getShow(Integer.MAX_VALUE);
//        verify(converter).convert(null, ShowTO.class);
//        verifyNoMoreInteractions(showService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getShow(Integer)} with null argument.
//     */
//    @Test
//    public void testGetShowWithNullArgument() {
//        try {
//            showFacade.getShow(null);
//            fail("Can't get show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getShow(Integer)} with exception in service tier.
//     */
//    @Test
//    public void testGetShowWithServiceTierException() {
//        doThrow(ServiceOperationException.class).when(showService).getShow(anyInt());
//
//        try {
//            showFacade.getShow(Integer.MAX_VALUE);
//            fail("Can't get show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(Integer.MAX_VALUE);
//        verifyNoMoreInteractions(showService);
//        verifyZeroInteractions(converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#add(ShowTO)}.
//     */
//    @Test
//    public void testAdd() {
//        final Show show = generate(Show.class);
//        show.setId(null);
//        final ShowTO showTO = generate(ShowTO.class);
//        showTO.setId(null);
//        final int id = generate(Integer.class);
//        final int position = generate(Integer.class);
//        doAnswer(setShowIdAndPosition(id, position)).when(showService).add(any(Show.class));
//        when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
//        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(show);
//
//        showFacade.add(showTO);
//        DeepAsserts.assertEquals(id, show.getId());
//        DeepAsserts.assertEquals(position, show.getPosition());
//
//        verify(showService).add(show);
//        for (final GenreTO genre : showTO.getGenres()) {
//            verify(genreService).getGenre(genre.getId());
//        }
//        verify(converter).convert(showTO, Show.class);
//        verify(showTOValidator).validateNewShowTO(showTO);
//        verifyNoMoreInteractions(showService, genreService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#add(ShowTO)} with null argument.
//     */
//    @Test
//    public void testAddWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(showTOValidator).validateNewShowTO(any(ShowTO.class));
//
//        try {
//            showFacade.add(null);
//            fail("Can't add show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateNewShowTO(null);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService, genreService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#add(ShowTO)} with argument with bad data.
//     */
//    @Test
//    public void testAddWithBadArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        show.setId(null);
//        doThrow(ValidationException.class).when(showTOValidator).validateNewShowTO(any(ShowTO.class));
//
//        try {
//            showFacade.add(show);
//            fail("Can't add show with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateNewShowTO(show);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService, genreService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#add(ShowTO)} with service tier not setting ID.
//     */
//    @Test
//    public void testAddWithNotServiceTierSettingID() {
//        final Show show = generate(Show.class);
//        show.setId(null);
//        final ShowTO showTO = generate(ShowTO.class);
//        showTO.setId(null);
//        when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
//        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(show);
//
//        try {
//            showFacade.add(showTO);
//            fail("Can't add show with service tier not setting ID.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).add(show);
//        for (final GenreTO genre : showTO.getGenres()) {
//            verify(genreService).getGenre(genre.getId());
//        }
//        verify(converter).convert(showTO, Show.class);
//        verify(showTOValidator).validateNewShowTO(showTO);
//        verifyNoMoreInteractions(showService, genreService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#add(ShowTO)} with exception in service tier.
//     */
//    @Test
//    public void testAddWithServiceTierException() {
//        final ShowTO show = generate(ShowTO.class);
//        show.setId(null);
//        doThrow(ServiceOperationException.class).when(genreService).getGenre(anyInt());
//
//        try {
//            showFacade.add(show);
//            fail("Can't add show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(genreService).getGenre(show.getGenres().get(0).getId());
//        verify(showTOValidator).validateNewShowTO(show);
//        verifyNoMoreInteractions(genreService, showTOValidator);
//        verifyZeroInteractions(showService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#update(ShowTO)}.
//     */
//    @Test
//    public void testUpdate() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.exists(any(Show.class))).thenReturn(true);
//        when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
//        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(show);
//
//        showFacade.update(showTO);
//
//        verify(showService).exists(show);
//        verify(showService).update(show);
//        for (final GenreTO genre : showTO.getGenres()) {
//            verify(genreService).getGenre(genre.getId());
//        }
//        verify(converter).convert(showTO, Show.class);
//        verify(showTOValidator).validateExistingShowTO(showTO);
//        verifyNoMoreInteractions(showService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#update(ShowTO)} with null argument.
//     */
//    @Test
//    public void testUpdateWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(showTOValidator).validateExistingShowTO(any(ShowTO.class));
//
//        try {
//            showFacade.update(null);
//            fail("Can't update show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateExistingShowTO(null);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService, genreService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#update(ShowTO)} with argument with bad data.
//     */
//    @Test
//    public void testUpdateWithBadArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ValidationException.class).when(showTOValidator).validateExistingShowTO(any(ShowTO.class));
//
//        try {
//            showFacade.update(show);
//            fail("Can't update show with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateExistingShowTO(show);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService, genreService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#update(ShowTO)} with not existing argument.
//     */
//    @Test
//    public void testUpdateWithNotExistingArgument() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.exists(any(Show.class))).thenReturn(false);
//        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(show);
//
//        try {
//            showFacade.update(showTO);
//            fail("Can't update show with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(showService).exists(show);
//        verify(converter).convert(showTO, Show.class);
//        verify(showTOValidator).validateExistingShowTO(showTO);
//        verifyNoMoreInteractions(showService, genreService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#update(ShowTO)} with exception in service tier.
//     */
//    @Test
//    public void testUpdateWithServiceTierException() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        doThrow(ServiceOperationException.class).when(showService).exists(any(Show.class));
//        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(show);
//
//        try {
//            showFacade.update(showTO);
//            fail("Can't update show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).exists(show);
//        verify(converter).convert(showTO, Show.class);
//        verify(showTOValidator).validateExistingShowTO(showTO);
//        verifyNoMoreInteractions(showService, converter, showTOValidator);
//        verifyZeroInteractions(genreService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#remove(ShowTO)}.
//     */
//    @Test
//    public void testRemove() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(show);
//
//        showFacade.remove(showTO);
//
//        verify(showService).getShow(showTO.getId());
//        verify(showService).remove(show);
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#remove(ShowTO)} with null argument.
//     */
//    @Test
//    public void testRemoveWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.remove(null);
//            fail("Can't remove show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(null);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#remove(ShowTO)} with argument with bad data.
//     */
//    @Test
//    public void testRemoveWithBadArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.remove(show);
//            fail("Can't remove show with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#remove(ShowTO)} with not existing argument.
//     */
//    @Test
//    public void testRemoveWithNotExistingArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(null);
//
//        try {
//            showFacade.remove(show);
//            fail("Can't remove show with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#remove(ShowTO)} with exception in service tier.
//     */
//    @Test
//    public void testRemoveWithServiceTierException() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ServiceOperationException.class).when(showService).getShow(anyInt());
//
//        try {
//            showFacade.remove(show);
//            fail("Can't remove show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#duplicate(ShowTO)}.
//     */
//    @Test
//    public void testDuplicate() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(show);
//
//        showFacade.duplicate(showTO);
//
//        verify(showService).getShow(showTO.getId());
//        verify(showService).duplicate(show);
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#duplicate(ShowTO)} with null argument.
//     */
//    @Test
//    public void testDuplicateWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.duplicate(null);
//            fail("Can't duplicate show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(null);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#duplicate(ShowTO)} with argument with bad data.
//     */
//    @Test
//    public void testDuplicateWithBadArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.duplicate(show);
//            fail("Can't duplicate show with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#duplicate(ShowTO)} with not existing argument.
//     */
//    @Test
//    public void testDuplicateWithNotExistingArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(null);
//
//        try {
//            showFacade.duplicate(show);
//            fail("Can't duplicate show with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#duplicate(ShowTO)} with exception in service tier.
//     */
//    @Test
//    public void testDuplicateWithServiceTierException() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ServiceOperationException.class).when(showService).getShow(anyInt());
//
//        try {
//            showFacade.duplicate(show);
//            fail("Can't duplicate show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveUp(ShowTO)}.
//     */
//    @Test
//    public void testMoveUp() {
//        final Show show = generate(Show.class);
//        final List<Show> shows = CollectionUtils.newList(mock(Show.class), show);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(show);
//        when(showService.getShows()).thenReturn(shows);
//
//        showFacade.moveUp(showTO);
//
//        verify(showService).getShow(showTO.getId());
//        verify(showService).getShows();
//        verify(showService).moveUp(show);
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveUp(ShowTO)} with null argument.
//     */
//    @Test
//    public void testMoveUpWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.moveUp(null);
//            fail("Can't move up show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(null);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveUp(ShowTO)} with argument with bad data.
//     */
//    @Test
//    public void testMoveUpWithBadArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.moveUp(show);
//            fail("Can't move up show with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveUp(ShowTO)} with not existing argument.
//     */
//    @Test
//    public void testMoveUpWithNotExistingArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(null);
//
//        try {
//            showFacade.moveUp(show);
//            fail("Can't move up show with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveUp(ShowTO)} with not movable argument.
//     */
//    @Test
//    public void testMoveUpWithNotMovableArgument() {
//        final Show show = generate(Show.class);
//        final List<Show> shows = CollectionUtils.newList(show, mock(Show.class));
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(show);
//        when(showService.getShows()).thenReturn(shows);
//
//        try {
//            showFacade.moveUp(showTO);
//            fail("Can't move up show with not thrown ValidationException for not movable argument.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(showTO.getId());
//        verify(showService).getShows();
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveUp(ShowTO)} with exception in service tier.
//     */
//    @Test
//    public void testMoveUpWithServiceTierException() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ServiceOperationException.class).when(showService).getShow(anyInt());
//
//        try {
//            showFacade.moveUp(show);
//            fail("Can't move up show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveDown(ShowTO)}.
//     */
//    @Test
//    public void testMoveDown() {
//        final Show show = generate(Show.class);
//        final List<Show> shows = CollectionUtils.newList(show, mock(Show.class));
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(show);
//        when(showService.getShows()).thenReturn(shows);
//
//        showFacade.moveDown(showTO);
//
//        verify(showService).getShow(showTO.getId());
//        verify(showService).getShows();
//        verify(showService).moveDown(show);
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveDown(ShowTO)} with null argument.
//     */
//    @Test
//    public void testMoveDownWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.moveDown(null);
//            fail("Can't move down show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(null);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveDown(ShowTO)} with argument with bad data.
//     */
//    @Test
//    public void testMoveDownWithBadArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.moveDown(show);
//            fail("Can't move down show with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveDown(ShowTO)} with not existing argument.
//     */
//    @Test
//    public void testMoveDownWithNotExistingArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(null);
//
//        try {
//            showFacade.moveDown(show);
//            fail("Can't move down show with not thrown RecordNotFoundException for not existing argument.");
//        } catch (final RecordNotFoundException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveDown(ShowTO)} with not movable argument.
//     */
//    @Test
//    public void testMoveDownWithNotMovableArgument() {
//        final Show show = generate(Show.class);
//        final List<Show> shows = CollectionUtils.newList(mock(Show.class), show);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.getShow(anyInt())).thenReturn(show);
//        when(showService.getShows()).thenReturn(shows);
//
//        try {
//            showFacade.moveDown(showTO);
//            fail("Can't move down show with not thrown ValidationException for not movable argument.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(showTO.getId());
//        verify(showService).getShows();
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#moveDown(ShowTO)} with exception in service tier.
//     */
//    @Test
//    public void testMoveDownWithServiceTierException() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ServiceOperationException.class).when(showService).getShow(anyInt());
//
//        try {
//            showFacade.moveDown(show);
//            fail("Can't move down show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getShow(show.getId());
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showService, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#exists(ShowTO)} with existing show.
//     */
//    @Test
//    public void testExistsWithExistingShow() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.exists(any(Show.class))).thenReturn(true);
//        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(show);
//
//        assertTrue(showFacade.exists(showTO));
//
//        verify(showService).exists(show);
//        verify(converter).convert(showTO, Show.class);
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#exists(ShowTO)} with not existing show.
//     */
//    @Test
//    public void testExistsWithNotExistingShow() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        when(showService.exists(any(Show.class))).thenReturn(false);
//        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(show);
//
//        assertFalse(showFacade.exists(showTO));
//
//        verify(showService).exists(show);
//        verify(converter).convert(showTO, Show.class);
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#exists(ShowTO)} with null argument.
//     */
//    @Test
//    public void testExistsWithNullArgument() {
//        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.exists(null);
//            fail("Can't exists show with not thrown IllegalArgumentException for null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(null);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#exists(ShowTO)} with argument with bad data.
//     */
//    @Test
//    public void testExistsWithBadArgument() {
//        final ShowTO show = generate(ShowTO.class);
//        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));
//
//        try {
//            showFacade.exists(show);
//            fail("Can't exists show with not thrown ValidationException for argument with bad data.");
//        } catch (final ValidationException ex) {
//            // OK
//        }
//
//        verify(showTOValidator).validateShowTOWithId(show);
//        verifyNoMoreInteractions(showTOValidator);
//        verifyZeroInteractions(showService, converter);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#exists(ShowTO)} with exception in service tier.
//     */
//    @Test
//    public void testExistsWithServiceTierException() {
//        final Show show = generate(Show.class);
//        final ShowTO showTO = generate(ShowTO.class);
//        doThrow(ServiceOperationException.class).when(showService).exists(any(Show.class));
//        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(show);
//
//        try {
//            showFacade.exists(showTO);
//            fail("Can't exists show with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).exists(show);
//        verify(converter).convert(showTO, Show.class);
//        verify(showTOValidator).validateShowTOWithId(showTO);
//        verifyNoMoreInteractions(showService, converter, showTOValidator);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#updatePositions()}.
//     */
//    @Test
//    public void testUpdatePositions() {
//        showFacade.updatePositions();
//
//        verify(showService).updatePositions();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#updatePositions()} with exception in service tier.
//     */
//    @Test
//    public void testUpdatePositionsWithServiceTierException() {
//        doThrow(ServiceOperationException.class).when(showService).updatePositions();
//
//        try {
//            showFacade.updatePositions();
//            fail("Can't update positions with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).updatePositions();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getTotalLength()}.
//     */
//    @Test
//    public void testGetTotalLength() {
//        final Time length = generate(Time.class);
//        when(showService.getTotalLength()).thenReturn(length);
//
//        DeepAsserts.assertEquals(length, showFacade.getTotalLength());
//
//        verify(showService).getTotalLength();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getTotalLength()} with exception in service tier.
//     */
//    @Test
//    public void testGetTotalLengthWithServiceTierException() {
//        doThrow(ServiceOperationException.class).when(showService).getTotalLength();
//
//        try {
//            showFacade.getTotalLength();
//            fail("Can't get total length with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getTotalLength();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getSeasonsCount()}.
//     */
//    @Test
//    public void testGetSeasonsCount() {
//        final int count = generate(Integer.class);
//        when(showService.getSeasonsCount()).thenReturn(count);
//
//        DeepAsserts.assertEquals(count, showFacade.getSeasonsCount());
//
//        verify(showService).getSeasonsCount();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getSeasonsCount()} with exception in service tier.
//     */
//    @Test
//    public void testGetSeasonsCountWithServiceTierException() {
//        doThrow(ServiceOperationException.class).when(showService).getSeasonsCount();
//
//        try {
//            showFacade.getSeasonsCount();
//            fail("Can't get count of seasons with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getSeasonsCount();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getEpisodesCount()}.
//     */
//    @Test
//    public void testGetEpisodesCount() {
//        final int count = generate(Integer.class);
//        when(showService.getEpisodesCount()).thenReturn(count);
//
//        DeepAsserts.assertEquals(count, showFacade.getEpisodesCount());
//
//        verify(showService).getEpisodesCount();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Test method for {@link ShowFacade#getEpisodesCount()} with exception in service tier.
//     */
//    @Test
//    public void testGetEpisodesCountWithServiceTierException() {
//        doThrow(ServiceOperationException.class).when(showService).getEpisodesCount();
//
//        try {
//            showFacade.getEpisodesCount();
//            fail("Can't get count of episodes with not thrown FacadeOperationException for service tier exception.");
//        } catch (final FacadeOperationException ex) {
//            // OK
//        }
//
//        verify(showService).getEpisodesCount();
//        verifyNoMoreInteractions(showService);
//    }
//
//    /**
//     * Sets show ID and position.
//     *
//     * @param id       ID
//     * @param position position
//     * @return mocked answer
//     */
//    private static Answer<Void> setShowIdAndPosition(final Integer id, final int position) {
//        return new Answer<Void>() {
//
//            @Override
//            public Void answer(final InvocationOnMock invocation) {
//                final Show show = (Show) invocation.getArguments()[0];
//                show.setId(id);
//                show.setPosition(position);
//                return null;
//            }
//
//        };
//    }
//
//}
