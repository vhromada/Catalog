package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.BookCategoryDAO;
import cz.vhromada.catalog.dao.BookDAO;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.BookCategoryService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for book categories.
 *
 * @author Vladimir Hromada
 */
@Component("bookCategoryService")
public class BookCategoryServiceImpl extends AbstractBookService implements BookCategoryService {

    /** DAO for book categories field */
    private static final String BOOK_CATEGORY_DAO_FIELD = "DAO for book categories";

    /** DAO for books field */
    private static final String BOOK_DAO_FIELD = "DAO for books";

    /** Book category argument */
    private static final String BOOK_CATEGORY_ARGUMENT = "Book category";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link ServiceOperationException} */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /** DAO for book categories */
    @Autowired
    private BookCategoryDAO bookCategoryDAO;

    /** DAO for books */
    @Autowired
    private BookDAO bookDAO;

    /**
     * Returns DAO book categories.
     *
     * @return DAO book categories
     */
    public BookCategoryDAO getBookCategoryDAO() {
        return bookCategoryDAO;
    }

    /**
     * Sets a new value to DAO book categories.
     *
     * @param bookCategoryDAO new value
     */
    public void setBookCategoryDAO(final BookCategoryDAO bookCategoryDAO) {
        this.bookCategoryDAO = bookCategoryDAO;
    }

    /**
     * Returns DAO books.
     *
     * @return DAO books
     */
    public BookDAO getBookDAO() {
        return bookDAO;
    }

    /**
     * Sets a new value to DAO books.
     *
     * @param bookDAO new value
     */
    public void setBookDAO(final BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or DAO for books isn't set
     *                                   or cache for books isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
        validateBookCacheNotNull();

        try {
            for (final BookCategory bookCategory : getCachedBookCategories(false)) {
                removeBookCategory(bookCategory);
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or cache for books isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<BookCategory> getBookCategories() {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        validateBookCacheNotNull();

        try {
            return getCachedBookCategories(true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or cache for books isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public BookCategory getBookCategory(final Integer id) {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        validateBookCacheNotNull();
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedBookCategory(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or cache for books isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        validateBookCacheNotNull();
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            bookCategoryDAO.add(bookCategory);
            addBookCategoryToCache(bookCategory);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or cache for books isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        validateBookCacheNotNull();
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            bookCategoryDAO.update(bookCategory);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or DAO for books isn't set
     *                                   or cache for books isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
        validateBookCacheNotNull();
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            removeBookCategory(bookCategory);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or DAO for books isn't set
     *                                   or cache for books isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
        validateBookCacheNotNull();
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            final BookCategory newBookCategory = new BookCategory();
            newBookCategory.setName(bookCategory.getName());
            newBookCategory.setNote(bookCategory.getNote());
            bookCategoryDAO.add(newBookCategory);
            newBookCategory.setPosition(bookCategory.getPosition());
            bookCategoryDAO.update(newBookCategory);

            for (final Book book : getCachedBooks(bookCategory, false)) {
                final Book newBook = new Book();
                newBook.setAuthor(book.getAuthor());
                newBook.setTitle(book.getTitle());
                newBook.setLanguages(new ArrayList<>(book.getLanguages()));
                newBook.setCategory(book.getCategory());
                newBook.setNote(book.getNote());
                newBook.setBookCategory(newBookCategory);
                bookDAO.add(newBook);
                newBook.setPosition(book.getPosition());
                bookDAO.update(newBook);
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or cache for books isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        validateBookCacheNotNull();
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            final List<BookCategory> bookCategories = getCachedBookCategories(false);
            final BookCategory otherBookCategory = bookCategories.get(bookCategories.indexOf(bookCategory) - 1);
            switchPosition(bookCategory, otherBookCategory);
            bookCategoryDAO.update(bookCategory);
            bookCategoryDAO.update(otherBookCategory);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or cache for books isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        validateBookCacheNotNull();
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            final List<BookCategory> bookCategories = getCachedBookCategories(false);
            final BookCategory otherBookCategory = bookCategories.get(bookCategories.indexOf(bookCategory) + 1);
            switchPosition(bookCategory, otherBookCategory);
            bookCategoryDAO.update(bookCategory);
            bookCategoryDAO.update(otherBookCategory);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or cache for books isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        validateBookCacheNotNull();
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            return getCachedBookCategory(bookCategory.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or DAO for books isn't set
     *                                   or cache for books isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
        validateBookCacheNotNull();

        try {
            final List<BookCategory> bookCategories = getCachedBookCategories(false);
            for (int i = 0; i < bookCategories.size(); i++) {
                final BookCategory bookCategory = bookCategories.get(i);
                bookCategory.setPosition(i);
                bookCategoryDAO.update(bookCategory);
                final List<Book> books = getCachedBooks(bookCategory, false);
                for (int j = 0; j < books.size(); j++) {
                    final Book book = books.get(j);
                    book.setPosition(j);
                    bookDAO.update(book);
                }
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for book categories isn't set
     *                                   or DAO for books isn't set
     *                                   or cache for books isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getBooksCount() {
        Validators.validateFieldNotNull(bookCategoryDAO, BOOK_CATEGORY_DAO_FIELD);
        Validators.validateFieldNotNull(bookDAO, BOOK_DAO_FIELD);
        validateBookCacheNotNull();

        try {
            int sum = 0;
            for (final BookCategory bookCategory : getCachedBookCategories(true)) {
                sum += getCachedBooks(bookCategory, true).size();
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<BookCategory> getDAOBookCategories() {
        return bookCategoryDAO.getBookCategories();
    }

    @Override
    protected List<Book> getDAOBooks(final BookCategory bookCategory) {
        return bookDAO.findBooksByBookCategory(bookCategory);
    }

    @Override
    protected BookCategory getDAOBookCategory(final Integer id) {
        return bookCategoryDAO.getBookCategory(id);
    }

    @Override
    protected Book getDAOBook(final Integer id) {
        return bookDAO.getBook(id);
    }

    /**
     * Removes book category.
     *
     * @param bookCategory book category
     */
    private void removeBookCategory(final BookCategory bookCategory) {
        for (final Book book : getCachedBooks(bookCategory, false)) {
            bookDAO.remove(book);
        }
        bookCategoryDAO.remove(bookCategory);
    }

    /**
     * Switch position of book categories.
     *
     * @param bookCategory1 1st book category
     * @param bookCategory2 2nd book category
     */
    private static void switchPosition(final BookCategory bookCategory1, final BookCategory bookCategory2) {
        final int position = bookCategory1.getPosition();
        bookCategory1.setPosition(bookCategory2.getPosition());
        bookCategory2.setPosition(position);
    }

}
