package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Song;

/**
 * An interface represents validator for song.
 *
 * @author Vladimir Hromada
 */
public interface SongValidator {

    /**
     * Validates new song.
     *
     * @param song validating song
     * @throws IllegalArgumentException if song is null
     *                                  or ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or length of song is negative value
     *                                  or note is null
     */
    void validateNewSong(Song song);

    /**
     * Validates existing song.
     *
     * @param song validating song
     * @throws IllegalArgumentException if song is null
     *                                  or ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or length of song is negative value
     *                                  or note is null
     */
    void validateExistingSong(Song song);

    /**
     * Validates song with ID.
     *
     * @param song validating song
     * @throws IllegalArgumentException if song is null
     *                                  or ID is null
     */
    void validateSongWithId(Song song);

}

