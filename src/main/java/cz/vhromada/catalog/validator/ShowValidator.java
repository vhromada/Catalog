package cz.vhromada.catalog.validator;

import java.util.ArrayList;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.utils.Constants;
import cz.vhromada.common.validator.AbstractMovableValidator;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents validator for show.
 *
 * @author Vladimir Hromada
 */
@Component("showValidator")
public class ShowValidator extends AbstractMovableValidator<Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Validator for picture
     */
    private final MovableValidator<Picture> pictureValidator;

    /**
     * Validator for genre
     */
    private final MovableValidator<Genre> genreValidator;

    /**
     * Creates a new instance of ShowValidator.
     *
     * @param showService      service for shows
     * @param pictureValidator validator for picture
     * @param genreValidator   validator for genre
     * @throws IllegalArgumentException if service for shows is null
     *                                  or validator for picture is null
     *                                  or validator for genre is null
     */
    @Autowired
    public ShowValidator(final MovableService<cz.vhromada.catalog.domain.Show> showService, final MovableValidator<Picture> pictureValidator,
        final MovableValidator<Genre> genreValidator) {
        super("Show", showService);

        Assert.notNull(genreValidator, "Validator for genre mustn't be null.");

        Assert.notNull(pictureValidator, "Validator for picture mustn't be null.");
        this.pictureValidator = pictureValidator;
        this.genreValidator = genreValidator;
    }

    /**
     * Validates show deeply.
     * <br>
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
     * <li>Note is null</li>
     * <li>Picture doesn't exist</li>
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
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null."));
        }
        validatePicture(data, result);
        validateGenres(data, result);
    }

    /**
     * Validates names.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Czech name is null</li>
     * <li>Czech name is empty string</li>
     * <li>Original name is null</li>
     * <li>Original name is empty string</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    private static void validateNames(final Show data, final Result<Void> result) {
        if (data.getCzechName() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null."));
        } else if (!StringUtils.hasText(data.getCzechName())) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string."));
        }
        if (data.getOriginalName() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null."));
        } else if (!StringUtils.hasText(data.getOriginalName())) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string."));
        }
    }

    /**
     * Validates URLs.
     * <br>
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
     * Validates picture.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Picture doesn't exist</li>
     * </ul>
     *
     * @param data   validating movie
     * @param result result with validation errors
     */
    @SuppressWarnings("Duplicates")
    private void validatePicture(final Show data, final Result<Void> result) {
        if (data.getPicture() != null) {
            final Picture picture = new Picture();
            picture.setId(data.getPicture());
            final Result<Void> validationResult = pictureValidator.validate(picture, ValidationType.EXISTS);
            result.addEvents(validationResult.getEvents());
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
     * @param data   validating show
     * @param result result with validation errors
     */
    private void validateGenres(final Show data, final Result<Void> result) {
        if (data.getGenres() == null) {
            result.addEvent(new Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null."));
        } else {
            if (new ArrayList<>(data.getGenres()).contains(null)) {
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
