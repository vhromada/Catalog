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

import java.util.Collections;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.common.Movable;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.utils.TestConstants;
import cz.vhromada.common.test.validator.MovableValidatorTest;
import cz.vhromada.common.utils.CollectionUtils;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

/**
 * A class represents test for class {@link ShowValidator}.
 *
 * @author Vladimir Hromada
 */
class ShowValidatorTest extends MovableValidatorTest<Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Event for invalid IMDB code
     */
    private static final Event INVALID_IMDB_CODE_EVENT = new Event(Severity.ERROR, "SHOW_IMDB_CODE_NOT_VALID",
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
     * Test method for {@link ShowValidator#ShowValidator(MovableService, MovableValidator, MovableValidator)} with null service for shows.
     */
    @Test
    void constructor_NullShowService() {
        assertThatThrownBy(() -> new ShowValidator(null, pictureValidator, genreValidator)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ShowValidator#ShowValidator(MovableService, MovableValidator, MovableValidator)} with null validator for genre.
     */
    @Test
    void constructor_NullPictureValidator() {
        assertThatThrownBy(() -> new ShowValidator(getMovableService(), null, genreValidator)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ShowValidator#ShowValidator(MovableService, MovableValidator, MovableValidator)} with null validator for genre.
     */
    @Test
    void constructor_NullGenreValidator() {
        assertThatThrownBy(() -> new ShowValidator(getMovableService(), pictureValidator, null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null czech name.
     */
    @Test
    void validate_Deep_NullCzechName() {
        final Show show = getValidatingData(1);
        show.setCzechName(null);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null.")));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with empty string as
     * czech name.
     */
    @Test
    void validate_Deep_EmptyCzechName() {
        final Show show = getValidatingData(1);
        show.setCzechName("");

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null original name.
     */
    @Test
    void validate_Deep_NullOriginalName() {
        final Show show = getValidatingData(1);
        show.setOriginalName(null);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with empty string as
     * original name.
     */
    @Test
    void validate_Deep_EmptyOriginalName() {
        final Show show = getValidatingData(1);
        show.setOriginalName("");

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to
     * ČSFD page about show.
     */
    @Test
    void validate_Deep_NullCsfd() {
        final Show show = getValidatingData(1);
        show.setCsfd(null);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad minimal IMDB code.
     */
    @Test
    void validate_Deep_BadMinimalImdb() {
        final Show show = getValidatingData(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad divider IMDB code.
     */
    @Test
    void validate_Deep_BadDividerImdb() {
        final Show show = getValidatingData(1);
        show.setImdbCode(0);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad maximal IMDB code.
     */
    @Test
    void validate_Deep_BadMaximalImdb() {
        final Show show = getValidatingData(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(INVALID_IMDB_CODE_EVENT));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about show.
     */
    @Test
    void validate_Deep_NullWikiEn() {
        final Show show = getValidatingData(1);
        show.setWikiEn(null);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_WIKI_EN_NULL",
                "URL to english Wikipedia page about show mustn't be null.")));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about show.
     */
    @Test
    void validate_Deep_NullWikiCz() {
        final Show show = getValidatingData(1);
        show.setWikiCz(null);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about show mustn't be null.")));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Show show = getValidatingData(1);
        show.setNote(null);

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null.")));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad picture.
     */
    @Test
    void validate_Deep_BadPicture() {
        final Event event = new Event(Severity.ERROR, "PICTURE_INVALID", "Invalid data");
        final Show show = getValidatingData(1);

        when(pictureValidator.validate(any(Picture.class), any())).thenReturn(Result.error(event.getKey(), event.getMessage()));
        when(genreValidator.validate(any(Genre.class), any())).thenReturn(new Result<>());

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(event));
        });

        verifyDeepMock(show);
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null genres.
     */
    @Test
    void validate_Deep_NullGenres() {
        final Show show = getValidatingData(1);
        show.setGenres(null);

        when(pictureValidator.validate(any(Picture.class), any())).thenReturn(new Result<>());

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null.")));
        });

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS));
        verifyNoMoreInteractions(pictureValidator);
        verifyZeroInteractions(getMovableService(), genreValidator);

        validatePicture(show, pictureArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with genres with null value.
     */
    @Test
    void validate_Deep_BadGenres() {
        final Show show = getValidatingData(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        initDeepMock(show);

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));
        });

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS));
        verify(genreValidator).validate(show.getGenres().get(0), ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(pictureValidator, genreValidator);
        verifyZeroInteractions(getMovableService());

        validatePicture(show, pictureArgumentCaptor.getValue());
    }

    /**
     * Test method for {@link ShowValidator#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with genres with genre with
     * invalid data.
     */
    @Test
    void validate_Deep_GenresWithGenreWithInvalidData() {
        final Event event = new Event(Severity.ERROR, "GENRE_INVALID", "Invalid data");
        final Show show = getValidatingData(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(null)));

        when(pictureValidator.validate(any(Picture.class), any())).thenReturn(new Result<>());
        when(genreValidator.validate(any(Genre.class), any())).thenReturn(Result.error(event.getKey(), event.getMessage()));

        final Result<Void> result = getMovableValidator().validate(show, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(event));
        });

        verifyDeepMock(show);
    }

    @Override
    protected void initDeepMock(final Show validatingData) {
        super.initDeepMock(validatingData);

        when(pictureValidator.validate(any(Picture.class), any())).thenReturn(new Result<>());
        when(genreValidator.validate(any(Genre.class), any())).thenReturn(new Result<>());
    }

    @Override
    protected void verifyDeepMock(final Show validatingData) {
        super.verifyDeepMock(validatingData);

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS));
        for (final Genre genre : validatingData.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(pictureValidator, genreValidator);
        verifyZeroInteractions(getMovableService());

        validatePicture(validatingData, pictureArgumentCaptor.getValue());
    }

    @Override
    protected MovableValidator<Show> getMovableValidator() {
        return new ShowValidator(getMovableService(), pictureValidator, genreValidator);
    }

    @Override
    protected Show getValidatingData(final Integer id) {
        return ShowUtils.newShow(id);
    }

    @Override
    protected Show getValidatingData(final Integer id, final Integer position) {
        final Show show = ShowUtils.newShow(id);
        show.setPosition(position);

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getRepositoryData(final Show validatingData) {
        return ShowUtils.newShowDomain(validatingData.getId());
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getItem1() {
        return ShowUtils.newShowDomain(1);
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getItem2() {
        return ShowUtils.newShowDomain(2);
    }

    @Override
    protected String getName() {
        return "Show";
    }

    /**
     * Validates picture.
     *
     * @param show    show
     * @param picture picture
     */
    private static void validatePicture(final Show show, final Picture picture) {
        assertThat(picture).isNotNull();
        assertThat(picture.getId()).isEqualTo(show.getPicture());
    }

}
