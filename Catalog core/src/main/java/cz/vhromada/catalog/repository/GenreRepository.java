package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.entities.Genre;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for genre.
 *
 * @author Vladimir Hromada
 */
public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
