package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.Constants;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieValidator")
public class MovieValidatorImpl extends AbstractCatalogValidator<Movie, cz.vhromada.catalog.domain.Movie> {

    /**
     * Validator for genre
     */
    private final CatalogValidator<Genre> genreValidator;

    /**
     * Creates a new instance of MovieValidatorImpl.
     *
     * @param movieService   service for movies
     * @param genreValidator validator for genre
     * @throws IllegalArgumentException if service for movies is null
     *                                  or validator for genre is null
     */
    @Autowired
    public MovieValidatorImpl(final CatalogService<cz.vhromada.catalog.domain.Movie> movieService,
            final CatalogValidator<Genre> genreValidator) {
        super("Movie", movieService);

        Assert.notNull(genreValidator, "Validator for genre mustn't be null.");

        this.genreValidator = genreValidator;
    }

    /**
     * Validates movie deeply.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Czech name is null</li>
     * <li>Czech name is empty string</li>
     * <li>Original name is null</li>
     * <li>Original name is empty string</li>
     * <li>Year isn't between 1940 and current year</li>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * <li>Media are null</li>
     * <li>Media contain null value</li>
     * <li>Length of medium is negative value</li>
     * <li>URL to ČSFD page about movie is null</li>
     * <li>IMDB code isn't -1 or between 1 and 9999999</li>
     * <li>URL to english Wikipedia page about movie is null</li>
     * <li>URL to czech Wikipedia page about movie is null</li>
     * <li>Path to file with movie's picture is null</li>
     * <li>Note is null</li>
     * <li>Genres are null</li>
     * <li>Genres contain null value</li>
     * <li>Genre ID is null</li>
     * <li>Genre name is null</li>
     * <li>Genre name is empty string</li>
     * <li>Genre doesn't exist</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Movie data, final Result<Void> result) {
        validateNames(data, result);
        if (data.getYear() < Constants.MIN_YEAR || data.getYear() > Constants.CURRENT_YEAR) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_YEAR_NOT_VALID", "Year must be between " + Constants.MIN_YEAR + " and "
                    + Constants.CURRENT_YEAR + '.'));
        }
        validateLanguages(data, result);
        validateMedia(data, result);
        validateUrls(data, result);
        if (data.getPicture() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_PICTURE_NULL", "Picture mustn't be null."));
        }
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null."));
        }
        validateGenres(data, result);
    }

    /**
     * Validates names.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private static void validateNames(final Movie data, final Result<Void> result) {
        if (data.getCzechName() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getCzechName()) || StringUtils.isEmpty(data.getCzechName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string."));
        }
        if (data.getOriginalName() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getOriginalName()) || StringUtils.isEmpty(data.getOriginalName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string."));
        }
    }

    /**
     * Validates languages.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Language is null</li>
     * <li>Subtitles are null</li>
     * <li>Subtitles contain null value</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private static void validateLanguages(final Movie data, final Result<Void> result) {
        if (data.getLanguage() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null."));
        }
        if (data.getSubtitles() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null."));
        } else if (data.getSubtitles().contains(null)) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value."));
        }
    }

    /**
     * Validates media.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Media are null</li>
     * <li>Media contain null value</li>
     * <li>Length of medium is negative value</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private static void validateMedia(final Movie data, final Result<Void> result) {
        if (data.getMedia() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null."));
        } else {
            if (data.getMedia().contains(null)) {
                result.addEvent(new Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value."));
            }
            for (final Medium medium : data.getMedia()) {
                if (medium != null && medium.getLength() <= 0) {
                    result.addEvent(new Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number."));
                }
            }
        }
    }

    /**
     * Validates URLs.
     * <br>
     * Validation errors:
     * <ul>
     * <li>URL to ČSFD page about movie is null</li>
     * <li>IMDB code isn't -1 or between 1 and 9999999</li>
     * <li>URL to english Wikipedia page about movie is null</li>
     * <li>URL to czech Wikipedia page about movie is null</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private static void validateUrls(final Movie data, final Result<Void> result) {
        if (data.getCsfd() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null."));
        }
        if (data.getImdbCode() != -1 && (data.getImdbCode() < 1 || data.getImdbCode() > Constants.MAX_IMDB_CODE)) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_IMDB_CODE_NOT_VALID", "IMDB code must be between 1 and 9999999 or -1."));
        }
        if (data.getWikiEn() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL", "URL to english Wikipedia page about movie mustn't be null."));
        }
        if (data.getWikiCz() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL", "URL to czech Wikipedia page about movie mustn't be null."));
        }
    }

    /**
     * Validates genres.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Genres are null</li>
     * <li>Genres contain null value</li>
     * <li>Genre ID is null</li>
     * <li>Genre name is null</li>
     * <li>Genre name is empty string</li>
     * <li>Genre doesn't exist</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private void validateGenres(final Movie data, final Result<Void> result) {
        if (data.getGenres() == null) {
            result.addEvent(new Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null."));
        } else {
            if (data.getGenres().contains(null)) {
                result.addEvent(new Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value."));
            }
            for (final Genre genre : data.getGenres()) {
                if (genre != null) {
                    final Result<Void> validationResult = genreValidator.validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
                    result.addEvents(validationResult.getEvents());
                }
            }
        }
    }

}
