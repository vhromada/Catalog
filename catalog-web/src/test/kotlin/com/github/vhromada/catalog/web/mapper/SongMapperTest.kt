package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.common.SongUtils
import com.github.vhromada.catalog.web.fo.SongFO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Song] and [SongFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class SongMapperTest {

    /**
     * Mapper for songs
     */
    @Autowired
    private lateinit var mapper: SongMapper

    /**
     * Test method for [SongMapper.map].
     */
    @Test
    fun map() {
        val song = SongUtils.getSong()

        val songFO = mapper.map(song)

        SongUtils.assertSongDeepEquals(songFO, song)
    }

    /**
     * Test method for [SongMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val songFO = SongUtils.getSongFO()

        val song = mapper.mapBack(songFO)

        SongUtils.assertSongDeepEquals(songFO, song)
    }

}
