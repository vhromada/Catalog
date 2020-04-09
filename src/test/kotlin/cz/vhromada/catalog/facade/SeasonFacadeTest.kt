package cz.vhromada.catalog.facade

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.entity.Season
import cz.vhromada.catalog.entity.Show
import cz.vhromada.catalog.facade.impl.SeasonFacadeImpl
import cz.vhromada.catalog.utils.SeasonUtils
import cz.vhromada.catalog.utils.ShowUtils
import cz.vhromada.common.facade.MovableChildFacade
import cz.vhromada.common.test.facade.MovableChildFacadeTest

/**
 * A class represents test for class [SeasonFacade].
 *
 * @author Vladimir Hromada
 */
class SeasonFacadeTest : MovableChildFacadeTest<Season, cz.vhromada.catalog.domain.Season, Show, cz.vhromada.catalog.domain.Show>() {

    override fun getFacade(): MovableChildFacade<Season, Show> {
        return SeasonFacadeImpl(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    override fun newParentEntity(id: Int): Show {
        return ShowUtils.newShow(id)
    }

    override fun newParentDomain(id: Int): cz.vhromada.catalog.domain.Show {
        return ShowUtils.newShowWithSeasons(id)
    }

    override fun newParentDomainWithChildren(id: Int, children: List<cz.vhromada.catalog.domain.Season>): cz.vhromada.catalog.domain.Show {
        return newParentDomain(id)
                .copy(seasons = children)
    }

    override fun newChildEntity(id: Int?): Season {
        return SeasonUtils.newSeason(id)
    }

    override fun newChildDomain(id: Int?): cz.vhromada.catalog.domain.Season {
        val season = SeasonUtils.newSeasonWithEpisodes(id)
        for (episode in season.episodes) {
            episode.position = 0
        }

        return season
    }

    override fun getParentRemovedData(parent: cz.vhromada.catalog.domain.Show, child: cz.vhromada.catalog.domain.Season): cz.vhromada.catalog.domain.Show {
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

    override fun anyChildDomain(): cz.vhromada.catalog.domain.Season {
        return any()
    }

    override fun argumentCaptorParentDomain(): KArgumentCaptor<cz.vhromada.catalog.domain.Show> {
        return argumentCaptor()
    }

    override fun assertParentDeepEquals(expected: cz.vhromada.catalog.domain.Show, actual: cz.vhromada.catalog.domain.Show) {
        ShowUtils.assertShowDeepEquals(expected, actual)
    }

}
