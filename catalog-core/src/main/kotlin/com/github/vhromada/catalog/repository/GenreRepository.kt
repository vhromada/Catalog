package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Genre
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for genres.
 *
 * @author Vladimir Hromada
 */
interface GenreRepository : JpaRepository<Genre, Int> {

    /**
     * Returns all genres created by user.
     *
     * @param user user's UUID
     * @return all genres created by user
     */
    fun findByAuditCreatedUser(user: String): List<Genre>

}
