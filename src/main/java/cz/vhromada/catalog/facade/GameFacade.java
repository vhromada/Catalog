package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.entity.Game;

/**
 * An interface represents facade for games.
 *
 * @author Vladimir Hromada
 */
public interface GameFacade {

    /**
     * Creates new data.
     */
    void newData();

    /**
     * Returns games.
     *
     * @return games
     */
    List<Game> getGames();

    /**
     * Returns game with ID or null if there isn't such game.
     *
     * @param id ID
     * @return game with ID or null if there isn't such game
     * @throws IllegalArgumentException if ID is null
     */
    Game getGame(Integer id);

    /**
     * Adds game. Sets new ID and position.
     *
     * @param game game
     * @throws IllegalArgumentException                              if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about game is null
     *                                                               or URL to czech Wikipedia page about game is null
     *                                                               or count of media isn't positive number
     *                                                               or other data is null
     *                                                               or note is null
     */
    void add(Game game);

    /**
     * Updates game.
     *
     * @param game new value of game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or name is null
     *                                                                   or name is empty string
     *                                                                   or URL to english Wikipedia page about game is null
     *                                                                   or URL to czech Wikipedia page about game is null
     *                                                                   or count of media isn't positive number
     *                                                                   or other data is null
     *                                                                   or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    void update(Game game);

    /**
     * Removes game.
     *
     * @param game game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    void remove(Game game);

    /**
     * Duplicates game.
     *
     * @param game game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    void duplicate(Game game);

    /**
     * Moves game in list one position up.
     *
     * @param game game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or game can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    void moveUp(Game game);

    /**
     * Moves game in list one position down.
     *
     * @param game game
     * @throws IllegalArgumentException                                  if game is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or game can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if game doesn't exist in data storage
     */
    void moveDown(Game game);

    /**
     * Updates positions.
     */
    void updatePositions();

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    int getTotalMediaCount();

}
