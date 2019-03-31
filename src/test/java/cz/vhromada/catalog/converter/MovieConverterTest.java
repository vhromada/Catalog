package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.utils.MovieUtils;

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
     * Instance of {@link MovieConverter}
     */
    @Autowired
    private MovieConverter converter;

    /**
     * Test method for {@link MovieConverter#convert(Movie)}.
     */
    @Test
    void convert() {
        final Movie movie = MovieUtils.newMovie(1);
        final cz.vhromada.catalog.domain.Movie movieDomain = converter.convert(movie);

        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link MovieConverter#convertBack(cz.vhromada.catalog.domain.Movie)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Movie movieDomain = MovieUtils.newMovieDomain(1);
        final Movie movie = converter.convertBack(movieDomain);

        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

}
