package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Music;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * An interface represents mapper for music.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface MusicMapper {

    @Mapping(target = "songs", ignore = true)
    cz.vhromada.catalog.domain.Music map(Music source);

    Music mapBack(cz.vhromada.catalog.domain.Music source);

}
