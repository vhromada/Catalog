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
 * A class represents test for class {@link cz.vhromada.catalog.facade.converters.SerieTOToSerieConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class SerieTOToSerieConverterSpringTest {

    /** Instance of {@link ConversionService} */
    @Autowired
    private ConversionService conversionService;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Test method for {@link cz.vhromada.catalog.facade.converters.SerieTOToSerieConverter#convert(SerieTO)}. */
    @Test
    public void testConvert() {
        final SerieTO serieTO = objectGenerator.generate(SerieTO.class);
        final Serie serie = conversionService.convert(serieTO, Serie.class);
        DeepAsserts.assertNotNull(serie);
        DeepAsserts.assertEquals(serieTO, serie, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
    }

    /** Test method for {@link cz.vhromada.catalog.facade.converters.SerieTOToSerieConverter#convert(SerieTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(conversionService.convert(null, Serie.class));
    }

}
