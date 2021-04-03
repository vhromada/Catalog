package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Song
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

/**
 * An interface represents repository for songs.
 *
 * @author Vladimir Hromada
 */
interface SongRepository : JpaRepository<Song, Int> {

    /**
     * Returns songs for music.
     *
     * @param id music ID
     * @return songs for music
     */
    fun findAllByMusicId(id: Int): List<Song>

    /**
     * Returns songs created by user for music.
     *
     * @param id   music ID
     * @param user user's UUID
     * @return songs created by user for music
     */
    fun findAllByMusicIdAndCreatedUser(id: Int, user: String): List<Song>

    /**
     * Returns song with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return song with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Song>

}
