package cz.vhromada.catalog.dao.impl;

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
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
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
 * A class represents test for class {@link SeasonDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SeasonDAOImplTest extends ObjectGeneratorTest {

	/** Instance of {@link EntityManager} */
	@Mock
	private EntityManager entityManager;

	/** Query for seasons */
	@Mock
	private TypedQuery<Season> seasonsQuery;

	/** Instance of {@link SeasonDAO} */
	@InjectMocks
	private SeasonDAO seasonDAO = new SeasonDAOImpl();

	/** Test method for {@link SeasonDAOImpl#getEntityManager()} and {@link SeasonDAOImpl#setEntityManager(EntityManager)}. */
	@Test
	public void testEntityManager() {
		final SeasonDAOImpl seasonDAOImpl = new SeasonDAOImpl();
		seasonDAOImpl.setEntityManager(entityManager);
		DeepAsserts.assertEquals(entityManager, seasonDAOImpl.getEntityManager());
	}

	/** Test method for {@link SeasonDAO#getSeason(Integer)} with existing season. */
	@Test
	public void testGetSeasonWithExistingSeason() {
		final int id = generate(Integer.class);
		final Season season = mock(Season.class);
		when(entityManager.find(eq(Season.class), anyInt())).thenReturn(season);

		DeepAsserts.assertEquals(season, seasonDAO.getSeason(id));

		verify(entityManager).find(Season.class, id);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAO#getSeason(Integer)} with not existing season. */
	@Test
	public void testGetSeasonWithNotExistingSeason() {
		when(entityManager.find(eq(Season.class), anyInt())).thenReturn(null);

		assertNull(seasonDAO.getSeason(Integer.MAX_VALUE));

		verify(entityManager).find(Season.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAOImpl#getSeason(Integer)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeasonWithNotSetEntityManager() {
		((SeasonDAOImpl) seasonDAO).setEntityManager(null);
		seasonDAO.getSeason(Integer.MAX_VALUE);
	}

	/** Test method for {@link SeasonDAO#getSeason(Integer)} with null argument. */
	@Test
	public void testGetSeasonWithNullArgument() {
		try {
			seasonDAO.getSeason(null);
			fail("Can't get season with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAOImpl#getSeason(Integer)} with exception in persistence. */
	@Test
	public void testGetSeasonWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).find(eq(Season.class), anyInt());

		try {
			seasonDAO.getSeason(Integer.MAX_VALUE);
			fail("Can't get season with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).find(Season.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAO#add(Season)}. */
	@Test
	public void testAdd() {
		final Season season = generate(Season.class);
		final int id = generate(Integer.class);
		doAnswer(setId(id)).when(entityManager).persist(any(Season.class));

		seasonDAO.add(season);
		DeepAsserts.assertEquals(id, season.getId());
		DeepAsserts.assertEquals(id - 1, season.getPosition());

		verify(entityManager).persist(season);
		verify(entityManager).merge(season);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAOImpl#add(Season)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEntityManager() {
		((SeasonDAOImpl) seasonDAO).setEntityManager(null);
		seasonDAO.add(mock(Season.class));
	}

	/** Test method for {@link SeasonDAO#add(Season)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			seasonDAO.add(null);
			fail("Can't add season with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAOImpl#add(Season)} with exception in persistence. */
	@Test
	public void testAddWithPersistenceException() {
		final Season season = generate(Season.class);
		doThrow(PersistenceException.class).when(entityManager).persist(any(Season.class));

		try {
			seasonDAO.add(season);
			fail("Can't add season with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).persist(season);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAO#update(Season)}. */
	@Test
	public void testUpdate() {
		final Season season = generate(Season.class);

		seasonDAO.update(season);

		verify(entityManager).merge(season);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAOImpl#update(Season)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEntityManager() {
		((SeasonDAOImpl) seasonDAO).setEntityManager(null);
		seasonDAO.update(mock(Season.class));
	}

	/** Test method for {@link SeasonDAO#update(Season)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			seasonDAO.update(null);
			fail("Can't update season with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAOImpl#update(Season)} with exception in persistence. */
	@Test
	public void testUpdateWithPersistenceException() {
		final Season season = generate(Season.class);
		doThrow(PersistenceException.class).when(entityManager).merge(any(Season.class));

		try {
			seasonDAO.update(season);
			fail("Can't update season with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).merge(season);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAO#remove(Season)} with managed season. */
	@Test
	public void testRemoveWithManagedSeason() {
		final Season season = generate(Season.class);
		when(entityManager.contains(any(Season.class))).thenReturn(true);

		seasonDAO.remove(season);

		verify(entityManager).contains(season);
		verify(entityManager).remove(season);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAO#remove(Season)} with not managed season. */
	@Test
	public void testRemoveWithNotManagedSeason() {
		final Season season = generate(Season.class);
		when(entityManager.contains(any(Season.class))).thenReturn(false);
		when(entityManager.getReference(eq(Season.class), anyInt())).thenReturn(season);

		seasonDAO.remove(season);

		verify(entityManager).contains(season);
		verify(entityManager).getReference(Season.class, season.getId());
		verify(entityManager).remove(season);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAOImpl#remove(Season)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEntityManager() {
		((SeasonDAOImpl) seasonDAO).setEntityManager(null);
		seasonDAO.remove(mock(Season.class));
	}

	/** Test method for {@link SeasonDAO#remove(Season)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			seasonDAO.remove(null);
			fail("Can't remove season with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAOImpl#remove(Season)} with exception in persistence. */
	@Test
	public void testRemoveWithPersistenceException() {
		final Season season = generate(Season.class);
		doThrow(PersistenceException.class).when(entityManager).contains(any(Season.class));

		try {
			seasonDAO.remove(season);
			fail("Can't remove season with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).contains(season);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SeasonDAO#findSeasonsBySerie(Serie)}. */
	@Test
	public void testFindSeasonsBySerie() {
		final Serie serie = generate(Serie.class);
		final List<Season> seasons = CollectionUtils.newList(generate(Season.class), generate(Season.class));
		when(entityManager.createNamedQuery(anyString(), eq(Season.class))).thenReturn(seasonsQuery);
		when(seasonsQuery.getResultList()).thenReturn(seasons);

		DeepAsserts.assertEquals(seasons, seasonDAO.findSeasonsBySerie(serie));

		verify(entityManager).createNamedQuery(Season.FIND_BY_SERIE, Season.class);
		verify(seasonsQuery).getResultList();
		verify(seasonsQuery).setParameter("serie", serie.getId());
		verifyNoMoreInteractions(entityManager, seasonsQuery);
	}

	/** Test method for {@link SeasonDAOImpl#add(Season)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testFindSeasonsBySerieWithNotSetEntityManager() {
		((SeasonDAOImpl) seasonDAO).setEntityManager(null);
		seasonDAO.findSeasonsBySerie(mock(Serie.class));
	}

	/** Test method for {@link SeasonDAO#findSeasonsBySerie(Serie)} with null argument. */
	@Test
	public void testFindSeasonsBySerieWithNullArgument() {
		try {
			seasonDAO.findSeasonsBySerie(null);
			fail("Can't find seasons by serie with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager, seasonsQuery);
	}

	/** Test method for {@link SeasonDAOImpl#findSeasonsBySerie(Serie)} with exception in persistence. */
	@Test
	public void testFindSeasonsBySerieWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Season.class));

		try {
			seasonDAO.findSeasonsBySerie(generate(Serie.class));
			fail("Can't find seasons by serie with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).createNamedQuery(Season.FIND_BY_SERIE, Season.class);
		verifyNoMoreInteractions(entityManager);
		verifyZeroInteractions(seasonsQuery);
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
				((Season) invocation.getArguments()[0]).setId(id);
				return null;
			}

		};
	}

}
