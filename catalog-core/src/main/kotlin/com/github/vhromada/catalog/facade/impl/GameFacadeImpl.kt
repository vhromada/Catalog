package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.GameFacade
import com.github.vhromada.common.facade.AbstractParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameFacade")
class GameFacadeImpl(
    private val gameService: ParentService<com.github.vhromada.catalog.domain.Game>,
    mapper: Mapper<Game, com.github.vhromada.catalog.domain.Game>,
    gameValidator: Validator<Game, com.github.vhromada.catalog.domain.Game>
) : AbstractParentFacade<Game, com.github.vhromada.catalog.domain.Game>(parentService = gameService, mapper = mapper, validator = gameValidator), GameFacade {

    @Suppress("DuplicatedCode")
    override fun updateData(data: Game): Result<Unit> {
        val storedGame = gameService.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedGame)
        if (validationResult.isOk()) {
            val game = mapper.map(source = data)
            game.createdUser = storedGame.get().createdUser
            game.createdTime = storedGame.get().createdTime
            gameService.update(data = game)
        }
        return validationResult
    }

    override fun addData(data: Game): Result<Unit> {
        gameService.add(data = mapper.map(source = data))
        return Result()
    }

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(data = gameService.getAll().sumBy { it.mediaCount })
    }

}
