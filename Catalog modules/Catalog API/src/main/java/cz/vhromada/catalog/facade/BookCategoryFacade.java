package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

/**
 * An interface represents facade for book categories.
 *
 * @author Vladimir Hromada
 */
public interface BookCategoryFacade {

	/**
	 * Creates new data.
	 *
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void newData();

	/**
	 * Returns list of book category.
	 *
	 * @return list of TO for book category
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	List<BookCategoryTO> getBookCategories();

	/**
	 * Returns TO for book category with ID or null if there isn't such TO for book category.
	 *
	 * @param id ID
	 * @return TO for book category with ID or null if there isn't such TO for book category
	 * @throws IllegalArgumentException if ID is null
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	BookCategoryTO getBookCategory(Integer id);

	/**
	 * Adds TO for book category. Sets new ID and position.
	 *
	 * @param bookCategory TO for book category
	 * @throws IllegalArgumentException if TO for book category is null
	 * @throws ValidationException      if ID isn't null
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or count of books is negative number
	 *                                  or note is null
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void add(BookCategoryTO bookCategory);

	/**
	 * Updates TO for book category.
	 *
	 * @param bookCategory new value of TO for book category
	 * @throws IllegalArgumentException if TO for book category is null
	 * @throws ValidationException      if ID is null
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or count of books is negative number
	 *                                  or note is null
	 * @throws RecordNotFoundException  if TO for book category doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void update(BookCategoryTO bookCategory);

	/**
	 * Removes TO for book category.
	 *
	 * @param bookCategory TO for book category
	 * @throws IllegalArgumentException if TO for book category is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if TO for book category doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void remove(BookCategoryTO bookCategory);

	/**
	 * Duplicates TO for book category.
	 *
	 * @param bookCategory TO for book category
	 * @throws IllegalArgumentException if TO for book category is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if TO for book category doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void duplicate(BookCategoryTO bookCategory);

	/**
	 * Moves TO for book category in list one position up.
	 *
	 * @param bookCategory TO for book category
	 * @throws IllegalArgumentException if TO for book category is null
	 * @throws ValidationException      if ID is null
	 *                                  or TO for book category can't be moved up
	 * @throws RecordNotFoundException  if TO for book category doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void moveUp(BookCategoryTO bookCategory);

	/**
	 * Moves TO for book category in list one position down.
	 *
	 * @param bookCategory TO for book category
	 * @throws IllegalArgumentException if TO for book category is null
	 * @throws ValidationException      if ID is null
	 *                                  or TO for book category can't be moved down
	 * @throws RecordNotFoundException  if TO for book category doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void moveDown(BookCategoryTO bookCategory);

	/**
	 * Returns true if TO for book category exists.
	 *
	 * @param bookCategory TO for book category
	 * @return true if TO for book category exists
	 * @throws IllegalArgumentException if TO for book category is null
	 * @throws ValidationException      if ID is null
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	boolean exists(BookCategoryTO bookCategory);

	/**
	 * Updates positions.
	 *
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void updatePositions();

	/**
	 * Returns count of books from all book categories.
	 *
	 * @return count of books from all book categories
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	int getBooksCount();

}
