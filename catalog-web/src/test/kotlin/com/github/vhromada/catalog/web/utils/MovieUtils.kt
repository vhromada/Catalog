package com.github.vhromada.catalog.web.utils

import com.github.vhromada.catalog.entity.Medium
import com.github.vhromada.catalog.entity.Movie
import com.github.vhromada.catalog.web.fo.MovieFO
import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.common.entity.Language
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for movies.
 *
 * @author Vladimir Hromada
 */
object MovieUtils {

    /**
     * Returns FO for movie.
     *
     * @return FO for movie
     */
    fun getMovieFO(): MovieFO {
        return MovieFO(
            id = TestConstants.ID,
            czechName = "czName",
            originalName = "origName",
            year = TestConstants.YEAR.toString(),
            language = Language.EN,
            subtitles = listOf(Language.CZ),
            csfd = "Csfd",
            imdb = true,
            imdbCode = "1000",
            wikiEn = TestConstants.EN_WIKI,
            wikiCz = TestConstants.CZ_WIKI,
            picture = TestConstants.ID,
            note = TestConstants.NOTE,
            position = TestConstants.POSITION,
            media = listOf(TimeUtils.getTimeFO()),
            genres = listOf(TestConstants.ID)
        )
    }

    /**
     * Returns movie.
     *
     * @return movie
     */
    fun getMovie(): Movie {
        val medium = Medium(
            id = TestConstants.ID,
            number = TestConstants.NUMBER,
            length = TestConstants.LENGTH
        )

        return Movie(
            id = TestConstants.ID,
            czechName = "czName",
            originalName = "origName",
            year = TestConstants.YEAR,
            language = Language.EN,
            subtitles = listOf(Language.CZ),
            csfd = "Csfd",
            imdbCode = 1000,
            wikiEn = TestConstants.EN_WIKI,
            wikiCz = TestConstants.CZ_WIKI,
            picture = TestConstants.ID,
            note = TestConstants.NOTE,
            position = TestConstants.POSITION,
            media = listOf(medium),
            genres = listOf(GenreUtils.getGenre())
        )
    }

    /**
     * Asserts movie deep equals.
     *
     * @param expected expected FO for movie
     * @param actual   actual movie
     */
    fun assertMovieDeepEquals(expected: MovieFO, actual: Movie) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.year).isEqualTo(expected.year!!.toInt())
            it.assertThat(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles).isEqualTo(expected.subtitles)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            ImdbUtils.assertImdbCodeDeepEquals(softly = it, expectedImdb = expected.imdb, expectedImdbCode = expected.imdbCode, actualImdbCode = actual.imdbCode)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            it.assertThat(actual.genres).isNull()
        }
        assertMediaDeepEquals(expected = expected.media, actual = actual.media)
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of FO for time
     * @param actual   actual list of medium
     */
    private fun assertMediaDeepEquals(expected: List<TimeFO?>?, actual: List<Medium?>?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertThat(actual!!.size).isEqualTo(expected.size)
            for (i in expected.indices) {
                assertMediumDeepEquals(expected = expected[i], actual = actual[i], index = i)
            }
        }
    }

    /**
     * Asserts medium deep equals.
     *
     * @param expected expected FO for time
     * @param actual   actual medium
     * @param index    index
     */
    private fun assertMediumDeepEquals(expected: TimeFO?, actual: Medium?, index: Int) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertSoftly {
                it.assertThat(actual!!.id).isNull()
                it.assertThat(actual.number).isEqualTo(index + 1)
                TimeUtils.assertTimeDeepEquals(softly = it, expected = expected, actual = actual.length)
            }
        }
    }

    /**
     * Asserts movie deep equals.
     *
     * @param expected expected movie
     * @param actual   actual FO for movie
     */
    fun assertMovieDeepEquals(expected: Movie, actual: MovieFO) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.year).isEqualTo(expected.year.toString())
            it.assertThat(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles).isEqualTo(expected.subtitles)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            ImdbUtils.assertImdbDeepEquals(softly = it, expectedImdbCode = expected.imdbCode!!, actualImdb = actual.imdb, actualImdbCode = actual.imdbCode)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
        assertMediumListDeepEquals(expected = expected.media, actual = actual.media)
        GenreUtils.assertGenresDeepEquals(expected = expected.genres, actual = actual.genres)
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of medium
     * @param actual   actual list of FO for time
     */
    private fun assertMediumListDeepEquals(expected: List<Medium?>?, actual: List<TimeFO?>?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertThat(actual!!.size).isEqualTo(expected.size)
            for (i in expected.indices) {
                assertSoftly {
                    TimeUtils.assertTimeDeepEquals(softly = it, expected = expected[i]!!.length, actual = actual[i])
                }
            }
        }
    }

}
