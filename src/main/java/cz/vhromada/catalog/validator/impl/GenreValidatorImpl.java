package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.validator.GenreValidator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreValidator")
public class GenreValidatorImpl implements GenreValidator {

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewGenreTO(final GenreTO genre) {
        validateGenreTO(genre);
        Assert.isNull(genre.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingGenreTO(final GenreTO genre) {
        validateGenreTO(genre);
        Assert.notNull(genre.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateGenreTOWithId(final GenreTO genre) {
        Assert.notNull(genre, "Genre mustn't be null.");
        Assert.notNull(genre.getId(), "ID mustn't be null.");
    }

    /**
     * Validates genre.
     *
     * @param genre validating genre
     * @throws IllegalArgumentException if genre is null
     *                                  or name is null
     *                                  or name is empty string
     */
    private static void validateGenreTO(final GenreTO genre) {
        Assert.notNull(genre, "Genre mustn't be null.");
        Assert.notNull(genre.getName(), "Name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(genre.getName()) && !StringUtils.isEmpty(genre.getName().trim()), "Name mustn't be empty string.");
    }

}
