package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.utils.MusicUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [cz.vhromada.catalog.domain.Music] and [Music].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class MusicMapperTest {

    /**
     * Instance of [MusicMapper]
     */
    @Autowired
    private lateinit var mapper: MusicMapper

    /**
     * Test method for [MusicMapper.map].
     */
    @Test
    fun map() {
        val music = MusicUtils.newMusic(1)
        val musicDomain = mapper.map(music)

        MusicUtils.assertMusicDeepEquals(music, musicDomain)
    }

    /**
     * Test method for [MusicMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val musicDomain = MusicUtils.newMusicDomain(1)
        val music = mapper.mapBack(musicDomain)

        MusicUtils.assertMusicDeepEquals(music, musicDomain)
    }

}
