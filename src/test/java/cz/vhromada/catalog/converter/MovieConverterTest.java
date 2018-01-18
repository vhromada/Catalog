package cz.vhromada.catalog.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Movie} and {@link Movie}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class MovieConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertMovieDomain() {
        final cz.vhromada.catalog.domain.Movie movieDomain = MovieUtils.newMovieDomain(1);
        final Movie movie = converter.convert(movieDomain, Movie.class);

        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null movie.
     */
    @Test
    void convertMovieDomain_NullMovie() {
        assertNull(converter.convert(null, Movie.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertMovie() {
        final Movie movie = MovieUtils.newMovie(1);
        final cz.vhromada.catalog.domain.Movie movieDomain = converter.convert(movie, cz.vhromada.catalog.domain.Movie.class);

        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null movie.
     */
    @Test
    void convertMovie_NullMovie() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Movie.class));
    }

}
