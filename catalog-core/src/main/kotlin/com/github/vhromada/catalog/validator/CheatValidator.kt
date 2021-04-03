package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.validator.AbstractValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for cheat.
 *
 * @author Vladimir Hromada
 */
@Component("cheatValidator")
class CheatValidator : AbstractValidator<Cheat, com.github.vhromada.catalog.domain.Cheat>(name = "Cheat") {

    /**
     * Validates cheat deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Setting for game is null
     *  * Setting for cheat is null
     *  * Cheat's data are null
     *  * Cheat's data contain null value
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *
     * @param data   validating cheat
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Cheat, result: Result<Unit>) {
        if (data.gameSetting == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "CHEAT_GAME_SETTING_NULL", message = "Setting for game mustn't be null."))
        }
        if (data.cheatSetting == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "CHEAT_CHEAT_SETTING_NULL", message = "Setting for cheat mustn't be null."))
        }
        validateCheatData(cheat = data, result = result)
    }

    /**
     * Validates cheat's data.
     * <br></br>
     * Validation errors:
     *
     *  * Cheat's data are null
     *  * Cheat's data contain null value
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *
     * @param cheat  validating cheat
     * @param result result with validation errors
     */
    private fun validateCheatData(cheat: Cheat, result: Result<Unit>) {
        if (cheat.data == null) {
            result.addEvent(event = Event(severity = Severity.ERROR, key = "CHEAT_DATA_NULL", message = "Cheat's data mustn't be null."))
        } else {
            if (cheat.data.contains(null)) {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "CHEAT_DATA_CONTAIN_NULL", message = "Cheat's data mustn't contain null value."))
            }
            for (cheatData in cheat.data) {
                if (cheatData != null) {
                    when {
                        cheatData.action == null -> {
                            result.addEvent(event = Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_NULL", message = "Cheat's data action mustn't be null."))
                        }
                        cheatData.action.isBlank() -> {
                            result.addEvent(event = Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_EMPTY", message = "Cheat's data action mustn't be empty string."))
                        }
                    }
                    when {
                        cheatData.description == null -> {
                            result.addEvent(event = Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_NULL", message = "Cheat's data description mustn't be null."))
                        }
                        cheatData.description.isBlank() -> {
                            result.addEvent(event = Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_EMPTY", message = "Cheat's data description mustn't be empty string."))
                        }
                    }
                }
            }
        }
    }

}
