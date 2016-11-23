package cz.vhromada.catalog.validators.impl;

import cz.vhromada.catalog.entity.GenreTO;
import cz.vhromada.catalog.validators.GenreTOValidator;
import cz.vhromada.validators.Validators;

import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreTOValidator")
public class GenreTOValidatorImpl implements GenreTOValidator {

    /**
     * TO for genre argument
     */
    private static final String GENRE_TO_ARGUMENT = "TO for genre";

    /**
     * Field ID
     */
    private static final String ID_FIELD = "ID";

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateNewGenreTO(final GenreTO genre) {
        validateGenreTO(genre);
        Validators.validateNull(genre.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateExistingGenreTO(final GenreTO genre) {
        validateGenreTO(genre);
        Validators.validateNotNull(genre.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateGenreTOWithId(final GenreTO genre) {
        Validators.validateArgumentNotNull(genre, GENRE_TO_ARGUMENT);
        Validators.validateNotNull(genre.getId(), ID_FIELD);
    }

    /**
     * Validates genre.
     *
     * @param genre validating TO for genre
     * @throws IllegalArgumentException                              if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if name is null
     *                                                               or name is empty string
     */
    private static void validateGenreTO(final GenreTO genre) {
        Validators.validateArgumentNotNull(genre, GENRE_TO_ARGUMENT);
        Validators.validateNotNull(genre.getName(), "Name");
        Validators.validateNotEmptyString(genre.getName(), "Name");
    }

}
