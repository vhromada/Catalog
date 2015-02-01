package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MovieTOToMovieConverter}.
 *
 * @author Vladimir Hromada
 */
public class MovieTOToMovieConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link MovieTOToMovieConverter} */
    private MovieTOToMovieConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new MovieTOToMovieConverter(new IntegerToMediumConverter(), new GenreTOToGenreConverter());
    }

    /**
     * Test method for {@link MovieTOToMovieConverter#MovieTOToMovieConverter(IntegerToMediumConverter, GenreTOToGenreConverter)} with null converter
     * from integer to entity medium.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullIntegerToMediumConverter() {
        new MovieTOToMovieConverter(null, new GenreTOToGenreConverter());
    }

    /**
     * Test method for {@link MovieTOToMovieConverter#MovieTOToMovieConverter(IntegerToMediumConverter, GenreTOToGenreConverter)} with null converter
     * from TO for genre to entity genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenreTOToGenreConverter() {
        new MovieTOToMovieConverter(new IntegerToMediumConverter(), null);
    }

    /** Test method for {@link MovieTOToMovieConverter#convert(MovieTO)}. */
    @Test
    public void testConvert() {
        final MovieTO movieTO = generate(MovieTO.class);
        final Movie movie = converter.convert(movieTO);
        DeepAsserts.assertNotNull(movie);
        DeepAsserts.assertEquals(movieTO, movie, "subtitlesAsString", "media", "totalLength", "genresAsString");
        DeepAsserts.assertEquals(movieTO.getMedia().size(), movie.getMedia().size());
        for (int i = 0; i < movieTO.getMedia().size(); i++) {
            DeepAsserts.assertEquals(movieTO.getMedia().get(i), movie.getMedia().get(i).getLength());
        }
    }

    /** Test method for {@link MovieTOToMovieConverter#convert(MovieTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

}
