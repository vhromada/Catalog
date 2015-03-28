package cz.vhromada.catalog.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * A class represents test for class {@link SeasonServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SeasonServiceImplTest extends ObjectGeneratorTest {

    /**
     * Cache key for list of seasons
     */
    private static final String SEASONS_CACHE_KEY = "seasons";

    /**
     * Cache key for season
     */
    private static final String SEASON_CACHE_KEY = "season";

    /**
     * Cache key for list of episodes
     */
    private static final String EPISODES_CACHE_KEY = "episodes";

    /**
     * Instance of {@link SeasonDAO}
     */
    @Mock
    private SeasonDAO seasonDAO;

    /**
     * Instance of {@link EpisodeDAO}
     */
    @Mock
    private EpisodeDAO episodeDAO;

    /**
     * Instance of {@link Cache}
     */
    @Mock
    private Cache showCache;

    /**
     * Instance of {@link SeasonService}
     */
    private SeasonService seasonService;

    /**
     * Initializes service for seasons.
     */
    @Before
    public void setUp() {
        seasonService = new SeasonServiceImpl(seasonDAO, episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonServiceImpl#SeasonServiceImpl(SeasonDAO, EpisodeDAO, Cache)} with null DAO for seasons.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSeasonDAO() {
        new SeasonServiceImpl(null, episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonServiceImpl#SeasonServiceImpl(SeasonDAO, EpisodeDAO, Cache)} with null DAO for episodes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEpisodeDAO() {
        new SeasonServiceImpl(seasonDAO, null, showCache);
    }

    /**
     * Test method for {@link SeasonServiceImpl#SeasonServiceImpl(SeasonDAO, EpisodeDAO, Cache))} with null cache for shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullShowCache() {
        new SeasonServiceImpl(seasonDAO, episodeDAO, null);
    }

    /**
     * Test method for {@link SeasonService#getSeason(Integer)} with cached existing season.
     */
    @Test
    public void testGetSeasonWithCachedExistingSeason() {
        final Season season = generate(Season.class);
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(season));

        DeepAsserts.assertEquals(season, seasonService.getSeason(season.getId()));

        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(showCache);
        verifyZeroInteractions(seasonDAO);
    }

    /**
     * Test method for {@link SeasonService#getSeason(Integer)} with cached not existing season.
     */
    @Test
    public void testGetSeasonWithCachedNotExistingSeason() {
        final Season season = generate(Season.class);
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertNull(seasonService.getSeason(season.getId()));

        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(showCache);
        verifyZeroInteractions(seasonDAO);
    }

    /**
     * Test method for {@link SeasonService#getSeason(Integer)} with not cached existing season.
     */
    @Test
    public void testGetSeasonWithNotCachedExistingSeason() {
        final Season season = generate(Season.class);
        when(seasonDAO.getSeason(anyInt())).thenReturn(season);
        when(showCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(season, seasonService.getSeason(season.getId()));

        verify(seasonDAO).getSeason(season.getId());
        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verify(showCache).put(SEASON_CACHE_KEY + season.getId(), season);
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#getSeason(Integer)} with not cached not existing season.
     */
    @Test
    public void testGetSeasonWithNotCachedNotExistingSeason() {
        final Season season = generate(Season.class);
        when(seasonDAO.getSeason(anyInt())).thenReturn(null);
        when(showCache.get(anyString())).thenReturn(null);

        assertNull(seasonService.getSeason(season.getId()));

        verify(seasonDAO).getSeason(season.getId());
        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verify(showCache).put(SEASON_CACHE_KEY + season.getId(), null);
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#getSeason(Integer)} with null argument.
     */
    @Test
    public void testGetSeasonWithNullArgument() {
        try {
            seasonService.getSeason(null);
            fail("Can't get season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#getSeason(Integer)} with exception in DAO tier.
     */
    @Test
    public void testGetSeasonWithDAOTierException() {
        doThrow(DataStorageException.class).when(seasonDAO).getSeason(anyInt());
        when(showCache.get(anyString())).thenReturn(null);

        try {
            seasonService.getSeason(Integer.MAX_VALUE);
            fail("Can't get season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).getSeason(Integer.MAX_VALUE);
        verify(showCache).get(SEASON_CACHE_KEY + Integer.MAX_VALUE);
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#add(Season)} with cached seasons.
     */
    @Test
    public void testAddWithCachedSeasons() {
        final Season season = generate(Season.class);
        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
        final List<Season> seasonsList = new ArrayList<>(seasons);
        seasonsList.add(season);
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

        seasonService.add(season);

        verify(seasonDAO).add(season);
        verify(showCache).get(SEASONS_CACHE_KEY + season.getShow().getId());
        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verify(showCache).put(SEASONS_CACHE_KEY + season.getShow().getId(), seasonsList);
        verify(showCache).put(SEASON_CACHE_KEY + season.getId(), season);
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#add(Season)} with not cached seasons.
     */
    @Test
    public void testAddWithNotCachedSeasons() {
        final Season season = generate(Season.class);
        when(showCache.get(anyString())).thenReturn(null);

        seasonService.add(season);

        verify(seasonDAO).add(season);
        verify(showCache).get(SEASONS_CACHE_KEY + season.getShow().getId());
        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#add(Season)} with null argument.
     */
    @Test
    public void testAddWithNullArgument() {
        try {
            seasonService.add(null);
            fail("Can't add season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#add(Season)} with exception in DAO tier.
     */
    @Test
    public void testAddWithDAOTierException() {
        final Season season = generate(Season.class);
        doThrow(DataStorageException.class).when(seasonDAO).add(any(Season.class));

        try {
            seasonService.add(season);
            fail("Can't add season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).add(season);
        verifyNoMoreInteractions(seasonDAO);
        verifyZeroInteractions(showCache);
    }

    /**
     * Test method for {@link SeasonService#update(Season)}.
     */
    @Test
    public void testUpdate() {
        final Season season = generate(Season.class);

        seasonService.update(season);

        verify(seasonDAO).update(season);
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#update(Season)} with null argument.
     */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            seasonService.update(null);
            fail("Can't update season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#update(Season)} with exception in DAO tier.
     */
    @Test
    public void testUpdateWithDAOTierException() {
        final Season season = generate(Season.class);
        doThrow(DataStorageException.class).when(seasonDAO).update(any(Season.class));

        try {
            seasonService.update(season);
            fail("Can't update season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).update(season);
        verifyNoMoreInteractions(seasonDAO);
        verifyZeroInteractions(showCache);
    }

    /**
     * Test method for {@link SeasonService#remove(Season)} with cached data.
     */
    @Test
    public void testRemoveWithCachedData() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

        seasonService.remove(season);

        verify(seasonDAO).remove(season);
        for (final Episode episode : episodes) {
            verify(episodeDAO).remove(episode);
        }
        verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#remove(Season)} with not cached data.
     */
    @Test
    public void testRemoveWithNotCachedData() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(showCache.get(anyString())).thenReturn(null);

        seasonService.remove(season);

        verify(seasonDAO).remove(season);
        verify(episodeDAO).findEpisodesBySeason(season);
        for (final Episode episode : episodes) {
            verify(episodeDAO).remove(episode);
        }
        verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#remove(Season)} with null argument.
     */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            seasonService.remove(null);
            fail("Can't remove season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#remove(Season)} with exception in DAO tier.
     */
    @Test
    public void testRemoveWithDAOTierException() {
        final Season season = generate(Season.class);
        doThrow(DataStorageException.class).when(episodeDAO).findEpisodesBySeason(any(Season.class));
        when(showCache.get(anyString())).thenReturn(null);

        try {
            seasonService.remove(season);
            fail("Can't remove season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(episodeDAO).findEpisodesBySeason(season);
        verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(seasonDAO, showCache);
        verifyZeroInteractions(episodeDAO);
    }

    /**
     * Test method for {@link SeasonService#duplicate(Season)} with cached data.
     */
    @Test
    public void testDuplicateWithCachedData() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

        seasonService.duplicate(season);

        verify(seasonDAO).add(any(Season.class));
        verify(seasonDAO).update(any(Season.class));
        verify(episodeDAO, times(episodes.size())).add(any(Episode.class));
        verify(episodeDAO, times(episodes.size())).update(any(Episode.class));
        verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#duplicate(Season)} with not cached data.
     */
    @Test
    public void testDuplicateWithNotCachedData() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(showCache.get(anyString())).thenReturn(null);

        seasonService.duplicate(season);

        verify(seasonDAO).add(any(Season.class));
        verify(seasonDAO).update(any(Season.class));
        verify(episodeDAO).findEpisodesBySeason(season);
        verify(episodeDAO, times(episodes.size())).add(any(Episode.class));
        verify(episodeDAO, times(episodes.size())).update(any(Episode.class));
        verify(showCache).get(EPISODES_CACHE_KEY + season.getId());
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#duplicate(Season)} with null argument.
     */
    @Test
    public void testDuplicateWithNullArgument() {
        try {
            seasonService.duplicate(null);
            fail("Can't duplicate season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#duplicate(Season)} with exception in DAO tier.
     */
    @Test
    public void testDuplicateWithDAOTierException() {
        doThrow(DataStorageException.class).when(seasonDAO).add(any(Season.class));

        try {
            seasonService.duplicate(generate(Season.class));
            fail("Can't duplicate season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).add(any(Season.class));
        verifyNoMoreInteractions(seasonDAO);
        verifyZeroInteractions(episodeDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#moveUp(Season)} with cached seasons.
     */
    @Test
    public void testMoveUpWithCachedSeasons() {
        final Show show = generate(Show.class);
        final Season season1 = generate(Season.class);
        season1.setShow(show);
        final int position1 = season1.getPosition();
        final Season season2 = generate(Season.class);
        season2.setShow(show);
        final int position2 = season2.getPosition();
        final List<Season> seasons = CollectionUtils.newList(season1, season2);
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

        seasonService.moveUp(season2);
        DeepAsserts.assertEquals(position2, season1.getPosition());
        DeepAsserts.assertEquals(position1, season2.getPosition());

        verify(seasonDAO).update(season1);
        verify(seasonDAO).update(season2);
        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#moveUp(Season)} with not cached seasons.
     */
    @Test
    public void testMoveUpWithNotCachedSeasons() {
        final Show show = generate(Show.class);
        final Season season1 = generate(Season.class);
        season1.setShow(show);
        final int position1 = season1.getPosition();
        final Season season2 = generate(Season.class);
        season2.setShow(show);
        final int position2 = season2.getPosition();
        final List<Season> seasons = CollectionUtils.newList(season1, season2);
        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
        when(showCache.get(anyString())).thenReturn(null);

        seasonService.moveUp(season2);
        DeepAsserts.assertEquals(position2, season1.getPosition());
        DeepAsserts.assertEquals(position1, season2.getPosition());

        verify(seasonDAO).update(season1);
        verify(seasonDAO).update(season2);
        verify(seasonDAO).findSeasonsByShow(show);
        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#moveUp(Season)} with null argument.
     */
    @Test
    public void testMoveUpWithNullArgument() {
        try {
            seasonService.moveUp(null);
            fail("Can't move up season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#moveUp(Season)} with exception in DAO tier.
     */
    @Test
    public void testMoveUpWithDAOTierException() {
        final Show show = generate(Show.class);
        final Season season = generate(Season.class);
        season.setShow(show);
        doThrow(DataStorageException.class).when(seasonDAO).findSeasonsByShow(any(Show.class));
        when(showCache.get(anyString())).thenReturn(null);

        try {
            seasonService.moveUp(season);
            fail("Can't move up season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).findSeasonsByShow(show);
        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#moveDown(Season)} with cached seasons.
     */
    @Test
    public void testMoveDownWithCachedSeasons() {
        final Show show = generate(Show.class);
        final Season season1 = generate(Season.class);
        season1.setShow(show);
        final int position1 = season1.getPosition();
        final Season season2 = generate(Season.class);
        season2.setShow(show);
        final int position2 = season2.getPosition();
        final List<Season> seasons = CollectionUtils.newList(season1, season2);
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

        seasonService.moveDown(season1);
        DeepAsserts.assertEquals(position2, season1.getPosition());
        DeepAsserts.assertEquals(position1, season2.getPosition());

        verify(seasonDAO).update(season1);
        verify(seasonDAO).update(season2);
        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#moveDown(Season)} with not cached seasons.
     */
    @Test
    public void testMoveDownWithNotCachedSeasons() {
        final Show show = generate(Show.class);
        final Season season1 = generate(Season.class);
        season1.setShow(show);
        final int position1 = season1.getPosition();
        final Season season2 = generate(Season.class);
        season2.setShow(show);
        final int position2 = season2.getPosition();
        final List<Season> seasons = CollectionUtils.newList(season1, season2);
        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
        when(showCache.get(anyString())).thenReturn(null);

        seasonService.moveDown(season1);
        DeepAsserts.assertEquals(position2, season1.getPosition());
        DeepAsserts.assertEquals(position1, season2.getPosition());

        verify(seasonDAO).update(season1);
        verify(seasonDAO).update(season2);
        verify(seasonDAO).findSeasonsByShow(show);
        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verify(showCache).clear();
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#moveDown(Season)} with null argument.
     */
    @Test
    public void testMoveDownWithNullArgument() {
        try {
            seasonService.moveDown(null);
            fail("Can't move down season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#moveDown(Season)} with exception in DAO tier.
     */
    @Test
    public void testMoveDownWithDAOTierException() {
        final Show show = generate(Show.class);
        final Season season = generate(Season.class);
        season.setShow(show);
        doThrow(DataStorageException.class).when(seasonDAO).findSeasonsByShow(any(Show.class));
        when(showCache.get(anyString())).thenReturn(null);

        try {
            seasonService.moveDown(season);
            fail("Can't move down season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).findSeasonsByShow(show);
        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#exists(Season)} with cached existing season.
     */
    @Test
    public void testExistsWithCachedExistingSeason() {
        final Season season = generate(Season.class);
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(season));

        assertTrue(seasonService.exists(season));

        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(showCache);
        verifyZeroInteractions(seasonDAO);
    }

    /**
     * Test method for {@link SeasonService#exists(Season)} with cached not existing season.
     */
    @Test
    public void testExistsWithCachedNotExistingSeason() {
        final Season season = generate(Season.class);
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertFalse(seasonService.exists(season));

        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(showCache);
        verifyZeroInteractions(seasonDAO);
    }

    /**
     * Test method for {@link SeasonService#exists(Season)} with not cached existing season.
     */
    @Test
    public void testExistsWithNotCachedExistingSeason() {
        final Season season = generate(Season.class);
        when(seasonDAO.getSeason(anyInt())).thenReturn(season);
        when(showCache.get(anyString())).thenReturn(null);

        assertTrue(seasonService.exists(season));

        verify(seasonDAO).getSeason(season.getId());
        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verify(showCache).put(SEASON_CACHE_KEY + season.getId(), season);
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#exists(Season)} with not cached not existing season.
     */
    @Test
    public void testExistsWithNotCachedNotExistingSeason() {
        final Season season = generate(Season.class);
        when(seasonDAO.getSeason(anyInt())).thenReturn(null);
        when(showCache.get(anyString())).thenReturn(null);

        assertFalse(seasonService.exists(season));

        verify(seasonDAO).getSeason(season.getId());
        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verify(showCache).put(SEASON_CACHE_KEY + season.getId(), null);
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#exists(Season)} with null argument.
     */
    @Test
    public void testExistsWithNullArgument() {
        try {
            seasonService.exists(null);
            fail("Can't exists season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#exists(Season)} with exception in DAO tier.
     */
    @Test
    public void testExistsWithDAOTierException() {
        final Season season = generate(Season.class);
        doThrow(DataStorageException.class).when(seasonDAO).getSeason(anyInt());
        when(showCache.get(anyString())).thenReturn(null);

        try {
            seasonService.exists(season);
            fail("Can't exists season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).getSeason(season.getId());
        verify(showCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#findSeasonsByShow(Show)} with cached seasons.
     */
    @Test
    public void testFindSeasonsByShowWithCachedSeasons() {
        final Show show = generate(Show.class);
        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
        when(showCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

        DeepAsserts.assertEquals(seasons, seasonService.findSeasonsByShow(show));

        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verifyNoMoreInteractions(showCache);
        verifyZeroInteractions(seasonDAO);
    }

    /**
     * Test method for {@link SeasonService#findSeasonsByShow(Show)} with not cached seasons.
     */
    @Test
    public void testFindSeasonsByShowWithNotCachedSeasons() {
        final Show show = generate(Show.class);
        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
        when(seasonDAO.findSeasonsByShow(any(Show.class))).thenReturn(seasons);
        when(showCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(seasons, seasonService.findSeasonsByShow(show));

        verify(seasonDAO).findSeasonsByShow(show);
        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verify(showCache).put(SEASONS_CACHE_KEY + show.getId(), seasons);
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#findSeasonsByShow(Show)} with null argument.
     */
    @Test
    public void testFindSeasonsByShowWithNullArgument() {
        try {
            seasonService.findSeasonsByShow(null);
            fail("Can't find seasons by show with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, showCache);
    }

    /**
     * Test method for {@link SeasonService#findSeasonsByShow(Show)} with exception in DAO tier.
     */
    @Test
    public void testFindSeasonsByShowWithDAOTierException() {
        final Show show = generate(Show.class);
        doThrow(DataStorageException.class).when(seasonDAO).findSeasonsByShow(any(Show.class));
        when(showCache.get(anyString())).thenReturn(null);

        try {
            seasonService.findSeasonsByShow(show);
            fail("Can't find seasons by show with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).findSeasonsByShow(show);
        verify(showCache).get(SEASONS_CACHE_KEY + show.getId());
        verifyNoMoreInteractions(seasonDAO, showCache);
    }

}
