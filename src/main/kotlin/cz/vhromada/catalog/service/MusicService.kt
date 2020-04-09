package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Music
import cz.vhromada.catalog.repository.MusicRepository
import cz.vhromada.common.entity.Account
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
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
        private val musicRepository: MusicRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Music>(musicRepository, accountProvider, timeProvider, cache, "music") {

    override fun getAccountData(account: Account): List<Music> {
        return musicRepository.findByAuditCreatedUser(account.id)
    }

    override fun getCopy(data: Music): Music {
        return data.copy(id = null, songs = data.songs.map { it.copy(id = null) })
    }

    @Suppress("DuplicatedCode")
    override fun updatePositions(data: List<Music>) {
        val audit = getAudit()
        for (i in data.indices) {
            val music = data[i]
            music.position = i
            music.modify(audit)
            val songs = music.songs.sorted()
            for (j in songs.indices) {
                val song = songs[j]
                song.position = j
                song.modify(audit)
            }
        }
    }

}
