package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.mapper.GameMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for game.
 *
 * @author Vladimir Hromada
 */
@Component
public class GameConverter implements MovableConverter<Game, cz.vhromada.catalog.domain.Game> {

    /**
     * Mapper for game
     */
    private GameMapper mapper;

    /**
     * Creates a new instance of GameConverter.
     */
    public GameConverter() {
        this.mapper = Mappers.getMapper(GameMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Game convert(final Game source) {
        return mapper.map(source);
    }

    @Override
    public Game convertBack(final cz.vhromada.catalog.domain.Game source) {
        return mapper.mapBack(source);
    }

}
