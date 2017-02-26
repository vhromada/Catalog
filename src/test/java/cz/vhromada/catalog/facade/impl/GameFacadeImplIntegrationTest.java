package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

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
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class GameFacadeImplIntegrationTest {

    /**
     * Event for null game
     */
    private static final Event NULL_GAME_EVENT = new Event(Severity.ERROR, "GAME_NULL", "Game mustn't be null.");

    /**
     * Event for game with null ID
     */
    private static final Event NULL_GAME_ID_EVENT = new Event(Severity.ERROR, "GAME_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not exiting game
     */
    private static final Event NOT_EXIST_GAME_EVENT = new Event(Severity.ERROR, "GAME_NOT_EXIST", "Game doesn't exist.");

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
    public void newData() {
        final Result<Void> result = gameFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(GameUtils.getGamesCount(entityManager), is(0));
    }

    /**
     * Test method for {@link GameFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final Result<List<Game>> result = gameFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));
        GameUtils.assertGameListDeepEquals(result.getData(), GameUtils.getGames());

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= GameUtils.GAMES_COUNT; i++) {
            final Result<Game> result = gameFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            GameUtils.assertGameDeepEquals(result.getData(), GameUtils.getGame(i));
        }

        final Result<Game> result = gameFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#get(Integer)} with null game.
     */
    @Test
    public void get_NullGame() {
        final Result<Game> result = gameFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final Result<Void> result = gameFacade.add(GameUtils.newGame(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Game addedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1);
        GameUtils.assertGameDeepEquals(GameUtils.newGameDomain(GameUtils.GAMES_COUNT + 1), addedGame);
        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT + 1));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with null game.
     */
    @Test
    public void add_NullGame() {
        final Result<Void> result = gameFacade.add(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with not null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = gameFacade.add(GameUtils.newGame(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_ID_NOT_NULL", "ID must be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null name.
     */
    @Test
    public void add_NullName() {
        final Game game = GameUtils.newGame(null);
        game.setName(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Game game = GameUtils.newGame(null);
        game.setName("");

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null URL to english Wikipedia about game.
     */
    @Test
    public void add_NullWikiEn() {
        final Game game = GameUtils.newGame(null);
        game.setWikiEn(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null URL to czech Wikipedia about game.
     */
    @Test
    public void add_NullWikiCz() {
        final Game game = GameUtils.newGame(null);
        game.setWikiCz(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with not positive count of media.
     */
    @Test
    public void add_NotPositiveMediaCount() {
        final Game game = GameUtils.newGame(null);
        game.setMediaCount(0);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null other data.
     */
    @Test
    public void add_NullOtherData() {
        final Game game = GameUtils.newGame(null);
        game.setOtherData(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null note.
     */
    @Test
    public void add_NullNote() {
        final Game game = GameUtils.newGame(null);
        game.setNote(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Game game = GameUtils.newGame(1);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Game updatedGame = GameUtils.getGame(entityManager, 1);
        GameUtils.assertGameDeepEquals(game, updatedGame);
        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with null game.
     */
    @Test
    public void update_NullGame() {
        final Result<Void> result = gameFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null ID.
     */
    @Test
    public void update_NullId() {
        final Result<Void> result = gameFacade.update(GameUtils.newGame(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_ID_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null name.
     */
    @Test
    public void update_NullName() {
        final Game game = GameUtils.newGame(1);
        game.setName(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Game game = GameUtils.newGame(1);
        game.setName("");

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null URL to english Wikipedia about game.
     */
    @Test
    public void update_NullWikiEn() {
        final Game game = GameUtils.newGame(1);
        game.setWikiEn(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null URL to czech Wikipedia about game.
     */
    @Test
    public void update_NullWikiCz() {
        final Game game = GameUtils.newGame(1);
        game.setWikiCz(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with not positive count of media.
     */
    @Test
    public void update_NotPositiveMediaCount() {
        final Game game = GameUtils.newGame(1);
        game.setMediaCount(0);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null other data.
     */
    @Test
    public void update_NullOtherData() {
        final Game game = GameUtils.newGame(1);
        game.setOtherData(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null note.
     */
    @Test
    public void update_NullNote() {
        final Game game = GameUtils.newGame(1);
        game.setNote(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with bad ID.
     */
    @Test
    public void update_BadId() {
        final Result<Void> result = gameFacade.update(GameUtils.newGame(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Result<Void> result = gameFacade.remove(GameUtils.newGame(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(GameUtils.getGame(entityManager, 1), is(nullValue()));
        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT - 1));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with null game.
     */
    @Test
    public void remove_NullGame() {
        final Result<Void> result = gameFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with game with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = gameFacade.remove(GameUtils.newGame(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_ID_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#remove(Game)} with game with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = gameFacade.remove(GameUtils.newGame(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Game game = GameUtils.getGame(GameUtils.GAMES_COUNT);
        game.setId(GameUtils.GAMES_COUNT + 1);

        final Result<Void> result = gameFacade.duplicate(GameUtils.newGame(GameUtils.GAMES_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Game duplicatedGame = GameUtils.getGame(entityManager, GameUtils.GAMES_COUNT + 1);
        GameUtils.assertGameDeepEquals(game, duplicatedGame);
        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT + 1));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with null game.
     */
    @Test
    public void duplicate_NullGame() {
        final Result<Void> result = gameFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with game with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = gameFacade.duplicate(GameUtils.newGame(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_ID_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#duplicate(Game)} with game with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = gameFacade.duplicate(GameUtils.newGame(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.getGame(1);
        game1.setPosition(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.getGame(2);
        game2.setPosition(0);

        final Result<Void> result = gameFacade.moveUp(GameUtils.newGame(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        GameUtils.assertGameDeepEquals(game1, GameUtils.getGame(entityManager, 1));
        GameUtils.assertGameDeepEquals(game2, GameUtils.getGame(entityManager, 2));
        for (int i = 3; i <= GameUtils.GAMES_COUNT; i++) {
            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), GameUtils.getGame(entityManager, i));
        }
        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with null game.
     */
    @Test
    public void moveUp_NullGame() {
        final Result<Void> result = gameFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with game with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = gameFacade.moveUp(GameUtils.newGame(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_ID_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with not movable game.
     */
    @Test
    public void moveUp_NotMovableGame() {
        final Result<Void> result = gameFacade.moveUp(GameUtils.newGame(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NOT_MOVABLE", "Game can't be moved up.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveUp(Game)} with game with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = gameFacade.moveUp(GameUtils.newGame(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.getGame(1);
        game1.setPosition(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.getGame(2);
        game2.setPosition(0);

        final Result<Void> result = gameFacade.moveDown(GameUtils.newGame(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        GameUtils.assertGameDeepEquals(game1, GameUtils.getGame(entityManager, 1));
        GameUtils.assertGameDeepEquals(game2, GameUtils.getGame(entityManager, 2));
        for (int i = 3; i <= GameUtils.GAMES_COUNT; i++) {
            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), GameUtils.getGame(entityManager, i));
        }
        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with null game.
     */
    @Test
    public void moveDown_NullGame() {
        final Result<Void> result = gameFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with game with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = gameFacade.moveDown(GameUtils.newGame(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GAME_ID_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with not movable game.
     */
    @Test
    public void moveDown_NotMovableGame() {
        final Result<Void> result = gameFacade.moveDown(GameUtils.newGame(GameUtils.GAMES_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NOT_MOVABLE", "Game can't be moved down.")));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#moveDown(Game)} with game with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = gameFacade.moveDown(GameUtils.newGame(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GAME_EVENT));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void updatePositions() {
        final Result<Void> result = gameFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        for (int i = 1; i <= GameUtils.GAMES_COUNT; i++) {
            GameUtils.assertGameDeepEquals(GameUtils.getGame(i), GameUtils.getGame(entityManager, i));
        }
        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    public void getTotalMediaCount() {
        final Result<Integer> result = gameFacade.getTotalMediaCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(6));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(GameUtils.getGamesCount(entityManager), is(GameUtils.GAMES_COUNT));
    }

}
