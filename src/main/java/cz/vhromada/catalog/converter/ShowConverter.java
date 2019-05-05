package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.mapper.ShowMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for show.
 *
 * @author Vladimir Hromada
 */
@Component
public class ShowConverter implements MovableConverter<Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Mapper for show
     */
    private ShowMapper mapper;

    /**
     * Creates a new instance of ShowConverter.
     */
    public ShowConverter() {
        this.mapper = Mappers.getMapper(ShowMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Show convert(final Show source) {
        return mapper.map(source);
    }

    @Override
    public Show convertBack(final cz.vhromada.catalog.domain.Show source) {
        return mapper.mapBack(source);
    }

}
