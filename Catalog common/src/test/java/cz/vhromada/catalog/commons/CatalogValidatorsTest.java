package cz.vhromada.catalog.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Test;

/**
 * A class represents test for class {@link CatalogValidators}.
 *
 * @author Vladimir Hromada
 */
public class CatalogValidatorsTest {

    /**
     * Name
     */
    private static final String NAME = "Name";

    /**
     * Year
     */
    private static final int YEAR = 2000;

    /**
     * Message for assertion failure
     */
    private static final String FAIL_MESSAGE = "Method didn't throw exception.";

    /**
     * Test method for {@link CatalogValidators#validateYear(int, String)}.
     */
    @Test
    public void testValidateYear() {
        CatalogValidators.validateYear(Constants.MIN_YEAR, NAME);
        CatalogValidators.validateYear(Constants.MIN_YEAR + 1, NAME);
        CatalogValidators.validateYear(Constants.CURRENT_YEAR - 1, NAME);
        CatalogValidators.validateYear(Constants.CURRENT_YEAR, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateYear(int, String)} with not valid data.
     */
    @Test
    public void testValidateYearWithNotValidData() {
        final String errorMessage = NAME + " must be between " + Constants.MIN_YEAR + " and " + Constants.CURRENT_YEAR + '.';
        try {
            CatalogValidators.validateYear(Constants.MIN_YEAR - 1, NAME);
            fail(FAIL_MESSAGE);
        } catch (final ValidationException ex) {
            assertEquals(errorMessage, ex.getMessage());
        }
        try {
            CatalogValidators.validateYear(Constants.CURRENT_YEAR + 1, NAME);
            fail(FAIL_MESSAGE);
        } catch (final ValidationException ex) {
            assertEquals(errorMessage, ex.getMessage());
        }
    }

    /**
     * Test method for {@link CatalogValidators#validateImdbCode(int, String)}.
     */
    @Test
    public void testValidateImdbCode() {
        CatalogValidators.validateImdbCode(-1, NAME);
        CatalogValidators.validateImdbCode(1, NAME);
        CatalogValidators.validateImdbCode(Constants.MAX_IMDB_CODE, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateImdbCode(int, String)} with not valid data.
     */
    @Test
    public void testValidateImdbCodeWithNotValidData() {
        final String errorMessage = NAME + " must be between 1 and 9999999 or -1.";
        try {
            CatalogValidators.validateImdbCode(-2, NAME);
            fail(FAIL_MESSAGE);
        } catch (final ValidationException ex) {
            assertEquals(errorMessage, ex.getMessage());
        }
        try {
            CatalogValidators.validateImdbCode(0, NAME);
            fail(FAIL_MESSAGE);
        } catch (final ValidationException ex) {
            assertEquals(errorMessage, ex.getMessage());
        }
        try {
            CatalogValidators.validateImdbCode(Constants.MAX_IMDB_CODE + 1, NAME);
            fail(FAIL_MESSAGE);
        } catch (final ValidationException ex) {
            assertEquals(errorMessage, ex.getMessage());
        }
    }

    /**
     * Test method for {@link CatalogValidators#validateYears(int, int)}.
     */
    @Test
    public void testValidateYears() {
        CatalogValidators.validateYears(YEAR, YEAR);
        CatalogValidators.validateYears(YEAR, YEAR + 1);
    }

    /**
     * Test method for {@link CatalogValidators#validateYears(int, int)} with not valid data.
     */
    @Test
    public void testValidateYearsWithNotValidData() {
        try {
            CatalogValidators.validateYears(YEAR + 1, YEAR);
            fail(FAIL_MESSAGE);
        } catch (final ValidationException ex) {
            assertEquals("Starting year mustn't be greater than ending year.", ex.getMessage());
        }
    }

}
