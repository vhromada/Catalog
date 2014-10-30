package cz.vhromada.catalog.facade.validators.impl;

import static cz.vhromada.catalog.commons.TestConstants.BAD_MAX_IMDB_CODE;
import static cz.vhromada.catalog.commons.TestConstants.BAD_MIN_IMDB_CODE;
import static cz.vhromada.catalog.commons.TestConstants.NEGATIVE_TIME;
import static org.mockito.Mockito.mock;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SerieTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class SerieTOValidatorImplTest extends ObjectGeneratorTest {

	/** Instance of {@link SerieTOValidator} */
	private SerieTOValidator serieTOValidator;

	/** Initializes validator for TO for serie. */
	@Before
	public void setUp() {
		final SerieTOValidatorImpl serieValidator = new SerieTOValidatorImpl();
		serieValidator.setGenreTOValidator(new GenreTOValidatorImpl());
		serieTOValidator = serieValidator;
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testValidateNewSerieTOWithNotSetGenreTOValidator() {
		((SerieTOValidatorImpl) serieTOValidator).setGenreTOValidator(null);
		serieTOValidator.validateNewSerieTO(mock(SerieTO.class));
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNewSerieTOWithNullArgument() {
		serieTOValidator.validateNewSerieTO(null);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with not null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNotNullId() {
		serieTOValidator.validateNewSerieTO(generate(SerieTO.class));
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullCzechName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setCzechName(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithEmptyCzechName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setCzechName("");

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullOriginalName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setOriginalName(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithEmptyOriginalName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setOriginalName("");

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullCsfd() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setCsfd(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithBadMinimalImdb() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setImdbCode(BAD_MIN_IMDB_CODE);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithBadDividerImdb() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setImdbCode(0);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithBadMaximalImdb() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setImdbCode(BAD_MAX_IMDB_CODE);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullWikiEn() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setWikiEn(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullWikiCz() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setWikiCz(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullPicture() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setPicture(null);
		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNegativeSeasonsCount() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setSeasonsCount(-1);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNegativeEpisodesCount() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setEpisodesCount(-1);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullTotalLength() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setTotalLength(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNegativeTotalLength() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setTotalLength(NEGATIVE_TIME);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullNote() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setNote(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithBadGenres() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setGenres(CollectionUtils.newList(generate(GenreTO.class), null));

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithGenresWithGenreWithNullId() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		final GenreTO genre = generate(GenreTO.class);
		genre.setId(null);
		serie.setGenres(CollectionUtils.newList(generate(GenreTO.class), genre));

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithGenresWithGenreWithNullName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		final GenreTO genre = generate(GenreTO.class);
		genre.setName(null);
		serie.setGenres(CollectionUtils.newList(generate(GenreTO.class), genre));

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testValidateExistingSerieTOWithNotSetGenreTOValidator() {
		((SerieTOValidatorImpl) serieTOValidator).setGenreTOValidator(null);
		serieTOValidator.validateExistingSerieTO(mock(SerieTO.class));
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingSerieTOWithNullArgument() {
		serieTOValidator.validateExistingSerieTO(null);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullId() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullCzechName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setCzechName(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithEmptyCzechName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setCzechName("");

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullOriginalName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setOriginalName(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithEmptyOriginalName() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setOriginalName("");

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullCsfd() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setCsfd(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithBadMinimalImdb() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setImdbCode(BAD_MIN_IMDB_CODE);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithBadDividerImdb() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setImdbCode(0);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithBadMaximalImdb() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setImdbCode(BAD_MAX_IMDB_CODE);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullWikiEn() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setWikiEn(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullWikiCz() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setWikiCz(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullPicture() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setPicture(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNegativeSeasonsCount() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setSeasonsCount(-1);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNegativeEpisodesCount() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setEpisodesCount(-1);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullTotalLength() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setTotalLength(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNegativeTotalLength() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);
		serie.setTotalLength(NEGATIVE_TIME);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullNote() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setNote(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null genres. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullGenres() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setGenres(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithBadGenres() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setGenres(CollectionUtils.newList(generate(GenreTO.class), null));

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithGenresWithGenreWithNullId() {
		final SerieTO serie = generate(SerieTO.class);
		final GenreTO genre = generate(GenreTO.class);
		genre.setId(null);
		serie.setGenres(CollectionUtils.newList(generate(GenreTO.class), genre));

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithGenresWithGenreWithNullName() {
		final SerieTO serie = generate(SerieTO.class);
		final GenreTO genre = generate(GenreTO.class);
		genre.setName(null);
		serie.setGenres(CollectionUtils.newList(generate(GenreTO.class), genre));

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateSerieTOWithId(SerieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateSerieTOWithIdWithNullArgument() {
		serieTOValidator.validateSerieTOWithId(null);
	}

	/** Test method for {@link SerieTOValidator#validateSerieTOWithId(SerieTO)} with TO for serie with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateSerieTOWithIdWithNullId() {
		final SerieTO serie = generate(SerieTO.class);
		serie.setId(null);

		serieTOValidator.validateSerieTOWithId(serie);
	}

}
