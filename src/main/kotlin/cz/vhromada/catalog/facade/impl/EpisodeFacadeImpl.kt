package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.domain.Show
import cz.vhromada.catalog.entity.Episode
import cz.vhromada.catalog.entity.Season
import cz.vhromada.catalog.facade.EpisodeFacade
import cz.vhromada.common.Movable
import cz.vhromada.common.facade.AbstractMovableChildFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.utils.sorted
import cz.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of facade for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeFacade")
class EpisodeFacadeImpl(
        showService: MovableService<Show>,
        mapper: Mapper<Episode, cz.vhromada.catalog.domain.Episode>,
        seasonValidator: MovableValidator<Season>,
        episodeValidator: MovableValidator<Episode>
) : AbstractMovableChildFacade<Episode, cz.vhromada.catalog.domain.Episode, Season, Show>(showService, mapper, seasonValidator, episodeValidator), EpisodeFacade {

    override fun getDomainData(id: Int): cz.vhromada.catalog.domain.Episode? {
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

    override fun getDomainList(parent: Season): List<cz.vhromada.catalog.domain.Episode> {
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

    override fun getForAdd(parent: Season, data: cz.vhromada.catalog.domain.Episode): Show {
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
    private fun getSeason(show: Show, season: Season): cz.vhromada.catalog.domain.Season {
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
    private fun getSeason(show: Show, episode: Movable): cz.vhromada.catalog.domain.Season {
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
    private fun getEpisode(id: Int?, show: Show): cz.vhromada.catalog.domain.Episode {
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
    private fun updateSeason(show: Show, season: cz.vhromada.catalog.domain.Season): List<cz.vhromada.catalog.domain.Season> {
        val seasons = mutableListOf<cz.vhromada.catalog.domain.Season>()
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
    private fun updateEpisode(season: cz.vhromada.catalog.domain.Season, episode: cz.vhromada.catalog.domain.Episode): List<cz.vhromada.catalog.domain.Episode> {
        val episodes = mutableListOf<cz.vhromada.catalog.domain.Episode>()
        for (episodeDomain in season.episodes) {
            if (episodeDomain == episode) {
                episodes.add(episode)
            } else {
                episodes.add(episodeDomain)
            }
        }
        return episodes
    }

}