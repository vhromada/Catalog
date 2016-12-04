package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Episode;

/**
 * An interface represents validator for episode.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeValidator {

    /**
     * Validates new episode.
     *
     * @param episode validating episode
     * @throws IllegalArgumentException if episode is null
     *                                  or ID isn't null
     *                                  or number of episode isn't positive number
     *                                  or name is null
     *                                  or name is empty string
     *                                  or length of episode is negative value
     *                                  or note is null
     */
    void validateNewEpisode(Episode episode);

    /**
     * Validates existing episode.
     *
     * @param episode validating episode
     * @throws IllegalArgumentException if episode is null
     *                                  or ID is null
     *                                  or number of episode isn't positive number
     *                                  or name is null
     *                                  or name is empty string
     *                                  or length of episode is negative value
     *                                  or note is null
     */
    void validateExistingEpisode(Episode episode);

    /**
     * Validates episode with ID.
     *
     * @param episode validating episode
     * @throws IllegalArgumentException if episode is null
     *                                  or ID is null
     */
    void validateEpisodeWithId(Episode episode);

}
