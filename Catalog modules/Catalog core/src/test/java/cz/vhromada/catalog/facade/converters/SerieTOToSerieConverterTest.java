package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SerieTOToSerieConverter}.
 *
 * @author Vladimir Hromada
 */
public class SerieTOToSerieConverterTest {

	/** Instance of {@link SerieTOToSerieConverter} */
	private SerieTOToSerieConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new SerieTOToSerieConverter();
		converter.setConverter(new GenreTOToGenreConverter());
	}

	/** Test method for {@link SerieTOToSerieConverter#getConverter()} and {@link SerieTOToSerieConverter#setConverter(GenreTOToGenreConverter)}. */
	@Test
	public void testConverter() {
		final GenreTOToGenreConverter genreConverter = new GenreTOToGenreConverter();
		converter.setConverter(genreConverter);
		DeepAsserts.assertEquals(genreConverter, converter.getConverter());
	}

	/** Test method for {@link SerieTOToSerieConverter#convert(SerieTO)}. */
	@Test
	public void testConvert() {
		final SerieTO serieTO = ToGenerator.createSerie(ID);
		final Serie serie = converter.convert(serieTO);
		DeepAsserts.assertNotNull(serie);
		DeepAsserts.assertEquals(serieTO, serie, "seasonsCount", "episodesCount", "totalLength", "genresAsString");
	}

	/** Test method for {@link SerieTOToSerieConverter#convert(SerieTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

	/** Test method for {@link SerieTOToSerieConverter#convert(SerieTO)} with not set converter for genre. */
	@Test(expected = IllegalStateException.class)
	public void testConvertWithNotSetGenreConverter() {
		converter.setConverter(null);
		converter.convert(new SerieTO());
	}

}
