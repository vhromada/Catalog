package cz.vhromada.catalog.facade.impl;

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
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Service for music argument
     */
    private static final String MUSIC_SERVICE_ARGUMENT = "Service for music";

    /**
     * Service for songs argument
     */
    private static final String SONG_SERVICE_ARGUMENT = "Service for songs";

    /**
     * Converter argument
     */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /**
     * Validator for TO for music argument
     */
    private static final String MUSIC_TO_VALIDATOR_ARGUMENT = "Validator for TO for music";

    /**
     * Validator for TO for song argument
     */
    private static final String SONG_TO_VALIDATOR_ARGUMENT = "Validator for TO for song";

    /**
     * Song argument
     */
    private static final String SONG_ARGUMENT = "song";

    /**
     * TO for music argument
     */
    private static final String MUSIC_TO_ARGUMENT = "TO for music";

    /**
     * TO for song argument
     */
    private static final String SONG_TO_ARGUMENT = "TO for song";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link FacadeOperationException}
     */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /**
     * Message for not setting ID
     */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /**
     * Service for music
     */
    private MusicService musicService;

    /**
     * Service for songs
     */
    private SongService songService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for music
     */
    private MusicTOValidator musicTOValidator;

    /**
     * Validator for TO for song
     */
    private SongTOValidator songTOValidator;

    /**
     * Creates a new instance of SongFacadeImpl.
     *
     * @param musicService     service for music
     * @param songService      service for songs
     * @param converter        converter
     * @param musicTOValidator validator for TO for music
     * @param songTOValidator  validator for TO for song
     * @throws IllegalArgumentException if service for music is null
     *                                  or service for songs is null
     *                                  or converter is null
     *                                  or validator for TO for music is null
     *                                  or validator for TO for song is null
     */
    @Autowired
    public SongFacadeImpl(final MusicService musicService,
            final SongService songService,
            final Converter converter,
            final MusicTOValidator musicTOValidator,
            final SongTOValidator songTOValidator) {
        Validators.validateArgumentNotNull(musicService, MUSIC_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(songService, SONG_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(musicTOValidator, MUSIC_TO_VALIDATOR_ARGUMENT);
        Validators.validateArgumentNotNull(songTOValidator, SONG_TO_VALIDATOR_ARGUMENT);

        this.musicService = musicService;
        this.songService = songService;
        this.converter = converter;
        this.musicTOValidator = musicTOValidator;
        this.songTOValidator = songTOValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SongTO getSong(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(songService.getSong(id), SongTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void add(final SongTO song) {
        songTOValidator.validateNewSongTO(song);
        try {
            final Music music = musicService.getMusic(song.getMusic().getId());
            Validators.validateExists(music, MUSIC_TO_ARGUMENT);

            final Song songEntity = converter.convert(song, Song.class);
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void update(final SongTO song) {
        songTOValidator.validateExistingSongTO(song);
        try {
            final Song songEntity = converter.convert(song, Song.class);
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void remove(final SongTO song) {
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void duplicate(final SongTO song) {
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveUp(final SongTO song) {
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveDown(final SongTO song) {
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
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final SongTO song) {
        songTOValidator.validateSongTOWithId(song);
        try {

            return songService.exists(converter.convert(song, Song.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<SongTO> findSongsByMusic(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        try {
            final Music musicEntity = musicService.getMusic(music.getId());
            Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

            final List<SongTO> songs = converter.convertCollection(songService.findSongsByMusic(musicEntity), SongTO.class);
            Collections.sort(songs);
            return songs;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}

