package cz.vhromada.catalog.validator

import cz.vhromada.catalog.entity.Music
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Result
import cz.vhromada.validation.result.Severity
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * A class represents validator for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicValidator")
class MusicValidator(musicService: MovableService<cz.vhromada.catalog.domain.Music>) : AbstractMovableValidator<Music, cz.vhromada.catalog.domain.Music>("Music", musicService) {

    /**
     * Validates music deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about music is null
     *  * URL to czech Wikipedia page about music is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Note is null
     *
     * @param data   validating music
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Music, result: Result<Unit>) {
        if (data.name == null) {
            result.addEvent(Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null."))
        } else if (!StringUtils.hasText(data.name)) {
            result.addEvent(Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string."))
        }
        validateUrls(data, result)
        if (data.mediaCount == null) {
            result.addEvent(Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NULL", "Count of media mustn't be null."))
        } else if (data.mediaCount <= 0) {
            result.addEvent(Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."))
        }
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null."))
        }
    }

    /**
     * Validates URLs.
     * <br></br>
     * Validation errors:
     *
     *  * URL to english Wikipedia page about music is null
     *  * URL to czech Wikipedia page about music is null
     *
     * @param data   validating show
     * @param result result with validation errors
     */
    private fun validateUrls(data: Music, result: Result<Unit>) {
        if (data.wikiEn == null) {
            result.addEvent(Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL", "URL to english Wikipedia page about music mustn't be null."))
        }
        if (data.wikiCz == null) {
            result.addEvent(Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL", "URL to czech Wikipedia page about music mustn't be null."))
        }
    }

}
