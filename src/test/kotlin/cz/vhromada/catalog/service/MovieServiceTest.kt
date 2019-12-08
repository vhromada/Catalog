package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.domain.Movie
import cz.vhromada.catalog.repository.MovieRepository
import cz.vhromada.catalog.utils.MovieUtils
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
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

    override fun getRepository(): JpaRepository<Movie, Int> {
        return repository
    }

    override fun getService(): MovableService<Movie> {
        return MovieService(repository, cache)
    }

    override fun getCacheKey(): String {
        return "movies"
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
        movie.id = null
        for (medium in movie.media) {
            medium.id = null
        }

        return movie
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
