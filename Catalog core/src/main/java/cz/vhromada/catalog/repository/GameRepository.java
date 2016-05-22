package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.dao.entities.Game;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for games.
 *
 * @author Vladimir Hromada
 */
public interface GameRepository extends JpaRepository<Game, Integer> {
}
