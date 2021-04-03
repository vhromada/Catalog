package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.CheatFacade
import com.github.vhromada.common.facade.AbstractChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ChildService
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component
import kotlin.math.min

/**
 * A class represents implementation of facade for cheats.
 *
 * @author Vladimir Hromada
 */
@Component("cheatFacade")
class CheatFacadeImpl(
    cheatService: ChildService<com.github.vhromada.catalog.domain.Cheat>,
    gameService: ParentService<com.github.vhromada.catalog.domain.Game>,
    mapper: Mapper<Cheat, com.github.vhromada.catalog.domain.Cheat>,
    cheatValidator: Validator<Cheat, com.github.vhromada.catalog.domain.Cheat>,
    gameValidator: Validator<Game, com.github.vhromada.catalog.domain.Game>
) : AbstractChildFacade<Cheat, com.github.vhromada.catalog.domain.Cheat, Game, com.github.vhromada.catalog.domain.Game>(
    childService = cheatService,
    parentService = gameService,
    mapper = mapper,
    childValidator = cheatValidator,
    parentValidator = gameValidator
), CheatFacade {

    override fun duplicate(id: Int): Result<Unit> {
        return Result.error("CHEAT_NOT_DUPLICABLE", "Cheat can't be duplicated.")
    }

    override fun moveUp(id: Int): Result<Unit> {
        return Result.error("CHEAT_NOT_MOVABLE", "Cheat can't be moved up.")
    }

    override fun moveDown(id: Int): Result<Unit> {
        return Result.error("CHEAT_NOT_MOVABLE", "Cheat can't be moved down.")
    }

    override fun updateData(data: Cheat): Result<Unit> {
        val storedCheat = service.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedCheat)
        if (validationResult.isOk()) {
            val cheat = mapper.map(source = data)
                .copy(data = getUpdatedCheatData(originalData = storedCheat.get().data, updatedData = data.data!!))
            cheat.createdUser = storedCheat.get().createdUser
            cheat.createdTime = storedCheat.get().createdTime
            cheat.game = storedCheat.get().game
            service.update(data = cheat)
        }
        return validationResult
    }

    override fun addData(parent: com.github.vhromada.catalog.domain.Game, data: Cheat): Result<Unit> {
        if (parent.cheat != null) {
            return Result.error(key = "GAME_CHEAT_EXIST", "Game already has cheat.")
        }

        parent.cheat = mapper.map(source = data)
        parentService.update(data = parent)
        return Result()
    }

    override fun getParent(data: com.github.vhromada.catalog.domain.Cheat): com.github.vhromada.catalog.domain.Game {
        return data.game!!
    }

    /**
     * Updates cheat's data.
     *
     * @param originalData original cheat's data
     * @param updatedData  updated cheat's data
     * @return updated cheat's data
     */
    private fun getUpdatedCheatData(originalData: List<com.github.vhromada.catalog.domain.CheatData>, updatedData: List<CheatData?>): List<com.github.vhromada.catalog.domain.CheatData> {
        val result = mutableListOf<com.github.vhromada.catalog.domain.CheatData>()

        var index = 0
        val max = min(originalData.size, updatedData.size)
        while (index < max) {
            val originalCheatData = originalData[index]
            val updatedCheatData = updatedData[index]!!
            val cheatData = com.github.vhromada.catalog.domain.CheatData(id = originalCheatData.id, action = updatedCheatData.action!!, description = updatedCheatData.description!!)
            cheatData.createdUser = originalCheatData.createdUser
            cheatData.createdTime = originalCheatData.createdTime
            result.add(cheatData)
            index++
        }
        while (index < updatedData.size) {
            val updatedCheatData = updatedData[index]!!
            val cheatData = com.github.vhromada.catalog.domain.CheatData(id = null, action = updatedCheatData.action!!, description = updatedCheatData.description!!)
            result.add(cheatData)
            index++
        }

        return result
    }

}
