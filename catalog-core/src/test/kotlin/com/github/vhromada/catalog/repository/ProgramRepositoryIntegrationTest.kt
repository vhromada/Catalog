package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.ProgramUtils
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [ProgramRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class ProgramRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [ProgramRepository]
     */
    @Autowired
    private lateinit var programRepository: ProgramRepository

    /**
     * Test method for get programs.
     */
    @Test
    fun getPrograms() {
        val programs = programRepository.findAll()

        ProgramUtils.assertProgramsDeepEquals(ProgramUtils.getPrograms(), programs)

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for get program.
     */
    @Test
    @Suppress("UsePropertyAccessSyntax")
    fun getProgram() {
        for (i in 1..ProgramUtils.PROGRAMS_COUNT) {
            val program = programRepository.findById(i).orElse(null)

            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), program)
        }

        assertThat(programRepository.findById(Integer.MAX_VALUE).isPresent).isFalse()

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for add program.
     */
    @Test
    fun add() {
        val audit = AuditUtils.getAudit()
        val program = ProgramUtils.newProgramDomain(null)
                .copy(position = ProgramUtils.PROGRAMS_COUNT, audit = audit)

        programRepository.save(program)

        assertThat(program.id).isEqualTo(ProgramUtils.PROGRAMS_COUNT + 1)

        val addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1)!!
        val expectedAddProgram = ProgramUtils.newProgramDomain(null)
                .copy(id = ProgramUtils.PROGRAMS_COUNT + 1, position = ProgramUtils.PROGRAMS_COUNT, audit = audit)
        ProgramUtils.assertProgramDeepEquals(expectedAddProgram, addedProgram)

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT + 1)
    }

    /**
     * Test method for update program.
     */
    @Test
    fun update() {
        val program = ProgramUtils.updateProgram(entityManager, 1)

        programRepository.save(program)

        val updatedProgram = ProgramUtils.getProgram(entityManager, 1)!!
        val expectedUpdatedProgram = ProgramUtils.getProgram(1)
                .updated()
                .copy(position = ProgramUtils.POSITION)
        ProgramUtils.assertProgramDeepEquals(expectedUpdatedProgram, updatedProgram)

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for remove program.
     */
    @Test
    fun remove() {
        programRepository.delete(ProgramUtils.getProgram(entityManager, 1)!!)

        assertThat(ProgramUtils.getProgram(entityManager, 1)).isNull()

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT - 1)
    }

    /**
     * Test method for remove all programs.
     */
    @Test
    fun removeAll() {
        programRepository.deleteAll()

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(0)
    }

    /**
     * Test method for get programs for user.
     */
    @Test
    fun findByAuditCreatedUser() {
        val programs = programRepository.findByAuditCreatedUser(AuditUtils.getAudit().createdUser)

        ProgramUtils.assertProgramsDeepEquals(ProgramUtils.getPrograms(), programs)

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

}
