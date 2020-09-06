package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.domain.Show
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.common.entity.Movable
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.Constants
import com.github.vhromada.common.utils.sorted
import com.github.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents validator for season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonValidator")
class SeasonValidator(showService: MovableService<Show>) : AbstractMovableValidator<Season, Show>("Season", showService) {

    override fun getData(data: Season): Optional<Movable> {
        for (show in service.getAll()) {
            for (season in show.seasons) {
                if (data.id == season.id) {
                    return Optional.of(season)
                }
            }
        }

        return Optional.empty()
    }

    override fun getList(data: Season): List<com.github.vhromada.catalog.domain.Season> {
        for (show in service.getAll()) {
            for (season in show.seasons) {
                if (data.id == season.id) {
                    return show.seasons
                            .sorted()
                }
            }
        }

        return emptyList()
    }

    /**
     * Validates season deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Number of season is null
     *  * Number of season isn't positive number
     *  * Starting year is null
     *  * Starting year isn't between 1940 and current year
     *  * Ending year is null
     *  * Ending year isn't between 1940 and current year
     *  * Starting year is greater than ending year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Note is null
     *
     * @param data   validating season
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Season, result: Result<Unit>) {
        when {
            data.number == null -> {
                result.addEvent(Event(Severity.ERROR, "SEASON_NUMBER_NULL", "Number of season mustn't be null."))
            }
            data.number <= 0 -> {
                result.addEvent(Event(Severity.ERROR, "SEASON_NUMBER_NOT_POSITIVE", "Number of season must be positive number."))
            }
        }
        validateYears(data, result)
        validateLanguages(data, result)
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "SEASON_NOTE_NULL", "Note mustn't be null."))
        }
    }

    /**
     * Validates season deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Starting year is null
     *  * Starting year isn't between 1940 and current year
     *  * Ending year is null
     *  * Ending year isn't between 1940 and current year
     *  * Starting year is greater than ending year
     *
     * @param data   validating season
     * @param result result with validation errors
     */
    private fun validateYears(data: Season, result: Result<Unit>) {
        when {
            data.startYear == null -> {
                result.addEvent(Event(Severity.ERROR, "SEASON_START_YEAR_NULL", "Starting year mustn't be null."))
            }
            data.startYear < Constants.MIN_YEAR || data.startYear > Constants.CURRENT_YEAR -> {
                result.addEvent(Event(Severity.ERROR, "SEASON_START_YEAR_NOT_VALID", "Starting year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}."))
            }
        }
        when {
            data.endYear == null -> {
                result.addEvent(Event(Severity.ERROR, "SEASON_END_YEAR_NULL", "Ending year mustn't be null."))
            }
            data.endYear < Constants.MIN_YEAR || data.endYear > Constants.CURRENT_YEAR -> {
                result.addEvent(Event(Severity.ERROR, "SEASON_END_YEAR_NOT_VALID", "Ending year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}."))
            }
        }
        if (data.startYear != null && data.endYear != null && data.startYear > data.endYear) {
            result.addEvent(Event(Severity.ERROR, "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year."))
        }
    }

    /**
     * Validates languages.
     * <br></br>
     * Validation errors:
     *
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private fun validateLanguages(data: Season, result: Result<Unit>) {
        if (data.language == null) {
            result.addEvent(Event(Severity.ERROR, "SEASON_LANGUAGE_NULL", "Language mustn't be null."))
        }
        when {
            data.subtitles == null -> {
                result.addEvent(Event(Severity.ERROR, "SEASON_SUBTITLES_NULL", "Subtitles mustn't be null."))
            }
            data.subtitles.contains(null) -> {
                result.addEvent(Event(Severity.ERROR, "SEASON_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value."))
            }
        }
    }

}
