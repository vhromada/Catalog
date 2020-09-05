package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Music
import org.springframework.data.jpa.repository.JpaRepository

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
    fun findByAuditCreatedUser(user: String): List<Music>

}
