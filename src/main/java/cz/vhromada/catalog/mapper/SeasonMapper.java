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

    @Mapping(target = "episodes", ignore = true)
    cz.vhromada.catalog.domain.Season map(Season source);

    Season mapBack(cz.vhromada.catalog.domain.Season source);

}
