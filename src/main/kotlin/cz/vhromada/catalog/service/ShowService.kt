package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Season
import cz.vhromada.catalog.domain.Show
import cz.vhromada.catalog.repository.ShowRepository
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
        showRepository: ShowRepository,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Show>(showRepository, cache, "shows") {

    override fun getCopy(data: Show): Show {
        return data.copy(id = null, seasons = data.seasons.map { getCopy(it) }, genres = data.genres.map { it })
    }

    override fun updatePositions(data: List<Show>) {
        for (i in data.indices) {
            val show = data[i]
            show.position = i
            val seasons = show.seasons.sorted()
            for (j in seasons.indices) {
                val season = seasons[j]
                season.position = j
                val episodes = season.episodes.sorted()
                for (k in episodes.indices) {
                    episodes[k].position = k
                }
            }
        }
    }

    private fun getCopy(data: Season): Season {
        return data.copy(id = null, subtitles = data.subtitles.map { it }, episodes = data.episodes.map { it.copy(id = null) })
    }

}
