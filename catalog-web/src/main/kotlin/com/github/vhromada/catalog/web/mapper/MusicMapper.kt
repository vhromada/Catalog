package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.web.fo.MusicFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for music.
 *
 * @author Vladimir Hromada
 */
@Component("webMusicMapper")
class MusicMapper : Mapper<Music, MusicFO> {

    override fun map(source: Music): MusicFO {
        return MusicFO(
            id = source.id,
            name = source.name,
            wikiEn = source.wikiEn,
            wikiCz = source.wikiCz,
            mediaCount = source.mediaCount!!.toString(),
            note = source.note,
            position = source.position
        )
    }

    override fun mapBack(source: MusicFO): Music {
        return Music(
            id = source.id,
            name = source.name,
            wikiEn = source.wikiEn,
            wikiCz = source.wikiCz,
            mediaCount = source.mediaCount!!.toInt(),
            note = source.note,
            position = source.position
        )
    }

}
