package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.SongTO;

/**
 * An interface represents validator for TO for song.
 *
 * @author Vladimir Hromada
 */
public interface SongTOValidator {

    /**
     * Validates new TO for song.
     *
     * @param song validating TO for song
     * @throws IllegalArgumentException                              if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID isn't null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or length of song is negative value
     *                                                               or note is null
     */
    void validateNewSongTO(SongTO song);

    /**
     * Validates existing TO for song.
     *
     * @param song validating TO for song
     * @throws IllegalArgumentException                              if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     *                                                               or name is null
     *                                                               or name is empty string
     *                                                               or length of song is negative value
     *                                                               or note is null
     */
    void validateExistingSongTO(SongTO song);

    /**
     * Validates TO for song with ID.
     *
     * @param song validating TO for song
     * @throws IllegalArgumentException                              if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if ID is null
     */
    void validateSongTOWithId(SongTO song);

}

