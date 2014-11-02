package cz.vhromada.catalog.rest.controller;

import cz.vhromada.catalog.facade.BookFacade;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class represents controller for books.
 *
 * @author Vladimir Hromada
 */
@Controller("bookController")
@RequestMapping("/bookCategories/{bookCategoryId}/books")
public class BookController extends JsonController {

	@Autowired
	@Qualifier("bookFacade")
	private BookFacade bookFacade;

	/**
	 * Returns book with ID or null if there isn't such book.
	 *
	 * @param bookCategoryId book category ID
	 * @param bookId         book ID
	 * @return book with ID or null if there isn't such book
	 * @throws IllegalArgumentException if ID is null
	 */
	@RequestMapping(value = "/{bookId}", method = RequestMethod.GET)
	@ResponseBody
	public String getBook(@PathVariable("bookCategoryId") @SuppressWarnings("unused") final Integer bookCategoryId,
			@PathVariable("bookId") final Integer bookId) {
		return serialize(bookFacade.getBook(bookId));
	}

	/**
	 * Adds book. Sets new ID and position.
	 *
	 * @param bookCategoryId book category ID
	 * @param book           book
	 * @throws IllegalArgumentException if book is null
	 * @throws ValidationException      if ID isn't null
	 *                                  or author is null
	 *                                  or author is empty string
	 *                                  or title is null
	 *                                  or title is empty string
	 *                                  or languages are null
	 *                                  or languages contain null value
	 *                                  or category is null
	 *                                  or category is empty string
	 *                                  or note is null
	 *                                  or book category is null
	 *                                  or book category ID is null
	 * @throws RecordNotFoundException  if book category doesn't exist in data storage
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public void add(@PathVariable("bookCategoryId") @SuppressWarnings("unused") final Integer bookCategoryId, final String book) {
		bookFacade.add(deserialize(book, BookTO.class));
	}

	/**
	 * Updates book.
	 *
	 * @param bookCategoryId book category ID
	 * @param book           new value of book
	 * @throws IllegalArgumentException if book is null
	 * @throws ValidationException      if ID is null
	 *                                  or author is null
	 *                                  or author is empty string
	 *                                  or title is null
	 *                                  or title is empty string
	 *                                  or languages are null
	 *                                  or languages contain null value
	 *                                  or category is null
	 *                                  or category is empty string
	 *                                  or note is null
	 *                                  or book category is null
	 *                                  or book category ID is null
	 * @throws RecordNotFoundException  if book doesn't exist in data storage
	 *                                  or book category doesn't exist in data storage
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public void update(@PathVariable("bookCategoryId") @SuppressWarnings("unused") final Integer bookCategoryId, final String book) {
		bookFacade.update(deserialize(book, BookTO.class));
	}

	/**
	 * Removes book.
	 *
	 * @param bookCategoryId book category ID
	 * @param book           book
	 * @throws IllegalArgumentException if book is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if book doesn't exist in data storage
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public void remove(@PathVariable("bookCategoryId") @SuppressWarnings("unused") final Integer bookCategoryId, final String book) {
		bookFacade.remove(deserialize(book, BookTO.class));
	}

	/**
	 * Duplicates book.
	 *
	 * @param bookCategoryId book category ID
	 * @param book           book
	 * @throws IllegalArgumentException if book is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if book doesn't exist in data storage
	 */
	@RequestMapping(value = "/duplicate", method = RequestMethod.POST)
	@ResponseBody
	public void duplicate(@PathVariable("bookCategoryId") @SuppressWarnings("unused") final Integer bookCategoryId, final String book) {
		bookFacade.duplicate(deserialize(book, BookTO.class));
	}

	/**
	 * Moves book in list one position up.
	 *
	 * @param bookCategoryId book category ID
	 * @param book           book
	 * @throws IllegalArgumentException if book is null
	 * @throws ValidationException      if ID is null
	 *                                  or book can't be moved up
	 * @throws RecordNotFoundException  if book doesn't exist in data storage
	 */
	@RequestMapping(value = "/moveUp", method = RequestMethod.POST)
	@ResponseBody
	public void moveUp(@PathVariable("bookCategoryId") @SuppressWarnings("unused") final Integer bookCategoryId, final String book) {
		bookFacade.moveUp(deserialize(book, BookTO.class));
	}

	/**
	 * Moves book in list one position down.
	 *
	 * @param bookCategoryId book category ID
	 * @param book           book
	 * @throws IllegalArgumentException if book is null
	 * @throws ValidationException      if ID is null
	 *                                  or book can't be moved down
	 * @throws RecordNotFoundException  if book doesn't exist in data storage
	 */
	@RequestMapping(value = "/moveDown", method = RequestMethod.POST)
	@ResponseBody
	public void moveDown(@PathVariable("bookCategoryId") @SuppressWarnings("unused") final Integer bookCategoryId, final String book) {
		bookFacade.moveDown(deserialize(book, BookTO.class));
	}

	/**
	 * Returns true if book exists.
	 *
	 * @param bookCategoryId book category ID
	 * @param book           book
	 * @return true if book exists
	 * @throws IllegalArgumentException if book is null
	 * @throws ValidationException      if ID is null
	 */
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public String exists(@PathVariable("bookCategoryId") @SuppressWarnings("unused") final Integer bookCategoryId, final String book) {
		return serialize(bookFacade.exists(deserialize(book, BookTO.class)));
	}

	/**
	 * Returns list of books for specified book category.
	 *
	 * @param bookCategoryId book category ID
	 * @return list of books for specified book category
	 * @throws IllegalArgumentException if book category is null
	 * @throws ValidationException      if ID is null
	 * @throws RecordNotFoundException  if book category doesn't exist in data storage
	 */
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	@ResponseBody
	public String findBooksByBookCategory(@PathVariable("bookCategoryId") final Integer bookCategoryId) {
		final BookCategoryTO bookCategory = new BookCategoryTO();
		bookCategory.setId(bookCategoryId);

		return serialize(bookFacade.findBooksByBookCategory(bookCategory));
	}

}
