package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
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
 * A class represents integration test for class [CheatRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class CheatRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [CheatRepository]
     */
    @Autowired
    private lateinit var repository: CheatRepository

    /**
     * Test method for get cheat.
     */
    @Test
    fun getCheat() {
        for (i in 1..CheatUtils.CHEATS_COUNT) {
            val cheat = repository.findById(i).orElse(null)

            CheatUtils.assertCheatDeepEquals(expected = CheatUtils.getCheatDomain(index = i), actual = cheat)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for update cheat.
     */
    @Test
    fun update() {
        val cheat = CheatUtils.updateCheat(entityManager = entityManager, id = 1)
        val expectedCheat = CheatUtils.getCheatDomain(index = 1)
            .updated()
            .fillAudit(AuditUtils.updatedAudit())
        expectedCheat.game = GameUtils.getGame(entityManager = entityManager, id = 1)

        repository.saveAndFlush(cheat)

        val updatedCheat = CheatUtils.getCheat(entityManager = entityManager, id = 1)
        assertThat(updatedCheat).isNotNull
        CheatUtils.assertCheatDeepEquals(expectedCheat, updatedCheat!!)

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for update cheat with added cheat's data.
     */
    @Test
    @DirtiesContext
    fun updateAddedData() {
        var cheat = CheatUtils.updateCheat(entityManager = entityManager, id = 1)
        val data = cheat.data.toMutableList()
        data.add(CheatDataUtils.newCheatDataDomain(id = null))
        cheat = cheat.copy(data = data)
            .fillAudit(audit = cheat)
        var expectedCheat = CheatUtils.getCheatDomain(index = 1)
            .updated()
        val expectedData = expectedCheat.data.toMutableList()
        expectedData.add(CheatDataUtils.newCheatDataDomain(id = CheatDataUtils.CHEAT_DATA_COUNT + 1).fillAudit(audit = AuditUtils.newAudit()))
        expectedCheat = expectedCheat.copy(data = expectedData)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(cheat)

        val updatedCheat = CheatUtils.getCheat(entityManager, 1)
        assertThat(updatedCheat).isNotNull
        CheatUtils.assertCheatDeepEquals(expected = expectedCheat, actual = updatedCheat!!)

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT + 1)
        }
    }

    /**
     * Test method for update cheat with removed cheat's data.
     */
    @Test
    fun updateRemovedData() {
        var cheat = CheatUtils.updateCheat(entityManager = entityManager, id = 1)
        cheat = cheat.copy(data = emptyList())
            .fillAudit(audit = cheat)
        val expectedCheat = CheatUtils.getCheatDomain(index = 1)
            .updated()
            .copy(data = emptyList())
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(cheat)

        val updatedCheat = CheatUtils.getCheat(entityManager, 1)
        assertThat(updatedCheat).isNotNull
        CheatUtils.assertCheatDeepEquals(expected = expectedCheat, actual = updatedCheat!!)

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT - CheatUtils.getCheatDomain(index = 1).data.size)
        }
    }

    /**
     * Test method for get cheats by game.
     */
    @Test
    fun findAllByGameId() {
        for (i in 1..GameUtils.GAMES_COUNT) {
            val cheats = repository.findAllByGameId(id = i)

            CheatUtils.assertDomainCheatsDeepEquals(expected = CheatUtils.getCheats(game = i), actual = cheats)
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for get cheats for user by game.
     */
    @Test
    fun findAllByGameIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..GameUtils.GAMES_COUNT) {
            val cheats = repository.findAllByGameIdAndCreatedUser(id = i, user = user)

            CheatUtils.assertDomainCheatsDeepEquals(expected = CheatUtils.getCheats(game = i), actual = cheats)
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for get cheat by ID for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..CheatUtils.CHEATS_COUNT) {
            val cheat = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            CheatUtils.assertCheatDeepEquals(expected = CheatUtils.getCheatDomain(index = i), actual = cheat)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

}
