package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.GenreUtils;
import cz.vhromada.catalog.commons.MediumUtils;
import cz.vhromada.catalog.commons.MovieUtils;
import cz.vhromada.catalog.entities.Movie;

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
@ContextConfiguration("classpath:testRepositoryContext.xml")
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
    public void testGetMovies() {
        final List<Movie> movies = movieRepository.findAll(new Sort("position", "id"));

        MovieUtils.assertMoviesDeepEquals(MovieUtils.getMovies(), movies);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT, MediumUtils.getMediaCount(entityManager));
    }

    /**
     * Test method for get movie.
     */
    @Test
    public void testGetMovie() {
        for (int i = 1; i <= MovieUtils.MOVIES_COUNT; i++) {
            final Movie movie = movieRepository.findOne(i);

            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), movie);
        }

        assertNull(movieRepository.findOne(Integer.MAX_VALUE));

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT, MediumUtils.getMediaCount(entityManager));
    }

    /**
     * Test method for add movie.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(null)));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(entityManager, 1)));

        movieRepository.saveAndFlush(movie);

        assertNotNull(movie.getId());
        assertEquals(MovieUtils.MOVIES_COUNT + 1, movie.getId().intValue());

        final Movie addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        final Movie expectedAddedMovie = MovieUtils.newMovie(null);
        expectedAddedMovie.setId(MovieUtils.MOVIES_COUNT + 1);
        expectedAddedMovie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(MediumUtils.MEDIA_COUNT + 1)));
        expectedAddedMovie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        MovieUtils.assertMovieDeepEquals(expectedAddedMovie, addedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT + 1, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT + 1, MediumUtils.getMediaCount(entityManager));
    }

    /**
     * Test method for update movie with no media change.
     */
    @Test
    public void testUpdate_NoMediaChange() {
        final Movie movie = MovieUtils.updateMovie(entityManager, 1);

        movieRepository.saveAndFlush(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT, MediumUtils.getMediaCount(entityManager));
    }

    /**
     * Test method for update movie with added medium.
     */
    @Test
    @DirtiesContext
    public void testUpdate_AddedMedium() {
        final Movie movie = MovieUtils.updateMovie(entityManager, 1);
        movie.getMedia().add(MediumUtils.newMedium(null));

        movieRepository.saveAndFlush(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.getMedia().add(MediumUtils.newMedium(MediumUtils.MEDIA_COUNT + 1));
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT + 1, MediumUtils.getMediaCount(entityManager));
    }

    /**
     * Test method for update movie with removed medium.
     */
    @Test
    public void testUpdate_RemovedMedium() {
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

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT - mediaCount, MediumUtils.getMediaCount(entityManager));
    }

    /**
     * Test method for remove movie.
     */
    @Test
    public void testRemove() {
        final int mediaCount = MovieUtils.getMovie(1).getMedia().size();

        movieRepository.delete(MovieUtils.getMovie(entityManager, 1));

        assertNull(MovieUtils.getMovie(entityManager, 1));

        assertEquals(MovieUtils.MOVIES_COUNT - 1, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT - mediaCount, MediumUtils.getMediaCount(entityManager));
    }

    /**
     * Test method for remove all movies.
     */
    @Test
    public void testRemoveAll() {
        movieRepository.deleteAll();

        assertEquals(0, MovieUtils.getMoviesCount(entityManager));
        assertEquals(0, MediumUtils.getMediaCount(entityManager));
    }

}
