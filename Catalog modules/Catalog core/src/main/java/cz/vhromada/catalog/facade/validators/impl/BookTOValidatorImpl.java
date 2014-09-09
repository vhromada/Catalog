package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.catalog.facade.validators.BookTOValidator;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for book.
 *
 * @author Vladimir Hromada
 */
@Component("bookTOValidator")
public class BookTOValidatorImpl implements BookTOValidator {

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateNewBookTO(final BookTO book) {
		validateBookTO(book);
		Validators.validateNull(book.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateExistingBookTO(final BookTO book) {
		validateBookTO(book);
		Validators.validateNotNull(book.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateBookTOWithId(final BookTO book) {
		Validators.validateArgumentNotNull(book, "TO for book");
		Validators.validateNotNull(book.getId(), "ID");
	}

	/**
	 * Validates TO for book.
	 *
	 * @param book validating TO for book
	 * @throws IllegalArgumentException if TO for book is null
	 * @throws ValidationException      if author is null
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
	private void validateBookTO(final BookTO book) {
		Validators.validateArgumentNotNull(book, "TO for book");
		Validators.validateNotNull(book.getAuthor(), "Author");
		Validators.validateNotEmptyString(book.getAuthor(), "Author");
		Validators.validateNotNull(book.getTitle(), "Title");
		Validators.validateNotEmptyString(book.getTitle(), "Title");
		Validators.validateNotNull(book.getLanguages(), "Languages");
		Validators.validateCollectionNotContainNull(book.getLanguages(), "Languages");
		Validators.validateNotNull(book.getCategory(), "Category");
		Validators.validateNotEmptyString(book.getCategory(), "Category");
		Validators.validateNotNull(book.getNote(), "Note");
		Validators.validateNotNull(book.getBookCategory(), "TO for book category");
		Validators.validateNotNull(book.getBookCategory().getId(), "TO for book category ID");
	}

}
