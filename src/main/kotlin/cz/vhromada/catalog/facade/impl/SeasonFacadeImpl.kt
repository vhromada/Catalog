package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Season
import cz.vhromada.catalog.entity.Show
import cz.vhromada.catalog.facade.SeasonFacade
import cz.vhromada.common.facade.AbstractMovableChildFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.utils.sorted
import cz.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonFacade")
class SeasonFacadeImpl(
        showService: MovableService<cz.vhromada.catalog.domain.Show>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Season, cz.vhromada.catalog.domain.Season>,
        showValidator: MovableValidator<Show>,
        seasonValidator: MovableValidator<Season>
) : AbstractMovableChildFacade<Season, cz.vhromada.catalog.domain.Season, Show, cz.vhromada.catalog.domain.Show>(showService, accountProvider, timeProvider, mapper, showValidator,
        seasonValidator), SeasonFacade {

    override fun getDomainData(id: Int): cz.vhromada.catalog.domain.Season? {
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

    override fun getDomainList(parent: Show): List<cz.vhromada.catalog.domain.Season> {
        return service.get(parent.id!!)!!.seasons
    }

    override fun getForAdd(parent: Show, data: cz.vhromada.catalog.domain.Season): cz.vhromada.catalog.domain.Show {
        val show = service.get(parent.id!!)!!
        val seasons = show.seasons.toMutableList()
        seasons.add(data)

        return show.copy(seasons = seasons)
    }

    override fun getForUpdate(data: Season): cz.vhromada.catalog.domain.Show {
        val show = getShow(data)
        val season = getDataForUpdate(data).copy(episodes = getSeason(data.id, show).episodes)

        return show.copy(seasons = updateSeason(show, season))
    }

    override fun getForRemove(data: Season): cz.vhromada.catalog.domain.Show {
        val show = getShow(data)
        val seasons = show.seasons.filter { it.id != data.id }

        return show.copy(seasons = seasons)
    }

    override fun getForDuplicate(data: Season): cz.vhromada.catalog.domain.Show {
        val show = getShow(data)
        val season = getSeason(data.id, show)
        val seasons = show.seasons.toMutableList()
        seasons.add(season.copy(id = null, episodes = season.episodes.map { it.copy(id = null) }))

        return show.copy(seasons = seasons)
    }

    @Suppress("DuplicatedCode")
    override fun getForMove(data: Season, up: Boolean): cz.vhromada.catalog.domain.Show {
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
    private fun getShow(season: Season): cz.vhromada.catalog.domain.Show {
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
    private fun getSeason(id: Int?, show: cz.vhromada.catalog.domain.Show): cz.vhromada.catalog.domain.Season {
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
    private fun updateSeason(show: cz.vhromada.catalog.domain.Show, season: cz.vhromada.catalog.domain.Season): List<cz.vhromada.catalog.domain.Season> {
        val seasons = mutableListOf<cz.vhromada.catalog.domain.Season>()
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
