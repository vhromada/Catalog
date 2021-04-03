package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.facade.impl.EpisodeFacadeImpl
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.service.ChildService
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
 * A class represents test for class [EpisodeFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class EpisodeFacadeTest {

    /**
     * Instance of [ChildService] for episodes
     */
    @Mock
    private lateinit var episodeService: ChildService<com.github.vhromada.catalog.domain.Episode>

    /**
     * Instance of [ChildService] for seasons
     */
    @Mock
    private lateinit var seasonService: ChildService<com.github.vhromada.catalog.domain.Season>

    /**
     * Instance of [Mapper] for episodes
     */
    @Mock
    private lateinit var mapper: Mapper<Episode, com.github.vhromada.catalog.domain.Episode>

    /**
     * Instance of [Validator] for episodes
     */
    @Mock
    private lateinit var episodeValidator: Validator<Episode, com.github.vhromada.catalog.domain.Episode>

    /**
     * Instance of [Validator] for seasons
     */
    @Mock
    private lateinit var seasonValidator: Validator<Season, com.github.vhromada.catalog.domain.Season>

    /**
     * Instance of [EpisodeFacade]
     */
    private lateinit var facade: EpisodeFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = EpisodeFacadeImpl(episodeService = episodeService, seasonService = seasonService, mapper = mapper, episodeValidator = episodeValidator, seasonValidator = seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.get] with existing episode.
     */
    @Test
    fun getExistingEpisode() {
        val entity = EpisodeUtils.newEpisode(id = 1)
        val domain = EpisodeUtils.newEpisodeDomain(id = 1)

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Episode>())).thenReturn(entity)
        whenever(episodeValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.get(id = entity.id!!)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entity)
            it.assertThat(result.events()).isEmpty()
        }

        verify(episodeService).get(id = entity.id!!)
        verify(mapper).mapBack(source = domain)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(episodeService, mapper, episodeValidator)
        verifyZeroInteractions(seasonService, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.get] with not existing episode.
     */
    @Test
    fun getNotExistingEpisode() {
        whenever(episodeService.get(id = any())).thenReturn(Optional.empty())
        whenever(episodeValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeService).get(id = Int.MAX_VALUE)
        verify(episodeValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.update].
     */
    @Test
    fun update() {
        val entity = EpisodeUtils.newEpisode(id = 1)
        val domain = EpisodeUtils.newEpisodeDomain(id = 1)

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.map(source = any<Episode>())).thenReturn(domain)
        whenever(episodeValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(episodeValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(episodeService).get(id = entity.id!!)
        verify(episodeService).update(data = domain)
        verify(mapper).map(source = entity)
        verify(episodeValidator).validate(data = entity, update = true)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(episodeService, mapper, episodeValidator)
        verifyZeroInteractions(seasonService, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.update] with invalid episode.
     */
    @Test
    fun updateInvalidEpisode() {
        val entity = EpisodeUtils.newEpisode(id = Int.MAX_VALUE)

        whenever(episodeValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeValidator).validate(data = entity, update = true)
        verifyNoMoreInteractions(episodeValidator)
        verifyZeroInteractions(episodeService, seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.update] with not existing episode.
     */
    @Test
    fun updateNotExistingEpisode() {
        val entity = EpisodeUtils.newEpisode(id = Int.MAX_VALUE)

        whenever(episodeService.get(id = any())).thenReturn(Optional.empty())
        whenever(episodeValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(episodeValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeService).get(id = entity.id!!)
        verify(episodeValidator).validate(data = entity, update = true)
        verify(episodeValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.remove].
     */
    @Test
    fun remove() {
        val domain = EpisodeUtils.newEpisodeDomain(id = 1)

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(episodeValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.remove(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(episodeService).get(id = 1)
        verify(episodeService).remove(data = domain)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.remove] with invalid episode.
     */
    @Test
    fun removeInvalidEpisode() {
        whenever(episodeService.get(id = any())).thenReturn(Optional.empty())
        whenever(episodeValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeService).get(id = Int.MAX_VALUE)
        verify(episodeValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = EpisodeUtils.newEpisodeDomain(id = 1)

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(episodeValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.duplicate(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(episodeService).get(id = 1)
        verify(episodeService).duplicate(data = domain)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.duplicate] with invalid episode.
     */
    @Test
    fun duplicateInvalidEpisode() {
        whenever(episodeService.get(id = any())).thenReturn(Optional.empty())
        whenever(episodeValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeService).get(id = Int.MAX_VALUE)
        verify(episodeValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)
        val episodes = listOf(domain, EpisodeUtils.newEpisodeDomain(id = 2))

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(episodeService.find(parent = any())).thenReturn(episodes)
        whenever(episodeValidator.validateExists(data = any())).thenReturn(Result())
        whenever(episodeValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(episodeService).get(id = 1)
        verify(episodeService).find(parent = domain.season!!.id!!)
        verify(episodeService).moveUp(data = domain)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verify(episodeValidator).validateMovingData(data = domain, list = episodes, up = true)
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.moveUp] not existing episode.
     */
    @Test
    fun moveUpNotExistingEpisode() {
        val domain = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(episodeValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeService).get(id = 1)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.moveUp] with not movable episode.
     */
    @Test
    fun moveUpNotMovableEpisode() {
        val domain = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)
        val episodes = listOf(domain, EpisodeUtils.newEpisodeDomain(id = 2))

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(episodeService.find(parent = any())).thenReturn(episodes)
        whenever(episodeValidator.validateExists(data = any())).thenReturn(Result())
        whenever(episodeValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeService).get(id = 1)
        verify(episodeService).find(parent = domain.season!!.id!!)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verify(episodeValidator).validateMovingData(data = domain, list = episodes, up = true)
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)
        val episodes = listOf(domain, EpisodeUtils.newEpisodeDomain(id = 2))

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(episodeService.find(parent = any())).thenReturn(episodes)
        whenever(episodeValidator.validateExists(data = any())).thenReturn(Result())
        whenever(episodeValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(Result())

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(episodeService).get(id = 1)
        verify(episodeService).find(parent = domain.season!!.id!!)
        verify(episodeService).moveDown(data = domain)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verify(episodeValidator).validateMovingData(data = domain, list = episodes, up = false)
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.moveDown] with not existing episode.
     */
    @Test
    fun moveDownNotExistingEpisode() {
        val domain = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(episodeValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeService).get(id = 1)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.moveDown] with not movable episode.
     */
    @Test
    fun moveDownNotMovableEpisode() {
        val domain = EpisodeUtils.newEpisodeDomainWithSeason(id = 1)
        val episodes = listOf(domain, EpisodeUtils.newEpisodeDomain(id = 2))

        whenever(episodeService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(episodeService.find(parent = any())).thenReturn(episodes)
        whenever(episodeValidator.validateExists(data = any())).thenReturn(Result())
        whenever(episodeValidator.validateMovingData(data = any(), list = any(), up = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(episodeService).get(id = 1)
        verify(episodeService).find(parent = domain.season!!.id!!)
        verify(episodeValidator).validateExists(data = Optional.of(domain))
        verify(episodeValidator).validateMovingData(data = domain, list = episodes, up = false)
        verifyNoMoreInteractions(episodeService, episodeValidator)
        verifyZeroInteractions(seasonService, mapper, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.add].
     */
    @Test
    fun add() {
        val entity = EpisodeUtils.newEpisode(id = 1)
        val domain = EpisodeUtils.newEpisodeDomain(id = 1)
        val season = SeasonUtils.newSeasonDomain(id = 2)

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(season))
        whenever(mapper.map(source = any<Episode>())).thenReturn(domain)
        whenever(episodeValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(episodeService).add(data = domain)
        verify(seasonService).get(id = 2)
        verify(mapper).map(source = entity)
        verify(episodeValidator).validate(data = entity, update = false)
        verify(seasonValidator).validateExists(data = Optional.of(season))
        verifyNoMoreInteractions(episodeService, seasonService, mapper, episodeValidator, seasonValidator)
    }

    /**
     * Test method for [EpisodeFacade.add] with invalid season.
     */
    @Test
    fun addInvalidSeason() {
        val entity = EpisodeUtils.newEpisode(id = 1)

        whenever(seasonService.get(id = any())).thenReturn(Optional.empty())
        whenever(episodeValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(seasonValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = 2)
        verify(episodeValidator).validate(data = entity, update = false)
        verify(seasonValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(seasonService, episodeValidator, seasonValidator)
        verifyZeroInteractions(episodeService, mapper)
    }

    /**
     * Test method for [EpisodeFacade.add] with invalid episode.
     */
    @Test
    fun addInvalidEpisode() {
        val entity = EpisodeUtils.newEpisode(id = Int.MAX_VALUE)
        val season = SeasonUtils.newSeasonDomain(id = 2)

        whenever(seasonService.get(id = any())).thenReturn(Optional.of(season))
        whenever(episodeValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = 2)
        verify(episodeValidator).validate(data = entity, update = false)
        verify(seasonValidator).validateExists(data = Optional.of(season))
        verifyNoMoreInteractions(seasonService, episodeValidator, seasonValidator)
        verifyZeroInteractions(episodeService, mapper)
    }

    /**
     * Test method for [EpisodeFacade.find].
     */
    @Test
    fun find() {
        val entityList = listOf(EpisodeUtils.newEpisode(id = 1), EpisodeUtils.newEpisode(id = 2))
        val domainList = listOf(EpisodeUtils.newEpisodeDomain(id = 1), EpisodeUtils.newEpisodeDomain(id = 2))
        val season = SeasonUtils.newSeasonDomain(id = 2)

        whenever(episodeService.find(parent = any())).thenReturn(domainList)
        whenever(seasonService.get(id = any())).thenReturn(Optional.of(season))
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Episode>>())).thenReturn(entityList)
        whenever(seasonValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.find(parent = 2)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entityList)
            it.assertThat(result.events()).isEmpty()
        }

        verify(episodeService).find(parent = 2)
        verify(seasonService).get(id = 2)
        verify(mapper).mapBack(source = domainList)
        verify(seasonValidator).validateExists(data = Optional.of(season))
        verifyNoMoreInteractions(episodeService, seasonService, mapper, seasonValidator)
        verifyZeroInteractions(episodeValidator)
    }

    /**
     * Test method for [EpisodeFacade.find] with invalid season.
     */
    @Test
    fun findInvalidSeason() {
        whenever(seasonService.get(id = any())).thenReturn(Optional.empty())
        whenever(seasonValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.find(parent = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(seasonService).get(id = Int.MAX_VALUE)
        verify(seasonValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(seasonService, seasonValidator)
        verifyZeroInteractions(episodeService, mapper, episodeValidator)
    }

}
