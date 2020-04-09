package cz.vhromada.catalog.facade

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.entity.Program
import cz.vhromada.catalog.facade.impl.ProgramFacadeImpl
import cz.vhromada.catalog.utils.ProgramUtils
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.facade.MovableParentFacadeTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [ProgramFacade].
 *
 * @author Vladimir Hromada
 */
class ProgramFacadeTest : MovableParentFacadeTest<Program, cz.vhromada.catalog.domain.Program>() {

    /**
     * Test method for [ProgramFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val program1 = ProgramUtils.newProgramDomain(1)
        val program2 = ProgramUtils.newProgramDomain(2)
        val expectedCount = program1.mediaCount + program2.mediaCount

        whenever(service.getAll()).thenReturn(listOf(program1, program2))

        val result = (getFacade() as ProgramFacade).getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedCount)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(accountProvider, mapper, validator)
    }

    override fun getFacade(): MovableParentFacade<Program> {
        return ProgramFacadeImpl(service, accountProvider, timeProvider, mapper, validator)
    }

    override fun newEntity(id: Int?): Program {
        return ProgramUtils.newProgram(id)
    }

    override fun newDomain(id: Int?): cz.vhromada.catalog.domain.Program {
        return ProgramUtils.newProgramDomain(id)
    }

    override fun anyDomain(): cz.vhromada.catalog.domain.Program {
        return any()
    }

    override fun anyEntity(): Program {
        return any()
    }

}
