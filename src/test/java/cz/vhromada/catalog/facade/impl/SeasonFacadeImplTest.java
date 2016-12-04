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

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.validator.SeasonValidator;
import cz.vhromada.catalog.validator.ShowValidator;
import cz.vhromada.converters.Converter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link SeasonFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SeasonFacadeImplTest {

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<cz.vhromada.catalog.domain.Show> showService;

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
     * Instance of {@link SeasonValidator}
     */
    @Mock
    private SeasonValidator seasonValidator;

    /**
     * Instance of (@link SeasonFacade}
     */
    private SeasonFacade seasonFacade;

    /**
     * Initializes facade for seasons.
     */
    @Before
    public void setUp() {
        seasonFacade = new SeasonFacadeImpl(showService, converter, showValidator, seasonValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, ShowValidator, SeasonValidator)} with null service for
     * shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowService() {
        new SeasonFacadeImpl(null, converter, showValidator, seasonValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, ShowValidator, SeasonValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new SeasonFacadeImpl(showService, null, showValidator, seasonValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, ShowValidator, SeasonValidator)} with null validator for TO
     * for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowTOValidator() {
        new SeasonFacadeImpl(showService, converter, null, seasonValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, ShowValidator, SeasonValidator)} with null validator for TO
     * for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSeasonTOValidator() {
        new SeasonFacadeImpl(showService, converter, showValidator, null);
    }

    /**
     * Test method for {@link SeasonFacade#getSeason(Integer)} with existing season.
     */
    @Test
    public void testGetSeason_ExistingSeason() {
        final Season expectedSeason = SeasonUtils.newSeason(1);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(cz.vhromada.catalog.domain.Season.class), eq(Season.class))).thenReturn(expectedSeason);

        final Season season = seasonFacade.getSeason(1);

        assertNotNull(season);
        assertEquals(expectedSeason, season);

        verify(showService).getAll();
        verify(converter).convert(SeasonUtils.newSeasonDomain(1), Season.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(showValidator, seasonValidator);
    }

    /**
     * Test method for {@link SeasonFacade#getSeason(Integer)} with not existing season.
     */
    @Test
    public void testGetSeason_NotExistingSeason() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(cz.vhromada.catalog.domain.Season.class), eq(Season.class))).thenReturn(null);

        assertNull(seasonFacade.getSeason(Integer.MAX_VALUE));

        verify(showService).getAll();
        verify(converter).convert(null, Season.class);
        verifyNoMoreInteractions(showService, converter);
    }

    /**
     * Test method for {@link SeasonFacade#getSeason(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSeason_NullArgument() {
        seasonFacade.getSeason(null);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)}.
     */
    @Test
    public void testAdd() {
        final Show show = ShowUtils.newShow(1);
        final Season season = SeasonUtils.newSeason(null);
        final cz.vhromada.catalog.domain.Season seasonEntity = SeasonUtils.newSeasonDomain(null);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Show> showArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Show.class);

        when(showService.get(anyInt())).thenReturn(ShowUtils.newShowWithSeasons(1));
        when(converter.convert(any(Season.class), eq(cz.vhromada.catalog.domain.Season.class))).thenReturn(seasonEntity);

        seasonFacade.add(show, season);

        verify(showService).get(show.getId());
        verify(showService).update(showArgumentCaptor.capture());
        verify(showValidator).validateShowWithId(show);
        verify(seasonValidator).validateNewSeason(season);
        verify(converter).convert(season, cz.vhromada.catalog.domain.Season.class);
        verifyNoMoreInteractions(showService, converter, showValidator, seasonValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, SeasonUtils.newSeasonWithEpisodes(1), seasonEntity), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with null TO for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullShowTO() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        seasonFacade.add(null, SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with null TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonTO() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateNewSeason(any(Season.class));

        seasonFacade.add(ShowUtils.newShow(1), null);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with TO for show with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadShowTO() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        seasonFacade.add(ShowUtils.newShow(1), SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with TO for season with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadSeasonTO() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateNewSeason(any(Season.class));

        seasonFacade.add(ShowUtils.newShow(1), SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        seasonFacade.add(ShowUtils.newShow(Integer.MAX_VALUE), SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)}.
     */
    @Test
    public void testUpdate() {
        final Season season = SeasonUtils.newSeason(1);
        final cz.vhromada.catalog.domain.Season seasonEntity = SeasonUtils.newSeasonWithEpisodes(1);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Show> showArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.update(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonValidator).validateExistingSeason(season);
        verifyNoMoreInteractions(showService, seasonValidator);
        verifyZeroInteractions(converter, showValidator);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShowWithSeasons(1), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateExistingSeason(any(Season.class));

        seasonFacade.update(null);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateExistingSeason(any(Season.class));

        seasonFacade.update(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.update(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)}.
     */
    @Test
    public void testRemove() {
        final Season season = SeasonUtils.newSeason(1);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Show> showArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.remove(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonValidator).validateSeasonWithId(season);
        verifyNoMoreInteractions(showService, seasonValidator);
        verifyZeroInteractions(converter, showValidator);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShowDomain(1), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        seasonFacade.remove(null);
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        seasonFacade.remove(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.remove(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)}.
     */
    @Test
    public void testDuplicate() {
        final Season season = SeasonUtils.newSeason(1);
        final cz.vhromada.catalog.domain.Season seasonEntity = SeasonUtils.newSeasonWithEpisodes(null);
        season.setPosition(0);
        final cz.vhromada.catalog.domain.Show expectedShow = newShowWithSeasons(1, SeasonUtils.newSeasonWithEpisodes(1), seasonEntity);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Show> showArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.duplicate(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonValidator).validateSeasonWithId(season);
        verifyNoMoreInteractions(showService, seasonValidator);
        verifyZeroInteractions(converter, showValidator);

        ShowUtils.assertShowDeepEquals(expectedShow, showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        seasonFacade.duplicate(null);
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        seasonFacade.duplicate(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.duplicate(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)}.
     */
    @Test
    public void testMoveUp() {
        final Season season = SeasonUtils.newSeason(2);
        final cz.vhromada.catalog.domain.Season expectedSeason1 = SeasonUtils.newSeasonDomain(1);
        expectedSeason1.setPosition(1);
        final cz.vhromada.catalog.domain.Season expectedSeason2 = SeasonUtils.newSeasonDomain(2);
        expectedSeason2.setPosition(0);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Show> showArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, SeasonUtils.newSeasonDomain(1), SeasonUtils.newSeasonDomain(2))));

        seasonFacade.moveUp(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonValidator).validateSeasonWithId(season);
        verifyNoMoreInteractions(showService, seasonValidator);
        verifyZeroInteractions(converter, showValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, expectedSeason1, expectedSeason2), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        seasonFacade.moveUp(null);
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        seasonFacade.moveUp(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.moveUp(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, SeasonUtils.newSeasonDomain(1), SeasonUtils.newSeasonDomain(2))));

        seasonFacade.moveUp(SeasonUtils.newSeason(1));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)}.
     */
    @Test
    public void testMoveDown() {
        final Season season = SeasonUtils.newSeason(1);
        final cz.vhromada.catalog.domain.Season expectedSeason1 = SeasonUtils.newSeasonDomain(1);
        expectedSeason1.setPosition(1);
        final cz.vhromada.catalog.domain.Season expectedSeason2 = SeasonUtils.newSeasonDomain(2);
        expectedSeason2.setPosition(0);
        final ArgumentCaptor<cz.vhromada.catalog.domain.Show> showArgumentCaptor = ArgumentCaptor.forClass(cz.vhromada.catalog.domain.Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, SeasonUtils.newSeasonDomain(1), SeasonUtils.newSeasonDomain(2))));

        seasonFacade.moveDown(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonValidator).validateSeasonWithId(season);
        verifyNoMoreInteractions(showService, seasonValidator);
        verifyZeroInteractions(converter, showValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, expectedSeason1, expectedSeason2), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        seasonFacade.moveDown(null);
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        seasonFacade.moveDown(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.moveDown(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, SeasonUtils.newSeasonDomain(1), SeasonUtils.newSeasonDomain(2))));

        seasonFacade.moveDown(SeasonUtils.newSeason(2));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(Show)}.
     */
    @Test
    public void testFindSeasonsByShow() {
        final Show show = ShowUtils.newShow(1);
        final List<Season> expectedSeasons = CollectionUtils.newList(SeasonUtils.newSeason(1));

        when(showService.get(anyInt())).thenReturn(ShowUtils.newShowWithSeasons(1));
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Season.class), eq(Season.class))).thenReturn(expectedSeasons);

        final List<Season> seasons = seasonFacade.findSeasonsByShow(show);

        assertNotNull(seasons);
        assertEquals(expectedSeasons, seasons);

        verify(showService).get(show.getId());
        verify(converter).convertCollection(CollectionUtils.newList(SeasonUtils.newSeasonDomain(1)), Season.class);
        verify(showValidator).validateShowWithId(show);
        verifyNoMoreInteractions(showService, converter, showValidator);
        verifyZeroInteractions(seasonValidator);
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        seasonFacade.findSeasonsByShow(null);
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(Show)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_BadArgument() {
        doThrow(IllegalArgumentException.class).when(showValidator).validateShowWithId(any(Show.class));

        seasonFacade.findSeasonsByShow(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(Show)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        seasonFacade.findSeasonsByShow(ShowUtils.newShow(Integer.MAX_VALUE));
    }

    /**
     * Returns show with shows.
     *
     * @param id      ID
     * @param seasons seasons
     * @return show with shows
     */
    private static cz.vhromada.catalog.domain.Show newShowWithSeasons(final Integer id, final cz.vhromada.catalog.domain.Season... seasons) {
        final cz.vhromada.catalog.domain.Show show = ShowUtils.newShowDomain(id);
        show.setSeasons(CollectionUtils.newList(seasons));

        return show;
    }

}
