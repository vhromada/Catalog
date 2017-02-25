package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.domain.Game;
import cz.vhromada.catalog.repository.GameRepository;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.GameUtils;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link GameServiceImpl}.
 *
 * @author Vladimir Hromada
 */
public class GameServiceImplTest extends AbstractServiceTest<Game> {

    /**
     * Instance of {@link GameRepository}
     */
    @Mock
    private GameRepository gameRepository;

    /**
     * Test method for {@link GameServiceImpl#GameServiceImpl(GameRepository, Cache)} with null repository for games.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGameRepository() {
        new GameServiceImpl(null, getCache());
    }

    /**
     * Test method for {@link GameServiceImpl#GameServiceImpl(GameRepository, Cache)} with null cache.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullCache() {
        new GameServiceImpl(gameRepository, null);
    }

    @Override
    protected JpaRepository<Game, Integer> getRepository() {
        return gameRepository;
    }

    @Override
    protected CatalogService<Game> getCatalogService() {
        return new GameServiceImpl(gameRepository, getCache());
    }

    @Override
    protected String getCacheKey() {
        return "games";
    }

    @Override
    protected Game getItem1() {
        return GameUtils.newGameDomain(1);
    }

    @Override
    protected Game getItem2() {
        return GameUtils.newGameDomain(2);
    }

    @Override
    protected Game getAddItem() {
        return GameUtils.newGameDomain(null);
    }

    @Override
    protected Game getCopyItem() {
        final Game game = GameUtils.newGameDomain(null);
        game.setPosition(0);

        return game;
    }

    @Override
    protected Class<Game> getItemClass() {
        return Game.class;
    }

    @Override
    protected void assertDataDeepEquals(final Game expected, final Game actual) {
        GameUtils.assertGameDeepEquals(expected, actual);
    }

}
