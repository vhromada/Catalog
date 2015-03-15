package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import cz.vhromada.catalog.dao.GameDAO;
import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameDAO")
public class GameDAOImpl implements GameDAO {

    /** Entity manager argument */
    private static final String ENTITY_MANAGER_ARGUMENT = "Entity manager";

    /** Game argument */
    private static final String GAME_ARGUMENT = "Game";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link DataStorageException} */
    private static final String DATA_STORAGE_EXCEPTION_MESSAGE = "Error in working with ORM.";

    /** Entity manager */
    private EntityManager entityManager;

    /**
     * Creates a new instance of GameDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public GameDAOImpl(final EntityManager entityManager) {
        Validators.validateArgumentNotNull(entityManager, ENTITY_MANAGER_ARGUMENT);

        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @throws DataStorageException {@inheritDoc}
     */
    @Override
    public List<Game> getGames() {
        try {
            return new ArrayList<>(entityManager.createNamedQuery(Game.SELECT_GAMES, Game.class).getResultList());
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Game getGame(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(Game.class, id);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            entityManager.persist(game);
            game.setPosition(game.getId() - 1);
            entityManager.merge(game);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            entityManager.merge(game);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Game game) {
        Validators.validateArgumentNotNull(game, GAME_ARGUMENT);

        try {
            if (entityManager.contains(game)) {
                entityManager.remove(game);
            } else {
                entityManager.remove(entityManager.getReference(Game.class, game.getId()));
            }
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
