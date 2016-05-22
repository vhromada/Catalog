package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.dao.entities.Episode;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for episodes.
 *
 * @author Vladimir Hromada
 */
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
}
