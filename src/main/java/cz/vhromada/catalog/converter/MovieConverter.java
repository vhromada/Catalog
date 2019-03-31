package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.mapper.MovieMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for movie.
 *
 * @author Vladimir Hromada
 */
@Component
public class MovieConverter implements MovableConverter<Movie, cz.vhromada.catalog.domain.Movie> {

    private MovieMapper mapper;

    public MovieConverter() {
        this.mapper = Mappers.getMapper(MovieMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Movie convert(final Movie source) {
        return mapper.map(source);
    }

    @Override
    public Movie convertBack(final cz.vhromada.catalog.domain.Movie source) {
        return mapper.mapBack(source);
    }

}
