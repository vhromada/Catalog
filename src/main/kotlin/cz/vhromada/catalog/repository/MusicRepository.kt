package cz.vhromada.catalog.repository

import cz.vhromada.catalog.domain.Music
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for music.
 *
 * @author Vladimir Hromada
 */
interface MusicRepository : JpaRepository<Music, Int>
