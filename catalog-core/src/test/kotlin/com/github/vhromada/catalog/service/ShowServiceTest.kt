package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Show
import com.github.vhromada.catalog.repository.ShowRepository
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.provider.AccountProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

/**
 * A class represents test for class [ShowService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class ShowServiceTest {

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
     * Instance of [ShowService]
     */
    private lateinit var service: ShowService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = ShowService(showRepository = repository, accountProvider = accountProvider)
    }

    /**
     * Test method for [ShowService.get] with existing show for admin.
     */
    @Test
    fun getExistingAdmin() {
        val show = ShowUtils.newShowDomain(id = 1)

        whenever(repository.findById(any())).thenReturn(Optional.of(show))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = show.id!!)

        assertThat(result).contains(show)

        verify(repository).findById(show.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.get] with existing show for account.
     */
    @Test
    fun getExistingAccount() {
        val show = ShowUtils.newShowDomain(id = 1)

        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(show))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = show.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(show)

        verify(repository).findByIdAndCreatedUser(id = show.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.get] with not existing show for admin.
     */
    @Test
    fun getNotExistingAdmin() {
        whenever(repository.findById(any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(repository).findById(Int.MAX_VALUE)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.get] with not existing show for account.
     */
    @Test
    fun getNotExistingAccount() {
        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.empty())
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = Int.MAX_VALUE)

        assertThat(result).isNotPresent

        verify(repository).findByIdAndCreatedUser(id = Int.MAX_VALUE, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.add].
     */
    @Test
    fun add() {
        val show = ShowUtils.newShowDomain(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = show)

        assertSoftly {
            it.assertThat(show.id).isEqualTo(1)
            it.assertThat(show.position).isEqualTo(2)
        }

        verify(repository, times(2)).save(show)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(show)
    }

    /**
     * Test method for [ShowService.update].
     */
    @Test
    fun update() {
        val show = ShowUtils.newShowDomain(id = 1)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = show)

        verify(repository).save(show)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(show)
    }

    /**
     * Test method for [ShowService.remove].
     */
    @Test
    fun remove() {
        val show = ShowUtils.newShowDomain(id = 1)

        service.remove(data = show)

        verify(repository).delete(show)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
    }

    /**
     * Test method for [ShowService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Show>()
        val expectedShow = ShowUtils.newShowDomain(id = 1)
            .copy(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = ShowUtils.newShowDomain(id = 1))

        verify(repository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        ShowUtils.assertShowDeepEquals(expected = expectedShow, actual = result)
    }

    /**
     * Test method for [ShowService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val show1 = ShowUtils.newShowDomain(id = 1)
        val show2 = ShowUtils.newShowDomain(id = 2)
        val position1 = show1.position
        val position2 = show2.position

        whenever(repository.findAll()).thenReturn(listOf(show1, show2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = show2)

        assertSoftly {
            it.assertThat(show1.position).isEqualTo(position2)
            it.assertThat(show2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(show2, show1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val show1 = ShowUtils.newShowDomain(id = 1)
        val show2 = ShowUtils.newShowDomain(id = 2)
        val position1 = show1.position
        val position2 = show2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(show1, show2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = show2)

        assertSoftly {
            it.assertThat(show1.position).isEqualTo(position2)
            it.assertThat(show2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(show2, show1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val show1 = ShowUtils.newShowDomain(id = 1)
        val show2 = ShowUtils.newShowDomain(id = 2)
        val position1 = show1.position
        val position2 = show2.position

        whenever(repository.findAll()).thenReturn(listOf(show1, show2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = show1)

        assertSoftly {
            it.assertThat(show1.position).isEqualTo(position2)
            it.assertThat(show2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(show1, show2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val show1 = ShowUtils.newShowDomain(id = 1)
        val show2 = ShowUtils.newShowDomain(id = 2)
        val position1 = show1.position
        val position2 = show2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(show1, show2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = show1)

        assertSoftly {
            it.assertThat(show1.position).isEqualTo(position2)
            it.assertThat(show2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(show1, show2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.newData] for admin.
     */
    @Test
    fun newDataAdmin() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        whenever(repository.findAll()).thenReturn(shows)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.newData()

        verify(repository).findAll()
        verify(repository).deleteAll(shows)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.newData] for account.
     */
    @Test
    fun newDataAccount() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(shows)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.newData()

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).deleteAll(shows)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.getAll] for admin.
     */
    @Test
    fun getAllAdmin() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        whenever(repository.findAll()).thenReturn(shows)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.getAll()

        assertThat(result).isEqualTo(shows)

        verify(repository).findAll()
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.getAll] for account.
     */
    @Test
    fun getAllAccount() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(shows)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.getAll()

        assertThat(result).isEqualTo(shows)

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.updatePositions] for admin.
     */
    @Test
    fun updatePositionsAdmin() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        whenever(repository.findAll()).thenReturn(shows)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.updatePositions()

        for (i in shows.indices) {
            assertThat(shows[i].position).isEqualTo(i)
        }

        verify(repository).findAll()
        verify(repository).saveAll(shows)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [ShowService.updatePositions] for account.
     */
    @Test
    fun updatePositionsAccount() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(shows)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.updatePositions()

        for (i in shows.indices) {
            assertThat(shows[i].position).isEqualTo(i)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(shows)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Returns any mock for domain show.
     *
     * @return any mock for domain show
     */
    private fun anyDomain(): Show {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Show {
        return {
            val item = it.arguments[0] as Show
            item.id = 1
            item.position = 2
            item
        }
    }

    /**
     * Copying answer.
     *
     * @return mocked answer
     */
    private fun copy(): (InvocationOnMock) -> Any {
        return {
            it.arguments[0]
        }
    }

}
