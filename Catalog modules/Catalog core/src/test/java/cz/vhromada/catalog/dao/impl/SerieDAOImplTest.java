package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
import cz.vhromada.catalog.dao.SerieDAO;
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
 * A class represents test for class {@link SerieDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SerieDAOImplTest extends ObjectGeneratorTest {

	/** Instance of {@link EntityManager} */
	@Mock
	private EntityManager entityManager;

	/** Query for series */
	@Mock
	private TypedQuery<Serie> seriesQuery;

	/** Instance of {@link SerieDAO} */
	@InjectMocks
	private SerieDAO serieDAO = new SerieDAOImpl();

	/** Test method for {@link SerieDAO#getSeries()}. */
	@Test
	public void testGetSeries() {
		final List<Serie> series = CollectionUtils.newList(generate(Serie.class), generate(Serie.class));
		when(entityManager.createNamedQuery(anyString(), eq(Serie.class))).thenReturn(seriesQuery);
		when(seriesQuery.getResultList()).thenReturn(series);

		DeepAsserts.assertEquals(series, serieDAO.getSeries());

		verify(entityManager).createNamedQuery(Serie.SELECT_SERIES, Serie.class);
		verify(seriesQuery).getResultList();
		verifyNoMoreInteractions(entityManager, seriesQuery);
	}

	/** Test method for {@link SerieDAOImpl#getSeries()} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetSeriesWithNotSetEntityManager() {
		((SerieDAOImpl) serieDAO).setEntityManager(null);
		serieDAO.getSeries();
	}

	/** Test method for {@link SerieDAOImpl#getSeries()} with exception in persistence. */
	@Test
	public void testGetSeriesWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Serie.class));

		try {
			serieDAO.getSeries();
			fail("Can't get series with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).createNamedQuery(Serie.SELECT_SERIES, Serie.class);
		verifyNoMoreInteractions(entityManager);
		verifyZeroInteractions(seriesQuery);
	}

	/** Test method for {@link SerieDAO#getSerie(Integer)} with existing serie. */
	@Test
	public void testGetSerieWithExistingSerie() {
		final int id = generate(Integer.class);
		final Serie serie = mock(Serie.class);
		when(entityManager.find(eq(Serie.class), anyInt())).thenReturn(serie);

		DeepAsserts.assertEquals(serie, serieDAO.getSerie(id));

		verify(entityManager).find(Serie.class, id);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAO#getSerie(Integer)} with not existing serie. */
	@Test
	public void testGetSerieWithNotExistingSerie() {
		when(entityManager.find(eq(Serie.class), anyInt())).thenReturn(null);

		assertNull(serieDAO.getSerie(Integer.MAX_VALUE));

		verify(entityManager).find(Serie.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAOImpl#getSerie(Integer)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetSerieWithNotSetEntityManager() {
		((SerieDAOImpl) serieDAO).setEntityManager(null);
		serieDAO.getSerie(Integer.MAX_VALUE);
	}

	/** Test method for {@link SerieDAO#getSerie(Integer)} with null argument. */
	@Test
	public void testGetSerieWithNullArgument() {
		try {
			serieDAO.getSerie(null);
			fail("Can't get serie with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link SerieDAOImpl#getSerie(Integer)} with exception in persistence. */
	@Test
	public void testGetSerieWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).find(eq(Serie.class), anyInt());

		try {
			serieDAO.getSerie(Integer.MAX_VALUE);
			fail("Can't get serie with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).find(Serie.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAO#add(Serie)}. */
	@Test
	public void testAdd() {
		final Serie serie = generate(Serie.class);
		final int id = generate(Integer.class);
		doAnswer(setId(id)).when(entityManager).persist(any(Serie.class));

		serieDAO.add(serie);
		DeepAsserts.assertEquals(id, serie.getId());
		DeepAsserts.assertEquals(id - 1, serie.getPosition());

		verify(entityManager).persist(serie);
		verify(entityManager).merge(serie);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAOImpl#add(Serie)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEntityManager() {
		((SerieDAOImpl) serieDAO).setEntityManager(null);
		serieDAO.add(mock(Serie.class));
	}

	/** Test method for {@link SerieDAO#add(Serie)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			serieDAO.add(null);
			fail("Can't add serie with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link SerieDAOImpl#add(Serie)} with exception in persistence. */
	@Test
	public void testAddWithPersistenceException() {
		final Serie serie = generate(Serie.class);
		doThrow(PersistenceException.class).when(entityManager).persist(any(Serie.class));

		try {
			serieDAO.add(serie);
			fail("Can't add serie with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).persist(serie);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAO#update(Serie)}. */
	@Test
	public void testUpdate() {
		final Serie serie = generate(Serie.class);

		serieDAO.update(serie);

		verify(entityManager).merge(serie);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAOImpl#update(Serie)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEntityManager() {
		((SerieDAOImpl) serieDAO).setEntityManager(null);
		serieDAO.update(mock(Serie.class));
	}

	/** Test method for {@link SerieDAO#update(Serie)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			serieDAO.update(null);
			fail("Can't update serie with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link SerieDAOImpl#update(Serie)} with exception in persistence. */
	@Test
	public void testUpdateWithPersistenceException() {
		final Serie serie = generate(Serie.class);
		doThrow(PersistenceException.class).when(entityManager).merge(any(Serie.class));

		try {
			serieDAO.update(serie);
			fail("Can't update serie with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).merge(serie);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAO#remove(Serie)} with managed serie. */
	@Test
	public void testRemoveWithManagedSerie() {
		final Serie serie = generate(Serie.class);
		when(entityManager.contains(any(Serie.class))).thenReturn(true);

		serieDAO.remove(serie);

		verify(entityManager).contains(serie);
		verify(entityManager).remove(serie);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAO#remove(Serie)} with not managed serie. */
	@Test
	public void testRemoveWithNotManagedSerie() {
		final Serie serie = generate(Serie.class);
		when(entityManager.contains(any(Serie.class))).thenReturn(false);
		when(entityManager.getReference(eq(Serie.class), anyInt())).thenReturn(serie);

		serieDAO.remove(serie);

		verify(entityManager).contains(serie);
		verify(entityManager).getReference(Serie.class, serie.getId());
		verify(entityManager).remove(serie);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link SerieDAOImpl#remove(Serie)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEntityManager() {
		((SerieDAOImpl) serieDAO).setEntityManager(null);
		serieDAO.remove(mock(Serie.class));
	}

	/** Test method for {@link SerieDAO#remove(Serie)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			serieDAO.remove(null);
			fail("Can't remove serie with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link SerieDAOImpl#remove(Serie)} with exception in persistence. */
	@Test
	public void testRemoveWithPersistenceException() {
		final Serie serie = generate(Serie.class);
		doThrow(PersistenceException.class).when(entityManager).contains(any(Serie.class));

		try {
			serieDAO.remove(serie);
			fail("Can't remove serie with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).contains(serie);
		verifyNoMoreInteractions(entityManager);
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
				((Serie) invocation.getArguments()[0]).setId(id);
				return null;
			}

		};
	}

}
