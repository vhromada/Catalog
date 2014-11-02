package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
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
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
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

	/** Service for book categories */
	@Autowired
	private BookCategoryService bookCategoryService;

	/** Service for books */
	@Autowired
	private BookService bookService;

	/** Conversion service */
	@Autowired
	@Qualifier("coreConversionService")
	private ConversionService conversionService;

	/** Validator for TO for book category */
	@Autowired
	private BookCategoryTOValidator bookCategoryTOValidator;

	/** Validator for TO for book */
	@Autowired
	private BookTOValidator bookTOValidator;

	/**
	 * Returns service for book categories.
	 *
	 * @return service for book categories
	 */
	public BookCategoryService getBookCategoryService() {
		return bookCategoryService;
	}

	/**
	 * Sets a new value to service for book categories.
	 *
	 * @param bookCategoryService new value
	 */
	public void setBookCategoryService(final BookCategoryService bookCategoryService) {
		this.bookCategoryService = bookCategoryService;
	}

	/**
	 * Returns service for books.
	 *
	 * @return service for books
	 */
	public BookService getBookService() {
		return bookService;
	}

	/**
	 * Sets a new value to service for books.
	 *
	 * @param bookService new value
	 */
	public void setBookService(final BookService bookService) {
		this.bookService = bookService;
	}

	/**
	 * Returns conversion service.
	 *
	 * @return conversion service
	 */
	public ConversionService getConversionService() {
		return conversionService;
	}

	/**
	 * Sets a new value to conversion service.
	 *
	 * @param conversionService new value
	 */
	public void setConversionService(final ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/**
	 * Returns validator for TO for book category.
	 *
	 * @return validator for TO for book category
	 */
	public BookCategoryTOValidator getBookCategoryTOValidator() {
		return bookCategoryTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for book category.
	 *
	 * @param bookCategoryTOValidator new value
	 */
	public void setBookCategoryTOValidator(final BookCategoryTOValidator bookCategoryTOValidator) {
		this.bookCategoryTOValidator = bookCategoryTOValidator;
	}

	/**
	 * Returns validator for TO for book.
	 *
	 * @return validator for TO for book
	 */
	public BookTOValidator getBookTOValidator() {
		return bookTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for book.
	 *
	 * @param bookTOValidator new value
	 */
	public void setBookTOValidator(final BookTOValidator bookTOValidator) {
		this.bookTOValidator = bookTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for books isn't set
	 *                                  or conversion service isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BookTO getBook(final Integer id) {
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return conversionService.convert(bookService.getBook(id), BookTO.class);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or service for books isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for book isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final BookTO book) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(bookTOValidator, "Validator for TO for book");
		bookTOValidator.validateNewBookTO(book);
		try {
			final BookCategory bookCategory = bookCategoryService.getBookCategory(book.getBookCategory().getId());
			Validators.validateExists(bookCategory, "TO for book category");

			final Book bookEntity = conversionService.convert(book, Book.class);
			bookEntity.setBookCategory(bookCategory);
			bookService.add(bookEntity);
			if (bookEntity.getId() == null) {
				throw new FacadeOperationException("service tier doesn't set ID.");
			}
			book.setId(bookEntity.getId());
			book.setPosition(bookEntity.getPosition());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or service for books isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for book isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final BookTO book) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(bookTOValidator, "Validator for TO for book");
		bookTOValidator.validateExistingBookTO(book);
		try {
			final Book bookEntity = conversionService.convert(book, Book.class);
			Validators.validateExists(bookService.exists(bookEntity), "TO for book");
			final BookCategory bookCategory = bookCategoryService.getBookCategory(book.getBookCategory().getId());
			Validators.validateExists(bookCategory, "TO for book category");

			bookEntity.setBookCategory(bookCategory);
			bookService.update(bookEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for books isn't set
	 *                                  or validator for TO for book isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final BookTO book) {
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(bookTOValidator, "Validator for TO for book");
		bookTOValidator.validateBookTOWithId(book);
		try {
			final Book bookEntity = bookService.getBook(book.getId());
			Validators.validateExists(bookEntity, "TO for book");

			bookService.remove(bookEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for books isn't set
	 *                                  or validator for TO for book isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final BookTO book) {
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(bookTOValidator, "Validator for TO for book");
		bookTOValidator.validateBookTOWithId(book);
		try {
			final Book oldBook = bookService.getBook(book.getId());
			Validators.validateExists(oldBook, "TO for book");

			bookService.duplicate(oldBook);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for books isn't set
	 *                                  or validator for TO for book isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final BookTO book) {
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(bookTOValidator, "Validator for TO for book");
		bookTOValidator.validateBookTOWithId(book);
		try {
			final Book bookEntity = bookService.getBook(book.getId());
			Validators.validateExists(bookEntity, "TO for book");
			final List<Book> books = bookService.findBooksByBookCategory(bookEntity.getBookCategory());
			Validators.validateMoveUp(books, bookEntity, "book");

			bookService.moveUp(bookEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for books isn't set
	 *                                  or validator for TO for book isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final BookTO book) {
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(bookTOValidator, "Validator for TO for book");
		bookTOValidator.validateBookTOWithId(book);
		try {
			final Book bookEntity = bookService.getBook(book.getId());
			Validators.validateExists(bookEntity, "TO for book");
			final List<Book> books = bookService.findBooksByBookCategory(bookEntity.getBookCategory());
			Validators.validateMoveDown(books, bookEntity, "book");

			bookService.moveDown(bookEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for books isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for book isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final BookTO book) {
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(bookTOValidator, "Validator for TO for book");
		bookTOValidator.validateBookTOWithId(book);
		try {

			return bookService.exists(conversionService.convert(book, Book.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or service for books isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for book category isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<BookTO> findBooksByBookCategory(final BookCategoryTO bookCategory) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookService, "Service for books");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(bookCategoryTOValidator, "Validator for TO for book category");
		bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
		try {
			final BookCategory bookCategoryEntity = bookCategoryService.getBookCategory(bookCategory.getId());
			Validators.validateExists(bookCategoryEntity, "TO for book category");

			final List<BookTO> books = new ArrayList<>();
			for (Book book : bookService.findBooksByBookCategory(bookCategoryEntity)) {
				books.add(conversionService.convert(book, BookTO.class));
			}
			Collections.sort(books);
			return books;
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

}

