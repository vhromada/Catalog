package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.common.entity.Movable
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.sorted
import com.github.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents validator for cheat's data.
 *
 * @author Vladimir Hromada
 */
@Component("cheatDataValidator")
class CheatDataValidator(gameService: MovableService<Game>) : AbstractMovableValidator<CheatData, Game>("Cheat's data", gameService) {

    override fun getData(data: CheatData): Optional<Movable> {
        for (game in service.getAll()) {
            if (game.cheat != null) {
                for (cheatData in game.cheat.data) {
                    if (data.id == cheatData.id) {
                        return Optional.of(cheatData)
                    }
                }
            }
        }
        return Optional.empty()
    }

    override fun getList(data: CheatData): List<com.github.vhromada.catalog.domain.CheatData> {
        for (game in service.getAll()) {
            if (game.cheat != null) {
                for (cheatData in game.cheat.data) {
                    return game.cheat.data
                            .sorted()
                }
            }
        }

        return emptyList()
    }

    override fun getPrefix(): String {
        return "CHEAT_DATA"
    }

    /**
     * Validates cheat's data deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Action is null
     *  * Action is empty string
     *  * Description is null
     *  * Description is empty string
     *
     * @param data   validating cheat's data
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: CheatData, result: Result<Unit>) {
        when {
            data.action == null -> {
                result.addEvent(Event(Severity.ERROR, "${getPrefix()}_ACTION_NULL", "Action mustn't be null."))
            }
            data.action.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "${getPrefix()}_ACTION_EMPTY", "Action mustn't be empty string."))
            }
        }
        when {
            data.description == null -> {
                result.addEvent(Event(Severity.ERROR, "${getPrefix()}_DESCRIPTION_NULL", "Description mustn't be null."))
            }
            data.description.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "${getPrefix()}_DESCRIPTION_EMPTY", "Description mustn't be empty string."))
            }
        }
    }

}
