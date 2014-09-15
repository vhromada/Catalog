package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.BOOKS_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.BOOK_CATEGORIES_COUNT;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringToUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.BookCategoryFacade;
import cz.vhromada.catalog.facade.impl.BookCategoryFacadeImpl;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A class represents test for class {@link BookCategoryFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class BookCategoryFacadeImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link PlatformTransactionManager} */
	@Autowired
	private PlatformTransactionManager transactionManager;

	/** Instance of {@link BookCategoryFacade} */
	@Autowired
	private BookCategoryFacade bookCategoryFacade;

	/** Initializes database. */
	@Before
	public void setUp() {
		SpringUtils.remove(transactionManager, entityManager, Book.class);
		SpringUtils.remove(transactionManager, entityManager, BookCategory.class);
		SpringUtils.updateSequence(transactionManager, entityManager, "book_categories_sq");
		SpringUtils.updateSequence(transactionManager, entityManager, "books_sq");
		for (BookCategory bookCategory : SpringEntitiesUtils.getBookCategories()) {
			bookCategory.setId(null);
			SpringUtils.persist(transactionManager, entityManager, bookCategory);
		}
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			for (Book book : SpringEntitiesUtils.getBooks(i)) {
				book.setId(null);
				SpringUtils.persist(transactionManager, entityManager, book);
			}
		}
	}

	/** Test method for {@link BookCategoryFacade#newData()}. */
	@Test
	public void testNewData() {
		bookCategoryFacade.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#getBookCategories()}. */
	@Test
	public void testGetBookCategories() {
		DeepAsserts.assertEquals(SpringToUtils.getBookCategories(), bookCategoryFacade.getBookCategories());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)}. */
	@Test
	public void testGetBookCategory() {
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getBookCategory(i), bookCategoryFacade.getBookCategory(i));
		}

		assertNull(bookCategoryFacade.getBookCategory(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetBookCategoryWithNullArgument() {
		bookCategoryFacade.getBookCategory(null);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)}. */
	@Test
	public void testAdd() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		final BookCategory expectedBookCategory = EntityGenerator.createBookCategory(BOOK_CATEGORIES_COUNT + 1);
		expectedBookCategory.setPosition(BOOK_CATEGORIES_COUNT);

		bookCategoryFacade.add(bookCategory);

		DeepAsserts.assertNotNull(bookCategory.getId());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, bookCategory.getId());
		final BookCategory addedBookCategory = SpringUtils.getBookCategory(entityManager, BOOK_CATEGORIES_COUNT + 1);
		DeepAsserts.assertEquals(bookCategory, addedBookCategory, "booksCount");
		DeepAsserts.assertEquals(expectedBookCategory, addedBookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		bookCategoryFacade.add(null);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with book category with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithBookCategoryWithNotNullId() {
		bookCategoryFacade.add(ToGenerator.createBookCategory(Integer.MAX_VALUE));
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with book category with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithBookCategoryWithNullName() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setName(null);

		bookCategoryFacade.add(bookCategory);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with book category with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithBookCategoryWithEmptyName() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setName("");

		bookCategoryFacade.add(bookCategory);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with book category with negative count of books. */
	@Test(expected = ValidationException.class)
	public void testAddWithBookCategoryWithNotNegativeBooksCount() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setBooksCount(-1);

		bookCategoryFacade.add(bookCategory);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with book category with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithBookCategoryWithNullNote() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		bookCategory.setNote(null);

		bookCategoryFacade.add(bookCategory);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)}. */
	@Test
	public void testUpdate() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(1);
		final BookCategory expectedBookCategory = EntityGenerator.createBookCategory(1);

		bookCategoryFacade.update(bookCategory);

		final BookCategory updatedBookCategory = SpringUtils.getBookCategory(entityManager, 1);
		DeepAsserts.assertEquals(bookCategory, updatedBookCategory, "booksCount");
		DeepAsserts.assertEquals(expectedBookCategory, updatedBookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		bookCategoryFacade.update(null);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBookCategoryWithNullId() {
		bookCategoryFacade.update(ToGenerator.createBookCategory());
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with book category with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBookCategoryWithNullName() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(PRIMARY_ID);
		bookCategory.setName(null);

		bookCategoryFacade.update(bookCategory);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with book category with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBookCategoryWithEmptyName() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(PRIMARY_ID);
		bookCategory.setName(null);

		bookCategoryFacade.update(bookCategory);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with book category with negative count of books. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBookCategoryWithNotNegativeBooksCount() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(PRIMARY_ID);
		bookCategory.setBooksCount(-1);

		bookCategoryFacade.update(bookCategory);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with book category with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithBookCategoryWithNullNote() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(PRIMARY_ID);
		bookCategory.setNote(null);

		bookCategoryFacade.update(bookCategory);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with book category with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithBookCategoryWithBadId() {
		bookCategoryFacade.update(ToGenerator.createBookCategory(Integer.MAX_VALUE));
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)}. */
	@Test
	public void testRemove() {
		bookCategoryFacade.remove(ToGenerator.createBookCategory(1));

		assertNull(SpringUtils.getBookCategory(entityManager, 1));
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT - 1, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		bookCategoryFacade.remove(null);
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithBookCategoryWithNullId() {
		bookCategoryFacade.remove(ToGenerator.createBookCategory());
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with book category with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithBookCategoryWithBadId() {
		bookCategoryFacade.remove(ToGenerator.createBookCategory(Integer.MAX_VALUE));
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)}. */
	@Test
	public void testDuplicate() {
		final BookCategory bookCategory = SpringEntitiesUtils.getBookCategory(BOOK_CATEGORIES_COUNT);
		bookCategory.setId(BOOK_CATEGORIES_COUNT + 1);

		bookCategoryFacade.duplicate(ToGenerator.createBookCategory(BOOK_CATEGORIES_COUNT));

		final BookCategory duplicatedBookCategory = SpringUtils.getBookCategory(entityManager, BOOK_CATEGORIES_COUNT + 1);
		DeepAsserts.assertEquals(bookCategory, duplicatedBookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		bookCategoryFacade.duplicate(null);
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithBookCategoryWithNullId() {
		bookCategoryFacade.duplicate(ToGenerator.createBookCategory());
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with book category with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithBookCategoryWithBadId() {
		bookCategoryFacade.duplicate(ToGenerator.createBookCategory(Integer.MAX_VALUE));
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)}. */
	@Test
	public void testMoveUp() {
		final BookCategory bookCategory1 = SpringEntitiesUtils.getBookCategory(1);
		bookCategory1.setPosition(1);
		final BookCategory bookCategory2 = SpringEntitiesUtils.getBookCategory(2);
		bookCategory2.setPosition(0);

		bookCategoryFacade.moveUp(ToGenerator.createBookCategory(2));
		DeepAsserts.assertEquals(bookCategory1, SpringUtils.getBookCategory(entityManager, 1));
		DeepAsserts.assertEquals(bookCategory2, SpringUtils.getBookCategory(entityManager, 2));
		for (int i = 3; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		bookCategoryFacade.moveUp(null);
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithBookCategoryWithNullId() {
		bookCategoryFacade.moveUp(ToGenerator.createBookCategory());
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		bookCategoryFacade.moveUp(ToGenerator.createBookCategory(1));
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		bookCategoryFacade.moveUp(ToGenerator.createBookCategory(Integer.MAX_VALUE));
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)}. */
	@Test
	public void testMoveDown() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(1);
		final BookCategory bookCategory1 = SpringEntitiesUtils.getBookCategory(1);
		bookCategory1.setPosition(1);
		final BookCategory bookCategory2 = SpringEntitiesUtils.getBookCategory(2);
		bookCategory2.setPosition(0);

		bookCategoryFacade.moveDown(bookCategory);
		DeepAsserts.assertEquals(bookCategory1, SpringUtils.getBookCategory(entityManager, 1));
		DeepAsserts.assertEquals(bookCategory2, SpringUtils.getBookCategory(entityManager, 2));
		for (int i = 3; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		bookCategoryFacade.moveDown(null);
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithBookCategoryWithNullId() {
		bookCategoryFacade.moveDown(ToGenerator.createBookCategory());
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		bookCategoryFacade.moveDown(ToGenerator.createBookCategory(BOOK_CATEGORIES_COUNT));
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		bookCategoryFacade.moveDown(ToGenerator.createBookCategory(Integer.MAX_VALUE));
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with existing bookCategory. */
	@Test
	public void testExists() {
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			assertTrue(bookCategoryFacade.exists(ToGenerator.createBookCategory(i)));
		}

		assertFalse(bookCategoryFacade.exists(ToGenerator.createBookCategory(Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		bookCategoryFacade.exists(null);
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with book category with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithBookCategoryWithNullId() {
		bookCategoryFacade.exists(ToGenerator.createBookCategory());
	}

	/** Test method for {@link BookCategoryFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		bookCategoryFacade.updatePositions();

		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryFacade#getBooksCount()}. */
	@Test
	public void testGetBooksCount() {
		DeepAsserts.assertEquals(BOOKS_COUNT, bookCategoryFacade.getBooksCount());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

}
