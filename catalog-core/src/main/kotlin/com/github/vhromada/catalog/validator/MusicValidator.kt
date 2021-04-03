package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.validator.AbstractValidator
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * A class represents validator for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicValidator")
class MusicValidator : AbstractValidator<Music, com.github.vhromada.catalog.domain.Music>(name = "Music") {

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
        when {
            data.name == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MUSIC_NAME_NULL", message = "Name mustn't be null."))
            }
            !StringUtils.hasText(data.name) -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MUSIC_NAME_EMPTY", message = "Name mustn't be empty string."))
            }
        }
        validateUrls(music = data, result = result)
        when {
            data.mediaCount == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NULL", message = "Count of media mustn't be null."))
            }
            data.mediaCount <= 0 -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number."))
            }
        }
        if (data.note == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MUSIC_NOTE_NULL", message = "Note mustn't be null."))
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
     * @param music  validating show
     * @param result result with validation errors
     */
    private fun validateUrls(music: Music, result: Result<Unit>) {
        if (music.wikiEn == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MUSIC_WIKI_EN_NULL", message = "URL to english Wikipedia page about music mustn't be null."))
        }
        if (music.wikiCz == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "MUSIC_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about music mustn't be null."))
        }
    }

}
