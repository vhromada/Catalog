package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.GenreTO;

/**
 * An interface represents validator for TO for genre.
 *
 * @author Vladimir Hromada
 */
public interface GenreTOValidator {

    /**
     * Validates new TO for genre.
     *
     * @param genre validating TO for genre
     * @throws IllegalArgumentException if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     */
    void validateNewGenreTO(GenreTO genre);

    /**
     * Validates existing TO for genre.
     *
     * @param genre validating TO for genre
     * @throws IllegalArgumentException if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or name is null
     *                                  or name is empty string
     */
    void validateExistingGenreTO(GenreTO genre);

    /**
     * Validates TO for genre with ID.
     *
     * @param genre validating TO for genre
     * @throws IllegalArgumentException if TO for genre is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     */
    void validateGenreTOWithId(GenreTO genre);

}
