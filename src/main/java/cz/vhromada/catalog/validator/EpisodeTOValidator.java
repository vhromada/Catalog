package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.EpisodeTO;

/**
 * An interface represents validator for TO for episode.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeTOValidator {

    /**
     * Validates new TO for episode.
     *
     * @param episode validating TO for episode
     * @throws IllegalArgumentException                              if TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or number of episode isn't positive number
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or length of episode is negative value
     *                                                               or note is null
     */
    void validateNewEpisodeTO(EpisodeTO episode);

    /**
     * Validates existing TO for episode.
     *
     * @param episode validating TO for episode
     * @throws IllegalArgumentException                              if TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     *                                                               or number of episode isn't positive number
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or length of episode is negative value
     *                                                               or note is null
     */
    void validateExistingEpisodeTO(EpisodeTO episode);

    /**
     * Validates TO for episode with ID.
     *
     * @param episode validating TO for episode
     * @throws IllegalArgumentException                              if TO for episode is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    void validateEpisodeTOWithId(EpisodeTO episode);

}
