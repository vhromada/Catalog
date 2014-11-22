package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.GameTO;

/**
 * An interface represents validator for TO for game.
 *
 * @author Vladimir Hromada
 */
public interface GameTOValidator {

    /**
     * Validates new TO for game.
     *
     * @param game validating TO for game
     * @throws IllegalArgumentException if TO for game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about game is null
     *                                  or URL to czech Wikipedia page about game is null
     *                                  or count of media isn't positive number
     *                                  or other data is null
     *                                  or note is null
     */
    void validateNewGameTO(GameTO game);

    /**
     * Validates existing TO for game.
     *
     * @param game validating TO for game
     * @throws IllegalArgumentException if TO for game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about game is null
     *                                  or URL to czech Wikipedia page about game is null
     *                                  or count of media isn't positive number
     *                                  or other data is null
     *                                  or note is null
     */
    void validateExistingGameTO(GameTO game);

    /**
     * Validates TO for game with ID.
     *
     * @param game validating TO for game
     * @throws IllegalArgumentException if TO for game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     */
    void validateGameTOWithId(GameTO game);

}
