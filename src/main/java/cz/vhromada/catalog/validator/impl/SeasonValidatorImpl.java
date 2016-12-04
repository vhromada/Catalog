package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.util.Constants;
import cz.vhromada.catalog.validator.SeasonValidator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of validator for season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonValidator")
public class SeasonValidatorImpl implements SeasonValidator {

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewSeasonTO(final SeasonTO season) {
        validateSeasonTO(season);
        Assert.isNull(season.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingSeasonTO(final SeasonTO season) {
        validateSeasonTO(season);
        Assert.notNull(season.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateSeasonTOWithId(final SeasonTO season) {
        Assert.notNull(season, "Season mustn't be null.");
        Assert.notNull(season.getId(), "ID mustn't be null.");
    }

    /**
     * Validates season.
     *
     * @param season validating season
     * @throws IllegalArgumentException if season is null
     *                                  or number of season isn't positive number
     *                                  or starting year isn't between 1940 and current year
     *                                  or ending year isn't between 1940 and current year
     *                                  or starting year is greater than ending year
     *                                  or language is null
     *                                  or subtitles are null
     *                                  or subtitles contain null value
     *                                  or note is null
     */
    private static void validateSeasonTO(final SeasonTO season) {
        Assert.notNull(season, "Season mustn't be null.");
        Assert.isTrue(season.getNumber() > 0, "Number of season must be positive number.");
        Assert.isTrue(season.getStartYear() >= Constants.MIN_YEAR && season.getStartYear() <= Constants.CURRENT_YEAR, "Starting year must be between "
                + Constants.MIN_YEAR + " and " + Constants.CURRENT_YEAR + ".");
        Assert.isTrue(season.getEndYear() >= Constants.MIN_YEAR && season.getEndYear() <= Constants.CURRENT_YEAR, "Ending year must be between "
                + Constants.MIN_YEAR + " and " + Constants.CURRENT_YEAR + ".");
        Assert.isTrue(season.getStartYear() <= season.getEndYear(), "Starting year mustn't be greater than ending year.");
        Assert.notNull(season.getLanguage(), "Language mustn't be null.");
        Assert.notNull(season.getSubtitles(), "Subtitles mustn't be null.");
        Assert.isTrue(!season.getSubtitles().contains(null), "Subtitles mustn't contain null value.");
        Assert.notNull(season.getNote(), "Note mustn't be null.");
    }

}
