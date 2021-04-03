package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Song
import com.github.vhromada.catalog.repository.MusicRepository
import com.github.vhromada.catalog.repository.SongRepository
import com.github.vhromada.catalog.utils.SongUtils
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
 * A class represents test for class [SongService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class SongServiceTest {

    /**
     * Instance of [SongRepository]
     */
    @Mock
    private lateinit var songRepository: SongRepository

    /**
     * Instance of [MusicRepository]
     */
    @Mock
    private lateinit var musicRepository: MusicRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [SongService]
     */
    private lateinit var service: SongService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = SongService(songRepository = songRepository, musicRepository = musicRepository, accountProvider = accountProvider)
    }

    /**
     * Test method for [SongService.get] with existing song for admin.
     */
    @Test
    fun getExistingAdmin() {
        val song = SongUtils.newSongDomain(id = 1)

        whenever(songRepository.findById(any())).thenReturn(Optional.of(song))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = song.id!!)

        assertThat(result).contains(song)

        verify(songRepository).findById(song.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.get] with existing song for account.
     */
    @Test
    fun getExistingAccount() {
        val song = SongUtils.newSongDomain(id = 1)

        whenever(songRepository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(song))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = song.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(song)

        verify(songRepository).findByIdAndCreatedUser(id = song.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.get] with not existing song for admin.
     */
    @Test
    fun getNotExistingAdmin() {
        whenever(songRepository.findById(any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(songRepository).findById(Int.MAX_VALUE)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.get] with not existing song for account.
     */
    @Test
    fun getNotExistingAccount() {
        whenever(songRepository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(songRepository).findByIdAndCreatedUser(id = Int.MAX_VALUE, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.add].
     */
    @Test
    fun add() {
        val song = SongUtils.newSongDomain(id = null)

        whenever(songRepository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = song)

        assertSoftly {
            it.assertThat(song.id).isEqualTo(1)
            it.assertThat(song.position).isEqualTo(2)
        }

        verify(songRepository, times(2)).save(song)
        verifyNoMoreInteractions(songRepository)
        verifyZeroInteractions(musicRepository, accountProvider)
        assertThat(result).isSameAs(song)
    }

    /**
     * Test method for [SongService.update].
     */
    @Test
    fun update() {
        val song = SongUtils.newSongDomain(id = 1)

        whenever(songRepository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = song)

        verify(songRepository).save(song)
        verifyNoMoreInteractions(songRepository)
        verifyZeroInteractions(musicRepository, accountProvider)
        assertThat(result).isSameAs(song)
    }

    /**
     * Test method for [SongService.remove].
     */
    @Test
    fun remove() {
        val song = SongUtils.newSongDomainWithMusic(id = 1)

        service.remove(data = song)

        verify(musicRepository).save(song.music!!)
        verifyNoMoreInteractions(musicRepository)
        verifyZeroInteractions(songRepository, accountProvider)
    }

    /**
     * Test method for [SongService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Song>()
        val expectedSong = SongUtils.newSongDomain(id = 1)
            .copy(id = null)

        whenever(songRepository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = SongUtils.newSongDomain(id = 1))

        verify(songRepository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(songRepository)
        verifyZeroInteractions(musicRepository, accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        SongUtils.assertSongDeepEquals(expected = expectedSong, actual = result)
    }

    /**
     * Test method for [SongService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val song1 = SongUtils.newSongDomainWithMusic(id = 1)
        val song2 = SongUtils.newSongDomainWithMusic(id = 2)
        val position1 = song1.position
        val position2 = song2.position

        whenever(songRepository.findAllByMusicId(id = any())).thenReturn(listOf(song1, song2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = song2)

        assertSoftly {
            it.assertThat(song1.position).isEqualTo(position2)
            it.assertThat(song2.position).isEqualTo(position1)
        }

        verify(songRepository).findAllByMusicId(id = song2.music!!.id!!)
        verify(songRepository).saveAll(listOf(song2, song1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val song1 = SongUtils.newSongDomainWithMusic(id = 1)
        val song2 = SongUtils.newSongDomainWithMusic(id = 2)
        val position1 = song1.position
        val position2 = song2.position

        whenever(songRepository.findAllByMusicIdAndCreatedUser(id = any(), user = any())).thenReturn(listOf(song1, song2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = song2)

        assertSoftly {
            it.assertThat(song1.position).isEqualTo(position2)
            it.assertThat(song2.position).isEqualTo(position1)
        }

        verify(songRepository).findAllByMusicIdAndCreatedUser(id = song2.music!!.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(songRepository).saveAll(listOf(song2, song1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val song1 = SongUtils.newSongDomainWithMusic(id = 1)
        val song2 = SongUtils.newSongDomainWithMusic(id = 2)
        val position1 = song1.position
        val position2 = song2.position

        whenever(songRepository.findAllByMusicId(id = any())).thenReturn(listOf(song1, song2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = song1)

        assertSoftly {
            it.assertThat(song1.position).isEqualTo(position2)
            it.assertThat(song2.position).isEqualTo(position1)
        }

        verify(songRepository).findAllByMusicId(id = song1.music!!.id!!)
        verify(songRepository).saveAll(listOf(song1, song2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val song1 = SongUtils.newSongDomainWithMusic(id = 1)
        val song2 = SongUtils.newSongDomainWithMusic(id = 2)
        val position1 = song1.position
        val position2 = song2.position

        whenever(songRepository.findAllByMusicIdAndCreatedUser(id = any(), user = any())).thenReturn(listOf(song1, song2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = song1)

        assertSoftly {
            it.assertThat(song1.position).isEqualTo(position2)
            it.assertThat(song2.position).isEqualTo(position1)
        }

        verify(songRepository).findAllByMusicIdAndCreatedUser(id = song1.music!!.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(songRepository).saveAll(listOf(song1, song2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.find] for admin.
     */
    @Test
    fun findAdmin() {
        val songs = listOf(SongUtils.newSongDomain(id = 1), SongUtils.newSongDomain(id = 2))

        whenever(songRepository.findAllByMusicId(id = any())).thenReturn(songs)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.find(parent = 1)

        assertThat(result).isEqualTo(songs)

        verify(songRepository).findAllByMusicId(id = 1)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Test method for [SongService.find] for account.
     */
    @Test
    fun findAccount() {
        val songs = listOf(SongUtils.newSongDomain(id = 1), SongUtils.newSongDomain(id = 2))

        whenever(songRepository.findAllByMusicIdAndCreatedUser(id = any(), user = any())).thenReturn(songs)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.find(parent = 1)

        assertThat(result).isEqualTo(songs)

        verify(songRepository).findAllByMusicIdAndCreatedUser(id = 1, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(songRepository, accountProvider)
        verifyZeroInteractions(musicRepository)
    }

    /**
     * Returns any mock for domain song.
     *
     * @return any mock for domain song
     */
    private fun anyDomain(): Song {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Song {
        return {
            val song = it.arguments[0] as Song
            song.id = 1
            song.position = 2
            song
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
