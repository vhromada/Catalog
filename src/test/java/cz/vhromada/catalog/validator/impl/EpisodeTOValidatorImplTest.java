package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.EpisodeUtils;
import cz.vhromada.catalog.entity.EpisodeTO;
import cz.vhromada.catalog.validator.EpisodeTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link EpisodeTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class EpisodeTOValidatorImplTest {

    /**
     * Instance of {@link EpisodeTOValidator}
     */
    private EpisodeTOValidator episodeTOValidator;

    /**
     * Initializes validator for TO for episode.
     */
    @Before
    public void setUp() {
        episodeTOValidator = new EpisodeTOValidatorImpl();
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisodeTO_NullArgument() {
        episodeTOValidator.validateNewEpisodeTO(null);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NotNullId() {
        episodeTOValidator.validateNewEpisodeTO(EpisodeUtils.newEpisodeTO(1));
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NotPositiveNumber() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setNumber(0);

        episodeTOValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NullName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setName(null);

        episodeTOValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_EmptyName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setName("");

        episodeTOValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NegativeLength() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setLength(-1);

        episodeTOValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NullNote() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setNote(null);

        episodeTOValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisodeTO_NullArgument() {
        episodeTOValidator.validateExistingEpisodeTO(null);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullId() {
        episodeTOValidator.validateExistingEpisodeTO(EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NotPositiveNumber() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setNumber(0);

        episodeTOValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setName(null);

        episodeTOValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_EmptyName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setName("");

        episodeTOValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NegativeLength() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setLength(-1);

        episodeTOValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullNote() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setNote(null);

        episodeTOValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateEpisodeTOWithId(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateEpisodeTOWithId_NullArgument() {
        episodeTOValidator.validateEpisodeTOWithId(null);
    }

    /**
     * Test method for {@link EpisodeTOValidator#validateEpisodeTOWithId(EpisodeTO)} with TO for episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateEpisodeTOWithId_NullId() {
        episodeTOValidator.validateEpisodeTOWithId(EpisodeUtils.newEpisodeTO(null));
    }

}
