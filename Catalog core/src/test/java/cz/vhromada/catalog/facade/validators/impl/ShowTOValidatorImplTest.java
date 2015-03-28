package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.TestConstants;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.ShowTO;
import cz.vhromada.catalog.facade.validators.ShowTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link ShowTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class ShowTOValidatorImplTest extends ObjectGeneratorTest {

    /**
     * Instance of {@link ShowTOValidator}
     */
    private ShowTOValidator showTOValidator;

    /**
     * Initializes validator for TO for show.
     */
    @Before
    public void setUp() {
        showTOValidator = new ShowTOValidatorImpl(new GenreTOValidatorImpl());
    }

    /**
     * Test method for {@link ShowTOValidatorImpl#ShowTOValidatorImpl(cz.vhromada.catalog.facade.validators.GenreTOValidator)} with null validator for
     * TO for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenreTOValidator() {
        new ShowTOValidatorImpl(null);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewShowTOWithNullArgument() {
        showTOValidator.validateNewShowTO(null);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNotNullId() {
        showTOValidator.validateNewShowTO(ToGenerator.newShowWithId(getObjectGenerator()));
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNullCzechName() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setCzechName(null);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithEmptyCzechName() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setCzechName("");

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNullOriginalName() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setOriginalName(null);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithEmptyOriginalName() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setOriginalName("");

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNullCsfd() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setCsfd(null);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithBadMinimalImdb() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithBadDividerImdb() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setImdbCode(0);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithBadMaximalImdb() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNullWikiEn() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setWikiEn(null);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNullWikiCz() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setWikiCz(null);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNullPicture() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setPicture(null);
        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNullNote() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setNote(null);

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithBadGenres() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), null));

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithGenresWithGenreWithNullId() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        show.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), ToGenerator.newGenre(getObjectGenerator())));

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateNewShowTO(ShowTO)} with TO for show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithGenresWithGenreWithNullName() {
        final ShowTO show = ToGenerator.newShow(getObjectGenerator());
        final GenreTO genre = ToGenerator.newGenreWithId(getObjectGenerator());
        genre.setName(null);
        show.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), genre));

        showTOValidator.validateNewShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingShowTOWithNullArgument() {
        showTOValidator.validateExistingShowTO(null);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewShowTOWithNullId() {
        showTOValidator.validateExistingShowTO(ToGenerator.newShow(getObjectGenerator()));
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithNullCzechName() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setCzechName(null);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithEmptyCzechName() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setCzechName("");

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithNullOriginalName() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setOriginalName(null);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithEmptyOriginalName() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setOriginalName("");

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to ČSFD page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithNullCsfd() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setCsfd(null);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithBadMinimalImdb() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithBadDividerImdb() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setImdbCode(0);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithBadMaximalImdb() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to english Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithNullWikiEn() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setWikiEn(null);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null URL to czech Wikipedia page about show.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithNullWikiCz() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setWikiCz(null);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null path to file with show picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithNullPicture() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setPicture(null);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithNullNote() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setNote(null);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithNullGenres() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setGenres(null);

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithBadGenres() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), null));

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithGenresWithGenreWithNullId() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        show.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), ToGenerator.newGenre(getObjectGenerator())));

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateExistingShowTO(ShowTO)} with TO for show with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingShowTOWithGenresWithGenreWithNullName() {
        final ShowTO show = ToGenerator.newShowWithId(getObjectGenerator());
        final GenreTO genre = ToGenerator.newGenreWithId(getObjectGenerator());
        genre.setName(null);
        show.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), genre));

        showTOValidator.validateExistingShowTO(show);
    }

    /**
     * Test method for {@link ShowTOValidator#validateShowTOWithId(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateShowTOWithIdWithNullArgument() {
        showTOValidator.validateShowTOWithId(null);
    }

    /**
     * Test method for {@link ShowTOValidator#validateShowTOWithId(ShowTO)} with TO for show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateShowTOWithIdWithNullId() {
        showTOValidator.validateShowTOWithId(ToGenerator.newShow(getObjectGenerator()));
    }

}
