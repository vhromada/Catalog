package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for show.
 *
 * @author Vladimir Hromada
 */
@Component("showMapper")
class ShowMapper(private val genreMapper: Mapper<Genre, com.github.vhromada.catalog.domain.Genre>) : Mapper<Show, com.github.vhromada.catalog.domain.Show> {

    override fun map(source: Show): com.github.vhromada.catalog.domain.Show {
        return com.github.vhromada.catalog.domain.Show(
                id = source.id,
                czechName = source.czechName!!,
                originalName = source.originalName!!,
                csfd = source.csfd,
                imdbCode = source.imdbCode,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                picture = source.picture,
                note = source.note,
                position = source.position,
                genres = genreMapper.map(source.genres!!.filterNotNull()),
                seasons = emptyList(),
                audit = null)
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Show): Show {
        return Show(
                id = source.id,
                czechName = source.czechName,
                originalName = source.originalName,
                csfd = source.csfd,
                imdbCode = source.imdbCode,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                picture = source.picture,
                note = source.note,
                position = source.position,
                genres = genreMapper.mapBack(source.genres))
    }

}
