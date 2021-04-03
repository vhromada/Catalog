package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Episode
import com.github.vhromada.catalog.domain.Season
import com.github.vhromada.catalog.domain.Show
import com.github.vhromada.catalog.repository.ShowRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractParentService
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents service for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showService")
class ShowService(
    private val showRepository: ShowRepository,
    accountProvider: AccountProvider
) : AbstractParentService<Show>(repository = showRepository, accountProvider = accountProvider) {

    override fun getCopy(data: Show): Show {
        val show = data.copy(id = null, genres = data.genres.map { it }, seasons = mutableListOf())
        show.seasons.addAll(data.seasons.map { getCopy(season = it, show = show) })
        return show
    }

    override fun getAccountData(account: Account, id: Int): Optional<Show> {
        return showRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun getAccountDataList(account: Account): List<Show> {
        return showRepository.findByCreatedUser(user = account.uuid!!)
    }

    /**
     * Returns copy of season.
     *
     * @param season season
     * @param show   show
     * @return copy of season
     */
    private fun getCopy(season: Season, show: Show): Season {
        val result = season.copy(id = null, subtitles = season.subtitles.map { it }, episodes = mutableListOf())
        result.episodes.addAll(season.episodes.map { getCopy(episode = it, season = season) })
        result.show = show
        return result
    }

    /**
     * Returns copy of episode.
     *
     * @param episode episode
     * @param season  season
     * @return copy of episode
     */
    private fun getCopy(episode: Episode, season: Season): Episode {
        val result = episode.copy(id = null)
        result.season = season
        return result
    }

}
