package cz.vhromada.catalog.repository;

import java.util.List;

import cz.vhromada.catalog.domain.Music;
import cz.vhromada.common.repository.MovableRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;

/**
 * An interface represents repository for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicRepository extends MovableRepository<Music>, JpaRepository<Music, Integer> {

    @Override
    default List<Music> getAll() {
        return findAll();
    }

    @Override
    default Music add(final Music data) {
        assertData(data);

        return save(data);
    }

    @Override
    default Music update(final Music data) {
        assertData(data);

        return save(data);
    }

    @Override
    default List<Music> updateAll(final List<Music> data) {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data mustn't be null or empty.");
        }

        return saveAll(data);
    }

    @Override
    default void remove(final Music data) {
        assertData(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    private static void assertData(final Music data) {
        if (data == null) {
            throw new IllegalArgumentException("Data mustn't be null.");
        }
    }

}
