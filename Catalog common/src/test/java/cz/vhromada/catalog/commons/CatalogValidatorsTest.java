package cz.vhromada.catalog.commons;

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
     * Test method for {@link CatalogValidators#validateYear(int, String)} with min year.
     */
    @Test
    public void testValidateYear_MinYear() {
        CatalogValidators.validateYear(Constants.MIN_YEAR, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateYear(int, String)} with current year.
     */
    @Test
    public void testValidateYear_CurrentYear() {
        CatalogValidators.validateYear(Constants.MIN_YEAR, NAME);
        CatalogValidators.validateYear(Constants.CURRENT_YEAR, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateYear(int, String)} with valid year.
     */
    @Test
    public void testValidateYear_ValidYear() {
        CatalogValidators.validateYear(Constants.MIN_YEAR + 1, NAME);
        CatalogValidators.validateYear(Constants.CURRENT_YEAR - 1, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateYear(int, String)} with year before min year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateYear_YearBeforeMinYear() {
        CatalogValidators.validateYear(Constants.MIN_YEAR - 1, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateYear(int, String)} with future year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateYear_FutureYear() {
        CatalogValidators.validateYear(Constants.CURRENT_YEAR + 1, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateImdbCode(int, String)} with -1.
     */
    @Test
    public void testValidateImdbCode_MinusOne() {
        CatalogValidators.validateImdbCode(-1, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateImdbCode(int, String)} with 1.
     */
    @Test
    public void testValidateImdbCode_One() {
        CatalogValidators.validateImdbCode(1, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateImdbCode(int, String)} with max value.
     */
    @Test
    public void testValidateImdbCode_MaxValue() {
        CatalogValidators.validateImdbCode(Constants.MAX_IMDB_CODE, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateImdbCode(int, String)} with bad negative value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateImdbCode_BadNegativeValue() {
        CatalogValidators.validateImdbCode(-2, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateImdbCode(int, String)} with 0.
     */
    @Test(expected = ValidationException.class)
    public void testValidateImdbCode_Zeri() {
        CatalogValidators.validateImdbCode(0, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateImdbCode(int, String)} with value greater than max.
     */
    @Test(expected = ValidationException.class)
    public void testValidateImdbCode_GreaterThanMax() {
        CatalogValidators.validateImdbCode(Constants.MAX_IMDB_CODE + 1, NAME);
    }

    /**
     * Test method for {@link CatalogValidators#validateYears(int, int)} with save values.
     */
    @Test
    public void testValidateYears_SameValues() {
        CatalogValidators.validateYears(YEAR, YEAR);
    }

    /**
     * Test method for {@link CatalogValidators#validateYears(int, int)} with different value.
     */
    @Test
    public void testValidateYears_DifferentValues() {
        CatalogValidators.validateYears(YEAR, YEAR + 1);
    }

    /**
     * Test method for {@link CatalogValidators#validateYears(int, int)} with min year greater than max year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateYears_MinGreaterMax() {
        CatalogValidators.validateYears(YEAR + 1, YEAR);
    }

}
