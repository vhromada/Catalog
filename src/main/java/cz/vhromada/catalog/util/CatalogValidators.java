package cz.vhromada.catalog.util;

import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.ValidationException;

/**
 * An utility class represents catalog validators.
 *
 * @author Vladimir Hromada
 */
public final class CatalogValidators {

    /**
     * Creates a new instance of CatalogValidators.
     */
    private CatalogValidators() {
    }

    /**
     * Validates if value is valid year (1940 - current year).
     *
     * @param value value to validate
     * @param name  name of value
     * @throws ValidationException if value isn't valid year
     */
    public static void validateYear(final int value, final String name) {
        Validators.validateRange(value, Constants.MIN_YEAR, Constants.CURRENT_YEAR, name);
    }

    /**
     * Validates if value is valid IMDB code (-1 or between 1 and 9999999).
     *
     * @param value value to validate
     * @param name  name of value
     * @throws ValidationException if value isn't IMDB code
     */
    public static void validateImdbCode(final int value, final String name) {
        if (value != -1 && (value < 1 || value > Constants.MAX_IMDB_CODE)) {
            throw new ValidationException(name + " must be between 1 and 9999999 or -1.");
        }
    }

    /**
     * Validates if year range is valid.
     *
     * @param startYear starting year of range to validate
     * @param endYear   ending year of range to validate
     * @throws ValidationException if starting year of range if greater than ending year of range
     */
    public static void validateYears(final int startYear, final int endYear) {
        if (startYear > endYear) {
            throw new ValidationException("Starting year mustn't be greater than ending year.");
        }
    }

}
