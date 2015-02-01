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
    private MusicTOToMusicConverter converter;

    /**
     * Creates a new instance of SongTOToSongConverter.
     *
     * @param converter converter from TO for music to entity music
     * @throws IllegalArgumentException if converter from TO for music to entity seaso is null
     */
    @Autowired
    public SongTOToSongConverter(final MusicTOToMusicConverter converter) {
        Validators.validateArgumentNotNull(converter, "Converter");

        this.converter = converter;
    }

    @Override
    public Song convert(final SongTO source) {
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
