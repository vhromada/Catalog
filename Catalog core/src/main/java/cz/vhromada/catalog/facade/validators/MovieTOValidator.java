package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.MovieTO;

/**
 * An interface represents validator for TO for movie.
 *
 * @author Vladimir Hromada
 */
public interface MovieTOValidator {

    /**
     * Validates new TO for movie.
     *
     * @param movie validating TO for movie
     * @throws IllegalArgumentException                              if TO for movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or czech name is null
     *                                                               or czech name is empty string
     *                                                               or original name is null
     *                                                               or original name is empty string
     *                                                               or year isn't between 1940 and current year
     *                                                               or language is null
     *                                                               or subtitles are null
     *                                                               or subtitles contain null value
     *                                                               or media are null
     *                                                               or media contain null value
     *                                                               or media contain negative value
     *                                                               or URL to ČSFD page about movie is null
     *                                                               or IMDB code isn't -1 or between 1 and 9999999
     *                                                               or URL to english Wikipedia page about movie is null
     *                                                               or URL to czech Wikipedia page about movie is null
     *                                                               or path to file with movie's picture is null
     *                                                               or note is null
     *                                                               or genres contain null value
     *                                                               or genre ID is null
     *                                                               or genre name is null
     *                                                               or genre name is empty string
     */
    void validateNewMovieTO(MovieTO movie);

    /**
     * Validates existing TO for movie.
     *
     * @param movie validating TO for movie
     * @throws IllegalArgumentException                              if TO for movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     *                                                               or czech name is null
     *                                                               or czech name is empty string
     *                                                               or original name is null
     *                                                               or original name is empty string
     *                                                               or year isn't between 1940 and current year
     *                                                               or language is null
     *                                                               or subtitles are null
     *                                                               or subtitles contain null value
     *                                                               or media are null
     *                                                               or media contain null value
     *                                                               or media contain negative value
     *                                                               or URL to ČSFD page about movie is null
     *                                                               or IMDB code isn't -1 or between 1 and 9999999
     *                                                               or URL to english Wikipedia page about movie is null
     *                                                               or URL to czech Wikipedia page about movie is null
     *                                                               or path to file with movie's picture is null
     *                                                               or note is null
     *                                                               or genres contain null value
     *                                                               or genre ID is null
     *                                                               or genre name is null
     *                                                               or genre name is empty string
     */
    void validateExistingMovieTO(MovieTO movie);

    /**
     * Validates TO for movie with ID.
     *
     * @param movie validating TO for movie
     * @throws IllegalArgumentException                              if TO for movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    void validateMovieTOWithId(MovieTO movie);

}
