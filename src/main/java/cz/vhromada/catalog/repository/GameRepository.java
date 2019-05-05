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
        assertGame(data);

        return save(data);
    }

    @Override
    default Game update(final Game data) {
        assertGame(data);

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
        assertGame(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    /**
     * Checks game
     *
     * @param game game
     * @throws IllegalArgumentException if game is null
     */
    private static void assertGame(final Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game mustn't be null.");
        }
    }

}
