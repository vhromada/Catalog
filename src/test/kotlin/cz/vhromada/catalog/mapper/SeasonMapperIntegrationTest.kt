package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Season
import cz.vhromada.catalog.utils.SeasonUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [cz.vhromada.catalog.domain.Season] and [Season].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class SeasonMapperIntegrationTest {

    /**
     * Instance of [SeasonMapper]
     */
    @Autowired
    private lateinit var mapper: SeasonMapper

    /**
     * Test method for [SeasonMapper.map].
     */
    @Test
    fun map() {
        val season = SeasonUtils.newSeason(1)
        val seasonDomain = mapper.map(season)

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain)
    }

    /**
     * Test method for [SeasonMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val seasonDomain = SeasonUtils.newSeasonDomain(1)
        val season = mapper.mapBack(seasonDomain)

        SeasonUtils.assertSeasonDeepEquals(season, seasonDomain)
    }

}
