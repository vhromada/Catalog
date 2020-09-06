package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.repository.GameRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameService")
@Suppress("SpringElInspection", "ELValidationInJSP")
class GameService(
        private val gameRepository: GameRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Game>(gameRepository, accountProvider, timeProvider, cache, "games") {

    override fun getAccountData(account: Account): List<Game> {
        return gameRepository.findByAuditCreatedUser(account.uuid!!)
    }

    override fun getCopy(data: Game): Game {
        return data.copy(id = null)
    }

}
