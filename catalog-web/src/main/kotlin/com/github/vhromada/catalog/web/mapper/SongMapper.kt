package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.web.fo.SongFO
import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for songs.
 *
 * @author Vladimir Hromada
 */
@Component("webSongMapper")
class SongMapper(private val timeMapper: Mapper<Int, TimeFO>) : Mapper<Song, SongFO> {

    override fun map(source: Song): SongFO {
        return SongFO(
            id = source.id,
            name = source.name,
            length = timeMapper.map(source = source.length!!),
            note = source.note,
            position = source.position
        )
    }

    override fun mapBack(source: SongFO): Song {
        return Song(
            id = source.id,
            name = source.name,
            length = timeMapper.mapBack(source = source.length!!),
            note = source.note,
            position = source.position
        )
    }

}
