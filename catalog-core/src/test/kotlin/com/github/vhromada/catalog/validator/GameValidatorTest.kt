package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [GameValidator].
 *
 * @author Vladimir Hromada
 */
class GameValidatorTest {

    /**
     * Instance of [GameValidator]
     */
    private lateinit var validator: GameValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = GameValidator()
    }

    /**
     * Test method for [GameValidator.validate] with correct new game.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = GameUtils.newGame(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GameValidator.validate] with null new game.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NULL", message = "Game mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val game = GameUtils.newGame(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val game = GameUtils.newGame(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_POSITION_NOT_NULL", message = "Position must be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with null name.
     */
    @Test
    fun validateNewNullName() {
        val game = GameUtils.newGame(id = null)
            .copy(name = null)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with empty name.
     */
    @Test
    fun validateNewEmptyName() {
        val game = GameUtils.newGame(id = null)
            .copy(name = "")

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with null URL to english Wikipedia page about game.
     */
    @Test
    fun validateNewNullWikiEn() {
        val game = GameUtils.newGame(id = null)
            .copy(wikiEn = null)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_WIKI_EN_NULL", message = "URL to english Wikipedia page about game mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with null URL to czech Wikipedia page about game.
     */
    @Test
    fun validateNewNullWikiCz() {
        val game = GameUtils.newGame(id = null)
            .copy(wikiCz = null)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about game mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with null count of media.
     */
    @Test
    fun validateNewNullMediaCount() {
        val game = GameUtils.newGame(id = null)
            .copy(mediaCount = null)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with  not positive count of media.
     */
    @Test
    fun validateNewNotPositiveMediaCount() {
        val game = GameUtils.newGame(id = null)
            .copy(mediaCount = 0)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with null format.
     */
    @Test
    fun validateNewNullFormat() {
        val game = GameUtils.newGame(id = null)
            .copy(format = null)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_FORMAT_NULL", message = "Format mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with null other data.
     */
    @Test
    fun validateNewNullOtherData() {
        val game = GameUtils.newGame(id = null)
            .copy(otherData = null)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_OTHER_DATA_NULL", message = "Other data mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with new game with null note.
     */
    @Test
    fun validateNewNullNote() {
        val game = GameUtils.newGame(id = null)
            .copy(note = null)

        val result = validator.validate(data = game, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with with update correct game.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = GameUtils.newGame(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GameValidator.validate] with null update game.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NULL", message = "Game mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val game = GameUtils.newGame(id = 1)
            .copy(id = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val game = GameUtils.newGame(id = 1)
            .copy(position = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_POSITION_NULL", message = "Position mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null name.
     */
    @Test
    fun validateUpdateNullName() {
        val game = GameUtils.newGame(id = 1)
            .copy(name = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with empty name.
     */
    @Test
    fun validateUpdateEmptyName() {
        val game = GameUtils.newGame(id = 1)
            .copy(name = "")

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null URL to english Wikipedia page about game.
     */
    @Test
    fun validateUpdateNullWikiEn() {
        val game = GameUtils.newGame(id = 1)
            .copy(wikiEn = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_WIKI_EN_NULL", message = "URL to english Wikipedia page about game mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null URL to czech Wikipedia page about game.
     */
    @Test
    fun validateUpdateNullWikiCz() {
        val game = GameUtils.newGame(id = 1)
            .copy(wikiCz = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about game mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null count of media.
     */
    @Test
    fun validateUpdateNullMediaCount() {
        val game = GameUtils.newGame(id = 1)
            .copy(mediaCount = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with not positive count of media.
     */
    @Test
    fun validateUpdateNotPositiveMediaCount() {
        val game = GameUtils.newGame(id = 1)
            .copy(mediaCount = 0)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null format.
     */
    @Test
    fun validateUpdateNullFormat() {
        val game = GameUtils.newGame(id = 1)
            .copy(format = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_FORMAT_NULL", message = "Format mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null other data.
     */
    @Test
    fun validateUpdateNullOtherData() {
        val game = GameUtils.newGame(id = 1)
            .copy(otherData = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_OTHER_DATA_NULL", message = "Other data mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validate] with update game with null note.
     */
    @Test
    fun validateUpdateNullNote() {
        val game = GameUtils.newGame(id = 1)
            .copy(note = null)

        val result = validator.validate(data = game, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [GameValidator.validateExists] with correct game.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(GameUtils.newGameDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GameValidator.validateExists] with invalid game.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOT_EXIST", message = "Game doesn't exist.")))
        }
    }

    /**
     * Test method for [GameValidator.validateMovingData] with correct up game.
     */
    @Test
    fun validateMovingDataUp() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        val result = validator.validateMovingData(data = games[1], list = games, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GameValidator.validateMovingData] with with invalid up game.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        val result = validator.validateMovingData(data = games[0], list = games, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOT_MOVABLE", message = "Game can't be moved up.")))
        }
    }

    /**
     * Test method for [GameValidator.validateMovingData] with correct down game.
     */
    @Test
    fun validateMovingDataDown() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        val result = validator.validateMovingData(data = games[0], list = games, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GameValidator.validateMovingData] with with invalid down game.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        val result = validator.validateMovingData(data = games[1], list = games, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GAME_NOT_MOVABLE", message = "Game can't be moved down.")))
        }
    }

}
