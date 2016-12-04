package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.utils.GameUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testCatalogContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GameFacadeImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link GameFacade}
     */
    @Autowired
    private GameFacade gameFacade;

    /**
     * Test method for {@link GameFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void testNewData() {
        gameFacade.newData();

        assertEquals(0, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#getGames()}.
     */
    @Test
    public void testGetGames() {
        final List<Game> games = gameFacade.getGames();

        GameUtils.assertGameListDeepEquals(games, GameUtils.getGames());

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#getGame(Integer)}.
     */
    @Test
    public void testGetGame() {
        for (int i = 1; i <= GameUtils.GAMES_COUNT; i++) {
            GameUtils.assertGameDeepEquals(gameFacade.getGame(i), GameUtils.getGame(i));
        }

        assertNull(gameFacade.getGame(Integer.MAX_VALUE));

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
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
    @DirtiesContext
    public void testAdd() {
        gameFacade.add(GameUtils.newGame(null));

        final cz.vhromada.catalog.domain.Game addedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1);
        GameUtils.assertGameDeepEquals(GameUtils.newGameDomain(GameUtils.GAMES_COUNT + 1), addedGame);

        assertEquals(GameUtils.GAMES_COUNT + 1, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        gameFacade.add(null);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotNullId() {
        gameFacade.add(GameUtils.newGame(1));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullName() {
        final Game game = GameUtils.newGame(null);
        game.setName(null);

        gameFacade.add(game);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyName() {
        final Game game = GameUtils.newGame(null);
        game.setName("");

        gameFacade.add(game);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null URL to english Wikipedia about game.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullWikiEn() {
        final Game game = GameUtils.newGame(null);
        game.setWikiEn(null);

        gameFacade.add(game);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null URL to czech Wikipedia about game.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullWikiCz() {
        final Game game = GameUtils.newGame(null);
        game.setWikiCz(null);

        gameFacade.add(game);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotPositiveMediaCount() {
        final Game game = GameUtils.newGame(null);
        game.setMediaCount(0);

        gameFacade.add(game);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null other data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullOtherData() {
        final Game game = GameUtils.newGame(null);
        game.setOtherData(null);

        gameFacade.add(game);
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullNote() {
        final Game game = GameUtils.newGame(null);
        game.setNote(null);

        gameFacade.add(game);
    }

    /**
     * Test method for {@link GameFacade#update(Game)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final Game game = GameUtils.newGame(1);

        gameFacade.update(game);

        final cz.vhromada.catalog.domain.Game updatedGame = GameUtils.getGame(entityManager, 1);
        GameUtils.assertGameDeepEquals(game, updatedGame);

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        gameFacade.update(null);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullId() {
        gameFacade.update(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullName() {
        final Game game = GameUtils.newGame(1);
        game.setName(null);

        gameFacade.update(game);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyName() {
        final Game game = GameUtils.newGame(1);
        game.setName(null);

        gameFacade.update(game);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null URL to english Wikipedia about game.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullWikiEn() {
        final Game game = GameUtils.newGame(1);
        game.setWikiEn(null);

        gameFacade.update(game);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null URL to czech Wikipedia about game.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullWikiCz() {
        final Game game = GameUtils.newGame(1);
        game.setWikiCz(null);

        gameFacade.update(game);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotPositiveMediaCount() {
        final Game game = GameUtils.newGame(1);
        game.setMediaCount(0);

        gameFacade.update(game);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null other data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullOtherData() {
        final Game game = GameUtils.newGame(1);
        game.setOtherData(null);

        gameFacade.update(game);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullNote() {
        final Game game = GameUtils.newGame(1);
        game.setNote(null);

        gameFacade.update(game);
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadId() {
        gameFacade.update(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        gameFacade.remove(GameUtils.newGame(1));

        assertNull(GameUtils.getGame(entityManager, 1));

        assertEquals(GameUtils.GAMES_COUNT - 1, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        gameFacade.remove(null);
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with game with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        gameFacade.remove(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with game with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadId() {
        gameFacade.remove(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Game game = GameUtils.getGame(GameUtils.GAMES_COUNT);
        game.setId(GameUtils.GAMES_COUNT + 1);

        gameFacade.duplicate(GameUtils.newGame(GameUtils.GAMES_COUNT));

        final cz.vhromada.catalog.domain.Game duplicatedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1);
        GameUtils.assertGameDeepEquals(game, duplicatedGame);

        assertEquals(GameUtils.GAMES_COUNT + 1, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        gameFacade.duplicate(null);
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with game with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullId() {
        gameFacade.duplicate(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with game with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadId() {
        gameFacade.duplicate(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.getGame(1);
        game1.setPosition(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.getGame(2);
        game2.setPosition(0);

        gameFacade.moveUp(GameUtils.newGame(2));

        GameUtils.assertGameDeepEquals(game1, GameUtils.getGame(entityManager, 1));
        GameUtils.assertGameDeepEquals(game2, GameUtils.getGame(entityManager, 2));
        for (int i = 3; i <= GameUtils.GAMES_COUNT; i++) {
            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), GameUtils.getGame(entityManager, i));
        }

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        gameFacade.moveUp(null);
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with game with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullId() {
        gameFacade.moveUp(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        gameFacade.moveUp(GameUtils.newGame(1));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadId() {
        gameFacade.moveUp(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.getGame(1);
        game1.setPosition(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.getGame(2);
        game2.setPosition(0);

        gameFacade.moveDown(GameUtils.newGame(1));

        GameUtils.assertGameDeepEquals(game1, GameUtils.getGame(entityManager, 1));
        GameUtils.assertGameDeepEquals(game2, GameUtils.getGame(entityManager, 2));
        for (int i = 3; i <= GameUtils.GAMES_COUNT; i++) {
            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), GameUtils.getGame(entityManager, i));
        }

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        gameFacade.moveDown(null);
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with game with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullId() {
        gameFacade.moveDown(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        gameFacade.moveDown(GameUtils.newGame(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadId() {
        gameFacade.moveDown(GameUtils.newGame(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GameFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void testUpdatePositions() {
        gameFacade.updatePositions();

        for (int i = 1; i <= GameUtils.GAMES_COUNT; i++) {
            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), GameUtils.getGame(entityManager, i));
        }

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        assertEquals(6, gameFacade.getTotalMediaCount());

        assertEquals(GameUtils.GAMES_COUNT, GameUtils.getGamesCount(entityManager));
    }

}
