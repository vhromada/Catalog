package cz.vhromada.catalog.facade.impl;

import static cz.vhromada.catalog.common.TestConstants.ADD_ID;
import static cz.vhromada.catalog.common.TestConstants.ADD_POSITION;
import static cz.vhromada.catalog.common.TestConstants.COUNT;
import static cz.vhromada.catalog.common.TestConstants.INNER_COUNT;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.BookCategoryFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.validators.BookCategoryTOValidator;
import cz.vhromada.catalog.service.BookCategoryService;
import cz.vhromada.catalog.service.BookService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.convert.ConversionService;

/**
 * A class represents test for class {@link BookCategoryFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class BookCategoryFacadeImplTest {

	/** Instance of {@link BookCategoryService} */
	@Mock
	private BookCategoryService bookCategoryService;

	/** Instance of {@link BookService} */
	@Mock
	private BookService bookService;

	/** Instance of {@link ConversionService} */
	@Mock
	private ConversionService conversionService;

	/** Instance of {@link BookCategoryTOValidator} */
	@Mock
	private BookCategoryTOValidator bookCategoryTOValidator;

	/** Instance of {@link BookCategoryFacade} */
	@InjectMocks
	private BookCategoryFacade bookCategoryFacade = new BookCategoryFacadeImpl();

	/**
	 * Test method for {@link BookCategoryFacadeImpl#getBookCategoryService()} and
	 * {@link BookCategoryFacadeImpl#setBookCategoryService(BookCategoryService)}.
	 */
	@Test
	public void testBookCategoryService() {
		final BookCategoryFacadeImpl bookCategoryFacade = new BookCategoryFacadeImpl();
		bookCategoryFacade.setBookCategoryService(bookCategoryService);
		DeepAsserts.assertEquals(bookCategoryService, bookCategoryFacade.getBookCategoryService());
	}

	/** Test method for {@link BookCategoryFacadeImpl#getBookService()} and {@link BookCategoryFacadeImpl#setBookService(BookService)}. */
	@Test
	public void testBookService() {
		final BookCategoryFacadeImpl bookCategoryFacade = new BookCategoryFacadeImpl();
		bookCategoryFacade.setBookService(bookService);
		DeepAsserts.assertEquals(bookService, bookCategoryFacade.getBookService());
	}

	/** Test method for {@link BookCategoryFacadeImpl#getConversionService()} and {@link BookCategoryFacadeImpl#setConversionService(ConversionService)}. */
	@Test
	public void testConversionService() {
		final BookCategoryFacadeImpl bookCategoryFacade = new BookCategoryFacadeImpl();
		bookCategoryFacade.setConversionService(conversionService);
		DeepAsserts.assertEquals(conversionService, bookCategoryFacade.getConversionService());
	}

	/**
	 * Test method for {@link BookCategoryFacadeImpl#getBookCategoryTOValidator()} and
	 * {@link BookCategoryFacadeImpl#setBookCategoryTOValidator(BookCategoryTOValidator)}.
	 */
	@Test
	public void testBookCategoryTOValidator() {
		final BookCategoryFacadeImpl bookCategoryFacade = new BookCategoryFacadeImpl();
		bookCategoryFacade.setBookCategoryTOValidator(bookCategoryTOValidator);
		DeepAsserts.assertEquals(bookCategoryTOValidator, bookCategoryFacade.getBookCategoryTOValidator());
	}

	/** Test method for {@link BookCategoryFacade#newData()}. */
	@Test
	public void testNewData() {
		bookCategoryFacade.newData();

		verify(bookCategoryService).newData();
		verifyNoMoreInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#newData()} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.newData();
	}

	/** Test method for {@link BookCategoryFacade#newData()} with exception in service tier. */
	@Test
	public void testNewDataWithFacadeTierException() {
		doThrow(ServiceOperationException.class).when(bookCategoryService).newData();

		try {
			bookCategoryFacade.newData();
			fail("Can't create new data with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).newData();
		verifyNoMoreInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategories()}. */
	@Test
	public void testGetBookCategories() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(EntityGenerator.createBookCategory(PRIMARY_ID),
				EntityGenerator.createBookCategory(SECONDARY_ID));
		final List<BookCategoryTO> bookCategoriesList = CollectionUtils.newList(ToGenerator.createBookCategory(PRIMARY_ID),
				ToGenerator.createBookCategory(SECONDARY_ID));
		final List<Book> books = new ArrayList<>();
		for (int i = 0; i < INNER_COUNT; i++) {
			books.add(EntityGenerator.createBook(i));
		}
		when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);
		when(bookService.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		for (int i = 0; i < bookCategories.size(); i++) {
			final BookCategory bookCategory = bookCategories.get(i);
			when(conversionService.convert(bookCategory, BookCategoryTO.class)).thenReturn(bookCategoriesList.get(i));
		}

		DeepAsserts.assertEquals(bookCategoriesList, bookCategoryFacade.getBookCategories());

		verify(bookCategoryService).getBookCategories();
		for (BookCategory bookCategory : bookCategories) {
			verify(bookService).findBooksByBookCategory(bookCategory);
			verify(conversionService).convert(bookCategory, BookCategoryTO.class);
		}
		verifyNoMoreInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategories()} with null book category. */
	@Test
	public void testGetBookCategoriesWithNullBookCategory() {
		final List<BookCategory> bookCategories = CollectionUtils.newList(EntityGenerator.createBookCategory(PRIMARY_ID),
				EntityGenerator.createBookCategory(SECONDARY_ID));
		final List<BookCategoryTO> bookCategoriesList = CollectionUtils.newList(null, null);
		when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);
		when(conversionService.convert(any(BookCategory.class), eq(BookCategoryTO.class))).thenReturn(null);

		final List<BookCategoryTO> result = bookCategoryFacade.getBookCategories();
		DeepAsserts.assertEquals(bookCategoriesList, result);

		verify(bookCategoryService).getBookCategories();
		for (BookCategory bookCategory : bookCategories) {
			verify(conversionService).convert(bookCategory, BookCategoryTO.class);
		}
		verifyNoMoreInteractions(bookCategoryService, conversionService);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategories()} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoriesWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.getBookCategories();
	}

	/** Test method for {@link BookCategoryFacade#getBookCategories()} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoriesWithNotSetBookService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookService(null);
		bookCategoryFacade.getBookCategories();
	}

	/** Test method for {@link BookCategoryFacade#getBookCategories()} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoriesWithNotSetConversionService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setConversionService(null);
		bookCategoryFacade.getBookCategories();
	}

	/** Test method for {@link BookCategoryFacade#getBookCategories()} with exception in service tier. */
	@Test
	public void testGetBookCategoriesWithFacadeTierException() {
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategories();

		try {
			bookCategoryFacade.getBookCategories();
			fail("Can't get book categories with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategories();
		verifyNoMoreInteractions(bookCategoryService);
		verifyZeroInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with existing book category. */
	@Test
	public void testGetBookCategoryWithExistingBookCategory() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		final List<Book> books = new ArrayList<>();
		for (int i = 0; i < INNER_COUNT; i++) {
			books.add(EntityGenerator.createBook(i));
		}
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookService.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		when(conversionService.convert(any(BookCategory.class), eq(BookCategoryTO.class))).thenReturn(bookCategoryTO);

		DeepAsserts.assertEquals(bookCategoryTO, bookCategoryFacade.getBookCategory(PRIMARY_ID));

		verify(bookCategoryService).getBookCategory(PRIMARY_ID);
		verify(bookService).findBooksByBookCategory(bookCategory);
		verify(conversionService).convert(bookCategory, BookCategoryTO.class);
		verifyNoMoreInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with not existing book category. */
	@Test
	public void testGetBookCategoryWithNotExistingBookCategory() {
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);
		when(conversionService.convert(any(BookCategory.class), eq(BookCategoryTO.class))).thenReturn(null);

		assertNull(bookCategoryFacade.getBookCategory(Integer.MAX_VALUE));

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(conversionService).convert(null, BookCategoryTO.class);
		verifyNoMoreInteractions(bookCategoryService, conversionService);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoryWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.getBookCategory(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoryWithNotSetBookService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookService(null);
		bookCategoryFacade.getBookCategory(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookCategoryWithNotSetConversionService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setConversionService(null);
		bookCategoryFacade.getBookCategory(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with null argument. */
	@Test
	public void testGetBookCategoryWithNullArgument() {
		try {
			bookCategoryFacade.getBookCategory(null);
			fail("Can't get book category with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with exception in service tier. */
	@Test
	public void testGetBookCategoryWithFacadeTierException() {
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

		try {
			bookCategoryFacade.getBookCategory(Integer.MAX_VALUE);
			fail("Can't get book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(anyInt());
		verifyNoMoreInteractions(bookCategoryService);
		verifyZeroInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)}. */
	@Test
	public void testAdd() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory();
		doAnswer(setBookCategoryIdAndPosition(ADD_ID, ADD_POSITION)).when(bookCategoryService).add(any(BookCategory.class));
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		bookCategoryFacade.add(bookCategoryTO);
		DeepAsserts.assertEquals(ADD_ID, bookCategoryTO.getId());
		DeepAsserts.assertEquals(ADD_POSITION, bookCategoryTO.getPosition());

		verify(bookCategoryService).add(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateNewBookCategoryTO(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.add(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setConversionService(null);
		bookCategoryFacade.add(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with not set validator for TO for book category. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookCategoryTOValidator() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryTOValidator(null);
		bookCategoryFacade.add(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookCategoryTOValidator).validateNewBookCategoryTO(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.add(null);
			fail("Can't add book category with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateNewBookCategoryTO(null);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with argument with bad data. */
	@Test
	public void testAddWithBadArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory();
		doThrow(ValidationException.class).when(bookCategoryTOValidator).validateNewBookCategoryTO(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.add(bookCategory);
			fail("Can't add book category with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateNewBookCategoryTO(bookCategory);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with service tier not setting ID. */
	@Test
	public void testAddWithNotServiceTierSettingID() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory();
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		try {
			bookCategoryFacade.add(bookCategoryTO);
			fail("Can't add book category with service tier not setting ID.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).add(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateNewBookCategoryTO(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with exception in service tier. */
	@Test
	public void testAddWithServiceTierException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory();
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory();
		doThrow(ServiceOperationException.class).when(bookCategoryService).add(any(BookCategory.class));
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		try {
			bookCategoryFacade.add(bookCategoryTO);
			fail("Can't add book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).add(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateNewBookCategoryTO(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)}. */
	@Test
	public void testUpdate() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		when(bookCategoryService.exists(any(BookCategory.class))).thenReturn(true);
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		bookCategoryFacade.update(bookCategoryTO);

		verify(bookCategoryService).exists(bookCategory);
		verify(bookCategoryService).update(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateExistingBookCategoryTO(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.update(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setConversionService(null);
		bookCategoryFacade.update(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with not set validator for TO for book category. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookCategoryTOValidator() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryTOValidator(null);
		bookCategoryFacade.update(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookCategoryTOValidator).validateExistingBookCategoryTO(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.update(null);
			fail("Can't update book category with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateExistingBookCategoryTO(null);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with argument with bad data. */
	@Test
	public void testUpdateWithBadArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookCategoryTOValidator).validateExistingBookCategoryTO(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.update(bookCategory);
			fail("Can't update book category with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateExistingBookCategoryTO(bookCategory);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with not existing argument. */
	@Test
	public void testUpdateWithNotExistingArgument() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(Integer.MAX_VALUE);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		when(bookCategoryService.exists(any(BookCategory.class))).thenReturn(false);
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		try {
			bookCategoryFacade.update(bookCategoryTO);
			fail("Can't update book category with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookCategoryService).exists(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateExistingBookCategoryTO(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with exception in service tier. */
	@Test
	public void testUpdateWithServiceTierException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(Integer.MAX_VALUE);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookCategoryService).exists(any(BookCategory.class));
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		try {
			bookCategoryFacade.update(bookCategoryTO);
			fail("Can't update book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).exists(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateExistingBookCategoryTO(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)}. */
	@Test
	public void testRemove() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);

		bookCategoryFacade.remove(bookCategoryTO);

		verify(bookCategoryService).getBookCategory(PRIMARY_ID);
		verify(bookCategoryService).remove(bookCategory);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.remove(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with not set validator for TO for book category. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookCategoryTOValidator() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryTOValidator(null);
		bookCategoryFacade.remove(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.remove(null);
			fail("Can't remove book category with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(null);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with argument with bad data. */
	@Test
	public void testRemoveWithBadArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.remove(bookCategory);
			fail("Can't remove book category with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with not existing argument. */
	@Test
	public void testRemoveWithNotExistingArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

		try {
			bookCategoryFacade.remove(bookCategory);
			fail("Can't remove book category with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

		try {
			bookCategoryFacade.remove(bookCategory);
			fail("Can't remove book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)}. */
	@Test
	public void testDuplicate() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);

		bookCategoryFacade.duplicate(bookCategoryTO);

		verify(bookCategoryService).getBookCategory(PRIMARY_ID);
		verify(bookCategoryService).duplicate(bookCategory);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.duplicate(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with not set validator for TO for book category. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookCategoryTOValidator() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryTOValidator(null);
		bookCategoryFacade.duplicate(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.duplicate(null);
			fail("Can't duplicate book category with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(null);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with argument with bad data. */
	@Test
	public void testDuplicateWithBadArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.duplicate(bookCategory);
			fail("Can't duplicate book category with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with not existing argument. */
	@Test
	public void testDuplicateWithNotExistingArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

		try {
			bookCategoryFacade.duplicate(bookCategory);
			fail("Can't duplicate book category with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

		try {
			bookCategoryFacade.duplicate(bookCategory);
			fail("Can't duplicate book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)}. */
	@Test
	public void testMoveUp() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final List<BookCategory> bookCategories = CollectionUtils.newList(mock(BookCategory.class), bookCategory);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);

		bookCategoryFacade.moveUp(bookCategoryTO);

		verify(bookCategoryService).getBookCategory(PRIMARY_ID);
		verify(bookCategoryService).getBookCategories();
		verify(bookCategoryService).moveUp(bookCategory);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.moveUp(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with not set validator for TO for book category. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetBookCategoryTOValidator() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryTOValidator(null);
		bookCategoryFacade.moveUp(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.moveUp(null);
			fail("Can't move up book category with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(null);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with argument with bad data. */
	@Test
	public void testMoveUpWithBadArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.moveUp(bookCategory);
			fail("Can't move up book category with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with not existing argument. */
	@Test
	public void testMoveUpWithNotExistingArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

		try {
			bookCategoryFacade.moveUp(bookCategory);
			fail("Can't move up book category with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with not moveable argument. */
	@Test
	public void testMoveUpWithNotMoveableArgument() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(Integer.MAX_VALUE);
		final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory, mock(BookCategory.class));
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);

		try {
			bookCategoryFacade.moveUp(bookCategoryTO);
			fail("Can't move up book category with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryService).getBookCategories();
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with exception in service tier. */
	@Test
	public void testMoveUpWithServiceTierException() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

		try {
			bookCategoryFacade.moveUp(bookCategory);
			fail("Can't move up book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)}. */
	@Test
	public void testMoveDown() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory, mock(BookCategory.class));
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);

		bookCategoryFacade.moveDown(bookCategoryTO);

		verify(bookCategoryService).getBookCategory(PRIMARY_ID);
		verify(bookCategoryService).getBookCategories();
		verify(bookCategoryService).moveDown(bookCategory);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.moveDown(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with not set validator for TO for book category. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetBookCategoryTOValidator() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryTOValidator(null);
		bookCategoryFacade.moveDown(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.moveDown(null);
			fail("Can't move down book category with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(null);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with argument with bad data. */
	@Test
	public void testMoveDownWithBadArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.moveDown(bookCategory);
			fail("Can't move down book category with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with not existing argument. */
	@Test
	public void testMoveDownWithNotExistingArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

		try {
			bookCategoryFacade.moveDown(bookCategory);
			fail("Can't move down book category with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with not moveable argument. */
	@Test
	public void testMoveDownWithNotMoveableArgument() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(Integer.MAX_VALUE);
		final List<BookCategory> bookCategories = CollectionUtils.newList(mock(BookCategory.class), bookCategory);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);

		try {
			bookCategoryFacade.moveDown(bookCategoryTO);
			fail("Can't move down book category with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryService).getBookCategories();
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with exception in service tier. */
	@Test
	public void testMoveDownWithServiceTierException() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

		try {
			bookCategoryFacade.moveDown(bookCategory);
			fail("Can't move down book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with existing book category. */
	@Test
	public void testExistsWithExistingBookCategory() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		when(bookCategoryService.exists(any(BookCategory.class))).thenReturn(true);
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		assertTrue(bookCategoryFacade.exists(bookCategoryTO));

		verify(bookCategoryService).exists(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with not existing book category. */
	@Test
	public void testExistsWithNotExistingBookCategory() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		when(bookCategoryService.exists(any(BookCategory.class))).thenReturn(false);
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		assertFalse(bookCategoryFacade.exists(bookCategoryTO));

		verify(bookCategoryService).exists(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.exists(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setConversionService(null);
		bookCategoryFacade.exists(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with not set validator for TO for book category. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetBookCategoryTOValidator() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryTOValidator(null);
		bookCategoryFacade.exists(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.exists(null);
			fail("Can't exists book category with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(null);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with argument with bad data. */
	@Test
	public void testExistsWithBadArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookCategoryFacade.exists(bookCategory);
			fail("Can't exists book category with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService, conversionService);
	}

	/** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with exception in service tier. */
	@Test
	public void testExistsWithServiceTierException() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		doThrow(ServiceOperationException.class).when(bookCategoryService).exists(any(BookCategory.class));
		when(conversionService.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

		try {
			bookCategoryFacade.exists(bookCategoryTO);
			fail("Can't exists book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).exists(bookCategory);
		verify(conversionService).convert(bookCategoryTO, BookCategory.class);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookCategoryFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		bookCategoryFacade.updatePositions();

		verify(bookCategoryService).updatePositions();
		verifyNoMoreInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#updatePositions()} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.updatePositions();
	}

	/** Test method for {@link BookCategoryFacade#updatePositions()} with exception in service tier. */
	@Test
	public void testUpdatePositionsWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(bookCategoryService).updatePositions();

		try {
			bookCategoryFacade.updatePositions();
			fail("Can't update positions with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).updatePositions();
		verifyNoMoreInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#getBooksCount()}. */
	@Test
	public void testGetBooksCount() {
		when(bookCategoryService.getBooksCount()).thenReturn(COUNT);

		DeepAsserts.assertEquals(COUNT, bookCategoryFacade.getBooksCount());

		verify(bookCategoryService).getBooksCount();
		verifyNoMoreInteractions(bookCategoryService);
	}

	/** Test method for {@link BookCategoryFacade#getBooksCount()} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testGetBooksCountWithNotSetBookCategoryService() {
		((BookCategoryFacadeImpl) bookCategoryFacade).setBookCategoryService(null);
		bookCategoryFacade.getBooksCount();
	}

	/** Test method for {@link BookCategoryFacade#getBooksCount()} with exception in service tier. */
	@Test
	public void testGetBooksCountWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBooksCount();

		try {
			bookCategoryFacade.getBooksCount();
			fail("Can't get total media count with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBooksCount();
		verifyNoMoreInteractions(bookCategoryService);
	}

	/**
	 * Sets book category's ID and position.
	 *
	 * @param id       ID
	 * @param position position
	 * @return mocked answer
	 */
	private Answer<Void> setBookCategoryIdAndPosition(final Integer id, final int position) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final BookCategory bookCategory = (BookCategory) invocation.getArguments()[0];
				bookCategory.setId(id);
				bookCategory.setPosition(position);
				return null;
			}

		};
	}

}
