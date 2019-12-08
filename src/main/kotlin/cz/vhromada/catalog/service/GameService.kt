package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Game
import cz.vhromada.catalog.repository.GameRepository
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
        gameRepository: GameRepository,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Game>(gameRepository, cache, "games") {

    override fun getCopy(data: Game): Game {
        return data.copy(id = null)
    }

}
