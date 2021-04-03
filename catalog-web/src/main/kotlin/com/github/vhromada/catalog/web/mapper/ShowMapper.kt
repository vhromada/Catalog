package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.web.fo.ShowFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for shows.
 *
 * @author Vladimir Hromada
 */
@Component("webShowMapper")
class ShowMapper : Mapper<Show, ShowFO> {

    override fun map(source: Show): ShowFO {
        return ShowFO(id = source.id,
            czechName = source.czechName,
            originalName = source.originalName,
            csfd = source.csfd,
            imdb = source.imdbCode!! >= 1,
            imdbCode = if (source.imdbCode!! < 1) null else source.imdbCode!!.toString(),
            wikiEn = source.wikiEn,
            wikiCz = source.wikiCz,
            picture = source.picture,
            note = source.note,
            position = source.position,
            genres = source.genres!!.map { it!!.id!! })
    }

    override fun mapBack(source: ShowFO): Show {
        return Show(
            id = source.id,
            czechName = source.czechName,
            originalName = source.originalName,
            csfd = source.csfd,
            imdbCode = if (source.imdb) source.imdbCode!!.toInt() else -1,
            wikiEn = source.wikiEn,
            wikiCz = source.wikiCz,
            picture = source.picture,
            note = source.note,
            position = source.position,
            genres = null
        )
    }

}
