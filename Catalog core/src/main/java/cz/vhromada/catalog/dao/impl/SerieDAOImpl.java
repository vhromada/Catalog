package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import cz.vhromada.catalog.dao.SerieDAO;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for series.
 *
 * @author Vladimir Hromada
 */
@Component("serieDAO")
public class SerieDAOImpl implements SerieDAO {

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
	public List<Serie> getSeries() {
		Validators.validateFieldNotNull(entityManager, "Entity manager");

		try {
			return new ArrayList<>(entityManager.createNamedQuery(Serie.SELECT_SERIES, Serie.class).getResultList());
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
	public Serie getSerie(final Integer id) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return entityManager.find(Serie.class, id);
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
	public void add(final Serie serie) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(serie, "Serie");

		try {
			entityManager.persist(serie);
			serie.setPosition(serie.getId() - 1);
			entityManager.merge(serie);
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
	public void update(final Serie serie) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(serie, "Serie");

		try {
			entityManager.merge(serie);
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
	public void remove(final Serie serie) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(serie, "Serie");

		try {
			if (entityManager.contains(serie)) {
				entityManager.remove(serie);
			} else {
				entityManager.remove(entityManager.getReference(Serie.class, serie.getId()));
			}
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

}
