package cz.vhromada.catalog.validator.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.Constants;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonValidator")
public class SeasonValidatorImpl extends AbstractCatalogValidator<Season, Show> {

    /**
     * Creates a new instance of SeasonValidatorImpl.
     *
     * @param showService service for shows
     * @throws IllegalArgumentException if service for shows is null
     */
    @Autowired
    public SeasonValidatorImpl(final CatalogService<Show> showService) {
        super("Season", showService);
    }

    @Override
    protected cz.vhromada.catalog.domain.Season getData(final Season data) {
        for (final Show show : getCatalogService().getAll()) {
            for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
                if (data.getId().equals(season.getId())) {
                    return season;
                }
            }
        }

        return null;
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Season> getList(final Season data) {
        for (final Show show : getCatalogService().getAll()) {
            for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
                if (data.getId().equals(season.getId())) {
                    return CollectionUtils.getSortedData(show.getSeasons());
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Validates season deeply.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Number of season isn't positive number</li>
     * <li>Starting year isn't between 1940 and current year</li>
     * <li>Ending year isn't between 1940 and current year</li>
     * <li>Starting year is greater than ending year</li>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data   validating season
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Season data, final Result<Void> result) {
        if (data.getNumber() <= 0) {
            result.addEvent(new Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number."));
        }
        validateYears(data, result);
        validateLanguages(data, result);
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null."));
        }
    }

    /**
     * Validates season deeply.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Starting year isn't between 1940 and current year</li>
     * <li>Ending year isn't between 1940 and current year</li>
     * <li>Starting year is greater than ending year</li>
     * </ul>
     *
     * @param data   validating season
     * @param result result with validation errors
     */
    private static void validateYears(final Season data, final Result<Void> result) {
        if (data.getStartYear() < Constants.MIN_YEAR || data.getStartYear() > Constants.CURRENT_YEAR) {
            result.addEvent(new Event(Severity.ERROR, "SEASON_START_YEAR_NOT_VALID", "Starting year must be between " + Constants.MIN_YEAR
                    + " and " + Constants.CURRENT_YEAR + '.'));
        }
        if (data.getEndYear() < Constants.MIN_YEAR || data.getEndYear() > Constants.CURRENT_YEAR) {
            result.addEvent(new Event(Severity.ERROR, "SEASON_END_YEAR_NOT_VALID", "Ending year must be between " + Constants.MIN_YEAR
                    + " and " + Constants.CURRENT_YEAR + '.'));
        }
        if (data.getStartYear() > data.getEndYear()) {
            result.addEvent(new Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year."));
        }
    }

    /**
     * Validates languages.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private static void validateLanguages(final Season data, final Result<Void> result) {
        if (data.getLanguage() == null) {
            result.addEvent(new Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null."));
        }
        if (data.getSubtitles() == null) {
            result.addEvent(new Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null."));
        } else if (data.getSubtitles().contains(null)) {
            result.addEvent(new Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value."));
        }
    }

}
