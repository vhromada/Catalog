package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SeasonToSeasonTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class SeasonToSeasonTOConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link SeasonToSeasonTOConverter} */
    private SeasonToSeasonTOConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        final SerieToSerieTOConverter serieToSerieTOConverter = new SerieToSerieTOConverter();
        serieToSerieTOConverter.setConverter(new GenreToGenreTOConverter());
        converter = new SeasonToSeasonTOConverter();
        converter.setConverter(serieToSerieTOConverter);
    }

    /** Test method for {@link SeasonToSeasonTOConverter#convert(Season)}. */
    @Test
    public void testConvert() {
        final Season season = generate(Season.class);
        final SeasonTO seasonTO = converter.convert(season);
        DeepAsserts.assertNotNull(seasonTO, "totalLength");
        DeepAsserts.assertEquals(season, seasonTO, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
    }

    /** Test method for {@link SeasonToSeasonTOConverter#convert(Season)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

    /** Test method for {@link SeasonToSeasonTOConverter#convert(Season)} with not set converter for serie. */
    @Test(expected = IllegalStateException.class)
    public void testConvertWithNotSetSerieConverter() {
        converter.setConverter(null);
        converter.convert(new Season());
    }

}
