package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.GAMES_COUNT;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringToUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.impl.GameFacadeImpl;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A class represents test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class GameFacadeImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link PlatformTransactionManager} */
	@Autowired
	private PlatformTransactionManager transactionManager;

	/** Instance of {@link GameFacade} */
	@Autowired
	private GameFacade gameFacade;

	/** Initializes database. */
	@Before
	public void setUp() {
		SpringUtils.remove(transactionManager, entityManager, Game.class);
		SpringUtils.updateSequence(transactionManager, entityManager, "games_sq");
		for (Game game : SpringEntitiesUtils.getGames()) {
			game.setId(null);
			SpringUtils.persist(transactionManager, entityManager, game);
		}
	}

	/** Test method for {@link GameFacade#newData()}. */
	@Test
	public void testNewData() {
		gameFacade.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#getGames()}. */
	@Test
	public void testGetGames() {
		DeepAsserts.assertEquals(SpringToUtils.getGames(), gameFacade.getGames());
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#getGame(Integer)}. */
	@Test
	public void testGetGame() {
		for (int i = 1; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getGame(i), gameFacade.getGame(i));
		}

		assertNull(gameFacade.getGame(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#getGame(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetGameWithNullArgument() {
		gameFacade.getGame(null);
	}

	/** Test method for {@link GameFacade#add(GameTO)}. */
	@Test
	public void testAdd() {
		final GameTO game = ToGenerator.createGame();
		final Game expectedGame = EntityGenerator.createGame(GAMES_COUNT + 1);
		expectedGame.setPosition(GAMES_COUNT);

		gameFacade.add(game);

		DeepAsserts.assertNotNull(game.getId());
		DeepAsserts.assertEquals(GAMES_COUNT + 1, game.getId());
		final Game addedGame = SpringUtils.getGame(entityManager, GAMES_COUNT + 1);
		DeepAsserts.assertEquals(game, addedGame, "additionalData");
		DeepAsserts.assertEquals(expectedGame, addedGame);
		DeepAsserts.assertEquals(GAMES_COUNT + 1, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#add(GameTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		gameFacade.add(null);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNotNullId() {
		gameFacade.add(ToGenerator.createGame(Integer.MAX_VALUE));
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullName() {
		final GameTO game = ToGenerator.createGame();
		game.setName(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithEmptyName() {
		final GameTO game = ToGenerator.createGame();
		game.setName("");

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null URL to english Wikipedia about game. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullWikiEn() {
		final GameTO game = ToGenerator.createGame();
		game.setWikiEn(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null URL to czech Wikipedia about game. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullWikiCz() {
		final GameTO game = ToGenerator.createGame();
		game.setWikiCz(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNotPositiveMediaCount() {
		final GameTO game = ToGenerator.createGame();
		game.setMediaCount(0);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null other data. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullOtherData() {
		final GameTO game = ToGenerator.createGame();
		game.setOtherData(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullNote() {
		final GameTO game = ToGenerator.createGame();
		game.setNote(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)}. */
	@Test
	public void testUpdate() {
		final GameTO game = ToGenerator.createGame(1);
		final Game expectedGame = EntityGenerator.createGame(1);

		gameFacade.update(game);

		final Game updatedGame = SpringUtils.getGame(entityManager, 1);
		DeepAsserts.assertEquals(game, updatedGame, "additionalData");
		DeepAsserts.assertEquals(expectedGame, updatedGame);
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#update(GameTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		gameFacade.update(null);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullId() {
		gameFacade.update(ToGenerator.createGame());
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullName() {
		final GameTO game = ToGenerator.createGame(PRIMARY_ID);
		game.setName(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithEmptyName() {
		final GameTO game = ToGenerator.createGame(PRIMARY_ID);
		game.setName(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null URL to english Wikipedia about game. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullWikiEn() {
		final GameTO game = ToGenerator.createGame(PRIMARY_ID);
		game.setWikiEn(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null URL to czech Wikipedia about game. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullWikiCz() {
		final GameTO game = ToGenerator.createGame(PRIMARY_ID);
		game.setWikiCz(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNotPositiveMediaCount() {
		final GameTO game = ToGenerator.createGame(PRIMARY_ID);
		game.setMediaCount(0);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null other data. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullOtherData() {
		final GameTO game = ToGenerator.createGame(PRIMARY_ID);
		game.setOtherData(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullNote() {
		final GameTO game = ToGenerator.createGame(PRIMARY_ID);
		game.setNote(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithGameWithBadId() {
		gameFacade.update(ToGenerator.createGame(Integer.MAX_VALUE));
	}

	/** Test method for {@link GameFacade#remove(GameTO)}. */
	@Test
	public void testRemove() {
		gameFacade.remove(ToGenerator.createGame(1));

		assertNull(SpringUtils.getGame(entityManager, 1));
		DeepAsserts.assertEquals(GAMES_COUNT - 1, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		gameFacade.remove(null);
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with game with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithGameWithNullId() {
		gameFacade.remove(ToGenerator.createGame());
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with game with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithGameWithBadId() {
		gameFacade.remove(ToGenerator.createGame(Integer.MAX_VALUE));
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)}. */
	@Test
	public void testDuplicate() {
		final Game game = SpringEntitiesUtils.getGame(GAMES_COUNT);
		game.setId(GAMES_COUNT + 1);

		gameFacade.duplicate(ToGenerator.createGame(GAMES_COUNT));

		final Game duplicatedGame = SpringUtils.getGame(entityManager, GAMES_COUNT + 1);
		DeepAsserts.assertEquals(game, duplicatedGame);
		DeepAsserts.assertEquals(GAMES_COUNT + 1, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		gameFacade.duplicate(null);
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with game with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithGameWithNullId() {
		gameFacade.duplicate(ToGenerator.createGame());
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with game with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithGameWithBadId() {
		gameFacade.duplicate(ToGenerator.createGame(Integer.MAX_VALUE));
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)}. */
	@Test
	public void testMoveUp() {
		final Game game1 = SpringEntitiesUtils.getGame(1);
		game1.setPosition(1);
		final Game game2 = SpringEntitiesUtils.getGame(2);
		game2.setPosition(0);

		gameFacade.moveUp(ToGenerator.createGame(2));
		DeepAsserts.assertEquals(game1, SpringUtils.getGame(entityManager, 1));
		DeepAsserts.assertEquals(game2, SpringUtils.getGame(entityManager, 2));
		for (int i = 3; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), SpringUtils.getGame(entityManager, i));
		}
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		gameFacade.moveUp(null);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with game with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithGameWithNullId() {
		gameFacade.moveUp(ToGenerator.createGame());
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		gameFacade.moveUp(ToGenerator.createGame(1));
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		gameFacade.moveUp(ToGenerator.createGame(Integer.MAX_VALUE));
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)}. */
	@Test
	public void testMoveDown() {
		final GameTO game = ToGenerator.createGame(1);
		final Game game1 = SpringEntitiesUtils.getGame(1);
		game1.setPosition(1);
		final Game game2 = SpringEntitiesUtils.getGame(2);
		game2.setPosition(0);

		gameFacade.moveDown(game);
		DeepAsserts.assertEquals(game1, SpringUtils.getGame(entityManager, 1));
		DeepAsserts.assertEquals(game2, SpringUtils.getGame(entityManager, 2));
		for (int i = 3; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), SpringUtils.getGame(entityManager, i));
		}
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		gameFacade.moveDown(null);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with game with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithGameWithNullId() {
		gameFacade.moveDown(ToGenerator.createGame());
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		gameFacade.moveDown(ToGenerator.createGame(GAMES_COUNT));
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		gameFacade.moveDown(ToGenerator.createGame(Integer.MAX_VALUE));
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with existing game. */
	@Test
	public void testExists() {
		for (int i = 1; i <= GAMES_COUNT; i++) {
			assertTrue(gameFacade.exists(ToGenerator.createGame(i)));
		}

		assertFalse(gameFacade.exists(ToGenerator.createGame(Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		gameFacade.exists(null);
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with game with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithGameWithNullId() {
		gameFacade.exists(ToGenerator.createGame());
	}

	/** Test method for {@link GameFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		gameFacade.updatePositions();

		for (int i = 1; i <= GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), SpringUtils.getGame(entityManager, i));
		}
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameFacade#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		DeepAsserts.assertEquals(6, gameFacade.getTotalMediaCount());
		DeepAsserts.assertEquals(GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

}
