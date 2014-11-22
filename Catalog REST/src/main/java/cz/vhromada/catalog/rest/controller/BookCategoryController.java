package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.BookCategoryFacade;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for book categories.
 *
 * @author Vladimir Hromada
 */
@Controller("bookCategoryController")
@RequestMapping("/bookCategories")
public class BookCategoryController extends JsonController {

    @Autowired
    @Qualifier("bookCategoryFacade")
    private BookCategoryFacade bookCategoryFacade;

    /** Creates new data. */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    public void newData() {
        bookCategoryFacade.newData();
    }

    /**
     * Returns list of book categories.
     *
     * @return list of book categories
     */
    @RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
    @ResponseBody
    public String getBookCategories() {
        return serialize(bookCategoryFacade.getBookCategories());
    }

    /**
     * Returns book category with ID or null if there isn't such book category.
     *
     * @param id ID
     * @return book category with ID or null if there isn't such book category
     * @throws IllegalArgumentException if ID is null
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getBookCategory(@PathVariable("id") final Integer id) {
        return serialize(bookCategoryFacade.getBookCategory(id));
    }

    /**
     * Adds book category. Sets new ID and position.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException if book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID isn't null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or count of books is negative number
     *                                  or note is null
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void add(final String bookCategory) {
        bookCategoryFacade.add(deserialize(bookCategory, BookCategoryTO.class));
    }

    /**
     * Updates book category.
     *
     * @param bookCategory new value of book category
     * @throws IllegalArgumentException if book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or count of books is negative number
     *                                  or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if book category doesn't exist in data storage
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(final String bookCategory) {
        bookCategoryFacade.update(deserialize(bookCategory, BookCategoryTO.class));
    }

    /**
     * Removes book category.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException if book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if book category doesn't exist in data storage
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public void remove(final String bookCategory) {
        bookCategoryFacade.remove(deserialize(bookCategory, BookCategoryTO.class));
    }

    /**
     * Duplicates book category.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException if book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if book category doesn't exist in data storage
     */
    @RequestMapping(value = "/duplicate", method = RequestMethod.POST)
    @ResponseBody
    public void duplicate(final String bookCategory) {
        bookCategoryFacade.duplicate(deserialize(bookCategory, BookCategoryTO.class));
    }

    /**
     * Moves book category in list one position up.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException if book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or book category can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if book category doesn't exist in data storage
     */
    @RequestMapping(value = "/moveUp", method = RequestMethod.POST)
    @ResponseBody
    public void moveUp(final String bookCategory) {
        bookCategoryFacade.moveUp(deserialize(bookCategory, BookCategoryTO.class));
    }

    /**
     * Moves book category in list one position down.
     *
     * @param bookCategory book category
     * @throws IllegalArgumentException if book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     *                                  or book category can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  if book category doesn't exist in data storage
     */
    @RequestMapping(value = "/moveDown", method = RequestMethod.POST)
    @ResponseBody
    public void moveDown(final String bookCategory) {
        bookCategoryFacade.moveDown(deserialize(bookCategory, BookCategoryTO.class));
    }

    /**
     * Returns true if book category exists.
     *
     * @param bookCategory book category
     * @return true if book category exists
     * @throws IllegalArgumentException if book category is null
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  if ID is null
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public String exists(final String bookCategory) {
        return serialize(bookCategoryFacade.exists(deserialize(bookCategory, BookCategoryTO.class)));
    }

    /** Updates positions. */
    @RequestMapping(value = "/updatePositions", method = RequestMethod.GET)
    @ResponseBody
    public void updatePositions() {
        bookCategoryFacade.updatePositions();
    }

    /**
     * Returns count of books from all book categories.
     *
     * @return count of books from all book categories
     */
    @RequestMapping(value = "/booksCount", method = RequestMethod.GET)
    @ResponseBody
    public String getBooksCount() {
        return serialize(bookCategoryFacade.getBooksCount());
    }

}
