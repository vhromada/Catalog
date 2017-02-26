package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CatalogUtils;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songFacade")
public class SongFacadeImpl implements SongFacade {

    /**
     * Message for not existing song
     */
    private static final String NOT_EXISTING_SONG_MESSAGE = "Song doesn't exist.";

    /**
     * Message for not movable song
     */
    private static final String NOT_MOVABLE_SONG_MESSAGE = "ID isn't valid - song can't be moved.";

    /**
     * Message for not existing music
     */
    private static final String NOT_EXISTING_MUSIC_MESSAGE = "Music doesn't exist.";

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
    private CatalogValidator<Music> musicValidator;

    /**
     * Validator for song
     */
    private CatalogValidator<Song> songValidator;

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
    public SongFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Music> musicService,
            final Converter converter,
            final CatalogValidator<Music> musicValidator,
            final CatalogValidator<Song> songValidator) {
        Assert.notNull(musicService, "Service for music mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(musicValidator, "Validator for music mustn't be null.");
        Assert.notNull(songValidator, "Validator for song mustn't be null.");

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
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(getSongDomain(id), Song.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void add(final Music music, final Song song) {
        musicValidator.validate(music, ValidationType.EXISTS);
        songValidator.validate(song, ValidationType.NEW, ValidationType.DEEP);
        final cz.vhromada.catalog.domain.Music musicDomain = musicService.get(music.getId());
        if (musicDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MUSIC_MESSAGE);
        }

        final cz.vhromada.catalog.domain.Song songDomain = converter.convert(song, cz.vhromada.catalog.domain.Song.class);
        songDomain.setPosition(Integer.MAX_VALUE);
        musicDomain.getSongs().add(songDomain);

        musicService.update(musicDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void update(final Song song) {
        songValidator.validate(song, ValidationType.EXISTS, ValidationType.DEEP);
        final cz.vhromada.catalog.domain.Music music = getMusic(song);
        final cz.vhromada.catalog.domain.Song songDomain = getSong(song.getId(), music);
        if (songDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SONG_MESSAGE);
        }

        updateSong(music, converter.convert(song, cz.vhromada.catalog.domain.Song.class));

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void remove(final Song song) {
        songValidator.validate(song, ValidationType.EXISTS);
        final cz.vhromada.catalog.domain.Music music = getMusic(song);
        final cz.vhromada.catalog.domain.Song songDomain = getSong(song.getId(), music);
        if (songDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SONG_MESSAGE);
        }

        final List<cz.vhromada.catalog.domain.Song> songs = music.getSongs().stream().filter(songValue -> !songValue.getId().equals(song.getId()))
                .collect(Collectors.toList());
        music.setSongs(songs);

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void duplicate(final Song song) {
        songValidator.validate(song, ValidationType.EXISTS);
        final cz.vhromada.catalog.domain.Music music = getMusic(song);
        final cz.vhromada.catalog.domain.Song songDomain = getSong(song.getId(), music);
        if (songDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SONG_MESSAGE);
        }

        final cz.vhromada.catalog.domain.Song newSong = CatalogUtils.duplicateSong(songDomain);
        music.getSongs().add(newSong);

        musicService.update(music);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveUp(final Song song) {
        move(song, true);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveDown(final Song song) {
        move(song, false);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public List<Song> findSongsByMusic(final Music music) {
        musicValidator.validate(music, ValidationType.EXISTS);
        final cz.vhromada.catalog.domain.Music musicDomain = musicService.get(music.getId());
        if (musicDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MUSIC_MESSAGE);
        }

        return CollectionUtils.getSortedData(converter.convertCollection(musicDomain.getSongs(), Song.class));
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
        for (final cz.vhromada.catalog.domain.Song songDomain : music.getSongs()) {
            if (songDomain.equals(song)) {
                songs.add(song);
            } else {
                songs.add(songDomain);
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
    private cz.vhromada.catalog.domain.Song getSongDomain(final Integer id) {
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
            for (final cz.vhromada.catalog.domain.Song songDomain : music.getSongs()) {
                if (song.getId().equals(songDomain.getId())) {
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
        songValidator.validate(song, ValidationType.EXISTS, up ? ValidationType.UP : ValidationType.DOWN);
        final cz.vhromada.catalog.domain.Music music = getMusic(song);
        final cz.vhromada.catalog.domain.Song songDomain = getSong(song.getId(), music);
        if (songDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SONG_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Song> songs = CollectionUtils.getSortedData(music.getSongs());
        if (up) {
            if (songs.indexOf(songDomain) <= 0) {
                throw new IllegalArgumentException(NOT_MOVABLE_SONG_MESSAGE);
            }
        } else if (songs.indexOf(songDomain) >= songs.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_SONG_MESSAGE);
        }

        final int index = songs.indexOf(songDomain);
        final cz.vhromada.catalog.domain.Song other = songs.get(up ? index - 1 : index + 1);
        final int position = songDomain.getPosition();
        songDomain.setPosition(other.getPosition());
        other.setPosition(position);

        updateSong(music, songDomain);
        updateSong(music, other);

        musicService.update(music);
    }

}

