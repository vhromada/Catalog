package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.ProgramUtils
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * A class represents integration test for class [ProgramFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class ProgramFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [ProgramFacade]
     */
    @Autowired
    private lateinit var facade: ProgramFacade

    /**
     * Test method for [ProgramFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..ProgramUtils.PROGRAMS_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            ProgramUtils.assertProgramDeepEquals(expected = ProgramUtils.getProgramDomain(index = i), actual = result.data!!)
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PROGRAM_NOT_EXIST_EVENT))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update].
     */
    @Test
    fun update() {
        val program = ProgramUtils.newProgram(id = 1)
        val expectedProgram = ProgramUtils.newProgramDomain(id = 1)
            .fillAudit(audit = AuditUtils.updatedAudit())

        val result = facade.update(data = program)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        ProgramUtils.assertProgramDeepEquals(expected = expectedProgram, actual = ProgramUtils.getProgram(entityManager = entityManager, id = 1)!!)

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null ID.
     */
    @Test
    fun updateNullId() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(id = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_ID_NULL", message = "ID mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null position.
     */
    @Test
    fun updateNullPosition() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(position = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null name.
     */
    @Test
    fun updateNullName() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(name = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(name = "")

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null URL to english Wikipedia about program.
     */
    @Test
    fun updateNullWikiEn() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(wikiEn = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_EN_NULL", message = "URL to english Wikipedia page about program mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null URL to czech Wikipedia about program.
     */
    @Test
    fun updateNullWikiCz() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(wikiCz = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about program mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null count of media.
     */
    @Test
    fun updateNullMediaCount() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(mediaCount = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with not positive count of media.
     */
    @Test
    fun updateNotPositiveMediaCount() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(mediaCount = 0)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null format.
     */
    @Test
    fun updateNullFormat() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(format = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_FORMAT_NULL", message = "Format mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null other data.
     */
    @Test
    fun updateNullOtherData() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(otherData = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_OTHER_DATA_NULL", message = "Other data mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with null note.
     */
    @Test
    fun updateNullNote() {
        val program = ProgramUtils.newProgram(id = 1)
            .copy(note = null)

        val result = facade.update(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.update] with program with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = ProgramUtils.newProgram(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PROGRAM_NOT_EXIST_EVENT))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(ProgramUtils.getProgram(entityManager = entityManager, id = 1)).isNull()

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT - 1)
    }

    /**
     * Test method for [ProgramFacade.remove] with program with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PROGRAM_NOT_EXIST_EVENT))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        val expectedProgram = ProgramUtils.getProgramDomain(index = ProgramUtils.PROGRAMS_COUNT)
            .copy(id = ProgramUtils.PROGRAMS_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.duplicate(id = ProgramUtils.PROGRAMS_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        ProgramUtils.assertProgramDeepEquals(expected = expectedProgram, actual = ProgramUtils.getProgram(entityManager = entityManager, id = ProgramUtils.PROGRAMS_COUNT + 1)!!)

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT + 1)
    }

    /**
     * Test method for [ProgramFacade.duplicate] with program with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PROGRAM_NOT_EXIST_EVENT))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val program1 = ProgramUtils.getProgramDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val program2 = ProgramUtils.getProgramDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        ProgramUtils.assertProgramDeepEquals(expected = program1, actual = ProgramUtils.getProgram(entityManager = entityManager, id = 1)!!)
        ProgramUtils.assertProgramDeepEquals(expected = program2, actual = ProgramUtils.getProgram(entityManager = entityManager, id = 2)!!)
        for (i in 3..ProgramUtils.PROGRAMS_COUNT) {
            ProgramUtils.assertProgramDeepEquals(expected = ProgramUtils.getProgramDomain(i), actual = ProgramUtils.getProgram(entityManager = entityManager, id = i)!!)
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.moveUp] with not movable program.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOT_MOVABLE", message = "Program can't be moved up.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.moveUp] with program with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PROGRAM_NOT_EXIST_EVENT))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val program1 = ProgramUtils.getProgramDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val program2 = ProgramUtils.getProgramDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        ProgramUtils.assertProgramDeepEquals(expected = program1, actual = ProgramUtils.getProgram(entityManager = entityManager, id = 1)!!)
        ProgramUtils.assertProgramDeepEquals(expected = program2, actual = ProgramUtils.getProgram(entityManager = entityManager, id = 2)!!)
        for (i in 3..ProgramUtils.PROGRAMS_COUNT) {
            ProgramUtils.assertProgramDeepEquals(expected = ProgramUtils.getProgramDomain(i), actual = ProgramUtils.getProgram(entityManager = entityManager, id = i)!!)
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.moveDown] with not movable program.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = ProgramUtils.PROGRAMS_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOT_MOVABLE", message = "Program can't be moved down.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.moveDown] with program with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PROGRAM_NOT_EXIST_EVENT))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.newData].
     */
    @Test
    fun newData() {
        val result = facade.newData()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(0)
    }

    /**
     * Test method for [ProgramFacade.getAll].
     */
    @Test
    fun getAll() {
        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isNotNull
            it.assertThat(result.events()).isEmpty()
        }
        ProgramUtils.assertProgramListDeepEquals(expected = ProgramUtils.getPrograms(), actual = result.data!!)

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedProgram = ProgramUtils.newProgramDomain(id = ProgramUtils.PROGRAMS_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.add(ProgramUtils.newProgram(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        ProgramUtils.assertProgramDeepEquals(expected = expectedProgram, actual = ProgramUtils.getProgram(entityManager = entityManager, id = ProgramUtils.PROGRAMS_COUNT + 1)!!)

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT + 1)
    }

    /**
     * Test method for [ProgramFacade.add] with program with not null ID.
     */
    @Test
    fun addNotNullId() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with null name.
     */
    @Test
    fun addNullName() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(name = null)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(name = "")

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with null URL to english Wikipedia about program.
     */
    @Test
    fun addNullWikiEn() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(wikiEn = null)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_EN_NULL", message = "URL to english Wikipedia page about program mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with null URL to czech Wikipedia about program.
     */
    @Test
    fun addNullWikiCz() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(wikiCz = null)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about program mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with null count of media.
     */
    @Test
    fun addNullMediaCount() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(mediaCount = null)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with not positive count of media.
     */
    @Test
    fun addNotPositiveMediaCount() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(mediaCount = 0)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with null format.
     */
    @Test
    fun addNullFormat() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(format = null)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_FORMAT_NULL", message = "Format mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with null other data.
     */
    @Test
    fun addNullOtherData() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(otherData = null)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_OTHER_DATA_NULL", message = "Other data mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.add] with program with null note.
     */
    @Test
    fun addNullNote() {
        val program = ProgramUtils.newProgram(id = null)
            .copy(note = null)

        val result = facade.add(data = program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PROGRAM_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        for (i in 1..ProgramUtils.PROGRAMS_COUNT) {
            val expectedProgram = ProgramUtils.getProgramDomain(index = i)
                .copy(position = i - 1)
                .fillAudit(audit = AuditUtils.updatedAudit())
            ProgramUtils.assertProgramDeepEquals(expected = expectedProgram, actual = ProgramUtils.getProgram(entityManager = entityManager, id = i)!!)
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    /**
     * Test method for [ProgramFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val result = facade.getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(600)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(ProgramUtils.getProgramsCount(entityManager = entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT)
    }

    companion object {

        /**
         * Event for not existing program
         */
        private val PROGRAM_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "PROGRAM_NOT_EXIST", message = "Program doesn't exist.")

    }

}
