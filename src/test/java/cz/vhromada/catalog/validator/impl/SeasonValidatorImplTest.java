package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.CollectionUtils;
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
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeasonTO_NullArgument() {
        seasonValidator.validateNewSeason(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NotNullId() {
        seasonValidator.validateNewSeason(SeasonUtils.newSeasonTO(1));
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not positive number of season.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NotPositiveNumber() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setNumber(0);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMinimumStartYear() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMaximumStartYear() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMinimumEndYear() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadMaximumEndYear() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_BadYear() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(season.getEndYear() + 1);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NullLanguage() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setLanguage(null);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTOWIthNullSubtitles() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setSubtitles(null);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTOWIthBadSubtitles() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewSeasonTO_NullNote() {
        final Season season = SeasonUtils.newSeasonTO(null);
        season.setNote(null);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeasonTO_NullArgument() {
        seasonValidator.validateExistingSeason(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullId() {
        seasonValidator.validateExistingSeason(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with not positive number of season.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NotPositiveNumber() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setNumber(0);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMinimumStartYear() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMaximumStartYear() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMinimumEndYear() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadMaximumEndYear() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_BadYear() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(season.getEndYear() + 1);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullLanguage() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setLanguage(null);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTOWIthNullSubtitles() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setSubtitles(null);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTOWIthBadSubtitles() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingSeasonTO_NullNote() {
        final Season season = SeasonUtils.newSeasonTO(1);
        season.setNote(null);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateSeasonWithId(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSeasonTOWithId_NullArgument() {
        seasonValidator.validateSeasonWithId(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateSeasonTOWithId(SeasonTO)} with TO for season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateSeasonTOWithId_NullId() {
        seasonValidator.validateSeasonWithId(SeasonUtils.newSeasonTO(null));
    }

}
