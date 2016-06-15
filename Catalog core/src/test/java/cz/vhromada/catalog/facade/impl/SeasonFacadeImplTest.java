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

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SeasonUtils;
import cz.vhromada.catalog.commons.ShowUtils;
import cz.vhromada.catalog.entities.Season;
import cz.vhromada.catalog.entities.Show;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.ShowTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.facade.validators.ShowTOValidator;
import cz.vhromada.catalog.service.CatalogService;
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
    private CatalogService<Show> showService;

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
     * Instance of {@link SeasonTOValidator}
     */
    @Mock
    private SeasonTOValidator seasonTOValidator;

    /**
     * Instance of (@link SeasonFacade}
     */
    private SeasonFacade seasonFacade;

    /**
     * Initializes facade for seasons.
     */
    @Before
    public void setUp() {
        seasonFacade = new SeasonFacadeImpl(showService, converter, showTOValidator, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, ShowTOValidator, SeasonTOValidator)} with null service for
     * shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowService() {
        new SeasonFacadeImpl(null, converter, showTOValidator, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, ShowTOValidator, SeasonTOValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new SeasonFacadeImpl(showService, null, showTOValidator, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, ShowTOValidator, SeasonTOValidator)} with null validator for TO
     * for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowTOValidator() {
        new SeasonFacadeImpl(showService, converter, null, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, ShowTOValidator, SeasonTOValidator)} with null validator for TO
     * for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSeasonTOValidator() {
        new SeasonFacadeImpl(showService, converter, showTOValidator, null);
    }

    /**
     * Test method for {@link SeasonFacade#getSeason(Integer)} with existing season.
     */
    @Test
    public void testGetSeason_ExistingSeason() {
        final SeasonTO expectedSeason = SeasonUtils.newSeasonTO(1);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(Season.class), eq(SeasonTO.class))).thenReturn(expectedSeason);

        final SeasonTO season = seasonFacade.getSeason(1);

        assertNotNull(season);
        assertEquals(expectedSeason, season);

        verify(showService).getAll();
        verify(converter).convert(SeasonUtils.newSeason(1), SeasonTO.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(showTOValidator, seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacade#getSeason(Integer)} with not existing season.
     */
    @Test
    public void testGetSeason_NotExistingSeason() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(Season.class), eq(SeasonTO.class))).thenReturn(null);

        assertNull(seasonFacade.getSeason(Integer.MAX_VALUE));

        verify(showService).getAll();
        verify(converter).convert(null, SeasonTO.class);
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
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)}.
     */
    @Test
    public void testAdd() {
        final ShowTO show = ShowUtils.newShowTO(1);
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        final Season seasonEntity = SeasonUtils.newSeason(null);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.get(anyInt())).thenReturn(ShowUtils.newShowWithSeasons(1));
        when(converter.convert(any(SeasonTO.class), eq(Season.class))).thenReturn(seasonEntity);

        seasonFacade.add(show, season);

        verify(showService).get(show.getId());
        verify(showService).update(showArgumentCaptor.capture());
        verify(showTOValidator).validateShowTOWithId(show);
        verify(seasonTOValidator).validateNewSeasonTO(season);
        verify(converter).convert(season, Season.class);
        verifyNoMoreInteractions(showService, converter, showTOValidator, seasonTOValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, SeasonUtils.newSeasonWithEpisodes(1), seasonEntity), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with null TO for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullShowTO() {
        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        seasonFacade.add(null, SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with null TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonTO() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateNewSeasonTO(any(SeasonTO.class));

        seasonFacade.add(ShowUtils.newShowTO(1), null);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with TO for show with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadShowTO() {
        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        seasonFacade.add(ShowUtils.newShowTO(1), SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with TO for season with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadSeasonTO() {
        doThrow(ValidationException.class).when(seasonTOValidator).validateNewSeasonTO(any(SeasonTO.class));

        seasonFacade.add(ShowUtils.newShowTO(1), SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        seasonFacade.add(ShowUtils.newShowTO(Integer.MAX_VALUE), SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)}.
     */
    @Test
    public void testUpdate() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        final Season seasonEntity = SeasonUtils.newSeasonWithEpisodes(1);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.update(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonTOValidator).validateExistingSeasonTO(season);
        verifyNoMoreInteractions(showService, seasonTOValidator);
        verifyZeroInteractions(converter, showTOValidator);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShowWithSeasons(1), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateExistingSeasonTO(any(SeasonTO.class));

        seasonFacade.update(null);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(seasonTOValidator).validateExistingSeasonTO(any(SeasonTO.class));

        seasonFacade.update(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.update(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#remove(SeasonTO)}.
     */
    @Test
    public void testRemove() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.remove(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(showService, seasonTOValidator);
        verifyZeroInteractions(converter, showTOValidator);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShow(1), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#remove(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        seasonFacade.remove(null);
    }

    /**
     * Test method for {@link SeasonFacade#remove(SeasonTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        seasonFacade.remove(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#remove(SeasonTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.remove(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(SeasonTO)}.
     */
    @Test
    public void testDuplicate() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        final Season seasonEntity = SeasonUtils.newSeasonWithEpisodes(null);
        season.setPosition(0);
        final Show expectedShow = newShowWithSeasons(1, SeasonUtils.newSeasonWithEpisodes(1), seasonEntity);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.duplicate(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(showService, seasonTOValidator);
        verifyZeroInteractions(converter, showTOValidator);

        ShowUtils.assertShowDeepEquals(expectedShow, showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        seasonFacade.duplicate(null);
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        seasonFacade.duplicate(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.duplicate(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)}.
     */
    @Test
    public void testMoveUp() {
        final SeasonTO season = SeasonUtils.newSeasonTO(2);
        final Season expectedSeason1 = SeasonUtils.newSeason(1);
        expectedSeason1.setPosition(1);
        final Season expectedSeason2 = SeasonUtils.newSeason(2);
        expectedSeason2.setPosition(0);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, SeasonUtils.newSeason(1), SeasonUtils.newSeason(2))));

        seasonFacade.moveUp(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(showService, seasonTOValidator);
        verifyZeroInteractions(converter, showTOValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, expectedSeason1, expectedSeason2), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        seasonFacade.moveUp(null);
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        seasonFacade.moveUp(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.moveUp(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, SeasonUtils.newSeason(1), SeasonUtils.newSeason(2))));

        seasonFacade.moveUp(SeasonUtils.newSeasonTO(1));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)}.
     */
    @Test
    public void testMoveDown() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        final Season expectedSeason1 = SeasonUtils.newSeason(1);
        expectedSeason1.setPosition(1);
        final Season expectedSeason2 = SeasonUtils.newSeason(2);
        expectedSeason2.setPosition(0);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, SeasonUtils.newSeason(1), SeasonUtils.newSeason(2))));

        seasonFacade.moveDown(season);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(showService, seasonTOValidator);
        verifyZeroInteractions(converter, showTOValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, expectedSeason1, expectedSeason2), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        seasonFacade.moveDown(null);
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        seasonFacade.moveDown(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        seasonFacade.moveDown(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, SeasonUtils.newSeason(1), SeasonUtils.newSeason(2))));

        seasonFacade.moveDown(SeasonUtils.newSeasonTO(2));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)}.
     */
    @Test
    public void testFindSeasonsByShow() {
        final ShowTO show = ShowUtils.newShowTO(1);
        final List<SeasonTO> expectedSeasons = CollectionUtils.newList(SeasonUtils.newSeasonTO(1));

        when(showService.get(anyInt())).thenReturn(ShowUtils.newShowWithSeasons(1));
        when(converter.convertCollection(anyListOf(Season.class), eq(SeasonTO.class))).thenReturn(expectedSeasons);

        final List<SeasonTO> seasons = seasonFacade.findSeasonsByShow(show);

        assertNotNull(seasons);
        assertEquals(expectedSeasons, seasons);

        verify(showService).get(show.getId());
        verify(converter).convertCollection(CollectionUtils.newList(SeasonUtils.newSeason(1)), SeasonTO.class);
        verify(showTOValidator).validateShowTOWithId(show);
        verifyNoMoreInteractions(showService, converter, showTOValidator);
        verifyZeroInteractions(seasonTOValidator);
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_NullArgument() {
        doThrow(IllegalArgumentException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        seasonFacade.findSeasonsByShow(null);
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testFindSeasonsByShow_BadArgument() {
        doThrow(ValidationException.class).when(showTOValidator).validateShowTOWithId(any(ShowTO.class));

        seasonFacade.findSeasonsByShow(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testFindSeasonsByShow_NotExistingArgument() {
        when(showService.get(anyInt())).thenReturn(null);

        seasonFacade.findSeasonsByShow(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

    /**
     * Returns show with shows.
     *
     * @param id      ID
     * @param seasons seasons
     * @return show with shows
     */
    private static Show newShowWithSeasons(final Integer id, final Season... seasons) {
        final Show show = ShowUtils.newShow(id);
        show.setSeasons(CollectionUtils.newList(seasons));

        return show;
    }

}
