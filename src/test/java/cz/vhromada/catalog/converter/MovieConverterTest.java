package cz.vhromada.catalog.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Movie} and {@link Movie}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class MovieConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void convertMovieDomain() {
        final cz.vhromada.catalog.domain.Movie movieDomain = MovieUtils.newMovieDomain(1);
        final Movie movie = converter.convert(movieDomain, Movie.class);

        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null movie.
     */
    @Test
    public void convertMovieDomain_NullMovie() {
        assertThat(converter.convert(null, Movie.class), is(nullValue()));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void convertMovie() {
        final Movie movie = MovieUtils.newMovie(1);
        final cz.vhromada.catalog.domain.Movie movieDomain = converter.convert(movie, cz.vhromada.catalog.domain.Movie.class);

        assertThat(movieDomain, is(notNullValue()));
        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null movie.
     */
    @Test
    public void convertMovie_NullMovie() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Movie.class), is(nullValue()));
    }

}
