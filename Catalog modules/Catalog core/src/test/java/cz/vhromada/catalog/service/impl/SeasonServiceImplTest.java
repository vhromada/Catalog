package cz.vhromada.catalog.service.impl;

import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.MOVE_POSITION;
import static cz.vhromada.catalog.common.TestConstants.POSITION;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_ID;
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

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
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
public class SeasonServiceImplTest {

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

	/** Test method for {@link SeasonServiceImpl#getSeasonDAO()} and {@link SeasonServiceImpl#setSeasonDAO(SeasonDAO)}. */
	@Test
	public void testSeasonDAO() {
		final SeasonServiceImpl seasonService = new SeasonServiceImpl();
		seasonService.setSeasonDAO(seasonDAO);
		DeepAsserts.assertEquals(seasonDAO, seasonService.getSeasonDAO());
	}

	/** Test method for {@link SeasonServiceImpl#getEpisodeDAO()} and {@link SeasonServiceImpl#setEpisodeDAO(EpisodeDAO)}. */
	@Test
	public void testEpisodeDAO() {
		final SeasonServiceImpl seasonService = new SeasonServiceImpl();
		seasonService.setEpisodeDAO(episodeDAO);
		DeepAsserts.assertEquals(episodeDAO, seasonService.getEpisodeDAO());
	}

	/** Test method for {@link SeasonServiceImpl#getSerieCache()} and {@link SeasonServiceImpl#setSerieCache(Cache)}. */
	@Test
	public void testSerieCache() {
		final SeasonServiceImpl seasonService = new SeasonServiceImpl();
		seasonService.setSerieCache(serieCache);
		DeepAsserts.assertEquals(serieCache, seasonService.getSerieCache());
	}

