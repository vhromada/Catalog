package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.facade.impl.CheatFacadeImpl
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
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
 * A class represents test for class [CheatFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class CheatFacadeTest {

    /**
     * Instance of [ChildService] for cheats
     */
    @Mock
    private lateinit var cheatService: ChildService<com.github.vhromada.catalog.domain.Cheat>

    /**
     * Instance of [ParentService] for games
     */
    @Mock
    private lateinit var gameService: ParentService<com.github.vhromada.catalog.domain.Game>

    /**
     * Instance of [Mapper] for cheats
     */
    @Mock
    private lateinit var mapper: Mapper<Cheat, com.github.vhromada.catalog.domain.Cheat>

    /**
     * Instance of [Validator] for cheats
     */
    @Mock
    private lateinit var cheatValidator: Validator<Cheat, com.github.vhromada.catalog.domain.Cheat>

    /**
     * Instance of [Validator] for games
     */
    @Mock
    private lateinit var gameValidator: Validator<Game, com.github.vhromada.catalog.domain.Game>

    /**
     * Instance of [CheatFacade]
     */
    private lateinit var facade: CheatFacade

    /**
     * Initializes facade.
     */
    @BeforeEach
    fun setUp() {
        facade = CheatFacadeImpl(cheatService = cheatService, gameService = gameService, mapper = mapper, cheatValidator = cheatValidator, gameValidator = gameValidator)
    }

    /**
     * Test method for [CheatFacade.get] with existing cheat.
     */
    @Test
    fun getExistingCheat() {
        val entity = CheatUtils.newCheat(id = 1)
        val domain = CheatUtils.newCheatDomain(id = 1)

        whenever(cheatService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.mapBack(source = any<com.github.vhromada.catalog.domain.Cheat>())).thenReturn(entity)
        whenever(cheatValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.get(id = entity.id!!)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entity)
            it.assertThat(result.events()).isEmpty()
        }

        verify(cheatService).get(id = entity.id!!)
        verify(mapper).mapBack(source = domain)
        verify(cheatValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(cheatService, mapper, cheatValidator)
        verifyZeroInteractions(gameService, gameValidator)
    }

    /**
     * Test method for [CheatFacade.get] with not existing cheat.
     */
    @Test
    fun getNotExistingCheat() {
        whenever(cheatService.get(id = any())).thenReturn(Optional.empty())
        whenever(cheatValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(cheatService).get(id = Int.MAX_VALUE)
        verify(cheatValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(cheatService, cheatValidator)
        verifyZeroInteractions(gameService, mapper, gameValidator)
    }

    /**
     * Test method for [CheatFacade.update].
     */
    @Test
    fun update() {
        val entity = CheatUtils.newCheat(id = 1)
        val domain = CheatUtils.newCheatDomain(id = 1)

        whenever(cheatService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(mapper.map(source = any<Cheat>())).thenReturn(domain)
        whenever(cheatValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(cheatValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(cheatService).get(id = entity.id!!)
        verify(cheatService).update(data = domain)
        verify(mapper).map(source = entity)
        verify(cheatValidator).validate(data = entity, update = true)
        verify(cheatValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(cheatService, mapper, cheatValidator)
        verifyZeroInteractions(gameService, gameValidator)
    }

    /**
     * Test method for [CheatFacade.update] with invalid cheat.
     */
    @Test
    fun updateInvalidCheat() {
        val entity = CheatUtils.newCheat(id = Int.MAX_VALUE)

        whenever(cheatValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(cheatValidator).validate(data = entity, update = true)
        verifyNoMoreInteractions(cheatValidator)
        verifyZeroInteractions(cheatService, gameService, mapper, gameValidator)
    }

    /**
     * Test method for [CheatFacade.update] with not existing cheat.
     */
    @Test
    fun updateNotExistingCheat() {
        val entity = CheatUtils.newCheat(id = Int.MAX_VALUE)

        whenever(cheatService.get(id = any())).thenReturn(Optional.empty())
        whenever(cheatValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(cheatValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.update(data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(cheatService).get(id = entity.id!!)
        verify(cheatValidator).validate(data = entity, update = true)
        verify(cheatValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(cheatService, cheatValidator)
        verifyZeroInteractions(gameService, mapper, gameValidator)
    }

    /**
     * Test method for [CheatFacade.remove].
     */
    @Test
    fun remove() {
        val domain = CheatUtils.newCheatDomain(id = 1)

        whenever(cheatService.get(id = any())).thenReturn(Optional.of(domain))
        whenever(cheatValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.remove(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(cheatService).get(id = 1)
        verify(cheatService).remove(data = domain)
        verify(cheatValidator).validateExists(data = Optional.of(domain))
        verifyNoMoreInteractions(cheatService, cheatValidator)
        verifyZeroInteractions(gameService, mapper, gameValidator)
    }

    /**
     * Test method for [CheatFacade.remove] with invalid cheat.
     */
    @Test
    fun removeInvalidCheat() {
        whenever(cheatService.get(id = any())).thenReturn(Optional.empty())
        whenever(cheatValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(cheatService).get(id = Int.MAX_VALUE)
        verify(cheatValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(cheatService, cheatValidator)
        verifyZeroInteractions(gameService, mapper, gameValidator)
    }

    /**
     * Test method for [CheatFacade.duplicate].
     */
    @Test
    fun duplicate() {
        val result = facade.duplicate(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_DUPLICABLE", message = "Cheat can't be duplicated.")))
        }

        verifyZeroInteractions(cheatService, gameService, mapper, cheatValidator, gameValidator)
    }

    /**
     * Test method for [CheatFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_MOVABLE", message = "Cheat can't be moved up.")))
        }

        verifyZeroInteractions(cheatService, gameService, mapper, cheatValidator, gameValidator)
    }

    /**
     * Test method for [CheatFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "CHEAT_NOT_MOVABLE", message = "Cheat can't be moved down.")))
        }

        verifyZeroInteractions(cheatService, gameService, mapper, cheatValidator, gameValidator)
    }

    /**
     * Test method for [CheatFacade.add].
     */
    @Test
    fun add() {
        val entity = CheatUtils.newCheat(id = 1)
        val domain = CheatUtils.newCheatDomain(id = 1)
        val game = GameUtils.newGameDomain(id = 2)

        whenever(gameService.get(id = any())).thenReturn(Optional.of(game))
        whenever(mapper.map(source = any<Cheat>())).thenReturn(domain)
        whenever(cheatValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(gameValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verify(gameService).get(id = 2)
        verify(gameService).update(data = game)
        verify(mapper).map(source = entity)
        verify(cheatValidator).validate(data = entity, update = false)
        verify(gameValidator).validateExists(data = Optional.of(game))
        verifyNoMoreInteractions(gameService, mapper, cheatValidator, gameValidator)
        verifyZeroInteractions(cheatService)
    }

    /**
     * Test method for [CheatFacade.add] with invalid game.
     */
    @Test
    fun addInvalidGame() {
        val entity = CheatUtils.newCheat(id = 1)

        whenever(gameService.get(id = any())).thenReturn(Optional.empty())
        whenever(cheatValidator.validate(data = any(), update = any())).thenReturn(Result())
        whenever(gameValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(gameService).get(id = 2)
        verify(cheatValidator).validate(data = entity, update = false)
        verify(gameValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(gameService, cheatValidator, gameValidator)
        verifyZeroInteractions(cheatService, mapper)
    }

    /**
     * Test method for [CheatFacade.add] with invalid cheat.
     */
    @Test
    fun addInvalidCheat() {
        val entity = CheatUtils.newCheat(id = Int.MAX_VALUE)
        val game = GameUtils.newGameDomain(id = 2)

        whenever(gameService.get(id = any())).thenReturn(Optional.of(game))
        whenever(cheatValidator.validate(data = any(), update = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)
        whenever(gameValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.add(parent = 2, data = entity)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(gameService).get(id = 2)
        verify(cheatValidator).validate(data = entity, update = false)
        verify(gameValidator).validateExists(data = Optional.of(game))
        verifyNoMoreInteractions(gameService, cheatValidator, gameValidator)
        verifyZeroInteractions(cheatService, mapper)
    }

    /**
     * Test method for [CheatFacade.find].
     */
    @Test
    fun find() {
        val entityList = listOf(CheatUtils.newCheat(id = 1), CheatUtils.newCheat(id = 2))
        val domainList = listOf(CheatUtils.newCheatDomain(id = 1), CheatUtils.newCheatDomain(id = 2))
        val game = GameUtils.newGameDomain(id = 2)

        whenever(cheatService.find(parent = any())).thenReturn(domainList)
        whenever(gameService.get(id = any())).thenReturn(Optional.of(game))
        whenever(mapper.mapBack(source = any<List<com.github.vhromada.catalog.domain.Cheat>>())).thenReturn(entityList)
        whenever(gameValidator.validateExists(data = any())).thenReturn(Result())

        val result = facade.find(parent = 2)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(entityList)
            it.assertThat(result.events()).isEmpty()
        }

        verify(cheatService).find(parent = 2)
        verify(gameService).get(id = 2)
        verify(mapper).mapBack(source = domainList)
        verify(gameValidator).validateExists(data = Optional.of(game))
        verifyNoMoreInteractions(cheatService, gameService, mapper, gameValidator)
        verifyZeroInteractions(cheatValidator)
    }

    /**
     * Test method for [CheatFacade.find] with invalid game.
     */
    @Test
    fun findInvalidGame() {
        whenever(gameService.get(id = any())).thenReturn(Optional.empty())
        whenever(gameValidator.validateExists(data = any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = facade.find(parent = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        verify(gameService).get(id = Int.MAX_VALUE)
        verify(gameValidator).validateExists(data = Optional.empty())
        verifyNoMoreInteractions(gameService, gameValidator)
        verifyZeroInteractions(cheatService, mapper, cheatValidator)
    }

}
