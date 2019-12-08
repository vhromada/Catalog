package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Episode
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeMapper")
class EpisodeMapper : Mapper<Episode, cz.vhromada.catalog.domain.Episode> {

    override fun map(source: Episode): cz.vhromada.catalog.domain.Episode {
        return cz.vhromada.catalog.domain.Episode(
                id = source.id,
                number = source.number!!,
                name = source.name!!,
                length = source.length!!,
                note = source.note,
                position = source.position)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Episode): Episode {
        return Episode(
                id = source.id,
                number = source.number,
                name = source.name,
                length = source.length,
                note = source.note,
                position = source.position)
    }

}
