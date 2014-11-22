package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for song to entity song.
 *
 * @author Vladimir Hromada
 */
@Component("songTOToSongConverter")
public class SongTOToSongConverter implements Converter<SongTO, Song> {

    /** Converter from TO for music to entity music */
    @Autowired
    private MusicTOToMusicConverter converter;

    /**
     * Returns converter from TO for music to entity music.
     *
     * @return converter from TO for music to entity music
     */
    public MusicTOToMusicConverter getConverter() {
        return converter;
    }

    /**
     * Sets a new value to converter from TO for music to entity music.
     *
     * @param converter new value
     */
    public void setConverter(final MusicTOToMusicConverter converter) {
        this.converter = converter;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws IllegalStateException    if converter from TO for music to entity music isn't set
     */
    @Override
    public Song convert(final SongTO source) {
        Validators.validateFieldNotNull(converter, "Converter");

        if (source == null) {
            return null;
        }

        final Song song = new Song();
        song.setId(source.getId());
        song.setName(source.getName());
        song.setLength(source.getLength());
        song.setNote(source.getNote());
        song.setPosition(source.getPosition());
        song.setMusic(converter.convert(source.getMusic()));
        return song;
    }

}
