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
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.SerieDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SerieService;
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
 * A class represents test for class {@link SerieServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SerieServiceImplTest extends ObjectGeneratorTest {

    /** Cache key for list of series */
    private static final String SERIES_CACHE_KEY = "series";

    /** Cache key for book serie */
    private static final String SERIE_CACHE_KEY = "serie";

    /** Cache key for list of seasons */
    private static final String SEASONS_CACHE_KEY = "seasons";

    /** Cache key for list of episodes */
    private static final String EPISODES_CACHE_KEY = "episodes";

    /** Instance of {@link SerieDAO} */
    @Mock
    private SerieDAO serieDAO;

    /** Instance of {@link SeasonDAO} */
    @Mock
    private SeasonDAO seasonDAO;

    /** Instance of {@link EpisodeDAO} */
    @Mock
    private EpisodeDAO episodeDAO;

    /** Instance of {@link Cache} */
    @Mock
    private Cache serieCache;

    /** Instance of {@link SerieService} */
    @InjectMocks
    private SerieService serieService = new SerieServiceImpl();

    /** Test method for {@link SerieService#newData()} with cached data. */
    @Test
    public void testNewDataWithCachedData() {
        final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(serieCache.get(SERIES_CACHE_KEY)).thenReturn(new SimpleValueWrapper(series));
        for (final Serie serie : series) {
            when(serieCache.get(SEASONS_CACHE_KEY + serie.getId())).thenReturn(new SimpleValueWrapper(seasons));
        }
        for (final Season season : seasons) {
            when(serieCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
        }

        serieService.newData();

        for (final Serie serie : series) {
            verify(serieDAO).remove(serie);
            verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        }
        for (final Season season : seasons) {
            verify(seasonDAO, times(series.size())).remove(season);
            verify(serieCache, times(series.size())).get(EPISODES_CACHE_KEY + season.getId());
        }
        for (final Episode episode : episodes) {
            verify(episodeDAO, times(series.size() * seasons.size())).remove(episode);
        }
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#newData()} with not cached data. */
    @Test
    public void testNewDataWithNotCachedData() {
        final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(serieDAO.getSeries()).thenReturn(series);
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(serieCache.get(anyString())).thenReturn(null);

        serieService.newData();

        verify(serieDAO).getSeries();
        for (final Serie serie : series) {
            verify(serieDAO).remove(serie);
            verify(seasonDAO).findSeasonsBySerie(serie);
            verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        }
        for (final Season season : seasons) {
            verify(seasonDAO, times(series.size())).remove(season);
            verify(episodeDAO, times(series.size())).findEpisodesBySeason(season);
            verify(serieCache, times(series.size())).get(EPISODES_CACHE_KEY + season.getId());
        }
        for (final Episode episode : episodes) {
            verify(episodeDAO, times(series.size() * seasons.size())).remove(episode);
        }
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#newData()} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testNewDataWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.newData();
    }

    /** Test method for {@link SerieService#newData()} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testNewDataWithNotSetSeasonDAO() {
        ((SerieServiceImpl) serieService).setSeasonDAO(null);
        serieService.newData();
    }

    /** Test method for {@link SerieService#newData()} with not set DAO for episodes. */
    @Test(expected = IllegalStateException.class)
    public void testNewDataWithNotSetEpisodeDAO() {
        ((SerieServiceImpl) serieService).setEpisodeDAO(null);
        serieService.newData();
    }

    /** Test method for {@link SerieService#newData()} with not set serie cache. */
    @Test(expected = IllegalStateException.class)
    public void testNewDataWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.newData();
    }

    /** Test method for {@link SerieService#newData()} with exception in DAO tier. */
    @Test
    public void testNewDataWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSeries();
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.newData();
            fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieDAO, serieCache);
        verifyZeroInteractions(seasonDAO, episodeDAO);
    }

    /** Test method for {@link SerieService#getSeries()} with cached series. */
    @Test
    public void testGetSeriesWithCachedSeries() {
        final List<Serie> series = CollectionUtils.newList(mock(Serie.class), mock(Serie.class));
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(series));

        DeepAsserts.assertEquals(series, serieService.getSeries());

        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(serieDAO);
    }

    /** Test method for {@link SerieService#getSeries()} with not cached series. */
    @Test
    public void testGetSeriesWithNotCachedSeries() {
        final List<Serie> series = CollectionUtils.newList(mock(Serie.class), mock(Serie.class));
        when(serieDAO.getSeries()).thenReturn(series);
        when(serieCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(series, serieService.getSeries());

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).put(SERIES_CACHE_KEY, series);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#getSeries()} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetSeriesWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.getSeries();
    }

    /** Test method for {@link SerieService#getSeries()} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetSeriesWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.getSeries();
    }

    /** Test method for {@link SerieService#getSeries()} with exception in DAO tier. */
    @Test
    public void testGetSeriesWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSeries();
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.getSeries();
            fail("Can't get series with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#getSerie(Integer)} with cached existing serie. */
    @Test
    public void testGetSerieWithCachedExistingSerie() {
        final Serie serie = generate(Serie.class);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(serie));

        DeepAsserts.assertEquals(serie, serieService.getSerie(serie.getId()));

        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(serieDAO);
    }

    /** Test method for {@link SerieService#getSerie(Integer)} with cached not existing serie. */
    @Test
    public void testGetSerieWithCachedNotExistingSerie() {
        final Serie serie = generate(Serie.class);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertNull(serieService.getSerie(serie.getId()));

        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(serieDAO);
    }

    /** Test method for {@link SerieService#getSerie(Integer)} with not cached existing serie. */
    @Test
    public void testGetSerieWithNotCachedExistingSerie() {
        final Serie serie = generate(Serie.class);
        when(serieDAO.getSerie(anyInt())).thenReturn(serie);
        when(serieCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(serie, serieService.getSerie(serie.getId()));

        verify(serieDAO).getSerie(serie.getId());
        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verify(serieCache).put(SERIE_CACHE_KEY + serie.getId(), serie);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#getSerie(Integer)} with not cached not existing serie. */
    @Test
    public void testGetSerieWithNotCachedNotExistingSerie() {
        final Serie serie = generate(Serie.class);
        when(serieDAO.getSerie(anyInt())).thenReturn(null);
        when(serieCache.get(anyString())).thenReturn(null);

        assertNull(serieService.getSerie(serie.getId()));

        verify(serieDAO).getSerie(serie.getId());
        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verify(serieCache).put(SERIE_CACHE_KEY + serie.getId(), null);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#getSerie(Integer)} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetSerieWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.getSerie(Integer.MAX_VALUE);
    }

    /** Test method for {@link SerieService#getSerie(Integer)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetSerieWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.getSerie(Integer.MAX_VALUE);
    }

    /** Test method for {@link SerieService#getSerie(Integer)} with null argument. */
    @Test
    public void testGetSerieWithNullArgument() {
        try {
            serieService.getSerie(null);
            fail("Can't get serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#getSerie(Integer)} with exception in DAO tier. */
    @Test
    public void testGetSerieWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSerie(anyInt());
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.getSerie(Integer.MAX_VALUE);
            fail("Can't get serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSerie(Integer.MAX_VALUE);
        verify(serieCache).get(SERIE_CACHE_KEY + Integer.MAX_VALUE);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#add(Serie)} with cached series. */
    @Test
    public void testAddWithCachedSeries() {
        final Serie serie = generate(Serie.class);
        final List<Serie> series = CollectionUtils.newList(mock(Serie.class), mock(Serie.class));
        final List<Serie> seriesList = new ArrayList<>(series);
        seriesList.add(serie);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(series));

        serieService.add(serie);

        verify(serieDAO).add(serie);
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verify(serieCache).put(SERIES_CACHE_KEY, seriesList);
        verify(serieCache).put(SERIE_CACHE_KEY + serie.getId(), serie);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#add(Serie)} with not cached series. */
    @Test
    public void testAddWithNotCachedSeries() {
        final Serie serie = generate(Serie.class);
        when(serieCache.get(anyString())).thenReturn(null);

        serieService.add(serie);

        verify(serieDAO).add(serie);
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#add(Serie)} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.add(mock(Serie.class));
    }

    /** Test method for {@link SerieService#add(Serie)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testAddWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.add(mock(Serie.class));
    }

    /** Test method for {@link SerieService#add(Serie)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        try {
            serieService.add(null);
            fail("Can't add serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#add(Serie)} with exception in DAO tier. */
    @Test
    public void testAddWithDAOTierException() {
        final Serie serie = generate(Serie.class);
        doThrow(DataStorageException.class).when(serieDAO).add(any(Serie.class));

        try {
            serieService.add(serie);
            fail("Can't add serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).add(serie);
        verifyNoMoreInteractions(serieDAO);
        verifyZeroInteractions(serieCache);
    }

    /** Test method for {@link SerieService#update(Serie)}. */
    @Test
    public void testUpdate() {
        final Serie serie = generate(Serie.class);

        serieService.update(serie);

        verify(serieDAO).update(serie);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#update(Serie)} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.update(mock(Serie.class));
    }

    /** Test method for {@link SerieService#update(Serie)} with not set serie cache. */
    @Test(expected = IllegalStateException.class)
    public void testUpdateWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.update(mock(Serie.class));
    }

    /** Test method for {@link SerieService#update(Serie)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            serieService.update(null);
            fail("Can't update serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#update(Serie)} with exception in DAO tier. */
    @Test
    public void testUpdateWithDAOTierException() {
        final Serie serie = generate(Serie.class);
        doThrow(DataStorageException.class).when(serieDAO).update(any(Serie.class));

        try {
            serieService.update(serie);
            fail("Can't update serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).update(serie);
        verifyNoMoreInteractions(serieDAO);
        verifyZeroInteractions(serieCache);
    }

    /** Test method for {@link SerieService#remove(Serie)} with cached data. */
    @Test
    public void testRemoveWithCachedData() {
        final Serie serie = generate(Serie.class);
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(serieCache.get(SEASONS_CACHE_KEY + serie.getId())).thenReturn(new SimpleValueWrapper(seasons));
        for (final Season season : seasons) {
            when(serieCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
        }

        serieService.remove(serie);

        verify(serieDAO).remove(serie);
        for (final Season season : seasons) {
            verify(seasonDAO).remove(season);
            verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        }
        for (final Episode episode : episodes) {
            verify(episodeDAO, times(seasons.size())).remove(episode);
        }
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#remove(Serie)} with not cached data. */
    @Test
    public void testRemoveWithNotCachedData() {
        final Serie serie = generate(Serie.class);
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(serieCache.get(anyString())).thenReturn(null);

        serieService.remove(serie);

        verify(serieDAO).remove(serie);
        verify(seasonDAO).findSeasonsBySerie(serie);
        for (final Season season : seasons) {
            verify(seasonDAO).remove(season);
            verify(episodeDAO).findEpisodesBySeason(season);
            verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        }
        for (final Episode episode : episodes) {
            verify(episodeDAO, times(seasons.size())).remove(episode);
        }
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#remove(Serie)} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.remove(mock(Serie.class));
    }

    /** Test method for {@link SerieService#remove(Serie)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetSeasonDAO() {
        ((SerieServiceImpl) serieService).setSeasonDAO(null);
        serieService.remove(mock(Serie.class));
    }

    /** Test method for {@link SerieService#remove(Serie)} with not set DAO for episodes. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetEpisodeDAO() {
        ((SerieServiceImpl) serieService).setEpisodeDAO(null);
        serieService.remove(mock(Serie.class));
    }

    /** Test method for {@link SerieService#remove(Serie)} with not set serie cache. */
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.remove(mock(Serie.class));
    }

    /** Test method for {@link SerieService#remove(Serie)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            serieService.remove(null);
            fail("Can't remove serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#remove(Serie)} with exception in DAO tier. */
    @Test
    public void testRemoveWithDAOTierException() {
        final Serie serie = generate(Serie.class);
        doThrow(DataStorageException.class).when(seasonDAO).findSeasonsBySerie(any(Serie.class));
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.remove(serie);
            fail("Can't remove serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(seasonDAO).findSeasonsBySerie(serie);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(seasonDAO, serieCache);
        verifyZeroInteractions(serieDAO, episodeDAO);
    }

    /** Test method for {@link SerieService#duplicate(Serie)} with cached data. */
    @Test
    public void testDuplicateWithCachedData() {
        final Serie serie = generate(Serie.class);
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(serieCache.get(SEASONS_CACHE_KEY + serie.getId())).thenReturn(new SimpleValueWrapper(seasons));
        for (final Season season : seasons) {
            when(serieCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
        }

        serieService.duplicate(serie);

        verify(serieDAO).add(any(Serie.class));
        verify(serieDAO).update(any(Serie.class));
        verify(seasonDAO, times(seasons.size())).add(any(Season.class));
        verify(seasonDAO, times(seasons.size())).update(any(Season.class));
        verify(episodeDAO, times(seasons.size() * episodes.size())).add(any(Episode.class));
        verify(episodeDAO, times(seasons.size() * episodes.size())).update(any(Episode.class));
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        for (final Season season : seasons) {
            verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        }
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#duplicate(Serie)} with not cached data. */
    @Test
    public void testDuplicateWithNotCachedData() {
        final Serie serie = generate(Serie.class);
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(serieCache.get(anyString())).thenReturn(null);

        serieService.duplicate(serie);

        verify(serieDAO).add(any(Serie.class));
        verify(serieDAO).update(any(Serie.class));
        verify(seasonDAO, times(seasons.size())).add(any(Season.class));
        verify(seasonDAO, times(seasons.size())).update(any(Season.class));
        verify(seasonDAO).findSeasonsBySerie(serie);
        verify(episodeDAO, times(seasons.size() * episodes.size())).add(any(Episode.class));
        verify(episodeDAO, times(seasons.size() * episodes.size())).update(any(Episode.class));
        for (final Season season : seasons) {
            verify(episodeDAO).findEpisodesBySeason(season);
        }
        verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        for (final Season season : seasons) {
            verify(serieCache).get(EPISODES_CACHE_KEY + season.getId());
        }
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#duplicate(Serie)} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.duplicate(mock(Serie.class));
    }

    /** Test method for {@link SerieService#duplicate(Serie)} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetSeasonDAO() {
        ((SerieServiceImpl) serieService).setSeasonDAO(null);
        serieService.duplicate(mock(Serie.class));
    }

    /** Test method for {@link SerieService#duplicate(Serie)} with not set DAO for episodes. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetEpisodeDAO() {
        ((SerieServiceImpl) serieService).setEpisodeDAO(null);
        serieService.duplicate(mock(Serie.class));
    }

    /** Test method for {@link SerieService#duplicate(Serie)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testDuplicateWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.duplicate(mock(Serie.class));
    }

    /** Test method for {@link SerieService#duplicate(Serie)} with null argument. */
    @Test
    public void testDuplicateWithNullArgument() {
        try {
            serieService.duplicate(null);
            fail("Can't duplicate serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#duplicate(Serie)} with exception in DAO tier. */
    @Test
    public void testDuplicateWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).add(any(Serie.class));

        try {
            serieService.duplicate(generate(Serie.class));
            fail("Can't duplicate serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).add(any(Serie.class));
        verifyNoMoreInteractions(serieDAO);
        verifyZeroInteractions(seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#moveUp(Serie)} with cached series. */
    @Test
    public void testMoveUpWithCachedSeries() {
        final Serie serie1 = generate(Serie.class);
        final int position1 = serie1.getPosition();
        final Serie serie2 = generate(Serie.class);
        final int position2 = serie2.getPosition();
        final List<Serie> series = CollectionUtils.newList(serie1, serie2);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(series));

        serieService.moveUp(serie2);
        DeepAsserts.assertEquals(position2, serie1.getPosition());
        DeepAsserts.assertEquals(position1, serie2.getPosition());

        verify(serieDAO).update(serie1);
        verify(serieDAO).update(serie2);
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#moveUp(Serie)} with not cached series. */
    @Test
    public void testMoveUpWithNotCachedSeries() {
        final Serie serie1 = generate(Serie.class);
        final int position1 = serie1.getPosition();
        final Serie serie2 = generate(Serie.class);
        final int position2 = serie2.getPosition();
        final List<Serie> series = CollectionUtils.newList(serie1, serie2);
        when(serieDAO.getSeries()).thenReturn(series);
        when(serieCache.get(anyString())).thenReturn(null);

        serieService.moveUp(serie2);
        DeepAsserts.assertEquals(position2, serie1.getPosition());
        DeepAsserts.assertEquals(position1, serie2.getPosition());

        verify(serieDAO).update(serie1);
        verify(serieDAO).update(serie2);
        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#moveUp(Serie)} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testMoveUpWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.moveUp(mock(Serie.class));
    }

    /** Test method for {@link SerieService#moveUp(Serie)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testMoveUpWithNotSetSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.moveUp(mock(Serie.class));
    }

    /** Test method for {@link SerieService#moveUp(Serie)} with null argument. */
    @Test
    public void testMoveUpWithNullArgument() {
        try {
            serieService.moveUp(null);
            fail("Can't move up serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#moveUp(Serie)} with exception in DAO tier. */
    @Test
    public void testMoveUpWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSeries();
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.moveUp(generate(Serie.class));
            fail("Can't move up serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#moveDown(Serie)} with cached series. */
    @Test
    public void testMoveDownWithCachedSeries() {
        final Serie serie1 = generate(Serie.class);
        final int position1 = serie1.getPosition();
        final Serie serie2 = generate(Serie.class);
        final int position2 = serie2.getPosition();
        final List<Serie> series = CollectionUtils.newList(serie1, serie2);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(series));

        serieService.moveDown(serie1);
        DeepAsserts.assertEquals(position2, serie1.getPosition());
        DeepAsserts.assertEquals(position1, serie2.getPosition());

        verify(serieDAO).update(serie1);
        verify(serieDAO).update(serie2);
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#moveDown(Serie)} with not cached series. */
    @Test
    public void testMoveDownWithNotCachedSeries() {
        final Serie serie1 = generate(Serie.class);
        final int position1 = serie1.getPosition();
        final Serie serie2 = generate(Serie.class);
        final int position2 = serie2.getPosition();
        final List<Serie> series = CollectionUtils.newList(serie1, serie2);
        when(serieDAO.getSeries()).thenReturn(series);
        when(serieCache.get(anyString())).thenReturn(null);

        serieService.moveDown(serie1);
        DeepAsserts.assertEquals(position2, serie1.getPosition());
        DeepAsserts.assertEquals(position1, serie2.getPosition());

        verify(serieDAO).update(serie1);
        verify(serieDAO).update(serie2);
        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#moveDown(Serie)} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testMoveDownWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.moveDown(mock(Serie.class));
    }

    /** Test method for {@link SerieService#moveDown(Serie)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testMoveDownWithNotSetSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.moveDown(mock(Serie.class));
    }

    /** Test method for {@link SerieService#moveDown(Serie)} with null argument. */
    @Test
    public void testMoveDownWithNullArgument() {
        try {
            serieService.moveDown(null);
            fail("Can't move down serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#moveDown(Serie)} with exception in DAO tier. */
    @Test
    public void testMoveDownWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSeries();
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.moveDown(generate(Serie.class));
            fail("Can't move down serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#exists(Serie)} with cached existing serie. */
    @Test
    public void testExistsWithCachedExistingSerie() {
        final Serie serie = generate(Serie.class);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(serie));

        assertTrue(serieService.exists(serie));

        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(serieDAO);
    }

    /** Test method for {@link SerieService#exists(Serie)} with cached not existing serie. */
    @Test
    public void testExistsWithCachedNotExistingSerie() {
        final Serie serie = generate(Serie.class);
        when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertFalse(serieService.exists(serie));

        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(serieDAO);
    }

    /** Test method for {@link SerieService#exists(Serie)} with not cached existing serie. */
    @Test
    public void testExistsWithNotCachedExistingSerie() {
        final Serie serie = generate(Serie.class);
        when(serieDAO.getSerie(anyInt())).thenReturn(serie);
        when(serieCache.get(anyString())).thenReturn(null);

        assertTrue(serieService.exists(serie));

        verify(serieDAO).getSerie(serie.getId());
        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verify(serieCache).put(SERIE_CACHE_KEY + serie.getId(), serie);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#exists(Serie)} with not cached not existing serie. */
    @Test
    public void testExistsWithNotCachedNotExistingSerie() {
        final Serie serie = generate(Serie.class);
        when(serieDAO.getSerie(anyInt())).thenReturn(null);
        when(serieCache.get(anyString())).thenReturn(null);

        assertFalse(serieService.exists(serie));

        verify(serieDAO).getSerie(serie.getId());
        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verify(serieCache).put(SERIE_CACHE_KEY + serie.getId(), null);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#exists(Serie)} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testExistsWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.exists(mock(Serie.class));
    }

    /** Test method for {@link SerieService#exists(Serie)} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testExistsWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.exists(mock(Serie.class));
    }

    /** Test method for {@link SerieService#exists(Serie)} with null argument. */
    @Test
    public void testExistsWithNullArgument() {
        try {
            serieService.exists(null);
            fail("Can't exists serie with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#exists(Serie)} with exception in DAO tier. */
    @Test
    public void testExistsWithDAOTierException() {
        final Serie serie = generate(Serie.class);
        doThrow(DataStorageException.class).when(serieDAO).getSerie(anyInt());
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.exists(serie);
            fail("Can't exists serie with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSerie(serie.getId());
        verify(serieCache).get(SERIE_CACHE_KEY + serie.getId());
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#updatePositions()} with cached data. */
    @Test
    public void testUpdatePositionsWithCachedData() {
        final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(generate(Episode.class), generate(Episode.class));
        when(serieCache.get(SERIES_CACHE_KEY)).thenReturn(new SimpleValueWrapper(series));
        for (final Serie serie : series) {
            when(serieCache.get(SEASONS_CACHE_KEY + serie.getId())).thenReturn(new SimpleValueWrapper(seasons));
        }
        for (final Season season : seasons) {
            when(serieCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
        }

        serieService.updatePositions();

        for (int i = 0; i < series.size(); i++) {
            final Serie serie = series.get(i);
            DeepAsserts.assertEquals(i, serie.getPosition());
            verify(serieDAO).update(serie);
            verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        }
        for (int i = 0; i < seasons.size(); i++) {
            final Season season = seasons.get(i);
            DeepAsserts.assertEquals(i, season.getPosition());
            verify(seasonDAO, times(series.size())).update(season);
            verify(serieCache, times(series.size())).get(EPISODES_CACHE_KEY + season.getId());
        }
        for (int i = 0; i < episodes.size(); i++) {
            final Episode episode = episodes.get(i);
            DeepAsserts.assertEquals(i, episode.getPosition());
            verify(episodeDAO, times(series.size() * seasons.size())).update(episode);
        }
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#updatePositions()} with not cached data. */
    @Test
    public void testUpdatePositionsWithNotCachedData() {
        final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(generate(Episode.class), generate(Episode.class));
        when(serieDAO.getSeries()).thenReturn(series);
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(serieCache.get(anyString())).thenReturn(null);

        serieService.updatePositions();

        verify(serieDAO).getSeries();
        for (int i = 0; i < series.size(); i++) {
            final Serie serie = series.get(i);
            DeepAsserts.assertEquals(i, serie.getPosition());
            verify(serieDAO).update(serie);
            verify(seasonDAO).findSeasonsBySerie(serie);
            verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        }
        for (int i = 0; i < seasons.size(); i++) {
            final Season season = seasons.get(i);
            DeepAsserts.assertEquals(i, season.getPosition());
            verify(seasonDAO, times(series.size())).update(season);
            verify(episodeDAO, times(series.size())).findEpisodesBySeason(season);
            verify(serieCache, times(series.size())).get(EPISODES_CACHE_KEY + season.getId());
        }
        for (int i = 0; i < episodes.size(); i++) {
            final Episode episode = episodes.get(i);
            DeepAsserts.assertEquals(i, episode.getPosition());
            verify(episodeDAO, times(series.size() * seasons.size())).update(episode);
        }
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).clear();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#updatePositions()} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testUpdatePositionsWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.updatePositions();
    }

    /** Test method for {@link SerieService#updatePositions()} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testUpdatePositionsWithNotSetSeasonDAO() {
        ((SerieServiceImpl) serieService).setSeasonDAO(null);
        serieService.updatePositions();
    }

    /** Test method for {@link SerieService#updatePositions()} with not set DAO for episodes. */
    @Test(expected = IllegalStateException.class)
    public void testUpdatePositionsWithNotSetEpisodDAO() {
        ((SerieServiceImpl) serieService).setEpisodeDAO(null);
        serieService.updatePositions();
    }

    /** Test method for {@link SerieService#updatePositions()} with not set serie cache. */
    @Test(expected = IllegalStateException.class)
    public void testUpdatePositionsWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.updatePositions();
    }

    /** Test method for {@link SerieService#updatePositions()} with exception in DAO tier. */
    @Test
    public void testUpdatePositionsWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSeries();
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.updatePositions();
            fail("Can't update positions with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieDAO, serieCache);
        verifyZeroInteractions(seasonDAO, episodeDAO);
    }

    /** Test method for {@link SerieService#getTotalLength()} with cached series. */
    @Test
    public void testGetTotalLengthWithCachedSeries() {
        final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final Episode episode1 = mock(Episode.class);
        final Episode episode2 = mock(Episode.class);
        final List<Episode> episodes = CollectionUtils.newList(episode1, episode2);
        final int[] lengths = new int[2];
        int totalLength = 0;
        for (int i = 0; i < 2; i++) {
            final int length = generate(Integer.class);
            lengths[i] = length;
            totalLength += length;
        }
        when(serieCache.get(SERIES_CACHE_KEY)).thenReturn(new SimpleValueWrapper(series));
        for (final Serie serie : series) {
            when(serieCache.get(SEASONS_CACHE_KEY + serie.getId())).thenReturn(new SimpleValueWrapper(seasons));
        }
        for (final Season season : seasons) {
            when(serieCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
        }
        when(episode1.getLength()).thenReturn(lengths[0]);
        when(episode2.getLength()).thenReturn(lengths[1]);

        DeepAsserts.assertEquals(new Time(series.size() * seasons.size() * totalLength), serieService.getTotalLength());

        verify(serieCache).get(SERIES_CACHE_KEY);
        for (final Serie serie : series) {
            verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        }
        for (final Season season : seasons) {
            verify(serieCache, times(series.size())).get(EPISODES_CACHE_KEY + season.getId());
        }
        verify(episode1, times(series.size() * seasons.size())).getLength();
        verify(episode2, times(series.size() * seasons.size())).getLength();
        verifyNoMoreInteractions(serieCache, episode1, episode2);
        verifyZeroInteractions(serieDAO, seasonDAO, episodeDAO);
    }

    /** Test method for {@link SerieService#getTotalLength()} with not cached series. */
    @Test
    public void testGetTotalLengthWithNotCachedSeries() {
        final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final Episode episode1 = mock(Episode.class);
        final Episode episode2 = mock(Episode.class);
        final List<Episode> episodes = CollectionUtils.newList(episode1, episode2);
        final int[] lengths = new int[2];
        int totalLength = 0;
        for (int i = 0; i < 2; i++) {
            final int length = generate(Integer.class);
            lengths[i] = length;
            totalLength += length;
        }
        when(serieDAO.getSeries()).thenReturn(series);
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(serieCache.get(anyString())).thenReturn(null);
        when(episode1.getLength()).thenReturn(lengths[0]);
        when(episode2.getLength()).thenReturn(lengths[1]);

        DeepAsserts.assertEquals(new Time(series.size() * seasons.size() * totalLength), serieService.getTotalLength());

        verify(serieDAO).getSeries();
        for (final Serie serie : series) {
            verify(seasonDAO).findSeasonsBySerie(serie);
            verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
            verify(serieCache).put(SEASONS_CACHE_KEY + serie.getId(), seasons);
        }
        for (final Season season : seasons) {
            verify(episodeDAO, times(series.size())).findEpisodesBySeason(season);
            verify(serieCache, times(series.size())).get(EPISODES_CACHE_KEY + season.getId());
            verify(serieCache, times(series.size())).put(EPISODES_CACHE_KEY + season.getId(), episodes);
        }
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).put(SERIES_CACHE_KEY, series);
        verify(episode1, times(series.size() * seasons.size())).getLength();
        verify(episode2, times(series.size() * seasons.size())).getLength();
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache, episode1, episode2);
    }

    /** Test method for {@link SerieService#getTotalLength()} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetTotalLengthWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.getTotalLength();
    }

    /** Test method for {@link SerieService#getTotalLength()} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testGetTotalLengthWithNotSetSeasonDAO() {
        ((SerieServiceImpl) serieService).setSeasonDAO(null);
        serieService.getTotalLength();
    }

    /** Test method for {@link SerieService#getTotalLength()} with not set DAO for episodes. */
    @Test(expected = IllegalStateException.class)
    public void testGetTotalLengthWithNotSetEpisodeDAO() {
        ((SerieServiceImpl) serieService).setEpisodeDAO(null);
        serieService.getTotalLength();
    }

    /** Test method for {@link SerieService#getTotalLength()} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetTotalLengthWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.getTotalLength();
    }

    /** Test method for {@link SerieService#getTotalLength()} with exception in DAO tier. */
    @Test
    public void testGetTotalLengthWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSeries();
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.getTotalLength();
            fail("Can't get total length with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieDAO, serieCache);
    }

    /** Test method for {@link SerieService#getSeasonsCount()} with cached data. */
    @Test
    public void testGetSeasonsCountWithCachedData() {
        final Serie serie1 = generate(Serie.class);
        final Serie serie2 = generate(Serie.class);
        final List<Serie> series = CollectionUtils.newList(serie1, serie2);
        final List<Season> seasons1 = CollectionUtils.newList(mock(Season.class));
        final List<Season> seasons2 = CollectionUtils.newList(mock(Season.class), mock(Season.class));
        when(serieCache.get(SERIES_CACHE_KEY)).thenReturn(new SimpleValueWrapper(series));
        when(serieCache.get(SEASONS_CACHE_KEY + serie1.getId())).thenReturn(new SimpleValueWrapper(seasons1));
        when(serieCache.get(SEASONS_CACHE_KEY + serie2.getId())).thenReturn(new SimpleValueWrapper(seasons2));

        DeepAsserts.assertEquals(seasons1.size() + seasons2.size(), serieService.getSeasonsCount());

        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie1.getId());
        verify(serieCache).get(SEASONS_CACHE_KEY + serie2.getId());
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(serieDAO, seasonDAO);
    }

    /** Test method for {@link SerieService#getSeasonsCount()} with not cached data. */
    @Test
    public void testGetSeasonsCountWithNotCachedSeasonCategories() {
        final Serie serie1 = generate(Serie.class);
        final Serie serie2 = generate(Serie.class);
        final List<Serie> series = CollectionUtils.newList(serie1, serie2);
        final List<Season> seasons1 = CollectionUtils.newList(mock(Season.class));
        final List<Season> seasons2 = CollectionUtils.newList(mock(Season.class), mock(Season.class));
        when(serieDAO.getSeries()).thenReturn(series);
        when(seasonDAO.findSeasonsBySerie(serie1)).thenReturn(seasons1);
        when(seasonDAO.findSeasonsBySerie(serie2)).thenReturn(seasons2);
        when(serieCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(seasons1.size() + seasons2.size(), serieService.getSeasonsCount());

        verify(serieDAO).getSeries();
        verify(seasonDAO).findSeasonsBySerie(serie1);
        verify(seasonDAO).findSeasonsBySerie(serie2);
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).put(SERIES_CACHE_KEY, series);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie1.getId());
        verify(serieCache).put(SEASONS_CACHE_KEY + serie1.getId(), seasons1);
        verify(serieCache).get(SEASONS_CACHE_KEY + serie2.getId());
        verify(serieCache).put(SEASONS_CACHE_KEY + serie2.getId(), seasons2);
        verifyNoMoreInteractions(serieDAO, seasonDAO, serieCache);
    }

    /** Test method for {@link SerieService#getSeasonsCount()} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetSeasonsCountWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.getSeasonsCount();
    }

    /** Test method for {@link SerieService#getSeasonsCount()} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testGetSeasonsCountWithNotSetSeasonDAO() {
        ((SerieServiceImpl) serieService).setSeasonDAO(null);
        serieService.getSeasonsCount();
    }

    /** Test method for {@link SerieService#getSeasonsCount()} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetSeasonsCountWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.getSeasonsCount();
    }

    /** Test method for {@link SerieService#getSeasonsCount()} with exception in DAO tier. */
    @Test
    public void testGetSeasonsCountWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSeries();
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.getSeasonsCount();
            fail("Can't get seasons count with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieDAO, serieCache);
        verifyZeroInteractions(seasonDAO);
    }

    /** Test method for {@link SerieService#getEpisodesCount()} with cached data. */
    @Test
    public void testGetEpisodesCountWithCachedData() {
        final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(serieCache.get(SERIES_CACHE_KEY)).thenReturn(new SimpleValueWrapper(series));
        for (final Serie serie : series) {
            when(serieCache.get(SEASONS_CACHE_KEY + serie.getId())).thenReturn(new SimpleValueWrapper(seasons));
        }
        for (final Season season : seasons) {
            when(serieCache.get(EPISODES_CACHE_KEY + season.getId())).thenReturn(new SimpleValueWrapper(episodes));
        }

        DeepAsserts.assertEquals(series.size() * seasons.size() * episodes.size(), serieService.getEpisodesCount());

        verify(serieCache).get(SERIES_CACHE_KEY);
        for (final Serie serie : series) {
            verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
        }
        for (final Season season : seasons) {
            verify(serieCache, times(series.size())).get(EPISODES_CACHE_KEY + season.getId());
        }
        verifyNoMoreInteractions(serieCache);
        verifyZeroInteractions(serieDAO, seasonDAO, episodeDAO);
    }

    /** Test method for {@link SerieService#getEpisodesCount()} with not cached data. */
    @Test
    public void testGetEpisodesCountWithNotCachedSeasonCategories() {
        final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
        final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
        when(serieDAO.getSeries()).thenReturn(series);
        when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
        when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(serieCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(series.size() * seasons.size() * episodes.size(), serieService.getEpisodesCount());

        verify(serieDAO).getSeries();
        for (final Serie serie : series) {
            verify(seasonDAO).findSeasonsBySerie(serie);
            verify(serieCache).get(SEASONS_CACHE_KEY + serie.getId());
            verify(serieCache).put(SEASONS_CACHE_KEY + serie.getId(), seasons);
        }
        for (final Season season : seasons) {
            verify(episodeDAO, times(series.size())).findEpisodesBySeason(season);
            verify(serieCache, times(series.size())).get(EPISODES_CACHE_KEY + season.getId());
            verify(serieCache, times(series.size())).put(EPISODES_CACHE_KEY + season.getId(), episodes);
        }
        verify(serieCache).get(SERIES_CACHE_KEY);
        verify(serieCache).put(SERIES_CACHE_KEY, series);
        verifyNoMoreInteractions(serieDAO, seasonDAO, episodeDAO, serieCache);
    }

    /** Test method for {@link SerieService#getEpisodesCount()} with not set DAO for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetEpisodesCountWithNotSetSerieDAO() {
        ((SerieServiceImpl) serieService).setSerieDAO(null);
        serieService.getEpisodesCount();
    }

    /** Test method for {@link SerieService#getEpisodesCount()} with not set DAO for seasons. */
    @Test(expected = IllegalStateException.class)
    public void testGetEpisodesCountWithNotSetSeasonDAO() {
        ((SerieServiceImpl) serieService).setSeasonDAO(null);
        serieService.getEpisodesCount();
    }

    /** Test method for {@link SerieService#getEpisodesCount()} with not set DAO for episodes. */
    @Test(expected = IllegalStateException.class)
    public void testGetEpisodesCountWithNotSetEpisodeDAO() {
        ((SerieServiceImpl) serieService).setEpisodeDAO(null);
        serieService.getEpisodesCount();
    }

    /** Test method for {@link SerieService#getEpisodesCount()} with not set cache for series. */
    @Test(expected = IllegalStateException.class)
    public void testGetEpisodesCountWithNotSetSerieCache() {
        ((SerieServiceImpl) serieService).setSerieCache(null);
        serieService.getEpisodesCount();
    }

    /** Test method for {@link SerieService#getEpisodesCount()} with exception in DAO tier. */
    @Test
    public void testGetEpisodesCountWithDAOTierException() {
        doThrow(DataStorageException.class).when(serieDAO).getSeries();
        when(serieCache.get(anyString())).thenReturn(null);

        try {
            serieService.getEpisodesCount();
            fail("Can't get seasons count with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(serieDAO).getSeries();
        verify(serieCache).get(SERIES_CACHE_KEY);
        verifyNoMoreInteractions(serieDAO, serieCache);
        verifyZeroInteractions(seasonDAO, episodeDAO);
    }

}
