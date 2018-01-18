package cz.vhromada.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Game;
import cz.vhromada.catalog.utils.GameUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GameRepository}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
class GameRepositoryIntegrationTest {

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
    void getGames() {
        final List<Game> games = gameRepository.findAll();

        GameUtils.assertGamesDeepEquals(GameUtils.getGames(), games);

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for get game.
     */
    @Test
    void getGame() {
        for (int i = 1; i <= GameUtils.GAMES_COUNT; i++) {
            final Game game = gameRepository.findById(i).orElse(null);

            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), game);
        }

        assertFalse(gameRepository.findById(Integer.MAX_VALUE).isPresent());

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for add game.
     */
    @Test
    void add() {
        final Game game = GameUtils.newGameDomain(null);

        gameRepository.save(game);

        assertEquals(Integer.valueOf(GameUtils.GAMES_COUNT + 1), game.getId());

        final Game addedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1);
        final Game expectedAddGame = GameUtils.newGameDomain(null);
        expectedAddGame.setId(GameUtils.GAMES_COUNT + 1);
        GameUtils.assertGameDeepEquals(expectedAddGame, addedGame);

        assertEquals(GameUtils.GAMES_COUNT + 1, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for update game.
     */
    @Test
    void update() {
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
    void remove() {
        gameRepository.delete(GameUtils.getGame(entityManager, 1));

        assertNull(GameUtils.getGame(entityManager, 1));

        assertEquals(GameUtils.GAMES_COUNT - 1, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for remove all games.
     */
    @Test
    void removeAll() {
        gameRepository.deleteAll();

        assertEquals(0, GameUtils.getGamesCount(entityManager));
    }

}
