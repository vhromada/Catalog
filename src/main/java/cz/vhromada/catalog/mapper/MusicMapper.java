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

    /**
     * Maps entity music to domain music.
     *
     * @param source entity music
     * @return mapped domain music
     */
    @Mapping(target = "songs", ignore = true)
    cz.vhromada.catalog.domain.Music map(Music source);

    /**
     * Maps domain music to entity music.
     *
     * @param source domain music
     * @return mapped entity music
     */
    Music mapBack(cz.vhromada.catalog.domain.Music source);

}
