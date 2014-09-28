package cz.vhromada.catalog.service.impl;

import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.MOVE_POSITION;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.EpisodeService;
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
 * A class represents test for class {@link EpisodeServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class EpisodeServiceImplTest {

	/** Instance of {@link EpisodeDAO} */
	@Mock
	private EpisodeDAO episodeDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache serieCache;

	/** Instance of {@link EpisodeService} */
	@InjectMocks
	private EpisodeService episodeService = new EpisodeServiceImpl();

	/** Test method for {@link EpisodeServiceImpl#getEpisodeDAO()} and {@link EpisodeServiceImpl#setEpisodeDAO(EpisodeDAO)}. */
	@Test
	public void testEpisodeDAO() {
		final EpisodeServiceImpl episodeService = new EpisodeServiceImpl();
		episodeService.setEpisodeDAO(episodeDAO);
		DeepAsserts.assertEquals(episodeDAO, episodeService.getEpisodeDAO());
	}

	/** Test method for {@link EpisodeServiceImpl#getSerieCache()} and {@link EpisodeServiceImpl#setSerieCache(Cache)}. */
	@Test
	public void testSerieCache() {
		final EpisodeServiceImpl episodeService = new EpisodeServiceImpl();
		episodeService.setSerieCache(serieCache);
		DeepAsserts.assertEquals(serieCache, episodeService.getSerieCache());
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with cached existing episode. */
	@Test
	public void testGetEpisodeWithCachedExistingEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episode));

		DeepAsserts.assertEquals(episode, episodeService.getEpisode(PRIMARY_ID));

		verify(serieCache).get("episode" + PRIMARY_ID);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(episodeDAO);
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with cached not existing episode. */
	@Test
	public void testGetEpisodeWithCachedNotExistingEpisode() {
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(episodeService.getEpisode(PRIMARY_ID));

		verify(serieCache).get("episode" + PRIMARY_ID);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(episodeDAO);
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with not cached existing episode. */
	@Test
	public void testGetEpisodeWithNotCachedExistingEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID));
		when(episodeDAO.getEpisode(anyInt())).thenReturn(episode);
		when(serieCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(episode, episodeService.getEpisode(PRIMARY_ID));

		verify(episodeDAO).getEpisode(PRIMARY_ID);
		verify(serieCache).get("episode" + PRIMARY_ID);
		verify(serieCache).put("episode" + PRIMARY_ID, episode);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with not cached not existing episode. */
	@Test
	public void testGetEpisodeWithNotCachedNotExistingEpisode() {
		when(episodeDAO.getEpisode(anyInt())).thenReturn(null);
		when(serieCache.get(anyString())).thenReturn(null);

		assertNull(episodeService.getEpisode(PRIMARY_ID));

		verify(episodeDAO).getEpisode(PRIMARY_ID);
		verify(serieCache).get("episode" + PRIMARY_ID);
		verify(serieCache).put("episode" + PRIMARY_ID, null);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testGetEpisodeWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.getEpisode(Integer.MAX_VALUE);
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testGetEpisodeWithNotSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.getEpisode(Integer.MAX_VALUE);
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with null argument. */
	@Test
	public void testGetEpisodeWithNullArgument() {
		try {
			episodeService.getEpisode(null);
			fail("Can't get episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#getEpisode(Integer)} with exception in DAO tier. */
	@Test
	public void testGetEpisodeWithDAOTierException() {
		doThrow(DataStorageException.class).when(episodeDAO).getEpisode(anyInt());
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			episodeService.getEpisode(Integer.MAX_VALUE);
			fail("Can't get episode with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).getEpisode(Integer.MAX_VALUE);
		verify(serieCache).get("episode" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#add(Episode)} with cached episodes. */
	@Test
	public void testAddWithCachedEpisodes() {
		final Episode episode = EntityGenerator.createEpisode(EntityGenerator.createSeason(INNER_ID));
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
		final List<Episode> episodesList = new ArrayList<>(episodes);
		episodesList.add(episode);
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

		episodeService.add(episode);

		verify(episodeDAO).add(episode);
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).get("episode" + null);
		verify(serieCache).put("episodes" + INNER_ID, episodesList);
		verify(serieCache).put("episode" + null, episode);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#add(Episode)} with not cached episodes. */
	@Test
	public void testAddWithNotCachedEpisodes() {
		final Episode episode = EntityGenerator.createEpisode(EntityGenerator.createSeason(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(null);

		episodeService.add(episode);

		verify(episodeDAO).add(episode);
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).get("episode" + episode.getId());
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#add(Episode)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.add(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#add(Episode)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.add(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#add(Episode)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			episodeService.add(null);
			fail("Can't add episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#add(Episode)} with exception in DAO tier. */
	@Test
	public void testAddWithDAOTierException() {
		final Episode episode = EntityGenerator.createEpisode(EntityGenerator.createSeason(INNER_ID));
		doThrow(DataStorageException.class).when(episodeDAO).add(any(Episode.class));

		try {
			episodeService.add(episode);
			fail("Can't add episode with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).add(episode);
		verifyNoMoreInteractions(episodeDAO);
		verifyZeroInteractions(serieCache);
	}

	/** Test method for {@link EpisodeService#update(Episode)}. */
	@Test
	public void testUpdate() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID));

		episodeService.update(episode);

		verify(episodeDAO).update(episode);
		verify(serieCache).clear();
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#update(Episode)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.update(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#update(Episode)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.update(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#update(Episode)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			episodeService.update(null);
			fail("Can't update episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#update(Episode)} with exception in DAO tier. */
	@Test
	public void testUpdateWithDAOTierException() {
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE, EntityGenerator.createSeason(INNER_ID));
		doThrow(DataStorageException.class).when(episodeDAO).update(any(Episode.class));

		try {
			episodeService.update(episode);
			fail("Can't update episode with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).update(episode);
		verifyNoMoreInteractions(episodeDAO);
		verifyZeroInteractions(serieCache);
	}

	/** Test method for {@link EpisodeService#remove(Episode)} with cached episodes. */
	@Test
	public void testRemoveWithCachedEpisodes() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID));
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
		final List<Episode> episodesList = new ArrayList<>(episodes);
		episodesList.add(episode);
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodesList));

		episodeService.remove(episode);

		verify(episodeDAO).remove(episode);
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).put("episodes" + INNER_ID, episodes);
		verify(serieCache).evict("episode" + PRIMARY_ID);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#remove(Episode)} with not cached episodes. */
	@Test
	public void testRemoveWithNotCachedEpisodes() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(null);

		episodeService.remove(episode);

		verify(episodeDAO).remove(episode);
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).evict("episode" + PRIMARY_ID);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#remove(Episode)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.remove(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#remove(Episode)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.remove(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#remove(Episode)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			episodeService.remove(null);
			fail("Can't remove episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#remove(Episode)} with exception in DAO tier. */
	@Test
	public void testRemoveWithDAOTierException() {
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE, EntityGenerator.createSeason(INNER_ID));
		doThrow(DataStorageException.class).when(episodeDAO).remove(any(Episode.class));

		try {
			episodeService.remove(episode);
			fail("Can't remove episode with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).remove(episode);
		verifyNoMoreInteractions(episodeDAO);
		verifyZeroInteractions(serieCache);
	}

	/** Test method for {@link EpisodeService#duplicate(Episode)} with cached episodes. */
	@Test
	public void testDuplicateWithCachedEpisodes() {
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(CollectionUtils.newList(mock(Episode.class), mock(Episode.class))));

		episodeService.duplicate(EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID)));

		verify(episodeDAO).add(any(Episode.class));
		verify(episodeDAO).update(any(Episode.class));
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).get("episode" + null);
		verify(serieCache).put(eq("episodes" + INNER_ID), anyListOf(Episode.class));
		verify(serieCache).put(eq("episode" + null), any(Episode.class));
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#duplicate(Episode)} with not cached episodes. */
	@Test
	public void testDuplicateWithNotCachedEpisodes() {
		when(serieCache.get(anyString())).thenReturn(null);

		episodeService.duplicate(EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID)));

		verify(episodeDAO).add(any(Episode.class));
		verify(episodeDAO).update(any(Episode.class));
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).get("episode" + null);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#duplicate(Episode)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.duplicate(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#duplicate(Episode)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.duplicate(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#duplicate(Episode)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		try {
			episodeService.duplicate(null);
			fail("Can't duplicate episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#duplicate(Episode)} with exception in DAO tier. */
	@Test
	public void testDuplicateWithDAOTierException() {
		doThrow(DataStorageException.class).when(episodeDAO).add(any(Episode.class));

		try {
			episodeService.duplicate(EntityGenerator.createEpisode(Integer.MAX_VALUE, EntityGenerator.createSeason(INNER_ID)));
			fail("Can't duplicate episode with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).add(any(Episode.class));
		verifyNoMoreInteractions(episodeDAO);
		verifyZeroInteractions(serieCache);
	}

	/** Test method for {@link EpisodeService#moveUp(Episode)} with cached episodes. */
	@Test
	public void testMoveUpWithCachedEpisodes() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode1 = EntityGenerator.createEpisode(PRIMARY_ID, season);
		episode1.setPosition(MOVE_POSITION);
		final Episode episode2 = EntityGenerator.createEpisode(SECONDARY_ID, season);
		final List<Episode> episodes = CollectionUtils.newList(episode1, episode2);
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

		episodeService.moveUp(episode2);
		DeepAsserts.assertEquals(POSITION, episode1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, episode2.getPosition());

		verify(episodeDAO).update(episode1);
		verify(episodeDAO).update(episode2);
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).clear();
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#moveUp(Episode)} with not cached episodes. */
	@Test
	public void testMoveUpWithNotCachedEpisodes() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode1 = EntityGenerator.createEpisode(PRIMARY_ID, season);
		episode1.setPosition(MOVE_POSITION);
		final Episode episode2 = EntityGenerator.createEpisode(SECONDARY_ID, season);
		final List<Episode> episodes = CollectionUtils.newList(episode1, episode2);
		when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(serieCache.get(anyString())).thenReturn(null);

		episodeService.moveUp(episode2);
		DeepAsserts.assertEquals(POSITION, episode1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, episode2.getPosition());

		verify(episodeDAO).update(episode1);
		verify(episodeDAO).update(episode2);
		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).clear();
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#moveUp(Episode)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.moveUp(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#moveUp(Episode)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.moveUp(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#moveUp(Episode)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		try {
			episodeService.moveUp(null);
			fail("Can't move up episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#moveUp(Episode)} with exception in DAO tier. */
	@Test
	public void testMoveUpWithDAOTierException() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		doThrow(DataStorageException.class).when(episodeDAO).findEpisodesBySeason(any(Season.class));
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			episodeService.moveUp(EntityGenerator.createEpisode(Integer.MAX_VALUE, season));
			fail("Can't move up episode with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + INNER_ID);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#moveDown(Episode)} with cached episodes. */
	@Test
	public void testMoveDownWithCachedEpisodes() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode1 = EntityGenerator.createEpisode(PRIMARY_ID, season);
		final Episode episode2 = EntityGenerator.createEpisode(SECONDARY_ID, season);
		episode2.setPosition(MOVE_POSITION);
		final List<Episode> episodes = CollectionUtils.newList(episode1, episode2);
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

		episodeService.moveDown(episode1);
		DeepAsserts.assertEquals(MOVE_POSITION, episode1.getPosition());
		DeepAsserts.assertEquals(POSITION, episode2.getPosition());

		verify(episodeDAO).update(episode1);
		verify(episodeDAO).update(episode2);
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).clear();
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#moveDown(Episode)} with not cached episodes. */
	@Test
	public void testMoveDownWithNotCachedEpisodes() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		final Episode episode1 = EntityGenerator.createEpisode(PRIMARY_ID, season);
		final Episode episode2 = EntityGenerator.createEpisode(SECONDARY_ID, season);
		episode2.setPosition(MOVE_POSITION);
		final List<Episode> episodes = CollectionUtils.newList(episode1, episode2);
		when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(serieCache.get(anyString())).thenReturn(null);

		episodeService.moveDown(episode1);
		DeepAsserts.assertEquals(MOVE_POSITION, episode1.getPosition());
		DeepAsserts.assertEquals(POSITION, episode2.getPosition());

		verify(episodeDAO).update(episode1);
		verify(episodeDAO).update(episode2);
		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + INNER_ID);
		verify(serieCache).clear();
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#moveDown(Episode)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.moveDown(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#moveDown(Episode)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.moveDown(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#moveDown(Episode)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		try {
			episodeService.moveDown(null);
			fail("Can't move down episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#moveDown(Episode)} with exception in DAO tier. */
	@Test
	public void testMoveDownWithDAOTierException() {
		final Season season = EntityGenerator.createSeason(INNER_ID);
		doThrow(DataStorageException.class).when(episodeDAO).findEpisodesBySeason(any(Season.class));
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			episodeService.moveDown(EntityGenerator.createEpisode(Integer.MAX_VALUE, season));
			fail("Can't move down episode with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + INNER_ID);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with cached existing episode. */
	@Test
	public void testExistsWithCachedExistingEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episode));

		assertTrue(episodeService.exists(episode));

		verify(serieCache).get("episode" + PRIMARY_ID);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(episodeDAO);
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with cached not existing episode. */
	@Test
	public void testExistsWithCachedNotExistingEpisode() {
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE, EntityGenerator.createSeason(INNER_ID));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(episodeService.exists(episode));

		verify(serieCache).get("episode" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(episodeDAO);
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with not cached existing episode. */
	@Test
	public void testExistsWithNotCachedExistingEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID, EntityGenerator.createSeason(INNER_ID));
		when(episodeDAO.getEpisode(anyInt())).thenReturn(episode);
		when(serieCache.get(anyString())).thenReturn(null);

		assertTrue(episodeService.exists(episode));

		verify(episodeDAO).getEpisode(PRIMARY_ID);
		verify(serieCache).get("episode" + PRIMARY_ID);
		verify(serieCache).put("episode" + PRIMARY_ID, episode);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with not cached not existing episode. */
	@Test
	public void testExistsWithNotCachedNotExistingEpisode() {
		when(episodeDAO.getEpisode(anyInt())).thenReturn(null);
		when(serieCache.get(anyString())).thenReturn(null);

		assertFalse(episodeService.exists(EntityGenerator.createEpisode(Integer.MAX_VALUE, EntityGenerator.createSeason(INNER_ID))));

		verify(episodeDAO).getEpisode(Integer.MAX_VALUE);
		verify(serieCache).get("episode" + Integer.MAX_VALUE);
		verify(serieCache).put("episode" + Integer.MAX_VALUE, null);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.exists(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.exists(mock(Episode.class));
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		try {
			episodeService.exists(null);
			fail("Can't exists episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#exists(Episode)} with exception in DAO tier. */
	@Test
	public void testExistsWithDAOTierException() {
		doThrow(DataStorageException.class).when(episodeDAO).getEpisode(anyInt());
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			episodeService.exists(EntityGenerator.createEpisode(Integer.MAX_VALUE, EntityGenerator.createSeason(INNER_ID)));
			fail("Can't exists episode with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).getEpisode(Integer.MAX_VALUE);
		verify(serieCache).get("episode" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#findEpisodesBySeason(Season)} with cached episodes. */
	@Test
	public void testFindEpisodesBySeasonWithCachedEpisodes() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));

		DeepAsserts.assertEquals(episodes, episodeService.findEpisodesBySeason(season));

		verify(serieCache).get("episodes" + PRIMARY_ID);
		verifyNoMoreInteractions(serieCache);
		verifyZeroInteractions(episodeDAO);
	}

	/** Test method for {@link EpisodeService#findEpisodesBySeason(Season)} with not cached episodes. */
	@Test
	public void testFindEpisodesBySeasonWithNotCachedEpisodes() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), mock(Episode.class));
		when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(serieCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(episodes, episodeService.findEpisodesBySeason(season));

		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + PRIMARY_ID);
		verify(serieCache).put("episodes" + PRIMARY_ID, episodes);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#findEpisodesBySeason(Season)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testFindEpisodesBySeasonWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.findEpisodesBySeason(mock(Season.class));
	}

	/** Test method for {@link EpisodeService#findEpisodesBySeason(Season)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testFindEpisodesBySeasonWithNotSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.findEpisodesBySeason(mock(Season.class));
	}

	/** Test method for {@link EpisodeService#findEpisodesBySeason(Season)} with null argument. */
	@Test
	public void testFindEpisodesBySeasonWithNullArgument() {
		try {
			episodeService.findEpisodesBySeason(null);
			fail("Can't find episodes by season with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#findEpisodesBySeason(Season)} with exception in DAO tier. */
	@Test
	public void testFindEpisodesBySeasonWithDAOTierException() {
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(episodeDAO).findEpisodesBySeason(any(Season.class));
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			episodeService.findEpisodesBySeason(season);
			fail("Can't find episodes by season with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#getTotalLengthBySeason(Season)} with cached episodes. */
	@Test
	public void testGetTotalLengthBySeasonWithCachedEpisodes() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final Episode episode1 = mock(Episode.class);
		final Episode episode2 = mock(Episode.class);
		final Episode episode3 = mock(Episode.class);
		final List<Episode> episodes = CollectionUtils.newList(episode1, episode2, episode3);
		when(serieCache.get(anyString())).thenReturn(new SimpleValueWrapper(episodes));
		when(episode1.getLength()).thenReturn(100);
		when(episode2.getLength()).thenReturn(200);
		when(episode3.getLength()).thenReturn(300);

		DeepAsserts.assertEquals(new Time(600), episodeService.getTotalLengthBySeason(season));

		verify(serieCache).get("episodes" + PRIMARY_ID);
		verify(episode1).getLength();
		verify(episode2).getLength();
		verify(episode3).getLength();
		verifyNoMoreInteractions(serieCache, episode1, episode2, episode3);
		verifyZeroInteractions(episodeDAO);
	}

	/** Test method for {@link EpisodeService#getTotalLengthBySeason(Season)} with not cached episodes. */
	@Test
	public void testGetTotalLengthBySeasonWithNotCachedEpisodes() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final Episode episode1 = mock(Episode.class);
		final Episode episode2 = mock(Episode.class);
		final Episode episode3 = mock(Episode.class);
		final List<Episode> episodes = CollectionUtils.newList(episode1, episode2, episode3);
		when(episodeDAO.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
		when(serieCache.get(anyString())).thenReturn(null);
		when(episode1.getLength()).thenReturn(100);
		when(episode2.getLength()).thenReturn(200);
		when(episode3.getLength()).thenReturn(300);

		DeepAsserts.assertEquals(new Time(600), episodeService.getTotalLengthBySeason(season));

		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + PRIMARY_ID);
		verify(serieCache).put("episodes" + PRIMARY_ID, episodes);
		verify(episode1).getLength();
		verify(episode2).getLength();
		verify(episode3).getLength();
		verifyNoMoreInteractions(episodeDAO, serieCache, episode1, episode2, episode3);
	}

	/** Test method for {@link EpisodeService#getTotalLengthBySeason(Season)} with not set DAO for episodes. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthBySeasonWithNotSetEpisodeDAO() {
		((EpisodeServiceImpl) episodeService).setEpisodeDAO(null);
		episodeService.getTotalLengthBySeason(mock(Season.class));
	}

	/** Test method for {@link EpisodeService#getTotalLengthBySeason(Season)} with not set cache for series. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthBySeasonWithNotSetSerieCache() {
		((EpisodeServiceImpl) episodeService).setSerieCache(null);
		episodeService.getTotalLengthBySeason(mock(Season.class));
	}

	/** Test method for {@link EpisodeService#getTotalLengthBySeason(Season)} with null argument. */
	@Test
	public void testGetTotalLengthBySeasonWithNullArgument() {
		try {
			episodeService.getTotalLengthBySeason(null);
			fail("Can't get total length by season with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(episodeDAO, serieCache);
	}

	/** Test method for {@link EpisodeService#getTotalLengthBySeason(Season)} with exception in DAO tier. */
	@Test
	public void testGetTotalLengthBySeasonWithDAOTierException() {
		final Season season = EntityGenerator.createSeason(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(episodeDAO).findEpisodesBySeason(any(Season.class));
		when(serieCache.get(anyString())).thenReturn(null);

		try {
			episodeService.getTotalLengthBySeason(season);
			fail("Can't get total length by season with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(episodeDAO).findEpisodesBySeason(season);
		verify(serieCache).get("episodes" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(episodeDAO, serieCache);
	}

}
