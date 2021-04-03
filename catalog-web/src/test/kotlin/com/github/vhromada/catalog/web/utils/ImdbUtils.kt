package com.github.vhromada.catalog.web.utils

import org.assertj.core.api.SoftAssertions

/**
 * A class represents utility class for IMDB.
 *
 * @author Vladimir Hromada
 */
object ImdbUtils {

    /**
     * Asserts IMDB code deep equals.
     *
     * @param softly           soft assertions
     * @param expectedImdb     expected IMDB selection
     * @param expectedImdbCode expected IMDB code
     * @param actualImdbCode   actual IMDB code
     */
    fun assertImdbCodeDeepEquals(softly: SoftAssertions, expectedImdb: Boolean, expectedImdbCode: String?, actualImdbCode: Int?) {
        softly.assertThat(expectedImdb).isNotNull
        softly.assertThat(actualImdbCode).isNotNull
        if (expectedImdb) {
            softly.assertThat(actualImdbCode).isEqualTo(expectedImdbCode?.toInt())
        } else {
            softly.assertThat(actualImdbCode).isEqualTo(-1)
        }
    }

    /**
     * Asserts IMDB code deep equals.
     *
     * @param softly           soft assertions
     * @param expectedImdbCode expected IMDB code
     * @param actualImdb       actual IMDB selection
     * @param actualImdbCode   actual IMDB code
     */
    fun assertImdbDeepEquals(softly: SoftAssertions, expectedImdbCode: Int, actualImdb: Boolean, actualImdbCode: String?) {
        softly.assertThat(actualImdbCode).isNotNull
        if (expectedImdbCode < 1) {
            softly.assertThat(actualImdb).isFalse
            softly.assertThat(actualImdbCode).isNull()
        } else {
            softly.assertThat(actualImdb).isTrue
            softly.assertThat(actualImdbCode).isEqualTo(expectedImdbCode.toString())
        }
    }

}
