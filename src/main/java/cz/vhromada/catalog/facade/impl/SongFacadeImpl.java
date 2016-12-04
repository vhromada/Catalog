package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.MusicTO;
import cz.vhromada.catalog.entity.SongTO;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CatalogUtils;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.MusicValidator;
import cz.vhromada.catalog.validator.SongValidator;
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
    private MusicValidator musicValidator;

    /**
     * Validator for TO for song
     */
    private SongValidator songValidator;

    /**
     * Creates a new instance of SongFacadeImpl.
     *
     * @param musicService   service for music
     * @param converter      converter
     * @param musicValidator validator for TO for music
     * @param songValidator  validator for TO for song
     * @throws IllegalArgumentException if service for music is null
     *                                  or converter is null
     *                                  or validator for TO for music is null
     *                                  or validator for TO for song is null
     */
    @Autowired
    public SongFacadeImpl(@Qualifier("musicService") final CatalogService<Music> musicService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final MusicValidator musicValidator,
            final SongValidator songValidator) {
        Validators.validateArgumentNotNull(musicService, "Service for music");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(musicValidator, "Validator for TO for music");
        Validators.validateArgumentNotNull(songValidator, "Validator for TO for song");

        this.musicService = musicService;
        this.converter = converter;
        this.musicValidator = musicValidator;
        this.songValidator = songValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SongTO getSong(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(getSongEntity(id), SongTO.class);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void add(final MusicTO music, final SongTO song) {
        musicValidator.validateMusicTOWithId(music);
        songValidator.validateNewSongTO(song);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

        final Song songEntity = converter.convert(song, Song.class);
        songEntity.setPosition(Integer.MAX_VALUE);
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
        songValidator.validateExistingSongTO(song);
        final Music music = getMusic(song);
        final Song songEntity = getSong(song.getId(), music);
        Validators.validateExists(songEntity, SONG_TO_ARGUMENT);

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
        songValidator.validateSongTOWithId(song);
        final Music music = getMusic(song);
        final Song songEntity = getSong(song.getId(), music);
        Validators.validateExists(songEntity, SONG_TO_ARGUMENT);
        assert music != null;

        final List<Song> songs = music.getSongs().stream().filter(songValue -> !songValue.getId().equals(song.getId())).collect(Collectors.toList());
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
        songValidator.validateSongTOWithId(song);
        final Music music = getMusic(song);
        final Song songEntity = getSong(song.getId(), music);
        Validators.validateExists(songEntity, SONG_TO_ARGUMENT);
        assert music != null;

        final Song newSong = CatalogUtils.duplicateSong(songEntity);
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
        musicValidator.validateMusicTOWithId(music);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

        return CollectionUtils.getSortedData(converter.convertCollection(musicEntity.getSongs(), SongTO.class));
    }

    /**
     * Returns song with ID.
     *
     * @param id    ID
     * @param music music
     * @return song with ID
     */
    private static Song getSong(final Integer id, final Music music) {
        if (music == null) {
            return null;
        }

        for (final Song song : music.getSongs()) {
            if (id.equals(song.getId())) {
                return song;
            }
        }

        return null;
    }

    /**
     * Updates song in music.
     *
     * @param music music
     * @param song  song
     */
    private static void updateSong(final Music music, final Song song) {
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
     * Returns song with ID.
     *
     * @param id ID
     * @return song with ID
     */
    private Song getSongEntity(final Integer id) {
        final List<Music> musicList = musicService.getAll();
        for (final Music music : musicList) {
            for (final Song song : music.getSongs()) {
                if (id.equals(song.getId())) {
                    return song;
                }
            }
        }

        return null;
    }

    /**
     * Returns music for TO for song.
     *
     * @param song TO for song
     * @return music for TO for song
     */
    private Music getMusic(final SongTO song) {
        for (final Music music : musicService.getAll()) {
            for (final Song songEntity : music.getSongs()) {
                if (song.getId().equals(songEntity.getId())) {
                    return music;
                }
            }
        }

        return null;
    }

    /**
     * Moves TO for song in list one position up or down.
     *
     * @param song TO for song
     * @param up   true if moving TO for song up
     */
    private void move(final SongTO song, final boolean up) {
        songValidator.validateSongTOWithId(song);
        final Music music = getMusic(song);
        final Song songEntity = getSong(song.getId(), music);
        Validators.validateExists(songEntity, SONG_TO_ARGUMENT);
        assert music != null;
        final List<Song> songs = CollectionUtils.getSortedData(music.getSongs());
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

