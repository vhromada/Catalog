package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Cheat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

/**
 * An interface represents repository for cheats.
 *
 * @author Vladimir Hromada
 */
interface CheatRepository : JpaRepository<Cheat, Int> {

    /**
     * Returns cheats for game.
     *
     * @param id game ID
     * @return cheats for game
     */
    fun findAllByGameId(id: Int): List<Cheat>

    /**
     * Returns cheats created by user for game.
     *
     * @param id   game ID
     * @param user user's UUID
     * @return cheats created by user for game
     */
    fun findAllByGameIdAndCreatedUser(id: Int, user: String): List<Cheat>

    /**
     * Returns cheat with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return cheat with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Cheat>

}
