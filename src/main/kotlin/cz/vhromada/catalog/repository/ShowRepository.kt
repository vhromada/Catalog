package cz.vhromada.catalog.repository

import cz.vhromada.catalog.domain.Show
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
     * @param user user's ID
     * @return all shows created by user
     */
    fun findByAuditCreatedUser(user: Int): List<Show>

}
