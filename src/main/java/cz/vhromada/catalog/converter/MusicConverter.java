package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.mapper.MusicMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for music.
 *
 * @author Vladimir Hromada
 */
@Component
public class MusicConverter implements MovableConverter<Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Mapper for music
     */
    private MusicMapper mapper;

    /**
     * Creates a new instance of MusicConverter.
     */
    public MusicConverter() {
        this.mapper = Mappers.getMapper(MusicMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Music convert(final Music source) {
        return mapper.map(source);
    }

    @Override
    public Music convertBack(final cz.vhromada.catalog.domain.Music source) {
        return mapper.mapBack(source);
    }

}
