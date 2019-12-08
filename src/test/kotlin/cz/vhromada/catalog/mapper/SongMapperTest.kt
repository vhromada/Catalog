package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Song
import cz.vhromada.catalog.utils.SongUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [cz.vhromada.catalog.domain.Song] and [Song].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class SongMapperTest {

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
        val song = SongUtils.newSong(1)
        val songDomain = mapper.map(song)

        SongUtils.assertSongDeepEquals(song, songDomain)
    }

    /**
     * Test method for [SongMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val songDomain = SongUtils.newSongDomain(1)
        val song = mapper.mapBack(songDomain)

        SongUtils.assertSongDeepEquals(song, songDomain)
    }

}
