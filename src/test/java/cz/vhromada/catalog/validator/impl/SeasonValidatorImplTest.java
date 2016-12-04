package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.TestConstants;
import cz.vhromada.catalog.validator.SeasonValidator;

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
     * Initializes validator for season.
     */
    @Before
    public void setUp() {
        seasonValidator = new SeasonValidatorImpl();
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_NullArgument() {
        seasonValidator.validateNewSeason(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_NotNullId() {
        seasonValidator.validateNewSeason(SeasonUtils.newSeason(1));
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with not positive number of season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_NotPositiveNumber() {
        final Season season = SeasonUtils.newSeason(null);
        season.setNumber(0);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with bad minimum starting year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_BadMinimumStartYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with bad maximum starting year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_BadMaximumStartYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with bad minimum ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_BadMinimumEndYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with bad maximum ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_BadMaximumEndYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with starting year greater than ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_BadYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(season.getEndYear() + 1);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with null language.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_NullLanguage() {
        final Season season = SeasonUtils.newSeason(null);
        season.setLanguage(null);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with null subtitles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeasonWIthNullSubtitles() {
        final Season season = SeasonUtils.newSeason(null);
        season.setSubtitles(null);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with subtitles with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeasonWIthBadSubtitles() {
        final Season season = SeasonUtils.newSeason(null);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateNewSeason(Season)} with season with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewSeason_NullNote() {
        final Season season = SeasonUtils.newSeason(null);
        season.setNote(null);

        seasonValidator.validateNewSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_NullArgument() {
        seasonValidator.validateExistingSeason(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_NullId() {
        seasonValidator.validateExistingSeason(SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with not positive number of season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_NotPositiveNumber() {
        final Season season = SeasonUtils.newSeason(1);
        season.setNumber(0);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with bad minimum starting year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_BadMinimumStartYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with bad maximum starting year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_BadMaximumStartYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with bad minimum ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_BadMinimumEndYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with bad maximum ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_BadMaximumEndYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with starting year greater than ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_BadYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(season.getEndYear() + 1);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with null language.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_NullLanguage() {
        final Season season = SeasonUtils.newSeason(1);
        season.setLanguage(null);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with null subtitles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeasonWIthNullSubtitles() {
        final Season season = SeasonUtils.newSeason(1);
        season.setSubtitles(null);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with subtitles with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeasonWIthBadSubtitles() {
        final Season season = SeasonUtils.newSeason(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateExistingSeason(Season)} with season with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingSeason_NullNote() {
        final Season season = SeasonUtils.newSeason(1);
        season.setNote(null);

        seasonValidator.validateExistingSeason(season);
    }

    /**
     * Test method for {@link SeasonValidator#validateSeasonWithId(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSeasonWithId_NullArgument() {
        seasonValidator.validateSeasonWithId(null);
    }

    /**
     * Test method for {@link SeasonValidator#validateSeasonWithId(Season)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSeasonWithId_NullId() {
        seasonValidator.validateSeasonWithId(SeasonUtils.newSeason(null));
    }

}
