package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Season;
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
 * A class represents test for class {@link cz.vhromada.catalog.facade.converters.SeasonTOToSeasonConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SeasonTOToSeasonConverterSpringTest {

    /** Instance of {@link ConversionService} */
    @Autowired
    private ConversionService conversionService;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Test method for {@link cz.vhromada.catalog.facade.converters.SeasonTOToSeasonConverter#convert(SeasonTO)}. */
    @Test
    public void testConvert() {
        final SeasonTO seasonTO = objectGenerator.generate(SeasonTO.class);
        final Season season = conversionService.convert(seasonTO, Season.class);
        DeepAsserts.assertNotNull(season);
        DeepAsserts.assertEquals(seasonTO, season, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
    }

    /** Test method for {@link cz.vhromada.catalog.facade.converters.SeasonTOToSeasonConverter#convert(SeasonTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(conversionService.convert(null, Season.class));
    }

}
