package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogConfiguration;
import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.utils.GenreUtils;

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
@ContextConfiguration(classes = { CatalogConfiguration.class, CatalogTestConfiguration.class })
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
            GenreUtils.assertGenreDeepEquals(genreFacade.getGenre(i), GenreUtils.getGenreDomain(i));
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
     * Test method for {@link GenreFacade#add(Genre)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        genreFacade.add(GenreUtils.newGenre(null));

        final cz.vhromada.catalog.domain.Genre addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(GenreUtils.newGenreDomain(GenreUtils.GENRES_COUNT + 1), addedGenre);

        assertEquals(GenreUtils.GENRES_COUNT + 1, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        genreFacade.add(null);
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with genre with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotNullId() {
        genreFacade.add(GenreUtils.newGenre(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullName() {
        final Genre genre = GenreUtils.newGenre(null);
        genre.setName(null);

        genreFacade.add(genre);
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with genre with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyName() {
        final Genre genre = GenreUtils.newGenre(null);
        genre.setName("");

        genreFacade.add(genre);
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final Genre genre = GenreUtils.newGenre(1);

        genreFacade.update(genre);

        final cz.vhromada.catalog.domain.Genre updatedGenre = GenreUtils.getGenre(entityManager, 1);
        GenreUtils.assertGenreDeepEquals(genre, updatedGenre);

        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        genreFacade.update(null);
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullId() {
        genreFacade.update(GenreUtils.newGenre(null));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullName() {
        final Genre genre = GenreUtils.newGenre(1);
        genre.setName(null);

        genreFacade.update(genre);
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyName() {
        final Genre genre = GenreUtils.newGenre(1);
        genre.setName("");

        genreFacade.update(genre);
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadId() {
        genreFacade.update(GenreUtils.newGenre(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        clearReferencedData();

        genreFacade.remove(GenreUtils.newGenre(1));

        assertNull(GenreUtils.getGenre(entityManager, 1));

        assertEquals(GenreUtils.GENRES_COUNT - 1, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        genreFacade.remove(null);
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        genreFacade.remove(GenreUtils.newGenre(null));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with genre with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadId() {
        genreFacade.remove(GenreUtils.newGenre(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Genre genre = GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT);
        genre.setId(GenreUtils.GENRES_COUNT + 1);

        genreFacade.duplicate(GenreUtils.newGenre(GenreUtils.GENRES_COUNT));

        final cz.vhromada.catalog.domain.Genre duplicatedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(genre, duplicatedGenre);

        assertEquals(GenreUtils.GENRES_COUNT + 1, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        genreFacade.duplicate(null);
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullId() {
        genreFacade.duplicate(GenreUtils.newGenre(null));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with genre with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadId() {
        genreFacade.duplicate(GenreUtils.newGenre(Integer.MAX_VALUE));
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
