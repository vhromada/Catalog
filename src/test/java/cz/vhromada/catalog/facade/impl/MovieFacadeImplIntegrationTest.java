package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MediumUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.catalog.utils.PictureUtils;
import cz.vhromada.common.Language;
import cz.vhromada.common.Time;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest;
import cz.vhromada.common.test.utils.TestConstants;
import cz.vhromada.common.utils.CollectionUtils;
import cz.vhromada.common.utils.Constants;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

/**
 * A class represents integration test for class {@link MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class MovieFacadeImplIntegrationTest extends MovableParentFacadeIntegrationTest<Movie, cz.vhromada.catalog.domain.Movie> {

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_YEAR_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_YEAR_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                "URL to english Wikipedia page about movie mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about movie mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with not existing picture.
     */
    @Test
    void add_NotExistingPicture() {
        final Movie movie = newData(null);
        movie.setPicture(Integer.MAX_VALUE);

        final Result<Void> result = movieFacade.add(movie);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PICTURE_NOT_EXIST", "Picture doesn't exist.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with show with genres with not existing genre.
     */
    @Test
    void add_NotExistingGenre() {
        final Movie show = newData(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(Integer.MAX_VALUE)));

        final Result<Void> result = movieFacade.add(show);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NOT_EXIST", "Genre doesn't exist.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_YEAR_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_YEAR_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                "URL to english Wikipedia page about movie mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about movie mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with not existing picture.
     */
    @Test
    void update_NotExistingPicture() {
        final Movie movie = newData(1);
        movie.setPicture(Integer.MAX_VALUE);

        final Result<Void> result = movieFacade.update(movie);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PICTURE_NOT_EXIST", "Picture doesn't exist.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));
        });

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

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with show with genres with not existing genre.
     */
    @Test
    void update_NotExistingGenre() {
        final Movie show = newData(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(Integer.MAX_VALUE)));

        final Result<Void> result = movieFacade.update(show);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NOT_EXIST", "Genre doesn't exist.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final Result<Integer> result = movieFacade.getTotalMediaCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(MediumUtils.MEDIA_COUNT);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    void getTotalLength() {
        final Result<Time> result = movieFacade.getTotalLength();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(new Time(1000));
            softly.assertThat(result.getEvents()).isEmpty();
        });

        assertDefaultRepositoryData();
    }


    @Override
    protected MovableParentFacade<Movie> getMovableParentFacade() {
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
            movie.setPicture(1);
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

        assertSoftly(softly -> {
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(0);
            softly.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
            softly.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
        });
    }

    @Override
    protected void assertAddRepositoryData() {
        super.assertAddRepositoryData();

        assertSoftly(softly -> {
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 1);
            softly.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
            softly.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
        });
    }

    @Override
    protected void assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData();

        assertSoftly(softly -> {
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - MovieUtils.getMovie(1).getMedia().size());
            softly.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
            softly.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
        });
    }

    @Override
    protected void assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData();

        assertSoftly(softly -> {
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 2);
            softly.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
            softly.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
        });
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
        movie.setPicture(1);
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
        assertSoftly(softly -> {
            softly.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT);
            softly.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
            softly.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
        });
    }

}
