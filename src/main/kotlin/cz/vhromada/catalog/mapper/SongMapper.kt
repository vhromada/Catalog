package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.entity.Song
import cz.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for song.
 *
 * @author Vladimir Hromada
 */
@Component("songMapper")
class SongMapper : Mapper<Song, cz.vhromada.catalog.domain.Song> {

    override fun map(source: Song): cz.vhromada.catalog.domain.Song {
        return cz.vhromada.catalog.domain.Song(
                id = source.id,
                name = source.name!!,
                length = source.length!!,
                note = source.note,
                position = source.position)
    }

    override fun mapBack(source: cz.vhromada.catalog.domain.Song): Song {
        return Song(
                id = source.id,
                name = source.name,
                length = source.length,
                note = source.note,
                position = source.position)
    }

}
