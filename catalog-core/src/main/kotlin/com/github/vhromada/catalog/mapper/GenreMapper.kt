package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreMapper")
class GenreMapper : Mapper<Genre, com.github.vhromada.catalog.domain.Genre> {

    override fun map(source: Genre): com.github.vhromada.catalog.domain.Genre {
        return com.github.vhromada.catalog.domain.Genre(
            id = source.id,
            name = source.name!!,
            position = source.position
        )
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Genre): Genre {
        return Genre(
            id = source.id,
            name = source.name,
            position = source.position
        )
    }

}
