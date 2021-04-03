package com.github.vhromada.catalog.service

import com.github.vhromada.catalog.domain.Picture
import com.github.vhromada.catalog.repository.PictureRepository
import com.github.vhromada.catalog.utils.PictureUtils
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
 * A class represents test for class [PictureService].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class PictureServiceTest {

    /**
     * Instance of [PictureRepository]
     */
    @Mock
    private lateinit var repository: PictureRepository

    /**
     * Instance of [AccountProvider]
     */
    @Mock
    private lateinit var accountProvider: AccountProvider

    /**
     * Instance of [PictureService]
     */
    private lateinit var service: PictureService

    /**
     * Initializes service.
     */
    @BeforeEach
    fun setUp() {
        service = PictureService(pictureRepository = repository, accountProvider = accountProvider)
    }

    /**
     * Test method for [PictureService.get] with existing picture for admin.
     */
    @Test
    fun getExistingAdmin() {
        val picture = PictureUtils.newPictureDomain(id = 1)

        whenever(repository.findById(any())).thenReturn(Optional.of(picture))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.get(id = picture.id!!)

        assertThat(result).contains(picture)

        verify(repository).findById(picture.id!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.get] with existing picture for account.
     */
    @Test
    fun getExistingAccount() {
        val picture = PictureUtils.newPictureDomain(id = 1)

        whenever(repository.findByIdAndCreatedUser(id = any(), user = any())).thenReturn(Optional.of(picture))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.get(id = picture.id!!)

        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(picture)

        verify(repository).findByIdAndCreatedUser(id = picture.id!!, user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.get] with not existing picture for admin.
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
     * Test method for [PictureService.get] with not existing picture for account.
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
     * Test method for [PictureService.add].
     */
    @Test
    fun add() {
        val picture = PictureUtils.newPictureDomain(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(setIdAndPosition())

        val result = service.add(data = picture)

        assertSoftly {
            it.assertThat(picture.id).isEqualTo(1)
            it.assertThat(picture.position).isEqualTo(2)
        }

        verify(repository, times(2)).save(picture)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(picture)
    }

    /**
     * Test method for [PictureService.update].
     */
    @Test
    fun update() {
        val picture = PictureUtils.newPictureDomain(id = 1)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.update(data = picture)

        verify(repository).save(picture)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(picture)
    }

    /**
     * Test method for [PictureService.remove].
     */
    @Test
    fun remove() {
        val picture = PictureUtils.newPictureDomain(id = 1)

        service.remove(data = picture)

        verify(repository).delete(picture)
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
    }

    /**
     * Test method for [PictureService.duplicate].
     */
    @Test
    fun duplicate() {
        val copyArgumentCaptor = argumentCaptor<Picture>()
        val expectedPicture = PictureUtils.newPictureDomain(id = 1)
            .copy(id = null)

        whenever(repository.save(anyDomain())).thenAnswer(copy())

        val result = service.duplicate(data = PictureUtils.newPictureDomain(id = 1))

        verify(repository).save(copyArgumentCaptor.capture())
        verifyNoMoreInteractions(repository)
        verifyZeroInteractions(accountProvider)
        assertThat(result).isSameAs(copyArgumentCaptor.lastValue)
        PictureUtils.assertPictureDeepEquals(expected = expectedPicture, actual = result)
    }

    /**
     * Test method for [PictureService.moveUp] for admin.
     */
    @Test
    fun moveUpAdmin() {
        val picture1 = PictureUtils.newPictureDomain(id = 1)
        val picture2 = PictureUtils.newPictureDomain(id = 2)
        val position1 = picture1.position
        val position2 = picture2.position

        whenever(repository.findAll()).thenReturn(listOf(picture1, picture2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveUp(data = picture2)

        assertSoftly {
            it.assertThat(picture1.position).isEqualTo(position2)
            it.assertThat(picture2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(picture2, picture1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.moveUp] for account.
     */
    @Test
    fun moveUpAccount() {
        val picture1 = PictureUtils.newPictureDomain(id = 1)
        val picture2 = PictureUtils.newPictureDomain(id = 2)
        val position1 = picture1.position
        val position2 = picture2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(picture1, picture2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveUp(data = picture2)

        assertSoftly {
            it.assertThat(picture1.position).isEqualTo(position2)
            it.assertThat(picture2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(picture2, picture1))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.moveDown] for admin.
     */
    @Test
    fun moveDownAdmin() {
        val picture1 = PictureUtils.newPictureDomain(id = 1)
        val picture2 = PictureUtils.newPictureDomain(id = 2)
        val position1 = picture1.position
        val position2 = picture2.position

        whenever(repository.findAll()).thenReturn(listOf(picture1, picture2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.moveDown(data = picture1)

        assertSoftly {
            it.assertThat(picture1.position).isEqualTo(position2)
            it.assertThat(picture2.position).isEqualTo(position1)
        }

        verify(repository).findAll()
        verify(repository).saveAll(listOf(picture1, picture2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.moveDown] for account.
     */
    @Test
    fun moveDownAccount() {
        val picture1 = PictureUtils.newPictureDomain(id = 1)
        val picture2 = PictureUtils.newPictureDomain(id = 2)
        val position1 = picture1.position
        val position2 = picture2.position

        whenever(repository.findByCreatedUser(user = any())).thenReturn(listOf(picture1, picture2))
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.moveDown(data = picture1)

        assertSoftly {
            it.assertThat(picture1.position).isEqualTo(position2)
            it.assertThat(picture2.position).isEqualTo(position1)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(listOf(picture1, picture2))
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.newData] for admin.
     */
    @Test
    fun newDataAdmin() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        whenever(repository.findAll()).thenReturn(pictures)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.newData()

        verify(repository).findAll()
        verify(repository).deleteAll(pictures)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.newData] for account.
     */
    @Test
    fun newDataAccount() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(pictures)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.newData()

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).deleteAll(pictures)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.getAll] for admin.
     */
    @Test
    fun getAllAdmin() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        whenever(repository.findAll()).thenReturn(pictures)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        val result = service.getAll()

        assertThat(result).isEqualTo(pictures)

        verify(repository).findAll()
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.getAll] for account.
     */
    @Test
    fun getAllAccount() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(pictures)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        val result = service.getAll()

        assertThat(result).isEqualTo(pictures)

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.updatePositions] for admin.
     */
    @Test
    fun updatePositionsAdmin() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        whenever(repository.findAll()).thenReturn(pictures)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ADMIN)

        service.updatePositions()

        for (i in pictures.indices) {
            assertThat(pictures[i].position).isEqualTo(i)
        }

        verify(repository).findAll()
        verify(repository).saveAll(pictures)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Test method for [PictureService.updatePositions] for account.
     */
    @Test
    fun updatePositionsAccount() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        whenever(repository.findByCreatedUser(user = any())).thenReturn(pictures)
        whenever(accountProvider.getAccount()).thenReturn(TestConstants.ACCOUNT)

        service.updatePositions()

        for (i in pictures.indices) {
            assertThat(pictures[i].position).isEqualTo(i)
        }

        verify(repository).findByCreatedUser(user = TestConstants.ACCOUNT.uuid!!)
        verify(repository).saveAll(pictures)
        verify(accountProvider).getAccount()
        verifyNoMoreInteractions(repository, accountProvider)
    }

    /**
     * Returns any mock for domain picture.
     *
     * @return any mock for domain picture
     */
    private fun anyDomain(): Picture {
        return any()
    }

    /**
     * Sets ID and position.
     *
     * @return mocked answer
     */
    private fun setIdAndPosition(): (InvocationOnMock) -> Picture {
        return {
            val item = it.arguments[0] as Picture
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
