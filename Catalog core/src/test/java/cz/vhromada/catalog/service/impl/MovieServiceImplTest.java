package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.commons.MovieUtils;
import cz.vhromada.catalog.entities.Medium;
import cz.vhromada.catalog.entities.Movie;
import cz.vhromada.catalog.repository.MovieRepository;
import cz.vhromada.catalog.service.CatalogService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link MovieServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieServiceImplTest extends AbstractServiceTest<Movie> {

    /**
     * Instance of {@link MovieRepository}
     */
    @Mock
    private MovieRepository movieRepository;

    /**
     * Test method for {@link MovieServiceImpl#MovieServiceImpl(MovieRepository, Cache)} with null repository for movies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMovieRepository() {
        new MovieServiceImpl(null, getCache());
    }

    /**
     * Test method for {@link MovieServiceImpl#MovieServiceImpl(MovieRepository, Cache)} with null cache.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullCache() {
        new MovieServiceImpl(movieRepository, null);
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
        return MovieUtils.newMovie(1);
    }

    @Override
    protected Movie getItem2() {
        return MovieUtils.newMovie(2);
    }

    @Override
    protected Movie getAddItem() {
        return MovieUtils.newMovie(null);
    }

    @Override
    protected Movie getCopyItem() {
        final Movie movie = MovieUtils.newMovie(1);
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
