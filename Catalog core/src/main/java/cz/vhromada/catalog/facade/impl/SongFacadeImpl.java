package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.catalog.facade.validators.SongTOValidator;
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
 * A class represents implementation of service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songFacade")
@Transactional
public class SongFacadeImpl implements SongFacade {

    /** Service for music field */
    private static final String MUSIC_SERVICE_FIELD = "Service for music";

    /** Service for songs field */
    private static final String SONG_SERVICE_FIELD = "Service for songs";

    /** Conversion service field */
    private static final String CONVERSION_SERVICE_FIELD = "Conversion service";

    /** Validator for TO for music field */
    private static final String MUSIC_TO_VALIDATOR_FIELD = "Validator for TO for music";

    /** Validator for TO for song field */
    private static final String SONG_TO_VALIDATOR_FIELD = "Validator for TO for song";

    /** Song argument */
    private static final String SONG_ARGUMENT = "song";

    /** TO for music argument */
    private static final String MUSIC_TO_ARGUMENT = "TO for music";

    /** TO for song argument */
    private static final String SONG_TO_ARGUMENT = "TO for song";

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

    /** Validator for TO for song */
    @Autowired
    private SongTOValidator songTOValidator;

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
     * Returns validator for TO for song.
     *
     * @return validator for TO for song
     */
    public SongTOValidator getSongTOValidator() {
        return songTOValidator;
    }

    /**
     * Sets a new value to validator for TO for song.
     *
     * @param songTOValidator new value
     */
    public void setSongTOValidator(final SongTOValidator songTOValidator) {
        this.songTOValidator = songTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for songs isn't set
     *                                  or conversion service isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SongTO getSong(final Integer id) {
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return conversionService.convert(songService.getSong(id), SongTO.class);
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
     *                                  or validator for TO for song isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final SongTO song) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(songTOValidator, SONG_TO_VALIDATOR_FIELD);
        songTOValidator.validateNewSongTO(song);
        try {
            final Music music = musicService.getMusic(song.getMusic().getId());
            Validators.validateExists(music, MUSIC_TO_ARGUMENT);

            final Song songEntity = conversionService.convert(song, Song.class);
            songEntity.setMusic(music);
            songService.add(songEntity);
            if (songEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            song.setId(songEntity.getId());
            song.setPosition(songEntity.getPosition());
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
     *                                  or validator for TO for song isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void update(final SongTO song) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(songTOValidator, SONG_TO_VALIDATOR_FIELD);
        songTOValidator.validateExistingSongTO(song);
        try {
            final Song songEntity = conversionService.convert(song, Song.class);
            Validators.validateExists(songService.exists(songEntity), SONG_TO_ARGUMENT);
            final Music music = musicService.getMusic(song.getMusic().getId());
            Validators.validateExists(music, MUSIC_TO_ARGUMENT);

            songEntity.setMusic(music);
            songService.update(songEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for songs isn't set
     *                                  or validator for TO for song isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void remove(final SongTO song) {
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(songTOValidator, SONG_TO_VALIDATOR_FIELD);
        songTOValidator.validateSongTOWithId(song);
        try {
            final Song songEntity = songService.getSong(song.getId());
            Validators.validateExists(songEntity, SONG_TO_ARGUMENT);

            songService.remove(songEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for songs isn't set
     *                                  or validator for TO for song isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final SongTO song) {
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(songTOValidator, SONG_TO_VALIDATOR_FIELD);
        songTOValidator.validateSongTOWithId(song);
        try {
            final Song oldSong = songService.getSong(song.getId());
            Validators.validateExists(oldSong, SONG_TO_ARGUMENT);

            songService.duplicate(oldSong);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for songs isn't set
     *                                  or validator for TO for song isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final SongTO song) {
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(songTOValidator, SONG_TO_VALIDATOR_FIELD);
        songTOValidator.validateSongTOWithId(song);
        try {
            final Song songEntity = songService.getSong(song.getId());
            Validators.validateExists(songEntity, SONG_TO_ARGUMENT);
            final List<Song> songs = songService.findSongsByMusic(songEntity.getMusic());
            Validators.validateMoveUp(songs, songEntity, SONG_ARGUMENT);

            songService.moveUp(songEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for songs isn't set
     *                                  or validator for TO for song isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final SongTO song) {
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(songTOValidator, SONG_TO_VALIDATOR_FIELD);
        songTOValidator.validateSongTOWithId(song);
        try {
            final Song songEntity = songService.getSong(song.getId());
            Validators.validateExists(songEntity, SONG_TO_ARGUMENT);
            final List<Song> songs = songService.findSongsByMusic(songEntity.getMusic());
            Validators.validateMoveDown(songs, songEntity, SONG_ARGUMENT);

            songService.moveDown(songEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for songs isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for song isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final SongTO song) {
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(songTOValidator, SONG_TO_VALIDATOR_FIELD);
        songTOValidator.validateSongTOWithId(song);
        try {

            return songService.exists(conversionService.convert(song, Song.class));
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
     *                                  or validator for TO for music isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<SongTO> findSongsByMusic(final MusicTO music) {
        Validators.validateFieldNotNull(musicService, MUSIC_SERVICE_FIELD);
        Validators.validateFieldNotNull(songService, SONG_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_FIELD);
        musicTOValidator.validateMusicTOWithId(music);
        try {
            final Music musicEntity = musicService.getMusic(music.getId());
            Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

            final List<SongTO> songs = new ArrayList<>();
            for (final Song song : songService.findSongsByMusic(musicEntity)) {
                songs.add(conversionService.convert(song, SongTO.class));
            }
            Collections.sort(songs);
            return songs;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}

