package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicMapper")
class MusicMapper : Mapper<Music, com.github.vhromada.catalog.domain.Music> {

    override fun map(source: Music): com.github.vhromada.catalog.domain.Music {
        return com.github.vhromada.catalog.domain.Music(
                id = source.id,
                name = source.name!!,
                wikiEn = source.wikiEn,
                wikiCz = source.wikiCz,
                mediaCount = source.mediaCount!!,
                note = source.note,
                position = source.position,
                songs = emptyList(),
                audit = null)
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Music): Music {
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
