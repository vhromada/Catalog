package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
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
 * A class represents implementation of validator for show.
 *
 * @author Vladimir Hromada
 */
@Component("showValidator")
public class ShowValidatorImpl extends AbstractCatalogValidator<Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Validator for genre
     */
    private final CatalogValidator<Genre> genreValidator;

    /**
     * Creates a new instance of ShowValidatorImpl.
     *
     * @param showService    service for shows
     * @param genreValidator validator for genre
     * @throws IllegalArgumentException if service for shows is null
     *                                  or validator for genre is null
     */
    @Autowired
    public ShowValidatorImpl(final CatalogService<cz.vhromada.catalog.domain.Show> showService,
            final CatalogValidator<Genre> genreValidator) {
        super("Show", showService);

        Assert.notNull(genreValidator, "Validator for genre mustn't be null.");

        this.genreValidator = genreValidator;
    }

    /**
     * Validates show deeply.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>Czech name is null</li>
     * <li>Czech name is empty string</li>
     * <li>Original name is null</li>
     * <li>Original name is empty string</li>
     * <li>URL to ČSFD page about show is null</li>
     * <li>IMDB code isn't -1 or between 1 and 9999999</li>
     * <li>URL to english Wikipedia page about show is null</li>
     * <li>URL to czech Wikipedia page about show is null</li>
     * <li>Path to file with show's picture is null</li>
     * <li>Note is null</li>
     * <li>Genres are null</li>
     * <li>Genres contain null value</li>
     * <li>Genre ID is null</li>
     * <li>Genre name is null</li>
     * <li>Genre name is empty string</li>
     * <li>Genre doesn't exist</li>
     * </ul>
     *
     * @param data   validating show
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Show data, final Result<Void> result) {
        validateNames(data, result);
        validateUrls(data, result);
        if (data.getPicture() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_PICTURE_NULL", "Picture mustn't be null."));
        }
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null."));
        }
        validateGenres(data, result);
    }

    /**
     * Validates names.
     * <br/>
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
    private static void validateNames(final Show data, final Result<Void> result) {
        if (data.getCzechName() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getCzechName()) || StringUtils.isEmpty(data.getCzechName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string."));
        }
        if (data.getOriginalName() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getOriginalName()) || StringUtils.isEmpty(data.getOriginalName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string."));
        }
    }

    /**
     * Validates URLs.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>URL to ČSFD page about show is null</li>
     * <li>IMDB code isn't -1 or between 1 and 9999999</li>
     * <li>URL to english Wikipedia page about show is null</li>
     * <li>URL to czech Wikipedia page about show is null</li>
     * </ul>
     *
     * @param data   validating show
     * @param result result with validation errors
     */
    private static void validateUrls(final Show data, final Result<Void> result) {
        if (data.getCsfd() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null."));
        }
        if (data.getImdbCode() != -1 && (data.getImdbCode() < 1 || data.getImdbCode() > Constants.MAX_IMDB_CODE)) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_IMDB_CODE_NOT_VALID", "IMDB code must be between 1 and 9999999 or -1."));
        }
        if (data.getWikiEn() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_WIKI_EN_NULL", "URL to english Wikipedia page about show mustn't be null."));
        }
        if (data.getWikiCz() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL", "URL to czech Wikipedia page about show mustn't be null."));
        }
    }

    /**
     * Validates genres.
     * <br/>
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
     * @param data   validating show
     * @param result result with validation errors
     */
    private void validateGenres(final Show data, final Result<Void> result) {
        if (data.getGenres() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null."));
        } else {
            if (data.getGenres().contains(null)) {
                result.addEvent(new Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value."));
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
