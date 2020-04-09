package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Episode
import cz.vhromada.catalog.utils.EpisodeUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [cz.vhromada.catalog.domain.Episode] and [Episode].
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
    private lateinit var mapper: EpisodeMapper

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
