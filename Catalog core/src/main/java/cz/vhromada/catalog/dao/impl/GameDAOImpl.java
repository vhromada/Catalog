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

	/** Entity manager */
	@Autowired
	private EntityManager entityManager;

	/**
	 * Returns entity manager.
	 *
	 * @return entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Sets a new value to entity manager.
	 *
	 * @param entityManager new value
	 */
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException if entity manager isn't set
	 * @throws DataStorageException  {@inheritDoc}
	 */
	@Override
	public List<Game> getGames() {
		Validators.validateFieldNotNull(entityManager, "Entity manager");

		try {
			return new ArrayList<>(entityManager.createNamedQuery(Game.SELECT_GAMES, Game.class).getResultList());
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if entity manager isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws DataStorageException     {@inheritDoc}
	 */
	@Override
	public Game getGame(final Integer id) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return entityManager.find(Game.class, id);
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if entity manager isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws DataStorageException     {@inheritDoc}
	 */
	@Override
	public void add(final Game game) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(game, "Game");

		try {
			entityManager.persist(game);
			game.setPosition(game.getId() - 1);
			entityManager.merge(game);
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if entity manager isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws DataStorageException     {@inheritDoc}
	 */
	@Override
	public void update(final Game game) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(game, "Game");

		try {
			entityManager.merge(game);
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if entity manager isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws DataStorageException     {@inheritDoc}
	 */
	@Override
	public void remove(final Game game) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(game, "Game");

		try {
			if (entityManager.contains(game)) {
				entityManager.remove(game);
			} else {
				entityManager.remove(entityManager.getReference(Game.class, game.getId()));
			}
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

}
