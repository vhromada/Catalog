package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.facade.CheatDataFacade
import com.github.vhromada.common.entity.Movable
import com.github.vhromada.common.facade.AbstractMovableChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.sorted
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for cheat's data.
 *
 * @author Vladimir Hromada
 */
@Component("cheatDataFacade")
class CheatDataFacadeImpl(
        gameService: MovableService<Game>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<CheatData, com.github.vhromada.catalog.domain.CheatData>,
        cheatValidator: MovableValidator<Cheat>,
        cheatDataValidator: MovableValidator<CheatData>
) : AbstractMovableChildFacade<CheatData, com.github.vhromada.catalog.domain.CheatData, Cheat, Game>(gameService, accountProvider, timeProvider, mapper, cheatValidator, cheatDataValidator), CheatDataFacade {

    override fun getDomainData(id: Int): com.github.vhromada.catalog.domain.CheatData? {
        for (game in service.getAll()) {
            if (game.cheat != null) {
                for (cheatData in game.cheat.data) {
                    if (id == cheatData.id) {
                        return cheatData
                    }
                }
            }
        }

        return null
    }

    override fun getDomainList(parent: Cheat): List<com.github.vhromada.catalog.domain.CheatData> {
        for (game in service.getAll()) {
            if (game.cheat?.id == parent.id) {
                return game.cheat!!.data
            }
        }

        return emptyList()
    }

    override fun getForAdd(parent: Cheat, data: com.github.vhromada.catalog.domain.CheatData): Game {
        val game = getGame(parent)
        val cheat = getCheat(game, parent)
        val cheatData = cheat.data.toMutableList()
        cheatData.add(data)

        return game.copy(cheat = cheat.copy(data = cheatData))
    }

    override fun getForUpdate(data: CheatData): Game {
        val game = getGame(data)
        val cheat = getCheat(game, data)
        val cheatData = getDataForUpdate(data)

        return game.copy(cheat = cheat.copy(data = updateCheatData(cheat, cheatData)))
    }

    override fun getForRemove(data: CheatData): Game {
        val game = getGame(data)
        val cheat = getCheat(game, data)
        val cheatData = cheat.data.filter { it.id != data.id }

        return game.copy(cheat = cheat.copy(data = cheatData))
    }

    override fun getForDuplicate(data: CheatData): Game {
        val game = getGame(data)
        val cheat = getCheat(game, data)
        val cheatData = getCheatData(data.id, game)
        val dataList = cheat.data.toMutableList()
        dataList.add(cheatData.copy(id = null))

        return game.copy(cheat = cheat.copy(data = dataList))
    }

    @Suppress("DuplicatedCode")
    override fun getForMove(data: CheatData, up: Boolean): Game {
        var game = getGame(data)
        val cheat = getCheat(game, data)
        val cheatData = getCheatData(data.id, game)
        val dataList = cheat.data.sorted()

        val index = dataList.indexOf(cheatData)
        val other = dataList[if (up) index - 1 else index + 1]
        val position = cheatData.position!!
        cheatData.position = other.position
        other.position = position

        game = game.copy(cheat = cheat.copy(data = updateCheatData(cheat, cheatData)))
        return game.copy(cheat = cheat.copy(data = updateCheatData(cheat, other)))
    }

    /**
     * Returns game for cheat.
     *
     * @param cheat cheat
     * @return game for cheat
     */
    private fun getGame(cheat: Cheat): Game {
        for (game in service.getAll()) {
            if (game.cheat?.id == cheat.id) {
                return game
            }
        }

        throw IllegalStateException("Unknown cheat.")
    }

    /**
     * Returns game for cheat's data.
     *
     * @param cheatData cheat's data
     * @return game for cheat's data
     */
    private fun getGame(cheatData: CheatData): Game {
        for (game in service.getAll()) {
            if (game.cheat != null) {
                for (cheatDataDomain in game.cheat.data) {
                    if (cheatData.id == cheatDataDomain.id) {
                        return game
                    }
                }
            }
        }

        throw IllegalStateException("Unknown cheat's data.")
    }

    /**
     * Returns cheat for game.
     *
     * @param game   game
     * @param cheat cheat
     * @return cheat for game
     */
    private fun getCheat(game: Game, cheat: Cheat): com.github.vhromada.catalog.domain.Cheat {
        if (game.cheat?.id == cheat.id) {
            return game.cheat!!
        }

        throw IllegalStateException("Unknown cheat")
    }

    /**
     * Returns cheat for cheat's data.
     *
     * @param game      game
     * @param cheatData cheat's data
     * @return cheat for cheat's data
     */
    private fun getCheat(game: Game, cheatData: Movable): com.github.vhromada.catalog.domain.Cheat {
        if (game.cheat != null) {
            for ((id) in game.cheat.data) {
                if (cheatData.id == id) {
                    return game.cheat
                }
            }
        }

        throw IllegalStateException("Unknown cheat's data")
    }

    /**
     * Returns cheat's data with ID.
     *
     * @param id   ID
     * @param game game
     * @return cheat's data with ID
     */
    private fun getCheatData(id: Int?, game: Game): com.github.vhromada.catalog.domain.CheatData {
        if (game.cheat != null) {
            for (cheatData in game.cheat.data) {
                if (id == cheatData.id) {
                    return cheatData
                }
            }
        }

        throw IllegalStateException("Unknown cheat's Data")
    }

    /**
     * Updates cheat's data.
     *
     * @param cheat     cheat
     * @param cheatData cheat's data
     * @return updated cheat's data
     */
    @Suppress("DuplicatedCode")
    private fun updateCheatData(cheat: com.github.vhromada.catalog.domain.Cheat, cheatData: com.github.vhromada.catalog.domain.CheatData): List<com.github.vhromada.catalog.domain.CheatData> {
        val cheatDataList = mutableListOf<com.github.vhromada.catalog.domain.CheatData>()
        for (cheatDataDomain in cheat.data) {
            if (cheatDataDomain == cheatData) {
                val audit = getAudit()
                cheatData.audit = cheatDataDomain.audit!!.copy(updatedUser = audit.updatedUser, updatedTime = audit.updatedTime)
                cheatDataList.add(cheatData)
            } else {
                cheatDataList.add(cheatDataDomain)
            }
        }
        return cheatDataList
    }

}
