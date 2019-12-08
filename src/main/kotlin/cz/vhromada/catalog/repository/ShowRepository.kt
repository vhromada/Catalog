package cz.vhromada.catalog.repository

import cz.vhromada.catalog.domain.Show
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for shows.
 *
 * @author Vladimir Hromada
 */
interface ShowRepository : JpaRepository<Show, Int>