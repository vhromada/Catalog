package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.impl.GameFacadeImpl
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.facade.MovableParentFacade
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableParentFacadeTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [GameFacade].
 *
 * @author Vladimir Hromada
 */
class GameFacadeTest : MovableParentFacadeTest<Game, com.github.vhromada.catalog.domain.Game>() {

    /**
     * Test method for [GameFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val game1 = GameUtils.newGameDomain(1)
        val game2 = GameUtils.newGameDomain(2)
        val expectedCount = game1.mediaCount + game2.mediaCount

        whenever(service.getAll()).thenReturn(listOf(game1, game2))

        val result = (getFacade() as GameFacade).getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedCount)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(accountProvider, mapper, validator)
    }

    override fun getFacade(): MovableParentFacade<Game> {
        return GameFacadeImpl(service, accountProvider, timeProvider, mapper, validator)
    }

    override fun newEntity(id: Int?): Game {
        return GameUtils.newGame(id)
    }

    override fun newDomain(id: Int?): com.github.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(id)
    }

    override fun anyDomain(): com.github.vhromada.catalog.domain.Game {
        return any()
    }

    override fun anyEntity(): Game {
        return any()
    }

}
