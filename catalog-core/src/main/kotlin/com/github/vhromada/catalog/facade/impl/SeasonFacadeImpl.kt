package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.SeasonFacade
import com.github.vhromada.common.facade.AbstractMovableChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.sorted
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonFacade")
class SeasonFacadeImpl(
        showService: MovableService<com.github.vhromada.catalog.domain.Show>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Season, com.github.vhromada.catalog.domain.Season>,
        showValidator: MovableValidator<Show>,
        seasonValidator: MovableValidator<Season>
) : AbstractMovableChildFacade<Season, com.github.vhromada.catalog.domain.Season, Show, com.github.vhromada.catalog.domain.Show>(showService, accountProvider, timeProvider, mapper, showValidator,
        seasonValidator), SeasonFacade {

    override fun getDomainData(id: Int): com.github.vhromada.catalog.domain.Season? {
        val shows = service.getAll()
        for (show in shows) {
            for (season in show.seasons) {
                if (id == season.id) {
                    return season
                }
            }
        }

        return null
    }

    override fun getDomainList(parent: Show): List<com.github.vhromada.catalog.domain.Season> {
        return service.get(parent.id!!).get().seasons
    }

    override fun getForAdd(parent: Show, data: com.github.vhromada.catalog.domain.Season): com.github.vhromada.catalog.domain.Show {
        val show = service.get(parent.id!!).get()
        val seasons = show.seasons.toMutableList()
        seasons.add(data)

        return show.copy(seasons = seasons)
    }

    override fun getForUpdate(data: Season): com.github.vhromada.catalog.domain.Show {
        val show = getShow(data)
        val season = getDataForUpdate(data).copy(episodes = getSeason(data.id, show).episodes)

        return show.copy(seasons = updateSeason(show, season))
    }

    override fun getForRemove(data: Season): com.github.vhromada.catalog.domain.Show {
        val show = getShow(data)
        val seasons = show.seasons.filter { it.id != data.id }

        return show.copy(seasons = seasons)
    }

    override fun getForDuplicate(data: Season): com.github.vhromada.catalog.domain.Show {
        val show = getShow(data)
        val season = getSeason(data.id, show)
        val seasons = show.seasons.toMutableList()
        seasons.add(season.copy(id = null, episodes = season.episodes.map { it.copy(id = null) }))

        return show.copy(seasons = seasons)
    }

    @Suppress("DuplicatedCode")
    override fun getForMove(data: Season, up: Boolean): com.github.vhromada.catalog.domain.Show {
        var show = getShow(data)
        val season = getSeason(data.id, show)
        val seasons = show.seasons.sorted()

        val index = seasons.indexOf(season)
        val other = seasons[if (up) index - 1 else index + 1]
        val position = season.position!!
        season.position = other.position
        other.position = position

        show = show.copy(seasons = updateSeason(show, season))
        return show.copy(seasons = updateSeason(show, other))
    }

    /**
     * Returns show for season.
     *
     * @param season season
     * @return show for season
     */
    private fun getShow(season: Season): com.github.vhromada.catalog.domain.Show {
        for (show in service.getAll()) {
            for (seasonDomain in show.seasons) {
                if (season.id == seasonDomain.id) {
                    return show
                }
            }
        }

        throw IllegalStateException("Unknown season.")
    }

    /**
     * Returns season with ID.
     *
     * @param id   ID
     * @param show show
     * @return season with ID
     */
    private fun getSeason(id: Int?, show: com.github.vhromada.catalog.domain.Show): com.github.vhromada.catalog.domain.Season {
        for (season in show.seasons) {
            if (id == season.id) {
                return season
            }
        }

        throw IllegalStateException("Unknown season.")
    }

    /**
     * Updates season.
     *
     * @param show   show
     * @param season season
     * @return updated seasons
     */
    @Suppress("DuplicatedCode")
    private fun updateSeason(show: com.github.vhromada.catalog.domain.Show, season: com.github.vhromada.catalog.domain.Season): List<com.github.vhromada.catalog.domain.Season> {
        val seasons = mutableListOf<com.github.vhromada.catalog.domain.Season>()
        for (seasonDomain in show.seasons) {
            if (seasonDomain == season) {
                val audit = getAudit()
                season.audit = seasonDomain.audit!!.copy(updatedUser = audit.updatedUser, updatedTime = audit.updatedTime)
                seasons.add(season)
            } else {
                seasons.add(seasonDomain)
            }
        }

        return seasons
    }

}
