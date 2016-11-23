package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.CollectionUtils;
import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.common.ShowUtils;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.entity.ShowTO;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validators.ShowTOValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link ShowFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ShowFacadeImplTest {

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<Show> showService;

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<Genre> genreService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link ShowTOValidator}
     */
    @Mock
    private ShowTOValidator showTOValidator;

    /**
     * Instance of {@link ShowFacade}
     */
    private ShowFacade showFacade;

    /**
     * Initializes facade for shows.
     */
    @Before
    public void setUp() {
        showFacade = new ShowFacadeImpl(showService, genreService, converter, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, CatalogService, Converter, ShowTOValidator)} with null service for shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowService() {
        new ShowFacadeImpl(null, genreService, converter, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, CatalogService, Converter, ShowTOValidator)} with null service for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreService() {
        new ShowFacadeImpl(showService, null, converter, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, CatalogService, Converter, ShowTOValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new ShowFacadeImpl(showService, genreService, null, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, CatalogService, Converter, ShowTOValidator)} with null validator for TO for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowTOValidator() {
        new ShowFacadeImpl(showService, genreService, converter, null);
    }

    /**
     * Test method for {@link ShowFacade#newData()}.
     */
    @Test
    public void testNewData() {
        showFacade.newData();

        verify(showService).newData();
        verifyNoMoreInteractions(showService);
        verifyZeroInteractions(genreService, converter, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacade#getShows()}.
     */
    @Test
    public void testGetShows() {
        final List<Show> showList = CollectionUtils.newList(ShowUtils.newShow(1), ShowUtils.newShow(2));
        final List<ShowTO> expectedShows = CollectionUtils.newList(ShowUtils.newShowTO(1), ShowUtils.newShowTO(2));

        when(showService.getAll()).thenReturn(showList);
        when(converter.convertCollection(anyListOf(Show.class), eq(ShowTO.class))).thenReturn(expectedShows);

        final List<ShowTO> shows = showFacade.getShows();

        assertNotNull(shows);
        assertEquals(expectedShows, shows);

        verify(showService).getAll();
        verify(converter).convertCollection(showList, ShowTO.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(genreService, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)} with existing show.
     */
    @Test
    public void testGetShow_ExistingShow() {
        final Show showEntity = ShowUtils.newShow(1);
        final ShowTO expectedShow = ShowUtils.newShowTO(1);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(converter.convert(any(Show.class), eq(ShowTO.class))).thenReturn(expectedShow);

        final ShowTO show = showFacade.getShow(1);

        assertNotNull(show);
        assertEquals(expectedShow, show);

        verify(showService).get(1);
        verify(converter).convert(showEntity, ShowTO.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(genreService, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)} with not existing show.
     */
    @Test
    public void testGetShow_NotExistingShow() {
        when(showService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(Show.class), eq(ShowTO.class))).thenReturn(null);

        assertNull(showFacade.getShow(Integer.MAX_VALUE));

        verify(showService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, ShowTO.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(genreService, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetShow_NullArgument() {
        showFacade.getShow(null);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)}.
     */
    @Test
    public void testAdd() {
        final Show showEntity = ShowUtils.newShow(null);
        final ShowTO show = ShowUtils.newShowTO(null);

        when(genreService.get(anyInt())).thenReturn(GenreUtils.newGenre(1));
        when(converter.convert(any(ShowTO.class), eq(Show.class))).thenReturn(showEntity);

        showFacade.add(show);

        verify(showService).add(showEntity);
        for (final GenreTO genre : show.getGenres()) {
            verify(genreService).get(genre.getId());
        }
        verify(converter).convert(show, Show.class);
        verify(showTOValidator).validateNewShowTO(show);
        verifyNoMoreInteractions(showService, genreService, converter, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showTOValidator).validateNewShowTO(any(ShowTO.class));

        showFacade.add(null);
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadArgument() {
        doThrow(ValidationException.class).when(showTOValidator).validateNewShowTO(any(ShowTO.class));

        showFacade.add(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#add(ShowTO)} with argument with not existing genre.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingGenre() {
        when(genreService.get(anyInt())).thenReturn(null);

        showFacade.add(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)}.
     */
    @Test
    public void testUpdate() {
        final ShowTO show = ShowUtils.newShowTO(1);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.get(anyInt())).thenReturn(ShowUtils.newShow(1));
        when(genreService.get(anyInt())).thenReturn(GenreUtils.newGenre(1));

        showFacade.update(show);

        verify(showService).get(show.getId());
        verify(showService).update(showArgumentCaptor.capture());
        for (final GenreTO genre : show.getGenres()) {
            verify(genreService).get(genre.getId());
        }
        verify(showTOValidator).validateExistingShowTO(show);
        verifyNoMoreInteractions(showService, genreService, showTOValidator);
        verifyZeroInteractions(converter);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShow(1), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showTOValidator).validateExistingShowTO(any(ShowTO.class));

        showFacade.update(null);
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(showTOValidator).validateExistingShowTO(any(ShowTO.class));

        showFacade.update(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with not existing show.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingShow() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.update(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#update(ShowTO)} with not existing genre.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingGenre() {
        when(showService.get(anyInt())).thenReturn(ShowUtils.newShow(1));
        when(genreService.get(anyInt())).thenReturn(null);

        showFacade.update(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)}.
     */
    @Test
    public void testRemove() {
        final Show showEntity = ShowUtils.newShow(1);
        final ShowTO show = ShowUtils.newShowTO(1);

        when(showService.get(anyInt())).thenReturn(showEntity);

        showFacade.remove(show);

        verify(showService).get(1);
        verify(showService).remove(showEntity);
        verify(showTOValidator).validateShowTOWithId(show);
        verifyNoMoreInteractions(showService, showTOValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        showFacade.remove(null);
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        showFacade.remove(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#remove(ShowTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.remove(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)}.
     */
    @Test
    public void testDuplicate() {
        final Show showEntity = ShowUtils.newShow(1);
        final ShowTO show = ShowUtils.newShowTO(1);

        when(showService.get(anyInt())).thenReturn(showEntity);

        showFacade.duplicate(show);

        verify(showService).get(1);
        verify(showService).duplicate(showEntity);
        verify(showTOValidator).validateShowTOWithId(show);
        verifyNoMoreInteractions(showService, showTOValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        showFacade.duplicate(null);
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        showFacade.duplicate(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(ShowTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.duplicate(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)}.
     */
    @Test
    public void testMoveUp() {
        final Show showEntity = ShowUtils.newShow(2);
        final List<Show> shows = CollectionUtils.newList(ShowUtils.newShow(1), showEntity);
        final ShowTO show = ShowUtils.newShowTO(2);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(showService.getAll()).thenReturn(shows);

        showFacade.moveUp(show);

        verify(showService).get(2);
        verify(showService).getAll();
        verify(showService).moveUp(showEntity);
        verify(showTOValidator).validateShowTOWithId(show);
        verifyNoMoreInteractions(showService, showTOValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        showFacade.moveUp(null);
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        showFacade.moveUp(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.moveUp(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(ShowTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        final Show showEntity = ShowUtils.newShow(Integer.MAX_VALUE);
        final List<Show> shows = CollectionUtils.newList(showEntity, ShowUtils.newShow(1));
        final ShowTO show = ShowUtils.newShowTO(Integer.MAX_VALUE);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(showService.getAll()).thenReturn(shows);

        showFacade.moveUp(show);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)}.
     */
    @Test
    public void testMoveDown() {
        final Show showEntity = ShowUtils.newShow(1);
        final List<Show> shows = CollectionUtils.newList(showEntity, ShowUtils.newShow(2));
        final ShowTO show = ShowUtils.newShowTO(1);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(showService.getAll()).thenReturn(shows);

        showFacade.moveDown(show);

        verify(showService).get(1);
        verify(showService).getAll();
        verify(showService).moveDown(showEntity);
        verify(showTOValidator).validateShowTOWithId(show);
        verifyNoMoreInteractions(showService, showTOValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        showFacade.moveDown(null);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        showFacade.moveDown(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.moveDown(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(ShowTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        final Show showEntity = ShowUtils.newShow(Integer.MAX_VALUE);
        final List<Show> shows = CollectionUtils.newList(ShowUtils.newShow(1), showEntity);
        final ShowTO show = ShowUtils.newShowTO(Integer.MAX_VALUE);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(showService.getAll()).thenReturn(shows);

        showFacade.moveDown(show);
    }

    /**
     * Test method for {@link ShowFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        showFacade.updatePositions();

        verify(showService).updatePositions();
        verifyNoMoreInteractions(showService);
        verifyZeroInteractions(genreService, converter, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final List<Show> showList = CollectionUtils.newList(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2));
        int expectedTotalLength = 0;
        for (final Show show : showList) {
            for (final Season season : show.getSeasons()) {
                for (final Episode episode : season.getEpisodes()) {
                    expectedTotalLength += episode.getLength();
                }
            }
        }

        when(showService.getAll()).thenReturn(showList);

        assertEquals(new Time(expectedTotalLength), showFacade.getTotalLength());

        verify(showService).getAll();
        verifyNoMoreInteractions(showService);
        verifyZeroInteractions(genreService, converter, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacade#getSeasonsCount()}.
     */
    @Test
    public void testGetSeasonsCount() {
        final Show show1 = ShowUtils.newShowWithSeasons(1);
        final Show show2 = ShowUtils.newShowWithSeasons(2);
        final int expectedSeasons = show1.getSeasons().size() + show2.getSeasons().size();

        when(showService.getAll()).thenReturn(CollectionUtils.newList(show1, show2));

        assertEquals(expectedSeasons, showFacade.getSeasonsCount());

        verify(showService).getAll();
        verifyNoMoreInteractions(showService);
        verifyZeroInteractions(genreService, converter, showTOValidator);
    }

    /**
     * Test method for {@link ShowFacade#getEpisodesCount()}.
     */
    @Test
    public void testGetEpisodesCount() {
        final List<Show> showList = CollectionUtils.newList(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2));
        int expectedEpisodes = 0;
        for (final Show show : showList) {
            for (final Season season : show.getSeasons()) {
                expectedEpisodes += season.getEpisodes().size();
            }
        }

        when(showService.getAll()).thenReturn(showList);

        assertEquals(expectedEpisodes, showFacade.getEpisodesCount());

        verify(showService).getAll();
        verifyNoMoreInteractions(showService);
        verifyZeroInteractions(genreService, converter, showTOValidator);
    }

}
