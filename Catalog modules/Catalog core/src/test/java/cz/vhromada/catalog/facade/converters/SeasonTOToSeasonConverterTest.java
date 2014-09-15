package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.ToGenerator;
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
public class SeasonTOToSeasonConverterTest {

	/** Instance of {@link SeasonTOToSeasonConverter} */
	private SeasonTOToSeasonConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		final SerieTOToSerieConverter serieTOToSerieConverter = new SerieTOToSerieConverter();
		serieTOToSerieConverter.setConverter(new GenreTOToGenreConverter());
		converter = new SeasonTOToSeasonConverter();
		converter.setConverter(serieTOToSerieConverter);
	}

	/** Test method for {@link SeasonTOToSeasonConverter#getConverter()} and {@link SeasonTOToSeasonConverter#setConverter(SerieTOToSerieConverter)}. */
	@Test
	public void testConverter() {
		final SerieTOToSerieConverter serieConverter = new SerieTOToSerieConverter();
		converter.setConverter(serieConverter);
		DeepAsserts.assertEquals(serieConverter, converter.getConverter());
	}

	/** Test method for {@link SeasonTOToSeasonConverter#convert(SeasonTO)}. */
	@Test
	public void testConvert() {
		final SeasonTO seasonTO = ToGenerator.createSeason(ID, ToGenerator.createSerie(INNER_ID));
		final Season season = converter.convert(seasonTO);
		DeepAsserts.assertNotNull(season);
		DeepAsserts.assertEquals(seasonTO, season, "year", "subtitlesAsString", "episodesCount", "totalLength", "seasonsCount", "genresAsString");
	}

	/** Test method for {@link SeasonTOToSeasonConverter#convert(SeasonTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

	/** Test method for {@link SeasonTOToSeasonConverter#convert(SeasonTO)} with not set converter for serie. */
	@Test(expected = IllegalStateException.class)
	public void testConvertWithNotSetSerieConverter() {
		converter.setConverter(null);
		converter.convert(new SeasonTO());
	}

}
