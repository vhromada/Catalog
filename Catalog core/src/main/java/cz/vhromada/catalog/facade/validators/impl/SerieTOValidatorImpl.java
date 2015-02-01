package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.CatalogValidators;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.GenreTOValidator;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for serie.
 *
 * @author Vladimir Hromada
 */
@Component("serieTOValidator")
public class SerieTOValidatorImpl implements SerieTOValidator {

    /** Validator for TO for genre */
    private GenreTOValidator genreTOValidator;

    /**
     * Creates a new instance of SerieTOValidatorImpl.
     *
     * @param genreTOValidator validator for TO for genre
     * @throws IllegalArgumentException if validator for TO for genre is null
     */
    @Autowired
    public SerieTOValidatorImpl(final GenreTOValidator genreTOValidator) {
        Validators.validateArgumentNotNull(genreTOValidator, "Validator for TO for genre");

        this.genreTOValidator = genreTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     */
    @Override
    public void validateNewSerieTO(final SerieTO serie) {
        validateSerieTO(serie);
        Validators.validateNull(serie.getId(), "ID");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     */
    @Override
    public void validateExistingSerieTO(final SerieTO serie) {
        validateSerieTO(serie);
        Validators.validateNotNull(serie.getId(), "ID");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     */
    @Override
    public void validateSerieTOWithId(final SerieTO serie) {
        Validators.validateArgumentNotNull(serie, "TO for serie");
        Validators.validateNotNull(serie.getId(), "ID");
    }

    /**
     * Validates TO for serie.
     *
     * @param serie validating TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or URL to ČSFD page about serie is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about serie is null
     *                                  or URL to czech Wikipedia page about serie is null
     *                                  or path to file with serie's picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    private void validateSerieTO(final SerieTO serie) {
        Validators.validateArgumentNotNull(serie, "TO for serie");
        Validators.validateNotNull(serie.getCzechName(), "Czech name");
        Validators.validateNotEmptyString(serie.getCzechName(), "Czech name");
        Validators.validateNotNull(serie.getOriginalName(), "Original name");
        Validators.validateNotEmptyString(serie.getOriginalName(), "Original name");
        Validators.validateNotNull(serie.getCsfd(), "URL to ČSFD page about serie");
        CatalogValidators.validateImdbCode(serie.getImdbCode(), "IMDB code");
        Validators.validateNotNull(serie.getWikiEn(), "URL to english Wikipedia page about serie");
        Validators.validateNotNull(serie.getWikiCz(), "URL to czech Wikipedia page about serie");
        Validators.validateNotNull(serie.getPicture(), "Path to file with serie's picture");
        Validators.validateNotNull(serie.getNote(), "Note");
        Validators.validateNotNull(serie.getGenres(), "Genres");
        Validators.validateCollectionNotContainNull(serie.getGenres(), "Genres");
        for (final GenreTO genre : serie.getGenres()) {
            genreTOValidator.validateExistingGenreTO(genre);
        }
    }

}
