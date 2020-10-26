package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.impl.CheatFacadeImpl
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableChildFacadeTest
import com.github.vhromada.common.test.utils.TestConstants
import com.github.vhromada.common.validator.ValidationType
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [CheatFacade].
 *
 * @author Vladimir Hromada
 */
class CheatFacadeTest : MovableChildFacadeTest<Cheat, com.github.vhromada.catalog.domain.Cheat, Game, com.github.vhromada.catalog.domain.Game>() {

    @Test
    override fun add() {
        val parentEntity = newParentEntity(1)
        val childEntity = newChildEntity(null)
        val childDomain = newChildDomain(null)
        val argumentCaptor = argumentCaptorParentDomain()
        val childArgumentCaptor = argumentCaptor<Cheat>()

        whenever(service.get(any())).thenReturn(Optional.of(GameUtils.newGameDomain(1)))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)
        whenever(timeProvider.getTime()).thenReturn(TestConstants.TIME)
        whenever(mapper.map(anyChildEntity())).thenReturn(childDomain)
        whenever(parentMovableValidator.validate(anyParentEntity(), any())).thenReturn(Result())
        whenever(childMovableValidator.validate(anyChildEntity(), any())).thenReturn(Result())

        val result = getFacade().add(parentEntity, childEntity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service, times(2)).get(parentEntity.id!!)
        verify(service).update(argumentCaptor.capture())
        verify(accountProvider).getAccount()
        verify(timeProvider).getTime()
        verify(parentMovableValidator).validate(parentEntity, ValidationType.EXISTS)
        verify(childMovableValidator).validate(childArgumentCaptor.capture(), eq(ValidationType.NEW), eq(ValidationType.DEEP))
        verify(mapper).map(childArgumentCaptor.capture())
        verifyNoMoreInteractions(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)

        assertParentDeepEquals(newParentDomainWithChildren(1, listOf(newChildDomain(1), childDomain)), argumentCaptor.lastValue)
        childArgumentCaptor.allValues.forEach {
            assertCheatDeepEquals(childEntity, it)
        }
    }

    @Test
    override fun addInvalidData() {
        val parentEntity = GameUtils.newGame(null)
        val childEntity = newChildEntity(null)
        val childArgumentCaptor = argumentCaptor<Cheat>()
        val invalidParentResult = Result.error<Unit>("PARENT_INVALID", "Parent must be valid.")
        val invalidChildResult = Result.error<Unit>("CHILD_INVALID", "Child must be valid.")

        whenever(parentMovableValidator.validate(anyParentEntity(), any())).thenReturn(invalidParentResult)
        whenever(childMovableValidator.validate(anyChildEntity(), any())).thenReturn(invalidChildResult)

        val result = getFacade().add(parentEntity, childEntity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(invalidParentResult.events()[0], invalidChildResult.events()[0]))
        }

        verify(parentMovableValidator).validate(parentEntity, ValidationType.EXISTS)
        verify(childMovableValidator).validate(childArgumentCaptor.capture(), eq(ValidationType.NEW), eq(ValidationType.DEEP))
        verifyNoMoreInteractions(parentMovableValidator, childMovableValidator)
        verifyZeroInteractions(service, accountProvider, timeProvider, mapper)

        assertCheatDeepEquals(childEntity, childArgumentCaptor.lastValue)
    }

    @Test
    fun addInvalidCheatData() {
        val parentEntity = GameUtils.newGame(Int.MAX_VALUE)
        val childEntity = newChildEntity(null)
        val childArgumentCaptor = argumentCaptor<Cheat>()

        whenever(service.get(any())).thenReturn(Optional.of(newParentDomain(1)))
        whenever(parentMovableValidator.validate(anyParentEntity(), any())).thenReturn(Result())
        whenever(childMovableValidator.validate(anyChildEntity(), any())).thenReturn(Result())

        val result = getFacade().add(parentEntity, childEntity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_CHEAT_EXIST", "Game already has cheat.")))
        }

        verify(service).get(parentEntity.id!!)
        verify(parentMovableValidator).validate(parentEntity, ValidationType.EXISTS)
        verify(childMovableValidator).validate(childArgumentCaptor.capture(), eq(ValidationType.NEW), eq(ValidationType.DEEP))
        verifyNoMoreInteractions(service, parentMovableValidator, childMovableValidator)
        verifyZeroInteractions(accountProvider, timeProvider, mapper)

        assertCheatDeepEquals(childEntity, childArgumentCaptor.lastValue)
    }

    @Test
    override fun duplicate() {
        val result = getFacade().duplicate(newChildEntity(Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "CHEAT_NOT_DUPLICABLE", "Cheat can't be duplicated.")))
        }

        verifyZeroInteractions(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    @Test
    override fun duplicateInvalidData() {
        // no test
    }

    @Test
    override fun moveUp() {
        val result = getFacade().moveUp(newChildEntity(Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "CHEAT_NOT_MOVABLE", "Cheat can't be moved up.")))
        }

        verifyZeroInteractions(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    @Test
    override fun moveUpInvalidData() {
        // no test
    }

    @Test
    override fun moveDown() {
        val result = getFacade().moveDown(newChildEntity(Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "CHEAT_NOT_MOVABLE", "Cheat can't be moved down.")))
        }

        verifyZeroInteractions(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    @Test
    override fun moveDownInvalidData() {
        // no test
    }

    override fun getFacade(): MovableChildFacade<Cheat, Game> {
        return CheatFacadeImpl(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    override fun newParentEntity(id: Int): Game {
        return GameUtils.newGame(id)
    }

    override fun newParentDomain(id: Int): com.github.vhromada.catalog.domain.Game {
        return GameUtils.newGameWithCheat(id)
    }

    @Suppress("SameParameterValue")
    override fun newParentDomainWithChildren(id: Int, children: List<com.github.vhromada.catalog.domain.Cheat>): com.github.vhromada.catalog.domain.Game {
        return newParentDomain(id)
                .copy(cheat = children[0])
    }

    override fun newChildEntity(id: Int?): Cheat {
        return CheatUtils.newCheat(id)
    }

    override fun newChildDomain(id: Int?): com.github.vhromada.catalog.domain.Cheat {
        val cheat = CheatUtils.newCheatWithData(id)
        for (data in cheat.data) {
            data.position = 0
        }

        return cheat
    }

    override fun getParentRemovedData(parent: com.github.vhromada.catalog.domain.Game, child: com.github.vhromada.catalog.domain.Cheat): com.github.vhromada.catalog.domain.Game {
        return parent.copy(cheat = null)
    }

    override fun anyParentEntity(): Game {
        return any()
    }

    override fun anyChildEntity(): Cheat {
        return any()
    }

    override fun anyChildDomain(): com.github.vhromada.catalog.domain.Cheat {
        return any()
    }

    override fun argumentCaptorParentDomain(): KArgumentCaptor<com.github.vhromada.catalog.domain.Game> {
        return argumentCaptor()
    }

    override fun assertParentDeepEquals(expected: com.github.vhromada.catalog.domain.Game, actual: com.github.vhromada.catalog.domain.Game) {
        GameUtils.assertGameDeepEquals(expected, actual)
    }

    /**
     * Assert cheat deep equals.
     *
     * @param expected expected cheat
     * @param actual   actual cheat
     */
    private fun assertCheatDeepEquals(expected: Cheat, actual: Cheat) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.gameSetting).isEqualTo(expected.gameSetting)
            it.assertThat(actual.cheatSetting).isEqualTo(expected.cheatSetting)
            it.assertThat(actual.data).isEqualTo(expected.data)
            it.assertThat(actual.position).isNull()
        }
    }

}
