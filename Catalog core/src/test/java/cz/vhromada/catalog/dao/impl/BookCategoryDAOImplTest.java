package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.BookCategoryDAO;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.test.DeepAsserts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link BookCategoryDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class BookCategoryDAOImplTest extends ObjectGeneratorTest {

    /** Instance of {@link EntityManager} */
    @Mock
    private EntityManager entityManager;

    /** Query for book categories */
    @Mock
    private TypedQuery<BookCategory> bookCategoriesQuery;

    /** Instance of {@link BookCategoryDAO} */
    private BookCategoryDAO bookCategoryDAO;

    /** Initializes DAO for book categories. */
    @Before
    public void setUp() {
        bookCategoryDAO = new BookCategoryDAOImpl(entityManager);
    }

    /** Test method for {@link BookCategoryDAOImpl#BookCategoryDAOImpl(EntityManager)} with null entity manager. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEntityManager() {
        new BookCategoryDAOImpl(null);
    }

    /** Test method for {@link BookCategoryDAO#getBookCategories()}. */
    @Test
    public void testGetBookCategories() {
        final List<BookCategory> bookCategories = CollectionUtils.newList(generate(BookCategory.class), generate(BookCategory.class));
        when(entityManager.createNamedQuery(anyString(), eq(BookCategory.class))).thenReturn(bookCategoriesQuery);
        when(bookCategoriesQuery.getResultList()).thenReturn(bookCategories);

        DeepAsserts.assertEquals(bookCategories, bookCategoryDAO.getBookCategories());

        verify(entityManager).createNamedQuery(BookCategory.SELECT_BOOK_CATEGORIES, BookCategory.class);
        verify(bookCategoriesQuery).getResultList();
        verifyNoMoreInteractions(entityManager, bookCategoriesQuery);
    }

    /** Test method for {@link BookCategoryDAO#getBookCategories()} with exception in persistence. */
    @Test
    public void testGetBookCategoriesWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(BookCategory.class));

        try {
            bookCategoryDAO.getBookCategories();
            fail("Can't get book categories with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).createNamedQuery(BookCategory.SELECT_BOOK_CATEGORIES, BookCategory.class);
        verifyNoMoreInteractions(entityManager);
        verifyZeroInteractions(bookCategoriesQuery);
    }

    /** Test method for {@link BookCategoryDAO#getBookCategory(Integer)} with existing bookCategory. */
    @Test
    public void testGetBookCategoryWithExistingBookCategory() {
        final int id = generate(Integer.class);
        final BookCategory bookCategory = mock(BookCategory.class);
        when(entityManager.find(eq(BookCategory.class), anyInt())).thenReturn(bookCategory);

        DeepAsserts.assertEquals(bookCategory, bookCategoryDAO.getBookCategory(id));

        verify(entityManager).find(BookCategory.class, id);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#getBookCategory(Integer)} with not existing bookCategory. */
    @Test
    public void testGetBookCategoryWithNotExistingBookCategory() {
        when(entityManager.find(eq(BookCategory.class), anyInt())).thenReturn(null);

        assertNull(bookCategoryDAO.getBookCategory(Integer.MAX_VALUE));

        verify(entityManager).find(BookCategory.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#getBookCategory(Integer)} with null argument. */
    @Test
    public void testGetBookCategoryWithNullArgument() {
        try {
            bookCategoryDAO.getBookCategory(null);
            fail("Can't get book category with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#getBookCategory(Integer)} with exception in persistence. */
    @Test
    public void testGetBookCategoryWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(BookCategory.class), anyInt());

        try {
            bookCategoryDAO.getBookCategory(Integer.MAX_VALUE);
            fail("Can't get book category with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).find(BookCategory.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#add(BookCategory)} with empty data storage. */
    @Test
    public void testAddWithEmptyDataStorage() {
        final BookCategory bookCategory = generate(BookCategory.class);
        final int id = generate(Integer.class);
        doAnswer(setId(id)).when(entityManager).persist(any(BookCategory.class));

        bookCategoryDAO.add(bookCategory);
        DeepAsserts.assertEquals(id, bookCategory.getId());
        DeepAsserts.assertEquals(id - 1, bookCategory.getPosition());

        verify(entityManager).persist(bookCategory);
        verify(entityManager).merge(bookCategory);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#add(BookCategory)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        try {
            bookCategoryDAO.add(null);
            fail("Can't add book category with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#add(BookCategory)} with exception in persistence. */
    @Test
    public void testAddWithPersistenceException() {
        final BookCategory bookCategory = generate(BookCategory.class);
        doThrow(PersistenceException.class).when(entityManager).persist(any(BookCategory.class));

        try {
            bookCategoryDAO.add(bookCategory);
            fail("Can't add book category with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).persist(bookCategory);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#update(BookCategory)}. */
    @Test
    public void testUpdate() {
        final BookCategory bookCategory = generate(BookCategory.class);

        bookCategoryDAO.update(bookCategory);

        verify(entityManager).merge(bookCategory);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#update(BookCategory)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            bookCategoryDAO.update(null);
            fail("Can't update book category with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#update(BookCategory)} with exception in persistence. */
    @Test
    public void testUpdateWithPersistenceException() {
        final BookCategory bookCategory = generate(BookCategory.class);
        doThrow(PersistenceException.class).when(entityManager).merge(any(BookCategory.class));

        try {
            bookCategoryDAO.update(bookCategory);
            fail("Can't update book category with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).merge(bookCategory);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#remove(BookCategory)} with managed book category. */
    @Test
    public void testRemoveWithManagedBookCategory() {
        final BookCategory bookCategory = generate(BookCategory.class);
        when(entityManager.contains(any(BookCategory.class))).thenReturn(true);

        bookCategoryDAO.remove(bookCategory);

        verify(entityManager).contains(bookCategory);
        verify(entityManager).remove(bookCategory);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#remove(BookCategory)} with not managed book category. */
    @Test
    public void testRemoveWithNotManagedBookCategory() {
        final BookCategory bookCategory = generate(BookCategory.class);
        when(entityManager.contains(any(BookCategory.class))).thenReturn(false);
        when(entityManager.getReference(eq(BookCategory.class), anyInt())).thenReturn(bookCategory);

        bookCategoryDAO.remove(bookCategory);

        verify(entityManager).contains(bookCategory);
        verify(entityManager).getReference(BookCategory.class, bookCategory.getId());
        verify(entityManager).remove(bookCategory);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#remove(BookCategory)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            bookCategoryDAO.remove(null);
            fail("Can't remove book category with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link BookCategoryDAO#remove(BookCategory)} with exception in persistence. */
    @Test
    public void testRemoveWithPersistenceException() {
        final BookCategory bookCategory = generate(BookCategory.class);
        doThrow(PersistenceException.class).when(entityManager).contains(any(BookCategory.class));

        try {
            bookCategoryDAO.remove(bookCategory);
            fail("Can't remove book category with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).contains(bookCategory);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                ((BookCategory) invocation.getArguments()[0]).setId(id);
                return null;
            }

        };
    }

}
