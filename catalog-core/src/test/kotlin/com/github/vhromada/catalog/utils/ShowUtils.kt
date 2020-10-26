package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.domain.Genre
import com.github.vhromada.catalog.entity.Show
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates show fields.
 *
 * @return updated show
 */
fun com.github.vhromada.catalog.domain.Show.updated(): com.github.vhromada.catalog.domain.Show {
    return copy(czechName = "czName",
            originalName = "origName",
            csfd = "Csfd",
            imdbCode = 1000,
            wikiEn = "enWiki",
            wikiCz = "czWiki",
            note = "Note",
            audit = AuditUtils.newAudit())
}

/**
 * Updates show fields.
 *
 * @return updated show
 */
fun Show.updated(): Show {
    return copy(czechName = "czName",
            originalName = "origName",
            csfd = "Csfd",
            imdbCode = 1000,
            wikiEn = "enWiki",
            wikiCz = "czWiki",
            note = "Note")
}

/**
 * A class represents utility class for shows.
 *
 * @author Vladimir Hromada
 */
object ShowUtils {

    /**
     * Count of shows
     */
    const val SHOWS_COUNT = 3

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Show name
     */
    private const val SHOW = "Show "

    /**
     * Returns shows.
     *
     * @return shows
     */
    fun getShows(): List<com.github.vhromada.catalog.domain.Show> {
        val shows = mutableListOf<com.github.vhromada.catalog.domain.Show>()
        for (i in 0 until SHOWS_COUNT) {
            shows.add(getShow(i + 1))
        }

        return shows
    }

    /**
     * Returns show.
     *
     * @param id ID
     * @return show
     */
    fun newShowDomain(id: Int?): com.github.vhromada.catalog.domain.Show {
        return com.github.vhromada.catalog.domain.Show(
                id = id,
                czechName = "",
                originalName = "",
                csfd = null,
                imdbCode = null,
                wikiEn = null,
                wikiCz = null,
                picture = id,
                note = null,
                position = if (id == null) null else id - 1,
                genres = listOf(GenreUtils.newGenreDomain(id)),
                seasons = emptyList(),
                audit = null)
                .updated()
    }

    /**
     * Returns show with seasons.
     *
     * @param id ID
     * @return show with seasons
     */
    fun newShowWithSeasons(id: Int?): com.github.vhromada.catalog.domain.Show {
        return newShowDomain(id)
                .copy(seasons = listOf(SeasonUtils.newSeasonWithEpisodes(id)))
    }

    /**
     * Returns show.
     *
     * @param id ID
     * @return show
     */
    fun newShow(id: Int?): Show {
        return Show(
                id = id,
                czechName = "",
                originalName = "",
                csfd = null,
                imdbCode = null,
                wikiEn = null,
                wikiCz = null,
                picture = id,
                note = null,
                position = if (id == null) null else id - 1,
                genres = listOf(GenreUtils.newGenre(id)))
                .updated()
    }

    /**
     * Returns show for index.
     *
     * @param index index
     * @return show for index
     */
    fun getShow(index: Int): com.github.vhromada.catalog.domain.Show {
        val imdbMultiplier = 100
        val genres = mutableListOf<Genre>()
        genres.add(GenreUtils.getGenreDomain(index))
        if (index == 3) {
            genres.add(GenreUtils.getGenreDomain(4))
        }

        return com.github.vhromada.catalog.domain.Show(
                id = index,
                czechName = "$SHOW$index czech name",
                originalName = "$SHOW$index original name",
                csfd = "$SHOW$index CSFD",
                imdbCode = index * imdbMultiplier,
                wikiEn = "$SHOW$index English Wikipedia",
                wikiCz = "$SHOW$index Czech Wikipedia",
                picture = MovieUtils.MOVIES_COUNT + index,
                note = if (index == 2) SHOW + "2 note" else "",
                position = index - 1,
                genres = genres,
                seasons = SeasonUtils.getSeasons(index),
                audit = AuditUtils.getAudit())
    }

    /**
     * Returns show.
     *
     * @param entityManager entity manager
     * @param id            show ID
     * @return show
     */
    fun getShow(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Show? {
        return entityManager.find(com.github.vhromada.catalog.domain.Show::class.java, id)
    }

    /**
     * Returns show with updated fields.
     *
     * @param id            show ID
     * @param entityManager entity manager
     * @return show with updated fields
     */
    fun updateShow(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Show {
        return getShow(entityManager, id)!!
                .updated()
                .copy(position = POSITION, genres = mutableListOf(GenreUtils.getGenreDomain(1)))
    }

    /**
     * Returns count of shows.
     *
     * @param entityManager entity manager
     * @return count of shows
     */
    fun getShowsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(s.id) FROM Show s", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts shows deep equals.
     *
     * @param expected expected list of shows
     * @param actual   actual list of shows
     */
    fun assertShowsDeepEquals(expected: List<com.github.vhromada.catalog.domain.Show?>?, actual: List<com.github.vhromada.catalog.domain.Show?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertShowDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts show deep equals.
     *
     * @param expected expected show
     * @param actual   actual show
     */
    fun assertShowDeepEquals(expected: com.github.vhromada.catalog.domain.Show?, actual: com.github.vhromada.catalog.domain.Show?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            it.assertThat(actual.imdbCode).isEqualTo(expected.imdbCode)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
        GenreUtils.assertGenresDeepEquals(expected!!.genres, actual!!.genres)
        SeasonUtils.assertSeasonsDeepEquals(expected.seasons, actual.seasons)
        AuditUtils.assertAuditDeepEquals(expected.audit, actual.audit)
    }

    /**
     * Asserts shows deep equals.
     *
     * @param expected expected list of shows
     * @param actual   actual list of shows
     */
    fun assertShowListDeepEquals(expected: List<Show?>?, actual: List<com.github.vhromada.catalog.domain.Show?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertShowDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts show deep equals.
     *
     * @param expected expected show
     * @param actual   actual show
     */
    fun assertShowDeepEquals(expected: Show?, actual: com.github.vhromada.catalog.domain.Show?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.czechName).isEqualTo(expected.czechName)
            it.assertThat(actual.originalName).isEqualTo(expected.originalName)
            it.assertThat(actual.csfd).isEqualTo(expected.csfd)
            it.assertThat(actual.imdbCode).isEqualTo(expected.imdbCode)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.picture).isEqualTo(expected.picture)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
        GenreUtils.assertGenreListDeepEquals(expected!!.genres, actual!!.genres)
    }

}
