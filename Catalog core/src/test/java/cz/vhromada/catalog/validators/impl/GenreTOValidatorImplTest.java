package cz.vhromada.catalog.validators.impl;

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.validators.GenreTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GenreTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class GenreTOValidatorImplTest {

    /**
     * Instance of {@link GenreTOValidator}
     */
    private GenreTOValidator genreTOValidator;

    /**
     * Initializes validator for TO for genre.
     */
    @Before
    public void setUp() {
        genreTOValidator = new GenreTOValidatorImpl();
    }

    /**
     * Test method for {@link GenreTOValidator#validateNewGenreTO(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGenreTO_NullArgument() {
        genreTOValidator.validateNewGenreTO(null);
    }

    /**
     * Test method for {@link GenreTOValidator#validateNewGenreTO(GenreTO)} with TO for genre with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_NotNullId() {
        genreTOValidator.validateNewGenreTO(GenreUtils.newGenreTO(1));
    }

    /**
     * Test method for {@link GenreTOValidator#validateNewGenreTO(GenreTO)} with TO for genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_NullName() {
        final GenreTO genre = GenreUtils.newGenreTO(null);
        genre.setName(null);

        genreTOValidator.validateNewGenreTO(genre);
    }

    /**
     * Test method for {@link GenreTOValidator#validateNewGenreTO(GenreTO)} with TO for genre with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_EmptyName() {
        final GenreTO genre = GenreUtils.newGenreTO(null);
        genre.setName("");

        genreTOValidator.validateNewGenreTO(genre);
    }

    /**
     * Test method for {@link GenreTOValidator#validateExistingGenreTO(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGenreTO_NullArgument() {
        genreTOValidator.validateExistingGenreTO(null);
    }

    /**
     * Test method for {@link GenreTOValidator#validateExistingGenreTO(GenreTO)} with TO for genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_NullId() {
        genreTOValidator.validateExistingGenreTO(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreTOValidator#validateExistingGenreTO(GenreTO)} with TO for genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_NullName() {
        final GenreTO genre = GenreUtils.newGenreTO(1);
        genre.setName(null);

        genreTOValidator.validateExistingGenreTO(genre);
    }

    /**
     * Test method for {@link GenreTOValidator#validateExistingGenreTO(GenreTO)} with TO for genre with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_EmptyName() {
        final GenreTO genre = GenreUtils.newGenreTO(1);
        genre.setName("");

        genreTOValidator.validateExistingGenreTO(genre);
    }

    /**
     * Test method for {@link GenreTOValidator#validateGenreTOWithId(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGenreTOWithId_NullArgument() {
        genreTOValidator.validateGenreTOWithId(null);
    }

    /**
     * Test method for {@link GenreTOValidator#validateGenreTOWithId(GenreTO)} with TO for genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateGenreTOWithId_NullId() {
        genreTOValidator.validateGenreTOWithId(new GenreTO());
    }

}
