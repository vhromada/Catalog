package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.mapper.GenreMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for genre.
 *
 * @author Vladimir Hromada
 */
@Component
public class GenreConverter implements MovableConverter<Genre, cz.vhromada.catalog.domain.Genre> {

    private GenreMapper mapper;

    public GenreConverter() {
        this.mapper = Mappers.getMapper(GenreMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Genre convert(final Genre source) {
        return mapper.map(source);
    }

    @Override
    public Genre convertBack(final cz.vhromada.catalog.domain.Genre source) {
        return mapper.mapBack(source);
    }

}
