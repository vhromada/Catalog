package cz.vhromada.catalog.service.impl;

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
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.BookDAO;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.BookService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class BookServiceImplTest extends ObjectGeneratorTest {

    /** Cache key for list of books */
    private static final String BOOKS_CACHE_KEY = "books";

    /** Cache key for book */
    private static final String BOOK_CACHE_KEY = "book";

    /** Instance of {@link BookDAO} */
    @Mock
    private BookDAO bookDAO;

    /** Instance of {@link Cache} */
    @Mock
    private Cache bookCache;

    /** Instance of {@link BookService} */
    private BookService bookService;

    /** Initializes service for books. */
    @Before
    public void setUp() {
        bookService = new BookServiceImpl(bookDAO, bookCache);
    }

    /** Test method for {@link BookServiceImpl#BookServiceImpl(BookDAO, Cache)} with null DAO for books. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBookDAO() {
        new BookServiceImpl(null, bookCache);
    }

    /** Test method for {@link BookServiceImpl#BookServiceImpl(BookDAO, Cache))} with null cache for books. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBookCache() {
        new BookServiceImpl(bookDAO, null);
    }

    /** Test method for {@link BookService#getBook(Integer)} with cached existing book. */
    @Test
    public void testGetBookWithCachedExistingBook() {
        final Book book = generate(Book.class);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(book));

        DeepAsserts.assertEquals(book, bookService.getBook(book.getId()));

        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verifyNoMoreInteractions(bookCache);
        verifyZeroInteractions(bookDAO);
    }

    /** Test method for {@link BookService#getBook(Integer)} with cached not existing book. */
    @Test
    public void testGetBookWithCachedNotExistingBook() {
        final Book book = generate(Book.class);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertNull(bookService.getBook(book.getId()));

        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verifyNoMoreInteractions(bookCache);
        verifyZeroInteractions(bookDAO);
    }

    /** Test method for {@link BookService#getBook(Integer)} with not cached existing book. */
    @Test
    public void testGetBookWithNotCachedExistingBook() {
        final Book book = generate(Book.class);
        when(bookDAO.getBook(anyInt())).thenReturn(book);
        when(bookCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(book, bookService.getBook(book.getId()));

        verify(bookDAO).getBook(book.getId());
        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verify(bookCache).put(BOOK_CACHE_KEY + book.getId(), book);
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#getBook(Integer)} with not cached not existing book. */
    @Test
    public void testGetBookWithNotCachedNotExistingBook() {
        final Book book = generate(Book.class);
        when(bookDAO.getBook(anyInt())).thenReturn(null);
        when(bookCache.get(anyString())).thenReturn(null);

        assertNull(bookService.getBook(book.getId()));

        verify(bookDAO).getBook(book.getId());
        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verify(bookCache).put(BOOK_CACHE_KEY + book.getId(), null);
        verifyNoMoreInteractions(bookDAO, bookCache);
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
        verify(bookCache).get(BOOK_CACHE_KEY + Integer.MAX_VALUE);
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#add(Book)} with cached books. */
    @Test
    public void testAddWithCachedBooks() {
        final Book book = generate(Book.class);
        final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
        final List<Book> booksList = new ArrayList<>(books);
        booksList.add(book);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

        bookService.add(book);

        verify(bookDAO).add(book);
        verify(bookCache).get(BOOKS_CACHE_KEY + book.getBookCategory().getId());
        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verify(bookCache).put(BOOKS_CACHE_KEY + book.getBookCategory().getId(), booksList);
        verify(bookCache).put(BOOK_CACHE_KEY + book.getId(), book);
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#add(Book)} with not cached books. */
    @Test
    public void testAddWithNotCachedBooks() {
        final Book book = generate(Book.class);
        when(bookCache.get(anyString())).thenReturn(null);

        bookService.add(book);

        verify(bookDAO).add(book);
        verify(bookCache).get(BOOKS_CACHE_KEY + book.getBookCategory().getId());
        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verifyNoMoreInteractions(bookDAO, bookCache);
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
        final Book book = generate(Book.class);
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
        final Book book = generate(Book.class);

        bookService.update(book);

        verify(bookDAO).update(book);
        verify(bookCache).clear();
        verifyNoMoreInteractions(bookDAO, bookCache);
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
        final Book book = generate(Book.class);
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
        final Book book = generate(Book.class);
        final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
        final List<Book> booksList = new ArrayList<>(books);
        booksList.add(book);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(booksList));

        bookService.remove(book);

        verify(bookDAO).remove(book);
        verify(bookCache).get(BOOKS_CACHE_KEY + book.getBookCategory().getId());
        verify(bookCache).put(BOOKS_CACHE_KEY + book.getBookCategory().getId(), books);
        verify(bookCache).evict(BOOK_CACHE_KEY + book.getId());
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#remove(Book)} with not cached books. */
    @Test
    public void testRemoveWithNotCachedBooks() {
        final Book book = generate(Book.class);
        when(bookCache.get(anyString())).thenReturn(null);

        bookService.remove(book);

        verify(bookDAO).remove(book);
        verify(bookCache).get(BOOKS_CACHE_KEY + book.getBookCategory().getId());
        verify(bookCache).evict(BOOK_CACHE_KEY + book.getId());
        verifyNoMoreInteractions(bookDAO, bookCache);
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
        final Book book = generate(Book.class);
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
        final Book book = generate(Book.class);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(CollectionUtils.newList(mock(Book.class), mock(Book.class))));

        bookService.duplicate(book);

        verify(bookDAO).add(any(Book.class));
        verify(bookDAO).update(any(Book.class));
        verify(bookCache).get(BOOKS_CACHE_KEY + book.getBookCategory().getId());
        verify(bookCache).get(BOOK_CACHE_KEY + null);
        verify(bookCache).put(eq(BOOKS_CACHE_KEY + book.getBookCategory().getId()), anyListOf(Book.class));
        verify(bookCache).put(eq(BOOK_CACHE_KEY + null), any(Book.class));
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#duplicate(Book)} with not cached books. */
    @Test
    public void testDuplicateWithNotCachedBooks() {
        final Book book = generate(Book.class);
        when(bookCache.get(anyString())).thenReturn(null);

        bookService.duplicate(book);

        verify(bookDAO).add(any(Book.class));
        verify(bookDAO).update(any(Book.class));
        verify(bookCache).get(BOOKS_CACHE_KEY + book.getBookCategory().getId());
        verify(bookCache).get(BOOK_CACHE_KEY + null);
        verifyNoMoreInteractions(bookDAO, bookCache);
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
            bookService.duplicate(generate(Book.class));
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
        final BookCategory bookCategory = generate(BookCategory.class);
        final Book book1 = generate(Book.class);
        book1.setBookCategory(bookCategory);
        final int position1 = book1.getPosition();
        final Book book2 = generate(Book.class);
        book2.setBookCategory(bookCategory);
        final int position2 = book2.getPosition();
        final List<Book> books = CollectionUtils.newList(book1, book2);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

        bookService.moveUp(book2);
        DeepAsserts.assertEquals(position2, book1.getPosition());
        DeepAsserts.assertEquals(position1, book2.getPosition());

        verify(bookDAO).update(book1);
        verify(bookDAO).update(book2);
        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verify(bookCache).clear();
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#moveUp(Book)} with not cached books. */
    @Test
    public void testMoveUpWithNotCachedBooks() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final Book book1 = generate(Book.class);
        book1.setBookCategory(bookCategory);
        final int position1 = book1.getPosition();
        final Book book2 = generate(Book.class);
        book2.setBookCategory(bookCategory);
        final int position2 = book2.getPosition();
        final List<Book> books = CollectionUtils.newList(book1, book2);
        when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
        when(bookCache.get(anyString())).thenReturn(null);

        bookService.moveUp(book2);
        DeepAsserts.assertEquals(position2, book1.getPosition());
        DeepAsserts.assertEquals(position1, book2.getPosition());

        verify(bookDAO).update(book1);
        verify(bookDAO).update(book2);
        verify(bookDAO).findBooksByBookCategory(bookCategory);
        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verify(bookCache).clear();
        verifyNoMoreInteractions(bookDAO, bookCache);
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
        final BookCategory bookCategory = generate(BookCategory.class);
        final Book book = generate(Book.class);
        book.setBookCategory(bookCategory);
        doThrow(DataStorageException.class).when(bookDAO).findBooksByBookCategory(any(BookCategory.class));
        when(bookCache.get(anyString())).thenReturn(null);

        try {
            bookService.moveUp(book);
            fail("Can't move up book with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(bookDAO).findBooksByBookCategory(bookCategory);
        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#moveDown(Book)} with cached books. */
    @Test
    public void testMoveDownWithCachedBooks() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final Book book1 = generate(Book.class);
        book1.setBookCategory(bookCategory);
        final int position1 = book1.getPosition();
        final Book book2 = generate(Book.class);
        book2.setBookCategory(bookCategory);
        final int position2 = book2.getPosition();
        final List<Book> books = CollectionUtils.newList(book1, book2);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

        bookService.moveDown(book1);
        DeepAsserts.assertEquals(position2, book1.getPosition());
        DeepAsserts.assertEquals(position1, book2.getPosition());

        verify(bookDAO).update(book1);
        verify(bookDAO).update(book2);
        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verify(bookCache).clear();
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#moveDown(Book)} with not cached books. */
    @Test
    public void testMoveDownWithNotCachedBooks() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final Book book1 = generate(Book.class);
        book1.setBookCategory(bookCategory);
        final int position1 = book1.getPosition();
        final Book book2 = generate(Book.class);
        book2.setBookCategory(bookCategory);
        final int position2 = book2.getPosition();
        final List<Book> books = CollectionUtils.newList(book1, book2);
        when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
        when(bookCache.get(anyString())).thenReturn(null);

        bookService.moveDown(book1);
        DeepAsserts.assertEquals(position2, book1.getPosition());
        DeepAsserts.assertEquals(position1, book2.getPosition());

        verify(bookDAO).update(book1);
        verify(bookDAO).update(book2);
        verify(bookDAO).findBooksByBookCategory(bookCategory);
        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verify(bookCache).clear();
        verifyNoMoreInteractions(bookDAO, bookCache);
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
        final BookCategory bookCategory = generate(BookCategory.class);
        final Book book = generate(Book.class);
        book.setBookCategory(bookCategory);
        doThrow(DataStorageException.class).when(bookDAO).findBooksByBookCategory(any(BookCategory.class));
        when(bookCache.get(anyString())).thenReturn(null);

        try {
            bookService.moveDown(book);
            fail("Can't move down book with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(bookDAO).findBooksByBookCategory(bookCategory);
        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#exists(Book)} with cached existing book. */
    @Test
    public void testExistsWithCachedExistingBook() {
        final Book book = generate(Book.class);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(book));

        assertTrue(bookService.exists(book));

        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verifyNoMoreInteractions(bookCache);
        verifyZeroInteractions(bookDAO);
    }

    /** Test method for {@link BookService#exists(Book)} with cached not existing book. */
    @Test
    public void testExistsWithCachedNotExistingBook() {
        final Book book = generate(Book.class);
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertFalse(bookService.exists(book));

        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verifyNoMoreInteractions(bookCache);
        verifyZeroInteractions(bookDAO);
    }

    /** Test method for {@link BookService#exists(Book)} with not cached existing book. */
    @Test
    public void testExistsWithNotCachedExistingBook() {
        final Book book = generate(Book.class);
        when(bookDAO.getBook(anyInt())).thenReturn(book);
        when(bookCache.get(anyString())).thenReturn(null);

        assertTrue(bookService.exists(book));

        verify(bookDAO).getBook(book.getId());
        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verify(bookCache).put(BOOK_CACHE_KEY + book.getId(), book);
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#exists(Book)} with not cached not existing book. */
    @Test
    public void testExistsWithNotCachedNotExistingBook() {
        final Book book = generate(Book.class);
        when(bookDAO.getBook(anyInt())).thenReturn(null);
        when(bookCache.get(anyString())).thenReturn(null);

        assertFalse(bookService.exists(book));

        verify(bookDAO).getBook(book.getId());
        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verify(bookCache).put(BOOK_CACHE_KEY + book.getId(), null);
        verifyNoMoreInteractions(bookDAO, bookCache);
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
        final Book book = generate(Book.class);
        doThrow(DataStorageException.class).when(bookDAO).getBook(anyInt());
        when(bookCache.get(anyString())).thenReturn(null);

        try {
            bookService.exists(book);
            fail("Can't exists book with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(bookDAO).getBook(book.getId());
        verify(bookCache).get(BOOK_CACHE_KEY + book.getId());
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

    /** Test method for {@link BookService#findBooksByBookCategory(BookCategory)} with cached books. */
    @Test
    public void testFindBooksByBookCategoryWithCachedBooks() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
        when(bookCache.get(anyString())).thenReturn(new SimpleValueWrapper(books));

        DeepAsserts.assertEquals(books, bookService.findBooksByBookCategory(bookCategory));

        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verifyNoMoreInteractions(bookCache);
        verifyZeroInteractions(bookDAO);
    }

    /** Test method for {@link BookService#findBooksByBookCategory(BookCategory)} with not cached books. */
    @Test
    public void testFindBooksByBookCategoryWithNotCachedBooks() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final List<Book> books = CollectionUtils.newList(mock(Book.class), mock(Book.class));
        when(bookDAO.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
        when(bookCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(books, bookService.findBooksByBookCategory(bookCategory));

        verify(bookDAO).findBooksByBookCategory(bookCategory);
        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verify(bookCache).put(BOOKS_CACHE_KEY + bookCategory.getId(), books);
        verifyNoMoreInteractions(bookDAO, bookCache);
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
        final BookCategory bookCategory = generate(BookCategory.class);
        doThrow(DataStorageException.class).when(bookDAO).findBooksByBookCategory(any(BookCategory.class));
        when(bookCache.get(anyString())).thenReturn(null);

        try {
            bookService.findBooksByBookCategory(bookCategory);
            fail("Can't find books by book category with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(bookDAO).findBooksByBookCategory(bookCategory);
        verify(bookCache).get(BOOKS_CACHE_KEY + bookCategory.getId());
        verifyNoMoreInteractions(bookDAO, bookCache);
    }

}
