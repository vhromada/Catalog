package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.validators.Validators;
import org.springframework.cache.Cache;

/**
 * An abstract class represents service with book cache.
 *
 * @author Vladimir Hromada
 */
public abstract class AbstractBookService extends AbstractInnerService<BookCategory, Book> {

    /** Cache for books argument */
    private static final String BOOK_CACHE_ARGUMENT = "Cache for books";

    /** Cache key for list of book categories */
    private static final String BOOK_CATEGORIES_CACHE_KEY = "bookCategories";

    /** Cache key for book category */
    private static final String BOOK_CATEGORY_CACHE_KEY = "bookCategory";

    /** Cache key for list of books */
    private static final String BOOKS_CACHE_KEY = "books";

    /** Cache key for book */
    private static final String BOOK_CACHE_KEY = "book";

    /** Cache for books */
    private Cache bookCache;

    /**
     * Creates a new instance of AbstractBookService.
     *
     * @param bookCache cache for books
     * @throws IllegalArgumentException if cache for books is null
     */
    public AbstractBookService(final Cache bookCache) {
        Validators.validateArgumentNotNull(bookCache, BOOK_CACHE_ARGUMENT);

        this.bookCache = bookCache;
    }

    /** Remove all mappings from the cache for books. */
    protected void clearCache() {
        bookCache.clear();
    }

    /**
     * Returns list of book categories.
     *
     * @param cached true if returned data from DAO should be cached
     * @return list of categories
     */
    protected List<BookCategory> getCachedBookCategories(final boolean cached) {
        return getCachedObjects(bookCache, BOOK_CATEGORIES_CACHE_KEY, cached);
    }

    /**
     * Returns list of books for specified book category.
     *
     * @param bookCategory book category
     * @param cached       true if returned data from DAO should be cached
     * @return list of books for specified book category
     */
    protected List<Book> getCachedBooks(final BookCategory bookCategory, final boolean cached) {
        return getCachedInnerObjects(bookCache, BOOKS_CACHE_KEY + bookCategory.getId(), cached, bookCategory);
    }

    /**
     * Returns book category with ID or null if there isn't such book category.
     *
     * @param id ID
     * @return book category with ID or null if there isn't such book category
     */
    protected BookCategory getCachedBookCategory(final Integer id) {
        return getCachedObject(bookCache, BOOK_CATEGORY_CACHE_KEY, id, true);
    }

    /**
     * Returns book with ID or null if there isn't such book.
     *
     * @param id ID
     * @return book with ID or null if there isn't such book
     */
    protected Book getCachedBook(final Integer id) {
        return getCachedInnerObject(bookCache, BOOK_CACHE_KEY, id);
    }

    /**
     * Adds book category to cache.
     *
     * @param bookCategory book category
     */
    protected void addBookCategoryToCache(final BookCategory bookCategory) {
        addObjectToListCache(bookCache, BOOK_CATEGORIES_CACHE_KEY, bookCategory);
        addObjectToCache(bookCache, BOOK_CATEGORY_CACHE_KEY + bookCategory.getId(), bookCategory);
    }

    /**
     * Adds book to cache.
     *
     * @param book book
     */
    protected void addBookToCache(final Book book) {
        addInnerObjectToListCache(bookCache, BOOKS_CACHE_KEY + book.getBookCategory().getId(), book);
        addInnerObjectToCache(bookCache, BOOK_CACHE_KEY + book.getId(), book);
    }

    /**
     * Removes book from cache.
     *
     * @param book book
     */
    protected void removeBookFromCache(final Book book) {
        removeInnerObjectFromCache(bookCache, BOOKS_CACHE_KEY + book.getBookCategory().getId(), book);
        bookCache.evict(BOOK_CACHE_KEY + book.getId());
    }

    @Override
    protected List<BookCategory> getData() {
        return getDAOBookCategories();
    }

    @Override
    protected BookCategory getData(final Integer id) {
        return getDAOBookCategory(id);
    }

    @Override
    protected List<Book> getInnerData(final BookCategory parent) {
        return getDAOBooks(parent);
    }

    @Override
    protected Book getInnerData(final Integer id) {
        return getDAOBook(id);
    }

    /**
     * Returns list of book categories from DAO tier.
     *
     * @return list of book categories from DAO tier
     */
    protected abstract List<BookCategory> getDAOBookCategories();

    /**
     * Returns list of books for specified book category from DAO tier.
     *
     * @param bookCategory book category
     * @return list of books for specified book category from DAO tier
     */
    protected abstract List<Book> getDAOBooks(final BookCategory bookCategory);

    /**
     * Returns book category with ID from DAO tier.
     *
     * @param id ID
     * @return book category with ID from DAO tier
     */
    protected abstract BookCategory getDAOBookCategory(final Integer id);

    /**
     * Returns book with ID from DAO tier.
     *
     * @param id ID
     * @return book with ID from DAO tier
     */
    protected abstract Book getDAOBook(final Integer id);

}
