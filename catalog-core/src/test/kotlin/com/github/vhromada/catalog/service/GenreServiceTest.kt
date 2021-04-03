package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Genre
import com.github.vhromada.catalog.repository.GenreRepository
import com.github.vhromada.catalog.utils.GenreUtils
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
 * A class represents test for class [GenreService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class GenreServiceTest {

    /**
     * Instance of [GenreRepository]
     */
    @Mock
    private lateinit var repository: GenreRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [GenreService]
     */
    private lateinit var service: GenreService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = GenreService(genreRepository = repository, accountProvider = accountProvider)
    }

    /**
     * Test method for [GenreService.get] with existing genre for admin.
     */
    @Test
    fun getExistingAdmin() {
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(repository.findById(any())).thenReturn(Optional.of(genre))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = genre.id!!)

        assertThat(result).contains(genre)

        verify(repository).findById(genre.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.get] with existing genre for account.
     */
    @Test
    fun getExistingAccount() {
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(genre))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = genre.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(genre)

        verify(repository).findByIdAndCreatedUser(id = genre.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.get] with not existing genre for admin.
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
     * Test method for [GenreService.get] with not existing genre for account.
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
     * Test method for [GenreService.add].
     */
    @Test
    fun add() {
        val genre = GenreUtils.newGenreDomain(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = genre)

        assertSoftly {
            it.assertThat(genre.id).isEqualTo(1)
            it.assertThat(genre.position).isEqualTo(2)
        }

        verify(repository, times(2)).save(genre)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(genre)
    }

    /**
     * Test method for [GenreService.update].
     */
    @Test
    fun update() {
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = genre)

        verify(repository).save(genre)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(genre)
    }

    /**
     * Test method for [GenreService.remove].
     */
    @Test
    fun remove() {
        val genre = GenreUtils.newGenreDomain(id = 1)

        service.remove(data = genre)

        verify(repository).delete(genre)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
    }

    /**
     * Test method for [GenreService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Genre>()
        val expectedGenre = GenreUtils.newGenreDomain(id = 1)
            .copy(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = GenreUtils.newGenreDomain(id = 1))

        verify(repository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        GenreUtils.assertGenreDeepEquals(expected = expectedGenre, actual = result)
    }

    /**
     * Test method for [GenreService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val genre1 = GenreUtils.newGenreDomain(id = 1)
        val genre2 = GenreUtils.newGenreDomain(id = 2)
        val position1 = genre1.position
        val position2 = genre2.position

        whenever(repository.findAll()).thenReturn(listOf(genre1, genre2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = genre2)

        assertSoftly {
            it.assertThat(genre1.position).isEqualTo(position2)
            it.assertThat(genre2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(genre2, genre1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val genre1 = GenreUtils.newGenreDomain(id = 1)
        val genre2 = GenreUtils.newGenreDomain(id = 2)
        val position1 = genre1.position
        val position2 = genre2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(genre1, genre2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = genre2)

        assertSoftly {
            it.assertThat(genre1.position).isEqualTo(position2)
            it.assertThat(genre2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(genre2, genre1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val genre1 = GenreUtils.newGenreDomain(id = 1)
        val genre2 = GenreUtils.newGenreDomain(id = 2)
        val position1 = genre1.position
        val position2 = genre2.position

        whenever(repository.findAll()).thenReturn(listOf(genre1, genre2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = genre1)

        assertSoftly {
            it.assertThat(genre1.position).isEqualTo(position2)
            it.assertThat(genre2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(genre1, genre2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val genre1 = GenreUtils.newGenreDomain(id = 1)
        val genre2 = GenreUtils.newGenreDomain(id = 2)
        val position1 = genre1.position
        val position2 = genre2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(genre1, genre2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = genre1)

        assertSoftly {
            it.assertThat(genre1.position).isEqualTo(position2)
            it.assertThat(genre2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(genre1, genre2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.newData] for admin.
     */
    @Test
    fun newDataAdmin() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        whenever(repository.findAll()).thenReturn(genres)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.newData()

        verify(repository).findAll()
        verify(repository).deleteAll(genres)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.newData] for account.
     */
    @Test
    fun newDataAccount() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(genres)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.newData()

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).deleteAll(genres)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.getAll] for admin.
     */
    @Test
    fun getAllAdmin() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        whenever(repository.findAll()).thenReturn(genres)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.getAll()

        assertThat(result).isEqualTo(genres)

        verify(repository).findAll()
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.getAll] for account.
     */
    @Test
    fun getAllAccount() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(genres)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.getAll()

        assertThat(result).isEqualTo(genres)

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.updatePositions] for admin.
     */
    @Test
    fun updatePositionsAdmin() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        whenever(repository.findAll()).thenReturn(genres)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.updatePositions()

        for (i in genres.indices) {
            assertThat(genres[i].position).isEqualTo(i)
        }

        verify(repository).findAll()
        verify(repository).saveAll(genres)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [GenreService.updatePositions] for account.
     */
    @Test
    fun updatePositionsAccount() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(genres)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.updatePositions()

        for (i in genres.indices) {
            assertThat(genres[i].position).isEqualTo(i)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(genres)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Returns any mock for domain genre.
     *
     * @return any mock for domain genre
     */
    private fun anyDomain(): Genre {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Genre {
        return {
            val item = it.arguments[0] as Genre
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
