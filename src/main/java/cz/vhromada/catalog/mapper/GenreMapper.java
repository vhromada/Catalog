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

    cz.vhromada.catalog.domain.Genre map(Genre source);

    Genre mapBack(cz.vhromada.catalog.domain.Genre source);

}
