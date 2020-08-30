package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.common.MusicUtils
import com.github.vhromada.catalog.web.fo.MusicFO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Music] and [MusicFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class MusicMapperTest {

    /**
     * Mapper for music
     */
    @Autowired
    private lateinit var mapper: MusicMapper

    /**
     * Test method for [MusicMapper.map].
     */
    @Test
    fun map() {
        val music = MusicUtils.getMusic()

        val musicFO = mapper.map(music)

        MusicUtils.assertMusicDeepEquals(musicFO, music)
    }

    /**
     * Test method for [MusicMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val musicFO = MusicUtils.getMusicFO()

        val music = mapper.mapBack(musicFO)

        MusicUtils.assertMusicDeepEquals(musicFO, music)
    }

}
