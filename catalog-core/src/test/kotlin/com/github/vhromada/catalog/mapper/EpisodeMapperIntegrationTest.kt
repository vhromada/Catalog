package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Episode] and [Episode].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class EpisodeMapperIntegrationTest {

    /**
     * Instance of [EpisodeMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Episode, com.github.vhromada.catalog.domain.Episode>

    /**
     * Test method for [EpisodeMapper.map].
     */
    @Test
    fun map() {
        val episode = EpisodeUtils.newEpisode(1)
        val episodeDomain = mapper.map(episode)

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain)
    }

    /**
     * Test method for [EpisodeMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val episodeDomain = EpisodeUtils.newEpisodeDomain(1)
        val episode = mapper.mapBack(episodeDomain)

        EpisodeUtils.assertEpisodeDeepEquals(episode, episodeDomain)
    }

}
