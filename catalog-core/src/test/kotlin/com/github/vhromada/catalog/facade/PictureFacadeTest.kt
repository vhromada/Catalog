package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.facade.impl.PictureFacadeImpl
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

/**
 * A class represents test for class [PictureFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class PictureFacadeTest {

    /**
     * Instance of [ParentService] for pictures
     */
    @Mock
    private lateinit var service: ParentService<com.github.vhromada.catalog.domain.Picture>

    /**
     * Instance of [Mapper] for pictures
     */
    @Mock
    private lateinit var mapper: Mapper<Picture, com.github.vhromada.catalog.domain.Picture>

    /**
     * Instance of [Validator] for pictures
     */
    @Mock
    private lateinit var validator: Validator<Picture, com.github.vhromada.catalog.domain.Picture>

    /**
     * Instance of [PictureFacade]
     */
    private lateinit var facade: PictureFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = PictureFacadeImpl(pictureService = service, mapper = mapper, pictureValidator = validator)
    }

    /**
     * Test method for [PictureFacade.get] with existing picture.
     */
    @Test
    fun getExistingPicture() {
        val entity = PictureUtils.newPicture(id = 1)
        val domain = PictureUtils.newPictureDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Picture>())).thenReturn(entity)
        whenever(validator.validateExists(data = any())).thenReturn(Result())

        val result = facade.get(id = entity.id!!)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entity)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = entity.id!!)
        verify(mapper).mapBack(source = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, mapper, validator)
    }

    /**
     * Test method for [PictureFacade.get] with not existing picture.
     */
    @Test
    fun getNotExistingPicture() {
        whenever(service.get(id = any())).thenReturn(Optional.empty())
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = Int.MAX_VALUE)
        verify(validator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.update].
     */
    @Test
    fun update() {
        val entity = PictureUtils.newPicture(id = 1)
        val domain = PictureUtils.newPictureDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.map(source = any<Picture>())).thenReturn(domain)
        whenever(validator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(validator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = entity.id!!)
        verify(service).update(data = domain)
        verify(mapper).map(source = entity)
        verify(validator).validate(data = entity, update = true)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, mapper, validator)
    }

    /**
     * Test method for [PictureFacade.update] with invalid picture.
     */
    @Test
    fun updateInvalidPicture() {
        val entity = PictureUtils.newPicture(id = Int.MAX_VALUE)

        whenever(validator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(validator).validate(data = entity, update = true)
        verifyNoMoreInteractions(validator)
        verifyZeroInteractions(service, mapper)
    }

    /**
     * Test method for [PictureFacade.update] with not existing picture.
     */
    @Test
    fun updateNotExistingPicture() {
        val entity = PictureUtils.newPicture(id = Int.MAX_VALUE)

        whenever(service.get(id = any())).thenReturn(Optional.empty())
        whenever(validator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = entity.id!!)
        verify(validator).validate(data = entity, update = true)
        verify(validator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.remove].
     */
    @Test
    fun remove() {
        val domain = PictureUtils.newPictureDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(validator.validateExists(data = any())).thenReturn(Result())

        val result = facade.remove(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = 1)
        verify(service).remove(data = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.remove] with invalid picture.
     */
    @Test
    fun removeInvalidPicture() {
        whenever(service.get(id = any())).thenReturn(Optional.empty())
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = Int.MAX_VALUE)
        verify(validator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = PictureUtils.newPictureDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(validator.validateExists(data = any())).thenReturn(Result())

        val result = facade.duplicate(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = 1)
        verify(service).duplicate(data = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.duplicate] with invalid picture.
     */
    @Test
    fun duplicateInvalidPicture() {
        whenever(service.get(id = any())).thenReturn(Optional.empty())
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = Int.MAX_VALUE)
        verify(validator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = PictureUtils.newPictureDomain(id = 1)
        val pictures = listOf(domain, PictureUtils.newPictureDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(pictures)
        whenever(validator.validateExists(data = any())).thenReturn(Result())
        whenever(validator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = 1)
        verify(service).getAll()
        verify(service).moveUp(data = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verify(validator).validateMovingData(data = domain, list = pictures, up = true)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.moveUp] not existing picture.
     */
    @Test
    fun moveUpNotExistingPicture() {
        val domain = PictureUtils.newPictureDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = 1)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.moveUp] with not movable picture.
     */
    @Test
    fun moveUpNotMovablePicture() {
        val domain = PictureUtils.newPictureDomain(id = 1)
        val pictures = listOf(domain, PictureUtils.newPictureDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(pictures)
        whenever(validator.validateExists(data = any())).thenReturn(Result())
        whenever(validator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = 1)
        verify(service).getAll()
        verify(validator).validateExists(data = Optional.of(domain))
        verify(validator).validateMovingData(data = domain, list = pictures, up = true)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = PictureUtils.newPictureDomain(id = 1)
        val pictures = listOf(domain, PictureUtils.newPictureDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(pictures)
        whenever(validator.validateExists(data = any())).thenReturn(Result())
        whenever(validator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).get(id = 1)
        verify(service).getAll()
        verify(service).moveDown(data = domain)
        verify(validator).validateExists(data = Optional.of(domain))
        verify(validator).validateMovingData(data = domain, list = pictures, up = false)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.moveDown] with not existing picture.
     */
    @Test
    fun moveDownNotExistingPicture() {
        val domain = PictureUtils.newPictureDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(validator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = 1)
        verify(validator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.moveDown] with not movable picture.
     */
    @Test
    fun moveDownNotMovablePicture() {
        val domain = PictureUtils.newPictureDomain(id = 1)
        val pictures = listOf(domain, PictureUtils.newPictureDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(pictures)
        whenever(validator.validateExists(data = any())).thenReturn(Result())
        whenever(validator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(service).get(id = 1)
        verify(service).getAll()
        verify(validator).validateExists(data = Optional.of(domain))
        verify(validator).validateMovingData(data = domain, list = pictures, up = false)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [PictureFacade.newData].
     */
    @Test
    fun newData() {
        val result = facade.newData()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).newData()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

    /**
     * Test method for [PictureFacade.getAll].
     */
    @Test
    fun getAll() {
        val entityList = listOf(PictureUtils.newPicture(id = 1), PictureUtils.newPicture(id = 2))
        val domainList = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        whenever(service.getAll()).thenReturn(domainList)
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Picture>>())).thenReturn(entityList)

        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entityList)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verify(mapper).mapBack(source = domainList)
        verifyNoMoreInteractions(service, mapper)
        verifyZeroInteractions(validator)
    }

    /**
     * Test method for [PictureFacade.add].
     */
    @Test
    fun add() {
        val entity = PictureUtils.newPicture(id = 1)
        val domain = PictureUtils.newPictureDomain(id = 1)

        whenever(mapper.map(source = any<Picture>())).thenReturn(domain)
        whenever(validator.validate(data = any(), update = any())).thenReturn(Result())

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).add(data = domain)
        verify(mapper).map(source = entity)
        verify(validator).validate(data = entity, update = false)
        verifyNoMoreInteractions(service, mapper, validator)
    }

    /**
     * Test method for [PictureFacade.add] with invalid picture.
     */
    @Test
    fun addInvalidPicture() {
        val entity = PictureUtils.newPicture(id = Int.MAX_VALUE)

        whenever(validator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(validator).validate(data = entity, update = false)
        verifyNoMoreInteractions(validator)
        verifyZeroInteractions(service, mapper)
    }

    /**
     * Test method for [PictureFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).updatePositions()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

}
