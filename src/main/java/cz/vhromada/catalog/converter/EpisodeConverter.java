package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.mapper.EpisodeMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for episode.
 *
 * @author Vladimir Hromada
 */
@Component
public class EpisodeConverter implements MovableConverter<Episode, cz.vhromada.catalog.domain.Episode> {

    private EpisodeMapper mapper;

    public EpisodeConverter() {
        this.mapper = Mappers.getMapper(EpisodeMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Episode convert(final Episode source) {
        return mapper.map(source);
    }

    @Override
    public Episode convertBack(final cz.vhromada.catalog.domain.Episode source) {
        return mapper.mapBack(source);
    }

}
