package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.validators.BookCategoryTOValidator;
import cz.vhromada.validators.Validators;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for book category.
 *
 * @author Vladimir Hromada
 */
@Component("bookCategoryTOValidator")
public class BookCategoryTOValidatorImpl implements BookCategoryTOValidator {

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     */
    @Override
    public void validateNewBookCategoryTO(final BookCategoryTO bookCategory) {
        validateBookCategoryTO(bookCategory);
        Validators.validateNull(bookCategory.getId(), "ID");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     */
    @Override
    public void validateExistingBookCategoryTO(final BookCategoryTO bookCategory) {
        validateBookCategoryTO(bookCategory);
        Validators.validateNotNull(bookCategory.getId(), "ID");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     */
    @Override
    public void validateBookCategoryTOWithId(final BookCategoryTO bookCategory) {
        Validators.validateArgumentNotNull(bookCategory, "TO for book category");
        Validators.validateNotNull(bookCategory.getId(), "ID");
    }

    /**
     * Validates TO for book category.
     *
     * @param bookCategory validating TO for book category
     * @throws IllegalArgumentException if TO for book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if name is null
     *                                  or name is empty string
     *                                  or note is null
     */
    private static void validateBookCategoryTO(final BookCategoryTO bookCategory) {
        Validators.validateArgumentNotNull(bookCategory, "TO for book category");
        Validators.validateNotNull(bookCategory.getName(), "Name");
        Validators.validateNotEmptyString(bookCategory.getName(), "Name");
        Validators.validateNotNull(bookCategory.getNote(), "Note");
    }

}
