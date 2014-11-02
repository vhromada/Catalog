package cz.vhromada.catalog.service.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.service.BookCategoryService;
import cz.vhromada.generator.ObjectGenerator;
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
 * A class represents test for class {@link cz.vhromada.catalog.service.impl.BookCategoryServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class BookCategoryServiceImplSpringTest {

	/** Cache key for list of book categories */
	private static final String BOOK_CATEGORIES_CACHE_KEY = "bookCategories";

	/** Cache key for book category */
	private static final String BOOK_CATEGORY_CACHE_KEY = "bookCategory";

	/** Cache key for list of books */
	private static final String BOOKS_CACHE_KEY = "books";

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('bookCache')}")
	private Cache bookCache;

	/** Instance of {@link BookCategoryService} */
	@Autowired
	private BookCategoryService bookCategoryService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

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
		final String key = BOOK_CATEGORIES_CACHE_KEY;

		DeepAsserts.assertEquals(bookCategories, bookCategoryService.getBookCategories());
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, bookCategories);
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with existing book category. */
	@Test
	public void testGetBookCategoryWithExistingBookCategory() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			keys.add(BOOK_CATEGORY_CACHE_KEY + i);
		}

		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), bookCategoryService.getBookCategory(i));
		}
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			SpringUtils.assertCacheValue(bookCache, keys.get(i - 1), SpringEntitiesUtils.getBookCategory(i));
		}
	}

	/** Test method for {@link BookCategoryService#getBookCategory(Integer)} with not existing book category. */
	@Test
	public void testGetBookCategoryWithNotExistingBookCategory() {
		final String key = BOOK_CATEGORY_CACHE_KEY + Integer.MAX_VALUE;

		assertNull(bookCategoryService.getBookCategory(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, null);
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final BookCategory bookCategory = SpringEntitiesUtils.newBookCategory(objectGenerator);

		bookCategoryService.add(bookCategory);

		DeepAsserts.assertNotNull(bookCategory.getId());
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, bookCategory.getId());
		final BookCategory addedBookCategory = SpringUtils.getBookCategory(entityManager, SpringUtils.BOOK_CATEGORIES_COUNT + 1);
		DeepAsserts.assertEquals(bookCategory, addedBookCategory);
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#add(BookCategory)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final BookCategory bookCategory = SpringEntitiesUtils.newBookCategory(objectGenerator);
		final String keyList = BOOK_CATEGORIES_CACHE_KEY;
		final String keyItem = BOOK_CATEGORY_CACHE_KEY + (SpringUtils.BOOK_CATEGORIES_COUNT + 1);
		bookCache.put(keyList, new ArrayList<>());
		bookCache.put(keyItem, null);

		bookCategoryService.add(bookCategory);

		DeepAsserts.assertNotNull(bookCategory.getId());
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, bookCategory.getId());
		final BookCategory addedBookCategory = SpringUtils.getBookCategory(entityManager, SpringUtils.BOOK_CATEGORIES_COUNT + 1);
		DeepAsserts.assertEquals(bookCategory, addedBookCategory);
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, keyList, CollectionUtils.newList(bookCategory));
		SpringUtils.assertCacheValue(bookCache, keyItem, bookCategory);
	}

	/** Test method for {@link BookCategoryService#update(BookCategory)}. */
	@Test
	public void testUpdate() {
		final BookCategory bookCategory = SpringEntitiesUtils.updateBookCategory(1, objectGenerator, entityManager);

		bookCategoryService.update(bookCategory);

		final BookCategory updatedBookCategory = SpringUtils.getBookCategory(entityManager, 1);
		DeepAsserts.assertEquals(bookCategory, updatedBookCategory);
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final BookCategory bookCategory = SpringEntitiesUtils.newBookCategory(objectGenerator);
		entityManager.persist(bookCategory);
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));

		bookCategoryService.remove(bookCategory);

		assertNull(SpringUtils.getBookCategory(entityManager, bookCategory.getId()));
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#remove(BookCategory)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final BookCategory bookCategory = SpringEntitiesUtils.newBookCategory(objectGenerator);
		entityManager.persist(bookCategory);
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		final String key = BOOK_CATEGORIES_CACHE_KEY;
		final List<BookCategory> cacheBookCategories = new ArrayList<>();
		cacheBookCategories.add(bookCategory);
		bookCache.put(key, cacheBookCategories);

		bookCategoryService.remove(bookCategory);

		assertNull(SpringUtils.getBookCategory(entityManager, bookCategory.getId()));
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with empty cache. */
	@Test
	public void testDuplicateWithEmptyCache() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, 3);
		final BookCategory expectedBookCategory = SpringEntitiesUtils.getBookCategory(3);
		expectedBookCategory.setId(SpringUtils.BOOK_CATEGORIES_COUNT + 1);

		bookCategoryService.duplicate(bookCategory);

		DeepAsserts.assertEquals(expectedBookCategory, SpringUtils.getBookCategory(entityManager, SpringUtils.BOOK_CATEGORIES_COUNT + 1));
		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#duplicate(BookCategory)} with not empty cache. */
	@Test
	public void testDuplicateWithNotEmptyCache() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, 3);
		final BookCategory expectedBookCategory = SpringEntitiesUtils.getBookCategory(3);
		expectedBookCategory.setId(SpringUtils.BOOK_CATEGORIES_COUNT + 1);
		final String keyList = BOOK_CATEGORIES_CACHE_KEY;
		final String keyItem = BOOK_CATEGORY_CACHE_KEY + (SpringUtils.BOOK_CATEGORIES_COUNT + 1);
		bookCache.put(keyList, new ArrayList<>());
		bookCache.put(keyItem, null);

		bookCategoryService.duplicate(bookCategory);

		DeepAsserts.assertEquals(expectedBookCategory, SpringUtils.getBookCategory(entityManager, SpringUtils.BOOK_CATEGORIES_COUNT + 1));
		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT + 1, SpringUtils.getBookCategoriesCount(entityManager));
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
		for (int i = 3; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
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
		for (int i = 3; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}


	/** Test method for {@link BookCategoryService#exists(BookCategory)} with existing book category. */
	@Test
	public void testExistsWithExistingBookCategory() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			keys.add(BOOK_CATEGORY_CACHE_KEY + i);
		}

		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			assertTrue(bookCategoryService.exists(SpringEntitiesUtils.getBookCategory(i)));
		}
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			SpringUtils.assertCacheValue(bookCache, keys.get(i - 1), SpringEntitiesUtils.getBookCategory(i));
		}
	}

	/** Test method for {@link BookCategoryService#exists(BookCategory)} with not existing book category. */
	@Test
	public void testExistsWithNotExistingBookCategory() {
		final BookCategory bookCategory = SpringEntitiesUtils.newBookCategory(objectGenerator);
		bookCategory.setId(Integer.MAX_VALUE);
		final String key = BOOK_CATEGORY_CACHE_KEY + Integer.MAX_VALUE;

		assertFalse(bookCategoryService.exists(bookCategory));
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, null);
	}

	/** Test method for {@link BookCategoryService#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, SpringUtils.BOOK_CATEGORIES_COUNT);
		bookCategory.setPosition(5000);
		entityManager.merge(bookCategory);

		bookCategoryService.updatePositions();

		for (int i = 1; i <= SpringUtils.BOOK_CATEGORIES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBookCategory(i), SpringUtils.getBookCategory(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookCategoryService#getBooksCount()}. */
	@Test
	public void testGetBooksCount() {
		final String keyList = BOOK_CATEGORIES_CACHE_KEY;
		final List<String> keyItems = new ArrayList<>();
		final int count = SpringUtils.BOOKS_COUNT / SpringUtils.BOOK_CATEGORIES_COUNT;
		for (int i = 1; i <= count; i++) {
			keyItems.add(BOOKS_CACHE_KEY + i);
		}
		final List<String> keys = new ArrayList<>();
		keys.add(keyList);
		keys.addAll(keyItems);

		DeepAsserts.assertEquals(SpringUtils.BOOKS_COUNT, bookCategoryService.getBooksCount());
		DeepAsserts.assertEquals(SpringUtils.BOOK_CATEGORIES_COUNT, SpringUtils.getBookCategoriesCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, keyList, SpringEntitiesUtils.getBookCategories());
		for (int i = 1; i <= count; i++) {
			SpringUtils.assertCacheValue(bookCache, keyItems.get(i - 1), SpringEntitiesUtils.getBooks(i));
		}
	}

}
