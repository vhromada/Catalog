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

import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.validator.EpisodeValidator;
import cz.vhromada.catalog.validator.SeasonValidator;
import cz.vhromada.converters.Converter;

import org.junit.Before;
import org.junit.Ignore;
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
@Ignore
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
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonValidator, EpisodeValidator)} with null service for shows.
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
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonValidator, EpisodeValidator)} with null validator for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSeasonEpisodeValidator() {
        new EpisodeFacadeImpl(showService, converter, null, episodeValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, SeasonValidator, EpisodeValidator)} with null validator for
     * episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEpisodeValidator() {
        new EpisodeFacadeImpl(showService, converter, seasonValidator, null);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with existing episode.
     */
    @Test
    public void testGetEpisode_ExistingEpisode() {
        final Episode expectedEpisode = EpisodeUtils.newEpisode(1);

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convert(any(cz.vhromada.catalog.domain.Episode.class), eq(Episode.class))).thenReturn(expectedEpisode);

        final Episode episode = episodeFacade.getEpisode(1);

        assertNotNull(episode);
        assertEquals(expectedEpisode, episode);

        verify(showService).getAll();
        verify(converter).convert(EpisodeUtils.newEpisodeDomain(1), Episode.class);
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
        final Season season = SeasonUtils.newSeason(1);
        final Episode episode = EpisodeUtils.newEpisode(null);
        final cz.vhromada.catalog.domain.Episode episodeEntity = EpisodeUtils.newEpisodeDomain(null);
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

        ShowUtils.assertShowDeepEquals(newShowWithSeasons(1, EpisodeUtils.newEpisodeDomain(1), episodeEntity), showArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with null season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonEpisode() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        episodeFacade.add(null, EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with null episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullEpisode() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateNewEpisode(any(Episode.class));

        episodeFacade.add(SeasonUtils.newSeason(1), null);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with season with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadSeasonEpisode() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        episodeFacade.add(SeasonUtils.newSeason(1), EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadEpisode() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateNewEpisode(any(Episode.class));

        episodeFacade.add(SeasonUtils.newSeason(1), EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.add(SeasonUtils.newSeason(Integer.MAX_VALUE), EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)}.
     */
    @Test
    public void testUpdate() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        final cz.vhromada.catalog.domain.Episode episodeEntity = EpisodeUtils.newEpisodeDomain(1);
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
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateExistingEpisode(any(Episode.class));

        episodeFacade.update(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.update(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)}.
     */
    @Test
    public void testRemove() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        final Show expectedShow = ShowUtils.newShowDomain(1);
        expectedShow.setSeasons(CollectionUtils.newList(SeasonUtils.newSeasonDomain(1)));
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
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.remove(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.remove(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)}.
     */
    @Test
    public void testDuplicate() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        final cz.vhromada.catalog.domain.Episode episodeEntity = EpisodeUtils.newEpisodeDomain(null);
        episode.setPosition(0);
        final Show expectedShow = newShowWithSeasons(1, EpisodeUtils.newEpisodeDomain(1), episodeEntity);
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
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.duplicate(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.duplicate(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)}.
     */
    @Test
    public void testMoveUp() {
        final Episode episode = EpisodeUtils.newEpisode(2);
        final cz.vhromada.catalog.domain.Episode expectedEpisode1 = EpisodeUtils.newEpisodeDomain(1);
        expectedEpisode1.setPosition(1);
        final cz.vhromada.catalog.domain.Episode expectedEpisode2 = EpisodeUtils.newEpisodeDomain(2);
        expectedEpisode2.setPosition(0);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll())
                .thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisodeDomain(1), EpisodeUtils.newEpisodeDomain(2))));

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
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.moveUp(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.moveUp(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        when(showService.getAll())
                .thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisodeDomain(1), EpisodeUtils.newEpisodeDomain(2))));

        episodeFacade.moveUp(EpisodeUtils.newEpisode(1));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)}.
     */
    @Test
    public void testMoveDown() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        final cz.vhromada.catalog.domain.Episode expectedEpisode1 = EpisodeUtils.newEpisodeDomain(1);
        expectedEpisode1.setPosition(1);
        final cz.vhromada.catalog.domain.Episode expectedEpisode2 = EpisodeUtils.newEpisodeDomain(2);
        expectedEpisode2.setPosition(0);
        final ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(Show.class);

        when(showService.getAll())
                .thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisodeDomain(1), EpisodeUtils.newEpisodeDomain(2))));

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
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadArgument() {
        doThrow(IllegalArgumentException.class).when(episodeValidator).validateEpisodeWithId(any(Episode.class));

        episodeFacade.moveDown(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.moveDown(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        when(showService.getAll())
                .thenReturn(CollectionUtils.newList(newShowWithSeasons(1, EpisodeUtils.newEpisodeDomain(1), EpisodeUtils.newEpisodeDomain(2))));

        episodeFacade.moveDown(EpisodeUtils.newEpisode(2));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)}.
     */
    @Test
    public void testFindEpisodesBySeason() {
        final Season season = SeasonUtils.newSeason(1);
        final List<Episode> expectedEpisodes = CollectionUtils.newList(EpisodeUtils.newEpisode(1));

        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Episode.class), eq(Episode.class))).thenReturn(expectedEpisodes);

        final List<Episode> episodes = episodeFacade.findEpisodesBySeason(season);

        assertNotNull(episodes);
        assertEquals(expectedEpisodes, episodes);

        verify(showService).getAll();
        verify(converter).convertCollection(CollectionUtils.newList(EpisodeUtils.newEpisodeDomain(1)), Episode.class);
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
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_BadArgument() {
        doThrow(IllegalArgumentException.class).when(seasonValidator).validateSeasonWithId(any(Season.class));

        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_NotExistingArgument() {
        when(showService.getAll()).thenReturn(CollectionUtils.newList(ShowUtils.newShowWithSeasons(1)));

        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Returns show with seasons.
     *
     * @param id       ID
     * @param episodes episodes
     * @return show with seasons
     */
    private static Show newShowWithSeasons(final Integer id, final cz.vhromada.catalog.domain.Episode... episodes) {
        final cz.vhromada.catalog.domain.Season season = SeasonUtils.newSeasonDomain(id);
        season.setEpisodes(CollectionUtils.newList(episodes));

        final Show show = ShowUtils.newShowDomain(id);
        show.setSeasons(CollectionUtils.newList(season));

        return show;
    }

}
