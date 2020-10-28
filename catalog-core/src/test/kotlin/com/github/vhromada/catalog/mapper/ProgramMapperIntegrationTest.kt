package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.utils.ProgramUtils
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [com.github.vhromada.catalog.domain.Program] and [Program].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class ProgramMapperIntegrationTest {

    /**
     * Instance of [ProgramMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<Program, com.github.vhromada.catalog.domain.Program>

    /**
     * Test method for [ProgramMapper.map].
     */
    @Test
    fun map() {
        val program = ProgramUtils.newProgram(1)
        val programDomain = mapper.map(program)

        ProgramUtils.assertProgramDeepEquals(program, programDomain)
    }

    /**
     * Test method for [ProgramMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val programDomain = ProgramUtils.newProgramDomain(1)
        val program = mapper.mapBack(programDomain)

        ProgramUtils.assertProgramDeepEquals(program, programDomain)
    }

}
