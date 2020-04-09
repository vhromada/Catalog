package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Movie
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieMapper")
class MovieMapper(
        private val mediumMapper: MediumMapper,
        private val genreMapper: GenreMapper) : Mapper<Movie, cz.vhromada.catalog.domain.Movie> {

    override fun map(source: Movie): cz.vhromada.catalog.domain.Movie {
        return cz.vhromada.catalog.domain.Movie(
                id = source.id,
                czechName = source.czechName!!,
                originalName = source.originalName!!,
                year = source.year!!,
                language = source.language!!,
                subtitles = source.subtitles!!.filterNotNull(),
                media = mediumMapper.map(source.media!!.filterNotNull()),
                csfd = source.csfd,
                imdbCode = source.imdbCode,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                picture = source.picture,
                note = source.note,
                position = source.position,
                genres = genreMapper.map(source.genres!!.filterNotNull()),
                audit = null)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Movie): Movie {
        return Movie(
                id = source.id,
                czechName = source.czechName,
                originalName = source.originalName,
                year = source.year,
                language = source.language,
                subtitles = source.subtitles,
                media = mediumMapper.mapBack(source.media),
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
