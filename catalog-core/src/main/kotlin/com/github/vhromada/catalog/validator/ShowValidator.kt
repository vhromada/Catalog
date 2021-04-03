package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.utils.Constants
import com.github.vhromada.common.validator.AbstractValidator
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents validator for show.
 *
 * @author Vladimir Hromada
 */
@Component("showValidator")
class ShowValidator(
    private val genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>
) : AbstractValidator<Show, com.github.vhromada.catalog.domain.Show>(name = "Show") {

    /**
     * Validates show deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *  * URL to ČSFD page about show is null
     *  * IMDB code is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about show is null
     *  * URL to czech Wikipedia page about show is null
     *  * Note is null
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *
     * @param data   validating show
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Show, result: Result<Unit>) {
        validateNames(show = data, result = result)
        validateUrls(show = data, result = result)
        if (data.note == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_NOTE_NULL", message = "Note mustn't be null."))
        }
        validateGenres(show = data, result = result)
    }

    /**
     * Validates names.
     * <br></br>
     * Validation errors:
     *
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *
     * @param show   validating show
     * @param result result with validation errors
     */
    private fun validateNames(show: Show, result: Result<Unit>) {
        when {
            show.czechName == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_NULL", message = "Czech name mustn't be null."))
            }
            show.czechName.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string."))
            }
        }
        when {
            show.originalName == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_NULL", message = "Original name mustn't be null."))
            }
            show.originalName.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string."))
            }
        }
    }

    /**
     * Validates URLs.
     * <br></br>
     * Validation errors:
     *
     *  * URL to ČSFD page about show is null
     *  * IMDB code is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about show is null
     *  * URL to czech Wikipedia page about show is null
     *
     * @param show   validating show
     * @param result result with validation errors
     */
    private fun validateUrls(show: Show, result: Result<Unit>) {
        if (show.csfd == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_CSFD_NULL", message = "URL to ČSFD page about show mustn't be null."))
        }
        when {
            show.imdbCode == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_IMDB_CODE_NULL", message = "IMDB code mustn't be null."))
            }
            show.imdbCode != -1 && (show.imdbCode < 1 || show.imdbCode > Constants.MAX_IMDB_CODE) -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_IMDB_CODE_NOT_VALID", message = "IMDB code must be between 1 and 9999999 or -1."))
            }
        }
        if (show.wikiEn == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_WIKI_EN_NULL", message = "URL to english Wikipedia page about show mustn't be null."))
        }
        if (show.wikiCz == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about show mustn't be null."))
        }
    }

    /**
     * Validates genres.
     * <br></br>
     * Validation errors:
     *
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *
     * @param show   validating show
     * @param result result with validation errors
     */
    private fun validateGenres(show: Show, result: Result<Unit>) {
        if (show.genres == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_GENRES_NULL", message = "Genres mustn't be null."))
        } else {
            if (show.genres.contains(null)) {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SHOW_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value."))
            }
            for (genre in show.genres) {
                if (genre != null) {
                    val validationResult = genreValidator.validate(data = genre, update = true)
                    result.addEvents(validationResult.events())
                }
            }
        }
    }

}
