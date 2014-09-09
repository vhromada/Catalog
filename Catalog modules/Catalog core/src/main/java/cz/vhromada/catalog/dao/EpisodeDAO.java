package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

/**
 * An interface represents DAO for episodes.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeDAO {

	/**
	 * Returns episode with ID or null if there isn't such episode.
	 *
	 * @param id ID
	 * @return episode with ID or null if there isn't such episode
	 * @throws IllegalArgumentException if ID is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	Episode getEpisode(Integer id);

	/**
	 * Adds episode. Sets new ID and position.
	 *
	 * @param episode episode
	 * @throws IllegalArgumentException if episode is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void add(Episode episode);

	/**
	 * Updates episode.
	 *
	 * @param episode episode
	 * @throws IllegalArgumentException if episode is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void update(Episode episode);

	/**
	 * Removes episode.
	 *
	 * @param episode episode
	 * @throws IllegalArgumentException if episode is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void remove(Episode episode);

	/**
	 * Returns list of episodes for specified season.
	 *
	 * @param season season
	 * @return list of episodes for specified season
	 * @throws IllegalArgumentException if season is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if season doesn't exist in data storage
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	List<Episode> findEpisodesBySeason(Season season);

}
