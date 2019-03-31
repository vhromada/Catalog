package cz.vhromada.catalog.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Game;
import cz.vhromada.catalog.repository.GameRepository;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.common.repository.MovableRepository;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.service.MovableServiceTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;

/**
 * A class represents test for class {@link GameService}.
 *
 * @author Vladimir Hromada
 */
class GameServiceTest extends MovableServiceTest<Game> {

    /**
     * Instance of {@link GameRepository}
     */
    @Mock
    private GameRepository repository;

    /**
     * Test method for {@link GameService#GameService(GameRepository, Cache)} with null repository for games.
     */
    @Test
    void constructor_NullGameRepository() {
        assertThatThrownBy(() -> new GameService(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GameService#GameService(GameRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new GameService(repository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableRepository<Game> getRepository() {
        return repository;
    }

    @Override
    protected MovableService<Game> getService() {
        return new GameService(repository, getCache());
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
