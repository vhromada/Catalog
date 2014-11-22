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
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

    /** Cache key for list of seasons */
    private static final String SEASONS_CACHE_KEY = "seasons";

    /** Cache key for season */
    private static final String SEASON_CACHE_KEY = "season";

    /** Cache key for list of episodes */
    private static final String EPISODES_CACHE_KEY = "episodes";

    /** Instance of {@link SeasonDAO} */
    @Mock
    private SeasonDAO seasonDAO;

    /** Instance of {@link EpisodeDAO} */
    @Mock
    private EpisodeDAO episodeDAO;

    /** Instance of {@link Cache} */
    @Mock
    private Cache serieCache;

    /** Instance of {@link SeasonService} */
    @InjectMocks
    private SeasonService seasonService = new SeasonServiceImpl();

    /** Test method for {@link SeasonService#getSeason(Integer)} with cached existing season. */
    @Test
    public void testGetSeasonWithCachedExistingSeason() {
        final Season season = generate(Season.class);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(season));

        DeepAsserts.assertEquals(season, seasonService.getSeason(season.getId()));

        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(seasonDAO);
    }

    /** Test method for {@link SeasonService#getSeason(Integer)} with cached not existing season. */
    @Test
    public void testGetSeasonWithCachedNotExistingSeason() {
        final Season season = generate(Season.class);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertNull(seasonService.getSeason(season.getId()));

        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(seasonDAO);
    }

    /** Test method for {@link SeasonService#getSeason(Integer)} with not cached existing season. */
    @Test
    public void testGetSeasonWithNotCachedExistingSeason() {
        final Season season = generate(Season.class);
        when(seasonDAO.getSeason(anyInt())).thenReturn(season);
        when(serieCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(season, seasonService.getSeason(season.getId()));

        verify(seasonDAO).getSeason(season.getId());
        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verify(serieCache).put(SEASON_CACHE_KEY + season.getId(), season);
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#getSeason(Integer)} with not cached not existing season. */
    @Test
    public void testGetSeasonWithNotCachedNotExistingSeason() {
        final Season season = generate(Season.class);
        when(seasonDAO.getSeason(anyInt())).thenReturn(null);
        when(serieCache.get(anyString())).thenReturn(null);

        assertNull(seasonService.getSeason(season.getId()));

        verify(seasonDAO).getSeason(season.getId());
        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verify(serieCache).put(SEASON_CACHE_KEY + season.getId(), null);
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#getSeason(Integer)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testGetSeasonWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.getSeason(Integer.MAX_VALUE);
    }

    /** Test method for {@link SeasonService#getSeason(Integer)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetSeasonWithNotSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.getSeason(Integer.MAX_VALUE);
    }

    /** Test method for {@link SeasonService#getSeason(Integer)} with null argument. */
    @Test
    public void testGetSeasonWithNullArgument() {
        try {
            seasonService.getSeason(null);
            fail("Can't get season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#getSeason(Integer)} with exception in DAO tier. */
    @Test
    public void testGetSeasonWithDAOTierException() {
        doThrow(DataStorageException.class).when(seasonDAO).getSeason(anyInt());
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            seasonService.getSeason(Integer.MAX_VALUE);
            fail("Can't get season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).getSeason(Integer.MAX_VALUE);
        verify(serieCache).get(SEASON_CACHE_KEY + Integer.MAX_VALUE);
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#add(Season)} with cached seasons. */
    @Test
    public void testAddWithCachedSeasons() {
        final Season season = generate(Season.class);
        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
        final List<Season> seasonsList = new ArrayList<>(seasons);
        seasonsList.add(season);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

        seasonService.add(season);

        verify(seasonDAO).add(season);
        verify(serieCache).get(SEASONS_CACHE_KEY + season.getSerie().getId());
        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verify(serieCache).put(SEASONS_CACHE_KEY + season.getSerie().getId(), seasonsList);
        verify(serieCache).put(SEASON_CACHE_KEY + season.getId(), season);
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#add(Season)} with not cached seasons. */
    @Test
    public void testAddWithNotCachedSeasons() {
        final Season season = generate(Season.class);
        when(serieCache.get(anyString())).thenReturn(null);

        seasonService.add(season);

        verify(seasonDAO).add(season);
        verify(serieCache).get(SEASONS_CACHE_KEY + season.getSerie().getId());
        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#add(Season)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.add(mock(Season.class));
    }

    /** Test method for {@link SeasonService#add(Season)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.add(mock(Season.class));
    }

    /** Test method for {@link SeasonService#add(Season)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        try {
            seasonService.add(null);
            fail("Can't add season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#add(Season)} with exception in DAO tier. */
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
        verifyZeroInteractions(serieCache);
    }

    /** Test method for {@link SeasonService#update(Season)}. */
    @Test
    public void testUpdate() {
        final Season season = generate(Season.class);

        seasonService.update(season);

        verify(seasonDAO).update(season);
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#update(Season)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.update(mock(Season.class));
    }

    /** Test method for {@link SeasonService#update(Season)} with not set season cache. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.update(mock(Season.class));
    }

    /** Test method for {@link SeasonService#update(Season)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            seasonService.update(null);
            fail("Can't update season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#update(Season)} with exception in DAO tier. */
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
        verifyZeroInteractions(serieCache);
    }

    /** Test method for {@link SeasonService#remove(Season)} with cached data. */
    @Test
    public void testRemoveWithCachedData() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

        seasonService.remove(season);

        verify(seasonDAO).remove(season);
        for (final Episode episode : episodes) {
            verify(episodeDAO).remove(episode);
        }
        verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SeasonService#remove(Season)} with not cached data. */
    @Test
    public void testRemoveWithNotCachedData() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(serieCache.get(anyString())).thenReturn(null);

        seasonService.remove(season);

        verify(seasonDAO).remove(season);
        verify(episodeDAO).findEpisodesBySeason(season);
        for (final Episode episode : episodes) {
            verify(episodeDAO).remove(episode);
        }
        verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SeasonService#remove(Season)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.remove(mock(Season.class));
    }

    /** Test method for {@link SeasonService#remove(Season)} with not set DAO for episodes. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetEpisodeDAO() {
        ((SeasonServiceImpl) seasonService).setEpisodeDAO(null);
        seasonService.remove(mock(Season.class));
    }

    /** Test method for {@link SeasonService#remove(Season)} with not set season cache. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.remove(mock(Season.class));
    }

    /** Test method for {@link SeasonService#remove(Season)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            seasonService.remove(null);
            fail("Can't remove season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SeasonService#remove(Season)} with exception in DAO tier. */
    @Test
    public void testRemoveWithDAOTierException() {
        final Season season = generate(Season.class);
        doThrow(DataStorageException.class).when(episodeDAO).findEpisodesBySeason(any(Season.class));
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            seasonService.remove(season);
            fail("Can't remove season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(episodeDAO).findEpisodesBySeason(season);
        verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(seasonDAO, serieCache);
        verifyZeroInteractions(episodeDAO);
    }

    /** Test method for {@link SeasonService#duplicate(Season)} with cached data. */
    @Test
    public void testDuplicateWithCachedData() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

        seasonService.duplicate(season);

        verify(seasonDAO).add(any(Season.class));
        verify(seasonDAO).update(any(Season.class));
        verify(episodeDAO, times(episodes.size())).add(any(Episode.class));
        verify(episodeDAO, times(episodes.size())).update(any(Episode.class));
        verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SeasonService#duplicate(Season)} with not cached data. */
    @Test
    public void testDuplicateWithNotCachedData() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(serieCache.get(anyString())).thenReturn(null);

        seasonService.duplicate(season);

        verify(seasonDAO).add(any(Season.class));
        verify(seasonDAO).update(any(Season.class));
        verify(episodeDAO).findEpisodesBySeason(season);
        verify(episodeDAO, times(episodes.size())).add(any(Episode.class));
        verify(episodeDAO, times(episodes.size())).update(any(Episode.class));
        verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SeasonService#duplicate(Season)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.duplicate(mock(Season.class));
    }

    /** Test method for {@link SeasonService#duplicate(Season)} with not set DAO for episodes. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetEpisodeDAO() {
        ((SeasonServiceImpl) seasonService).setEpisodeDAO(null);
        seasonService.duplicate(mock(Season.class));
    }

    /** Test method for {@link SeasonService#duplicate(Season)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.duplicate(mock(Season.class));
    }

    /** Test method for {@link SeasonService#duplicate(Season)} with null argument. */
    @Test
    public void testDuplicateWithNullArgument() {
        try {
            seasonService.duplicate(null);
            fail("Can't duplicate season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SeasonService#duplicate(Season)} with exception in DAO tier. */
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
        verifyZeroInteractions(episodeDAO, serieCache);
    }

    /** Test method for {@link SeasonService#moveUp(Season)} with cached seasons. */
    @Test
    public void testMoveUpWithCachedSeasons() {
        final Serie serie = generate(Serie.class);
        final Season season1 = generate(Season.class);
        season1.setSerie(serie);
        final int position1 = season1.getPosition();
        final Season season2 = generate(Season.class);
        season2.setSerie(serie);
        final int position2 = season2.getPosition();
        final List<Season> seasons = CollectionUtils.newList(season1, season2);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

        seasonService.moveUp(season2);
        DeepAsserts.assertEquals(position2, season1.getPosition());
        DeepAsserts.assertEquals(position1, season2.getPosition());

        verify(seasonDAO).update(season1);
        verify(seasonDAO).update(season2);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#moveUp(Season)} with not cached seasons. */
    @Test
    public void testMoveUpWithNotCachedSeasons() {
        final Serie serie = generate(Serie.class);
        final Season season1 = generate(Season.class);
        season1.setSerie(serie);
        final int position1 = season1.getPosition();
        final Season season2 = generate(Season.class);
        season2.setSerie(serie);
        final int position2 = season2.getPosition();
        final List<Season> seasons = CollectionUtils.newList(season1, season2);
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(serieCache.get(anyString())).thenReturn(null);

        seasonService.moveUp(season2);
        DeepAsserts.assertEquals(position2, season1.getPosition());
        DeepAsserts.assertEquals(position1, season2.getPosition());

        verify(seasonDAO).update(season1);
        verify(seasonDAO).update(season2);
        verify(seasonDAO).findSeasonsBySerie(serie);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#moveUp(Season)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testMoveUpWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.moveUp(mock(Season.class));
    }

    /** Test method for {@link SeasonService#moveUp(Season)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testMoveUpWithNotSetSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.moveUp(mock(Season.class));
    }

    /** Test method for {@link SeasonService#moveUp(Season)} with null argument. */
    @Test
    public void testMoveUpWithNullArgument() {
        try {
            seasonService.moveUp(null);
            fail("Can't move up season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#moveUp(Season)} with exception in DAO tier. */
    @Test
    public void testMoveUpWithDAOTierException() {
        final Serie serie = generate(Serie.class);
        final Season season = generate(Season.class);
        season.setSerie(serie);
        doThrow(DataStorageException.class).when(seasonDAO).findSeasonsBySerie(any(Serie.class));
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            seasonService.moveUp(season);
            fail("Can't move up season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).findSeasonsBySerie(serie);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#moveDown(Season)} with cached seasons. */
    @Test
    public void testMoveDownWithCachedSeasons() {
        final Serie serie = generate(Serie.class);
        final Season season1 = generate(Season.class);
        season1.setSerie(serie);
        final int position1 = season1.getPosition();
        final Season season2 = generate(Season.class);
        season2.setSerie(serie);
        final int position2 = season2.getPosition();
        final List<Season> seasons = CollectionUtils.newList(season1, season2);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

        seasonService.moveDown(season1);
        DeepAsserts.assertEquals(position2, season1.getPosition());
        DeepAsserts.assertEquals(position1, season2.getPosition());

        verify(seasonDAO).update(season1);
        verify(seasonDAO).update(season2);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#moveDown(Season)} with not cached seasons. */
    @Test
    public void testMoveDownWithNotCachedSeasons() {
        final Serie serie = generate(Serie.class);
        final Season season1 = generate(Season.class);
        season1.setSerie(serie);
        final int position1 = season1.getPosition();
        final Season season2 = generate(Season.class);
        season2.setSerie(serie);
        final int position2 = season2.getPosition();
        final List<Season> seasons = CollectionUtils.newList(season1, season2);
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(serieCache.get(anyString())).thenReturn(null);

        seasonService.moveDown(season1);
        DeepAsserts.assertEquals(position2, season1.getPosition());
        DeepAsserts.assertEquals(position1, season2.getPosition());

        verify(seasonDAO).update(season1);
        verify(seasonDAO).update(season2);
        verify(seasonDAO).findSeasonsBySerie(serie);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#moveDown(Season)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testMoveDownWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.moveDown(mock(Season.class));
    }

    /** Test method for {@link SeasonService#moveDown(Season)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testMoveDownWithNotSetSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.moveDown(mock(Season.class));
    }

    /** Test method for {@link SeasonService#moveDown(Season)} with null argument. */
    @Test
    public void testMoveDownWithNullArgument() {
        try {
            seasonService.moveDown(null);
            fail("Can't move down season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#moveDown(Season)} with exception in DAO tier. */
    @Test
    public void testMoveDownWithDAOTierException() {
        final Serie serie = generate(Serie.class);
        final Season season = generate(Season.class);
        season.setSerie(serie);
        doThrow(DataStorageException.class).when(seasonDAO).findSeasonsBySerie(any(Serie.class));
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            seasonService.moveDown(season);
            fail("Can't move down season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).findSeasonsBySerie(serie);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#exists(Season)} with cached existing season. */
    @Test
    public void testExistsWithCachedExistingSeason() {
        final Season season = generate(Season.class);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(season));

        assertTrue(seasonService.exists(season));

        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(seasonDAO);
    }

    /** Test method for {@link SeasonService#exists(Season)} with cached not existing season. */
    @Test
    public void testExistsWithCachedNotExistingSeason() {
        final Season season = generate(Season.class);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertFalse(seasonService.exists(season));

        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(seasonDAO);
    }

    /** Test method for {@link SeasonService#exists(Season)} with not cached existing season. */
    @Test
    public void testExistsWithNotCachedExistingSeason() {
        final Season season = generate(Season.class);
        when(seasonDAO.getSeason(anyInt())).thenReturn(season);
        when(serieCache.get(anyString())).thenReturn(null);

        assertTrue(seasonService.exists(season));

        verify(seasonDAO).getSeason(season.getId());
        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verify(serieCache).put(SEASON_CACHE_KEY + season.getId(), season);
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#exists(Season)} with not cached not existing season. */
    @Test
    public void testExistsWithNotCachedNotExistingSeason() {
        final Season season = generate(Season.class);
        when(seasonDAO.getSeason(anyInt())).thenReturn(null);
        when(serieCache.get(anyString())).thenReturn(null);

        assertFalse(seasonService.exists(season));

        verify(seasonDAO).getSeason(season.getId());
        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verify(serieCache).put(SEASON_CACHE_KEY + season.getId(), null);
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#exists(Season)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testExistsWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.exists(mock(Season.class));
    }

    /** Test method for {@link SeasonService#exists(Season)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testExistsWithNotSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.exists(mock(Season.class));
    }

    /** Test method for {@link SeasonService#exists(Season)} with null argument. */
    @Test
    public void testExistsWithNullArgument() {
        try {
            seasonService.exists(null);
            fail("Can't exists season with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#exists(Season)} with exception in DAO tier. */
    @Test
    public void testExistsWithDAOTierException() {
        final Season season = generate(Season.class);
        doThrow(DataStorageException.class).when(seasonDAO).getSeason(anyInt());
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            seasonService.exists(season);
            fail("Can't exists season with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).getSeason(season.getId());
        verify(serieCache).get(SEASON_CACHE_KEY + season.getId());
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#findSeasonsBySerie(Serie)} with cached seasons. */
    @Test
    public void testFindSeasonsBySerieWithCachedSeasons() {
        final Serie serie = generate(Serie.class);
        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

        DeepAsserts.assertEquals(seasons, seasonService.findSeasonsBySerie(serie));

        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(seasonDAO);
    }

    /** Test method for {@link SeasonService#findSeasonsBySerie(Serie)} with not cached seasons. */
    @Test
    public void testFindSeasonsBySerieWithNotCachedSeasons() {
        final Serie serie = generate(Serie.class);
        final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(serieCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(seasons, seasonService.findSeasonsBySerie(serie));

        verify(seasonDAO).findSeasonsBySerie(serie);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verify(serieCache).put(SEASONS_CACHE_KEY + serie.getId(), seasons);
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#findSeasonsBySerie(Serie)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testFindSeasonsBySerieWithNotSetSeasonDAO() {
        ((SeasonServiceImpl) seasonService).setSeasonDAO(null);
        seasonService.findSeasonsBySerie(mock(Serie.class));
    }

    /** Test method for {@link SeasonService#findSeasonsBySerie(Serie)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testFindSeasonsBySerieWithNotSetSerieCache() {
        ((SeasonServiceImpl) seasonService).setSerieCache(null);
        seasonService.findSeasonsBySerie(mock(Serie.class));
    }

    /** Test method for {@link SeasonService#findSeasonsBySerie(Serie)} with null argument. */
    @Test
    public void testFindSeasonsBySerieWithNullArgument() {
        try {
            seasonService.findSeasonsBySerie(null);
            fail("Can't find seasons by serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(seasonDAO, serieCache);
    }

    /** Test method for {@link SeasonService#findSeasonsBySerie(Serie)} with exception in DAO tier. */
    @Test
    public void testFindSeasonsBySerieWithDAOTierException() {
        final Serie serie = generate(Serie.class);
        doThrow(DataStorageException.class).when(seasonDAO).findSeasonsBySerie(any(Serie.class));
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            seasonService.findSeasonsBySerie(serie);
            fail("Can't find seasons by serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).findSeasonsBySerie(serie);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(seasonDAO, serieCache);
    }

}
