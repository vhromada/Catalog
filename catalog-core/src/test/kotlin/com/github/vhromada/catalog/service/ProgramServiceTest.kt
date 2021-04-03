package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Program
import com.github.vhromada.catalog.repository.ProgramRepository
import com.github.vhromada.catalog.utils.ProgramUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.provider.AccountProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

/**
 * A class represents test for class [ProgramService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class ProgramServiceTest {

    /**
     * Instance of [ProgramRepository]
     */
    @Mock
    private lateinit var repository: ProgramRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [ProgramService]
     */
    private lateinit var service: ProgramService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = ProgramService(programRepository = repository, accountProvider = accountProvider)
    }

    /**
     * Test method for [ProgramService.get] with existing program for admin.
     */
    @Test
    fun getExistingAdmin() {
        val program = ProgramUtils.newProgramDomain(id = 1)

        whenever(repository.findById(any())).thenReturn(Optional.of(program))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = program.id!!)

        assertThat(result).contains(program)

        verify(repository).findById(program.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.get] with existing program for account.
     */
    @Test
    fun getExistingAccount() {
        val program = ProgramUtils.newProgramDomain(id = 1)

        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(program))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = program.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(program)

        verify(repository).findByIdAndCreatedUser(id = program.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.get] with not existing program for admin.
     */
    @Test
    fun getNotExistingAdmin() {
        whenever(repository.findById(any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(repository).findById(Int.MAX_VALUE)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.get] with not existing program for account.
     */
    @Test
    fun getNotExistingAccount() {
        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(repository).findByIdAndCreatedUser(id = Int.MAX_VALUE, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.add].
     */
    @Test
    fun add() {
        val program = ProgramUtils.newProgramDomain(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = program)

        assertSoftly {
            it.assertThat(program.id).isEqualTo(1)
            it.assertThat(program.position).isEqualTo(2)
        }

        verify(repository, times(2)).save(program)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(program)
    }

    /**
     * Test method for [ProgramService.update].
     */
    @Test
    fun update() {
        val program = ProgramUtils.newProgramDomain(id = 1)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = program)

        verify(repository).save(program)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(program)
    }

    /**
     * Test method for [ProgramService.remove].
     */
    @Test
    fun remove() {
        val program = ProgramUtils.newProgramDomain(id = 1)

        service.remove(data = program)

        verify(repository).delete(program)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
    }

    /**
     * Test method for [ProgramService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Program>()
        val expectedProgram = ProgramUtils.newProgramDomain(id = 1)
            .copy(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = ProgramUtils.newProgramDomain(id = 1))

        verify(repository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        ProgramUtils.assertProgramDeepEquals(expected = expectedProgram, actual = result)
    }

    /**
     * Test method for [ProgramService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val program1 = ProgramUtils.newProgramDomain(id = 1)
        val program2 = ProgramUtils.newProgramDomain(id = 2)
        val position1 = program1.position
        val position2 = program2.position

        whenever(repository.findAll()).thenReturn(listOf(program1, program2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = program2)

        assertSoftly {
            it.assertThat(program1.position).isEqualTo(position2)
            it.assertThat(program2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(program2, program1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val program1 = ProgramUtils.newProgramDomain(id = 1)
        val program2 = ProgramUtils.newProgramDomain(id = 2)
        val position1 = program1.position
        val position2 = program2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(program1, program2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = program2)

        assertSoftly {
            it.assertThat(program1.position).isEqualTo(position2)
            it.assertThat(program2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(program2, program1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val program1 = ProgramUtils.newProgramDomain(id = 1)
        val program2 = ProgramUtils.newProgramDomain(id = 2)
        val position1 = program1.position
        val position2 = program2.position

        whenever(repository.findAll()).thenReturn(listOf(program1, program2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = program1)

        assertSoftly {
            it.assertThat(program1.position).isEqualTo(position2)
            it.assertThat(program2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(program1, program2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val program1 = ProgramUtils.newProgramDomain(id = 1)
        val program2 = ProgramUtils.newProgramDomain(id = 2)
        val position1 = program1.position
        val position2 = program2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(program1, program2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = program1)

        assertSoftly {
            it.assertThat(program1.position).isEqualTo(position2)
            it.assertThat(program2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(program1, program2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.newData] for admin.
     */
    @Test
    fun newDataAdmin() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        whenever(repository.findAll()).thenReturn(programs)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.newData()

        verify(repository).findAll()
        verify(repository).deleteAll(programs)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.newData] for account.
     */
    @Test
    fun newDataAccount() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(programs)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.newData()

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).deleteAll(programs)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.getAll] for admin.
     */
    @Test
    fun getAllAdmin() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        whenever(repository.findAll()).thenReturn(programs)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.getAll()

        assertThat(result).isEqualTo(programs)

        verify(repository).findAll()
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.getAll] for account.
     */
    @Test
    fun getAllAccount() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(programs)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.getAll()

        assertThat(result).isEqualTo(programs)

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.updatePositions] for admin.
     */
    @Test
    fun updatePositionsAdmin() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        whenever(repository.findAll()).thenReturn(programs)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.updatePositions()

        for (i in programs.indices) {
            assertThat(programs[i].position).isEqualTo(i)
        }

        verify(repository).findAll()
        verify(repository).saveAll(programs)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ProgramService.updatePositions] for account.
     */
    @Test
    fun updatePositionsAccount() {
        val programs = listOf(ProgramUtils.newProgramDomain(id = 1), ProgramUtils.newProgramDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(programs)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.updatePositions()

        for (i in programs.indices) {
            assertThat(programs[i].position).isEqualTo(i)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(programs)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Returns any mock for domain program.
     *
     * @return any mock for domain program
     */
    private fun anyDomain(): Program {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Program {
        return {
            val item = it.arguments[0] as Program
            item.id = 1
            item.position = 2
            item
        }
    }

    /**
     * Copying answer.
     *
     * @return mocked answer
     */
    private fun copy(): (InvocationOnMock) -> Any {
        return {
            it.arguments[0]
        }
    }

}
