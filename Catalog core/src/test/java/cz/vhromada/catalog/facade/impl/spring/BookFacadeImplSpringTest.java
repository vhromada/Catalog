package cz.vhromada.catalog.facade.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.BookFacade;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.generator.ObjectGenerator;
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
 * A class represents test for class {@link cz.vhromada.catalog.facade.impl.BookFacadeImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class BookFacadeImplSpringTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link PlatformTransactionManager}
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * Instance of (@link BookFacade}
     */
    @Autowired
    private BookFacade bookFacade;

    /**
     * Instance of {@link ObjectGenerator}
     */
    @Autowired
    private ObjectGenerator objectGenerator;

    /**
     * Initializes database.
     */
    @Before
    public void setUp() {
        SpringUtils.remove(transactionManager, entityManager, Book.class);
        SpringUtils.remove(transactionManager, entityManager, BookCategory.class);
        SpringUtils.updateSequence(transactionManager, entityManager, "book_categories_sq");
        SpringUtils.updateSequence(transactionManager, entityManager, "books_sq");
        for (final BookCategory bookCategory : SpringEntitiesUtils.getBookCategories()) {
            bookCategory.setId(null);
            SpringUtils.persist(transactionManager, entityManager, bookCategory);
        }
        for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
            for (final Book book : SpringEntitiesUtils.getBooks(i)) {
                book.setId(null);
                SpringUtils.persist(transactionManager, entityManager, book);
            }
        }
    }

    /**
     * Test method for {@link BookFacade#getBook(Integer)}.
     */
    @Test
    public void testGetBook() {
        for (int i = 0; i < SpringUtils.BOOKS_COUNT; i++) {
            final int bookCategoryNumber = i / SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
            final int bookNumber = i % SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
            DeepAsserts.assertEquals(SpringToUtils.getBook(bookCategoryNumber, bookNumber), bookFacade.getBook(i + 1), "booksCount");
        }

        assertNull(bookFacade.getBook(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#getBook(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetBookWithNullArgument() {
        bookFacade.getBook(null);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)}.
     */
    @Test
    public void testAdd() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);

        bookFacade.add(book);

        DeepAsserts.assertNotNull(book.getId());
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT + 1, book.getId());
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, book.getPosition());
        final Book addedBook = SpringUtils.getBook(entityManager, SpringUtils.BOOKS_COUNT + 1);
        DeepAsserts.assertEquals(book, addedBook, "booksCount");
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNullArgument() {
        bookFacade.add(null);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithNotNullId() {
        bookFacade.add(SpringToUtils.newBookWithId(objectGenerator));
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with null author.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithNullAuthor() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.setAuthor(null);

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with empty string as author.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithEmptyAuthor() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.setAuthor("");

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with null title.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithNullTitle() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.setTitle(null);

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with empty string as title.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithEmptyTitle() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.setTitle("");

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with null languages.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithNullLanguages() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.setLanguages(null);

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with languages with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithBadLanguages() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.setLanguages(CollectionUtils.newList(objectGenerator.generate(Language.class), null));

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithNullNote() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.setNote(null);

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with null TO for book category.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithNullBookCategory() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.setBookCategory(null);

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with TO for book category with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAddWithBookWithBookCategoryWithNullId() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.getBookCategory().setId(null);

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#add(BookTO)} with book with not existing book category.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAddWithBookWithNotExistingBookCategory() {
        final BookTO book = SpringToUtils.newBook(objectGenerator);
        book.getBookCategory().setId(Integer.MAX_VALUE);

        bookFacade.add(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)}.
     */
    @Test
    public void testUpdate() {
        final BookTO book = SpringToUtils.newBook(objectGenerator, 1);

        bookFacade.update(book);

        final Book updatedBook = SpringUtils.getBook(entityManager, 1);
        DeepAsserts.assertEquals(book, updatedBook, "booksCount");
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullArgument() {
        bookFacade.update(null);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithNullId() {
        bookFacade.update(SpringToUtils.newBook(objectGenerator));
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with null author.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithNullAuthor() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.setAuthor(null);

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with empty string as author.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithEmptyAuthor() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.setAuthor("");

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with null title.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithNullTitle() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.setTitle(null);

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with empty string as title.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithEmptyTitle() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.setTitle("");

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with null languages.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithNullLanguages() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.setLanguages(null);

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with languages with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithBadLanguages() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.setLanguages(CollectionUtils.newList(objectGenerator.generate(Language.class), null));

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithNullNote() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.setNote(null);

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with null TO for book category.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithNullBookCategory() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.setBookCategory(null);

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with book with TO for book category with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateWithBookWithBookCategoryWithNullId() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.getBookCategory().setId(null);

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithBadId() {
        bookFacade.update(SpringToUtils.newBook(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link BookFacade#update(BookTO)} with not existing book category.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithNotExistingBookCategory() {
        final BookTO book = SpringToUtils.newBookWithId(objectGenerator);
        book.getBookCategory().setId(Integer.MAX_VALUE);

        bookFacade.update(book);
    }

    /**
     * Test method for {@link BookFacade#remove(BookTO)}.
     */
    @Test
    public void testRemove() {
        bookFacade.remove(SpringToUtils.newBook(objectGenerator, 1));

        assertNull(SpringUtils.getBook(entityManager, 1));
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT - 1, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#remove(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullArgument() {
        bookFacade.remove(null);
    }

    /**
     * Test method for {@link BookFacade#remove(BookTO)} with book with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemoveWithBookWithNullId() {
        bookFacade.remove(SpringToUtils.newBook(objectGenerator));
    }

    /**
     * Test method for {@link BookFacade#remove(BookTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemoveWithBadId() {
        bookFacade.remove(SpringToUtils.newBook(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link BookFacade#duplicate(BookTO)}.
     */
    @Test
    public void testDuplicate() {
        final Book book = SpringEntitiesUtils.getBook(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT);
        book.setId(SpringUtils.BOOKS_COUNT + 1);

        bookFacade.duplicate(SpringToUtils.newBook(objectGenerator, SpringUtils.BOOKS_COUNT));

        final Book duplicatedBook = SpringUtils.getBook(entityManager, SpringUtils.BOOKS_COUNT + 1);
        DeepAsserts.assertEquals(book, duplicatedBook);
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#duplicate(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateWithNullArgument() {
        bookFacade.duplicate(null);
    }

    /**
     * Test method for {@link BookFacade#duplicate(BookTO)} with book with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicateWithBookWithNullId() {
        bookFacade.duplicate(SpringToUtils.newBook(objectGenerator));
    }

    /**
     * Test method for {@link BookFacade#duplicate(BookTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicateWithBadId() {
        bookFacade.duplicate(SpringToUtils.newBook(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link BookFacade#moveUp(BookTO)}.
     */
    @Test
    public void testMoveUp() {
        final Book book1 = SpringEntitiesUtils.getBook(1, 1);
        book1.setPosition(1);
        final Book book2 = SpringEntitiesUtils.getBook(1, 2);
        book2.setPosition(0);

        bookFacade.moveUp(SpringToUtils.newBook(objectGenerator, 2));
        DeepAsserts.assertEquals(book1, SpringUtils.getBook(entityManager, 1));
        DeepAsserts.assertEquals(book2, SpringUtils.getBook(entityManager, 2));
        for (int i = 2; i < SpringUtils.BOOKS_COUNT; i++) {
            final int bookCategoryNumber = i / SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
            final int bookNumber = i % SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber), SpringUtils.getBook(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#moveUp(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUpWithNullArgument() {
        bookFacade.moveUp(null);
    }

    /**
     * Test method for {@link BookFacade#moveUp(BookTO)} with book with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithBookWithNullId() {
        bookFacade.moveUp(SpringToUtils.newBook(objectGenerator));
    }

    /**
     * Test method for {@link BookFacade#moveUp(BookTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUpWithNotMovableArgument() {
        bookFacade.moveUp(SpringToUtils.newBook(objectGenerator, 1));
    }

    /**
     * Test method for {@link BookFacade#moveUp(BookTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUpWithBadId() {
        bookFacade.moveUp(SpringToUtils.newBook(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link BookFacade#moveDown(BookTO)}.
     */
    @Test
    public void testMoveDown() {
        final Book book1 = SpringEntitiesUtils.getBook(1, 1);
        book1.setPosition(1);
        final Book book2 = SpringEntitiesUtils.getBook(1, 2);
        book2.setPosition(0);

        bookFacade.moveDown(SpringToUtils.newBook(objectGenerator, 1));
        DeepAsserts.assertEquals(book1, SpringUtils.getBook(entityManager, 1));
        DeepAsserts.assertEquals(book2, SpringUtils.getBook(entityManager, 2));
        for (int i = 2; i < SpringUtils.BOOKS_COUNT; i++) {
            final int bookCategoryNumber = i / SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
            final int bookNumber = i % SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber), SpringUtils.getBook(entityManager, i + 1));
        }
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#moveDown(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDownWithNullArgument() {
        bookFacade.moveDown(null);
    }

    /**
     * Test method for {@link BookFacade#moveDown(BookTO)} with book with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithBookWithNullId() {
        bookFacade.moveDown(SpringToUtils.newBook(objectGenerator));
    }

    /**
     * Test method for {@link BookFacade#moveDown(BookTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDownWithNotMovableArgument() {
        bookFacade.moveDown(SpringToUtils.newBook(objectGenerator, SpringUtils.BOOKS_COUNT));
    }

    /**
     * Test method for {@link BookFacade#moveDown(BookTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDownWithBadId() {
        bookFacade.moveDown(SpringToUtils.newBook(objectGenerator, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link BookFacade#exists(BookTO)}.
     */
    @Test
    public void testExists() {
        for (int i = 1; i <= SpringUtils.BOOKS_COUNT; i++) {
            assertTrue(bookFacade.exists(SpringToUtils.newBook(objectGenerator, i)));
        }

        assertFalse(bookFacade.exists(SpringToUtils.newBook(objectGenerator, Integer.MAX_VALUE)));

        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#exists(BookTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsWithNullArgument() {
        bookFacade.exists(null);
    }

    /**
     * Test method for {@link BookFacade#exists(BookTO)} with book with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testExistsWithBookWithNullId() {
        bookFacade.exists(SpringToUtils.newBook(objectGenerator));
    }

    /**
     * Test method for {@link BookFacade#findBooksByBookCategory(cz.vhromada.catalog.facade.to.BookCategoryTO)}.
     */
    @Test
    public void testFindBooksByBookCategory() {
        for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
            DeepAsserts.assertEquals(SpringToUtils.getBooks(i), bookFacade.findBooksByBookCategory(SpringToUtils.newBookCategory(objectGenerator, i)),
                    "booksCount");
        }
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookFacade#findBooksByBookCategory(cz.vhromada.catalog.facade.to.BookCategoryTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindBooksByBookCategoryWithNullArgument() {
        bookFacade.findBooksByBookCategory(null);
    }

    /**
     * Test method for {@link BookFacade#findBooksByBookCategory(cz.vhromada.catalog.facade.to.BookCategoryTO)} with book category with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testFindBooksByBookCategoryWithNullId() {
        bookFacade.findBooksByBookCategory(SpringToUtils.newBookCategory(objectGenerator));
    }

    /**
     * Test method for {@link BookFacade#findBooksByBookCategory(cz.vhromada.catalog.facade.to.BookCategoryTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testFindBooksByBookCategoryWithBadId() {
        bookFacade.findBooksByBookCategory(SpringToUtils.newBookCategory(objectGenerator, Integer.MAX_VALUE));
    }

}
