package cz.vhromada.catalog.validators.impl;

import cz.vhromada.catalog.common.CatalogValidators;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.validators.SeasonTOValidator;
import cz.vhromada.validators.Validators;

import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonTOValidator")
public class SeasonTOValidatorImpl implements SeasonTOValidator {

    /**
     * TO for season argument
     */
    private static final String SEASON_TO_ARGUMENT = "TO for season";

    /**
     * Field ID
     */
    private static final String ID_FIELD = "ID";

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateNewSeasonTO(final SeasonTO season) {
        validateSeasonTO(season);
        Validators.validateNull(season.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateExistingSeasonTO(final SeasonTO season) {
        validateSeasonTO(season);
        Validators.validateNotNull(season.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateSeasonTOWithId(final SeasonTO season) {
        Validators.validateArgumentNotNull(season, SEASON_TO_ARGUMENT);
        Validators.validateNotNull(season.getId(), ID_FIELD);
    }

    /**
     * Validates TO for season.
     *
     * @param season validating TO for season
     * @throws IllegalArgumentException                              if TO for season is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if number of season isn't positive number
     *                                                               or starting year isn't between 1940 and current year
     *                                                               or ending year isn't between 1940 and current year
     *                                                               or starting year is greater than ending year
     *                                                               or language is null
     *                                                               or subtitles are null
     *                                                               or subtitles contain null value
     *                                                               or note is null
     */
    private static void validateSeasonTO(final SeasonTO season) {
        Validators.validateArgumentNotNull(season, SEASON_TO_ARGUMENT);
        Validators.validatePositiveNumber(season.getNumber(), "Number of season");
        CatalogValidators.validateYear(season.getStartYear(), "Starting year");
        CatalogValidators.validateYear(season.getEndYear(), "Ending year");
        CatalogValidators.validateYears(season.getStartYear(), season.getEndYear());
        Validators.validateNotNull(season.getLanguage(), "Language");
        Validators.validateNotNull(season.getSubtitles(), "Subtitles");
        Validators.validateCollectionNotContainNull(season.getSubtitles(), "Subtitles");
        Validators.validateNotNull(season.getNote(), "Note");
    }

}
