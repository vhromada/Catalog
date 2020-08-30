package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.domain.Show
import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.facade.EpisodeFacade
import com.github.vhromada.common.entity.Movable
import com.github.vhromada.common.facade.AbstractMovableChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.sorted
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeFacade")
class EpisodeFacadeImpl(
        showService: MovableService<Show>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Episode, com.github.vhromada.catalog.domain.Episode>,
        seasonValidator: MovableValidator<Season>,
        episodeValidator: MovableValidator<Episode>
) : AbstractMovableChildFacade<Episode, com.github.vhromada.catalog.domain.Episode, Season, Show>(showService, accountProvider, timeProvider, mapper, seasonValidator, episodeValidator), EpisodeFacade {

    override fun getDomainData(id: Int): com.github.vhromada.catalog.domain.Episode? {
        val shows = service.getAll()
        for (show in shows) {
            for (season in show.seasons) {
                for (episode in season.episodes) {
                    if (id == episode.id) {
                        return episode
                    }
                }
            }
        }

        return null
    }

    override fun getDomainList(parent: Season): List<com.github.vhromada.catalog.domain.Episode> {
        val shows = service.getAll()
        for (show in shows) {
            for (season in show.seasons) {
                if (parent.id == season.id) {
                    return season.episodes
                }
            }
        }

        return emptyList()
    }

    override fun getForAdd(parent: Season, data: com.github.vhromada.catalog.domain.Episode): Show {
        val show = getShow(parent)
        val season = getSeason(show, parent)
        val episodes = season.episodes.toMutableList()
        episodes.add(data)

        return show.copy(seasons = updateSeason(show, season.copy(episodes = episodes)))
    }

    override fun getForUpdate(data: Episode): Show {
        val show = getShow(data)
        val season = getSeason(show, data)
        val episode = getDataForUpdate(data)

        return show.copy(seasons = updateSeason(show, season.copy(episodes = updateEpisode(season, episode))))
    }

    override fun getForRemove(data: Episode): Show {
        val show = getShow(data)
        val season = getSeason(show, data)
        val episodes = season.episodes.filter { it.id != data.id }

        return show.copy(seasons = updateSeason(show, season.copy(episodes = episodes)))
    }

    override fun getForDuplicate(data: Episode): Show {
        val show = getShow(data)
        val season = getSeason(show, data)
        val episode = getEpisode(data.id, show)
        val episodes = season.episodes.toMutableList()
        episodes.add(episode.copy(id = null))

        return show.copy(seasons = updateSeason(show, season.copy(episodes = episodes)))
    }

    @Suppress("DuplicatedCode")
    override fun getForMove(data: Episode, up: Boolean): Show {
        var show = getShow(data)
        val season = getSeason(show, data)
        val episode = getEpisode(data.id, show)
        val episodes = season.episodes.sorted()

        val index = episodes.indexOf(episode)
        val other = episodes[if (up) index - 1 else index + 1]
        val position = episode.position!!
        episode.position = other.position
        other.position = position

        show = show.copy(seasons = updateSeason(show, season.copy(episodes = updateEpisode(season, episode))))
        return show.copy(seasons = updateSeason(show, season.copy(episodes = updateEpisode(season, other))))
    }

    /**
     * Returns show for season.
     *
     * @param season season
     * @return show for season
     */
    private fun getShow(season: Season): Show {
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
     * Returns show for episode.
     *
     * @param episode episode
     * @return show for episode
     */
    private fun getShow(episode: Episode): Show {
        for (show in service.getAll()) {
            for (season in show.seasons) {
                for (episodeDomain in season.episodes) {
                    if (episode.id == episodeDomain.id) {
                        return show
                    }
                }
            }
        }

        throw IllegalStateException("Unknown episode.")
    }

    /**
     * Returns season for show.
     *
     * @param show   show
     * @param season season
     * @return season for show
     */
    private fun getSeason(show: Show, season: Season): com.github.vhromada.catalog.domain.Season {
        for (seasonDomain in show.seasons) {
            if (season.id == seasonDomain.id) {
                return seasonDomain
            }
        }

        throw IllegalStateException("Unknown season")
    }

    /**
     * Returns season for episode.
     *
     * @param show    show
     * @param episode episode
     * @return season for episode
     */
    private fun getSeason(show: Show, episode: Movable): com.github.vhromada.catalog.domain.Season {
        for (season in show.seasons) {
            for ((id) in season.episodes) {
                if (episode.id == id) {
                    return season
                }
            }
        }

        throw IllegalStateException("Unknown episode")
    }


    /**
     * Returns episode with ID.
     *
     * @param id   ID
     * @param show show
     * @return episode with ID
     */
    private fun getEpisode(id: Int?, show: Show): com.github.vhromada.catalog.domain.Episode {
        for (season in show.seasons) {
            for (episode in season.episodes) {
                if (id == episode.id) {
                    return episode
                }
            }
        }

        throw IllegalStateException("Unknown episode")
    }


    /**
     * Updates season in show.
     *
     * @param show   show
     * @param season season
     * @return updated seasons
     */
    @Suppress("DuplicatedCode")
    private fun updateSeason(show: Show, season: com.github.vhromada.catalog.domain.Season): List<com.github.vhromada.catalog.domain.Season> {
        val seasons = mutableListOf<com.github.vhromada.catalog.domain.Season>()
        for (seasonDomain in show.seasons) {
            if (seasonDomain == season) {
                seasons.add(season)
            } else {
                seasons.add(seasonDomain)
            }
        }

        return seasons
    }

    /**
     * Updates episode.
     *
     * @param season    season
     * @param episode episode
     * @return updated episodes
     */
    @Suppress("DuplicatedCode")
    private fun updateEpisode(season: com.github.vhromada.catalog.domain.Season, episode: com.github.vhromada.catalog.domain.Episode): List<com.github.vhromada.catalog.domain.Episode> {
        val episodes = mutableListOf<com.github.vhromada.catalog.domain.Episode>()
        for (episodeDomain in season.episodes) {
            if (episodeDomain == episode) {
                val audit = getAudit()
                episode.audit = episodeDomain.audit!!.copy(updatedUser = audit.updatedUser, updatedTime = audit.updatedTime)
                episodes.add(episode)
            } else {
                episodes.add(episodeDomain)
            }
        }
        return episodes
    }

}
