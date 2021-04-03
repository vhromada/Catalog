package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.validator.AbstractValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for song.
 *
 * @author Vladimir Hromada
 */
@Component("songValidator")
class SongValidator : AbstractValidator<Song, com.github.vhromada.catalog.domain.Song>(name = "Song") {

    /**
     * Validates song deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Name is null
     *  * Name is empty string
     *  * Length of song is null
     *  * Length of song is negative value
     *  * Note is null
     *
     * @param data   validating song
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Song, result: Result<Unit>) {
        when {
            data.name == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SONG_NAME_NULL", message = "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SONG_NAME_EMPTY", message = "Name mustn't be empty string."))
            }
        }
        when {
            data.length == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SONG_LENGTH_NULL", message = "Length of song mustn't be null."))
            }
            data.length < 0 -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "SONG_LENGTH_NEGATIVE", message = "Length of song mustn't be negative number."))
            }
        }
        if (data.note == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "SONG_NOTE_NULL", message = "Note mustn't be null."))
        }
    }

}
