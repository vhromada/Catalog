package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.dao.entities.Movie;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for movies.
 *
 * @author Vladimir Hromada
 */
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}