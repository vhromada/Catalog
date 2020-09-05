package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Genre
import com.github.vhromada.catalog.repository.GenreRepository
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.test.service.MovableServiceTest
import com.github.vhromada.common.test.utils.TestConstants
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
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

    override fun getRepository(): JpaRepository<Genre, Int> {
        return repository
    }

    override fun getAccountProvider(): AccountProvider {
        return accountProvider
    }

    override fun getTimeProvider(): TimeProvider {
        return timeProvider
    }

    override fun getService(): MovableService<Genre> {
        return GenreService(repository, accountProvider, timeProvider, cache)
    }

    override fun getCacheKey(): String {
        return "genres${TestConstants.ACCOUNT.id}"
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

    override fun initAllDataMock(data: List<Genre>) {
        whenever(repository.findByAuditCreatedUser(any())).thenReturn(data)
    }

    override fun verifyAllDataMock() {
        verify(repository).findByAuditCreatedUser(TestConstants.ACCOUNT_UUID)
        verifyNoMoreInteractions(repository)
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
