package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Season
import cz.vhromada.catalog.domain.Show
import cz.vhromada.catalog.repository.ShowRepository
import cz.vhromada.common.entity.Account
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.AbstractMovableService
import cz.vhromada.common.utils.sorted
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showService")
class ShowService(
        private val showRepository: ShowRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Show>(showRepository, accountProvider, timeProvider, cache, "shows") {

    override fun getAccountData(account: Account): List<Show> {
        return showRepository.findByAuditCreatedUser(account.id)
    }

    override fun getCopy(data: Show): Show {
        return data.copy(id = null, seasons = data.seasons.map { getCopy(it) }, genres = data.genres.map { it })
    }

    @Suppress("DuplicatedCode")
    override fun updatePositions(data: List<Show>) {
        val audit = getAudit()
        for (i in data.indices) {
            val show = data[i]
            show.position = i
            show.modify(audit)
            val seasons = show.seasons.sorted()
            for (j in seasons.indices) {
                val season = seasons[j]
                season.position = j
                season.modify(audit)
                val episodes = season.episodes.sorted()
                for (k in episodes.indices) {
                    val episode = episodes[k]
                    episode.position = k
                    episode.modify(audit)
                }
            }
        }
    }

    private fun getCopy(data: Season): Season {
        return data.copy(id = null, subtitles = data.subtitles.map { it }, episodes = data.episodes.map { it.copy(id = null) })
    }

}
