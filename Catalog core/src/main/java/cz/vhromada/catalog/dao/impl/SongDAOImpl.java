package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songDAO")
public class SongDAOImpl implements SongDAO {

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
	public Song getSong(final Integer id) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return entityManager.find(Song.class, id);
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if entity manager isn't set
	 *                                  or song's validator isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws DataStorageException     {@inheritDoc}
	 */
	@Override
	public void add(final Song song) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(song, "Song");

		try {
			entityManager.persist(song);
			song.setPosition(song.getId() - 1);
			entityManager.merge(song);
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
	public void update(final Song song) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(song, "Song");

		try {
			entityManager.merge(song);
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
	public void remove(final Song song) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(song, "Song");

		try {
			if (entityManager.contains(song)) {
				entityManager.remove(song);
			} else {
				entityManager.remove(entityManager.getReference(Song.class, song.getId()));
			}
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
	public List<Song> findSongsByMusic(final Music music) {
		Validators.validateFieldNotNull(entityManager, "Entity manager");
		Validators.validateArgumentNotNull(music, "Music");

		try {
			final TypedQuery<Song> query = entityManager.createNamedQuery(Song.FIND_BY_MUSIC, Song.class);
			query.setParameter("music", music.getId());
			return query.getResultList();
		} catch (final PersistenceException ex) {
			throw new DataStorageException("Error in working with ORM.", ex);
		}
	}

}