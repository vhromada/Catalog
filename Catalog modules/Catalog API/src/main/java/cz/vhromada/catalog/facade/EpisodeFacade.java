package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

/**
 * An interface represents facade for episodes.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeFacade {

	/**
	 * Returns TO for episode with ID or null if there isn't such TO for episode.
	 *
	 * @param id ID
	 * @return TO for episode with ID or null if there isn't such TO for episode
	 * @throws IllegalArgumentException if ID is null
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	EpisodeTO getEpisode(Integer id);

	/**
	 * Adds TO for episode. Sets new ID and position.
	 *
	 * @param episode TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID isn't null
	 *                                  or number of episode isn't positive number
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of episode is negative value
	 *                                  or note is null
	 *                                  or TO for season is null
	 *                                  or TO for season ID is null
	 * @throws RecordNotFoundException  TO for season doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void add(EpisodeTO episode);

	/**
	 * Updates TO for episode.
	 *
	 * @param episode new value of TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID is null
	 *                                  or number of episode isn't positive number
	 *                                  or name is null
	 *                                  or name is empty string
	 *                                  or length of episode is negative value
	 *                                  or note is null
	 *                                  or TO for season is null
	 *                                  or TO for season ID is null
	 * @throws RecordNotFoundException  if TO for episode doesn't exist in data storage
	 *                                  or TO for season doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void update(EpisodeTO episode);

	/**
	 * Removes TO for episode.
	 *
	 * @param episode TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if TO for episode doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void remove(EpisodeTO episode);

	/**
	 * Duplicates TO for episode.
	 *
	 * @param episode TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if TO for episode doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void duplicate(EpisodeTO episode);

	/**
	 * Moves TO for episode in list one position up.
	 *
	 * @param episode TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID is null
	 *                                  or TO for episode can't be moved up
	 * @throws RecordNotFoundException  if TO for episode doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void moveUp(EpisodeTO episode);

	/**
	 * Moves TO for episode in list one position down.
	 *
	 * @param episode TO for episode
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID is null
	 *                                  or TO for episode can't be moved down
	 * @throws RecordNotFoundException  if TO for episode doesn't exist in data storage
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	void moveDown(EpisodeTO episode);

	/**
	 * Returns true if TO for episode exists.
	 *
	 * @param episode TO for episode
	 * @return true if TO for episode exists
	 * @throws IllegalArgumentException if TO for episode is null
	 * @throws ValidationException      if ID is null
	 * @throws FacadeOperationException if there was error in working with service tier
	 */
	boolean exists(EpisodeTO episode);

	/**
	 * Returns list of TO for episode for specified TO for season.
	 *
	 * @param season TO for season
	 * @return list of TO for episode for specified TO for season
	 * @throws IllegalArgumentException if TO for season is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if TO for season doesn't exist in data storage
	 * @throws FacadeOperationException if there was error with working with service tier
	 */
	List<EpisodeTO> findEpisodesBySeason(SeasonTO season);

}
