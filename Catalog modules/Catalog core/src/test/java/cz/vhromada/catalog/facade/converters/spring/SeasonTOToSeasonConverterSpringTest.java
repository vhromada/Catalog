package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.converters.SeasonTOToSeasonConverter;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link SeasonTOToSeasonConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SeasonTOToSeasonConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link SeasonTOToSeasonConverter#convert(SeasonTO)}. */
	@Test
	public void testConvert() {
		final SeasonTO seasonTO = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		final Season season = conversionService.convert(seasonTO, Season.class);
		DeepAsserts.assertNotNull(season);
		DeepAsserts.assertEquals(seasonTO, season, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
	}

	/** Test method for {@link SeasonTOToSeasonConverter#convert(SeasonTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Season.class));
	}

}
