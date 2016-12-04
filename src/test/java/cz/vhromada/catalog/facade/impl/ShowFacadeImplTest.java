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

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.validator.ShowValidator;
import cz.vhromada.converters.Converter;

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
    private CatalogService<cz.vhromada.catalog.domain.Show> showService;

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<cz.vhromada.catalog.domain.Genre> genreService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link ShowValidator}
     */
    @Mock
    private ShowValidator showValidator;

    /**
     * Instance of {@link ShowFacade}
     */
    private ShowFacade showFacade;

    /**
     * Initializes facade for shows.
     */
    @Before
    public void setUp() {
        showFacade = new ShowFacadeImpl(showService, genreService, converter, showValidator);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, CatalogService, Converter, ShowValidator)} with null service for shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowService() {
        new ShowFacadeImpl(null, genreService, converter, showValidator);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, CatalogService, Converter, ShowValidator)} with null service for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreService() {
        new ShowFacadeImpl(showService, null, converter, showValidator);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, CatalogService, Converter, ShowValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new ShowFacadeImpl(showService, genreService, null, showValidator);
    }

    /**
     * Test method for {@link ShowFacadeImpl#ShowFacadeImpl(CatalogService, CatalogService, Converter, ShowValidator)} with null validator for TO for show.
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
        verifyZeroInteractions(genreService, converter, showValidator);
    }

    /**
     * Test method for {@link ShowFacade#getShows()}.
     */
    @Test
    public void testGetShows() {
        final List<cz.vhromada.catalog.domain.Show> showList = CollectionUtils.newList(ShowUtils.newShowDomain(1), ShowUtils.newShowDomain(2));
        final List<Show> expectedShows = CollectionUtils.newList(ShowUtils.newShow(1), ShowUtils.newShow(2));

        when(showService.getAll()).thenReturn(showList);
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Show.class), eq(Show.class))).thenReturn(expectedShows);

        final List<Show> shows = showFacade.getShows();

        assertNotNull(shows);
        assertEquals(expectedShows, shows);

        verify(showService).getAll();
        verify(converter).convertCollection(showList, Show.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(genreService, showValidator);
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)} with existing show.
     */
    @Test
    public void testGetShow_ExistingShow() {
        final cz.vhromada.catalog.domain.Show showEntity = ShowUtils.newShowDomain(1);
        final Show expectedShow = ShowUtils.newShow(1);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(converter.convert(any(cz.vhromada.catalog.domain.Show.class), eq(Show.class))).thenReturn(expectedShow);

        final Show show = showFacade.getShow(1);

        assertNotNull(show);
        assertEquals(expectedShow, show);

        verify(showService).get(1);
        verify(converter).convert(showEntity, Show.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(genreService, showValidator);
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)} with not existing show.
     */
    @Test
    public void testGetShow_NotExistingShow() {
        when(showService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(cz.vhromada.catalog.domain.Show.class), eq(Show.class))).thenReturn(null);

        assertNull(showFacade.getShow(Integer.MAX_VALUE));

        verify(showService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, Show.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(genreService, showValidator);
    }

    /**
     * Test method for {@link ShowFacade#getShow(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetShow_NullArgument() {
        showFacade.getShow(null);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)}.
     */
    @Test
    public void testAdd() {
        final cz.vhromada.catalog.domain.Show showEntity = ShowUtils.newShowDomain(null);
        final Show show = ShowUtils.newShow(null);

        when(genreService.get(anyInt())).thenReturn(GenreUtils.newGenreDomain(1));
        when(converter.convert(any(Show.class), eq(cz.vhromada.catalog.domain.Show.class))).thenReturn(showEntity);

        showFacade.add(show);

        verify(showService).add(showEntity);
        for (final Genre genre : show.getGenres()) {
            verify(genreService).get(genre.getId());
        }
        verify(converter).convert(show, cz.vhromada.catalog.domain.Show.class);
        verify(showValidator).validateNewShow(show);
        verifyNoMoreInteractions(showService, genreService, converter, showValidator);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateNewShow(any(Show.class));

        showFacade.add(null);
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateNewShow(any(Show.class));

        showFacade.add(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#add(Show)} with argument with not existing genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotExistingGenre() {
        when(genreService.get(anyInt())).thenReturn(null);

        showFacade.add(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)}.
     */
    @Test
    public void testUpdate() {
        final Show show = ShowUtils.newShow(1);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Show> showArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Show.class);

        when(showService.get(anyInt())).thenReturn(ShowUtils.newShowDomain(1));
        when(genreService.get(anyInt())).thenReturn(GenreUtils.newGenreDomain(1));

        showFacade.update(show);

        verify(showService).get(show.getId());
        verify(showService).update(showArgumentCaptor.capture());
        for (final Genre genre : show.getGenres()) {
            verify(genreService).get(genre.getId());
        }
        verify(showValidator).validateExistingShow(show);
        verifyNoMoreInteractions(showService, genreService, showValidator);
        verifyZeroInteractions(converter);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShowDomain(1), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateExistingShow(any(Show.class));

        showFacade.update(null);
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateExistingShow(any(Show.class));

        showFacade.update(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with not existing show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotExistingShow() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.update(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#update(Show)} with not existing genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotExistingGenre() {
        when(showService.get(anyInt())).thenReturn(ShowUtils.newShowDomain(1));
        when(genreService.get(anyInt())).thenReturn(null);

        showFacade.update(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)}.
     */
    @Test
    public void testRemove() {
        final cz.vhromada.catalog.domain.Show showEntity = ShowUtils.newShowDomain(1);
        final Show show = ShowUtils.newShow(1);

        when(showService.get(anyInt())).thenReturn(showEntity);

        showFacade.remove(show);

        verify(showService).get(1);
        verify(showService).remove(showEntity);
        verify(showValidator).validateShowWithId(show);
        verifyNoMoreInteractions(showService, showValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        showFacade.remove(null);
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        showFacade.remove(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#remove(Show)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.remove(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)}.
     */
    @Test
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Show showEntity = ShowUtils.newShowDomain(1);
        final Show show = ShowUtils.newShow(1);

        when(showService.get(anyInt())).thenReturn(showEntity);

        showFacade.duplicate(show);

        verify(showService).get(1);
        verify(showService).duplicate(showEntity);
        verify(showValidator).validateShowWithId(show);
        verifyNoMoreInteractions(showService, showValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        showFacade.duplicate(null);
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        showFacade.duplicate(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#duplicate(Show)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.duplicate(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)}.
     */
    @Test
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Show showEntity = ShowUtils.newShowDomain(2);
        final List<cz.vhromada.catalog.domain.Show> shows = CollectionUtils.newList(ShowUtils.newShowDomain(1), showEntity);
        final Show show = ShowUtils.newShow(2);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(showService.getAll()).thenReturn(shows);

        showFacade.moveUp(show);

        verify(showService).get(2);
        verify(showService).getAll();
        verify(showService).moveUp(showEntity);
        verify(showValidator).validateShowWithId(show);
        verifyNoMoreInteractions(showService, showValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        showFacade.moveUp(null);
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        showFacade.moveUp(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.moveUp(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveUp(Show)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Show showEntity = ShowUtils.newShowDomain(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Show> shows = CollectionUtils.newList(showEntity, ShowUtils.newShowDomain(1));
        final Show show = ShowUtils.newShow(Integer.MAX_VALUE);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(showService.getAll()).thenReturn(shows);

        showFacade.moveUp(show);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)}.
     */
    @Test
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Show showEntity = ShowUtils.newShowDomain(1);
        final List<cz.vhromada.catalog.domain.Show> shows = CollectionUtils.newList(showEntity, ShowUtils.newShowDomain(2));
        final Show show = ShowUtils.newShow(1);

        when(showService.get(anyInt())).thenReturn(showEntity);
        when(showService.getAll()).thenReturn(shows);

        showFacade.moveDown(show);

        verify(showService).get(1);
        verify(showService).getAll();
        verify(showService).moveDown(showEntity);
        verify(showValidator).validateShowWithId(show);
        verifyNoMoreInteractions(showService, showValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        showFacade.moveDown(null);
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        showFacade.moveDown(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        showFacade.moveDown(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ShowFacade#moveDown(Show)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Show showEntity = ShowUtils.newShowDomain(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Show> shows = CollectionUtils.newList(ShowUtils.newShowDomain(1), showEntity);
        final Show show = ShowUtils.newShow(Integer.MAX_VALUE);

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
        verifyZeroInteractions(genreService, converter, showValidator);
    }

    /**
     * Test method for {@link ShowFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final List<cz.vhromada.catalog.domain.Show> showList = CollectionUtils.newList(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2));
        int expectedTotalLength = 0;
        for (final cz.vhromada.catalog.domain.Show show : showList) {
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
        verifyZeroInteractions(genreService, converter, showValidator);
    }

    /**
     * Test method for {@link ShowFacade#getSeasonsCount()}.
     */
    @Test
    public void testGetSeasonsCount() {
        final cz.vhromada.catalog.domain.Show show1 = ShowUtils.newShowWithSeasons(1);
        final cz.vhromada.catalog.domain.Show show2 = ShowUtils.newShowWithSeasons(2);
        final int expectedSeasons = show1.getSeasons().size() + show2.getSeasons().size();

        when(showService.getAll()).thenReturn(CollectionUtils.newList(show1, show2));

        assertEquals(expectedSeasons, showFacade.getSeasonsCount());

        verify(showService).getAll();
        verifyNoMoreInteractions(showService);
        verifyZeroInteractions(genreService, converter, showValidator);
    }

    /**
     * Test method for {@link ShowFacade#getEpisodesCount()}.
     */
    @Test
    public void testGetEpisodesCount() {
        final List<cz.vhromada.catalog.domain.Show> showList = CollectionUtils.newList(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2));
        int expectedEpisodes = 0;
        for (final cz.vhromada.catalog.domain.Show show : showList) {
            for (final Season season : show.getSeasons()) {
                expectedEpisodes += season.getEpisodes().size();
            }
        }

        when(showService.getAll()).thenReturn(showList);

        assertEquals(expectedEpisodes, showFacade.getEpisodesCount());

        verify(showService).getAll();
        verifyNoMoreInteractions(showService);
        verifyZeroInteractions(genreService, converter, showValidator);
    }

}
