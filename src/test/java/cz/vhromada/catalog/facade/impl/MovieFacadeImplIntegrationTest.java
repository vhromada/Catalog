package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.Constants;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MediumUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.catalog.utils.TestConstants;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class MovieFacadeImplIntegrationTest {

    /**
     * Event for null movie
     */
    private static final Event NULL_MOVIE_EVENT = new Event(Severity.ERROR, "MOVIE_NULL", "Movie mustn't be null.");

    /**
     * Event for movie with null ID
     */
    private static final Event NULL_MOVIE_ID_EVENT = new Event(Severity.ERROR, "MOVIE_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not existing movie
     */
    private static final Event NOT_EXIST_MOVIE_EVENT = new Event(Severity.ERROR, "MOVIE_NOT_EXIST", "Movie doesn't exist.");

    /**
     * Event for invalid year
     */
    private static final Event INVALID_YEAR_EVENT = new Event(Severity.ERROR, "MOVIE_YEAR_NOT_VALID", "Year must be between " + Constants.MIN_YEAR + " and "
            + Constants.CURRENT_YEAR + '.');

    /**
     * Event for invalid IMDB code
     */
    private static final Event INVALID_IMDB_CODE_EVENT = new Event(Severity.ERROR, "MOVIE_IMDB_CODE_NOT_VALID",
            "IMDB code must be between 1 and 9999999 or -1.");

    /**
     * Event for genre with null ID
     */
    private static final Event NULL_GENRE_ID_EVENT = new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.");

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link MovieFacade}
     */
    @Autowired
    private MovieFacade movieFacade;

    /**
     * Test method for {@link MovieFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void newData() {
        final Result<Void> result = movieFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(0));
        assertThat(MediumUtils.getMediaCount(entityManager), is(0));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final Result<List<Movie>> result = movieFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));
        MovieUtils.assertMovieListDeepEquals(result.getData(), MovieUtils.getMovies());

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= MovieUtils.MOVIES_COUNT; i++) {
            final Result<Movie> result = movieFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            MovieUtils.assertMovieDeepEquals(result.getData(), MovieUtils.getMovie(i));
        }

        final Result<Movie> result = movieFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#get(Integer)} with null movie.
     */
    @Test
    public void get_NullMovie() {
        final Result<Movie> result = movieFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        final cz.vhromada.catalog.domain.Movie expectedMovie = MovieUtils.newMovieDomain(MovieUtils.MOVIES_COUNT + 1);
        expectedMovie.setMedia(CollectionUtils.newList(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1)));
        expectedMovie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Movie addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        MovieUtils.assertMovieDeepEquals(expectedMovie, addedMovie);
        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT + 1));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT + 1));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with null movie.
     */
    @Test
    public void add_NullMovie() {
        final Result<Void> result = movieFacade.add(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with not null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = movieFacade.add(MovieUtils.newMovie(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_ID_NOT_NULL", "ID must be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null czech name.
     */
    @Test
    public void add_NullCzechName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setCzechName(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with empty string as czech name.
     */
    @Test
    public void add_EmptyCzechName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setCzechName("");

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null original name.
     */
    @Test
    public void add_NullOriginalName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setOriginalName(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with empty string as original name.
     */
    @Test
    public void add_EmptyOriginalName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setOriginalName("");

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad minimum year.
     */
    @Test
    public void add_BadMinimumYear() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_YEAR_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad maximum year.
     */
    @Test
    public void add_BadMaximumYear() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_YEAR_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null language.
     */
    @Test
    public void add_NullLanguage() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setLanguage(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null subtitles.
     */
    @Test
    public void add_NullSubtitles() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setSubtitles(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with subtitles with null value.
     */
    @Test
    public void add_BadSubtitles() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null media.
     */
    @Test
    public void add_NullMedia() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setMedia(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with media with null value.
     */
    @Test
    public void add_BadMedia() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with media with negative value as medium.
     */
    @Test
    public void add_BadMedium() {
        final Medium badMedium = MediumUtils.newMedium(Integer.MAX_VALUE);
        badMedium.setLength(-1);
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to ČSFD page about movie.
     */
    @Test
    public void add_NullCsfd() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setCsfd(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad minimal IMDB code.
     */
    @Test
    public void add_BadMinimalImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad divider IMDB code.
     */
    @Test
    public void add_BadDividerImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setImdbCode(0);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad maximal IMDB code.
     */
    @Test
    public void add_BadMaximalImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test
    public void add_NullWikiEn() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setWikiEn(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                "URL to english Wikipedia page about movie mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    public void add_NullWikiCz() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setWikiCz(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL", "URL to czech Wikipedia page about movie mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null path to file with movie's picture.
     */
    @Test
    public void add_NullPicture() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setPicture(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_PICTURE_NULL", "Picture mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null note.
     */
    @Test
    public void add_NullNote() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        movie.setNote(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null genres.
     */
    @Test
    public void add_NullGenres() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(null);

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with null value.
     */
    @Test
    public void add_BadGenres() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with genre with null ID.
     */
    @Test
    public void add_NullGenreId() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with genre with null name.
     */
    @Test
    public void add_NullGenreName() {
        final Movie movie = MovieUtils.newMovie(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with genre with empty string as name.
     */
    @Test
    public void add_EmptyGenreName() {
        final Movie movie = MovieUtils.newMovie(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName("");
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = movieFacade.add(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        MovieUtils.assertMovieDeepEquals(movie, updatedMovie);
        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with null movie.
     */
    @Test
    public void update_NullMovie() {
        final Result<Void> result = movieFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null ID.
     */
    @Test
    public void update_NullId() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setId(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_ID_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null czech name.
     */
    @Test
    public void update_NullCzechName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCzechName(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with empty string as czech name.
     */
    @Test
    public void update_EmptyCzechName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCzechName("");

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null original name.
     */
    @Test
    public void update_NullOriginalName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setOriginalName(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with empty string as original name.
     */
    @Test
    public void update_EmptyOriginalName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setOriginalName("");

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad minimum year.
     */
    @Test
    public void update_BadMinimumYear() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_YEAR_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad maximum year.
     */
    @Test
    public void update_BadMaximumYear() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_YEAR_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null language.
     */
    @Test
    public void update_NullLanguage() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setLanguage(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null subtitles.
     */
    @Test
    public void update_NullSubtitles() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setSubtitles(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with subtitles with null value.
     */
    @Test
    public void update_BadSubtitles() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null media.
     */
    @Test
    public void update_NullMedia() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with media with null value.
     */
    @Test
    public void update_BadMedia() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with media with negative value as medium.
     */
    @Test
    public void update_BadMedium() {
        final Medium badMedium = MediumUtils.newMedium(Integer.MAX_VALUE);
        badMedium.setLength(-1);
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to ČSFD page about movie.
     */
    @Test
    public void update_NullCsfd() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCsfd(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad minimal IMDB code.
     */
    @Test
    public void update_BadMinimalImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad divider IMDB code.
     */
    @Test
    public void update_BadDividerImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(0);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad maximal IMDB code.
     */
    @Test
    public void update_BadMaximalImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test
    public void update_NullWikiEn() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setWikiEn(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                "URL to english Wikipedia page about movie mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    public void update_NullWikiCz() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setWikiCz(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL", "URL to czech Wikipedia page about movie mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null path to file with movie's picture.
     */
    @Test
    public void update_NullPicture() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setPicture(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_PICTURE_NULL", "Picture mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null note.
     */
    @Test
    public void update_NullNote() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setNote(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null genres.
     */
    @Test
    public void update_NullGenres() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(null);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with null value.
     */
    @Test
    public void update_BadGenres() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with genre with null ID.
     */
    @Test
    public void update_NullGenreId() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with genre with null name.
     */
    @Test
    public void update_NullGenreName() {
        final Movie movie = MovieUtils.newMovie(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with genre with empty string as name.
     */
    @Test
    public void update_EmptyGenreName() {
        final Movie movie = MovieUtils.newMovie(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName("");
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad ID.
     */
    @Test
    public void update_BadId() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setId(Integer.MAX_VALUE);

        final Result<Void> result = movieFacade.update(movie);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#remove(cz.vhromada.catalog.common.Movable)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Result<Void> result = movieFacade.remove(MovieUtils.newMovie(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MovieUtils.getMovie(entityManager, 1), is(nullValue()));
        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT - 1));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT - MovieUtils.getMovie(1).getMedia().size()));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#remove(cz.vhromada.catalog.common.Movable)} with null movie.
     */
    @Test
    public void remove_NullMovie() {
        final Result<Void> result = movieFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#remove(cz.vhromada.catalog.common.Movable)} with movie with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = movieFacade.remove(MovieUtils.newMovie(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_ID_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#remove(cz.vhromada.catalog.common.Movable)} with movie with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = movieFacade.remove(MovieUtils.newMovie(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(cz.vhromada.catalog.common.Movable)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Medium medium1 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT - 1);
        medium1.setId(MediumUtils.MEDIA_COUNT + 1);
        final cz.vhromada.catalog.domain.Medium medium2 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT);
        medium2.setId(MediumUtils.MEDIA_COUNT + 2);
        final cz.vhromada.catalog.domain.Movie movie = MovieUtils.getMovie(MovieUtils.MOVIES_COUNT);
        movie.setId(MovieUtils.MOVIES_COUNT + 1);
        movie.setMedia(CollectionUtils.newList(medium1, medium2));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT - 1), GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT)));

        final Result<Void> result = movieFacade.duplicate(MovieUtils.newMovie(MovieUtils.MOVIES_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Movie duplicatedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        MovieUtils.assertMovieDeepEquals(movie, duplicatedMovie);
        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT + 1));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT + 2));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(cz.vhromada.catalog.common.Movable)} with null movie.
     */
    @Test
    public void duplicate_NullMovie() {
        final Result<Void> result = movieFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(cz.vhromada.catalog.common.Movable)} with movie with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = movieFacade.duplicate(MovieUtils.newMovie(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_ID_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(cz.vhromada.catalog.common.Movable)} with movie with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = movieFacade.duplicate(MovieUtils.newMovie(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(cz.vhromada.catalog.common.Movable)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Movie movie1 = MovieUtils.getMovie(1);
        movie1.setPosition(1);
        final cz.vhromada.catalog.domain.Movie movie2 = MovieUtils.getMovie(2);
        movie2.setPosition(0);

        final Result<Void> result = movieFacade.moveUp(MovieUtils.newMovie(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        MovieUtils.assertMovieDeepEquals(movie1, MovieUtils.getMovie(entityManager, 1));
        MovieUtils.assertMovieDeepEquals(movie2, MovieUtils.getMovie(entityManager, 2));
        for (int i = 3; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), MovieUtils.getMovie(entityManager, i));
        }
        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(cz.vhromada.catalog.common.Movable)} with null movie.
     */
    @Test
    public void moveUp_NullMovie() {
        final Result<Void> result = movieFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(cz.vhromada.catalog.common.Movable)} with movie with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = movieFacade.moveUp(MovieUtils.newMovie(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_ID_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(cz.vhromada.catalog.common.Movable)} with not movable movie.
     */
    @Test
    public void moveUp_NotMovableMovie() {
        final Result<Void> result = movieFacade.moveUp(MovieUtils.newMovie(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_NOT_MOVABLE", "Movie can't be moved up.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(cz.vhromada.catalog.common.Movable)} with movie with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = movieFacade.moveUp(MovieUtils.newMovie(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(cz.vhromada.catalog.common.Movable)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Movie movie1 = MovieUtils.getMovie(1);
        movie1.setPosition(1);
        final cz.vhromada.catalog.domain.Movie movie2 = MovieUtils.getMovie(2);
        movie2.setPosition(0);

        final Result<Void> result = movieFacade.moveDown(MovieUtils.newMovie(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        MovieUtils.assertMovieDeepEquals(movie1, MovieUtils.getMovie(entityManager, 1));
        MovieUtils.assertMovieDeepEquals(movie2, MovieUtils.getMovie(entityManager, 2));
        for (int i = 3; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), MovieUtils.getMovie(entityManager, i));
        }
        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(cz.vhromada.catalog.common.Movable)} with null movie.
     */
    @Test
    public void moveDown_NullMovie() {
        final Result<Void> result = movieFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(cz.vhromada.catalog.common.Movable)} with movie with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = movieFacade.moveDown(MovieUtils.newMovie(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_MOVIE_ID_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(cz.vhromada.catalog.common.Movable)} with not movable movie.
     */
    @Test
    public void moveDown_NotMovableMovie() {
        final Result<Void> result = movieFacade.moveDown(MovieUtils.newMovie(MovieUtils.MOVIES_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "MOVIE_NOT_MOVABLE", "Movie can't be moved down.")));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(cz.vhromada.catalog.common.Movable)} with movie with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = movieFacade.moveDown(MovieUtils.newMovie(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_MOVIE_EVENT));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void updatePositions() {
        final Result<Void> result = movieFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        for (int i = 1; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), MovieUtils.getMovie(entityManager, i));
        }
        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    public void getTotalMediaCount() {
        final Result<Integer> result = movieFacade.getTotalMediaCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(MediumUtils.MEDIA_COUNT));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    public void getTotalLength() {
        final Result<Time> result = movieFacade.getTotalLength();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(new Time(1000)));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(MovieUtils.getMoviesCount(entityManager), is(MovieUtils.MOVIES_COUNT));
        assertThat(MediumUtils.getMediaCount(entityManager), is(MediumUtils.MEDIA_COUNT));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

}
