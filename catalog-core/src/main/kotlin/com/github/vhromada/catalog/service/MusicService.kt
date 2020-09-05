package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Music
import com.github.vhromada.catalog.repository.MusicRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.AbstractMovableService
import com.github.vhromada.common.utils.sorted
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicService")
@Suppress("SpringElInspection", "ELValidationInJSP")
class MusicService(
        private val musicRepository: MusicRepository,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Music>(musicRepository, accountProvider, timeProvider, cache, "music") {

    override fun getAccountData(account: Account): List<Music> {
        return musicRepository.findByAuditCreatedUser(account.uuid)
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
