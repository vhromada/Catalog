package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.EpisodeUtils;
import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.common.ShowUtils;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.EpisodeTO;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.EpisodeTOValidator;
import cz.vhromada.catalog.validator.SeasonTOValidator;
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
 * A class represents test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class EpisodeFacadeImplTest {

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
     * Instance of {@link SeasonTOValidator}
     */
    @Mock
    private SeasonTOValidator seasonTOValidator;

    /**
     * Instance of {@link EpisodeTOValidator}
     */
    @Mock
    private EpisodeTOValidator episodeTOValidator;

    /**
     * Instance of (@link EpisodeFacade}
     */
    private EpisodeFacade episodeFacade;

    /**
     * Initializes facade for episodes.
     */
    @Before
    public void setUp() {
        episodeFacade = new EpisodeFacadeImpl(showService, converter, seasonTOValidator, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonTOValidator, EpisodeTOValidator)} with null service for
     * shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowService() {
        new EpisodeFacadeImpl(null, converter, seasonTOValidator, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonTOValidator, EpisodeTOValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new EpisodeFacadeImpl(showService, null, seasonTOValidator, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonTOValidator, EpisodeTOValidator)} with null validator for TO
     * for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSeasonTOValidator() {
        new EpisodeFacadeImpl(showService, converter, null, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonTOValidator, EpisodeTOValidator)} with null validator for TO
     * for episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEpisodeTOValidator() {
        new EpisodeFacadeImpl(showService, converter, seasonTOValidator, null);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with existing episode.
     */
    @Test
    public void testGetEpisode_ExistingEpisode() {
        final EpisodeTO expectedEpisode = EpisodeUtils.newEpisodeTO(1);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(Episode.class), eq(EpisodeTO.class))).thenReturn(expectedEpisode);

        final EpisodeTO episode = episodeFacade.getEpisode(1);

        assertNotNull(episode);
        assertEquals(expectedEpisode, episode);

        verify(showService).getAll();
        verify(converter).convert(EpisodeUtils.newEpisode(1), EpisodeTO.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(seasonTOValidator, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with not existing episode.
     */
    @Test
    public void testGetEpisode_NotExistingEpisode() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(Episode.class), eq(EpisodeTO.class))).thenReturn(null);

        assertNull(episodeFacade.getEpisode(Integer.MAX_VALUE));

        verify(showService).getAll();
        verify(converter).convert(null, EpisodeTO.class);
        verifyNoMoreInteractions(showService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEpisode_NullArgument() {
        episodeFacade.getEpisode(null);
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)}.
     */
    @Test
    public void testAdd() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        final Episode episodeEntity = EpisodeUtils.newEpisode(null);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episodeEntity);

        episodeFacade.add(season, episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verify(episodeTOValidator).validateNewEpisodeTO(episode);
        verify(converter).convert(episode, Episode.class);
        verifyNoMoreInteractions(showService, converter, seasonTOValidator, episodeTOValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), episodeEntity), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with null TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonTO() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        episodeFacade.add(null, EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with null TO for episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullEpisodeTO() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateNewEpisodeTO(any(EpisodeTO.class));

        episodeFacade.add(SeasonUtils.newSeasonTO(1), null);
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with TO for season with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadSeasonTO() {
        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        episodeFacade.add(SeasonUtils.newSeasonTO(1), EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with TO for episode with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadEpisodeTO() {
        doThrow(ValidationException.class).when(episodeTOValidator).validateNewEpisodeTO(any(EpisodeTO.class));

        episodeFacade.add(SeasonUtils.newSeasonTO(1), EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.add(SeasonUtils.newSeasonTO(Integer.MAX_VALUE), EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)}.
     */
    @Test
    public void testUpdate() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        final Episode episodeEntity = EpisodeUtils.newEpisode(1);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episodeEntity);

        episodeFacade.update(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(converter).convert(episode, Episode.class);
        verify(episodeTOValidator).validateExistingEpisodeTO(episode);
        verifyNoMoreInteractions(showService, converter, episodeTOValidator);
        verifyZeroInteractions(seasonTOValidator);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShowWithSeasons(1), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateExistingEpisodeTO(any(EpisodeTO.class));

        episodeFacade.update(null);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(episodeTOValidator).validateExistingEpisodeTO(any(EpisodeTO.class));

        episodeFacade.update(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.update(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)}.
     */
    @Test
    public void testRemove() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        final Show expectedShow = ShowUtils.newShow(1);
        expectedShow.setSeasons(CollectionUtils.newList(SeasonUtils.newSeason(1)));
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.remove(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(showService, episodeTOValidator);
        verifyZeroInteractions(converter, seasonTOValidator);

        ShowUtils.assertShowDeepEquals(expectedShow, showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        episodeFacade.remove(null);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        episodeFacade.remove(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.remove(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)}.
     */
    @Test
    public void testDuplicate() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        final Episode episodeEntity = EpisodeUtils.newEpisode(null);
        episode.setPosition(0);
        final Show expectedShow = newShowWithSeasons(1, EpisodeUtils.newEpisode(1), episodeEntity);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.duplicate(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(showService, episodeTOValidator);
        verifyZeroInteractions(converter, seasonTOValidator);

        ShowUtils.assertShowDeepEquals(expectedShow, showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        episodeFacade.duplicate(null);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        episodeFacade.duplicate(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.duplicate(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)}.
     */
    @Test
    public void testMoveUp() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(2);
        final Episode expectedEpisode1 = EpisodeUtils.newEpisode(1);
        expectedEpisode1.setPosition(1);
        final Episode expectedEpisode2 = EpisodeUtils.newEpisode(2);
        expectedEpisode2.setPosition(0);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), EpisodeUtils.newEpisode(2))));

        episodeFacade.moveUp(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(showService, episodeTOValidator);
        verifyZeroInteractions(converter, seasonTOValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, expectedEpisode1, expectedEpisode2), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        episodeFacade.moveUp(null);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), EpisodeUtils.newEpisode(2))));

        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(1));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)}.
     */
    @Test
    public void testMoveDown() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        final Episode expectedEpisode1 = EpisodeUtils.newEpisode(1);
        expectedEpisode1.setPosition(1);
        final Episode expectedEpisode2 = EpisodeUtils.newEpisode(2);
        expectedEpisode2.setPosition(0);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), EpisodeUtils.newEpisode(2))));

        episodeFacade.moveDown(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(showService, episodeTOValidator);
        verifyZeroInteractions(converter, seasonTOValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, expectedEpisode1, expectedEpisode2), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        episodeFacade.moveDown(null);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), EpisodeUtils.newEpisode(2))));

        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(2));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)}.
     */
    @Test
    public void testFindEpisodesBySeason() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        final List<EpisodeTO> expectedEpisodes = CollectionUtils.newList(EpisodeUtils.newEpisodeTO(1));

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convertCollection(anyListOf(Episode.class), eq(EpisodeTO.class))).thenReturn(expectedEpisodes);

        final List<EpisodeTO> episodes = episodeFacade.findEpisodesBySeason(season);

        assertNotNull(episodes);
        assertEquals(expectedEpisodes, episodes);

        verify(showService).getAll();
        verify(converter).convertCollection(CollectionUtils.newList(EpisodeUtils.newEpisode(1)), EpisodeTO.class);
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(showService, converter, seasonTOValidator);
        verifyZeroInteractions(episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        episodeFacade.findEpisodesBySeason(null);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testFindEpisodesBySeason_BadArgument() {
        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testFindEpisodesBySeason_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Returns show with seasons.
     *
     * @param id       ID
     * @param episodes episodes
     * @return show with seasons
     */
    private static Show newShowWithSeasons(final Integer id, final Episode... episodes) {
        final Season season = SeasonUtils.newSeason(id);
        season.setEpisodes(CollectionUtils.newList(episodes));

        final Show show = ShowUtils.newShow(id);
        show.setSeasons(CollectionUtils.newList(season));

        return show;
    }

}
