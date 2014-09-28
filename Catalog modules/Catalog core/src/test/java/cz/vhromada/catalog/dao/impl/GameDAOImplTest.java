package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.GameDAO;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link GameDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GameDAOImplTest extends ObjectGeneratorTest {

	/** Instance of {@link EntityManager} */
	@Mock
	private EntityManager entityManager;

	/** Query for games */
	@Mock
	private TypedQuery<Game> gamesQuery;

	/** Instance of {@link GameDAO} */
	@InjectMocks
	private GameDAO gameDAO = new GameDAOImpl();

	/** Test method for {@link GameDAOImpl#getEntityManager()} and {@link GameDAOImpl#setEntityManager(EntityManager)}. */
	@Test
	public void testEntityManager() {
		final GameDAOImpl gameDAOImpl = new GameDAOImpl();
		gameDAOImpl.setEntityManager(entityManager);
		DeepAsserts.assertEquals(entityManager, gameDAOImpl.getEntityManager());
	}

	/** Test method for {@link GameDAO#getGames()}. */
	@Test
	public void testGetGames() {
		final List<Game> games = CollectionUtils.newList(generate(Game.class), generate(Game.class));
		when(entityManager.createNamedQuery(anyString(), eq(Game.class))).thenReturn(gamesQuery);
		when(gamesQuery.getResultList()).thenReturn(games);

		DeepAsserts.assertEquals(games, gameDAO.getGames());

		verify(entityManager).createNamedQuery(Game.SELECT_GAMES, Game.class);
		verify(gamesQuery).getResultList();
		verifyNoMoreInteractions(entityManager, gamesQuery);
	}

	/** Test method for {@link GameDAOImpl#getGames()} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetGamesWithNotSetEntityManager() {
		((GameDAOImpl) gameDAO).setEntityManager(null);
		gameDAO.getGames();
	}

	/** Test method for {@link GameDAOImpl#getGames()} with exception in persistence. */
	@Test
	public void testGetGamesWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Game.class));

		try {
			gameDAO.getGames();
			fail("Can't get games with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).createNamedQuery(Game.SELECT_GAMES, Game.class);
		verifyNoMoreInteractions(entityManager);
		verifyZeroInteractions(gamesQuery);
	}

	/** Test method for {@link GameDAO#getGame(Integer)} with existing game. */
	@Test
	public void testGetGameWithExistingGame() {
		final int id = generate(Integer.class);
		final Game game = mock(Game.class);
		when(entityManager.find(eq(Game.class), anyInt())).thenReturn(game);

		DeepAsserts.assertEquals(game, gameDAO.getGame(id));

		verify(entityManager).find(Game.class, id);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAO#getGame(Integer)} with not existing game. */
	@Test
	public void testGetGameWithNotExistingGame() {
		when(entityManager.find(eq(Game.class), anyInt())).thenReturn(null);

		assertNull(gameDAO.getGame(Integer.MAX_VALUE));

		verify(entityManager).find(Game.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAOImpl#getGame(Integer)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetGameWithNotSetEntityManager() {
		((GameDAOImpl) gameDAO).setEntityManager(null);
		gameDAO.getGame(Integer.MAX_VALUE);
	}

	/** Test method for {@link GameDAO#getGame(Integer)} with null argument. */
	@Test
	public void testGetGameWithNullArgument() {
		try {
			gameDAO.getGame(null);
			fail("Can't get game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link GameDAOImpl#getGame(Integer)} with exception in persistence. */
	@Test
	public void testGetGameWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).find(eq(Game.class), anyInt());

		try {
			gameDAO.getGame(Integer.MAX_VALUE);
			fail("Can't get game with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).find(Game.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAO#add(Game)}. */
	@Test
	public void testAdd() {
		final Game game = generate(Game.class);
		final int id = generate(Integer.class);
		doAnswer(setId(id)).when(entityManager).persist(any(Game.class));

		gameDAO.add(game);
		DeepAsserts.assertEquals(id, game.getId());
		DeepAsserts.assertEquals(id - 1, game.getPosition());

		verify(entityManager).persist(game);
		verify(entityManager).merge(game);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAOImpl#add(Game)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEntityManager() {
		((GameDAOImpl) gameDAO).setEntityManager(null);
		gameDAO.add(mock(Game.class));
	}

	/** Test method for {@link GameDAO#add(Game)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			gameDAO.add(null);
			fail("Can't add game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link GameDAOImpl#add(Game)} with exception in persistence. */
	@Test
	public void testAddWithPersistenceException() {
		final Game game = generate(Game.class);
		doThrow(PersistenceException.class).when(entityManager).persist(any(Game.class));

		try {
			gameDAO.add(game);
			fail("Can't add game with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).persist(game);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAO#update(Game)}. */
	@Test
	public void testUpdate() {
		final Game game = generate(Game.class);

		gameDAO.update(game);

		verify(entityManager).merge(game);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAOImpl#update(Game)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEntityManager() {
		((GameDAOImpl) gameDAO).setEntityManager(null);
		gameDAO.update(mock(Game.class));
	}

	/** Test method for {@link GameDAO#update(Game)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			gameDAO.update(null);
			fail("Can't update game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link GameDAOImpl#update(Game)} with exception in persistence. */
	@Test
	public void testUpdateWithPersistenceException() {
		final Game game = generate(Game.class);
		doThrow(PersistenceException.class).when(entityManager).merge(any(Game.class));

		try {
			gameDAO.update(game);
			fail("Can't update game with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).merge(game);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAO#remove(Game)} with managed game. */
	@Test
	public void testRemoveWithManagedGame() {
		final Game game = generate(Game.class);
		when(entityManager.contains(any(Game.class))).thenReturn(true);

		gameDAO.remove(game);

		verify(entityManager).contains(game);
		verify(entityManager).remove(game);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAO#remove(Game)} with not managed game. */
	@Test
	public void testRemoveWithNotManagedGame() {
		final Game game = generate(Game.class);
		when(entityManager.contains(any(Game.class))).thenReturn(false);
		when(entityManager.getReference(eq(Game.class), anyInt())).thenReturn(game);

		gameDAO.remove(game);

		verify(entityManager).contains(game);
		verify(entityManager).getReference(Game.class, game.getId());
		verify(entityManager).remove(game);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link GameDAOImpl#remove(Game)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEntityManager() {
		((GameDAOImpl) gameDAO).setEntityManager(null);
		gameDAO.remove(mock(Game.class));
	}

	/** Test method for {@link GameDAO#remove(Game)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			gameDAO.remove(null);
			fail("Can't remove game with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link GameDAOImpl#remove(Game)} with exception in persistence. */
	@Test
	public void testRemoveWithPersistenceException() {
		final Game game = generate(Game.class);
		doThrow(PersistenceException.class).when(entityManager).contains(any(Game.class));

		try {
			gameDAO.remove(game);
			fail("Can't remove game with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).contains(game);
		verifyNoMoreInteractions(entityManager);
	}

	/**
	 * Sets ID.
	 *
	 * @param id ID
	 * @return mocked answer
	 */
	private Answer<Void> setId(final Integer id) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				((Game) invocation.getArguments()[0]).setId(id);
				return null;
			}

		};
	}

}
