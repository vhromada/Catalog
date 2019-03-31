package cz.vhromada.catalog.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.domain.Movie;
import cz.vhromada.catalog.repository.MovieRepository;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.common.repository.MovableRepository;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.service.MovableServiceTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;

/**
 * A class represents test for class {@link MovieService}.
 *
 * @author Vladimir Hromada
 */
class MovieServiceTest extends MovableServiceTest<Movie> {

    /**
     * Instance of {@link MovieRepository}
     */
    @Mock
    private MovieRepository repository;

    /**
     * Test method for {@link MovieService#MovieService(MovieRepository, Cache)} with null repository for movies.
     */
    @Test
    void constructor_NullMovieRepository() {
        assertThatThrownBy(() -> new MovieService(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieService#MovieService(MovieRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new MovieService(repository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableRepository<Movie> getRepository() {
        return repository;
    }

    @Override
    protected MovableService<Movie> getService() {
        return new MovieService(repository, getCache());
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
