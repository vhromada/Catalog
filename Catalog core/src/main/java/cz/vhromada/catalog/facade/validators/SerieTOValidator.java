package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.SerieTO;

/**
 * An interface represents validator for TO for serie.
 *
 * @author Vladimir Hromada
 */
public interface SerieTOValidator {

    /**
     * Validates new TO for serie.
     *
     * @param serie validating TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID isn't null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or URL to ČSFD page about serie is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about serie is null
     *                                  or URL to czech Wikipedia page about serie is null
     *                                  or path to file with serie's picture is null
     *                                  or count of seasons is negative number
     *                                  or count of episodes is negative number
     *                                  or total length of seasons is null
     *                                  or total length of seasons is negative number
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    void validateNewSerieTO(SerieTO serie);

    /**
     * Validates existing TO for serie.
     *
     * @param serie validating TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or URL to ČSFD page about serie is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about serie is null
     *                                  or URL to czech Wikipedia page about serie is null
     *                                  or path to file with serie's picture is null
     *                                  or count of seasons is negative number
     *                                  or count of episodes is negative number
     *                                  or total length of seasons is null
     *                                  or total length of seasons is negative number
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    void validateExistingSerieTO(SerieTO serie);

    /**
     * Validates TO for serie with ID.
     *
     * @param serie validating TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     */
    void validateSerieTOWithId(SerieTO serie);

}
