package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for program.
 *
 * @author Vladimir Hromada
 */
@Component("programValidator")
class ProgramValidator(programService: MovableService<com.github.vhromada.catalog.domain.Program>) : AbstractMovableValidator<Program, com.github.vhromada.catalog.domain.Program>("Program", programService) {

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
                result.addEvent(Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string."))
            }
        }
        validateUrls(data, result)
        when {
            data.mediaCount == null -> {
                result.addEvent(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NULL", "Count of media mustn't be null."))
            }
            data.mediaCount <= 0 -> {
                result.addEvent(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."))
            }
        }
        if (data.format == null) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_FORMAT_NULL", "Format mustn't be null."))
        }
        if (data.otherData == null) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null."))
        }
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null."))
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
     * @param data   validating show
     * @param result result with validation errors
     */
    private fun validateUrls(data: Program, result: Result<Unit>) {
        if (data.wikiEn == null) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL", "URL to english Wikipedia page about program mustn't be null."))
        }
        if (data.wikiCz == null) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL", "URL to czech Wikipedia page about program mustn't be null."))
        }
    }

}
