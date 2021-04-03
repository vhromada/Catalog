package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.EpisodeFO
import com.github.vhromada.catalog.web.utils.EpisodeUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Episode] and [EpisodeFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class EpisodeMapperIntegrationTest {

    /**
     * Instance of [EpisodeMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Episode, EpisodeFO>

    /**
     * Test method for [EpisodeMapper.map].
     */
    @Test
    fun map() {
        val episode = EpisodeUtils.getEpisode()
        val episodeFO = mapper.map(source = episode)

        EpisodeUtils.assertEpisodeDeepEquals(expected = episode, actual = episodeFO)
    }

    /**
     * Test method for [EpisodeMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val episodeFO = EpisodeUtils.getEpisodeFO()
        val episode = mapper.mapBack(source = episodeFO)

        EpisodeUtils.assertEpisodeDeepEquals(expected = episodeFO, actual = episode)
    }

}
