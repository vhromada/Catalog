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
import cz.vhromada.catalog.dao.GenreDAO;
import cz.vhromada.catalog.dao.entities.Genre;
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
 * A class represents test for class {@link GenreDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GenreDAOImplTest extends ObjectGeneratorTest {

	/** Instance of {@link EntityManager} */
	@Mock
	private EntityManager entityManager;

	/** Query for genres */
	@Mock
	private TypedQuery<Genre> genresQuery;

	/** Instance of {@link GenreDAO} */
	@InjectMocks
	private GenreDAO genreDAO = new GenreDAOImpl();

	/** Test method for {@link GenreDAO#getGenres()}. */
	@Test
	public void testGetGenres() {
		final List<Genre> genres = CollectionUtils.newList(generate(Genre.class), generate(Genre.class));
		when(entityManager.createNamedQuery(anyString(), eq(Genre.class))).thenReturn(genresQuery);
		when(genresQuery.getResultList()).thenReturn(genres);

		DeepAsserts.assertEquals(genres, genreDAO.getGenres());

		verify(entityManager).createNamedQuery(Genre.SELECT_GENRES, Genre.class);
		verify(genresQuery).getResultList();
		verifyNoMoreInteractions(entityManager, genresQuery);
	}

	/** Test method for {@link GenreDAOImpl#getGenres()} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenresWithNotSetEntityManager() {
		((GenreDAOImpl) genreDAO).setEntityManager(null);
		genreDAO.getGenres();
	}

	/** Test method for {@link GenreDAOImpl#getGenres()} with exception in persistence. */
	@Test
	public void testGetGenresWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Genre.class));

		try {
			genreDAO.getGenres();
			fail("Can't get genres with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).createNamedQuery(Genre.SELECT_GENRES, Genre.class);
		verifyNoMoreInteractions(entityManager);
		verifyZeroInteractions(genresQuery);
	}

	/** Test method for {@link GenreDAO#getGenre(Integer)} with existing genre. */
	@Test
	public void testGetGenreWithExistingGenre() {
		final Genre genre = mock(Genre.class);
		final int id = generate(Integer.class);
		when(entityManager.find(eq(Genre.class), anyInt())).thenReturn(genre);

		DeepAsserts.assertEquals(genre, genreDAO.getGenre(id));

		verify(entityManager).find(Genre.class, id);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAO#getGenre(Integer)} with not existing genre. */
	@Test
	public void testGetGenreWithNotExistingGenre() {
		when(entityManager.find(eq(Genre.class), anyInt())).thenReturn(null);

		assertNull(genreDAO.getGenre(Integer.MAX_VALUE));

		verify(entityManager).find(Genre.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAOImpl#getGenre(Integer)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetGenreWithNotSetEntityManager() {
		((GenreDAOImpl) genreDAO).setEntityManager(null);
		genreDAO.getGenre(Integer.MAX_VALUE);
	}

	/** Test method for {@link GenreDAO#getGenre(Integer)} with null argument. */
	@Test
	public void testGetGenreWithNullArgument() {
		try {
			genreDAO.getGenre(null);
			fail("Can't get genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link GenreDAOImpl#getGenre(Integer)} with exception in persistence. */
	@Test
	public void testGetGenreWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).find(eq(Genre.class), anyInt());

		try {
			genreDAO.getGenre(Integer.MAX_VALUE);
			fail("Can't get genre with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).find(Genre.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAO#add(Genre)}. */
	@Test
	public void testAdd() {
		final int id = generate(Integer.class);
		final Genre genre = generate(Genre.class);
		doAnswer(setId(id)).when(entityManager).persist(any(Genre.class));

		genreDAO.add(genre);
		DeepAsserts.assertEquals(id, genre.getId());

		verify(entityManager).persist(genre);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAOImpl#add(Genre)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEntityManager() {
		((GenreDAOImpl) genreDAO).setEntityManager(null);
		genreDAO.add(mock(Genre.class));
	}

	/** Test method for {@link GenreDAO#add(Genre)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			genreDAO.add(null);
			fail("Can't add genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link GenreDAOImpl#add(Genre)} with exception in persistence. */
	@Test
	public void testAddWithPersistenceException() {
		final Genre genre = generate(Genre.class);
		doThrow(PersistenceException.class).when(entityManager).persist(any(Genre.class));

		try {
			genreDAO.add(genre);
			fail("Can't add genre with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).persist(genre);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAO#update(Genre)}. */
	@Test
	public void testUpdate() {
		final Genre genre = generate(Genre.class);

		genreDAO.update(genre);

		verify(entityManager).merge(genre);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAOImpl#update(Genre)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEntityManager() {
		((GenreDAOImpl) genreDAO).setEntityManager(null);
		genreDAO.update(mock(Genre.class));
	}

	/** Test method for {@link GenreDAO#update(Genre)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			genreDAO.update(null);
			fail("Can't update genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link GenreDAOImpl#update(Genre)} with exception in persistence. */
	@Test
	public void testUpdateWithPersistenceException() {
		final Genre genre = generate(Genre.class);
		doThrow(PersistenceException.class).when(entityManager).merge(any(Genre.class));

		try {
			genreDAO.update(genre);
			fail("Can't update genre with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).merge(genre);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAO#remove(Genre)} with managed genre. */
	@Test
	public void testRemoveWithManagedGenre() {
		final Genre genre = generate(Genre.class);
		when(entityManager.contains(any(Genre.class))).thenReturn(true);

		genreDAO.remove(genre);

		verify(entityManager).contains(genre);
		verify(entityManager).remove(genre);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAO#remove(Genre)} with not managed genre. */
	@Test
	public void testRemoveWithNotManagedGenre() {
		final Genre genre = generate(Genre.class);
		when(entityManager.contains(any(Genre.class))).thenReturn(false);
		when(entityManager.getReference(eq(Genre.class), anyInt())).thenReturn(genre);

		genreDAO.remove(genre);

		verify(entityManager).contains(genre);
		verify(entityManager).getReference(Genre.class, genre.getId());
		verify(entityManager).remove(genre);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GenreDAOImpl#remove(Genre)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEntityManager() {
		((GenreDAOImpl) genreDAO).setEntityManager(null);
		genreDAO.remove(mock(Genre.class));
	}

	/** Test method for {@link GenreDAO#remove(Genre)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			genreDAO.remove(null);
			fail("Can't remove genre with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link GenreDAOImpl#remove(Genre)} with exception in persistence. */
	@Test
	public void testRemoveWithPersistenceException() {
		final Genre genre = generate(Genre.class);
		doThrow(PersistenceException.class).when(entityManager).contains(any(Genre.class));

		try {
			genreDAO.remove(genre);
			fail("Can't remove genre with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).contains(genre);
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
				((Genre) invocation.getArguments()[0]).setId(id);
				return null;
			}

		};
	}

}
