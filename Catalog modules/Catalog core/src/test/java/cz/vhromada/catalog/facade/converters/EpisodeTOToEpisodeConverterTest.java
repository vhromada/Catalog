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
		final SerieTOToSerieConverter serieTOToSerieConverter = new SerieTOToSerieConverter();
		serieTOToSerieConverter.setConverter(new GenreTOToGenreConverter());
		final SeasonTOToSeasonConverter seasonTOToSeasonConverter = new SeasonTOToSeasonConverter();
		seasonTOToSeasonConverter.setConverter(serieTOToSerieConverter);
		converter = new EpisodeTOToEpisodeConverter();
		converter.setConverter(seasonTOToSeasonConverter);
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

	/** Test method for {@link EpisodeTOToEpisodeConverter#convert(EpisodeTO)} with not set converter for season. */
	@Test(expected = IllegalStateException.class)
	public void testConvertWithNotSetSeasonConverter() {
		converter.setConverter(null);
		converter.convert(new EpisodeTO());
	}

}
