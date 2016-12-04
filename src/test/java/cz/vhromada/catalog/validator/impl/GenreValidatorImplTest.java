package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.entity.Genre;
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
     * Test method for {@link GenreValidator#validateNewGenre(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGenreTO_NullArgument() {
        genreValidator.validateNewGenre(null);
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenre(GenreTO)} with TO for genre with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_NotNullId() {
        genreValidator.validateNewGenre(GenreUtils.newGenreTO(1));
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenre(GenreTO)} with TO for genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_NullName() {
        final Genre genre = GenreUtils.newGenreTO(null);
        genre.setName(null);

        genreValidator.validateNewGenre(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenre(GenreTO)} with TO for genre with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGenreTO_EmptyName() {
        final Genre genre = GenreUtils.newGenreTO(null);
        genre.setName("");

        genreValidator.validateNewGenre(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenre(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGenreTO_NullArgument() {
        genreValidator.validateExistingGenre(null);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenreTO(GenreTO)} with TO for genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_NullId() {
        genreValidator.validateExistingGenre(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenreTO(GenreTO)} with TO for genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_NullName() {
        final Genre genre = GenreUtils.newGenreTO(1);
        genre.setName(null);

        genreValidator.validateExistingGenre(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenreTO(GenreTO)} with TO for genre with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGenreTO_EmptyName() {
        final Genre genre = GenreUtils.newGenreTO(1);
        genre.setName("");

        genreValidator.validateExistingGenre(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateGenreWithId(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGenreTOWithId_NullArgument() {
        genreValidator.validateGenreWithId(null);
    }

    /**
     * Test method for {@link GenreValidator#validateGenreTOWithId(GenreTO)} with TO for genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateGenreTOWithId_NullId() {
        genreValidator.validateGenreWithId(new Genre());
    }

}
