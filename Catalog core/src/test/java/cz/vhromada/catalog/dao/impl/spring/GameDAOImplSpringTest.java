package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.GameDAO;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.GameDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class GameDAOImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link GameDAO} */
	@Autowired
	private GameDAO gameDAO;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Restarts sequence. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE games_sq RESTART WITH 4").executeUpdate();
	}

	/** Test method for {@link GameDAO#getGames()}. */
	@Test
	public void testGetGames() {
		DeepAsserts.assertEquals(SpringEntitiesUtils.getGames(), gameDAO.getGames());
		DeepAsserts.assertEquals(SpringUtils.GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameDAO#getGame(Integer)}. */
	@Test
	public void testGetGame() {
		for (int i = 1; i <= SpringUtils.GAMES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getGame(i), gameDAO.getGame(i));
		}

		assertNull(gameDAO.getGame(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SpringUtils.GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameDAO#add(Game)}. */
	@Test
	public void testAdd() {
		final Game game = SpringEntitiesUtils.newGame(objectGenerator);

		gameDAO.add(game);

		DeepAsserts.assertNotNull(game.getId());
		DeepAsserts.assertEquals(SpringUtils.GAMES_COUNT + 1, game.getId());
		DeepAsserts.assertEquals(SpringUtils.GAMES_COUNT, game.getPosition());
		final Game addedGame = SpringUtils.getGame(entityManager, SpringUtils.GAMES_COUNT + 1);
		DeepAsserts.assertEquals(game, addedGame);
		DeepAsserts.assertEquals(SpringUtils.GAMES_COUNT + 1, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameDAO#update(Game)}. */
	@Test
	public void testUpdate() {
		final Game game = SpringEntitiesUtils.updateGame(1, objectGenerator, entityManager);

		gameDAO.update(game);

		final Game updatedGame = SpringUtils.getGame(entityManager, 1);
		DeepAsserts.assertEquals(game, updatedGame);
		DeepAsserts.assertEquals(SpringUtils.GAMES_COUNT, SpringUtils.getGamesCount(entityManager));
	}

	/** Test method for {@link GameDAO#remove(Game)}. */
	@Test
	public void testRemove() {
		gameDAO.remove(SpringUtils.getGame(entityManager, 1));

		assertNull(SpringUtils.getGame(entityManager, 1));
		DeepAsserts.assertEquals(SpringUtils.GAMES_COUNT - 1, SpringUtils.getGamesCount(entityManager));
	}

}
