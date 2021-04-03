package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
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
 * A class represents integration test for class [GenreFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class GenreFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [GenreFacade]
     */
    @Autowired
    private lateinit var facade: GenreFacade

    /**
     * Test method for [GenreFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..GenreUtils.GENRES_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            GenreUtils.assertGenreDeepEquals(expected = GenreUtils.getGenreDomain(index = i), actual = result.data!!)
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GENRE_NOT_EXIST_EVENT))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.update].
     */
    @Test
    fun update() {
        val genre = GenreUtils.newGenre(id = 1)
        val expectedGenre = GenreUtils.newGenreDomain(id = 1)
            .fillAudit(audit = AuditUtils.updatedAudit())

        val result = facade.update(data = genre)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        GenreUtils.assertGenreDeepEquals(expected = expectedGenre, actual = GenreUtils.getGenre(entityManager = entityManager, id = 1)!!)

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.update] with genre with null ID.
     */
    @Test
    fun updateNullId() {
        val genre = GenreUtils.newGenre(id = 1)
            .copy(id = null)

        val result = facade.update(data = genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_ID_NULL", message = "ID mustn't be null.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.update] with genre with null position.
     */
    @Test
    fun updateNullPosition() {
        val genre = GenreUtils.newGenre(id = 1)
            .copy(position = null)

        val result = facade.update(data = genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.update] with genre with null name.
     */
    @Test
    fun updateNullName() {
        val genre = GenreUtils.newGenre(id = 1)
            .copy(name = null)

        val result = facade.update(data = genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.update] with genre with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val genre = GenreUtils.newGenre(id = 1)
            .copy(name = "")

        val result = facade.update(data = genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.update] with genre with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = GenreUtils.newGenre(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GENRE_NOT_EXIST_EVENT))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.remove].
     */
    @Test
    fun remove() {
        clearReferencedData()

        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(GenreUtils.getGenre(entityManager = entityManager, id = 1)).isNull()

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT - 1)
    }

    /**
     * Test method for [GenreFacade.remove] with genre with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GENRE_NOT_EXIST_EVENT))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        val expectedGenre = GenreUtils.getGenreDomain(index = GenreUtils.GENRES_COUNT)
            .copy(id = GenreUtils.GENRES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.duplicate(id = GenreUtils.GENRES_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        GenreUtils.assertGenreDeepEquals(expected = expectedGenre, actual = GenreUtils.getGenre(entityManager = entityManager, id = GenreUtils.GENRES_COUNT + 1)!!)

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT + 1)
    }

    /**
     * Test method for [GenreFacade.duplicate] with genre with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GENRE_NOT_EXIST_EVENT))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val genre1 = GenreUtils.getGenreDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val genre2 = GenreUtils.getGenreDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        GenreUtils.assertGenreDeepEquals(expected = genre1, actual = GenreUtils.getGenre(entityManager = entityManager, id = 1)!!)
        GenreUtils.assertGenreDeepEquals(expected = genre2, actual = GenreUtils.getGenre(entityManager = entityManager, id = 2)!!)
        for (i in 3..GenreUtils.GENRES_COUNT) {
            GenreUtils.assertGenreDeepEquals(expected = GenreUtils.getGenreDomain(i), actual = GenreUtils.getGenre(entityManager = entityManager, id = i)!!)
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.moveUp] with not movable genre.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_MOVABLE", message = "Genre can't be moved up.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.moveUp] with genre with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GENRE_NOT_EXIST_EVENT))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val genre1 = GenreUtils.getGenreDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val genre2 = GenreUtils.getGenreDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        GenreUtils.assertGenreDeepEquals(expected = genre1, actual = GenreUtils.getGenre(entityManager = entityManager, id = 1)!!)
        GenreUtils.assertGenreDeepEquals(expected = genre2, actual = GenreUtils.getGenre(entityManager = entityManager, id = 2)!!)
        for (i in 3..GenreUtils.GENRES_COUNT) {
            GenreUtils.assertGenreDeepEquals(expected = GenreUtils.getGenreDomain(i), actual = GenreUtils.getGenre(entityManager = entityManager, id = i)!!)
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.moveDown] with not movable genre.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = GenreUtils.GENRES_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_MOVABLE", message = "Genre can't be moved down.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.moveDown] with genre with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(GENRE_NOT_EXIST_EVENT))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.newData].
     */
    @Test
    fun newData() {
        clearReferencedData()

        val result = facade.newData()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(0)
    }

    /**
     * Test method for [GenreFacade.getAll].
     */
    @Test
    fun getAll() {
        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isNotNull
            it.assertThat(result.events()).isEmpty()
        }
        GenreUtils.assertGenreListDeepEquals(expected = GenreUtils.getGenres(), actual = result.data!!)

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedGenre = GenreUtils.newGenreDomain(id = GenreUtils.GENRES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.add(GenreUtils.newGenre(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        GenreUtils.assertGenreDeepEquals(expected = expectedGenre, actual = GenreUtils.getGenre(entityManager = entityManager, id = GenreUtils.GENRES_COUNT + 1)!!)

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT + 1)
    }

    /**
     * Test method for [GenreFacade.add] with genre with not null ID.
     */
    @Test
    fun addNotNullId() {
        val genre = GenreUtils.newGenre(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(data = genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.add] with genre with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val genre = GenreUtils.newGenre(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = facade.add(data = genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.add] with genre with null name.
     */
    @Test
    fun addNullName() {
        val genre = GenreUtils.newGenre(id = null)
            .copy(name = null)

        val result = facade.add(data = genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.add] with genre with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val genre = GenreUtils.newGenre(id = null)
            .copy(name = "")

        val result = facade.add(data = genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
    }

    /**
     * Test method for [GenreFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        for (i in 1..GenreUtils.GENRES_COUNT) {
            val expectedGenre = GenreUtils.getGenreDomain(index = i)
                .copy(position = i - 1)
                .fillAudit(audit = AuditUtils.updatedAudit())
            GenreUtils.assertGenreDeepEquals(expected = expectedGenre, actual = GenreUtils.getGenre(entityManager = entityManager, id = i)!!)
        }

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

    companion object {

        /**
         * Event for not existing genre
         */
        private val GENRE_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "GENRE_NOT_EXIST", message = "Genre doesn't exist.")

    }

}
