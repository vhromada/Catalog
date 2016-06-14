package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.GameUtils;
import cz.vhromada.catalog.entities.Game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GameRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testRepositoryContext.xml")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link GameRepository}
     */
    @Autowired
    private GameRepository gameRepository;

    /**
     * Test method for get games.
     */
    @Test
    public void testGetGames() {
        final List<Game> games = gameRepository.findAll();

        GameUtils.assertGamesDeepEquals(GameUtils.getGames(), games);

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for add game.
     */
    @Test
    public void testAdd() {
        final Game game = GameUtils.newGame(null);

        gameRepository.save(game);

        assertNotNull(game.getId());
        assertEquals(GameUtils.GAMES_COUNT + 1, game.getId().intValue());

        final Game addedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1);
        final Game expectedAddGame = GameUtils.newGame(null);
        expectedAddGame.setId(GameUtils.GAMES_COUNT + 1);
        GameUtils.assertGameDeepEquals(expectedAddGame, addedGame);

        assertEquals(GameUtils.GAMES_COUNT + 1, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for update game.
     */
    @Test
    public void testUpdate() {
        final Game game = GameUtils.updateGame(entityManager, 1);

        gameRepository.save(game);

        final Game updatedGame = GameUtils.getGame(entityManager, 1);
        final Game expectedUpdatedGame = GameUtils.getGame(1);
        GameUtils.updateGame(expectedUpdatedGame);
        expectedUpdatedGame.setPosition(GameUtils.POSITION);
        GameUtils.assertGameDeepEquals(expectedUpdatedGame, updatedGame);

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for remove game.
     */
    @Test
    public void testRemove() {
        gameRepository.delete(GameUtils.getGame(entityManager, 1));

        assertNull(GameUtils.getGame(entityManager, 1));

        assertEquals(GameUtils.GAMES_COUNT - 1, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for remove all games.
     */
    @Test
    public void testRemoveAll() {
        gameRepository.deleteAll();

        assertEquals(0, GameUtils.getGamesCount(entityManager));
    }

}
