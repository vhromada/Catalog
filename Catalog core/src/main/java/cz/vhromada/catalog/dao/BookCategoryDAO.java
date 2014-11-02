package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.BookCategory;

/**
 * An interface represents DAO for book categories.
 *
 * @author Vladimir Hromada
 */
public interface BookCategoryDAO {

	/**
	 * Returns list of book categories.
	 *
	 * @return list of book categories
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *          if there was error with working with data storage
	 */
	List<BookCategory> getBookCategories();

	/**
	 * Returns book category with ID or null if there isn't such book category.
	 *
	 * @param id ID
	 * @return book category with ID or null if there isn't such book category
	 * @throws IllegalArgumentException if ID is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	BookCategory getBookCategory(Integer id);

	/**
	 * Adds book category. Sets new ID and position.
	 *
	 * @param bookCategory book category
	 * @throws IllegalArgumentException if book category is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void add(BookCategory bookCategory);

	/**
	 * Updates book category.
	 *
	 * @param bookCategory book category
	 * @throws IllegalArgumentException if book category is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void update(BookCategory bookCategory);

	/**
	 * Removes book category.
	 *
	 * @param bookCategory book category
	 * @throws IllegalArgumentException if book category is null
	 * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException
	 *                                  if there was error with working with data storage
	 */
	void remove(BookCategory bookCategory);

}
