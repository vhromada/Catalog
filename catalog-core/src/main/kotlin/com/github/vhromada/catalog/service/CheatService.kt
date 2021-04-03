package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Cheat
import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.repository.CheatRepository
import com.github.vhromada.catalog.repository.GameRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractChildService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * A class represents service for cheats.
 *
 * @author Vladimir Hromada
 */
@Component("cheatService")
class CheatService(
    private val cheatRepository: CheatRepository,
    private val gameRepository: GameRepository,
    accountProvider: AccountProvider
) : AbstractChildService<Cheat, Game>(repository = cheatRepository, accountProvider = accountProvider) {

    @Transactional
    override fun remove(data: Cheat) {
        val game = getParent(data = data)
        game.cheat = null
        gameRepository.save(game)
    }

    override fun getCopy(data: Cheat): Cheat {
        return data.copy(id = null)
    }

    override fun getAccountData(account: Account, id: Int): Optional<Cheat> {
        return cheatRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun findByParent(parent: Int): List<Cheat> {
        return cheatRepository.findAllByGameId(id = parent)
    }

    override fun getParent(data: Cheat): Game {
        return data.game!!
    }

    override fun getAccountDataList(account: Account, parent: Int): List<Cheat> {
        return cheatRepository.findAllByGameIdAndCreatedUser(id = parent, user = account.uuid!!)
    }

}
