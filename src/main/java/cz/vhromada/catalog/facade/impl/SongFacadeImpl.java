package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.utils.CatalogUtils;
import cz.vhromada.common.facade.AbstractMovableChildFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.utils.CollectionUtils;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songFacade")
public class SongFacadeImpl extends AbstractMovableChildFacade<Song, cz.vhromada.catalog.domain.Song, Music, cz.vhromada.catalog.domain.Music>
    implements SongFacade {

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
    public SongFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Music> musicService, final Converter converter,
        final MovableValidator<Music> musicValidator, final MovableValidator<Song> songValidator) {
        super(musicService, converter, musicValidator, songValidator);
    }

    @Override
    protected cz.vhromada.catalog.domain.Song getDomainData(final Integer id) {
        final List<cz.vhromada.catalog.domain.Music> musicList = getMovableService().getAll();
        for (final cz.vhromada.catalog.domain.Music music : musicList) {
            for (final cz.vhromada.catalog.domain.Song song : music.getSongs()) {
                if (id.equals(song.getId())) {
                    return song;
                }
            }
        }

        return null;
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Song> getDomainList(final Music parent) {
        return getMovableService().get(parent.getId()).getSongs();
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getForAdd(final Music parent, final cz.vhromada.catalog.domain.Song data) {
        final cz.vhromada.catalog.domain.Music music = getMovableService().get(parent.getId());
        if (music.getSongs() == null) {
            music.setSongs(new ArrayList<>());
        }
        music.getSongs().add(data);

        return music;
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getForUpdate(final Song data) {
        final cz.vhromada.catalog.domain.Music music = getMusic(data);

        updateSong(music, getDataForUpdate(data));

        return music;
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getForRemove(final Song data) {
        final cz.vhromada.catalog.domain.Music music = getMusic(data);

        final List<cz.vhromada.catalog.domain.Song> songs = music.getSongs().stream()
            .filter(songValue -> !songValue.getId().equals(data.getId()))
            .collect(Collectors.toList());
        music.setSongs(songs);

        return music;
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getForDuplicate(final Song data) {
        final cz.vhromada.catalog.domain.Music music = getMusic(data);
        final cz.vhromada.catalog.domain.Song songDomain = getSong(data.getId(), music);

        final cz.vhromada.catalog.domain.Song newSong = CatalogUtils.duplicateSong(songDomain);
        music.getSongs().add(newSong);

        return music;
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getForMove(final Song data, final boolean up) {
        final cz.vhromada.catalog.domain.Music music = getMusic(data);
        final cz.vhromada.catalog.domain.Song song = getSong(data.getId(), music);
        final List<cz.vhromada.catalog.domain.Song> songs = CollectionUtils.getSortedData(music.getSongs());

        final int index = songs.indexOf(song);
        final cz.vhromada.catalog.domain.Song other = songs.get(up ? index - 1 : index + 1);
        final int position = song.getPosition();
        song.setPosition(other.getPosition());
        other.setPosition(position);

        updateSong(music, song);
        updateSong(music, other);

        return music;
    }

    @Override
    protected Class<Song> getEntityClass() {
        return Song.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Song> getDomainClass() {
        return cz.vhromada.catalog.domain.Song.class;
    }

    /**
     * Returns music for song.
     *
     * @param song song
     * @return music for song
     */
    private cz.vhromada.catalog.domain.Music getMusic(final Song song) {
        for (final cz.vhromada.catalog.domain.Music music : getMovableService().getAll()) {
            for (final cz.vhromada.catalog.domain.Song songDomain : music.getSongs()) {
                if (song.getId().equals(songDomain.getId())) {
                    return music;
                }
            }
        }

        throw new IllegalStateException("Unknown song.");
    }

    /**
     * Returns song with ID.
     *
     * @param id    ID
     * @param music music
     * @return song with ID
     */
    private static cz.vhromada.catalog.domain.Song getSong(final Integer id, final cz.vhromada.catalog.domain.Music music) {
        for (final cz.vhromada.catalog.domain.Song song : music.getSongs()) {
            if (id.equals(song.getId())) {
                return song;
            }
        }

        throw new IllegalStateException("Unknown song.");
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

}
