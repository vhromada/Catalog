package cz.vhromada.catalog.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Movie;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MediumUtils;
import cz.vhromada.catalog.utils.MovieUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link MovieRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
public class MovieRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link MovieRepository}
     */
    @Autowired
    private MovieRepository movieRepository;

    /**
     * Test method for get movies.
     */
    @Test
    public void getMovies() {
        final List<Movie> movies = movieRepository.findAll(Sort.by("position", "id"));

        MovieUtils.assertMoviesDeepEquals(MovieUtils.getMovies(), movies);

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
    }

    /**
     * Test method for get movie.
     */
    @Test
    public void getMovie() {
        for (int i = 1; i <= MovieUtils.MOVIES_COUNT; i++) {
            final Movie movie = movieRepository.findById(i).orElse(null);

            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), movie);
        }

        assertThat(movieRepository.findById(Integer.MAX_VALUE).isPresent(), is(false));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
    }

    /**
     * Test method for add movie.
     */
    @Test
    public void add() {
        final Movie movie = MovieUtils.newMovieDomain(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumDomain(null)));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(entityManager, 1)));

        movieRepository.saveAndFlush(movie);

        assertThat(movie.getId(), is(notNullValue()));
        assertThat(movie.getId(), is(MovieUtils.MOVIES_COUNT + 1));

        final Movie addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        final Movie expectedAddedMovie = MovieUtils.newMovieDomain(null);
        expectedAddedMovie.setId(MovieUtils.MOVIES_COUNT + 1);
        expectedAddedMovie.setMedia(CollectionUtils.newList(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1)));
        expectedAddedMovie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));
        MovieUtils.assertMovieDeepEquals(expectedAddedMovie, addedMovie);

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT + 1));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT + 1));
    }

    /**
     * Test method for update movie with no media change.
     */
    @Test
    public void update_NoMediaChange() {
        final Movie movie = MovieUtils.updateMovie(entityManager, 1);

        movieRepository.saveAndFlush(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
    }

    /**
     * Test method for update movie with added medium.
     */
    @Test
    @DirtiesContext
    public void update_AddedMedium() {
        final Movie movie = MovieUtils.updateMovie(entityManager, 1);
        movie.getMedia().add(MediumUtils.newMediumDomain(null));

        movieRepository.saveAndFlush(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.getMedia().add(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1));
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT + 1));
    }

    /**
     * Test method for update movie with removed medium.
     */
    @Test
    public void update_RemovedMedium() {
        final int mediaCount = MovieUtils.getMovie(1).getMedia().size();
        final Movie movie = MovieUtils.updateMovie(entityManager, 1);
        movie.getMedia().clear();

        movieRepository.saveAndFlush(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.setMedia(new ArrayList<>());
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT - mediaCount));
    }

    /**
     * Test method for remove movie.
     */
    @Test
    public void remove() {
        final int mediaCount = MovieUtils.getMovie(1).getMedia().size();

        movieRepository.delete(MovieUtils.getMovie(entityManager, 1));

        assertThat(MovieUtils.getMovie(entityManager, 1), is(nullValue()));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT - 1));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT - mediaCount));
    }

    /**
     * Test method for remove all movies.
     */
    @Test
    public void removeAll() {
        movieRepository.deleteAll();

        assertThat(MovieUtils.getMoviesCount(entityManager), is(0));
        assertThat(MediumUtils.getMediaCount(entityManager), is(0));
    }

}
