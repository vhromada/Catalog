package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.domain.Program
import cz.vhromada.catalog.repository.ProgramRepository
import cz.vhromada.catalog.utils.ProgramUtils
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import cz.vhromada.common.test.utils.TestConstants
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
        verify(repository).findByAuditCreatedUser(TestConstants.ACCOUNT_ID)
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
