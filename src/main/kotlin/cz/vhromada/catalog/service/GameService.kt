package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Game
import cz.vhromada.catalog.repository.GameRepository
import cz.vhromada.common.entity.Account
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameService")
class GameService(
        private val gameRepository: GameRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Game>(gameRepository, accountProvider, timeProvider, cache, "games") {

    override fun getAccountData(account: Account): List<Game> {
        return gameRepository.findByAuditCreatedUser(account.id)
    }

    override fun getCopy(data: Game): Game {
        return data.copy(id = null)
    }

}
