package cz.vhromada.catalog.facade.validators.impl;

import static org.mockito.Mockito.mock;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.TestConstants;
import cz.vhromada.catalog.commons.ToGenerator;
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
		serieTOValidator.validateNewSerieTO(ToGenerator.newSerieWithId(getObjectGenerator()));
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullCzechName() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setCzechName(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithEmptyCzechName() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setCzechName("");

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullOriginalName() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setOriginalName(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithEmptyOriginalName() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setOriginalName("");

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullCsfd() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setCsfd(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithBadMinimalImdb() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithBadDividerImdb() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setImdbCode(0);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithBadMaximalImdb() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullWikiEn() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setWikiEn(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullWikiCz() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setWikiCz(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullPicture() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setPicture(null);
		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNegativeSeasonsCount() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setSeasonsCount(-1);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNegativeEpisodesCount() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setEpisodesCount(-1);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullTotalLength() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setTotalLength(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNegativeTotalLength() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setTotalLength(TestConstants.NEGATIVE_TIME);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithNullNote() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setNote(null);

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithBadGenres() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), null));

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithGenresWithGenreWithNullId() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		serie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), ToGenerator.newGenre(getObjectGenerator())));

		serieTOValidator.validateNewSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateNewSerieTO(SerieTO)} with TO for serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewSerieTOWithGenresWithGenreWithNullName() {
		final SerieTO serie = ToGenerator.newSerie(getObjectGenerator());
		final GenreTO genre = ToGenerator.newGenreWithId(getObjectGenerator());
		genre.setName(null);
		serie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), genre));

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
		serieTOValidator.validateExistingSerieTO(ToGenerator.newSerie(getObjectGenerator()));
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullCzechName() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setCzechName(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithEmptyCzechName() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setCzechName("");

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null original name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullOriginalName() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setOriginalName(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithEmptyOriginalName() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setOriginalName("");

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null URL to ČSFD page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullCsfd() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setCsfd(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithBadMinimalImdb() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithBadDividerImdb() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setImdbCode(0);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithBadMaximalImdb() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null URL to english Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullWikiEn() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setWikiEn(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null URL to czech Wikipedia page about serie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullWikiCz() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setWikiCz(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null path to file with serie's picture. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullPicture() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setPicture(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with negative count of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNegativeSeasonsCount() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setSeasonsCount(-1);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with negative count of episodes. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNegativeEpisodesCount() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setEpisodesCount(-1);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullTotalLength() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setTotalLength(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with negative total length of seasons. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNegativeTotalLength() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setTotalLength(TestConstants.NEGATIVE_TIME);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullNote() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setNote(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with null genres. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithNullGenres() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setGenres(null);

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithBadGenres() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), null));

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithGenresWithGenreWithNullId() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		serie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), ToGenerator.newGenre(getObjectGenerator())));

		serieTOValidator.validateExistingSerieTO(serie);
	}

	/** Test method for {@link SerieTOValidator#validateExistingSerieTO(SerieTO)} with TO for serie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingSerieTOWithGenresWithGenreWithNullName() {
		final SerieTO serie = ToGenerator.newSerieWithId(getObjectGenerator());
		final GenreTO genre = ToGenerator.newGenreWithId(getObjectGenerator());
		genre.setName(null);
		serie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), genre));

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
		serieTOValidator.validateSerieTOWithId(ToGenerator.newSerie(getObjectGenerator()));
	}

}
