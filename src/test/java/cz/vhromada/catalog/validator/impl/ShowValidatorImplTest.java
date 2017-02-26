package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.utils.TestConstants;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * A class represents test for class {@link ShowValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class ShowValidatorImplTest extends AbstractValidatorTest<Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Event for invalid IMDB code
     */
    private static final Event INVALID_IMDB_CODE_EVENT = new Event(Severity.ERROR, "SHOW_IMDB_CODE_NOT_VALID",
            "IMDB code must be between 1 and 9999999 or -1.");

    /**
     * Validator for genre
     */
    @Mock
    private CatalogValidator<Genre> genreValidator;

    /**
     * Initializes validator for genre.
     */
    @Before
    @Override
    public void setUp() {
        super.setUp();

        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(new Result<>());
    }

    /**
     * Test method for {@link ShowValidatorImpl#ShowValidatorImpl(CatalogService, CatalogValidator)} with null validator for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGenreValidator() {
        new ShowValidatorImpl(getCatalogService(), null);
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null czech name.
     */
    @Test
    public void validate_Deep_NullCzechName() {
        final Show show = getValidatingData(1);
        show.setCzechName(null);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with empty string as
     * czech name.
     */
    @Test
    public void validate_Deep_EmptyCzechName() {
        final Show show = getValidatingData(1);
        show.setCzechName("");

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null original name.
     */
    @Test
    public void validate_Deep_NullOriginalName() {
        final Show show = getValidatingData(1);
        show.setOriginalName(null);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with empty string as
     * original name.
     */
    @Test
    public void validate_Deep_EmptyOriginalName() {
        final Show show = getValidatingData(1);
        show.setOriginalName("");

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to
     * ČSFD page about show.
     */
    @Test
    public void validate_Deep_NullCsfd() {
        final Show show = getValidatingData(1);
        show.setCsfd(null);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad minimal IMDB code.
     */
    @Test
    public void validate_Deep_BadMinimalImdb() {
        final Show show = getValidatingData(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad divider IMDB code.
     */
    @Test
    public void validate_Deep_BadDividerImdb() {
        final Show show = getValidatingData(1);
        show.setImdbCode(0);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with bad maximal IMDB code.
     */
    @Test
    public void validate_Deep_BadMaximalImdb() {
        final Show show = getValidatingData(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(INVALID_IMDB_CODE_EVENT));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about show.
     */
    @Test
    public void validate_Deep_NullWikiEn() {
        final Show show = getValidatingData(1);
        show.setWikiEn(null);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_EN_NULL", "URL to english Wikipedia page about show mustn't be null.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about show.
     */
    @Test
    public void validate_Deep_NullWikiCz() {
        final Show show = getValidatingData(1);
        show.setWikiCz(null);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL", "URL to czech Wikipedia page about show mustn't be null.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null path to file with
     * show picture.
     */
    @Test
    public void validate_Deep_NullPicture() {
        final Show show = getValidatingData(1);
        show.setPicture(null);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_PICTURE_NULL", "Picture mustn't be null.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    public void validate_Deep_NullNote() {
        final Show show = getValidatingData(1);
        show.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null.")));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with null genres.
     */
    @Test
    public void validate_Deep_NullGenres() {
        final Show show = getValidatingData(1);
        show.setGenres(null);

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null.")));

        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with genres with null value.
     */
    @Test
    public void validate_Deep_BadGenres() {
        final Show show = getValidatingData(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")));

        verify(genreValidator).validate(show.getGenres().get(0), ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link ShowValidatorImpl#validate(Movable, ValidationType...)}} with {@link ValidationType#DEEP} with data with genres with genre with
     * invalid data.
     */
    @Test
    public void validate_Deep_GenresWithGenreWithInvalidData() {
        final Event event = new Event(Severity.ERROR, "GENRE_INVALID", "Invalid data");
        final Show show = getValidatingData(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(null)));

        when(genreValidator.validate(any(Genre.class), anyVararg())).thenReturn(Result.error(event.getKey(), event.getMessage()));

        final Result<Void> result = getCatalogValidator().validate(show, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(event));

        for (final Genre genre : show.getGenres()) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
        verifyNoMoreInteractions(genreValidator);
        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Show> getCatalogValidator() {
        return new ShowValidatorImpl(getCatalogService(), genreValidator);
    }

    @Override
    protected Show getValidatingData(final Integer id) {
        return ShowUtils.newShow(id);
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

    @Override
    protected String getPrefix() {
        return "SHOW";
    }

}
