package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.Constants
import com.github.vhromada.common.validator.AbstractMovableValidator
import com.github.vhromada.common.validator.MovableValidator
import com.github.vhromada.common.validator.ValidationType
import org.springframework.stereotype.Component

/**
 * A class represents validator for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieValidator")
class MovieValidator(
        movieService: MovableService<com.github.vhromada.catalog.domain.Movie>,
        private val pictureValidator: MovableValidator<Picture>,
        private val genreValidator: MovableValidator<Genre>) : AbstractMovableValidator<Movie, com.github.vhromada.catalog.domain.Movie>("Movie", movieService) {

    /**
     * Validates movie deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Czech name is null
     *  * Czech name is empty string
     *  * Original name is null
     *  * Original name is empty string
     *  * Year is null
     *  * Year isn't between 1940 and current year
     *  * Language is null
     *  * Subtitles are null
     *  * Subtitles contain null value
     *  * Media are null
     *  * Media contain null value
     *  * Length of medium is null
     *  * Length of medium is negative value
     *  * URL to ČSFD page about movie is null
     *  * IMDB code is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about movie is null
     *  * URL to czech Wikipedia page about movie is null
     *  * Note is null
     *  * Picture doesn't exist
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *  * Genre doesn't exist
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Movie, result: Result<Unit>) {
        validateNames(data, result)
        when {
            data.year == null -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_YEAR_NULL", "Year mustn't be null."))
            }
            data.year < Constants.MIN_YEAR || data.year > Constants.CURRENT_YEAR -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_YEAR_NOT_VALID", "Year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}."))
            }
        }
        validateLanguages(data, result)
        validateMedia(data, result)
        validateUrls(data, result)
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null."))
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
    private fun validateNames(data: Movie, result: Result<Unit>) {
        when {
            data.czechName == null -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null."))
            }
            data.czechName.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string."))
            }
        }
        when {
            data.originalName == null -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null."))
            }
            data.originalName.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string."))
            }
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
    private fun validateLanguages(data: Movie, result: Result<Unit>) {
        if (data.language == null) {
            result.addEvent(Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null."))
        }
        when {
            data.subtitles == null -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null."))
            }
            data.subtitles.contains(null) -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value."))
            }
        }
    }

    /**
     * Validates media.
     * <br></br>
     * Validation errors:
     *
     *  * Media are null
     *  * Media contain null value
     *  * Length of medium is null
     *  * Length of medium is negative value
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private fun validateMedia(data: Movie, result: Result<Unit>) {
        if (data.media == null) {
            result.addEvent(Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null."))
        } else {
            if (data.media.contains(null)) {
                result.addEvent(Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value."))
            }
            for (medium in data.media) {
                if (medium != null) {
                    when {
                        medium.length == null -> {
                            result.addEvent(Event(Severity.ERROR, "MOVIE_MEDIUM_NULL", "Length of medium  mustn't be null."))
                        }
                        medium.length <= 0 -> {
                            result.addEvent(Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number."))
                        }
                    }
                }
            }
        }
    }

    /**
     * Validates URLs.
     * <br></br>
     * Validation errors:
     *
     *  * URL to ČSFD page about movie is null
     *  * IMDB code is null
     *  * IMDB code isn't -1 or between 1 and 9999999
     *  * URL to english Wikipedia page about movie is null
     *  * URL to czech Wikipedia page about movie is null
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private fun validateUrls(data: Movie, result: Result<Unit>) {
        if (data.csfd == null) {
            result.addEvent(Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null."))
        }
        when {
            data.imdbCode == null -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_IMDB_CODE_NULL", "IMDB code mustn't be null."))
            }
            data.imdbCode != -1 && (data.imdbCode < 1 || data.imdbCode > Constants.MAX_IMDB_CODE) -> {
                result.addEvent(Event(Severity.ERROR, "MOVIE_IMDB_CODE_NOT_VALID", "IMDB code must be between 1 and 9999999 or -1."))
            }
        }
        if (data.wikiEn == null) {
            result.addEvent(Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL", "URL to english Wikipedia page about movie mustn't be null."))
        }
        if (data.wikiCz == null) {
            result.addEvent(Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL", "URL to czech Wikipedia page about movie mustn't be null."))
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
    private fun validatePicture(data: Movie, result: Result<Unit>) {
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
     * @param data   validating movie
     * @param result result with validation errors
     */
    private fun validateGenres(data: Movie, result: Result<Unit>) {
        if (data.genres == null) {
            result.addEvent(Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null."))
        } else {
            if (data.genres.contains(null)) {
                result.addEvent(Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value."))
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
