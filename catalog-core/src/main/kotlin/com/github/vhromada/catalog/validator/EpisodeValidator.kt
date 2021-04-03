package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.validator.AbstractValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeValidator")
class EpisodeValidator : AbstractValidator<Episode, com.github.vhromada.catalog.domain.Episode>(name = "Episode") {

    /**
     * Validates episode deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Number of episode is null
     *  * Number of episode isn't positive number
     *  * Name is null
     *  * Name is empty string
     *  * Length of episode is null
     *  * Length of episode is negative value
     *  * Note is null
     *
     * @param data   validating episode
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Episode, result: Result<Unit>) {
        when {
            data.number == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NULL", message = "Number of episode mustn't be null."))
            }
            data.number <= 0 -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NOT_POSITIVE", message = "Number of episode must be positive number."))
            }
        }
        when {
            data.name == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "EPISODE_NAME_NULL", message = "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "EPISODE_NAME_EMPTY", message = "Name mustn't be empty string."))
            }
        }
        when {
            data.length == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NULL", message = "Length of episode mustn't be null."))
            }
            data.length < 0 -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NEGATIVE", message = "Length of episode mustn't be negative number."))
            }
        }
        if (data.note == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "EPISODE_NOTE_NULL", message = "Note mustn't be null."))
        }
    }

}
