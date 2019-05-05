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

    /**
     * Maps entity game to domain game.
     *
     * @param source entity game
     * @return mapped domain game
     */
    cz.vhromada.catalog.domain.Game map(Game source);

    /**
     * Maps domain game to entity game.
     *
     * @param source domain game
     * @return mapped entity game
     */
    Game mapBack(cz.vhromada.catalog.domain.Game source);

}
