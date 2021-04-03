package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Episode
import com.github.vhromada.catalog.repository.EpisodeRepository
import com.github.vhromada.catalog.repository.SeasonRepository
import com.github.vhromada.catalog.utils.EpisodeUtils
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
 * A class represents test for class [EpisodeService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class EpisodeServiceTest {

    /**
     * Instance of [EpisodeRepository]
     */
    @Mock
    private lateinit var episodeRepository: EpisodeRepository

    /**
     * Instance of [SeasonRepository]
     */
    @Mock
    private lateinit var seasonRepository: SeasonRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [EpisodeService]
     */
    private lateinit var service: EpisodeService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = EpisodeService(episodeRepository = episodeRepository, seasonRepository = seasonRepository, accountProvider = accountProvider)
    }

    /**
     * Test method for [EpisodeService.get] with existing episode for admin.
     */
    @Test
    fun getExistingAdmin() {
        val episode = EpisodeUtils.newEpisodeDomain(id = 1)

        whenever(episodeRepository.findById(any())).thenReturn(Optional.of(episode))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = episode.id!!)

        assertThat(result).contains(episode)

        verify(episodeRepository).findById(episode.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.get] with existing episode for account.
     */
    @Test
    fun getExistingAccount() {
        val episode = EpisodeUtils.newEpisodeDomain(id = 1)

        whenever(episodeRepository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(episode))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = episode.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(episode)

        verify(episodeRepository).findByIdAndCreatedUser(id = episode.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.get] with not existing episode for admin.
     */
    @Test
    fun getNotExistingAdmin() {
        whenever(episodeRepository.findById(any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(episodeRepository).findById(Int.MAX_VALUE)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.get] with not existing episode for account.
     */
    @Test
    fun getNotExistingAccount() {
        whenever(episodeRepository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(episodeRepository).findByIdAndCreatedUser(id = Int.MAX_VALUE, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.add].
     */
    @Test
    fun add() {
        val episode = EpisodeUtils.newEpisodeDomain(id = null)

        whenever(episodeRepository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = episode)

        assertSoftly {
            it.assertThat(episode.id).isEqualTo(1)
            it.assertThat(episode.position).isEqualTo(2)
        }

        verify(episodeRepository, times(2)).save(episode)
        verifyNoMoreInteractions(episodeRepository)
        verifyZeroInteractions(seasonRepository, accountProvider)
        assertThat(result).isSameAs(episode)
    }

    /**
     * Test method for [EpisodeService.update].
     */
    @Test
    fun update() {
        val episode = EpisodeUtils.newEpisodeDomain(id = 1)

        whenever(episodeRepository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = episode)

        verify(episodeRepository).save(episode)
        verifyNoMoreInteractions(episodeRepository)
        verifyZeroInteractions(seasonRepository, accountProvider)
        assertThat(result).isSameAs(episode)
    }

    /**
     * Test method for [EpisodeService.remove].
     */
    @Test
    fun remove() {
        val episode = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)

        service.remove(data = episode)

        verify(seasonRepository).save(episode.season!!)
        verifyNoMoreInteractions(seasonRepository)
        verifyZeroInteractions(episodeRepository, accountProvider)
    }

    /**
     * Test method for [EpisodeService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Episode>()
        val expectedEpisode = EpisodeUtils.newEpisodeDomain(id = 1)
            .copy(id = null)

        whenever(episodeRepository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = EpisodeUtils.newEpisodeDomain(id = 1))

        verify(episodeRepository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(episodeRepository)
        verifyZeroInteractions(seasonRepository, accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        EpisodeUtils.assertEpisodeDeepEquals(expected = expectedEpisode, actual = result)
    }

    /**
     * Test method for [EpisodeService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val episode1 = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)
        val episode2 = EpisodeUtils.newEpisodeDomainWithSeason(id = 2)
        val position1 = episode1.position
        val position2 = episode2.position

        whenever(episodeRepository.findAllBySeasonId(id = any())).thenReturn(listOf(episode1, episode2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = episode2)

        assertSoftly {
            it.assertThat(episode1.position).isEqualTo(position2)
            it.assertThat(episode2.position).isEqualTo(position1)
        }

        verify(episodeRepository).findAllBySeasonId(id = episode2.season!!.id!!)
        verify(episodeRepository).saveAll(listOf(episode2, episode1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val episode1 = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)
        val episode2 = EpisodeUtils.newEpisodeDomainWithSeason(id = 2)
        val position1 = episode1.position
        val position2 = episode2.position

        whenever(episodeRepository.findAllBySeasonIdAndCreatedUser(id = any(), user = any())).thenReturn(listOf(episode1, episode2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = episode2)

        assertSoftly {
            it.assertThat(episode1.position).isEqualTo(position2)
            it.assertThat(episode2.position).isEqualTo(position1)
        }

        verify(episodeRepository).findAllBySeasonIdAndCreatedUser(id = episode2.season!!.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(episodeRepository).saveAll(listOf(episode2, episode1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val episode1 = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)
        val episode2 = EpisodeUtils.newEpisodeDomainWithSeason(id = 2)
        val position1 = episode1.position
        val position2 = episode2.position

        whenever(episodeRepository.findAllBySeasonId(id = any())).thenReturn(listOf(episode1, episode2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = episode1)

        assertSoftly {
            it.assertThat(episode1.position).isEqualTo(position2)
            it.assertThat(episode2.position).isEqualTo(position1)
        }

        verify(episodeRepository).findAllBySeasonId(id = episode1.season!!.id!!)
        verify(episodeRepository).saveAll(listOf(episode1, episode2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val episode1 = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)
        val episode2 = EpisodeUtils.newEpisodeDomainWithSeason(id = 2)
        val position1 = episode1.position
        val position2 = episode2.position

        whenever(episodeRepository.findAllBySeasonIdAndCreatedUser(id = any(), user = any())).thenReturn(listOf(episode1, episode2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = episode1)

        assertSoftly {
            it.assertThat(episode1.position).isEqualTo(position2)
            it.assertThat(episode2.position).isEqualTo(position1)
        }

        verify(episodeRepository).findAllBySeasonIdAndCreatedUser(id = episode1.season!!.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(episodeRepository).saveAll(listOf(episode1, episode2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.find] for admin.
     */
    @Test
    fun findAdmin() {
        val episodes = listOf(EpisodeUtils.newEpisodeDomain(id = 1), EpisodeUtils.newEpisodeDomain(id = 2))

        whenever(episodeRepository.findAllBySeasonId(id = any())).thenReturn(episodes)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.find(parent = 1)

        assertThat(result).isEqualTo(episodes)

        verify(episodeRepository).findAllBySeasonId(id = 1)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Test method for [EpisodeService.find] for account.
     */
    @Test
    fun findAccount() {
        val episodes = listOf(EpisodeUtils.newEpisodeDomain(id = 1), EpisodeUtils.newEpisodeDomain(id = 2))

        whenever(episodeRepository.findAllBySeasonIdAndCreatedUser(id = any(), user = any())).thenReturn(episodes)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.find(parent = 1)

        assertThat(result).isEqualTo(episodes)

        verify(episodeRepository).findAllBySeasonIdAndCreatedUser(id = 1, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(episodeRepository, accountProvider)
        verifyZeroInteractions(seasonRepository)
    }

    /**
     * Returns any mock for domain episode.
     *
     * @return any mock for domain episode
     */
    private fun anyDomain(): Episode {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Episode {
        return {
            val episode = it.arguments[0] as Episode
            episode.id = 1
            episode.position = 2
            episode
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
