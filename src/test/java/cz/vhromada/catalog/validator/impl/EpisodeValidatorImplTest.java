package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.EpisodeUtils;
import cz.vhromada.catalog.entity.Episode;
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
     * Test method for {@link EpisodeValidator#validateNewEpisode(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisodeTO_NullArgument() {
        episodeValidator.validateNewEpisode(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NotNullId() {
        episodeValidator.validateNewEpisode(EpisodeUtils.newEpisodeTO(1));
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NotPositiveNumber() {
        final Episode episode = EpisodeUtils.newEpisodeTO(null);
        episode.setNumber(0);

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NullName() {
        final Episode episode = EpisodeUtils.newEpisodeTO(null);
        episode.setName(null);

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_EmptyName() {
        final Episode episode = EpisodeUtils.newEpisodeTO(null);
        episode.setName("");

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NegativeLength() {
        final Episode episode = EpisodeUtils.newEpisodeTO(null);
        episode.setLength(-1);

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisodeTO(EpisodeTO)} with TO for episode with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewEpisodeTO_NullNote() {
        final Episode episode = EpisodeUtils.newEpisodeTO(null);
        episode.setNote(null);

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisode(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisodeTO_NullArgument() {
        episodeValidator.validateExistingEpisode(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullId() {
        episodeValidator.validateExistingEpisode(EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with not positive number of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NotPositiveNumber() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        episode.setNumber(0);

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullName() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        episode.setName(null);

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_EmptyName() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        episode.setName("");

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with negative length of episode.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NegativeLength() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        episode.setLength(-1);

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisodeTO(EpisodeTO)} with TO for episode with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingEpisodeTO_NullNote() {
        final Episode episode = EpisodeUtils.newEpisodeTO(1);
        episode.setNote(null);

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateEpisodeWithId(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateEpisodeTOWithId_NullArgument() {
        episodeValidator.validateEpisodeWithId(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateEpisodeTOWithId(EpisodeTO)} with TO for episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateEpisodeTOWithId_NullId() {
        episodeValidator.validateEpisodeWithId(EpisodeUtils.newEpisodeTO(null));
    }

}
