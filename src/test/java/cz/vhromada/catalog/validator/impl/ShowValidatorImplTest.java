package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.utils.TestConstants;
import cz.vhromada.catalog.validator.GenreValidator;
import cz.vhromada.catalog.validator.ShowValidator;

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
     * Initializes validator for show.
     */
    @Before
    public void setUp() {
        showValidator = new ShowValidatorImpl(new GenreValidatorImpl());
    }

    /**
     * Test method for {@link ShowValidatorImpl#ShowValidatorImpl(GenreValidator)} with null validator for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreValidator() {
        new ShowValidatorImpl(null);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullArgument() {
        showValidator.validateNewShow(null);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NotNullId() {
        showValidator.validateNewShow(ShowUtils.newShow(1));
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with null czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullCzechName() {
        final Show show = ShowUtils.newShow(null);
        show.setCzechName(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with empty string as czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_EmptyCzechName() {
        final Show show = ShowUtils.newShow(null);
        show.setCzechName("");

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with null original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullOriginalName() {
        final Show show = ShowUtils.newShow(null);
        show.setOriginalName(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with empty string as original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_EmptyOriginalName() {
        final Show show = ShowUtils.newShow(null);
        show.setOriginalName("");

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with null URL to ČSFD page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullCsfd() {
        final Show show = ShowUtils.newShow(null);
        show.setCsfd(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with bad minimal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_BadMinimalImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with bad divider IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_BadDividerImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setImdbCode(0);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with bad maximal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_BadMaximalImdb() {
        final Show show = ShowUtils.newShow(null);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with null URL to english Wikipedia page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullWikiEn() {
        final Show show = ShowUtils.newShow(null);
        show.setWikiEn(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullWikiCz() {
        final Show show = ShowUtils.newShow(null);
        show.setWikiCz(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with null path to file with show picture.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullPicture() {
        final Show show = ShowUtils.newShow(null);
        show.setPicture(null);
        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullNote() {
        final Show show = ShowUtils.newShow(null);
        show.setNote(null);

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with genres with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_BadGenres() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with genres with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_GenresWithGenreWithNullId() {
        final Show show = ShowUtils.newShow(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateNewShow(Show)} with show with genres with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_GenresWithGenreWithNullName() {
        final Show show = ShowUtils.newShow(null);
        final Genre genre = GenreUtils.newGenre(1);
        genre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), genre));

        showValidator.validateNewShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullArgument() {
        showValidator.validateExistingShow(null);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShow_NullId() {
        showValidator.validateExistingShow(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullCzechName() {
        final Show show = ShowUtils.newShow(1);
        show.setCzechName(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with empty string as czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_EmptyCzechName() {
        final Show show = ShowUtils.newShow(1);
        show.setCzechName("");

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullOriginalName() {
        final Show show = ShowUtils.newShow(1);
        show.setOriginalName(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with empty string as original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_EmptyOriginalName() {
        final Show show = ShowUtils.newShow(1);
        show.setOriginalName("");

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null URL to ČSFD page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullCsfd() {
        final Show show = ShowUtils.newShow(1);
        show.setCsfd(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with bad minimal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_BadMinimalImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with bad divider IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_BadDividerImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(0);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with bad maximal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_BadMaximalImdb() {
        final Show show = ShowUtils.newShow(1);
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null URL to english Wikipedia page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullWikiEn() {
        final Show show = ShowUtils.newShow(1);
        show.setWikiEn(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullWikiCz() {
        final Show show = ShowUtils.newShow(1);
        show.setWikiCz(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null path to file with show picture.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullPicture() {
        final Show show = ShowUtils.newShow(1);
        show.setPicture(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullNote() {
        final Show show = ShowUtils.newShow(1);
        show.setNote(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with null genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_NullGenres() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(null);

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with genres with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_BadGenres() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with genres with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_GenresWithGenreWithNullId() {
        final Show show = ShowUtils.newShow(1);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateExistingShow(Show)} with show with genres with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShow_GenresWithGenreWithNullName() {
        final Show show = ShowUtils.newShow(1);
        final Genre genre = GenreUtils.newGenre(1);
        genre.setName(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), genre));

        showValidator.validateExistingShow(show);
    }

    /**
     * Test method for {@link ShowValidator#validateShowWithId(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateShowWithId_NullArgument() {
        showValidator.validateShowWithId(null);
    }

    /**
     * Test method for {@link ShowValidator#validateShowWithId(Show)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateShowWithId_NullId() {
        showValidator.validateShowWithId(ShowUtils.newShow(null));
    }

}
