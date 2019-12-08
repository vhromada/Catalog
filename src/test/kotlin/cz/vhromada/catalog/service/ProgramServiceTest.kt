package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.domain.Program
import cz.vhromada.catalog.repository.ProgramRepository
import cz.vhromada.catalog.utils.ProgramUtils
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import org.mockito.Mock
import org.springframework.data.jpa.repository.JpaRepository

/**
 * A class represents test for class [ProgramService].
 *
 * @author Vladimir Hromada
 */
class ProgramServiceTest : MovableServiceTest<Program>() {

    /**
     * Instance of [ProgramRepository]
     */
    @Mock
    private lateinit var repository: ProgramRepository

    override fun getRepository(): JpaRepository<Program, Int> {
        return repository
    }

    override fun getService(): MovableService<Program> {
        return ProgramService(repository, cache)
    }

    override fun getCacheKey(): String {
        return "programs"
    }

    override fun getItem1(): Program {
        return ProgramUtils.newProgramDomain(1)
    }

    override fun getItem2(): Program {
        return ProgramUtils.newProgramDomain(2)
    }

    override fun getAddItem(): Program {
        return ProgramUtils.newProgramDomain(null)
    }

    override fun getCopyItem(): Program {
        val program = ProgramUtils.newProgramDomain(null)
        program.position = 0

        return program
    }

    override fun anyItem(): Program {
        return any()
    }

    override fun argumentCaptorItem(): KArgumentCaptor<Program> {
        return argumentCaptor()
    }

    override fun assertDataDeepEquals(expected: Program, actual: Program) {
        ProgramUtils.assertProgramDeepEquals(expected, actual)
    }

}
