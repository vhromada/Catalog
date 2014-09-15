package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
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
public class SeasonToSeasonTOConverterTest {

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

	/** Test method for {@link SeasonToSeasonTOConverter#getConverter()} and {@link SeasonToSeasonTOConverter#setConverter(SerieToSerieTOConverter)}. */
	@Test
	public void testConverter() {
		final SerieToSerieTOConverter serieConverter = new SerieToSerieTOConverter();
		converter.setConverter(serieConverter);
		DeepAsserts.assertEquals(serieConverter, converter.getConverter());
	}

	/** Test method for {@link SeasonToSeasonTOConverter#convert(Season)}. */
	@Test
	public void testConvert() {
		final Season season = EntityGenerator.createSeason(ID, EntityGenerator.createSerie(INNER_ID));
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
