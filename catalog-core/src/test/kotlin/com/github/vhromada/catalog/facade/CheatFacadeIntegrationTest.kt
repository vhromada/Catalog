package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
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
 * A class represents integration test for class [CheatFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class CheatFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [CheatFacade]
     */
    @Autowired
    private lateinit var facade: CheatFacade

    /**
     * Test method for [CheatFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..CheatUtils.CHEATS_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            CheatUtils.assertCheatDeepEquals(expected = CheatUtils.getCheatDomain(index = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(CHEAT_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update].
     */
    @Test
    fun update() {
        val cheat = CheatUtils.newCheat(id = 1)
        val expectedCheat = CheatUtils.newCheatDomain(id = 1)
            .fillAudit(audit = AuditUtils.updatedAudit())
        expectedCheat.game = GameUtils.getGame(entityManager = entityManager, id = 2)
        expectedCheat.data.forEach { it.fillAudit(audit = AuditUtils.updatedAudit()) }

        val result = facade.update(data = cheat)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        CheatUtils.assertCheatDeepEquals(expected = expectedCheat, actual = CheatUtils.getCheat(entityManager = entityManager, id = 1)!!)

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT - CheatDataUtils.CHEAT_DATA_CHEAT_COUNT + 1)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with null ID.
     */
    @Test
    fun updateNullId() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(id = null)

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with null setting for game.
     */
    @Test
    fun updateNullGameSetting() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(gameSetting = null)

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_GAME_SETTING_NULL", message = "Setting for game mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with null setting for cheat.
     */
    @Test
    fun updateNullCheatSetting() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(cheatSetting = null)

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_CHEAT_SETTING_NULL", message = "Setting for cheat mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with null cheat's data.
     */
    @Test
    fun updateNullCheatData() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = null)

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_NULL", message = "Cheat's data mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with null value.
     */
    @Test
    fun updateBadCheatData() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), null))

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_CONTAIN_NULL", message = "Cheat's data mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with null action.
     */
    @Test
    fun updateCheatDataWithNullAction() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(action = null)
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_NULL", message = "Cheat's data action mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with empty action.
     */
    @Test
    fun updateCheatDataWithEmptyAction() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(action = "")
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_EMPTY", message = "Cheat's data action mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with null description.
     */
    @Test
    fun updateCheatDataWithNullDescription() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(description = null)
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_NULL", message = "Cheat's data description mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with empty description.
     */
    @Test
    fun updateCheatDataWithEmptyDescription() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(description = "")
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = facade.update(data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_EMPTY", message = "Cheat's data description mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.update] with cheat with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = CheatUtils.newCheat(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(CHEAT_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(CheatUtils.getCheat(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT - 1)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT - CheatDataUtils.CHEAT_DATA_CHEAT_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.remove] with cheat with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(CHEAT_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val result = facade.duplicate(id = CheatUtils.CHEATS_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_DUPLICABLE", message = "Cheat can't be duplicated.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_MOVABLE", message = "Cheat can't be moved up.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_MOVABLE", message = "Cheat can't be moved down.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedCheat = CheatUtils.newCheatDomain(id = CheatUtils.CHEATS_COUNT + 1)
            .copy(data = listOf(CheatDataUtils.newCheatDataDomain(id = CheatDataUtils.CHEAT_DATA_COUNT + 1)))
            .fillAudit(audit = AuditUtils.newAudit())
        expectedCheat.game = GameUtils.getGame(entityManager = entityManager, id = 1)
        expectedCheat.data.forEach { it.fillAudit(audit = AuditUtils.newAudit()) }

        val result = facade.add(parent = 1, data = CheatUtils.newCheat(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        CheatUtils.assertCheatDeepEquals(expected = expectedCheat, actual = CheatUtils.getCheat(entityManager = entityManager, id = CheatUtils.CHEATS_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT + 1)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT + 1)
        }
    }

    /**
     * Test method for [CheatFacade.add] with bad game ID.
     */
    @Test
    fun addBadGameId() {
        val result = facade.add(parent = Int.MAX_VALUE, data = CheatUtils.newCheat(id = null))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GAME_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with not null ID.
     */
    @Test
    fun addNotNullId() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with null setting for game.
     */
    @Test
    fun addNullGameSetting() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(gameSetting = null)

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_GAME_SETTING_NULL", message = "Setting for game mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with null setting for cheat.
     */
    @Test
    fun addNullCheatSetting() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(cheatSetting = null)

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_CHEAT_SETTING_NULL", message = "Setting for cheat mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with null cheat's data.
     */
    @Test
    fun addNullCheatData() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = null)

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_NULL", message = "Cheat's data mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with null value.
     */
    @Test
    fun addBadCheatData() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), null))

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_CONTAIN_NULL", message = "Cheat's data mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with null action.
     */
    @Test
    fun addCheatDataWithNullAction() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(action = null)
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_NULL", message = "Cheat's data action mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with empty action.
     */
    @Test
    fun addCheatDataWithEmptyAction() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(action = "")
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_EMPTY", message = "Cheat's data action mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with null description.
     */
    @Test
    fun addCheatDataWithNullDescription() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(description = null)
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_NULL", message = "Cheat's data description mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with empty description.
     */
    @Test
    fun addCheatDataWithEmptyDescription() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(description = "")
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = facade.add(parent = 1, data = cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_EMPTY", message = "Cheat's data description mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.add] with game with cheat.
     */
    @Test
    fun addGameWithCheat() {
        val result = facade.add(parent = 2, data = CheatUtils.newCheat(id = null))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_CHEAT_EXIST", message = "Game already has cheat.")))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.find].
     */
    @Test
    fun find() {
        for (i in 1..GameUtils.GAMES_COUNT) {
            val result = facade.find(parent = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            CheatUtils.assertCheatListDeepEquals(expected = CheatUtils.getCheats(game = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [CheatFacade.find] with bad game ID.
     */
    @Test
    fun findBadGameId() {
        val result = facade.find(parent = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GAME_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    companion object {

        /**
         * Event for not existing cheat
         */
        private val CHEAT_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "CHEAT_NOT_EXIST", message = "Cheat doesn't exist.")

        /**
         * Event for not existing game
         */
        private val GAME_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "GAME_NOT_EXIST", message = "Game doesn't exist.")

    }

}
