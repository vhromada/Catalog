package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Program
import cz.vhromada.catalog.facade.ProgramFacade
import cz.vhromada.catalog.utils.ProgramUtils
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Severity
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [ProgramFacadeImpl].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class ProgramFacadeImplIntegrationTest : MovableParentFacadeIntegrationTest<Program, cz.vhromada.catalog.domain.Program>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [ProgramFacade]
     */
    @Autowired
    private lateinit var facade: ProgramFacade

    /**
     * Test method for [ProgramFacade.add] with program with null name.
     */
    @Test
    fun addNullName() {
        val program = newData(null)
                .copy(name = null)

        val result = facade.add(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.add] with program with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val program = newData(null)
                .copy(name = "")

        val result = facade.add(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.add] with program with null URL to english Wikipedia about program.
     */
    @Test
    fun addNullWikiEn() {
        val program = newData(null)
                .copy(wikiEn = null)

        val result = facade.add(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL", "URL to english Wikipedia page about program mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.add] with program with null URL to czech Wikipedia about program.
     */
    @Test
    fun addNullWikiCz() {
        val program = newData(null)
                .copy(wikiCz = null)

        val result = facade.add(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL", "URL to czech Wikipedia page about program mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.add] with program with null count of media.
     */
    @Test
    fun addNullMediaCount() {
        val program = newData(null)
                .copy(mediaCount = null)

        val result = facade.add(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.add] with program with not positive count of media.
     */
    @Test
    fun addNotPositiveMediaCount() {
        val program = newData(null)
                .copy(mediaCount = 0)

        val result = facade.add(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.add] with program with null other data.
     */
    @Test
    fun addNullOtherData() {
        val program = newData(null)
                .copy(otherData = null)

        val result = facade.add(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.add] with program with null note.
     */
    @Test
    fun addNullNote() {
        val program = newData(null)
                .copy(note = null)

        val result = facade.add(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.update] with program with null name.
     */
    @Test
    fun updateNullName() {
        val program = newData(1)
                .copy(name = null)

        val result = facade.update(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.update] with program with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val program = newData(1)
                .copy(name = "")

        val result = facade.update(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.update] with program with null URL to english Wikipedia about program.
     */
    @Test
    fun updateNullWikiEn() {
        val program = newData(1)
                .copy(wikiEn = null)

        val result = facade.update(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL", "URL to english Wikipedia page about program mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.update] with program with null URL to czech Wikipedia about program.
     */
    @Test
    fun updateNullWikiCz() {
        val program = newData(1)
                .copy(wikiCz = null)

        val result = facade.update(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL", "URL to czech Wikipedia page about program mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.update] with program with null count of media.
     */
    @Test
    fun updateNullMediaCount() {
        val program = newData(1)
                .copy(mediaCount = null)

        val result = facade.update(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.update] with program with not positive count of media.
     */
    @Test
    fun updateNotPositiveMediaCount() {
        val program = newData(1)
                .copy(mediaCount = 0)

        val result = facade.update(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.update] with program with null other data.
     */
    @Test
    fun updateNullOtherData() {
        val program = newData(1)
                .copy(otherData = null)

        val result = facade.update(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ProgramFacade.update] with program with null note.
     */
    @Test
    fun updateNullNote() {
        val program = newData(1)
                .copy(note = null)

        val result = facade.update(program)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
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

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableParentFacade<Program> {
        return facade
    }

    override fun getDefaultDataCount(): Int {
        return ProgramUtils.PROGRAMS_COUNT
    }

    override fun getRepositoryDataCount(): Int {
        return ProgramUtils.getProgramsCount(entityManager)
    }

    override fun getDataList(): List<cz.vhromada.catalog.domain.Program> {
        return ProgramUtils.getPrograms()
    }

    override fun getDomainData(index: Int): cz.vhromada.catalog.domain.Program {
        return ProgramUtils.getProgram(index)
    }

    override fun newData(id: Int?): Program {
        return ProgramUtils.newProgram(id)
    }

    override fun newDomainData(id: Int): cz.vhromada.catalog.domain.Program {
        return ProgramUtils.newProgramDomain(id)
    }

    override fun getRepositoryData(id: Int): cz.vhromada.catalog.domain.Program? {
        return ProgramUtils.getProgram(entityManager, id)
    }

    override fun getName(): String {
        return "Program"
    }

    override fun clearReferencedData() {}

    override fun assertDataListDeepEquals(expected: List<Program>, actual: List<cz.vhromada.catalog.domain.Program>) {
        ProgramUtils.assertProgramListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Program, actual: cz.vhromada.catalog.domain.Program) {
        ProgramUtils.assertProgramDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: cz.vhromada.catalog.domain.Program, actual: cz.vhromada.catalog.domain.Program) {
        ProgramUtils.assertProgramDeepEquals(expected, actual)
    }

}
