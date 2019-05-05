package cz.vhromada.catalog.repository;

import java.util.List;

import cz.vhromada.catalog.domain.Movie;
import cz.vhromada.common.repository.MovableRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;

/**
 * An interface represents repository for movies.
 *
 * @author Vladimir Hromada
 */
public interface MovieRepository extends MovableRepository<Movie>, JpaRepository<Movie, Integer> {

    @Override
    default List<Movie> getAll() {
        return findAll();
    }

    @Override
    default Movie add(final Movie data) {
        assertMovie(data);

        return save(data);
    }

    @Override
    default Movie update(final Movie data) {
        assertMovie(data);

        return save(data);
    }

    @Override
    default List<Movie> updateAll(final List<Movie> data) {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data mustn't be null or empty.");
        }

        return saveAll(data);
    }

    @Override
    default void remove(final Movie data) {
        assertMovie(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    /**
     * Checks movie
     *
     * @param movie movie
     * @throws IllegalArgumentException if movie is null
     */
    private static void assertMovie(final Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie mustn't be null.");
        }
    }

}
