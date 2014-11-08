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

	/** Entity manager field */
	private static final String ENTITY_MANAGER_FIELD = "Entity manager";

	/** Serie argument */
	private static final String SERIE_ARGUMENT = "Serie";

	/** ID argument */
	private static final String ID_ARGUMENT = "ID";

	/** Message for {@link DataStorageException} */
	private static final String DATA_STORAGE_EXCEPTION_MESSAGE = "Error in working with ORM.";

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
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);

		try {
			return new ArrayList<>(entityManager.createNamedQuery(Serie.SELECT_SERIES, Serie.class).getResultList());
		} catch (final PersistenceException ex) {
			throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
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
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(id, ID_ARGUMENT);

		try {
			return entityManager.find(Serie.class, id);
		} catch (final PersistenceException ex) {
			throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
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
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

		try {
			entityManager.persist(serie);
			serie.setPosition(serie.getId() - 1);
			entityManager.merge(serie);
		} catch (final PersistenceException ex) {
			throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
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
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

		try {
			entityManager.merge(serie);
		} catch (final PersistenceException ex) {
			throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
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
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

		try {
			if (entityManager.contains(serie)) {
				entityManager.remove(serie);
			} else {
				entityManager.remove(entityManager.getReference(Serie.class, serie.getId()));
			}
		} catch (final PersistenceException ex) {
			throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
		}
	}

}