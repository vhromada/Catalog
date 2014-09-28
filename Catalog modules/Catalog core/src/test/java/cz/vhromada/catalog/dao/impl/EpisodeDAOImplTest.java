package cz.vhromada.catalog.dao.impl;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_INNER_ID;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link EpisodeDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class EpisodeDAOImplTest {

	/** Instance of {@link EntityManager} */
	@Mock
	private EntityManager entityManager;

	/** Query for episodes */
	@Mock
	private TypedQuery<Episode> episodesQuery;

	/** Instance of {@link EpisodeDAO} */
	@InjectMocks
	private EpisodeDAO episodeDAO = new EpisodeDAOImpl();

	/** Test method for {@link EpisodeDAOImpl#getEntityManager()} and {@link EpisodeDAOImpl#setEntityManager(EntityManager)}. */
	@Test
	public void testEntityManager() {
		final EpisodeDAOImpl episodeDAOImpl = new EpisodeDAOImpl();
		episodeDAOImpl.setEntityManager(entityManager);
		DeepAsserts.assertEquals(entityManager, episodeDAOImpl.getEntityManager());
	}

	/** Test method for {@link EpisodeDAO#getEpisode(Integer)} with existing episode. */
	@Test
	public void testGetEpisodeWithExistingEpisode() {
		final Episode episode = mock(Episode.class);
		when(entityManager.find(eq(Episode.class), anyInt())).thenReturn(episode);

		DeepAsserts.assertEquals(episode, episodeDAO.getEpisode(PRIMARY_ID));

		verify(entityManager).find(Episode.class, PRIMARY_ID);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAO#getEpisode(Integer)} with not existing episode. */
	@Test
	public void testGetEpisodeWithNotExistingEpisode() {
		when(entityManager.find(eq(Episode.class), anyInt())).thenReturn(null);

		assertNull(episodeDAO.getEpisode(Integer.MAX_VALUE));

		verify(entityManager).find(Episode.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAOImpl#getEpisode(Integer)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetEpisodeWithNotSetEntityManager() {
		((EpisodeDAOImpl) episodeDAO).setEntityManager(null);
		episodeDAO.getEpisode(Integer.MAX_VALUE);
	}

	/** Test method for {@link EpisodeDAO#getEpisode(Integer)} with null argument. */
	@Test
	public void testGetEpisodeWithNullArgument() {
		try {
			episodeDAO.getEpisode(null);
			fail("Can't get episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAOImpl#getEpisode(Integer)} with exception in persistence. */
	@Test
	public void testGetEpisodeWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).find(eq(Episode.class), anyInt());

		try {
			episodeDAO.getEpisode(Integer.MAX_VALUE);
			fail("Can't get episode with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).find(Episode.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAO#add(Episode)}. */
	@Test
	public void testAdd() {
		final Episode episode = EntityGenerator.createEpisode();
		doAnswer(setId(ID)).when(entityManager).persist(any(Episode.class));

		episodeDAO.add(episode);
		DeepAsserts.assertEquals(ID, episode.getId());
		DeepAsserts.assertEquals(ID - 1, episode.getPosition());

		verify(entityManager).persist(episode);
		verify(entityManager).merge(episode);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAOImpl#add(Episode)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEntityManager() {
		((EpisodeDAOImpl) episodeDAO).setEntityManager(null);
		episodeDAO.add(mock(Episode.class));
	}

	/** Test method for {@link EpisodeDAO#add(Episode)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			episodeDAO.add(null);
			fail("Can't add episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAOImpl#add(Episode)} with exception in persistence. */
	@Test
	public void testAddWithPersistenceException() {
		final Episode episode = EntityGenerator.createEpisode();
		doThrow(PersistenceException.class).when(entityManager).persist(any(Episode.class));

		try {
			episodeDAO.add(episode);
			fail("Can't add episode with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).persist(episode);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAO#update(Episode)}. */
	@Test
	public void testUpdate() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID);

		episodeDAO.update(episode);

		verify(entityManager).merge(episode);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAOImpl#update(Episode)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEntityManager() {
		((EpisodeDAOImpl) episodeDAO).setEntityManager(null);
		episodeDAO.update(mock(Episode.class));
	}

	/** Test method for {@link EpisodeDAO#update(Episode)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			episodeDAO.update(null);
			fail("Can't update episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAOImpl#update(Episode)} with exception in persistence. */
	@Test
	public void testUpdateWithPersistenceException() {
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(PersistenceException.class).when(entityManager).merge(any(Episode.class));

		try {
			episodeDAO.update(episode);
			fail("Can't update episode with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).merge(episode);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAO#remove(Episode)} with managed episode. */
	@Test
	public void testRemoveWithManagedEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID);
		when(entityManager.contains(any(Episode.class))).thenReturn(true);

		episodeDAO.remove(episode);

		verify(entityManager).contains(episode);
		verify(entityManager).remove(episode);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAO#remove(Episode)} with not managed episode. */
	@Test
	public void testRemoveWithNotManagedEpisode() {
		final Episode episode = EntityGenerator.createEpisode(PRIMARY_ID);
		when(entityManager.contains(any(Episode.class))).thenReturn(false);
		when(entityManager.getReference(eq(Episode.class), anyInt())).thenReturn(episode);

		episodeDAO.remove(episode);

		verify(entityManager).contains(episode);
		verify(entityManager).getReference(Episode.class, PRIMARY_ID);
		verify(entityManager).remove(episode);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAOImpl#remove(Episode)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEntityManager() {
		((EpisodeDAOImpl) episodeDAO).setEntityManager(null);
		episodeDAO.remove(mock(Episode.class));
	}

	/** Test method for {@link EpisodeDAO#remove(Episode)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			episodeDAO.remove(null);
			fail("Can't remove episode with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAOImpl#remove(Episode)} with exception in persistence. */
	@Test
	public void testRemoveWithPersistenceException() {
		final Episode episode = EntityGenerator.createEpisode(Integer.MAX_VALUE);
		doThrow(PersistenceException.class).when(entityManager).contains(any(Episode.class));

		try {
			episodeDAO.remove(episode);
			fail("Can't remove episode with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).contains(episode);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link EpisodeDAO#findEpisodesBySeason(Season)}. */
	@Test
	public void testFindEpisodesBySeason() {
		final Season season = EntityGenerator.createSeason(PRIMARY_ID);
		final List<Episode> episodes = CollectionUtils.newList(EntityGenerator.createEpisode(INNER_INNER_ID, season),
				EntityGenerator.createEpisode(SECONDARY_INNER_INNER_ID, season));
		when(entityManager.createNamedQuery(anyString(), eq(Episode.class))).thenReturn(episodesQuery);
		when(episodesQuery.getResultList()).thenReturn(episodes);

		DeepAsserts.assertEquals(episodes, episodeDAO.findEpisodesBySeason(season));

		verify(entityManager).createNamedQuery(Episode.FIND_BY_SEASON, Episode.class);
		verify(episodesQuery).getResultList();
		verify(episodesQuery).setParameter("season", season.getId());
		verifyNoMoreInteractions(entityManager, episodesQuery);
	}

	/** Test method for {@link EpisodeDAOImpl#add(Episode)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testFindEpisodesBySeasonWithNotSetEntityManager() {
		((EpisodeDAOImpl) episodeDAO).setEntityManager(null);
		episodeDAO.findEpisodesBySeason(mock(Season.class));
	}

	/** Test method for {@link EpisodeDAO#findEpisodesBySeason(Season)} with null argument. */
	@Test
	public void testFindEpisodesBySeasonWithNullArgument() {
		try {
			episodeDAO.findEpisodesBySeason(null);
			fail("Can't find episodes by season with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager, episodesQuery);
	}

	/** Test method for {@link EpisodeDAOImpl#findEpisodesBySeason(Season)} with exception in persistence. */
	@Test
	public void testFindEpisodesBySeasonWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Episode.class));

		try {
			episodeDAO.findEpisodesBySeason(EntityGenerator.createSeason(Integer.MAX_VALUE));
			fail("Can't find episodes by season with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).createNamedQuery(Episode.FIND_BY_SEASON, Episode.class);
		verifyNoMoreInteractions(entityManager);
		verifyZeroInteractions(episodesQuery);
	}

	/**
	 * Sets ID.
	 *
	 * @param id ID
	 * @return mocked answer
	 */
	private Answer<Void> setId(final Integer id) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				((Episode) invocation.getArguments()[0]).setId(id);
				return null;
			}

		};
	}

}
