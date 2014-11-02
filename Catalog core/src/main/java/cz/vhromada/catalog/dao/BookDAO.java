package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;

/**
 * An interface represents DAO for books.
 *
 * @author Vladimir Hromada
 */
public interface BookDAO {

	/**
	 * Returns book with ID or null if there isn't such book.
	 *
	 * @param id ID
	 * @return book with ID or null if there isn't such book
	 * @throws IllegalArgumentException if ID is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	Book getBook(Integer id);

	/**
	 * Adds book. Sets new ID and position.
	 *
	 * @param book book
	 * @throws IllegalArgumentException if book is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void add(Book book);

	/**
	 * Updates book.
	 *
	 * @param book book
	 * @throws IllegalArgumentException if book is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void update(Book book);

	/**
	 * Removes book.
	 *
	 * @param book book
	 * @throws IllegalArgumentException if book is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void remove(Book book);

	/**
	 * Returns list of books for specified book category.
	 *
	 * @param bookCategory book category
	 * @return list of books for specified book category
	 * @throws IllegalArgumentException if book category is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	List<Book> findBooksByBookCategory(BookCategory bookCategory);

}
