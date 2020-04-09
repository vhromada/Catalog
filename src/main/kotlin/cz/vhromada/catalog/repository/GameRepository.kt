package cz.vhromada.catalog.repository

import cz.vhromada.catalog.domain.Game
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for games.
 *
 * @author Vladimir Hromada
 */
interface GameRepository : JpaRepository<Game, Int> {

    /**
     * Returns all games created by user.
     *
     * @param user user's ID
     * @return all games created by user
     */
    fun findByAuditCreatedUser(user: Int): List<Game>

}
