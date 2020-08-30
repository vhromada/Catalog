package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.web.fo.ShowFO
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for shows.
 *
 * @author Vladimir Hromada
 */
object ShowUtils {

    /**
     * Returns FO for show.
     *
     * @return FO for show
     */
    fun getShowFO(): ShowFO {
        return ShowFO(id = CatalogUtils.ID,
                czechName = "czName",
                originalName = "origName",
                csfd = "Csfd",
                imdb = true,
                imdbCode = "1000",
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                picture = CatalogUtils.ID,
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION,
                genres = listOf(CatalogUtils.ID))
    }

    /**
     * Returns show.
     *
     * @return show
     */
    fun getShow(): Show {
        return Show(id = CatalogUtils.ID,
                czechName = "czName",
                originalName = "origName",
                csfd = "Csfd",
                imdbCode = 1000,
                wikiEn = CatalogUtils.EN_WIKI,
                wikiCz = CatalogUtils.CZ_WIKI,
                picture = CatalogUtils.ID,
                note = CatalogUtils.NOTE,
                position = CatalogUtils.POSITION,
                genres = listOf(GenreUtils.getGenre()))
    }

    /**
     * Asserts show deep equals.
     *
     * @param expected expected FO for show
     * @param actual   actual show
     */
    fun assertShowDeepEquals(expected: ShowFO?, actual: Show?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            CatalogUtils.assertImdbCodeDeepEquals(expected.imdb, expected.imdbCode, actual.imdbCode)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            GenreUtils.assertGenresDeepEquals(expected.genres, actual.genres)
        }
    }

    /**
     * Asserts show deep equals.
     *
     * @param expected expected show
     * @param actual   actual FO for show
     */
    fun assertShowDeepEquals(expected: Show?, actual: ShowFO?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            CatalogUtils.assertImdbDeepEquals(expected.imdbCode, actual.imdb, actual.imdbCode)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            GenreUtils.assertGenreListDeepEquals(expected.genres, actual.genres)
        }
    }

}
