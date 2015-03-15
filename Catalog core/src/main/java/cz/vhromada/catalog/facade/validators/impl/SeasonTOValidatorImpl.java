package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.CatalogValidators;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
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
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateNewSeasonTO(final SeasonTO season) {
        validateSeasonTO(season);
        Validators.validateNull(season.getId(), "ID");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateExistingSeasonTO(final SeasonTO season) {
        validateSeasonTO(season);
        Validators.validateNotNull(season.getId(), "ID");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateSeasonTOWithId(final SeasonTO season) {
        Validators.validateArgumentNotNull(season, "TO for season");
        Validators.validateNotNull(season.getId(), "ID");
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
     *                                                               or TO for serie is null
     *                                                               or TO for serie ID is null
     */
    private static void validateSeasonTO(final SeasonTO season) {
        Validators.validateArgumentNotNull(season, "TO for season");
        Validators.validatePositiveNumber(season.getNumber(), "Number of season");
        CatalogValidators.validateYear(season.getStartYear(), "Starting year");
        CatalogValidators.validateYear(season.getEndYear(), "Ending year");
        CatalogValidators.validateYears(season.getStartYear(), season.getEndYear());
        Validators.validateNotNull(season.getLanguage(), "Language");
        Validators.validateNotNull(season.getSubtitles(), "Subtitles");
        Validators.validateCollectionNotContainNull(season.getSubtitles(), "Subtitles");
        Validators.validateNotNull(season.getNote(), "Note");
        Validators.validateNotNull(season.getSerie(), "TO for serie");
        Validators.validateNotNull(season.getSerie().getId(), "TO for serie ID");
    }

}
