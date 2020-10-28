package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.validator.MovableValidatorTest
import com.github.vhromada.common.validator.AbstractMovableValidator
import com.github.vhromada.common.validator.MovableValidator
import com.github.vhromada.common.validator.ValidationType
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [CheatValidator].
 *
 * @author Vladimir Hromada
 */
class CheatValidatorTest : MovableValidatorTest<Cheat, Game>() {

    /**
     * Test method for [CheatValidator.validate] with [ValidationType.DEEP] with data with null setting for game.
     */
    @Test
    fun validateDeepNullGameSetting() {
        val cheat = getValidatingData(1)
                .copy(gameSetting = null)

        val result = getValidator().validate(cheat, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_GAME_SETTING_NULL", "Setting for game mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [CheatValidator.validate] with [ValidationType.DEEP] with data with null setting for cheat.
     */
    @Test
    fun validateDeepNullCheatSetting() {
        val cheat = getValidatingData(1)
                .copy(cheatSetting = null)

        val result = getValidator().validate(cheat, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_CHEAT_SETTING_NULL", "Setting for cheat mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null cheat's data.
     */
    @Test
    fun validateDeepNullCheatData() {
        val cheat = getValidatingData(1)
                .copy(data = null)

        val result = getValidator().validate(cheat, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_DATA_NULL", "Cheat's data mustn't be null.")))
        }

        verifyDeepMock(cheat)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with cheat's data with null value.
     */
    @Test
    fun validateDeepBadCheatData() {
        val cheat = getValidatingData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), null))

        val result = getValidator().validate(cheat, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_DATA_CONTAIN_NULL", "Cheat's data mustn't contain null value.")))
        }

        verifyDeepMock(cheat)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with cheat's data with null action.
     */
    @Test
    fun validateDeepCheatDataWithNullAction() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(action = null)
        val cheat = getValidatingData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = getValidator().validate(cheat, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_DATA_ACTION_NULL", "Cheat's data action mustn't be null.")))
        }

        verifyDeepMock(cheat)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with cheat's data with empty action.
     */
    @Test
    fun validateDeepCheatDataWithEmptyAction() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(action = "")
        val cheat = getValidatingData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = getValidator().validate(cheat, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_DATA_ACTION_EMPTY", "Cheat's data action mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with cheat's data with null description.
     */
    @Test
    fun validateDeepCheatDataWithNullDescription() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(description = null)
        val cheat = getValidatingData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = getValidator().validate(cheat, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_DATA_DESCRIPTION_NULL", "Cheat's data description mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with cheat's data with empty description.
     */
    @Test
    fun validateDeepCheatDataWithEmptyDescription() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(description = "")
        val cheat = getValidatingData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = getValidator().validate(cheat, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_DATA_DESCRIPTION_EMPTY", "Cheat's data description mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    @Test
    override fun validateUp() {
        // no test
    }

    @Test
    override fun validateUpInvalid() {
        // no test
    }

    @Test
    override fun validateDown() {
        // no test
    }

    @Test
    override fun validateDownInvalid() {
        // no test
    }

    override fun getValidator(): MovableValidator<Cheat> {
        return CheatValidator(service)
    }

    override fun getValidatingData(id: Int?): Cheat {
        return CheatUtils.newCheat(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Cheat {
        return CheatUtils.newCheat(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Cheat): Game {
        return GameUtils.newGameDomain(validatingData.id)
    }

    override fun getItem1(): Game {
        return GameUtils.newGameDomain(null)
    }

    override fun getItem2(): Game {
        return GameUtils.newGameDomain(null)
    }

    override fun getName(): String {
        return "Cheat"
    }

    override fun initExistsMock(validatingData: Cheat, exists: Boolean) {
        val game = if (exists) GameUtils.newGameWithCheat(validatingData.id) else GameUtils.newGameDomain(Int.MAX_VALUE)

        whenever(service.getAll()).thenReturn(listOf(game))
    }

    override fun verifyExistsMock(validatingData: Cheat) {
        verify(service).getAll()
        verifyNoMoreInteractions(service)
    }

}
