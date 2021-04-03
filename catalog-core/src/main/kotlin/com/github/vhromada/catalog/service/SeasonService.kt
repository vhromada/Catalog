package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Episode
import com.github.vhromada.catalog.domain.Season
import com.github.vhromada.catalog.domain.Show
import com.github.vhromada.catalog.repository.SeasonRepository
import com.github.vhromada.catalog.repository.ShowRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractChildService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * A class represents service for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonService")
class SeasonService(
    private val seasonRepository: SeasonRepository,
    private val showRepository: ShowRepository,
    accountProvider: AccountProvider
) : AbstractChildService<Season, Show>(repository = seasonRepository, accountProvider = accountProvider) {

    @Transactional
    override fun remove(data: Season) {
        val show = getParent(data = data)
        show.seasons.remove(data)
        showRepository.save(show)
    }

    override fun getCopy(data: Season): Season {
        val season = data.copy(id = null, subtitles = data.subtitles.map { it }, episodes = mutableListOf())
        season.episodes.addAll(data.episodes.map { getCopy(episode = it, season = season) })
        season.show = data.show
        return season
    }

    override fun getAccountData(account: Account, id: Int): Optional<Season> {
        return seasonRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun findByParent(parent: Int): List<Season> {
        return seasonRepository.findAllByShowId(id = parent)
    }

    override fun getParent(data: Season): Show {
        return data.show!!
    }

    override fun getAccountDataList(account: Account, parent: Int): List<Season> {
        return seasonRepository.findAllByShowIdAndCreatedUser(id = parent, user = account.uuid!!)
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
