package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.dao.entities.Season;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for seasons.
 *
 * @author Vladimir Hromada
 */
public interface SeasonRepository extends JpaRepository<Season, Integer> {
}
