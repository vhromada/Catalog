package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Game;

import org.mapstruct.Mapper;

/**
 * An interface represents mapper for game.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface GameMapper {

    cz.vhromada.catalog.domain.Game map(Game source);

    Game mapBack(cz.vhromada.catalog.domain.Game source);

}
