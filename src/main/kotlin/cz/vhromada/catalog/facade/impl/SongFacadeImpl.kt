package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.entity.Song
import cz.vhromada.catalog.facade.SongFacade
import cz.vhromada.common.facade.AbstractMovableChildFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.utils.sorted
import cz.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songFacade")
class SongFacadeImpl(
        musicService: MovableService<cz.vhromada.catalog.domain.Music>,
        mapper: Mapper<Song, cz.vhromada.catalog.domain.Song>,
        musicValidator: MovableValidator<Music>,
        songValidator: MovableValidator<Song>
) : AbstractMovableChildFacade<Song, cz.vhromada.catalog.domain.Song, Music, cz.vhromada.catalog.domain.Music>(musicService, mapper, musicValidator, songValidator), SongFacade {

    override fun getDomainData(id: Int): cz.vhromada.catalog.domain.Song? {
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

    override fun getDomainList(parent: Music): List<cz.vhromada.catalog.domain.Song> {
        return service.get(parent.id!!)!!.songs
    }

    override fun getForAdd(parent: Music, data: cz.vhromada.catalog.domain.Song): cz.vhromada.catalog.domain.Music {
        val music = service.get(parent.id!!)!!
        val songs = music.songs.toMutableList()
        songs.add(data)

        return music.copy(songs = songs)
    }

    override fun getForUpdate(data: Song): cz.vhromada.catalog.domain.Music {
        val music = getMusic(data)

        return music.copy(songs = updateSong(music, getDataForUpdate(data)))
    }

    override fun getForRemove(data: Song): cz.vhromada.catalog.domain.Music {
        val music = getMusic(data)
        val songs = music.songs.filter { it.id != data.id }

        return music.copy(songs = songs)
    }

    override fun getForDuplicate(data: Song): cz.vhromada.catalog.domain.Music {
        val music = getMusic(data)
        val song = getSong(data.id, music)
        val songs = music.songs.toMutableList()
        songs.add(song.copy(id = null))

        return music.copy(songs = songs)
    }

    @Suppress("DuplicatedCode")
    override fun getForMove(data: Song, up: Boolean): cz.vhromada.catalog.domain.Music {
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
    private fun getMusic(song: Song): cz.vhromada.catalog.domain.Music {
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
    private fun getSong(id: Int?, music: cz.vhromada.catalog.domain.Music): cz.vhromada.catalog.domain.Song {
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
    private fun updateSong(music: cz.vhromada.catalog.domain.Music, song: cz.vhromada.catalog.domain.Song): List<cz.vhromada.catalog.domain.Song> {
        val songs = mutableListOf<cz.vhromada.catalog.domain.Song>()
        for (songDomain in music.songs) {
            if (songDomain == song) {
                songs.add(song)
            } else {
                songs.add(songDomain)
            }
        }

        return songs
    }

}
