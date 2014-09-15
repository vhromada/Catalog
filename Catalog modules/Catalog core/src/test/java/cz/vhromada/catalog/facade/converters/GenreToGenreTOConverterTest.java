package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GenreToGenreTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class GenreToGenreTOConverterTest {

	/** Instance of {@link GenreToGenreTOConverter} */
	private GenreToGenreTOConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new GenreToGenreTOConverter();
	}

	/** Test method for {@link GenreToGenreTOConverter#convert(Genre)}. */
	@Test
	public void testConvert() {
		final Genre genre = EntityGenerator.createGenre(ID);
		final GenreTO genreTO = converter.convert(genre);
		DeepAsserts.assertNotNull(genreTO);
		DeepAsserts.assertEquals(genre, genreTO);
	}

	/** Test method for {@link GenreToGenreTOConverter#convert(Genre)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
