package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.validators.exceptions.ValidationException;

/**
 * An interface represents validator for TO for book.
 *
 * @author Vladimir Hromada
 */
public interface BookTOValidator {

	/**
	 * Validates new TO for book.
	 *
	 * @param book validating TO for book
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
	 */
	void validateNewBookTO(BookTO book);

	/**
	 * Validates existing TO for book.
	 *
	 * @param book validating TO for book
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
	 */
	void validateExistingBookTO(BookTO book);

	/**
	 * Validates TO for book with ID.
	 *
	 * @param book validating TO for book
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if ID is null
	 */
	void validateBookTOWithId(BookTO book);

}
