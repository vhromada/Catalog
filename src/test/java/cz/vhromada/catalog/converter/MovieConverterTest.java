package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.MovieUtils;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Movie} and {@link Movie}.
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
        final cz.vhromada.catalog.domain.Movie movie = MovieUtils.newMovie(1);
        final Movie movieTO = converter.convert(movie, Movie.class);

        MovieUtils.assertMovieDeepEquals(movieTO, movie);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertMovie_NullArgument() {
        assertNull(converter.convert(null, Movie.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertMovieTO() {
        final Movie movieTO = MovieUtils.newMovieTO(1);
        final cz.vhromada.catalog.domain.Movie movie = converter.convert(movieTO, cz.vhromada.catalog.domain.Movie.class);

        assertNotNull(movie);
        MovieUtils.assertMovieDeepEquals(movieTO, movie);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertMovieTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Movie.class));
    }

}
