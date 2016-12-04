package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.validator.EpisodeValidator;

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
     * Initializes validator for episode.
     */
    @Before
    public void setUp() {
        episodeValidator = new EpisodeValidatorImpl();
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisode(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisode_NullArgument() {
        episodeValidator.validateNewEpisode(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisode(Episode)} with episode with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisode_NotNullId() {
        episodeValidator.validateNewEpisode(EpisodeUtils.newEpisode(1));
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisode(Episode)} with episode with not positive number of episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisode_NotPositiveNumber() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setNumber(0);

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisode(Episode)} with episode with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisode_NullName() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setName(null);

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisode(Episode)} with episode with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisode_EmptyName() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setName("");

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisode(Episode)} with episode with negative length of episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisode_NegativeLength() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setLength(-1);

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateNewEpisode(Episode)} with episode with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewEpisode_NullNote() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setNote(null);

        episodeValidator.validateNewEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisode(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisode_NullArgument() {
        episodeValidator.validateExistingEpisode(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisode(Episode)} with episode with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisode_NullId() {
        episodeValidator.validateExistingEpisode(EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisode(Episode)} with episode with not positive number of episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisode_NotPositiveNumber() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setNumber(0);

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisode(Episode)} with episode with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisode_NullName() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setName(null);

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisode(Episode)} with episode with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisode_EmptyName() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setName("");

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisode(Episode)} with episode with negative length of episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisode_NegativeLength() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setLength(-1);

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateExistingEpisode(Episode)} with episode with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingEpisode_NullNote() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setNote(null);

        episodeValidator.validateExistingEpisode(episode);
    }

    /**
     * Test method for {@link EpisodeValidator#validateEpisodeWithId(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateEpisodeWithId_NullArgument() {
        episodeValidator.validateEpisodeWithId(null);
    }

    /**
     * Test method for {@link EpisodeValidator#validateEpisodeWithId(Episode)} with episode with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateEpisodeWithId_NullId() {
        episodeValidator.validateEpisodeWithId(EpisodeUtils.newEpisode(null));
    }

}
