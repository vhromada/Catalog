package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.BookDAO;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.BookDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class BookDAOImplSpringTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link BookDAO}
     */
    @Autowired
    private BookDAO bookDAO;

    /**
     * Instance of {@link ObjectGenerator}
     */
    @Autowired
    private ObjectGenerator objectGenerator;

    /**
     * Restarts sequence.
     */
    @Before
    public void setUp() {
        entityManager.createNativeQuery("ALTER SEQUENCE books_sq RESTART WITH 10").executeUpdate();
    }

    /**
     * Test method for {@link BookDAO#getBook(Integer)}.
     */
    @Test
    public void testGetBook() {
        for (int i = 0; i < SpringUtils.BOOKS_COUNT; i++) {
            final int bookCategoryNumber = i / SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
            final int bookNumber = i % SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
            DeepAsserts.assertEquals(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber), bookDAO.getBook(i + 1));
        }

        assertNull(bookDAO.getBook(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookDAO#add(Book)}.
     */
    @Test
    public void testAdd() {
        final Book book = SpringEntitiesUtils.newBook(objectGenerator, entityManager);

        bookDAO.add(book);

        DeepAsserts.assertNotNull(book.getId());
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT + 1, book.getId());
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, book.getPosition());
        final Book addedBook = SpringUtils.getBook(entityManager, SpringUtils.BOOKS_COUNT + 1);
        DeepAsserts.assertEquals(book, addedBook);
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookDAO#update(Book)}.
     */
    @Test
    public void testUpdate() {
        final Book book = SpringEntitiesUtils.updateBook(1, objectGenerator, entityManager);

        bookDAO.update(book);

        final Book updatedBook = SpringUtils.getBook(entityManager, 1);
        DeepAsserts.assertEquals(book, updatedBook);
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookDAO#remove(Book)}.
     */
    @Test
    public void testRemove() {
        bookDAO.remove(SpringUtils.getBook(entityManager, 1));

        assertNull(SpringUtils.getBook(entityManager, 1));
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT - 1, SpringUtils.getBooksCount(entityManager));
    }

    /**
     * Test method for {@link BookDAO#findBooksByBookCategory(BookCategory)}.
     */
    @Test
    public void testFindBooksByBookCategory() {
        for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
            final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, i);
            DeepAsserts.assertEquals(SpringEntitiesUtils.getBooks(i), bookDAO.findBooksByBookCategory(bookCategory));
        }
        DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
    }

}
