package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.domain.Movie
import cz.vhromada.catalog.repository.MovieRepository
import cz.vhromada.catalog.utils.MovieUtils
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import cz.vhromada.common.test.utils.TestConstants
import org.mockito.Mock
import org.springframework.data.jpa.repository.JpaRepository

/**
 * A class represents test for class [MovieService].
 *
 * @author Vladimir Hromada
 */
class MovieServiceTest : MovableServiceTest<Movie>() {

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
     * Instance of [TimeProvider]
     */
    @Mock
    private lateinit var timeProvider: TimeProvider

    override fun getRepository(): JpaRepository<Movie, Int> {
        return repository
    }

    override fun getAccountProvider(): AccountProvider {
        return accountProvider
    }

    override fun getTimeProvider(): TimeProvider {
        return timeProvider
    }

    override fun getService(): MovableService<Movie> {
        return MovieService(repository, accountProvider, timeProvider, cache)
    }

    override fun getCacheKey(): String {
        return "movies${TestConstants.ACCOUNT.id}"
    }

    override fun getItem1(): Movie {
        return MovieUtils.newMovieDomain(1)
    }

    override fun getItem2(): Movie {
        return MovieUtils.newMovieDomain(2)
    }

    override fun getAddItem(): Movie {
        return MovieUtils.newMovieDomain(null)
    }

    override fun getCopyItem(): Movie {
        val movie = MovieUtils.newMovieDomain(1)
                .copy(id = null)
        for (medium in movie.media) {
            medium.id = null
        }

        return movie
    }

    override fun initAllDataMock(data: List<Movie>) {
        whenever(repository.findByAuditCreatedUser(any())).thenReturn(data)
    }

    override fun verifyAllDataMock() {
        verify(repository).findByAuditCreatedUser(TestConstants.ACCOUNT_ID)
        verifyNoMoreInteractions(repository)
    }

    override fun anyItem(): Movie {
        return any()
    }

    override fun argumentCaptorItem(): KArgumentCaptor<Movie> {
        return argumentCaptor()
    }

    override fun assertDataDeepEquals(expected: Movie, actual: Movie) {
        MovieUtils.assertMovieDeepEquals(expected, actual)
    }

}
