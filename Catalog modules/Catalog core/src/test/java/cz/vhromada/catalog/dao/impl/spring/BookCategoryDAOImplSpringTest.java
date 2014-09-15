package cz.vhromada.catalog.dao.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.BOOK_CATEGORIES_COUNT;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.dao.BookCategoryDAO;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.impl.BookCategoryDAOImpl;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link BookCategoryDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class BookCategoryDAOImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link BookCategoryDAO} */
	@Autowired
	private BookCategoryDAO bookCategoryDAO;

	/** Restarts sequence. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE book_categories_sq RESTART WITH 4").executeUpdate();
	}

	/** Test method for {@link BookCategoryDAO#getBookCategories()}. */
	@Test
	public void testGetBookCategories() {
		DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategories(), bookCategoryDAO.getBookCategories());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryDAO#getBookCategory(Integer)}. */
	@Test
	public void testGetBookCategory() {
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), bookCategoryDAO.getBookCategory(i));
		}

		assertNull(bookCategoryDAO.getBookCategory(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryDAO#add(BookCategory)}. */
	@Test
	public void testAdd() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		final BookCategory expectedBookCategory = EntityGenerator.createBookCategory(BOOK_CATEGORIES_COUNT + 1);
		expectedBookCategory.setPosition(BOOK_CATEGORIES_COUNT);

		bookCategoryDAO.add(bookCategory);

		DeepAsserts.assertNotNull(bookCategory.getId());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, bookCategory.getId());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, bookCategory.getPosition());
		final BookCategory addedBookCategory = SpringUtils.getBookCategory(entityManager, BOOK_CATEGORIES_COUNT + 1);
		DeepAsserts.assertEquals(bookCategory, addedBookCategory);
		DeepAsserts.assertEquals(expectedBookCategory, addedBookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryDAO#update(BookCategory)}. */
	@Test
	public void testUpdate() {
		final BookCategory bookCategory = SpringEntitiesUtils.updateBookCategory(SpringUtils.getBookCategory(entityManager, 1));
		final BookCategory expectedBookCategory = EntityGenerator.createBookCategory(1);

		bookCategoryDAO.update(bookCategory);

		final BookCategory updatedBookCategory = SpringUtils.getBookCategory(entityManager, 1);
		DeepAsserts.assertEquals(bookCategory, updatedBookCategory);
		DeepAsserts.assertEquals(expectedBookCategory, updatedBookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryDAO#remove(BookCategory)}. */
	@Test
	public void testRemove() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		entityManager.persist(bookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));

		bookCategoryDAO.remove(bookCategory);

		assertNull(SpringUtils.getBookCategory(entityManager, bookCategory.getId()));
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

}
