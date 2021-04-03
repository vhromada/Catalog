package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Medium
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieMapper")
class MovieMapper(
    private val mediumMapper: Mapper<Medium, com.github.vhromada.catalog.domain.Medium>,
    private val genreMapper: Mapper<Genre, com.github.vhromada.catalog.domain.Genre>
) : Mapper<Movie, com.github.vhromada.catalog.domain.Movie> {

    override fun map(source: Movie): com.github.vhromada.catalog.domain.Movie {
        return com.github.vhromada.catalog.domain.Movie(
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
            genres = genreMapper.map(source.genres!!.filterNotNull())
        )
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Movie): Movie {
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
            genres = genreMapper.mapBack(source.genres)
        )
    }

}
