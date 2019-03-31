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
        assertData(data);

        return save(data);
    }

    @Override
    default Movie update(final Movie data) {
        assertData(data);

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
        assertData(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    private static void assertData(final Movie data) {
        if (data == null) {
            throw new IllegalArgumentException("Data mustn't be null.");
        }
    }

}
