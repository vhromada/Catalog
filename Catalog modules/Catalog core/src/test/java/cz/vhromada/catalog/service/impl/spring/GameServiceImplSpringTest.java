package cz.vhromada.catalog.service.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.GAMES_COUNT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.service.GameService;
import cz.vhromada.catalog.service.impl.GameServiceImpl;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link GameServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class GameServiceImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('gameCache')}")
	private Cache gameCache;

	/** Instance of {@link GameService} */
	@Autowired
	private GameService gameService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Clears cache and restarts sequence. */
	@Before
	public void setUp() {
		gameCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE games_sq RESTART WITH 4").executeUpdate();
	}

	/** Test method for {@link GameService#newData()}. */
	@Test
	public void testNewData() {
		gameService.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getGamesCount(entityManager));
		assertTrue(SpringUtils.getCacheKeys(gameCache).isEmpty());
	}

	/** Test method for {@link GameService#getGames()}. */
	@Test
	public void testGetGames() {
		final List<Game> games = SpringEntitiesUtils.getGames();
		final String key = "games";

		DeepAsserts.assertEquals(games, gameService.getGames());
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(gameCache));
		SpringUtils.assertCacheValue(gameCache, key, games);
	}

	/** Test method for {@link GameService#getGame(Integer)} with existing game. */
	@Test
	public void testGetGameWithExistingGame() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= GAMES_COUNT; i++) {
			keys.add("game" + i);
		}

		for (int i = 1; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), gameService.getGame(i));
		}
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(gameCache));
		for (int i = 1; i <= GAMES_COUNT; i++) {
			SpringUtils.assertCacheValue(gameCache, keys.get(i - 1), SpringEntitiesUtils.getGame(i));
		}
	}

	/** Test method for {@link GameService#getGame(Integer)} with not existing game. */
	@Test
	public void testGetGameWithNotExistingGame() {
		final String key = "game" + Integer.MAX_VALUE;

		assertNull(gameService.getGame(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(gameCache));
		SpringUtils.assertCacheValue(gameCache, key, null);
	}

	/** Test method for {@link GameService#add(Game)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final Game game = objectGenerator.generate(Game.class);
		game.setId(null);

		gameService.add(game);

		DeepAsserts.assertNotNull(game.getId());
		DeepAsserts.assertEquals(GAMES_COUNT + 1, game.getId());
		final Game addedGame = SpringUtils.getGame(entityManager, GAMES_COUNT + 1);
		DeepAsserts.assertEquals(game, addedGame);
		DeepAsserts.assertEquals(GAMES_COUNT + 1, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(gameCache).size());
	}

	/** Test method for {@link GameService#add(Game)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final Game game = objectGenerator.generate(Game.class);
		game.setId(null);
		final String keyList = "games";
		final String keyItem = "game" + (GAMES_COUNT + 1);
		gameCache.put(keyList, new ArrayList<>());
		gameCache.put(keyItem, null);

		gameService.add(game);

		DeepAsserts.assertNotNull(game.getId());
		DeepAsserts.assertEquals(GAMES_COUNT + 1, game.getId());
		final Game addedGame = SpringUtils.getGame(entityManager, GAMES_COUNT + 1);
		DeepAsserts.assertEquals(game, addedGame);
		DeepAsserts.assertEquals(GAMES_COUNT + 1, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(gameCache));
		SpringUtils.assertCacheValue(gameCache, keyList, CollectionUtils.newList(game));
		SpringUtils.assertCacheValue(gameCache, keyItem, game);
	}

	/** Test method for {@link GameService#update(Game)}. */
	@Test
	public void testUpdate() {
		final Game game = SpringEntitiesUtils.updateGame(SpringUtils.getGame(entityManager, 1), objectGenerator);

		gameService.update(game);

		final Game updatedGame = SpringUtils.getGame(entityManager, 1);
		DeepAsserts.assertEquals(game, updatedGame);
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(gameCache).size());
	}

	/** Test method for {@link GameService#remove(Game)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final Game game = objectGenerator.generate(Game.class);
		game.setId(null);
		entityManager.persist(game);
		DeepAsserts.assertEquals(GAMES_COUNT + 1, SpringUtils.getGamesCount(entityManager));

		gameService.remove(game);

		assertNull(SpringUtils.getGame(entityManager, game.getId()));
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(gameCache).size());
	}

	/** Test method for {@link GameService#remove(Game)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final Game game = objectGenerator.generate(Game.class);
		game.setId(null);
		entityManager.persist(game);
		DeepAsserts.assertEquals(GAMES_COUNT + 1, SpringUtils.getGamesCount(entityManager));
		final String key = "games";
		final List<Game> cacheGames = new ArrayList<>();
		cacheGames.add(game);
		gameCache.put(key, cacheGames);

		gameService.remove(game);

		assertNull(SpringUtils.getGame(entityManager, game.getId()));
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(gameCache));
		SpringUtils.assertCacheValue(gameCache, key, new ArrayList<>());
	}

	/** Test method for {@link GameService#duplicate(Game)}. */
	@Test
	public void testDuplicate() {
		final Game game = SpringUtils.getGame(entityManager, 3);
		final Game expectedGame = SpringEntitiesUtils.getGame(3);
		expectedGame.setId(GAMES_COUNT + 1);

		gameService.duplicate(game);

		DeepAsserts.assertEquals(expectedGame, SpringUtils.getGame(entityManager, GAMES_COUNT + 1));
		for (int i = 1; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), SpringUtils.getGame(entityManager, i));
		}
		DeepAsserts.assertEquals(GAMES_COUNT + 1, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(gameCache).size());
	}

	/** Test method for {@link GameService#moveUp(Game)}. */
	@Test
	public void testMoveUp() {
		final Game game = SpringUtils.getGame(entityManager, 2);
		final Game expectedGame1 = SpringEntitiesUtils.getGame(1);
		expectedGame1.setPosition(1);
		final Game expectedGame2 = SpringEntitiesUtils.getGame(2);
		expectedGame2.setPosition(0);

		gameService.moveUp(game);

		DeepAsserts.assertEquals(expectedGame1, SpringUtils.getGame(entityManager, 1));
		DeepAsserts.assertEquals(expectedGame2, SpringUtils.getGame(entityManager, 2));
		for (int i = 3; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), SpringUtils.getGame(entityManager, i));
		}
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(gameCache).size());
	}

	/** Test method for {@link GameService#moveDown(Game)}. */
	@Test
	public void testMoveDown() {
		final Game game = SpringUtils.getGame(entityManager, 1);
		final Game expectedGame1 = SpringEntitiesUtils.getGame(1);
		expectedGame1.setPosition(1);
		final Game expectedGame2 = SpringEntitiesUtils.getGame(2);
		expectedGame2.setPosition(0);

		gameService.moveDown(game);

		DeepAsserts.assertEquals(expectedGame1, SpringUtils.getGame(entityManager, 1));
		DeepAsserts.assertEquals(expectedGame2, SpringUtils.getGame(entityManager, 2));
		for (int i = 3; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), SpringUtils.getGame(entityManager, i));
		}
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(gameCache).size());
	}


	/** Test method for {@link GameService#exists(Game)} with existing game. */
	@Test
	public void testExistsWithExistingGame() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= GAMES_COUNT; i++) {
			keys.add("game" + i);
		}

		for (int i = 1; i <= GAMES_COUNT; i++) {
			assertTrue(gameService.exists(SpringEntitiesUtils.getGame(i)));
		}
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(gameCache));
		for (int i = 1; i <= GAMES_COUNT; i++) {
			SpringUtils.assertCacheValue(gameCache, keys.get(i - 1), SpringEntitiesUtils.getGame(i));
		}
	}

	/** Test method for {@link GameService#exists(Game)} with not existing game. */
	@Test
	public void testExistsWithNotExistingGame() {
		final Game game = objectGenerator.generate(Game.class);
		game.setId(Integer.MAX_VALUE);
		final String key = "game" + Integer.MAX_VALUE;

		assertFalse(gameService.exists(game));
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(gameCache));
		SpringUtils.assertCacheValue(gameCache, key, null);
	}

	/** Test method for {@link GameService#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		final Game game = SpringUtils.getGame(entityManager, GAMES_COUNT);
		game.setPosition(5000);
		entityManager.merge(game);

		gameService.updatePositions();

		for (int i = 1; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), SpringUtils.getGame(entityManager, i));
		}
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(gameCache).size());
	}

	/** Test method for {@link GameService#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		final String key = "games";

		DeepAsserts.assertEquals(6, gameService.getTotalMediaCount());
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(gameCache));
		SpringUtils.assertCacheValue(gameCache, key, SpringEntitiesUtils.getGames());
	}

}
