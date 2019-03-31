package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.mapper.SeasonMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for season.
 *
 * @author Vladimir Hromada
 */
@Component
public class SeasonConverter implements MovableConverter<Season, cz.vhromada.catalog.domain.Season> {

    private SeasonMapper mapper;

    public SeasonConverter() {
        this.mapper = Mappers.getMapper(SeasonMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Season convert(final Season source) {
        return mapper.map(source);
    }

    @Override
    public Season convertBack(final cz.vhromada.catalog.domain.Season source) {
        return mapper.mapBack(source);
    }

}
