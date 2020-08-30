package com.github.vhromada.catalog.web.mapper.impl

import com.github.vhromada.catalog.entity.Medium
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.web.fo.MovieFO
import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.catalog.web.mapper.MovieMapper
import com.github.vhromada.catalog.web.mapper.TimeMapper
import org.springframework.stereotype.Component

/**
 * A class represents implementation of mapper for movies.
 *
 * @author Vladimir Hromada
 */
@Component("webMovieMapper")
class MovieMapperImpl(private val timeMapper: TimeMapper) : MovieMapper {

    override fun map(source: Movie): MovieFO {
        return MovieFO(id = source.id,
                czechName = source.czechName,
                originalName = source.originalName,
                year = source.year.toString(),
                language = source.language,
                subtitles = source.subtitles!!.filterNotNull(),
                media = source.media!!.map { timeMapper.map(it!!.length!!) },
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
        return Movie(id = source.id,
                czechName = source.czechName,
                originalName = source.originalName,
                year = source.year!!.toInt(),
                language = source.language,
                subtitles = source.subtitles,
                media = source.media!!.mapIndexed { index, it -> mapMedium(index, it) },
                csfd = source.csfd,
                imdbCode = if (source.imdb) source.imdbCode!!.toInt() else -1,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                picture = source.picture,
                note = source.note,
                position = source.position,
                genres = null)
    }

    /**
     * Returns medium.
     *
     * @param index  index of FO for time
     * @param source FO for time
     * @return medium
     */
    private fun mapMedium(index: Int, source: TimeFO?): Medium {
        return Medium(id = null,
                length = timeMapper.mapBack(source!!),
                number = index + 1)
    }

}
