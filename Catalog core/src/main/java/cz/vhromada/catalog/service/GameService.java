package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Game;

/**
 * An interface represents service for games.
 *
 * @author Vladimir Hromada
 */
public interface GameService {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void newData();

    /**
     * Returns list of games.
     *
     * @return list of games
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    List<Game> getGames();

    /**
     * Returns game with ID or null if there isn't such game.
     *
     * @param id ID
     * @return game with ID or null if there isn't such game
     * @throws IllegalArgumentException                                         if ID is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    Game getGame(Integer id);

    /**
     * Adds game. Sets new ID and position.
     *
     * @param game game
     * @throws IllegalArgumentException                                         if game is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void add(Game game);

    /**
     * Updates game.
     *
     * @param game new value of game
     * @throws IllegalArgumentException                                         if game is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void update(Game game);

    /**
     * Removes game.
     *
     * @param game game
     * @throws IllegalArgumentException                                         if game is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void remove(Game game);

    /**
     * Duplicates game.
     *
     * @param game game
     * @throws IllegalArgumentException                                         if game is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void duplicate(Game game);

    /**
     * Moves game in list one position up.
     *
     * @param game game
     * @throws IllegalArgumentException                                         if game is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void moveUp(Game game);

    /**
     * Moves game in list one position down.
     *
     * @param game game
     * @throws IllegalArgumentException                                         if game is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void moveDown(Game game);

    /**
     * Returns true if game exists.
     *
     * @param game game
     * @return true if game exists
     * @throws IllegalArgumentException                                         if game is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    boolean exists(Game game);

    /**
     * Updates positions.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void updatePositions();

    /**
     * Returns total count of media.
     *
     * @return total count of media
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    int getTotalMediaCount();

}
