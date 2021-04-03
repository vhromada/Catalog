package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

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
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [GenreRepository]
     */
    @Autowired
    private lateinit var repository: GenreRepository

    /**
     * Test method for get genres.
     */
    @Test
    fun getGenres() {
        val genres = repository.findAll()

        GenreUtils.assertDomainGenresDeepEquals(expected = GenreUtils.getGenres(), actual = genres)

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for get genre.
     */
    @Test
    fun getGenre() {
        for (i in 1..GenreUtils.GENRES_COUNT) {
            val genre = repository.findById(i).orElse(null)

            GenreUtils.assertGenreDeepEquals(expected = GenreUtils.getGenreDomain(index = i), actual = genre)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for add genre.
     */
    @Test
    @DirtiesContext
    fun add() {
        val genre = GenreUtils.newGenreDomain(id = null)
            .copy(position = GenreUtils.GENRES_COUNT)
        val expectedGenre = GenreUtils.newGenreDomain(id = GenreUtils.GENRES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        repository.save(genre)

        assertSoftly {
            it.assertThat(genre.id).isEqualTo(GenreUtils.GENRES_COUNT + 1)
            it.assertThat(genre.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(genre.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(genre.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(genre.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedGenre = GenreUtils.getGenre(entityManager, GenreUtils.GENRES_COUNT + 1)!!
        assertThat(addedGenre).isNotNull
        GenreUtils.assertGenreDeepEquals(expected = expectedGenre, actual = addedGenre)

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT + 1)
    }

    /**
     * Test method for update genre.
     */
    @Test
    fun update() {
        val genre = GenreUtils.updateGenre(entityManager = entityManager, id = 1)
        val expectedGenre = GenreUtils.getGenreDomain(index = 1)
            .updated()
            .copy(position = GenreUtils.POSITION)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(genre)

        val updatedGenre = GenreUtils.getGenre(entityManager = entityManager, id = 1)
        assertThat(updatedGenre).isNotNull
        GenreUtils.assertGenreDeepEquals(expected = expectedGenre, actual = updatedGenre!!)

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for remove genre.
     */
    @Test
    fun remove() {
        clearReferencedData()

        repository.delete(GenreUtils.getGenre(entityManager = entityManager, id = 1)!!)

        assertThat(GenreUtils.getGenre(entityManager = entityManager, id = 1)).isNull()

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT - 1)
    }

    /**
     * Test method for remove all genres.
     */
    @Test
    fun removeAll() {
        clearReferencedData()

        repository.deleteAll()

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(0)
    }

    /**
     * Test method for get genres for user.
     */
    @Test
    fun findByCreatedUser() {
        val genres = repository.findByCreatedUser(user = AuditUtils.getAudit().createdUser!!)

        GenreUtils.assertDomainGenresDeepEquals(expected = GenreUtils.getGenres(), actual = genres)

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for get genre by id for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..GenreUtils.GENRES_COUNT) {
            val author = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            GenreUtils.assertGenreDeepEquals(expected = GenreUtils.getGenreDomain(index = i), actual = author)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Clears referenced data.
     */
    @Suppress("SqlDialectInspection", "SqlNoDataSourceInspection")
    private fun clearReferencedData() {
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate()
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate()
        entityManager.flush()
    }

}
