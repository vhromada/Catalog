package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.entities.Music;
import cz.vhromada.catalog.entities.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.catalog.facade.validators.SongTOValidator;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songFacade")
public class SongFacadeImpl implements SongFacade {

    /**
     * TO for music argument
     */
    private static final String MUSIC_TO_ARGUMENT = "TO for music";

    /**
     * TO for song argument
     */
    private static final String SONG_TO_ARGUMENT = "TO for song";

    /**
     * Service for music
     */
    private CatalogService<Music> musicService;

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
     * @param converter        converter
     * @param musicTOValidator validator for TO for music
     * @param songTOValidator  validator for TO for song
     * @throws IllegalArgumentException if service for music is null
     *                                  or converter is null
     *                                  or validator for TO for music is null
     *                                  or validator for TO for song is null
     */
    @Autowired
    public SongFacadeImpl(@Qualifier("musicService") final CatalogService<Music> musicService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final MusicTOValidator musicTOValidator,
            final SongTOValidator songTOValidator) {
        Validators.validateArgumentNotNull(musicService, "Service for music");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(musicTOValidator, "Validator for TO for music");
        Validators.validateArgumentNotNull(songTOValidator, "Validator for TO for song");

        this.musicService = musicService;
        this.converter = converter;
        this.musicTOValidator = musicTOValidator;
        this.songTOValidator = songTOValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SongTO getSong(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        final List<Music> musicList = musicService.getAll();
        for (final Music music : musicList) {
            for (final Song song : music.getSongs()) {
                if (id.equals(song.getId())) {
                    return converter.convert(song, SongTO.class);
                }
            }
        }

        return null;
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void add(final MusicTO music, final SongTO song) {
        musicTOValidator.validateMusicTOWithId(music);
        songTOValidator.validateNewSongTO(song);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

        final Song songEntity = converter.convert(song, Song.class);
        musicEntity.getSongs().add(songEntity);

        musicService.update(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final SongTO song) {
        songTOValidator.validateExistingSongTO(song);
        final SongTO songTO = getSong(song.getId());
        Validators.validateExists(songTO, SONG_TO_ARGUMENT);

        final Music music = getMusic(song);
        updateSong(music, converter.convert(song, Song.class));

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final SongTO song) {
        songTOValidator.validateSongTOWithId(song);
        final SongTO songTO = getSong(song.getId());
        Validators.validateExists(songTO, SONG_TO_ARGUMENT);

        final Music music = getMusic(song);
        final List<Song> songs = new ArrayList<>();
        for (final Song songEntity : music.getSongs()) {
            if (!songEntity.getId().equals(song.getId())) {
                songs.add(songEntity);
            }
        }
        music.setSongs(songs);

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final SongTO song) {
        songTOValidator.validateSongTOWithId(song);
        final SongTO songTO = getSong(song.getId());
        Validators.validateExists(songTO, SONG_TO_ARGUMENT);

        final Music music = getMusic(song);
        final Song newSong = new Song();
        newSong.setName(song.getName());
        newSong.setLength(song.getLength());
        newSong.setNote(song.getNote());
        newSong.setPosition(song.getPosition());
        music.getSongs().add(newSong);

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final SongTO song) {
        move(song, true);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final SongTO song) {
        move(song, false);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public List<SongTO> findSongsByMusic(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

        return CollectionUtils.getSortedData(converter.convertCollection(musicEntity.getSongs(), SongTO.class));
    }

    /**
     * Returns music for song.
     *
     * @param song TO for song
     * @return music for song
     */
    private Music getMusic(final SongTO song) {
        for (final Music music : musicService.getAll()) {
            for (final Song songEntity : music.getSongs()) {
                if (song.getId().equals(songEntity.getId())) {
                    return music;
                }
            }
        }

        throw new IllegalStateException("Unknown music");
    }

    /**
     * Updates song in music.
     *
     * @param music TO for music
     * @param song  TO for song
     */
    private void updateSong(final Music music, final Song song) {
        final List<Song> songs = new ArrayList<>();
        for (final Song songEntity : music.getSongs()) {
            if (songEntity.equals(song)) {
                songs.add(song);
            } else {
                songs.add(songEntity);
            }
        }
        music.setSongs(songs);
    }

    /**
     * Moves song in list one position up or down.
     *
     * @param song TO for song
     * @param up   if moving song up
     */
    private void move(final SongTO song, final boolean up) {
        songTOValidator.validateSongTOWithId(song);
        final SongTO songTO = getSong(song.getId());
        Validators.validateExists(songTO, SONG_TO_ARGUMENT);
        final Music music = getMusic(song);
        final List<Song> songs = CollectionUtils.getSortedData(music.getSongs());
        final Song songEntity = converter.convert(song, Song.class);
        if (up) {
            Validators.validateMoveUp(songs, songEntity, SONG_TO_ARGUMENT);
        } else {
            Validators.validateMoveDown(songs, songEntity, SONG_TO_ARGUMENT);
        }

        final int index = songs.indexOf(songEntity);
        final Song other = songs.get(up ? index - 1 : index + 1);
        final int position = songEntity.getPosition();
        songEntity.setPosition(other.getPosition());
        other.setPosition(position);

        updateSong(music, songEntity);
        updateSong(music, other);

        musicService.update(music);
    }

}

