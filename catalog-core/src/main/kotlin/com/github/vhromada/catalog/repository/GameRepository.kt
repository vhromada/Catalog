package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Game
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

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
    fun findByCreatedUser(user: String): List<Game>

    /**
     * Returns game with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return game with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Game>

}
