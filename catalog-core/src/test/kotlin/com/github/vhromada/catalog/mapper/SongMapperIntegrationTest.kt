package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.utils.SongUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Song] and [Song].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class SongMapperIntegrationTest {

    /**
     * Instance of [SongMapper]
     */
    @Autowired
    private lateinit var mapper: SongMapper

    /**
     * Test method for [SongMapper.map].
     */
    @Test
    fun map() {
        val song = SongUtils.newSong(id = 1)
        val songDomain = mapper.map(song)

        SongUtils.assertSongDeepEquals(expected = song, actual = songDomain)
    }

    /**
     * Test method for [SongMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val songDomain = SongUtils.newSongDomain(id = 1)
        val song = mapper.mapBack(songDomain)

        SongUtils.assertSongDeepEquals(expected = songDomain, actual = song)
    }

}
