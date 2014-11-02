package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

/**
 * An interface represents DAO for games.
 *
 * @author Vladimir Hromada
 */
public interface GameDAO {

	/**
	 * Returns list of games.
	 *
	 * @return list of games
	 * @throws DataStorageException if there was error with working with data storage
	 */
	List<Game> getGames();

	/**
	 * Returns game with ID or null if there isn't such game.
	 *
	 * @param id ID
	 * @return game with ID or null if there isn't such game
	 * @throws IllegalArgumentException if ID is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	Game getGame(Integer id);

	/**
	 * Adds game. Sets new ID and position.
	 *
	 * @param game game
	 * @throws IllegalArgumentException if game is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void add(Game game);

	/**
	 * Updates game.
	 *
	 * @param game game
	 * @throws IllegalArgumentException if game is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void update(Game game);

	/**
	 * Removes game.
	 *
	 * @param game game
	 * @throws IllegalArgumentException if game is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void remove(Game game);

}
