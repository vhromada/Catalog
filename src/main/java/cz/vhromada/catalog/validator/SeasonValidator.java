package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Season;

/**
 * An interface represents validator for season.
 *
 * @author Vladimir Hromada
 */
public interface SeasonValidator {

    /**
     * Validates new season.
     *
     * @param season validating season
     * @throws IllegalArgumentException if season is null
     *                                  or ID isn't null
     *                                  or number of season isn't positive number
     *                                  or starting year isn't between 1940 and current year
     *                                  or ending year isn't between 1940 and current year
     *                                  or starting year is greater than ending year
     *                                  or language is null
     *                                  or subtitles are null
     *                                  or subtitles contain null value
     *                                  or note is null
     */
    void validateNewSeason(Season season);

    /**
     * Validates existing season.
     *
     * @param season validating season
     * @throws IllegalArgumentException if season is null
     *                                  or ID is null
     *                                  or number of season isn't positive number
     *                                  or starting year isn't between 1940 and current year
     *                                  or ending year isn't between 1940 and current year
     *                                  or starting year is greater than ending year
     *                                  or language is null
     *                                  or subtitles are null
     *                                  or subtitles contain null value
     *                                  or note is null
     */
    void validateExistingSeason(Season season);

    /**
     * Validates season with ID.
     *
     * @param season validating season
     * @throws IllegalArgumentException if season is null
     *                                  or ID is null
     */
    void validateSeasonWithId(Season season);

}
