package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Music
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicMapper")
class MusicMapper : Mapper<Music, cz.vhromada.catalog.domain.Music> {

    override fun map(source: Music): cz.vhromada.catalog.domain.Music {
        return cz.vhromada.catalog.domain.Music(
                id = source.id,
                name = source.name!!,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!,
                note = source.note,
                position = source.position,
                songs = emptyList())
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Music): Music {
        return Music(
                id = source.id,
                name = source.name,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount,
                note = source.note,
                position = source.position)
    }

}
