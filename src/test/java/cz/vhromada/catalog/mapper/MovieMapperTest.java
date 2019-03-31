package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.utils.MovieUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Movie} and {@link Movie}.
 *
 * @author Vladimir Hromada
 */
class MovieMapperTest {

    private MovieMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(MovieMapper.class);
    }

    /**
     * Test method for {@link MovieMapper#map(Movie)}.
     */
    @Test
    void map() {
        final Movie movie = MovieUtils.newMovie(1);
        final cz.vhromada.catalog.domain.Movie movieDomain = mapper.map(movie);

        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link MovieMapper#map(Movie)} with null movie.
     */
    @Test
    void map_NullMovie() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link MovieMapper#mapBack(cz.vhromada.catalog.domain.Movie)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Movie movieDomain = MovieUtils.newMovieDomain(1);
        final Movie movie = mapper.mapBack(movieDomain);

        MovieUtils.assertMovieDeepEquals(movie, movieDomain);
    }

    /**
     * Test method for {@link MovieMapper#mapBack(cz.vhromada.catalog.domain.Movie)} with null movie.
     */
    @Test
    void mapBack_NullMovie() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
