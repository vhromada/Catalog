package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.GAMES_COUNT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.impl.GameFacadeImpl;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.generator.ObjectGenerator;
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

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

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
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);

		gameFacade.add(game);

		DeepAsserts.assertNotNull(game.getId());
		DeepAsserts.assertEquals(GAMES_COUNT + 1, game.getId());
		final Game addedGame = SpringUtils.getGame(entityManager, GAMES_COUNT + 1);
		DeepAsserts.assertEquals(game, addedGame, "additionalData");
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
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(Integer.MAX_VALUE);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullName() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);
		game.setName(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithEmptyName() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);
		game.setName("");

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null URL to english Wikipedia about game. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullWikiEn() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);
		game.setWikiEn(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null URL to czech Wikipedia about game. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullWikiCz() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);
		game.setWikiCz(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNotPositiveMediaCount() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);
		game.setMediaCount(0);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null other data. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullOtherData() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);
		game.setOtherData(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with game with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithGameWithNullNote() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);
		game.setNote(null);

		gameFacade.add(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)}. */
	@Test
	public void testUpdate() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(1);

		gameFacade.update(game);

		final Game updatedGame = SpringUtils.getGame(entityManager, 1);
		DeepAsserts.assertEquals(game, updatedGame, "additionalData");
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
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullName() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setName(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithEmptyName() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setName(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null URL to english Wikipedia about game. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullWikiEn() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setWikiEn(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null URL to czech Wikipedia about game. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullWikiCz() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setWikiCz(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNotPositiveMediaCount() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setMediaCount(0);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null other data. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullOtherData() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setOtherData(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGameWithNullNote() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setNote(null);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with game with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithGameWithBadId() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(Integer.MAX_VALUE);

		gameFacade.update(game);
	}

	/** Test method for {@link GameFacade#remove(GameTO)}. */
	@Test
	public void testRemove() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(1);

		gameFacade.remove(game);

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
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);

		gameFacade.remove(game);
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with game with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithGameWithBadId() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(Integer.MAX_VALUE);

		gameFacade.remove(game);
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)}. */
	@Test
	public void testDuplicate() {
		final GameTO gameTO = objectGenerator.generate(GameTO.class);
		gameTO.setId(GAMES_COUNT);
		final Game game = SpringEntitiesUtils.getGame(GAMES_COUNT);
		game.setId(GAMES_COUNT + 1);

		gameFacade.duplicate(gameTO);

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
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);

		gameFacade.duplicate(game);
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with game with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithGameWithBadId() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(Integer.MAX_VALUE);

		gameFacade.duplicate(game);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)}. */
	@Test
	public void testMoveUp() {
		final GameTO gameTO = objectGenerator.generate(GameTO.class);
		gameTO.setId(2);
		final Game game1 = SpringEntitiesUtils.getGame(1);
		game1.setPosition(1);
		final Game game2 = SpringEntitiesUtils.getGame(2);
		game2.setPosition(0);

		gameFacade.moveUp(gameTO);
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
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);

		gameFacade.moveUp(game);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(1);

		gameFacade.moveUp(game);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(Integer.MAX_VALUE);

		gameFacade.moveUp(game);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)}. */
	@Test
	public void testMoveDown() {
		final GameTO gameTO = objectGenerator.generate(GameTO.class);
		gameTO.setId(1);
		final Game game1 = SpringEntitiesUtils.getGame(1);
		game1.setPosition(1);
		final Game game2 = SpringEntitiesUtils.getGame(2);
		game2.setPosition(0);

		gameFacade.moveDown(gameTO);
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
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);

		gameFacade.moveDown(game);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(GAMES_COUNT);

		gameFacade.moveDown(game);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(Integer.MAX_VALUE);

		gameFacade.moveDown(game);
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with existing game. */
	@Test
	public void testExists() {
		for (int i = 1; i <= GAMES_COUNT; i++) {
			final GameTO game = objectGenerator.generate(GameTO.class);
			game.setId(i);
			assertTrue(gameFacade.exists(game));
		}

		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(Integer.MAX_VALUE);
		assertFalse(gameFacade.exists(game));

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
		final GameTO game = objectGenerator.generate(GameTO.class);
		game.setId(null);

		gameFacade.exists(game);
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
