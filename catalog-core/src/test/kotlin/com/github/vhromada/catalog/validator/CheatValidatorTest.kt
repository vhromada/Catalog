package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [CheatValidator].
 *
 * @author Vladimir Hromada
 */
class CheatValidatorTest {

    /**
     * Instance of [CheatValidator]
     */
    private lateinit var validator: CheatValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = CheatValidator()
    }

    /**
     * Test method for [CheatValidator.validate] with correct new cheat.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = CheatUtils.newCheat(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [CheatValidator.validate] with null new cheat.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NULL", message = "Cheat mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with null setting for game.
     */
    @Test
    fun validateNewNullGameSetting() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(gameSetting = null)

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_GAME_SETTING_NULL", message = "Setting for game mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with null setting for cheat.
     */
    @Test
    fun validateNewNullCheatSetting() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(cheatSetting = null)

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_CHEAT_SETTING_NULL", message = "Setting for cheat mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with null cheat's data.
     */
    @Test
    fun validateNewNullCheatData() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = null)

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_NULL", message = "Cheat's data mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with cheat's data with null value.
     */
    @Test
    fun validateNewBadCheatData() {
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), null))

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_CONTAIN_NULL", message = "Cheat's data mustn't contain null value.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with cheat's data with null action.
     */
    @Test
    fun validateNewCheatDataWithNullAction() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(action = null)
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_NULL", message = "Cheat's data action mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with cheat's data with empty action.
     */
    @Test
    fun validateNewCheatDataWithEmptyAction() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(action = "")
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_EMPTY", message = "Cheat's data action mustn't be empty string.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with cheat's data with null description.
     */
    @Test
    fun validateNewCheatDataWithNullDescription() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(description = null)
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_NULL", message = "Cheat's data description mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with new cheat with cheat's data with empty description.
     */
    @Test
    fun validateNewCheatDataWithEmptyDescription() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(description = "")
        val cheat = CheatUtils.newCheat(id = null)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = validator.validate(data = cheat, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_EMPTY", message = "Cheat's data description mustn't be empty string.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with with update correct cheat.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = CheatUtils.newCheat(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [CheatValidator.validate] with null update cheat.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NULL", message = "Cheat mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(id = null)

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with null setting for game.
     */
    @Test
    fun validateUpdateNullGameSetting() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(gameSetting = null)

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_GAME_SETTING_NULL", message = "Setting for game mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with null setting for cheat.
     */
    @Test
    fun validateUpdateNullCheatSetting() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(cheatSetting = null)

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_CHEAT_SETTING_NULL", message = "Setting for cheat mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with null cheat's data.
     */
    @Test
    fun validateUpdateNullCheatData() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = null)

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_NULL", message = "Cheat's data mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with cheat's data with null value.
     */
    @Test
    fun validateUpdateBadCheatData() {
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), null))

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_CONTAIN_NULL", message = "Cheat's data mustn't contain null value.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with cheat's data with null action.
     */
    @Test
    fun validateUpdateCheatDataWithNullAction() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(action = null)
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_NULL", message = "Cheat's data action mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with cheat's data with empty action.
     */
    @Test
    fun validateUpdateCheatDataWithEmptyAction() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(action = "")
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_ACTION_EMPTY", message = "Cheat's data action mustn't be empty string.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with cheat's data with null description.
     */
    @Test
    fun validateUpdateCheatDataWithNullDescription() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(description = null)
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_NULL", message = "Cheat's data description mustn't be null.")))
        }
    }

    /**
     * Test method for [CheatValidator.validate] with update cheat with cheat's data with empty description.
     */
    @Test
    fun validateUpdateCheatDataWithEmptyDescription() {
        val badCheatData = CheatDataUtils.newCheatData(id = 2)
            .copy(description = "")
        val cheat = CheatUtils.newCheat(id = 1)
            .copy(data = listOf(CheatDataUtils.newCheatData(id = 1), badCheatData))

        val result = validator.validate(data = cheat, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_DATA_DESCRIPTION_EMPTY", message = "Cheat's data description mustn't be empty string.")))
        }
    }

    /**
     * Test method for [CheatValidator.validateExists] with correct cheat.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(CheatUtils.newCheatDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [CheatValidator.validateExists] with invalid cheat.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_EXIST", message = "Cheat doesn't exist.")))
        }
    }

    /**
     * Test method for [CheatValidator.validateMovingData] with correct up cheat.
     */
    @Test
    fun validateMovingDataUp() {
        val cheats = listOf(CheatUtils.newCheatDomain(id = 1), CheatUtils.newCheatDomain(id = 2))

        val result = validator.validateMovingData(data = cheats[1], list = cheats, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [CheatValidator.validateMovingData] with with invalid up cheat.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val cheats = listOf(CheatUtils.newCheatDomain(id = 1), CheatUtils.newCheatDomain(id = 2))

        val result = validator.validateMovingData(data = cheats[0], list = cheats, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_MOVABLE", message = "Cheat can't be moved up.")))
        }
    }

    /**
     * Test method for [CheatValidator.validateMovingData] with correct down cheat.
     */
    @Test
    fun validateMovingDataDown() {
        val cheats = listOf(CheatUtils.newCheatDomain(id = 1), CheatUtils.newCheatDomain(id = 2))

        val result = validator.validateMovingData(data = cheats[0], list = cheats, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [CheatValidator.validateMovingData] with with invalid down cheat.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val cheats = listOf(CheatUtils.newCheatDomain(id = 1), CheatUtils.newCheatDomain(id = 2))

        val result = validator.validateMovingData(data = cheats[1], list = cheats, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_MOVABLE", message = "Cheat can't be moved down.")))
        }
    }

}
