package cz.vhromada.catalog.facade.validators.impl;

import static cz.vhromada.catalog.commons.TestConstants.ID;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.validators.BookCategoryTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link BookCategoryTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class BookCategoryTOValidatorImplTest {

	/** Instance of {@link BookCategoryTOValidator} */
	private BookCategoryTOValidator bookCategoryTOValidator;

	/** Initializes validator for TO for book category. */
	@Before
	public void setUp() {
		bookCategoryTOValidator = new BookCategoryTOValidatorImpl();
	}

	/** Test method for {@link BookCategoryTOValidator#validateNewBookCategoryTO(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNewBookCategoryTOWithNullArgument() {
		bookCategoryTOValidator.validateNewBookCategoryTO(null);
	}

	/** Test method for {@link BookCategoryTOValidator#validateNewBookCategoryTO(BookCategoryTO)} with TO for book category with not null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookCategoryTOWithNotNullId() {
		bookCategoryTOValidator.validateNewBookCategoryTO(ToGenerator.createBookCategory(ID));
	}

	/** Test method for {@link BookCategoryTOValidator#validateNewBookCategoryTO(BookCategoryTO)} with TO for book category with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookCategoryTOWithNullName() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setName(null);

		bookCategoryTOValidator.validateNewBookCategoryTO(bookCategory);
	}

	/** Test method for {@link BookCategoryTOValidator#validateNewBookCategoryTO(BookCategoryTO)} with TO for book category with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookCategoryTOWithEmptyName() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setName("");

		bookCategoryTOValidator.validateNewBookCategoryTO(bookCategory);
	}

	/** Test method for {@link BookCategoryTOValidator#validateNewBookCategoryTO(BookCategoryTO)} with TO for book category with negative count of books. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookCategoryTOWithNegativeBooksCount() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setBooksCount(-1);

		bookCategoryTOValidator.validateNewBookCategoryTO(bookCategory);
	}

	/** Test method for {@link BookCategoryTOValidator#validateNewBookCategoryTO(BookCategoryTO)} with TO for book category with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookCategoryTOWithNullNote() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setNote(null);

		bookCategoryTOValidator.validateNewBookCategoryTO(bookCategory);
	}

	/** Test method for {@link BookCategoryTOValidator#validateExistingBookCategoryTO(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingBookCategoryTOWithNullArgument() {
		bookCategoryTOValidator.validateExistingBookCategoryTO(null);
	}

	/** Test method for {@link BookCategoryTOValidator#validateExistingBookCategoryTO(BookCategoryTO)} with TO for book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookCategoryTOWithNullId() {
		bookCategoryTOValidator.validateExistingBookCategoryTO(ToGenerator.createBookCategory());
	}

	/** Test method for {@link BookCategoryTOValidator#validateExistingBookCategoryTO(BookCategoryTO)} with TO for book category with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookCategoryTOWithNullName() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(ID);
		bookCategory.setName(null);

		bookCategoryTOValidator.validateExistingBookCategoryTO(bookCategory);
	}

	/** Test method for {@link BookCategoryTOValidator#validateExistingBookCategoryTO(BookCategoryTO)} with TO for book category with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookCategoryTOWithEmptyName() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(ID);
		bookCategory.setName("");

		bookCategoryTOValidator.validateExistingBookCategoryTO(bookCategory);
	}

	/**
	 * Test method for {@link BookCategoryTOValidator#validateExistingBookCategoryTO(BookCategoryTO)} with TO for book category with negative count of
	 * books.
	 */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookCategoryTOWithNegativeBooksCount() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setBooksCount(-1);

		bookCategoryTOValidator.validateExistingBookCategoryTO(bookCategory);
	}

	/** Test method for {@link BookCategoryTOValidator#validateExistingBookCategoryTO(BookCategoryTO)} with TO for book category with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookCategoryTOWithNullNote() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(ID);
		bookCategory.setNote(null);

		bookCategoryTOValidator.validateExistingBookCategoryTO(bookCategory);
	}

	/** Test method for {@link BookCategoryTOValidator#validateBookCategoryTOWithId(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateBookCategoryTOWithIdWithNullArgument() {
		bookCategoryTOValidator.validateBookCategoryTOWithId(null);
	}

	/** Test method for {@link BookCategoryTOValidator#validateBookCategoryTOWithId(BookCategoryTO)} with TO for book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateBookCategoryTOWithIdWithNullId() {
		bookCategoryTOValidator.validateBookCategoryTOWithId(ToGenerator.createBookCategory());
	}

}
