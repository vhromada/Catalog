package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.entities.Music;
import cz.vhromada.catalog.entities.Song;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.catalog.service.CatalogService;
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
     * TO for music argument
     */
    private static final String MUSIC_TO_ARGUMENT = "TO for music";

    /**
     * Service for music
     */
    private final CatalogService<Music> musicService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for music
     */
    private MusicTOValidator musicTOValidator;

    /**
     * Creates a new instance of MusicFacadeImpl.
     *
     * @param musicService     service for music
     * @param converter        converter
     * @param musicTOValidator validator for TO for music
     * @throws IllegalArgumentException if service for music is null
     *                                  or converter is null
     *                                  or validator for TO for music is null
     */
    @Autowired
    public MusicFacadeImpl(@Qualifier("musicService") final CatalogService<Music> musicService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final MusicTOValidator musicTOValidator) {
        Validators.validateArgumentNotNull(musicService, "Service for music");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(musicTOValidator, "Validator for TO for music");

        this.musicService = musicService;
        this.converter = converter;
        this.musicTOValidator = musicTOValidator;
    }

    @Override
    public void newData() {
        musicService.newData();
    }

    @Override
    public List<MusicTO> getMusic() {
        return converter.convertCollection(musicService.getAll(), MusicTO.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public MusicTO getMusic(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(musicService.get(id), MusicTO.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final MusicTO music) {
        musicTOValidator.validateNewMusicTO(music);

        final Music musicEntity = converter.convert(music, Music.class);
        musicEntity.setSongs(new ArrayList<>());

        musicService.add(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final MusicTO music) {
        musicTOValidator.validateExistingMusicTO(music);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);
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
    public void remove(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

        musicService.remove(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void duplicate(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);

        musicService.duplicate(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void moveUp(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);
        final List<Music> musicList = musicService.getAll();
        Validators.validateMoveUp(musicList, musicEntity, MUSIC_TO_ARGUMENT);

        musicService.moveUp(musicEntity);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void moveDown(final MusicTO music) {
        musicTOValidator.validateMusicTOWithId(music);
        final Music musicEntity = musicService.get(music.getId());
        Validators.validateExists(musicEntity, MUSIC_TO_ARGUMENT);
        final List<Music> musicList = musicService.getAll();
        Validators.validateMoveDown(musicList, musicEntity, MUSIC_TO_ARGUMENT);

        musicService.moveDown(musicEntity);
    }

    @Override
    public void updatePositions() {
        musicService.updatePositions();
    }

    @Override
    public int getTotalMediaCount() {
        int totalMedia = 0;
        for (final Music music : musicService.getAll()) {
            totalMedia += music.getMediaCount();
        }

        return totalMedia;
    }

    @Override
    public Time getTotalLength() {
        int totalLength = 0;
        for (final Music music : musicService.getAll()) {
            for (final Song song : music.getSongs()) {
                totalLength += song.getLength();
            }
        }

        return new Time(totalLength);
    }

    @Override
    public int getSongsCount() {
        int songs = 0;
        for (final Music music : musicService.getAll()) {
            songs += music.getSongs().size();
        }

        return songs;
    }

}
