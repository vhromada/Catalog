package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Music
import com.github.vhromada.catalog.domain.Song
import com.github.vhromada.catalog.repository.MusicRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractParentService
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * A class represents service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicService")
class MusicService(
    private val musicRepository: MusicRepository,
    accountProvider: AccountProvider
) : AbstractParentService<Music>(repository = musicRepository, accountProvider = accountProvider) {

    override fun getCopy(data: Music): Music {
        val music = data.copy(id = null, songs = mutableListOf())
        music.songs.addAll(data.songs.map { getCopy(song = it, music = music) })
        return music
    }

    override fun getAccountData(account: Account, id: Int): Optional<Music> {
        return musicRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun getAccountDataList(account: Account): List<Music> {
        return musicRepository.findByCreatedUser(user = account.uuid!!)
    }

    /**
     * Returns copy of song.
     *
     * @param song  song
     * @param music music
     * @return copy of song
     */
    private fun getCopy(song: Song, music: Music): Song {
        val result = song.copy(id = null)
        result.music = music
        return result
    }

}
