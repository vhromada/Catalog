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

    /**
     * Maps entity show to domain show.
     *
     * @param source entity show
     * @return mapped domain show
     */
    @Mapping(target = "seasons", ignore = true)
    cz.vhromada.catalog.domain.Show map(Show source);

    /**
     * Maps domain show to entity show.
     *
     * @param source domain show
     * @return mapped entity show
     */
    Show mapBack(cz.vhromada.catalog.domain.Show source);

}
