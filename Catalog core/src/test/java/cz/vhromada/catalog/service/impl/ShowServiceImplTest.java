//package cz.vhromada.catalog.service.impl;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoMoreInteractions;
//import static org.mockito.Mockito.verifyZeroInteractions;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cz.vhromada.catalog.commons.CollectionUtils;
//import cz.vhromada.catalog.commons.ObjectGeneratorTest;
//import cz.vhromada.catalog.commons.Time;
//import cz.vhromada.catalog.dao.EpisodeDAO;
//import cz.vhromada.catalog.dao.SeasonDAO;
//import cz.vhromada.catalog.dao.ShowDAO;
//import cz.vhromada.catalog.dao.entities.Episode;
//import cz.vhromada.catalog.dao.entities.Season;
//import cz.vhromada.catalog.dao.entities.Show;
//import cz.vhromada.catalog.dao.exceptions.DataStorageException;
//import cz.vhromada.catalog.service.ShowService;
//import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
//import cz.vhromada.test.DeepAsserts;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.springframework.cache.Cache;
//import org.springframework.cache.support.SimpleValueWrapper;
//
///**
// * A class represents test for class {@link ShowServiceImpl}.
// *
// * @author Vladimir Hromada
// */
//@RunWith(MockitoJUnitRunner.class)
//public class ShowServiceImplTest extends ObjectGeneratorTest {
//
//    /**
//     * Cache key for list of shows
//     */
//    private static final String SHOWS_CACHE_KEY = "shows";
//
//    /**
//     * Cache key for book show
//     */
//    private static final String SHOW_CACHE_KEY = "show";
//
//    /**
//     * Cache key for list of seasons
//     */
//    private static final String SEASONS_CACHE_KEY = "seasons";
//
//    /**
//     * Cache key for list of episodes
//     */
//    private static final String EPISODES_CACHE_KEY = "episodes";
//
//    /**
//     * Instance of {@link ShowDAO}
//     */
//    @Mock
//    private ShowDAO showDAO;
//
//    /**
//     * Instance of {@link SeasonDAO}
//     */
//    @Mock
//    private SeasonDAO seasonDAO;
//
//    /**
//     * Instance of {@link EpisodeDAO}
//     */
//    @Mock
//    private EpisodeDAO episodeDAO;
//
//    /**
//     * Instance of {@link Cache}
//     */
//    @Mock
//    private Cache showCache;
//
//    /**
//     * Instance of {@link ShowService}
//     */
//    private ShowService showService;
//
//    /**
//     * Initializes service for shows.
//     */
//    @Before
//    public void setUp() {
//        showService = new ShowServiceImpl(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowServiceImpl#ShowServiceImpl(ShowDAO, SeasonDAO, EpisodeDAO, Cache)} with null DAO for shows.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullShowDAO() {
//        new ShowServiceImpl(null, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowServiceImpl#ShowServiceImpl(ShowDAO, SeasonDAO, EpisodeDAO, Cache)} with null DAO for seasons.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullSeasonDAO() {
//        new ShowServiceImpl(showDAO, null, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowServiceImpl#ShowServiceImpl(ShowDAO, SeasonDAO, EpisodeDAO, Cache)} with null DAO for episodes.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullEpisodeDAO() {
//        new ShowServiceImpl(showDAO, seasonDAO, null, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowServiceImpl#ShowServiceImpl(ShowDAO, SeasonDAO, EpisodeDAO, Cache))} with null cache for shows.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorWithNullShowCache() {
//        new ShowServiceImpl(showDAO, seasonDAO, episodeDAO, null);
//    }
//
//    /**
//     * Test method for {@link ShowService#newData()} with cached data.
//     */
//    @Test
//    public void testNewDataWithCachedData() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
//        when(showCache.get(SHOWS_CACHE_KEY)).thenReturn(new SimpleValueWrapper(shows));
//        for (final Show show : shows) {
//            when(showCache.get(SEASONS_CACHE_KEY + show.getId())).thenReturn(new SimpleValueWrapper(seasons));
//        }
//        for (final Season season : seasons) {
//            when(showCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
//        }
//
//        showService.newData();
//
//        for (final Show show : shows) {
//            verify(showDAO).remove(show);
//            verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        }
//        for (final Season season : seasons) {
//            verify(seasonDAO, times(shows.size())).remove(season);
//            verify(showCache, times(shows.size())).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        for (final Episode episode : episodes) {
//            verify(episodeDAO, times(shows.size() * seasons.size())).remove(episode);
//        }
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#newData()} with not cached data.
//     */
//    @Test
//    public void testNewDataWithNotCachedData() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
//        when(showDAO.getShows()).thenReturn(shows);
//        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        showService.newData();
//
//        verify(showDAO).getShows();
//        for (final Show show : shows) {
//            verify(showDAO).remove(show);
//            verify(seasonDAO).findSeasonsByShow(show);
//            verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        }
//        for (final Season season : seasons) {
//            verify(seasonDAO, times(shows.size())).remove(season);
//            verify(episodeDAO, times(shows.size())).findEpisodesBySeason(season);
//            verify(showCache, times(shows.size())).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        for (final Episode episode : episodes) {
//            verify(episodeDAO, times(shows.size() * seasons.size())).remove(episode);
//        }
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#newData()} with exception in DAO tier.
//     */
//    @Test
//    public void testNewDataWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShows();
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.newData();
//            fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showDAO, showCache);
//        verifyZeroInteractions(seasonDAO, episodeDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShows()} with cached shows.
//     */
//    @Test
//    public void testGetShowsWithCachedShows() {
//        final List<Show> shows = CollectionUtils.newList(mock(Show.class), mock(Show.class));
//        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(shows));
//
//        DeepAsserts.assertEquals(shows, showService.getShows());
//
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showCache);
//        verifyZeroInteractions(showDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShows()} with not cached shows.
//     */
//    @Test
//    public void testGetShowsWithNotCachedShows() {
//        final List<Show> shows = CollectionUtils.newList(mock(Show.class), mock(Show.class));
//        when(showDAO.getShows()).thenReturn(shows);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        DeepAsserts.assertEquals(shows, showService.getShows());
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).put(SHOWS_CACHE_KEY, shows);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShows()} with exception in DAO tier.
//     */
//    @Test
//    public void testGetShowsWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShows();
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.getShows();
//            fail("Can't get shows with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShow(Integer)} with cached existing show.
//     */
//    @Test
//    public void testGetShowWithCachedExistingShow() {
//        final Show show = generate(Show.class);
//        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(show));
//
//        DeepAsserts.assertEquals(show, showService.getShow(show.getId()));
//
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verifyNoMoreInteractions(showCache);
//        verifyZeroInteractions(showDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShow(Integer)} with cached not existing show.
//     */
//    @Test
//    public void testGetShowWithCachedNotExistingShow() {
//        final Show show = generate(Show.class);
//        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));
//
//        assertNull(showService.getShow(show.getId()));
//
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verifyNoMoreInteractions(showCache);
//        verifyZeroInteractions(showDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShow(Integer)} with not cached existing show.
//     */
//    @Test
//    public void testGetShowWithNotCachedExistingShow() {
//        final Show show = generate(Show.class);
//        when(showDAO.getShow(anyInt())).thenReturn(show);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        DeepAsserts.assertEquals(show, showService.getShow(show.getId()));
//
//        verify(showDAO).getShow(show.getId());
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verify(showCache).put(SHOW_CACHE_KEY + show.getId(), show);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShow(Integer)} with not cached not existing show.
//     */
//    @Test
//    public void testGetShowWithNotCachedNotExistingShow() {
//        final Show show = generate(Show.class);
//        when(showDAO.getShow(anyInt())).thenReturn(null);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        assertNull(showService.getShow(show.getId()));
//
//        verify(showDAO).getShow(show.getId());
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verify(showCache).put(SHOW_CACHE_KEY + show.getId(), null);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShow(Integer)} with null argument.
//     */
//    @Test
//    public void testGetShowWithNullArgument() {
//        try {
//            showService.getShow(null);
//            fail("Can't get show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#getShow(Integer)} with exception in DAO tier.
//     */
//    @Test
//    public void testGetShowWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShow(anyInt());
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.getShow(Integer.MAX_VALUE);
//            fail("Can't get show with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShow(Integer.MAX_VALUE);
//        verify(showCache).get(SHOW_CACHE_KEY + Integer.MAX_VALUE);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#add(Show)} with cached shows.
//     */
//    @Test
//    public void testAddWithCachedShows() {
//        final Show show = generate(Show.class);
//        final List<Show> shows = CollectionUtils.newList(mock(Show.class), mock(Show.class));
//        final List<Show> showsList = new ArrayList<>(shows);
//        showsList.add(show);
//        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(shows));
//
//        showService.add(show);
//
//        verify(showDAO).add(show);
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verify(showCache).put(SHOWS_CACHE_KEY, showsList);
//        verify(showCache).put(SHOW_CACHE_KEY + show.getId(), show);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#add(Show)} with not cached shows.
//     */
//    @Test
//    public void testAddWithNotCachedShows() {
//        final Show show = generate(Show.class);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        showService.add(show);
//
//        verify(showDAO).add(show);
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#add(Show)} with null argument.
//     */
//    @Test
//    public void testAddWithNullArgument() {
//        try {
//            showService.add(null);
//            fail("Can't add show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#add(Show)} with exception in DAO tier.
//     */
//    @Test
//    public void testAddWithDAOTierException() {
//        final Show show = generate(Show.class);
//        doThrow(DataStorageException.class).when(showDAO).add(any(Show.class));
//
//        try {
//            showService.add(show);
//            fail("Can't add show with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).add(show);
//        verifyNoMoreInteractions(showDAO);
//        verifyZeroInteractions(showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#update(Show)}.
//     */
//    @Test
//    public void testUpdate() {
//        final Show show = generate(Show.class);
//
//        showService.update(show);
//
//        verify(showDAO).update(show);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#update(Show)} with null argument.
//     */
//    @Test
//    public void testUpdateWithNullArgument() {
//        try {
//            showService.update(null);
//            fail("Can't update show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#update(Show)} with exception in DAO tier.
//     */
//    @Test
//    public void testUpdateWithDAOTierException() {
//        final Show show = generate(Show.class);
//        doThrow(DataStorageException.class).when(showDAO).update(any(Show.class));
//
//        try {
//            showService.update(show);
//            fail("Can't update show with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).update(show);
//        verifyNoMoreInteractions(showDAO);
//        verifyZeroInteractions(showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#remove(Show)} with cached data.
//     */
//    @Test
//    public void testRemoveWithCachedData() {
//        final Show show = generate(Show.class);
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
//        when(showCache.get(SEASONS_CACHE_KEY + show.getId())).thenReturn(new SimpleValueWrapper(seasons));
//        for (final Season season : seasons) {
//            when(showCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
//        }
//
//        showService.remove(show);
//
//        verify(showDAO).remove(show);
//        for (final Season season : seasons) {
//            verify(seasonDAO).remove(season);
//            verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        for (final Episode episode : episodes) {
//            verify(episodeDAO, times(seasons.size())).remove(episode);
//        }
//        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#remove(Show)} with not cached data.
//     */
//    @Test
//    public void testRemoveWithNotCachedData() {
//        final Show show = generate(Show.class);
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
//        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        showService.remove(show);
//
//        verify(showDAO).remove(show);
//        verify(seasonDAO).findSeasonsByShow(show);
//        for (final Season season : seasons) {
//            verify(seasonDAO).remove(season);
//            verify(episodeDAO).findEpisodesBySeason(season);
//            verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        for (final Episode episode : episodes) {
//            verify(episodeDAO, times(seasons.size())).remove(episode);
//        }
//        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#remove(Show)} with null argument.
//     */
//    @Test
//    public void testRemoveWithNullArgument() {
//        try {
//            showService.remove(null);
//            fail("Can't remove show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#remove(Show)} with exception in DAO tier.
//     */
//    @Test
//    public void testRemoveWithDAOTierException() {
//        final Show show = generate(Show.class);
//        doThrow(DataStorageException.class).when(seasonDAO).findSeasonsByShow(any(Show.class));
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.remove(show);
//            fail("Can't remove show with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(seasonDAO).findSeasonsByShow(show);
//        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        verifyNoMoreInteractions(seasonDAO, showCache);
//        verifyZeroInteractions(showDAO, episodeDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#duplicate(Show)} with cached data.
//     */
//    @Test
//    public void testDuplicateWithCachedData() {
//        final Show show = generate(Show.class);
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
//        when(showCache.get(SEASONS_CACHE_KEY + show.getId())).thenReturn(new SimpleValueWrapper(seasons));
//        for (final Season season : seasons) {
//            when(showCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
//        }
//
//        showService.duplicate(show);
//
//        verify(showDAO).add(any(Show.class));
//        verify(showDAO).update(any(Show.class));
//        verify(seasonDAO, times(seasons.size())).add(any(Season.class));
//        verify(seasonDAO, times(seasons.size())).update(any(Season.class));
//        verify(episodeDAO, times(seasons.size() * episodes.size())).add(any(Episode.class));
//        verify(episodeDAO, times(seasons.size() * episodes.size())).update(any(Episode.class));
//        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        for (final Season season : seasons) {
//            verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#duplicate(Show)} with not cached data.
//     */
//    @Test
//    public void testDuplicateWithNotCachedData() {
//        final Show show = generate(Show.class);
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
//        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        showService.duplicate(show);
//
//        verify(showDAO).add(any(Show.class));
//        verify(showDAO).update(any(Show.class));
//        verify(seasonDAO, times(seasons.size())).add(any(Season.class));
//        verify(seasonDAO, times(seasons.size())).update(any(Season.class));
//        verify(seasonDAO).findSeasonsByShow(show);
//        verify(episodeDAO, times(seasons.size() * episodes.size())).add(any(Episode.class));
//        verify(episodeDAO, times(seasons.size() * episodes.size())).update(any(Episode.class));
//        for (final Season season : seasons) {
//            verify(episodeDAO).findEpisodesBySeason(season);
//        }
//        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        for (final Season season : seasons) {
//            verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#duplicate(Show)} with null argument.
//     */
//    @Test
//    public void testDuplicateWithNullArgument() {
//        try {
//            showService.duplicate(null);
//            fail("Can't duplicate show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#duplicate(Show)} with exception in DAO tier.
//     */
//    @Test
//    public void testDuplicateWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).add(any(Show.class));
//
//        try {
//            showService.duplicate(generate(Show.class));
//            fail("Can't duplicate show with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).add(any(Show.class));
//        verifyNoMoreInteractions(showDAO);
//        verifyZeroInteractions(seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#moveUp(Show)} with cached shows.
//     */
//    @Test
//    public void testMoveUpWithCachedShows() {
//        final Show show1 = generate(Show.class);
//        final int position1 = show1.getPosition();
//        final Show show2 = generate(Show.class);
//        final int position2 = show2.getPosition();
//        final List<Show> shows = CollectionUtils.newList(show1, show2);
//        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(shows));
//
//        showService.moveUp(show2);
//        DeepAsserts.assertEquals(position2, show1.getPosition());
//        DeepAsserts.assertEquals(position1, show2.getPosition());
//
//        verify(showDAO).update(show1);
//        verify(showDAO).update(show2);
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#moveUp(Show)} with not cached shows.
//     */
//    @Test
//    public void testMoveUpWithNotCachedShows() {
//        final Show show1 = generate(Show.class);
//        final int position1 = show1.getPosition();
//        final Show show2 = generate(Show.class);
//        final int position2 = show2.getPosition();
//        final List<Show> shows = CollectionUtils.newList(show1, show2);
//        when(showDAO.getShows()).thenReturn(shows);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        showService.moveUp(show2);
//        DeepAsserts.assertEquals(position2, show1.getPosition());
//        DeepAsserts.assertEquals(position1, show2.getPosition());
//
//        verify(showDAO).update(show1);
//        verify(showDAO).update(show2);
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#moveUp(Show)} with null argument.
//     */
//    @Test
//    public void testMoveUpWithNullArgument() {
//        try {
//            showService.moveUp(null);
//            fail("Can't move up show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#moveUp(Show)} with exception in DAO tier.
//     */
//    @Test
//    public void testMoveUpWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShows();
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.moveUp(generate(Show.class));
//            fail("Can't move up show with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#moveDown(Show)} with cached shows.
//     */
//    @Test
//    public void testMoveDownWithCachedShows() {
//        final Show show1 = generate(Show.class);
//        final int position1 = show1.getPosition();
//        final Show show2 = generate(Show.class);
//        final int position2 = show2.getPosition();
//        final List<Show> shows = CollectionUtils.newList(show1, show2);
//        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(shows));
//
//        showService.moveDown(show1);
//        DeepAsserts.assertEquals(position2, show1.getPosition());
//        DeepAsserts.assertEquals(position1, show2.getPosition());
//
//        verify(showDAO).update(show1);
//        verify(showDAO).update(show2);
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#moveDown(Show)} with not cached shows.
//     */
//    @Test
//    public void testMoveDownWithNotCachedShows() {
//        final Show show1 = generate(Show.class);
//        final int position1 = show1.getPosition();
//        final Show show2 = generate(Show.class);
//        final int position2 = show2.getPosition();
//        final List<Show> shows = CollectionUtils.newList(show1, show2);
//        when(showDAO.getShows()).thenReturn(shows);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        showService.moveDown(show1);
//        DeepAsserts.assertEquals(position2, show1.getPosition());
//        DeepAsserts.assertEquals(position1, show2.getPosition());
//
//        verify(showDAO).update(show1);
//        verify(showDAO).update(show2);
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#moveDown(Show)} with null argument.
//     */
//    @Test
//    public void testMoveDownWithNullArgument() {
//        try {
//            showService.moveDown(null);
//            fail("Can't move down show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#moveDown(Show)} with exception in DAO tier.
//     */
//    @Test
//    public void testMoveDownWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShows();
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.moveDown(generate(Show.class));
//            fail("Can't move down show with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#exists(Show)} with cached existing show.
//     */
//    @Test
//    public void testExistsWithCachedExistingShow() {
//        final Show show = generate(Show.class);
//        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(show));
//
//        assertTrue(showService.exists(show));
//
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verifyNoMoreInteractions(showCache);
//        verifyZeroInteractions(showDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#exists(Show)} with cached not existing show.
//     */
//    @Test
//    public void testExistsWithCachedNotExistingShow() {
//        final Show show = generate(Show.class);
//        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));
//
//        assertFalse(showService.exists(show));
//
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verifyNoMoreInteractions(showCache);
//        verifyZeroInteractions(showDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#exists(Show)} with not cached existing show.
//     */
//    @Test
//    public void testExistsWithNotCachedExistingShow() {
//        final Show show = generate(Show.class);
//        when(showDAO.getShow(anyInt())).thenReturn(show);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        assertTrue(showService.exists(show));
//
//        verify(showDAO).getShow(show.getId());
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verify(showCache).put(SHOW_CACHE_KEY + show.getId(), show);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#exists(Show)} with not cached not existing show.
//     */
//    @Test
//    public void testExistsWithNotCachedNotExistingShow() {
//        final Show show = generate(Show.class);
//        when(showDAO.getShow(anyInt())).thenReturn(null);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        assertFalse(showService.exists(show));
//
//        verify(showDAO).getShow(show.getId());
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verify(showCache).put(SHOW_CACHE_KEY + show.getId(), null);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#exists(Show)} with null argument.
//     */
//    @Test
//    public void testExistsWithNullArgument() {
//        try {
//            showService.exists(null);
//            fail("Can't exists show with null argument.");
//        } catch (final IllegalArgumentException ex) {
//            // OK
//        }
//
//        verifyZeroInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#exists(Show)} with exception in DAO tier.
//     */
//    @Test
//    public void testExistsWithDAOTierException() {
//        final Show show = generate(Show.class);
//        doThrow(DataStorageException.class).when(showDAO).getShow(anyInt());
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.exists(show);
//            fail("Can't exists show with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShow(show.getId());
//        verify(showCache).get(SHOW_CACHE_KEY + show.getId());
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#updatePositions()} with cached data.
//     */
//    @Test
//    public void testUpdatePositionsWithCachedData() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(generate(Episode.class), generate(Episode.class));
//        when(showCache.get(SHOWS_CACHE_KEY)).thenReturn(new SimpleValueWrapper(shows));
//        for (final Show show : shows) {
//            when(showCache.get(SEASONS_CACHE_KEY + show.getId())).thenReturn(new SimpleValueWrapper(seasons));
//        }
//        for (final Season season : seasons) {
//            when(showCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
//        }
//
//        showService.updatePositions();
//
//        for (int i = 0; i < shows.size(); i++) {
//            final Show show = shows.get(i);
//            DeepAsserts.assertEquals(i, show.getPosition());
//            verify(showDAO).update(show);
//            verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        }
//        for (int i = 0; i < seasons.size(); i++) {
//            final Season season = seasons.get(i);
//            DeepAsserts.assertEquals(i, season.getPosition());
//            verify(seasonDAO, times(shows.size())).update(season);
//            verify(showCache, times(shows.size())).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        for (int i = 0; i < episodes.size(); i++) {
//            final Episode episode = episodes.get(i);
//            DeepAsserts.assertEquals(i, episode.getPosition());
//            verify(episodeDAO, times(shows.size() * seasons.size())).update(episode);
//        }
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#updatePositions()} with not cached data.
//     */
//    @Test
//    public void testUpdatePositionsWithNotCachedData() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(generate(Episode.class), generate(Episode.class));
//        when(showDAO.getShows()).thenReturn(shows);
//        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        showService.updatePositions();
//
//        verify(showDAO).getShows();
//        for (int i = 0; i < shows.size(); i++) {
//            final Show show = shows.get(i);
//            DeepAsserts.assertEquals(i, show.getPosition());
//            verify(showDAO).update(show);
//            verify(seasonDAO).findSeasonsByShow(show);
//            verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        }
//        for (int i = 0; i < seasons.size(); i++) {
//            final Season season = seasons.get(i);
//            DeepAsserts.assertEquals(i, season.getPosition());
//            verify(seasonDAO, times(shows.size())).update(season);
//            verify(episodeDAO, times(shows.size())).findEpisodesBySeason(season);
//            verify(showCache, times(shows.size())).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        for (int i = 0; i < episodes.size(); i++) {
//            final Episode episode = episodes.get(i);
//            DeepAsserts.assertEquals(i, episode.getPosition());
//            verify(episodeDAO, times(shows.size() * seasons.size())).update(episode);
//        }
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).clear();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#updatePositions()} with exception in DAO tier.
//     */
//    @Test
//    public void testUpdatePositionsWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShows();
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.updatePositions();
//            fail("Can't update positions with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showDAO, showCache);
//        verifyZeroInteractions(seasonDAO, episodeDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getTotalLength()} with cached shows.
//     */
//    @Test
//    public void testGetTotalLengthWithCachedShows() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final Episode episode1 = mock(Episode.class);
//        final Episode episode2 = mock(Episode.class);
//        final List<Episode> episodes = CollectionUtils.newList(episode1, episode2);
//        final int[] lengths = new int[2];
//        int totalLength = 0;
//        for (int i = 0; i < 2; i++) {
//            final int length = generate(Integer.class);
//            lengths[i] = length;
//            totalLength += length;
//        }
//        when(showCache.get(SHOWS_CACHE_KEY)).thenReturn(new SimpleValueWrapper(shows));
//        for (final Show show : shows) {
//            when(showCache.get(SEASONS_CACHE_KEY + show.getId())).thenReturn(new SimpleValueWrapper(seasons));
//        }
//        for (final Season season : seasons) {
//            when(showCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
//        }
//        when(episode1.getLength()).thenReturn(lengths[0]);
//        when(episode2.getLength()).thenReturn(lengths[1]);
//
//        DeepAsserts.assertEquals(new Time(shows.size() * seasons.size() * totalLength), showService.getTotalLength());
//
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        for (final Show show : shows) {
//            verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        }
//        for (final Season season : seasons) {
//            verify(showCache, times(shows.size())).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        verify(episode1, times(shows.size() * seasons.size())).getLength();
//        verify(episode2, times(shows.size() * seasons.size())).getLength();
//        verifyNoMoreInteractions(showCache, episode1, episode2);
//        verifyZeroInteractions(showDAO, seasonDAO, episodeDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getTotalLength()} with not cached shows.
//     */
//    @Test
//    public void testGetTotalLengthWithNotCachedShows() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final Episode episode1 = mock(Episode.class);
//        final Episode episode2 = mock(Episode.class);
//        final List<Episode> episodes = CollectionUtils.newList(episode1, episode2);
//        final int[] lengths = new int[2];
//        int totalLength = 0;
//        for (int i = 0; i < 2; i++) {
//            final int length = generate(Integer.class);
//            lengths[i] = length;
//            totalLength += length;
//        }
//        when(showDAO.getShows()).thenReturn(shows);
//        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
//        when(showCache.get(anyString())).thenReturn(null);
//        when(episode1.getLength()).thenReturn(lengths[0]);
//        when(episode2.getLength()).thenReturn(lengths[1]);
//
//        DeepAsserts.assertEquals(new Time(shows.size() * seasons.size() * totalLength), showService.getTotalLength());
//
//        verify(showDAO).getShows();
//        for (final Show show : shows) {
//            verify(seasonDAO).findSeasonsByShow(show);
//            verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//            verify(showCache).put(SEASONS_CACHE_KEY + show.getId(), seasons);
//        }
//        for (final Season season : seasons) {
//            verify(episodeDAO, times(shows.size())).findEpisodesBySeason(season);
//            verify(showCache, times(shows.size())).get(EPISODES_CACHE_KEY + season.getId());
//            verify(showCache, times(shows.size())).put(EPISODES_CACHE_KEY + season.getId(), episodes);
//        }
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).put(SHOWS_CACHE_KEY, shows);
//        verify(episode1, times(shows.size() * seasons.size())).getLength();
//        verify(episode2, times(shows.size() * seasons.size())).getLength();
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache, episode1, episode2);
//    }
//
//    /**
//     * Test method for {@link ShowService#getTotalLength()} with exception in DAO tier.
//     */
//    @Test
//    public void testGetTotalLengthWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShows();
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.getTotalLength();
//            fail("Can't get total length with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#getSeasonsCount()} with cached data.
//     */
//    @Test
//    public void testGetSeasonsCountWithCachedData() {
//        final Show show1 = generate(Show.class);
//        final Show show2 = generate(Show.class);
//        final List<Show> shows = CollectionUtils.newList(show1, show2);
//        final List<Season> seasons1 = CollectionUtils.newList(mock(Season.class));
//        final List<Season> seasons2 = CollectionUtils.newList(mock(Season.class), mock(Season.class));
//        when(showCache.get(SHOWS_CACHE_KEY)).thenReturn(new SimpleValueWrapper(shows));
//        when(showCache.get(SEASONS_CACHE_KEY + show1.getId())).thenReturn(new SimpleValueWrapper(seasons1));
//        when(showCache.get(SEASONS_CACHE_KEY + show2.getId())).thenReturn(new SimpleValueWrapper(seasons2));
//
//        DeepAsserts.assertEquals(seasons1.size() + seasons2.size(), showService.getSeasonsCount());
//
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).get(SEASONS_CACHE_KEY + show1.getId());
//        verify(showCache).get(SEASONS_CACHE_KEY + show2.getId());
//        verifyNoMoreInteractions(showCache);
//        verifyZeroInteractions(showDAO, seasonDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getSeasonsCount()} with not cached data.
//     */
//    @Test
//    public void testGetSeasonsCountWithNotCachedSeasonCategories() {
//        final Show show1 = generate(Show.class);
//        final Show show2 = generate(Show.class);
//        final List<Show> shows = CollectionUtils.newList(show1, show2);
//        final List<Season> seasons1 = CollectionUtils.newList(mock(Season.class));
//        final List<Season> seasons2 = CollectionUtils.newList(mock(Season.class), mock(Season.class));
//        when(showDAO.getShows()).thenReturn(shows);
//        when(seasonDAO.findSeasonsByShow(show1)).thenReturn(seasons1);
//        when(seasonDAO.findSeasonsByShow(show2)).thenReturn(seasons2);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        DeepAsserts.assertEquals(seasons1.size() + seasons2.size(), showService.getSeasonsCount());
//
//        verify(showDAO).getShows();
//        verify(seasonDAO).findSeasonsByShow(show1);
//        verify(seasonDAO).findSeasonsByShow(show2);
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).put(SHOWS_CACHE_KEY, shows);
//        verify(showCache).get(SEASONS_CACHE_KEY + show1.getId());
//        verify(showCache).put(SEASONS_CACHE_KEY + show1.getId(), seasons1);
//        verify(showCache).get(SEASONS_CACHE_KEY + show2.getId());
//        verify(showCache).put(SEASONS_CACHE_KEY + show2.getId(), seasons2);
//        verifyNoMoreInteractions(showDAO, seasonDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#getSeasonsCount()} with exception in DAO tier.
//     */
//    @Test
//    public void testGetSeasonsCountWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShows();
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.getSeasonsCount();
//            fail("Can't get seasons count with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showDAO, showCache);
//        verifyZeroInteractions(seasonDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getEpisodesCount()} with cached data.
//     */
//    @Test
//    public void testGetEpisodesCountWithCachedData() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
//        when(showCache.get(SHOWS_CACHE_KEY)).thenReturn(new SimpleValueWrapper(shows));
//        for (final Show show : shows) {
//            when(showCache.get(SEASONS_CACHE_KEY + show.getId())).thenReturn(new SimpleValueWrapper(seasons));
//        }
//        for (final Season season : seasons) {
//            when(showCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
//        }
//
//        DeepAsserts.assertEquals(shows.size() * seasons.size() * episodes.size(), showService.getEpisodesCount());
//
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        for (final Show show : shows) {
//            verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//        }
//        for (final Season season : seasons) {
//            verify(showCache, times(shows.size())).get(EPISODES_CACHE_KEY + season.getId());
//        }
//        verifyNoMoreInteractions(showCache);
//        verifyZeroInteractions(showDAO, seasonDAO, episodeDAO);
//    }
//
//    /**
//     * Test method for {@link ShowService#getEpisodesCount()} with not cached data.
//     */
//    @Test
//    public void testGetEpisodesCountWithNotCachedSeasonCategories() {
//        final List<Show> shows = CollectionUtils.newList(generate(Show.class), generate(Show.class));
//        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
//        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
//        when(showDAO.getShows()).thenReturn(shows);
//        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
//        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
//        when(showCache.get(anyString())).thenReturn(null);
//
//        DeepAsserts.assertEquals(shows.size() * seasons.size() * episodes.size(), showService.getEpisodesCount());
//
//        verify(showDAO).getShows();
//        for (final Show show : shows) {
//            verify(seasonDAO).findSeasonsByShow(show);
//            verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
//            verify(showCache).put(SEASONS_CACHE_KEY + show.getId(), seasons);
//        }
//        for (final Season season : seasons) {
//            verify(episodeDAO, times(shows.size())).findEpisodesBySeason(season);
//            verify(showCache, times(shows.size())).get(EPISODES_CACHE_KEY + season.getId());
//            verify(showCache, times(shows.size())).put(EPISODES_CACHE_KEY + season.getId(), episodes);
//        }
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verify(showCache).put(SHOWS_CACHE_KEY, shows);
//        verifyNoMoreInteractions(showDAO, seasonDAO, episodeDAO, showCache);
//    }
//
//    /**
//     * Test method for {@link ShowService#getEpisodesCount()} with exception in DAO tier.
//     */
//    @Test
//    public void testGetEpisodesCountWithDAOTierException() {
//        doThrow(DataStorageException.class).when(showDAO).getShows();
//        when(showCache.get(anyString())).thenReturn(null);
//
//        try {
//            showService.getEpisodesCount();
//            fail("Can't get seasons count with not thrown ServiceOperationException for DAO tier exception.");
//        } catch (final ServiceOperationException ex) {
//            // OK
//        }
//
//        verify(showDAO).getShows();
//        verify(showCache).get(SHOWS_CACHE_KEY);
//        verifyNoMoreInteractions(showDAO, showCache);
//        verifyZeroInteractions(seasonDAO, episodeDAO);
//    }
//
//}
