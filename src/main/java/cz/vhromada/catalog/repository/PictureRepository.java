package cz.vhromada.catalog.repository;

import java.util.List;

import cz.vhromada.catalog.domain.Picture;
import cz.vhromada.common.repository.MovableRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;

/**
 * An interface represents repository for pictures.
 *
 * @author Vladimir Hromada
 */
public interface PictureRepository extends MovableRepository<Picture>, JpaRepository<Picture, Integer> {

    @Override
    default List<Picture> getAll() {
        return findAll();
    }

    @Override
    default Picture add(final Picture data) {
        assertPicture(data);

        return save(data);
    }

    @Override
    default Picture update(final Picture data) {
        assertPicture(data);

        return save(data);
    }

    @Override
    default List<Picture> updateAll(final List<Picture> data) {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data mustn't be null or empty.");
        }

        return saveAll(data);
    }

    @Override
    default void remove(final Picture data) {
        assertPicture(data);

        delete(data);
    }

    @Override
    default void removeAll() {
        deleteAll();
    }

    /**
     * Checks picture
     *
     * @param picture picture
     * @throws IllegalArgumentException if picture is null
     */
    private static void assertPicture(final Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture mustn't be null.");
        }
    }

}
