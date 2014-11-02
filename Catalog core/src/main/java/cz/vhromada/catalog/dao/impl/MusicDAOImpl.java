package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicDAO")
public class MusicDAOImpl implements MusicDAO {

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
	public List<Music> getMusic() {
		Validators.validateFieldNotNull(entityManager, "Entity manager");

		try {
			return new ArrayList<>(entityManager.createNamedQuery(Music.SELECT_MUSIC, Music.class).getResultList());
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
	public Music getMusic(final Integer id) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return entityManager.find(Music.class, id);
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if entity manager isn't set
	 *                                  or music validator isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws DataStorageException     {@inheritDoc}
	 */
	@Override
	public void add(final Music music) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(music, "Music");

		try {
			entityManager.persist(music);
			music.setPosition(music.getId() - 1);
			entityManager.merge(music);
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
	public void update(final Music music) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(music, "Music");

		try {
			entityManager.merge(music);
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
	public void remove(final Music music) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(music, "Music");

		try {
			if (entityManager.contains(music)) {
				entityManager.remove(music);
			} else {
				entityManager.remove(entityManager.getReference(Music.class, music.getId()));
			}
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

}
