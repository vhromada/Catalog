package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

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
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [GameRepository]
     */
    @Autowired
    private lateinit var gameRepository: GameRepository

    /**
     * Test method for get games.
     */
    @Test
    fun getGames() {
        val games = gameRepository.findAll()

        GameUtils.assertGamesDeepEquals(GameUtils.getGames(), games)

        assertThat(GameUtils.getGamesCount(entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
    }

    /**
     * Test method for get game.
     */
    @Test
    @Suppress("UsePropertyAccessSyntax")
    fun getGame() {
        for (i in 1..GameUtils.GAMES_COUNT) {
            val game = gameRepository.findById(i).orElse(null)

            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), game)
        }

        assertThat(gameRepository.findById(Int.MAX_VALUE).isPresent).isFalse()

        assertThat(GameUtils.getGamesCount(entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
    }

    /**
     * Test method for add game.
     */
    @Test
    fun add() {
        val audit = AuditUtils.getAudit()
        val game = GameUtils.newGameDomain(null)
                .copy(position = GameUtils.GAMES_COUNT, audit = audit)

        gameRepository.save(game)

        assertThat(game.id).isEqualTo(GameUtils.GAMES_COUNT + 1)

        val addedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1)!!
        val expectedAddGame = GameUtils.newGameDomain(null)
                .copy(id = GameUtils.GAMES_COUNT + 1, position = GameUtils.GAMES_COUNT, audit = audit)
        GameUtils.assertGameDeepEquals(expectedAddGame, addedGame)

        assertThat(GameUtils.getGamesCount(entityManager)).isEqualTo(GameUtils.GAMES_COUNT + 1)
    }

    /**
     * Test method for update game.
     */
    @Test
    fun update() {
        val game = GameUtils.updateGame(entityManager, 1)

        gameRepository.save(game)

        val updatedGame = GameUtils.getGame(entityManager, 1)!!
        val expectedUpdatedGame = GameUtils.getGame(1)
                .updated()
                .copy(position = GameUtils.POSITION)
        GameUtils.assertGameDeepEquals(expectedUpdatedGame, updatedGame)

        assertThat(GameUtils.getGamesCount(entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
    }

    /**
     * Test method for remove game.
     */
    @Test
    fun remove() {
        gameRepository.delete(GameUtils.getGame(entityManager, 1)!!)

        assertThat(GameUtils.getGame(entityManager, 1)).isNull()

        assertThat(GameUtils.getGamesCount(entityManager)).isEqualTo(GameUtils.GAMES_COUNT - 1)
    }

    /**
     * Test method for remove all games.
     */
    @Test
    fun removeAll() {
        gameRepository.deleteAll()

        assertThat(GameUtils.getGamesCount(entityManager)).isEqualTo(0)
    }

    /**
     * Test method for get games for user.
     */
    @Test
    fun findByAuditCreatedUser() {
        val games = gameRepository.findByAuditCreatedUser(AuditUtils.getAudit().createdUser)

        GameUtils.assertGamesDeepEquals(GameUtils.getGames(), games)

        assertThat(GameUtils.getGamesCount(entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
    }

}
