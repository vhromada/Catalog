package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Genre
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreMapper")
class GenreMapper : Mapper<Genre, cz.vhromada.catalog.domain.Genre> {

    override fun map(source: Genre): cz.vhromada.catalog.domain.Genre {
        return cz.vhromada.catalog.domain.Genre(
                id = source.id,
                name = source.name!!,
                position = source.position,
                audit = null)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Genre): Genre {
        return Genre(
                id = source.id,
                name = source.name,
                position = source.position)
    }

}
