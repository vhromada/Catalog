package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.ProgramUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

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
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [ProgramRepository]
     */
    @Autowired
    private lateinit var repository: ProgramRepository

    /**
     * Test method for get programs.
     */
    @Test
    fun getPrograms() {
        val programs = repository.findAll()

        ProgramUtils.assertDomainProgramsDeepEquals(expected = ProgramUtils.getPrograms(), actual = programs)

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for get program.
     */
    @Test
    fun getProgram() {
        for (i in 1..ProgramUtils.PROGRAMS_COUNT) {
            val program = repository.findById(i).orElse(null)

            ProgramUtils.assertProgramDeepEquals(expected = ProgramUtils.getProgramDomain(index = i), actual = program)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for add program.
     */
    @Test
    fun add() {
        val program = ProgramUtils.newProgramDomain(id = null)
            .copy(position = ProgramUtils.PROGRAMS_COUNT)
        val expectedProgram = ProgramUtils.newProgramDomain(id = ProgramUtils.PROGRAMS_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        repository.save(program)

        assertSoftly {
            it.assertThat(program.id).isEqualTo(ProgramUtils.PROGRAMS_COUNT + 1)
            it.assertThat(program.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(program.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(program.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(program.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1)!!
        assertThat(addedProgram).isNotNull
        ProgramUtils.assertProgramDeepEquals(expected = expectedProgram, actual = addedProgram)

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT + 1)
    }

    /**
     * Test method for update program.
     */
    @Test
    fun update() {
        val program = ProgramUtils.updateProgram(entityManager = entityManager, id = 1)
        val expectedProgram = ProgramUtils.getProgramDomain(index = 1)
            .updated()
            .copy(position = ProgramUtils.POSITION)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(program)

        val updatedProgram = ProgramUtils.getProgram(entityManager = entityManager, id = 1)
        assertThat(updatedProgram).isNotNull
        ProgramUtils.assertProgramDeepEquals(expected = expectedProgram, actual = updatedProgram!!)

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for remove program.
     */
    @Test
    fun remove() {
        repository.delete(ProgramUtils.getProgram(entityManager = entityManager, id = 1)!!)

        assertThat(ProgramUtils.getProgram(entityManager = entityManager, id = 1)).isNull()

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT - 1)
    }

    /**
     * Test method for remove all programs.
     */
    @Test
    fun removeAll() {
        repository.deleteAll()

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(0)
    }

    /**
     * Test method for get programs for user.
     */
    @Test
    fun findByCreatedUser() {
        val programs = repository.findByCreatedUser(user = AuditUtils.getAudit().createdUser!!)

        ProgramUtils.assertDomainProgramsDeepEquals(expected = ProgramUtils.getPrograms(), actual = programs)

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for get program by id for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..ProgramUtils.PROGRAMS_COUNT) {
            val author = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            ProgramUtils.assertProgramDeepEquals(expected = ProgramUtils.getProgramDomain(index = i), actual = author)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

}
