package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Program
import com.github.vhromada.catalog.repository.ProgramRepository
import com.github.vhromada.catalog.utils.ProgramUtils
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.test.service.MovableServiceTest
import com.github.vhromada.common.test.utils.TestConstants
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
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

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [TimeProvider]
     */
    @Mock
    private lateinit var timeProvider: TimeProvider

    override fun getRepository(): JpaRepository<Program, Int> {
        return repository
    }

    override fun getAccountProvider(): AccountProvider {
        return accountProvider
    }

    override fun getTimeProvider(): TimeProvider {
        return timeProvider
    }

    override fun getService(): MovableService<Program> {
        return ProgramService(repository, accountProvider, timeProvider, cache)
    }

    override fun getCacheKey(): String {
        return "programs${TestConstants.ACCOUNT.id}"
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
        return ProgramUtils.newProgramDomain(null)
                .copy(position = 0)
    }

    override fun initAllDataMock(data: List<Program>) {
        whenever(repository.findByAuditCreatedUser(any())).thenReturn(data)
    }

    override fun verifyAllDataMock() {
        verify(repository).findByAuditCreatedUser(TestConstants.ACCOUNT_UUID)
        verifyNoMoreInteractions(repository)
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
