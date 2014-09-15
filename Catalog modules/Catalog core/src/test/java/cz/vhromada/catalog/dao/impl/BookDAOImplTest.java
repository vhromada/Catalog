package cz.vhromada.catalog.dao.impl;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_INNER_INNER_ID;
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

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.BookDAO;
import cz.vhromada.catalog.dao.entities.Book;
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
 * A class represents test for class {@link BookDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class BookDAOImplTest {

	/** Instance of {@link EntityManager} */
	@Mock
	private EntityManager entityManager;

	/** Query for books */
	@Mock
	private TypedQuery<Book> booksQuery;

	/** Instance of {@link BookDAO} */
	@InjectMocks
	private BookDAO bookDAO = new BookDAOImpl();

	/** Test method for {@link BookDAOImpl#getEntityManager()} and {@link BookDAOImpl#setEntityManager(EntityManager)}. */
	@Test
	public void testEntityManager() {
		final BookDAOImpl bookDAOImpl = new BookDAOImpl();
		bookDAOImpl.setEntityManager(entityManager);
		DeepAsserts.assertEquals(entityManager, bookDAOImpl.getEntityManager());
	}

	/** Test method for {@link BookDAO#getBook(Integer)} with existing book. */
	@Test
	public void testGetBookWithExistingBook() {
		final Book book = mock(Book.class);
		when(entityManager.find(eq(Book.class), anyInt())).thenReturn(book);

		DeepAsserts.assertEquals(book, bookDAO.getBook(PRIMARY_ID));

		verify(entityManager).find(Book.class, PRIMARY_ID);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAO#getBook(Integer)} with not existing book. */
	@Test
	public void testGetBookWithNotExistingBook() {
		when(entityManager.find(eq(Book.class), anyInt())).thenReturn(null);

		assertNull(bookDAO.getBook(Integer.MAX_VALUE));

		verify(entityManager).find(Book.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAOImpl#getBook(Integer)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookWithNotSetEntityManager() {
		((BookDAOImpl) bookDAO).setEntityManager(null);
		bookDAO.getBook(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookDAO#getBook(Integer)} with null argument. */
	@Test
	public void testGetBookWithNullArgument() {
		try {
			bookDAO.getBook(null);
			fail("Can't get book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link BookDAOImpl#getBook(Integer)} with exception in persistence. */
	@Test
	public void testGetBookWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).find(eq(Book.class), anyInt());

		try {
			bookDAO.getBook(Integer.MAX_VALUE);
			fail("Can't get book with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).find(Book.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAO#add(Book)}. */
	@Test
	public void testAdd() {
		final Book book = EntityGenerator.createBook();
		doAnswer(setId(ID)).when(entityManager).persist(any(Book.class));

		bookDAO.add(book);
		DeepAsserts.assertEquals(ID, book.getId());
		DeepAsserts.assertEquals(ID - 1, book.getPosition());

		verify(entityManager).persist(book);
		verify(entityManager).merge(book);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAOImpl#add(Book)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEntityManager() {
		((BookDAOImpl) bookDAO).setEntityManager(null);
		bookDAO.add(mock(Book.class));
	}

	/** Test method for {@link BookDAO#add(Book)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			bookDAO.add(null);
			fail("Can't add book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link BookDAOImpl#add(Book)} with exception in persistence. */
	@Test
	public void testAddWithPersistenceException() {
		final Book book = EntityGenerator.createBook();
		doThrow(PersistenceException.class).when(entityManager).persist(any(Book.class));

		try {
			bookDAO.add(book);
			fail("Can't add book with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).persist(book);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAO#update(Book)}. */
	@Test
	public void testUpdate() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID);

		bookDAO.update(book);

		verify(entityManager).merge(book);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAOImpl#update(Book)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEntityManager() {
		((BookDAOImpl) bookDAO).setEntityManager(null);
		bookDAO.update(mock(Book.class));
	}

	/** Test method for {@link BookDAO#update(Book)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			bookDAO.update(null);
			fail("Can't update book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link BookDAOImpl#update(Book)} with exception in persistence. */
	@Test
	public void testUpdateWithPersistenceException() {
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE);
		doThrow(PersistenceException.class).when(entityManager).merge(any(Book.class));

		try {
			bookDAO.update(book);
			fail("Can't update book with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).merge(book);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAO#remove(Book)} with managed book. */
	@Test
	public void testRemoveWithManagedBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID);
		when(entityManager.contains(any(Book.class))).thenReturn(true);

		bookDAO.remove(book);

		verify(entityManager).contains(book);
		verify(entityManager).remove(book);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAO#remove(Book)} with not managed book. */
	@Test
	public void testRemoveWithNotManagedBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID);
		when(entityManager.contains(any(Book.class))).thenReturn(false);
		when(entityManager.getReference(eq(Book.class), anyInt())).thenReturn(book);

		bookDAO.remove(book);

		verify(entityManager).contains(book);
		verify(entityManager).getReference(Book.class, PRIMARY_ID);
		verify(entityManager).remove(book);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAOImpl#remove(Book)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEntityManager() {
		((BookDAOImpl) bookDAO).setEntityManager(null);
		bookDAO.remove(mock(Book.class));
	}

	/** Test method for {@link BookDAO#remove(Book)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			bookDAO.remove(null);
			fail("Can't remove book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link BookDAOImpl#remove(Book)} with exception in persistence. */
	@Test
	public void testRemoveWithPersistenceException() {
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE);
		doThrow(PersistenceException.class).when(entityManager).contains(any(Book.class));

		try {
			bookDAO.remove(book);
			fail("Can't remove book with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).contains(book);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link BookDAO#findBooksByBookCategory(BookCategory)}. */
	@Test
	public void testFindBooksByBookCategory() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final List<Book> books = CollectionUtils.newList(EntityGenerator.createBook(INNER_INNER_ID, bookCategory),
				EntityGenerator.createBook(SECONDARY_INNER_INNER_ID, bookCategory));
		when(entityManager.createNamedQuery(anyString(), eq(Book.class))).thenReturn(booksQuery);
		when(booksQuery.getResultList()).thenReturn(books);

		DeepAsserts.assertEquals(books, bookDAO.findBooksByBookCategory(bookCategory));

		verify(entityManager).createNamedQuery(Book.FIND_BY_BOOK_CATEGORY, Book.class);
		verify(booksQuery).getResultList();
		verify(booksQuery).setParameter("bookCategory", bookCategory.getId());
		verifyNoMoreInteractions(entityManager, booksQuery);
	}

	/** Test method for {@link BookDAOImpl#add(Book)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testFindBooksByBookCategoryWithNotSetEntityManager() {
		((BookDAOImpl) bookDAO).setEntityManager(null);
		bookDAO.findBooksByBookCategory(mock(BookCategory.class));
	}

	/** Test method for {@link BookDAO#findBooksByBookCategory(BookCategory)} with null argument. */
	@Test
	public void testFindBooksByBookCategoryWithNullArgument() {
		try {
			bookDAO.findBooksByBookCategory(null);
			fail("Can't find books by book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager, booksQuery);
	}

	/** Test method for {@link BookDAOImpl#findBooksByBookCategory(BookCategory)} with exception in persistence. */
	@Test
	public void testFindBooksByBookCategoryWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Book.class));

		try {
			bookDAO.findBooksByBookCategory(EntityGenerator.createBookCategory(Integer.MAX_VALUE));
			fail("Can't find books by book category with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).createNamedQuery(Book.FIND_BY_BOOK_CATEGORY, Book.class);
		verifyNoMoreInteractions(entityManager);
		verifyZeroInteractions(booksQuery);
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
				((Book) invocation.getArguments()[0]).setId(id);
				return null;
			}

		};
	}

}
