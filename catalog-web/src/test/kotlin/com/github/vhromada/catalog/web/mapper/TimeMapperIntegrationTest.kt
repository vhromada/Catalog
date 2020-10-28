package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.common.TimeUtils
import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Int] and [TimeFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class TimeMapperIntegrationTest {

    /**
     * Instance of [TimeMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Int, TimeFO>

    /**
     * Test method for [TimeMapper.map].
     */
    @Test
    fun map() {
        val length = 100

        val time = mapper.map(length)

        TimeUtils.assertTimeDeepEquals(time, length)
    }

    /**
     * Test method for [TimeMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val time = TimeUtils.getTimeFO()

        val length = mapper.mapBack(time)

        TimeUtils.assertTimeDeepEquals(time, length)
    }

}
