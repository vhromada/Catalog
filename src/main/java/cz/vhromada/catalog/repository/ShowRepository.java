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
        assertData(data);

        return save(data);
    }

    @Override
    default Show update(final Show data) {
        assertData(data);

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
        assertData(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    private static void assertData(final Show data) {
        if (data == null) {
            throw new IllegalArgumentException("Data mustn't be null.");
        }
    }

}
