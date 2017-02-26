package cz.vhromada.catalog.validator.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for song.
 *
 * @author Vladimir Hromada
 */
@Component("songValidator")
public class SongValidatorImpl extends AbstractCatalogValidator<Song, Music> {

    /**
     * Creates a new instance of SongValidatorImpl.
     *
     * @param musicService service for music
     * @throws IllegalArgumentException if service for music is null
     */
    @Autowired
    public SongValidatorImpl(final CatalogService<Music> musicService) {
        super("Song", musicService);
    }

    @Override
    protected cz.vhromada.catalog.domain.Song getData(final Song data) {
        for (final Music music : getCatalogService().getAll()) {
            for (final cz.vhromada.catalog.domain.Song song : music.getSongs()) {
                if (data.getId().equals(song.getId())) {
                    return song;
                }
            }
        }

        return null;
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Song> getList(final Song data) {
        for (final Music music : getCatalogService().getAll()) {
            for (final cz.vhromada.catalog.domain.Song song : music.getSongs()) {
                if (data.getId().equals(song.getId())) {
                    return CollectionUtils.getSortedData(music.getSongs());
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Validates song deeply.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of song is negative value</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data   validating song
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Song data, final Result<Void> result) {
        if (data.getName() == null) {
            result.addEvent(new Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string."));
        }
        if (data.getLength() < 0) {
            result.addEvent(new Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number."));
        }
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null."));
        }
    }

}
