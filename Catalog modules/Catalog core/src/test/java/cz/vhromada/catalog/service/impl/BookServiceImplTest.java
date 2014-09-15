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

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.BookDAO;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.BookService;
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
 * A class represents test for class {@link BookServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {

	/** Instance of {@link BookDAO} */
	@Mock
	private BookDAO bookDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache bookCache;

	/** Instance of {@link BookService} */
	@InjectMocks
	private BookService bookService = new BookServiceImpl();

	/** Test method for {@link BookServiceImpl#getBookDAO()} and {@link BookServiceImpl#setBookDAO(BookDAO)}. */
	@Test
	public void testBookDAO() {
		final BookServiceImpl bookService = new BookServiceImpl();
		bookService.setBookDAO(bookDAO);
		DeepAsserts.assertEquals(bookDAO, bookService.getBookDAO());
	}

	/** Test method for {@link BookServiceImpl#getBookCache()} and {@link BookServiceImpl#setBookCache(Cache)}. */
	@Test
	public void testBookCache() {
		final BookServiceImpl bookService = new BookServiceImpl();
		bookService.setBookCache(bookCache);
		DeepAsserts.assertEquals(bookCache, bookService.getBookCache());
	}

	/** Test method for {@link BookService#getBook(Integer)} with cached existing book. */
	@Test
	public void testGetBookWithCachedExistingBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID));
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(book));

		DeepAsserts.assertEquals(book, bookService.getBook(PRIMARY_ID));

		verify(bookCache).get("book" + PRIMARY_ID);
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookDAO);
	}

	/** Test method for {@link BookService#getBook(Integer)} with cached not existing book. */
	@Test
	public void testGetBookWithCachedNotExistingBook() {
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(bookService.getBook(PRIMARY_ID));

		verify(bookCache).get("book" + PRIMARY_ID);
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookDAO);
	}

	/** Test method for {@link BookService#getBook(Integer)} with not cached existing book. */
	@Test
	public void testGetBookWithNotCachedExistingBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID));
		when(bookDAO.getBook(anyInt())).thenReturn(book);
		when(bookCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(book, bookService.getBook(PRIMARY_ID));

		verify(bookDAO).getBook(PRIMARY_ID);
		verify(bookCache).get("book" + PRIMARY_ID);
		verify(bookCache).put("book" + PRIMARY_ID, book);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#getBook(Integer)} with not cached not existing book. */
	@Test
	public void testGetBookWithNotCachedNotExistingBook() {
		when(bookDAO.getBook(anyInt())).thenReturn(null);
		when(bookCache.get(anyString())).thenReturn(null);

		assertNull(bookService.getBook(PRIMARY_ID));

		verify(bookDAO).getBook(PRIMARY_ID);
		verify(bookCache).get("book" + PRIMARY_ID);
		verify(bookCache).put("book" + PRIMARY_ID, null);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#getBook(Integer)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.getBook(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookService#getBook(Integer)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookWithNotSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.getBook(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookService#getBook(Integer)} with null argument. */
	@Test
	public void testGetBookWithNullArgument() {
		try {
			bookService.getBook(null);
			fail("Can't get book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#getBook(Integer)} with exception in DAO tier. */
	@Test
	public void testGetBookWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookDAO).getBook(anyInt());
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookService.getBook(Integer.MAX_VALUE);
			fail("Can't get book with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).getBook(Integer.MAX_VALUE);
		verify(bookCache).get("book" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#add(Book)} with cached books. */
	@Test
	public void testAddWithCachedBooks() {
		final Book book = EntityGenerator.createBook(EntityGenerator.createBookCategory(INNER_ID));
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		final List<Book> booksList = new ArrayList<>(books);
		booksList.add(book);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

		bookService.add(book);

		verify(bookDAO).add(book);
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).get("book" + null);
		verify(bookCache).put("books" + INNER_ID, booksList);
		verify(bookCache).put("book" + null, book);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#add(Book)} with not cached books. */
	@Test
	public void testAddWithNotCachedBooks() {
		final Book book = EntityGenerator.createBook(EntityGenerator.createBookCategory(INNER_ID));
		when(bookCache.get(anyString())).thenReturn(null);

		bookService.add(book);

		verify(bookDAO).add(book);
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).get("book" + book.getId());
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#add(Book)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.add(mock(Book.class));
	}

	/** Test method for {@link BookService#add(Book)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.add(mock(Book.class));
	}

	/** Test method for {@link BookService#add(Book)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			bookService.add(null);
			fail("Can't add book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#add(Book)} with exception in DAO tier. */
	@Test
	public void testAddWithDAOTierException() {
		final Book book = EntityGenerator.createBook(EntityGenerator.createBookCategory(INNER_ID));
		doThrow(DataStorageException.class).when(bookDAO).add(any(Book.class));

		try {
			bookService.add(book);
			fail("Can't add book with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).add(book);
		verifyNoMoreInteractions(bookDAO);
		verifyZeroInteractions(bookCache);
	}

	/** Test method for {@link BookService#update(Book)}. */
	@Test
	public void testUpdate() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID));

		bookService.update(book);

		verify(bookDAO).update(book);
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#update(Book)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.update(mock(Book.class));
	}

	/** Test method for {@link BookService#update(Book)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.update(mock(Book.class));
	}

	/** Test method for {@link BookService#update(Book)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			bookService.update(null);
			fail("Can't update book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#update(Book)} with exception in DAO tier. */
	@Test
	public void testUpdateWithDAOTierException() {
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE, EntityGenerator.createBookCategory(INNER_ID));
		doThrow(DataStorageException.class).when(bookDAO).update(any(Book.class));

		try {
			bookService.update(book);
			fail("Can't update book with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).update(book);
		verifyNoMoreInteractions(bookDAO);
		verifyZeroInteractions(bookCache);
	}

	/** Test method for {@link BookService#remove(Book)} with cached books. */
	@Test
	public void testRemoveWithCachedBooks() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID));
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		final List<Book> booksList = new ArrayList<>(books);
		booksList.add(book);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(booksList));

		bookService.remove(book);

		verify(bookDAO).remove(book);
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).put("books" + INNER_ID, books);
		verify(bookCache).evict("book" + PRIMARY_ID);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#remove(Book)} with not cached books. */
	@Test
	public void testRemoveWithNotCachedBooks() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID));
		when(bookCache.get(anyString())).thenReturn(null);

		bookService.remove(book);

		verify(bookDAO).remove(book);
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).evict("book" + PRIMARY_ID);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#remove(Book)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.remove(mock(Book.class));
	}

	/** Test method for {@link BookService#remove(Book)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.remove(mock(Book.class));
	}

	/** Test method for {@link BookService#remove(Book)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			bookService.remove(null);
			fail("Can't remove book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#remove(Book)} with exception in DAO tier. */
	@Test
	public void testRemoveWithDAOTierException() {
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE, EntityGenerator.createBookCategory(INNER_ID));
		doThrow(DataStorageException.class).when(bookDAO).remove(any(Book.class));

		try {
			bookService.remove(book);
			fail("Can't remove book with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).remove(book);
		verifyNoMoreInteractions(bookDAO);
		verifyZeroInteractions(bookCache);
	}

	/** Test method for {@link BookService#duplicate(Book)} with cached books. */
	@Test
	public void testDuplicateWithCachedBooks() {
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(CollectionUtils.newList(mock(Book.class), mock(Book.class))));

		bookService.duplicate(EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID)));

		verify(bookDAO).add(any(Book.class));
		verify(bookDAO).update(any(Book.class));
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).get("book" + null);
		verify(bookCache).put(eq("books" + INNER_ID), anyListOf(Book.class));
		verify(bookCache).put(eq("book" + null), any(Book.class));
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#duplicate(Book)} with not cached books. */
	@Test
	public void testDuplicateWithNotCachedBooks() {
		when(bookCache.get(anyString())).thenReturn(null);

		bookService.duplicate(EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID)));

		verify(bookDAO).add(any(Book.class));
		verify(bookDAO).update(any(Book.class));
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).get("book" + null);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#duplicate(Book)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.duplicate(mock(Book.class));
	}

	/** Test method for {@link BookService#duplicate(Book)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.duplicate(mock(Book.class));
	}

	/** Test method for {@link BookService#duplicate(Book)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		try {
			bookService.duplicate(null);
			fail("Can't duplicate book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#duplicate(Book)} with exception in DAO tier. */
	@Test
	public void testDuplicateWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookDAO).add(any(Book.class));

		try {
			bookService.duplicate(EntityGenerator.createBook(Integer.MAX_VALUE, EntityGenerator.createBookCategory(INNER_ID)));
			fail("Can't duplicate book with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).add(any(Book.class));
		verifyNoMoreInteractions(bookDAO);
		verifyZeroInteractions(bookCache);
	}

	/** Test method for {@link BookService#moveUp(Book)} with cached books. */
	@Test
	public void testMoveUpWithCachedBooks() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book1 = EntityGenerator.createBook(PRIMARY_ID, bookCategory);
		book1.setPosition(MOVE_POSITION);
		final Book book2 = EntityGenerator.createBook(SECONDARY_ID, bookCategory);
		final List<Book> books = CollectionUtils.newList(book1, book2);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

		bookService.moveUp(book2);
		DeepAsserts.assertEquals(POSITION, book1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, book2.getPosition());

		verify(bookDAO).update(book1);
		verify(bookDAO).update(book2);
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#moveUp(Book)} with not cached books. */
	@Test
	public void testMoveUpWithNotCachedBooks() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book1 = EntityGenerator.createBook(PRIMARY_ID, bookCategory);
		book1.setPosition(MOVE_POSITION);
		final Book book2 = EntityGenerator.createBook(SECONDARY_ID, bookCategory);
		final List<Book> books = CollectionUtils.newList(book1, book2);
		when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		when(bookCache.get(anyString())).thenReturn(null);

		bookService.moveUp(book2);
		DeepAsserts.assertEquals(POSITION, book1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, book2.getPosition());

		verify(bookDAO).update(book1);
		verify(bookDAO).update(book2);
		verify(bookDAO).findBooksByBookCategory(bookCategory);
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#moveUp(Book)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.moveUp(mock(Book.class));
	}

	/** Test method for {@link BookService#moveUp(Book)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.moveUp(mock(Book.class));
	}

	/** Test method for {@link BookService#moveUp(Book)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		try {
			bookService.moveUp(null);
			fail("Can't move up book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#moveUp(Book)} with exception in DAO tier. */
	@Test
	public void testMoveUpWithDAOTierException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		doThrow(DataStorageException.class).when(bookDAO).findBooksByBookCategory(any(BookCategory.class));
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookService.moveUp(EntityGenerator.createBook(Integer.MAX_VALUE, bookCategory));
			fail("Can't move up book with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).findBooksByBookCategory(bookCategory);
		verify(bookCache).get("books" + INNER_ID);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#moveDown(Book)} with cached books. */
	@Test
	public void testMoveDownWithCachedBooks() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book1 = EntityGenerator.createBook(PRIMARY_ID, bookCategory);
		final Book book2 = EntityGenerator.createBook(SECONDARY_ID, bookCategory);
		book2.setPosition(MOVE_POSITION);
		final List<Book> books = CollectionUtils.newList(book1, book2);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

		bookService.moveDown(book1);
		DeepAsserts.assertEquals(MOVE_POSITION, book1.getPosition());
		DeepAsserts.assertEquals(POSITION, book2.getPosition());

		verify(bookDAO).update(book1);
		verify(bookDAO).update(book2);
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#moveDown(Book)} with not cached books. */
	@Test
	public void testMoveDownWithNotCachedBooks() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book1 = EntityGenerator.createBook(PRIMARY_ID, bookCategory);
		final Book book2 = EntityGenerator.createBook(SECONDARY_ID, bookCategory);
		book2.setPosition(MOVE_POSITION);
		final List<Book> books = CollectionUtils.newList(book1, book2);
		when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		when(bookCache.get(anyString())).thenReturn(null);

		bookService.moveDown(book1);
		DeepAsserts.assertEquals(MOVE_POSITION, book1.getPosition());
		DeepAsserts.assertEquals(POSITION, book2.getPosition());

		verify(bookDAO).update(book1);
		verify(bookDAO).update(book2);
		verify(bookDAO).findBooksByBookCategory(bookCategory);
		verify(bookCache).get("books" + INNER_ID);
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#moveDown(Book)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.moveDown(mock(Book.class));
	}

	/** Test method for {@link BookService#moveDown(Book)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.moveDown(mock(Book.class));
	}

	/** Test method for {@link BookService#moveDown(Book)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		try {
			bookService.moveDown(null);
			fail("Can't move down book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#moveDown(Book)} with exception in DAO tier. */
	@Test
	public void testMoveDownWithDAOTierException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		doThrow(DataStorageException.class).when(bookDAO).findBooksByBookCategory(any(BookCategory.class));
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookService.moveDown(EntityGenerator.createBook(Integer.MAX_VALUE, bookCategory));
			fail("Can't move down book with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).findBooksByBookCategory(bookCategory);
		verify(bookCache).get("books" + INNER_ID);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#exists(Book)} with cached existing book. */
	@Test
	public void testExistsWithCachedExistingBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID));
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(book));

		assertTrue(bookService.exists(book));

		verify(bookCache).get("book" + PRIMARY_ID);
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookDAO);
	}

	/** Test method for {@link BookService#exists(Book)} with cached not existing book. */
	@Test
	public void testExistsWithCachedNotExistingBook() {
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE, EntityGenerator.createBookCategory(INNER_ID));
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(bookService.exists(book));

		verify(bookCache).get("book" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookDAO);
	}

	/** Test method for {@link BookService#exists(Book)} with not cached existing book. */
	@Test
	public void testExistsWithNotCachedExistingBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID));
		when(bookDAO.getBook(anyInt())).thenReturn(book);
		when(bookCache.get(anyString())).thenReturn(null);

		assertTrue(bookService.exists(book));

		verify(bookDAO).getBook(PRIMARY_ID);
		verify(bookCache).get("book" + PRIMARY_ID);
		verify(bookCache).put("book" + PRIMARY_ID, book);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#exists(Book)} with not cached not existing book. */
	@Test
	public void testExistsWithNotCachedNotExistingBook() {
		when(bookDAO.getBook(anyInt())).thenReturn(null);
		when(bookCache.get(anyString())).thenReturn(null);

		assertFalse(bookService.exists(EntityGenerator.createBook(Integer.MAX_VALUE, EntityGenerator.createBookCategory(INNER_ID))));

		verify(bookDAO).getBook(Integer.MAX_VALUE);
		verify(bookCache).get("book" + Integer.MAX_VALUE);
		verify(bookCache).put("book" + Integer.MAX_VALUE, null);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#exists(Book)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.exists(mock(Book.class));
	}

	/** Test method for {@link BookService#exists(Book)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.exists(mock(Book.class));
	}

	/** Test method for {@link BookService#exists(Book)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		try {
			bookService.exists(null);
			fail("Can't exists book with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#exists(Book)} with exception in DAO tier. */
	@Test
	public void testExistsWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookDAO).getBook(anyInt());
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookService.exists(EntityGenerator.createBook(Integer.MAX_VALUE, EntityGenerator.createBookCategory(INNER_ID)));
			fail("Can't exists book with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).getBook(Integer.MAX_VALUE);
		verify(bookCache).get("book" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#findBooksByBookCategory(BookCategory)} with cached books. */
	@Test
	public void testFindBooksByBookCategoryWithCachedBooks() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

		DeepAsserts.assertEquals(books, bookService.findBooksByBookCategory(bookCategory));

		verify(bookCache).get("books" + PRIMARY_ID);
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookDAO);
	}

	/** Test method for {@link BookService#findBooksByBookCategory(BookCategory)} with not cached books. */
	@Test
	public void testFindBooksByBookCategoryWithNotCachedBooks() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		when(bookCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(books, bookService.findBooksByBookCategory(bookCategory));

		verify(bookDAO).findBooksByBookCategory(bookCategory);
		verify(bookCache).get("books" + PRIMARY_ID);
		verify(bookCache).put("books" + PRIMARY_ID, books);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#findBooksByBookCategory(BookCategory)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testFindBooksByBookCategoryWithNotSetBookDAO() {
		((BookServiceImpl) bookService).setBookDAO(null);
		bookService.findBooksByBookCategory(mock(BookCategory.class));
	}

	/** Test method for {@link BookService#findBooksByBookCategory(BookCategory)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testFindBooksByBookCategoryWithNotSetBookCache() {
		((BookServiceImpl) bookService).setBookCache(null);
		bookService.findBooksByBookCategory(mock(BookCategory.class));
	}

	/** Test method for {@link BookService#findBooksByBookCategory(BookCategory)} with null argument. */
	@Test
	public void testFindBooksByBookCategoryWithNullArgument() {
		try {
			bookService.findBooksByBookCategory(null);
			fail("Can't find books by book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookService#findBooksByBookCategory(BookCategory)} with exception in DAO tier. */
	@Test
	public void testFindBooksByBookCategoryWithDAOTierException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(bookDAO).findBooksByBookCategory(any(BookCategory.class));
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookService.findBooksByBookCategory(bookCategory);
			fail("Can't find books by book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).findBooksByBookCategory(bookCategory);
		verify(bookCache).get("books" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(bookDAO, bookCache);
	}

}
