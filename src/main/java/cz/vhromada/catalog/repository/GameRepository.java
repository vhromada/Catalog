package cz.vhromada.catalog.repository;

import java.util.List;

import cz.vhromada.catalog.domain.Game;
import cz.vhromada.common.repository.MovableRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;

/**
 * An interface represents repository for games.
 *
 * @author Vladimir Hromada
 */
public interface GameRepository extends MovableRepository<Game>, JpaRepository<Game, Integer> {

    @Override
    default List<Game> getAll() {
        return findAll();
    }

    @Override
    default Game add(final Game data) {
        assertData(data);

        return save(data);
    }

    @Override
    default Game update(final Game data) {
        assertData(data);

        return save(data);
    }

    @Override
    default List<Game> updateAll(final List<Game> data) {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data mustn't be null or empty.");
        }

        return saveAll(data);
    }

    @Override
    default void remove(final Game data) {
        assertData(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    private static void assertData(final Game data) {
        if (data == null) {
            throw new IllegalArgumentException("Data mustn't be null.");
        }
    }

}
