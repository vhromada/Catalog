package cz.vhromada.catalog.validator.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.Constants;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MediumUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.catalog.utils.TestConstants;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * A class represents test for class {@link MovieValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
class MovieValidatorImplTest extends AbstractValidatorTest<Movie, cz.vhromada.catalog.domain.Movie> {

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
     * Validator for genre
     */
    @Mock
    private CatalogValidator<Genre> genreValidator;

    /**
     * Initializes validator for genre.
     */
    @BeforeEach
    @Override
    void setUp() {
        super.setUp();

        when(genreValidator.validate(any(Genre.class), any())).thenReturn(new Result<>());
    }

    /**
     * Test method for {@link MovieValidatorImpl#MovieValidatorImpl(CatalogService, CatalogValidator)} with null service for movies.
     */
    @Test
    void constructor_NullMovieService() {
        assertThatThrownBy(() -> new MovieValidatorImpl(null, genreValidator)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieValidatorImpl#MovieValidatorImpl(CatalogService, CatalogValidator)} with null validator for genre.
     */
    @Test
    void constructor_NullGenreValidator() {
        assertThatThrownBy(() -> new MovieValidatorImpl(getCatalogService(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null czech name.
     */
    @Test
    void validate_Deep_NullCzechName() {
        final Movie movie = getValidatingData(1);
        movie.setCzechName(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with empty string as
     * czech name.
     */
    @Test
    void validate_Deep_EmptyCzechName() {
        final Movie movie = getValidatingData(1);
        movie.setCzechName("");

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null original name.
     */
    @Test
    void validate_Deep_NullOriginalName() {
        final Movie movie = getValidatingData(1);
        movie.setOriginalName(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with empty string as
     * original name.
     */
    @Test
    void validate_Deep_EmptyOriginalName() {
        final Movie movie = getValidatingData(1);
        movie.setOriginalName("");

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad minimum year.
     */
    @Test
    void validate_Deep_BadMinimumYear() {
        final Movie movie = getValidatingData(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_YEAR_EVENT));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad maximum year.
     */
    @Test
    void validate_Deep_BadMaximumYear() {
        final Movie movie = getValidatingData(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_YEAR_EVENT));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null language.
     */
    @Test
    void validate_Deep_NullLanguage() {
        final Movie movie = getValidatingData(1);
        movie.setLanguage(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null subtitles.
     */
    @Test
    void validate_Deep_NullSubtitles() {
        final Movie movie = getValidatingData(1);
        movie.setSubtitles(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with subtitles with
     * null value.
     */
    @Test
    void validate_Deep_BadSubtitles() {
        final Movie movie = getValidatingData(1);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null media.
     */
    @Test
    void validate_Deep_NullMedia() {
        final Movie movie = getValidatingData(1);
        movie.setMedia(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data media with null value.
     */
    @Test
    void validate_Deep_BadMedia() {
        final Movie movie = getValidatingData(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with media with
     * negative value as medium.
     */
    @Test
    void validate_Deep_MediaWithBadMedium() {
        final Medium badMedium = MediumUtils.newMedium(2);
        badMedium.setLength(-1);
        final Movie movie = getValidatingData(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to
     * ČSFD page about movie.
     */
    @Test
    void validate_Deep_NullCsfd() {
        final Movie movie = getValidatingData(1);
        movie.setCsfd(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad minimal IMDB code.
     */
    @Test
    void validate_Deep_BadMinimalImdb() {
        final Movie movie = getValidatingData(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad divider IMDB code.
     */
    @Test
    void validate_Deep_BadDividerImdb() {
        final Movie movie = getValidatingData(1);
        movie.setImdbCode(0);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad maximal IMDB code.
     */
    @Test
    void validate_Deep_BadMaximalImdb() {
        final Movie movie = getValidatingData(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about movie.
     */
    @Test
    void validate_Deep_NullWikiEn() {
        final Movie movie = getValidatingData(1);
        movie.setWikiEn(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                "URL to english Wikipedia page about movie mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about movie.
     */
    @Test
    void validate_Deep_NullWikiCz() {
        final Movie movie = getValidatingData(1);
        movie.setWikiCz(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about movie mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null path to file with
     * movie picture.
     */
    @Test
    void validate_Deep_NullPicture() {
        final Movie movie = getValidatingData(1);
        movie.setPicture(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_PICTURE_NULL", "Picture mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Movie movie = getValidatingData(1);
        movie.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null genres.
     */
    @Test
    void validate_Deep_NullGenres() {
        final Movie movie = getValidatingData(1);
        movie.setGenres(null);

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")));
        });

        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with genres with null value.
     */
    @Test
    void validate_Deep_BadGenres() {
        final Movie movie = getValidatingData(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));
        });

        verify(genreValidator).validate(movie.getGenres().get(0), ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link MovieValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with genres with genre with
     * invalid data.
     */
    @Test
    void validate_Deep_GenresWithGenreWithInvalidData() {
        final Event event = new Event(Severity.ERROR, "GENRE_INVALID", "Invalid data");
        final Movie movie = getValidatingData(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(null)));

        when(genreValidator.validate(any(Genre.class), any())).thenReturn(Result.error(event.getKey(), event.getMessage()));

        final Result<Void> result = getCatalogValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(event));
        });

        for (final Genre genre : movie.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Movie> getCatalogValidator() {
        return new MovieValidatorImpl(getCatalogService(), genreValidator);
    }

    @Override
    protected Movie getValidatingData(final Integer id) {
        return MovieUtils.newMovie(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getRepositoryData(final Movie validatingData) {
        return MovieUtils.newMovieDomain(validatingData.getId());
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getItem1() {
        return MovieUtils.newMovieDomain(1);
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getItem2() {
        return MovieUtils.newMovieDomain(2);
    }

    @Override
    protected String getName() {
        return "Movie";
    }

}
