package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SerieToSerieTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class SerieToSerieTOConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link SerieToSerieTOConverter} */
    private SerieToSerieTOConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new SerieToSerieTOConverter();
        converter.setConverter(new GenreToGenreTOConverter());
    }

    /** Test method for {@link SerieToSerieTOConverter#convert(Serie)}. */
    @Test
    public void testConvert() {
        final Serie serie = generate(Serie.class);
        final SerieTO serieTO = converter.convert(serie);
        DeepAsserts.assertNotNull(serieTO, "totalLength");
        DeepAsserts.assertEquals(serie, serieTO, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
    }

    /** Test method for {@link SerieToSerieTOConverter#convert(Serie)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

    /** Test method for {@link SerieToSerieTOConverter#convert(Serie)} with not set converter for genre. */
    @Test(expected = IllegalStateException.class)
    public void testConvertWithNotSetGenreConverter() {
        converter.setConverter(null);
        converter.convert(new Serie());
    }

}
