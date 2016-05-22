package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.ShowDAO;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showDAO")
public class ShowDAOImpl extends AbstractDAO<Show> implements ShowDAO {

    /**
     * Creates a new instance of ShowDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public ShowDAOImpl(final EntityManager entityManager) {
        super(entityManager, Show.class, "Show");
    }

    /**
     * @throws DataStorageException {@inheritDoc}
     */
    @Override
    public List<Show> getShows() {
        return getData(Show.SELECT_SHOWS);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Show getShow(final Integer id) {
        return getItem(id);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Show show) {
        addItem(show);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Show show) {
        updateItem(show);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Show show) {
        removeItem(show);
    }

}
