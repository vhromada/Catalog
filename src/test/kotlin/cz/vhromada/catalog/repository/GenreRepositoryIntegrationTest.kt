package cz.vhromada.catalog.repository

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.utils.AuditUtils
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [GenreRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class GenreRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [GenreRepository]
     */
    @Autowired
    private lateinit var genreRepository: GenreRepository

    /**
     * Test method for get genres.
     */
    @Test
    fun getGenres() {
        val genres = genreRepository.findAll()

        GenreUtils.assertGenresDeepEquals(GenreUtils.getGenres(), genres)

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for get genre.
     */
    @Test
    @Suppress("UsePropertyAccessSyntax")
    fun getGenre() {
        for (i in 1..GenreUtils.GENRES_COUNT) {
            val genre = genreRepository.findById(i).orElse(null)

            GenreUtils.assertGenreDeepEquals(GenreUtils.getGenreDomain(i), genre)
        }

        assertThat(genreRepository.findById(Integer.MAX_VALUE).isPresent).isFalse()

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for add genre.
     */
    @Test
    fun add() {
        val audit = AuditUtils.getAudit()
        val genre = GenreUtils.newGenreDomain(null)
                .copy(position = GenreUtils.GENRES_COUNT, audit = audit)

        genreRepository.save(genre)

        assertThat(genre.id).isEqualTo(GenreUtils.GENRES_COUNT + 1)

        val addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1)!!
        val expectedAddGenre = GenreUtils.newGenreDomain(null)
                .copy(id = GenreUtils.GENRES_COUNT + 1, position = GenreUtils.GENRES_COUNT, audit = audit)
        GenreUtils.assertGenreDeepEquals(expectedAddGenre, addedGenre)

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT + 1)
    }

    /**
     * Test method for update genre.
     */
    @Test
    fun update() {
        val genre = GenreUtils.updateGenre(entityManager, 1)

        genreRepository.save(genre)

        val updatedGenre = GenreUtils.getGenre(entityManager, 1)!!
        val expectedUpdatedGenre = GenreUtils.getGenreDomain(1)
                .updated()
                .copy(position = GenreUtils.POSITION)
        GenreUtils.assertGenreDeepEquals(expectedUpdatedGenre, updatedGenre)

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for remove genre.
     */
    @Test
    @DirtiesContext
    fun remove() {
        val genre = GenreUtils.newGenreDomain(null)
                .copy(position = GenreUtils.GENRES_COUNT, audit = AuditUtils.getAudit())
        entityManager.persist(genre)
        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT + 1)

        genreRepository.delete(genre)

        assertThat(GenreUtils.getGenre(entityManager, genre.id!!)).isNull()

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for remove all genres.
     */
    @Test
    fun removeAll() {
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate()
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate()

        genreRepository.deleteAll()

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(0)
    }

    /**
     * Test method for get genres for user.
     */
    @Test
    fun findByAuditCreatedUser() {
        val genres = genreRepository.findByAuditCreatedUser(AuditUtils.getAudit().createdUser)

        GenreUtils.assertGenresDeepEquals(GenreUtils.getGenres(), genres)

        assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

}
