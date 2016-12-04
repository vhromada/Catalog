package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Movie;

/**
 * An interface represents facade for movies.
 *
 * @author Vladimir Hromada
 */
public interface MovieFacade {

    /**
     * Creates new data.
     */
    void newData();

    /**
     * Returns movies.
     *
     * @return movies
     */
    List<Movie> getMovies();

    /**
     * Returns movie with ID or null if there isn't such movie.
     *
     * @param id ID
     * @return movie with ID or null if there isn't such movie
     * @throws IllegalArgumentException if ID is null
     */
    Movie getMovie(Integer id);

    /**
     * Adds movie. Sets new ID and position.
     *
     * @param movie movie
     * @throws IllegalArgumentException                                  if movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID isn't null
     *                                                                   or czech name is null
     *                                                                   or czech name is empty string
     *                                                                   or original name is null
     *                                                                   or original name is empty string
     *                                                                   or year isn't between 1940 and current year
     *                                                                   or language is null
     *                                                                   or subtitles are null
     *                                                                   or subtitles contain null value
     *                                                                   or media are null
     *                                                                   or media contain null value
     *                                                                   or media contain negative value
     *                                                                   or URL to ČSFD page about movie is null
     *                                                                   or IMDB code isn't -1 or between 1 and 9999999
     *                                                                   or URL to english Wikipedia page about movie is null
     *                                                                   or URL to czech Wikipedia page about movie is null
     *                                                                   or path to file with movie's picture is null
     *                                                                   or note is null
     *                                                                   or genres are null
     *                                                                   or genres contain null value
     *                                                                   or genre ID is null
     *                                                                   or genre name is null
     *                                                                   or genre name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if genre doesn't exist in data storage
     */
    void add(Movie movie);

    /**
     * Updates movie.
     *
     * @param movie new value of movie
     * @throws IllegalArgumentException                                  if movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or czech name is null
     *                                                                   or czech name is empty string
     *                                                                   or original name is null
     *                                                                   or original name is empty string
     *                                                                   or year isn't between 1940 and current year
     *                                                                   or language is null
     *                                                                   or subtitles are null
     *                                                                   or subtitles contain null value
     *                                                                   or media are null
     *                                                                   or media contain null value
     *                                                                   or media contain negative value
     *                                                                   or URL to ČSFD page about movie is null
     *                                                                   or IMDB code isn't -1 or between 1 and 9999999
     *                                                                   or URL to english Wikipedia page about movie is null
     *                                                                   or URL to czech Wikipedia page about movie is null
     *                                                                   or path to file with movie's picture is null
     *                                                                   or note is null
     *                                                                   or genres are null
     *                                                                   or genres contain null value
     *                                                                   or genre ID is null
     *                                                                   or genre name is null
     *                                                                   or genre name is empty string
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if movie doesn't exist in data storage
     *                                                                   or genre doesn't exist in data storage
     */
    void update(Movie movie);

    /**
     * Removes movie.
     *
     * @param movie movie
     * @throws IllegalArgumentException                                  if movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if movie doesn't exist in data storage
     */
    void remove(Movie movie);

    /**
     * Duplicates movie.
     *
     * @param movie movie
     * @throws IllegalArgumentException                                  if movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if movie doesn't exist in data storage
     */
    void duplicate(Movie movie);

    /**
     * Moves movie in list one position up.
     *
     * @param movie movie
     * @throws IllegalArgumentException                                  if movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or movie can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if movie doesn't exist in data storage
     */
    void moveUp(Movie movie);

    /**
     * Moves movie in list one position down.
     *
     * @param movie movie
     * @throws IllegalArgumentException                                  if movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or movie can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if movie doesn't exist in data storage
     */
    void moveDown(Movie movie);

    /**
     * Updates positions.
     */
    void updatePositions();

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    int getTotalMediaCount();

    /**
     * Returns total length of all movies.
     *
     * @return total length of all movies
     */
    Time getTotalLength();

}
