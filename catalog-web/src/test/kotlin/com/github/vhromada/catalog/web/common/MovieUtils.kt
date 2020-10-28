package com.github.vhromada.catalog.web.common

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
        return MovieFO(id = CatalogUtils.ID,
                czechName = "czName",
                originalName = "origName",
                year = CatalogUtils.YEAR.toString(),
                language = Language.EN,
                subtitles = listOf(Language.CZ),
                csfd = "Csfd",
                imdb = true,
                imdbCode = "1000",
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                picture = CatalogUtils.ID,
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION,
                media = listOf(TimeUtils.getTimeFO()),
                genres = listOf(CatalogUtils.ID))
    }

    /**
     * Returns movie.
     *
     * @return movie
     */
    fun getMovie(): Movie {
        val medium = Medium(id = CatalogUtils.ID,
                number = CatalogUtils.NUMBER,
                length = CatalogUtils.LENGTH)

        return Movie(id = CatalogUtils.ID,
                czechName = "czName",
                originalName = "origName",
                year = CatalogUtils.YEAR,
                language = Language.EN,
                subtitles = listOf(Language.CZ),
                csfd = "Csfd",
                imdbCode = 1000,
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                picture = CatalogUtils.ID,
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION,
                media = listOf(medium),
                genres = listOf(GenreUtils.getGenre()))
    }

    /**
     * Asserts movie deep equals.
     *
     * @param expected expected FO for movie
     * @param actual   actual movie
     */
    fun assertMovieDeepEquals(expected: MovieFO?, actual: Movie?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.year).isEqualTo(expected.year!!.toInt())
            it.assertThat<Language>(actual.language).isEqualTo(expected.language)
            it.assertThat<Language>(actual.subtitles).isEqualTo(expected.subtitles)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
        CatalogUtils.assertImdbCodeDeepEquals(expected!!.imdb, expected.imdbCode, actual!!.imdbCode)
        assertMediaDeepEquals(expected.media, actual.media)
        GenreUtils.assertGenresDeepEquals(expected.genres, actual.genres)
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of FO for time
     * @param actual   actual list of medium
     */
    private fun assertMediaDeepEquals(expected: List<TimeFO?>?, actual: List<Medium?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(actual!!.size).isEqualTo(expected!!.size)
        for (i in expected.indices) {
            assertMediumDeepEquals(expected[i], actual[i], i)
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
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isNull()
            it.assertThat(actual.number).isEqualTo(index + 1)
        }
        TimeUtils.assertTimeDeepEquals(expected, actual!!.length)
    }

    /**
     * Asserts movie deep equals.
     *
     * @param expected expected movie
     * @param actual   actual FO for movie
     */
    fun assertMovieDeepEquals(expected: Movie?, actual: MovieFO?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.year).isEqualTo(expected.year.toString())
            it.assertThat<Language>(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles).isEqualTo(expected.subtitles)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
        CatalogUtils.assertImdbDeepEquals(expected!!.imdbCode, actual!!.imdb, actual.imdbCode)
        assertMediumListDeepEquals(expected.media, actual.media)
        GenreUtils.assertGenreListDeepEquals(expected.genres, actual.genres)
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of medium
     * @param actual   actual list of FO for time
     */
    private fun assertMediumListDeepEquals(expected: List<Medium?>?, actual: List<TimeFO?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(actual!!.size).isEqualTo(expected!!.size)
        for (i in expected.indices) {
            val actualTime = actual[i]
            assertThat(actualTime).isNotNull
            TimeUtils.assertTimeDeepEquals(actualTime, expected[i]!!.length)
        }
    }

}
