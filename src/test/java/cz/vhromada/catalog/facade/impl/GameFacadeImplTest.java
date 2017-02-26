package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converters.Converter;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GameFacadeImplTest {

    /**
     * Result for invalid game
     */
    private static final Result<Void> INVALID_GAME_RESULT = Result.error("GAME_INVALID", "Game must be valid.");

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<cz.vhromada.catalog.domain.Game> gameService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link CatalogValidator}
     */
    @Mock
    private CatalogValidator<Game> gameValidator;

    /**
     * Instance of {@link GameFacade}
     */
    private GameFacade gameFacade;

    /**
     * Initializes facade for games.
     */
    @Before
    public void setUp() {
        gameFacade = new GameFacadeImpl(gameService, converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for games.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGameService() {
        new GameFacadeImpl(null, converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new GameFacadeImpl(gameService, null, gameValidator);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for game.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGameValidator() {
        new GameFacadeImpl(gameService, converter, null);
    }

    /**
     * Test method for {@link GameFacade#newData()}.
     */
    @Test
    public void newData() {
        final Result<Void> result = gameFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).newData();
        verifyNoMoreInteractions(gameService);
        verifyZeroInteractions(converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final List<cz.vhromada.catalog.domain.Game> gameList = CollectionUtils.newList(GameUtils.newGameDomain(1), GameUtils.newGameDomain(2));
        final List<Game> expectedGames = CollectionUtils.newList(GameUtils.newGame(1), GameUtils.newGame(2));

        when(gameService.getAll()).thenReturn(gameList);
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Game.class), eq(Game.class))).thenReturn(expectedGames);

        final Result<List<Game>> result = gameFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedGames));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).getAll();
        verify(converter).convertCollection(gameList, Game.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameValidator);
    }

    /**
     * Test method for {@link GameFacade#get(Integer)} with existing game.
     */
    @Test
    public void get_ExistingGame() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game expectedGame = GameUtils.newGame(1);

        when(gameService.get(any(Integer.class))).thenReturn(gameEntity);
        when(converter.convert(any(cz.vhromada.catalog.domain.Game.class), eq(Game.class))).thenReturn(expectedGame);

        final Result<Game> result = gameFacade.get(1);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedGame));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).get(1);
        verify(converter).convert(gameEntity, Game.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameValidator);
    }

    /**
     * Test method for {@link GameFacade#get(Integer)} with not existing game.
     */
    @Test
    public void get_NotExistingGame() {
        when(gameService.get(any(Integer.class))).thenReturn(null);
        when(converter.convert(any(cz.vhromada.catalog.domain.Game.class), eq(Game.class))).thenReturn(null);

        final Result<Game> result = gameFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, Game.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameValidator);
    }

    /**
     * Test method for {@link GameFacade#get(Integer)} with null argument.
     */
    @Test
    public void get_NullArgument() {
        final Result<Game> result = gameFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        verifyZeroInteractions(gameService, converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#add(Game)}.
     */
    @Test
    public void add() {
        final Game game = GameUtils.newGame(null);
        final cz.vhromada.catalog.domain.Game gameDomain = GameUtils.newGameDomain(null);

        when(converter.convert(any(Game.class), eq(cz.vhromada.catalog.domain.Game.class))).thenReturn(gameDomain);
        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).add(gameDomain);
        verify(converter).convert(game, cz.vhromada.catalog.domain.Game.class);
        verify(gameValidator).validate(game, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(gameService, converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with invalid game.
     */
    @Test
    public void add_InvalidGame() {
        final Game game = GameUtils.newGame(Integer.MAX_VALUE);

        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(INVALID_GAME_RESULT);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GAME_RESULT));

        verify(gameValidator).validate(game, ValidationType.NEW, ValidationType.DEEP);
        verifyNoMoreInteractions(gameValidator);
        verifyZeroInteractions(gameService, converter);
    }

    /**
     * Test method for {@link GameFacade#update(Game)}.
     */
    @Test
    public void update() {
        final Game game = GameUtils.newGame(1);
        final cz.vhromada.catalog.domain.Game gameDomain = GameUtils.newGameDomain(1);

        when(converter.convert(any(Game.class), eq(cz.vhromada.catalog.domain.Game.class))).thenReturn(gameDomain);
        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).update(gameDomain);
        verify(converter).convert(game, cz.vhromada.catalog.domain.Game.class);
        verify(gameValidator).validate(game, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(gameService, converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with invalid game.
     */
    @Test
    public void update_InvalidGame() {
        final Game game = GameUtils.newGame(Integer.MAX_VALUE);

        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(INVALID_GAME_RESULT);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GAME_RESULT));

        verify(gameValidator).validate(game, ValidationType.EXISTS, ValidationType.DEEP);
        verifyNoMoreInteractions(gameValidator);
        verifyZeroInteractions(gameService, converter);
    }

    /**
     * Test method for {@link GameFacade#remove(Game)}.
     */
    @Test
    public void remove() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game game = GameUtils.newGame(1);

        when(gameService.get(any(Integer.class))).thenReturn(gameEntity);
        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = gameFacade.remove(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).get(1);
        verify(gameService).remove(gameEntity);
        verify(gameValidator).validate(game, ValidationType.EXISTS);
        verifyNoMoreInteractions(gameService, gameValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with invalid game.
     */
    @Test
    public void remove_InvalidGame() {
        final Game game = GameUtils.newGame(Integer.MAX_VALUE);

        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(INVALID_GAME_RESULT);

        final Result<Void> result = gameFacade.remove(game);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GAME_RESULT));

        verify(gameValidator).validate(game, ValidationType.EXISTS);
        verifyNoMoreInteractions(gameValidator);
        verifyZeroInteractions(gameService, converter);
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)}.
     */
    @Test
    public void duplicate() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game game = GameUtils.newGame(1);

        when(gameService.get(any(Integer.class))).thenReturn(gameEntity);
        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = gameFacade.duplicate(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).get(1);
        verify(gameService).duplicate(gameEntity);
        verify(gameValidator).validate(game, ValidationType.EXISTS);
        verifyNoMoreInteractions(gameService, gameValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with invalid game.
     */
    @Test
    public void duplicate_InvalidGame() {
        final Game game = GameUtils.newGame(Integer.MAX_VALUE);

        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(INVALID_GAME_RESULT);

        final Result<Void> result = gameFacade.duplicate(game);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GAME_RESULT));

        verify(gameValidator).validate(game, ValidationType.EXISTS);
        verifyNoMoreInteractions(gameValidator);
        verifyZeroInteractions(gameService, converter);
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)}.
     */
    @Test
    public void moveUp() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game game = GameUtils.newGame(1);

        when(gameService.get(any(Integer.class))).thenReturn(gameEntity);
        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = gameFacade.moveUp(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).get(1);
        verify(gameService).moveUp(gameEntity);
        verify(gameValidator).validate(game, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(gameService, gameValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with invalid game.
     */
    @Test
    public void moveUp_InvalidGame() {
        final Game game = GameUtils.newGame(Integer.MAX_VALUE);

        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(INVALID_GAME_RESULT);

        final Result<Void> result = gameFacade.moveUp(game);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GAME_RESULT));

        verify(gameValidator).validate(game, ValidationType.EXISTS, ValidationType.UP);
        verifyNoMoreInteractions(gameValidator);
        verifyZeroInteractions(gameService, converter);
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)}.
     */
    @Test
    public void moveDown() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game game = GameUtils.newGame(1);

        when(gameService.get(any(Integer.class))).thenReturn(gameEntity);
        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(new Result<>());

        final Result<Void> result = gameFacade.moveDown(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).get(1);
        verify(gameService).moveDown(gameEntity);
        verify(gameValidator).validate(game, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(gameService, gameValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with invalid game.
     */
    @Test
    public void moveDown_InvalidGame() {
        final Game game = GameUtils.newGame(Integer.MAX_VALUE);

        when(gameValidator.validate(any(Game.class), anyVararg())).thenReturn(INVALID_GAME_RESULT);

        final Result<Void> result = gameFacade.moveDown(game);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(INVALID_GAME_RESULT));

        verify(gameValidator).validate(game, ValidationType.EXISTS, ValidationType.DOWN);
        verifyNoMoreInteractions(gameValidator);
        verifyZeroInteractions(gameService, converter);
    }

    /**
     * Test method for {@link GameFacade#updatePositions()}.
     */
    @Test
    public void updatePositions() {
        final Result<Void> result = gameFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).updatePositions();
        verifyNoMoreInteractions(gameService);
        verifyZeroInteractions(converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    public void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.newGameDomain(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.newGameDomain(2);
        final int expectedCount = game1.getMediaCount() + game2.getMediaCount();

        when(gameService.getAll()).thenReturn(CollectionUtils.newList(game1, game2));

        final Result<Integer> result = gameFacade.getTotalMediaCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedCount));
        assertThat(result.getEvents().isEmpty(), is(true));

        verify(gameService).getAll();
        verifyNoMoreInteractions(gameService);
        verifyZeroInteractions(converter, gameValidator);
    }

}
