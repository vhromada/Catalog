package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.SongTO;
import cz.vhromada.catalog.validator.SongTOValidator;
import cz.vhromada.validators.Validators;

import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for song.
 *
 * @author Vladimir Hromada
 */
@Component("songTOValidator")
public class SongTOValidatorImpl implements SongTOValidator {

    /**
     * TO for song argument
     */
    private static final String SONG_TO_ARGUMENT = "TO for song";

    /**
     * Field ID
     */
    private static final String ID_FIELD = "ID";

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateNewSongTO(final SongTO song) {
        validateSongTO(song);
        Validators.validateNull(song.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateExistingSongTO(final SongTO song) {
        validateSongTO(song);
        Validators.validateNotNull(song.getId(), ID_FIELD);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateSongTOWithId(final SongTO song) {
        Validators.validateArgumentNotNull(song, SONG_TO_ARGUMENT);
        Validators.validateNotNull(song.getId(), ID_FIELD);
    }

    /**
     * Validates TO for song.
     *
     * @param song validating TO for song
     * @throws IllegalArgumentException                              if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if name is null
     *                                                               or name is empty string
     *                                                               or length of song is negative value
     *                                                               or note is null
     */
    private static void validateSongTO(final SongTO song) {
        Validators.validateArgumentNotNull(song, SONG_TO_ARGUMENT);
        Validators.validateNotNull(song.getName(), "Name");
        Validators.validateNotEmptyString(song.getName(), "Name");
        Validators.validateNotNegativeNumber(song.getLength(), "Length");
        Validators.validateNotNull(song.getNote(), "Note");
    }

}
