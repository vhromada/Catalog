package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.BookCategoryDAO;
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
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.BookCategoryDAOImpl} with Spring framework.
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

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Restarts sequence. */
	@Before
	public void setUp() {
		entityManager.createNativeQuery("ALTER SEQUENCE book_categories_sq RESTART WITH 4").executeUpdate();
	}

	/** Test method for {@link BookCategoryDAO#getBookCategories()}. */
	@Test
	public void testGetBookCategories() {
		DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategories(), bookCategoryDAO.getBookCategories());
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryDAO#getBookCategory(Integer)}. */
	@Test
	public void testGetBookCategory() {
		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), bookCategoryDAO.getBookCategory(i));
		}

		assertNull(bookCategoryDAO.getBookCategory(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryDAO#add(BookCategory)}. */
	@Test
	public void testAdd() {
		final BookCategory bookCategory = SpringEntitiesUtils.newBookCategory(objectGenerator);

		bookCategoryDAO.add(bookCategory);

		DeepAsserts.assertNotNull(bookCategory.getId());
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, bookCategory.getId());
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, bookCategory.getPosition());
		final BookCategory addedBookCategory = SpringUtils.getBookCategory(entityManager, SpringUtils.BOOK_CATEGORIES_COUNT + 1);
		DeepAsserts.assertEquals(bookCategory, addedBookCategory);
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryDAO#update(BookCategory)}. */
	@Test
	public void testUpdate() {
		final BookCategory bookCategory = SpringEntitiesUtils.updateBookCategory(1, objectGenerator, entityManager);

		bookCategoryDAO.update(bookCategory);

		final BookCategory updatedBookCategory = SpringUtils.getBookCategory(entityManager, 1);
		DeepAsserts.assertEquals(bookCategory, updatedBookCategory);
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

	/** Test method for {@link BookCategoryDAO#remove(BookCategory)}. */
	@Test
	public void testRemove() {
		final BookCategory bookCategory = SpringEntitiesUtils.newBookCategory(objectGenerator);
		entityManager.persist(bookCategory);
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));

		bookCategoryDAO.remove(bookCategory);

		assertNull(SpringUtils.getBookCategory(entityManager, bookCategory.getId()));
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
	}

}
