package cz.vhromada.catalog.facade.validators.impl;

import static cz.vhromada.catalog.common.TestConstants.BAD_MAX_YEAR;
import static cz.vhromada.catalog.common.TestConstants.BAD_MIN_YEAR;
import static cz.vhromada.catalog.common.TestConstants.BAD_SUBTITLES;
import static cz.vhromada.catalog.common.TestConstants.END_YEAR;
import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.NEGATIVE_TIME;
import static cz.vhromada.catalog.common.TestConstants.START_YEAR;

import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SeasonTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class SeasonTOValidatorImplTest {

	/** Instance of {@link SeasonTOValidator} */
	private SeasonTOValidator seasonTOValidator;

	/** Initializes validator for TO for season. */
	@Before
	public void setUp() {
		seasonTOValidator = new SeasonTOValidatorImpl();
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNewSeasonTOWithNullArgument() {
		seasonTOValidator.validateNewSeasonTO(null);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithNotNullId() {
		seasonTOValidator.validateNewSeasonTO(ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID)));
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with not positive number of season. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithNotPositiveNumber() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setNumber(0);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum starting year. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithBadMinimumStartYear() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setStartYear(BAD_MIN_YEAR);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum starting year. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithBadMaximumStartYear() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setStartYear(BAD_MAX_YEAR);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad minimum ending year. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithBadMinimumEndYear() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setEndYear(BAD_MIN_YEAR);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with bad maximum ending year. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithBadMaximumEndYear() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setEndYear(BAD_MAX_YEAR);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithBadYear() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setStartYear(END_YEAR);
		season.setEndYear(START_YEAR);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null language. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithNullLanguage() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setLanguage(null);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null subtitles. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWIthNullSubtitles() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setSubtitles(null);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with subtitles with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWIthBadSubtitles() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setSubtitles(BAD_SUBTITLES);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithNegativeEpisodesCount() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setEpisodesCount(-1);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null total length of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithNullTotalLength() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setTotalLength(null);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with negative total length of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithNegativeTotalLength() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setTotalLength(NEGATIVE_TIME);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithNullNote() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setNote(null);

		seasonTOValidator.validateNewSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with null TO for serie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithNullSerieTO() {
		seasonTOValidator.validateNewSeasonTO(ToGenerator.createSeason());
	}

	/** Test method for {@link SeasonTOValidator#validateNewSeasonTO(SeasonTO)} with TO for season with TO for serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSeasonTOWithSerieTOWithNullId() {
		seasonTOValidator.validateNewSeasonTO(ToGenerator.createSeason(ToGenerator.createSerie()));
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingSeasonTOWithNullArgument() {
		seasonTOValidator.validateExistingSeasonTO(null);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithNullId() {
		seasonTOValidator.validateExistingSeasonTO(ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID)));
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with not positive number of season. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithNotPositiveNumber() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setNumber(0);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum starting year. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithBadMinimumStartYear() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setStartYear(BAD_MIN_YEAR);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum starting year. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithBadMaximumStartYear() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setStartYear(BAD_MAX_YEAR);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad minimum ending year. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithBadMinimumEndYear() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setEndYear(BAD_MIN_YEAR);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with bad maximum ending year. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithBadMaximumEndYear() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setEndYear(BAD_MAX_YEAR);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with starting year greater than ending year. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithBadYear() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setStartYear(END_YEAR);
		season.setEndYear(START_YEAR);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null language. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithNullLanguage() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setLanguage(null);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null subtitles. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWIthNullSubtitles() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setSubtitles(null);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with subtitles with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWIthBadSubtitles() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setSubtitles(BAD_SUBTITLES);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithNegativeEpisodesCount() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setEpisodesCount(-1);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null total length of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithNullTotalLength() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setTotalLength(null);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with negative total length of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithNegativeTotalLength() {
		final SeasonTO season = ToGenerator.createSeason(ToGenerator.createSerie(INNER_ID));
		season.setTotalLength(NEGATIVE_TIME);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithNullNote() {
		final SeasonTO season = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		season.setNote(null);

		seasonTOValidator.validateExistingSeasonTO(season);
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with null TO for serie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithNullSerieTO() {
		seasonTOValidator.validateExistingSeasonTO(ToGenerator.createSeason(ID));
	}

	/** Test method for {@link SeasonTOValidator#validateExistingSeasonTO(SeasonTO)} with TO for season with TO for serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSeasonTOWithSerieTOWithNullId() {
		seasonTOValidator.validateExistingSeasonTO(ToGenerator.createSeason(ID, ToGenerator.createSerie()));
	}

	/** Test method for {@link SeasonTOValidator#validateSeasonTOWithId(SeasonTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateSeasonTOWithIdWithNullArgument() {
		seasonTOValidator.validateSeasonTOWithId(null);
	}

	/** Test method for {@link SeasonTOValidator#validateSeasonTOWithId(SeasonTO)} with TO for season with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateSeasonTOWithIdWithNullId() {
		seasonTOValidator.validateSeasonTOWithId(ToGenerator.createSeason());
	}

}
