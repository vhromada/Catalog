package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import cz.vhromada.catalog.dao.GenreDAO;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreDAO")
public class GenreDAOImpl implements GenreDAO {

	/** Entity manager field */
	private static final String ENTITY_MANAGER_FIELD = "Entity manager";

	/** Genre argument */
	private static final String GENRE_ARGUMENT = "Genre";

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
	public List<Genre> getGenres() {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);

		try {
			return new ArrayList<>(entityManager.createNamedQuery(Genre.SELECT_GENRES, Genre.class).getResultList());
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
	public Genre getGenre(final Integer id) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(id, ID_ARGUMENT);

		try {
			return entityManager.find(Genre.class, id);
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
	public void add(final Genre genre) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(genre, GENRE_ARGUMENT);

		try {
			entityManager.persist(genre);
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
	public void update(final Genre genre) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(genre, GENRE_ARGUMENT);

		try {
			entityManager.merge(genre);
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
	public void remove(final Genre genre) {
		Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
		Validators.validateArgumentNotNull(genre, GENRE_ARGUMENT);

		try {
			if (entityManager.contains(genre)) {
				entityManager.remove(genre);
			} else {
				entityManager.remove(entityManager.getReference(Genre.class, genre.getId()));
			}
		} catch (final PersistenceException ex) {
			throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
		}
	}

}