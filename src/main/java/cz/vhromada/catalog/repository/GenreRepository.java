package cz.vhromada.catalog.repository;

import java.util.List;

import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.common.repository.MovableRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;

/**
 * An interface represents repository for genres.
 *
 * @author Vladimir Hromada
 */
public interface GenreRepository extends MovableRepository<Genre>, JpaRepository<Genre, Integer> {

    @Override
    default List<Genre> getAll() {
        return findAll();
    }

    @Override
    default Genre add(final Genre data) {
        assertGenre(data);

        return save(data);
    }

    @Override
    default Genre update(final Genre data) {
        assertGenre(data);

        return save(data);
    }

    @Override
    default List<Genre> updateAll(final List<Genre> data) {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data mustn't be null or empty.");
        }

        return saveAll(data);
    }

    @Override
    default void remove(final Genre data) {
        assertGenre(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    /**
     * Checks genre
     *
     * @param genre genre
     * @throws IllegalArgumentException if genre is null
     */
    private static void assertGenre(final Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre mustn't be null.");
        }
    }

}
