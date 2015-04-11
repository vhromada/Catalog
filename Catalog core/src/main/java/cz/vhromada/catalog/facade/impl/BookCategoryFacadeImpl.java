package cz.vhromada.catalog.facade.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.BookCategoryFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.validators.BookCategoryTOValidator;
import cz.vhromada.catalog.service.BookCategoryService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    /**
     * Service for book categories argument
     */
    private static final String BOOK_CATEGORY_SERVICE_ARGUMENT = "Service for book categories";

    /**
     * Converter argument
     */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /**
     * Validator for TO for book category argument
     */
    private static final String BOOK_CATEGORY_TO_VALIDATOR_ARGUMENT = "Validator for TO for book category";

    /**
     * Book category argument
     */
    private static final String BOOK_CATEGORY_ARGUMENT = "book category";

    /**
     * TO for book category argument
     */
    private static final String BOOK_CATEGORY_TO_ARGUMENT = "TO for book category";

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
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for book category
     */
    private BookCategoryTOValidator bookCategoryTOValidator;

    /**
     * Creates a new instance of BookCategoryFacadeImpl.
     *
     * @param bookCategoryService     service for book categories
     * @param converter               converter
     * @param bookCategoryTOValidator validator for TO for book category
     * @throws IllegalArgumentException if service for book categories is null
     *                                  or converter is null
     *                                  or validator for TO for book category is null
     */
    @Autowired
    public BookCategoryFacadeImpl(final BookCategoryService bookCategoryService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final BookCategoryTOValidator bookCategoryTOValidator) {
        Validators.validateArgumentNotNull(bookCategoryService, BOOK_CATEGORY_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(bookCategoryTOValidator, BOOK_CATEGORY_TO_VALIDATOR_ARGUMENT);

        this.bookCategoryService = bookCategoryService;
        this.converter = converter;
        this.bookCategoryTOValidator = bookCategoryTOValidator;
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            bookCategoryService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookCategoryTO> getBookCategories() {
        try {
            final List<BookCategoryTO> bookCategories = converter.convertCollection(bookCategoryService.getBookCategories(), BookCategoryTO.class);
            Collections.sort(bookCategories);
            return bookCategories;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public BookCategoryTO getBookCategory(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(bookCategoryService.getBookCategory(id), BookCategoryTO.class);
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
    public void add(final BookCategoryTO bookCategory) {
        bookCategoryTOValidator.validateNewBookCategoryTO(bookCategory);

        try {
            final BookCategory bookCategoryEntity = converter.convert(bookCategory, BookCategory.class);
            bookCategoryService.add(bookCategoryEntity);
            if (bookCategoryEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            bookCategory.setId(bookCategoryEntity.getId());
            bookCategory.setPosition(bookCategoryEntity.getPosition());
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
    public void update(final BookCategoryTO bookCategory) {
        bookCategoryTOValidator.validateExistingBookCategoryTO(bookCategory);
        try {
            final BookCategory bookCategoryEntity = converter.convert(bookCategory, BookCategory.class);
            Validators.validateExists(bookCategoryService.exists(bookCategoryEntity), BOOK_CATEGORY_TO_ARGUMENT);

            bookCategoryService.update(bookCategoryEntity);
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
    public void remove(final BookCategoryTO bookCategory) {
        bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
        try {
            final BookCategory bookCategoryEntity = bookCategoryService.getBookCategory(bookCategory.getId());
            Validators.validateExists(bookCategoryEntity, BOOK_CATEGORY_TO_ARGUMENT);

            bookCategoryService.remove(bookCategoryEntity);
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
    public void duplicate(final BookCategoryTO bookCategory) {
        bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
        try {
            final BookCategory oldBookCategory = bookCategoryService.getBookCategory(bookCategory.getId());
            Validators.validateExists(oldBookCategory, BOOK_CATEGORY_TO_ARGUMENT);

            bookCategoryService.duplicate(oldBookCategory);
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
    public void moveUp(final BookCategoryTO bookCategory) {
        bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
        try {
            final BookCategory bookCategoryEntity = bookCategoryService.getBookCategory(bookCategory.getId());
            Validators.validateExists(bookCategoryEntity, BOOK_CATEGORY_TO_ARGUMENT);
            final List<BookCategory> bookCategories = bookCategoryService.getBookCategories();
            Validators.validateMoveUp(bookCategories, bookCategoryEntity, BOOK_CATEGORY_ARGUMENT);

            bookCategoryService.moveUp(bookCategoryEntity);
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
    public void moveDown(final BookCategoryTO bookCategory) {
        bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
        try {
            final BookCategory bookCategoryEntity = bookCategoryService.getBookCategory(bookCategory.getId());
            Validators.validateExists(bookCategoryEntity, BOOK_CATEGORY_TO_ARGUMENT);
            final List<BookCategory> bookCategories = bookCategoryService.getBookCategories();
            Validators.validateMoveDown(bookCategories, bookCategoryEntity, BOOK_CATEGORY_ARGUMENT);

            bookCategoryService.moveDown(bookCategoryEntity);
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
    public boolean exists(final BookCategoryTO bookCategory) {
        bookCategoryTOValidator.validateBookCategoryTOWithId(bookCategory);
        try {

            return bookCategoryService.exists(converter.convert(bookCategory, BookCategory.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            bookCategoryService.updatePositions();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getBooksCount() {
        try {
            return bookCategoryService.getBooksCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
