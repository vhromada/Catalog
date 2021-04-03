package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.repository.GameRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractParentService
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents service for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameService")
class GameService(
    private val gameRepository: GameRepository,
    accountProvider: AccountProvider
) : AbstractParentService<Game>(repository = gameRepository, accountProvider = accountProvider) {

    override fun getCopy(data: Game): Game {
        return data.copy(id = null, cheat = null)
    }

    override fun getAccountData(account: Account, id: Int): Optional<Game> {
        return gameRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun getAccountDataList(account: Account): List<Game> {
        return gameRepository.findByCreatedUser(user = account.uuid!!)
    }

}
