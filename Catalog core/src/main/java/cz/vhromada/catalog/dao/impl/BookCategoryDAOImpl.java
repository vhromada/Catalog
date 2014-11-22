package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import cz.vhromada.catalog.dao.BookCategoryDAO;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for book categories.
 *
 * @author Vladimir Hromada
 */
@Component("bookCategoryDAO")
public class BookCategoryDAOImpl implements BookCategoryDAO {

    /** Entity manager field */
    private static final String ENTITY_MANAGER_FIELD = "Entity manager";

    /** Book category argument */
    private static final String BOOK_CATEGORY_ARGUMENT = "Book category";

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
    public List<BookCategory> getBookCategories() {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);

        try {
            return new ArrayList<>(entityManager.createNamedQuery(BookCategory.SELECT_BOOK_CATEGORIES, BookCategory.class).getResultList());
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
    public BookCategory getBookCategory(final Integer id) {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(BookCategory.class, id);
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
    public void add(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            entityManager.persist(bookCategory);
            bookCategory.setPosition(bookCategory.getId() - 1);
            entityManager.merge(bookCategory);
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
    public void update(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            entityManager.merge(bookCategory);
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
    public void remove(final BookCategory bookCategory) {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            if (entityManager.contains(bookCategory)) {
                entityManager.remove(bookCategory);
            } else {
                entityManager.remove(entityManager.getReference(BookCategory.class, bookCategory.getId()));
            }
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
