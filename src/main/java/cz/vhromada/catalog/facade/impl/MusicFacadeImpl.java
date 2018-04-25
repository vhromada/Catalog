package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;

import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.common.Time;
import cz.vhromada.common.facade.AbstractMovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicFacade")
public class MusicFacadeImpl extends AbstractMovableParentFacade<Music, cz.vhromada.catalog.domain.Music> implements MusicFacade {

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
    public MusicFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Music> musicService, final Converter converter,
        final MovableValidator<Music> musicValidator) {
        super(musicService, converter, musicValidator);
    }

    @Override
    public Result<Integer> getTotalMediaCount() {
        int totalMedia = 0;
        for (final cz.vhromada.catalog.domain.Music music : getMovableService().getAll()) {
            totalMedia += music.getMediaCount();
        }

        return Result.of(totalMedia);
    }

    @Override
    public Result<Time> getTotalLength() {
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Music music : getMovableService().getAll()) {
            if (!CollectionUtils.isEmpty(music.getSongs())) {
                for (final Song song : music.getSongs()) {
                    totalLength += song.getLength();
                }
            }
        }

        return Result.of(new Time(totalLength));
    }

    @Override
    public Result<Integer> getSongsCount() {
        int songs = 0;
        for (final cz.vhromada.catalog.domain.Music music : getMovableService().getAll()) {
            if (!CollectionUtils.isEmpty(music.getSongs())) {
                songs += music.getSongs().size();
            }
        }

        return Result.of(songs);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getDataForAdd(final Music data) {
        final cz.vhromada.catalog.domain.Music music = super.getDataForAdd(data);
        music.setSongs(new ArrayList<>());

        return music;
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getDataForUpdate(final Music data) {
        final cz.vhromada.catalog.domain.Music music = super.getDataForUpdate(data);
        music.setSongs(getMovableService().get(data.getId()).getSongs());

        return music;
    }

    @Override
    protected Class<Music> getEntityClass() {
        return Music.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Music> getDomainClass() {
        return cz.vhromada.catalog.domain.Music.class;
    }

}
