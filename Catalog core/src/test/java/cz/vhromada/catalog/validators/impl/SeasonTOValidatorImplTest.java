package cz.vhromada.catalog.validators.impl;

import cz.vhromada.catalog.common.CollectionUtils;
import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.validators.SeasonTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SeasonTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class SeasonTOValidatorImplTest {

    /**
     * Instance of {@link SeasonTOValidator}
     */
    private SeasonTOValidator seasonTOValidator;

    /**
     * Initializes validator for TO for season.
     */
    @Before
    public void setUp() {
        seasonTOValidator = new SeasonTOValidatorImpl();
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeasonTO_NullArgument() {
        seasonTOValidator.validateNewSeasonTO(null);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NotNullId() {
        seasonTOValidator.validateNewSeasonTO(SeasonUtils.newSeasonTO(1));
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not positive number of season.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NotPositiveNumber() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setNumber(0);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMinimumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMaximumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMinimumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMaximumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(season.getEndYear() + 1);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NullLanguage() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setLanguage(null);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTOWIthNullSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setSubtitles(null);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTOWIthBadSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NullNote() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setNote(null);

        seasonTOValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeasonTO_NullArgument() {
        seasonTOValidator.validateExistingSeasonTO(null);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullId() {
        seasonTOValidator.validateExistingSeasonTO(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with not positive number of season.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NotPositiveNumber() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setNumber(0);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMinimumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMaximumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMinimumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMaximumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(season.getEndYear() + 1);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullLanguage() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setLanguage(null);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTOWIthNullSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setSubtitles(null);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTOWIthBadSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullNote() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setNote(null);

        seasonTOValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateSeasonTOWithId(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSeasonTOWithId_NullArgument() {
        seasonTOValidator.validateSeasonTOWithId(null);
    }

    /**
     * Test method for {@link SeasonTOValidator#validateSeasonTOWithId(SeasonTO)} with TO for season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateSeasonTOWithId_NullId() {
        seasonTOValidator.validateSeasonTOWithId(SeasonUtils.newSeasonTO(null));
    }

}
