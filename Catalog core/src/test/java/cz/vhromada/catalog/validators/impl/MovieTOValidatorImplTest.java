package cz.vhromada.catalog.validators.impl;

import cz.vhromada.catalog.common.CollectionUtils;
import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.MediumUtils;
import cz.vhromada.catalog.common.MovieUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.entity.MediumTO;
import cz.vhromada.catalog.entity.MovieTO;
import cz.vhromada.catalog.validators.MovieTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MovieTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class MovieTOValidatorImplTest {

    /**
     * Instance of {@link MovieTOValidator}
     */
    private MovieTOValidator movieTOValidator;

    /**
     * Initializes validator for TO for movie.
     */
    @Before
    public void setUp() {
        movieTOValidator = new MovieTOValidatorImpl(new GenreTOValidatorImpl());
    }

    /**
     * Test method for {@link MovieTOValidatorImpl#MovieTOValidatorImpl(cz.vhromada.catalog.validators.GenreTOValidator)} with null validator for
     * TO for genre.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreTOValidator() {
        new MovieTOValidatorImpl(null);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewMovieTO_NullArgument() {
        movieTOValidator.validateNewMovieTO(null);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NotNullId() {
        movieTOValidator.validateNewMovieTO(MovieUtils.newMovieTO(1));
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullCzechName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setCzechName(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_EmptyCzechName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setCzechName("");

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullOriginalName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setOriginalName(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_EmptyOriginalName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setOriginalName("");

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad minimum year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMinimumYear() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad maximum year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMaximumYear() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullLanguage() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setLanguage(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullSubtitles() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setSubtitles(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadSubtitles() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullMedia() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setMedia(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with media with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMedia() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), null));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with media with negative value as medium.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_MediaWithBadMedium() {
        final MediumTO badMedium = MediumUtils.newMediumTO(2);
        badMedium.setLength(-1);
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), badMedium));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to ČSFD page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullCsfd() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setCsfd(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMinimalImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadDividerImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(0);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadMaximalImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullWikiEn() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setWikiEn(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullWikiCz() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setWikiCz(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null path to file with movie's picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullPicture() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setPicture(null);
        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullNote() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setNote(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullGenres() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setGenres(null);

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_BadGenres() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_GenresWithGenreWithNullId() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateNewMovieTO(MovieTO)} with TO for movie with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_GenresWithGenreWithNullName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        final GenreTO badGenre = GenreUtils.newGenreTO(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), badGenre));

        movieTOValidator.validateNewMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingMovieTO_NullArgument() {
        movieTOValidator.validateExistingMovieTO(null);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewMovieTO_NullId() {
        movieTOValidator.validateExistingMovieTO(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullCzechName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setCzechName(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_EmptyCzechName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setCzechName("");

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullOriginalName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setOriginalName(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_EmptyOriginalName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setOriginalName("");

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad minimum year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMinimumYear() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad maximum year.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMaximumYear() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null language.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullLanguage() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setLanguage(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullSubtitles() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setSubtitles(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadSubtitles() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullMedia() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setMedia(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with media with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMedia() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), null));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with media with negative value as medium.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_MediaWithBadMedium() {
        final MediumTO badMedium = MediumUtils.newMediumTO(2);
        badMedium.setLength(-1);
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), badMedium));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to ČSFD page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullCsfd() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setCsfd(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMinimalImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadDividerImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(0);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadMaximalImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullWikiEn() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setWikiEn(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullWikiCz() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setWikiCz(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null path to file with movie's picture.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullPicture() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setPicture(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullNote() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setNote(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_NullGenres() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setGenres(null);

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_BadGenres() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_GenresWithGenreWithNullId() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateExistingMovieTO(MovieTO)} with TO for movie with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingMovieTO_GenresWithGenreWithNullName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        final GenreTO badGenre = GenreUtils.newGenreTO(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), badGenre));

        movieTOValidator.validateExistingMovieTO(movie);
    }

    /**
     * Test method for {@link MovieTOValidator#validateMovieTOWithId(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateMovieTOWithId_NullArgument() {
        movieTOValidator.validateMovieTOWithId(null);
    }

    /**
     * Test method for {@link MovieTOValidator#validateMovieTOWithId(MovieTO)} with TO for movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateMovieTOWithId_NullId() {
        movieTOValidator.validateMovieTOWithId(MovieUtils.newMovieTO(null));
    }

}
