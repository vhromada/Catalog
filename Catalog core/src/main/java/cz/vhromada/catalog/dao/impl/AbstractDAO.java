package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.Movable;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;

/*
 * An abstract class represents DAO.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public abstract class AbstractDAO<T extends Movable> {

    /**
     * Message for {@link DataStorageException}
     */
    private static final String DATA_STORAGE_EXCEPTION_MESSAGE = "Error in working with ORM.";

    /**
     * Entity manager
     */
    private EntityManager entityManager;

    /**
     * Class of data
     */
    private Class<T> clazz;

    /**
     * Name
     */
    private String name;

    /**
     * Creates a new instance of AbstractDAO.
     *
     * @param entityManager entity manager
     * @param clazz         class of data
     * @param name          name
     * @throws IllegalArgumentException if entity manager is null
     */
    public AbstractDAO(final EntityManager entityManager, final Class<T> clazz, final String name) {
        Validators.validateArgumentNotNull(entityManager, "Entity manager");
        Validators.validateArgumentNotNull(clazz, "Class");
        Validators.validateArgumentNotNull(name, "Name");

        this.entityManager = entityManager;
        this.clazz = clazz;
        this.name = name;
    }

    /**
     * Returns list of data.
     *
     * @param queryName name of query
     * @return list of data
     * @throws DataStorageException if there was error with working with data storage
     */
    protected List<T> getData(final String queryName) {
        try {
            return new ArrayList<>(entityManager.createNamedQuery(queryName, clazz).getResultList());
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * Returns list of data by specified object.
     *
     * @param object         object
     * @param objectName     name of object
     * @param queryName      name of query
     * @param queryParamName name of query param
     * @return list of data
     * @throws IllegalArgumentException if object is null
     * @throws DataStorageException     if there was error with working with data storage
     */
    protected List<T> getData(final Movable object, final String objectName, final String queryName, final String queryParamName) {
        Validators.validateArgumentNotNull(object, objectName);

        try {
            final TypedQuery<T> query = entityManager.createNamedQuery(queryName, clazz);
            query.setParameter(queryParamName, object.getId());

            return query.getResultList();
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * Returns item with ID or null if there isn't such item.
     *
     * @param id ID
     * @return item with ID or null if there isn't such item
     * @throws IllegalArgumentException if ID is null
     * @throws DataStorageException     if there was error with working with data storage
     */
    protected T getItem(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        try {
            return entityManager.find(clazz, id);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * Adds item. Sets new ID and position.
     *
     * @param item item
     * @throws IllegalArgumentException if item is null
     * @throws DataStorageException     if there was error with working with data storage
     */
    protected void addItem(final T item) {
        Validators.validateArgumentNotNull(item, name);

        try {
            entityManager.persist(item);
            item.setPosition(item.getId() - 1);
            entityManager.merge(item);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * Updates item.
     *
     * @param item item
     * @throws IllegalArgumentException if item is null
     * @throws DataStorageException     if there was error with working with data storage
     */
    protected void updateItem(final T item) {
        Validators.validateArgumentNotNull(item, name);

        try {
            entityManager.merge(item);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * Removes item.
     *
     * @param item item
     * @throws IllegalArgumentException if item is null
     * @throws DataStorageException     if there was error with working with data storage
     */
    protected void removeItem(final T item) {
        Validators.validateArgumentNotNull(item, name);

        try {
            if (entityManager.contains(item)) {
                entityManager.remove(item);
            } else {
                entityManager.remove(entityManager.getReference(clazz, item.getId()));
            }
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
