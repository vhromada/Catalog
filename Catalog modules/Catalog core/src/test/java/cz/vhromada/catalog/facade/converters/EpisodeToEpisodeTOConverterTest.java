package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link EpisodeToEpisodeTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class EpisodeToEpisodeTOConverterTest extends ObjectGeneratorTest {

	/** Instance of {@link EpisodeToEpisodeTOConverter} */
	private EpisodeToEpisodeTOConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		final SerieToSerieTOConverter serieToSerieTOConverter = new SerieToSerieTOConverter();
		serieToSerieTOConverter.setConverter(new GenreToGenreTOConverter());
		final SeasonToSeasonTOConverter seasonToSeasonTOConverter = new SeasonToSeasonTOConverter();
		seasonToSeasonTOConverter.setConverter(serieToSerieTOConverter);
		converter = new EpisodeToEpisodeTOConverter();
		converter.setConverter(seasonToSeasonTOConverter);
	}

	/** Test method for {@link EpisodeToEpisodeTOConverter#convert(Episode)}. */
	@Test
	public void testConvert() {
		final Episode episode = generate(Episode.class);
		final EpisodeTO episodeTO = converter.convert(episode);
		DeepAsserts.assertNotNull(episodeTO, "totalLength");
		DeepAsserts.assertEquals(episode, episodeTO, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
	}

	/** Test method for {@link EpisodeToEpisodeTOConverter#convert(Episode)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

	/** Test method for {@link EpisodeToEpisodeTOConverter#convert(Episode)} with not set converter for season. */
	@Test(expected = IllegalStateException.class)
	public void testConvertWithNotSetSeasonConverter() {
		converter.setConverter(null);
		converter.convert(new Episode());
	}

}
