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

    /**
     * Entity manager argument
     */
    private static final String ENTITY_MANAGER_ARGUMENT = "Entity manager";

    /**
     * Music argument
     */
    private static final String MUSIC_ARGUMENT = "Music";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link DataStorageException}
     */
    private static final String DATA_STORAGE_EXCEPTION_MESSAGE = "Error in working with ORM.";

    /**
     * Entity manager
     */
    private EntityManager entityManager;

    /**
     * Creates a new instance of MusicDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public MusicDAOImpl(final EntityManager entityManager) {
        Validators.validateArgumentNotNull(entityManager, ENTITY_MANAGER_ARGUMENT);

        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @throws DataStorageException {@inheritDoc}
     */
    @Override
    public List<Music> getMusic() {
        try {
            return new ArrayList<>(entityManager.createNamedQuery(Music.SELECT_MUSIC, Music.class).getResultList());
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
    public Music getMusic(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(Music.class, id);
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
    public void add(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            entityManager.persist(music);
            music.setPosition(music.getId() - 1);
            entityManager.merge(music);
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
    public void update(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            entityManager.merge(music);
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
    public void remove(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            if (entityManager.contains(music)) {
                entityManager.remove(music);
            } else {
                entityManager.remove(entityManager.getReference(Music.class, music.getId()));
            }
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
