package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.GameUtils;
import cz.vhromada.catalog.dao.GameDAO;
import cz.vhromada.catalog.dao.entities.Game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GameDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
@Rollback
public class GameDAOImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link GameDAO}
     */
    @Autowired
    private GameDAO gameDAO;

    /**
     * Test method for {@link GameDAO#getGames()}.
     */
    @Test
    public void testGetGames() {
        final List<Game> games = gameDAO.getGames();

        GameUtils.assertGamesDeepEquals(GameUtils.getGames(), games);

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameDAO#getGame(Integer)}.
     */
    @Test
    public void testGetGame() {
        for (int i = 1; i <= GameUtils.GAMES_COUNT; i++) {
            final Game game = gameDAO.getGame(i);

            assertNotNull(game);
            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), game);
        }

        assertNull(gameDAO.getGame(Integer.MAX_VALUE));

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameDAO#add(Game)}.
     */
    @Test
    public void testAdd() {
        final Game game = GameUtils.newGame(null);

        gameDAO.add(game);

        assertNotNull(game.getId());
        assertEquals(GameUtils.GAMES_COUNT + 1, game.getId().intValue());
        assertEquals(GameUtils.GAMES_COUNT, game.getPosition());

        final Game addedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1);
        GameUtils.assertGameDeepEquals(GameUtils.newGame(GameUtils.GAMES_COUNT + 1), addedGame);

        assertEquals(GameUtils.GAMES_COUNT + 1, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameDAO#update(Game)}.
     */
    @Test
    public void testUpdate() {
        final Game game = GameUtils.updateGame(1, entityManager);

        gameDAO.update(game);

        final Game updatedGame = GameUtils.getGame(entityManager, 1);
        final Game expectedUpdatedGame = GameUtils.getGame(1);
        GameUtils.updateGame(expectedUpdatedGame);
        expectedUpdatedGame.setPosition(GameUtils.POSITION);
        GameUtils.assertGameDeepEquals(expectedUpdatedGame, updatedGame);

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameDAO#remove(Game)}.
     */
    @Test
    public void testRemove() {
        gameDAO.remove(GameUtils.getGame(entityManager, 1));

        assertNull(GameUtils.getGame(entityManager, 1));

        assertEquals(GameUtils.GAMES_COUNT - 1, GameUtils.getGamesCount(entityManager));
    }

}
