package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.web.fo.ShowFO

/**
 * An interface represents mapper for shows.
 *
 * @author Vladimir Hromada
 */
interface ShowMapper {

    /**
     * Returns FO for show.
     *
     * @param source show
     * @return FO for show
     */
    fun map(source: Show): ShowFO

    /**
     * Returns show.
     *
     * @param source FO for show
     * @return show
     */
    fun mapBack(source: ShowFO): Show

}
