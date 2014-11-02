package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.validators.exceptions.ValidationException;

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
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID isn't null
	 *                                  or number of episode isn't positive number
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of episode is negative value
	 *                                  or note is null
	 *                                  or TO for season is null
	 *                                  or TO for season ID is null
	 */
	void validateNewEpisodeTO(EpisodeTO episode);

	/**
	 * Validates existing TO for episode.
	 *
	 * @param episode validating TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID is null
	 *                                  or number of episode isn't positive number
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of episode is negative value
	 *                                  or note is null
	 *                                  or TO for season is null
	 *                                  or TO for season ID is null
	 */
	void validateExistingEpisodeTO(EpisodeTO episode);

	/**
	 * Validates TO for episode with ID.
	 *
	 * @param episode validating TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID is null
	 */
	void validateEpisodeTOWithId(EpisodeTO episode);

}
