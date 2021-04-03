package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Medium
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.web.fo.MovieFO
import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for movies.
 *
 * @author Vladimir Hromada
 */
@Component("webMovieMapper")
class MovieMapper(private val timeMapper: Mapper<Int, TimeFO>) : Mapper<Movie, MovieFO> {

    override fun map(source: Movie): MovieFO {
        return MovieFO(id = source.id,
            czechName = source.czechName,
            originalName = source.originalName,
            year = source.year.toString(),
            language = source.language,
            subtitles = source.subtitles!!.filterNotNull(),
            media = source.media!!.map { timeMapper.map(source = it!!.length!!) },
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

    override fun mapBack(source: MovieFO): Movie {
        return Movie(
            id = source.id,
            czechName = source.czechName,
            originalName = source.originalName,
            year = source.year!!.toInt(),
            language = source.language,
            subtitles = source.subtitles,
            media = source.media!!.mapIndexed { index, it -> mapMedium(index = index, source = it) },
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

    /**
     * Returns medium.
     *
     * @param index  index of FO for time
     * @param source FO for time
     * @return medium
     */
    private fun mapMedium(index: Int, source: TimeFO?): Medium {
        return Medium(
            id = null,
            length = timeMapper.mapBack(source = source!!),
            number = index + 1
        )
    }

}
