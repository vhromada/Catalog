package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.GENRES_COUNT;
import static cz.vhromada.catalog.common.TestConstants.NAME_1;
import static cz.vhromada.catalog.common.TestConstants.NAME_2;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringToUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.impl.GenreFacadeImpl;
import cz.vhromada.catalog.facade.to.GenreTO;
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
 * A class represents test for class {@link GenreFacadeImpl}.
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

	/** Initializes database. */
	@Before
	public void setUp() {
		SpringUtils.remove(transactionManager, entityManager, Movie.class);
		SpringUtils.remove(transactionManager, entityManager, Episode.class);
		SpringUtils.remove(transactionManager, entityManager, Season.class);
		SpringUtils.remove(transactionManager, entityManager, Serie.class);
		SpringUtils.remove(transactionManager, entityManager, Genre.class);
		SpringUtils.updateSequence(transactionManager, entityManager, "genres_sq");
		for (Genre genre : SpringEntitiesUtils.getGenres()) {
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
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
	}

	/** Test method for {@link GenreFacade#getGenre(Integer)}. */
	@Test
	public void testGetGenre() {
		for (int i = 1; i <= GENRES_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getGenre(i), genreFacade.getGenre(i));
		}

		assertNull(genreFacade.getGenre(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
	}

	/** Test method for {@link GenreFacade#getGenre(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetGenreWithNullArgument() {
		genreFacade.getGenre(null);
	}

	/** Test method for {@link GenreFacade#add(GenreTO)}. */
	@Test
	public void testAdd() {
		final GenreTO genre = ToGenerator.createGenre();
		final Genre expectedGenre = EntityGenerator.createGenre(GENRES_COUNT + 1);

		genreFacade.add(genre);

		DeepAsserts.assertNotNull(genre.getId());
		DeepAsserts.assertEquals(GENRES_COUNT + 1, genre.getId());
		final Genre addedGenre = SpringUtils.getGenre(entityManager, GENRES_COUNT + 1);
		DeepAsserts.assertEquals(genre, addedGenre);
		DeepAsserts.assertEquals(expectedGenre, addedGenre);
		DeepAsserts.assertEquals(GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		genreFacade.add((GenreTO) null);
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with genre with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenreWithNotNullId() {
		genreFacade.add(ToGenerator.createGenre(Integer.MAX_VALUE));
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenreWithNullName() {
		final GenreTO genre = ToGenerator.createGenre();
		genre.setName(null);

		genreFacade.add(genre);
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with genre with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithGenreWithEmptyName() {
		final GenreTO genre = ToGenerator.createGenre();
		genre.setName("");

		genreFacade.add(genre);
	}

	/** Test method for {@link GenreFacade#add(List)}. */
	@Test
	public void testAddList() {
		final List<String> genres = CollectionUtils.newList(NAME_1, NAME_2);
		final Genre expectedGenre1 = EntityGenerator.createGenre(GENRES_COUNT + 1);
		expectedGenre1.setName(NAME_1);
		final Genre expectedGenre2 = EntityGenerator.createGenre(GENRES_COUNT + 2);
		expectedGenre2.setName(NAME_2);

		genreFacade.add(genres);

		DeepAsserts.assertEquals(SpringUtils.getGenre(entityManager, GENRES_COUNT + 1), expectedGenre1);
		DeepAsserts.assertEquals(SpringUtils.getGenre(entityManager, GENRES_COUNT + 2), expectedGenre2);
		DeepAsserts.assertEquals(GENRES_COUNT + 2, SpringUtils.getGenresCount(entityManager));
	}

	/** Test method for {@link GenreFacade#add(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddListWithNullArgument() {
		genreFacade.add((List<String>) null);
	}

	/** Test method for {@link GenreFacade#add(List)} with list of genre names with null value. */
	@Test(expected = ValidationException.class)
	public void testAddListWithGenreWithGenreNamesWithNull() {
		genreFacade.add(CollectionUtils.newList(NAME_1, null));
	}

	/** Test method for {@link GenreFacade#update(GenreTO)}. */
	@Test
	public void testUpdate() {
		final GenreTO genre = ToGenerator.createGenre(1);
		final Genre expectedGenre = EntityGenerator.createGenre(1);

		genreFacade.update(genre);

		final Genre updatedGenre = SpringUtils.getGenre(entityManager, 1);
		DeepAsserts.assertEquals(genre, updatedGenre);
		DeepAsserts.assertEquals(expectedGenre, updatedGenre);
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		genreFacade.update(null);
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenreWithNullId() {
		genreFacade.update(ToGenerator.createGenre());
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with genre with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenreWithNullName() {
		final GenreTO genre = ToGenerator.createGenre(PRIMARY_ID);
		genre.setName(null);

		genreFacade.update(genre);
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with genre with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithGenreWithEmptyName() {
		final GenreTO genre = ToGenerator.createGenre(PRIMARY_ID);
		genre.setName(null);

		genreFacade.update(genre);
	}

	/** Test method for {@link GenreFacade#update(GenreTO)} with genre with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithGenreWithBadId() {
		genreFacade.update(ToGenerator.createGenre(Integer.MAX_VALUE));
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)}. */
	@Test
	public void testRemove() {
		final Genre genre = EntityGenerator.createGenre();
		SpringUtils.persist(transactionManager, entityManager, genre);
		DeepAsserts.assertEquals(GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));

		genreFacade.remove(ToGenerator.createGenre(genre.getId()));

		assertNull(SpringUtils.getGenre(entityManager, genre.getId()));
		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		genreFacade.remove(null);
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithGenreWithNullId() {
		genreFacade.remove(ToGenerator.createGenre());
	}

	/** Test method for {@link GenreFacade#remove(GenreTO)} with genre with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithGenreWithBadId() {
		genreFacade.remove(ToGenerator.createGenre(Integer.MAX_VALUE));
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)}. */
	@Test
	public void testDuplicate() {
		final Genre genre = SpringEntitiesUtils.getGenre(GENRES_COUNT);
		genre.setId(GENRES_COUNT + 1);

		genreFacade.duplicate(ToGenerator.createGenre(GENRES_COUNT));

		final Genre duplicatedGenre = SpringUtils.getGenre(entityManager, GENRES_COUNT + 1);
		DeepAsserts.assertEquals(genre, duplicatedGenre);
		DeepAsserts.assertEquals(GENRES_COUNT + 1, SpringUtils.getGenresCount(entityManager));
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		genreFacade.duplicate(null);
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithGenreWithNullId() {
		genreFacade.duplicate(ToGenerator.createGenre());
	}

	/** Test method for {@link GenreFacade#duplicate(GenreTO)} with genre with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithGenreWithBadId() {
		genreFacade.duplicate(ToGenerator.createGenre(Integer.MAX_VALUE));
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with existing genre. */
	@Test
	public void testExists() {
		for (int i = 1; i <= GENRES_COUNT; i++) {
			assertTrue(genreFacade.exists(ToGenerator.createGenre(i)));
		}

		assertFalse(genreFacade.exists(ToGenerator.createGenre(Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(GENRES_COUNT, SpringUtils.getGenresCount(entityManager));
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		genreFacade.exists(null);
	}

	/** Test method for {@link GenreFacade#exists(GenreTO)} with genre with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithGenreWithNullId() {
		genreFacade.exists(ToGenerator.createGenre());
	}

}
