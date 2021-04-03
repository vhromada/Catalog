package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.SongFO
import com.github.vhromada.catalog.web.utils.SongUtils
import com.github.vhromada.common.mapper.Mapper
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
class SongMapperIntegrationTest {

    /**
     * Instance of [SongMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Song, SongFO>

    /**
     * Test method for [SongMapper.map].
     */
    @Test
    fun map() {
        val song = SongUtils.getSong()
        val songFO = mapper.map(source = song)

        SongUtils.assertSongDeepEquals(expected = song, actual = songFO)
    }

    /**
     * Test method for [SongMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val songFO = SongUtils.getSongFO()
        val song = mapper.mapBack(source = songFO)

        SongUtils.assertSongDeepEquals(expected = songFO, actual = song)
    }

}
