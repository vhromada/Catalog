package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity song to TO for song.
 *
 * @author Vladimir Hromada
 */
@Component("songToSongTOConverter")
public class SongToSongTOConverter implements Converter<Song, SongTO> {

    /** Converter from entity music to TO for music */
    private MusicToMusicTOConverter converter;

    /**
     * Creates a new instance of SongToSongTOConverter.
     *
     * @param converter converter from entity music to TO for music
     * @throws IllegalArgumentException if converter from entity music to TO for music is null
     */
    @Autowired
    public SongToSongTOConverter(final MusicToMusicTOConverter converter) {
        Validators.validateArgumentNotNull(converter, "Converter");

        this.converter = converter;
    }

    @Override
    public SongTO convert(final Song source) {
        if (source == null) {
            return null;
        }

        final SongTO song = new SongTO();
        song.setId(source.getId());
        song.setName(source.getName());
        song.setLength(source.getLength());
        song.setNote(source.getNote());
        song.setPosition(source.getPosition());
        song.setMusic(converter.convert(source.getMusic()));
        return song;
    }

}
