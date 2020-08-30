package com.github.vhromada.catalog.web.mapper.impl

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.web.fo.GenreFO
import com.github.vhromada.catalog.web.mapper.GenreMapper
import org.springframework.stereotype.Component

/**
 * A class represents implementation of mapper for genres.
 *
 * @author Vladimir Hromada
 */
@Component("webGenreMapper")
class GenreMapperImpl : GenreMapper {

    override fun map(source: Genre): GenreFO {
        return GenreFO(id = source.id,
                name = source.name,
                position = source.position)
    }

    override fun mapBack(source: GenreFO): Genre {
        return Genre(id = source.id,
                name = source.name,
                position = source.position)
    }

}
