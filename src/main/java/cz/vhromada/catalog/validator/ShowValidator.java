package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Show;

/**
 * An interface represents validator for show.
 *
 * @author Vladimir Hromada
 */
public interface ShowValidator {

    /**
     * Validates new show.
     *
     * @param show validating show
     * @throws IllegalArgumentException if show is null
     *                                  or ID isn't null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or URL to ČSFD page about show is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about show is null
     *                                  or URL to czech Wikipedia page about show is null
     *                                  or path to file with show picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    void validateNewShow(Show show);

    /**
     * Validates existing show.
     *
     * @param show validating show
     * @throws IllegalArgumentException if show is null
     *                                  or ID is null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or URL to ČSFD page about show is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about show is null
     *                                  or URL to czech Wikipedia page about show is null
     *                                  or path to file with show picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    void validateExistingShow(Show show);

    /**
     * Validates show with ID.
     *
     * @param show validating show
     * @throws IllegalArgumentException if show is null
     *                                  or ID is null
     */
    void validateShowWithId(Show show);

}
