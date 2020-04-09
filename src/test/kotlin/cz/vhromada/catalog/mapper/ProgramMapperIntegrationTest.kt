package cz.vhromada.catalog.mapper

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Program
import cz.vhromada.catalog.utils.ProgramUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [cz.vhromada.catalog.domain.Program] and [Program].
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
