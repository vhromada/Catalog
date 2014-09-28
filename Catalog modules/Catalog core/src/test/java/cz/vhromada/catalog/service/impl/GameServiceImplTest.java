package cz.vhromada.catalog.service.impl;

import static cz.vhromada.catalog.commons.TestConstants.MOVE_POSITION;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.dao.GameDAO;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.GameService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * A class represents test for class {@link GameServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplTest {

	/** Instance of {@link GameDAO} */
	@Mock
	private GameDAO gameDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache gameCache;

	/** Instance of {@link GameService} */
	@InjectMocks
	private GameService gameService = new GameServiceImpl();

	/** Test method for {@link GameServiceImpl#getGameDAO()} and {@link GameServiceImpl#setGameDAO(GameDAO)}. */
	@Test
	public void testGameDAO() {
		final GameServiceImpl gameService = new GameServiceImpl();
		gameService.setGameDAO(gameDAO);
		DeepAsserts.assertEquals(gameDAO, gameService.getGameDAO());
	}

	/** Test method for {@link GameServiceImpl#getGameCache()} and {@link GameServiceImpl#setGameCache(Cache)}. */
	@Test
	public void testGameCache() {
		final GameServiceImpl gameService = new GameServiceImpl();
		gameService.setGameCache(gameCache);
		DeepAsserts.assertEquals(gameCache, gameService.getGameCache());
	}

	/** Test method for {@link GameService#newData()} with cached games. */
	@Test
	public void testNewDataWithCachedGames() {
		final List<Game> games = CollectionUtils.newList(mock(Game.class), mock(Game.class));
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(games));

		gameService.newData();

		for (Game game : games) {
			verify(gameDAO).remove(game);
		}
		verify(gameCache).get("games");
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#newData()} with not cached games. */
	@Test
	public void testNewDataWithNotCachedGames() {
		final List<Game> games = CollectionUtils.newList(mock(Game.class), mock(Game.class));
		when(gameDAO.getGames()).thenReturn(games);
		when(gameCache.get(anyString())).thenReturn(null);

		gameService.newData();

		verify(gameDAO).getGames();
		for (Game game : games) {
			verify(gameDAO).remove(game);
		}
		verify(gameCache).get("games");
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#newData()} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.newData();
	}

	/** Test method for {@link GameService#newData()} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.newData();
	}

	/** Test method for {@link GameService#newData()} with exception in DAO tier. */
	@Test
	public void testNewDataWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).getGames();
		when(gameCache.get(anyString())).thenReturn(null);

		try {
			gameService.newData();
			fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#getGames()} with cached games. */
	@Test
	public void testGetGamesWithCachedGames() {
		final List<Game> games = CollectionUtils.newList(mock(Game.class), mock(Game.class));
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(games));

		DeepAsserts.assertEquals(games, gameService.getGames());

		verify(gameCache).get("games");
		verifyNoMoreInteractions(gameCache);
		verifyZeroInteractions(gameDAO);
	}

	/** Test method for {@link GameService#getGames()} with not cached games. */
	@Test
	public void testGetGamesWithNotCachedGames() {
		final List<Game> games = CollectionUtils.newList(mock(Game.class), mock(Game.class));
		when(gameDAO.getGames()).thenReturn(games);
		when(gameCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(games, gameService.getGames());

		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verify(gameCache).put("games", games);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#getGames()} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetGamesWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.getGames();
	}

	/** Test method for {@link GameService#getGames()} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetGamesWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.getGames();
	}

	/** Test method for {@link GameService#getGames()} with exception in DAO tier. */
	@Test
	public void testGetGamesWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).getGames();
		when(gameCache.get(anyString())).thenReturn(null);

		try {
			gameService.getGames();
			fail("Can't get games with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#getGame(Integer)} with cached existing game. */
	@Test
	public void testGetGameWithCachedExistingGame() {
		final Game game = EntityGenerator.createGame(PRIMARY_ID);
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(game));

		DeepAsserts.assertEquals(game, gameService.getGame(PRIMARY_ID));

		verify(gameCache).get("game" + PRIMARY_ID);
		verifyNoMoreInteractions(gameCache);
		verifyZeroInteractions(gameDAO);
	}

	/** Test method for {@link GameService#getGame(Integer)} with cached not existing game. */
	@Test
	public void testGetGameWithCachedNotExistingGame() {
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(gameService.getGame(PRIMARY_ID));

		verify(gameCache).get("game" + PRIMARY_ID);
		verifyNoMoreInteractions(gameCache);
		verifyZeroInteractions(gameDAO);
	}

	/** Test method for {@link GameService#getGame(Integer)} with not cached existing game. */
	@Test
	public void testGetGameWithNotCachedExistingGame() {
		final Game game = EntityGenerator.createGame(PRIMARY_ID);
		when(gameDAO.getGame(anyInt())).thenReturn(game);
		when(gameCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(game, gameService.getGame(PRIMARY_ID));

		verify(gameDAO).getGame(PRIMARY_ID);
		verify(gameCache).get("game" + PRIMARY_ID);
		verify(gameCache).put("game" + PRIMARY_ID, game);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#getGame(Integer)} with not cached not existing game. */
	@Test
	public void testGetGameWithNotCachedNotExistingGame() {
		when(gameDAO.getGame(anyInt())).thenReturn(null);
		when(gameCache.get(anyString())).thenReturn(null);

		assertNull(gameService.getGame(PRIMARY_ID));

		verify(gameDAO).getGame(PRIMARY_ID);
		verify(gameCache).get("game" + PRIMARY_ID);
		verify(gameCache).put("game" + PRIMARY_ID, null);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#getGame(Integer)} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetGameWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.getGame(Integer.MAX_VALUE);
	}

	/** Test method for {@link GameService#getGame(Integer)} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetGameWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.getGame(Integer.MAX_VALUE);
	}

	/** Test method for {@link GameService#getGame(Integer)} with null argument. */
	@Test
	public void testGetGameWithNullArgument() {
		try {
			gameService.getGame(null);
			fail("Can't get game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#getGame(Integer)} with exception in DAO tier. */
	@Test
	public void testGetGameWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).getGame(anyInt());
		when(gameCache.get(anyString())).thenReturn(null);

		try {
			gameService.getGame(Integer.MAX_VALUE);
			fail("Can't get game with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).getGame(Integer.MAX_VALUE);
		verify(gameCache).get("game" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#add(Game)} with cached games. */
	@Test
	public void testAddWithCachedGames() {
		final Game game = EntityGenerator.createGame();
		final List<Game> games = CollectionUtils.newList(mock(Game.class), mock(Game.class));
		final List<Game> gamesList = new ArrayList<>(games);
		gamesList.add(game);
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(games));

		gameService.add(game);

		verify(gameDAO).add(game);
		verify(gameCache).get("games");
		verify(gameCache).get("game" + game.getId());
		verify(gameCache).put("games", gamesList);
		verify(gameCache).put("game" + game.getId(), game);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#add(Game)} with not cached games. */
	@Test
	public void testAddWithNotCachedGames() {
		final Game game = EntityGenerator.createGame();
		when(gameCache.get(anyString())).thenReturn(null);

		gameService.add(game);

		verify(gameDAO).add(game);
		verify(gameCache).get("games");
		verify(gameCache).get("game" + game.getId());
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#add(Game)} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.add(mock(Game.class));
	}

	/** Test method for {@link GameService#add(Game)} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.add(mock(Game.class));
	}

	/** Test method for {@link GameService#add(Game)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			gameService.add(null);
			fail("Can't add game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#add(Game)} with exception in DAO tier. */
	@Test
	public void testAddWithDAOTierException() {
		final Game game = EntityGenerator.createGame();
		doThrow(DataStorageException.class).when(gameDAO).add(any(Game.class));

		try {
			gameService.add(game);
			fail("Can't add game with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).add(game);
		verifyNoMoreInteractions(gameDAO);
		verifyZeroInteractions(gameCache);
	}

	/** Test method for {@link GameService#update(Game)}. */
	@Test
	public void testUpdate() {
		final Game game = EntityGenerator.createGame(PRIMARY_ID);

		gameService.update(game);

		verify(gameDAO).update(game);
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#update(Game)} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.update(mock(Game.class));
	}

	/** Test method for {@link GameService#update(Game)} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.update(mock(Game.class));
	}

	/** Test method for {@link GameService#update(Game)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			gameService.update(null);
			fail("Can't update game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#update(Game)} with exception in DAO tier. */
	@Test
	public void testUpdateWithDAOTierException() {
		final Game game = EntityGenerator.createGame(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(gameDAO).update(any(Game.class));

		try {
			gameService.update(game);
			fail("Can't update game with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).update(game);
		verifyNoMoreInteractions(gameDAO);
		verifyZeroInteractions(gameCache);
	}

	/** Test method for {@link GameService#remove(Game)} with cached games. */
	@Test
	public void testRemoveWithCachedGames() {
		final Game game = EntityGenerator.createGame(PRIMARY_ID);
		final List<Game> games = CollectionUtils.newList(mock(Game.class), mock(Game.class));
		final List<Game> gamesList = new ArrayList<>(games);
		gamesList.add(game);
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(gamesList));

		gameService.remove(game);

		verify(gameDAO).remove(game);
		verify(gameCache).get("games");
		verify(gameCache).put("games", games);
		verify(gameCache).evict(PRIMARY_ID);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#remove(Game)} with not cached games. */
	@Test
	public void testRemoveWithNotCachedGames() {
		final Game game = EntityGenerator.createGame(PRIMARY_ID);
		when(gameCache.get(anyString())).thenReturn(null);

		gameService.remove(game);

		verify(gameDAO).remove(game);
		verify(gameCache).get("games");
		verify(gameCache).evict(PRIMARY_ID);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#remove(Game)} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.remove(mock(Game.class));
	}

	/** Test method for {@link GameService#remove(Game)} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.remove(mock(Game.class));
	}

	/** Test method for {@link GameService#remove(Game)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			gameService.remove(null);
			fail("Can't remove game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#remove(Game)} with exception in DAO tier. */
	@Test
	public void testRemoveWithDAOTierException() {
		final Game game = EntityGenerator.createGame(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(gameDAO).remove(any(Game.class));

		try {
			gameService.remove(game);
			fail("Can't remove game with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).remove(game);
		verifyNoMoreInteractions(gameDAO);
		verifyZeroInteractions(gameCache);
	}

	/** Test method for {@link GameService#duplicate(Game)}. */
	@Test
	public void testDuplicate() {
		gameService.duplicate(EntityGenerator.createGame(PRIMARY_ID));

		verify(gameDAO).add(any(Game.class));
		verify(gameDAO).update(any(Game.class));
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#duplicate(Game)} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.duplicate(mock(Game.class));
	}

	/** Test method for {@link GameService#duplicate(Game)} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.duplicate(mock(Game.class));
	}

	/** Test method for {@link GameService#duplicate(Game)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		try {
			gameService.duplicate(null);
			fail("Can't duplicate game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#duplicate(Game)} with exception in DAO tier. */
	@Test
	public void testDuplicateWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).add(any(Game.class));

		try {
			gameService.duplicate(EntityGenerator.createGame(Integer.MAX_VALUE));
			fail("Can't duplicate game with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).add(any(Game.class));
		verifyNoMoreInteractions(gameDAO);
		verifyZeroInteractions(gameCache);
	}

	/** Test method for {@link GameService#moveUp(Game)} with cached games. */
	@Test
	public void testMoveUpWithCachedGames() {
		final Game game1 = EntityGenerator.createGame(PRIMARY_ID);
		game1.setPosition(MOVE_POSITION);
		final Game game2 = EntityGenerator.createGame(SECONDARY_ID);
		final List<Game> games = CollectionUtils.newList(game1, game2);
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(games));

		gameService.moveUp(game2);
		DeepAsserts.assertEquals(POSITION, game1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, game2.getPosition());

		verify(gameDAO).update(game1);
		verify(gameDAO).update(game2);
		verify(gameCache).get("games");
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#moveUp(Game)} with not cached games. */
	@Test
	public void testMoveUpWithNotCachedGames() {
		final Game game1 = EntityGenerator.createGame(PRIMARY_ID);
		game1.setPosition(MOVE_POSITION);
		final Game game2 = EntityGenerator.createGame(SECONDARY_ID);
		final List<Game> games = CollectionUtils.newList(game1, game2);
		when(gameDAO.getGames()).thenReturn(games);
		when(gameCache.get(anyString())).thenReturn(null);

		gameService.moveUp(game2);
		DeepAsserts.assertEquals(POSITION, game1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, game2.getPosition());

		verify(gameDAO).update(game1);
		verify(gameDAO).update(game2);
		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#moveUp(Game)} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.moveUp(mock(Game.class));
	}

	/** Test method for {@link GameService#moveUp(Game)} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.moveUp(mock(Game.class));
	}

	/** Test method for {@link GameService#moveUp(Game)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		try {
			gameService.moveUp(null);
			fail("Can't move up game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#moveUp(Game)} with exception in DAO tier. */
	@Test
	public void testMoveUpWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).getGames();
		when(gameCache.get(anyString())).thenReturn(null);

		try {
			gameService.moveUp(EntityGenerator.createGame(Integer.MAX_VALUE));
			fail("Can't move up game with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#moveDown(Game)} with cached games. */
	@Test
	public void testMoveDownWithCachedGames() {
		final Game game1 = EntityGenerator.createGame(PRIMARY_ID);
		final Game game2 = EntityGenerator.createGame(SECONDARY_ID);
		game2.setPosition(MOVE_POSITION);
		final List<Game> games = CollectionUtils.newList(game1, game2);
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(games));

		gameService.moveDown(game1);
		DeepAsserts.assertEquals(MOVE_POSITION, game1.getPosition());
		DeepAsserts.assertEquals(POSITION, game2.getPosition());

		verify(gameDAO).update(game1);
		verify(gameDAO).update(game2);
		verify(gameCache).get("games");
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#moveDown(Game)} with not cached games. */
	@Test
	public void testMoveDownWithNotCachedGames() {
		final Game game1 = EntityGenerator.createGame(PRIMARY_ID);
		final Game game2 = EntityGenerator.createGame(SECONDARY_ID);
		game2.setPosition(MOVE_POSITION);
		final List<Game> games = CollectionUtils.newList(game1, game2);
		when(gameDAO.getGames()).thenReturn(games);
		when(gameCache.get(anyString())).thenReturn(null);

		gameService.moveDown(game1);
		DeepAsserts.assertEquals(MOVE_POSITION, game1.getPosition());
		DeepAsserts.assertEquals(POSITION, game2.getPosition());

		verify(gameDAO).update(game1);
		verify(gameDAO).update(game2);
		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#moveDown(Game)} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.moveDown(mock(Game.class));
	}

	/** Test method for {@link GameService#moveDown(Game)} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.moveDown(mock(Game.class));
	}

	/** Test method for {@link GameService#moveDown(Game)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		try {
			gameService.moveDown(null);
			fail("Can't move down game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#moveDown(Game)} with exception in DAO tier. */
	@Test
	public void testMoveDownWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).getGames();
		when(gameCache.get(anyString())).thenReturn(null);

		try {
			gameService.moveDown(EntityGenerator.createGame(Integer.MAX_VALUE));
			fail("Can't move down game with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#exists(Game)} with cached existing game. */
	@Test
	public void testExistsWithCachedExistingGame() {
		final Game game = EntityGenerator.createGame(PRIMARY_ID);
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(game));

		assertTrue(gameService.exists(game));

		verify(gameCache).get("game" + PRIMARY_ID);
		verifyNoMoreInteractions(gameCache);
		verifyZeroInteractions(gameDAO);
	}

	/** Test method for {@link GameService#exists(Game)} with cached not existing game. */
	@Test
	public void testExistsWithCachedNotExistingGame() {
		final Game game = EntityGenerator.createGame(Integer.MAX_VALUE);
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(gameService.exists(game));

		verify(gameCache).get("game" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(gameCache);
		verifyZeroInteractions(gameDAO);
	}

	/** Test method for {@link GameService#exists(Game)} with not cached existing game. */
	@Test
	public void testExistsWithNotCachedExistingGame() {
		final Game game = EntityGenerator.createGame(PRIMARY_ID);
		when(gameDAO.getGame(anyInt())).thenReturn(game);
		when(gameCache.get(anyString())).thenReturn(null);

		assertTrue(gameService.exists(game));

		verify(gameDAO).getGame(PRIMARY_ID);
		verify(gameCache).get("game" + PRIMARY_ID);
		verify(gameCache).put("game" + PRIMARY_ID, game);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#exists(Game)} with not cached not existing game. */
	@Test
	public void testExistsWithNotCachedNotExistingGame() {
		when(gameDAO.getGame(anyInt())).thenReturn(null);
		when(gameCache.get(anyString())).thenReturn(null);

		assertFalse(gameService.exists(EntityGenerator.createGame(Integer.MAX_VALUE)));

		verify(gameDAO).getGame(Integer.MAX_VALUE);
		verify(gameCache).get("game" + Integer.MAX_VALUE);
		verify(gameCache).put("game" + Integer.MAX_VALUE, null);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#exists(Game)} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.exists(mock(Game.class));
	}

	/** Test method for {@link GameService#exists(Game)} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.exists(mock(Game.class));
	}

	/** Test method for {@link GameService#exists(Game)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		try {
			gameService.exists(null);
			fail("Can't exists game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#exists(Game)} with exception in DAO tier. */
	@Test
	public void testExistsWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).getGame(anyInt());
		when(gameCache.get(anyString())).thenReturn(null);

		try {
			gameService.exists(EntityGenerator.createGame(Integer.MAX_VALUE));
			fail("Can't exists game with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).getGame(Integer.MAX_VALUE);
		verify(gameCache).get("game" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#updatePositions()} with cached games. */
	@Test
	public void testUpdatePositionsWithCachedGames() {
		final List<Game> games = CollectionUtils.newList(EntityGenerator.createGame(PRIMARY_ID), EntityGenerator.createGame(SECONDARY_ID));
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(games));

		gameService.updatePositions();

		for (int i = 0; i < games.size(); i++) {
			final Game game = games.get(i);
			DeepAsserts.assertEquals(i, game.getPosition());
			verify(gameDAO).update(game);
		}
		verify(gameCache).get("games");
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#updatePositions()} with not cached games. */
	@Test
	public void testUpdatePositionsWithNotCachedGames() {
		final List<Game> games = CollectionUtils.newList(EntityGenerator.createGame(PRIMARY_ID), EntityGenerator.createGame(SECONDARY_ID));
		when(gameDAO.getGames()).thenReturn(games);
		when(gameCache.get(anyString())).thenReturn(null);

		gameService.updatePositions();

		verify(gameDAO).getGames();
		for (int i = 0; i < games.size(); i++) {
			final Game game = games.get(i);
			DeepAsserts.assertEquals(i, game.getPosition());
			verify(gameDAO).update(game);
		}
		verify(gameCache).get("games");
		verify(gameCache).clear();
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#updatePositions()} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.updatePositions();
	}

	/** Test method for {@link GameService#updatePositions()} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.updatePositions();
	}

	/** Test method for {@link GameService#updatePositions()} with exception in DAO tier. */
	@Test
	public void testUpdatePositionsWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).getGames();
		when(gameCache.get(anyString())).thenReturn(null);

		try {
			gameService.updatePositions();
			fail("Can't update positions with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

	/** Test method for {@link GameService#getTotalMediaCount()} with cached games. */
	@Test
	public void testGetTotalMediaCountWithCachedGames() {
		final Game game1 = mock(Game.class);
		final Game game2 = mock(Game.class);
		final Game game3 = mock(Game.class);
		final List<Game> games = CollectionUtils.newList(game1, game2, game3);
		when(gameCache.get(anyString())).thenReturn(new SimpleValueWrapper(games));
		when(game1.getMediaCount()).thenReturn(1);
		when(game2.getMediaCount()).thenReturn(2);
		when(game3.getMediaCount()).thenReturn(3);

		DeepAsserts.assertEquals(6, gameService.getTotalMediaCount());

		verify(gameCache).get("games");
		verify(game1).getMediaCount();
		verify(game2).getMediaCount();
		verify(game3).getMediaCount();
		verifyNoMoreInteractions(gameCache, game1, game2, game3);
		verifyZeroInteractions(gameDAO);
	}

	/** Test method for {@link GameService#getTotalMediaCount()} with not cached games. */
	@Test
	public void testGetTotalMediaCountWithNotCachedGames() {
		final Game game1 = mock(Game.class);
		final Game game2 = mock(Game.class);
		final Game game3 = mock(Game.class);
		final List<Game> games = CollectionUtils.newList(game1, game2, game3);
		when(gameDAO.getGames()).thenReturn(games);
		when(gameCache.get(anyString())).thenReturn(null);
		when(game1.getMediaCount()).thenReturn(1);
		when(game2.getMediaCount()).thenReturn(2);
		when(game3.getMediaCount()).thenReturn(3);

		DeepAsserts.assertEquals(6, gameService.getTotalMediaCount());

		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verify(gameCache).put("games", games);
		verify(game1).getMediaCount();
		verify(game2).getMediaCount();
		verify(game3).getMediaCount();
		verifyNoMoreInteractions(gameDAO, gameCache, game1, game2, game3);
	}

	/** Test method for {@link GameService#getTotalMediaCount()} with not set DAO for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetGameDAO() {
		((GameServiceImpl) gameService).setGameDAO(null);
		gameService.getTotalMediaCount();
	}

	/** Test method for {@link GameService#getTotalMediaCount()} with not set cache for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetGameCache() {
		((GameServiceImpl) gameService).setGameCache(null);
		gameService.getTotalMediaCount();
	}

	/** Test method for {@link GameService#getTotalMediaCount()} with exception in DAO tier. */
	@Test
	public void testGetTotalMediaCountWithDAOTierException() {
		doThrow(DataStorageException.class).when(gameDAO).getGames();
		when(gameCache.get(anyString())).thenReturn(null);

		try {
			gameService.getTotalMediaCount();
			fail("Can't get total media count with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(gameDAO).getGames();
		verify(gameCache).get("games");
		verifyNoMoreInteractions(gameDAO, gameCache);
	}

}
