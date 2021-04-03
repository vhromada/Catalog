package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.impl.GameFacadeImpl
import com.github.vhromada.catalog.utils.GameUtils
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
 * A class represents test for class [GameFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class GameFacadeTest {

    /**
     * Instance of [ParentService] for games
     */
    @Mock
    private lateinit var service: ParentService<com.github.vhromada.catalog.domain.Game>

    /**
     * Instance of [Mapper] for games
     */
    @Mock
    private lateinit var mapper: Mapper<Game, com.github.vhromada.catalog.domain.Game>

    /**
     * Instance of [Validator] for games
     */
    @Mock
    private lateinit var validator: Validator<Game, com.github.vhromada.catalog.domain.Game>

    /**
     * Instance of [GameFacade]
     */
    private lateinit var facade: GameFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = GameFacadeImpl(gameService = service, mapper = mapper, gameValidator = validator)
    }

    /**
     * Test method for [GameFacade.get] with existing game.
     */
    @Test
    fun getExistingGame() {
        val entity = GameUtils.newGame(id = 1)
        val domain = GameUtils.newGameDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Game>())).thenReturn(entity)
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
     * Test method for [GameFacade.get] with not existing game.
     */
    @Test
    fun getNotExistingGame() {
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
     * Test method for [GameFacade.update].
     */
    @Test
    fun update() {
        val entity = GameUtils.newGame(id = 1)
        val domain = GameUtils.newGameDomain(id = 1)

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.map(source = any<Game>())).thenReturn(domain)
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
     * Test method for [GameFacade.update] with invalid game.
     */
    @Test
    fun updateInvalidGame() {
        val entity = GameUtils.newGame(id = Int.MAX_VALUE)

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
     * Test method for [GameFacade.update] with not existing game.
     */
    @Test
    fun updateNotExistingGame() {
        val entity = GameUtils.newGame(id = Int.MAX_VALUE)

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
     * Test method for [GameFacade.remove].
     */
    @Test
    fun remove() {
        val domain = GameUtils.newGameDomain(id = 1)

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
     * Test method for [GameFacade.remove] with invalid game.
     */
    @Test
    fun removeInvalidGame() {
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
     * Test method for [GameFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val domain = GameUtils.newGameDomain(id = 1)

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
     * Test method for [GameFacade.duplicate] with invalid game.
     */
    @Test
    fun duplicateInvalidGame() {
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
     * Test method for [GameFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val domain = GameUtils.newGameDomain(id = 1)
        val games = listOf(domain, GameUtils.newGameDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(games)
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
        verify(validator).validateMovingData(data = domain, list = games, up = true)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [GameFacade.moveUp] not existing game.
     */
    @Test
    fun moveUpNotExistingGame() {
        val domain = GameUtils.newGameDomain(id = 1)

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
     * Test method for [GameFacade.moveUp] with not movable game.
     */
    @Test
    fun moveUpNotMovableGame() {
        val domain = GameUtils.newGameDomain(id = 1)
        val games = listOf(domain, GameUtils.newGameDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(games)
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
        verify(validator).validateMovingData(data = domain, list = games, up = true)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [GameFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val domain = GameUtils.newGameDomain(id = 1)
        val games = listOf(domain, GameUtils.newGameDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(games)
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
        verify(validator).validateMovingData(data = domain, list = games, up = false)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [GameFacade.moveDown] with not existing game.
     */
    @Test
    fun moveDownNotExistingGame() {
        val domain = GameUtils.newGameDomain(id = 1)

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
     * Test method for [GameFacade.moveDown] with not movable game.
     */
    @Test
    fun moveDownNotMovableGame() {
        val domain = GameUtils.newGameDomain(id = 1)
        val games = listOf(domain, GameUtils.newGameDomain(id = 2))

        whenever(service.get(id = any())).thenReturn(Optional.of(domain))
        whenever(service.getAll()).thenReturn(games)
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
        verify(validator).validateMovingData(data = domain, list = games, up = false)
        verifyNoMoreInteractions(service, validator)
        verifyZeroInteractions(mapper)
    }

    /**
     * Test method for [GameFacade.newData].
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
     * Test method for [GameFacade.getAll].
     */
    @Test
    fun getAll() {
        val entityList = listOf(GameUtils.newGame(id = 1), GameUtils.newGame(id = 2))
        val domainList = listOf(GameUtils.newGameDomain(id = 1), GameUtils.newGameDomain(id = 2))

        whenever(service.getAll()).thenReturn(domainList)
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Game>>())).thenReturn(entityList)

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
     * Test method for [GameFacade.add].
     */
    @Test
    fun add() {
        val entity = GameUtils.newGame(id = 1)
        val domain = GameUtils.newGameDomain(id = 1)

        whenever(mapper.map(source = any<Game>())).thenReturn(domain)
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
     * Test method for [GameFacade.add] with invalid game.
     */
    @Test
    fun addInvalidGame() {
        val entity = GameUtils.newGame(id = Int.MAX_VALUE)

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
     * Test method for [GameFacade.updatePositions].
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

    /**
     * Test method for [GameFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val game1 = GameUtils.newGameDomain(id = 1)
        val game2 = GameUtils.newGameDomain(id = 2)
        val expectedCount = game1.mediaCount + game2.mediaCount

        whenever(service.getAll()).thenReturn(listOf(game1, game2))

        val result = facade.getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(expectedCount)
            it.assertThat(result.events()).isEmpty()
        }

        verify(service).getAll()
        verifyNoMoreInteractions(service)
        verifyZeroInteractions(mapper, validator)
    }

}
