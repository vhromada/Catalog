package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.common.ProgramUtils
import com.github.vhromada.catalog.web.fo.ProgramFO
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [Program] and [ProgramFO].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class ProgramMapperIntegrationTest {

    /**
     * Instance of [ProgramMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Program, ProgramFO>

    /**
     * Test method for [ProgramMapper.map].
     */
    @Test
    fun map() {
        val program = ProgramUtils.getProgram()

        val programFO = mapper.map(program)

        ProgramUtils.assertProgramDeepEquals(programFO, program)
    }

    /**
     * Test method for [ProgramMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val programFO = ProgramUtils.getProgramFO()

        val program = mapper.mapBack(programFO)

        ProgramUtils.assertProgramDeepEquals(programFO, program)
    }

}
