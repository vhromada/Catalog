package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Movie
import com.github.vhromada.catalog.repository.MovieRepository
import com.github.vhromada.catalog.utils.MediumUtils
import com.github.vhromada.catalog.utils.MovieUtils
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
 * A class represents test for class [MovieService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class MovieServiceTest {

    /**
     * Instance of [MovieRepository]
     */
    @Mock
    private lateinit var repository: MovieRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [MovieService]
     */
    private lateinit var service: MovieService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = MovieService(movieRepository = repository, accountProvider = accountProvider)
    }

    /**
     * Test method for [MovieService.get] with existing movie for admin.
     */
    @Test
    fun getExistingAdmin() {
        val movie = MovieUtils.newMovieDomain(id = 1)

        whenever(repository.findById(any())).thenReturn(Optional.of(movie))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = movie.id!!)

        assertThat(result).contains(movie)

        verify(repository).findById(movie.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.get] with existing movie for account.
     */
    @Test
    fun getExistingAccount() {
        val movie = MovieUtils.newMovieDomain(id = 1)

        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(movie))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = movie.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(movie)

        verify(repository).findByIdAndCreatedUser(id = movie.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.get] with not existing movie for admin.
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
     * Test method for [MovieService.get] with not existing movie for account.
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
     * Test method for [MovieService.add].
     */
    @Test
    fun add() {
        val movie = MovieUtils.newMovieDomain(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = movie)

        assertSoftly {
            it.assertThat(movie.id).isEqualTo(1)
            it.assertThat(movie.position).isEqualTo(2)
        }

        verify(repository, times(2)).save(movie)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(movie)
    }

    /**
     * Test method for [MovieService.update].
     */
    @Test
    fun update() {
        val movie = MovieUtils.newMovieDomain(id = 1)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = movie)

        verify(repository).save(movie)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(movie)
    }

    /**
     * Test method for [MovieService.remove].
     */
    @Test
    fun remove() {
        val movie = MovieUtils.newMovieDomain(id = 1)

        service.remove(data = movie)

        verify(repository).delete(movie)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
    }

    /**
     * Test method for [MovieService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Movie>()
        val expectedMovie = MovieUtils.newMovieDomain(id = 1)
            .copy(id = null, media = listOf(MediumUtils.newMediumDomain(id = null)))

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = MovieUtils.newMovieDomain(id = 1))

        verify(repository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = result)
    }

    /**
     * Test method for [MovieService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val movie1 = MovieUtils.newMovieDomain(id = 1)
        val movie2 = MovieUtils.newMovieDomain(id = 2)
        val position1 = movie1.position
        val position2 = movie2.position

        whenever(repository.findAll()).thenReturn(listOf(movie1, movie2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = movie2)

        assertSoftly {
            it.assertThat(movie1.position).isEqualTo(position2)
            it.assertThat(movie2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(movie2, movie1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val movie1 = MovieUtils.newMovieDomain(id = 1)
        val movie2 = MovieUtils.newMovieDomain(id = 2)
        val position1 = movie1.position
        val position2 = movie2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(movie1, movie2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = movie2)

        assertSoftly {
            it.assertThat(movie1.position).isEqualTo(position2)
            it.assertThat(movie2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(movie2, movie1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val movie1 = MovieUtils.newMovieDomain(id = 1)
        val movie2 = MovieUtils.newMovieDomain(id = 2)
        val position1 = movie1.position
        val position2 = movie2.position

        whenever(repository.findAll()).thenReturn(listOf(movie1, movie2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = movie1)

        assertSoftly {
            it.assertThat(movie1.position).isEqualTo(position2)
            it.assertThat(movie2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(movie1, movie2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val movie1 = MovieUtils.newMovieDomain(id = 1)
        val movie2 = MovieUtils.newMovieDomain(id = 2)
        val position1 = movie1.position
        val position2 = movie2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(movie1, movie2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = movie1)

        assertSoftly {
            it.assertThat(movie1.position).isEqualTo(position2)
            it.assertThat(movie2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(movie1, movie2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.newData] for admin.
     */
    @Test
    fun newDataAdmin() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        whenever(repository.findAll()).thenReturn(movies)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.newData()

        verify(repository).findAll()
        verify(repository).deleteAll(movies)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.newData] for account.
     */
    @Test
    fun newDataAccount() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(movies)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.newData()

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).deleteAll(movies)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.getAll] for admin.
     */
    @Test
    fun getAllAdmin() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        whenever(repository.findAll()).thenReturn(movies)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.getAll()

        assertThat(result).isEqualTo(movies)

        verify(repository).findAll()
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.getAll] for account.
     */
    @Test
    fun getAllAccount() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(movies)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.getAll()

        assertThat(result).isEqualTo(movies)

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.updatePositions] for admin.
     */
    @Test
    fun updatePositionsAdmin() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        whenever(repository.findAll()).thenReturn(movies)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.updatePositions()

        for (i in movies.indices) {
            assertThat(movies[i].position).isEqualTo(i)
        }

        verify(repository).findAll()
        verify(repository).saveAll(movies)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [MovieService.updatePositions] for account.
     */
    @Test
    fun updatePositionsAccount() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(movies)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.updatePositions()

        for (i in movies.indices) {
            assertThat(movies[i].position).isEqualTo(i)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(movies)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Returns any mock for domain movie.
     *
     * @return any mock for domain movie
     */
    private fun anyDomain(): Movie {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Movie {
        return {
            val item = it.arguments[0] as Movie
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
