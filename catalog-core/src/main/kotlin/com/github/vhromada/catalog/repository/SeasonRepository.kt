package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Season
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

/**
 * An interface represents repository for seasons.
 *
 * @author Vladimir Hromada
 */
interface SeasonRepository : JpaRepository<Season, Int> {

    /**
     * Returns seasons for show.
     *
     * @param id show ID
     * @return seasons for show
     */
    fun findAllByShowId(id: Int): List<Season>

    /**
     * Returns seasons created by user for show.
     *
     * @param id   show ID
     * @param user user's UUID
     * @return seasons created by user for show
     */
    fun findAllByShowIdAndCreatedUser(id: Int, user: String): List<Season>

    /**
     * Returns season with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return season with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Season>

}
