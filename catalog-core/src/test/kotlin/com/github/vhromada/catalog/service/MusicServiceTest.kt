package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Music
import com.github.vhromada.catalog.repository.MusicRepository
import com.github.vhromada.catalog.utils.MusicUtils
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
 * A class represents test for class [MusicService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class MusicServiceTest {

    /**
     * Instance of [MusicRepository]
     */
    @Mock
    private lateinit var repository: MusicRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [MusicService]
     */
    private lateinit var service: MusicService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = MusicService(musicRepository = repository, accountProvider = accountProvider)
    }

    /**
     * Test method for [MusicService.get] with existing music for admin.
     */
    @Test
    fun getExistingAdmin() {
        val music = MusicUtils.newMusicDomain(id = 1)

        whenever(repository.findById(any())).thenReturn(Optional.of(music))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = music.id!!)

        assertThat(result).contains(music)

        verify(repository).findById(music.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.get] with existing music for account.
     */
    @Test
    fun getExistingAccount() {
        val music = MusicUtils.newMusicDomain(id = 1)

        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(music))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = music.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(music)

        verify(repository).findByIdAndCreatedUser(id = music.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.get] with not existing music for admin.
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
     * Test method for [MusicService.get] with not existing music for account.
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
     * Test method for [MusicService.add].
     */
    @Test
    fun add() {
        val music = MusicUtils.newMusicDomain(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = music)

        assertSoftly {
            it.assertThat(music.id).isEqualTo(1)
            it.assertThat(music.position).isEqualTo(2)
        }

        verify(repository, times(2)).save(music)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(music)
    }

    /**
     * Test method for [MusicService.update].
     */
    @Test
    fun update() {
        val music = MusicUtils.newMusicDomain(id = 1)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = music)

        verify(repository).save(music)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(music)
    }

    /**
     * Test method for [MusicService.remove].
     */
    @Test
    fun remove() {
        val music = MusicUtils.newMusicDomain(id = 1)

        service.remove(data = music)

        verify(repository).delete(music)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
    }

    /**
     * Test method for [MusicService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Music>()
        val expectedMusic = MusicUtils.newMusicDomain(id = 1)
            .copy(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = MusicUtils.newMusicDomain(id = 1))

        verify(repository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        MusicUtils.assertMusicDeepEquals(expected = expectedMusic, actual = result)
    }

    /**
     * Test method for [MusicService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val music1 = MusicUtils.newMusicDomain(id = 1)
        val music2 = MusicUtils.newMusicDomain(id = 2)
        val position1 = music1.position
        val position2 = music2.position

        whenever(repository.findAll()).thenReturn(listOf(music1, music2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = music2)

        assertSoftly {
            it.assertThat(music1.position).isEqualTo(position2)
            it.assertThat(music2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(music2, music1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val music1 = MusicUtils.newMusicDomain(id = 1)
        val music2 = MusicUtils.newMusicDomain(id = 2)
        val position1 = music1.position
        val position2 = music2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(music1, music2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = music2)

        assertSoftly {
            it.assertThat(music1.position).isEqualTo(position2)
            it.assertThat(music2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(music2, music1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val music1 = MusicUtils.newMusicDomain(id = 1)
        val music2 = MusicUtils.newMusicDomain(id = 2)
        val position1 = music1.position
        val position2 = music2.position

        whenever(repository.findAll()).thenReturn(listOf(music1, music2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = music1)

        assertSoftly {
            it.assertThat(music1.position).isEqualTo(position2)
            it.assertThat(music2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(music1, music2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val music1 = MusicUtils.newMusicDomain(id = 1)
        val music2 = MusicUtils.newMusicDomain(id = 2)
        val position1 = music1.position
        val position2 = music2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(music1, music2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = music1)

        assertSoftly {
            it.assertThat(music1.position).isEqualTo(position2)
            it.assertThat(music2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(music1, music2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.newData] for admin.
     */
    @Test
    fun newDataAdmin() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        whenever(repository.findAll()).thenReturn(musicList)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.newData()

        verify(repository).findAll()
        verify(repository).deleteAll(musicList)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.newData] for account.
     */
    @Test
    fun newDataAccount() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(musicList)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.newData()

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).deleteAll(musicList)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.getAll] for admin.
     */
    @Test
    fun getAllAdmin() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        whenever(repository.findAll()).thenReturn(musicList)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.getAll()

        assertThat(result).isEqualTo(musicList)

        verify(repository).findAll()
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.getAll] for account.
     */
    @Test
    fun getAllAccount() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(musicList)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.getAll()

        assertThat(result).isEqualTo(musicList)

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.updatePositions] for admin.
     */
    @Test
    fun updatePositionsAdmin() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        whenever(repository.findAll()).thenReturn(musicList)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.updatePositions()

        for (i in musicList.indices) {
            assertThat(musicList[i].position).isEqualTo(i)
        }

        verify(repository).findAll()
        verify(repository).saveAll(musicList)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MusicService.updatePositions] for account.
     */
    @Test
    fun updatePositionsAccount() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(musicList)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.updatePositions()

        for (i in musicList.indices) {
            assertThat(musicList[i].position).isEqualTo(i)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(musicList)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Returns any mock for domain music.
     *
     * @return any mock for domain music
     */
    private fun anyDomain(): Music {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Music {
        return {
            val item = it.arguments[0] as Music
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
