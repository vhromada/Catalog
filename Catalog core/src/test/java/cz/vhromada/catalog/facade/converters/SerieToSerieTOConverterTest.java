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
        converter = new SerieToSerieTOConverter(new GenreToGenreTOConverter());
    }

    /**
     * Test method for {@link SerieToSerieTOConverter#SerieToSerieTOConverter(GenreToGenreTOConverter)} with null converter from entity genre to TO for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenreToGenreTOConverterr() {
        new SerieToSerieTOConverter(null);
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

}
