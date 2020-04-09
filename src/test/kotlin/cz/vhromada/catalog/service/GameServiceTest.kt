package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.domain.Game
import cz.vhromada.catalog.repository.GameRepository
import cz.vhromada.catalog.utils.GameUtils
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import cz.vhromada.common.test.utils.TestConstants
import org.mockito.Mock
import org.springframework.data.jpa.repository.JpaRepository

/**
 * A class represents test for class [GameService].
 *
 * @author Vladimir Hromada
 */
class GameServiceTest : MovableServiceTest<Game>() {

    /**
     * Instance of [GameRepository]
     */
    @Mock
    private lateinit var repository: GameRepository

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

    override fun getRepository(): JpaRepository<Game, Int> {
        return repository
    }

    override fun getAccountProvider(): AccountProvider {
        return accountProvider
    }

    override fun getTimeProvider(): TimeProvider {
        return timeProvider
    }

    override fun getService(): MovableService<Game> {
        return GameService(repository, accountProvider, timeProvider, cache)
    }

    override fun getCacheKey(): String {
        return "games${TestConstants.ACCOUNT.id}"
    }

    override fun getItem1(): Game {
        return GameUtils.newGameDomain(1)
    }

    override fun getItem2(): Game {
        return GameUtils.newGameDomain(2)
    }

    override fun getAddItem(): Game {
        return GameUtils.newGameDomain(null)
    }

    override fun getCopyItem(): Game {
        return GameUtils.newGameDomain(null)
                .copy(position = 0)
    }

    override fun initAllDataMock(data: List<Game>) {
        whenever(repository.findByAuditCreatedUser(any())).thenReturn(data)
    }

    override fun verifyAllDataMock() {
        verify(repository).findByAuditCreatedUser(TestConstants.ACCOUNT_ID)
        verifyNoMoreInteractions(repository)
    }

    override fun anyItem(): Game {
        return any()
    }

    override fun argumentCaptorItem(): KArgumentCaptor<Game> {
        return argumentCaptor()
    }

    override fun assertDataDeepEquals(expected: Game, actual: Game) {
        GameUtils.assertGameDeepEquals(expected, actual)
    }

}
