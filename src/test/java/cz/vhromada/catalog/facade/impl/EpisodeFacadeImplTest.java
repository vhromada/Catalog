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
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.EpisodeValidator;
import cz.vhromada.catalog.validator.SeasonValidator;
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
     * Instance of {@link SeasonValidator}
     */
    @Mock
    private SeasonValidator seasonValidator;

    /**
     * Instance of {@link EpisodeValidator}
     */
    @Mock
    private EpisodeValidator episodeValidator;

    /**
     * Instance of (@link EpisodeFacade}
     */
    private EpisodeFacade episodeFacade;

    /**
     * Initializes facade for episodes.
     */
    @Before
    public void setUp() {
        episodeFacade = new EpisodeFacadeImpl(showService, converter, seasonValidator, episodeValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonValidator, EpisodeValidator)} with null service for
     * shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowService() {
        new EpisodeFacadeImpl(null, converter, seasonValidator, episodeValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonValidator, EpisodeValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new EpisodeFacadeImpl(showService, null, seasonValidator, episodeValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonValidator, EpisodeValidator)} with null validator for TO
     * for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSeasonTOValidator() {
        new EpisodeFacadeImpl(showService, converter, null, episodeValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonValidator, EpisodeValidator)} with null validator for TO
     * for episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEpisodeTOValidator() {
        new EpisodeFacadeImpl(showService, converter, seasonValidator, null);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with existing episode.
     */
    @Test
    public void testGetEpisode_ExistingEpisode() {
        final Episode expectedEpisode = EpisodeUtils.newEpisodeTO(1);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(cz.vhromada.catalog.domain.Episode.class), eq(Episode.class))).thenReturn(expectedEpisode);

        final Episode episode = episodeFacade.getEpisode(1);

        assertNotNull(episode);
        assertEquals(expectedEpisode, episode);

        verify(showService).getAll();
        verify(converter).convert(EpisodeUtils.newEpisode(1), Episode.class);
        verifyNoMoreInteractions(showService, converter);
        verifyZeroInteractions(seasonValidator, episodeValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with not existing episode.
     */
    @Test
    public void testGetEpisode_NotExistingEpisode() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(cz.vhromada.catalog.domain.Episode.class), eq(Episode.class))).thenReturn(null);

        assertNull(episodeFacade.getEpisode(Integer.MAX_VALUE));

        verify(showService).getAll();
        verify(converter).convert(null, Episode.class);
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
     * Test method for {@link EpisodeFacade#add(Season, Episode)}.
     */
    @Test
    public void testAdd() {
        final Season season = SeasonUtils.newSeasonTO(1);
        final Episode episode = EpisodeUtils.newEpisodeTO(null);
        final cz.vhromada.catalog.domain.Episode episodeEntity = EpisodeUtils.newEpisode(null);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(Episode.class), eq(cz.vhromada.catalog.domain.Episode.class))).thenReturn(episodeEntity);

        episodeFacade.add(season, episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(seasonValidator).validateSeasonWithId(season);
        verify(episodeValidator).validateNewEpisode(episode);
        verify(converter).convert(episode, cz.vhromada.catalog.domain.Episode.class);
        verifyNoMoreInteractions(showService, converter, seasonValidator, episodeValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), episodeEntity), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with null TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonTO() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        episodeFacade.add(null, EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with null TO for episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullEpisodeTO() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateNewEpisode(any(Episode.class));

        episodeFacade.add(SeasonUtils.newSeasonTO(1), null);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with TO for season with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadSeasonTO() {
        doThrow(ValidationException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        episodeFacade.add(SeasonUtils.newSeasonTO(1), EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with TO for episode with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadEpisodeTO() {
        doThrow(ValidationException.class).when(episodeValidator).validateNewEpisode(any(Episode.class));

        episodeFacade.add(SeasonUtils.newSeasonTO(1), EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.add(SeasonUtils.newSeasonTO(Integer.MAX_VALUE), EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)}.
     */
    @Test
    public void testUpdate() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        final cz.vhromada.catalog.domain.Episode episodeEntity = EpisodeUtils.newEpisode(1);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(Episode.class), eq(cz.vhromada.catalog.domain.Episode.class))).thenReturn(episodeEntity);

        episodeFacade.update(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(converter).convert(episode, cz.vhromada.catalog.domain.Episode.class);
        verify(episodeValidator).validateExistingEpisode(episode);
        verifyNoMoreInteractions(showService, converter, episodeValidator);
        verifyZeroInteractions(seasonValidator);

        ShowUtils.assertShowDeepEquals(ShowUtils.newShowWithSeasons(1), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateExistingEpisode(any(Episode.class));

        episodeFacade.update(null);
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(episodeValidator).validateExistingEpisode(any(Episode.class));

        episodeFacade.update(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.update(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)}.
     */
    @Test
    public void testRemove() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        final Show expectedShow = ShowUtils.newShow(1);
        expectedShow.setSeasons(CollectionUtils.newList(SeasonUtils.newSeason(1)));
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.remove(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(episodeValidator).validateEpisodeWithId(episode);
        verifyNoMoreInteractions(showService, episodeValidator);
        verifyZeroInteractions(converter, seasonValidator);

        ShowUtils.assertShowDeepEquals(expectedShow, showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.remove(null);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.remove(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.remove(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)}.
     */
    @Test
    public void testDuplicate() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        final cz.vhromada.catalog.domain.Episode episodeEntity = EpisodeUtils.newEpisode(null);
        episode.setPosition(0);
        final Show expectedShow = newShowWithSeasons(1, EpisodeUtils.newEpisode(1), episodeEntity);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.duplicate(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(episodeValidator).validateEpisodeWithId(episode);
        verifyNoMoreInteractions(showService, episodeValidator);
        verifyZeroInteractions(converter, seasonValidator);

        ShowUtils.assertShowDeepEquals(expectedShow, showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.duplicate(null);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.duplicate(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.duplicate(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)}.
     */
    @Test
    public void testMoveUp() {
        final Episode episode = EpisodeUtils.newEpisodeTO(2);
        final cz.vhromada.catalog.domain.Episode expectedEpisode1 = EpisodeUtils.newEpisode(1);
        expectedEpisode1.setPosition(1);
        final cz.vhromada.catalog.domain.Episode expectedEpisode2 = EpisodeUtils.newEpisode(2);
        expectedEpisode2.setPosition(0);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), EpisodeUtils.newEpisode(2))));

        episodeFacade.moveUp(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(episodeValidator).validateEpisodeWithId(episode);
        verifyNoMoreInteractions(showService, episodeValidator);
        verifyZeroInteractions(converter, seasonValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, expectedEpisode1, expectedEpisode2), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.moveUp(null);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), EpisodeUtils.newEpisode(2))));

        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(1));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)}.
     */
    @Test
    public void testMoveDown() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        final cz.vhromada.catalog.domain.Episode expectedEpisode1 = EpisodeUtils.newEpisode(1);
        expectedEpisode1.setPosition(1);
        final cz.vhromada.catalog.domain.Episode expectedEpisode2 = EpisodeUtils.newEpisode(2);
        expectedEpisode2.setPosition(0);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), EpisodeUtils.newEpisode(2))));

        episodeFacade.moveDown(episode);

        verify(showService).getAll();
        verify(showService).update(showArgumentCaptor.capture());
        verify(episodeValidator).validateEpisodeWithId(episode);
        verifyNoMoreInteractions(showService, episodeValidator);
        verifyZeroInteractions(converter, seasonValidator);

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, expectedEpisode1, expectedEpisode2), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.moveDown(null);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisode(1), EpisodeUtils.newEpisode(2))));

        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(2));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)}.
     */
    @Test
    public void testFindEpisodesBySeason() {
        final Season season = SeasonUtils.newSeasonTO(1);
        final List<Episode> expectedEpisodes = CollectionUtils.newList(EpisodeUtils.newEpisodeTO(1));

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Episode.class), eq(Episode.class))).thenReturn(expectedEpisodes);

        final List<Episode> episodes = episodeFacade.findEpisodesBySeason(season);

        assertNotNull(episodes);
        assertEquals(expectedEpisodes, episodes);

        verify(showService).getAll();
        verify(converter).convertCollection(CollectionUtils.newList(EpisodeUtils.newEpisode(1)), Episode.class);
        verify(seasonValidator).validateSeasonWithId(season);
        verifyNoMoreInteractions(showService, converter, seasonValidator);
        verifyZeroInteractions(episodeValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_NullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        episodeFacade.findEpisodesBySeason(null);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testFindEpisodesBySeason_BadArgument() {
        doThrow(ValidationException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)} with not existing argument.
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
    private static Show newShowWithSeasons(final Integer id, final cz.vhromada.catalog.domain.Episode... episodes) {
        final cz.vhromada.catalog.domain.Season season = SeasonUtils.newSeason(id);
        season.setEpisodes(CollectionUtils.newList(episodes));

        final Show show = ShowUtils.newShow(id);
        show.setSeasons(CollectionUtils.newList(season));

        return show;
    }

}
