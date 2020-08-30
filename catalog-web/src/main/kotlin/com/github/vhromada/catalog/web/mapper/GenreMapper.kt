package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.web.fo.GenreFO

/**
 * An interface represents mapper for genres.
 *
 * @author Vladimir Hromada
 */
interface GenreMapper {

    /**
     * Returns FO for genre.
     *
     * @param source genre
     * @return FO for genre
     */
    fun map(source: Genre): GenreFO

    /**
     * Returns genre.
     *
     * @param source FO for genre
     * @return genre
     */
    fun mapBack(source: GenreFO): Genre

}
