package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.common.entity.Movable
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents validator for cheat.
 *
 * @author Vladimir Hromada
 */
@Component("cheatValidator")
class CheatValidator(gameService: MovableService<Game>) : AbstractMovableValidator<Cheat, Game>("Cheat", gameService) {

    override fun getData(data: Cheat): Optional<Movable> {
        val cheat = service.getAll()
                .map { it.cheat }
                .find { it?.id == data.id }
        return Optional.ofNullable(cheat)
    }

    override fun getList(data: Cheat): List<com.github.vhromada.catalog.domain.Cheat> {
        val cheat = service.getAll()
                .map { it.cheat }
                .find { it?.id == data.id }
        return if (cheat == null) emptyList() else listOf(cheat)
    }

    /**
     * Validates cheat deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Setting for game is null
     *  * Setting for cheat is null
     *
     * @param data   validating cheat
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Cheat, result: Result<Unit>) {
        if (data.gameSetting == null) {
            result.addEvent(Event(Severity.ERROR, "${getPrefix()}_GAME_SETTING_NULL", "Setting for game mustn't be null."))
        }
        if (data.cheatSetting == null) {
            result.addEvent(Event(Severity.ERROR, "${getPrefix()}_CHEAT_SETTING_NULL", "Setting for cheat mustn't be null."))
        }
    }

}
