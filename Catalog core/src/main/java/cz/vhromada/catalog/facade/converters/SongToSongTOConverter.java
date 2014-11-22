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
    @Autowired
    private MusicToMusicTOConverter converter;

    /**
     * Returns converter from entity music to TO for music.
     *
     * @return converter from entity music to TO for music
     */
    public MusicToMusicTOConverter getConverter() {
        return converter;
    }

    /**
     * Sets a new value to converter from entity music to TO for music.
     *
     * @param converter new value
     */
    public void setConverter(final MusicToMusicTOConverter converter) {
        this.converter = converter;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws IllegalStateException    if converter from entity music to TO for music isn't set
     */
    @Override
    public SongTO convert(final Song source) {
        Validators.validateFieldNotNull(converter, "Converter");

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
