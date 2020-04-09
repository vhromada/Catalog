package cz.vhromada.catalog.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.domain.Show
import cz.vhromada.catalog.repository.ShowRepository
import cz.vhromada.catalog.utils.ShowUtils
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.test.service.MovableServiceTest
import cz.vhromada.common.test.utils.TestConstants
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

    override fun getRepository(): JpaRepository<Show, Int> {
        return repository
    }

    override fun getAccountProvider(): AccountProvider {
        return accountProvider
    }

    override fun getTimeProvider(): TimeProvider {
        return timeProvider
    }

    override fun getService(): MovableService<Show> {
        return ShowService(repository, accountProvider, timeProvider, cache)
    }

    override fun getCacheKey(): String {
        return "shows${TestConstants.ACCOUNT.id}"
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

    override fun initAllDataMock(data: List<Show>) {
        whenever(repository.findByAuditCreatedUser(any())).thenReturn(data)
    }

    override fun verifyAllDataMock() {
        verify(repository).findByAuditCreatedUser(TestConstants.ACCOUNT_ID)
        verifyNoMoreInteractions(repository)
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
