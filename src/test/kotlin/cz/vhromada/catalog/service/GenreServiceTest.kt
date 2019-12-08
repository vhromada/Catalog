package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.domain.Genre
import cz.vhromada.catalog.repository.GenreRepository
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import org.mockito.Mock
import org.springframework.data.jpa.repository.JpaRepository

/**
 * A class represents test for class [GenreService].
 *
 * @author Vladimir Hromada
 */
class GenreServiceTest : MovableServiceTest<Genre>() {

    /**
     * Instance of [GenreRepository]
     */
    @Mock
    private lateinit var repository: GenreRepository

    override fun getRepository(): JpaRepository<Genre, Int> {
        return repository
    }

    override fun getService(): MovableService<Genre> {
        return GenreService(repository, cache)
    }

    override fun getCacheKey(): String {
        return "genres"
    }

    override fun getItem1(): Genre {
        return GenreUtils.newGenreDomain(1)
    }

    override fun getItem2(): Genre {
        return GenreUtils.newGenreDomain(2)
    }

    override fun getAddItem(): Genre {
        return GenreUtils.newGenreDomain(null)
    }

    override fun getCopyItem(): Genre {
        return GenreUtils.newGenreDomain(null)
                .copy(position = 0)
    }

    override fun anyItem(): Genre {
        return any()
    }

    override fun argumentCaptorItem(): KArgumentCaptor<Genre> {
        return argumentCaptor()
    }

    override fun assertDataDeepEquals(expected: Genre, actual: Genre) {
        GenreUtils.assertGenreDeepEquals(expected, actual)
    }

}
