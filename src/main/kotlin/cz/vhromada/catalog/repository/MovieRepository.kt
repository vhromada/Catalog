package cz.vhromada.catalog.repository

import cz.vhromada.catalog.domain.Movie
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for movies.
 *
 * @author Vladimir Hromada
 */
interface MovieRepository : JpaRepository<Movie, Int> {

    /**
     * Returns all movies created by user.
     *
     * @param user user's ID
     * @return all movies created by user
     */
    fun findByAuditCreatedUser(user: Int): List<Movie>

}
