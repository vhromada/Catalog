package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Game;

/**
 * An interface represents validator for game.
 *
 * @author Vladimir Hromada
 */
public interface GameValidator {

    /**
     * Validates new game.
     *
     * @param game validating game
     * @throws IllegalArgumentException if game is null
     *                                  or ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about game is null
     *                                  or URL to czech Wikipedia page about game is null
     *                                  or count of media isn't positive number
     *                                  or other data is null
     *                                  or note is null
     */
    void validateNewGame(Game game);

    /**
     * Validates existing game.
     *
     * @param game validating game
     * @throws IllegalArgumentException if game is null
     *                                  or ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about game is null
     *                                  or URL to czech Wikipedia page about game is null
     *                                  or count of media isn't positive number
     *                                  or other data is null
     *                                  or note is null
     */
    void validateExistingGame(Game game);

    /**
     * Validates game with ID.
     *
     * @param game validating game
     * @throws IllegalArgumentException if game is null
     *                                  or ID is null
     */
    void validateGameWith(Game game);

}
