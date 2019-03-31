package cz.vhromada.catalog.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MediumUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.common.Language;
import cz.vhromada.common.Movable;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.utils.TestConstants;
import cz.vhromada.common.test.validator.MovableValidatorTest;
import cz.vhromada.common.utils.Constants;
import cz.vhromada.common.validator.AbstractMovableValidator;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

/**
 * A class represents test for class {@link MovieValidator}.
 *
 * @author Vladimir Hromada
 */
class MovieValidatorTest extends MovableValidatorTest<Movie, cz.vhromada.catalog.domain.Movie> {

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
     * Validator for picture
     */
    @Mock
    private MovableValidator<Picture> pictureValidator;

    /**
     * Validator for genre
     */
    @Mock
    private MovableValidator<Genre> genreValidator;

    /**
     * Argument captor for picture
     */
    private ArgumentCaptor<Picture> pictureArgumentCaptor;

    /**
     * Initializes data.
     */
    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        pictureArgumentCaptor = ArgumentCaptor.forClass(Picture.class);
    }

    /**
     * Test method for {@link MovieValidator#MovieValidator(MovableService, MovableValidator, MovableValidator)} with null service for movies.
     */
    @Test
    void constructor_NullMovieService() {
        assertThatThrownBy(() -> new MovieValidator(null, pictureValidator, genreValidator)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieValidator#MovieValidator(MovableService, MovableValidator, MovableValidator)} with null validator for picture.
     */
    @Test
    void constructor_NullPictureValidator() {
        assertThatThrownBy(() -> new MovieValidator(getService(), null, genreValidator)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link MovieValidator#MovieValidator(MovableService, MovableValidator, MovableValidator)} with null validator for genre.
     */
    @Test
    void constructor_NullGenreValidator() {
        assertThatThrownBy(() -> new MovieValidator(getService(), pictureValidator, null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null czech name.
     */
    @Test
    void validate_Deep_NullCzechName() {
        final Movie movie = getValidatingData(1);
        movie.setCzechName(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with empty string as
     * czech name.
     */
    @Test
    void validate_Deep_EmptyCzechName() {
        final Movie movie = getValidatingData(1);
        movie.setCzechName("");

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null original
     * name.
     */
    @Test
    void validate_Deep_NullOriginalName() {
        final Movie movie = getValidatingData(1);
        movie.setOriginalName(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with empty string as
     * original name.
     */
    @Test
    void validate_Deep_EmptyOriginalName() {
        final Movie movie = getValidatingData(1);
        movie.setOriginalName("");

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad minimum year.
     */
    @Test
    void validate_Deep_BadMinimumYear() {
        final Movie movie = getValidatingData(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_YEAR_EVENT));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad maximum year.
     */
    @Test
    void validate_Deep_BadMaximumYear() {
        final Movie movie = getValidatingData(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_YEAR_EVENT));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null language.
     */
    @Test
    void validate_Deep_NullLanguage() {
        final Movie movie = getValidatingData(1);
        movie.setLanguage(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null subtitles.
     */
    @Test
    void validate_Deep_NullSubtitles() {
        final Movie movie = getValidatingData(1);
        movie.setSubtitles(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with subtitles with
     * null value.
     */
    @Test
    void validate_Deep_BadSubtitles() {
        final Movie movie = getValidatingData(1);
        movie.setSubtitles(Arrays.asList(Language.CZ, null));

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null media.
     */
    @Test
    void validate_Deep_NullMedia() {
        final Movie movie = getValidatingData(1);
        movie.setMedia(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data media with null value.
     */
    @Test
    void validate_Deep_BadMedia() {
        final Movie movie = getValidatingData(1);
        movie.setMedia(Arrays.asList(MediumUtils.newMedium(1), null));

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with media with
     * negative value as medium.
     */
    @Test
    void validate_Deep_MediaWithBadMedium() {
        final Medium badMedium = MediumUtils.newMedium(2);
        badMedium.setLength(-1);
        final Movie movie = getValidatingData(1);
        movie.setMedia(List.of(MediumUtils.newMedium(1), badMedium));

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to
     * ČSFD page about movie.
     */
    @Test
    void validate_Deep_NullCsfd() {
        final Movie movie = getValidatingData(1);
        movie.setCsfd(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad minimal
     * IMDB code.
     */
    @Test
    void validate_Deep_BadMinimalImdb() {
        final Movie movie = getValidatingData(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad divider
     * IMDB code.
     */
    @Test
    void validate_Deep_BadDividerImdb() {
        final Movie movie = getValidatingData(1);
        movie.setImdbCode(0);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad maximal
     * IMDB code.
     */
    @Test
    void validate_Deep_BadMaximalImdb() {
        final Movie movie = getValidatingData(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to
     * english Wikipedia page about movie.
     */
    @Test
    void validate_Deep_NullWikiEn() {
        final Movie movie = getValidatingData(1);
        movie.setWikiEn(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                "URL to english Wikipedia page about movie mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about movie.
     */
    @Test
    void validate_Deep_NullWikiCz() {
        final Movie movie = getValidatingData(1);
        movie.setWikiCz(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about movie mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Movie movie = getValidatingData(1);
        movie.setNote(null);

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad picture.
     */
    @Test
    void validate_Deep_BadPicture() {
        final Event event = new Event(Severity.ERROR, "PICTURE_INVALID", "Invalid data");
        final Movie movie = getValidatingData(1);

        when(pictureValidator.validate(any(Picture.class), any())).thenReturn(Result.error(event.getKey(), event.getMessage()));
        when(genreValidator.validate(any(Genre.class), any())).thenReturn(new Result<>());

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(event));
        });

        verifyDeepMock(movie);
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null genres.
     */
    @Test
    void validate_Deep_NullGenres() {
        final Movie movie = getValidatingData(1);
        movie.setGenres(null);

        when(pictureValidator.validate(any(Picture.class), any())).thenReturn(new Result<>());

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")));
        });

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS));
        verifyNoMoreInteractions(pictureValidator);
        verifyZeroInteractions(getService(), genreValidator);

        validatePicture(movie, pictureArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with genres with
     * null value.
     */
    @Test
    void validate_Deep_BadGenres() {
        final Movie movie = getValidatingData(1);
        movie.setGenres(Arrays.asList(GenreUtils.newGenre(1), null));

        initDeepMock(movie);

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));
        });

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS));
        verify(genreValidator).validate(movie.getGenres().get(0), ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(pictureValidator, genreValidator);
        verifyZeroInteractions(getService());

        validatePicture(movie, pictureArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link AbstractMovableValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with genres with genre
     * with invalid data.
     */
    @Test
    void validate_Deep_GenresWithGenreWithInvalidData() {
        final Event event = new Event(Severity.ERROR, "GENRE_INVALID", "Invalid data");
        final Movie movie = getValidatingData(1);
        movie.setGenres(List.of(GenreUtils.newGenre(null)));

        when(pictureValidator.validate(any(Picture.class), any())).thenReturn(new Result<>());
        when(genreValidator.validate(any(Genre.class), any())).thenReturn(Result.error(event.getKey(), event.getMessage()));

        final Result<Void> result = getValidator().validate(movie, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(event));
        });

        verifyDeepMock(movie);
    }

    @Override
    protected void initDeepMock(final Movie validatingData) {
        super.initDeepMock(validatingData);

        when(pictureValidator.validate(any(Picture.class), any())).thenReturn(new Result<>());
        when(genreValidator.validate(any(Genre.class), any())).thenReturn(new Result<>());
    }

    @Override
    protected void verifyDeepMock(final Movie validatingData) {
        super.verifyDeepMock(validatingData);

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS));
        for (final Genre genre : validatingData.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(pictureValidator, genreValidator);
        verifyZeroInteractions(getService());

        validatePicture(validatingData, pictureArgumentCaptor.getValue());
    }

    @Override
    protected MovableValidator<Movie> getValidator() {
        return new MovieValidator(getService(), pictureValidator, genreValidator);
    }

    @Override
    protected Movie getValidatingData(final Integer id) {
        return MovieUtils.newMovie(id);
    }

    @Override
    protected Movie getValidatingData(final Integer id, final Integer position) {
        final Movie movie = MovieUtils.newMovie(id);
        movie.setPosition(position);

        return movie;
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

    /**
     * Validates picture.
     *
     * @param movie   movie
     * @param picture picture
     */
    private static void validatePicture(final Movie movie, final Picture picture) {
        assertThat(picture).isNotNull();
        assertThat(picture.getId()).isEqualTo(movie.getPicture());
    }

}
