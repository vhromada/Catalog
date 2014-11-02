package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.facade.validators.GameTOValidator;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for game.
 *
 * @author Vladimir Hromada
 */
@Component("gameTOValidator")
public class GameTOValidatorImpl implements GameTOValidator {

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateNewGameTO(final GameTO game) {
		validateGameTO(game);
		Validators.validateNull(game.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateExistingGameTO(final GameTO game) {
		validateGameTO(game);
		Validators.validateNotNull(game.getId(), "ID");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 */
	@Override
	public void validateGameTOWithId(final GameTO game) {
		Validators.validateArgumentNotNull(game, "TO for game");
		Validators.validateNotNull(game.getId(), "ID");
	}

	/**
	 * Validates TO for game.
	 *
	 * @param game TO for game
	 * @throws IllegalArgumentException if TO for game is null
	 * @throws ValidationException      if name is null
	 *                                  or name is empty string
	 *                                  or URL to english Wikipedia page about game is null
	 *                                  or URL to czech Wikipedia page about game is null
	 *                                  or count of media isn't positive number
	 *                                  or other data is null
	 *                                  or note is null
	 */
	private void validateGameTO(final GameTO game) {
		Validators.validateArgumentNotNull(game, "TO for game");
		Validators.validateNotNull(game.getName(), "Name");
		Validators.validateNotEmptyString(game.getName(), "Name");
		Validators.validateNotNull(game.getWikiEn(), "URL to english Wikipedia page about game");
		Validators.validateNotNull(game.getWikiCz(), "URL to czech Wikipedia page about game");
		Validators.validatePositiveNumber(game.getMediaCount(), "Count of media");
		Validators.validateNotNull(game.getOtherData(), "Other data");
		Validators.validateNotNull(game.getNote(), "Note");
	}

}
