package cz.vhromada.catalog.facade.validators;

import cz.vhromada.catalog.facade.to.MusicTO;

/**
 * An interface represents validator for TO for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicTOValidator {

    /**
     * Validates new TO for music.
     *
     * @param music validating TO for music
     * @throws IllegalArgumentException if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about music is null
     *                                  or URL to czech Wikipedia page about music is null
     *                                  or count of media isn't positive number
     *                                  or count of songs is negative number
     *                                  or total length of songs is null
     *                                  or total length of songs is negative number
     *                                  or note is null
     */
    void validateNewMusicTO(MusicTO music);

    /**
     * Validates existing TO for music.
     *
     * @param music validating TO for music
     * @throws IllegalArgumentException if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about music is null
     *                                  or URL to czech Wikipedia page about music is null
     *                                  or count of media isn't positive number
     *                                  or count of songs is negative number
     *                                  or total length of songs is null
     *                                  or total length of songs is negative number
     *                                  or note is null
     */
    void validateExistingMusicTO(MusicTO music);

    /**
     * Validates TO for music with ID.
     *
     * @param music validating TO for music
     * @throws IllegalArgumentException if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     */
    void validateMusicTOWithId(MusicTO music);

}
