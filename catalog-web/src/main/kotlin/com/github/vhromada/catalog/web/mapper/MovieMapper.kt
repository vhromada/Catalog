package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.web.fo.MovieFO

/**
 * An interface represents mapper for movies.
 *
 * @author Vladimir Hromada
 */
interface MovieMapper {

    /**
     * Returns FO for movie.
     *
     * @param source movie
     * @return FO for movie
     */
    fun map(source: Movie): MovieFO

    /**
     * Returns movie.
     *
     * @param source FO for movie
     * @return movie
     */
    fun mapBack(source: MovieFO): Movie

}
