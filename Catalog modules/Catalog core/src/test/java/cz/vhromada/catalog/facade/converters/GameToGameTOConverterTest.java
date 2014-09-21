package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GameToGameTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class GameToGameTOConverterTest {

	/** Instance of {@link GameToGameTOConverter} */
	private GameToGameTOConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new GameToGameTOConverter();
	}

	/** Test method for {@link GameToGameTOConverter#convert(Game)}. */
	@Test
	public void testConvert() {
		final Game game = EntityGenerator.createGame(ID);
		final GameTO gameTO = converter.convert(game);
		DeepAsserts.assertNotNull(gameTO);
		DeepAsserts.assertEquals(game, gameTO, "additionalData");
	}

	/** Test method for {@link GameToGameTOConverter#convert(Game)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}