package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.CheatFacade
import com.github.vhromada.common.facade.AbstractMovableChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for cheats.
 *
 * @author Vladimir Hromada
 */
@Component("cheatFacade")
class CheatFacadeImpl(
        gameService: MovableService<com.github.vhromada.catalog.domain.Game>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Cheat, com.github.vhromada.catalog.domain.Cheat>,
        gameValidator: MovableValidator<Game>,
        cheatValidator: MovableValidator<Cheat>
) : AbstractMovableChildFacade<Cheat, com.github.vhromada.catalog.domain.Cheat, Game, com.github.vhromada.catalog.domain.Game>(gameService, accountProvider, timeProvider, mapper, gameValidator,
        cheatValidator), CheatFacade {

    override fun add(parent: Game, data: Cheat): Result<Unit> {
        return super.add(parent, data.copy(position = null))
    }

    override fun update(data: Cheat): Result<Unit> {
        return super.update(data.copy(position = -1))
    }

    override fun duplicate(data: Cheat): Result<Unit> {
        return Result.error("CHEAT_NOT_DUPLICABLE", "Cheat can't be duplicated.")
    }

    override fun moveUp(data: Cheat): Result<Unit> {
        return Result.error("CHEAT_NOT_MOVABLE", "Cheat can't be moved up.")
    }

    override fun moveDown(data: Cheat): Result<Unit> {
        return Result.error("CHEAT_NOT_MOVABLE", "Cheat can't be moved down.")
    }

    override fun customAddDataValidation(parentValidator: MovableValidator<Game>, dataValidator: MovableValidator<Cheat>, parent: Game, data: Cheat, result: Result<Unit>) {
        if (parent.id != null) {
            val cheat = service.get(parent.id!!)
                    .map { it.cheat }
            if (cheat.isPresent) {
                result.addEvent(Event(Severity.ERROR, "GAME_CHEAT_EXIST", "Game already has cheat."))
            }
        }
    }

    override fun getDomainData(id: Int): com.github.vhromada.catalog.domain.Cheat? {
        return service.getAll()
                .map { it.cheat }
                .find { it?.id == id }
    }

    override fun getDomainList(parent: Game): List<com.github.vhromada.catalog.domain.Cheat> {
        val cheat = service.get(parent.id!!).get().cheat
        return if (cheat == null) emptyList() else listOf(cheat)
    }

    override fun getForAdd(parent: Game, data: com.github.vhromada.catalog.domain.Cheat): com.github.vhromada.catalog.domain.Game {
        return service.get(parent.id!!).get()
                .copy(cheat = data)
    }

    override fun getForUpdate(data: Cheat): com.github.vhromada.catalog.domain.Game {
        val game = getGame(data)
        val audit = getAudit()
        val cheat = getDataForUpdate(data)
        cheat.audit = game.cheat!!.audit!!.copy(updatedUser = audit.updatedUser, updatedTime = audit.updatedTime)
        return game.copy(cheat = cheat)
    }

    override fun getForRemove(data: Cheat): com.github.vhromada.catalog.domain.Game {
        return getGame(data)
                .copy(cheat = null)
    }

    override fun getForDuplicate(data: Cheat): com.github.vhromada.catalog.domain.Game {
        throw UnsupportedOperationException("Not supported")
    }

    override fun getForMove(data: Cheat, up: Boolean): com.github.vhromada.catalog.domain.Game {
        throw UnsupportedOperationException("Not supported")
    }

    /**
     * Returns game for specified cheat.
     *
     * @param cheat cheat
     * @return game for specified cheat
     */
    private fun getGame(cheat: Cheat): com.github.vhromada.catalog.domain.Game {
        val game = service.getAll()
                .find { it.cheat?.id == cheat.id }
        return game ?: throw IllegalStateException("Unknown cheat.")
    }

}
