package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.facade.SongFacade
import com.github.vhromada.common.facade.AbstractMovableChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.sorted
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songFacade")
class SongFacadeImpl(
        musicService: MovableService<com.github.vhromada.catalog.domain.Music>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Song, com.github.vhromada.catalog.domain.Song>,
        musicValidator: MovableValidator<Music>,
        songValidator: MovableValidator<Song>
) : AbstractMovableChildFacade<Song, com.github.vhromada.catalog.domain.Song, Music, com.github.vhromada.catalog.domain.Music>(musicService, accountProvider, timeProvider, mapper, musicValidator,
        songValidator), SongFacade {

    override fun getDomainData(id: Int): com.github.vhromada.catalog.domain.Song? {
        val musicList = service.getAll()
        for (music in musicList) {
            for (song in music.songs) {
                if (id == song.id) {
                    return song
                }
            }
        }

        return null
    }

    override fun getDomainList(parent: Music): List<com.github.vhromada.catalog.domain.Song> {
        return service.get(parent.id!!).get().songs
    }

    override fun getForAdd(parent: Music, data: com.github.vhromada.catalog.domain.Song): com.github.vhromada.catalog.domain.Music {
        val music = service.get(parent.id!!).get()
        val songs = music.songs.toMutableList()
        songs.add(data)

        return music.copy(songs = songs)
    }

    override fun getForUpdate(data: Song): com.github.vhromada.catalog.domain.Music {
        val music = getMusic(data)

        return music.copy(songs = updateSong(music, getDataForUpdate(data)))
    }

    override fun getForRemove(data: Song): com.github.vhromada.catalog.domain.Music {
        val music = getMusic(data)
        val songs = music.songs.filter { it.id != data.id }

        return music.copy(songs = songs)
    }

    override fun getForDuplicate(data: Song): com.github.vhromada.catalog.domain.Music {
        val music = getMusic(data)
        val song = getSong(data.id, music)
        val songs = music.songs.toMutableList()
        songs.add(song.copy(id = null))

        return music.copy(songs = songs)
    }

    @Suppress("DuplicatedCode")
    override fun getForMove(data: Song, up: Boolean): com.github.vhromada.catalog.domain.Music {
        var music = getMusic(data)
        val song = getSong(data.id, music)
        val songs = music.songs.sorted()

        val index = songs.indexOf(song)
        val other = songs[if (up) index - 1 else index + 1]
        val position = song.position!!
        song.position = other.position
        other.position = position

        music = music.copy(songs = updateSong(music, song))
        return music.copy(songs = updateSong(music, other))
    }

    /**
     * Returns music for song.
     *
     * @param song song
     * @return music for song
     */
    private fun getMusic(song: Song): com.github.vhromada.catalog.domain.Music {
        for (music in service.getAll()) {
            for (songDomain in music.songs) {
                if (song.id == songDomain.id) {
                    return music
                }
            }
        }

        throw IllegalStateException("Unknown song.")
    }

    /**
     * Returns song with ID.
     *
     * @param id    ID
     * @param music music
     * @return song with ID
     */
    private fun getSong(id: Int?, music: com.github.vhromada.catalog.domain.Music): com.github.vhromada.catalog.domain.Song {
        for (song in music.songs) {
            if (id == song.id) {
                return song
            }
        }

        throw IllegalStateException("Unknown song.")
    }

    /**
     * Updates song.
     *
     * @param music music
     * @param song  song
     * @return updated songs
     */
    @Suppress("DuplicatedCode")
    private fun updateSong(music: com.github.vhromada.catalog.domain.Music, song: com.github.vhromada.catalog.domain.Song): List<com.github.vhromada.catalog.domain.Song> {
        val songs = mutableListOf<com.github.vhromada.catalog.domain.Song>()
        for (songDomain in music.songs) {
            if (songDomain == song) {
                val audit = getAudit()
                song.audit = songDomain.audit!!.copy(updatedUser = audit.updatedUser, updatedTime = audit.updatedTime)
                songs.add(song)
            } else {
                songs.add(songDomain)
            }
        }

        return songs
    }

}
