package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.BookCategoryTO;

/**
 * An interface represents validator for TO for book category.
 *
 * @author Vladimir Hromada
 */
public interface BookCategoryTOValidator {

    /**
     * Validates new TO for book category.
     *
     * @param bookCategory validating TO for book category
     * @throws IllegalArgumentException if TO for book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or note is null
     */
    void validateNewBookCategoryTO(BookCategoryTO bookCategory);

    /**
     * Validates existing TO for book category.
     *
     * @param bookCategory validating TO for book category
     * @throws IllegalArgumentException if TO for book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or note is null
     */
    void validateExistingBookCategoryTO(BookCategoryTO bookCategory);

    /**
     * Validates TO for book category with ID.
     *
     * @param bookCategory validating TO for book category
     * @throws IllegalArgumentException if TO for book is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     */
    void validateBookCategoryTOWithId(BookCategoryTO bookCategory);

}
