package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Game
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
     * @param user user's UUID
     * @return all games created by user
     */
    fun findByAuditCreatedUser(user: String): List<Game>

}
