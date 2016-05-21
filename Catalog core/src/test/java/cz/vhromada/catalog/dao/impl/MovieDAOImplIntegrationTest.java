package cz.vhromada.catalog.dao.impl;

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
import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link MovieDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
@Rollback
public class MovieDAOImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link MovieDAO}
     */
    @Autowired
    private MovieDAO movieDAO;

    /**
     * Test method for {@link MovieDAO#getMovies()}.
     */
    @Test
    public void testGetMovies() {
        final List<Movie> movies = movieDAO.getMovies();

        MovieUtils.assertMoviesDeepEquals(MovieUtils.getMovies(), movies);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieDAO#getMovie(Integer)}.
     */
    @Test
    public void testGetMovie() {
        for (int i = 1; i <= MovieUtils.MOVIES_COUNT; i++) {
            final Movie movie = movieDAO.getMovie(i);

            assertNotNull(movie);
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), movie);
        }

        assertNull(movieDAO.getMovie(Integer.MAX_VALUE));

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieDAO#add(Movie)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(null)));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(entityManager, 1)));

        movieDAO.add(movie);

        assertNotNull(movie.getId());
        assertEquals(MovieUtils.MOVIES_COUNT + 1, movie.getId().intValue());
        assertEquals(MovieUtils.MOVIES_COUNT, movie.getPosition());

        final Movie addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        final Movie expectedAddedMovie = MovieUtils.newMovie(MovieUtils.MOVIES_COUNT + 1);
        expectedAddedMovie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(MediumUtils.MEDIA_COUNT + 1)));
        expectedAddedMovie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        MovieUtils.assertMovieDeepEquals(expectedAddedMovie, addedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT + 1, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieDAO#update(Movie)} with no media change.
     */
    @Test
    public void testUpdate_NoMediaChange() {
        final Movie movie = MovieUtils.updateMovie(1, entityManager);

        movieDAO.update(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieDAO#update(Movie)} with added medium.
     */
    @Test
    @DirtiesContext
    public void testUpdate_AddedMedium() {
        final Movie movie = MovieUtils.updateMovie(1, entityManager);
        final List<Medium> media = movie.getMedia();
        media.add(MediumUtils.newMedium(null));
        movie.setMedia(media);

        movieDAO.update(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.getMedia().add(MediumUtils.newMedium(MediumUtils.MEDIA_COUNT + 1));
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieDAO#update(Movie)} with removed medium.
     */
    @Test
    public void testUpdate_RemovedMedium() {
        final Movie movie = MovieUtils.updateMovie(1, entityManager);
        movie.setMedia(new ArrayList<>());

        movieDAO.update(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.setMedia(new ArrayList<>());
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieDAO#remove(Movie)}.
     */
    @Test
    public void testRemove() {
        movieDAO.remove(MovieUtils.getMovie(entityManager, 1));

        assertNull(MovieUtils.getMovie(entityManager, 1));

        assertEquals(MovieUtils.MOVIES_COUNT - 1, MovieUtils.getMoviesCount(entityManager));
    }

}
