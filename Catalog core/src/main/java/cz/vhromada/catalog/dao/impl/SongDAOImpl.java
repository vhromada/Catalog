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

    /** Entity manager argument */
    private static final String ENTITY_MANAGER_ARGUMENT = "Entity manager";

    /** Music argument */
    private static final String MUSIC_ARGUMENT = "Music";

    /** Song argument */
    private static final String SONG_ARGUMENT = "Song";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link DataStorageException} */
    private static final String DATA_STORAGE_EXCEPTION_MESSAGE = "Error in working with ORM.";

    /** Entity manager */
    private EntityManager entityManager;

    /**
     * Creates a new instance of SongDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public SongDAOImpl(final EntityManager entityManager) {
        Validators.validateArgumentNotNull(entityManager, ENTITY_MANAGER_ARGUMENT);

        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Song getSong(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(Song.class, id);
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
    public void add(final Song song) {
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            entityManager.persist(song);
            song.setPosition(song.getId() - 1);
            entityManager.merge(song);
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
    public void update(final Song song) {
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            entityManager.merge(song);
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
    public void remove(final Song song) {
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            if (entityManager.contains(song)) {
                entityManager.remove(song);
            } else {
                entityManager.remove(entityManager.getReference(Song.class, song.getId()));
            }
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
    public List<Song> findSongsByMusic(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            final TypedQuery<Song> query = entityManager.createNamedQuery(Song.FIND_BY_MUSIC, Song.class);
            query.setParameter("music", music.getId());
            return query.getResultList();
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
