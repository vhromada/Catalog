package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SeasonTOToSeasonConverter}.
 *
 * @author Vladimir Hromada
 */
public class SeasonTOToSeasonConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link SeasonTOToSeasonConverter} */
    private SeasonTOToSeasonConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new SeasonTOToSeasonConverter(new SerieTOToSerieConverter(new GenreTOToGenreConverter()));
    }

    /**
     * Test method for {@link SeasonTOToSeasonConverter#SeasonTOToSeasonConverter(SerieTOToSerieConverter)} with null converter from TO for serie
     * to entity serie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSerieTOToSerieConverter() {
        new SeasonTOToSeasonConverter(null);
    }

    /** Test method for {@link SeasonTOToSeasonConverter#convert(SeasonTO)}. */
    @Test
    public void testConvert() {
        final SeasonTO seasonTO = generate(SeasonTO.class);
        final Season season = converter.convert(seasonTO);
        DeepAsserts.assertNotNull(season);
        DeepAsserts.assertEquals(seasonTO, season, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
    }

    /** Test method for {@link SeasonTOToSeasonConverter#convert(SeasonTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

}
