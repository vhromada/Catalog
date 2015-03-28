package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import cz.vhromada.catalog.dao.ShowDAO;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showDAO")
public class ShowDAOImpl implements ShowDAO {

    /**
     * Entity manager argument
     */
    private static final String ENTITY_MANAGER_ARGUMENT = "Entity manager";

    /**
     * Show argument
     */
    private static final String SHOW_ARGUMENT = "Show";

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
     * Creates a new instance of ShowDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public ShowDAOImpl(final EntityManager entityManager) {
        Validators.validateArgumentNotNull(entityManager, ENTITY_MANAGER_ARGUMENT);

        this.entityManager = entityManager;
    }

    /**
     * @throws DataStorageException {@inheritDoc}
     */
    @Override
    public List<Show> getShows() {
        try {
            return new ArrayList<>(entityManager.createNamedQuery(Show.SELECT_SHOWS, Show.class).getResultList());
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Show getShow(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(Show.class, id);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            entityManager.persist(show);
            show.setPosition(show.getId() - 1);
            entityManager.merge(show);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            entityManager.merge(show);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            if (entityManager.contains(show)) {
                entityManager.remove(show);
            } else {
                entityManager.remove(entityManager.getReference(Show.class, show.getId()));
            }
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
