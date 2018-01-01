package cz.vhromada.catalog.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.utils.GenreUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GenreRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
public class GenreRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link GenreRepository}
     */
    @Autowired
    private GenreRepository genreRepository;

    /**
     * Test method for get genres.
     */
    @Test
    public void getGenres() {
        final List<Genre> genres = genreRepository.findAll(Sort.by("position", "id"));

        GenreUtils.assertGenresDeepEquals(GenreUtils.getGenres(), genres);

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for get genre.
     */
    @Test
    public void getGenre() {
        for (int i = 1; i <= GenreUtils.GENRES_COUNT; i++) {
            final Genre genre = genreRepository.findById(i).orElse(null);

            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenreDomain(i), genre);
        }

        assertThat(genreRepository.findById(Integer.MAX_VALUE).isPresent(), is(false));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for add genre.
     */
    @Test
    public void add() {
        final Genre genre = GenreUtils.newGenreDomain(null);

        genreRepository.saveAndFlush(genre);

        assertThat(genre.getId(), is(notNullValue()));
        assertThat(genre.getId(), is(GenreUtils.GENRES_COUNT + 1));

        final Genre addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        final Genre expectedAddGenre = GenreUtils.newGenreDomain(null);
        expectedAddGenre.setId(GenreUtils.GENRES_COUNT + 1);
        GenreUtils.assertGenreDeepEquals(expectedAddGenre, addedGenre);

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT + 1));
    }

    /**
     * Test method for update genre.
     */
    @Test
    public void update() {
        final Genre genre = GenreUtils.updateGenre(entityManager, 1);

        genreRepository.saveAndFlush(genre);

        final Genre updatedGenre = GenreUtils.getGenre(entityManager, 1);
        final Genre expectedUpdatedGenre = GenreUtils.getGenreDomain(1);
        GenreUtils.updateGenre(expectedUpdatedGenre);
        expectedUpdatedGenre.setPosition(GenreUtils.POSITION);
        GenreUtils.assertGenreDeepEquals(expectedUpdatedGenre, updatedGenre);

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for remove genre.
     */
    @Test
    @DirtiesContext
    public void remove() {
        final Genre genre = GenreUtils.newGenreDomain(null);
        entityManager.persist(genre);
        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT + 1));

        genreRepository.delete(genre);

        assertThat(GenreUtils.getGenre(entityManager, genre.getId()), is(nullValue()));

        assertThat(GenreUtils.getGenresCount(entityManager), is(GenreUtils.GENRES_COUNT));
    }

    /**
     * Test method for remove all genres.
     */
    @Test
    public void removeAll() {
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate();

        genreRepository.deleteAll();

        assertThat(GenreUtils.getGenresCount(entityManager), is(0));
    }

}
