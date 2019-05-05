package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Picture;

import org.mapstruct.Mapper;

/**
 * An interface represents mapper for picture.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface PictureMapper {

    /**
     * Maps entity picture to domain picture.
     *
     * @param source entity picture
     * @return mapped domain picture
     */
    cz.vhromada.catalog.domain.Picture map(Picture source);

    /**
     * Maps domain picture to entity picture.
     *
     * @param source domain picture
     * @return mapped entity picture
     */
    Picture mapBack(cz.vhromada.catalog.domain.Picture source);

}
