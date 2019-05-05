package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.mapper.SongMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for song.
 *
 * @author Vladimir Hromada
 */
@Component
public class SongConverter implements MovableConverter<Song, cz.vhromada.catalog.domain.Song> {

    /**
     * Mapper for song
     */
    private SongMapper mapper;

    /**
     * Creates a new instance of SongConverter.
     */
    public SongConverter() {
        this.mapper = Mappers.getMapper(SongMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Song convert(final Song source) {
        return mapper.map(source);
    }

    @Override
    public Song convertBack(final cz.vhromada.catalog.domain.Song source) {
        return mapper.mapBack(source);
    }

}
