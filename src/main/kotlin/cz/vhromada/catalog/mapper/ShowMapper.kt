package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Show
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for show.
 *
 * @author Vladimir Hromada
 */
@Component("showMapper")
class ShowMapper(private val genreMapper: GenreMapper) : Mapper<Show, cz.vhromada.catalog.domain.Show> {

    override fun map(source: Show): cz.vhromada.catalog.domain.Show {
        return cz.vhromada.catalog.domain.Show(
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
                seasons = emptyList())
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Show): Show {
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
