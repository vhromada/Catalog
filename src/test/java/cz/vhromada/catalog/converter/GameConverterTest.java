package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.GameUtils;
import cz.vhromada.catalog.entity.Game;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Game} and {@link Game}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class GameConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO.
     */
    @Test
    public void testConvertGame() {
        final cz.vhromada.catalog.domain.Game game = GameUtils.newGame(1);
        final Game gameTO = converter.convert(game, Game.class);

        GameUtils.assertGameDeepEquals(gameTO, game);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertGame_NullArgument() {
        assertNull(converter.convert(null, Game.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertGameTO() {
        final Game gameTO = GameUtils.newGameTO(1);
        final cz.vhromada.catalog.domain.Game game = converter.convert(gameTO, cz.vhromada.catalog.domain.Game.class);

        assertNotNull(game);
        GameUtils.assertGameDeepEquals(gameTO, game);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertGameTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Game.class));
    }

}
