package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

/**
 * An interface represents facade for books.
 *
 * @author Vladimir Hromada
 */
public interface BookFacade {

	/**
	 * Returns TO for book with ID or null if there isn't such TO for book.
	 *
	 * @param id ID
	 * @return TO for book with ID or null if there isn't such TO for book
	 * @throws IllegalArgumentException if ID is null
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	BookTO getBook(Integer id);

	/**
	 * Adds TO for book. Sets new ID and position.
	 *
	 * @param book TO for book
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if ID isn't null
	 *                                  or author is null
	 *                                  or author is empty string
	 *                                  or title is null
	 *                                  or title is empty string
	 *                                  or languages are null
	 *                                  or languages contain null value
	 *                                  or category is null
	 *                                  or category is empty string
	 *                                  or note is null
	 *                                  or TO for book category is null
	 *                                  or TO for book category ID is null
	 * @throws RecordNotFoundException  if TO for book category doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void add(BookTO book);

	/**
	 * Updates TO for book.
	 *
	 * @param book new value of TO for book
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if ID is null
	 *                                  or author is null
	 *                                  or author is empty string
	 *                                  or title is null
	 *                                  or title is empty string
	 *                                  or languages are null
	 *                                  or languages contain null value
	 *                                  or category is null
	 *                                  or category is empty string
	 *                                  or note is null
	 *                                  or TO for book category is null
	 *                                  or TO for book category ID is null
	 * @throws RecordNotFoundException  if TO for book doesn't exist in data storage
	 *                                  or TO for book category doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void update(BookTO book);

	/**
	 * Removes TO for book.
	 *
	 * @param book TO for book
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if TO for book doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void remove(BookTO book);

	/**
	 * Duplicates TO for book.
	 *
	 * @param book TO for book
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if TO for book doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void duplicate(BookTO book);

	/**
	 * Moves TO for book in list one position up.
	 *
	 * @param book TO for book
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if ID is null
	 *                                  or TO for book can't be moved up
	 * @throws RecordNotFoundException  if TO for book doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void moveUp(BookTO book);

	/**
	 * Moves TO for book in list one position down.
	 *
	 * @param book TO for book
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if ID is null
	 *                                  or TO for book can't be moved down
	 * @throws RecordNotFoundException  if TO for book doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void moveDown(BookTO book);

	/**
	 * Returns true if TO for book exists.
	 *
	 * @param book TO for book
	 * @return true if TO for book exists
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if ID is null
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	boolean exists(BookTO book);

	/**
	 * Returns list of TO for books for specified TO for book category.
	 *
	 * @param bookCategory TO for book category
	 * @return list of TO for books for specified TO for book category
	 * @throws IllegalArgumentException if TO for book category is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if TO for book category doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	List<BookTO> findBooksByBookCategory(BookCategoryTO bookCategory);

}
