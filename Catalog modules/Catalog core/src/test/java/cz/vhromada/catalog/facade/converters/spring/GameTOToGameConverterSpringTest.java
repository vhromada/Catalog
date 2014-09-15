package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.converters.GameTOToGameConverter;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link GameTOToGameConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class GameTOToGameConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link GameTOToGameConverter#convert(GameTO)}. */
	@Test
	public void testConvert() {
		final GameTO gameTO = ToGenerator.createGame(ID);
		final Game game = conversionService.convert(gameTO, Game.class);
		DeepAsserts.assertNotNull(game);
		DeepAsserts.assertEquals(gameTO, game, "additionalData");
	}

	/** Test method for {@link GameTOToGameConverter#convert(GameTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Game.class));
	}

}
