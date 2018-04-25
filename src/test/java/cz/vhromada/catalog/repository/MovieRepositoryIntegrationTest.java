package cz.vhromada.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Movie;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MediumUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.common.utils.CollectionUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link MovieRepository}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
class MovieRepositoryIntegrationTest {

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
    void getMovies() {
        final List<Movie> movies = movieRepository.findAll(Sort.by("position", "id"));

        MovieUtils.assertMoviesDeepEquals(MovieUtils.getMovies(), movies);

        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT);
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT);
        });
    }

    /**
     * Test method for get movie.
     */
    @Test
    void getMovie() {
        for (int i = 1; i <= MovieUtils.MOVIES_COUNT; i++) {
            final Movie movie = movieRepository.findById(i).orElse(null);

            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), movie);
        }

        assertThat(movieRepository.findById(Integer.MAX_VALUE).isPresent()).isFalse();

        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT);
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT);
        });
    }

    /**
     * Test method for add movie.
     */
    @Test
    void add() {
        final Movie movie = MovieUtils.newMovieDomain(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumDomain(null)));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(entityManager, 1)));

        movieRepository.save(movie);

        assertThat(movie.getId()).isEqualTo(MovieUtils.MOVIES_COUNT + 1);

        final Movie addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        final Movie expectedAddedMovie = MovieUtils.newMovieDomain(null);
        expectedAddedMovie.setId(MovieUtils.MOVIES_COUNT + 1);
        expectedAddedMovie.setMedia(CollectionUtils.newList(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1)));
        expectedAddedMovie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));
        MovieUtils.assertMovieDeepEquals(expectedAddedMovie, addedMovie);

        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT + 1);
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 1);
        });
    }

    /**
     * Test method for update movie with no media change.
     */
    @Test
    void update_NoMediaChange() {
        final Movie movie = MovieUtils.updateMovie(entityManager, 1);

        movieRepository.save(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT);
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT);
        });
    }

    /**
     * Test method for update movie with added medium.
     */
    @Test
    @DirtiesContext
    void update_AddedMedium() {
        final Movie movie = MovieUtils.updateMovie(entityManager, 1);
        movie.getMedia().add(MediumUtils.newMediumDomain(null));

        movieRepository.save(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.getMedia().add(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1));
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT);
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 1);
        });
    }

    /**
     * Test method for update movie with removed medium.
     */
    @Test
    void update_RemovedMedium() {
        final int mediaCount = MovieUtils.getMovie(1).getMedia().size();
        final Movie movie = MovieUtils.updateMovie(entityManager, 1);
        movie.getMedia().clear();

        movieRepository.save(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        final Movie expectedUpdatedMovie = MovieUtils.getMovie(1);
        MovieUtils.updateMovie(expectedUpdatedMovie);
        expectedUpdatedMovie.setMedia(new ArrayList<>());
        expectedUpdatedMovie.setPosition(MovieUtils.POSITION);
        MovieUtils.assertMovieDeepEquals(expectedUpdatedMovie, updatedMovie);

        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT);
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - mediaCount);
        });
    }

    /**
     * Test method for remove movie.
     */
    @Test
    void remove() {
        final int mediaCount = MovieUtils.getMovie(1).getMedia().size();

        movieRepository.delete(MovieUtils.getMovie(entityManager, 1));

        assertThat(MovieUtils.getMovie(entityManager, 1)).isNull();

        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT - 1);
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - mediaCount);
        });
    }

    /**
     * Test method for remove all movies.
     */
    @Test
    void removeAll() {
        movieRepository.deleteAll();

        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(0);
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(0);
        });
    }

}
