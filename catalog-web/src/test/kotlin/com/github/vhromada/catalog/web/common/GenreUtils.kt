package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.web.fo.GenreFO
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for genres.
 *
 * @author Vladimir Hromada
 */
object GenreUtils {

    /**
     * Returns FO for genre.
     *
     * @return FO for genre
     */
    fun getGenreFO(): GenreFO {
        return GenreFO(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                position = CatalogUtils.POSITION)
    }

    /**
     * Returns genre.
     *
     * @return genre
     */
    fun getGenre(): Genre {
        return Genre(id = CatalogUtils.ID,
                name = CatalogUtils.NAME,
                position = CatalogUtils.POSITION)
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected FO for genre
     * @param actual   actual genre
     */
    fun assertGenreDeepEquals(expected: GenreFO?, actual: Genre?) {
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

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected list of genres
     * @param actual   actual list of genre
     */
    fun assertGenresDeepEquals(expected: List<Int?>?, actual: List<Genre?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNull()
        }
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected list of genre
     * @param actual   actual list of genres
     */
    fun assertGenreListDeepEquals(expected: List<Genre?>?, actual: List<Int?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(actual!!.size).isEqualTo(expected!!.size)
        for (i in expected.indices) {
            assertGenreDeepEquals(expected[i], actual[i])
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    private fun assertGenreDeepEquals(expected: Genre?, actual: Int?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly { it.assertThat(actual).isEqualTo(expected!!.id) }
    }

}
