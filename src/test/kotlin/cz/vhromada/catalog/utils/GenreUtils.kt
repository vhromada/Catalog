package cz.vhromada.catalog.utils

import cz.vhromada.catalog.entity.Genre
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates genre fields.
 *
 * @return updated genre
 */
fun cz.vhromada.catalog.domain.Genre.updated(): cz.vhromada.catalog.domain.Genre {
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
    fun getGenres(): List<cz.vhromada.catalog.domain.Genre> {
        val genres = mutableListOf<cz.vhromada.catalog.domain.Genre>()
        for (i in 0 until GENRES_COUNT) {
            genres.add(getGenreDomain(i + 1))
        }

        return genres
    }

    /**
     * Returns genre.
     *
     * @param id ID
     * @return genre
     */
    fun newGenreDomain(id: Int?): cz.vhromada.catalog.domain.Genre {
        return cz.vhromada.catalog.domain.Genre(id = id, name = "", position = if (id == null) null else id - 1)
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
    fun getGenreDomain(index: Int): cz.vhromada.catalog.domain.Genre {
        return cz.vhromada.catalog.domain.Genre(id = index, name = "Genre $index name", position = index - 1)
    }

    /**
     * Returns genre for index.
     *
     * @param index index
     * @return genre for index
     */
    fun getGenre(index: Int): Genre {
        return Genre(id = index, name = "Genre $index name", position = index - 1)
    }

    /**
     * Returns genre.
     *
     * @param entityManager entity manager
     * @param id            genre ID
     * @return genre
     */
    fun getGenre(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Genre? {
        return entityManager.find(cz.vhromada.catalog.domain.Genre::class.java, id)
    }

    /**
     * Returns genre with updated fields.
     *
     * @param entityManager entity manager
     * @param id            genre ID
     * @return genre with updated fields
     */
    fun updateGenre(entityManager: EntityManager, id: Int): cz.vhromada.catalog.domain.Genre {
        return getGenre(entityManager, id)!!
                .updated()
                .copy(position = POSITION)
    }

    /**
     * Returns count of genres.
     *
     * @param entityManager entity manager
     * @return count of genres
     */
    @Suppress("CheckStyle")
    fun getGenresCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(g.id) FROM Genre g", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected genres
     * @param actual   actual genres
     */
    fun assertGenresDeepEquals(expected: List<cz.vhromada.catalog.domain.Genre?>?, actual: List<cz.vhromada.catalog.domain.Genre?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertGenreDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    fun assertGenreDeepEquals(expected: cz.vhromada.catalog.domain.Genre?, actual: cz.vhromada.catalog.domain.Genre?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(expected!!.id).isEqualTo(actual!!.id)
            it.assertThat(expected.name).isEqualTo(actual.name)
            it.assertThat(expected.position).isEqualTo(actual.position)
        }
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected list of genre
     * @param actual   actual genres
     */
    fun assertGenreListDeepEquals(expected: List<Genre?>?, actual: List<cz.vhromada.catalog.domain.Genre?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertGenreDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    fun assertGenreDeepEquals(expected: Genre?, actual: cz.vhromada.catalog.domain.Genre?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
