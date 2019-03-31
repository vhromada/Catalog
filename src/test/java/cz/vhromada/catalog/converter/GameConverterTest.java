package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.utils.GameUtils;

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
     * Instance of {@link GameConverter}
     */
    @Autowired
    private GameConverter converter;

    /**
     * Test method for {@link GameConverter#convert(Game)}.
     */
    @Test
    void convert() {
        final Game game = GameUtils.newGame(1);
        final cz.vhromada.catalog.domain.Game gameDomain = converter.convert(game);

        GameUtils.assertGameDeepEquals(game, gameDomain);
    }

    /**
     * Test method for {@link GameConverter#convertBack(cz.vhromada.catalog.domain.Game)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Game gameDomain = GameUtils.newGameDomain(1);
        final Game game = converter.convertBack(gameDomain);

        GameUtils.assertGameDeepEquals(game, gameDomain);
    }

}
