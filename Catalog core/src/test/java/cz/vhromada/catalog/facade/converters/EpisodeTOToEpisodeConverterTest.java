package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link EpisodeTOToEpisodeConverter}.
 *
 * @author Vladimir Hromada
 */
public class EpisodeTOToEpisodeConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link EpisodeTOToEpisodeConverter} */
    private EpisodeTOToEpisodeConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        final SeasonTOToSeasonConverter seasonTOToSeasonConverter = new SeasonTOToSeasonConverter(new SerieTOToSerieConverter(new GenreTOToGenreConverter()));
        converter = new EpisodeTOToEpisodeConverter(seasonTOToSeasonConverter);
    }

    /**
     * Test method for {@link EpisodeTOToEpisodeConverter#EpisodeTOToEpisodeConverter(SeasonTOToSeasonConverter)} with null converter from TO for season
     * to entity season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSeasonTOToSeasonConverter() {
        new SeasonTOToSeasonConverter(null);
    }

    /** Test method for {@link EpisodeTOToEpisodeConverter#convert(EpisodeTO)}. */
    @Test
    public void testConvert() {
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        final Episode episode = converter.convert(episodeTO);
        DeepAsserts.assertNotNull(episode);
        DeepAsserts.assertEquals(episodeTO, episode, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
    }

    /** Test method for {@link EpisodeTOToEpisodeConverter#convert(EpisodeTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

}
