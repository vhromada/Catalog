package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Episode
import com.github.vhromada.catalog.domain.Season
import com.github.vhromada.catalog.repository.EpisodeRepository
import com.github.vhromada.catalog.repository.SeasonRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractChildService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * A class represents service for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeService")
class EpisodeService(
    private val episodeRepository: EpisodeRepository,
    private val seasonRepository: SeasonRepository,
    accountProvider: AccountProvider
) : AbstractChildService<Episode, Season>(repository = episodeRepository, accountProvider = accountProvider) {

    @Transactional
    override fun remove(data: Episode) {
        val season = getParent(data = data)
        season.episodes.remove(data)
        seasonRepository.save(season)
    }

    override fun getCopy(data: Episode): Episode {
        val episode = data.copy(id = null)
        episode.season = data.season
        return episode
    }

    override fun getAccountData(account: Account, id: Int): Optional<Episode> {
        return episodeRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun findByParent(parent: Int): List<Episode> {
        return episodeRepository.findAllBySeasonId(id = parent)
    }

    override fun getParent(data: Episode): Season {
        return data.season!!
    }

    override fun getAccountDataList(account: Account, parent: Int): List<Episode> {
        return episodeRepository.findAllBySeasonIdAndCreatedUser(id = parent, user = account.uuid!!)
    }

}
