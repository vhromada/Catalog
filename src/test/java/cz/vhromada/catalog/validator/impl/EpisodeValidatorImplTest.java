package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.EpisodeUtils;
import cz.vhromada.catalog.entity.EpisodeTO;
import cz.vhromada.catalog.validator.EpisodeValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link EpisodeValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class EpisodeValidatorImplTest {

    /**
     * Instance of {@link EpisodeValidator}
     */
    private EpisodeValidator episodeValidator;

    /**
     * Initializes validator for TO for episode.
     */
    @Before
    public void setUp() {
        episodeValidator = new EpisodeValidatorImpl();
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisodeTO_NullArgument() {
        episodeValidator.validateNewEpisodeTO(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NotNullId() {
        episodeValidator.validateNewEpisodeTO(EpisodeUtils.newEpisodeTO(1));
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NotPositiveNumber() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setNumber(0);

        episodeValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NullName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setName(null);

        episodeValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_EmptyName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setName("");

        episodeValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NegativeLength() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setLength(-1);

        episodeValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NullNote() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setNote(null);

        episodeValidator.validateNewEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisodeTO_NullArgument() {
        episodeValidator.validateExistingEpisodeTO(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullId() {
        episodeValidator.validateExistingEpisodeTO(EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NotPositiveNumber() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setNumber(0);

        episodeValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setName(null);

        episodeValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_EmptyName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setName("");

        episodeValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NegativeLength() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setLength(-1);

        episodeValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullNote() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setNote(null);

        episodeValidator.validateExistingEpisodeTO(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateEpisodeTOWithId(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateEpisodeTOWithId_NullArgument() {
        episodeValidator.validateEpisodeTOWithId(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateEpisodeTOWithId(EpisodeTO)} with TO for episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateEpisodeTOWithId_NullId() {
        episodeValidator.validateEpisodeTOWithId(EpisodeUtils.newEpisodeTO(null));
    }

}
