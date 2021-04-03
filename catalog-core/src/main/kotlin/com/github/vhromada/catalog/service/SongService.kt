package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Music
import com.github.vhromada.catalog.domain.Song
import com.github.vhromada.catalog.repository.MusicRepository
import com.github.vhromada.catalog.repository.SongRepository
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.service.AbstractChildService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * A class represents service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songService")
class SongService(
    private val songRepository: SongRepository,
    private val musicRepository: MusicRepository,
    accountProvider: AccountProvider
) : AbstractChildService<Song, Music>(repository = songRepository, accountProvider = accountProvider) {

    @Transactional
    override fun remove(data: Song) {
        val music = getParent(data = data)
        music.songs.remove(data)
        musicRepository.save(music)
    }

    override fun getCopy(data: Song): Song {
        val song = data.copy(id = null)
        song.music = data.music
        return song
    }

    override fun getAccountData(account: Account, id: Int): Optional<Song> {
        return songRepository.findByIdAndCreatedUser(id = id, user = account.uuid!!)
    }

    override fun findByParent(parent: Int): List<Song> {
        return songRepository.findAllByMusicId(id = parent)
    }

    override fun getParent(data: Song): Music {
        return data.music!!
    }

    override fun getAccountDataList(account: Account, parent: Int): List<Song> {
        return songRepository.findAllByMusicIdAndCreatedUser(id = parent, user = account.uuid!!)
    }

}
