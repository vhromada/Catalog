package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.dao.BookDAO;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for books.
 *
 * @author Vladimir Hromada
 */
@Component("bookDAO")
public class BookDAOImpl implements BookDAO {

    /**
     * Entity manager argument
     */
    private static final String ENTITY_MANAGER_ARGUMENT = "Entity manager";

    /**
     * Book category argument
     */
    private static final String BOOK_CATEGORY_ARGUMENT = "Book category";

    /**
     * Book argument
     */
    private static final String BOOK_ARGUMENT = "Book";

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
     * Creates a new instance of BookDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public BookDAOImpl(final EntityManager entityManager) {
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
    public Book getBook(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(Book.class, id);
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
    public void add(final Book book) {
        Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

        try {
            entityManager.persist(book);
            book.setPosition(book.getId() - 1);
            entityManager.merge(book);
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
    public void update(final Book book) {
        Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

        try {
            entityManager.merge(book);
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
    public void remove(final Book book) {
        Validators.validateArgumentNotNull(book, BOOK_ARGUMENT);

        try {
            if (entityManager.contains(book)) {
                entityManager.remove(book);
            } else {
                entityManager.remove(entityManager.getReference(Book.class, book.getId()));
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
    public List<Book> findBooksByBookCategory(final BookCategory bookCategory) {
        Validators.validateArgumentNotNull(bookCategory, BOOK_CATEGORY_ARGUMENT);

        try {
            final TypedQuery<Book> query = entityManager.createNamedQuery(Book.FIND_BY_BOOK_CATEGORY, Book.class);
            query.setParameter("bookCategory", bookCategory.getId());
            return query.getResultList();
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
