package cz.vhromada.catalog.facade.impl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.entity.Show
import cz.vhromada.catalog.facade.ShowFacade
import cz.vhromada.catalog.utils.ShowUtils
import cz.vhromada.common.Time
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.facade.MovableParentFacadeTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [ShowFacadeImpl].
 *
 * @author Vladimir Hromada
 */
class ShowFacadeImplTest : MovableParentFacadeTest<Show, cz.vhromada.catalog.domain.Show>() {

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
        verifyZeroInteractions(mapper, validator)
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
        verifyZeroInteractions(mapper, validator)
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
        verifyZeroInteractions(mapper, validator)
    }

    override fun initUpdateMock(domain: cz.vhromada.catalog.domain.Show) {
        super.initUpdateMock(domain)

        whenever(service.get(any())).thenReturn(domain)
    }

    override fun verifyUpdateMock(entity: Show, domain: cz.vhromada.catalog.domain.Show) {
        super.verifyUpdateMock(entity, domain)

        verify(service).get(entity.id!!)
    }

    override fun getFacade(): MovableParentFacade<Show> {
        return ShowFacadeImpl(service, mapper, validator)
    }

    override fun newEntity(id: Int?): Show {
        return ShowUtils.newShow(id)
    }

    override fun newDomain(id: Int?): cz.vhromada.catalog.domain.Show {
        return ShowUtils.newShowDomain(id)
    }

    override fun anyDomain(): cz.vhromada.catalog.domain.Show {
        return any()
    }

    override fun anyEntity(): Show {
        return any()
    }

}
