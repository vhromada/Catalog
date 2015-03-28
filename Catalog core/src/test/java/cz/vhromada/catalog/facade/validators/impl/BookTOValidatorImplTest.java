package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.ToGenerator;
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

    /**
     * Instance of {@link BookTOValidator}
     */
    private BookTOValidator bookTOValidator;

    /**
     * Initializes validator for TO for book.
     */
    @Before
    public void setUp() {
        bookTOValidator = new BookTOValidatorImpl();
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewBookTOWithNullArgument() {
        bookTOValidator.validateNewBookTO(null);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithNotNullId() {
        bookTOValidator.validateNewBookTO(ToGenerator.newBookWithId(getObjectGenerator()));
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null author.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithNullAuthor() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.setAuthor(null);

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with empty string as author.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithEmptyAuthor() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.setAuthor("");

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null title.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithNullTitle() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.setTitle(null);

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with empty string as title.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithEmptyTitle() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.setTitle("");

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null languages.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithNullLanguages() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.setLanguages(null);

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with languages with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithBadLanguages() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.setLanguages(CollectionUtils.newList(generate(Language.class), null));

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithNullNote() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.setNote(null);

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with null TO for book category.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookWithNullBookCategory() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.setBookCategory(null);

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateNewBookTO(BookTO)} with TO for book with TO for book category with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookWithBookCategoryWithNullId() {
        final BookTO book = ToGenerator.newBook(getObjectGenerator());
        book.getBookCategory().setId(null);

        bookTOValidator.validateNewBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingBookTOWithNullArgument() {
        bookTOValidator.validateExistingBookTO(null);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewBookTOWithNullId() {
        bookTOValidator.validateExistingBookTO(ToGenerator.newBook(getObjectGenerator()));
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null author.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookTOWithNullAuthor() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.setAuthor(null);

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with empty string as author.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookTOWithEmptyAuthor() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.setAuthor("");

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null title.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookTOWithNullTitle() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.setTitle(null);

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with empty string as title.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookTOWithEmptyTitle() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.setTitle("");

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null languages.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookTOWithNullLanguages() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.setLanguages(null);

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with languages with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookTOWithBadLanguages() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.setLanguages(CollectionUtils.newList(generate(Language.class), null));

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookTOWithNullNote() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.setNote(null);

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with null TO for book category.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookWithNullBookCategory() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.setBookCategory(null);

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateExistingBookTO(BookTO)} with TO for book with TO for book category with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingBookWithBookCategoryWithNullId() {
        final BookTO book = ToGenerator.newBookWithId(getObjectGenerator());
        book.getBookCategory().setId(null);

        bookTOValidator.validateExistingBookTO(book);
    }

    /**
     * Test method for {@link BookTOValidator#validateBookTOWithId(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateBookTOWithIdWithNullArgument() {
        bookTOValidator.validateBookTOWithId(null);
    }

    /**
     * Test method for {@link BookTOValidator#validateBookTOWithId(BookTO)} with TO for book with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateBookTOWithIdWithNullId() {
        bookTOValidator.validateBookTOWithId(ToGenerator.newBook(getObjectGenerator()));
    }

}
