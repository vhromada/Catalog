package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.domain.Picture;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for pictures.
 *
 * @author Vladimir Hromada
 */
public interface PictureRepository extends JpaRepository<Picture, Integer> {
}
