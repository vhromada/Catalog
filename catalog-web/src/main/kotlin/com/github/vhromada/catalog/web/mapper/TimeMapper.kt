package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.web.fo.TimeFO

/**
 * An interface represents mapper for time.
 *
 * @author Vladimir Hromada
 */
interface TimeMapper {

    /**
     * Returns FO for time.
     *
     * @param source length
     * @return time
     */
    fun map(source: Int): TimeFO

    /**
     * Returns length.
     *
     * @param source FO for time
     * @return length
     */
    fun mapBack(source: TimeFO): Int

}
