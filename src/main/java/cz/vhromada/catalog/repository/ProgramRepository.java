package cz.vhromada.catalog.repository;

import java.util.List;

import cz.vhromada.catalog.domain.Program;
import cz.vhromada.common.repository.MovableRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;

/**
 * An interface represents repository for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramRepository extends MovableRepository<Program>, JpaRepository<Program, Integer> {

    @Override
    default List<Program> getAll() {
        return findAll();
    }

    @Override
    default Program add(final Program data) {
        assertData(data);

        return save(data);
    }

    @Override
    default Program update(final Program data) {
        assertData(data);

        return save(data);
    }

    @Override
    default List<Program> updateAll(final List<Program> data) {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data mustn't be null or empty.");
        }

        return saveAll(data);
    }

    @Override
    default void remove(final Program data) {
        assertData(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    private static void assertData(final Program data) {
        if (data == null) {
            throw new IllegalArgumentException("Data mustn't be null.");
        }
    }

}
