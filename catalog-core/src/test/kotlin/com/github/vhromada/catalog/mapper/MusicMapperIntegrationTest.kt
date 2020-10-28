package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Music] and [Music].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class MusicMapperIntegrationTest {

    /**
     * Instance of [MusicMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Music, com.github.vhromada.catalog.domain.Music>

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
