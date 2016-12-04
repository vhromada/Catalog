package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.validator.GenreValidator;

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
     * Initializes validator for genre.
     */
    @Before
    public void setUp() {
        genreValidator = new GenreValidatorImpl();
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenre(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGenre_NullArgument() {
        genreValidator.validateNewGenre(null);
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenre(Genre)} with genre with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGenre_NotNullId() {
        genreValidator.validateNewGenre(GenreUtils.newGenre(1));
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenre(Genre)} with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGenre_NullName() {
        final Genre genre = GenreUtils.newGenre(null);
        genre.setName(null);

        genreValidator.validateNewGenre(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateNewGenre(Genre)} with genre with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGenre_EmptyName() {
        final Genre genre = GenreUtils.newGenre(null);
        genre.setName("");

        genreValidator.validateNewGenre(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenre(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGenre_NullArgument() {
        genreValidator.validateExistingGenre(null);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenre(Genre)} with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGenre_NullId() {
        genreValidator.validateExistingGenre(GenreUtils.newGenre(null));
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenre(Genre)} with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGenre_NullName() {
        final Genre genre = GenreUtils.newGenre(1);
        genre.setName(null);

        genreValidator.validateExistingGenre(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateExistingGenre(Genre)} with genre with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGenre_EmptyName() {
        final Genre genre = GenreUtils.newGenre(1);
        genre.setName("");

        genreValidator.validateExistingGenre(genre);
    }

    /**
     * Test method for {@link GenreValidator#validateGenreWithId(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGenreWithId_NullArgument() {
        genreValidator.validateGenreWithId(null);
    }

    /**
     * Test method for {@link GenreValidator#validateGenreWithId(Genre)} with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGenreWithId_NullId() {
        genreValidator.validateGenreWithId(new Genre());
    }

}
