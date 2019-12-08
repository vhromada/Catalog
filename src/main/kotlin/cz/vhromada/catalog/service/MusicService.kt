package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Music
import cz.vhromada.catalog.repository.MusicRepository
import cz.vhromada.common.service.AbstractMovableService
import cz.vhromada.common.utils.sorted
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicService")
class MusicService(
        musicRepository: MusicRepository,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Music>(musicRepository, cache, "music") {

    override fun getCopy(data: Music): Music {
        return data.copy(id = null, songs = data.songs.map { it.copy(id = null) })
    }

    override fun updatePositions(data: List<Music>) {
        for (i in data.indices) {
            val music = data[i]
            music.position = i
            val songs = music.songs.sorted()
            for (j in songs.indices) {
                songs[j].position = j
            }
        }
    }

}
