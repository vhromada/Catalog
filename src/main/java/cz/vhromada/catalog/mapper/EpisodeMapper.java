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

    /**
     * Maps entity episode to domain episode.
     *
     * @param source entity episode
     * @return mapped domain episode
     */
    cz.vhromada.catalog.domain.Episode map(Episode source);

    /**
     * Maps domain episode to entity episode.
     *
     * @param source domain episode
     * @return mapped entity episode
     */
    Episode mapBack(cz.vhromada.catalog.domain.Episode source);

}
