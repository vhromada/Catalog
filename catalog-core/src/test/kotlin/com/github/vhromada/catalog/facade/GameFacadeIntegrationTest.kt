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
 * A class represents integration test for class [GameFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class GameFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [GameFacade]
     */
    @Autowired
    private lateinit var facade: GameFacade

    /**
     * Test method for [GameFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..GameUtils.GAMES_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            GameUtils.assertGameDeepEquals(expected = GameUtils.getGameDomain(index = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GAME_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update].
     */
    @Test
    fun update() {
        val game = GameUtils.newGame(id = 1)
        val expectedGame = GameUtils.newGameDomain(id = 1)
            .fillAudit(audit = AuditUtils.updatedAudit())

        val result = facade.update(data = game)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        GameUtils.assertGameDeepEquals(expected = expectedGame, actual = GameUtils.getGame(entityManager = entityManager, id = 1)!!)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null ID.
     */
    @Test
    fun updateNullId() {
        val game = GameUtils.newGame(id = 1)
            .copy(id = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null position.
     */
    @Test
    fun updateNullPosition() {
        val game = GameUtils.newGame(id = 1)
            .copy(position = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null name.
     */
    @Test
    fun updateNullName() {
        val game = GameUtils.newGame(id = 1)
            .copy(name = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val game = GameUtils.newGame(id = 1)
            .copy(name = "")

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null URL to english Wikipedia about game.
     */
    @Test
    fun updateNullWikiEn() {
        val game = GameUtils.newGame(id = 1)
            .copy(wikiEn = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_WIKI_EN_NULL", message = "URL to english Wikipedia page about game mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null URL to czech Wikipedia about game.
     */
    @Test
    fun updateNullWikiCz() {
        val game = GameUtils.newGame(id = 1)
            .copy(wikiCz = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about game mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null count of media.
     */
    @Test
    fun updateNullMediaCount() {
        val game = GameUtils.newGame(id = 1)
            .copy(mediaCount = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with not positive count of media.
     */
    @Test
    fun updateNotPositiveMediaCount() {
        val game = GameUtils.newGame(id = 1)
            .copy(mediaCount = 0)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null format.
     */
    @Test
    fun updateNullFormat() {
        val game = GameUtils.newGame(id = 1)
            .copy(format = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_FORMAT_NULL", message = "Format mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null other data.
     */
    @Test
    fun updateNullOtherData() {
        val game = GameUtils.newGame(id = 1)
            .copy(otherData = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_OTHER_DATA_NULL", message = "Other data mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with null note.
     */
    @Test
    fun updateNullNote() {
        val game = GameUtils.newGame(id = 1)
            .copy(note = null)

        val result = facade.update(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.update] with game with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = GameUtils.newGame(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GAME_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(GameUtils.getGame(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT - 1)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.remove] with game with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GAME_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        val expectedGame = GameUtils.getGameDomain(index = GameUtils.GAMES_COUNT)
            .copy(id = GameUtils.GAMES_COUNT + 1, cheat = null)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.duplicate(id = GameUtils.GAMES_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        GameUtils.assertGameDeepEquals(expected = expectedGame, actual = GameUtils.getGame(entityManager = entityManager, id = GameUtils.GAMES_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT + 1)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.duplicate] with game with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GAME_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val game1 = GameUtils.getGameDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val game2 = GameUtils.getGameDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        GameUtils.assertGameDeepEquals(expected = game1, actual = GameUtils.getGame(entityManager = entityManager, id = 1)!!)
        GameUtils.assertGameDeepEquals(expected = game2, actual = GameUtils.getGame(entityManager = entityManager, id = 2)!!)
        for (i in 3..GameUtils.GAMES_COUNT) {
            GameUtils.assertGameDeepEquals(expected = GameUtils.getGameDomain(i), actual = GameUtils.getGame(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.moveUp] with not movable game.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOT_MOVABLE", message = "Game can't be moved up.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.moveUp] with game with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GAME_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val game1 = GameUtils.getGameDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val game2 = GameUtils.getGameDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        GameUtils.assertGameDeepEquals(expected = game1, actual = GameUtils.getGame(entityManager = entityManager, id = 1)!!)
        GameUtils.assertGameDeepEquals(expected = game2, actual = GameUtils.getGame(entityManager = entityManager, id = 2)!!)
        for (i in 3..GameUtils.GAMES_COUNT) {
            GameUtils.assertGameDeepEquals(expected = GameUtils.getGameDomain(i), actual = GameUtils.getGame(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.moveDown] with not movable game.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = GameUtils.GAMES_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOT_MOVABLE", message = "Game can't be moved down.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.moveDown] with game with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GAME_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.newData].
     */
    @Test
    fun newData() {
        val result = facade.newData()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(0)
        }
    }

    /**
     * Test method for [GameFacade.getAll].
     */
    @Test
    fun getAll() {
        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isNotNull
            it.assertThat(result.events()).isEmpty()
        }
        GameUtils.assertGameListDeepEquals(expected = GameUtils.getGames(), actual = result.data!!)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedGame = GameUtils.newGameDomain(id = GameUtils.GAMES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.add(GameUtils.newGame(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        GameUtils.assertGameDeepEquals(expected = expectedGame, actual = GameUtils.getGame(entityManager = entityManager, id = GameUtils.GAMES_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT + 1)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with not null ID.
     */
    @Test
    fun addNotNullId() {
        val game = GameUtils.newGame(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val game = GameUtils.newGame(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with null name.
     */
    @Test
    fun addNullName() {
        val game = GameUtils.newGame(id = null)
            .copy(name = null)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val game = GameUtils.newGame(id = null)
            .copy(name = "")

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with null URL to english Wikipedia about game.
     */
    @Test
    fun addNullWikiEn() {
        val game = GameUtils.newGame(id = null)
            .copy(wikiEn = null)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_WIKI_EN_NULL", message = "URL to english Wikipedia page about game mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with null URL to czech Wikipedia about game.
     */
    @Test
    fun addNullWikiCz() {
        val game = GameUtils.newGame(id = null)
            .copy(wikiCz = null)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about game mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with null count of media.
     */
    @Test
    fun addNullMediaCount() {
        val game = GameUtils.newGame(id = null)
            .copy(mediaCount = null)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with not positive count of media.
     */
    @Test
    fun addNotPositiveMediaCount() {
        val game = GameUtils.newGame(id = null)
            .copy(mediaCount = 0)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with null format.
     */
    @Test
    fun addNullFormat() {
        val game = GameUtils.newGame(id = null)
            .copy(format = null)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_FORMAT_NULL", message = "Format mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with null other data.
     */
    @Test
    fun addNullOtherData() {
        val game = GameUtils.newGame(id = null)
            .copy(otherData = null)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_OTHER_DATA_NULL", message = "Other data mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.add] with game with null note.
     */
    @Test
    fun addNullNote() {
        val game = GameUtils.newGame(id = null)
            .copy(note = null)

        val result = facade.add(data = game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        for (i in 1..GameUtils.GAMES_COUNT) {
            val expectedGame = GameUtils.getGameDomain(index = i)
                .copy(position = i - 1)
                .fillAudit(audit = AuditUtils.updatedAudit())
            GameUtils.assertGameDeepEquals(expected = expectedGame, actual = GameUtils.getGame(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    /**
     * Test method for [GameFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val result = facade.getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(6)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager = entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager = entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager = entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    companion object {

        /**
         * Event for not existing game
         */
        private val GAME_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "GAME_NOT_EXIST", message = "Game doesn't exist.")

    }

}
