package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.entities.Show;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for shows.
 *
 * @author Vladimir Hromada
 */
public interface ShowRepository extends JpaRepository<Show, Integer> {
}
