package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MediumUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.catalog.utils.TestConstants;
import cz.vhromada.catalog.validator.GenreValidator;
import cz.vhromada.catalog.validator.MovieValidator;

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
     * Initializes validator for movie.
     */
    @Before
    public void setUp() {
        movieValidator = new MovieValidatorImpl(new GenreValidatorImpl());
    }

    /**
     * Test method for {@link MovieValidatorImpl#MovieValidatorImpl(GenreValidator)} with null validator for
     * genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreTOValidator() {
        new MovieValidatorImpl(null);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullArgument() {
        movieValidator.validateNewMovie(null);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NotNullId() {
        movieValidator.validateNewMovie(MovieUtils.newMovie(1));
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullCzechName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setCzechName(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with empty string as czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_EmptyCzechName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setCzechName("");

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullOriginalName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setOriginalName(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with empty string as original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_EmptyOriginalName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setOriginalName("");

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with bad minimum year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_BadMinimumYear() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with bad maximum year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_BadMaximumYear() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null language.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullLanguage() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setLanguage(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null subtitles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullSubtitles() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setSubtitles(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with subtitles with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_BadSubtitles() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullMedia() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with media with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_BadMedia() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with media with negative value as medium.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_MediaWithBadMedium() {
        final Medium badMedium = MediumUtils.newMedium(2);
        badMedium.setLength(-1);
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null URL to ČSFD page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullCsfd() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setCsfd(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with bad minimal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_BadMinimalImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with bad divider IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_BadDividerImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setImdbCode(0);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with bad maximal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_BadMaximalImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullWikiEn() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setWikiEn(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullWikiCz() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setWikiCz(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null path to file with movie's picture.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullPicture() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setPicture(null);
        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullNote() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setNote(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with null genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullGenres() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(null);

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with genres with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_BadGenres() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with genres with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_GenresWithGenreWithNullId() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateNewMovie(Movie)} with movie with genres with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_GenresWithGenreWithNullName() {
        final Movie movie = MovieUtils.newMovie(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        movieValidator.validateNewMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullArgument() {
        movieValidator.validateExistingMovie(null);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovie_NullId() {
        movieValidator.validateExistingMovie(MovieUtils.newMovie(null));
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullCzechName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCzechName(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with empty string as czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_EmptyCzechName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCzechName("");

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullOriginalName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setOriginalName(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with empty string as original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_EmptyOriginalName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setOriginalName("");

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with bad minimum year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_BadMinimumYear() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with bad maximum year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_BadMaximumYear() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null language.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullLanguage() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setLanguage(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null subtitles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullSubtitles() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setSubtitles(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with subtitles with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_BadSubtitles() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullMedia() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with media with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_BadMedia() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with media with negative value as medium.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_MediaWithBadMedium() {
        final Medium badMedium = MediumUtils.newMedium(2);
        badMedium.setLength(-1);
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null URL to ČSFD page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullCsfd() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCsfd(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with bad minimal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_BadMinimalImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with bad divider IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_BadDividerImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(0);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with bad maximal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_BadMaximalImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullWikiEn() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setWikiEn(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullWikiCz() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setWikiCz(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null path to file with movie's picture.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullPicture() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setPicture(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullNote() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setNote(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with null genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_NullGenres() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(null);

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with genres with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_BadGenres() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with genres with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_GenresWithGenreWithNullId() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateExistingMovie(Movie)} with movie with genres with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovie_GenresWithGenreWithNullName() {
        final Movie movie = MovieUtils.newMovie(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        movieValidator.validateExistingMovie(movie);
    }

    /**
     * Test method for {@link MovieValidator#validateMovieWithId(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMovieWithId_NullArgument() {
        movieValidator.validateMovieWithId(null);
    }

    /**
     * Test method for {@link MovieValidator#validateMovieWithId(Movie)} with movie with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMovieWithId_NullId() {
        movieValidator.validateMovieWithId(MovieUtils.newMovie(null));
    }

}
