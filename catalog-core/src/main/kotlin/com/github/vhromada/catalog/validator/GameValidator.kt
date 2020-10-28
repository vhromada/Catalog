package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for game.
 *
 * @author Vladimir Hromada
 */
@Component("gameValidator")
class GameValidator(gameService: MovableService<com.github.vhromada.catalog.domain.Game>) : AbstractMovableValidator<Game, com.github.vhromada.catalog.domain.Game>("Game", gameService) {

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
     *  * Cheat is ot null
     *  * Other data is null
     *  * Note is null
     *
     * @param data   validating game
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Game, result: Result<Unit>) {
        when {
            data.name == null -> {
                result.addEvent(Event(Severity.ERROR, "${getPrefix()}_NAME_NULL", "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "${getPrefix()}_NAME_EMPTY", "Name mustn't be empty string."))
            }
        }
        validateUrls(data, result)
        when {
            data.mediaCount == null -> {
                result.addEvent(Event(Severity.ERROR, "${getPrefix()}_MEDIA_COUNT_NULL", "Count of media mustn't be null."))
            }
            data.mediaCount <= 0 -> {
                result.addEvent(Event(Severity.ERROR, "${getPrefix()}_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."))
            }
        }
        if (data.format == null) {
            result.addEvent(Event(Severity.ERROR, "${getPrefix()}_FORMAT_NULL", "Format mustn't be null."))
        }
        if (data.otherData == null) {
            result.addEvent(Event(Severity.ERROR, "${getPrefix()}_OTHER_DATA_NULL", "Other data mustn't be null."))
        }
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "${getPrefix()}_NOTE_NULL", "Note mustn't be null."))
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
     * @param data   validating show
     * @param result result with validation errors
     */
    private fun validateUrls(data: Game, result: Result<Unit>) {
        if (data.wikiEn == null) {
            result.addEvent(Event(Severity.ERROR, "${getPrefix()}_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null."))
        }
        if (data.wikiCz == null) {
            result.addEvent(Event(Severity.ERROR, "${getPrefix()}_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null."))
        }
    }

}
