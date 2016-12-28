package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.domain.Genre;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for genres.
 *
 * @author Vladimir Hromada
 */
public interface GenreRepository extends JpaRepository<Genre, Integer> {
}