package cz.vhromada.catalog.dao.impl;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_ID;
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

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.BookCategoryDAO;
import cz.vhromada.catalog.dao.entities.BookCategory;
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
 * A class represents test for class {@link BookCategoryDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class BookCategoryDAOImplTest {

	/** Instance of {@link EntityManager} */
	@Mock
	private EntityManager entityManager;

	/** Query for book categories */
	@Mock
	private TypedQuery<BookCategory> bookCategoriesQuery;

	/** Instance of {@link BookCategoryDAO} */
	@InjectMocks
	private BookCategoryDAO bookCategoryDAO = new BookCategoryDAOImpl();

	/** Test method for {@link BookCategoryDAOImpl#getEntityManager()} and {@link BookCategoryDAOImpl#setEntityManager(EntityManager)}. */
	@Test
	public void testEntityManager() {
		final BookCategoryDAOImpl bookCategoryDAOImpl = new BookCategoryDAOImpl();
		bookCategoryDAOImpl.setEntityManager(entityManager);
		DeepAsserts.assertEquals(entityManager, bookCategoryDAOImpl.getEntityManager());
	}

	/** Test method for {@link BookCategoryDAO#getBookCategories()}. */
	@Test
	public void testGetBookCategories() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(EntityGenerator.createBookCategory(PRIMARY_ID),
				EntityGenerator.createBookCategory(SECONDARY_ID));
		when(entityManager.createNamedQuery(anyString(), eq(BookCategory.class))).thenReturn(bookCategoriesQuery);
		when(bookCategoriesQuery.getResultList()).thenReturn(bookCategories);

		DeepAsserts.assertEquals(bookCategories, bookCategoryDAO.getBookCategories());

		verify(entityManager).createNamedQuery(BookCategory.SELECT_BOOK_CATEGORIES, BookCategory.class);
		verify(bookCategoriesQuery).getResultList();
		verifyNoMoreInteractions(entityManager, bookCategoriesQuery);
	}

	/** Test method for {@link BookCategoryDAOImpl#getBookCategories()} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoriesWithNotSetEntityManager() {
		((BookCategoryDAOImpl) bookCategoryDAO).setEntityManager(null);
		bookCategoryDAO.getBookCategories();
	}

	/** Test method for {@link BookCategoryDAOImpl#getBookCategories()} with exception in persistence. */
	@Test
	public void testGetBookCategoriesWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(BookCategory.class));

		try {
			bookCategoryDAO.getBookCategories();
			fail("Can't get book categories with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).createNamedQuery(BookCategory.SELECT_BOOK_CATEGORIES, BookCategory.class);
		verifyNoMoreInteractions(entityManager);
		verifyZeroInteractions(bookCategoriesQuery);
	}

	/** Test method for {@link BookCategoryDAO#getBookCategory(Integer)} with existing bookCategory. */
	@Test
	public void testGetBookCategoryWithExistingBookCategory() {
		final BookCategory bookCategory = mock(BookCategory.class);
		when(entityManager.find(eq(BookCategory.class), anyInt())).thenReturn(bookCategory);

		DeepAsserts.assertEquals(bookCategory, bookCategoryDAO.getBookCategory(PRIMARY_ID));

		verify(entityManager).find(BookCategory.class, PRIMARY_ID);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAO#getBookCategory(Integer)} with not existing bookCategory. */
	@Test
	public void testGetBookCategoryWithNotExistingBookCategory() {
		when(entityManager.find(eq(BookCategory.class), anyInt())).thenReturn(null);

		assertNull(bookCategoryDAO.getBookCategory(Integer.MAX_VALUE));

		verify(entityManager).find(BookCategory.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAOImpl#getBookCategory(Integer)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoryWithNotSetEntityManager() {
		((BookCategoryDAOImpl) bookCategoryDAO).setEntityManager(null);
		bookCategoryDAO.getBookCategory(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookCategoryDAO#getBookCategory(Integer)} with null argument. */
	@Test
	public void testGetBookCategoryWithNullArgument() {
		try {
			bookCategoryDAO.getBookCategory(null);
			fail("Can't get book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAOImpl#getBookCategory(Integer)} with exception in persistence. */
	@Test
	public void testGetBookCategoryWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).find(eq(BookCategory.class), anyInt());

		try {
			bookCategoryDAO.getBookCategory(Integer.MAX_VALUE);
			fail("Can't get book category with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).find(BookCategory.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAO#add(BookCategory)} with empty data storage. */
	@Test
	public void testAddWithEmptyDataStorage() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		doAnswer(setId(ID)).when(entityManager).persist(any(BookCategory.class));

		bookCategoryDAO.add(bookCategory);
		DeepAsserts.assertEquals(ID, bookCategory.getId());
		DeepAsserts.assertEquals(ID - 1, bookCategory.getPosition());

		verify(entityManager).persist(bookCategory);
		verify(entityManager).merge(bookCategory);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAOImpl#add(BookCategory)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEntityManager() {
		((BookCategoryDAOImpl) bookCategoryDAO).setEntityManager(null);
		bookCategoryDAO.add(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryDAO#add(BookCategory)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			bookCategoryDAO.add(null);
			fail("Can't add book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAOImpl#add(BookCategory)} with exception in persistence. */
	@Test
	public void testAddWithPersistenceException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		doThrow(PersistenceException.class).when(entityManager).persist(any(BookCategory.class));

		try {
			bookCategoryDAO.add(bookCategory);
			fail("Can't add book category with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).persist(bookCategory);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAO#update(BookCategory)}. */
	@Test
	public void testUpdate() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);

		bookCategoryDAO.update(bookCategory);

		verify(entityManager).merge(bookCategory);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAOImpl#update(BookCategory)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEntityManager() {
		((BookCategoryDAOImpl) bookCategoryDAO).setEntityManager(null);
		bookCategoryDAO.update(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryDAO#update(BookCategory)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			bookCategoryDAO.update(null);
			fail("Can't update book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAOImpl#update(BookCategory)} with exception in persistence. */
	@Test
	public void testUpdateWithPersistenceException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(PersistenceException.class).when(entityManager).merge(any(BookCategory.class));

		try {
			bookCategoryDAO.update(bookCategory);
			fail("Can't update book category with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).merge(bookCategory);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAO#remove(BookCategory)} with managed book category. */
	@Test
	public void testRemoveWithManagedBookCategory() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		when(entityManager.contains(any(BookCategory.class))).thenReturn(true);

		bookCategoryDAO.remove(bookCategory);

		verify(entityManager).contains(bookCategory);
		verify(entityManager).remove(bookCategory);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAO#remove(BookCategory)} with not managed book category. */
	@Test
	public void testRemoveWithNotManagedBookCategory() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		when(entityManager.contains(any(BookCategory.class))).thenReturn(false);
		when(entityManager.getReference(eq(BookCategory.class), anyInt())).thenReturn(bookCategory);

		bookCategoryDAO.remove(bookCategory);

		verify(entityManager).contains(bookCategory);
		verify(entityManager).getReference(BookCategory.class, PRIMARY_ID);
		verify(entityManager).remove(bookCategory);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAOImpl#remove(BookCategory)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEntityManager() {
		((BookCategoryDAOImpl) bookCategoryDAO).setEntityManager(null);
		bookCategoryDAO.remove(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryDAO#remove(BookCategory)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			bookCategoryDAO.remove(null);
			fail("Can't remove book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link BookCategoryDAOImpl#remove(BookCategory)} with exception in persistence. */
	@Test
	public void testRemoveWithPersistenceException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		doThrow(PersistenceException.class).when(entityManager).contains(any(BookCategory.class));

		try {
			bookCategoryDAO.remove(bookCategory);
			fail("Can't remove book category with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).contains(bookCategory);
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
				((BookCategory) invocation.getArguments()[0]).setId(id);
				return null;
			}

		};
	}

}
