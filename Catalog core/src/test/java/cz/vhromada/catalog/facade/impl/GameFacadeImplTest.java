package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.facade.validators.GameTOValidator;
import cz.vhromada.catalog.service.GameService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.convert.ConversionService;

/**
 * A class represents test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GameFacadeImplTest extends ObjectGeneratorTest {

	/** Instance of {@link GameService} */
	@Mock
	private GameService gameService;

	/** Instance of {@link ConversionService} */
	@Mock
	private ConversionService conversionService;

	/** Instance of {@link GameTOValidator} */
	@Mock
	private GameTOValidator gameTOValidator;

	/** Instance of {@link GameFacade} */
	@InjectMocks
	private GameFacade gameFacade = new GameFacadeImpl();

	/** Test method for {@link GameFacade#newData()}. */
	@Test
	public void testNewData() {
		gameFacade.newData();

		verify(gameService).newData();
		verifyNoMoreInteractions(gameService);
	}

	/** Test method for {@link GameFacade#newData()} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.newData();
	}

	/** Test method for {@link GameFacade#newData()} with exception in service tier. */
	@Test
	public void testNewDataWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(gameService).newData();

		try {
			gameFacade.newData();
			fail("Can't create new data with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).newData();
		verifyNoMoreInteractions(gameService);
	}

	/** Test method for {@link GameFacade#getGames()}. */
	@Test
	public void testGetGames() {
		final List<Game> games = CollectionUtils.newList(generate(Game.class), generate(Game.class));
		final List<GameTO> gamesList = CollectionUtils.newList(generate(GameTO.class), generate(GameTO.class));
		when(gameService.getGames()).thenReturn(games);
		for (int i = 0; i < games.size(); i++) {
			final Game game = games.get(i);
			when(conversionService.convert(game, GameTO.class)).thenReturn(gamesList.get(i));
		}

		DeepAsserts.assertEquals(gamesList, gameFacade.getGames());

		verify(gameService).getGames();
		for (Game game : games) {
			verify(conversionService).convert(game, GameTO.class);
		}
		verifyNoMoreInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#getGames()} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetGamesWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.getGames();
	}

	/** Test method for {@link GameFacade#getGames()} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetGamesWithNotSetConversionService() {
		((GameFacadeImpl) gameFacade).setConversionService(null);
		gameFacade.getGames();
	}

	/** Test method for {@link GameFacade#getGames()} with exception in service tier. */
	@Test
	public void testGetGamesWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(gameService).getGames();

		try {
			gameFacade.getGames();
			fail("Can't get games with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).getGames();
		verifyNoMoreInteractions(gameService);
		verifyZeroInteractions(conversionService);
	}

	/** Test method for {@link GameFacade#getGame(Integer)} with existing game. */
	@Test
	public void testGetGameWithExistingGame() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(game);
		when(conversionService.convert(any(Game.class), eq(GameTO.class))).thenReturn(gameTO);

		DeepAsserts.assertEquals(gameTO, gameFacade.getGame(gameTO.getId()));

		verify(gameService).getGame(gameTO.getId());
		verify(conversionService).convert(game, GameTO.class);
		verifyNoMoreInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#getGame(Integer)} with not existing game. */
	@Test
	public void testGetGameWithNotExistingGame() {
		when(gameService.getGame(anyInt())).thenReturn(null);
		when(conversionService.convert(any(Game.class), eq(GameTO.class))).thenReturn(null);

		assertNull(gameFacade.getGame(Integer.MAX_VALUE));

		verify(gameService).getGame(Integer.MAX_VALUE);
		verify(conversionService).convert(null, GameTO.class);
		verifyNoMoreInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#getGame(Integer)} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetGameWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.getGame(Integer.MAX_VALUE);
	}

	/** Test method for {@link GameFacade#getGame(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetGameWithNotSetConversionService() {
		((GameFacadeImpl) gameFacade).setConversionService(null);
		gameFacade.getGame(Integer.MAX_VALUE);
	}

	/** Test method for {@link GameFacade#getGame(Integer)} with null argument. */
	@Test
	public void testGetGameWithNullArgument() {
		try {
			gameFacade.getGame(null);
			fail("Can't get game with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#getGame(Integer)} with exception in service tier. */
	@Test
	public void testGetGameWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(gameService).getGame(anyInt());

		try {
			gameFacade.getGame(Integer.MAX_VALUE);
			fail("Can't get game with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).getGame(Integer.MAX_VALUE);
		verifyNoMoreInteractions(gameService);
		verifyZeroInteractions(conversionService);
	}

	/** Test method for {@link GameFacade#add(GameTO)}. */
	@Test
	public void testAdd() {
		final Game game = generate(Game.class);
		game.setId(null);
		final GameTO gameTO = generate(GameTO.class);
		gameTO.setId(null);
		final int id = generate(Integer.class);
		final int position = generate(Integer.class);
		doAnswer(setGameIdAndPosition(id, position)).when(gameService).add(any(Game.class));
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		gameFacade.add(gameTO);
		DeepAsserts.assertEquals(id, game.getId());
		DeepAsserts.assertEquals(position, game.getPosition());

		verify(gameService).add(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateNewGameTO(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.add(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#add(GameTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((GameFacadeImpl) gameFacade).setConversionService(null);
		gameFacade.add(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#add(GameTO)} with not set validator for TO for game. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetGameTOValidator() {
		((GameFacadeImpl) gameFacade).setGameTOValidator(null);
		gameFacade.add(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#add(GameTO)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(gameTOValidator).validateNewGameTO(any(GameTO.class));

		try {
			gameFacade.add(null);
			fail("Can't add game with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(gameTOValidator).validateNewGameTO(null);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with argument with bad data. */
	@Test
	public void testAddWithBadArgument() {
		final GameTO game = generate(GameTO.class);
		game.setId(null);
		doThrow(ValidationException.class).when(gameTOValidator).validateNewGameTO(any(GameTO.class));

		try {
			gameFacade.add(game);
			fail("Can't add game with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameTOValidator).validateNewGameTO(game);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with service tier not setting ID. */
	@Test
	public void testAddWithNotServiceTierSettingID() {
		final Game game = generate(Game.class);
		game.setId(null);
		final GameTO gameTO = generate(GameTO.class);
		gameTO.setId(null);
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		try {
			gameFacade.add(gameTO);
			fail("Can't add game with service tier not setting ID.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).add(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateNewGameTO(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#add(GameTO)} with exception in service tier. */
	@Test
	public void testAddWithServiceTierException() {
		final Game game = generate(Game.class);
		game.setId(null);
		final GameTO gameTO = generate(GameTO.class);
		gameTO.setId(null);
		doThrow(ServiceOperationException.class).when(gameService).add(any(Game.class));
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		try {
			gameFacade.add(gameTO);
			fail("Can't add game with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).add(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateNewGameTO(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#update(GameTO)}. */
	@Test
	public void testUpdate() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.exists(any(Game.class))).thenReturn(true);
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		gameFacade.update(gameTO);

		verify(gameService).exists(game);
		verify(gameService).update(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateExistingGameTO(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.update(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#update(GameTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((GameFacadeImpl) gameFacade).setConversionService(null);
		gameFacade.update(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#update(GameTO)} with not set validator for TO for game. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetGameTOValidator() {
		((GameFacadeImpl) gameFacade).setGameTOValidator(null);
		gameFacade.update(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#update(GameTO)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(gameTOValidator).validateExistingGameTO(any(GameTO.class));

		try {
			gameFacade.update(null);
			fail("Can't update game with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(gameTOValidator).validateExistingGameTO(null);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with argument with bad data. */
	@Test
	public void testUpdateWithBadArgument() {
		final GameTO game = generate(GameTO.class);
		doThrow(ValidationException.class).when(gameTOValidator).validateExistingGameTO(any(GameTO.class));

		try {
			gameFacade.update(game);
			fail("Can't update game with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameTOValidator).validateExistingGameTO(game);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with not existing argument. */
	@Test
	public void testUpdateWithNotExistingArgument() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.exists(any(Game.class))).thenReturn(false);
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		try {
			gameFacade.update(gameTO);
			fail("Can't update game with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(gameService).exists(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateExistingGameTO(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#update(GameTO)} with exception in service tier. */
	@Test
	public void testUpdateWithServiceTierException() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		doThrow(ServiceOperationException.class).when(gameService).exists(any(Game.class));
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		try {
			gameFacade.update(gameTO);
			fail("Can't update game with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).exists(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateExistingGameTO(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#remove(GameTO)}. */
	@Test
	public void testRemove() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(game);

		gameFacade.remove(gameTO);

		verify(gameService).getGame(gameTO.getId());
		verify(gameService).remove(game);
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.remove(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with not set validator for TO for game. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetGameTOValidator() {
		((GameFacadeImpl) gameFacade).setGameTOValidator(null);
		gameFacade.remove(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.remove(null);
			fail("Can't remove game with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(null);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService);
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with argument with bad data. */
	@Test
	public void testRemoveWithBadArgument() {
		final GameTO game = generate(GameTO.class);
		doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.remove(game);
			fail("Can't remove game with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService);
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with not existing argument. */
	@Test
	public void testRemoveWithNotExistingArgument() {
		final GameTO game = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(null);

		try {
			gameFacade.remove(game);
			fail("Can't remove game with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(gameService).getGame(game.getId());
		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#remove(GameTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final GameTO game = generate(GameTO.class);
		doThrow(ServiceOperationException.class).when(gameService).getGame(anyInt());

		try {
			gameFacade.remove(game);
			fail("Can't remove game with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).getGame(game.getId());
		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)}. */
	@Test
	public void testDuplicate() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(game);

		gameFacade.duplicate(gameTO);

		verify(gameService).getGame(gameTO.getId());
		verify(gameService).duplicate(game);
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.duplicate(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with not set validator for TO for game. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetGameTOValidator() {
		((GameFacadeImpl) gameFacade).setGameTOValidator(null);
		gameFacade.duplicate(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.duplicate(null);
			fail("Can't duplicate game with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(null);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService);
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with argument with bad data. */
	@Test
	public void testDuplicateWithBadArgument() {
		final GameTO game = generate(GameTO.class);
		doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.duplicate(game);
			fail("Can't duplicate game with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService);
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with not existing argument. */
	@Test
	public void testDuplicateWithNotExistingArgument() {
		final GameTO game = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(null);

		try {
			gameFacade.duplicate(game);
			fail("Can't duplicate game with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(gameService).getGame(game.getId());
		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#duplicate(GameTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final GameTO game = generate(GameTO.class);
		doThrow(ServiceOperationException.class).when(gameService).getGame(anyInt());

		try {
			gameFacade.duplicate(game);
			fail("Can't duplicate game with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).getGame(game.getId());
		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)}. */
	@Test
	public void testMoveUp() {
		final Game game = generate(Game.class);
		final List<Game> games = CollectionUtils.newList(mock(Game.class), game);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(game);
		when(gameService.getGames()).thenReturn(games);

		gameFacade.moveUp(gameTO);

		verify(gameService).getGame(gameTO.getId());
		verify(gameService).getGames();
		verify(gameService).moveUp(game);
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.moveUp(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with not set validator for TO for game. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetGameTOValidator() {
		((GameFacadeImpl) gameFacade).setGameTOValidator(null);
		gameFacade.moveUp(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.moveUp(null);
			fail("Can't move up game with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(null);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with argument with bad data. */
	@Test
	public void testMoveUpWithBadArgument() {
		final GameTO game = generate(GameTO.class);
		doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.moveUp(game);
			fail("Can't move up game with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with not existing argument. */
	@Test
	public void testMoveUpWithNotExistingArgument() {
		final GameTO game = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(null);

		try {
			gameFacade.moveUp(game);
			fail("Can't move up game with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(gameService).getGame(game.getId());
		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with not moveable argument. */
	@Test
	public void testMoveUpWithNotMoveableArgument() {
		final Game game = generate(Game.class);
		final List<Game> games = CollectionUtils.newList(game, mock(Game.class));
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(game);
		when(gameService.getGames()).thenReturn(games);

		try {
			gameFacade.moveUp(gameTO);
			fail("Can't move up game with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameService).getGame(gameTO.getId());
		verify(gameService).getGames();
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#moveUp(GameTO)} with exception in service tier. */
	@Test
	public void testMoveUpWithServiceTierException() {
		final GameTO game = generate(GameTO.class);
		doThrow(ServiceOperationException.class).when(gameService).getGame(anyInt());

		try {
			gameFacade.moveUp(game);
			fail("Can't move up game with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).getGame(game.getId());
		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)}. */
	@Test
	public void testMoveDown() {
		final Game game = generate(Game.class);
		final List<Game> games = CollectionUtils.newList(game, mock(Game.class));
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(game);
		when(gameService.getGames()).thenReturn(games);

		gameFacade.moveDown(gameTO);

		verify(gameService).getGame(gameTO.getId());
		verify(gameService).getGames();
		verify(gameService).moveDown(game);
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.moveDown(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with not set validator for TO for game. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetGameTOValidator() {
		((GameFacadeImpl) gameFacade).setGameTOValidator(null);
		gameFacade.moveDown(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.moveDown(null);
			fail("Can't move down game with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(null);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with argument with bad data. */
	@Test
	public void testMoveDownWithBadArgument() {
		final GameTO game = generate(GameTO.class);
		doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.moveDown(game);
			fail("Can't move down game with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with not existing argument. */
	@Test
	public void testMoveDownWithNotExistingArgument() {
		final GameTO game = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(null);

		try {
			gameFacade.moveDown(game);
			fail("Can't move down game with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(gameService).getGame(game.getId());
		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with not moveable argument. */
	@Test
	public void testMoveDownWithNotMoveableArgument() {
		final Game game = generate(Game.class);
		final List<Game> games = CollectionUtils.newList(mock(Game.class), game);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.getGame(anyInt())).thenReturn(game);
		when(gameService.getGames()).thenReturn(games);

		try {
			gameFacade.moveDown(gameTO);
			fail("Can't move down game with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameService).getGame(gameTO.getId());
		verify(gameService).getGames();
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#moveDown(GameTO)} with exception in service tier. */
	@Test
	public void testMoveDownWithServiceTierException() {
		final GameTO game = generate(GameTO.class);
		doThrow(ServiceOperationException.class).when(gameService).getGame(anyInt());

		try {
			gameFacade.moveDown(game);
			fail("Can't move down game with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).getGame(game.getId());
		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with existing game. */
	@Test
	public void testExistsWithExistingGame() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.exists(any(Game.class))).thenReturn(true);
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		assertTrue(gameFacade.exists(gameTO));

		verify(gameService).exists(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with not existing game. */
	@Test
	public void testExistsWithNotExistingGame() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		when(gameService.exists(any(Game.class))).thenReturn(false);
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		assertFalse(gameFacade.exists(gameTO));

		verify(gameService).exists(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.exists(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((GameFacadeImpl) gameFacade).setConversionService(null);
		gameFacade.exists(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with not set validator for TO for game. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetGameTOValidator() {
		((GameFacadeImpl) gameFacade).setGameTOValidator(null);
		gameFacade.exists(mock(GameTO.class));
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.exists(null);
			fail("Can't exists game with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(null);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with argument with bad data. */
	@Test
	public void testExistsWithBadArgument() {
		final GameTO game = generate(GameTO.class);
		doThrow(ValidationException.class).when(gameTOValidator).validateGameTOWithId(any(GameTO.class));

		try {
			gameFacade.exists(game);
			fail("Can't exists game with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(gameTOValidator).validateGameTOWithId(game);
		verifyNoMoreInteractions(gameTOValidator);
		verifyZeroInteractions(gameService, conversionService);
	}

	/** Test method for {@link GameFacade#exists(GameTO)} with exception in service tier. */
	@Test
	public void testExistsWithServiceTierException() {
		final Game game = generate(Game.class);
		final GameTO gameTO = generate(GameTO.class);
		doThrow(ServiceOperationException.class).when(gameService).exists(any(Game.class));
		when(conversionService.convert(any(GameTO.class), eq(Game.class))).thenReturn(game);

		try {
			gameFacade.exists(gameTO);
			fail("Can't exists game with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).exists(game);
		verify(conversionService).convert(gameTO, Game.class);
		verify(gameTOValidator).validateGameTOWithId(gameTO);
		verifyNoMoreInteractions(gameService, conversionService, gameTOValidator);
	}

	/** Test method for {@link GameFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		gameFacade.updatePositions();

		verify(gameService).updatePositions();
		verifyNoMoreInteractions(gameService);
	}

	/** Test method for {@link GameFacade#updatePositions()} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.updatePositions();
	}

	/** Test method for {@link GameFacade#updatePositions()} with exception in service tier. */
	@Test
	public void testUpdatePositionsWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(gameService).updatePositions();

		try {
			gameFacade.updatePositions();
			fail("Can't update positions with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).updatePositions();
		verifyNoMoreInteractions(gameService);
	}

	/** Test method for {@link GameFacade#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		final int count = generate(Integer.class);
		when(gameService.getTotalMediaCount()).thenReturn(count);

		DeepAsserts.assertEquals(count, gameFacade.getTotalMediaCount());

		verify(gameService).getTotalMediaCount();
		verifyNoMoreInteractions(gameService);
	}

	/** Test method for {@link GameFacade#getTotalMediaCount()} with not set service for games. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetGameService() {
		((GameFacadeImpl) gameFacade).setGameService(null);
		gameFacade.getTotalMediaCount();
	}

	/** Test method for {@link GameFacade#getTotalMediaCount()} with exception in service tier. */
	@Test
	public void testGetTotalMediaCountWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(gameService).getTotalMediaCount();

		try {
			gameFacade.getTotalMediaCount();
			fail("Can't get total media count with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(gameService).getTotalMediaCount();
		verifyNoMoreInteractions(gameService);
	}

	/**
	 * Sets game's ID and position.
	 *
	 * @param id       ID
	 * @param position position
	 * @return mocked answer
	 */
	private Answer<Void> setGameIdAndPosition(final Integer id, final int position) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Game game = (Game) invocation.getArguments()[0];
				game.setId(id);
				game.setPosition(position);
				return null;
			}

		};
	}

}
