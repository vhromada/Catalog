package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Season;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * An interface represents mapper for season.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface SeasonMapper {

    /**
     * Maps entity season to domain season.
     *
     * @param source entity season
     * @return mapped domain season
     */
    @Mapping(target = "episodes", ignore = true)
    cz.vhromada.catalog.domain.Season map(Season source);

    /**
     * Maps domain season to entity season.
     *
     * @param source domain season
     * @return mapped entity season
     */
    Season mapBack(cz.vhromada.catalog.domain.Season source);

}
