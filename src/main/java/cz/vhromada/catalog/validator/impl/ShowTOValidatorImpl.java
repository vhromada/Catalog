package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.entity.ShowTO;
import cz.vhromada.catalog.util.CatalogValidators;
import cz.vhromada.catalog.validator.GenreTOValidator;
import cz.vhromada.catalog.validator.ShowTOValidator;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for show.
 *
 * @author Vladimir Hromada
 */
@Component("showTOValidator")
public class ShowTOValidatorImpl implements ShowTOValidator {

    /**
     * TO for show argument
     */
    private static final String SHOW_TO_ARGUMENT = "TO for show";

    /**
     * Field ID
     */
    private static final String ID_FIELD = "ID";

    /**
     * Validator for TO for genre
     */
    private GenreTOValidator genreTOValidator;

    /**
     * Creates a new instance of ShowTOValidatorImpl.
     *
     * @param genreTOValidator validator for TO for genre
     * @throws IllegalArgumentException if validator for TO for genre is null
     */
    @Autowired
    public ShowTOValidatorImpl(final GenreTOValidator genreTOValidator) {
        Validators.validateArgumentNotNull(genreTOValidator, "Validator for TO for genre");

        this.genreTOValidator = genreTOValidator;
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateNewShowTO(final ShowTO show) {
        validateShowTO(show);
        Validators.validateNull(show.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateExistingShowTO(final ShowTO show) {
        validateShowTO(show);
        Validators.validateNotNull(show.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateShowTOWithId(final ShowTO show) {
        Validators.validateArgumentNotNull(show, SHOW_TO_ARGUMENT);
        Validators.validateNotNull(show.getId(), ID_FIELD);
    }

    /**
     * Validates TO for show.
     *
     * @param show validating TO for show
     * @throws IllegalArgumentException                              if TO for show is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if czech name is null
     *                                                               or czech name is empty string
     *                                                               or original name is null
     *                                                               or original name is empty string
     *                                                               or URL to ČSFD page about show is null
     *                                                               or IMDB code isn't -1 or between 1 and 9999999
     *                                                               or URL to english Wikipedia page about show is null
     *                                                               or URL to czech Wikipedia page about show is null
     *                                                               or path to file with show picture is null
     *                                                               or note is null
     *                                                               or genres are null
     *                                                               or genres contain null value
     *                                                               or genre ID is null
     *                                                               or genre name is null
     *                                                               or genre name is empty string
     */
    private void validateShowTO(final ShowTO show) {
        Validators.validateArgumentNotNull(show, SHOW_TO_ARGUMENT);
        Validators.validateNotNull(show.getCzechName(), "Czech name");
        Validators.validateNotEmptyString(show.getCzechName(), "Czech name");
        Validators.validateNotNull(show.getOriginalName(), "Original name");
        Validators.validateNotEmptyString(show.getOriginalName(), "Original name");
        Validators.validateNotNull(show.getCsfd(), "URL to ČSFD page about show");
        CatalogValidators.validateImdbCode(show.getImdbCode(), "IMDB code");
        Validators.validateNotNull(show.getWikiEn(), "URL to english Wikipedia page about show");
        Validators.validateNotNull(show.getWikiCz(), "URL to czech Wikipedia page about show");
        Validators.validateNotNull(show.getPicture(), "Path to file with show picture");
        Validators.validateNotNull(show.getNote(), "Note");
        Validators.validateNotNull(show.getGenres(), "Genres");
        Validators.validateCollectionNotContainNull(show.getGenres(), "Genres");
        for (final GenreTO genre : show.getGenres()) {
            genreTOValidator.validateExistingGenreTO(genre);
        }
    }

}
