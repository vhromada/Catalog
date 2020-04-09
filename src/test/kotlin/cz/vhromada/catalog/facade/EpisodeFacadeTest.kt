package cz.vhromada.catalog.facade

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.domain.Show
import cz.vhromada.catalog.entity.Episode
import cz.vhromada.catalog.entity.Season
import cz.vhromada.catalog.facade.impl.EpisodeFacadeImpl
import cz.vhromada.catalog.utils.EpisodeUtils
import cz.vhromada.catalog.utils.SeasonUtils
import cz.vhromada.catalog.utils.ShowUtils
import cz.vhromada.common.facade.MovableChildFacade
import cz.vhromada.common.test.facade.MovableChildFacadeTest

/**
 * A class represents test for class [EpisodeFacade].
 *
 * @author Vladimir Hromada
 */
class EpisodeFacadeTest : MovableChildFacadeTest<Episode, cz.vhromada.catalog.domain.Episode, Season, Show>() {

    override fun isFirstChild(): Boolean {
        return false
    }

    override fun getFacade(): MovableChildFacade<Episode, Season> {
        return EpisodeFacadeImpl(service, accountProvider, timeProvider, mapper, parentMovableValidator, childMovableValidator)
    }

    override fun newParentEntity(id: Int): Season {
        return SeasonUtils.newSeason(id)
    }

    override fun newParentDomain(id: Int): Show {
        return ShowUtils.newShowWithSeasons(id)
    }

    override fun newParentDomainWithChildren(id: Int, children: List<cz.vhromada.catalog.domain.Episode>): Show {
        val season = SeasonUtils.newSeasonDomain(id)
                .copy(episodes = children)

        return ShowUtils.newShowDomain(id)
                .copy(seasons = listOf(season))
    }

    override fun newChildEntity(id: Int?): Episode {
        return EpisodeUtils.newEpisode(id)
    }

    override fun newChildDomain(id: Int?): cz.vhromada.catalog.domain.Episode {
        return EpisodeUtils.newEpisodeDomain(id)
    }

    override fun getParentRemovedData(parent: Show, child: cz.vhromada.catalog.domain.Episode): Show {
        return parent.copy(seasons = parent.seasons.map {
            val episodes = it.episodes.toMutableList()
            episodes.remove(child)
            it.copy(episodes = episodes)
        })
    }

    override fun anyParentEntity(): Season {
        return any()
    }

    override fun anyChildEntity(): Episode {
        return any()
    }

    override fun anyChildDomain(): cz.vhromada.catalog.domain.Episode {
        return any()
    }

    override fun argumentCaptorParentDomain(): KArgumentCaptor<Show> {
        return argumentCaptor()
    }

    override fun assertParentDeepEquals(expected: Show, actual: Show) {
        ShowUtils.assertShowDeepEquals(expected, actual)
    }

}
