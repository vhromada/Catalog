package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.dao.entities.BookCategory;

/**
 * An interface represents service for book categories.
 *
 * @author Vladimir Hromada
 */
public interface BookCategoryService {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void newData();

    /**
     * Returns list of book categories.
     *
     * @return list of book categories
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    List<BookCategory> getBookCategories();

    /**
     * Returns book category with ID or null if there isn't such book category.
     *
     * @param id ID
     * @return book category with ID or null if there isn't such book category
     * @throws IllegalArgumentException                                         if ID is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    BookCategory getBookCategory(Integer id);

    /**
     * Adds book category. Sets new ID and position.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException                                         if book category is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void add(BookCategory bookCategory);

    /**
     * Updates book category.
     *
     * @param bookCategory new value of book category
     * @throws IllegalArgumentException                                         if book category is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void update(BookCategory bookCategory);

    /**
     * Removes book category.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException                                         if book category is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void remove(BookCategory bookCategory);

    /**
     * Duplicates book category.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException                                         if book category is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void duplicate(BookCategory bookCategory);

    /**
     * Moves book category in list one position up.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException                                         if book category is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void moveUp(BookCategory bookCategory);

    /**
     * Moves book category in list one position down.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException                                         if book category is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void moveDown(BookCategory bookCategory);

    /**
     * Returns true if book category exists.
     *
     * @param bookCategory book category
     * @return true if book category exists
     * @throws IllegalArgumentException                                         if book category is null
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    boolean exists(BookCategory bookCategory);

    /**
     * Updates positions.
     *
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    void updatePositions();

    /**
     * Returns count of books from all book categories.
     *
     * @return count of books from all book categories
     * @throws cz.vhromada.catalog.service.exceptions.ServiceOperationException if there was error in working with DAO tier
     */
    int getBooksCount();

}
