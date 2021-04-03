package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Episode
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

/**
 * An interface represents repository for episodes.
 *
 * @author Vladimir Hromada
 */
interface EpisodeRepository : JpaRepository<Episode, Int> {

    /**
     * Returns episodes for season.
     *
     * @param id season ID
     * @return episodes for season
     */
    fun findAllBySeasonId(id: Int): List<Episode>

    /**
     * Returns episodes created by user for season.
     *
     * @param id   season ID
     * @param user user's UUID
     * @return episodes created by user for season
     */
    fun findAllBySeasonIdAndCreatedUser(id: Int, user: String): List<Episode>

    /**
     * Returns episode with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return episode with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Episode>

}
