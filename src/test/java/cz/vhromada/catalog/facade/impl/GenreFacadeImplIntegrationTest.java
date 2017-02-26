package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

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
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@DirtiesContext
public class GenreFacadeImplIntegrationTest {

    /**
     * Event for null genre
     */
    private static final Event NULL_GENRE_EVENT = new Event(Severity.ERROR, "GENRE_NULL", "Genre mustn't be null.");

    /**
     * Event for genre with null ID
     */
    private static final Event NULL_GENRE_ID_EVENT = new Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.");

    /**
     * Event for not existing genre
     */
    private static final Event NOT_EXIST_GENRE_EVENT = new Event(Severity.ERROR, "GENRE_NOT_EXIST", "Genre doesn't exist.");

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
    public void newData() {
        clearReferencedData();

        final Result<Void> result = genreFacade.newData();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(GenreUtils.getGenresCount(entityManager), is(0));
    }

    /**
     * Test method for {@link GenreFacade#getAll()}.
     */
    @Test
    public void getAll() {
        final Result<List<Genre>> result = genreFacade.getAll();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));
        GenreUtils.assertGenreListDeepEquals(result.getData(), GenreUtils.getGenres());

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#get(Integer)}.
     */
    @Test
    public void get() {
        for (int i = 1; i <= GenreUtils.GENRES_COUNT; i++) {
            final Result<Genre> result = genreFacade.get(i);

            assertThat(result, is(notNullValue()));
            assertThat(result.getEvents(), is(notNullValue()));
            assertThat(result.getStatus(), is(Status.OK));
            assertThat(result.getEvents().isEmpty(), is(true));
            GenreUtils.assertGenreDeepEquals(result.getData(), GenreUtils.getGenreDomain(i));
        }

        final Result<Genre> result = genreFacade.get(Integer.MAX_VALUE);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#get(Integer)} with null genre.
     */
    @Test
    public void get_NullGenre() {
        final Result<Genre> result = genreFacade.get(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "ID_NULL", "ID mustn't be null.")));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)}.
     */
    @Test
    @DirtiesContext
    public void add() {
        final Result<Void> result = genreFacade.add(GenreUtils.newGenre(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Genre addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(GenreUtils.newGenreDomain(GenreUtils.GENRES_COUNT + 1), addedGenre);
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT + 1));
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with null genre.
     */
    @Test
    public void add_NullGenre() {
        final Result<Void> result = genreFacade.add(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with genre with not null ID.
     */
    @Test
    public void add_NotNullId() {
        final Result<Void> result = genreFacade.add(GenreUtils.newGenre(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_ID_NOT_NULL", "ID must be null.")));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with genre with null name.
     */
    @Test
    public void add_NullName() {
        final Genre genre = GenreUtils.newGenre(null);
        genre.setName(null);

        final Result<Void> result = genreFacade.add(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with genre with empty string as name.
     */
    @Test
    public void add_EmptyName() {
        final Genre genre = GenreUtils.newGenre(null);
        genre.setName("");

        final Result<Void> result = genreFacade.add(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)}.
     */
    @Test
    @DirtiesContext
    public void update() {
        final Genre genre = GenreUtils.newGenre(1);

        final Result<Void> result = genreFacade.update(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Genre updatedGenre = GenreUtils.getGenre(entityManager, 1);
        GenreUtils.assertGenreDeepEquals(genre, updatedGenre);
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with null genre.
     */
    @Test
    public void update_NullGenre() {
        final Result<Void> result = genreFacade.update(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getData(), is(nullValue()));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with null ID.
     */
    @Test
    public void update_NullId() {
        final Result<Void> result = genreFacade.update(GenreUtils.newGenre(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with null name.
     */
    @Test
    public void update_NullName() {
        final Genre genre = GenreUtils.newGenre(1);
        genre.setName(null);

        final Result<Void> result = genreFacade.update(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with empty string as name.
     */
    @Test
    public void update_EmptyName() {
        final Genre genre = GenreUtils.newGenre(1);
        genre.setName("");

        final Result<Void> result = genreFacade.update(genre);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with bad ID.
     */
    @Test
    public void update_BadId() {
        final Result<Void> result = genreFacade.update(GenreUtils.newGenre(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)}.
     */
    @Test
    @DirtiesContext
    public void remove() {
        clearReferencedData();

        final Result<Void> result = genreFacade.remove(GenreUtils.newGenre(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        assertThat(GenreUtils.getGenre(entityManager, 1), is(nullValue()));
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT - 1));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with null genre.
     */
    @Test
    public void remove_NullGenre() {
        final Result<Void> result = genreFacade.remove(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with genre with null ID.
     */
    @Test
    public void remove_NullId() {
        final Result<Void> result = genreFacade.remove(GenreUtils.newGenre(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#remove(Genre)} with genre with bad ID.
     */
    @Test
    public void remove_BadId() {
        final Result<Void> result = genreFacade.remove(GenreUtils.newGenre(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)}.
     */
    @Test
    @DirtiesContext
    public void duplicate() {
        final cz.vhromada.catalog.domain.Genre genre = GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT);
        genre.setId(GenreUtils.GENRES_COUNT + 1);

        final Result<Void> result = genreFacade.duplicate(GenreUtils.newGenre(GenreUtils.GENRES_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        final cz.vhromada.catalog.domain.Genre duplicatedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(genre, duplicatedGenre);
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT + 1));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with null genre.
     */
    @Test
    public void duplicate_NullGenre() {
        final Result<Void> result = genreFacade.duplicate(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with genre with null ID.
     */
    @Test
    public void duplicate_NullId() {
        final Result<Void> result = genreFacade.duplicate(GenreUtils.newGenre(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#duplicate(Genre)} with genre with bad ID.
     */
    @Test
    public void duplicate_BadId() {
        final Result<Void> result = genreFacade.duplicate(GenreUtils.newGenre(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)}.
     */
    @Test
    @DirtiesContext
    public void moveUp() {
        final cz.vhromada.catalog.domain.Genre genre1 = GenreUtils.getGenreDomain(1);
        genre1.setPosition(1);
        final cz.vhromada.catalog.domain.Genre genre2 = GenreUtils.getGenreDomain(2);
        genre2.setPosition(0);

        final Result<Void> result = genreFacade.moveUp(GenreUtils.newGenre(2));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        GenreUtils.assertGenreDeepEquals(genre1, GenreUtils.getGenre(entityManager, 1));
        GenreUtils.assertGenreDeepEquals(genre2, GenreUtils.getGenre(entityManager, 2));
        for (int i = 3; i <= GenreUtils.GENRES_COUNT; i++) {
            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenre(i), GenreUtils.getGenre(entityManager, i));
        }
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with null genre.
     */
    @Test
    public void moveUp_NullGenre() {
        final Result<Void> result = genreFacade.moveUp(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with genre with null ID.
     */
    @Test
    public void moveUp_NullId() {
        final Result<Void> result = genreFacade.moveUp(GenreUtils.newGenre(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with not movable genre.
     */
    @Test
    public void moveUp_NotMovableGenre() {
        final Result<Void> result = genreFacade.moveUp(GenreUtils.newGenre(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NOT_MOVABLE", "Genre can't be moved up.")));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveUp(Genre)} with genre with bad ID.
     */
    @Test
    public void moveUp_BadId() {
        final Result<Void> result = genreFacade.moveUp(GenreUtils.newGenre(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)}.
     */
    @Test
    @DirtiesContext
    public void moveDown() {
        final cz.vhromada.catalog.domain.Genre genre1 = GenreUtils.getGenreDomain(1);
        genre1.setPosition(1);
        final cz.vhromada.catalog.domain.Genre genre2 = GenreUtils.getGenreDomain(2);
        genre2.setPosition(0);

        final Result<Void> result = genreFacade.moveDown(GenreUtils.newGenre(1));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        GenreUtils.assertGenreDeepEquals(genre1, GenreUtils.getGenre(entityManager, 1));
        GenreUtils.assertGenreDeepEquals(genre2, GenreUtils.getGenre(entityManager, 2));
        for (int i = 3; i <= GenreUtils.GENRES_COUNT; i++) {
            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenre(i), GenreUtils.getGenre(entityManager, i));
        }
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with null genre.
     */
    @Test
    public void moveDown_NullGenre() {
        final Result<Void> result = genreFacade.moveDown(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with genre with null ID.
     */
    @Test
    public void moveDown_NullId() {
        final Result<Void> result = genreFacade.moveDown(GenreUtils.newGenre(null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NULL_GENRE_ID_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with not movable genre.
     */
    @Test
    public void moveDown_NotMovableGenre() {
        final Result<Void> result = genreFacade.moveDown(GenreUtils.newGenre(GenreUtils.GENRES_COUNT));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GENRE_NOT_MOVABLE", "Genre can't be moved down.")));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#moveDown(Genre)} with genre with bad ID.
     */
    @Test
    public void moveDown_BadId() {
        final Result<Void> result = genreFacade.moveDown(GenreUtils.newGenre(Integer.MAX_VALUE));

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(NOT_EXIST_GENRE_EVENT));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for {@link GenreFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void updatePositions() {
        clearReferencedData();

        final Result<Void> result = genreFacade.updatePositions();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getEvents().isEmpty(), is(true));

        for (int i = 1; i <= GenreUtils.GENRES_COUNT; i++) {
            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenre(i), GenreUtils.getGenre(entityManager, i));
        }
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
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
