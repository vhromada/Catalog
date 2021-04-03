package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.utils.Constants
import com.github.vhromada.common.validator.AbstractValidator
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents validator for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieValidator")
class MovieValidator(
    private val genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>
) : AbstractValidator<Movie, com.github.vhromada.catalog.domain.Movie>(name = "Movie") {

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
     *  * Genres are null
     *  * Genres contain null value
     *  * Genre ID is null
     *  * Genre name is null
     *  * Genre name is empty string
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Movie, result: Result<Unit>) {
        validateNames(data, result)
        when {
            data.year == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_YEAR_NULL", message = "Year mustn't be null."))
            }
            data.year < Constants.MIN_YEAR || data.year > Constants.CURRENT_YEAR -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_YEAR_NOT_VALID", message = "Year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}."))
            }
        }
        validateLanguages(movie = data, result = result)
        validateMedia(movie = data, result = result)
        validateUrls(movie = data, result = result)
        if (data.note == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_NOTE_NULL", message = "Note mustn't be null."))
        }
        validateGenres(movie = data, result = result)
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
     * @param movie  validating movie
     * @param result result with validation errors
     */
    private fun validateNames(movie: Movie, result: Result<Unit>) {
        when {
            movie.czechName == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_NULL", message = "Czech name mustn't be null."))
            }
            movie.czechName.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string."))
            }
        }
        when {
            movie.originalName == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_NULL", message = "Original name mustn't be null."))
            }
            movie.originalName.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string."))
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
     * @param movie  validating movie
     * @param result result with validation errors
     */
    private fun validateLanguages(movie: Movie, result: Result<Unit>) {
        if (movie.language == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_LANGUAGE_NULL", message = "Language mustn't be null."))
        }
        when {
            movie.subtitles == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_NULL", message = "Subtitles mustn't be null."))
            }
            movie.subtitles.contains(null) -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value."))
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
     * @param movie  validating movie
     * @param result result with validation errors
     */
    private fun validateMedia(movie: Movie, result: Result<Unit>) {
        if (movie.media == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_NULL", message = "Media mustn't be null."))
        } else {
            if (movie.media.contains(null)) {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_CONTAIN_NULL", message = "Media mustn't contain null value."))
            }
            for (medium in movie.media) {
                if (medium != null) {
                    when {
                        medium.length == null -> {
                            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_MEDIUM_NULL", message = "Length of medium mustn't be null."))
                        }
                        medium.length <= 0 -> {
                            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_MEDIUM_NOT_POSITIVE", message = "Length of medium must be positive number."))
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
     * @param movie  validating movie
     * @param result result with validation errors
     */
    private fun validateUrls(movie: Movie, result: Result<Unit>) {
        if (movie.csfd == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_CSFD_NULL", message = "URL to ČSFD page about movie mustn't be null."))
        }
        when {
            movie.imdbCode == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_IMDB_CODE_NULL", message = "IMDB code mustn't be null."))
            }
            movie.imdbCode != -1 && (movie.imdbCode < 1 || movie.imdbCode > Constants.MAX_IMDB_CODE) -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_IMDB_CODE_NOT_VALID", message = "IMDB code must be between 1 and 9999999 or -1."))
            }
        }
        if (movie.wikiEn == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_WIKI_EN_NULL", message = "URL to english Wikipedia page about movie mustn't be null."))
        }
        if (movie.wikiCz == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about movie mustn't be null."))
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
     * @param movie  validating movie
     * @param result result with validation errors
     */
    private fun validateGenres(movie: Movie, result: Result<Unit>) {
        if (movie.genres == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_GENRES_NULL", message = "Genres mustn't be null."))
        } else {
            if (movie.genres.contains(null)) {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MOVIE_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value."))
            }
            for (genre in movie.genres) {
                if (genre != null) {
                    val validationResult = genreValidator.validate(data = genre, update = true)
                    result.addEvents(eventList = validationResult.events())
                }
            }
        }
    }

}
