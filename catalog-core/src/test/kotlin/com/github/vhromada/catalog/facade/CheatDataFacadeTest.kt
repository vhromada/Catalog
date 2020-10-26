package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.facade.impl.CheatDataFacadeImpl
import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.test.facade.MovableChildFacadeTest
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor

/**
 * A class represents test for class [CheatDataFacade].
 *
 * @author Vladimir Hromada
 */
class CheatDataFacadeTest : MovableChildFacadeTest<CheatData, com.github.vhromada.catalog.domain.CheatData, Cheat, Game>() {

    override fun isFirstChild(): Boolean {
        return false
    }

    override fun getFacade(): MovableChildFacade<CheatData, Cheat> {
        return CheatDataFacadeImpl(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    override fun newParentEntity(id: Int): Cheat {
        return CheatUtils.newCheat(id)
    }

    override fun newParentDomain(id: Int): Game {
        return GameUtils.newGameWithCheat(id)
    }

    override fun newParentDomainWithChildren(id: Int, children: List<com.github.vhromada.catalog.domain.CheatData>): Game {
        val cheat = CheatUtils.newCheatDomain(id)
                .copy(data = children)

        return GameUtils.newGameDomain(id)
                .copy(cheat = cheat)
    }

    override fun newChildEntity(id: Int?): CheatData {
        return CheatDataUtils.newCheatData(id)
    }

    override fun newChildDomain(id: Int?): com.github.vhromada.catalog.domain.CheatData {
        return CheatDataUtils.newCheatDataDomain(id)
    }

    override fun getParentRemovedData(parent: Game, child: com.github.vhromada.catalog.domain.CheatData): Game {
        val cheatData = parent.cheat!!.data.toMutableList()
        cheatData.remove(child)
        return parent.copy(cheat = parent.cheat!!.copy(data = cheatData))
    }

    override fun anyParentEntity(): Cheat {
        return any()
    }

    override fun anyChildEntity(): CheatData {
        return any()
    }

    override fun anyChildDomain(): com.github.vhromada.catalog.domain.CheatData {
        return any()
    }

    override fun argumentCaptorParentDomain(): KArgumentCaptor<Game> {
        return argumentCaptor()
    }

    override fun assertParentDeepEquals(expected: Game, actual: Game) {
        GameUtils.assertGameDeepEquals(expected, actual)
    }

}
