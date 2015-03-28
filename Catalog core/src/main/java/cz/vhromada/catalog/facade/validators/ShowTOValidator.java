package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.ShowTO;

/**
 * An interface represents validator for TO for show.
 *
 * @author Vladimir Hromada
 */
public interface ShowTOValidator {

    /**
     * Validates new TO for show.
     *
     * @param show validating TO for show
     * @throws IllegalArgumentException                              if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or czech name is null
     *                                                               or czech name is empty string
     *                                                               or original name is null
     *                                                               or original name is empty string
     *                                                               or URL to ČSFD page about show is null
     *                                                               or IMDB code isn't -1 or between 1 and 9999999
     *                                                               or URL to english Wikipedia page about show is null
     *                                                               or URL to czech Wikipedia page about show is null
     *                                                               or path to file with show picture is null
     *                                                               or note is null
     *                                                               or genres are null
     *                                                               or genres contain null value
     *                                                               or genre ID is null
     *                                                               or genre name is null
     *                                                               or genre name is empty string
     */
    void validateNewShowTO(ShowTO show);

    /**
     * Validates existing TO for show.
     *
     * @param show validating TO for show
     * @throws IllegalArgumentException                              if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     *                                                               or czech name is null
     *                                                               or czech name is empty string
     *                                                               or original name is null
     *                                                               or original name is empty string
     *                                                               or URL to ČSFD page about show is null
     *                                                               or IMDB code isn't -1 or between 1 and 9999999
     *                                                               or URL to english Wikipedia page about show is null
     *                                                               or URL to czech Wikipedia page about show is null
     *                                                               or path to file with show picture is null
     *                                                               or note is null
     *                                                               or genres are null
     *                                                               or genres contain null value
     *                                                               or genre ID is null
     *                                                               or genre name is null
     *                                                               or genre name is empty string
     */
    void validateExistingShowTO(ShowTO show);

    /**
     * Validates TO for show with ID.
     *
     * @param show validating TO for show
     * @throws IllegalArgumentException                              if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    void validateShowTOWithId(ShowTO show);

}
