package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonDAO")
public class SeasonDAOImpl implements SeasonDAO {

	/** Entity manager field */
	private static final String ENTITY_MANAGER_FIELD = "Entity manager";

	/** Serie argument */
	private static final String SERIE_ARGUMENT = "Serie";

	/** Season argument */
	private static final String SEASON_ARGUMENT = "Season";

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
	 * @throws IllegalStateException    if entity manager isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws DataStorageException     {@inheritDoc}
	 */
	@Override
	public Season getSeason(final Integer id) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(id, ID_ARGUMENT);

		try {
			return entityManager.find(Season.class, id);
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
	public void add(final Season season) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

		try {
			entityManager.persist(season);
			season.setPosition(season.getId() - 1);
			entityManager.merge(season);
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
	public void update(final Season season) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

		try {
			entityManager.merge(season);
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
	public void remove(final Season season) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

		try {
			if (entityManager.contains(season)) {
				entityManager.remove(season);
			} else {
				entityManager.remove(entityManager.getReference(Season.class, season.getId()));
			}
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
	public List<Season> findSeasonsBySerie(final Serie serie) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

		try {
			final TypedQuery<Season> query = entityManager.createNamedQuery(Season.FIND_BY_SERIE, Season.class);
			query.setParameter("serie", serie.getId());
			return query.getResultList();
		} catch (final PersistenceException ex) {
			throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
		}
	}

}
