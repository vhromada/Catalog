package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.utils.GameUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Game} and {@link Game}.
 *
 * @author Vladimir Hromada
 */
class GameMapperTest {

    private GameMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(GameMapper.class);
    }

    /**
     * Test method for {@link GameMapper#map(Game)}.
     */
    @Test
    void map() {
        final Game game = GameUtils.newGame(1);
        final cz.vhromada.catalog.domain.Game gameDomain = mapper.map(game);

        GameUtils.assertGameDeepEquals(game, gameDomain);
    }

    /**
     * Test method for {@link GameMapper#map(Game)} with null game.
     */
    @Test
    void map_NullGame() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link GameMapper#mapBack(cz.vhromada.catalog.domain.Game)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Game gameDomain = GameUtils.newGameDomain(1);
        final Game game = mapper.mapBack(gameDomain);

        GameUtils.assertGameDeepEquals(game, gameDomain);
    }

    /**
     * Test method for {@link GameMapper#mapBack(cz.vhromada.catalog.domain.Game)} with null game.
     */
    @Test
    void mapBack_NullGame() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
