package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GameTOToGameConverter}.
 *
 * @author Vladimir Hromada
 */
public class GameTOToGameConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link GameTOToGameConverter} */
    private GameTOToGameConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new GameTOToGameConverter();
    }

    /** Test method for {@link GameTOToGameConverter#convert(GameTO)}. */
    @Test
    public void testConvert() {
        final GameTO gameTO = generate(GameTO.class);
        final Game game = converter.convert(gameTO);
        DeepAsserts.assertNotNull(game);
        DeepAsserts.assertEquals(gameTO, game, "additionalData");
    }

    /** Test method for {@link GameTOToGameConverter#convert(GameTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

}
