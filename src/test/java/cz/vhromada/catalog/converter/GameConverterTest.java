package cz.vhromada.catalog.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Game} and {@link Game}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class GameConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertGameDomain() {
        final cz.vhromada.catalog.domain.Game gameDomain = GameUtils.newGameDomain(1);
        final Game game = converter.convert(gameDomain, Game.class);

        GameUtils.assertGameDeepEquals(game, gameDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null game.
     */
    @Test
    void convertGameDomain_NullGame() {
        assertNull(converter.convert(null, Game.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertGame() {
        final Game game = GameUtils.newGame(1);
        final cz.vhromada.catalog.domain.Game gameDomain = converter.convert(game, cz.vhromada.catalog.domain.Game.class);

        GameUtils.assertGameDeepEquals(game, gameDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null game.
     */
    @Test
    void convertGame_NullGame() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Game.class));
    }

}
