package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Movie
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

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
    fun findByCreatedUser(user: String): List<Movie>

    /**
     * Returns movie with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return movie with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Movie>

}
