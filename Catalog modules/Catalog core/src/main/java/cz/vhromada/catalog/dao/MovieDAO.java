package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

/**
 * An interface represents DAO for movies.
 *
 * @author Vladimir Hromada
 */
public interface MovieDAO {

	/**
	 * Returns list of movies.
	 *
	 * @return list of movies
	 * @throws DataStorageException if there was error with working with data storage
	 */
	List<Movie> getMovies();

	/**
	 * Returns movie with ID or null if there isn't such movie.
	 *
	 * @param id ID
	 * @return movie with ID or null if there isn't such movie
	 * @throws IllegalArgumentException if ID is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	Movie getMovie(Integer id);

	/**
	 * Adds movie. Sets new ID and position.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void add(Movie movie);

	/**
	 * Updates movie.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void update(Movie movie);

	/**
	 * Removes movie.
	 *
	 * @param movie movie
	 * @throws IllegalArgumentException if movie is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void remove(Movie movie);

}
