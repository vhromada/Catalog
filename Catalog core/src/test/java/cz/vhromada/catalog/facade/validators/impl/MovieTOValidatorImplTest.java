package cz.vhromada.catalog.facade.validators.impl;

import static org.mockito.Mockito.mock;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.TestConstants;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.facade.validators.MovieTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MovieTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class MovieTOValidatorImplTest extends ObjectGeneratorTest {

    /** Instance of {@link MovieTOValidator} */
    private MovieTOValidator movieTOValidator;

    /** Initializes validator for TO for movie. */
    @Before
    public void setUp() {
        final MovieTOValidatorImpl movieValidator = new MovieTOValidatorImpl();
        movieValidator.setGenreTOValidator(new GenreTOValidatorImpl());
        movieTOValidator = movieValidator;
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
        movieTOValidator.validateNewMovieTO(ToGenerator.newMovieWithId(getObjectGenerator()));
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null czech name. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullCzechName() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setCzechName(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with empty string as czech name. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithEmptyCzechName() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setCzechName("");

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null original name. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullOriginalName() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setOriginalName(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with empty string as original name. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithEmptyOriginalName() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setOriginalName("");

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad minimum year. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithBadMinimumYear() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad maximum year. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithBadMaximumYear() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null language. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullLanguage() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setLanguage(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null subtitles. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullSubtitles() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setSubtitles(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with subtitles with null value. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithBadSubtitles() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setSubtitles(CollectionUtils.newList(generate(Language.class), null));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null media. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullMedia() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setMedia(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with media with null value. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithBadMedia() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setMedia(CollectionUtils.newList(generate(Integer.class), null));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with media with negative value as medium. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithMediaWithBadMedium() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setMedia(CollectionUtils.newList(generate(Integer.class), -1));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to ČSFD page about movie. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullCsfd() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setCsfd(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad minimal IMDB code. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithBadMinimalImdb() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad divider IMDB code. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithBadDividerImdb() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setImdbCode(0);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad maximal IMDB code. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithBadMaximalImdb() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to english Wikipedia page about movie. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullWikiEn() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setWikiEn(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to czech Wikipedia page about movie. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullWikiCz() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setWikiCz(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null path to file with movie's picture. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullPicture() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setPicture(null);
        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null note. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullNote() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setNote(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null genres. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithNullGenres() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setGenres(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with null value. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithBadGenres() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), null));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with genre with null ID. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithGenresWithGenreWithNullId() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        movie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), ToGenerator.newGenre(getObjectGenerator())));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with genre with null name. */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTOWithGenresWithGenreWithNullName() {
        final MovieTO movie = ToGenerator.newMovie(getObjectGenerator());
        final GenreTO badGenre = ToGenerator.newGenreWithId(getObjectGenerator());
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), badGenre));

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
        movieTOValidator.validateExistingMovieTO(ToGenerator.newMovie(getObjectGenerator()));
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null czech name. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullCzechName() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setCzechName(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with empty string as czech name. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithEmptyCzechName() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setCzechName("");

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null original name. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullOriginalName() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setOriginalName(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with empty string as original name. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithEmptyOriginalName() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setOriginalName("");

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad minimum year. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithBadMinimumYear() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad maximum year. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithBadMaximumYear() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null language. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullLanguage() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setLanguage(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null subtitles. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullSubtitles() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setSubtitles(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with subtitles with null value. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithBadSubtitles() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setSubtitles(CollectionUtils.newList(generate(Language.class), null));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null media. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullMedia() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setMedia(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with media with null value. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithBadMedia() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setMedia(CollectionUtils.newList(generate(Integer.class), null));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with media with negative value as medium. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithMediaWithBadMedium() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setMedia(CollectionUtils.newList(generate(Integer.class), -1));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to ČSFD page about movie. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullCsfd() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setCsfd(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad minimal IMDB code. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithBadMinimalImdb() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad divider IMDB code. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithBadDividerImdb() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setImdbCode(0);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad maximal IMDB code. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithBadMaximalImdb() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to english Wikipedia page about movie. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullWikiEn() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setWikiEn(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to czech Wikipedia page about movie. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullWikiCz() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setWikiCz(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null path to file with movie's picture. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullPicture() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setPicture(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null note. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullNote() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setNote(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null genres. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithNullGenres() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setGenres(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with null value. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithBadGenres() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), null));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with genre with null ID. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithGenresWithGenreWithNullId() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        movie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), ToGenerator.newGenre(getObjectGenerator())));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /** Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with genre with null name. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTOWithGenresWithGenreWithNullName() {
        final MovieTO movie = ToGenerator.newMovieWithId(getObjectGenerator());
        final GenreTO badGenre = ToGenerator.newGenreWithId(getObjectGenerator());
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(ToGenerator.newGenreWithId(getObjectGenerator()), badGenre));

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
        movieTOValidator.validateMovieTOWithId(ToGenerator.newMovie(getObjectGenerator()));
    }

}

