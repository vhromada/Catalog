package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.catalog.service.MusicService;
import cz.vhromada.catalog.service.SongService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
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

    /** Service for music field */
    private static final String MUSIC_SERVICE_FIELD = "Service for music";

    /** Service for songs field */
    private static final String SONG_SERVICE_FIELD = "Service for songs";

    /** Conversion service field */
    private static final String CONVERSION_SERVICE_FIELD = "Conversion service";

    /** Validator for TO for music field */
    private static final String MUSIC_TO_VALIDATOR_FIELD = "Validator for TO for music";

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
    @Autowired
    private MusicService musicService;

    /** Service for songs */
    @Autowired
    private SongService songService;

    /** Conversion service */
    @Autowired
    @Qualifier("coreConversionService")
    private ConversionService conversionService;

    /** Validator for TO for music */
    @Autowired
    private MusicTOValidator musicTOValidator;

    /**
     * Returns service for music.
     *
     * @return service for music
     */
    public MusicService getMusicService() {
        return musicService;
    }

    /**
     * Sets a new value to service for music.
     *
     * @param musicService new value
     */
    public void setMusicService(final MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * Returns service for songs.
     *
     * @return service for songs
     */
    public SongService getSongService() {
        return songService;
    }

    /**
     * Sets a new value to service for songs.
     *
     * @param songService new value
     */
    public void setSongService(final SongService songService) {
        this.songService = songService;
    }

    /**
     * Returns conversion service.
     *
     * @return conversion service
     */
    public ConversionService getConversionService() {
        return conversionService;
    }

    /**
     * Sets a new value to conversion service.
     *
     * @param conversionService new value
     */
    public void setConversionService(final ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * Returns validator for TO for music.
     *
     * @return validator for TO for music
     */
    public MusicTOValidator getMusicTOValidator() {
        return musicTOValidator;
    }

    /**
     * Sets a new value to validator for TO for music.
     *
     * @param musicTOValidator new value
     */
    public void setMusicTOValidator(final MusicTOValidator musicTOValidator) {
        this.musicTOValidator = musicTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);

        try {
            musicService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     *                                  or service for songs isn't set
     *                                  or conversion service isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MusicTO> getMusic() {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);

        try {
            final List<MusicTO> musicList = new ArrayList<>();
            for (final Music music : musicService.getMusic()) {
                musicList.add(convertMusicToMusicTO(music));
            }
            return musicList;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     *                                  or service for songs isn't set
     *                                  or conversion service isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MusicTO getMusic(final Integer id) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return convertMusicToMusicTO(musicService.getMusic(id));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for music isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final MusicTO music) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_FIELD);
        musicTOValidator.validateNewMusicTO(music);

        try {
            final Music musicEntity = conversionService.convert(music, Music.class);
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
     * @throws IllegalStateException    if service for music isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for music isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void update(final MusicTO music) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_FIELD);
        musicTOValidator.validateExistingMusicTO(music);
        try {
            final Music musicEntity = conversionService.convert(music, Music.class);
            Validators.validateExists(musicService.exists(musicEntity), MUSIC_TO_ARGUMENT);

            musicService.update(musicEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     *                                  or validator for TO for music isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void remove(final MusicTO music) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_FIELD);
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
     * @throws IllegalStateException    if service for music isn't set
     *                                  or validator for TO for music isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final MusicTO music) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_FIELD);
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
     * @throws IllegalStateException    if service for music isn't set
     *                                  or validator for TO for music isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final MusicTO music) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_FIELD);
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
     * @throws IllegalStateException    if service for music isn't set
     *                                  or validator for TO for music isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final MusicTO music) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_FIELD);
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
     * @throws IllegalStateException    if service for music isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for music isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final MusicTO music) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_FIELD);
        musicTOValidator.validateMusicTOWithId(music);
        try {

            return musicService.exists(conversionService.convert(music, Music.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);

        try {
            musicService.updatePositions();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getTotalMediaCount() {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);

        try {
            return musicService.getTotalMediaCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Time getTotalLength() {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);

        try {
            return musicService.getTotalLength();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for music isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getSongsCount() {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);

        try {
            return musicService.getSongsCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * Converts entity music to TO for music.
     *
     * @param music converting entity music
     * @return converted TO for music
     */
    private MusicTO convertMusicToMusicTO(final Music music) {
        final MusicTO musicTO = conversionService.convert(music, MusicTO.class);
        if (musicTO != null) {
            musicTO.setSongsCount(songService.findSongsByMusic(music).size());
            musicTO.setTotalLength(songService.getTotalLengthByMusic(music));
        }
        return musicTO;
    }

}
