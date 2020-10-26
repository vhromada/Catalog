package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.entity.CheatData
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
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [CheatDataValidator].
 *
 * @author Vladimir Hromada
 */
class CheatDataValidatorTest : MovableValidatorTest<CheatData, Game>() {

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null action.
     */
    @Test
    fun validateDeepNullAction() {
        val cheatData = getValidatingData(1)
                .copy(action = null)

        val result = getValidator().validate(cheatData, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "CHEAT_DATA_ACTION_NULL", "Action mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with empty action.
     */
    @Test
    fun validateDeepEmptyAction() {
        val cheatData = getValidatingData(1)
                .copy(action = "")

        val result = getValidator().validate(cheatData, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "CHEAT_DATA_ACTION_EMPTY", "Action mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null description.
     */
    @Test
    fun validateDeepNullDescription() {
        val cheatData = getValidatingData(1)
                .copy(description = null)

        val result = getValidator().validate(cheatData, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "CHEAT_DATA_DESCRIPTION_NULL", "Description mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with empty description.
     */
    @Test
    fun validateDeepEmptyDescription() {
        val cheatData = getValidatingData(1)
                .copy(description = "")

        val result = getValidator().validate(cheatData, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "CHEAT_DATA_DESCRIPTION_EMPTY", "Description mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<CheatData> {
        return CheatDataValidator(service)
    }

    override fun getValidatingData(id: Int?): CheatData {
        return CheatDataUtils.newCheatData(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): CheatData {
        return CheatDataUtils.newCheatData(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: CheatData): Game {
        val cheat = CheatUtils.newCheatDomain(validatingData.id)
                .copy(data = listOf(CheatDataUtils.newCheatDataDomain(validatingData.id)))
        return GameUtils.newGameDomain(validatingData.id)
                .copy(cheat = cheat)
    }

    override fun getItem1(): Game {
        return GameUtils.newGameDomain(null)
    }

    override fun getItem2(): Game {
        return GameUtils.newGameDomain(null)
    }

    override fun getName(): String {
        return "Cheat's data"
    }

    override fun getPrefix(): String {
        return "CHEAT_DATA"
    }

    override fun initExistsMock(validatingData: CheatData, exists: Boolean) {
        val game = if (exists) GameUtils.newGameWithCheat(validatingData.id) else GameUtils.newGameDomain(Int.MAX_VALUE)

        whenever(service.getAll()).thenReturn(listOf(game))
    }

    override fun verifyExistsMock(validatingData: CheatData) {
        verify(service).getAll()
        verifyNoMoreInteractions(service)
    }

    override fun initMovingMock(validatingData: CheatData, up: Boolean, valid: Boolean) {
        val cheatData = if (up && valid || !up && !valid) {
            listOf(CheatDataUtils.newCheatDataDomain(1), CheatDataUtils.newCheatDataDomain(validatingData.id))
        } else {
            listOf(CheatDataUtils.newCheatDataDomain(validatingData.id), CheatDataUtils.newCheatDataDomain(Int.MAX_VALUE))
        }
        val cheat = CheatUtils.newCheatDomain(1)
                .copy(data = cheatData)
        val game = GameUtils.newGameDomain(1)
                .copy(cheat = cheat)

        whenever(service.getAll()).thenReturn(listOf(game))
    }

    override fun verifyMovingMock(validatingData: CheatData) {
        verify(service, times(2)).getAll()
        verifyNoMoreInteractions(service)
    }

}
