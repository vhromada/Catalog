package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * A class represents integration test for class {@link GenreFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class GenreFacadeImplIntegrationTest extends MovableParentFacadeIntegrationTest<Genre, cz.vhromada.catalog.domain.Genre> {

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
    private GenreFacade facade;

    /**
     * Test method for {@link GenreFacade#add(Genre)} with genre with null name.
     */
    @Test
    void add_NullName() {
        final Genre genre = newData(null);
        genre.setName(null);

        final Result<Void> result = facade.add(genre);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GenreFacade#add(Genre)} with genre with empty string as name.
     */
    @Test
    void add_EmptyName() {
        final Genre genre = newData(null);
        genre.setName("");

        final Result<Void> result = facade.add(genre);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with null name.
     */
    @Test
    void update_NullName() {
        final Genre genre = newData(1);
        genre.setName(null);

        final Result<Void> result = facade.update(genre);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link GenreFacade#update(Genre)} with genre with empty string as name.
     */
    @Test
    void update_EmptyName() {
        final Genre genre = newData(1);
        genre.setName("");

        final Result<Void> result = facade.update(genre);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        assertDefaultRepositoryData();
    }

    @Override
    protected MovableParentFacade<Genre> getFacade() {
        return facade;
    }

    @Override
    protected Integer getDefaultDataCount() {
        return GenreUtils.GENRES_COUNT;
    }

    @Override
    protected Integer getRepositoryDataCount() {
        return GenreUtils.getGenresCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Genre> getDataList() {
        return GenreUtils.getGenres();
    }

    @Override
    protected cz.vhromada.catalog.domain.Genre getDomainData(final Integer index) {
        return GenreUtils.getGenreDomain(index);
    }

    @Override
    protected Genre newData(final Integer id) {
        return GenreUtils.newGenre(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Genre newDomainData(final Integer id) {
        return GenreUtils.newGenreDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Genre getRepositoryData(final Integer id) {
        return GenreUtils.getGenre(entityManager, id);
    }

    @Override
    protected String getName() {
        return "Genre";
    }

    @Override
    @SuppressWarnings("SqlWithoutWhere")
    protected void clearReferencedData() {
        final TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate();
        transactionManager.commit(transactionStatus);
    }

    @Override
    protected void assertDataListDeepEquals(final List<Genre> expected, final List<cz.vhromada.catalog.domain.Genre> actual) {
        GenreUtils.assertGenreListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Genre expected, final cz.vhromada.catalog.domain.Genre actual) {
        GenreUtils.assertGenreDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Genre expected, final cz.vhromada.catalog.domain.Genre actual) {
        GenreUtils.assertGenreDeepEquals(expected, actual);
    }

    @Override
    protected void assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertNewRepositoryData() {
        super.assertNewRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertAddRepositoryData() {
        super.assertAddRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData();

        assertReferences();
    }

    /**
     * Asserts references.
     */
    private void assertReferences() {
        assertSoftly(softly -> {
            softly.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT);
            softly.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT);
        });
    }

}
