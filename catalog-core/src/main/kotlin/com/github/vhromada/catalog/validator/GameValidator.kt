package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.validator.AbstractValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for game.
 *
 * @author Vladimir Hromada
 */
@Component("gameValidator")
class GameValidator : AbstractValidator<Game, com.github.vhromada.catalog.domain.Game>(name = "Game") {

    /**
     * Validates game deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about game is null
     *  * URL to czech Wikipedia page about game is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Format is null
     *  * Other data is null
     *  * Note is null
     *
     * @param data   validating game
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Game, result: Result<Unit>) {
        when {
            data.name == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_NAME_NULL", message = "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_NAME_EMPTY", message = "Name mustn't be empty string."))
            }
        }
        validateUrls(game = data, result = result)
        when {
            data.mediaCount == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NULL", message = "Count of media mustn't be null."))
            }
            data.mediaCount <= 0 -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number."))
            }
        }
        if (data.format == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_FORMAT_NULL", message = "Format mustn't be null."))
        }
        if (data.otherData == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_OTHER_DATA_NULL", message = "Other data mustn't be null."))
        }
        if (data.note == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_NOTE_NULL", message = "Note mustn't be null."))
        }
    }

    /**
     * Validates URLs.
     * <br></br>
     * Validation errors:
     *
     *  * URL to english Wikipedia page about game is null
     *  * URL to czech Wikipedia page about game is null
     *
     * @param game   validating show
     * @param result result with validation errors
     */
    private fun validateUrls(game: Game, result: Result<Unit>) {
        if (game.wikiEn == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_WIKI_EN_NULL", message = "URL to english Wikipedia page about game mustn't be null."))
        }
        if (game.wikiCz == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "GAME_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about game mustn't be null."))
        }
    }

}
