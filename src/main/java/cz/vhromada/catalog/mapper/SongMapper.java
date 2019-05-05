package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Song;

import org.mapstruct.Mapper;

/**
 * An interface represents mapper for song.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface SongMapper {

    /**
     * Maps entity song to domain song.
     *
     * @param source entity song
     * @return mapped domain song
     */
    cz.vhromada.catalog.domain.Song map(Song source);

    /**
     * Maps domain song to entity song.
     *
     * @param source domain song
     * @return mapped entity song
     */
    Song mapBack(cz.vhromada.catalog.domain.Song source);

}
