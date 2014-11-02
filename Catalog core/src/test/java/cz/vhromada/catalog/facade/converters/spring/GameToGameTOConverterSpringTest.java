package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.converters.GameToGameTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class GameToGameTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link cz.vhromada.catalog.facade.converters.GameToGameTOConverter#convert(Game)}. */
	@Test
	public void testConvert() {
		final Game game = objectGenerator.generate(Game.class);
		final GameTO gameTO = conversionService.convert(game, GameTO.class);
		DeepAsserts.assertNotNull(gameTO);
		DeepAsserts.assertEquals(game, gameTO, "additionalData");
	}

	/** Test method for {@link cz.vhromada.catalog.facade.converters.GameToGameTOConverter#convert(Game)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, GameTO.class));
	}

}
