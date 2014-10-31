package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.converters.SeasonToSeasonTOConverter;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link SeasonToSeasonTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SeasonToSeasonTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link SeasonToSeasonTOConverter#convert(Season)}. */
	@Test
	public void testConvert() {
		final Season season = objectGenerator.generate(Season.class);
		final SeasonTO seasonTO = conversionService.convert(season, SeasonTO.class);
		DeepAsserts.assertNotNull(seasonTO, "totalLength");
		DeepAsserts.assertEquals(season, seasonTO, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
	}

	/** Test method for {@link SeasonToSeasonTOConverter#convert(Season)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, SeasonTO.class));
	}

}
