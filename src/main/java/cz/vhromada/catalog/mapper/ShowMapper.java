package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Show;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * An interface represents mapper for show.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface ShowMapper {

    @Mapping(target = "seasons", ignore = true)
    cz.vhromada.catalog.domain.Show map(Show source);

    Show mapBack(cz.vhromada.catalog.domain.Show source);

}
