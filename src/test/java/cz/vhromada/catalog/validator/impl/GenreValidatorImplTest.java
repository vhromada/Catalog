package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.validator.GenreValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GenreValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class GenreValidatorImplTest {

    /**
     * Instance of {@link GenreValidator}
     */
    private GenreValidator genreValidator;

    /**
     * Initializes validator for TO for genre.
     */
    @Before
    public void setUp() {
        genreValidator = new GenreValidatorImpl();
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenreTO(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGenreTO_NullArgument() {
        genreValidator.validateNewGenreTO(null);
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenreTO(GenreTO)} with TO for genre with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_NotNullId() {
        genreValidator.validateNewGenreTO(GenreUtils.newGenreTO(1));
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenreTO(GenreTO)} with TO for genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_NullName() {
        final GenreTO genre = GenreUtils.newGenreTO(null);
        genre.setName(null);

        genreValidator.validateNewGenreTO(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenreTO(GenreTO)} with TO for genre with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_EmptyName() {
        final GenreTO genre = GenreUtils.newGenreTO(null);
        genre.setName("");

        genreValidator.validateNewGenreTO(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenreTO(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGenreTO_NullArgument() {
        genreValidator.validateExistingGenreTO(null);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenreTO(GenreTO)} with TO for genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_NullId() {
        genreValidator.validateExistingGenreTO(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenreTO(GenreTO)} with TO for genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_NullName() {
        final GenreTO genre = GenreUtils.newGenreTO(1);
        genre.setName(null);

        genreValidator.validateExistingGenreTO(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenreTO(GenreTO)} with TO for genre with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_EmptyName() {
        final GenreTO genre = GenreUtils.newGenreTO(1);
        genre.setName("");

        genreValidator.validateExistingGenreTO(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateGenreTOWithId(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGenreTOWithId_NullArgument() {
        genreValidator.validateGenreTOWithId(null);
    }

    /**
     * Test method for {@link GenreValidator#validateGenreTOWithId(GenreTO)} with TO for genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateGenreTOWithId_NullId() {
        genreValidator.validateGenreTOWithId(new GenreTO());
    }

}
