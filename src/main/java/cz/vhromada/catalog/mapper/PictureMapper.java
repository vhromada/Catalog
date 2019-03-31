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

    cz.vhromada.catalog.domain.Picture map(Picture source);

    Picture mapBack(cz.vhromada.catalog.domain.Picture source);

}
