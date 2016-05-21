package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.GameDAO;
import cz.vhromada.catalog.dao.entities.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameDAO")
public class GameDAOImpl extends AbstractDAO<Game> implements GameDAO {

    /**
     * Creates a new instance of GameDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public GameDAOImpl(final EntityManager entityManager) {
        super(entityManager, Game.class, "Game");
    }

    /**
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public List<Game> getGames() {
        return getData(Game.SELECT_GAMES);
    }

    /**
     * @throws IllegalArgumentException                                {@inheritDoc}
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public Game getGame(final Integer id) {
        return getItem(id);
    }

    /**
     * @throws IllegalArgumentException                                {@inheritDoc}
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public void add(final Game game) {
        addItem(game);
    }

    /**
     * @throws IllegalArgumentException                                {@inheritDoc}
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public void update(final Game game) {
        updateItem(game);
    }

    /**
     * @throws IllegalArgumentException                                {@inheritDoc}
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public void remove(final Game game) {
        removeItem(game);
    }

}
