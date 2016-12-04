package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.GenreTO;

/**
 * An interface represents validator for genre.
 *
 * @author Vladimir Hromada
 */
public interface GenreValidator {

    /**
     * Validates new genre.
     *
     * @param genre validating genre
     * @throws IllegalArgumentException if genre is null
     *                                  or ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     */
    void validateNewGenreTO(GenreTO genre);

    /**
     * Validates existing genre.
     *
     * @param genre validating genre
     * @throws IllegalArgumentException if genre is null
     *                                  or ID is null
     *                                  or name is null
     *                                  or name is empty string
     */
    void validateExistingGenreTO(GenreTO genre);

    /**
     * Validates genre with ID.
     *
     * @param genre validating genre
     * @throws IllegalArgumentException if genre is null
     *                                  or ID is null
     */
    void validateGenreTOWithId(GenreTO genre);

}
