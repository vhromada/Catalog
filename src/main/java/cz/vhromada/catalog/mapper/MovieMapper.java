package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Movie;

import org.mapstruct.Mapper;

/**
 * An interface represents mapper for movie.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface MovieMapper {

    cz.vhromada.catalog.domain.Movie map(Movie source);

    Movie mapBack(cz.vhromada.catalog.domain.Movie source);

}
