package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.domain.Game
import cz.vhromada.catalog.repository.GameRepository
import cz.vhromada.catalog.utils.GameUtils
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import org.mockito.Mock
import org.springframework.data.jpa.repository.JpaRepository

/**
 * A class represents test for class [GameService].
 *
 * @author Vladimir Hromada
 */
class GameServiceTest : MovableServiceTest<Game>() {

    /**
     * Instance of [GameRepository]
     */
    @Mock
    private lateinit var repository: GameRepository

    override fun getRepository(): JpaRepository<Game, Int> {
        return repository
    }

    override fun getService(): MovableService<Game> {
        return GameService(repository, cache)
    }

    override fun getCacheKey(): String {
        return "games"
    }

    override fun getItem1(): Game {
        return GameUtils.newGameDomain(1)
    }

    override fun getItem2(): Game {
        return GameUtils.newGameDomain(2)
    }

    override fun getAddItem(): Game {
        return GameUtils.newGameDomain(null)
    }

    override fun getCopyItem(): Game {
        val game = GameUtils.newGameDomain(null)
        game.position = 0

        return game
    }

    override fun anyItem(): Game {
        return any()
    }

    override fun argumentCaptorItem(): KArgumentCaptor<Game> {
        return argumentCaptor()
    }

    override fun assertDataDeepEquals(expected: Game, actual: Game) {
        GameUtils.assertGameDeepEquals(expected, actual)
    }

}
