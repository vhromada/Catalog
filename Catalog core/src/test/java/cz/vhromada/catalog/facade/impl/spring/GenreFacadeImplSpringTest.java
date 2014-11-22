package cz.vhromada.catalog.facade.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.impl.GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class GenreFacadeImplSpringTest {

    /** Instance of {@link EntityManager} */
    @Autowired
    private EntityManager entityManager;

    /** Instance of {@link PlatformTransactionManager} */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /** Instance of {@link GenreFacade} */
    @Autowired
    private GenreFacade genreFacade;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Initializes database. */
    @Before
    public void setUp() {
        SpringUtils.remove(transactionManager, entityManager, Movie.class);
        SpringUtils.remove(transactionManager, entityManager, Episode.class);
        SpringUtils.remove(transactionManager, entityManager, Season.class);
        SpringUtils.remove(transactionManager, entityManager, Serie.class);
        SpringUtils.remove(transactionManager, entityManager, Genre.class);
        SpringUtils.updateSequence(transactionManager, entityManager, "genres_sq");
        for (final Genre genre : SpringEntitiesUtils.getGenres()) {
            genre.setId(null);
            SpringUtils.persist(transactionManager, entityManager, genre);
        }
    }

    /** Test method for {@link GenreFacade#newData()}. */
    @Test
    public void testNewData() {
        genreFacade.newData();

        DeepAsserts.assertEquals(0, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#getGenres()}. */
    @Test
    public void testGetGenres() {
        DeepAsserts.assertEquals(SpringToUtils.getGenres(), genreFacade.getGenres());
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#getGenre(Integer)}. */
    @Test
    public void testGetGenre() {
        for (int i = 1; i <= SpringUtils.GENRES_COUNT; i++) {
            DeepAsserts.assertEquals(SpringToUtils.getGenre(i), genreFacade.getGenre(i));
        }

        assertNull(genreFacade.getGenre(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#getGenre(Integer)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testGetGenreWithNullArgument() {
        genreFacade.getGenre(null);
    }

    /** Test method for {@link GenreFacade#add(GenreTO)}. */
    @Test
    public void testAdd() {
        final GenreTO genre = SpringToUtils.newGenre(objectGenerator);

        genreFacade.add(genre);

        DeepAsserts.assertNotNull(genre.getId());
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT + 1, genre.getId());
        final Genre addedGenre = SpringUtils.getGenre(entityManager, SpringUtils.GENRES_COUNT + 1);
        DeepAsserts.assertEquals(genre, addedGenre);
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#add(GenreTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNullArgument() {
        genreFacade.add((GenreTO) null);
    }

    /** Test method for {@link GenreFacade#add(GenreTO)} with genre with not null ID. */
    @Test(expected = ValidationException.class)
    public void testAddWithGenreWithNotNullId() {
        genreFacade.add(SpringToUtils.newGenreWithId(objectGenerator));
    }

    /** Test method for {@link GenreFacade#add(GenreTO)} with genre with null name. */
    @Test(expected = ValidationException.class)
    public void testAddWithGenreWithNullName() {
        final GenreTO genre = SpringToUtils.newGenre(objectGenerator);
        genre.setName(null);

        genreFacade.add(genre);
    }

    /** Test method for {@link GenreFacade#add(GenreTO)} with genre with empty string as name. */
    @Test(expected = ValidationException.class)
    public void testAddWithGenreWithEmptyName() {
        final GenreTO genre = SpringToUtils.newGenre(objectGenerator);
        genre.setName("");

        genreFacade.add(genre);
    }

    /** Test method for {@link GenreFacade#add(List)}. */
    @Test
    public void testAddList() {
        final List<String> names = CollectionUtils.newList(objectGenerator.generate(String.class), objectGenerator.generate(String.class));

        genreFacade.add(names);

        final Genre addedGenre1 = SpringUtils.getGenre(entityManager, SpringUtils.GENRES_COUNT + 1);
        final Genre addedGenre2 = SpringUtils.getGenre(entityManager, SpringUtils.GENRES_COUNT + 2);
        DeepAsserts.assertEquals(createGenre(SpringUtils.GENRES_COUNT + 1, names.get(0)), addedGenre1);
        DeepAsserts.assertEquals(createGenre(SpringUtils.GENRES_COUNT + 2, names.get(1)), addedGenre2);
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT + 2, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#add(GenreTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testAddListWithNullArgument() {
        genreFacade.add((List<String>) null);
    }

    /** Test method for {@link GenreFacade#add(List)} with list of genre names with null value. */
    @Test(expected = ValidationException.class)
    public void testAddListWithGenreWithGenreNamesWithNull() {
        genreFacade.add(CollectionUtils.newList(objectGenerator.generate(String.class), null));
    }

    /** Test method for {@link GenreFacade#update(GenreTO)}. */
    @Test
    public void testUpdate() {
        final GenreTO genre = SpringToUtils.newGenre(objectGenerator, 1);

        genreFacade.update(genre);

        final Genre updatedGenre = SpringUtils.getGenre(entityManager, 1);
        DeepAsserts.assertEquals(genre, updatedGenre);
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#update(GenreTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullArgument() {
        genreFacade.update(null);
    }

    /** Test method for {@link GenreFacade#update(GenreTO)} with genre with null ID. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithGenreWithNullId() {
        genreFacade.update(SpringToUtils.newGenre(objectGenerator));
    }

    /** Test method for {@link GenreFacade#update(GenreTO)} with genre with null name. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithGenreWithNullName() {
        final GenreTO genre = SpringToUtils.newGenreWithId(objectGenerator);
        genre.setName(null);

        genreFacade.update(genre);
    }

    /** Test method for {@link GenreFacade#update(GenreTO)} with genre with empty string as name. */
    @Test(expected = ValidationException.class)
    public void testUpdateWithGenreWithEmptyName() {
        final GenreTO genre = SpringToUtils.newGenreWithId(objectGenerator);
        genre.setName("");

        genreFacade.update(genre);
    }

    /** Test method for {@link GenreFacade#update(GenreTO)} with genre with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdateWithGenreWithBadId() {
        genreFacade.update(SpringToUtils.newGenre(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link GenreFacade#remove(GenreTO)}. */
    @Test
    public void testRemove() {
        final Genre genre = SpringEntitiesUtils.newGenre(objectGenerator);
        SpringUtils.persist(transactionManager, entityManager, genre);
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));

        genreFacade.remove(SpringToUtils.newGenre(objectGenerator, genre.getId()));

        assertNull(SpringUtils.getGenre(entityManager, genre.getId()));
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#remove(GenreTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullArgument() {
        genreFacade.remove(null);
    }

    /** Test method for {@link GenreFacade#remove(GenreTO)} with genre with null ID. */
    @Test(expected = ValidationException.class)
    public void testRemoveWithGenreWithNullId() {
        genreFacade.remove(SpringToUtils.newGenre(objectGenerator));
    }

    /** Test method for {@link GenreFacade#remove(GenreTO)} with genre with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testRemoveWithGenreWithBadId() {
        genreFacade.remove(SpringToUtils.newGenre(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link GenreFacade#duplicate(GenreTO)}. */
    @Test
    public void testDuplicate() {
        final Genre genre = SpringEntitiesUtils.getGenre(SpringUtils.GENRES_COUNT);
        genre.setId(SpringUtils.GENRES_COUNT + 1);

        genreFacade.duplicate(SpringToUtils.newGenre(objectGenerator, SpringUtils.GENRES_COUNT));

        final Genre duplicatedGenre = SpringUtils.getGenre(entityManager, SpringUtils.GENRES_COUNT + 1);
        DeepAsserts.assertEquals(genre, duplicatedGenre);
        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#duplicate(GenreTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateWithNullArgument() {
        genreFacade.duplicate(null);
    }

    /** Test method for {@link GenreFacade#duplicate(GenreTO)} with genre with null ID. */
    @Test(expected = ValidationException.class)
    public void testDuplicateWithGenreWithNullId() {
        genreFacade.duplicate(SpringToUtils.newGenre(objectGenerator));
    }

    /** Test method for {@link GenreFacade#duplicate(GenreTO)} with genre with bad ID. */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicateWithGenreWithBadId() {
        genreFacade.duplicate(SpringToUtils.newGenre(objectGenerator, Integer.MAX_VALUE));
    }

    /** Test method for {@link GenreFacade#exists(GenreTO)} with existing genre. */
    @Test
    public void testExists() {
        for (int i = 1; i <= SpringUtils.GENRES_COUNT; i++) {
            assertTrue(genreFacade.exists(SpringToUtils.newGenre(objectGenerator, i)));
        }

        assertFalse(genreFacade.exists(SpringToUtils.newGenre(objectGenerator, Integer.MAX_VALUE)));

        DeepAsserts.assertEquals(SpringUtils.GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
    }

    /** Test method for {@link GenreFacade#exists(GenreTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsWithNullArgument() {
        genreFacade.exists(null);
    }

    /** Test method for {@link GenreFacade#exists(GenreTO)} with genre with null ID. */
    @Test(expected = ValidationException.class)
    public void testExistsWithGenreWithNullId() {
        genreFacade.exists(SpringToUtils.newGenre(objectGenerator));
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

        return genre;
    }

}
