package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * A class represents integration test for class [GameRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class GameRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [GameRepository]
     */
    @Autowired
    private lateinit var repository: GameRepository

    /**
     * Test method for get games.
     */
    @Test
    fun getGames() {
        val games = repository.findAll()

        GameUtils.assertDomainGamesDeepEquals(expected = GameUtils.getGames(), actual = games)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for get game.
     */
    @Test
    fun getGame() {
        for (i in 1..GameUtils.GAMES_COUNT) {
            val game = repository.findById(i).orElse(null)

            GameUtils.assertGameDeepEquals(expected = GameUtils.getGameDomain(index = i), actual = game)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for add game.
     */
    @Test
    fun add() {
        val game = GameUtils.newGameDomain(id = null)
            .copy(position = GameUtils.GAMES_COUNT)
        val expectedGame = GameUtils.newGameDomain(id = GameUtils.GAMES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        repository.save(game)

        assertSoftly {
            it.assertThat(game.id).isEqualTo(GameUtils.GAMES_COUNT + 1)
            it.assertThat(game.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(game.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(game.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(game.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1)!!
        assertThat(addedGame).isNotNull
        GameUtils.assertGameDeepEquals(expected = expectedGame, actual = addedGame)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT + 1)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for update game.
     */
    @Test
    fun update() {
        val game = GameUtils.updateGame(entityManager = entityManager, id = 1)
        val expectedGame = GameUtils.getGameDomain(index = 1)
            .updated()
            .copy(position = GameUtils.POSITION)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(game)

        val updatedGame = GameUtils.getGame(entityManager = entityManager, id = 1)
        assertThat(updatedGame).isNotNull
        GameUtils.assertGameDeepEquals(expected = expectedGame, actual = updatedGame!!)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for update game with added cheat.
     */
    @Test
    @DirtiesContext
    fun updateAddCheat() {
        val game = GameUtils.getGame(entityManager = entityManager, id = 1)!!
        game.cheat = CheatUtils.newCheatDomain(id = null)
        val expectedCheat = CheatUtils.newCheatDomain(id = CheatUtils.CHEATS_COUNT + 1)
            .copy(data = listOf(CheatDataUtils.newCheatDataDomain(id = CheatDataUtils.CHEAT_DATA_COUNT + 1).fillAudit(audit = AuditUtils.newAudit())))
            .fillAudit(audit = AuditUtils.newAudit())
        val expectedGame = GameUtils.getGameDomain(index = 1)
            .fillAudit(audit = AuditUtils.updatedAudit())
        expectedGame.cheat = expectedCheat

        repository.saveAndFlush(game)

        val updatedGame = GameUtils.getGame(entityManager = entityManager, id = 1)
        assertThat(updatedGame).isNotNull
        GameUtils.assertGameDeepEquals(expected = expectedGame, actual = updatedGame!!)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT + 1)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT + 1)
        }
    }

    /**
     * Test method for remove game.
     */
    @Test
    fun remove() {
        repository.delete(GameUtils.getGame(entityManager = entityManager, id = 1)!!)

        assertThat(GameUtils.getGame(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT - 1)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for remove all games.
     */
    @Test
    fun removeAll() {
        repository.deleteAll()

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(0)
        }
    }

    /**
     * Test method for get games for user.
     */
    @Test
    fun findByCreatedUser() {
        val games = repository.findByCreatedUser(user = AuditUtils.getAudit().createdUser!!)

        GameUtils.assertDomainGamesDeepEquals(expected = GameUtils.getGames(), actual = games)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for get game by id for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..GameUtils.GAMES_COUNT) {
            val author = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            GameUtils.assertGameDeepEquals(expected = GameUtils.getGameDomain(index = i), actual = author)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

}
