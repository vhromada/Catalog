package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.GenreUtils;
import cz.vhromada.catalog.entities.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * A class represents integration test for class {@link GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GenreFacadeImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link PlatformTransactionManager}
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * Instance of {@link GenreFacade}
     */
    @Autowired
    private GenreFacade genreFacade;

    /**
     * Test method for {@link GenreFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void testNewData() {
        clearReferencedData();

        genreFacade.newData();

        assertEquals(0, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#getGenres()}.
     */
    @Test
    public void testGetGenres() {
        GenreUtils.assertGenreListDeepEquals(genreFacade.getGenres(), GenreUtils.getGenres());

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#getGenre(Integer)}.
     */
    @Test
    public void testGetGenre() {
        for (int i = 1; i <= GenreUtils.GENRES_COUNT; i++) {
            GenreUtils.assertGenreDeepEquals(genreFacade.getGenre(i), GenreUtils.getGenre(i));
        }

        assertNull(genreFacade.getGenre(Integer.MAX_VALUE));

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#getGenre(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetGenre_NullArgument() {
        genreFacade.getGenre(null);
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        genreFacade.add(GenreUtils.newGenreTO(null));

        final Genre addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(GenreUtils.newGenre(GenreUtils.GENRES_COUNT + 1), addedGenre);

        assertEquals(GenreUtils.GENRES_COUNT + 1, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        genreFacade.add((GenreTO) null);
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)} with genre with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotNullId() {
        genreFacade.add(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)} with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullName() {
        final GenreTO genre = GenreUtils.newGenreTO(null);
        genre.setName(null);

        genreFacade.add(genre);
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)} with genre with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyName() {
        final GenreTO genre = GenreUtils.newGenreTO(null);
        genre.setName("");

        genreFacade.add(genre);
    }

    /**
     * Test method for {@link GenreFacade#add(List)}.
     */
    @Test
    @DirtiesContext
    public void testAddList() {
        final List<String> names = CollectionUtils.newList("Name1", "Name2");

        genreFacade.add(names);

        final Genre addedGenre1 = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        final Genre addedGenre2 = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 2);
        GenreUtils.assertGenreDeepEquals(createGenre(GenreUtils.GENRES_COUNT + 1, names.get(0)), addedGenre1);
        GenreUtils.assertGenreDeepEquals(createGenre(GenreUtils.GENRES_COUNT + 2, names.get(1)), addedGenre2);

        assertEquals(GenreUtils.GENRES_COUNT + 2, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#add(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddList_NullArgument() {
        genreFacade.add((List<String>) null);
    }

    /**
     * Test method for {@link GenreFacade#add(List)} with list of genre names with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAddList_NullGenreNames() {
        genreFacade.add(CollectionUtils.newList("Name", null));
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final GenreTO genre = GenreUtils.newGenreTO(1);

        genreFacade.update(genre);

        final Genre updatedGenre = GenreUtils.getGenre(entityManager, 1);
        GenreUtils.assertGenreDeepEquals(genre, updatedGenre);

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        genreFacade.update(null);
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)} with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullId() {
        genreFacade.update(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)} with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullName() {
        final GenreTO genre = GenreUtils.newGenreTO(1);
        genre.setName(null);

        genreFacade.update(genre);
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)} with genre with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyName() {
        final GenreTO genre = GenreUtils.newGenreTO(1);
        genre.setName("");

        genreFacade.update(genre);
    }

    /**
     * Test method for {@link GenreFacade#update(GenreTO)} with genre with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_BadId() {
        genreFacade.update(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#remove(GenreTO)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        clearReferencedData();

        genreFacade.remove(GenreUtils.newGenreTO(1));

        assertNull(GenreUtils.getGenre(entityManager, 1));

        assertEquals(GenreUtils.GENRES_COUNT - 1, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#remove(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        genreFacade.remove(null);
    }

    /**
     * Test method for {@link GenreFacade#remove(GenreTO)} with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_NullId() {
        genreFacade.remove(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#remove(GenreTO)} with genre with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_BadId() {
        genreFacade.remove(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(GenreTO)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final Genre genre = GenreUtils.getGenre(GenreUtils.GENRES_COUNT);
        genre.setId(GenreUtils.GENRES_COUNT + 1);

        genreFacade.duplicate(GenreUtils.newGenreTO(GenreUtils.GENRES_COUNT));

        final Genre duplicatedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(genre, duplicatedGenre);

        assertEquals(GenreUtils.GENRES_COUNT + 1, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(GenreTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        genreFacade.duplicate(null);
    }

    /**
     * Test method for {@link GenreFacade#duplicate(GenreTO)} with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_NullId() {
        genreFacade.duplicate(GenreUtils.newGenreTO(null));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(GenreTO)} with genre with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_BadId() {
        genreFacade.duplicate(GenreUtils.newGenreTO(Integer.MAX_VALUE));
    }

    /**
     * Returns new TO for genre.
     *
     * @param id   ID
     * @param name name
     * @return new TO for genre
     */
    private static GenreTO createGenre(final int id, final String name) {
        final GenreTO genre = new GenreTO();
        genre.setId(id);
        genre.setName(name);
        genre.setPosition(id - 1);

        return genre;
    }

    /**
     * Clears referenced data.
     */
    private void clearReferencedData() {
        final TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate();
        transactionManager.commit(transactionStatus);
    }

}
