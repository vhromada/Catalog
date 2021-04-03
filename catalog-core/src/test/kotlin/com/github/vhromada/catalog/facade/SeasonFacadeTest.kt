package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.facade.impl.SeasonFacadeImpl
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.service.ChildService
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
 * A class represents test for class [SeasonFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class SeasonFacadeTest {

    /**
     * Instance of [ChildService] for seasons
     */
    @Mock
    private lateinit var seasonService: ChildService<com.github.vhromada.catalog.domain.Season>

    /**
     * Instance of [ParentService] for shows
     */
    @Mock
    private lateinit var showService: ParentService<com.github.vhromada.catalog.domain.Show>

    /**
     * Instance of [Mapper] for seasons
     */
    @Mock
    private lateinit var mapper: Mapper<Season, com.github.vhromada.catalog.domain.Season>

    /**
     * Instance of [Validator] for seasons
     */
    @Mock
    private lateinit var seasonValidator: Validator<Season, com.github.vhromada.catalog.domain.Season>

    /**
     * Instance of [Validator] for shows
     */
    @Mock
    private lateinit var showValidator: Validator<Show, com.github.vhromada.catalog.domain.Show>

    /**
     * Instance of [SeasonFacade]
     */
    private lateinit var facade: SeasonFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = SeasonFacadeImpl(seasonService = seasonService, showService = showService, mapper = mapper, seasonValidator = seasonValidator, showValidator = showValidator)
    }

    /**
     * Test method for [SeasonFacade.get] with existing season.
     */
    @Test
    fun getExistingSeason() {
        val entity = SeasonUtils.newSeason(id = 1)
        val domain = SeasonUtils.newSeasonDomain(id = 1)

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Season>())).thenReturn(entity)
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.get(id = entity.id!!)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entity)
            it.assertThat(result.events()).isEmpty()
        }

        verify(seasonService).get(id = entity.id!!)
        verify(mapper).mapBack(source = domain)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(seasonService, mapper, seasonValidator)
        verifyZeroInteractions(showService, showValidator)
    }

    /**
     * Test method for [SeasonFacade.get] with not existing season.
     */
    @Test
    fun getNotExistingSeason() {
        whenever(seasonService.get(id = any())).thenReturn(Optional.empty())
        whenever(seasonValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = Int.MAX_VALUE)
        verify(seasonValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.update].
     */
    @Test
    fun update() {
        val entity = SeasonUtils.newSeason(id = 1)
        val domain = SeasonUtils.newSeasonDomain(id = 1)

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.map(source = any<Season>())).thenReturn(domain)
        whenever(seasonValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(seasonService).get(id = entity.id!!)
        verify(seasonService).update(data = domain)
        verify(mapper).map(source = entity)
        verify(seasonValidator).validate(data = entity, update = true)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(seasonService, mapper, seasonValidator)
        verifyZeroInteractions(showService, showValidator)
    }

    /**
     * Test method for [SeasonFacade.update] with invalid season.
     */
    @Test
    fun updateInvalidSeason() {
        val entity = SeasonUtils.newSeason(id = Int.MAX_VALUE)

        whenever(seasonValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonValidator).validate(data = entity, update = true)
        verifyNoMoreInteractions(seasonValidator)
        verifyZeroInteractions(seasonService, showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.update] with not existing season.
     */
    @Test
    fun updateNotExistingSeason() {
        val entity = SeasonUtils.newSeason(id = Int.MAX_VALUE)

        whenever(seasonService.get(id = any())).thenReturn(Optional.empty())
        whenever(seasonValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(seasonValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = entity.id!!)
        verify(seasonValidator).validate(data = entity, update = true)
        verify(seasonValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.remove].
     */
    @Test
    fun remove() {
        val domain = SeasonUtils.newSeasonDomain(id = 1)

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.remove(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(seasonService).get(id = 1)
        verify(seasonService).remove(data = domain)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.remove] with invalid season.
     */
    @Test
    fun removeInvalidSeason() {
        whenever(seasonService.get(id = any())).thenReturn(Optional.empty())
        whenever(seasonValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = Int.MAX_VALUE)
        verify(seasonValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = SeasonUtils.newSeasonDomain(id = 1)

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.duplicate(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(seasonService).get(id = 1)
        verify(seasonService).duplicate(data = domain)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.duplicate] with invalid season.
     */
    @Test
    fun duplicateInvalidSeason() {
        whenever(seasonService.get(id = any())).thenReturn(Optional.empty())
        whenever(seasonValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = Int.MAX_VALUE)
        verify(seasonValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = SeasonUtils.newSeasonDomainWithShow(id = 1)
        val seasons = listOf(domain, SeasonUtils.newSeasonDomain(id = 2))

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(seasonService.find(parent = any())).thenReturn(seasons)
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())
        whenever(seasonValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(seasonService).get(id = 1)
        verify(seasonService).find(parent = domain.show!!.id!!)
        verify(seasonService).moveUp(data = domain)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verify(seasonValidator).validateMovingData(data = domain, list = seasons, up = true)
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.moveUp] not existing season.
     */
    @Test
    fun moveUpNotExistingSeason() {
        val domain = SeasonUtils.newSeasonDomainWithShow(id = 1)

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(seasonValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = 1)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.moveUp] with not movable season.
     */
    @Test
    fun moveUpNotMovableSeason() {
        val domain = SeasonUtils.newSeasonDomainWithShow(id = 1)
        val seasons = listOf(domain, SeasonUtils.newSeasonDomain(id = 2))

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(seasonService.find(parent = any())).thenReturn(seasons)
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())
        whenever(seasonValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = 1)
        verify(seasonService).find(parent = domain.show!!.id!!)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verify(seasonValidator).validateMovingData(data = domain, list = seasons, up = true)
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = SeasonUtils.newSeasonDomainWithShow(id = 1)
        val seasons = listOf(domain, SeasonUtils.newSeasonDomain(id = 2))

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(seasonService.find(parent = any())).thenReturn(seasons)
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())
        whenever(seasonValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(seasonService).get(id = 1)
        verify(seasonService).find(parent = domain.show!!.id!!)
        verify(seasonService).moveDown(data = domain)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verify(seasonValidator).validateMovingData(data = domain, list = seasons, up = false)
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.moveDown] with not existing season.
     */
    @Test
    fun moveDownNotExistingSeason() {
        val domain = SeasonUtils.newSeasonDomainWithShow(id = 1)

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(seasonValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = 1)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.moveDown] with not movable season.
     */
    @Test
    fun moveDownNotMovableSeason() {
        val domain = SeasonUtils.newSeasonDomainWithShow(id = 1)
        val seasons = listOf(domain, SeasonUtils.newSeasonDomain(id = 2))

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(seasonService.find(parent = any())).thenReturn(seasons)
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())
        whenever(seasonValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = 1)
        verify(seasonService).find(parent = domain.show!!.id!!)
        verify(seasonValidator).validateExists(data = Optional.of(domain))
        verify(seasonValidator).validateMovingData(data = domain, list = seasons, up = false)
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(showService, mapper, showValidator)
    }

    /**
     * Test method for [SeasonFacade.add].
     */
    @Test
    fun add() {
        val entity = SeasonUtils.newSeason(id = 1)
        val domain = SeasonUtils.newSeasonDomain(id = 1)
        val show = ShowUtils.newShowDomain(id = 2)

        whenever(showService.get(id = any())).thenReturn(Optional.of(show))
        whenever(mapper.map(source = any<Season>())).thenReturn(domain)
        whenever(seasonValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(seasonService).add(data = domain)
        verify(showService).get(id = 2)
        verify(mapper).map(source = entity)
        verify(seasonValidator).validate(data = entity, update = false)
        verify(showValidator).validateExists(data = Optional.of(show))
        verifyNoMoreInteractions(seasonService, showService, mapper, seasonValidator, showValidator)
    }

    /**
     * Test method for [SeasonFacade.add] with invalid show.
     */
    @Test
    fun addInvalidShow() {
        val entity = SeasonUtils.newSeason(id = 1)

        whenever(showService.get(id = any())).thenReturn(Optional.empty())
        whenever(seasonValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(showValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = 2)
        verify(seasonValidator).validate(data = entity, update = false)
        verify(showValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(showService, seasonValidator, showValidator)
        verifyZeroInteractions(seasonService, mapper)
    }

    /**
     * Test method for [SeasonFacade.add] with invalid season.
     */
    @Test
    fun addInvalidSeason() {
        val entity = SeasonUtils.newSeason(id = Int.MAX_VALUE)
        val show = ShowUtils.newShowDomain(id = 2)

        whenever(showService.get(id = any())).thenReturn(Optional.of(show))
        whenever(seasonValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = 2)
        verify(seasonValidator).validate(data = entity, update = false)
        verify(showValidator).validateExists(data = Optional.of(show))
        verifyNoMoreInteractions(showService, seasonValidator, showValidator)
        verifyZeroInteractions(seasonService, mapper)
    }

    /**
     * Test method for [SeasonFacade.find].
     */
    @Test
    fun find() {
        val entityList = listOf(SeasonUtils.newSeason(id = 1), SeasonUtils.newSeason(id = 2))
        val domainList = listOf(SeasonUtils.newSeasonDomain(id = 1), SeasonUtils.newSeasonDomain(id = 2))
        val show = ShowUtils.newShowDomain(id = 2)

        whenever(seasonService.find(parent = any())).thenReturn(domainList)
        whenever(showService.get(id = any())).thenReturn(Optional.of(show))
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Season>>())).thenReturn(entityList)
        whenever(showValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.find(parent = 2)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entityList)
            it.assertThat(result.events()).isEmpty()
        }

        verify(seasonService).find(parent = 2)
        verify(showService).get(id = 2)
        verify(mapper).mapBack(source = domainList)
        verify(showValidator).validateExists(data = Optional.of(show))
        verifyNoMoreInteractions(seasonService, showService, mapper, showValidator)
        verifyZeroInteractions(seasonValidator)
    }

    /**
     * Test method for [SeasonFacade.find] with invalid show.
     */
    @Test
    fun findInvalidShow() {
        whenever(showService.get(id = any())).thenReturn(Optional.empty())
        whenever(showValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.find(parent = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(showService).get(id = Int.MAX_VALUE)
        verify(showValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(showService, showValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

}
