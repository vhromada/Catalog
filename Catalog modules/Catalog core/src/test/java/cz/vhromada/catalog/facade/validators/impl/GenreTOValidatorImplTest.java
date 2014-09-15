package cz.vhromada.catalog.facade.validators.impl;

import static cz.vhromada.catalog.common.TestConstants.ID;

import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.validators.GenreTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GenreTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class GenreTOValidatorImplTest {

	/** Instance of {@link GenreTOValidator} */
	private GenreTOValidator genreTOValidator;

	/** Initializes validator for TO for genre. */
	@Before
	public void setUp() {
		genreTOValidator = new GenreTOValidatorImpl();
	}

	/** Test method for {@link GenreTOValidator#validateNewGenreTO(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNewGenreTOWithNullArgument() {
		genreTOValidator.validateNewGenreTO(null);
	}

	/** Test method for {@link GenreTOValidator#validateNewGenreTO(GenreTO)} with TO for genre with not null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewGenreTOWithNotNullId() {
		genreTOValidator.validateNewGenreTO(ToGenerator.createGenre(ID));
	}

	/** Test method for {@link GenreTOValidator#validateNewGenreTO(GenreTO)} with TO for genre with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewGenreTOWithNullName() {
		final GenreTO genre = ToGenerator.createGenre();
		genre.setName(null);

		genreTOValidator.validateNewGenreTO(genre);
	}

	/** Test method for {@link GenreTOValidator#validateNewGenreTO(GenreTO)} with TO for genre with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewGenreTOWithEmptyName() {
		final GenreTO genre = ToGenerator.createGenre();
		genre.setName("");
		genreTOValidator.validateNewGenreTO(genre);
	}

	/** Test method for {@link GenreTOValidator#validateExistingGenreTO(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingGenreTOWithNullArgument() {
		genreTOValidator.validateExistingGenreTO(null);
	}

	/** Test method for {@link GenreTOValidator#validateExistingGenreTO(GenreTO)} with TO for genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingGenreTOWithNullId() {
		genreTOValidator.validateExistingGenreTO(ToGenerator.createGenre());
	}

	/** Test method for {@link GenreTOValidator#validateExistingGenreTO(GenreTO)} with TO for genre with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingGenreTOWithNullName() {
		final GenreTO genre = ToGenerator.createGenre(ID);
		genre.setName(null);

		genreTOValidator.validateExistingGenreTO(genre);
	}

	/** Test method for {@link GenreTOValidator#validateExistingGenreTO(GenreTO)} with TO for genre with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingGenreTOWithEmptyName() {
		final GenreTO genre = ToGenerator.createGenre(ID);
		genre.setName("");

		genreTOValidator.validateExistingGenreTO(genre);
	}

	/** Test method for {@link GenreTOValidator#validateGenreTOWithId(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateGenreTOWithIdWithNullArgument() {
		genreTOValidator.validateGenreTOWithId(null);
	}

	/** Test method for {@link GenreTOValidator#validateGenreTOWithId(GenreTO)} with TO for genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateGenreTOWithIdWithNullId() {
		genreTOValidator.validateGenreTOWithId(ToGenerator.createGenre());
	}

}
