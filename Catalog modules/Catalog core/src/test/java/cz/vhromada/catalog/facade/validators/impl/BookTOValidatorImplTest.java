package cz.vhromada.catalog.facade.validators.impl;

import static cz.vhromada.catalog.commons.TestConstants.BAD_LANGUAGES;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.catalog.facade.validators.BookTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link BookTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class BookTOValidatorImplTest extends ObjectGeneratorTest {

	/** Instance of {@link BookTOValidator} */
	private BookTOValidator bookTOValidator;

	/** Initializes validator for TO for book. */
	@Before
	public void setUp() {
		bookTOValidator = new BookTOValidatorImpl();
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNewBookTOWithNullArgument() {
		bookTOValidator.validateNewBookTO(null);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with not null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithNotNullId() {
		bookTOValidator.validateNewBookTO(generate(BookTO.class));
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null author. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithNullAuthor() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setAuthor(null);

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with empty string as author. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithEmptyAuthor() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setAuthor("");

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null title. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithNullTitle() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setTitle(null);

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with empty string as title. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithEmptyTitle() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setTitle("");

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null languages. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithNullLanguages() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setLanguages(null);

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with languages with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithBadLanguages() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setLanguages(BAD_LANGUAGES);

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null category. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithNullCategory() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setCategory(null);

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with empty string as category. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithEmptyCategory() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setCategory("");

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithNullNote() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setNote(null);

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null TO for book category. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookWithNullBookCategory() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.setBookCategory(null);

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with TO for book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookWithBookCategoryWithNullId() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);
		book.getBookCategory().setId(null);

		bookTOValidator.validateNewBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingBookTOWithNullArgument() {
		bookTOValidator.validateExistingBookTO(null);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewBookTOWithNullId() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null author. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithNullAuthor() {
		final BookTO book = generate(BookTO.class);
		book.setAuthor(null);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with empty string as author. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithEmptyAuthor() {
		final BookTO book = generate(BookTO.class);
		book.setAuthor("");

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null title. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithNullTitle() {
		final BookTO book = generate(BookTO.class);
		book.setTitle(null);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with empty string as title. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithEmptyTitle() {
		final BookTO book = generate(BookTO.class);
		book.setTitle("");

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null languages. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithNullLanguages() {
		final BookTO book = generate(BookTO.class);
		book.setLanguages(null);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with languages with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithBadLanguages() {
		final BookTO book = generate(BookTO.class);
		book.setLanguages(BAD_LANGUAGES);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null category. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithNullCategory() {
		final BookTO book = generate(BookTO.class);
		book.setCategory(null);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with empty string as category. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithEmptyCategory() {
		final BookTO book = generate(BookTO.class);
		book.setCategory("");

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookTOWithNullNote() {
		final BookTO book = generate(BookTO.class);
		book.setNote(null);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null TO for book category. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookWithNullBookCategory() {
		final BookTO book = generate(BookTO.class);
		book.setBookCategory(null);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with TO for book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingBookWithBookCategoryWithNullId() {
		final BookTO book = generate(BookTO.class);
		book.getBookCategory().setId(null);

		bookTOValidator.validateExistingBookTO(book);
	}

	/** Test method for {@link BookTOValidator#validateBookTOWithId(BookTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateBookTOWithIdWithNullArgument() {
		bookTOValidator.validateBookTOWithId(null);
	}

	/** Test method for {@link BookTOValidator#validateBookTOWithId(BookTO)} with TO for book with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateBookTOWithIdWithNullId() {
		final BookTO book = generate(BookTO.class);
		book.setId(null);

		bookTOValidator.validateBookTOWithId(book);
	}

}
