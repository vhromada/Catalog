package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.SeasonTO;

/**
 * An interface represents validator for TO for season.
 *
 * @author Vladimir Hromada
 */
public interface SeasonTOValidator {

    /**
     * Validates new TO for season.
     *
     * @param season validating TO for season
     * @throws IllegalArgumentException                              if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or number of season isn't positive number
     *                                                               or starting year isn't between 1940 and current year
     *                                                               or ending year isn't between 1940 and current year
     *                                                               or starting year is greater than ending year
     *                                                               or language is null
     *                                                               or subtitles are null
     *                                                               or subtitles contain null value
     *                                                               or note is null
     *                                                               or TO for serie is null
     *                                                               or TO for serie ID is null
     */
    void validateNewSeasonTO(SeasonTO season);

    /**
     * Validates existing TO for season.
     *
     * @param season validating TO for season
     * @throws IllegalArgumentException                              if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     *                                                               or number of season isn't positive number
     *                                                               or starting year isn't between 1940 and current year
     *                                                               or ending year isn't between 1940 and current year
     *                                                               or starting year is greater than ending year
     *                                                               or language is null
     *                                                               or subtitles are null
     *                                                               or subtitles contain null value
     *                                                               or note is null
     *                                                               or TO for serie is null
     *                                                               or TO for serie ID is null
     */
    void validateExistingSeasonTO(SeasonTO season);

    /**
     * Validates TO for season with ID.
     *
     * @param season validating TO for season
     * @throws IllegalArgumentException                              if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    void validateSeasonTOWithId(SeasonTO season);

}
