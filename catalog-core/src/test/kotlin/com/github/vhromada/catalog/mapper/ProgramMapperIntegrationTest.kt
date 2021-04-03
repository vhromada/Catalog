package com.github.vhromada.catalog.mapper

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.utils.ProgramUtils
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
    private lateinit var mapper: ProgramMapper

    /**
     * Test method for [ProgramMapper.map].
     */
    @Test
    fun map() {
        val program = ProgramUtils.newProgram(id = 1)
        val programDomain = mapper.map(program)

        ProgramUtils.assertProgramDeepEquals(expected = program, actual = programDomain)
    }

    /**
     * Test method for [ProgramMapper.mapBack].
     */
    @Test
    fun mapBack() {
        val programDomain = ProgramUtils.newProgramDomain(id = 1)
        val program = mapper.mapBack(programDomain)

        ProgramUtils.assertProgramDeepEquals(expected = programDomain, actual = program)
    }

}
