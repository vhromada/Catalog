package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GenreTOToGenreConverter}.
 *
 * @author Vladimir Hromada
 */
public class GenreTOToGenreConverterTest {

	/** Instance of {@link GenreTOToGenreConverter} */
	private GenreTOToGenreConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new GenreTOToGenreConverter();
	}

	/** Test method for {@link GenreTOToGenreConverter#convert(GenreTO)}. */
	@Test
	public void testConvert() {
		final GenreTO genreTO = ToGenerator.createGenre(ID);
		final Genre genre = converter.convert(genreTO);
		DeepAsserts.assertNotNull(genre);
		DeepAsserts.assertEquals(genreTO, genre);
	}

	/** Test method for {@link GenreTOToGenreConverter#convert(GenreTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}