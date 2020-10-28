package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.web.fo.EpisodeFO
import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("webEpisodeMapper")
class EpisodeMapper(private val timeMapper: Mapper<Int, TimeFO>) : Mapper<Episode, EpisodeFO> {

    override fun map(source: Episode): EpisodeFO {
        return EpisodeFO(id = source.id,
                number = source.number!!.toString(),
                length = timeMapper.map(source.length!!),
                name = source.name,
                note = source.note,
                position = source.position)
    }

    override fun mapBack(source: EpisodeFO): Episode {
        return Episode(id = source.id,
                number = source.number!!.toInt(),
                length = timeMapper.mapBack(source.length!!),
                name = source.name,
                note = source.note,
                position = source.position)
    }

}
