package cz.vhromada.catalog.validator

import cz.vhromada.catalog.entity.Genre
import cz.vhromada.catalog.entity.Picture
import cz.vhromada.catalog.entity.Show
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.utils.Constants
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.common.validator.ValidationType
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Result
import cz.vhromada.validation.result.Severity
import org.springframework.stereotype.Component

/**
 * A class represents validator for show.
 *
 * @author Vladimir Hromada
 */
@Component("showValidator")
class ShowValidator(
        showService: MovableService<cz.vhromada.catalog.domain.Show>,
        private val pictureValidator: MovableValidator<Picture>,
        private val genreValidator: MovableValidator<Genre>) : AbstractMovableValidator<Show, cz.vhromada.catalog.domain.Show>("Show", showService) {

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
     *  * Picture doesn't exist
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *
     * @param data   validating show
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Show, result: Result<Unit>) {
        validateNames(data, result)
        validateUrls(data, result)
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null."))
        }
        validatePicture(data, result)
        validateGenres(data, result)
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
     * @param data   validating movie
     * @param result result with validation errors
     */
    private fun validateNames(data: Show, result: Result<Unit>) {
        if (data.czechName == null) {
            result.addEvent(Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null."))
        } else if (data.czechName.isBlank()) {
            result.addEvent(Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string."))
        }
        if (data.originalName == null) {
            result.addEvent(Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null."))
        } else if (data.originalName.isBlank()) {
            result.addEvent(Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string."))
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
     * @param data   validating show
     * @param result result with validation errors
     */
    private fun validateUrls(data: Show, result: Result<Unit>) {
        if (data.csfd == null) {
            result.addEvent(Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null."))
        }
        if (data.imdbCode == null) {
            result.addEvent(Event(Severity.ERROR, "SHOW_IMDB_CODE_NULL", "IMDB code mustn't be null."))
        } else if (data.imdbCode != -1 && (data.imdbCode < 1 || data.imdbCode > Constants.MAX_IMDB_CODE)) {
            result.addEvent(Event(Severity.ERROR, "SHOW_IMDB_CODE_NOT_VALID", "IMDB code must be between 1 and 9999999 or -1."))
        }
        if (data.wikiEn == null) {
            result.addEvent(Event(Severity.ERROR, "SHOW_WIKI_EN_NULL", "URL to english Wikipedia page about show mustn't be null."))
        }
        if (data.wikiCz == null) {
            result.addEvent(Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL", "URL to czech Wikipedia page about show mustn't be null."))
        }
    }

    /**
     * Validates picture.
     * <br></br>
     * Validation errors:
     *
     *  * Picture doesn't exist
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private fun validatePicture(data: Show, result: Result<Unit>) {
        if (data.picture != null) {
            val picture = Picture(data.picture, null, null)
            val validationResult = pictureValidator.validate(picture, ValidationType.EXISTS)
            result.addEvents(validationResult.events())
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
     *  * Genre doesn't exist
     *
     * @param data   validating show
     * @param result result with validation errors
     */
    private fun validateGenres(data: Show, result: Result<Unit>) {
        if (data.genres == null) {
            result.addEvent(Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null."))
        } else {
            if (data.genres.contains(null)) {
                result.addEvent(Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value."))
            }
            for (genre in data.genres) {
                if (genre != null) {
                    val validationResult = genreValidator.validate(genre, ValidationType.EXISTS, ValidationType.DEEP)
                    result.addEvents(validationResult.events())
                }
            }
        }
    }

}
