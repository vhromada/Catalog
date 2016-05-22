package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.dao.entities.Song;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for songs.
 *
 * @author Vladimir Hromada
 */
public interface SongRepository extends JpaRepository<Song, Integer> {
}
