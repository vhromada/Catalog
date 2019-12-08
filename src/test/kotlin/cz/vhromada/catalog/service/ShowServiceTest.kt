package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import cz.vhromada.catalog.domain.Show
import cz.vhromada.catalog.repository.ShowRepository
import cz.vhromada.catalog.utils.ShowUtils
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import org.mockito.Mock
import org.springframework.data.jpa.repository.JpaRepository

/**
 * A class represents test for class [ShowService].
 *
 * @author Vladimir Hromada
 */
class ShowServiceTest : MovableServiceTest<Show>() {

    /**
     * Instance of [ShowRepository]
     */
    @Mock
    private lateinit var repository: ShowRepository

    override fun getRepository(): JpaRepository<Show, Int> {
        return repository
    }

    override fun getService(): MovableService<Show> {
        return ShowService(repository, cache)
    }

    override fun getCacheKey(): String {
        return "shows"
    }

    override fun getItem1(): Show {
        return ShowUtils.newShowWithSeasons(1)
    }

    override fun getItem2(): Show {
        return ShowUtils.newShowWithSeasons(2)
    }

    override fun getAddItem(): Show {
        return ShowUtils.newShowDomain(null)
    }

    override fun getCopyItem(): Show {
        val show = ShowUtils.newShowWithSeasons(null)
        for (genre in show.genres) {
            genre.id = 1
            genre.position = 0
        }
        for (season in show.seasons) {
            season.position = 0
            for (episode in season.episodes) {
                episode.position = 0
            }
        }

        return show.copy(picture = 1, position = 0)
    }

    override fun anyItem(): Show {
        return any()
    }

    override fun argumentCaptorItem(): KArgumentCaptor<Show> {
        return argumentCaptor()
    }

    override fun assertDataDeepEquals(expected: Show, actual: Show) {
        ShowUtils.assertShowDeepEquals(expected, actual)
    }

}
