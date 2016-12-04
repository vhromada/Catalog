package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.utils.MovieUtils;
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
@ContextConfiguration("classpath:dozerMappingContext.xml")
public class MovieConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void testConvertMovieDomain() {
        final cz.vhromada.catalog.domain.Movie movieDomain = MovieUtils.newMovieDomain(1);
        final Movie movie = converter.convert(movieDomain, Movie.class);

        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void testConvertMovieDomain_NullArgument() {
        assertNull(converter.convert(null, Movie.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertMovie() {
        final Movie movie = MovieUtils.newMovie(1);
        final cz.vhromada.catalog.domain.Movie movieDomain = converter.convert(movie, cz.vhromada.catalog.domain.Movie.class);

        assertNotNull(movie);
        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertMovie_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Movie.class));
    }

}
