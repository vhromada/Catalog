package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Movie;

/**
 * An interface represents validator for movie.
 *
 * @author Vladimir Hromada
 */
public interface MovieValidator {

    /**
     * Validates new movie.
     *
     * @param movie validating movie
     * @throws IllegalArgumentException if movie is null
     *                                  or ID isn't null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or year isn't between 1940 and current year
     *                                  or language is null
     *                                  or subtitles are null
     *                                  or subtitles contain null value
     *                                  or media are null
     *                                  or media contain null value
     *                                  or length of medium is negative value
     *                                  or URL to ČSFD page about movie is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about movie is null
     *                                  or URL to czech Wikipedia page about movie is null
     *                                  or path to file with movie's picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    void validateNewMovie(Movie movie);

    /**
     * Validates existing movie.
     *
     * @param movie validating movie
     * @throws IllegalArgumentException if movie is null
     *                                  or ID is null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or year isn't between 1940 and current year
     *                                  or language is null
     *                                  or subtitles are null
     *                                  or subtitles contain null value
     *                                  or media are null
     *                                  or media contain null value
     *                                  or length of medium is negative value
     *                                  or URL to ČSFD page about movie is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about movie is null
     *                                  or URL to czech Wikipedia page about movie is null
     *                                  or path to file with movie's picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    void validateExistingMovie(Movie movie);

    /**
     * Validates movie with ID.
     *
     * @param movie validating movie
     * @throws IllegalArgumentException if movie is null
     *                                  or ID is null
     */
    void validateMovieWithId(Movie movie);

}
