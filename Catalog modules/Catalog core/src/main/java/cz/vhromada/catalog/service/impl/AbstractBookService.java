package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;

/**
 * An abstract class represents service with book cache.
 *
 * @author Vladimir Hromada
 */
public abstract class AbstractBookService extends AbstractInnerService<BookCategory, Book> {

	/** Cache for books */
	@Value("#{cacheManager.getCache('bookCache')}")
	private Cache bookCache;

	/**
	 * Returns cache for books.
	 *
	 * @return cache for books
	 */
	public Cache getBookCache() {
		return bookCache;
	}

	/**
	 * Sets a new value to cache for books.
	 *
	 * @param bookCache new value
	 */
	public void setBookCache(final Cache bookCache) {
		this.bookCache = bookCache;
	}

	/**
	 * Validates if cache for books is set.
	 *
	 * @throws IllegalStateException if cache for books is null
	 */
	protected void validateBookCacheNotNull() {
		Validators.validateFieldNotNull(bookCache, "Cache for books");
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
		return getCachedObjects(bookCache, "bookCategories", cached);
	}

	/**
	 * Returns list of books for specified book category.
	 *
	 * @param bookCategory book category
	 * @param cached       true if returned data from DAO should be cached
	 * @return list of books for specified book category
	 */
	protected List<Book> getCachedBooks(final BookCategory bookCategory, final boolean cached) {
		return getCachedInnerObjects(bookCache, "books" + bookCategory.getId(), cached, bookCategory);
	}

	/**
	 * Returns book category with ID or null if there isn't such book category.
	 *
	 * @param id ID
	 * @return book category with ID or null if there isn't such book category
	 */
	protected BookCategory getCachedBookCategory(final Integer id) {
		return getCachedObject(bookCache, "bookCategory", id, true);
	}

	/**
	 * Returns book with ID or null if there isn't such book.
	 *
	 * @param id ID
	 * @return book with ID or null if there isn't such book
	 */
	protected Book getCachedBook(final Integer id) {
		return getCachedInnerObject(bookCache, "book", id);
	}

	/**
	 * Adds book category to cache.
	 *
	 * @param bookCategory book category
	 */
	protected void addBookCategoryToCache(final BookCategory bookCategory) {
		addObjectToListCache(bookCache, "bookCategories", bookCategory);
		addObjectToCache(bookCache, "bookCategory" + bookCategory.getId(), bookCategory);
	}

	/**
	 * Adds book to cache.
	 *
	 * @param book book
	 */
	protected void addBookToCache(final Book book) {
		addInnerObjectToListCache(bookCache, "books" + book.getBookCategory().getId(), book);
		addInnerObjectToCache(bookCache, "book" + book.getId(), book);
	}

	/**
	 * Removes book from cache.
	 *
	 * @param book book
	 */
	protected void removeBookFromCache(final Book book) {
		removeInnerObjectFromCache(bookCache, "books" + book.getBookCategory().getId(), book);
		bookCache.evict("book" + book.getId());
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
