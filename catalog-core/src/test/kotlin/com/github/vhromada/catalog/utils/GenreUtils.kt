package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Genre
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates genre fields.
 *
 * @return updated genre
 */
fun com.github.vhromada.catalog.domain.Genre.updated(): com.github.vhromada.catalog.domain.Genre {
    return copy(name = "Name")
}

/**
 * Updates genre fields.
 *
 * @return updated genre
 */
fun Genre.updated(): Genre {
    return copy(name = "Name")
}

/**
 * A class represents utility class for genres.
 *
 * @author Vladimir Hromada
 */
object GenreUtils {

    /**
     * Count of genres
     */
    const val GENRES_COUNT = 4

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Returns genres.
     *
     * @return genres
     */
    fun getGenres(): List<com.github.vhromada.catalog.domain.Genre> {
        val genres = mutableListOf<com.github.vhromada.catalog.domain.Genre>()
        for (i in 1..GENRES_COUNT) {
            genres.add(getGenreDomain(index = i))
        }

        return genres
    }

    /**
     * Returns genre.
     *
     * @param id ID
     * @return genre
     */
    fun newGenreDomain(id: Int?): com.github.vhromada.catalog.domain.Genre {
        return com.github.vhromada.catalog.domain.Genre(id = id, name = "", position = if (id == null) null else id - 1)
            .updated()
    }

    /**
     * Returns genre.
     *
     * @param id ID
     * @return genre
     */
    fun newGenre(id: Int?): Genre {
        return Genre(id = id, name = "", position = if (id == null) null else id - 1)
            .updated()
    }

    /**
     * Returns genre for index.
     *
     * @param index index
     * @return genre for index
     */
    fun getGenreDomain(index: Int): com.github.vhromada.catalog.domain.Genre {
        return com.github.vhromada.catalog.domain.Genre(id = index, name = "Genre $index name", position = index + 9)
            .fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns genre for index.
     *
     * @param index index
     * @return genre for index
     */
    fun getGenre(index: Int): Genre {
        return Genre(id = index, name = "Genre $index name", position = index + 9)
    }

    /**
     * Returns genre.
     *
     * @param entityManager entity manager
     * @param id            genre ID
     * @return genre
     */
    fun getGenre(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Genre? {
        return entityManager.find(com.github.vhromada.catalog.domain.Genre::class.java, id)
    }

    /**
     * Returns genre with updated fields.
     *
     * @param entityManager entity manager
     * @param id            genre ID
     * @return genre with updated fields
     */
    fun updateGenre(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Genre {
        val genre = getGenre(entityManager = entityManager, id = id)!!
        return genre
            .updated()
            .copy(position = POSITION)
            .fillAudit(audit = genre)
    }

    /**
     * Returns count of genres.
     *
     * @param entityManager entity manager
     * @return count of genres
     */
    @Suppress("JpaQlInspection")
    fun getGenresCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(g.id) FROM Genre g", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected list of genres
     * @param actual   actual list of genres
     */
    fun assertDomainGenresDeepEquals(expected: List<com.github.vhromada.catalog.domain.Genre>, actual: List<com.github.vhromada.catalog.domain.Genre>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertGenreDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    fun assertGenreDeepEquals(expected: com.github.vhromada.catalog.domain.Genre, actual: com.github.vhromada.catalog.domain.Genre) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.position).isEqualTo(expected.position)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected list of genres
     * @param actual   actual list of genres
     */
    fun assertGenresDeepEquals(expected: List<Genre>, actual: List<com.github.vhromada.catalog.domain.Genre>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertGenreDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    fun assertGenreDeepEquals(expected: Genre, actual: com.github.vhromada.catalog.domain.Genre) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.position).isEqualTo(expected.position)
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
        }
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected list of genres
     * @param actual   actual list of genres
     */
    fun assertGenreListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Genre>, actual: List<Genre>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertGenreDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    fun assertGenreDeepEquals(expected: com.github.vhromada.catalog.domain.Genre, actual: Genre) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
