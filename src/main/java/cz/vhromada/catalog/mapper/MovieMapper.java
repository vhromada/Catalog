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

    /**
     * Maps entity movie to domain movie.
     *
     * @param source entity movie
     * @return mapped domain movie
     */
    cz.vhromada.catalog.domain.Movie map(Movie source);

    /**
     * Maps domain movie to entity movie.
     *
     * @param source domain movie
     * @return mapped entity movie
     */
    Movie mapBack(cz.vhromada.catalog.domain.Movie source);

}
