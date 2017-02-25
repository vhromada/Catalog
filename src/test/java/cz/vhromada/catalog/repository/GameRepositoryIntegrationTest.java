package cz.vhromada.catalog.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Game;
import cz.vhromada.catalog.utils.GameUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GameRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
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
    public void getGames() {
        final List<Game> games = gameRepository.findAll();

        GameUtils.assertGamesDeepEquals(GameUtils.getGames(), games);

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for get game.
     */
    @Test
    public void getGame() {
        for (int i = 1; i <= GameUtils.GAMES_COUNT; i++) {
            final Game game = gameRepository.findOne(i);

            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), game);
        }

        assertThat(gameRepository.findOne(Integer.MAX_VALUE), is(nullValue()));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for add game.
     */
    @Test
    public void add() {
        final Game game = GameUtils.newGameDomain(null);

        gameRepository.save(game);

        assertThat(game.getId(), is(notNullValue()));
        assertThat(game.getId(), is(GameUtils.GAMES_COUNT + 1));

        final Game addedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1);
        final Game expectedAddGame = GameUtils.newGameDomain(null);
        expectedAddGame.setId(GameUtils.GAMES_COUNT + 1);
        GameUtils.assertGameDeepEquals(expectedAddGame, addedGame);

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT + 1));
    }

    /**
     * Test method for update game.
     */
    @Test
    public void update() {
        final Game game = GameUtils.updateGame(entityManager, 1);

        gameRepository.save(game);

        final Game updatedGame = GameUtils.getGame(entityManager, 1);
        final Game expectedUpdatedGame = GameUtils.getGame(1);
        GameUtils.updateGame(expectedUpdatedGame);
        expectedUpdatedGame.setPosition(GameUtils.POSITION);
        GameUtils.assertGameDeepEquals(expectedUpdatedGame, updatedGame);

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for remove game.
     */
    @Test
    public void remove() {
        gameRepository.delete(GameUtils.getGame(entityManager, 1));

        assertThat(GameUtils.getGame(entityManager, 1), is(nullValue()));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT - 1));
    }

    /**
     * Test method for remove all games.
     */
    @Test
    public void removeAll() {
        gameRepository.deleteAll();

        assertThat(GameUtils.getGamesCount(entityManager), is(0));
    }

}
