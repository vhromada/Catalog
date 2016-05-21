package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.GenreDAO;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreDAO")
public class GenreDAOImpl extends AbstractDAO<Genre> implements GenreDAO {

    /**
     * Creates a new instance of GenreDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public GenreDAOImpl(final EntityManager entityManager) {
        super(entityManager, Genre.class, "Genre");
    }

    /**
     * @throws DataStorageException {@inheritDoc}
     */
    @Override
    public List<Genre> getGenres() {
        return getData(Genre.SELECT_GENRES);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Genre getGenre(final Integer id) {
        return getItem(id);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Genre genre) {
        addItem(genre);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Genre genre) {
        updateItem(genre);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Genre genre) {
        removeItem(genre);
    }

}
