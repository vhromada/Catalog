package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.impl.SeasonFacadeImpl
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.test.facade.MovableChildFacadeTest
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor

/**
 * A class represents test for class [SeasonFacade].
 *
 * @author Vladimir Hromada
 */
class SeasonFacadeTest : MovableChildFacadeTest<Season, com.github.vhromada.catalog.domain.Season, Show, com.github.vhromada.catalog.domain.Show>() {

    override fun getFacade(): MovableChildFacade<Season, Show> {
        return SeasonFacadeImpl(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    override fun newParentEntity(id: Int): Show {
        return ShowUtils.newShow(id)
    }

    override fun newParentDomain(id: Int): com.github.vhromada.catalog.domain.Show {
        return ShowUtils.newShowWithSeasons(id)
    }

    override fun newParentDomainWithChildren(id: Int, children: List<com.github.vhromada.catalog.domain.Season>): com.github.vhromada.catalog.domain.Show {
        return newParentDomain(id)
                .copy(seasons = children)
    }

    override fun newChildEntity(id: Int?): Season {
        return SeasonUtils.newSeason(id)
    }

    override fun newChildDomain(id: Int?): com.github.vhromada.catalog.domain.Season {
        val season = SeasonUtils.newSeasonWithEpisodes(id)
        for (episode in season.episodes) {
            episode.position = 0
        }

        return season
    }

    override fun getParentRemovedData(parent: com.github.vhromada.catalog.domain.Show, child: com.github.vhromada.catalog.domain.Season): com.github.vhromada.catalog.domain.Show {
        val seasons = parent.seasons.toMutableList()
        seasons.remove(child)
        return parent.copy(seasons = seasons)
    }

    override fun anyParentEntity(): Show {
        return any()
    }

    override fun anyChildEntity(): Season {
        return any()
    }

    override fun anyChildDomain(): com.github.vhromada.catalog.domain.Season {
        return any()
    }

    override fun argumentCaptorParentDomain(): KArgumentCaptor<com.github.vhromada.catalog.domain.Show> {
        return argumentCaptor()
    }

    override fun assertParentDeepEquals(expected: com.github.vhromada.catalog.domain.Show, actual: com.github.vhromada.catalog.domain.Show) {
        ShowUtils.assertShowDeepEquals(expected, actual)
    }

}
