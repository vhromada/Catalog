package cz.vhromada.catalog.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Game} and {@link Game}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class GameConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void convertGameDomain() {
        final cz.vhromada.catalog.domain.Game gameDomain = GameUtils.newGameDomain(1);
        final Game game = converter.convert(gameDomain, Game.class);

        GameUtils.assertGameDeepEquals(game, gameDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void convertGameDomain_NullArgument() {
        assertThat(converter.convert(null, Game.class), is(nullValue()));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void convertGame() {
        final Game game = GameUtils.newGame(1);
        final cz.vhromada.catalog.domain.Game gameDomain = converter.convert(game, cz.vhromada.catalog.domain.Game.class);

        assertThat(gameDomain, is(notNullValue()));
        GameUtils.assertGameDeepEquals(game, gameDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void convertGame_NullArgument() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Game.class), is(nullValue()));
    }

}
