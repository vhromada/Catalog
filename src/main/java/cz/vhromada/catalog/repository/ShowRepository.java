package cz.vhromada.catalog.repository;

import java.util.List;

import cz.vhromada.catalog.domain.Show;
import cz.vhromada.common.repository.MovableRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;

/**
 * An interface represents repository for shows.
 *
 * @author Vladimir Hromada
 */
public interface ShowRepository extends MovableRepository<Show>, JpaRepository<Show, Integer> {

    @Override
    default List<Show> getAll() {
        return findAll();
    }

    @Override
    default Show add(final Show data) {
        assertShow(data);

        return save(data);
    }

    @Override
    default Show update(final Show data) {
        assertShow(data);

        return save(data);
    }

    @Override
    default List<Show> updateAll(final List<Show> data) {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data mustn't be null or empty.");
        }

        return saveAll(data);
    }

    @Override
    default void remove(final Show data) {
        assertShow(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    /**
     * Checks show
     *
     * @param show show
     * @throws IllegalArgumentException if show is null
     */
    private static void assertShow(final Show show) {
        if (show == null) {
            throw new IllegalArgumentException("Show mustn't be null.");
        }
    }

}
