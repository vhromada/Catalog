package cz.vhromada.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.utils.GenreUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link GenreRepository}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
class GenreRepositoryIntegrationTest {

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
    void getGenres() {
        final List<Genre> genres = genreRepository.findAll();

        GenreUtils.assertGenresDeepEquals(GenreUtils.getGenres(), genres);

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
    }

    /**
     * Test method for get genre.
     */
    @Test
    void getGenre() {
        for (int i = 1; i <= GenreUtils.GENRES_COUNT; i++) {
            final Genre genre = genreRepository.findById(i).orElse(null);

            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenreDomain(i), genre);
        }

        assertThat(genreRepository.findById(Integer.MAX_VALUE).isPresent()).isFalse();

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
    }

    /**
     * Test method for add genre.
     */
    @Test
    void add() {
        final Genre genre = GenreUtils.newGenreDomain(null);
        genre.setPosition(GenreUtils.GENRES_COUNT);

        genreRepository.save(genre);

        assertThat(genre.getId()).isEqualTo(GenreUtils.GENRES_COUNT + 1);

        final Genre addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1);
        final Genre expectedAddGenre = GenreUtils.newGenreDomain(null);
        expectedAddGenre.setId(GenreUtils.GENRES_COUNT + 1);
        expectedAddGenre.setPosition(GenreUtils.GENRES_COUNT);
        GenreUtils.assertGenreDeepEquals(expectedAddGenre, addedGenre);

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT + 1);
    }

    /**
     * Test method for update genre.
     */
    @Test
    void update() {
        final Genre genre = GenreUtils.updateGenre(entityManager, 1);

        genreRepository.save(genre);

        final Genre updatedGenre = GenreUtils.getGenre(entityManager, 1);
        final Genre expectedUpdatedGenre = GenreUtils.getGenreDomain(1);
        GenreUtils.updateGenre(expectedUpdatedGenre);
        expectedUpdatedGenre.setPosition(GenreUtils.POSITION);
        GenreUtils.assertGenreDeepEquals(expectedUpdatedGenre, updatedGenre);

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
    }

    /**
     * Test method for remove genre.
     */
    @Test
    @DirtiesContext
    void remove() {
        final Genre genre = GenreUtils.newGenreDomain(null);
        genre.setPosition(GenreUtils.GENRES_COUNT);
        entityManager.persist(genre);
        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT + 1);

        genreRepository.delete(genre);

        assertThat(GenreUtils.getGenre(entityManager, genre.getId())).isNull();

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT);
    }

    /**
     * Test method for remove all genres.
     */
    @Test
    @SuppressWarnings("SqlWithoutWhere")
    void removeAll() {
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate();

        genreRepository.deleteAll();

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(0);
    }

}
