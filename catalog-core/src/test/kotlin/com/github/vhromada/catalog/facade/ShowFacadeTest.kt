package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.impl.ShowFacadeImpl
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.MovableParentFacade
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableParentFacadeTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [ShowFacade].
 *
 * @author Vladimir Hromada
 */
class ShowFacadeTest : MovableParentFacadeTest<Show, com.github.vhromada.catalog.domain.Show>() {

    /**
     * Test method for [ShowFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val showList = listOf(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2))
        var totalLength = 0
        for (show in showList) {
            for (season in show.seasons) {
                for (episode in season.episodes) {
                    totalLength += episode.length
                }
            }
        }
        val expectedTotalLength = totalLength

        whenever(service.getAll()).thenReturn(showList)

        val result = (getFacade() as ShowFacade).getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(expectedTotalLength))
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(accountProvider, mapper, validator)
    }

    /**
     * Test method for [ShowFacade.getSeasonsCount].
     */
    @Test
    fun getSeasonsCount() {
        val show1 = ShowUtils.newShowWithSeasons(1)
        val show2 = ShowUtils.newShowWithSeasons(2)
        val expectedSeasons = show1.seasons.size + show2.seasons.size

        whenever(service.getAll()).thenReturn(listOf(show1, show2))

        val result = (getFacade() as ShowFacade).getSeasonsCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedSeasons)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(accountProvider, mapper, validator)
    }

    /**
     * Test method for [ShowFacade.getEpisodesCount].
     */
    @Test
    fun getEpisodesCount() {
        val showList = listOf(ShowUtils.newShowWithSeasons(1), ShowUtils.newShowWithSeasons(2))
        var episodesCount = 0
        for (show in showList) {
            for (season in show.seasons) {
                episodesCount += season.episodes.size
            }
        }
        val expectedEpisodes = episodesCount

        whenever(service.getAll()).thenReturn(showList)

        val result = (getFacade() as ShowFacade).getEpisodesCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedEpisodes)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(accountProvider, mapper, validator)
    }

    override fun initUpdateMock(domain: com.github.vhromada.catalog.domain.Show) {
        super.initUpdateMock(domain)

        whenever(service.get(any())).thenReturn(Optional.of(domain))
    }

    override fun getFacade(): MovableParentFacade<Show> {
        return ShowFacadeImpl(service, accountProvider, timeProvider, mapper, validator)
    }

    override fun newEntity(id: Int?): Show {
        return ShowUtils.newShow(id)
    }

    override fun newDomain(id: Int?): com.github.vhromada.catalog.domain.Show {
        return ShowUtils.newShowDomain(id)
    }

    override fun anyDomain(): com.github.vhromada.catalog.domain.Show {
        return any()
    }

    override fun anyEntity(): Show {
        return any()
    }

}
