package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Music
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

/**
 * An interface represents repository for music.
 *
 * @author Vladimir Hromada
 */
interface MusicRepository : JpaRepository<Music, Int> {

    /**
     * Returns all music created by user.
     *
     * @param user user's UUID
     * @return all music created by user
     */
    fun findByCreatedUser(user: String): List<Music>

    /**
     * Returns music with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return music with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Music>

}
