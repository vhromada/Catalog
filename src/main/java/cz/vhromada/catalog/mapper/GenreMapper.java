package cz.vhromada.catalog.mapper;

import cz.vhromada.catalog.entity.Genre;

import org.mapstruct.Mapper;

/**
 * An interface represents mapper for genre.
 *
 * @author Vladimir Hromada
 */
@Mapper
public interface GenreMapper {

    /**
     * Maps entity genre to domain genre.
     *
     * @param source entity genre
     * @return mapped domain genre
     */
    cz.vhromada.catalog.domain.Genre map(Genre source);

    /**
     * Maps domain genre to entity genre.
     *
     * @param source domain genre
     * @return mapped entity genre
     */
    Genre mapBack(cz.vhromada.catalog.domain.Genre source);

}
