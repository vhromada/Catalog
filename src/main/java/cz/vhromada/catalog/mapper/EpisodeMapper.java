package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Episode;

import org.mapstruct.Mapper;

/**
 * An interface represents mapper for episode.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface EpisodeMapper {

    cz.vhromada.catalog.domain.Episode map(Episode source);

    Episode mapBack(cz.vhromada.catalog.domain.Episode source);

}
