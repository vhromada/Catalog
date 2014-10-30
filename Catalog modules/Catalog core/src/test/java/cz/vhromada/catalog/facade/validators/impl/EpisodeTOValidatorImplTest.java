package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
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
public class EpisodeTOValidatorImplTest extends ObjectGeneratorTest {

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
		episodeTOValidator.validateNewEpisodeTO(generate(EpisodeTO.class));
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNotPositiveNumber() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);
		episode.setNumber(0);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNullName() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);
		episode.setName(null);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithEmptyName() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);
		episode.setName("");

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNegativeLength() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);
		episode.setLength(-1);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNullNote() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);
		episode.setNote(null);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null TO for season. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithNullSeasonTO() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);
		episode.setSeason(null);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with TO for season with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewEpisodeTOWithSeasonTOWithNullId() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);
		episode.getSeason().setId(null);

		episodeTOValidator.validateNewEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingEpisodeTOWithNullArgument() {
		episodeTOValidator.validateExistingEpisodeTO(null);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNullId() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNotPositiveNumber() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setNumber(0);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNullName() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setName(null);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithEmptyName() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setName("");

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNegativeLength() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setLength(-1);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNullNote() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setNote(null);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null TO for season. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithNullSeasonTO() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setSeason(null);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with TO for season with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingEpisodeTOWithSeasonTOWithNullId() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.getSeason().setId(null);

		episodeTOValidator.validateExistingEpisodeTO(episode);
	}

	/** Test method for {@link EpisodeTOValidator#validateEpisodeTOWithId(EpisodeTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateEpisodeTOWithIdWithNullArgument() {
		episodeTOValidator.validateEpisodeTOWithId(null);
	}

	/** Test method for {@link EpisodeTOValidator#validateEpisodeTOWithId(EpisodeTO)} with TO for episode with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateEpisodeTOWithIdWithNullId() {
		final EpisodeTO episode = generate(EpisodeTO.class);
		episode.setId(null);

		episodeTOValidator.validateEpisodeTOWithId(episode);
	}

}
