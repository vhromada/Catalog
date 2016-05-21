package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.GameUtils;
import cz.vhromada.catalog.dao.GameDAO;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link GameDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GameDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for games
     */
    @Mock
    private TypedQuery<Game> gamesQuery;

    /**
     * Instance of {@link GameDAO}
     */
    private GameDAO gameDAO;

    /**
     * Initializes DAO for games.
     */
    @Before
    public void setUp() {
        gameDAO = new GameDAOImpl(entityManager);
    }

    /**
     * Test method for {@link GameDAOImpl#GameDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new GameDAOImpl(null);
    }

    /**
     * Test method for {@link GameDAO#getGames()}.
     */
    @Test
    public void testGetGames() {
        when(entityManager.createNamedQuery(anyString(), eq(Game.class))).thenReturn(gamesQuery);
        when(gamesQuery.getResultList()).thenReturn(CollectionUtils.newList(GameUtils.newGame(GameUtils.ID), GameUtils.newGame(2)));

        final List<Game> games = gameDAO.getGames();

        GameUtils.assertGamesDeepEquals(CollectionUtils.newList(GameUtils.newGame(GameUtils.ID), GameUtils.newGame(2)), games);

        verify(entityManager).createNamedQuery(Game.SELECT_GAMES, Game.class);
        verify(gamesQuery).getResultList();
        verifyNoMoreInteractions(entityManager, gamesQuery);
    }

    /**
     * Test method for {@link GameDAO#getGames()} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetGames_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Game.class));

        gameDAO.getGames();
    }

    /**
     * Test method for {@link GameDAO#getGame(Integer)} with existing game.
     */
    @Test
    public void testGetGame_ExistingGame() {
        when(entityManager.find(eq(Game.class), anyInt())).thenReturn(GameUtils.newGame(GameUtils.ID));

        final Game game = gameDAO.getGame(GameUtils.ID);

        GameUtils.assertGameDeepEquals(GameUtils.newGame(GameUtils.ID), game);

        verify(entityManager).find(Game.class, GameUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GameDAO#getGame(Integer)} with not existing game.
     */
    @Test
    public void testGetGame_NotExistingGame() {
        when(entityManager.find(eq(Game.class), anyInt())).thenReturn(null);

        final Game game = gameDAO.getGame(Integer.MAX_VALUE);

        assertNull(game);

        verify(entityManager).find(Game.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GameDAO#getGame(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetGame_NullArgument() {
        gameDAO.getGame(null);
    }

    /**
     * Test method for {@link GameDAO#getGame(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetGame_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Game.class), anyInt());

        gameDAO.getGame(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link GameDAO#add(Game)}.
     */
    @Test
    public void testAdd() {
        final Game game = GameUtils.newGame(GameUtils.ID);
        doAnswer(setId(GameUtils.ID)).when(entityManager).persist(any(Game.class));

        gameDAO.add(game);

        assertEquals(GameUtils.ID, game.getId());
        assertEquals(GameUtils.ID - 1, game.getPosition());

        verify(entityManager).persist(game);
        verify(entityManager).merge(game);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GameDAO#add(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        gameDAO.add(null);
    }

    /**
     * Test method for {@link GameDAO#add(Game)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Game.class));

        gameDAO.add(GameUtils.newGame(GameUtils.ID));
    }

    /**
     * Test method for {@link GameDAO#update(Game)}.
     */
    @Test
    public void testUpdate() {
        final Game game = GameUtils.newGame(GameUtils.ID);

        gameDAO.update(game);

        verify(entityManager).merge(game);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GameDAO#update(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        gameDAO.update(null);
    }

    /**
     * Test method for {@link GameDAO#update(Game)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Game.class));

        gameDAO.update(GameUtils.newGame(GameUtils.ID));
    }

    /**
     * Test method for {@link GameDAO#remove(Game)} with managed game.
     */
    @Test
    public void testRemove_ManagedGame() {
        final Game game = GameUtils.newGame(GameUtils.ID);
        when(entityManager.contains(any(Game.class))).thenReturn(true);

        gameDAO.remove(game);

        verify(entityManager).contains(game);
        verify(entityManager).remove(game);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GameDAO#remove(Game)} with not managed game.
     */
    @Test
    public void testRemove_NotManagedGame() {
        final Game game = GameUtils.newGame(GameUtils.ID);
        when(entityManager.contains(any(Game.class))).thenReturn(false);
        when(entityManager.getReference(eq(Game.class), anyInt())).thenReturn(game);

        gameDAO.remove(game);

        verify(entityManager).contains(game);
        verify(entityManager).getReference(Game.class, game.getId());
        verify(entityManager).remove(game);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GameDAO#remove(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        gameDAO.remove(null);
    }

    /**
     * Test method for {@link GameDAO#remove(Game)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Game.class));

        gameDAO.remove(GameUtils.newGame(GameUtils.ID));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Game) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
