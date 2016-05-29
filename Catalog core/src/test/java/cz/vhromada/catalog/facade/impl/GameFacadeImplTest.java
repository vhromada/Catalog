package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.GameUtils;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.facade.validators.GameTOValidator;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

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
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<Game> gameService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link GameTOValidator}
     */
    @Mock
    private GameTOValidator gameTOValidator;

    /**
     * Instance of {@link GameFacade}
     */
    private GameFacade gameFacade;

    /**
     * Initializes facade for games.
     */
    @Before
    public void setUp() {
        gameFacade = new GameFacadeImpl(gameService, converter, gameTOValidator);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, GameTOValidator)} with null service for games.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGameService() {
        new GameFacadeImpl(null, converter, gameTOValidator);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, GameTOValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new GameFacadeImpl(gameService, null, gameTOValidator);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, GameTOValidator)} with null validator for TO for game.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGameTOValidator() {
        new GameFacadeImpl(gameService, converter, null);
    }

    /**
     * Test method for {@link GameFacade#newData()}.
     */
    @Test
    public void testNewData() {
        gameFacade.newData();

        verify(gameService).newData();
        verifyNoMoreInteractions(gameService);
    }

    /**
     * Test method for {@link GameFacade#getGames()}.
     */
    @Test
    public void testGetGames() {
        final List<Game> gameList = CollectionUtils.newList(GameUtils.newGame(1), GameUtils.newGame(2));
        final List<GameTO> expectedGames = CollectionUtils.newList(GameUtils.newGameTO(1), GameUtils.newGameTO(2));
        when(gameService.getAll()).thenReturn(gameList);
        when(converter.convertCollection(anyListOf(Game.class), eq(GameTO.class))).thenReturn(expectedGames);

        final List<GameTO> games = gameFacade.getGames();

        assertNotNull(games);
        assertEquals(expectedGames, games);

        verify(gameService).getAll();
        verify(converter).convertCollection(gameList, GameTO.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameTOValidator);
    }

    /**
     * Test method for {@link GameFacade#getGame(Integer)} with existing game.
     */
    @Test
    public void testGetGame_ExistingGame() {
        final Game entityGame = GameUtils.newGame(1);
        final GameTO expectedGame = GameUtils.newGameTO(1);
        when(gameService.get(anyInt())).thenReturn(entityGame);
        when(converter.convert(any(Game.class), eq(GameTO.class))).thenReturn(expectedGame);

        final GameTO game = gameFacade.getGame(1);

        assertNotNull(game);
        assertEquals(expectedGame, game);

        verify(gameService).get(1);
        verify(converter).convert(entityGame, GameTO.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameTOValidator);
    }

    /**
     * Test method for {@link GameFacade#getGame(Integer)} with not existing game.
     */
    @Test
    public void testGetGame_NotExistingGame() {
        when(gameService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(Game.class), eq(GameTO.class))).thenReturn(null);

        assertNull(gameFacade.getGame(Integer.MAX_VALUE));

        verify(gameService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, GameTO.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameTOValidator);
    }

    /**
     * Test method for {@link GameFacade#getGame(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetGame_NullArgument() {
        gameFacade.getGame(null);
    }

    /**
     * Test method for {@link GameFacade#add(GameTO)}.
     */
    @Test
    public void testAdd() {
        final Game entityGame = GameUtils.newGame(null);
        final GameTO game = GameUtils.newGameTO(null);
        when(converter.convert(any(GameTO.class), eq(Game.class))).thenReturn(entityGame);

        gameFacade.add(game);

        verify(gameService).add(entityGame);
        verify(converter).convert(game, Game.class);
        verify(gameTOValidator).validateNewGameTO(game);
        verifyNoMoreInteractions(gameService, converter, gameTOValidator);
    }

    /**
     * Test method for {@link GameFacade#add(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameTOValidator).validateNewGameTO(any(GameTO.class));

        gameFacade.add(null);
    }

    /**
     * Test method for {@link GameFacade#add(GameTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadArgument() {
        doThrow(ValidationException.class).when(gameTOValidator).validateNewGameTO(any(GameTO.class));

        gameFacade.add(GameUtils.newGameTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#update(GameTO)}.
     */
    @Test
    public void testUpdate() {
        final Game entityGame = GameUtils.newGame(1);
        final GameTO game = GameUtils.newGameTO(1);
        when(gameService.get(anyInt())).thenReturn(entityGame);
        when(converter.convert(any(GameTO.class), eq(Game.class))).thenReturn(entityGame);

        gameFacade.update(game);

        verify(gameService).get(1);
        verify(gameService).update(entityGame);
        verify(converter).convert(game, Game.class);
        verify(gameTOValidator).validateExistingGameTO(game);
        verifyNoMoreInteractions(gameService, converter, gameTOValidator);
    }

    /**
     * Test method for {@link GameFacade#update(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameTOValidator).validateExistingGameTO(any(GameTO.class));

        gameFacade.update(null);
    }

    /**
     * Test method for {@link GameFacade#update(GameTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(gameTOValidator).validateExistingGameTO(any(GameTO.class));

        gameFacade.update(GameUtils.newGameTO(null));
    }

    /**
     * Test method for {@link GameFacade#update(GameTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.update(GameUtils.newGameTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#remove(GameTO)}.
     */
    @Test
    public void testRemove() {
        final Game entityGame = GameUtils.newGame(1);
        final GameTO game = GameUtils.newGameTO(1);
        when(gameService.get(anyInt())).thenReturn(entityGame);

        gameFacade.remove(game);

        verify(gameService).get(1);
        verify(gameService).remove(entityGame);
        verify(gameTOValidator).validateGameTOWithId(game);
        verifyNoMoreInteractions(gameService, gameTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#remove(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

        gameFacade.remove(null);
    }

    /**
     * Test method for {@link GameFacade#remove(GameTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

        gameFacade.remove(GameUtils.newGameTO(null));
    }

    /**
     * Test method for {@link GameFacade#remove(GameTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.remove(GameUtils.newGameTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#duplicate(GameTO)}.
     */
    @Test
    public void testDuplicate() {
        final Game entityGame = GameUtils.newGame(1);
        final GameTO game = GameUtils.newGameTO(1);
        when(gameService.get(anyInt())).thenReturn(entityGame);

        gameFacade.duplicate(game);

        verify(gameService).get(1);
        verify(gameService).duplicate(entityGame);
        verify(gameTOValidator).validateGameTOWithId(game);
        verifyNoMoreInteractions(gameService, gameTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#duplicate(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

        gameFacade.duplicate(null);
    }

    /**
     * Test method for {@link GameFacade#duplicate(GameTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

        gameFacade.duplicate(GameUtils.newGameTO(null));
    }

    /**
     * Test method for {@link GameFacade#duplicate(GameTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.duplicate(GameUtils.newGameTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#moveUp(GameTO)}.
     */
    @Test
    public void testMoveUp() {
        final Game entityGame = GameUtils.newGame(2);
        final List<Game> games = CollectionUtils.newList(GameUtils.newGame(1), entityGame);
        final GameTO game = GameUtils.newGameTO(2);
        when(gameService.get(anyInt())).thenReturn(entityGame);
        when(gameService.getAll()).thenReturn(games);

        gameFacade.moveUp(game);

        verify(gameService).get(2);
        verify(gameService).getAll();
        verify(gameService).moveUp(entityGame);
        verify(gameTOValidator).validateGameTOWithId(game);
        verifyNoMoreInteractions(gameService, gameTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#moveUp(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

        gameFacade.moveUp(null);
    }

    /**
     * Test method for {@link GameFacade#moveUp(GameTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

        gameFacade.moveUp(GameUtils.newGameTO(null));
    }

    /**
     * Test method for {@link GameFacade#moveUp(GameTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.moveUp(GameUtils.newGameTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#moveUp(GameTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        final Game entityGame = GameUtils.newGame(Integer.MAX_VALUE);
        final List<Game> games = CollectionUtils.newList(entityGame, GameUtils.newGame(1));
        final GameTO game = GameUtils.newGameTO(Integer.MAX_VALUE);
        when(gameService.get(anyInt())).thenReturn(entityGame);
        when(gameService.getAll()).thenReturn(games);

        gameFacade.moveUp(game);
    }

    /**
     * Test method for {@link GameFacade#moveDown(GameTO)}.
     */
    @Test
    public void testMoveDown() {
        final Game entityGame = GameUtils.newGame(1);
        final List<Game> games = CollectionUtils.newList(entityGame, GameUtils.newGame(2));
        final GameTO game = GameUtils.newGameTO(1);
        when(gameService.get(anyInt())).thenReturn(entityGame);
        when(gameService.getAll()).thenReturn(games);

        gameFacade.moveDown(game);

        verify(gameService).get(1);
        verify(gameService).getAll();
        verify(gameService).moveDown(entityGame);
        verify(gameTOValidator).validateGameTOWithId(game);
        verifyNoMoreInteractions(gameService, gameTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#moveDown(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

        gameFacade.moveDown(null);
    }

    /**
     * Test method for {@link GameFacade#moveDown(GameTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

        gameFacade.moveDown(GameUtils.newGameTO(null));
    }

    /**
     * Test method for {@link GameFacade#moveDown(GameTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.moveDown(GameUtils.newGameTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#moveDown(GameTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        final Game entityGame = GameUtils.newGame(Integer.MAX_VALUE);
        final List<Game> games = CollectionUtils.newList(GameUtils.newGame(1), entityGame);
        final GameTO game = GameUtils.newGameTO(Integer.MAX_VALUE);
        when(gameService.get(anyInt())).thenReturn(entityGame);
        when(gameService.getAll()).thenReturn(games);

        gameFacade.moveDown(game);
    }

    /**
     * Test method for {@link GameFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        gameFacade.updatePositions();

        verify(gameService).updatePositions();
        verifyNoMoreInteractions(gameService);
        verifyZeroInteractions(converter, gameTOValidator);
    }

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        final Game game1 = GameUtils.newGame(1);
        final Game game2 = GameUtils.newGame(2);
        final int expectedCount = game1.getMediaCount() + game2.getMediaCount();
        when(gameService.getAll()).thenReturn(CollectionUtils.newList(game1, game2));

        assertEquals(expectedCount, gameFacade.getTotalMediaCount());

        verify(gameService).getAll();
        verifyNoMoreInteractions(gameService);
        verifyZeroInteractions(converter, gameTOValidator);
    }

}
