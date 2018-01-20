package cz.vhromada.catalog.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.domain.Movie;
import cz.vhromada.catalog.repository.MovieRepository;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.MovieUtils;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link MovieServiceImpl}.
 *
 * @author Vladimir Hromada
 */
class MovieServiceImplTest extends AbstractServiceTest<Movie> {

    /**
     * Instance of {@link MovieRepository}
     */
    @Mock
    private MovieRepository movieRepository;

    /**
     * Test method for {@link MovieServiceImpl#MovieServiceImpl(MovieRepository, Cache)} with null repository for movies.
     */
    @Test
    void constructor_NullMovieRepository() {
        assertThatThrownBy(() -> new MovieServiceImpl(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieServiceImpl#MovieServiceImpl(MovieRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new MovieServiceImpl(movieRepository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected JpaRepository<Movie, Integer> getRepository() {
        return movieRepository;
    }

    @Override
    protected CatalogService<Movie> getCatalogService() {
        return new MovieServiceImpl(movieRepository, getCache());
    }

    @Override
    protected String getCacheKey() {
        return "movies";
    }

    @Override
    protected Movie getItem1() {
        return MovieUtils.newMovieDomain(1);
    }

    @Override
    protected Movie getItem2() {
        return MovieUtils.newMovieDomain(2);
    }

    @Override
    protected Movie getAddItem() {
        return MovieUtils.newMovieDomain(null);
    }

    @Override
    protected Movie getCopyItem() {
        final Movie movie = MovieUtils.newMovieDomain(1);
        movie.setId(null);
        for (final Medium medium : movie.getMedia()) {
            medium.setId(null);
        }

        return movie;
    }

    @Override
    protected Class<Movie> getItemClass() {
        return Movie.class;
    }

    @Override
    protected void assertDataDeepEquals(final Movie expected, final Movie actual) {
        MovieUtils.assertMovieDeepEquals(expected, actual);
    }

}
