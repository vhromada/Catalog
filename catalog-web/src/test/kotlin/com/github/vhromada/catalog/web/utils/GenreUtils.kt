package com.github.vhromada.catalog.web.utils

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
        return GenreFO(
            id = TestConstants.ID,
            name = TestConstants.NAME,
            position = TestConstants.POSITION
        )
    }

    /**
     * Returns genre.
     *
     * @return genre
     */
    fun getGenre(): Genre {
        return Genre(
            id = TestConstants.ID,
            name = TestConstants.NAME,
            position = TestConstants.POSITION
        )
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected FO for genre
     * @param actual   actual genre
     */
    fun assertGenreDeepEquals(expected: GenreFO, actual: Genre) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual FO for genre
     */
    fun assertGenreDeepEquals(expected: Genre, actual: GenreFO) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected list of genre
     * @param actual   actual list of genres
     */
    fun assertGenresDeepEquals(expected: List<Genre?>?, actual: List<Int?>?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertThat(expected.size).isEqualTo(actual!!.size)
            if (expected.isNotEmpty()) {
                for (i in expected.indices) {
                    assertGenreDeepEquals(expected = expected[i], actual = actual[i])
                }
            }
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    private fun assertGenreDeepEquals(expected: Genre?, actual: Int?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertThat(actual).isEqualTo(expected.id)
        }
    }

}
