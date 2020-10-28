package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.common.SeasonUtils
import com.github.vhromada.catalog.web.fo.SeasonFO
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Season] and [SeasonFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class SeasonMapperIntegrationTest {

    /**
     * Instance of [SeasonMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Season, SeasonFO>

    /**
     * Test method for [SeasonMapper.map].
     */
    @Test
    fun map() {
        val season = SeasonUtils.getSeason()

        val seasonFO = mapper.map(season)

        SeasonUtils.assertSeasonDeepEquals(seasonFO, season)
    }

    /**
     * Test method for [SeasonMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val seasonFO = SeasonUtils.getSeasonFO()

        val season = mapper.mapBack(seasonFO)

        SeasonUtils.assertSeasonDeepEquals(seasonFO, season)
    }

}
