//package cz.vhromada.catalog.facade.validators.impl;
//
//import cz.vhromada.catalog.commons.CollectionUtils;
//import cz.vhromada.catalog.commons.Language;
//import cz.vhromada.catalog.commons.ObjectGeneratorTest;
//import cz.vhromada.catalog.commons.TestConstants;
//import cz.vhromada.catalog.commons.ToGenerator;
//import cz.vhromada.catalog.facade.to.SeasonTO;
//import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
//import cz.vhromada.validators.exceptions.ValidationException;
//
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * A class represents test for class {@link SeasonTOValidatorImpl}.
// *
// * @author Vladimir Hromada
// */
//public class SeasonTOValidatorImplTest extends ObjectGeneratorTest {
//
//    /**
//     * Instance of {@link SeasonTOValidator}
//     */
//    private SeasonTOValidator seasonTOValidator;
//
//    /**
//     * Initializes validator for TO for season.
//     */
//    @Before
//    public void setUp() {
//        seasonTOValidator = new SeasonTOValidatorImpl();
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testValidateNewSeasonTOWithNullArgument() {
//        seasonTOValidator.validateNewSeasonTO(null);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithNotNullId() {
//        seasonTOValidator.validateNewSeasonTO(ToGenerator.newSeasonWithId(getObjectGenerator()));
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not positive number of season.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithNotPositiveNumber() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setNumber(0);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum starting year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithBadMinimumStartYear() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setStartYear(TestConstants.BAD_MIN_YEAR);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum starting year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithBadMaximumStartYear() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setStartYear(TestConstants.BAD_MAX_YEAR);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum ending year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithBadMinimumEndYear() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setEndYear(TestConstants.BAD_MIN_YEAR);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum ending year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithBadMaximumEndYear() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setEndYear(TestConstants.BAD_MAX_YEAR);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithBadYear() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setStartYear(season.getEndYear() + 1);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null language.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithNullLanguage() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setLanguage(null);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null subtitles.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWIthNullSubtitles() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setSubtitles(null);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with subtitles with null value.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWIthBadSubtitles() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setSubtitles(CollectionUtils.newList(generate(Language.class), null));
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null note.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithNullNote() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setNote(null);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null TO for show.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithNullShowTO() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.setShow(null);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with TO for show with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateNewSeasonTOWithShowTOWithNullId() {
//        final SeasonTO season = ToGenerator.newSeason(getObjectGenerator());
//        season.getShow().setId(null);
//
//        seasonTOValidator.validateNewSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testValidateExistingSeasonTOWithNullArgument() {
//        seasonTOValidator.validateExistingSeasonTO(null);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithNullId() {
//        seasonTOValidator.validateExistingSeasonTO(ToGenerator.newSeason(getObjectGenerator()));
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with not positive number of season.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithNotPositiveNumber() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setNumber(0);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum starting year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithBadMinimumStartYear() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setStartYear(TestConstants.BAD_MIN_YEAR);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum starting year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithBadMaximumStartYear() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setStartYear(TestConstants.BAD_MAX_YEAR);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum ending year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithBadMinimumEndYear() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setEndYear(TestConstants.BAD_MIN_YEAR);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum ending year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithBadMaximumEndYear() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setEndYear(TestConstants.BAD_MAX_YEAR);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithBadYear() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setStartYear(season.getEndYear() + 1);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null language.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithNullLanguage() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setLanguage(null);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null subtitles.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWIthNullSubtitles() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setSubtitles(null);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with subtitles with null value.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWIthBadSubtitles() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setSubtitles(CollectionUtils.newList(generate(Language.class), null));
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null note.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithNullNote() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setNote(null);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null TO for show.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithNullShowTO() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.setShow(null);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with TO for show with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateExistingSeasonTOWithShowTOWithNullId() {
//        final SeasonTO season = ToGenerator.newSeasonWithId(getObjectGenerator());
//        season.getShow().setId(null);
//
//        seasonTOValidator.validateExistingSeasonTO(season);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateSeasonTOWithId(SeasonTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testValidateSeasonTOWithIdWithNullArgument() {
//        seasonTOValidator.validateSeasonTOWithId(null);
//    }
//
//    /**
//     * Test method for {@link SeasonTOValidator#validateSeasonTOWithId(SeasonTO)} with TO for season with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testValidateSeasonTOWithIdWithNullId() {
//        seasonTOValidator.validateSeasonTOWithId(ToGenerator.newSeason(getObjectGenerator()));
//    }
//
//}
