package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicFacade")
public class MusicFacadeImpl implements MusicFacade {

    /**
     * Message for not existing music
     */
    private static final String NOT_EXISTING_MUSIC_MESSAGE = "Music doesn't exist.";

    /**
     * Message for not movable music
     */
    private static final String NOT_MOVABLE_MUSIC_MESSAGE = "ID isn't valid - music can't be moved.";

    /**
     * Service for music
     */
    private final CatalogService<cz.vhromada.catalog.domain.Music> musicService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for music
     */
    private CatalogValidator<Music> musicValidator;

    /**
     * Creates a new instance of MusicFacadeImpl.
     *
     * @param musicService   service for music
     * @param converter      converter
     * @param musicValidator validator for music
     * @throws IllegalArgumentException if service for music is null
     *                                  or converter is null
     *                                  or validator for music is null
     */
    @Autowired
    public MusicFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Music> musicService,
            final Converter converter,
            final CatalogValidator<Music> musicValidator) {
        Assert.notNull(musicService, "Service for music mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(musicValidator, "Validator for music mustn't be null.");

        this.musicService = musicService;
        this.converter = converter;
        this.musicValidator = musicValidator;
    }

    @Override
    public void newData() {
        musicService.newData();
    }

    @Override
    public List<Music> getMusic() {
        return converter.convertCollection(musicService.getAll(), Music.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Music getMusic(final Integer id) {
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(musicService.get(id), Music.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void add(final Music music) {
        musicValidator.validate(music, ValidationType.NEW, ValidationType.DEEP);

        final cz.vhromada.catalog.domain.Music musicDomain = converter.convert(music, cz.vhromada.catalog.domain.Music.class);
        musicDomain.setSongs(new ArrayList<>());

        musicService.add(musicDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void update(final Music music) {
        musicValidator.validate(music, ValidationType.EXISTS, ValidationType.DEEP);
        final cz.vhromada.catalog.domain.Music musicDomain = musicService.get(music.getId());
        if (musicDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MUSIC_MESSAGE);
        }

        musicDomain.setName(music.getName());
        musicDomain.setWikiEn(music.getWikiEn());
        musicDomain.setWikiCz(music.getWikiCz());
        musicDomain.setMediaCount(music.getMediaCount());
        musicDomain.setNote(music.getNote());
        musicDomain.setPosition(music.getPosition());

        musicService.update(musicDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void remove(final Music music) {
        musicValidator.validate(music, ValidationType.EXISTS);
        final cz.vhromada.catalog.domain.Music musicDomain = musicService.get(music.getId());
        if (musicDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MUSIC_MESSAGE);
        }

        musicService.remove(musicDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void duplicate(final Music music) {
        musicValidator.validate(music, ValidationType.EXISTS);
        final cz.vhromada.catalog.domain.Music musicDomain = musicService.get(music.getId());
        if (musicDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MUSIC_MESSAGE);
        }

        musicService.duplicate(musicDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveUp(final Music music) {
        musicValidator.validate(music, ValidationType.EXISTS, ValidationType.UP);
        final cz.vhromada.catalog.domain.Music musicDomain = musicService.get(music.getId());
        if (musicDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MUSIC_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Music> musicList = musicService.getAll();
        if (musicList.indexOf(musicDomain) <= 0) {
            throw new IllegalArgumentException(NOT_MOVABLE_MUSIC_MESSAGE);
        }

        musicService.moveUp(musicDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveDown(final Music music) {
        musicValidator.validate(music, ValidationType.EXISTS, ValidationType.DOWN);
        final cz.vhromada.catalog.domain.Music musicDomain = musicService.get(music.getId());
        if (musicDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_MUSIC_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Music> musicList = musicService.getAll();
        if (musicList.indexOf(musicDomain) >= musicList.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_MUSIC_MESSAGE);
        }

        musicService.moveDown(musicDomain);
    }

    @Override
    public void updatePositions() {
        musicService.updatePositions();
    }

    @Override
    public int getTotalMediaCount() {
        int totalMedia = 0;
        for (final cz.vhromada.catalog.domain.Music music : musicService.getAll()) {
            totalMedia += music.getMediaCount();
        }

        return totalMedia;
    }

    @Override
    public Time getTotalLength() {
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Music music : musicService.getAll()) {
            for (final Song song : music.getSongs()) {
                totalLength += song.getLength();
            }
        }

        return new Time(totalLength);
    }

    @Override
    public int getSongsCount() {
        int songs = 0;
        for (final cz.vhromada.catalog.domain.Music music : musicService.getAll()) {
            songs += music.getSongs().size();
        }

        return songs;
    }

}
