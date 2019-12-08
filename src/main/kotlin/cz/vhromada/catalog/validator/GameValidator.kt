package cz.vhromada.catalog.validator

import cz.vhromada.catalog.entity.Game
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Result
import cz.vhromada.validation.result.Severity
import org.springframework.stereotype.Component

/**
 * A class represents validator for game.
 *
 * @author Vladimir Hromada
 */
@Component("gameValidator")
class GameValidator(gameService: MovableService<cz.vhromada.catalog.domain.Game>) : AbstractMovableValidator<Game, cz.vhromada.catalog.domain.Game>("Game", gameService) {

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
     *  * Other data is null
     *  * Note is null
     *
     * @param data   validating game
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Game, result: Result<Unit>) {
        if (data.name == null) {
            result.addEvent(Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null."))
        } else if (data.name.isBlank()) {
            result.addEvent(Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string."))
        }
        validateUrls(data, result)
        if (data.mediaCount == null) {
            result.addEvent(Event(Severity.ERROR, "GAME_MEDIA_COUNT_NULL", "Count of media mustn't be null."))
        } else if (data.mediaCount <= 0) {
            result.addEvent(Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."))
        }
        if (data.otherData == null) {
            result.addEvent(Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null."))
        }
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null."))
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
            result.addEvent(Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null."))
        }
        if (data.wikiCz == null) {
            result.addEvent(Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null."))
        }
    }

}
