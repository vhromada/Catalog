package cz.vhromada.catalog.service.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.BOOKS_COUNT;
import static cz.vhromada.catalog.common.SpringUtils.BOOK_CATEGORIES_COUNT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.service.BookCategoryService;
import cz.vhromada.catalog.service.impl.BookCategoryServiceImpl;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link BookCategoryServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class BookCategoryServiceImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('bookCache')}")
	private Cache bookCache;

	/** Instance of {@link BookCategoryService} */
	@Autowired
	private BookCategoryService bookCategoryService;

	/** Clears cache and restarts sequences. */
	@Before
	public void setUp() {
		bookCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE book_categories_sq RESTART WITH 4").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE books_sq RESTART WITH 10").executeUpdate();
	}

	/** Test method for {@link BookCategoryService#newData()}. */
	@Test
	public void testNewData() {
		bookCategoryService.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getBooksCount(entityManager));
		assertTrue(SpringUtils.getCacheKeys(bookCache).isEmpty());
	}

	/** Test method for {@link BookCategoryService#getBookCategories()}. */
	@Test
	public void testGetBookCategories() {
		final List<BookCategory> bookCategories = SpringEntitiesUtils.getBookCategories();
		final String key = "bookCategories";

		DeepAsserts.assertEquals(bookCategories, bookCategoryService.getBookCategories());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, bookCategories);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with existing bookCategory. */
	@Test
	public void testGetBookCategoryWithExistingBookCategory() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			keys.add("bookCategory" + i);
		}

		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), bookCategoryService.getBookCategory(i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			SpringUtils.assertCacheValue(bookCache, keys.get(i - 1), SpringEntitiesUtils.getBookCategory(i));
		}
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with not existing bookCategory. */
	@Test
	public void testGetBookCategoryWithNotExistingBookCategory() {
		final String key = "bookCategory" + Integer.MAX_VALUE;

		assertNull(bookCategoryService.getBookCategory(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, null);
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		final BookCategory expectedBookCategory = EntityGenerator.createBookCategory(BOOK_CATEGORIES_COUNT + 1);
		expectedBookCategory.setPosition(BOOK_CATEGORIES_COUNT);

		bookCategoryService.add(bookCategory);

		DeepAsserts.assertNotNull(bookCategory.getId());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, bookCategory.getId());
		final BookCategory addedBookCategory = SpringUtils.getBookCategory(entityManager, BOOK_CATEGORIES_COUNT + 1);
		DeepAsserts.assertEquals(bookCategory, addedBookCategory);
		DeepAsserts.assertEquals(expectedBookCategory, addedBookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		final BookCategory expectedBookCategory = EntityGenerator.createBookCategory(BOOK_CATEGORIES_COUNT + 1);
		expectedBookCategory.setPosition(BOOK_CATEGORIES_COUNT);
		final String keyList = "bookCategories";
		final String keyItem = "bookCategory" + (BOOK_CATEGORIES_COUNT + 1);
		bookCache.put(keyList, new ArrayList<>());
		bookCache.put(keyItem, null);

		bookCategoryService.add(bookCategory);

		DeepAsserts.assertNotNull(bookCategory.getId());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, bookCategory.getId());
		final BookCategory addedBookCategory = SpringUtils.getBookCategory(entityManager, BOOK_CATEGORIES_COUNT + 1);
		DeepAsserts.assertEquals(bookCategory, addedBookCategory);
		DeepAsserts.assertEquals(expectedBookCategory, addedBookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, keyList, CollectionUtils.newList(bookCategory));
		SpringUtils.assertCacheValue(bookCache, keyItem, bookCategory);
	}

	/** Test method for {@link BookCategoryService#update(BookCategory)}. */
	@Test
	public void testUpdate() {
		final BookCategory bookCategory = SpringEntitiesUtils.updateBookCategory(SpringUtils.getBookCategory(entityManager, 1));

		bookCategoryService.update(bookCategory);

		final BookCategory updatedBookCategory = SpringUtils.getBookCategory(entityManager, 1);
		DeepAsserts.assertEquals(bookCategory, updatedBookCategory);
		DeepAsserts.assertEquals(EntityGenerator.createBookCategory(1), updatedBookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		entityManager.persist(bookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));

		bookCategoryService.remove(bookCategory);

		assertNull(SpringUtils.getBookCategory(entityManager, bookCategory.getId()));
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		entityManager.persist(bookCategory);
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		final String key = "bookCategories";
		final List<BookCategory> cacheBookCategories = new ArrayList<>();
		cacheBookCategories.add(bookCategory);
		bookCache.put(key, cacheBookCategories);

		bookCategoryService.remove(bookCategory);

		assertNull(SpringUtils.getBookCategory(entityManager, bookCategory.getId()));
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with empty cache. */
	@Test
	public void testDuplicateWithEmptyCache() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, 3);
		final BookCategory expectedBookCategory = SpringEntitiesUtils.getBookCategory(3);
		expectedBookCategory.setId(BOOK_CATEGORIES_COUNT + 1);

		bookCategoryService.duplicate(bookCategory);

		DeepAsserts.assertEquals(expectedBookCategory, SpringUtils.getBookCategory(entityManager, BOOK_CATEGORIES_COUNT + 1));
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with not empty cache. */
	@Test
	public void testDuplicateWithNotEmptyCache() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, 3);
		final BookCategory expectedBookCategory = SpringEntitiesUtils.getBookCategory(3);
		expectedBookCategory.setId(BOOK_CATEGORIES_COUNT + 1);
		final String keyList = "bookCategories";
		final String keyItem = "bookCategory" + (BOOK_CATEGORIES_COUNT + 1);
		bookCache.put(keyList, new ArrayList<>());
		bookCache.put(keyItem, null);

		bookCategoryService.duplicate(bookCategory);

		DeepAsserts.assertEquals(expectedBookCategory, SpringUtils.getBookCategory(entityManager, BOOK_CATEGORIES_COUNT + 1));
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#moveUp(BookCategory)}. */
	@Test
	public void testMoveUp() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, 2);
		final BookCategory expectedBookCategory1 = SpringEntitiesUtils.getBookCategory(1);
		expectedBookCategory1.setPosition(1);
		final BookCategory expectedBookCategory2 = SpringEntitiesUtils.getBookCategory(2);
		expectedBookCategory2.setPosition(0);

		bookCategoryService.moveUp(bookCategory);

		DeepAsserts.assertEquals(expectedBookCategory1, SpringUtils.getBookCategory(entityManager, 1));
		DeepAsserts.assertEquals(expectedBookCategory2, SpringUtils.getBookCategory(entityManager, 2));
		for (int i = 3; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#moveDown(BookCategory)}. */
	@Test
	public void testMoveDown() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, 1);
		final BookCategory expectedBookCategory1 = SpringEntitiesUtils.getBookCategory(1);
		expectedBookCategory1.setPosition(1);
		final BookCategory expectedBookCategory2 = SpringEntitiesUtils.getBookCategory(2);
		expectedBookCategory2.setPosition(0);

		bookCategoryService.moveDown(bookCategory);

		DeepAsserts.assertEquals(expectedBookCategory1, SpringUtils.getBookCategory(entityManager, 1));
		DeepAsserts.assertEquals(expectedBookCategory2, SpringUtils.getBookCategory(entityManager, 2));
		for (int i = 3; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}


	/** Test method for {@link BookCategoryService#exists(BookCategory)} with existing bookCategory. */
	@Test
	public void testExistsWithExistingBookCategory() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			keys.add("bookCategory" + i);
		}

		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			assertTrue(bookCategoryService.exists(SpringEntitiesUtils.getBookCategory(i)));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			SpringUtils.assertCacheValue(bookCache, keys.get(i - 1), SpringEntitiesUtils.getBookCategory(i));
		}
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with not existing bookCategory. */
	@Test
	public void testExistsWithNotExistingBookCategory() {
		final String key = "bookCategory" + Integer.MAX_VALUE;

		assertFalse(bookCategoryService.exists(EntityGenerator.createBookCategory(Integer.MAX_VALUE)));
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, null);
	}

	/** Test method for {@link BookCategoryService#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, BOOK_CATEGORIES_COUNT);
		bookCategory.setPosition(5000);
		entityManager.merge(bookCategory);

		bookCategoryService.updatePositions();

		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#getBooksCount()}. */
	@Test
	public void testGetBooksCount() {
		final String keyList = "bookCategories";
		final List<String> keyItems = new ArrayList<>();
		final int count = BOOKS_COUNT / BOOK_CATEGORIES_COUNT;
		for (int i = 1; i <= count; i++) {
			keyItems.add("books" + i);
		}
		final List<String> keys = new ArrayList<>();
		keys.add(keyList);
		keys.addAll(keyItems);

		DeepAsserts.assertEquals(BOOKS_COUNT, bookCategoryService.getBooksCount());
		DeepAsserts.assertEquals(BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, keyList, SpringEntitiesUtils.getBookCategories());
		for (int i = 1; i <= count; i++) {
			SpringUtils.assertCacheValue(bookCache, keyItems.get(i - 1), SpringEntitiesUtils.getBooks(i));
		}
	}

}
