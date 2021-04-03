package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.facade.impl.GenreFacadeImpl
import com.github.vhromada.catalog.utils.GenreUtils
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
 * A class represents test for class [GenreFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class GenreFacadeTest {

    /**
     * Instance of [ParentService] for genres
     */
    @Mock
    private lateinit var service: ParentService<com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [Mapper] for genres
     */
    @Mock
    private lateinit var mapper: Mapper<Genre, com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [Validator] for genres
     */
    @Mock
    private lateinit var validator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [GenreFacade]
     */
    private lateinit var facade: GenreFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = GenreFacadeImpl(genreService = service, mapper = mapper, genreValidator = validator)
    }

    /**
     * Test method for [GenreFacade.get] with existing genre.
     */
    @Test
    fun getExistingGenre() {
        val entity = GenreUtils.newGenre(id = 1)
        val domain = GenreUtils.newGenreDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Genre>())).thenReturn(entity)
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
     * Test method for [GenreFacade.get] with not existing genre.
     */
    @Test
    fun getNotExistingGenre() {
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
     * Test method for [GenreFacade.update].
     */
    @Test
    fun update() {
        val entity = GenreUtils.newGenre(id = 1)
        val domain = GenreUtils.newGenreDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.map(source = any<Genre>())).thenReturn(domain)
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
     * Test method for [GenreFacade.update] with invalid genre.
     */
    @Test
    fun updateInvalidGenre() {
        val entity = GenreUtils.newGenre(id = Int.MAX_VALUE)

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
     * Test method for [GenreFacade.update] with not existing genre.
     */
    @Test
    fun updateNotExistingGenre() {
        val entity = GenreUtils.newGenre(id = Int.MAX_VALUE)

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
     * Test method for [GenreFacade.remove].
     */
    @Test
    fun remove() {
        val domain = GenreUtils.newGenreDomain(id = 1)

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
     * Test method for [GenreFacade.remove] with invalid genre.
     */
    @Test
    fun removeInvalidGenre() {
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
     * Test method for [GenreFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = GenreUtils.newGenreDomain(id = 1)

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
     * Test method for [GenreFacade.duplicate] with invalid genre.
     */
    @Test
    fun duplicateInvalidGenre() {
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
     * Test method for [GenreFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = GenreUtils.newGenreDomain(id = 1)
        val genres = listOf(domain, GenreUtils.newGenreDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(genres)
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
        verify(validator).validateMovingData(data = domain, list = genres, up = true)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [GenreFacade.moveUp] not existing genre.
     */
    @Test
    fun moveUpNotExistingGenre() {
        val domain = GenreUtils.newGenreDomain(id = 1)

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
     * Test method for [GenreFacade.moveUp] with not movable genre.
     */
    @Test
    fun moveUpNotMovableGenre() {
        val domain = GenreUtils.newGenreDomain(id = 1)
        val genres = listOf(domain, GenreUtils.newGenreDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(genres)
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
        verify(validator).validateMovingData(data = domain, list = genres, up = true)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [GenreFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = GenreUtils.newGenreDomain(id = 1)
        val genres = listOf(domain, GenreUtils.newGenreDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(genres)
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
        verify(validator).validateMovingData(data = domain, list = genres, up = false)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [GenreFacade.moveDown] with not existing genre.
     */
    @Test
    fun moveDownNotExistingGenre() {
        val domain = GenreUtils.newGenreDomain(id = 1)

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
     * Test method for [GenreFacade.moveDown] with not movable genre.
     */
    @Test
    fun moveDownNotMovableGenre() {
        val domain = GenreUtils.newGenreDomain(id = 1)
        val genres = listOf(domain, GenreUtils.newGenreDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(genres)
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
        verify(validator).validateMovingData(data = domain, list = genres, up = false)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [GenreFacade.newData].
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
     * Test method for [GenreFacade.getAll].
     */
    @Test
    fun getAll() {
        val entityList = listOf(GenreUtils.newGenre(id = 1), GenreUtils.newGenre(id = 2))
        val domainList = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        whenever(service.getAll()).thenReturn(domainList)
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Genre>>())).thenReturn(entityList)

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
     * Test method for [GenreFacade.add].
     */
    @Test
    fun add() {
        val entity = GenreUtils.newGenre(id = 1)
        val domain = GenreUtils.newGenreDomain(id = 1)

        whenever(mapper.map(source = any<Genre>())).thenReturn(domain)
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
     * Test method for [GenreFacade.add] with invalid genre.
     */
    @Test
    fun addInvalidGenre() {
        val entity = GenreUtils.newGenre(id = Int.MAX_VALUE)

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
     * Test method for [GenreFacade.updatePositions].
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
