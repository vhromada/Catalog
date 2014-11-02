package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;

/**
 * An interface represents service for movies.
 *
 * @author Vladimir Hromada
 */
public interface MovieService {

	/**
	 * Creates new data.
	 *
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void newData();

	/**
	 * Returns list of movies.
	 *
	 * @return list of movies
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	List<Movie> getMovies();

	/**
	 * Returns movie with ID or null if there isn't such movie.
	 *
	 * @param id ID
	 * @return movie with ID or null if there isn't such movie
	 * @throws IllegalArgumentException  if ID is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	Movie getMovie(Integer id);

	/**
	 * Adds movie. Sets new ID and position.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException  if movie is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void add(Movie movie);

	/**
	 * Updates movie.
	 *
	 * @param movie new value of movie
	 * @throws IllegalArgumentException  if movie is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void update(Movie movie);

	/**
	 * Removes movie.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException  if movie is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void remove(Movie movie);

	/**
	 * Duplicates movie.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException  if movie is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void duplicate(Movie movie);

	/**
	 * Moves movie in list one position up.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException  if movie is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void moveUp(Movie movie);

	/**
	 * Moves movie in list one position down.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException  if movie is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void moveDown(Movie movie);

	/**
	 * Returns true if movie exists.
	 *
	 * @param movie movie
	 * @return true if movie exists
	 * @throws IllegalArgumentException  if movie is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	boolean exists(Movie movie);

	/**
	 * Updates positions.
	 *
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void updatePositions();

	/**
	 * Returns total count of media.
	 *
	 * @return total count of media
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	int getTotalMediaCount();

	/**
	 * Returns total length of all movies.
	 *
	 * @return total length of all movies
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	Time getTotalLength();

}
