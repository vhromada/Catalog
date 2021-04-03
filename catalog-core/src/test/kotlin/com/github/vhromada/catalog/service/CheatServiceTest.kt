package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Cheat
import com.github.vhromada.catalog.repository.CheatRepository
import com.github.vhromada.catalog.repository.GameRepository
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.provider.AccountProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
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
 * A class represents test for class [CheatService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class CheatServiceTest {

    /**
     * Instance of [CheatRepository]
     */
    @Mock
    private lateinit var cheatRepository: CheatRepository

    /**
     * Instance of [GameRepository]
     */
    @Mock
    private lateinit var gameRepository: GameRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [CheatService]
     */
    private lateinit var service: CheatService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = CheatService(cheatRepository = cheatRepository, gameRepository = gameRepository, accountProvider = accountProvider)
    }

    /**
     * Test method for [CheatService.get] with existing cheat for admin.
     */
    @Test
    fun getExistingAdmin() {
        val cheat = CheatUtils.newCheatDomain(id = 1)

        whenever(cheatRepository.findById(any())).thenReturn(Optional.of(cheat))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = cheat.id!!)

        assertThat(result).contains(cheat)

        verify(cheatRepository).findById(cheat.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(cheatRepository, accountProvider)
        verifyZeroInteractions(gameRepository)
    }

    /**
     * Test method for [CheatService.get] with existing cheat for account.
     */
    @Test
    fun getExistingAccount() {
        val cheat = CheatUtils.newCheatDomain(id = 1)

        whenever(cheatRepository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(cheat))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = cheat.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(cheat)

        verify(cheatRepository).findByIdAndCreatedUser(id = cheat.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(cheatRepository, accountProvider)
        verifyZeroInteractions(gameRepository)
    }

    /**
     * Test method for [CheatService.get] with not existing cheat for admin.
     */
    @Test
    fun getNotExistingAdmin() {
        whenever(cheatRepository.findById(any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(cheatRepository).findById(Int.MAX_VALUE)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(cheatRepository, accountProvider)
        verifyZeroInteractions(gameRepository)
    }

    /**
     * Test method for [CheatService.get] with not existing cheat for account.
     */
    @Test
    fun getNotExistingAccount() {
        whenever(cheatRepository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(cheatRepository).findByIdAndCreatedUser(id = Int.MAX_VALUE, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(cheatRepository, accountProvider)
        verifyZeroInteractions(gameRepository)
    }

    /**
     * Test method for [CheatService.add].
     */
    @Test
    fun add() {
        val cheat = CheatUtils.newCheatDomain(id = null)

        whenever(cheatRepository.save(anyDomain())).thenAnswer(setId())

        val result = service.add(data = cheat)

        assertSoftly {
            it.assertThat(cheat.id).isEqualTo(1)
        }

        verify(cheatRepository).save(cheat)
        verifyNoMoreInteractions(cheatRepository)
        verifyZeroInteractions(gameRepository, accountProvider)
        assertThat(result).isSameAs(cheat)
    }

    /**
     * Test method for [CheatService.update].
     */
    @Test
    fun update() {
        val cheat = CheatUtils.newCheatDomain(id = 1)

        whenever(cheatRepository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = cheat)

        verify(cheatRepository).save(cheat)
        verifyNoMoreInteractions(cheatRepository)
        verifyZeroInteractions(gameRepository, accountProvider)
        assertThat(result).isSameAs(cheat)
    }

    /**
     * Test method for [CheatService.remove].
     */
    @Test
    fun remove() {
        val cheat = CheatUtils.newCheatDomainWithGame(id = 1)

        service.remove(data = cheat)

        verify(gameRepository).save(cheat.game!!)
        verifyNoMoreInteractions(gameRepository)
        verifyZeroInteractions(cheatRepository, accountProvider)
    }

    /**
     * Test method for [CheatService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Cheat>()
        val expectedCheat = CheatUtils.newCheatDomain(id = 1)
            .copy(id = null)

        whenever(cheatRepository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = CheatUtils.newCheatDomain(id = 1))

        verify(cheatRepository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(cheatRepository)
        verifyZeroInteractions(gameRepository, accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        CheatUtils.assertCheatDeepEquals(expected = expectedCheat, actual = result)
    }

    /**
     * Test method for [CheatService.moveUp].
     */
    @Test
    fun moveUp() {
        service.moveUp(data = CheatUtils.newCheatDomain(id = 1))

        verifyZeroInteractions(cheatRepository, gameRepository, accountProvider)
    }

    /**
     * Test method for [CheatService.moveDown].
     */
    @Test
    fun moveDown() {
        service.moveDown(data = CheatUtils.newCheatDomain(id = 1))

        verifyZeroInteractions(cheatRepository, gameRepository, accountProvider)
    }

    /**
     * Test method for [CheatService.find] for admin.
     */
    @Test
    fun findAdmin() {
        val cheats = listOf(CheatUtils.newCheatDomain(id = 1), CheatUtils.newCheatDomain(id = 2))

        whenever(cheatRepository.findAllByGameId(id = any())).thenReturn(cheats)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.find(parent = 1)

        assertThat(result).isEqualTo(cheats)

        verify(cheatRepository).findAllByGameId(id = 1)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(cheatRepository, accountProvider)
        verifyZeroInteractions(gameRepository)
    }

    /**
     * Test method for [CheatService.find] for account.
     */
    @Test
    fun findAccount() {
        val cheats = listOf(CheatUtils.newCheatDomain(id = 1), CheatUtils.newCheatDomain(id = 2))

        whenever(cheatRepository.findAllByGameIdAndCreatedUser(id = any(), user = any())).thenReturn(cheats)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.find(parent = 1)

        assertThat(result).isEqualTo(cheats)

        verify(cheatRepository).findAllByGameIdAndCreatedUser(id = 1, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(cheatRepository, accountProvider)
        verifyZeroInteractions(gameRepository)
    }

    /**
     * Returns any mock for domain cheat.
     *
     * @return any mock for domain cheat
     */
    private fun anyDomain(): Cheat {
        return any()
    }

    /**
     * Sets ID.
     *
     * @return mocked answer
     */
    private fun setId(): (InvocationOnMock) -> Cheat {
        return {
            val cheat = it.arguments[0] as Cheat
            cheat.id = 1
            cheat
        }
    }

    /**
     * Coping answer.
     *
     * @return mocked answer
     */
    private fun copy(): (InvocationOnMock) -> Any {
        return {
            it.arguments[0]
        }
    }

}
