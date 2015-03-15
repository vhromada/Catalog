package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.GameDAO;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.GameService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameService")
public class GameServiceImpl extends AbstractService<Game> implements GameService {

    /**
     * DAO for games field
     */
    private static final String GAME_DAO_ARGUMENT = "DAO for games";

    /**
     * Cache for games field
     */
    private static final String GAME_CACHE_ARGUMENT = "Cache for games";

    /**
     * Game argument
     */
    private static final String GAME_ARGUMENT = "Game";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link ServiceOperationException}
     */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /**
     * Cache key for list of games
     */
    private static final String GAMES_CACHE_KEY = "games";

    /**
     * Cache key for game
     */
    private static final String GAME_CACHE_KEY = "game";

    /**
     * DAO for games
     */
    private GameDAO gameDAO;

    /**
     * Cache for games
     */
    private Cache gameCache;

    /**
     * Creates a new instance of GameServiceImpl.
     *
     * @param gameDAO   DAO for games
     * @param gameCache cache for games
     * @throws IllegalArgumentException if DAO for games is null
     *                                  or cache for games is null
     */
    @Autowired
    public GameServiceImpl(final GameDAO gameDAO,
            @Value("#{cacheManager.getCache('gameCache')}") final Cache gameCache) {
        Validators.validateArgumentNotNull(gameDAO, GAME_DAO_ARGUMENT);
        Validators.validateArgumentNotNull(gameCache, GAME_CACHE_ARGUMENT);

        this.gameDAO = gameDAO;
        this.gameCache = gameCache;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            for (final Game game : getCachedGames(false)) {
                gameDAO.remove(game);
            }
            gameCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Game> getGames() {
        try {
            return getCachedGames(true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Game getGame(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedGame(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            gameDAO.add(game);
            addObjectToListCache(gameCache, GAMES_CACHE_KEY, game);
            addObjectToCache(gameCache, GAME_CACHE_KEY + game.getId(), game);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            gameDAO.update(game);
            gameCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            gameDAO.remove(game);
            removeObjectFromCache(gameCache, GAMES_CACHE_KEY, game);
            gameCache.evict(game.getId());
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            final Game newGame = new Game();
            newGame.setName(game.getName());
            newGame.setWikiEn(game.getWikiEn());
            newGame.setWikiCz(game.getWikiCz());
            newGame.setMediaCount(game.getMediaCount());
            newGame.setCrack(game.hasCrack());
            newGame.setSerialKey(game.hasSerialKey());
            newGame.setPatch(game.hasPatch());
            newGame.setTrainer(game.hasTrainer());
            newGame.setTrainerData(game.hasTrainerData());
            newGame.setEditor(game.hasEditor());
            newGame.setSaves(game.haveSaves());
            newGame.setOtherData(game.getOtherData());
            newGame.setNote(game.getNote());
            gameDAO.add(newGame);
            newGame.setPosition(game.getPosition());
            gameDAO.update(newGame);
            gameCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            final List<Game> games = getCachedGames(false);
            final Game otherGame = games.get(games.indexOf(game) - 1);
            switchPosition(game, otherGame);
            gameDAO.update(game);
            gameDAO.update(otherGame);
            gameCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            final List<Game> games = getCachedGames(false);
            final Game otherGame = games.get(games.indexOf(game) + 1);
            switchPosition(game, otherGame);
            gameDAO.update(game);
            gameDAO.update(otherGame);
            gameCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            return getCachedGame(game.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            final List<Game> games = getCachedGames(false);
            for (int i = 0; i < games.size(); i++) {
                final Game game = games.get(i);
                game.setPosition(i);
                gameDAO.update(game);
            }
            gameCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getTotalMediaCount() {
        try {
            int sum = 0;
            for (final Game game : getCachedGames(true)) {
                sum += game.getMediaCount();
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<Game> getData() {
        return gameDAO.getGames();
    }

    @Override
    protected Game getData(final Integer id) {
        return gameDAO.getGame(id);
    }

    /**
     * Returns list of games.
     *
     * @param cached true if returned data from DAO should be cached
     * @return list of games
     */
    private List<Game> getCachedGames(final boolean cached) {
        return getCachedObjects(gameCache, GAMES_CACHE_KEY, cached);
    }

    /**
     * Returns game with ID or null if there isn't such game.
     *
     * @param id ID
     * @return game with ID or null if there isn't such game
     */
    private Game getCachedGame(final Integer id) {
        return getCachedObject(gameCache, GAME_CACHE_KEY, id, true);
    }

    /**
     * Switch position of games.
     *
     * @param game1 1st game
     * @param game2 2nd game
     */
    private static void switchPosition(final Game game1, final Game game2) {
        final int position = game1.getPosition();
        game1.setPosition(game2.getPosition());
        game2.setPosition(position);
    }

}
