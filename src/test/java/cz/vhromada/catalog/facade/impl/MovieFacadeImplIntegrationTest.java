package cz.vhromada.catalog.facade.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.CatalogParentFacade;
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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A class represents integration test for class {@link MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class MovieFacadeImplIntegrationTest extends AbstractParentFacadeIntegrationTest<Movie, cz.vhromada.catalog.domain.Movie> {

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
     * Test method for {@link MovieFacade#add(Movie)} with movie with null czech name.
     */
    @Test
    void add_NullCzechName() {
        final Movie movie = newData(null);
        movie.setCzechName(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with empty string as czech name.
     */
    @Test
    void add_EmptyCzechName() {
        final Movie movie = newData(null);
        movie.setCzechName("");

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null original name.
     */
    @Test
    void add_NullOriginalName() {
        final Movie movie = newData(null);
        movie.setOriginalName(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with empty string as original name.
     */
    @Test
    void add_EmptyOriginalName() {
        final Movie movie = newData(null);
        movie.setOriginalName("");

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad minimum year.
     */
    @Test
    void add_BadMinimumYear() {
        final Movie movie = newData(null);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_YEAR_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad maximum year.
     */
    @Test
    void add_BadMaximumYear() {
        final Movie movie = newData(null);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_YEAR_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null language.
     */
    @Test
    void add_NullLanguage() {
        final Movie movie = newData(null);
        movie.setLanguage(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null subtitles.
     */
    @Test
    void add_NullSubtitles() {
        final Movie movie = newData(null);
        movie.setSubtitles(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with subtitles with null value.
     */
    @Test
    void add_BadSubtitles() {
        final Movie movie = newData(null);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null media.
     */
    @Test
    void add_NullMedia() {
        final Movie movie = newData(null);
        movie.setMedia(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with media with null value.
     */
    @Test
    void add_BadMedia() {
        final Movie movie = newData(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with media with negative value as medium.
     */
    @Test
    void add_BadMedium() {
        final Medium badMedium = MediumUtils.newMedium(Integer.MAX_VALUE);
        badMedium.setLength(-1);
        final Movie movie = newData(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to ČSFD page about movie.
     */
    @Test
    void add_NullCsfd() {
        final Movie movie = newData(null);
        movie.setCsfd(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad minimal IMDB code.
     */
    @Test
    void add_BadMinimalImdb() {
        final Movie movie = newData(null);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_IMDB_CODE_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad divider IMDB code.
     */
    @Test
    void add_BadDividerImdb() {
        final Movie movie = newData(null);
        movie.setImdbCode(0);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_IMDB_CODE_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad maximal IMDB code.
     */
    @Test
    void add_BadMaximalImdb() {
        final Movie movie = newData(null);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_IMDB_CODE_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test
    void add_NullWikiEn() {
        final Movie movie = newData(null);
        movie.setWikiEn(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                "URL to english Wikipedia page about movie mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    void add_NullWikiCz() {
        final Movie movie = newData(null);
        movie.setWikiCz(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about movie mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null path to file with movie's picture.
     */
    @Test
    void add_NullPicture() {
        final Movie movie = newData(null);
        movie.setPicture(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_PICTURE_NULL", "Picture mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null note.
     */
    @Test
    void add_NullNote() {
        final Movie movie = newData(null);
        movie.setNote(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null genres.
     */
    @Test
    void add_NullGenres() {
        final Movie movie = newData(null);
        movie.setGenres(null);

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with null value.
     */
    @Test
    void add_BadGenres() {
        final Movie movie = newData(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with genre with null ID.
     */
    @Test
    void add_NullGenreId() {
        final Movie movie = newData(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with genre with null name.
     */
    @Test
    void add_NullGenreName() {
        final Movie movie = newData(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with genre with empty string as name.
     */
    @Test
    void add_EmptyGenreName() {
        final Movie movie = newData(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName("");
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = movieFacade.add(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null czech name.
     */
    @Test
    void update_NullCzechName() {
        final Movie movie = newData(1);
        movie.setCzechName(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with empty string as czech name.
     */
    @Test
    void update_EmptyCzechName() {
        final Movie movie = newData(1);
        movie.setCzechName("");

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null original name.
     */
    @Test
    void update_NullOriginalName() {
        final Movie movie = newData(1);
        movie.setOriginalName(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with empty string as original name.
     */
    @Test
    void update_EmptyOriginalName() {
        final Movie movie = newData(1);
        movie.setOriginalName("");

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad minimum year.
     */
    @Test
    void update_BadMinimumYear() {
        final Movie movie = newData(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_YEAR_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad maximum year.
     */
    @Test
    void update_BadMaximumYear() {
        final Movie movie = newData(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_YEAR_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null language.
     */
    @Test
    void update_NullLanguage() {
        final Movie movie = newData(1);
        movie.setLanguage(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null subtitles.
     */
    @Test
    void update_NullSubtitles() {
        final Movie movie = newData(1);
        movie.setSubtitles(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with subtitles with null value.
     */
    @Test
    void update_BadSubtitles() {
        final Movie movie = newData(1);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null media.
     */
    @Test
    void update_NullMedia() {
        final Movie movie = newData(1);
        movie.setMedia(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with media with null value.
     */
    @Test
    void update_BadMedia() {
        final Movie movie = newData(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with media with negative value as medium.
     */
    @Test
    void update_BadMedium() {
        final Medium badMedium = MediumUtils.newMedium(Integer.MAX_VALUE);
        badMedium.setLength(-1);
        final Movie movie = newData(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")),
                result.getEvents())
        );
        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to ČSFD page about movie.
     */
    @Test
    void update_NullCsfd() {
        final Movie movie = newData(1);
        movie.setCsfd(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad minimal IMDB code.
     */
    @Test
    void update_BadMinimalImdb() {
        final Movie movie = newData(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_IMDB_CODE_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad divider IMDB code.
     */
    @Test
    void update_BadDividerImdb() {
        final Movie movie = newData(1);
        movie.setImdbCode(0);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_IMDB_CODE_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad maximal IMDB code.
     */
    @Test
    void update_BadMaximalImdb() {
        final Movie movie = newData(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(INVALID_IMDB_CODE_EVENT), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test
    void update_NullWikiEn() {
        final Movie movie = newData(1);
        movie.setWikiEn(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                "URL to english Wikipedia page about movie mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    void update_NullWikiCz() {
        final Movie movie = newData(1);
        movie.setWikiCz(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about movie mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null path to file with movie's picture.
     */
    @Test
    void update_NullPicture() {
        final Movie movie = newData(1);
        movie.setPicture(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_PICTURE_NULL", "Picture mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null note.
     */
    @Test
    void update_NullNote() {
        final Movie movie = newData(1);
        movie.setNote(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null genres.
     */
    @Test
    void update_NullGenres() {
        final Movie movie = newData(1);
        movie.setGenres(null);

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with null value.
     */
    @Test
    void update_BadGenres() {
        final Movie movie = newData(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")),
                result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with genre with null ID.
     */
    @Test
    void update_NullGenreId() {
        final Movie movie = newData(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with genre with null name.
     */
    @Test
    void update_NullGenreName() {
        final Movie movie = newData(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with genre with empty string as name.
     */
    @Test
    void update_EmptyGenreName() {
        final Movie movie = newData(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName("");
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        final Result<Void> result = movieFacade.update(movie);

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.ERROR, result.getStatus()),
            () -> assertEquals(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")), result.getEvents())
        );

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final Result<Integer> result = movieFacade.getTotalMediaCount();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertEquals(Integer.valueOf(MediumUtils.MEDIA_COUNT), result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertAll(
            () -> assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager)),
            () -> assertEquals(MediumUtils.MEDIA_COUNT, MediumUtils.getMediaCount(entityManager)),
            () -> assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager))
        );
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    void getTotalLength() {
        final Result<Time> result = movieFacade.getTotalLength();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertEquals(new Time(1000), result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        assertAll(
            () -> assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager)),
            () -> assertEquals(MediumUtils.MEDIA_COUNT, MediumUtils.getMediaCount(entityManager)),
            () -> assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager))
        );
    }


    @Override
    protected CatalogParentFacade<Movie> getCatalogParentFacade() {
        return movieFacade;
    }

    @Override
    protected Integer getDefaultDataCount() {
        return MovieUtils.MOVIES_COUNT;
    }

    @Override
    protected Integer getRepositoryDataCount() {
        return MovieUtils.getMoviesCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Movie> getDataList() {
        return MovieUtils.getMovies();
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getDomainData(final Integer index) {
        return MovieUtils.getMovie(index);
    }

    @Override
    protected Movie newData(final Integer id) {
        final Movie movie = MovieUtils.newMovie(id);
        if (id == null || Integer.MAX_VALUE == id) {
            movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1)));
        }

        return movie;
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie newDomainData(final Integer id) {
        return MovieUtils.newMovieDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getRepositoryData(final Integer id) {
        return MovieUtils.getMovie(entityManager, id);
    }

    @Override
    protected String getName() {
        return "Movie";
    }

    @Override
    protected void clearReferencedData() {
    }

    @Override
    protected void assertDataListDeepEquals(final List<Movie> expected, final List<cz.vhromada.catalog.domain.Movie> actual) {
        MovieUtils.assertMovieListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Movie expected, final cz.vhromada.catalog.domain.Movie actual) {
        MovieUtils.assertMovieDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Movie expected, final cz.vhromada.catalog.domain.Movie actual) {
        MovieUtils.assertMovieDeepEquals(expected, actual);
    }

    @Override
    protected void assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertNewRepositoryData() {
        super.assertNewRepositoryData();

        assertAll(
            () -> assertEquals(0, MediumUtils.getMediaCount(entityManager)),
            () -> assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager))
        );
    }

    @Override
    protected void assertAddRepositoryData() {
        super.assertAddRepositoryData();

        assertAll(
            () -> assertEquals(MediumUtils.MEDIA_COUNT + 1, MediumUtils.getMediaCount(entityManager)),
            () -> assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager))
        );
    }

    @Override
    protected void assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData();

        assertAll(
            () -> assertEquals(MediumUtils.MEDIA_COUNT - MovieUtils.getMovie(1).getMedia().size(), MediumUtils.getMediaCount(entityManager)),
            () -> assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager))
        );
    }

    @Override
    protected void assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData();

        assertAll(
            () -> assertEquals(MediumUtils.MEDIA_COUNT + 2, MediumUtils.getMediaCount(entityManager)),
            () -> assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager))
        );
    }

    @Override
    protected Movie getUpdateData(final Integer id) {
        final Movie movie = super.getUpdateData(id);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));

        return movie;
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getExpectedAddData() {
        final cz.vhromada.catalog.domain.Movie movie = super.getExpectedAddData();
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1)));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));

        return movie;
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getExpectedDuplicatedData() {
        final cz.vhromada.catalog.domain.Movie movie = super.getExpectedDuplicatedData();
        final cz.vhromada.catalog.domain.Medium medium1 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT - 1);
        medium1.setId(MediumUtils.MEDIA_COUNT + 1);
        final cz.vhromada.catalog.domain.Medium medium2 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT);
        medium2.setId(MediumUtils.MEDIA_COUNT + 2);
        movie.setMedia(CollectionUtils.newList(medium1, medium2));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT - 1), GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT)));

        return movie;
    }

    /**
     * Asserts references.
     */
    private void assertReferences() {
        assertAll(
            () -> assertEquals(MediumUtils.MEDIA_COUNT, MediumUtils.getMediaCount(entityManager)),
            () -> assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager))
        );
    }

}
