package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.validators.EpisodeTOValidator;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeTOValidator")
public class EpisodeTOValidatorImpl implements EpisodeTOValidator {

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateNewEpisodeTO(final EpisodeTO episode) {
		validateEpisodeTO(episode);
		Validators.validateNull(episode.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateExistingEpisodeTO(final EpisodeTO episode) {
		validateEpisodeTO(episode);
		Validators.validateNotNull(episode.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateEpisodeTOWithId(final EpisodeTO episode) {
		Validators.validateArgumentNotNull(episode, "TO for episode");
		Validators.validateNotNull(episode.getId(), "ID");
	}

	/**
	 * Validates TO for episode.
	 *
	 * @param episode validating TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if number of episode isn't positive number
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of episode is negative value
	 *                                  or note is null
	 *                                  or TO for season is null
	 *                                  or TO for season ID is null
	 */
	private void validateEpisodeTO(final EpisodeTO episode) {
		Validators.validateArgumentNotNull(episode, "TO for episode");
		Validators.validatePositiveNumber(episode.getNumber(), "Number of episode");
		Validators.validateNotNull(episode.getName(), "Name");
		Validators.validateNotEmptyString(episode.getName(), "Name");
		Validators.validateNotNegativeNumber(episode.getLength(), "Length");
		Validators.validateNotNull(episode.getNote(), "Note");
		Validators.validateNotNull(episode.getSeason(), "TO for season");
		Validators.validateNotNull(episode.getSeason().getId(), "TO for season ID");
	}

}
