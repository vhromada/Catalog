package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Movie
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for movies.
 *
 * @author Vladimir Hromada
 */
interface MovieRepository : JpaRepository<Movie, Int> {

    /**
     * Returns all movies created by user.
     *
     * @param user user's UUID
     * @return all movies created by user
     */
    fun findByAuditCreatedUser(user: String): List<Movie>

}
