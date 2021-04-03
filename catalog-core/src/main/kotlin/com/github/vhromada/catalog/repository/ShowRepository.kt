package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Show
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

/**
 * An interface represents repository for shows.
 *
 * @author Vladimir Hromada
 */
interface ShowRepository : JpaRepository<Show, Int> {

    /**
     * Returns all shows created by user.
     *
     * @param user user's UUID
     * @return all shows created by user
     */
    fun findByCreatedUser(user: String): List<Show>

    /**
     * Returns show with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return show with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Show>

}
