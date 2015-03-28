package cz.vhromada.catalog.facade.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.BookFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.catalog.facade.validators.BookCategoryTOValidator;
import cz.vhromada.catalog.facade.validators.BookTOValidator;
import cz.vhromada.catalog.service.BookCategoryService;
import cz.vhromada.catalog.service.BookService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of service for books.
 *
 * @author Vladimir Hromada
 */
@Component("bookFacade")
@Transactional
public class BookFacadeImpl implements BookFacade {

    /**
     * Service for book categories argument
     */
    private static final String BOOK_CATEGORY_SERVICE_ARGUMENT = "Service for book categories";

    /**
     * Service for books argument
     */
    private static final String BOOK_SERVICE_ARGUMENT = "Service for books";

    /**
     * Converter argument
     */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /**
     * Validator for TO for book category field
     */
    private static final String BOOK_CATEGORY_TO_VALIDATOR_ARGUMENT = "Validator for TO for book category";

    /**
     * Validator for TO for book field
     */
    private static final String BOOK_TO_VALIDATOR_ARGUMENT = "Validator for TO for book";

    /**
     * Book argument
     */
    private static final String BOOK_ARGUMENT = "book";

    /**
     * TO for book category argument
     */
    private static final String BOOK_CATEGORY_TO_ARGUMENT = "TO for book category";

    /**
     * TO for book  argument
     */
    private static final String BOOK_TO_ARGUMENT = "TO for book";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link FacadeOperationException}
     */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /**
     * Message for not setting ID
     */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /**
     * Service for book categories
     */
    private BookCategoryService bookCategoryService;

    /**
     * Service for books
     */
    private BookService bookService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for book category
     */
    private BookCategoryTOValidator bookCategoryTOValidator;

    /**
     * Validator for TO for book
     */
    private BookTOValidator bookTOValidator;

    /**
     * Creates a new instance of BookFacadeImpl.
     *
     * @param bookCategoryService     service for book categories
     * @param bookService             service for book
     * @param converter               converter
     * @param bookCategoryTOValidator validator for TO for book category
     * @param bookTOValidator         validator for TO for book
     * @throws IllegalArgumentException if service for book categories is null
     *                                  or service for books is null
     *                                  or converter is null
     *                                  or validator for TO for book category is null
     */
    @Autowired
    public BookFacadeImpl(final BookCategoryService bookCategoryService,
            final BookService bookService,
            final Converter converter,
            final BookCategoryTOValidator bookCategoryTOValidator,
            final BookTOValidator bookTOValidator) {
        Validators.validateArgumentNotNull(bookCategoryService, BOOK_CATEGORY_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(bookService, BOOK_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(bookCategoryTOValidator, BOOK_CATEGORY_TO_VALIDATOR_ARGUMENT);
        Validators.validateArgumentNotNull(bookTOValidator, BOOK_TO_VALIDATOR_ARGUMENT);

        this.bookCategoryService = bookCategoryService;
        this.bookService = bookService;
        this.converter = converter;
        this.bookCategoryTOValidator = bookCategoryTOValidator;
        this.bookTOValidator = bookTOValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public BookTO getBook(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(bookService.getBook(id), BookTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void add(final BookTO book) {
        bookTOValidator.validateNewBookTO(book);
        try {
            final BookCategory bookCategory = bookCategoryService.getBookCategory(book.getBookCategory().getId());
            Validators.validateExists(bookCategory, BOOK_CATEGORY_TO_ARGUMENT);

            final Book bookEntity = converter.convert(book, Book.class);
            bookEntity.setBookCategory(bookCategory);
            bookService.add(bookEntity);
            if (bookEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            book.setId(bookEntity.getId());
            book.setPosition(bookEntity.getPosition());
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void update(final BookTO book) {
        bookTOValidator.validateExistingBookTO(book);
        try {
            final Book bookEntity = converter.convert(book, Book.class);
            Validators.validateExists(bookService.exists(bookEntity), BOOK_TO_ARGUMENT);
            final BookCategory bookCategory = bookCategoryService.getBookCategory(book.getBookCategory().getId());
            Validators.validateExists(bookCategory, BOOK_CATEGORY_TO_ARGUMENT);

            bookEntity.setBookCategory(bookCategory);
            bookService.update(bookEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void remove(final BookTO book) {
        bookTOValidator.validateBookTOWithId(book);
        try {
            final Book bookEntity = bookService.getBook(book.getId());
            Validators.validateExists(bookEntity, BOOK_TO_ARGUMENT);

            bookService.remove(bookEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void duplicate(final BookTO book) {
        bookTOValidator.validateBookTOWithId(book);
        try {
            final Book oldBook = bookService.getBook(book.getId());
            Validators.validateExists(oldBook, BOOK_TO_ARGUMENT);

            bookService.duplicate(oldBook);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveUp(final BookTO book) {
        bookTOValidator.validateBookTOWithId(book);
        try {
            final Book bookEntity = bookService.getBook(book.getId());
            Validators.validateExists(bookEntity, BOOK_TO_ARGUMENT);
            final List<Book> books = bookService.findBooksByBookCategory(bookEntity.getBookCategory());
            Validators.validateMoveUp(books, bookEntity, BOOK_ARGUMENT);

            bookService.moveUp(bookEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveDown(final BookTO book) {
        bookTOValidator.validateBookTOWithId(book);
        try {
            final Book bookEntity = bookService.getBook(book.getId());
            Validators.validateExists(bookEntity, BOOK_TO_ARGUMENT);
            final List<Book> books = bookService.findBooksByBookCategory(bookEntity.getBookCategory());
            Validators.validateMoveDown(books, bookEntity, BOOK_ARGUMENT);

            bookService.moveDown(bookEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final BookTO book) {
        bookTOValidator.validateBookTOWithId(book);

        try {
            return bookService.exists(converter.convert(book, Book.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookTO> findBooksByBookCategory(final BookCategoryTO bookCategory) {
        bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
        try {
            final BookCategory bookCategoryEntity = bookCategoryService.getBookCategory(bookCategory.getId());
            Validators.validateExists(bookCategoryEntity, BOOK_CATEGORY_TO_ARGUMENT);

            final List<BookTO> books = converter.convertCollection(bookService.findBooksByBookCategory(bookCategoryEntity), BookTO.class);
            Collections.sort(books);
            return books;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}

