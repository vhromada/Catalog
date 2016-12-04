package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
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
     * Music argument
     */
    private static final String MUSIC_ARGUMENT = "music";

    /**
     * Song argument
     */
    private static final String SONG_ARGUMENT = "song";

    /**
     * Service for music
     */
    private CatalogService<cz.vhromada.catalog.domain.Music> musicService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for music
     */
    private MusicValidator musicValidator;

    /**
     * Validator for song
     */
    private SongValidator songValidator;

    /**
     * Creates a new instance of SongFacadeImpl.
     *
     * @param musicService   service for music
     * @param converter      converter
     * @param musicValidator validator for music
     * @param songValidator  validator for song
     * @throws IllegalArgumentException if service for music is null
     *                                  or converter is null
     *                                  or validator for music is null
     *                                  or validator for song is null
     */
    @Autowired
    public SongFacadeImpl(@Qualifier("musicService") final CatalogService<cz.vhromada.catalog.domain.Music> musicService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final MusicValidator musicValidator,
            final SongValidator songValidator) {
        Validators.validateArgumentNotNull(musicService, "Service for music");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(musicValidator, "Validator for music");
        Validators.validateArgumentNotNull(songValidator, "Validator for song");

        this.musicService = musicService;
        this.converter = converter;
        this.musicValidator = musicValidator;
        this.songValidator = songValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Song getSong(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(getSongEntity(id), Song.class);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void add(final Music music, final Song song) {
        musicValidator.validateMusicWithId(music);
        songValidator.validateNewSong(song);
        final cz.vhromada.catalog.domain.Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_ARGUMENT);

        final cz.vhromada.catalog.domain.Song songEntity = converter.convert(song, cz.vhromada.catalog.domain.Song.class);
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
    public void update(final Song song) {
        songValidator.validateExistingSong(song);
        final cz.vhromada.catalog.domain.Music music = getMusic(song);
        final cz.vhromada.catalog.domain.Song songEntity = getSong(song.getId(), music);
        Validators.validateExists(songEntity, SONG_ARGUMENT);

        updateSong(music, converter.convert(song, cz.vhromada.catalog.domain.Song.class));

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final Song song) {
        songValidator.validateSongWithId(song);
        final cz.vhromada.catalog.domain.Music music = getMusic(song);
        final cz.vhromada.catalog.domain.Song songEntity = getSong(song.getId(), music);
        Validators.validateExists(songEntity, SONG_ARGUMENT);
        assert music != null;

        final List<cz.vhromada.catalog.domain.Song> songs = music.getSongs().stream().filter(songValue -> !songValue.getId().equals(song.getId()))
                .collect(Collectors.toList());
        music.setSongs(songs);

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final Song song) {
        songValidator.validateSongWithId(song);
        final cz.vhromada.catalog.domain.Music music = getMusic(song);
        final cz.vhromada.catalog.domain.Song songEntity = getSong(song.getId(), music);
        Validators.validateExists(songEntity, SONG_ARGUMENT);
        assert music != null;

        final cz.vhromada.catalog.domain.Song newSong = CatalogUtils.duplicateSong(songEntity);
        music.getSongs().add(newSong);

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final Song song) {
        move(song, true);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final Song song) {
        move(song, false);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public List<Song> findSongsByMusic(final Music music) {
        musicValidator.validateMusicWithId(music);
        final cz.vhromada.catalog.domain.Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_ARGUMENT);

        return CollectionUtils.getSortedData(converter.convertCollection(musicEntity.getSongs(), Song.class));
    }

    /**
     * Returns song with ID.
     *
     * @param id    ID
     * @param music music
     * @return song with ID
     */
    private static cz.vhromada.catalog.domain.Song getSong(final Integer id, final cz.vhromada.catalog.domain.Music music) {
        if (music == null) {
            return null;
        }

        for (final cz.vhromada.catalog.domain.Song song : music.getSongs()) {
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
    private static void updateSong(final cz.vhromada.catalog.domain.Music music, final cz.vhromada.catalog.domain.Song song) {
        final List<cz.vhromada.catalog.domain.Song> songs = new ArrayList<>();
        for (final cz.vhromada.catalog.domain.Song songEntity : music.getSongs()) {
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
    private cz.vhromada.catalog.domain.Song getSongEntity(final Integer id) {
        final List<cz.vhromada.catalog.domain.Music> musicList = musicService.getAll();
        for (final cz.vhromada.catalog.domain.Music music : musicList) {
            for (final cz.vhromada.catalog.domain.Song song : music.getSongs()) {
                if (id.equals(song.getId())) {
                    return song;
                }
            }
        }

        return null;
    }

    /**
     * Returns music for song.
     *
     * @param song song
     * @return music for song
     */
    private cz.vhromada.catalog.domain.Music getMusic(final Song song) {
        for (final cz.vhromada.catalog.domain.Music music : musicService.getAll()) {
            for (final cz.vhromada.catalog.domain.Song songEntity : music.getSongs()) {
                if (song.getId().equals(songEntity.getId())) {
                    return music;
                }
            }
        }

        return null;
    }

    /**
     * Moves song in list one position up or down.
     *
     * @param song song
     * @param up   true if moving song up
     */
    private void move(final Song song, final boolean up) {
        songValidator.validateSongWithId(song);
        final cz.vhromada.catalog.domain.Music music = getMusic(song);
        final cz.vhromada.catalog.domain.Song songEntity = getSong(song.getId(), music);
        Validators.validateExists(songEntity, SONG_ARGUMENT);
        assert music != null;
        final List<cz.vhromada.catalog.domain.Song> songs = CollectionUtils.getSortedData(music.getSongs());
        if (up) {
            Validators.validateMoveUp(songs, songEntity, SONG_ARGUMENT);
        } else {
            Validators.validateMoveDown(songs, songEntity, SONG_ARGUMENT);
        }

        final int index = songs.indexOf(songEntity);
        final cz.vhromada.catalog.domain.Song other = songs.get(up ? index - 1 : index + 1);
        final int position = songEntity.getPosition();
        songEntity.setPosition(other.getPosition());
        other.setPosition(position);

        updateSong(music, songEntity);
        updateSong(music, other);

        musicService.update(music);
    }

}

