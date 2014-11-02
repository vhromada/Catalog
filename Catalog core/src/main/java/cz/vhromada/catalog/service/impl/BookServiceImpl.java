package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.BookDAO;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.BookService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for books.
 *
 * @author Vladimir Hromada
 */
@Component("bookService")
public class BookServiceImpl extends AbstractBookService implements BookService {

	/** DAO for books field */
	private static final String BOOK_DAO_FIELD = "DAO for books";

	/** Book category argument */
	private static final String BOOK_CATEGORY_ARGUMENT = "Book category";

	/** Book argument */
	private static final String BOOK_ARGUMENT = "Book";

	/** ID argument */
	private static final String ID_ARGUMENT = "ID";

	/** Message for {@link ServiceOperationException} */
	private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

	/** DAO for books */
	@Autowired
	private BookDAO bookDAO;

	/**
	 * Returns DAO for books.
	 *
	 * @return DAO for books
	 */
	public BookDAO getBookDAO() {
		return bookDAO;
	}

	/**
	 * Sets a new value to DAO for books.
	 *
	 * @param bookDAO new value
	 */
	public void setBookDAO(final BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public Book getBook(final Integer id) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(id, ID_ARGUMENT);

		try {
			return getCachedBook(id);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void add(final Book book) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

		try {
			bookDAO.add(book);
			addBookToCache(book);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void update(final Book book) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

		try {
			bookDAO.update(book);
			clearCache();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final Book book) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

		try {
			bookDAO.remove(book);
			removeBookFromCache(book);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final Book book) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

		try {
			final Book newBook = new Book();
			newBook.setAuthor(book.getAuthor());
			newBook.setTitle(book.getTitle());
			newBook.setLanguages(new ArrayList<>(book.getLanguages()));
			newBook.setCategory(book.getCategory());
			newBook.setNote(book.getNote());
			newBook.setBookCategory(book.getBookCategory());
			bookDAO.add(newBook);
			newBook.setPosition(book.getPosition());
			bookDAO.update(newBook);
			addBookToCache(newBook);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final Book book) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

		try {
			final List<Book> books = getCachedBooks(book.getBookCategory(), false);
			final Book otherBook = books.get(books.indexOf(book) - 1);
			switchPosition(book, otherBook);
			bookDAO.update(book);
			bookDAO.update(otherBook);
			clearCache();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final Book book) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

		try {
			final List<Book> books = getCachedBooks(book.getBookCategory(), false);
			final Book otherBook = books.get(books.indexOf(book) + 1);
			switchPosition(book, otherBook);
			bookDAO.update(book);
			bookDAO.update(otherBook);
			clearCache();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}


	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public boolean exists(final Book book) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

		try {
			return getCachedBook(book.getId()) != null;
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for books isn't set
	 *                                   or cache for books isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public List<Book> findBooksByBookCategory(final BookCategory bookCategory) {
		Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
		validateBookCacheNotNull();
		Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

		try {
			return getCachedBooks(bookCategory, true);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	@Override
	protected List<BookCategory> getDAOBookCategories() {
		return null;
	}

	@Override
	protected List<Book> getDAOBooks(final BookCategory bookCategory) {
		return bookDAO.findBooksByBookCategory(bookCategory);
	}

	@Override
	protected BookCategory getDAOBookCategory(final Integer id) {
		return null;
	}

	@Override
	protected Book getDAOBook(final Integer id) {
		return bookDAO.getBook(id);
	}

	/**
	 * Switch position of books.
	 *
	 * @param book1 1st book
	 * @param book2 2nd book
	 */
	private void switchPosition(final Book book1, final Book book2) {
		final int position = book1.getPosition();
		book1.setPosition(book2.getPosition());
		book2.setPosition(position);
	}

}

