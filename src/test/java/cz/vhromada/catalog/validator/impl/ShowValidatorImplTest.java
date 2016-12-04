package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.common.ShowUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.GenreValidator;
import cz.vhromada.catalog.validator.ShowValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link ShowValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class ShowValidatorImplTest {

    /**
     * Instance of {@link ShowValidator}
     */
    private ShowValidator showValidator;

    /**
     * Initializes validator for TO for show.
     */
    @Before
    public void setUp() {
        showValidator = new ShowValidatorImpl(new GenreValidatorImpl());
    }

    /**
     * Test method for {@link ShowValidatorImpl#ShowValidatorImpl(GenreValidator)} with null validator for
     * TO for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreTOValidator() {
        new ShowValidatorImpl(null);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShowTO_NullArgument() {
        showValidator.validateNewShow(null);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NotNullId() {
        showValidator.validateNewShow(ShowUtils.newShowTO(1));
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullCzechName() {
        final Show show = ShowUtils.newShowTO(null);
        show.setCzechName(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_EmptyCzechName() {
        final Show show = ShowUtils.newShowTO(null);
        show.setCzechName("");

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullOriginalName() {
        final Show show = ShowUtils.newShowTO(null);
        show.setOriginalName(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_EmptyOriginalName() {
        final Show show = ShowUtils.newShowTO(null);
        show.setOriginalName("");

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullCsfd() {
        final Show show = ShowUtils.newShowTO(null);
        show.setCsfd(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_BadMinimalImdb() {
        final Show show = ShowUtils.newShowTO(null);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_BadDividerImdb() {
        final Show show = ShowUtils.newShowTO(null);
        show.setImdbCode(0);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_BadMaximalImdb() {
        final Show show = ShowUtils.newShowTO(null);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullWikiEn() {
        final Show show = ShowUtils.newShowTO(null);
        show.setWikiEn(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullWikiCz() {
        final Show show = ShowUtils.newShowTO(null);
        show.setWikiCz(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullPicture() {
        final Show show = ShowUtils.newShowTO(null);
        show.setPicture(null);
        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullNote() {
        final Show show = ShowUtils.newShowTO(null);
        show.setNote(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_BadGenres() {
        final Show show = ShowUtils.newShowTO(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_GenresWithGenreWithNullId() {
        final Show show = ShowUtils.newShowTO(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_GenresWithGenreWithNullName() {
        final Show show = ShowUtils.newShowTO(null);
        final Genre genre = GenreUtils.newGenreTO(1);
        genre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), genre));

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShowTO_NullArgument() {
        showValidator.validateExistingShow(null);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullId() {
        showValidator.validateExistingShow(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullCzechName() {
        final Show show = ShowUtils.newShowTO(1);
        show.setCzechName(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_EmptyCzechName() {
        final Show show = ShowUtils.newShowTO(1);
        show.setCzechName("");

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullOriginalName() {
        final Show show = ShowUtils.newShowTO(1);
        show.setOriginalName(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_EmptyOriginalName() {
        final Show show = ShowUtils.newShowTO(1);
        show.setOriginalName("");

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullCsfd() {
        final Show show = ShowUtils.newShowTO(1);
        show.setCsfd(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_BadMinimalImdb() {
        final Show show = ShowUtils.newShowTO(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_BadDividerImdb() {
        final Show show = ShowUtils.newShowTO(1);
        show.setImdbCode(0);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_BadMaximalImdb() {
        final Show show = ShowUtils.newShowTO(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullWikiEn() {
        final Show show = ShowUtils.newShowTO(1);
        show.setWikiEn(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullWikiCz() {
        final Show show = ShowUtils.newShowTO(1);
        show.setWikiCz(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullPicture() {
        final Show show = ShowUtils.newShowTO(1);
        show.setPicture(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullNote() {
        final Show show = ShowUtils.newShowTO(1);
        show.setNote(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullGenres() {
        final Show show = ShowUtils.newShowTO(1);
        show.setGenres(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_BadGenres() {
        final Show show = ShowUtils.newShowTO(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_GenresWithGenreWithNullId() {
        final Show show = ShowUtils.newShowTO(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_GenresWithGenreWithNullName() {
        final Show show = ShowUtils.newShowTO(1);
        final Genre genre = GenreUtils.newGenreTO(1);
        genre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), genre));

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateShowWithId(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateShowTOWithId_NullArgument() {
        showValidator.validateShowWithId(null);
    }

    /**
     * Test method for {@link ShowValidator#validateShowTOWithId(ShowTO)} with TO for show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateShowTOWithId_NullId() {
        showValidator.validateShowWithId(ShowUtils.newShowTO(null));
    }

}
