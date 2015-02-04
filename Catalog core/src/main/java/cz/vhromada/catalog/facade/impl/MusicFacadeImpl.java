package cz.vhromada.catalog.facade.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.catalog.service.MusicService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicFacade")
@Transactional
public class MusicFacadeImpl implements MusicFacade {

    /** Service for music argument */
    private static final String MUSIC_SERVICE_ARGUMENT = "Service for music";

    /** Converter argument */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /** Validator for TO for music argument */
    private static final String MUSIC_TO_VALIDATOR_ARGUMENT = "Validator for TO for music";

    /** Music argument */
    private static final String MUSIC_ARGUMENT = "music";

    /** TO for music argument */
    private static final String MUSIC_TO_ARGUMENT = "TO for music";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link FacadeOperationException} */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /** Message for not setting ID */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /** Service for music */
    private MusicService musicService;

    /** Converter */
    private Converter converter;

    /** Validator for TO for music */
    private MusicTOValidator musicTOValidator;

    /**
     * Creates a new instance of MusicFacadeImpl.
     *
     * @param musicService service for music
     * @param converter converter
     * @param musicTOValidator validator for TO for music
     * @throws IllegalArgumentException if service for music is null
     *                                  or converter is null
     *                                  or validator for TO for music is null
     */
    @Autowired
    public MusicFacadeImpl(final MusicService musicService,
            final Converter converter,
            final MusicTOValidator musicTOValidator) {
        Validators.validateArgumentNotNull(musicService, MUSIC_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_ARGUMENT);

        this.musicService = musicService;
        this.converter = converter;
        this.musicTOValidator = musicTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            musicService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MusicTO> getMusic() {
        try {
            final List<MusicTO> musicList = converter.convertCollection(musicService.getMusic(), MusicTO.class);
            Collections.sort(musicList);
            return musicList;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MusicTO getMusic(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(musicService.getMusic(id), MusicTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final MusicTO music) {
        musicTOValidator.validateNewMusicTO(music);

        try {
            final Music musicEntity = converter.convert(music, Music.class);
            musicService.add(musicEntity);
            if (musicEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            music.setId(musicEntity.getId());
            music.setPosition(musicEntity.getPosition());
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void update(final MusicTO music) {
        musicTOValidator.validateExistingMusicTO(music);
        try {
            final Music musicEntity = converter.convert(music, Music.class);
            Validators.validateExists(musicService.exists(musicEntity), MUSIC_TO_ARGUMENT);

            musicService.update(musicEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void remove(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        try {
            final Music musicEntity = musicService.getMusic(music.getId());
            Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

            musicService.remove(musicEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        try {
            final Music oldMusic = musicService.getMusic(music.getId());
            Validators.validateExists(oldMusic, MUSIC_TO_ARGUMENT);

            musicService.duplicate(oldMusic);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        try {
            final Music musicEntity = musicService.getMusic(music.getId());
            Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);
            final List<Music> musicList = musicService.getMusic();
            Validators.validateMoveUp(musicList, musicEntity, MUSIC_ARGUMENT);

            musicService.moveUp(musicEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        try {
            final Music musicEntity = musicService.getMusic(music.getId());
            Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);
            final List<Music> musicList = musicService.getMusic();
            Validators.validateMoveDown(musicList, musicEntity, MUSIC_ARGUMENT);

            musicService.moveDown(musicEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        try {

            return musicService.exists(converter.convert(music, Music.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            musicService.updatePositions();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getTotalMediaCount() {
        try {
            return musicService.getTotalMediaCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Time getTotalLength() {
        try {
            return musicService.getTotalLength();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getSongsCount() {
        try {
            return musicService.getSongsCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
