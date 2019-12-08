package cz.vhromada.catalog.repository

import cz.vhromada.catalog.domain.Genre
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for genres.
 *
 * @author Vladimir Hromada
 */
interface GenreRepository : JpaRepository<Genre, Int>

