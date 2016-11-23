package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.MovieUtils;
import cz.vhromada.catalog.domain.Movie;
import cz.vhromada.catalog.entity.MovieTO;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link Movie} and {@link MovieTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class MovieConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO.
     */
    @Test
    public void testConvertMovie() {
        final Movie movie = MovieUtils.newMovie(1);
        final MovieTO movieTO = converter.convert(movie, MovieTO.class);

        MovieUtils.assertMovieDeepEquals(movieTO, movie);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertMovie_NullArgument() {
        assertNull(converter.convert(null, MovieTO.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertMovieTO() {
        final MovieTO movieTO = MovieUtils.newMovieTO(1);
        final Movie movie = converter.convert(movieTO, Movie.class);

        assertNotNull(movie);
        MovieUtils.assertMovieDeepEquals(movieTO, movie);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertMovieTO_NullArgument() {
        assertNull(converter.convert(null, Movie.class));
    }

}
