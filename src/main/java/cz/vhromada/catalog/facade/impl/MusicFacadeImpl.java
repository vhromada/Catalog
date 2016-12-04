package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.MusicValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicFacade")
public class MusicFacadeImpl implements MusicFacade {

    /**
     * Music argument
     */
    private static final String MUSIC_ARGUMENT = "music";

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
    private MusicValidator musicValidator;

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
    public MusicFacadeImpl(@Qualifier("musicService") final CatalogService<cz.vhromada.catalog.domain.Music> musicService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final MusicValidator musicValidator) {
        Validators.validateArgumentNotNull(musicService, "Service for music");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(musicValidator, "Validator for music");

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
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(musicService.get(id), Music.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final Music music) {
        musicValidator.validateNewMusic(music);

        final cz.vhromada.catalog.domain.Music musicEntity = converter.convert(music, cz.vhromada.catalog.domain.Music.class);
        musicEntity.setSongs(new ArrayList<>());

        musicService.add(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final Music music) {
        musicValidator.validateExistingMusic(music);
        final cz.vhromada.catalog.domain.Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_ARGUMENT);
        assert musicEntity != null;

        musicEntity.setName(music.getName());
        musicEntity.setWikiEn(music.getWikiEn());
        musicEntity.setWikiCz(music.getWikiCz());
        musicEntity.setMediaCount(music.getMediaCount());
        musicEntity.setNote(music.getNote());
        musicEntity.setPosition(music.getPosition());

        musicService.update(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void remove(final Music music) {
        musicValidator.validateMusicWithId(music);
        final cz.vhromada.catalog.domain.Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_ARGUMENT);

        musicService.remove(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Music music) {
        musicValidator.validateMusicWithId(music);
        final cz.vhromada.catalog.domain.Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_ARGUMENT);

        musicService.duplicate(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Music music) {
        musicValidator.validateMusicWithId(music);
        final cz.vhromada.catalog.domain.Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Music> musicList = musicService.getAll();
        Validators.validateMoveUp(musicList, musicEntity, MUSIC_ARGUMENT);

        musicService.moveUp(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Music music) {
        musicValidator.validateMusicWithId(music);
        final cz.vhromada.catalog.domain.Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Music> musicList = musicService.getAll();
        Validators.validateMoveDown(musicList, musicEntity, MUSIC_ARGUMENT);

        musicService.moveDown(musicEntity);
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
