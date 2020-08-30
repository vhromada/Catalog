package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for song.
 *
 * @author Vladimir Hromada
 */
@Component("songMapper")
class SongMapper : Mapper<Song, com.github.vhromada.catalog.domain.Song> {

    override fun map(source: Song): com.github.vhromada.catalog.domain.Song {
        return com.github.vhromada.catalog.domain.Song(
                id = source.id,
                name = source.name!!,
                length = source.length!!,
                note = source.note,
                position = source.position,
                audit = null)
    }

    override fun mapBack(source: com.github.vhromada.catalog.domain.Song): Song {
        return Song(
                id = source.id,
                name = source.name,
                length = source.length,
                note = source.note,
                position = source.position)
    }

}
