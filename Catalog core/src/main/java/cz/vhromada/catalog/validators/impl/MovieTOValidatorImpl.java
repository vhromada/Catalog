package cz.vhromada.catalog.validators.impl;

import cz.vhromada.catalog.common.CatalogValidators;
import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.entity.MediumTO;
import cz.vhromada.catalog.entity.MovieTO;
import cz.vhromada.catalog.validators.GenreTOValidator;
import cz.vhromada.catalog.validators.MovieTOValidator;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieTOValidator")
public class MovieTOValidatorImpl implements MovieTOValidator {

    /**
     * TO for movie argument
     */
    private static final String MOVIE_TO_ARGUMENT = "TO for movie";

    /**
     * Field ID
     */
    private static final String ID_FIELD = "ID";

    /**
     * Validator for TO for genre
     */
    private GenreTOValidator genreTOValidator;

    /**
     * Creates a new instance of MovieTOValidatorImpl.
     *
     * @param genreTOValidator validator for TO for genre
     * @throws IllegalArgumentException if validator for TO for genre is null
     */
    @Autowired
    public MovieTOValidatorImpl(final GenreTOValidator genreTOValidator) {
        Validators.validateArgumentNotNull(genreTOValidator, "Validator for TO for genre");

        this.genreTOValidator = genreTOValidator;
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateNewMovieTO(final MovieTO movie) {
        validateMovieTO(movie);
        Validators.validateNull(movie.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateExistingMovieTO(final MovieTO movie) {
        validateMovieTO(movie);
        Validators.validateNotNull(movie.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateMovieTOWithId(final MovieTO movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_TO_ARGUMENT);
        Validators.validateNotNull(movie.getId(), ID_FIELD);
    }

    /**
     * Validates TO for movie.
     *
     * @param movie validating TO for movie
     * @throws IllegalArgumentException                              if TO for movie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if czech name is null
     *                                                               or czech name is empty string
     *                                                               or original name is null
     *                                                               or original name is empty string
     *                                                               or year isn't between 1940 and current year
     *                                                               or language is null
     *                                                               or subtitles are null
     *                                                               or subtitles contain null value
     *                                                               or media are null
     *                                                               or media contain null value
     *                                                               or media contain negative value
     *                                                               or URL to ČSFD page about movie is null
     *                                                               or IMDB code isn't -1 or between 1 and 9999999
     *                                                               or URL to english Wikipedia page about movie is null
     *                                                               or URL to czech Wikipedia page about movie is null
     *                                                               or path to file with movie's picture is null
     *                                                               or note is null
     *                                                               or genres are null
     *                                                               or genres contain null value
     *                                                               or genre ID is null
     *                                                               or genre name is null
     *                                                               or genre name is empty string
     */
    private void validateMovieTO(final MovieTO movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_TO_ARGUMENT);
        Validators.validateNotNull(movie.getCzechName(), "Czech name");
        Validators.validateNotEmptyString(movie.getCzechName(), "Czech name");
        Validators.validateNotNull(movie.getOriginalName(), "Original name");
        Validators.validateNotEmptyString(movie.getOriginalName(), "Original name");
        CatalogValidators.validateYear(movie.getYear(), "Year");
        Validators.validateNotNull(movie.getLanguage(), "Language");
        Validators.validateNotNull(movie.getSubtitles(), "Subtitles");
        Validators.validateCollectionNotContainNull(movie.getSubtitles(), "Subtitles");
        final String mediaField = "Media";
        Validators.validateNotNull(movie.getMedia(), mediaField);
        Validators.validateCollectionNotContainNull(movie.getMedia(), mediaField);
        for (final MediumTO medium : movie.getMedia()) {
            Validators.validateNotNegativeNumber(medium.getLength(), mediaField);
        }
        Validators.validateNotNull(movie.getCsfd(), "URL to ČSFD page about movie");
        CatalogValidators.validateImdbCode(movie.getImdbCode(), "IMDB code");
        Validators.validateNotNull(movie.getWikiEn(), "URL to english Wikipedia page about movie");
        Validators.validateNotNull(movie.getWikiCz(), "URL to czech Wikipedia page about movie");
        Validators.validateNotNull(movie.getPicture(), "Path to file with movie's picture");
        Validators.validateNotNull(movie.getNote(), "Note");
        Validators.validateNotNull(movie.getGenres(), "Genres");
        Validators.validateCollectionNotContainNull(movie.getGenres(), "Genres");
        for (final GenreTO genre : movie.getGenres()) {
            genreTOValidator.validateExistingGenreTO(genre);
        }
    }

}
