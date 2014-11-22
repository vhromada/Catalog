package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;

/**
 * An interface represents service for books.
 *
 * @author Vladimir Hromada
 */
public interface BookService {

    /**
     * Returns book with ID or null if there isn't such book.
     *
     * @param id ID
     * @return book with ID or null if there isn't such book
     * @throws IllegalArgumentException if ID is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    Book getBook(Integer id);

    /**
     * Adds book. Sets new ID and position.
     *
     * @param book book
     * @throws IllegalArgumentException if book is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void add(Book book);

    /**
     * Updates book.
     *
     * @param book new value of book
     * @throws IllegalArgumentException if book is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void update(Book book);

    /**
     * Removes book.
     *
     * @param book book
     * @throws IllegalArgumentException if book is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void remove(Book book);

    /**
     * Duplicates book.
     *
     * @param book book
     * @throws IllegalArgumentException if book is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void duplicate(Book book);

    /**
     * Moves book in list one position up.
     *
     * @param book book
     * @throws IllegalArgumentException if book is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void moveUp(Book book);

    /**
     * Moves book in list one position down.
     *
     * @param book book
     * @throws IllegalArgumentException if book is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    void moveDown(Book book);

    /**
     * Returns true if book exists.
     *
     * @param book book
     * @return true if book exists
     * @throws IllegalArgumentException if book is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    boolean exists(Book book);

    /**
     * Returns list of books for specified book category.
     *
     * @param bookCategory book category
     * @return list of books for specified book category
     * @throws IllegalArgumentException if book category is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException
     *                                  if there was error in working with DAO tier
     */
    List<Book> findBooksByBookCategory(BookCategory bookCategory);

}
