package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.common.ShowUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.entity.ShowTO;
import cz.vhromada.catalog.util.CollectionUtils;
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
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShowTO_NullArgument() {
        showValidator.validateNewShowTO(null);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NotNullId() {
        showValidator.validateNewShowTO(ShowUtils.newShowTO(1));
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullCzechName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setCzechName(null);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_EmptyCzechName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setCzechName("");

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullOriginalName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setOriginalName(null);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_EmptyOriginalName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setOriginalName("");

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullCsfd() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setCsfd(null);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_BadMinimalImdb() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_BadDividerImdb() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setImdbCode(0);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_BadMaximalImdb() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullWikiEn() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setWikiEn(null);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullWikiCz() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setWikiCz(null);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullPicture() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setPicture(null);
        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullNote() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setNote(null);

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_BadGenres() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_GenresWithGenreWithNullId() {
        final ShowTO show = ShowUtils.newShowTO(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShowTO(ShowTO)} with TO for show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_GenresWithGenreWithNullName() {
        final ShowTO show = ShowUtils.newShowTO(null);
        final GenreTO genre = GenreUtils.newGenreTO(1);
        genre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), genre));

        showValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShowTO_NullArgument() {
        showValidator.validateExistingShowTO(null);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTO_NullId() {
        showValidator.validateExistingShowTO(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullCzechName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setCzechName(null);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_EmptyCzechName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setCzechName("");

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullOriginalName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setOriginalName(null);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_EmptyOriginalName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setOriginalName("");

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullCsfd() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setCsfd(null);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_BadMinimalImdb() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_BadDividerImdb() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setImdbCode(0);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_BadMaximalImdb() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullWikiEn() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setWikiEn(null);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullWikiCz() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setWikiCz(null);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullPicture() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setPicture(null);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullNote() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setNote(null);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_NullGenres() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setGenres(null);

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_BadGenres() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_GenresWithGenreWithNullId() {
        final ShowTO show = ShowUtils.newShowTO(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTO_GenresWithGenreWithNullName() {
        final ShowTO show = ShowUtils.newShowTO(1);
        final GenreTO genre = GenreUtils.newGenreTO(1);
        genre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), genre));

        showValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowValidator#validateShowTOWithId(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateShowTOWithId_NullArgument() {
        showValidator.validateShowTOWithId(null);
    }

    /**
     * Test method for {@link ShowValidator#validateShowTOWithId(ShowTO)} with TO for show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateShowTOWithId_NullId() {
        showValidator.validateShowTOWithId(ShowUtils.newShowTO(null));
    }

}
