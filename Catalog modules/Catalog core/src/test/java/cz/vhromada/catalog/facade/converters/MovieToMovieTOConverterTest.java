package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MovieToMovieTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class MovieToMovieTOConverterTest {

	/** Instance of {@link MovieToMovieTOConverter} */
	private MovieToMovieTOConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new MovieToMovieTOConverter();
		converter.setMediumConverter(new MediumToIntegerConverter());
		converter.setGenreConverter(new GenreToGenreTOConverter());
	}

	/**
	 * Test method for {@link MovieToMovieTOConverter#getMediumConverter()} and
	 * {@link MovieToMovieTOConverter#setMediumConverter(MediumToIntegerConverter)}.
	 */
	@Test
	public void testMediumConverter() {
		final MediumToIntegerConverter mediumConverter = new MediumToIntegerConverter();
		converter.setMediumConverter(mediumConverter);
		DeepAsserts.assertEquals(mediumConverter, converter.getMediumConverter());
	}

	/** Test method for {@link MovieToMovieTOConverter#getGenreConverter()} and {@link MovieToMovieTOConverter#setGenreConverter(GenreToGenreTOConverter)}. */
	@Test
	public void testGenreConverter() {
		final GenreToGenreTOConverter genreConverter = new GenreToGenreTOConverter();
		converter.setGenreConverter(genreConverter);
		DeepAsserts.assertEquals(genreConverter, converter.getGenreConverter());
	}

	/** Test method for {@link MovieToMovieTOConverter#convert(Movie)}. */
	@Test
	public void testConvert() {
		final Movie movie = EntityGenerator.createMovie(ID);
		final MovieTO movieTO = converter.convert(movie);
		DeepAsserts.assertNotNull(movieTO);
		DeepAsserts.assertEquals(movie, movieTO, "subtitlesAsString", "media", "totalLength", "genresAsString");
		DeepAsserts.assertEquals(movie.getMedia().size(), movieTO.getMedia().size());
		for (int i = 0; i < movie.getMedia().size(); i++) {
			DeepAsserts.assertEquals(movie.getMedia().get(i).getLength(), movieTO.getMedia().get(i));
		}
	}

	/** Test method for {@link MovieToMovieTOConverter#convert(Movie)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

	/** Test method for {@link MovieToMovieTOConverter#convert(Movie)} with not set converter for medium. */
	@Test(expected = IllegalStateException.class)
	public void testConvertWithNotSetMediumConverter() {
		converter.setMediumConverter(null);
		converter.convert(new Movie());
	}

	/** Test method for {@link MovieToMovieTOConverter#convert(Movie)} with not set converter for genre. */
	@Test(expected = IllegalStateException.class)
	public void testConvertWithNotSetGenreConverter() {
		converter.setGenreConverter(null);
		converter.convert(new Movie());
	}

}
