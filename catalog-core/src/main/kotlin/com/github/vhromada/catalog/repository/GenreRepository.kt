package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Genre
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

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
    fun findByCreatedUser(user: String): List<Genre>

    /**
     * Returns genre with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return genre with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Genre>

}
