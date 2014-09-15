package cz.vhromada.catalog.facade.impl;

import static cz.vhromada.catalog.common.TestConstants.ADD_ID;
import static cz.vhromada.catalog.common.TestConstants.ADD_POSITION;
import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_INNER_ID;
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

import java.util.List;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.BookFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.catalog.facade.validators.BookCategoryTOValidator;
import cz.vhromada.catalog.facade.validators.BookTOValidator;
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
 * A class represents test for class {@link BookFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class BookFacadeImplTest {

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

	/** Instance of {@link BookTOValidator} */
	@Mock
	private BookTOValidator bookTOValidator;

	/** Instance of (@link BookFacade} */
	@InjectMocks
	private BookFacade bookFacade = new BookFacadeImpl();

	/** Test method for {@link BookFacadeImpl#getBookCategoryService()} and {@link BookFacadeImpl#setBookCategoryService(BookCategoryService)}. */
	@Test
	public void testBookCategoryService() {
		final BookFacadeImpl bookFacade = new BookFacadeImpl();
		bookFacade.setBookCategoryService(bookCategoryService);
		DeepAsserts.assertEquals(bookCategoryService, bookFacade.getBookCategoryService());
	}

	/** Test method for {@link BookFacadeImpl#getBookService()} and {@link BookFacadeImpl#setBookService(BookService)}. */
	@Test
	public void testBookService() {
		final BookFacadeImpl bookFacade = new BookFacadeImpl();
		bookFacade.setBookService(bookService);
		DeepAsserts.assertEquals(bookService, bookFacade.getBookService());
	}

	/** Test method for {@link BookFacadeImpl#getConversionService()} and {@link BookFacadeImpl#setConversionService(ConversionService)}. */
	@Test
	public void testConversionService() {
		final BookFacadeImpl bookFacade = new BookFacadeImpl();
		bookFacade.setConversionService(conversionService);
		DeepAsserts.assertEquals(conversionService, bookFacade.getConversionService());
	}

	/** Test method for {@link BookFacadeImpl#getBookCategoryTOValidator()} and {@link BookFacadeImpl#setBookCategoryTOValidator(BookCategoryTOValidator)}. */
	@Test
	public void testBookCategoryTOValidator() {
		final BookFacadeImpl bookFacade = new BookFacadeImpl();
		bookFacade.setBookCategoryTOValidator(bookCategoryTOValidator);
		DeepAsserts.assertEquals(bookCategoryTOValidator, bookFacade.getBookCategoryTOValidator());
	}

	/** Test method for {@link BookFacadeImpl#getBookTOValidator()} and {@link BookFacadeImpl#setBookTOValidator(BookTOValidator)}. */
	@Test
	public void testBookTOValidator() {
		final BookTOValidator bookTOValidator = mock(BookTOValidator.class);
		final BookFacadeImpl bookFacade = new BookFacadeImpl();
		bookFacade.setBookTOValidator(bookTOValidator);
		DeepAsserts.assertEquals(bookTOValidator, bookFacade.getBookTOValidator());
	}

	/** Test method for {@link BookFacade#getBook(Integer)} with existing book. */
	@Test
	public void testGetBookWithExistingBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID, EntityGenerator.createBookCategory(INNER_ID));
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID, ToGenerator.createBookCategory(INNER_ID));
		when(bookService.getBook(anyInt())).thenReturn(book);
		when(conversionService.convert(any(Book.class), eq(BookTO.class))).thenReturn(bookTO);

		DeepAsserts.assertEquals(bookTO, bookFacade.getBook(PRIMARY_ID));

		verify(bookService).getBook(PRIMARY_ID);
		verify(conversionService).convert(book, BookTO.class);
		verifyNoMoreInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookFacade#getBook(Integer)} with not existing book. */
	@Test
	public void testGetBookWithNotExistingBook() {
		when(bookService.getBook(anyInt())).thenReturn(null);
		when(conversionService.convert(any(Book.class), eq(BookTO.class))).thenReturn(null);

		assertNull(bookFacade.getBook(Integer.MAX_VALUE));

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(conversionService).convert(null, BookTO.class);
		verifyNoMoreInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookFacade#getBook(Integer)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.getBook(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookFacade#getBook(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetBookWithNotSetConversionService() {
		((BookFacadeImpl) bookFacade).setConversionService(null);
		bookFacade.getBook(Integer.MAX_VALUE);
	}

	/** Test method for {@link BookFacade#getBook(Integer)} with null argument. */
	@Test
	public void testGetBookWithNullArgument() {
		try {
			bookFacade.getBook(null);
			fail("Can't get book with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookFacade#getBook(Integer)} with exception in service tier. */
	@Test
	public void testGetBookWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(bookService).getBook(anyInt());

		try {
			bookFacade.getBook(Integer.MAX_VALUE);
			fail("Can't get book with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verifyNoMoreInteractions(bookService);
		verifyZeroInteractions(conversionService);
	}

	/** Test method for {@link BookFacade#add(BookTO)}. */
	@Test
	public void testAdd() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book = EntityGenerator.createBook(bookCategory);
		final BookTO bookTO = ToGenerator.createBook(ToGenerator.createBookCategory(INNER_ID));
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		doAnswer(setBookIdAndPosition(ADD_ID, ADD_POSITION)).when(bookService).add(any(Book.class));
		when(conversionService.convert(any(BookTO.class), eq(Book.class))).thenReturn(book);

		bookFacade.add(bookTO);

		DeepAsserts.assertEquals(ADD_ID, book.getId());
		DeepAsserts.assertEquals(ADD_POSITION, book.getPosition());

		verify(bookCategoryService).getBookCategory(INNER_ID);
		verify(bookService).add(book);
		verify(bookTOValidator).validateNewBookTO(bookTO);
		verify(conversionService).convert(bookTO, Book.class);
		verifyNoMoreInteractions(bookCategoryService, bookService, conversionService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#add(BookTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookCategoryService() {
		((BookFacadeImpl) bookFacade).setBookCategoryService(null);
		bookFacade.add(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#add(BookTO)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.add(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#add(BookTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((BookFacadeImpl) bookFacade).setConversionService(null);
		bookFacade.add(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#add(BookTO)} with not set validator for TO for book. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetBookTOValidator() {
		((BookFacadeImpl) bookFacade).setBookTOValidator(null);
		bookFacade.add(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#add(BookTO)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookTOValidator).validateNewBookTO(any(BookTO.class));

		try {
			bookFacade.add(null);
			fail("Can't add book with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookTOValidator).validateNewBookTO(null);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookFacade#add(BookTO)} with argument with bad data. */
	@Test
	public void testAddWithBadArgument() {
		final BookTO book = ToGenerator.createBook();
		doThrow(ValidationException.class).when(bookTOValidator).validateNewBookTO(any(BookTO.class));

		try {
			bookFacade.add(book);
			fail("Can't add book with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookTOValidator).validateNewBookTO(book);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookFacade#add(BookTO)} with not existing argument. */
	@Test
	public void testAddWithNotExistingArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		final BookTO book = ToGenerator.createBook(bookCategory);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

		try {
			bookFacade.add(book);
			fail("Can't add book with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookTOValidator).validateNewBookTO(book);
		verifyNoMoreInteractions(bookCategoryService, bookTOValidator);
		verifyZeroInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookFacade#add(BookTO)} with service tier not setting ID. */
	@Test
	public void testAddWithNotServiceTierSettingID() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book = EntityGenerator.createBook(bookCategory);
		final BookTO bookTO = ToGenerator.createBook(ToGenerator.createBookCategory(INNER_ID));
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(conversionService.convert(any(BookTO.class), eq(Book.class))).thenReturn(book);

		try {
			bookFacade.add(bookTO);
			fail("Can't add book with service tier not setting ID.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(INNER_ID);
		verify(bookService).add(book);
		verify(conversionService).convert(bookTO, Book.class);
		verify(bookTOValidator).validateNewBookTO(bookTO);
		verifyNoMoreInteractions(bookCategoryService, bookService, conversionService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#add(BookTO)} with exception in service tier. */
	@Test
	public void testAddWithServiceTierException() {
		final BookTO book = ToGenerator.createBook(ToGenerator.createBookCategory(Integer.MAX_VALUE));
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

		try {
			bookFacade.add(book);
			fail("Can't add book with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookTOValidator).validateNewBookTO(book);
		verifyNoMoreInteractions(bookCategoryService, bookTOValidator);
		verifyZeroInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookFacade#update(BookTO)}. */
	@Test
	public void testUpdate() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book = EntityGenerator.createBook(PRIMARY_ID, bookCategory);
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID, ToGenerator.createBookCategory(INNER_ID));
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookService.exists(any(Book.class))).thenReturn(true);
		when(conversionService.convert(any(BookTO.class), eq(Book.class))).thenReturn(book);

		bookFacade.update(bookTO);

		verify(bookCategoryService).getBookCategory(INNER_ID);
		verify(bookService).exists(book);
		verify(bookService).update(book);
		verify(conversionService).convert(bookTO, Book.class);
		verify(bookTOValidator).validateExistingBookTO(bookTO);
		verifyNoMoreInteractions(bookCategoryService, bookService, conversionService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#update(BookTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookCategoryService() {
		((BookFacadeImpl) bookFacade).setBookCategoryService(null);
		bookFacade.update(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#update(BookTO)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.update(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#update(BookTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((BookFacadeImpl) bookFacade).setConversionService(null);
		bookFacade.update(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#update(BookTO)} with not set validator for TO for book. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetBookTOValidator() {
		((BookFacadeImpl) bookFacade).setBookTOValidator(null);
		bookFacade.update(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#update(BookTO)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookTOValidator).validateExistingBookTO(any(BookTO.class));

		try {
			bookFacade.update(null);
			fail("Can't update book with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookTOValidator).validateExistingBookTO(null);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookFacade#update(BookTO)} with argument with bad data. */
	@Test
	public void testUpdateWithBadArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookTOValidator).validateExistingBookTO(any(BookTO.class));

		try {
			bookFacade.update(book);
			fail("Can't update book with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookTOValidator).validateExistingBookTO(book);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookFacade#update(BookTO)} with not existing argument. */
	@Test
	public void testUpdateWithNotExistingArgument() {
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE);
		final BookTO bookTO = ToGenerator.createBook(Integer.MAX_VALUE);
		when(bookService.exists(any(Book.class))).thenReturn(false);
		when(conversionService.convert(any(BookTO.class), eq(Book.class))).thenReturn(book);

		try {
			bookFacade.update(bookTO);
			fail("Can't update book with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookService).exists(book);
		verify(conversionService).convert(bookTO, Book.class);
		verify(bookTOValidator).validateExistingBookTO(bookTO);
		verifyNoMoreInteractions(bookService, conversionService, bookTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookFacade#update(BookTO)} with exception in service tier. */
	@Test
	public void testUpdateWithServiceTierException() {
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE);
		final BookTO bookTO = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookService).exists(any(Book.class));
		when(conversionService.convert(any(BookTO.class), eq(Book.class))).thenReturn(book);

		try {
			bookFacade.update(bookTO);
			fail("Can't update book with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookService).exists(book);
		verify(conversionService).convert(bookTO, Book.class);
		verify(bookTOValidator).validateExistingBookTO(bookTO);
		verifyNoMoreInteractions(bookService, conversionService, bookTOValidator);
		verifyZeroInteractions(bookCategoryService);
	}

	/** Test method for {@link BookFacade#remove(BookTO)}. */
	@Test
	public void testRemove() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID);
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID);
		when(bookService.getBook(anyInt())).thenReturn(book);

		bookFacade.remove(bookTO);

		verify(bookService).getBook(PRIMARY_ID);
		verify(bookService).remove(book);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#remove(BookTO)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.remove(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#remove(BookTO)} with not set validator for TO for book. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetBookTOValidator() {
		((BookFacadeImpl) bookFacade).setBookTOValidator(null);
		bookFacade.remove(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#remove(BookTO)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.remove(null);
			fail("Can't remove book with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(null);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookFacade#remove(BookTO)} with argument with bad data. */
	@Test
	public void testRemoveWithBadArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.remove(book);
			fail("Can't remove book with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookFacade#remove(BookTO)} with not existing argument. */
	@Test
	public void testRemoveWithNotExistingArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		when(bookService.getBook(anyInt())).thenReturn(null);

		try {
			bookFacade.remove(book);
			fail("Can't remove book with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#remove(BookTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookService).getBook(anyInt());

		try {
			bookFacade.remove(book);
			fail("Can't remove book with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#duplicate(BookTO)}. */
	@Test
	public void testDuplicate() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID);
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID);
		when(bookService.getBook(anyInt())).thenReturn(book);

		bookFacade.duplicate(bookTO);

		verify(bookService).getBook(PRIMARY_ID);
		verify(bookService).duplicate(book);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#duplicate(BookTO)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.duplicate(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#duplicate(BookTO)} with not set validator for TO for book. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetBookTOValidator() {
		((BookFacadeImpl) bookFacade).setBookTOValidator(null);
		bookFacade.duplicate(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#duplicate(BookTO)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.duplicate(null);
			fail("Can't duplicate book with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(null);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookFacade#duplicate(BookTO)} with argument with bad data. */
	@Test
	public void testDuplicateWithBadArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.duplicate(book);
			fail("Can't duplicate book with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookFacade#duplicate(BookTO)} with not existing argument. */
	@Test
	public void testDuplicateWithNotExistingArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		when(bookService.getBook(anyInt())).thenReturn(null);

		try {
			bookFacade.duplicate(book);
			fail("Can't duplicate book with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#duplicate(BookTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookService).getBook(anyInt());

		try {
			bookFacade.duplicate(book);
			fail("Can't duplicate book with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#moveUp(BookTO)}. */
	@Test
	public void testMoveUp() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book = EntityGenerator.createBook(PRIMARY_ID, bookCategory);
		final List<Book> books = CollectionUtils.newList(mock(Book.class), book);
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID);
		when(bookService.getBook(anyInt())).thenReturn(book);
		when(bookService.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);

		bookFacade.moveUp(bookTO);

		verify(bookService).getBook(PRIMARY_ID);
		verify(bookService).findBooksByBookCategory(bookCategory);
		verify(bookService).moveUp(book);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#moveUp(BookTO)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.moveUp(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#moveUp(BookTO)} with not set validator for TO for book. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetBookTOValidator() {
		((BookFacadeImpl) bookFacade).setBookTOValidator(null);
		bookFacade.moveUp(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#moveUp(BookTO)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.moveUp(null);
			fail("Can't move up book with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(null);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookFacade#moveUp(BookTO)} with argument with bad data. */
	@Test
	public void testMoveUpWithBadArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.moveUp(book);
			fail("Can't move up book with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookFacade#moveUp(BookTO)} with not existing argument. */
	@Test
	public void testMoveUpWithNotExistingArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		when(bookService.getBook(anyInt())).thenReturn(null);

		try {
			bookFacade.moveUp(book);
			fail("Can't move up book with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#moveUp(BookTO)} with not moveable argument. */
	@Test
	public void testMoveUpWithNotMoveableArgument() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE, bookCategory);
		final List<Book> books = CollectionUtils.newList(book, mock(Book.class));
		final BookTO bookTO = ToGenerator.createBook(Integer.MAX_VALUE);
		when(bookService.getBook(anyInt())).thenReturn(book);
		when(bookService.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);

		try {
			bookFacade.moveUp(bookTO);
			fail("Can't move up book with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookService).findBooksByBookCategory(bookCategory);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#moveUp(BookTO)} with exception in service tier. */
	@Test
	public void testMoveUpWithServiceTierException() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookService).getBook(anyInt());

		try {
			bookFacade.moveUp(book);
			fail("Can't move up book with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#moveDown(BookTO)}. */
	@Test
	public void testMoveDown() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book = EntityGenerator.createBook(PRIMARY_ID, bookCategory);
		final List<Book> books = CollectionUtils.newList(book, mock(Book.class));
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID);
		when(bookService.getBook(anyInt())).thenReturn(book);
		when(bookService.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);

		bookFacade.moveDown(bookTO);

		verify(bookService).getBook(PRIMARY_ID);
		verify(bookService).findBooksByBookCategory(bookCategory);
		verify(bookService).moveDown(book);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#moveDown(BookTO)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.moveDown(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#moveDown(BookTO)} with not set validator for TO for book. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetBookTOValidator() {
		((BookFacadeImpl) bookFacade).setBookTOValidator(null);
		bookFacade.moveDown(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#moveDown(BookTO)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.moveDown(null);
			fail("Can't move down book with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(null);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookFacade#moveDown(BookTO)} with argument with bad data. */
	@Test
	public void testMoveDownWithBadArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.moveDown(book);
			fail("Can't move down book with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService);
	}

	/** Test method for {@link BookFacade#moveDown(BookTO)} with not existing argument. */
	@Test
	public void testMoveDownWithNotExistingArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		when(bookService.getBook(anyInt())).thenReturn(null);

		try {
			bookFacade.moveDown(book);
			fail("Can't move down book with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#moveDown(BookTO)} with not moveable argument. */
	@Test
	public void testMoveDownWithNotMoveableArgument() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(INNER_ID);
		final Book book = EntityGenerator.createBook(Integer.MAX_VALUE, bookCategory);
		final List<Book> books = CollectionUtils.newList(mock(Book.class), book);
		final BookTO bookTO = ToGenerator.createBook(Integer.MAX_VALUE);
		when(bookService.getBook(anyInt())).thenReturn(book);
		when(bookService.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);

		try {
			bookFacade.moveDown(bookTO);
			fail("Can't move down book with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookService).findBooksByBookCategory(bookCategory);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#moveDown(BookTO)} with exception in service tier. */
	@Test
	public void testMoveDownWithServiceTierException() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookService).getBook(anyInt());

		try {
			bookFacade.moveDown(book);
			fail("Can't move down book with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookService).getBook(Integer.MAX_VALUE);
		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#exists(BookTO)} with existing book. */
	@Test
	public void testExistsWithExistingBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID);
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID);
		when(bookService.exists(any(Book.class))).thenReturn(true);
		when(conversionService.convert(any(BookTO.class), eq(Book.class))).thenReturn(book);

		assertTrue(bookFacade.exists(bookTO));

		verify(bookService).exists(book);
		verify(conversionService).convert(bookTO, Book.class);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, conversionService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#exists(BookTO)} with not existing book. */
	@Test
	public void testExistsWithNotExistingBook() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID);
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID);
		when(bookService.exists(any(Book.class))).thenReturn(false);
		when(conversionService.convert(any(BookTO.class), eq(Book.class))).thenReturn(book);

		assertFalse(bookFacade.exists(bookTO));

		verify(bookService).exists(book);
		verify(conversionService).convert(bookTO, Book.class);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, conversionService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#exists(BookTO)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.exists(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#exists(BookTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((BookFacadeImpl) bookFacade).setConversionService(null);
		bookFacade.exists(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#exists(BookTO)} with not set validator for TO for book. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetBookTOValidator() {
		((BookFacadeImpl) bookFacade).setBookTOValidator(null);
		bookFacade.exists(mock(BookTO.class));
	}

	/** Test method for {@link BookFacade#exists(BookTO)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.exists(null);
			fail("Can't exists book with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(null);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookFacade#exists(BookTO)} with argument with bad data. */
	@Test
	public void testExistsWithBadArgument() {
		final BookTO book = ToGenerator.createBook(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookTOValidator).validateBookTOWithId(any(BookTO.class));

		try {
			bookFacade.exists(book);
			fail("Can't exists book with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookTOValidator).validateBookTOWithId(book);
		verifyNoMoreInteractions(bookTOValidator);
		verifyZeroInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookFacade#exists(BookTO)} with exception in service tier. */
	@Test
	public void testExistsWithServiceTierException() {
		final Book book = EntityGenerator.createBook(PRIMARY_ID);
		final BookTO bookTO = ToGenerator.createBook(PRIMARY_ID);
		doThrow(ServiceOperationException.class).when(bookService).exists(any(Book.class));
		when(conversionService.convert(any(BookTO.class), eq(Book.class))).thenReturn(book);

		try {
			bookFacade.exists(bookTO);
			fail("Can't exists book with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookService).exists(book);
		verify(conversionService).convert(bookTO, Book.class);
		verify(bookTOValidator).validateBookTOWithId(bookTO);
		verifyNoMoreInteractions(bookService, conversionService, bookTOValidator);
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)}. */
	@Test
	public void testFindBooksByBookCategory() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(PRIMARY_ID);
		final List<Book> books = CollectionUtils.newList(EntityGenerator.createBook(INNER_ID, bookCategory),
				EntityGenerator.createBook(SECONDARY_INNER_ID, bookCategory));
		final BookCategoryTO bookCategoryTO = ToGenerator.createBookCategory(PRIMARY_ID);
		final List<BookTO> booksList = CollectionUtils.newList(ToGenerator.createBook(INNER_ID, bookCategoryTO),
				ToGenerator.createBook(SECONDARY_INNER_ID, bookCategoryTO));
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
		when(bookService.findBooksByBookCategory(any(BookCategory.class))).thenReturn(books);
		for (int i = 0; i < books.size(); i++) {
			final Book book = books.get(i);
			when(conversionService.convert(book, BookTO.class)).thenReturn(booksList.get(i));
		}

		DeepAsserts.assertEquals(booksList, bookFacade.findBooksByBookCategory(bookCategoryTO));

		verify(bookCategoryService).getBookCategory(PRIMARY_ID);
		verify(bookService).findBooksByBookCategory(bookCategory);
		for (Book book : books) {
			verify(conversionService).convert(book, BookTO.class);
		}
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
		verifyNoMoreInteractions(bookCategoryService, bookService, conversionService, bookCategoryTOValidator);
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)} with not set service for book categories. */
	@Test(expected = IllegalStateException.class)
	public void testFindBooksByBookCategoryWithNotSetBookCategoryService() {
		((BookFacadeImpl) bookFacade).setBookCategoryService(null);
		bookFacade.findBooksByBookCategory(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)} with not set service for books. */
	@Test(expected = IllegalStateException.class)
	public void testFindBooksByBookCategoryWithNotSetBookService() {
		((BookFacadeImpl) bookFacade).setBookService(null);
		bookFacade.findBooksByBookCategory(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testFindBooksByBookCategoryWithNotSetConversionService() {
		((BookFacadeImpl) bookFacade).setConversionService(null);
		bookFacade.findBooksByBookCategory(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)} with not set validator for TO for book category. */
	@Test(expected = IllegalStateException.class)
	public void testFindBooksByBookCategoryWithNotSetBookCategoryTOValidator() {
		((BookFacadeImpl) bookFacade).setBookCategoryTOValidator(null);
		bookFacade.findBooksByBookCategory(mock(BookCategoryTO.class));
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)} with null argument. */
	@Test
	public void testFindBooksByBookCategoryWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookFacade.findBooksByBookCategory(null);
			fail("Can't find books by bookCategory with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(null);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)} with argument with bad data. */
	@Test
	public void testFindBooksByBookCategoryWithBadArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

		try {
			bookFacade.findBooksByBookCategory(bookCategory);
			fail("Can't find books by book category with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryTOValidator);
		verifyZeroInteractions(bookCategoryService, bookService, conversionService);
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)} with not existing argument. */
	@Test
	public void testFindBooksByBookCategoryWithNotExistingArgument() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

		try {
			bookFacade.findBooksByBookCategory(bookCategory);
			fail("Can't find books by book category with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
		verifyZeroInteractions(bookService, conversionService);
	}

	/** Test method for {@link BookFacade#findBooksByBookCategory(BookCategoryTO)} with exception in service tier. */
	@Test
	public void testFindBooksByBookCategoryWithServiceTierException() {
		final BookCategoryTO bookCategory = ToGenerator.createBookCategory(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

		try {
			bookFacade.findBooksByBookCategory(bookCategory);
			fail("Can't find books by book category with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
		verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
		verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
		verifyZeroInteractions(bookService, conversionService);
	}

	/**
	 * Sets book's ID and position.
	 *
	 * @param id       ID
	 * @param position position
	 * @return mocked answer
	 */
	private Answer<Void> setBookIdAndPosition(final Integer id, final int position) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Book book = (Book) invocation.getArguments()[0];
				book.setId(id);
				book.setPosition(position);
				return null;
			}

		};
	}

}
