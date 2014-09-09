package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.BookCategoryFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.validators.BookCategoryTOValidator;
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
 * A class represents implementation of service for book categories.
 *
 * @author Vladimir Hromada
 */
@Component("bookCategoryFacade")
@Transactional
public class BookCategoryFacadeImpl implements BookCategoryFacade {

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
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void newData() {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");

		try {
			bookCategoryService.newData();
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
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<BookCategoryTO> getBookCategories() {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookService, "Service for book");
		Validators.validateFieldNotNull(conversionService, "Conversion service");

		try {
			final List<BookCategoryTO> bookCategories = new ArrayList<>();
			for (BookCategory bookCategory : bookCategoryService.getBookCategories()) {
				bookCategories.add(convertBookCategoryToBookCategoryTO(bookCategory));
			}
			return bookCategories;
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
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BookCategoryTO getBookCategory(final Integer id) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookService, "Service for book");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return convertBookCategoryToBookCategoryTO(bookCategoryService.getBookCategory(id));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for book category isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final BookCategoryTO bookCategory) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(bookCategoryTOValidator, "Validator for TO for book category");
		bookCategoryTOValidator.validateNewBookCategoryTO(bookCategory);

		try {
			final BookCategory bookCategoryEntity = conversionService.convert(bookCategory, BookCategory.class);
			bookCategoryService.add(bookCategoryEntity);
			if (bookCategoryEntity.getId() == null) {
				throw new FacadeOperationException("service tier doesn't set ID.");
			}
			bookCategory.setId(bookCategoryEntity.getId());
			bookCategory.setPosition(bookCategoryEntity.getPosition());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for book category isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final BookCategoryTO bookCategory) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(bookCategoryTOValidator, "Validator for TO for book category");
		bookCategoryTOValidator.validateExistingBookCategoryTO(bookCategory);
		try {
			final BookCategory bookCategoryEntity = conversionService.convert(bookCategory, BookCategory.class);
			Validators.validateExists(bookCategoryService.exists(bookCategoryEntity), "TO for book category");

			bookCategoryService.update(bookCategoryEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or validator for TO for book category isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final BookCategoryTO bookCategory) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookCategoryTOValidator, "Validator for TO for book category");
		bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
		try {
			final BookCategory bookCategoryEntity = bookCategoryService.getBookCategory(bookCategory.getId());
			Validators.validateExists(bookCategoryEntity, "TO for book category");

			bookCategoryService.remove(bookCategoryEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or validator for TO for book category isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final BookCategoryTO bookCategory) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookCategoryTOValidator, "Validator for TO for book category");
		bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
		try {
			final BookCategory oldBookCategory = bookCategoryService.getBookCategory(bookCategory.getId());
			Validators.validateExists(oldBookCategory, "TO for book category");

			bookCategoryService.duplicate(oldBookCategory);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or validator for TO for book category isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final BookCategoryTO bookCategory) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookCategoryTOValidator, "Validator for TO for book category");
		bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
		try {
			final BookCategory bookCategoryEntity = bookCategoryService.getBookCategory(bookCategory.getId());
			Validators.validateExists(bookCategoryEntity, "TO for book category");
			final List<BookCategory> bookCategories = bookCategoryService.getBookCategories();
			Validators.validateMoveUp(bookCategories, bookCategoryEntity, "book category");

			bookCategoryService.moveUp(bookCategoryEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or validator for TO for book category isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final BookCategoryTO bookCategory) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(bookCategoryTOValidator, "Validator for TO for book category");
		bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
		try {
			final BookCategory bookCategoryEntity = bookCategoryService.getBookCategory(bookCategory.getId());
			Validators.validateExists(bookCategoryEntity, "TO for book category");
			final List<BookCategory> bookCategories = bookCategoryService.getBookCategories();
			Validators.validateMoveDown(bookCategories, bookCategoryEntity, "book category");

			bookCategoryService.moveDown(bookCategoryEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for book category isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final BookCategoryTO bookCategory) {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(bookCategoryTOValidator, "Validator for TO for book category");
		bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
		try {

			return bookCategoryService.exists(conversionService.convert(bookCategory, BookCategory.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void updatePositions() {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");

		try {
			bookCategoryService.updatePositions();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for book categories isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public int getBooksCount() {
		Validators.validateFieldNotNull(bookCategoryService, "Service for book categories");

		try {
			return bookCategoryService.getBooksCount();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * Converts entity book category to TO for book category.
	 *
	 * @param bookCategory converting entity category
	 * @return converted TO for book category
	 */
	private BookCategoryTO convertBookCategoryToBookCategoryTO(final BookCategory bookCategory) {
		final BookCategoryTO bookCategoryTO = conversionService.convert(bookCategory, BookCategoryTO.class);
		if (bookCategoryTO != null) {
			bookCategoryTO.setBooksCount(bookService.findBooksByBookCategory(bookCategory).size());
		}
		return bookCategoryTO;
	}

}
