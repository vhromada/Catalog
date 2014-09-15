package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.converters.GameToGameTOConverter;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link GameToGameTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class GameToGameTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link GameToGameTOConverter#convert(Game)}. */
	@Test
	public void testConvert() {
		final Game game = EntityGenerator.createGame(ID);
		final GameTO gameTO = conversionService.convert(game, GameTO.class);
		DeepAsserts.assertNotNull(gameTO);
		DeepAsserts.assertEquals(game, gameTO, "additionalData");
	}

	/** Test method for {@link GameToGameTOConverter#convert(Game)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, GameTO.class));
	}

}
