package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.SeasonValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SeasonValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class SeasonValidatorImplTest {

    /**
     * Instance of {@link SeasonValidator}
     */
    private SeasonValidator seasonValidator;

    /**
     * Initializes validator for TO for season.
     */
    @Before
    public void setUp() {
        seasonValidator = new SeasonValidatorImpl();
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeasonTO_NullArgument() {
        seasonValidator.validateNewSeasonTO(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NotNullId() {
        seasonValidator.validateNewSeasonTO(SeasonUtils.newSeasonTO(1));
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not positive number of season.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NotPositiveNumber() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setNumber(0);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMinimumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMaximumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMinimumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMaximumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(season.getEndYear() + 1);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NullLanguage() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setLanguage(null);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTOWIthNullSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setSubtitles(null);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTOWIthBadSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NullNote() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setNote(null);

        seasonValidator.validateNewSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeasonTO_NullArgument() {
        seasonValidator.validateExistingSeasonTO(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullId() {
        seasonValidator.validateExistingSeasonTO(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with not positive number of season.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NotPositiveNumber() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setNumber(0);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMinimumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMaximumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMinimumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMaximumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(season.getEndYear() + 1);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullLanguage() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setLanguage(null);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTOWIthNullSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setSubtitles(null);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTOWIthBadSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullNote() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setNote(null);

        seasonValidator.validateExistingSeasonTO(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateSeasonTOWithId(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSeasonTOWithId_NullArgument() {
        seasonValidator.validateSeasonTOWithId(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateSeasonTOWithId(SeasonTO)} with TO for season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateSeasonTOWithId_NullId() {
        seasonValidator.validateSeasonTOWithId(SeasonUtils.newSeasonTO(null));
    }

}
