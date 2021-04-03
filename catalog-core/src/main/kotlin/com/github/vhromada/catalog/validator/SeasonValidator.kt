package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.utils.Constants
import com.github.vhromada.common.validator.AbstractValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for season.
 *
 * @author Vladimir Hromada
 */
@Component("seasonValidator")
class SeasonValidator : AbstractValidator<Season, com.github.vhromada.catalog.domain.Season>(name = "Season") {

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
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NULL", message = "Number of season mustn't be null."))
            }
            data.number <= 0 -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NOT_POSITIVE", message = "Number of season must be positive number."))
            }
        }
        validateYears(season = data, result = result)
        validateLanguages(season = data, result = result)
        if (data.note == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_NOTE_NULL", message = "Note mustn't be null."))
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
     * @param season validating season
     * @param result result with validation errors
     */
    private fun validateYears(season: Season, result: Result<Unit>) {
        when {
            season.startYear == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_START_YEAR_NULL", message = "Starting year mustn't be null."))
            }
            season.startYear < Constants.MIN_YEAR || season.startYear > Constants.CURRENT_YEAR -> {
                result.addEvent(
                    event = Event(
                        severity = Severity.ERROR,
                        key = "SEASON_START_YEAR_NOT_VALID",
                        message = "Starting year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}."
                    )
                )
            }
        }
        when {
            season.endYear == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_END_YEAR_NULL", message = "Ending year mustn't be null."))
            }
            season.endYear < Constants.MIN_YEAR || season.endYear > Constants.CURRENT_YEAR -> {
                result.addEvent(
                    event = Event(
                        severity = Severity.ERROR,
                        key = "SEASON_END_YEAR_NOT_VALID",
                        message = "Ending year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}."
                    )
                )
            }
        }
        if (season.startYear != null && season.endYear != null && season.startYear > season.endYear) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_YEARS_NOT_VALID", message = "Starting year mustn't be greater than ending year."))
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
     * @param season validating season
     * @param result result with validation errors
     */
    private fun validateLanguages(season: Season, result: Result<Unit>) {
        if (season.language == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_LANGUAGE_NULL", message = "Language mustn't be null."))
        }
        when {
            season.subtitles == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_NULL", message = "Subtitles mustn't be null."))
            }
            season.subtitles.contains(null) -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value."))
            }
        }
    }

}
