package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.facade.to.GenreTO;

/**
 * An interface represents facade for genres.
 *
 * @author Vladimir Hromada
 */
public interface GenreFacade {

	/**
	 * Creates new data.
	 *
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *          if there was error in working with service tier
	 */
	void newData();

	/**
	 * Returns list of TO for genre.
	 *
	 * @return list of TO for genre
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *          if there was error in working with service tier
	 */
	List<GenreTO> getGenres();

	/**
	 * Returns TO for genre with ID or null if there isn't such TO for genre.
	 *
	 * @param id ID
	 * @return TO for genre with ID or null if there isn't such TO for genre
	 * @throws IllegalArgumentException if ID is null
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	GenreTO getGenre(Integer id);

	/**
	 * Adds TO for genre. Sets new ID.
	 *
	 * @param genre TO for genre
	 * @throws IllegalArgumentException if TO for genre is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID isn't null
	 *                                  or name is null
	 *                                  or name is empty string
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void add(GenreTO genre);

	/**
	 * Adds list of genre names.
	 *
	 * @param genres list of genre names
	 * @throws IllegalArgumentException if list of genre names is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if list of genre names contains null value
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void add(List<String> genres);

	/**
	 * Updates TO for genre.
	 *
	 * @param genre new value of TO for genre
	 * @throws IllegalArgumentException if TO for genre is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 *                                  or name is null
	 *                                  or name is empty string
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for genre doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void update(GenreTO genre);

	/**
	 * Removes TO for genre.
	 *
	 * @param genre TO for genre
	 * @throws IllegalArgumentException if TO for genre is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for genre doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void remove(GenreTO genre);

	/**
	 * Duplicates TO for genre.
	 *
	 * @param genre TO for genre
	 * @throws IllegalArgumentException if TO for genre is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  if TO for genre doesn't exist in data storage
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	void duplicate(GenreTO genre);

	/**
	 * Returns true if TO for genre exists.
	 *
	 * @param genre TO for genre
	 * @return true if TO for genre exists
	 * @throws IllegalArgumentException if TO for genre is null
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  if ID is null
	 * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException
	 *                                  if there was error in working with service tier
	 */
	boolean exists(GenreTO genre);

}
