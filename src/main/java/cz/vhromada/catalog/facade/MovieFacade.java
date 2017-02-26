package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for movies.
 *
 * @author Vladimir Hromada
 */
public interface MovieFacade {

    /**
     * Creates new data.
     *
     * @return result
     */
    Result<Void> newData();

    /**
     * Returns movies.
     *
     * @return result with list of movies
     */
    Result<List<Movie>> getAll();

    /**
     * Returns movie with ID or null if there isn't such movie.
     * <br>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * </ul>
     *
     * @param id ID
     * @return result with movie or validation errors
     */
    Result<Movie> get(Integer id);

    /**
     * Adds movie. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Movie is null</li>
     * <li>ID isn't null</li>
     * <li>Czech name is null</li>
     * <li>Czech name is empty string</li>
     * <li>Original name is null</li>
     * <li>Original name is empty string</li>
     * <li>Year isn't between 1940 and current year</li>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * <li>Media are null</li>
     * <li>Media contain null value</li>
     * <li>Length of medium is negative value</li>
     * <li>URL to ČSFD page about movie is null</li>
     * <li>IMDB code isn't -1 or between 1 and 9999999</li>
     * <li>URL to english Wikipedia page about movie is null</li>
     * <li>URL to czech Wikipedia page about movie is null</li>
     * <li>Path to file with movie's picture is null</li>
     * <li>Note is null</li>
     * <li>Genres are null</li>
     * <li>Genres contain null value</li>
     * <li>Genre ID is null</li>
     * <li>Genre name is null</li>
     * <li>Genre name is empty string</li>
     * <li>Genre doesn't exist</li>
     * </ul>
     *
     * @param movie movie
     * @return result with validation errors
     */
    Result<Void> add(Movie movie);

    /**
     * Updates movie.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Movie is null</li>
     * <li>ID is null</li>
     * <li>Czech name is null</li>
     * <li>Czech name is empty string</li>
     * <li>Original name is null</li>
     * <li>Original name is empty string</li>
     * <li>Year isn't between 1940 and current year</li>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * <li>Media are null</li>
     * <li>Media contain null value</li>
     * <li>Length of medium is negative value</li>
     * <li>URL to ČSFD page about movie is null</li>
     * <li>IMDB code isn't -1 or between 1 and 9999999</li>
     * <li>URL to english Wikipedia page about movie is null</li>
     * <li>URL to czech Wikipedia page about movie is null</li>
     * <li>Path to file with movie's picture is null</li>
     * <li>Note is null</li>
     * <li>Genres are null</li>
     * <li>Genres contain null value</li>
     * <li>Genre ID is null</li>
     * <li>Genre name is null</li>
     * <li>Genre name is empty string</li>
     * <li>Genre doesn't exist</li>
     * <li>Movie doesn't exist in data storage</li>
     * </ul>
     *
     * @param movie new value of movie
     * @return result with validation errors
     */
    Result<Void> update(Movie movie);

    /**
     * Removes movie.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Movie is null</li>
     * <li>ID is null</li>
     * <li>Movie doesn't exist in data storage</li>
     *
     * @param movie movie
     * @return result with validation errors
     */
    Result<Void> remove(Movie movie);

    /**
     * Duplicates movie.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Movie is null</li>
     * <li>ID is null</li>
     * <li>Movie doesn't exist in data storage</li>
     *
     * @param movie movie
     * @return result with validation errors
     */
    Result<Void> duplicate(Movie movie);

    /**
     * Moves movie in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Movie is null</li>
     * <li>ID is null</li>
     * <li>Movie can't be moved up</li>
     * <li>Movie doesn't exist in data storage</li>
     *
     * @param movie movie
     * @return result with validation errors
     */
    Result<Void> moveUp(Movie movie);

    /**
     * Moves movie in list one position down.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Movie is null</li>
     * <li>ID is null</li>
     * <li>Movie can't be moved down</li>
     * <li>Movie doesn't exist in data storage</li>
     *
     * @param movie movie
     * @return result with validation errors
     */
    Result<Void> moveDown(Movie movie);

    /**
     * Updates positions.
     *
     * @return result
     */
    Result<Void> updatePositions();

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    Result<Integer> getTotalMediaCount();

    /**
     * Returns total length of all movies.
     *
     * @return result with total length of all movies
     */
    Result<Time> getTotalLength();

}
