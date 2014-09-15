package cz.vhromada.catalog.facade.validators.impl;

import static cz.vhromada.catalog.common.TestConstants.BAD_MAX_IMDB_CODE;
import static cz.vhromada.catalog.common.TestConstants.BAD_MAX_YEAR;
import static cz.vhromada.catalog.common.TestConstants.BAD_MIN_IMDB_CODE;
import static cz.vhromada.catalog.common.TestConstants.BAD_MIN_YEAR;
import static cz.vhromada.catalog.common.TestConstants.BAD_SUBTITLES;
import static cz.vhromada.catalog.common.TestConstants.ID;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.MEDIUM_1;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_INNER_ID;
import static org.mockito.Mockito.mock;

import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.facade.validators.GenreTOValidator;
import cz.vhromada.catalog.facade.validators.MovieTOValidator;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MovieTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class MovieTOValidatorImplTest {

	/** Instance of {@link MovieTOValidator} */
	private MovieTOValidator movieTOValidator;

	/** Initializes validator for TO for movie. */
	@Before
	public void setUp() {
		final MovieTOValidatorImpl movieValidator = new MovieTOValidatorImpl();
		movieValidator.setGenreTOValidator(new GenreTOValidatorImpl());
		movieTOValidator = movieValidator;
	}

	/** Test method for {@link MovieTOValidatorImpl#getGenreTOValidator()} and {@link MovieTOValidatorImpl#setGenreTOValidator(GenreTOValidator)}. */
	@Test
	public void testGenreValidator() {
		final GenreTOValidator genreTOValidator = mock(GenreTOValidator.class);
		final MovieTOValidatorImpl movieTOValidator = new MovieTOValidatorImpl();
		movieTOValidator.setGenreTOValidator(genreTOValidator);
		DeepAsserts.assertEquals(genreTOValidator, movieTOValidator.getGenreTOValidator());
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testValidateNewMovieTOWithNotSetGenreTOValidator() {
		((MovieTOValidatorImpl) movieTOValidator).setGenreTOValidator(null);
		movieTOValidator.validateNewMovieTO(mock(MovieTO.class));
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateNewMovieTOWithNullArgument() {
		movieTOValidator.validateNewMovieTO(null);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with not null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNotNullId() {
		movieTOValidator.validateNewMovieTO(ToGenerator.createMovie(ID));
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullCzechName() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setCzechName(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithEmptyCzechName() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setCzechName("");

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null original name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullOriginalName() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setOriginalName(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithEmptyOriginalName() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setOriginalName("");

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad minimum year. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithBadMinimumYear() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setYear(BAD_MIN_YEAR);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad maximum year. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithBadMaximumYear() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setYear(BAD_MAX_YEAR);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null language. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullLanguage() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setLanguage(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null subtitles. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullSubtitles() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setSubtitles(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with subtitles with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithBadSubtitles() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setSubtitles(BAD_SUBTITLES);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null media. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullMedia() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setMedia(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with media with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithBadMedia() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setMedia(CollectionUtils.newList(MEDIUM_1, null));

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with media with negative value as medium. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithMediaWithBadMedium() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setMedia(CollectionUtils.newList(MEDIUM_1, -1));

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to ČSFD page about movie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullCsfd() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setCsfd(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithBadMinimalImdb() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setImdbCode(BAD_MIN_IMDB_CODE);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithBadDividerImdb() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setImdbCode(0);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithBadMaximalImdb() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setImdbCode(BAD_MAX_IMDB_CODE);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to english Wikipedia page about movie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullWikiEn() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setWikiEn(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to czech Wikipedia page about movie. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullWikiCz() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setWikiCz(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null path to file with movie's picture. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullPicture() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setPicture(null);
		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullNote() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setNote(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null genres. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullGenres() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setGenres(null);

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithBadGenres() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), null));

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithGenresWithGenreWithNullId() {
		final MovieTO movie = ToGenerator.createMovie();
		movie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), ToGenerator.createGenre()));

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithGenresWithGenreWithNullName() {
		final MovieTO movie = ToGenerator.createMovie();
		final GenreTO badGenre = ToGenerator.createGenre(SECONDARY_INNER_ID);
		badGenre.setName(null);
		movie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), badGenre));

		movieTOValidator.validateNewMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with not set validator for TO for genre. */
	@Test(expected = IllegalStateException.class)
	public void testValidateExistingMovieTOWithNotSetGenreTOValidator() {
		((MovieTOValidatorImpl) movieTOValidator).setGenreTOValidator(null);
		movieTOValidator.validateExistingMovieTO(mock(MovieTO.class));
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateExistingMovieTOWithNullArgument() {
		movieTOValidator.validateExistingMovieTO(null);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateNewMovieTOWithNullId() {
		movieTOValidator.validateExistingMovieTO(ToGenerator.createMovie());
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullCzechName() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setCzechName(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with empty string as czech name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithEmptyCzechName() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setCzechName("");

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null original name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullOriginalName() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setOriginalName(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with empty string as original name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithEmptyOriginalName() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setOriginalName("");

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad minimum year. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithBadMinimumYear() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setYear(BAD_MIN_YEAR);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad maximum year. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithBadMaximumYear() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setYear(BAD_MAX_YEAR);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null language. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullLanguage() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setLanguage(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null subtitles. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullSubtitles() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setSubtitles(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with subtitles with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithBadSubtitles() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setSubtitles(BAD_SUBTITLES);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null media. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullMedia() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setMedia(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with media with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithBadMedia() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setMedia(CollectionUtils.newList(MEDIUM_1, null));

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with media with negative value as medium. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithMediaWithBadMedium() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setMedia(CollectionUtils.newList(MEDIUM_1, -1));

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to ČSFD page about movie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullCsfd() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setCsfd(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad minimal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithBadMinimalImdb() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setImdbCode(BAD_MIN_IMDB_CODE);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad divider IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithBadDividerImdb() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setImdbCode(0);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad maximal IMDB code. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithBadMaximalImdb() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setImdbCode(BAD_MAX_IMDB_CODE);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to english Wikipedia page about movie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullWikiEn() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setWikiEn(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to czech Wikipedia page about movie. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullWikiCz() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setWikiCz(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null path to file with movie's picture. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullPicture() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setPicture(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null note. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullNote() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setNote(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null genres. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithNullGenres() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setGenres(null);

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with null value. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithBadGenres() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), null));

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithGenresWithGenreWithNullId() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		movie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), ToGenerator.createGenre()));

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testValidateExistingMovieTOWithGenresWithGenreWithNullName() {
		final MovieTO movie = ToGenerator.createMovie(ID);
		final GenreTO badGenre = ToGenerator.createGenre(SECONDARY_INNER_ID);
		badGenre.setName(null);
		movie.setGenres(CollectionUtils.newList(ToGenerator.createGenre(INNER_ID), badGenre));

		movieTOValidator.validateExistingMovieTO(movie);
	}

	/** Test method for {@link MovieTOValidator#validateMovieTOWithId(MovieTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testValidateMovieTOWithIdWithNullArgument() {
		movieTOValidator.validateMovieTOWithId(null);
	}

	/** Test method for {@link MovieTOValidator#validateMovieTOWithId(MovieTO)} with TO for movie with null ID. */
	@Test(expected = ValidationException.class)
	public void testValidateMovieTOWithIdWithNullId() {
		movieTOValidator.validateMovieTOWithId(ToGenerator.createMovie());
	}

}

