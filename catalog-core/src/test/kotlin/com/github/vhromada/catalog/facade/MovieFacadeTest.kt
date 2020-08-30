package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.facade.impl.MovieFacadeImpl
import com.github.vhromada.catalog.utils.MovieUtils
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.MovableParentFacade
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableParentFacadeTest
import com.github.vhromada.common.test.utils.TestConstants
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [MovieFacade].
 *
 * @author Vladimir Hromada
 */
class MovieFacadeTest : MovableParentFacadeTest<Movie, com.github.vhromada.catalog.domain.Movie>() {

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
        verifyZeroInteractions(accountProvider, mapper, validator)
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
        verifyZeroInteractions(accountProvider, mapper, validator)
    }

    override fun initUpdateMock(domain: com.github.vhromada.catalog.domain.Movie) {
        super.initUpdateMock(domain)

        whenever(service.get(any())).thenReturn(domain)
    }

    override fun initAddProviders() {
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)
        whenever(timeProvider.getTime()).thenReturn(TestConstants.TIME)
    }

    override fun verifyAddProviders() {
        verify(accountProvider).getAccount()
        verify(timeProvider).getTime()
        verifyNoMoreInteractions(accountProvider, timeProvider)
    }

    override fun getFacade(): MovableParentFacade<Movie> {
        return MovieFacadeImpl(service, accountProvider, timeProvider, mapper, validator)
    }

    override fun newEntity(id: Int?): Movie {
        return MovieUtils.newMovie(id)
    }

    override fun newDomain(id: Int?): com.github.vhromada.catalog.domain.Movie {
        return MovieUtils.newMovieDomain(id)
    }

    override fun anyDomain(): com.github.vhromada.catalog.domain.Movie {
        return any()
    }

    override fun anyEntity(): Movie {
        return any()
    }

}
