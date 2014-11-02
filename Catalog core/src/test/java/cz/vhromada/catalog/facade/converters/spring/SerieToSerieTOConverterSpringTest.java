package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.converters.SerieToSerieTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SerieToSerieTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link cz.vhromada.catalog.facade.converters.SerieToSerieTOConverter#convert(Serie)}. */
	@Test
	public void testConvert() {
		final Serie serie = objectGenerator.generate(Serie.class);
		final SerieTO serieTO = conversionService.convert(serie, SerieTO.class);
		DeepAsserts.assertNotNull(serieTO, "totalLength");
		DeepAsserts.assertEquals(serie, serieTO, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
	}

	/** Test method for {@link cz.vhromada.catalog.facade.converters.SerieToSerieTOConverter#convert(Serie)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, SerieTO.class));
	}

}
