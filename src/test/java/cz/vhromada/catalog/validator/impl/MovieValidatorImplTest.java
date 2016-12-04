package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.MediumUtils;
import cz.vhromada.catalog.common.MovieUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.GenreValidator;
import cz.vhromada.catalog.validator.MovieValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MovieValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class MovieValidatorImplTest {

    /**
     * Instance of {@link MovieValidator}
     */
    private MovieValidator movieValidator;

    /**
     * Initializes validator for TO for movie.
     */
    @Before
    public void setUp() {
        movieValidator = new MovieValidatorImpl(new GenreValidatorImpl());
    }

    /**
     * Test method for {@link MovieValidatorImpl#MovieValidatorImpl(GenreValidator)} with null validator for
     * TO for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreTOValidator() {
        new MovieValidatorImpl(null);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovieTO_NullArgument() {
        movieValidator.validateNewMovie(null);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NotNullId() {
        movieValidator.validateNewMovie(MovieUtils.newMovieTO(1));
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullCzechName() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setCzechName(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_EmptyCzechName() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setCzechName("");

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullOriginalName() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setOriginalName(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_EmptyOriginalName() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setOriginalName("");

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad minimum year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMinimumYear() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad maximum year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMaximumYear() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullLanguage() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setLanguage(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullSubtitles() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setSubtitles(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadSubtitles() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullMedia() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setMedia(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with media with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMedia() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), null));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with media with negative value as medium.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_MediaWithBadMedium() {
        final Medium badMedium = MediumUtils.newMediumTO(2);
        badMedium.setLength(-1);
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), badMedium));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to ČSFD page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullCsfd() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setCsfd(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMinimalImdb() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadDividerImdb() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(0);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMaximalImdb() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullWikiEn() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setWikiEn(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullWikiCz() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setWikiCz(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null path to file with movie's picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullPicture() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setPicture(null);
        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullNote() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setNote(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullGenres() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setGenres(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadGenres() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_GenresWithGenreWithNullId() {
        final Movie movie = MovieUtils.newMovieTO(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_GenresWithGenreWithNullName() {
        final Movie movie = MovieUtils.newMovieTO(null);
        final Genre badGenre = GenreUtils.newGenreTO(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), badGenre));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovieTO_NullArgument() {
        movieValidator.validateExistingMovie(null);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullId() {
        movieValidator.validateExistingMovie(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullCzechName() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setCzechName(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_EmptyCzechName() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setCzechName("");

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullOriginalName() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setOriginalName(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_EmptyOriginalName() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setOriginalName("");

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad minimum year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMinimumYear() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad maximum year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMaximumYear() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullLanguage() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setLanguage(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullSubtitles() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setSubtitles(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadSubtitles() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullMedia() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setMedia(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with media with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMedia() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), null));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with media with negative value as medium.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_MediaWithBadMedium() {
        final Medium badMedium = MediumUtils.newMediumTO(2);
        badMedium.setLength(-1);
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), badMedium));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to ČSFD page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullCsfd() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setCsfd(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMinimalImdb() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadDividerImdb() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(0);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMaximalImdb() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullWikiEn() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setWikiEn(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullWikiCz() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setWikiCz(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null path to file with movie's picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullPicture() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setPicture(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullNote() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setNote(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullGenres() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setGenres(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadGenres() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_GenresWithGenreWithNullId() {
        final Movie movie = MovieUtils.newMovieTO(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_GenresWithGenreWithNullName() {
        final Movie movie = MovieUtils.newMovieTO(1);
        final Genre badGenre = GenreUtils.newGenreTO(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), badGenre));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateMovieWithId(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMovieTOWithId_NullArgument() {
        movieValidator.validateMovieWithId(null);
    }

    /**
     * Test method for {@link MovieValidator#validateMovieTOWithId(MovieTO)} with TO for movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateMovieTOWithId_NullId() {
        movieValidator.validateMovieWithId(MovieUtils.newMovieTO(null));
    }

}
