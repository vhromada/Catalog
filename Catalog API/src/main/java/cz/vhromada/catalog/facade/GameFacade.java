package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.facade.to.GameTO;

/**
 * An interface represents facade for games.
 *
 * @author Vladimir Hromada
 */
public interface GameFacade {

	/**
	 * Creates new data.
	 *
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *          if there was error in working with service tier
	 */
	void newData();

	/**
	 * Returns list of TO for game.
	 *
	 * @return list of TO for game
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *          if there was error in working with service tier
	 */
	List<GameTO> getGames();

	/**
	 * Returns TO for game with ID or null if there isn't such TO for game.
	 *
	 * @param id ID
	 * @return TO for game with ID or null if there isn't such TO for game
	 * @throws IllegalArgumentException if ID is null
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	GameTO getGame(Integer id);

	/**
	 * Adds TO for game. Sets new ID and position.
	 *
	 * @param game TO for game
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
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void add(GameTO game);

	/**
	 * Updates TO for game.
	 *
	 * @param game new value of TO for game
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
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for game doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void update(GameTO game);

	/**
	 * Removes TO for game.
	 *
	 * @param game TO for game
	 * @throws IllegalArgumentException if TO for game is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for game doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void remove(GameTO game);

	/**
	 * Duplicates TO for game.
	 *
	 * @param game TO for game
	 * @throws IllegalArgumentException if TO for game is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for game doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void duplicate(GameTO game);

	/**
	 * Moves TO for game in list one position up.
	 *
	 * @param game TO for game
	 * @throws IllegalArgumentException if TO for game is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or TO for game can't be moved up
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for game doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void moveUp(GameTO game);

	/**
	 * Moves TO for game in list one position down.
	 *
	 * @param game TO for game
	 * @throws IllegalArgumentException if TO for game is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or TO for game can't be moved down
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for game doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void moveDown(GameTO game);

	/**
	 * Returns true if TO for game exists.
	 *
	 * @param game TO for game
	 * @return true if TO for game exists
	 * @throws IllegalArgumentException if TO for game is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	boolean exists(GameTO game);

	/**
	 * Updates positions.
	 *
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *          if there was error in working with service tier
	 */
	void updatePositions();

	/**
	 * Returns total count of media.
	 *
	 * @return total count of media
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *          if there was error in working with service tier
	 */
	int getTotalMediaCount();

}
