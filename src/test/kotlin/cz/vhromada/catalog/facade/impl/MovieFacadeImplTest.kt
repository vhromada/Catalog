package cz.vhromada.catalog.facade.impl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.entity.Movie
import cz.vhromada.catalog.facade.MovieFacade
import cz.vhromada.catalog.utils.MovieUtils
import cz.vhromada.common.Time
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.facade.MovableParentFacadeTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [MovieFacadeImpl].
 *
 * @author Vladimir Hromada
 */
class MovieFacadeImplTest : MovableParentFacadeTest<Movie, cz.vhromada.catalog.domain.Movie>() {

    /**
     * Test method for [MovieFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val movie1 = MovieUtils.newMovieDomain(1)
        val movie2 = MovieUtils.newMovieDomain(2)
        val expectedCount = movie1.media.size + movie2.media.size

        whenever(service.getAll()).thenReturn(listOf(movie1, movie2))

        val result = (getFacade() as MovieFacade).getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedCount)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

    /**
     * Test method for [MovieFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val movies = listOf(MovieUtils.newMovieDomain(1), MovieUtils.newMovieDomain(2))
        var totalLength = 0
        for (movie in movies) {
            for (medium in movie.media) {
                totalLength += medium.length
            }
        }
        val expectedTotalLength = totalLength

        whenever(service.getAll()).thenReturn(movies)

        val result = (getFacade() as MovieFacade).getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(expectedTotalLength))
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

    override fun initUpdateMock(domain: cz.vhromada.catalog.domain.Movie) {
        super.initUpdateMock(domain)

        whenever(service.get(any())).thenReturn(domain)
    }

    override fun verifyUpdateMock(entity: Movie, domain: cz.vhromada.catalog.domain.Movie) {
        super.verifyUpdateMock(entity, domain)

        verify(service).get(entity.id!!)
    }

    override fun getFacade(): MovableParentFacade<Movie> {
        return MovieFacadeImpl(service, mapper, validator)
    }

    override fun newEntity(id: Int?): Movie {
        return MovieUtils.newMovie(id)
    }

    override fun newDomain(id: Int?): cz.vhromada.catalog.domain.Movie {
        return MovieUtils.newMovieDomain(id)
    }

    override fun anyDomain(): cz.vhromada.catalog.domain.Movie {
        return any()
    }

    override fun anyEntity(): Movie {
        return any()
    }

}
