package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.converters.SerieToSerieTOConverter;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link SerieToSerieTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SerieToSerieTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link SerieToSerieTOConverter#convert(Serie)}. */
	@Test
	public void testConvert() {
		final Serie serie = EntityGenerator.createSerie(ID);
		final SerieTO serieTO = conversionService.convert(serie, SerieTO.class);
		DeepAsserts.assertNotNull(serieTO, "totalLength");
		DeepAsserts.assertEquals(serie, serieTO, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
	}

	/** Test method for {@link SerieToSerieTOConverter#convert(Serie)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, SerieTO.class));
	}

}
