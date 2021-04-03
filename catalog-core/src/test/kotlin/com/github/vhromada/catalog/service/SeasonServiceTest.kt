package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Season
import com.github.vhromada.catalog.repository.SeasonRepository
import com.github.vhromada.catalog.repository.ShowRepository
import com.github.vhromada.catalog.utils.SeasonUtils
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
 * A class represents test for class [SeasonService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class SeasonServiceTest {

    /**
     * Instance of [SeasonRepository]
     */
    @Mock
    private lateinit var seasonRepository: SeasonRepository

    /**
     * Instance of [ShowRepository]
     */
    @Mock
    private lateinit var showRepository: ShowRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [SeasonService]
     */
    private lateinit var service: SeasonService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = SeasonService(seasonRepository = seasonRepository, showRepository = showRepository, accountProvider = accountProvider)
    }

    /**
     * Test method for [SeasonService.get] with existing season for admin.
     */
    @Test
    fun getExistingAdmin() {
        val season = SeasonUtils.newSeasonDomain(id = 1)

        whenever(seasonRepository.findById(any())).thenReturn(Optional.of(season))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = season.id!!)

        assertThat(result).contains(season)

        verify(seasonRepository).findById(season.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.get] with existing season for account.
     */
    @Test
    fun getExistingAccount() {
        val season = SeasonUtils.newSeasonDomain(id = 1)

        whenever(seasonRepository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(season))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = season.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(season)

        verify(seasonRepository).findByIdAndCreatedUser(id = season.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.get] with not existing season for admin.
     */
    @Test
    fun getNotExistingAdmin() {
        whenever(seasonRepository.findById(any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(seasonRepository).findById(Int.MAX_VALUE)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.get] with not existing season for account.
     */
    @Test
    fun getNotExistingAccount() {
        whenever(seasonRepository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(seasonRepository).findByIdAndCreatedUser(id = Int.MAX_VALUE, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.add].
     */
    @Test
    fun add() {
        val season = SeasonUtils.newSeasonDomain(id = null)

        whenever(seasonRepository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = season)

        assertSoftly {
            it.assertThat(season.id).isEqualTo(1)
            it.assertThat(season.position).isEqualTo(2)
        }

        verify(seasonRepository, times(2)).save(season)
        verifyNoMoreInteractions(seasonRepository)
        verifyZeroInteractions(showRepository, accountProvider)
        assertThat(result).isSameAs(season)
    }

    /**
     * Test method for [SeasonService.update].
     */
    @Test
    fun update() {
        val season = SeasonUtils.newSeasonDomain(id = 1)

        whenever(seasonRepository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = season)

        verify(seasonRepository).save(season)
        verifyNoMoreInteractions(seasonRepository)
        verifyZeroInteractions(showRepository, accountProvider)
        assertThat(result).isSameAs(season)
    }

    /**
     * Test method for [SeasonService.remove].
     */
    @Test
    fun remove() {
        val season = SeasonUtils.newSeasonDomainWithShow(id = 1)

        service.remove(data = season)

        verify(showRepository).save(season.show!!)
        verifyNoMoreInteractions(showRepository)
        verifyZeroInteractions(seasonRepository, accountProvider)
    }

    /**
     * Test method for [SeasonService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Season>()
        val expectedSeason = SeasonUtils.newSeasonDomain(id = 1)
            .copy(id = null)

        whenever(seasonRepository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = SeasonUtils.newSeasonDomain(id = 1))

        verify(seasonRepository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(seasonRepository)
        verifyZeroInteractions(showRepository, accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        SeasonUtils.assertSeasonDeepEquals(expected = expectedSeason, actual = result)
    }

    /**
     * Test method for [SeasonService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val season1 = SeasonUtils.newSeasonDomainWithShow(id = 1)
        val season2 = SeasonUtils.newSeasonDomainWithShow(id = 2)
        val position1 = season1.position
        val position2 = season2.position

        whenever(seasonRepository.findAllByShowId(id = any())).thenReturn(listOf(season1, season2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = season2)

        assertSoftly {
            it.assertThat(season1.position).isEqualTo(position2)
            it.assertThat(season2.position).isEqualTo(position1)
        }

        verify(seasonRepository).findAllByShowId(id = season2.show!!.id!!)
        verify(seasonRepository).saveAll(listOf(season2, season1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val season1 = SeasonUtils.newSeasonDomainWithShow(id = 1)
        val season2 = SeasonUtils.newSeasonDomainWithShow(id = 2)
        val position1 = season1.position
        val position2 = season2.position

        whenever(seasonRepository.findAllByShowIdAndCreatedUser(id = any(), user = any())).thenReturn(listOf(season1, season2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = season2)

        assertSoftly {
            it.assertThat(season1.position).isEqualTo(position2)
            it.assertThat(season2.position).isEqualTo(position1)
        }

        verify(seasonRepository).findAllByShowIdAndCreatedUser(id = season2.show!!.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(seasonRepository).saveAll(listOf(season2, season1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val season1 = SeasonUtils.newSeasonDomainWithShow(id = 1)
        val season2 = SeasonUtils.newSeasonDomainWithShow(id = 2)
        val position1 = season1.position
        val position2 = season2.position

        whenever(seasonRepository.findAllByShowId(id = any())).thenReturn(listOf(season1, season2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = season1)

        assertSoftly {
            it.assertThat(season1.position).isEqualTo(position2)
            it.assertThat(season2.position).isEqualTo(position1)
        }

        verify(seasonRepository).findAllByShowId(id = season1.show!!.id!!)
        verify(seasonRepository).saveAll(listOf(season1, season2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val season1 = SeasonUtils.newSeasonDomainWithShow(id = 1)
        val season2 = SeasonUtils.newSeasonDomainWithShow(id = 2)
        val position1 = season1.position
        val position2 = season2.position

        whenever(seasonRepository.findAllByShowIdAndCreatedUser(id = any(), user = any())).thenReturn(listOf(season1, season2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = season1)

        assertSoftly {
            it.assertThat(season1.position).isEqualTo(position2)
            it.assertThat(season2.position).isEqualTo(position1)
        }

        verify(seasonRepository).findAllByShowIdAndCreatedUser(id = season1.show!!.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(seasonRepository).saveAll(listOf(season1, season2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.find] for admin.
     */
    @Test
    fun findAdmin() {
        val seasons = listOf(SeasonUtils.newSeasonDomain(id = 1), SeasonUtils.newSeasonDomain(id = 2))

        whenever(seasonRepository.findAllByShowId(id = any())).thenReturn(seasons)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.find(parent = 1)

        assertThat(result).isEqualTo(seasons)

        verify(seasonRepository).findAllByShowId(id = 1)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Test method for [SeasonService.find] for account.
     */
    @Test
    fun findAccount() {
        val seasons = listOf(SeasonUtils.newSeasonDomain(id = 1), SeasonUtils.newSeasonDomain(id = 2))

        whenever(seasonRepository.findAllByShowIdAndCreatedUser(id = any(), user = any())).thenReturn(seasons)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.find(parent = 1)

        assertThat(result).isEqualTo(seasons)

        verify(seasonRepository).findAllByShowIdAndCreatedUser(id = 1, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(seasonRepository, accountProvider)
        verifyZeroInteractions(showRepository)
    }

    /**
     * Returns any mock for domain season.
     *
     * @return any mock for domain season
     */
    private fun anyDomain(): Season {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Season {
        return {
            val season = it.arguments[0] as Season
            season.id = 1
            season.position = 2
            season
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
