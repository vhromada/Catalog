package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for games.
 *
 * @author Vladimir Hromada
 */
public interface GameFacade extends CatalogParentFacade<Game> {

    /**
     * Adds game. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Game is null</li>
     * <li>ID isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about game is null</li>
     * <li>URL to czech Wikipedia page about game is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data game
     * @return result with validation errors
     */
    @Override
    Result<Void> add(Game data);

    /**
     * Updates game.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Game is null</li>
     * <li>ID is null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about game is null</li>
     * <li>URL to czech Wikipedia page about game is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * <li>Game doesn't exist in data storage</li>
     * </ul>
     *
     * @param data new value of game
     * @return result with validation errors
     */
    @Override
    Result<Void> update(Game data);

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    Result<Integer> getTotalMediaCount();

}
