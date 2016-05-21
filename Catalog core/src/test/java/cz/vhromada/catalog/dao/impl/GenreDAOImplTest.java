package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.GenreUtils;
import cz.vhromada.catalog.dao.GenreDAO;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link GenreDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GenreDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for genres
     */
    @Mock
    private TypedQuery<Genre> genresQuery;

    /**
     * Instance of {@link GenreDAO}
     */
    private GenreDAO genreDAO;

    /**
     * Initializes DAO for genres.
     */
    @Before
    public void setUp() {
        genreDAO = new GenreDAOImpl(entityManager);
    }

    /**
     * Test method for {@link GenreDAOImpl#GenreDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new GenreDAOImpl(null);
    }

    /**
     * Test method for {@link GenreDAO#getGenres()}.
     */
    @Test
    public void testGetGenres() {
        when(entityManager.createNamedQuery(anyString(), eq(Genre.class))).thenReturn(genresQuery);
        when(genresQuery.getResultList()).thenReturn(CollectionUtils.newList(GenreUtils.newGenre(GenreUtils.ID), GenreUtils.newGenre(2)));

        final List<Genre> genres = genreDAO.getGenres();

        GenreUtils.assertGenresDeepEquals(CollectionUtils.newList(GenreUtils.newGenre(GenreUtils.ID), GenreUtils.newGenre(2)), genres);

        verify(entityManager).createNamedQuery(Genre.SELECT_GENRES, Genre.class);
        verify(genresQuery).getResultList();
        verifyNoMoreInteractions(entityManager, genresQuery);
    }

    /**
     * Test method for {@link GenreDAO#getGenres()} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetGenres_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Genre.class));

        genreDAO.getGenres();
    }

    /**
     * Test method for {@link GenreDAO#getGenre(Integer)} with existing genre.
     */
    @Test
    public void testGetGenre_ExistingGenre() {
        when(entityManager.find(eq(Genre.class), anyInt())).thenReturn(GenreUtils.newGenre(GenreUtils.ID));

        final Genre genre = genreDAO.getGenre(GenreUtils.ID);

        GenreUtils.assertGenreDeepEquals(GenreUtils.newGenre(GenreUtils.ID), genre);

        verify(entityManager).find(Genre.class, GenreUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GenreDAO#getGenre(Integer)} with not existing genre.
     */
    @Test
    public void testGetGenre_NotExistingGenre() {
        when(entityManager.find(eq(Genre.class), anyInt())).thenReturn(null);

        final Genre genre = genreDAO.getGenre(Integer.MAX_VALUE);

        assertNull(genre);

        verify(entityManager).find(Genre.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GenreDAO#getGenre(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetGenre_NullArgument() {
        genreDAO.getGenre(null);
    }

    /**
     * Test method for {@link GenreDAO#getGenre(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetGenre_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Genre.class), anyInt());

        genreDAO.getGenre(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link GenreDAO#add(Genre)}.
     */
    @Test
    public void testAdd() {
        final Genre genre = GenreUtils.newGenre(GenreUtils.ID);
        doAnswer(setId(GenreUtils.ID)).when(entityManager).persist(any(Genre.class));

        genreDAO.add(genre);

        assertEquals(GenreUtils.ID, genre.getId());
        assertEquals(GenreUtils.ID - 1, genre.getPosition());

        verify(entityManager).persist(genre);
        verify(entityManager).merge(genre);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GenreDAO#add(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        genreDAO.add(null);
    }

    /**
     * Test method for {@link GenreDAO#add(Genre)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Genre.class));

        genreDAO.add(GenreUtils.newGenre(GenreUtils.ID));
    }

    /**
     * Test method for {@link GenreDAO#update(Genre)}.
     */
    @Test
    public void testUpdate() {
        final Genre genre = GenreUtils.newGenre(GenreUtils.ID);

        genreDAO.update(genre);

        verify(entityManager).merge(genre);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GenreDAO#update(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        genreDAO.update(null);
    }

    /**
     * Test method for {@link GenreDAO#update(Genre)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Genre.class));

        genreDAO.update(GenreUtils.newGenre(GenreUtils.ID));
    }

    /**
     * Test method for {@link GenreDAO#remove(Genre)} with managed genre.
     */
    @Test
    public void testRemove_ManagedGenre() {
        final Genre genre = GenreUtils.newGenre(GenreUtils.ID);
        when(entityManager.contains(any(Genre.class))).thenReturn(true);

        genreDAO.remove(genre);

        verify(entityManager).contains(genre);
        verify(entityManager).remove(genre);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GenreDAO#remove(Genre)} with not managed genre.
     */
    @Test
    public void testRemove_NotManagedGenre() {
        final Genre genre = GenreUtils.newGenre(GenreUtils.ID);
        when(entityManager.contains(any(Genre.class))).thenReturn(false);
        when(entityManager.getReference(eq(Genre.class), anyInt())).thenReturn(genre);

        genreDAO.remove(genre);

        verify(entityManager).contains(genre);
        verify(entityManager).getReference(Genre.class, genre.getId());
        verify(entityManager).remove(genre);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link GenreDAO#remove(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        genreDAO.remove(null);
    }

    /**
     * Test method for {@link GenreDAO#remove(Genre)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Genre.class));

        genreDAO.remove(GenreUtils.newGenre(GenreUtils.ID));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Genre) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
