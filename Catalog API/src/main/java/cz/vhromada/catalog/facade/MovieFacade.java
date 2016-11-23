package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.MovieTO;

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
     * Returns list of TO for movie.
     *
     * @return list of TO for movie
     */
    List<MovieTO> getMovies();

    /**
     * Returns TO for movie with ID or null if there isn't such TO for movie.
     *
     * @param id ID
     * @return TO for movie with ID or null if there isn't such TO for movie
     * @throws IllegalArgumentException if ID is null
     */
    MovieTO getMovie(Integer id);

    /**
     * Adds TO for movie. Sets new ID and position.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException                                  if TO for movie is null
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
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for genre doesn't exist in data storage
     */
    void add(MovieTO movie);

    /**
     * Updates TO for movie.
     *
     * @param movie new value of TO for movie
     * @throws IllegalArgumentException                                  if TO for movie is null
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
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for movie doesn't exist in data storage
     *                                                                   or TO for genre doesn't exist in data storage
     */
    void update(MovieTO movie);

    /**
     * Removes TO for movie.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException                                  if TO for movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for movie doesn't exist in data storage
     */
    void remove(MovieTO movie);

    /**
     * Duplicates TO for movie.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException                                  if TO for movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for movie doesn't exist in data storage
     */
    void duplicate(MovieTO movie);

    /**
     * Moves TO for movie in list one position up.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException                                  if TO for movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or TO for movie can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for movie doesn't exist in data storage
     */
    void moveUp(MovieTO movie);

    /**
     * Moves TO for movie in list one position down.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException                                  if TO for movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException     if ID is null
     *                                                                   or TO for movie can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException if TO for movie doesn't exist in data storage
     */
    void moveDown(MovieTO movie);

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