	/** Test method for {@link SeasonService#getSeason(Integer)} with cached existing season. */
	@Test
	public void testGetSeasonWithCachedExistingSeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(season));

		DeepAsserts.assertEquals(season, seasonService.getSeason(PRIMARY_ID));

		verify(serieCache).get("season" + PRIMARY_ID);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(seasonDAO);
	}

	/** Test method for {@link SeasonService#getSeason(Integer)} with cached not existing season. */
	@Test
	public void testGetSeasonWithCachedNotExistingSeason() {
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(seasonService.getSeason(PRIMARY_ID));

		verify(serieCache).get("season" + PRIMARY_ID);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(seasonDAO);
	}

	/** Test method for {@link SeasonService#getSeason(Integer)} with not cached existing season. */
	@Test
	public void testGetSeasonWithNotCachedExistingSeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID));
		when(seasonDAO.getSeason(anyInt())).thenReturn(season);
		when(serieCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(season, seasonService.getSeason(PRIMARY_ID));

		verify(seasonDAO).getSeason(PRIMARY_ID);
		verify(serieCache).get("season" + PRIMARY_ID);
		verify(serieCache).put("season" + PRIMARY_ID, season);
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#getSeason(Integer)} with not cached not existing season. */
	@Test
	public void testGetSeasonWithNotCachedNotExistingSeason() {
		when(seasonDAO.getSeason(anyInt())).thenReturn(null);
		when(serieCache.get(anyString())).thenReturn(null);

		assertNull(seasonService.getSeason(PRIMARY_ID));

		verify(seasonDAO).getSeason(PRIMARY_ID);
		verify(serieCache).get("season" + PRIMARY_ID);
		verify(serieCache).put("season" + PRIMARY_ID, null);
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
		verify(serieCache).get("season" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#add(Season)} with cached seasons. */
	@Test
	public void testAddWithCachedSeasons() {
		final Season season = EntityGenerator.createSeason(EntityGenerator.createSerie(INNER_ID));
		final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
		final List<Season> seasonsList = new ArrayList<>(seasons);
		seasonsList.add(season);
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

		seasonService.add(season);

		verify(seasonDAO).add(season);
		verify(serieCache).get("seasons" + INNER_ID);
		verify(serieCache).get("season" + null);
		verify(serieCache).put("seasons" + INNER_ID, seasonsList);
		verify(serieCache).put("season" + null, season);
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#add(Season)} with not cached seasons. */
	@Test
	public void testAddWithNotCachedSeasons() {
		final Season season = EntityGenerator.createSeason(EntityGenerator.createSerie(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(null);

		seasonService.add(season);

		verify(seasonDAO).add(season);
		verify(serieCache).get("seasons" + INNER_ID);
		verify(serieCache).get("season" + null);
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
		final Season season = EntityGenerator.createSeason(EntityGenerator.createSerie(INNER_ID));
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
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID));

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
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE, EntityGenerator.createSerie(INNER_ID));
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
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID));
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

		seasonService.remove(season);

		verify(seasonDAO).remove(season);
		for (Episode episode : episodes) {
			verify(episodeDAO).remove(episode);
		}
		verify(serieCache).get("episodes" + PRIMARY_ID);
		verify(serieCache).clear();
		verifyNoMoreInteractions(seasonDAO, episodeDAO, serieCache);
	}

	/** Test method for {@link SeasonService#remove(Season)} with not cached data. */
	@Test
	public void testRemoveWithNotCachedData() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID));
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
		when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(serieCache.get(anyString())).thenReturn(null);

		seasonService.remove(season);

		verify(seasonDAO).remove(season);
		verify(episodeDAO).findEpisodesBySeason(season);
		for (Episode episode : episodes) {
			verify(episodeDAO).remove(episode);
		}
		verify(serieCache).get("episodes" + PRIMARY_ID);
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
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE, EntityGenerator.createSerie(INNER_ID));
		doThrow(DataStorageException.class).when(episodeDAO).findEpisodesBySeason(any(Season.class));
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			seasonService.remove(season);
			fail("Can't remove season with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(seasonDAO, serieCache);
		verifyZeroInteractions(episodeDAO);
	}

	/** Test method for {@link SeasonService#duplicate(Season)} with cached data. */
	@Test
	public void testDuplicateWithCachedData() {
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

		seasonService.duplicate(EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID)));

		verify(seasonDAO).add(any(Season.class));
		verify(seasonDAO).update(any(Season.class));
		verify(episodeDAO, times(episodes.size())).add(any(Episode.class));
		verify(episodeDAO, times(episodes.size())).update(any(Episode.class));
		verify(serieCache).get("episodes" + PRIMARY_ID);
		verify(serieCache).clear();
		verifyNoMoreInteractions(seasonDAO, episodeDAO, serieCache);
	}

	/** Test method for {@link SeasonService#duplicate(Season)} with not cached data. */
	@Test
	public void testDuplicateWithNotCachedData() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID));
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
		when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(serieCache.get(anyString())).thenReturn(null);

		seasonService.duplicate(season);

		verify(seasonDAO).add(any(Season.class));
		verify(seasonDAO).update(any(Season.class));
		verify(episodeDAO).findEpisodesBySeason(season);
		verify(episodeDAO, times(episodes.size())).add(any(Episode.class));
		verify(episodeDAO, times(episodes.size())).update(any(Episode.class));
		verify(serieCache).get("episodes" + PRIMARY_ID);
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
			seasonService.duplicate(EntityGenerator.createSeason(Integer.MAX_VALUE, EntityGenerator.createSerie(INNER_ID)));
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
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season1 = EntityGenerator.createSeason(PRIMARY_ID, serie);
		season1.setPosition(MOVE_POSITION);
		final Season season2 = EntityGenerator.createSeason(SECONDARY_ID, serie);
		final List<Season> seasons = CollectionUtils.newList(season1, season2);
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

		seasonService.moveUp(season2);
		DeepAsserts.assertEquals(POSITION, season1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, season2.getPosition());

		verify(seasonDAO).update(season1);
		verify(seasonDAO).update(season2);
		verify(serieCache).get("seasons" + INNER_ID);
		verify(serieCache).clear();
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#moveUp(Season)} with not cached seasons. */
	@Test
	public void testMoveUpWithNotCachedSeasons() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season1 = EntityGenerator.createSeason(PRIMARY_ID, serie);
		season1.setPosition(MOVE_POSITION);
		final Season season2 = EntityGenerator.createSeason(SECONDARY_ID, serie);
		final List<Season> seasons = CollectionUtils.newList(season1, season2);
		when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
		when(serieCache.get(anyString())).thenReturn(null);

		seasonService.moveUp(season2);
		DeepAsserts.assertEquals(POSITION, season1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, season2.getPosition());

		verify(seasonDAO).update(season1);
		verify(seasonDAO).update(season2);
		verify(seasonDAO).findSeasonsBySerie(serie);
		verify(serieCache).get("seasons" + INNER_ID);
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
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		doThrow(DataStorageException.class).when(seasonDAO).findSeasonsBySerie(any(Serie.class));
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			seasonService.moveUp(EntityGenerator.createSeason(Integer.MAX_VALUE, serie));
			fail("Can't move up season with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(seasonDAO).findSeasonsBySerie(serie);
		verify(serieCache).get("seasons" + INNER_ID);
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#moveDown(Season)} with cached seasons. */
	@Test
	public void testMoveDownWithCachedSeasons() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season1 = EntityGenerator.createSeason(PRIMARY_ID, serie);
		final Season season2 = EntityGenerator.createSeason(SECONDARY_ID, serie);
		season2.setPosition(MOVE_POSITION);
		final List<Season> seasons = CollectionUtils.newList(season1, season2);
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

		seasonService.moveDown(season1);
		DeepAsserts.assertEquals(MOVE_POSITION, season1.getPosition());
		DeepAsserts.assertEquals(POSITION, season2.getPosition());

		verify(seasonDAO).update(season1);
		verify(seasonDAO).update(season2);
		verify(serieCache).get("seasons" + INNER_ID);
		verify(serieCache).clear();
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#moveDown(Season)} with not cached seasons. */
	@Test
	public void testMoveDownWithNotCachedSeasons() {
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		final Season season1 = EntityGenerator.createSeason(PRIMARY_ID, serie);
		final Season season2 = EntityGenerator.createSeason(SECONDARY_ID, serie);
		season2.setPosition(MOVE_POSITION);
		final List<Season> seasons = CollectionUtils.newList(season1, season2);
		when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
		when(serieCache.get(anyString())).thenReturn(null);

		seasonService.moveDown(season1);
		DeepAsserts.assertEquals(MOVE_POSITION, season1.getPosition());
		DeepAsserts.assertEquals(POSITION, season2.getPosition());

		verify(seasonDAO).update(season1);
		verify(seasonDAO).update(season2);
		verify(seasonDAO).findSeasonsBySerie(serie);
		verify(serieCache).get("seasons" + INNER_ID);
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
		final Serie serie = EntityGenerator.createSerie(INNER_ID);
		doThrow(DataStorageException.class).when(seasonDAO).findSeasonsBySerie(any(Serie.class));
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			seasonService.moveDown(EntityGenerator.createSeason(Integer.MAX_VALUE, serie));
			fail("Can't move down season with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(seasonDAO).findSeasonsBySerie(serie);
		verify(serieCache).get("seasons" + INNER_ID);
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#exists(Season)} with cached existing season. */
	@Test
	public void testExistsWithCachedExistingSeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(season));

		assertTrue(seasonService.exists(season));

		verify(serieCache).get("season" + PRIMARY_ID);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(seasonDAO);
	}

	/** Test method for {@link SeasonService#exists(Season)} with cached not existing season. */
	@Test
	public void testExistsWithCachedNotExistingSeason() {
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE, EntityGenerator.createSerie(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(seasonService.exists(season));

		verify(serieCache).get("season" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(seasonDAO);
	}

	/** Test method for {@link SeasonService#exists(Season)} with not cached existing season. */
	@Test
	public void testExistsWithNotCachedExistingSeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID, EntityGenerator.createSerie(INNER_ID));
		when(seasonDAO.getSeason(anyInt())).thenReturn(season);
		when(serieCache.get(anyString())).thenReturn(null);

		assertTrue(seasonService.exists(season));

		verify(seasonDAO).getSeason(PRIMARY_ID);
		verify(serieCache).get("season" + PRIMARY_ID);
		verify(serieCache).put("season" + PRIMARY_ID, season);
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#exists(Season)} with not cached not existing season. */
	@Test
	public void testExistsWithNotCachedNotExistingSeason() {
		when(seasonDAO.getSeason(anyInt())).thenReturn(null);
		when(serieCache.get(anyString())).thenReturn(null);

		assertFalse(seasonService.exists(EntityGenerator.createSeason(Integer.MAX_VALUE, EntityGenerator.createSerie(INNER_ID))));

		verify(seasonDAO).getSeason(Integer.MAX_VALUE);
		verify(serieCache).get("season" + Integer.MAX_VALUE);
		verify(serieCache).put("season" + Integer.MAX_VALUE, null);
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
		doThrow(DataStorageException.class).when(seasonDAO).getSeason(anyInt());
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			seasonService.exists(EntityGenerator.createSeason(Integer.MAX_VALUE, EntityGenerator.createSerie(INNER_ID)));
			fail("Can't exists season with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(seasonDAO).getSeason(Integer.MAX_VALUE);
		verify(serieCache).get("season" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

	/** Test method for {@link SeasonService#findSeasonsBySerie(Serie)} with cached seasons. */
	@Test
	public void testFindSeasonsBySerieWithCachedSeasons() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(seasons));

		DeepAsserts.assertEquals(seasons, seasonService.findSeasonsBySerie(serie));

		verify(serieCache).get("seasons" + PRIMARY_ID);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(seasonDAO);
	}

	/** Test method for {@link SeasonService#findSeasonsBySerie(Serie)} with not cached seasons. */
	@Test
	public void testFindSeasonsBySerieWithNotCachedSeasons() {
		final Serie serie = EntityGenerator.createSerie(PRIMARY_ID);
		final List<Season> seasons = CollectionUtils.newList(mock(Season.class), mock(Season.class));
		when(seasonDAO.findSeasonsBySerie(any(Serie.class))).thenReturn(seasons);
		when(serieCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(seasons, seasonService.findSeasonsBySerie(serie));

		verify(seasonDAO).findSeasonsBySerie(serie);
		verify(serieCache).get("seasons" + PRIMARY_ID);
		verify(serieCache).put("seasons" + PRIMARY_ID, seasons);
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
		final Serie serie = EntityGenerator.createSerie(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(seasonDAO).findSeasonsBySerie(any(Serie.class));
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			seasonService.findSeasonsBySerie(serie);
			fail("Can't find seasons by serie with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(seasonDAO).findSeasonsBySerie(serie);
		verify(serieCache).get("seasons" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(seasonDAO, serieCache);
	}

}
