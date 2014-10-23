package cz.vhromada.catalog.service.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.BOOKS_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.BOOKS_PER_BOOK_CATEGORY_COUNT;
import static cz.vhromada.catalog.commons.SpringUtils.BOOK_CATEGORIES_COUNT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.service.BookService;
import cz.vhromada.catalog.service.impl.BookServiceImpl;
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
 * A class represents test for class {@link BookServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class BookServiceImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('bookCache')}")
	private Cache bookCache;

	/** Instance of {@link BookService} */
	@Autowired
	private BookService bookService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Clears cache and restarts sequence. */
	@Before
	public void setUp() {
		bookCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE books_sq RESTART WITH 10").executeUpdate();
	}

	/** Test method for {@link BookService#getBook(Integer)} with existing book. */
	@Test
	public void testGetBookWithExistingBook() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= BOOKS_COUNT; i++) {
			keys.add("book" + i);
		}

		for (int i = 0; i < BOOKS_COUNT; i++) {
			final int bookCategoryNumber = i / BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			final int bookNumber = i % BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber), bookService.getBook(i + 1));
		}
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		for (int i = 0; i < BOOKS_COUNT; i++) {
			final int bookCategoryNumber = i / BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			final int bookNumber = i % BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			SpringUtils.assertCacheValue(bookCache, keys.get(i), SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber));
		}
	}

	/** Test method for {@link BookService#getBook(Integer)} with not existing book. */
	@Test
	public void testGetBookWithNotExistingBook() {
		final String key = "book" + Integer.MAX_VALUE;

		assertNull(bookService.getBook(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, null);
	}

	/** Test method for {@link BookService#add(Book)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final Book book = objectGenerator.generate(Book.class);
		book.setId(null);
		book.setBookCategory(SpringUtils.getBookCategory(entityManager, 1));

		bookService.add(book);

		DeepAsserts.assertNotNull(book.getId());
		DeepAsserts.assertEquals(BOOKS_COUNT + 1, book.getId());
		final Book addedBook = SpringUtils.getBook(entityManager, BOOKS_COUNT + 1);
		DeepAsserts.assertEquals(book, addedBook);
		DeepAsserts.assertEquals(BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookService#add(Book)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, 1);
		final Book book = objectGenerator.generate(Book.class);
		book.setId(null);
		book.setBookCategory(bookCategory);
		final String keyList = "books" + bookCategory.getId();
		final String keyItem = "book" + (BOOKS_COUNT + 1);
		bookCache.put(keyList, new ArrayList<>());
		bookCache.put(keyItem, null);

		bookService.add(book);

		DeepAsserts.assertNotNull(book.getId());
		DeepAsserts.assertEquals(BOOKS_COUNT + 1, book.getId());
		final Book addedBook = SpringUtils.getBook(entityManager, BOOKS_COUNT + 1);
		DeepAsserts.assertEquals(book, addedBook);
		DeepAsserts.assertEquals(BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, keyList, CollectionUtils.newList(book));
		SpringUtils.assertCacheValue(bookCache, keyItem, book);
	}

	/** Test method for {@link BookService#update(Book)}. */
	@Test
	public void testUpdate() {
		final Book book = SpringEntitiesUtils.updateBook(SpringUtils.getBook(entityManager, 1), objectGenerator);

		bookService.update(book);

		final Book updatedBook = SpringUtils.getBook(entityManager, 1);
		DeepAsserts.assertEquals(book, updatedBook);
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookService#remove(Book)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final Book book = objectGenerator.generate(Book.class);
		book.setId(null);
		book.setBookCategory(SpringUtils.getBookCategory(entityManager, 1));
		entityManager.persist(book);
		DeepAsserts.assertEquals(BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));

		bookService.remove(book);

		assertNull(SpringUtils.getBook(entityManager, book.getId()));
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookService#remove(Book)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final Book book = objectGenerator.generate(Book.class);
		book.setId(null);
		book.setBookCategory(SpringUtils.getBookCategory(entityManager, 1));
		entityManager.persist(book);
		DeepAsserts.assertEquals(BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));
		final String key = "books" + book.getBookCategory().getId();
		final List<Book> cacheBooks = new ArrayList<>();
		cacheBooks.add(book);
		bookCache.put(key, cacheBooks);

		bookService.remove(book);

		assertNull(SpringUtils.getBook(entityManager, book.getId()));
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, new ArrayList<>());
	}

	/** Test method for {@link BookService#duplicate(Book)} with empty cache. */
	@Test
	public void testDuplicateWithEmptyCache() {
		final Book book = SpringUtils.getBook(entityManager, 3);
		final Book expectedBook = SpringEntitiesUtils.getBook(1, 3);
		expectedBook.setId(BOOKS_COUNT + 1);

		bookService.duplicate(book);

		DeepAsserts.assertEquals(expectedBook, SpringUtils.getBook(entityManager, BOOKS_COUNT + 1));
		for (int i = 0; i < BOOKS_COUNT; i++) {
			final int bookCategoryNumber = i / BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			final int bookNumber = i % BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber), SpringUtils.getBook(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookService#duplicate(Book)} with not empty cache. */
	@Test
	public void testDuplicateWithNotEmptyCache() {
		final Book book = SpringUtils.getBook(entityManager, 3);
		final Book expectedBook = SpringEntitiesUtils.getBook(1, 3);
		expectedBook.setId(BOOKS_COUNT + 1);
		final String keyList = "books" + book.getBookCategory().getId();
		final String keyItem = "book" + (BOOKS_COUNT + 1);
		bookCache.put(keyList, new ArrayList<>());
		bookCache.put(keyItem, null);

		bookService.duplicate(book);

		DeepAsserts.assertEquals(expectedBook, SpringUtils.getBook(entityManager, BOOKS_COUNT + 1));
		for (int i = 0; i < BOOKS_COUNT; i++) {
			final int bookCategoryNumber = i / BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			final int bookNumber = i % BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber), SpringUtils.getBook(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(BOOKS_COUNT + 1, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, keyList, CollectionUtils.newList(expectedBook));
		SpringUtils.assertCacheValue(bookCache, keyItem, expectedBook);
	}

	/** Test method for {@link BookService#moveUp(Book)}. */
	@Test
	public void testMoveUp() {
		final Book book = SpringUtils.getBook(entityManager, 2);
		final Book expectedBook1 = SpringEntitiesUtils.getBook(1, 1);
		expectedBook1.setPosition(1);
		final Book expectedBook2 = SpringEntitiesUtils.getBook(1, 2);
		expectedBook2.setPosition(0);

		bookService.moveUp(book);

		DeepAsserts.assertEquals(expectedBook1, SpringUtils.getBook(entityManager, 1));
		DeepAsserts.assertEquals(expectedBook2, SpringUtils.getBook(entityManager, 2));
		for (int i = 2; i < BOOKS_COUNT; i++) {
			final int bookCategoryNumber = i / BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			final int bookNumber = i % BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber), SpringUtils.getBook(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}

	/** Test method for {@link BookService#moveDown(Book)}. */
	@Test
	public void testMoveDown() {
		final Book book = SpringUtils.getBook(entityManager, 1);
		final Book expectedBook1 = SpringEntitiesUtils.getBook(1, 1);
		expectedBook1.setPosition(1);
		final Book expectedBook2 = SpringEntitiesUtils.getBook(1, 2);
		expectedBook2.setPosition(0);

		bookService.moveDown(book);

		DeepAsserts.assertEquals(expectedBook1, SpringUtils.getBook(entityManager, 1));
		DeepAsserts.assertEquals(expectedBook2, SpringUtils.getBook(entityManager, 2));
		for (int i = 2; i < BOOKS_COUNT; i++) {
			final int bookCategoryNumber = i / BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			final int bookNumber = i % BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber), SpringUtils.getBook(entityManager, i + 1));
		}
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(bookCache).size());
	}


	/** Test method for {@link BookService#exists(Book)} with existing book. */
	@Test
	public void testExistsWithExistingBook() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= BOOKS_COUNT; i++) {
			keys.add("book" + i);
		}

		for (int i = 0; i < BOOKS_COUNT; i++) {
			final int bookCategoryNumber = i / BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			final int bookNumber = i % BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			assertTrue(bookService.exists(SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber)));
		}
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		for (int i = 0; i < BOOKS_COUNT; i++) {
			final int bookCategoryNumber = i / BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			final int bookNumber = i % BOOKS_PER_BOOK_CATEGORY_COUNT + 1;
			SpringUtils.assertCacheValue(bookCache, keys.get(i), SpringEntitiesUtils.getBook(bookCategoryNumber, bookNumber));
		}
	}

	/** Test method for {@link BookService#exists(Book)} with not existing book. */
	@Test
	public void testExistsWithNotExistingBook() {
		final Book book = objectGenerator.generate(Book.class);
		book.setId(Integer.MAX_VALUE);
		final String key = "book" + Integer.MAX_VALUE;

		assertFalse(bookService.exists(book));
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(bookCache));
		SpringUtils.assertCacheValue(bookCache, key, null);
	}

	/** Test method for {@link BookService#findBooksByBookCategory(BookCategory)}. */
	@Test
	public void testFindBooksByBookCategory() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			keys.add("books" + i);
		}

		for (int i = 1; i <= BOOK_CATEGORIES_COUNT; i++) {
			final BookCategory bookCategory = SpringUtils.getBookCategory(entityManager, i);
			DeepAsserts.assertEquals(SpringEntitiesUtils.getBooks(i), bookService.findBooksByBookCategory(bookCategory));
		}
		DeepAsserts.assertEquals(BOOKS_COUNT, SpringUtils.getBooksCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(bookCache));
		for (int i = 0; i < BOOK_CATEGORIES_COUNT; i++) {
			SpringUtils.assertCacheValue(bookCache, keys.get(i), SpringEntitiesUtils.getBooks(i + 1));
		}
	}

}
