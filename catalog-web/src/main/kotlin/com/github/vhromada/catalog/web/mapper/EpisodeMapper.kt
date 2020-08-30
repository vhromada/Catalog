package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.web.fo.EpisodeFO

/**
 * An interface represents mapper for episodes.
 *
 * @author Vladimir Hromada
 */
interface EpisodeMapper {

    /**
     * Returns FO for episode.
     *
     * @param source episode
     * @return FO for episode
     */
    fun map(source: Episode): EpisodeFO

    /**
     * Returns episode.
     *
     * @param source FO for episode
     * @return episode
     */
    fun mapBack(source: EpisodeFO): Episode

}
