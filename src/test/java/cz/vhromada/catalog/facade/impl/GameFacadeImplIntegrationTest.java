package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A class represents integration test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class GameFacadeImplIntegrationTest extends AbstractParentFacadeIntegrationTest<Game, cz.vhromada.catalog.domain.Game> {

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
     * Test method for {@link GameFacade#add(Game)} with game with null name.
     */
    @Test
    public void add_NullName() {
        final Game game = newData(null);
        game.setName(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Game game = newData(null);
        game.setName("");

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null URL to english Wikipedia about game.
     */
    @Test
    public void add_NullWikiEn() {
        final Game game = newData(null);
        game.setWikiEn(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null URL to czech Wikipedia about game.
     */
    @Test
    public void add_NullWikiCz() {
        final Game game = newData(null);
        game.setWikiCz(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with not positive count of media.
     */
    @Test
    public void add_NotPositiveMediaCount() {
        final Game game = newData(null);
        game.setMediaCount(0);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null other data.
     */
    @Test
    public void add_NullOtherData() {
        final Game game = newData(null);
        game.setOtherData(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#add(Game)} with game with null note.
     */
    @Test
    public void add_NullNote() {
        final Game game = newData(null);
        game.setNote(null);

        final Result<Void> result = gameFacade.add(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null name.
     */
    @Test
    public void update_NullName() {
        final Game game = newData(1);
        game.setName(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Game game = newData(1);
        game.setName("");

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null URL to english Wikipedia about game.
     */
    @Test
    public void update_NullWikiEn() {
        final Game game = newData(1);
        game.setWikiEn(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null URL to czech Wikipedia about game.
     */
    @Test
    public void update_NullWikiCz() {
        final Game game = newData(1);
        game.setWikiCz(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with not positive count of media.
     */
    @Test
    public void update_NotPositiveMediaCount() {
        final Game game = newData(1);
        game.setMediaCount(0);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null other data.
     */
    @Test
    public void update_NullOtherData() {
        final Game game = newData(1);
        game.setOtherData(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")));

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GameFacade#update(Game)} with game with null note.
     */
    @Test
    public void update_NullNote() {
        final Game game = newData(1);
        game.setNote(null);

        final Result<Void> result = gameFacade.update(game);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")));

        assertDefaultRepositoryData();
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

        assertDefaultRepositoryData();
    }

    @Override
    protected CatalogParentFacade<Game> getCatalogParentFacade() {
        return gameFacade;
    }

    @Override
    protected Integer getDefaultDataCount() {
        return GameUtils.GAMES_COUNT;
    }

    @Override
    protected Integer getRepositoryDataCount() {
        return GameUtils.getGamesCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Game> getDataList() {
        return GameUtils.getGames();
    }

    @Override
    protected cz.vhromada.catalog.domain.Game getDomainData(final Integer index) {
        return GameUtils.getGame(index);
    }

    @Override
    protected Game newData(final Integer id) {
        return GameUtils.newGame(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Game newDomainData(final Integer id) {
        return GameUtils.newGameDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Game getRepositoryData(final Integer id) {
        return GameUtils.getGame(entityManager, id);
    }

    @Override
    protected String getName() {
        return "Game";
    }

    @Override
    protected void clearReferencedData() {
    }

    @Override
    protected void assertDataListDeepEquals(final List<Game> expected, final List<cz.vhromada.catalog.domain.Game> actual) {
        GameUtils.assertGameListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Game expected, final cz.vhromada.catalog.domain.Game actual) {
        GameUtils.assertGameDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Game expected, final cz.vhromada.catalog.domain.Game actual) {
        GameUtils.assertGameDeepEquals(expected, actual);
    }

}
