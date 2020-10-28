package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Season] and [Season].
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
    private lateinit var mapper: Mapper<Season, com.github.vhromada.catalog.domain.Season>

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
