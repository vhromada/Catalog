package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Music;

/**
 * An interface represents validator for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicValidator {

    /**
     * Validates new music.
     *
     * @param music validating music
     * @throws IllegalArgumentException if music is null
     *                                  or ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about music is null
     *                                  or URL to czech Wikipedia page about music is null
     *                                  or count of media isn't positive number
     *                                  or note is null
     */
    void validateNewMusic(Music music);

    /**
     * Validates existing music.
     *
     * @param music validating music
     * @throws IllegalArgumentException if music is null
     *                                  or ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about music is null
     *                                  or URL to czech Wikipedia page about music is null
     *                                  or count of media isn't positive number
     *                                  or note is null
     */
    void validateExistingMusic(Music music);

    /**
     * Validates music with ID.
     *
     * @param music validating music
     * @throws IllegalArgumentException if music is null
     *                                  or ID is null
     */
    void validateMusicWithId(Music music);

}
