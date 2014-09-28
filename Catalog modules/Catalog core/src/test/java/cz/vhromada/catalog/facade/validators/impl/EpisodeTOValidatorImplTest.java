package cz.vhromada.catalog.facade.validators.impl;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.validators.EpisodeTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link EpisodeTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class EpisodeTOValidatorImplTest {

	/** Instance of {@link EpisodeTOValidator} */
	private EpisodeTOValidator episodeTOValidator;

	/** Initializes validator for TO for episode. */
	@Before
	public void setUp() {
		episodeTOValidator = new EpisodeTOValidatorImpl();
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNewEpisodeTOWithNullArgument() {
		episodeTOValidator.validateNewEpisodeTO(null);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNotNullId() {
		episodeTOValidator.validateNewEpisodeTO(ToGenerator.createEpisode(ID, ToGenerator.createSeason(INNER_ID)));
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNotPositiveNumber() {
		final EpisodeTO episode = ToGenerator.createEpisode(ToGenerator.createSeason(INNER_ID));
		episode.setNumber(0);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNullName() {
		final EpisodeTO episode = ToGenerator.createEpisode(ToGenerator.createSeason(INNER_ID));
		episode.setName(null);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithEmptyName() {
		final EpisodeTO episode = ToGenerator.createEpisode(ToGenerator.createSeason(INNER_ID));
		episode.setName("");

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNegativeLength() {
		final EpisodeTO episode = ToGenerator.createEpisode(ToGenerator.createSeason(INNER_ID));
		episode.setLength(-1);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNullNote() {
		final EpisodeTO episode = ToGenerator.createEpisode(ToGenerator.createSeason(INNER_ID));
		episode.setNote(null);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null TO for season. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNullSeasonTO() {
		episodeTOValidator.validateNewEpisodeTO(ToGenerator.createEpisode());
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with TO for season with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithSeasonTOWithNullId() {
		episodeTOValidator.validateNewEpisodeTO(ToGenerator.createEpisode(ToGenerator.createSeason()));
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingEpisodeTOWithNullArgument() {
		episodeTOValidator.validateExistingEpisodeTO(null);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNullId() {
		episodeTOValidator.validateExistingEpisodeTO(ToGenerator.createEpisode(ToGenerator.createSeason(INNER_ID)));
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNotPositiveNumber() {
		final EpisodeTO episode = ToGenerator.createEpisode(ID, ToGenerator.createSeason(INNER_ID));
		episode.setNumber(0);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNullName() {
		final EpisodeTO episode = ToGenerator.createEpisode(ID, ToGenerator.createSeason(INNER_ID));
		episode.setName(null);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithEmptyName() {
		final EpisodeTO episode = ToGenerator.createEpisode(ID, ToGenerator.createSeason(INNER_ID));
		episode.setName("");

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNegativeLength() {
		final EpisodeTO episode = ToGenerator.createEpisode(ID, ToGenerator.createSeason(INNER_ID));
		episode.setLength(-1);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNullNote() {
		final EpisodeTO episode = ToGenerator.createEpisode(ID, ToGenerator.createSeason(INNER_ID));
		episode.setNote(null);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null TO for season. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNullSeasonTO() {
		episodeTOValidator.validateExistingEpisodeTO(ToGenerator.createEpisode(ID));
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with TO for season with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithSeasonTOWithNullId() {
		episodeTOValidator.validateExistingEpisodeTO(ToGenerator.createEpisode(ID, ToGenerator.createSeason()));
	}

	/** Test method for {@link EpisodeTOValidator#validateEpisodeTOWithId(EpisodeTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateEpisodeTOWithIdWithNullArgument() {
		episodeTOValidator.validateEpisodeTOWithId(null);
	}

	/** Test method for {@link EpisodeTOValidator#validateEpisodeTOWithId(EpisodeTO)} with TO for episode with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateEpisodeTOWithIdWithNullId() {
		episodeTOValidator.validateEpisodeTOWithId(ToGenerator.createEpisode());
	}

}
