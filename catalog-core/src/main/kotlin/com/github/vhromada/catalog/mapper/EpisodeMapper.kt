package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeMapper")
class EpisodeMapper : Mapper<Episode, com.github.vhromada.catalog.domain.Episode> {

    override fun map(source: Episode): com.github.vhromada.catalog.domain.Episode {
        return com.github.vhromada.catalog.domain.Episode(
            id = source.id,
            number = source.number!!,
            name = source.name!!,
            length = source.length!!,
            note = source.note,
            position = source.position
        )
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Episode): Episode {
        return Episode(
            id = source.id,
            number = source.number,
            name = source.name,
            length = source.length,
            note = source.note,
            position = source.position
        )
    }

}
