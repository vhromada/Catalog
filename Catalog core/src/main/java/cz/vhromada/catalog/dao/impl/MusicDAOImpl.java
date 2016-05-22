package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicDAO")
public class MusicDAOImpl extends AbstractDAO<Music> implements MusicDAO {

    /**
     * Creates a new instance of MusicDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public MusicDAOImpl(final EntityManager entityManager) {
        super(entityManager, Music.class, "Music");
    }

    /**
     * @throws DataStorageException {@inheritDoc}
     */
    @Override
    public List<Music> getMusic() {
        return getData(Music.SELECT_MUSIC);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Music getMusic(final Integer id) {
        return getItem(id);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Music music) {
        addItem(music);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Music music) {
        updateItem(music);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Music music) {
        removeItem(music);
    }

}
