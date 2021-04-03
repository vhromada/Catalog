package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.validator.AbstractValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for program.
 *
 * @author Vladimir Hromada
 */
@Component("programValidator")
class ProgramValidator : AbstractValidator<Program, com.github.vhromada.catalog.domain.Program>(name = "Program") {

    /**
     * Validates program deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about program is null
     *  * URL to czech Wikipedia page about program is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Format is null
     *  * Other data is null
     *  * Note is null
     *
     * @param data   validating program
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Program, result: Result<Unit>) {
        when {
            data.name == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_NAME_NULL", message = "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_NAME_EMPTY", message = "Name mustn't be empty string."))
            }
        }
        validateUrls(program = data, result = result)
        when {
            data.mediaCount == null -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NULL", message = "Count of media mustn't be null."))
            }
            data.mediaCount <= 0 -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number."))
            }
        }
        if (data.format == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_FORMAT_NULL", message = "Format mustn't be null."))
        }
        if (data.otherData == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_OTHER_DATA_NULL", message = "Other data mustn't be null."))
        }
        if (data.note == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_NOTE_NULL", message = "Note mustn't be null."))
        }
    }

    /**
     * Validates URLs.
     * <br></br>
     * Validation errors:
     *
     *  * URL to english Wikipedia page about program is null
     *  * URL to czech Wikipedia page about program is null
     *
     * @param program validating program
     * @param result  result with validation errors
     */
    private fun validateUrls(program: Program, result: Result<Unit>) {
        if (program.wikiEn == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_EN_NULL", message = "URL to english Wikipedia page about program mustn't be null."))
        }
        if (program.wikiCz == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about program mustn't be null."))
        }
    }

}
