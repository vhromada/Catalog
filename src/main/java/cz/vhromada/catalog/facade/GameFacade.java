package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for games.
 *
 * @author Vladimir Hromada
 */
public interface GameFacade {

    /**
     * Creates new data.
     *
     * @return result
     */
    Result<Void> newData();

    /**
     * Returns games.
     *
     * @return result with list of games
     */
    Result<List<Game>> getAll();

    /**
     * Returns game with ID or null if there isn't such game.
     * <br>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * </ul>
     *
     * @param id ID
     * @return result with game or validation errors
     */
    Result<Game> get(Integer id);

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
     * @param game game
     * @return result with validation errors
     */
    Result<Void> add(Game game);

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
     * @param game new value of game
     * @return result with validation errors
     */
    Result<Void> update(Game game);

    /**
     * Removes game.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Game is null</li>
     * <li>ID is null</li>
     * <li>Game doesn't exist in data storage</li>
     *
     * @param game game
     * @return result with validation errors
     */
    Result<Void> remove(Game game);

    /**
     * Duplicates game.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Game is null</li>
     * <li>ID is null</li>
     * <li>Game doesn't exist in data storage</li>
     *
     * @param game game
     * @return result with validation errors
     */
    Result<Void> duplicate(Game game);

    /**
     * Moves game in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Game is null</li>
     * <li>ID is null</li>
     * <li>Game can't be moved up</li>
     * <li>Game doesn't exist in data storage</li>
     *
     * @param game game
     * @return result with validation errors
     */
    Result<Void> moveUp(Game game);

    /**
     * Moves game in list one position down.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Game is null</li>
     * <li>ID is null</li>
     * <li>Game can't be moved down</li>
     * <li>Game doesn't exist in data storage</li>
     *
     * @param game game
     * @return result with validation errors
     */
    Result<Void> moveDown(Game game);

    /**
     * Updates positions.
     *
     * @return result
     */
    Result<Void> updatePositions();

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    Result<Integer> getTotalMediaCount();

}
