package cz.vhromada.catalog.validator

import cz.vhromada.catalog.entity.Program
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Result
import cz.vhromada.validation.result.Severity
import org.springframework.stereotype.Component

/**
 * A class represents validator for program.
 *
 * @author Vladimir Hromada
 */
@Component("programValidator")
class ProgramValidator(programService: MovableService<cz.vhromada.catalog.domain.Program>) : AbstractMovableValidator<Program, cz.vhromada.catalog.domain.Program>("Program", programService) {

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
     *  * Other data is null
     *  * Note is null
     *
     * @param data   validating program
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Program, result: Result<Unit>) {
        if (data.name == null) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null."))
        } else if (data.name.isBlank()) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string."))
        }
        validateUrls(data, result)
        if (data.mediaCount == null) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NULL", "Count of media mustn't be null."))
        } else if (data.mediaCount <= 0) {
            result.addEvent(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."))
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
