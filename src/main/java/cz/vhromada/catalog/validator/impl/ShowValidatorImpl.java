package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.Constants;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.ShowValidator;
import cz.vhromada.catalog.validator.common.ValidationType;

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
public class ShowValidatorImpl implements ShowValidator {

    /**
     * Validator for genre
     */
    private final CatalogValidator<Genre> genreValidator;

    /**
     * Creates a new instance of ShowValidatorImpl.
     *
     * @param genreValidator validator for genre
     * @throws IllegalArgumentException if validator for genre is null
     */
    @Autowired
    public ShowValidatorImpl(final CatalogValidator<Genre> genreValidator) {
        Assert.notNull(genreValidator, "Validator for genre mustn't be null.");

        this.genreValidator = genreValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewShow(final Show show) {
        validateShow(show);
        Assert.isNull(show.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingShow(final Show show) {
        validateShow(show);
        Assert.notNull(show.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateShowWithId(final Show show) {
        Assert.notNull(show, "Show mustn't be null.");
        Assert.notNull(show.getId(), "ID mustn't be null.");
    }

    /**
     * Validates show.
     *
     * @param show validating show
     * @throws IllegalArgumentException if show is null
     * @throws IllegalArgumentException if czech name is null
     *                                  or czech name is empty string
     *                                  or original name is null
     *                                  or original name is empty string
     *                                  or URL to ČSFD page about show is null
     *                                  or IMDB code isn't -1 or between 1 and 9999999
     *                                  or URL to english Wikipedia page about show is null
     *                                  or URL to czech Wikipedia page about show is null
     *                                  or path to file with show picture is null
     *                                  or note is null
     *                                  or genres are null
     *                                  or genres contain null value
     *                                  or genre ID is null
     *                                  or genre name is null
     *                                  or genre name is empty string
     */
    private void validateShow(final Show show) {
        Assert.notNull(show, "Movie mustn't be null.");
        Assert.notNull(show.getCzechName(), "Czech name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(show.getCzechName()) && !StringUtils.isEmpty(show.getCzechName().trim()), "Czech name mustn't be empty string.");
        Assert.notNull(show.getOriginalName(), "Original name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(show.getOriginalName()) && !StringUtils.isEmpty(show.getOriginalName().trim()),
                "Original name mustn't be empty string.");
        Assert.notNull(show.getCsfd(), "URL to ČSFD page about show mustn't be null.");
        Assert.isTrue(show.getImdbCode() == -1 || show.getImdbCode() >= 1 && show.getImdbCode() <= Constants.MAX_IMDB_CODE,
                "IMDB code must be between 1 and 9999999 or -1.");
        Assert.notNull(show.getWikiEn(), "URL to english Wikipedia page about show mustn't be null.");
        Assert.notNull(show.getWikiCz(), "URL to czech Wikipedia page about show mustn't be null.");
        Assert.notNull(show.getPicture(), "Path to file with show's picture mustn't be null.");
        Assert.notNull(show.getNote(), "Note mustn't be null.");
        Assert.notNull(show.getGenres(), "Genres mustn't be null.");
        Assert.isTrue(!show.getGenres().contains(null), "Genres mustn't contain null value.");
        for (final Genre genre : show.getGenres()) {
            genreValidator.validate(genre, ValidationType.EXISTS, ValidationType.DEEP);
        }
    }

}
