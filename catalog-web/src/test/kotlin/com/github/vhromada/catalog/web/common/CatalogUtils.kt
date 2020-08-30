package com.github.vhromada.catalog.web.common

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for tests.
 *
 * @author Vladimir Hromada
 */
object CatalogUtils {

    /**
     * ID
     */
    const val ID = 1

    /**
     * English Wikipedia
     */
    const val EN_WIKI = "enWiki"

    /**
     * Czech Wikipedia
     */
    const val CZ_WIKI = "czWiki"

    /**
     * Media
     */
    const val MEDIA = 10

    /**
     * Length
     */
    const val LENGTH = 100

    /**
     * Name
     */
    const val NAME = "Name"

    /**
     * Note
     */
    const val NOTE = "Note"

    /**
     * Number
     */
    const val NUMBER = 2

    /**
     * Year
     */
    const val YEAR = 2000

    /**
     * Position
     */
    const val POSITION = 0

    /**
     * Asserts IMDB code deep equals.
     *
     * @param expectedImdb     expected IMDB selection
     * @param expectedImdbCode expected IMDB code
     * @param actualImdbCode   actual IMDB code
     */
    fun assertImdbCodeDeepEquals(expectedImdb: Boolean, expectedImdbCode: String?, actualImdbCode: Int?) {
        assertSoftly {
            it.assertThat(expectedImdb).isNotNull
            it.assertThat(actualImdbCode).isNotNull
        }
        if (expectedImdb) {
            assertThat(actualImdbCode).isEqualTo(expectedImdbCode?.toInt())
        } else {
            assertThat(actualImdbCode).isEqualTo(-1)
        }
    }

    /**
     * Asserts IMDB code deep equals.
     *
     * @param expectedImdbCode expected IMDB code
     * @param actualImdb       actual IMDB selection
     * @param actualImdbCode   actual IMDB code
     */
    fun assertImdbDeepEquals(expectedImdbCode: Int?, actualImdb: Boolean, actualImdbCode: String?) {
        assertSoftly {
            it.assertThat(expectedImdbCode).isNotNull
            it.assertThat(actualImdbCode).isNotNull
        }
        if (expectedImdbCode!! < 1) {
            assertSoftly {
                it.assertThat(actualImdb).isFalse
                it.assertThat(actualImdbCode).isNull()
            }
        } else {
            assertSoftly {
                it.assertThat(actualImdb).isTrue
                it.assertThat(actualImdbCode).isEqualTo(expectedImdbCode.toString())
            }
        }
    }

}
