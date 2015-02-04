package cz.vhromada.catalog.facade.impl;

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

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.BookCategoryFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.catalog.facade.validators.BookCategoryTOValidator;
import cz.vhromada.catalog.service.BookCategoryService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link BookCategoryFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class BookCategoryFacadeImplTest extends ObjectGeneratorTest {

    /** Instance of {@link BookCategoryService} */
    @Mock
    private BookCategoryService bookCategoryService;

    /** Instance of {@link Converter} */
    @Mock
    private Converter converter;

    /** Instance of {@link BookCategoryTOValidator} */
    @Mock
    private BookCategoryTOValidator bookCategoryTOValidator;

    /** Instance of {@link BookCategoryFacade} */
    private BookCategoryFacade bookCategoryFacade;

    /** Initializes facade for book categories. */
    @Before
    public void setUp() {
        bookCategoryFacade = new BookCategoryFacadeImpl(bookCategoryService, converter, bookCategoryTOValidator);
    }

    /**
     * Test method for {@link BookCategoryFacadeImpl#BookCategoryFacadeImpl(BookCategoryService, Converter, BookCategoryTOValidator)} with null
     * service for book categories.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBookCategoryService() {
        new BookCategoryFacadeImpl(null, converter, bookCategoryTOValidator);
    }

    /**
     * Test method for {@link BookCategoryFacadeImpl#BookCategoryFacadeImpl(BookCategoryService, Converter, BookCategoryTOValidator)} with null
     * converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConverter() {
        new BookCategoryFacadeImpl(bookCategoryService, null, bookCategoryTOValidator);
    }

    /**
     * Test method for {@link BookCategoryFacadeImpl#BookCategoryFacadeImpl(BookCategoryService, Converter, BookCategoryTOValidator)} with null
     * validator for TO for book category.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBookCategoryTOValidator() {
        new BookCategoryFacadeImpl(bookCategoryService, converter, null);
    }

    /** Test method for {@link BookCategoryFacade#newData()}. */
    @Test
    public void testNewData() {
        bookCategoryFacade.newData();

        verify(bookCategoryService).newData();
        verifyNoMoreInteractions(bookCategoryService);
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
        final List<BookCategory> bookCategories = CollectionUtils.newList(generate(BookCategory.class), generate(BookCategory.class));
        final List<BookCategoryTO> bookCategoriesList = CollectionUtils.newList(generate(BookCategoryTO.class), generate(BookCategoryTO.class));
        when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);
        when(converter.convertCollection(bookCategories, BookCategoryTO.class)).thenReturn(bookCategoriesList);

        DeepAsserts.assertEquals(bookCategoriesList, bookCategoryFacade.getBookCategories());

        verify(bookCategoryService).getBookCategories();
        verify(converter).convertCollection(bookCategories, BookCategoryTO.class);
        verifyNoMoreInteractions(bookCategoryService, converter);
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
        verifyZeroInteractions(converter);
    }

    /** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with existing book category. */
    @Test
    public void testGetBookCategoryWithExistingBookCategory() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
        when(converter.convert(any(BookCategory.class), eq(BookCategoryTO.class))).thenReturn(bookCategoryTO);

        DeepAsserts.assertEquals(bookCategoryTO, bookCategoryFacade.getBookCategory(bookCategoryTO.getId()));

        verify(bookCategoryService).getBookCategory(bookCategoryTO.getId());
        verify(converter).convert(bookCategory, BookCategoryTO.class);
        verifyNoMoreInteractions(bookCategoryService, converter);
    }

    /** Test method for {@link BookCategoryFacade#getBookCategory(Integer)} with not existing book category. */
    @Test
    public void testGetBookCategoryWithNotExistingBookCategory() {
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);
        when(converter.convert(any(BookCategory.class), eq(BookCategoryTO.class))).thenReturn(null);

        assertNull(bookCategoryFacade.getBookCategory(Integer.MAX_VALUE));

        verify(bookCategoryService).getBookCategory(Integer.MAX_VALUE);
        verify(converter).convert(null, BookCategoryTO.class);
        verifyNoMoreInteractions(bookCategoryService, converter);
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

        verifyZeroInteractions(bookCategoryService, converter);
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
        verifyZeroInteractions(converter);
    }

    /** Test method for {@link BookCategoryFacade#add(BookCategoryTO)}. */
    @Test
    public void testAdd() {
        final BookCategory bookCategory = generate(BookCategory.class);
        bookCategory.setId(null);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        bookCategoryTO.setId(null);
        final int id = generate(Integer.class);
        final int position = generate(Integer.class);
        doAnswer(setBookCategoryIdAndPosition(id, position)).when(bookCategoryService).add(any(BookCategory.class));
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        bookCategoryFacade.add(bookCategoryTO);
        DeepAsserts.assertEquals(id, bookCategoryTO.getId());
        DeepAsserts.assertEquals(position, bookCategoryTO.getPosition());

        verify(bookCategoryService).add(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateNewBookCategoryTO(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
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
        verifyZeroInteractions(bookCategoryService, converter);
    }

    /** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with argument with bad data. */
    @Test
    public void testAddWithBadArgument() {
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        bookCategory.setId(null);
        doThrow(ValidationException.class).when(bookCategoryTOValidator).validateNewBookCategoryTO(any(BookCategoryTO.class));

        try {
            bookCategoryFacade.add(bookCategory);
            fail("Can't add book category with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(bookCategoryTOValidator).validateNewBookCategoryTO(bookCategory);
        verifyNoMoreInteractions(bookCategoryTOValidator);
        verifyZeroInteractions(bookCategoryService, converter);
    }

    /** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with service tier not setting ID. */
    @Test
    public void testAddWithNotServiceTierSettingID() {
        final BookCategory bookCategory = generate(BookCategory.class);
        bookCategory.setId(null);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        bookCategoryTO.setId(null);
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        try {
            bookCategoryFacade.add(bookCategoryTO);
            fail("Can't add book category with service tier not setting ID.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(bookCategoryService).add(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateNewBookCategoryTO(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#add(BookCategoryTO)} with exception in service tier. */
    @Test
    public void testAddWithServiceTierException() {
        final BookCategory bookCategory = generate(BookCategory.class);
        bookCategory.setId(null);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        bookCategoryTO.setId(null);
        doThrow(ServiceOperationException.class).when(bookCategoryService).add(any(BookCategory.class));
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        try {
            bookCategoryFacade.add(bookCategoryTO);
            fail("Can't add book category with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(bookCategoryService).add(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateNewBookCategoryTO(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#update(BookCategoryTO)}. */
    @Test
    public void testUpdate() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.exists(any(BookCategory.class))).thenReturn(true);
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        bookCategoryFacade.update(bookCategoryTO);

        verify(bookCategoryService).exists(bookCategory);
        verify(bookCategoryService).update(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateExistingBookCategoryTO(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
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
        verifyZeroInteractions(bookCategoryService, converter);
    }

    /** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with argument with bad data. */
    @Test
    public void testUpdateWithBadArgument() {
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        doThrow(ValidationException.class).when(bookCategoryTOValidator).validateExistingBookCategoryTO(any(BookCategoryTO.class));

        try {
            bookCategoryFacade.update(bookCategory);
            fail("Can't update book category with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(bookCategoryTOValidator).validateExistingBookCategoryTO(bookCategory);
        verifyNoMoreInteractions(bookCategoryTOValidator);
        verifyZeroInteractions(bookCategoryService, converter);
    }

    /** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with not existing argument. */
    @Test
    public void testUpdateWithNotExistingArgument() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.exists(any(BookCategory.class))).thenReturn(false);
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        try {
            bookCategoryFacade.update(bookCategoryTO);
            fail("Can't update book category with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(bookCategoryService).exists(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateExistingBookCategoryTO(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#update(BookCategoryTO)} with exception in service tier. */
    @Test
    public void testUpdateWithServiceTierException() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        doThrow(ServiceOperationException.class).when(bookCategoryService).exists(any(BookCategory.class));
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        try {
            bookCategoryFacade.update(bookCategoryTO);
            fail("Can't update book category with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(bookCategoryService).exists(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateExistingBookCategoryTO(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)}. */
    @Test
    public void testRemove() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);

        bookCategoryFacade.remove(bookCategoryTO);

        verify(bookCategoryService).getBookCategory(bookCategoryTO.getId());
        verify(bookCategoryService).remove(bookCategory);
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
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
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
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
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

        try {
            bookCategoryFacade.remove(bookCategory);
            fail("Can't remove book category with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategory.getId());
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#remove(BookCategoryTO)} with exception in service tier. */
    @Test
    public void testRemoveWithServiceTierException() {
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

        try {
            bookCategoryFacade.remove(bookCategory);
            fail("Can't remove book category with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategory.getId());
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)}. */
    @Test
    public void testDuplicate() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);

        bookCategoryFacade.duplicate(bookCategoryTO);

        verify(bookCategoryService).getBookCategory(bookCategoryTO.getId());
        verify(bookCategoryService).duplicate(bookCategory);
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
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
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
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
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

        try {
            bookCategoryFacade.duplicate(bookCategory);
            fail("Can't duplicate book category with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategory.getId());
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#duplicate(BookCategoryTO)} with exception in service tier. */
    @Test
    public void testDuplicateWithServiceTierException() {
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

        try {
            bookCategoryFacade.duplicate(bookCategory);
            fail("Can't duplicate book category with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategory.getId());
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)}. */
    @Test
    public void testMoveUp() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final List<BookCategory> bookCategories = CollectionUtils.newList(mock(BookCategory.class), bookCategory);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
        when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);

        bookCategoryFacade.moveUp(bookCategoryTO);

        verify(bookCategoryService).getBookCategory(bookCategoryTO.getId());
        verify(bookCategoryService).getBookCategories();
        verify(bookCategoryService).moveUp(bookCategory);
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
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
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
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
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

        try {
            bookCategoryFacade.moveUp(bookCategory);
            fail("Can't move up book category with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategory.getId());
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with not moveable argument. */
    @Test
    public void testMoveUpWithNotMoveableArgument() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory, mock(BookCategory.class));
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
        when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);

        try {
            bookCategoryFacade.moveUp(bookCategoryTO);
            fail("Can't move up book category with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategoryTO.getId());
        verify(bookCategoryService).getBookCategories();
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#moveUp(BookCategoryTO)} with exception in service tier. */
    @Test
    public void testMoveUpWithServiceTierException() {
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

        try {
            bookCategoryFacade.moveUp(bookCategory);
            fail("Can't move up book category with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategory.getId());
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)}. */
    @Test
    public void testMoveDown() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final List<BookCategory> bookCategories = CollectionUtils.newList(bookCategory, mock(BookCategory.class));
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
        when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);

        bookCategoryFacade.moveDown(bookCategoryTO);

        verify(bookCategoryService).getBookCategory(bookCategoryTO.getId());
        verify(bookCategoryService).getBookCategories();
        verify(bookCategoryService).moveDown(bookCategory);
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
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
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
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
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(null);

        try {
            bookCategoryFacade.moveDown(bookCategory);
            fail("Can't move down book category with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategory.getId());
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with not moveable argument. */
    @Test
    public void testMoveDownWithNotMoveableArgument() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final List<BookCategory> bookCategories = CollectionUtils.newList(mock(BookCategory.class), bookCategory);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.getBookCategory(anyInt())).thenReturn(bookCategory);
        when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);

        try {
            bookCategoryFacade.moveDown(bookCategoryTO);
            fail("Can't move down book category with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategoryTO.getId());
        verify(bookCategoryService).getBookCategories();
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#moveDown(BookCategoryTO)} with exception in service tier. */
    @Test
    public void testMoveDownWithServiceTierException() {
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        doThrow(ServiceOperationException.class).when(bookCategoryService).getBookCategory(anyInt());

        try {
            bookCategoryFacade.moveDown(bookCategory);
            fail("Can't move down book category with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(bookCategoryService).getBookCategory(bookCategory.getId());
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryService, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with existing book category. */
    @Test
    public void testExistsWithExistingBookCategory() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.exists(any(BookCategory.class))).thenReturn(true);
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        assertTrue(bookCategoryFacade.exists(bookCategoryTO));

        verify(bookCategoryService).exists(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with not existing book category. */
    @Test
    public void testExistsWithNotExistingBookCategory() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        when(bookCategoryService.exists(any(BookCategory.class))).thenReturn(false);
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        assertFalse(bookCategoryFacade.exists(bookCategoryTO));

        verify(bookCategoryService).exists(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
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
        verifyZeroInteractions(bookCategoryService, converter);
    }

    /** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with argument with bad data. */
    @Test
    public void testExistsWithBadArgument() {
        final BookCategoryTO bookCategory = generate(BookCategoryTO.class);
        doThrow(ValidationException.class).when(bookCategoryTOValidator).validateBookCategoryTOWithId(any(BookCategoryTO.class));

        try {
            bookCategoryFacade.exists(bookCategory);
            fail("Can't exists book category with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategory);
        verifyNoMoreInteractions(bookCategoryTOValidator);
        verifyZeroInteractions(bookCategoryService, converter);
    }

    /** Test method for {@link BookCategoryFacade#exists(BookCategoryTO)} with exception in service tier. */
    @Test
    public void testExistsWithServiceTierException() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        doThrow(ServiceOperationException.class).when(bookCategoryService).exists(any(BookCategory.class));
        when(converter.convert(any(BookCategoryTO.class), eq(BookCategory.class))).thenReturn(bookCategory);

        try {
            bookCategoryFacade.exists(bookCategoryTO);
            fail("Can't exists book category with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(bookCategoryService).exists(bookCategory);
        verify(converter).convert(bookCategoryTO, BookCategory.class);
        verify(bookCategoryTOValidator).validateBookCategoryTOWithId(bookCategoryTO);
        verifyNoMoreInteractions(bookCategoryService, converter, bookCategoryTOValidator);
    }

    /** Test method for {@link BookCategoryFacade#updatePositions()}. */
    @Test
    public void testUpdatePositions() {
        bookCategoryFacade.updatePositions();

        verify(bookCategoryService).updatePositions();
        verifyNoMoreInteractions(bookCategoryService);
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
        final int count = generate(Integer.class);
        when(bookCategoryService.getBooksCount()).thenReturn(count);

        DeepAsserts.assertEquals(count, bookCategoryFacade.getBooksCount());

        verify(bookCategoryService).getBooksCount();
        verifyNoMoreInteractions(bookCategoryService);
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
    private static Answer<Void> setBookCategoryIdAndPosition(final Integer id, final int position) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                final BookCategory bookCategory = (BookCategory) invocation.getArguments()[0];
                bookCategory.setId(id);
                bookCategory.setPosition(position);
                return null;
            }

        };
    }

}
