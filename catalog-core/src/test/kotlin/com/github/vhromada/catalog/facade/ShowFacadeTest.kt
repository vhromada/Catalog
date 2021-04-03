package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.impl.ShowFacadeImpl
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
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
 * A class represents test for class [ShowFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class ShowFacadeTest {

    /**
     * Instance of [ParentService] for shows
     */
    @Mock
    private lateinit var showService: ParentService<com.github.vhromada.catalog.domain.Show>

    /**
     * Instance of [ParentService] for pictures
     */
    @Mock
    private lateinit var pictureService: ParentService<com.github.vhromada.catalog.domain.Picture>

    /**
     * Instance of [ParentService] for genres
     */
    @Mock
    private lateinit var genreService: ParentService<com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [Mapper] for shows
     */
    @Mock
    private lateinit var mapper: Mapper<Show, com.github.vhromada.catalog.domain.Show>

    /**
     * Instance of [Validator] for shows
     */
    @Mock
    private lateinit var showValidator: Validator<Show, com.github.vhromada.catalog.domain.Show>

    /**
     * Instance of [Validator] for pictures
     */
    @Mock
    private lateinit var pictureValidator: Validator<Picture, com.github.vhromada.catalog.domain.Picture>

    /**
     * Instance of [Validator] for genres
     */
    @Mock
    private lateinit var genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [ShowFacade]
     */
    private lateinit var facade: ShowFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = ShowFacadeImpl(
            showService = showService,
            pictureService = pictureService,
            genreService = genreService,
            mapper = mapper,
            showValidator = showValidator,
            pictureValidator = pictureValidator,
            genreValidator = genreValidator
        )
    }

    /**
     * Test method for [ShowFacade.get] with existing show.
     */
    @Test
    fun getExistingShow() {
        val entity = ShowUtils.newShow(id = 1)
        val domain = ShowUtils.newShowDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Show>())).thenReturn(entity)
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.get(id = entity.id!!)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entity)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).get(id = entity.id!!)
        verify(mapper).mapBack(source = domain)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(showService, mapper, showValidator)
        verifyZeroInteractions(pictureService, genreService, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.get] with not existing show.
     */
    @Test
    fun getNotExistingShow() {
        whenever(showService.get(id = any())).thenReturn(Optional.empty())
        whenever(showValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = Int.MAX_VALUE)
        verify(showValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.update].
     */
    @Test
    fun update() {
        val entity = ShowUtils.newShow(id = 1)
        val domain = ShowUtils.newShowDomain(id = 1)
        val picture = PictureUtils.newPictureDomain(id = 1)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(mapper.map(source = any<Show>())).thenReturn(domain)
        whenever(showValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).get(id = entity.id!!)
        verify(showService).update(data = domain)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(mapper).map(source = entity)
        verify(showValidator).validate(data = entity, update = true)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(showService, pictureService, genreService, mapper, showValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.update] with invalid show.
     */
    @Test
    fun updateInvalidShow() {
        val entity = ShowUtils.newShow(id = Int.MAX_VALUE)

        whenever(showValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showValidator).validate(data = entity, update = true)
        verifyNoMoreInteractions(showValidator)
        verifyZeroInteractions(showService, pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.update] with show with invalid picture.
     */
    @Test
    fun updateInvalidPicture() {
        val entity = ShowUtils.newShow(id = 1)
        val domain = ShowUtils.newShowDomain(id = 1)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(pictureService.get(id = any())).thenReturn(Optional.empty())
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(showValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = entity.id!!)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(showValidator).validate(data = entity, update = true)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verify(pictureValidator).validateExists(data = Optional.empty())
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(showService, pictureService, genreService, showValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [ShowFacade.update] with show with invalid genre.
     */
    @Test
    fun updateInvalidGenre() {
        val entity = ShowUtils.newShow(id = 1)
        val domain = ShowUtils.newShowDomain(id = 1)
        val picture = PictureUtils.newPictureDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.empty())
        whenever(showValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = entity.id!!)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(showValidator).validate(data = entity, update = true)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(showService, pictureService, genreService, showValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [ShowFacade.update] with not existing show.
     */
    @Test
    fun updateNotExistingShow() {
        val entity = ShowUtils.newShow(id = Int.MAX_VALUE)
        val picture = PictureUtils.newPictureDomain(id = 1)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.empty())
        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(showValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(showValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = entity.id!!)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(showValidator).validate(data = entity, update = true)
        verify(showValidator).validateExists(data = Optional.empty())
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(showService, pictureService, genreService, showValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [ShowFacade.remove].
     */
    @Test
    fun remove() {
        val domain = ShowUtils.newShowDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.remove(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).get(id = 1)
        verify(showService).remove(data = domain)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.remove] with invalid show.
     */
    @Test
    fun removeInvalidShow() {
        whenever(showService.get(id = any())).thenReturn(Optional.empty())
        whenever(showValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = Int.MAX_VALUE)
        verify(showValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = ShowUtils.newShowDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.duplicate(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).get(id = 1)
        verify(showService).duplicate(data = domain)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.duplicate] with invalid show.
     */
    @Test
    fun duplicateInvalidShow() {
        whenever(showService.get(id = any())).thenReturn(Optional.empty())
        whenever(showValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = Int.MAX_VALUE)
        verify(showValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = ShowUtils.newShowDomain(id = 1)
        val shows = listOf(domain, ShowUtils.newShowDomain(id = 2))

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(showService.getAll()).thenReturn(shows)
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())
        whenever(showValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).get(id = 1)
        verify(showService).getAll()
        verify(showService).moveUp(data = domain)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verify(showValidator).validateMovingData(data = domain, list = shows, up = true)
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.moveUp] not existing show.
     */
    @Test
    fun moveUpNotExistingShow() {
        val domain = ShowUtils.newShowDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(showValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = 1)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.moveUp] with not movable show.
     */
    @Test
    fun moveUpNotMovableShow() {
        val domain = ShowUtils.newShowDomain(id = 1)
        val shows = listOf(domain, ShowUtils.newShowDomain(id = 2))

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(showService.getAll()).thenReturn(shows)
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())
        whenever(showValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = 1)
        verify(showService).getAll()
        verify(showValidator).validateExists(data = Optional.of(domain))
        verify(showValidator).validateMovingData(data = domain, list = shows, up = true)
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = ShowUtils.newShowDomain(id = 1)
        val shows = listOf(domain, ShowUtils.newShowDomain(id = 2))

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(showService.getAll()).thenReturn(shows)
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())
        whenever(showValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).get(id = 1)
        verify(showService).getAll()
        verify(showService).moveDown(data = domain)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verify(showValidator).validateMovingData(data = domain, list = shows, up = false)
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.moveDown] with not existing show.
     */
    @Test
    fun moveDownNotExistingShow() {
        val domain = ShowUtils.newShowDomain(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(showValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = 1)
        verify(showValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.moveDown] with not movable show.
     */
    @Test
    fun moveDownNotMovableShow() {
        val domain = ShowUtils.newShowDomain(id = 1)
        val shows = listOf(domain, ShowUtils.newShowDomain(id = 2))

        whenever(showService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(showService.getAll()).thenReturn(shows)
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())
        whenever(showValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = 1)
        verify(showService).getAll()
        verify(showValidator).validateExists(data = Optional.of(domain))
        verify(showValidator).validateMovingData(data = domain, list = shows, up = false)
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.newData].
     */
    @Test
    fun newData() {
        val result = facade.newData()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).newData()
        verifyNoMoreInteractions(showService)
        verifyZeroInteractions(pictureService, genreService, mapper, showValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.getAll].
     */
    @Test
    fun getAll() {
        val entityList = listOf(ShowUtils.newShow(id = 1), ShowUtils.newShow(id = 2))
        val domainList = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        whenever(showService.getAll()).thenReturn(domainList)
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Show>>())).thenReturn(entityList)

        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entityList)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).getAll()
        verify(mapper).mapBack(source = domainList)
        verifyNoMoreInteractions(showService, mapper)
        verifyZeroInteractions(pictureService, genreService, showValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.add].
     */
    @Test
    fun add() {
        val entity = ShowUtils.newShow(id = 1)
        val domain = ShowUtils.newShowDomain(id = 1)
        val picture = PictureUtils.newPictureDomain(id = 1)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(mapper.map(source = any<Show>())).thenReturn(domain)
        whenever(showValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).add(data = domain)
        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(mapper).map(source = entity)
        verify(showValidator).validate(data = entity, update = false)
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(showService, pictureService, genreService, mapper, showValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.add] with invalid show.
     */
    @Test
    fun addInvalidShow() {
        val entity = ShowUtils.newShow(id = Int.MAX_VALUE)

        whenever(showValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showValidator).validate(data = entity, update = false)
        verifyNoMoreInteractions(showValidator)
        verifyZeroInteractions(showService, pictureService, genreService, mapper, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.add] with show with invalid picture.
     */
    @Test
    fun addInvalidPicture() {
        val entity = ShowUtils.newShow(id = Int.MAX_VALUE)
        val genre = GenreUtils.newGenreDomain(id = 1)

        whenever(pictureService.get(id = any())).thenReturn(Optional.empty())
        whenever(genreService.get(id = any())).thenReturn(Optional.of(genre))
        whenever(showValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(genreValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(showValidator).validate(data = entity, update = false)
        verify(pictureValidator).validateExists(data = Optional.empty())
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.of(genre))
        verifyNoMoreInteractions(pictureService, genreService, showValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(showService, mapper)
    }

    /**
     * Test method for [ShowFacade.add] with show with invalid genre.
     */
    @Test
    fun addInvalidGenre() {
        val entity = ShowUtils.newShow(id = Int.MAX_VALUE)
        val picture = PictureUtils.newPictureDomain(id = 1)

        whenever(pictureService.get(id = any())).thenReturn(Optional.of(picture))
        whenever(genreService.get(id = any())).thenReturn(Optional.empty())
        whenever(showValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(pictureValidator.validateExists(data = any())).thenReturn(Result())
        whenever(genreValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(pictureService).get(id = entity.picture!!)
        entity.genres!!.filterNotNull().forEach { verify(genreService).get(id = it.id!!) }
        verify(showValidator).validate(data = entity, update = false)
        verify(pictureValidator).validateExists(data = Optional.of(picture))
        verify(genreValidator, times(entity.genres!!.filterNotNull().size)).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(pictureService, genreService, showValidator, pictureValidator, genreValidator)
        verifyZeroInteractions(showService, mapper)
    }

    /**
     * Test method for [ShowFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).updatePositions()
        verifyNoMoreInteractions(showService)
        verifyZeroInteractions(pictureService, genreService, mapper, showValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val showList = listOf(ShowUtils.newShowDomainWithSeasons(id = 1), ShowUtils.newShowDomainWithSeasons(id = 2))
        var totalLength = 0
        for (show in showList) {
            for (season in show.seasons) {
                for (episode in season.episodes) {
                    totalLength += episode.length
                }
            }
        }
        val expectedTotalLength = totalLength

        whenever(showService.getAll()).thenReturn(showList)

        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(expectedTotalLength))
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).getAll()
        verifyNoMoreInteractions(showService)
        verifyZeroInteractions(pictureService, genreService, mapper, showValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.getSeasonsCount].
     */
    @Test
    fun getSeasonsCount() {
        val show1 = ShowUtils.newShowDomainWithSeasons(id = 1)
        val show2 = ShowUtils.newShowDomainWithSeasons(id = 2)
        val expectedSeasons = show1.seasons.size + show2.seasons.size

        whenever(showService.getAll()).thenReturn(listOf(show1, show2))

        val result = facade.getSeasonsCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedSeasons)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).getAll()
        verifyNoMoreInteractions(showService)
        verifyZeroInteractions(pictureService, genreService, mapper, showValidator, pictureValidator, genreValidator)
    }

    /**
     * Test method for [ShowFacade.getEpisodesCount].
     */
    @Test
    fun getEpisodesCount() {
        val showList = listOf(ShowUtils.newShowDomainWithSeasons(id = 1), ShowUtils.newShowDomainWithSeasons(id = 2))
        var episodesCount = 0
        for (show in showList) {
            for (season in show.seasons) {
                episodesCount += season.episodes.size
            }
        }
        val expectedEpisodes = episodesCount

        whenever(showService.getAll()).thenReturn(showList)

        val result = facade.getEpisodesCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedEpisodes)
            it.assertThat(result.events()).isEmpty()
        }

        verify(showService).getAll()
        verifyNoMoreInteractions(showService)
        verifyZeroInteractions(pictureService, genreService, mapper, showValidator, pictureValidator, genreValidator)
    }

}
