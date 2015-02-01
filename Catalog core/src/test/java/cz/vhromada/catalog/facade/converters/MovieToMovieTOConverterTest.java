package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
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
public class MovieToMovieTOConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link MovieToMovieTOConverter} */
    private MovieToMovieTOConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new MovieToMovieTOConverter(new MediumToIntegerConverter(), new GenreToGenreTOConverter());
    }

    /**
     * Test method for {@link MovieToMovieTOConverter#MovieToMovieTOConverter(MediumToIntegerConverter, GenreToGenreTOConverter)} with null converter
     * from entity medium to integer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMediumToIntegerConverter() {
        new MovieToMovieTOConverter(null, new GenreToGenreTOConverter());
    }

    /**
     * Test method for {@link MovieToMovieTOConverter#MovieToMovieTOConverter(MediumToIntegerConverter, GenreToGenreTOConverter)} with null converter
     * from entity genre to TO for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenreToGenreTOConverter() {
        new MovieToMovieTOConverter(new MediumToIntegerConverter(), null);
    }

    /** Test method for {@link MovieToMovieTOConverter#convert(Movie)}. */
    @Test
    public void testConvert() {
        final Movie movie = generate(Movie.class);
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

}
