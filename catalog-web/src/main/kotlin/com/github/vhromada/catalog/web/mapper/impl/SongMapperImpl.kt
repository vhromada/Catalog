package com.github.vhromada.catalog.web.mapper.impl

import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.web.fo.SongFO
import com.github.vhromada.catalog.web.mapper.SongMapper
import com.github.vhromada.catalog.web.mapper.TimeMapper
import org.springframework.stereotype.Component

/**
 * A class represents implementation of mapper for songs.
 *
 * @author Vladimir Hromada
 */
@Component("webSongMapper")
class SongMapperImpl(private val timeMapper: TimeMapper) : SongMapper {

    override fun map(source: Song): SongFO {
        return SongFO(id = source.id,
                name = source.name,
                length = timeMapper.map(source.length!!),
                note = source.note,
                position = source.position)
    }

    override fun mapBack(source: SongFO): Song {
        return Song(id = source.id,
                name = source.name,
                length = timeMapper.mapBack(source.length!!),
                note = source.note,
                position = source.position)
    }

}
