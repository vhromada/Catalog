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

    cz.vhromada.catalog.domain.Song map(Song source);

    Song mapBack(cz.vhromada.catalog.domain.Song source);

}
