package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.facade.impl.ProgramFacadeImpl
import com.github.vhromada.catalog.utils.ProgramUtils
import com.github.vhromada.common.facade.MovableParentFacade
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableParentFacadeTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [ProgramFacade].
 *
 * @author Vladimir Hromada
 */
class ProgramFacadeTest : MovableParentFacadeTest<Program, com.github.vhromada.catalog.domain.Program>() {

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

    override fun newDomain(id: Int?): com.github.vhromada.catalog.domain.Program {
        return ProgramUtils.newProgramDomain(id)
    }

    override fun anyDomain(): com.github.vhromada.catalog.domain.Program {
        return any()
    }

    override fun anyEntity(): Program {
        return any()
    }

}
