package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;

/**
 * An interface represents facade for series.
 *
 * @author Vladimir Hromada
 */
public interface SeasonFacade {

	/**
	 * Returns TO for season with ID or null if there isn't such TO for season.
	 *
	 * @param id ID
	 * @return TO for season with ID or null if there isn't such TO for season
	 * @throws IllegalArgumentException if ID is null
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	SeasonTO getSeason(Integer id);

	/**
	 * Adds TO for season. Sets new ID and position.
	 *
	 * @param season TO for season
	 * @throws IllegalArgumentException if TO for season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID isn't null
	 *                                  or number of season isn't positive number
	 *                                  or starting year isn't between 1940 and current year
	 *                                  or ending year isn't between 1940 and current year
	 *                                  or starting year is greater than ending year
	 *                                  or language is null
	 *                                  or subtitles are null
	 *                                  or subtitles contain null value
	 *                                  or count of episodes is negative number
	 *                                  or total length of episodes is null
	 *                                  or total length of episodes is negative number
	 *                                  or note is null
	 *                                  or TO for serie is null
	 *                                  or TO for serie ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for serie doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void add(SeasonTO season);

	/**
	 * Updates TO for season.
	 *
	 * @param season new value of TO for season
	 * @throws IllegalArgumentException if TO for season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or number of season isn't positive number
	 *                                  or starting year isn't between 1940 and current year
	 *                                  or ending year isn't between 1940 and current year
	 *                                  or starting year is greater than ending year
	 *                                  or language is null
	 *                                  or subtitles are null
	 *                                  or subtitles contain null value
	 *                                  or count of episodes is negative number
	 *                                  or total length of episodes is null
	 *                                  or total length of episodes is negative number
	 *                                  or note is null
	 *                                  or TO for serie is null
	 *                                  or TO for serie ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for season doesn't exist in data storage
	 *                                  or TO for serie doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void update(SeasonTO season);

	/**
	 * Removes TO for season.
	 *
	 * @param season TO for season
	 * @throws IllegalArgumentException if TO for season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for season doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void remove(SeasonTO season);

	/**
	 * Duplicates TO for season.
	 *
	 * @param season TO for season
	 * @throws IllegalArgumentException if TO for season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for season doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void duplicate(SeasonTO season);

	/**
	 * Moves TO for season in list one position up.
	 *
	 * @param season TO for season
	 * @throws IllegalArgumentException if TO for season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or TO for season can't be moved up
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for season doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void moveUp(SeasonTO season);

	/**
	 * Moves TO for season in list one position down.
	 *
	 * @param season TO for season
	 * @throws IllegalArgumentException if TO for season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or TO for season can't be moved down
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for season doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void moveDown(SeasonTO season);

	/**
	 * Returns true if TO for season exists.
	 *
	 * @param season TO for season
	 * @return true if TO for season exists
	 * @throws IllegalArgumentException if TO for season is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	boolean exists(SeasonTO season);

	/**
	 * Returns list of TO for season for specified TO for serie.
	 *
	 * @param serie TO for serie
	 * @return list of TO for seasons for specified TO for serie
	 * @throws IllegalArgumentException if TO for serie is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for serie doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	List<SeasonTO> findSeasonsBySerie(SerieTO serie);

}
