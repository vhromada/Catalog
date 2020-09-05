package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Show
import org.springframework.data.jpa.repository.JpaRepository

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
    fun findByAuditCreatedUser(user: String): List<Show>

}
