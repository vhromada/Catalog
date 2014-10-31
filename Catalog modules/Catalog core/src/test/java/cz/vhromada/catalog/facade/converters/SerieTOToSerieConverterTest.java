package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
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
public class SerieTOToSerieConverterTest extends ObjectGeneratorTest {

	/** Instance of {@link SerieTOToSerieConverter} */
	private SerieTOToSerieConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new SerieTOToSerieConverter();
		converter.setConverter(new GenreTOToGenreConverter());
	}

	/** Test method for {@link SerieTOToSerieConverter#convert(SerieTO)}. */
	@Test
	public void testConvert() {
		final SerieTO serieTO = generate(SerieTO.class);
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
