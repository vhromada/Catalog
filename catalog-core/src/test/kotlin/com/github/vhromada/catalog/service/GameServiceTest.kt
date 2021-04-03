package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Game
import com.github.vhromada.catalog.repository.GameRepository
import com.github.vhromada.catalog.utils.GameUtils
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
 * A class represents test for class [GameService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class GameServiceTest {

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
     * Instance of [GameService]
     */
    private lateinit var service: GameService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = GameService(gameRepository = repository, accountProvider = accountProvider)
    }

    /**
     * Test method for [GameService.get] with existing game for admin.
     */
    @Test
    fun getExistingAdmin() {
        val game = GameUtils.newGameDomain(id = 1)

        whenever(repository.findById(any())).thenReturn(Optional.of(game))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = game.id!!)

        assertThat(result).contains(game)

        verify(repository).findById(game.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.get] with existing game for account.
     */
    @Test
    fun getExistingAccount() {
        val game = GameUtils.newGameDomain(id = 1)

        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(game))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = game.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(game)

        verify(repository).findByIdAndCreatedUser(id = game.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.get] with not existing game for admin.
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
     * Test method for [GameService.get] with not existing game for account.
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
     * Test method for [GameService.add].
     */
    @Test
    fun add() {
        val game = GameUtils.newGameDomain(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = game)

        assertSoftly {
            it.assertThat(game.id).isEqualTo(1)
            it.assertThat(game.position).isEqualTo(2)
        }

        verify(repository, times(2)).save(game)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(game)
    }

    /**
     * Test method for [GameService.update].
     */
    @Test
    fun update() {
        val game = GameUtils.newGameDomain(id = 1)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = game)

        verify(repository).save(game)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(game)
    }

    /**
     * Test method for [GameService.remove].
     */
    @Test
    fun remove() {
        val game = GameUtils.newGameDomain(id = 1)

        service.remove(data = game)

        verify(repository).delete(game)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
    }

    /**
     * Test method for [GameService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Game>()
        val expectedGame = GameUtils.newGameDomain(id = 1)
            .copy(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = GameUtils.newGameDomain(id = 1))

        verify(repository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        GameUtils.assertGameDeepEquals(expected = expectedGame, actual = result)
    }

    /**
     * Test method for [GameService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val game1 = GameUtils.newGameDomain(id = 1)
        val game2 = GameUtils.newGameDomain(id = 2)
        val position1 = game1.position
        val position2 = game2.position

        whenever(repository.findAll()).thenReturn(listOf(game1, game2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = game2)

        assertSoftly {
            it.assertThat(game1.position).isEqualTo(position2)
            it.assertThat(game2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(game2, game1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val game1 = GameUtils.newGameDomain(id = 1)
        val game2 = GameUtils.newGameDomain(id = 2)
        val position1 = game1.position
        val position2 = game2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(game1, game2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = game2)

        assertSoftly {
            it.assertThat(game1.position).isEqualTo(position2)
            it.assertThat(game2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(game2, game1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val game1 = GameUtils.newGameDomain(id = 1)
        val game2 = GameUtils.newGameDomain(id = 2)
        val position1 = game1.position
        val position2 = game2.position

        whenever(repository.findAll()).thenReturn(listOf(game1, game2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = game1)

        assertSoftly {
            it.assertThat(game1.position).isEqualTo(position2)
            it.assertThat(game2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(game1, game2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val game1 = GameUtils.newGameDomain(id = 1)
        val game2 = GameUtils.newGameDomain(id = 2)
        val position1 = game1.position
        val position2 = game2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(game1, game2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = game1)

        assertSoftly {
            it.assertThat(game1.position).isEqualTo(position2)
            it.assertThat(game2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(game1, game2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.newData] for admin.
     */
    @Test
    fun newDataAdmin() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        whenever(repository.findAll()).thenReturn(games)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.newData()

        verify(repository).findAll()
        verify(repository).deleteAll(games)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.newData] for account.
     */
    @Test
    fun newDataAccount() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(games)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.newData()

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).deleteAll(games)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.getAll] for admin.
     */
    @Test
    fun getAllAdmin() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        whenever(repository.findAll()).thenReturn(games)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.getAll()

        assertThat(result).isEqualTo(games)

        verify(repository).findAll()
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.getAll] for account.
     */
    @Test
    fun getAllAccount() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(games)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.getAll()

        assertThat(result).isEqualTo(games)

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.updatePositions] for admin.
     */
    @Test
    fun updatePositionsAdmin() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        whenever(repository.findAll()).thenReturn(games)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.updatePositions()

        for (i in games.indices) {
            assertThat(games[i].position).isEqualTo(i)
        }

        verify(repository).findAll()
        verify(repository).saveAll(games)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GameService.updatePositions] for account.
     */
    @Test
    fun updatePositionsAccount() {
        val games = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(games)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.updatePositions()

        for (i in games.indices) {
            assertThat(games[i].position).isEqualTo(i)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(games)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Returns any mock for domain game.
     *
     * @return any mock for domain game
     */
    private fun anyDomain(): Game {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Game {
        return {
            val item = it.arguments[0] as Game
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
