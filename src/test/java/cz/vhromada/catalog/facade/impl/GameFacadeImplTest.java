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

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.GameValidator;
import cz.vhromada.converters.Converter;

import org.junit.Before;
import org.junit.Ignore;
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
@Ignore
public class GameFacadeImplTest {

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
     * Instance of {@link GameValidator}
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
    public void testConstructor_NullGameService() {
        new GameFacadeImpl(null, converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new GameFacadeImpl(gameService, null, gameValidator);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for game.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGameEpisodeValidator() {
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
        verifyZeroInteractions(converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#getGames()}.
     */
    @Test
    public void testGetGames() {
        final List<cz.vhromada.catalog.domain.Game> gameList = CollectionUtils.newList(GameUtils.newGameDomain(1), GameUtils.newGameDomain(2));
        final List<Game> expectedGames = CollectionUtils.newList(GameUtils.newGame(1), GameUtils.newGame(2));

        when(gameService.getAll()).thenReturn(gameList);
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Game.class), eq(Game.class))).thenReturn(expectedGames);

        final List<Game> games = gameFacade.getGames();

        assertNotNull(games);
        assertEquals(expectedGames, games);

        verify(gameService).getAll();
        verify(converter).convertCollection(gameList, Game.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameValidator);
    }

    /**
     * Test method for {@link GameFacade#getGame(Integer)} with existing game.
     */
    @Test
    public void testGetGame_ExistingGame() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game expectedGame = GameUtils.newGame(1);

        when(gameService.get(anyInt())).thenReturn(gameEntity);
        when(converter.convert(any(cz.vhromada.catalog.domain.Game.class), eq(Game.class))).thenReturn(expectedGame);

        final Game game = gameFacade.getGame(1);

        assertNotNull(game);
        assertEquals(expectedGame, game);

        verify(gameService).get(1);
        verify(converter).convert(gameEntity, Game.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameValidator);
    }

    /**
     * Test method for {@link GameFacade#getGame(Integer)} with not existing game.
     */
    @Test
    public void testGetGame_NotExistingGame() {
        when(gameService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(cz.vhromada.catalog.domain.Game.class), eq(Game.class))).thenReturn(null);

        assertNull(gameFacade.getGame(Integer.MAX_VALUE));

        verify(gameService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, Game.class);
        verifyNoMoreInteractions(gameService, converter);
        verifyZeroInteractions(gameValidator);
    }

    /**
     * Test method for {@link GameFacade#getGame(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetGame_NullArgument() {
        gameFacade.getGame(null);
    }

    /**
     * Test method for {@link GameFacade#add(Game)}.
     */
    @Test
    public void testAdd() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(null);
        final Game game = GameUtils.newGame(null);

        when(converter.convert(any(Game.class), eq(cz.vhromada.catalog.domain.Game.class))).thenReturn(gameEntity);

        gameFacade.add(game);

        verify(gameService).add(gameEntity);
        verify(converter).convert(game, cz.vhromada.catalog.domain.Game.class);
        verify(gameValidator).validate(game);
        verifyNoMoreInteractions(gameService, converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.add(null);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.add(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#update(Game)}.
     */
    @Test
    public void testUpdate() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game game = GameUtils.newGame(1);

        when(gameService.get(anyInt())).thenReturn(gameEntity);
        when(converter.convert(any(Game.class), eq(cz.vhromada.catalog.domain.Game.class))).thenReturn(gameEntity);

        gameFacade.update(game);

        verify(gameService).get(1);
        verify(gameService).update(gameEntity);
        verify(converter).convert(game, cz.vhromada.catalog.domain.Game.class);
        verify(gameValidator).validate(game);
        verifyNoMoreInteractions(gameService, converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.update(null);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.update(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.update(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)}.
     */
    @Test
    public void testRemove() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game game = GameUtils.newGame(1);

        when(gameService.get(anyInt())).thenReturn(gameEntity);

        gameFacade.remove(game);

        verify(gameService).get(1);
        verify(gameService).remove(gameEntity);
        verify(gameValidator).validate(game);
        verifyNoMoreInteractions(gameService, gameValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.remove(null);
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.remove(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.remove(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)}.
     */
    @Test
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final Game game = GameUtils.newGame(1);

        when(gameService.get(anyInt())).thenReturn(gameEntity);

        gameFacade.duplicate(game);

        verify(gameService).get(1);
        verify(gameService).duplicate(gameEntity);
        verify(gameValidator).validate(game);
        verifyNoMoreInteractions(gameService, gameValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.duplicate(null);
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.duplicate(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.duplicate(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)}.
     */
    @Test
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(2);
        final List<cz.vhromada.catalog.domain.Game> games = CollectionUtils.newList(GameUtils.newGameDomain(1), gameEntity);
        final Game game = GameUtils.newGame(2);

        when(gameService.get(anyInt())).thenReturn(gameEntity);
        when(gameService.getAll()).thenReturn(games);

        gameFacade.moveUp(game);

        verify(gameService).get(2);
        verify(gameService).getAll();
        verify(gameService).moveUp(gameEntity);
        verify(gameValidator).validate(game);
        verifyNoMoreInteractions(gameService, gameValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.moveUp(null);
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.moveUp(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.moveUp(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Game> games = CollectionUtils.newList(gameEntity, GameUtils.newGameDomain(1));
        final Game game = GameUtils.newGame(Integer.MAX_VALUE);

        when(gameService.get(anyInt())).thenReturn(gameEntity);
        when(gameService.getAll()).thenReturn(games);

        gameFacade.moveUp(game);
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)}.
     */
    @Test
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(1);
        final List<cz.vhromada.catalog.domain.Game> games = CollectionUtils.newList(gameEntity, GameUtils.newGameDomain(2));
        final Game game = GameUtils.newGame(1);

        when(gameService.get(anyInt())).thenReturn(gameEntity);
        when(gameService.getAll()).thenReturn(games);

        gameFacade.moveDown(game);

        verify(gameService).get(1);
        verify(gameService).getAll();
        verify(gameService).moveDown(gameEntity);
        verify(gameValidator).validate(game);
        verifyNoMoreInteractions(gameService, gameValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.moveDown(null);
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with argument with bad data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadArgument() {
        doThrow(IllegalArgumentException.class).when(gameValidator).validate(any(Game.class));

        gameFacade.moveDown(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with not existing argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotExistingArgument() {
        when(gameService.get(anyInt())).thenReturn(null);

        gameFacade.moveDown(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Game gameEntity = GameUtils.newGameDomain(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Game> games = CollectionUtils.newList(GameUtils.newGameDomain(1), gameEntity);
        final Game game = GameUtils.newGame(Integer.MAX_VALUE);

        when(gameService.get(anyInt())).thenReturn(gameEntity);
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
        verifyZeroInteractions(converter, gameValidator);
    }

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.newGameDomain(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.newGameDomain(2);
        final int expectedCount = game1.getMediaCount() + game2.getMediaCount();

        when(gameService.getAll()).thenReturn(CollectionUtils.newList(game1, game2));

        assertEquals(expectedCount, gameFacade.getTotalMediaCount());

        verify(gameService).getAll();
        verifyNoMoreInteractions(gameService);
        verifyZeroInteractions(converter, gameValidator);
    }

}
