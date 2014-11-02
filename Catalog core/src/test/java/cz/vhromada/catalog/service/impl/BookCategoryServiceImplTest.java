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
import cz.vhromada.catalog.dao.BookCategoryDAO;
import cz.vhromada.catalog.dao.BookDAO;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.BookCategoryService;
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
 * A class represents test for class {@link BookCategoryServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class BookCategoryServiceImplTest extends ObjectGeneratorTest {

	/** Instance of {@link BookCategoryDAO} */
	@Mock
	private BookCategoryDAO bookCategoryDAO;

	/** Instance of {@link BookDAO} */
	@Mock
	private BookDAO bookDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache bookCache;

	/** Instance of {@link BookCategoryService} */
	@InjectMocks
	private BookCategoryService bookCategoryService = new BookCategoryServiceImpl();

	/** Test method for {@link BookCategoryService#newData()} with cached data. */
	@Test
	public void testNewDataWithCachedData() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(generate(BookCategory.class), generate(BookCategory.class));
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookCache.get("bookCategories")).thenReturn(new SimpleValueWrapper(bookCategories));
		for (BookCategory bookCategory : bookCategories) {
			when(bookCache.get("books" + bookCategory.getId())).thenReturn(new SimpleValueWrapper(books));
		}

		bookCategoryService.newData();

		for (BookCategory bookCategory : bookCategories) {
			verify(bookCategoryDAO).remove(bookCategory);
			verify(bookCache).get("books" + bookCategory.getId());
		}
		for (Book book : books) {
			verify(bookDAO, times(bookCategories.size())).remove(book);
		}
		verify(bookCache).get("bookCategories");
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#newData()} with not cached data. */
	@Test
	public void testNewDataWithNotCachedData() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(generate(BookCategory.class), generate(BookCategory.class));
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookCategoryDAO.getBookCategories()).thenReturn(bookCategories);
		when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		when(bookCache.get(anyString())).thenReturn(null);

		bookCategoryService.newData();

		verify(bookCategoryDAO).getBookCategories();
		for (BookCategory bookCategory : bookCategories) {
			verify(bookCategoryDAO).remove(bookCategory);
			verify(bookDAO).findBooksByBookCategory(bookCategory);
			verify(bookCache).get("books" + bookCategory.getId());
		}
		for (Book book : books) {
			verify(bookDAO, times(bookCategories.size())).remove(book);
		}
		verify(bookCache).get("bookCategories");
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#newData()} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.newData();
	}

	/** Test method for {@link BookCategoryService#newData()} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetBookDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookDAO(null);
		bookCategoryService.newData();
	}

	/** Test method for {@link BookCategoryService#newData()} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.newData();
	}

	/** Test method for {@link BookCategoryService#newData()} with exception in DAO tier. */
	@Test
	public void testNewDataWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookCategoryDAO).getBookCategories();
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.newData();
			fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
		verifyZeroInteractions(bookDAO);
	}

	/** Test method for {@link BookCategoryService#getBookCategories()} with cached book categories. */
	@Test
	public void testGetBookCategoriesWithCachedBookCategories() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(mock(BookCategory.class), mock(BookCategory.class));
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(bookCategories));

		DeepAsserts.assertEquals(bookCategories, bookCategoryService.getBookCategories());

		verify(bookCache).get("bookCategories");
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookCategoryDAO);
	}

	/** Test method for {@link BookCategoryService#getBookCategories()} with not cached book categories. */
	@Test
	public void testGetBookCategoriesWithNotCachedBookCategories() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(mock(BookCategory.class), mock(BookCategory.class));
		when(bookCategoryDAO.getBookCategories()).thenReturn(bookCategories);
		when(bookCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(bookCategories, bookCategoryService.getBookCategories());

		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verify(bookCache).put("bookCategories", bookCategories);
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#getBookCategories()} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoriesWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.getBookCategories();
	}

	/** Test method for {@link BookCategoryService#getBookCategories()} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoriesWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.getBookCategories();
	}

	/** Test method for {@link BookCategoryService#getBookCategories()} with exception in DAO tier. */
	@Test
	public void testGetBookCategoriesWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookCategoryDAO).getBookCategories();
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.getBookCategories();
			fail("Can't get book categories with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with cached existing book category. */
	@Test
	public void testGetBookCategoryWithCachedExistingBookCategory() {
		final BookCategory bookCategory = generate(BookCategory.class);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(bookCategory));

		DeepAsserts.assertEquals(bookCategory, bookCategoryService.getBookCategory(bookCategory.getId()));

		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookCategoryDAO);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with cached not existing book category. */
	@Test
	public void testGetBookCategoryWithCachedNotExistingBookCategory() {
		final int id = generate(Integer.class);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(bookCategoryService.getBookCategory(id));

		verify(bookCache).get("bookCategory" + id);
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookCategoryDAO);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with not cached existing book category. */
	@Test
	public void testGetBookCategoryWithNotCachedExistingBookCategory() {
		final BookCategory bookCategory = generate(BookCategory.class);
		when(bookCategoryDAO.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(bookCategory, bookCategoryService.getBookCategory(bookCategory.getId()));

		verify(bookCategoryDAO).getBookCategory(bookCategory.getId());
		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verify(bookCache).put("bookCategory" + bookCategory.getId(), bookCategory);
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with not cached not existing book category. */
	@Test
	public void testGetBookCategoryWithNotCachedNotExistingBookCategory() {
		final int id = generate(Integer.class);
		when(bookCategoryDAO.getBookCategory(anyInt())).thenReturn(null);
		when(bookCache.get(anyString())).thenReturn(null);

		assertNull(bookCategoryService.getBookCategory(id));

		verify(bookCategoryDAO).getBookCategory(id);
		verify(bookCache).get("bookCategory" + id);
		verify(bookCache).put("bookCategory" + id, null);
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoryWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.getBookCategory(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoryWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.getBookCategory(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with null argument. */
	@Test
	public void testGetBookCategoryWithNullArgument() {
		try {
			bookCategoryService.getBookCategory(null);
			fail("Can't get book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with exception in DAO tier. */
	@Test
	public void testGetBookCategoryWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookCategoryDAO).getBookCategory(anyInt());
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.getBookCategory(Integer.MAX_VALUE);
			fail("Can't get book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).getBookCategory(Integer.MAX_VALUE);
		verify(bookCache).get("bookCategory" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with cached book categories. */
	@Test
	public void testAddWithCachedBookCategories() {
		final BookCategory bookCategory = generate(BookCategory.class);
		final List<BookCategory> bookCategories = CollectionUtils.newList(mock(BookCategory.class), mock(BookCategory.class));
		final List<BookCategory> bookCategoriesList = new ArrayList<>(bookCategories);
		bookCategoriesList.add(bookCategory);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(bookCategories));

		bookCategoryService.add(bookCategory);

		verify(bookCategoryDAO).add(bookCategory);
		verify(bookCache).get("bookCategories");
		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verify(bookCache).put("bookCategories", bookCategoriesList);
		verify(bookCache).put("bookCategory" + bookCategory.getId(), bookCategory);
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with not cached book categories. */
	@Test
	public void testAddWithNotCachedBookCategories() {
		final BookCategory bookCategory = generate(BookCategory.class);
		when(bookCache.get(anyString())).thenReturn(null);

		bookCategoryService.add(bookCategory);

		verify(bookCategoryDAO).add(bookCategory);
		verify(bookCache).get("bookCategories");
		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.add(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.add(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			bookCategoryService.add(null);
			fail("Can't add book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with exception in DAO tier. */
	@Test
	public void testAddWithDAOTierException() {
		final BookCategory bookCategory = generate(BookCategory.class);
		doThrow(DataStorageException.class).when(bookCategoryDAO).add(any(BookCategory.class));

		try {
			bookCategoryService.add(bookCategory);
			fail("Can't add book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).add(bookCategory);
		verifyNoMoreInteractions(bookCategoryDAO);
		verifyZeroInteractions(bookCache);
	}

	/** Test method for {@link BookCategoryService#update(BookCategory)}. */
	@Test
	public void testUpdate() {
		final BookCategory bookCategory = generate(BookCategory.class);

		bookCategoryService.update(bookCategory);

		verify(bookCategoryDAO).update(bookCategory);
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#update(BookCategory)} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.update(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#update(BookCategory)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.update(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#update(BookCategory)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			bookCategoryService.update(null);
			fail("Can't update book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#update(BookCategory)} with exception in DAO tier. */
	@Test
	public void testUpdateWithDAOTierException() {
		final BookCategory bookCategory = generate(BookCategory.class);
		doThrow(DataStorageException.class).when(bookCategoryDAO).update(any(BookCategory.class));

		try {
			bookCategoryService.update(bookCategory);
			fail("Can't update book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).update(bookCategory);
		verifyNoMoreInteractions(bookCategoryDAO);
		verifyZeroInteractions(bookCache);
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with cached books. */
	@Test
	public void testRemoveWithCachedBooks() {
		final BookCategory bookCategory = generate(BookCategory.class);
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

		bookCategoryService.remove(bookCategory);

		verify(bookCategoryDAO).remove(bookCategory);
		for (Book book : books) {
			verify(bookDAO).remove(book);
		}
		verify(bookCache).get("books" + bookCategory.getId());
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with not cached books. */
	@Test
	public void testRemoveWithNotCachedBooks() {
		final BookCategory bookCategory = generate(BookCategory.class);
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		when(bookCache.get(anyString())).thenReturn(null);

		bookCategoryService.remove(bookCategory);

		verify(bookCategoryDAO).remove(bookCategory);
		verify(bookDAO).findBooksByBookCategory(bookCategory);
		for (Book book : books) {
			verify(bookDAO).remove(book);
		}
		verify(bookCache).get("books" + bookCategory.getId());
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.remove(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookDAO(null);
		bookCategoryService.remove(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.remove(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			bookCategoryService.remove(null);
			fail("Can't remove book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with exception in DAO tier. */
	@Test
	public void testRemoveWithDAOTierException() {
		final BookCategory bookCategory = generate(BookCategory.class);
		doThrow(DataStorageException.class).when(bookDAO).findBooksByBookCategory(any(BookCategory.class));
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.remove(bookCategory);
			fail("Can't remove book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookDAO).findBooksByBookCategory(bookCategory);
		verify(bookCache).get("books" + bookCategory.getId());
		verifyNoMoreInteractions(bookDAO, bookCache);
		verifyZeroInteractions(bookCategoryDAO);
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with cached books. */
	@Test
	public void testDuplicateWithCachedBooks() {
		final BookCategory bookCategory = generate(BookCategory.class);
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

		bookCategoryService.duplicate(bookCategory);

		verify(bookCategoryDAO).add(any(BookCategory.class));
		verify(bookCategoryDAO).update(any(BookCategory.class));
		verify(bookDAO, times(books.size())).add(any(Book.class));
		verify(bookDAO, times(books.size())).update(any(Book.class));
		verify(bookCache).get("books" + bookCategory.getId());
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with not cached books. */
	@Test
	public void testDuplicateWithNotCachedBooks() {
		final BookCategory bookCategory = generate(BookCategory.class);
		final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		when(bookCache.get(anyString())).thenReturn(null);

		bookCategoryService.duplicate(bookCategory);

		verify(bookCategoryDAO).add(any(BookCategory.class));
		verify(bookCategoryDAO).update(any(BookCategory.class));
		verify(bookDAO).findBooksByBookCategory(bookCategory);
		verify(bookDAO, times(books.size())).add(any(Book.class));
		verify(bookDAO, times(books.size())).update(any(Book.class));
		verify(bookCache).get("books" + bookCategory.getId());
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.duplicate(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookDAO(null);
		bookCategoryService.duplicate(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.duplicate(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		try {
			bookCategoryService.duplicate(null);
			fail("Can't duplicate book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with exception in DAO tier. */
	@Test
	public void testDuplicateWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookCategoryDAO).add(any(BookCategory.class));

		try {
			bookCategoryService.duplicate(generate(BookCategory.class));
			fail("Can't duplicate book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).add(any(BookCategory.class));
		verifyNoMoreInteractions(bookCategoryDAO);
		verifyZeroInteractions(bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#moveUp(BookCategory)} with cached book categories. */
	@Test
	public void testMoveUpWithCachedBookCategories() {
		final BookCategory bookCategory1 = generate(BookCategory.class);
		final int position1 = bookCategory1.getPosition();
		final BookCategory bookCategory2 = generate(BookCategory.class);
		final int position2 = bookCategory2.getPosition();
		final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory1, bookCategory2);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(bookCategories));

		bookCategoryService.moveUp(bookCategory2);
		DeepAsserts.assertEquals(position2, bookCategory1.getPosition());
		DeepAsserts.assertEquals(position1, bookCategory2.getPosition());

		verify(bookCategoryDAO).update(bookCategory1);
		verify(bookCategoryDAO).update(bookCategory2);
		verify(bookCache).get("bookCategories");
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#moveUp(BookCategory)} with not cached book categories. */
	@Test
	public void testMoveUpWithNotCachedBookCategories() {
		final BookCategory bookCategory1 = generate(BookCategory.class);
		final int position1 = bookCategory1.getPosition();
		final BookCategory bookCategory2 = generate(BookCategory.class);
		final int position2 = bookCategory2.getPosition();
		final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory1, bookCategory2);
		when(bookCategoryDAO.getBookCategories()).thenReturn(bookCategories);
		when(bookCache.get(anyString())).thenReturn(null);

		bookCategoryService.moveUp(bookCategory2);
		DeepAsserts.assertEquals(position2, bookCategory1.getPosition());
		DeepAsserts.assertEquals(position1, bookCategory2.getPosition());

		verify(bookCategoryDAO).update(bookCategory1);
		verify(bookCategoryDAO).update(bookCategory2);
		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#moveUp(BookCategory)} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.moveUp(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#moveUp(BookCategory)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.moveUp(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#moveUp(BookCategory)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		try {
			bookCategoryService.moveUp(null);
			fail("Can't move up book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#moveUp(BookCategory)} with exception in DAO tier. */
	@Test
	public void testMoveUpWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookCategoryDAO).getBookCategories();
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.moveUp(generate(BookCategory.class));
			fail("Can't move up book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#moveDown(BookCategory)} with cached book categories. */
	@Test
	public void testMoveDownWithCachedBookCategories() {
		final BookCategory bookCategory1 = generate(BookCategory.class);
		final int position1 = bookCategory1.getPosition();
		final BookCategory bookCategory2 = generate(BookCategory.class);
		final int position2 = bookCategory2.getPosition();
		final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory1, bookCategory2);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(bookCategories));

		bookCategoryService.moveDown(bookCategory1);
		DeepAsserts.assertEquals(position2, bookCategory1.getPosition());
		DeepAsserts.assertEquals(position1, bookCategory2.getPosition());

		verify(bookCategoryDAO).update(bookCategory1);
		verify(bookCategoryDAO).update(bookCategory2);
		verify(bookCache).get("bookCategories");
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#moveDown(BookCategory)} with not cached book categories. */
	@Test
	public void testMoveDownWithNotCachedBookCategories() {
		final BookCategory bookCategory1 = generate(BookCategory.class);
		final int position1 = bookCategory1.getPosition();
		final BookCategory bookCategory2 = generate(BookCategory.class);
		final int position2 = bookCategory2.getPosition();
		final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory1, bookCategory2);
		when(bookCategoryDAO.getBookCategories()).thenReturn(bookCategories);
		when(bookCache.get(anyString())).thenReturn(null);

		bookCategoryService.moveDown(bookCategory1);
		DeepAsserts.assertEquals(position2, bookCategory1.getPosition());
		DeepAsserts.assertEquals(position1, bookCategory2.getPosition());

		verify(bookCategoryDAO).update(bookCategory1);
		verify(bookCategoryDAO).update(bookCategory2);
		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#moveDown(BookCategory)} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.moveDown(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#moveDown(BookCategory)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.moveDown(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#moveDown(BookCategory)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		try {
			bookCategoryService.moveDown(null);
			fail("Can't move down book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#moveDown(BookCategory)} with exception in DAO tier. */
	@Test
	public void testMoveDownWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookCategoryDAO).getBookCategories();
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.moveDown(generate(BookCategory.class));
			fail("Can't move down book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with cached existing book category. */
	@Test
	public void testExistsWithCachedExistingBookCategory() {
		final BookCategory bookCategory = generate(BookCategory.class);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(bookCategory));

		assertTrue(bookCategoryService.exists(bookCategory));

		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookCategoryDAO);
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with cached not existing book category. */
	@Test
	public void testExistsWithCachedNotExistingBookCategory() {
		final BookCategory bookCategory = generate(BookCategory.class);
		when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(bookCategoryService.exists(bookCategory));

		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookCategoryDAO);
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with not cached existing book category. */
	@Test
	public void testExistsWithNotCachedExistingBookCategory() {
		final BookCategory bookCategory = generate(BookCategory.class);
		when(bookCategoryDAO.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookCache.get(anyString())).thenReturn(null);

		assertTrue(bookCategoryService.exists(bookCategory));

		verify(bookCategoryDAO).getBookCategory(bookCategory.getId());
		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verify(bookCache).put("bookCategory" + bookCategory.getId(), bookCategory);
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with not cached not existing book category. */
	@Test
	public void testExistsWithNotCachedNotExistingBookCategory() {
		final BookCategory bookCategory = generate(BookCategory.class);
		when(bookCategoryDAO.getBookCategory(anyInt())).thenReturn(null);
		when(bookCache.get(anyString())).thenReturn(null);

		assertFalse(bookCategoryService.exists(bookCategory));

		verify(bookCategoryDAO).getBookCategory(bookCategory.getId());
		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verify(bookCache).put("bookCategory" + bookCategory.getId(), null);
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.exists(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.exists(mock(BookCategory.class));
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		try {
			bookCategoryService.exists(null);
			fail("Can't exists book category with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with exception in DAO tier. */
	@Test
	public void testExistsWithDAOTierException() {
		final BookCategory bookCategory = generate(BookCategory.class);
		doThrow(DataStorageException.class).when(bookCategoryDAO).getBookCategory(anyInt());
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.exists(bookCategory);
			fail("Can't exists book category with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).getBookCategory(bookCategory.getId());
		verify(bookCache).get("bookCategory" + bookCategory.getId());
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#updatePositions()} with cached data. */
	@Test
	public void testUpdatePositionsWithCachedData() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(generate(BookCategory.class), generate(BookCategory.class));
		final List<Book> books = CollectionUtils.newList(generate(Book.class), generate(Book.class));
		when(bookCache.get("bookCategories")).thenReturn(new SimpleValueWrapper(bookCategories));
		for (BookCategory bookCategory : bookCategories) {
			when(bookCache.get("books" + bookCategory.getId())).thenReturn(new SimpleValueWrapper(books));
		}

		bookCategoryService.updatePositions();

		for (int i = 0; i < bookCategories.size(); i++) {
			final BookCategory bookCategory = bookCategories.get(i);
			DeepAsserts.assertEquals(i, bookCategory.getPosition());
			verify(bookCategoryDAO).update(bookCategory);
			verify(bookCache).get("books" + bookCategory.getId());
		}
		for (int i = 0; i < books.size(); i++) {
			final Book book = books.get(i);
			DeepAsserts.assertEquals(i, book.getPosition());
			verify(bookDAO, times(bookCategories.size())).update(book);
		}
		verify(bookCache).get("bookCategories");
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#updatePositions()} with not cached data. */
	@Test
	public void testUpdatePositionsWithNotCachedData() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(generate(BookCategory.class), generate(BookCategory.class));
		final List<Book> books = CollectionUtils.newList(generate(Book.class), generate(Book.class));
		when(bookCategoryDAO.getBookCategories()).thenReturn(bookCategories);
		when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		when(bookCache.get(anyString())).thenReturn(null);

		bookCategoryService.updatePositions();

		verify(bookCategoryDAO).getBookCategories();
		for (int i = 0; i < bookCategories.size(); i++) {
			final BookCategory bookCategory = bookCategories.get(i);
			DeepAsserts.assertEquals(i, bookCategory.getPosition());
			verify(bookCategoryDAO).update(bookCategory);
			verify(bookDAO).findBooksByBookCategory(bookCategory);
			verify(bookCache).get("books" + bookCategory.getId());
		}
		for (int i = 0; i < books.size(); i++) {
			final Book book = books.get(i);
			DeepAsserts.assertEquals(i, book.getPosition());
			verify(bookDAO, times(bookCategories.size())).update(book);
		}
		verify(bookCache).get("bookCategories");
		verify(bookCache).clear();
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#updatePositions()} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.updatePositions();
	}

	/** Test method for {@link BookCategoryService#updatePositions()} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetBookDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookDAO(null);
		bookCategoryService.updatePositions();
	}

	/** Test method for {@link BookCategoryService#updatePositions()} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.updatePositions();
	}

	/** Test method for {@link BookCategoryService#updatePositions()} with exception in DAO tier. */
	@Test
	public void testUpdatePositionsWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookCategoryDAO).getBookCategories();
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.updatePositions();
			fail("Can't update positions with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
		verifyZeroInteractions(bookDAO);
	}

	/** Test method for {@link BookCategoryService#getBooksCount()} with cached data. */
	@Test
	public void testGetBooksCountWithCachedData() {
		final BookCategory bookCategory1 = generate(BookCategory.class);
		final BookCategory bookCategory2 = generate(BookCategory.class);
		final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory1, bookCategory2);
		final List<Book> books1 = CollectionUtils.newList(mock(Book.class));
		final List<Book> books2 = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookCache.get("bookCategories")).thenReturn(new SimpleValueWrapper(bookCategories));
		when(bookCache.get("books" + bookCategory1.getId())).thenReturn(new SimpleValueWrapper(books1));
		when(bookCache.get("books" + bookCategory2.getId())).thenReturn(new SimpleValueWrapper(books2));

		DeepAsserts.assertEquals(books1.size() + books2.size(), bookCategoryService.getBooksCount());

		verify(bookCache).get("bookCategories");
		verify(bookCache).get("books" + bookCategory1.getId());
		verify(bookCache).get("books" + bookCategory2.getId());
		verifyNoMoreInteractions(bookCache);
		verifyZeroInteractions(bookCategoryDAO, bookDAO);
	}

	/** Test method for {@link BookCategoryService#getBooksCount()} with not cached data. */
	@Test
	public void testGetBooksCountWithNotCachedBookCategories() {
		final BookCategory bookCategory1 = generate(BookCategory.class);
		final BookCategory bookCategory2 = generate(BookCategory.class);
		final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory1, bookCategory2);
		final List<Book> books1 = CollectionUtils.newList(mock(Book.class));
		final List<Book> books2 = CollectionUtils.newList(mock(Book.class), mock(Book.class));
		when(bookCategoryDAO.getBookCategories()).thenReturn(bookCategories);
		when(bookDAO.findBooksByBookCategory(bookCategory1)).thenReturn(books1);
		when(bookDAO.findBooksByBookCategory(bookCategory2)).thenReturn(books2);
		when(bookCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(books1.size() + books2.size(), bookCategoryService.getBooksCount());

		verify(bookCategoryDAO).getBookCategories();
		verify(bookDAO).findBooksByBookCategory(bookCategory1);
		verify(bookDAO).findBooksByBookCategory(bookCategory2);
		verify(bookCache).get("bookCategories");
		verify(bookCache).put("bookCategories", bookCategories);
		verify(bookCache).get("books" + bookCategory1.getId());
		verify(bookCache).put("books" + bookCategory1.getId(), books1);
		verify(bookCache).get("books" + bookCategory2.getId());
		verify(bookCache).put("books" + bookCategory2.getId(), books2);
		verifyNoMoreInteractions(bookCategoryDAO, bookDAO, bookCache);
	}

	/** Test method for {@link BookCategoryService#getBooksCount()} with not set DAO for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testGetBooksCountWithNotSetBookCategoryDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCategoryDAO(null);
		bookCategoryService.getBooksCount();
	}

	/** Test method for {@link BookCategoryService#getBooksCount()} with not set DAO for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBooksCountWithNotSetBookDAO() {
		((BookCategoryServiceImpl) bookCategoryService).setBookDAO(null);
		bookCategoryService.getBooksCount();
	}

	/** Test method for {@link BookCategoryService#getBooksCount()} with not set cache for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBooksCountWithNotSetBookCache() {
		((BookCategoryServiceImpl) bookCategoryService).setBookCache(null);
		bookCategoryService.getBooksCount();
	}

	/** Test method for {@link BookCategoryService#getBooksCount()} with exception in DAO tier. */
	@Test
	public void testGetBooksCountWithDAOTierException() {
		doThrow(DataStorageException.class).when(bookCategoryDAO).getBookCategories();
		when(bookCache.get(anyString())).thenReturn(null);

		try {
			bookCategoryService.getBooksCount();
			fail("Can't get books count with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(bookCategoryDAO).getBookCategories();
		verify(bookCache).get("bookCategories");
		verifyNoMoreInteractions(bookCategoryDAO, bookCache);
		verifyZeroInteractions(bookDAO);
	}

}
