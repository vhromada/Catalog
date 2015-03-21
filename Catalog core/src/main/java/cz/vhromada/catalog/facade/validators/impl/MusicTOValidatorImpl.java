package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.validators.Validators;

import org.springframework.stereotype.Component;

/**
 * A class represents implementation of validator for TO for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicTOValidator")
public class MusicTOValidatorImpl implements MusicTOValidator {

    /**
     * TO for music argument
     */
    private static final String MUSIC_TO_ARGUMENT = "TO for music";

    /**
     * Field ID
     */
    private static final String ID_FIELD = "ID";

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateNewMusicTO(final MusicTO music) {
        validateMusicTO(music);
        Validators.validateNull(music.getId(), ID_FIELD);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateExistingMusicTO(final MusicTO music) {
        validateMusicTO(music);
        Validators.validateNotNull(music.getId(), ID_FIELD);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void validateMusicTOWithId(final MusicTO music) {
        Validators.validateArgumentNotNull(music, MUSIC_TO_ARGUMENT);
        Validators.validateNotNull(music.getId(), ID_FIELD);
    }

    /**
     * Validates TO for music.
     *
     * @param music validating TO for music
     * @throws IllegalArgumentException                              if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException if name is null
     *                                                               or name is empty string
     *                                                               or URL to english Wikipedia page about music is null
     *                                                               or URL to czech Wikipedia page about music is null
     *                                                               or count of media isn't positive number
     *                                                               or note is null
     */
    private static void validateMusicTO(final MusicTO music) {
        Validators.validateArgumentNotNull(music, MUSIC_TO_ARGUMENT);
        Validators.validateNotNull(music.getName(), "Name");
        Validators.validateNotEmptyString(music.getName(), "Name");
        Validators.validateNotNull(music.getWikiEn(), "URL to english Wikipedia page about music");
        Validators.validateNotNull(music.getWikiCz(), "URL to czech Wikipedia page about music");
        Validators.validatePositiveNumber(music.getMediaCount(), "Count of media");
        Validators.validateNotNull(music.getNote(), "Note");
    }

}
